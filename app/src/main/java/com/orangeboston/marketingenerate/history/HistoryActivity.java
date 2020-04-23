package com.orangeboston.marketingenerate.history;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.VibrateUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.orangeboston.marketingenerate.R;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.orangeboston.marketingenerate.MainActivity.copyTextToClipboard;
import static com.orangeboston.marketingenerate.MainActivity.mapTransitionList;

public class HistoryActivity extends AppCompatActivity {

    private QMUITopBarLayout mTopBar;
    private TextToSpeech textToSpeech;
    private RecyclerView mRecyclerView;
    private HistoryAdapter adapter;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initTopBar();

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.SUCCESS) {

                }
            }
        });

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (!StringUtils.isEmpty(intent.getStringExtra("type"))) {
            type = intent.getStringExtra("type");
        } else {
            type = "d";
        }

        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 侧滑监听
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                VibrateUtils.vibrate(99999);
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                VibrateUtils.cancel();
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Set<String> set = SPUtils.getInstance(type).getAll().keySet();
                Iterator<String> it = set.iterator();
                List<String> keys = new ArrayList<>();
                while (it.hasNext()) {
                    keys.add(it.next());
                }
                SPUtils.getInstance(type).remove(keys.get(pos));
                VibrateUtils.cancel();
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(HistoryActivity.this, R.color.qmui_config_color_50_blue));
            }
        };

        List<String> stringList = mapTransitionList(SPUtils.getInstance(type).getAll());
        adapter = new HistoryAdapter(stringList);
        adapter.setAnimationEnable(true);
        adapter.getDraggableModule().setSwipeEnabled(true);
        adapter.getDraggableModule().setOnItemSwipeListener(onItemSwipeListener);
        adapter.getDraggableModule().getItemTouchHelperCallback().setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> a, @NonNull View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("type", type);
                intent.putExtra("content", adapter.getItem(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter a, @NonNull View v, int position) {
                quickAction(v, adapter.getItem(position));
                return true;
            }
        });

    }

    private void quickAction(View v, String content) {
        QMUIPopups.quickAction(this,
                QMUIDisplayHelper.dp2px(this, 56),
                QMUIDisplayHelper.dp2px(this, 56))
                .shadow(true)
                .skinManager(QMUISkinManager.defaultInstance(this))
                .edgeProtection(QMUIDisplayHelper.dp2px(this, 5))
//                .addAction(new QMUIQuickAction.Action().icon(R.drawable.ic_clear2).text("删除").onClick(
//                        new QMUIQuickAction.OnClickListener() {
//                            @Override
//                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
//                                quickAction.dismiss();
//                                Set<String> set = SPUtils.getInstance(type).getAll().keySet();
//                                Iterator<String> it = set.iterator();
//                                List<String> keys = new ArrayList<>();
//                                while (it.hasNext()) {
//                                    keys.add(it.next());
//                                }
//                                SPUtils.getInstance(type).remove(keys.get(position));
//                                adapter.notifyItemChanged();
//                            }
//                        }
//                ))
                .addAction(new QMUIQuickAction.Action().icon(R.drawable.ic_voice).text("语音").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                quickAction.dismiss();
                                speek(content);
                            }
                        }
                ))
                .addAction(new QMUIQuickAction.Action().icon(R.drawable.ic_copy).text("复制").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                quickAction.dismiss();
                                copyTextToClipboard(HistoryActivity.this, content);
                                ToastUtils.showLong("复制成功<(=￣ˇ￣=)>");
                            }
                        }
                ))
                .show(v);
    }

    private void initTopBar() {
        mTopBar = findViewById(R.id.topbar);
        mTopBar.setTitle("历史记录");
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                stopSpeech();
            }
        });
    }

    private void speek(String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showLong("没有需要播放的内容(=╯_╰=)");
            return;
        }

        //判断是否支持下面两种语言
        int result1 = textToSpeech.setLanguage(Locale.US);
        int result2 = textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
        boolean a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED);
        boolean b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED);
        if (a || b) {
            ToastUtils.showLong("没有语音引擎或语音引擎不支持");
            return;
        }

        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        textToSpeech.setPitch(1.0f);
        // 设置语速
        textToSpeech.setSpeechRate(1.0f);
        // queueMode用于指定发音队列模式，两种模式选择
        //（1）TextToSpeech.QUEUE_FLUSH：该模式下在有新任务时候会清除当前语音任务，执行新的语音任务
        //（2）TextToSpeech.QUEUE_ADD：该模式下会把新的语音任务放到语音任务之后，等前面的语音任务执行完了才会执行新的语音任务
        textToSpeech.speak(content, TextToSpeech.QUEUE_ADD, null);
    }

    private void stopSpeech() {
        if (textToSpeech.isSpeaking()) {
            return;
        }
        textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
        textToSpeech.shutdown(); // 关闭，释放资源
    }

    @Override
    public void finish() {
        super.finish();
        stopSpeech();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSpeech();
    }
}
