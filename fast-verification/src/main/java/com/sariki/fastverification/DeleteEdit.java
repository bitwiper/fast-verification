package com.sariki.fastverification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

/**
 * @author sariki
 * @date 2020/04/02.
 */
@SuppressLint("AppCompatCustomView")
public class DeleteEdit extends EditText {
    private OnDelKeyEventListener delKeyEventListener;

    public DeleteEdit(Context context) {
        super(context);
    }

    public DeleteEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeleteEdit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new DeletInputConnection(super.onCreateInputConnection(outAttrs), true, this);
    }

    /**
     * 重写InputConnectionWrapper
     */
    private class DeletInputConnection extends InputConnectionWrapper {
        DeleteEdit editText;

        public DeletInputConnection(InputConnection target, boolean mutable, DeleteEdit editText) {
            super(target, mutable);
            this.editText = editText;
        }

        /**
         * 重新 sendKeyEnvent 通过监听通知外部删除键被点击
         */
        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if (delKeyEventListener != null) {
                    delKeyEventListener.onDeleteClick(editText);
                }
            }
            return super.sendKeyEvent(event);
        }

        /**
         * 这个方法必需写在有些情况下删除键只会调用该方法，不会调用sendKeyEvent需要手动调用下
         */
        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    public void setDelKeyEventListener(OnDelKeyEventListener delKeyEventListener) {
        this.delKeyEventListener = delKeyEventListener;
    }

    public interface OnDelKeyEventListener {
        void onDeleteClick(DeleteEdit editText);
    }
}
