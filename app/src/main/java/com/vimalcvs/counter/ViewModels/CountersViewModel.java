package com.vimalcvs.counter.ViewModels;

import android.app.Application;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vimalcvs.counter.Database.Models.Counter;
import com.vimalcvs.counter.Database.Repo;
import com.vimalcvs.counter.R;

import java.util.Date;
import java.util.List;

public class CountersViewModel extends AndroidViewModel {
    private final Repo mRepo;
    private final Resources mRes;
    public LiveData<List<Counter>> mCounters;

    public CountersViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repo(application);
        mCounters = mRepo.getAllCounters();
        mRes = application.getResources();
    }

    public void incCounter(Counter counter) {
        long maxValue;
        long incOn;
        long value = counter.value;
        maxValue = counter.maxValue;
        incOn = counter.step;
        value += incOn;

        if (value > maxValue) {
            Toast.makeText(getApplication(), mRes.getString(R.string.thisIsMaximum), Toast.LENGTH_SHORT).show();
            counter.value = maxValue;
        } else {
            counter.value = Math.max(counter.minValue, value);
        }

        if (counter.value == counter.minValue){
            Toast.makeText(getApplication(), mRes.getString(R.string.thisIsMinimum), Toast.LENGTH_SHORT).show();
        }

        if (counter.value > counter.counterMaxValue)
            counter.counterMaxValue = counter.value;

        if (counter.value < counter.counterMinValue)
            counter.counterMaxValue = counter.value;
        mRepo.updateCounter(counter);
    }

    public void decCounter(Counter counter) {
        long minValue;
        long decOn;
        minValue = counter.minValue;
        long value = counter.value;
        decOn = counter.step;
        value -= decOn;

        if (value < minValue){
            Toast.makeText(getApplication(), mRes.getString(R.string.thisIsMinimum), Toast.LENGTH_SHORT).show();
            counter.value = minValue;
        }else {
            counter.value = Math.min(counter.maxValue, value);
        }
        if (counter.value == counter.maxValue){
            Toast.makeText(getApplication(), mRes.getString(R.string.thisIsMaximum), Toast.LENGTH_SHORT).show();
        }

        if (counter.value > counter.counterMaxValue)
            counter.counterMaxValue = counter.value;

        if (counter.value < counter.counterMinValue)
            counter.counterMinValue = counter.value;

        mRepo.updateCounter(counter);
    }

    public void countersMoved(Counter counterFrom, Counter counterTo) {

        Date dataFrom;
        Date dataTo;
        if (counterFrom.createDateSort!=null && counterTo.createDateSort!=null){
            dataFrom = counterFrom.createDateSort;
            dataTo = counterTo.createDateSort;
        }else {
            dataFrom = counterFrom.createDate;
            dataTo = counterTo.createDate;
        }

        if (!dataFrom.equals(dataTo)) {
            counterTo.createDateSort = dataFrom;
            mRepo.updateCounter(counterTo);
            counterFrom.createDateSort = dataTo;
            mRepo.updateCounter(counterFrom);
        }

    }

    public LiveData<List<Counter>> getCountersByGroup(String group_title) {
        return mRepo.getCountersByGroup(group_title);
    }

    public LiveData<List<String>> getGroups() {
        return mRepo.getGroups();
    }
}
