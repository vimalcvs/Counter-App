package com.vimalcvs.counter;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.preference.PreferenceManager;

public class FastCountButton implements View.OnTouchListener, Handler.Callback {

    public static final int MAX = 300;
    public static final int MIN = 30;
    private static final int FAST_COUNT_MSG = 0;

    private final int mFastCountInterval;
    private final Handler mHandler = new Handler(this);
    private final View mView;
    public boolean mFastCounting;


    public FastCountButton(View view, Runnable action) {
        mView = view;
        view.setOnClickListener(v -> action.run());
        view.setOnTouchListener(this);
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        mFastCountInterval = MAX - mSharedPreferences.getInt("fastCountSpeed", 200) + MIN;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mView.setPressed(true);
                mHandler.sendEmptyMessageDelayed(FAST_COUNT_MSG, ViewConfiguration.getLongPressTimeout());
                break;
            case MotionEvent.ACTION_UP:
                if (!mFastCounting) {
                    mView.performClick();
                }
                // no break
            case MotionEvent.ACTION_CANCEL:
                mHandler.removeMessages(FAST_COUNT_MSG);
                mFastCounting = false;
                mView.setPressed(false);
                mView.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == FAST_COUNT_MSG) {

            if (!mFastCounting) {
                mFastCounting = true;
                mView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                mView.getParent().requestDisallowInterceptTouchEvent(true);
            }
            mView.performClick();
            mHandler.sendEmptyMessageDelayed(FAST_COUNT_MSG, mFastCountInterval);
        }
        return false;
    }

}
