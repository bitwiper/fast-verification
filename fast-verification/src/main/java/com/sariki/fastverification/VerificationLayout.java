package com.sariki.fastverification;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.sariki.fastverification.VerificationType.LENGTH_LONG;
import static com.sariki.fastverification.VerificationType.LENGTH_SHORT;

/**
 * @author sariki
 * @date 2020/04/01.
 */
public class VerificationLayout extends LinearLayout {
    private List<DrawText> drawTexts;
    private DeleteEdit edit;
    private Context mActivity;
    private InputMethodManager imm;

    private int mWidth;

    private int length;
    private VerificationTypeListener listener;

    public VerificationLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VerificationLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasureWidth(widthMeasureSpec, length);
    }

    private int getMeasureWidth(int spec, int length) {
        int mode = MeasureSpec.getMode(spec);
        int size = MeasureSpec.getSize(spec);

        switch (mode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                return size / (length + 1);
            case MeasureSpec.UNSPECIFIED:
                if (length == LENGTH_SHORT) {
                    return 150;
                }
                return 120;
        }
        return size;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        addText();
    }

    /**
     * 初始化布局
     *
     * @param context 上下文
     * @param length  验证码框数量
     */
    public void init(Context context, int length) {
        if (length == LENGTH_LONG || length == LENGTH_SHORT) {
            this.setOrientation(LinearLayout.HORIZONTAL);
            this.length = length;
            drawTexts = new ArrayList<>();
            edit = new DeleteEdit(getContext());
            this.mActivity = context;
            this.addView(edit);
            this.setGravity(Gravity.CENTER_HORIZONTAL);
            imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
            initEditListener();
        } else {
            throw new IllegalArgumentException("出于美观考虑，仅支持4或6位长度");
        }
    }

    /**
     * 初始化输入框
     */
    private void initEditListener() {
        edit.setWidth(1);

        //隐藏光标
        edit.setCursorVisible(false);
        //设置字体和背景为透明
        edit.setTextColor(getResources().getColor(android.R.color.transparent));
        edit.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        //默认仅输入数字
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        //限制输入长度
        edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});

        //edit输入监听
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setTextContent(editable);
            }
        });

        //删除键监听
        edit.setDelKeyEventListener(new DeleteEdit.OnDelKeyEventListener() {
            @Override
            public void onDeleteClick(DeleteEdit editText) {
                removeLastChar();
            }
        });

        //点击弹出软键盘
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputKey();
            }
        });
    }

    /**
     * 动态添加textview
     */
    private void addText() {
        if (drawTexts.isEmpty()) {
            int horizontalMargin = mWidth / 18;
            int verticalMargin = horizontalMargin * 3;
            for (int i = 0; i < length; i++) {
                RelativeLayout drawTextParent = new RelativeLayout(getContext());
                LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(mWidth, mWidth);
                parentParams.setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin);
                drawTextParent.setGravity(Gravity.CENTER);
                drawTextParent.setBackgroundResource(drawBackground == 0 ? R.drawable.bg_drawtext : drawBackground);
                drawTextParent.setLayoutParams(parentParams);

                DrawText drawText = new DrawText(getContext());
                drawText.setPadding(horizontalMargin, horizontalMargin, horizontalMargin, horizontalMargin);
                drawText.setTextSize(drawSize == 0 ? 18 : drawSize);
                drawText.setTextColor(drawColor == 0 ? 0xff000000 : drawColor);
                drawTexts.add(drawText);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                drawText.setLayoutParams(params);
                drawTextParent.addView(drawText);
                this.addView(drawTextParent);
            }
        }
    }

    /**
     * 监听edit修改textview显示内容
     *
     * @param editable 数据来源
     */
    private void setTextContent(Editable editable) {
        for (int i = 0; i < editable.length(); i++) {
            if (TextUtils.isEmpty(drawTexts.get(i).getText().toString())) {
                drawTexts.get(i).darwText(String.valueOf(editable.charAt(i)));
            }
            if (i == editable.length() - 1 && editable.length() == drawTexts.size()) {
                listener.onFinish(editable.toString());
            }
        }
    }

    private void setDrawError() {
        ObjectAnimator objectAnimator;
        for (int i = 0; i < drawTexts.size(); i++) {
            drawTexts.get(i).setTextColor(getResources().getColor(R.color.verifi_err));
            objectAnimator = ObjectAnimator.ofFloat(drawTexts.get(i), "translationY", drawTexts.get(i).getTranslationY(), 30, drawTexts.get(i).getTranslationY(), -30, drawTexts.get(i).getTranslationY());
            objectAnimator.setDuration(1000);
            objectAnimator.start();
        }
        //动画结束后触发
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                removeAllChar();
            }
        }, 1000);
    }

    /**
     * 删除textview所有内容
     */
    private void removeAllChar() {
        for (int i = 0; i < drawTexts.size(); i++) {
            drawTexts.get(i).setText("");
            edit.setText("");
        }
        initDrawText();
    }

    /**
     * 删除末尾textview内容
     */
    private void removeLastChar() {
        int lastPostion = 0;
        for (int i = drawTexts.size() - 1; i >= 0; i--) {
            if (!TextUtils.isEmpty(drawTexts.get(i).getText().toString())) {
                lastPostion = i;
                break;
            }
        }
        drawTexts.get(lastPostion).setText("");
    }

    /**
     * 获取edit焦点，显示软键盘
     */
    private void showInputKey() {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        imm.showSoftInput(edit, InputMethodManager.SHOW_FORCED);
    }

    private void initDrawText() {
        for (int i = 0; i < drawTexts.size(); i++) {
            drawTexts.get(i).setTextColor(drawColor == 0 ? 0xff000000 : drawColor);
        }
    }

    /*********************外部可调用方法*********************/

    /**
     * 设置字体大小，在init前调用
     */
    float drawSize = 0;

    public void setDrawSize(float size) {
        this.drawSize = size;
    }

    /**
     * 设置字体颜色，在init前调用
     */
    int drawColor = 0;

    public void setDrawColor(int color) {
        this.drawColor = color;
    }


    /**
     * 设置验证码框背景,在init前调用
     *
     * @param resource
     */
    int drawBackground = 0;

    public void setDrawBackground(int drawBackground) {
        this.drawBackground = drawBackground;
    }

    /**
     * 调用验证码出错时默认处理
     */
    public void setErrType() {
        setDrawError();
    }

    public void setEndListener(VerificationTypeListener listener) {
        this.listener = listener;
    }
}
