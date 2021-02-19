package com.vimalcvs.counter.ViewModels;

import android.app.Application;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.Objects;

import com.vimalcvs.counter.Accessibility;
import com.vimalcvs.counter.Database.Models.Counter;
import com.vimalcvs.counter.Database.Models.CounterHistory;
import com.vimalcvs.counter.Database.Repo;
import com.vimalcvs.counter.R;
import com.vimalcvs.counter.Utility;

public class CounterViewModel extends AndroidViewModel {
    private final Repo mRepo;
    private long mOldValue;
    public LiveData<Counter> mCounter;
    private final Accessibility mAccessibility;
    private final Resources mRes;



    public CounterViewModel(@NonNull Application application, long counterId) {
        super(application);
        mRepo = new Repo(application);
        mCounter = mRepo.getCounter(counterId);
        mAccessibility = new Accessibility(application);
        mRes = application.getResources();

    }

    public void incCounter(View view) {
        long maxValue;
        long incOn;
        long value = Objects.requireNonNull(mCounter.getValue()).value;
        incOn = mCounter.getValue().step;
        maxValue = mCounter.getValue().maxValue;
        value += incOn;

        if (value > maxValue) {
            Toast.makeText(getApplication(), mRes.getString(R.string.thisIsMaximum), Toast.LENGTH_SHORT).show();
            mCounter.getValue().value = maxValue;
        } else {
            mCounter.getValue().value = Math.max(mCounter.getValue().minValue, value);
        }
        if (mCounter.getValue().value == mCounter.getValue().minValue){
            Toast.makeText(getApplication(), mRes.getString(R.string.thisIsMinimum), Toast.LENGTH_SHORT).show();
        }

        if (mCounter.getValue().value > mCounter.getValue().counterMaxValue)
            mCounter.getValue().counterMaxValue = mCounter.getValue().value;

        if (mCounter.getValue().value < mCounter.getValue().counterMinValue)
            mCounter.getValue().counterMaxValue = mCounter.getValue().value;

        mAccessibility.playIncFeedback(view, String.valueOf(mCounter.getValue().value));
        mRepo.updateCounter(mCounter.getValue());
    }

    public void decCounter(View view){
        long minValue;
        long decOn;
        long value = Objects.requireNonNull(mCounter.getValue()).value;
        decOn = mCounter.getValue().step;
        minValue = mCounter.getValue().minValue;
        value -= decOn;

        if (value < minValue){
            Toast.makeText(getApplication(), mRes.getString(R.string.thisIsMinimum), Toast.LENGTH_SHORT).show();
            mCounter.getValue().value = minValue;
        }else {
            mCounter.getValue().value = Math.min(mCounter.getValue().maxValue, value);
        }
        if (mCounter.getValue().value == mCounter.getValue().maxValue){
            Toast.makeText(getApplication(), mRes.getString(R.string.thisIsMaximum), Toast.LENGTH_SHORT).show();
        }

        if (mCounter.getValue().value > mCounter.getValue().counterMaxValue)
            mCounter.getValue().counterMaxValue = mCounter.getValue().value;

        if (mCounter.getValue().value < mCounter.getValue().counterMinValue)
            mCounter.getValue().counterMinValue = mCounter.getValue().value;

        mAccessibility.playDecFeedback(view, String.valueOf(mCounter.getValue().value));
        mRepo.updateCounter(mCounter.getValue());
    }

    public void resetCounter(){
        mOldValue = Objects.requireNonNull(mCounter.getValue()).value;
        mCounter.getValue().lastResetValue = mOldValue;
        mCounter.getValue().lastResetDate = new Date();
        if (mCounter.getValue().minValue > 0){
            mCounter.getValue().value = mCounter.getValue().minValue;
        }else {
            mCounter.getValue().value = 0;
        }
        mAccessibility.speechOutput(String.valueOf(mCounter.getValue().value));
        mRepo.updateCounter(mCounter.getValue());
    }

    public void restoreValue(){
        Objects.requireNonNull(mCounter.getValue()).value = mOldValue;
        mAccessibility.speechOutput(String.valueOf(mCounter.getValue().value));
        mRepo.updateCounter(mCounter.getValue());
    }

    public void saveValueToHistory(){
        mRepo.insertCounterHistory(new CounterHistory(Objects.requireNonNull(
                mCounter.getValue()).value, Utility.formatDateToString(new Date()), mCounter.getValue().id));
        Toast.makeText(getApplication(), getApplication().getString(R.string.createEditCounterCounterValueHint) + " " +
                mCounter.getValue().value + " " + getApplication().getString(R.string.saveToHistoryToast), Toast.LENGTH_SHORT).show();
    }
    public void deleteCounter(){
        mRepo.deleteCounterHistory(Objects.requireNonNull(mCounter.getValue()).id);
        new Handler().postDelayed(() -> mRepo.deleteCounter(mCounter.getValue()),500);
    }

    /*method for changing the font size depending on the counter value*/
    public int getValueTvSize() {
        switch (String.valueOf(mCounter.getValue().value).length()) {
            case 1:
            case 2:
                return 150;
            case 3:
                return 130;
            case 4:
                return 120;
            case 5:
                return 110;
            case 6:
                return 100;
            case 7:
                return 90;
            case 8:
                return 80;
            case 9:
                return 70;
            case 10:
            case 11:
                return 60;
            case 12:
            case 13:
                return 50;
            case 14:
            case 17:
            case 15:
            case 16:
            case 18:
            case 19:
                return 40;
        }
        return 0;
    }

}
