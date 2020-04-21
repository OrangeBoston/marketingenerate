package com.orangeboston.marketingenerate;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoxing.view.sharpview.SharpEditText;

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

    private QMUITopBarLayout mTopBar;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private String a, b, c, d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTopBar();
    }

    @OnClick({R.id.btn_generate, R.id.iv_history_a, R.id.iv_history_b, R.id.iv_history_c,
            R.id.iv_edit_d, R.id.iv_copy_d, R.id.iv_history_d, R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_generate:
                initGenerate();
                break;
            case R.id.btn_clear:
                clearAll();
                break;
            case R.id.iv_history_a:
                ToastUtils.showShort("这里是历史记录，但是现在还没做出来(｡•ˇ‸ˇ•｡)");
                break;
            case R.id.iv_history_b:
                ToastUtils.showShort("这里是历史记录，但是现在还没做出来(｡•ˇ‸ˇ•｡)");
                break;
            case R.id.iv_history_c:
                ToastUtils.showShort("这里是历史记录，但是现在还没做出来(｡•ˇ‸ˇ•｡)");
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
                ToastUtils.showShort("这里是历史记录，但是现在还没做出来(｡•ˇ‸ˇ•｡)");
                break;
        }
    }

    /**
     * a：谁
     * b：做了什么
     * c：原来是
     * 　ab是怎么回事呢？a相信大家都很熟悉，但是ab是怎么回事呢，下面就让小编带大家一起了解吧。
     * 　ab，其实就是c，大家可能会很惊讶a怎么会b呢？但事实就是这样，小编也感到非常惊讶。
     * 　这就是关于ab的事情了，大家有什么想法呢，欢迎在评论区告诉小编一起讨论哦！
     */
    private void initGenerate() {
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
        }
        etD.setText(d);
        KeyboardUtils.hideSoftInput(this);
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
                .setMessage("营销号生成器\n\n酷安 @夜色微微微凉\n\n仅供娱乐，可随意分享！\n\n(图标来自于网络；无任何商业用途；请勿商用)")
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
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
}
