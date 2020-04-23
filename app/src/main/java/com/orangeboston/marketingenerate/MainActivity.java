package com.orangeboston.marketingenerate;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.VibrateUtils;
import com.orangeboston.marketingenerate.history.HistoryActivity;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoxing.view.sharpview.SharpEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_history_a)
    ImageView ivHistoryA;
    @BindView(R.id.iv_history_b)
    ImageView ivHistoryB;
    @BindView(R.id.iv_history_c)
    ImageView ivHistoryC;
    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.et_a)
    SharpEditText etA;
    @BindView(R.id.et_b)
    SharpEditText etB;
    @BindView(R.id.et_c)
    SharpEditText etC;
    @BindView(R.id.et_d)
    SharpEditText etD;
    @BindView(R.id.btn_generate)
    QMUIRoundButton btnGenerate;
    @BindView(R.id.iv_edit_d)
    ImageView ivEditD;
    @BindView(R.id.iv_copy_d)
    ImageView ivCopyD;
    @BindView(R.id.iv_history_d)
    ImageView ivHistoryD;
    @BindView(R.id.btn_clear)
    QMUIRoundButton btnClear;
    @BindView(R.id.iv_voice_a)
    ImageView ivVoiceA;
    @BindView(R.id.iv_voice_b)
    ImageView ivVoiceB;
    @BindView(R.id.iv_voice_c)
    ImageView ivVoiceC;
    @BindView(R.id.iv_voice_d)
    ImageView ivVoiceD;

    private QMUITopBarLayout mTopBar;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private String a, b, c, d;
    private QMUIPopup mNormalPopup;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTopBar();
        OnLongClick();
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.SUCCESS) {
                    ToastUtils.showLong("因为某些比较复杂的原因，语音引擎罢工了(￣^￣)");
                }
            }
        });

    }

    @OnClick({R.id.btn_generate, R.id.iv_history_a, R.id.iv_history_b, R.id.iv_history_c,
            R.id.iv_edit_d, R.id.iv_copy_d, R.id.iv_history_d, R.id.btn_clear,
            R.id.iv_voice_a, R.id.iv_voice_b, R.id.iv_voice_c, R.id.iv_voice_d})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_generate:
                initGenerate();
                break;
            case R.id.btn_clear:
                clearAll();
                break;
            case R.id.iv_history_a:
                List<String> stringLista = mapTransitionList(SPUtils.getInstance("a").getAll());
                String[] listItemsa = stringLista.toArray(new String[stringLista.size()]);
                if (stringLista.size() == 0) {
                    popupHistoryNull(ivHistoryA);
                } else {
                    popupHistory(ivHistoryA, listItemsa, "a");
                }
                break;
            case R.id.iv_history_b:
                List<String> stringListb = mapTransitionList(SPUtils.getInstance("b").getAll());
                String[] listItemsb = stringListb.toArray(new String[stringListb.size()]);
                if (stringListb.size() == 0) {
                    popupHistoryNull(ivHistoryB);
                } else {
                    popupHistory(ivHistoryB, listItemsb, "b");
                }
                break;
            case R.id.iv_history_c:
                List<String> stringListc = mapTransitionList(SPUtils.getInstance("c").getAll());
                String[] listItemsc = stringListc.toArray(new String[stringListc.size()]);
                if (stringListc.size() == 0) {
                    popupHistoryNull(ivHistoryC);
                } else {
                    popupHistory(ivHistoryC, listItemsc, "c");
                }
                break;
            case R.id.iv_edit_d:
                switchEdit();
                break;
            case R.id.iv_copy_d:
                if (StringUtils.isEmpty(etD.getText().toString())) {
                    ToastUtils.showLong("复制了个空气<(='_'=)???>");
                } else {
                    copyTextToClipboard(this, etD.getText().toString());
                    ToastUtils.showLong("复制成功<(=￣ˇ￣=)>");
                }
                break;
            case R.id.iv_history_d:
//                List<String> stringListd = mapTransitionList(SPUtils.getInstance("d").getAll());
//                String[] listItemsd = stringListd.toArray(new String[stringListd.size()]);
//                if (stringListd.size() == 0) {
//                    popupHistoryNull(ivHistoryD);
//                } else {
//                    popupHistory(ivHistoryD, listItemsd, "d");
//                }
                Intent intent = new Intent(this, HistoryActivity.class);
                intent.putExtra("type", "d");
                startActivityForResult(intent, 324);
                break;
            case R.id.iv_voice_a:
                speek(etA.getText().toString());
                break;
            case R.id.iv_voice_b:
                speek(etB.getText().toString());
                break;
            case R.id.iv_voice_c:
                speek(etC.getText().toString());
                break;
            case R.id.iv_voice_d:
                speek(etD.getText().toString());
                break;
        }
    }

    private void OnLongClick() {
        ivHistoryA.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                quickAction(ivHistoryA, "a");
                return true;
            }
        });
        ivHistoryB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                quickAction(ivHistoryB, "b");
                return true;
            }
        });
        ivHistoryC.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                quickAction(ivHistoryC, "c");
                return true;
            }
        });
    }

    private void initGenerate() {
        int num = SPUtils.getInstance().getInt("num", 0);
        a = etA.getText().toString();
        b = etB.getText().toString();
        c = etC.getText().toString();
        if (StringUtils.isEmpty(a)) {
            ToastUtils.showLong("主体没输入ヾ(=ﾟ･ﾟ=)ﾉ！");
        } else if (StringUtils.isEmpty(b)) {
            ToastUtils.showLong("事件没输入ヾ(=ﾟ･ﾟ=)ﾉ！");
        } else if (StringUtils.isEmpty(c)) {
            ToastUtils.showLong("其实就是啥ヾ(=ﾟ･ﾟ=)ﾉ！？");
        } else {
            d = a + b + "是怎么回事呢？" + a + "相信大家都很熟悉，但是" + a + b + "是怎么回事呢，下面就让小编带大家一起了解吧。" +
                    a + b + "，其实就是" + c + "，大家可能会很惊讶" + a + "怎么会" + b + "呢？但事实就是这样，小编也感到非常惊讶。" +
                    "这就是关于" + a + b + "的事情了，大家有什么想法呢，欢迎在评论区告诉小编一起讨论哦！";
            SPUtils.getInstance("a").put(num + "", a);
            SPUtils.getInstance("b").put(num + "", b);
            SPUtils.getInstance("c").put(num + "", c);
            SPUtils.getInstance("d").put(num + "", d);
            SPUtils.getInstance().put("num", num + 1);
            VibrateUtils.vibrate(100);
            KeyboardUtils.hideSoftInput(this);
        }
        etD.setText(d);
    }

    private void clearAll() {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("确定要清空所有已输入内容？")
                .setSkinManager(QMUISkinManager.defaultInstance(this))
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        etA.setText("");
                        etB.setText("");
                        etC.setText("");
                        etD.setText("");
                        ToastUtils.showShort("清空了ヾ(≧O≦)〃嗷~");
                        VibrateUtils.vibrate(200);
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void switchEdit() {
        if (etD.isEnabled()) {
            etD.setEnabled(false);
            ivEditD.setImageResource(R.drawable.ic_edit_off);
            ToastUtils.showShort("<(=╯_╰=)>OFF");
        } else {
            etD.setEnabled(true);
            ivEditD.setImageResource(R.drawable.ic_edit_on);
            ToastUtils.showShort("<(=^_^=)>ON");
        }
    }

    public static void copyTextToClipboard(Context context, String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        } else {
            ClipboardManager clip = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clip.setText(string);
        }
    }

    private void showAbout() {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("关于此软件")
                .setSkinManager(QMUISkinManager.defaultInstance(this))
                .setMessage(AppUtils.getAppName() + "  v" + AppUtils.getAppVersionName() + "\n\n酷安 @夜色微微微凉\n\n仅供娱乐，可随意分享！\n\n(图标来自于网络；无任何商业用途；请勿商用)")
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
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
    protected void onDestroy() {
        super.onDestroy();
        stopSpeech();
    }

    private void popupHistory(View v, String[] listItems, String type) {
        List<String> data = new ArrayList<>();
        Collections.addAll(data, listItems);
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.item_history_list, data);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (type) {
                    case "a":
                        etA.setText(adapterView.getItemAtPosition(i).toString());
//                        Set<String> set = SPUtils.getInstance("a").getAll().keySet();
//                        Iterator<String> it = set.iterator();
//                        List<String> keys = new ArrayList<>();
//                        while (it.hasNext()) {
//                            keys.add(it.next());
//                        }
                        break;
                    case "b":
                        etB.setText(adapterView.getItemAtPosition(i).toString());
                        break;
                    case "c":
                        etC.setText(adapterView.getItemAtPosition(i).toString());
                        break;
                    case "d":
                        etD.setText(adapterView.getItemAtPosition(i).toString());
                        break;
                    default:
                        break;
                }
                if (mNormalPopup != null) {
                    mNormalPopup.dismiss();
                }
            }
        };
        mNormalPopup = QMUIPopups.listPopup(this,
                QMUIDisplayHelper.dp2px(this, 250),
                QMUIDisplayHelper.dp2px(this, 300),
                adapter,
                onItemClickListener)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_TOP)
                .shadow(true)
                .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 5))
                .skinManager(QMUISkinManager.defaultInstance(this))
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
//                        ToastUtils.showShort("onDismiss");
                    }
                })
                .show(v);
    }

    public static List<String> mapTransitionList(Map map) {
        List<String> list = new ArrayList();
        Iterator iter = map.entrySet().iterator(); //获得map的Iterator
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            list.add(entry.getValue().toString());
        }
        return list;
    }

    private void quickAction(View v, String type) {
        QMUIPopups.quickAction(this,
                QMUIDisplayHelper.dp2px(this, 56),
                QMUIDisplayHelper.dp2px(this, 56))
                .shadow(true)
                .skinManager(QMUISkinManager.defaultInstance(this))
                .edgeProtection(QMUIDisplayHelper.dp2px(this, 5))
                .addAction(new QMUIQuickAction.Action().icon(R.drawable.ic_clear).text("清除").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                quickAction.dismiss();
                                SPUtils.getInstance(type).clear();
                            }
                        }
                ))
                .addAction(new QMUIQuickAction.Action().icon(R.drawable.ic_all).text("全部").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                quickAction.dismiss();
                                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                                intent.putExtra("type", type);
                                startActivityForResult(intent, 324);
                            }
                        }
                ))
                .show(v);
    }

    private void popupHistoryNull(View v) {
        TextView textView = new TextView(this);
        textView.setLineSpacing(QMUIDisplayHelper.dp2px(this, 4), 1.0f);
        int padding = QMUIDisplayHelper.dp2px(this, 20);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText("我是一个没有历史的历史记录(｡•ˇ‸ˇ•｡)");
        mNormalPopup = QMUIPopups.popup(this, QMUIDisplayHelper.dp2px(this, 250))
                .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                .view(textView)
                .skinManager(QMUISkinManager.defaultInstance(this))
                .edgeProtection(QMUIDisplayHelper.dp2px(this, 5))
                .offsetX(QMUIDisplayHelper.dp2px(this, 5))
                .offsetYIfBottom(QMUIDisplayHelper.dp2px(this, 5))
                .shadow(true)
                .arrow(true)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
//
                    }
                })
                .show(v);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mNormalPopup.dismiss();
            }
        }, 2000);
    }

    private void initTopBar() {
        mTopBar = findViewById(R.id.topbar);
        mTopBar.setTitle(getResources().getString(R.string.app_name));
        mTopBar.addRightImageButton(R.drawable.ic_about, R.id.topbar_right_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAbout();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 324) {
                String mType, mContent;
                mType = data.getStringExtra("type");
                mContent = data.getStringExtra("content");
                switch (mType) {
                    case "a":
                        etA.setText(mContent);
                        break;
                    case "b":
                        etB.setText(mContent);
                        break;
                    case "c":
                        etC.setText(mContent);
                        break;
                    case "d":
                        etD.setText(mContent);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}