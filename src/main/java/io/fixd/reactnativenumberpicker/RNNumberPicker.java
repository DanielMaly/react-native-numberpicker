package io.fixd.reactnativenumberpicker;

import javax.annotation.Nullable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.EditText;
import android.graphics.Color;

import com.facebook.react.bridge.ReactContext;

public class RNNumberPicker extends NumberPicker {

    private @Nullable OnChangeListener mOnChangeListener;
    private boolean mSuppressNextEvent;
    private @Nullable Integer mStagedSelection;
    private @Nullable Integer mTextSize;
    private @Nullable Integer mTextColor;

    private ReactContext reactContext;

    /**
     * Listener interface for events.
     */
    public interface OnChangeListener {
        void onValueChange(int value, int viewId, ReactContext reactContext);
    }

    public RNNumberPicker(ReactContext context) {
        super(context);
        this.reactContext = context;
    }

    public RNNumberPicker(ReactContext context, AttributeSet attrs) {
        super(context, attrs);
        this.reactContext = context;
    }

    public RNNumberPicker(ReactContext context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.reactContext = context;
    }

    public void setKeyboardInputEnabled(boolean enabled) {
        if(!enabled) {
            this.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        }
        else {
            this.setDescendantFocusability(NumberPicker.FOCUS_AFTER_DESCENDANTS);
        }
    }


    public void setOnChangeListener(@Nullable OnChangeListener onValueChangeListener) {
        setOnValueChangedListener(
            new OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    if (!mSuppressNextEvent && mOnChangeListener != null) {
                        mOnChangeListener.onValueChange(newVal, getId(), reactContext);
                    }
                    mSuppressNextEvent = false;
                }
            }
        );
        mOnChangeListener = onValueChangeListener;
    }

    @Nullable public OnChangeListener getOnChangeListener() {
        return mOnChangeListener;
    }

    /**
     * Will cache "selection" value locally and set it only once {@link #updateStagedSelection} is
     * called
     */
    public void setStagedSelection(int selection) {
        mStagedSelection = selection;
    }

    public void updateStagedSelection() {
        if (mStagedSelection != null) {
            setValueWithSuppressEvent(mStagedSelection);
            mStagedSelection = null;
        }
    }

    /**
     * Set the selection while suppressing the follow-up event.
     * This is used so we don't get an event when changing the selection ourselves.
     *
     * @param value
     */
    private void setValueWithSuppressEvent(int value) {
        if (value != getValue()) {
            mSuppressNextEvent = true;
            setValue(value);
        }
    }

}
