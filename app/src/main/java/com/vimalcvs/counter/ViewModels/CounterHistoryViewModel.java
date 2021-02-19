package com.vimalcvs.counter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vimalcvs.counter.Database.Models.CounterHistory;
import com.vimalcvs.counter.Database.Repo;

import java.util.List;

public class CounterHistoryViewModel extends AndroidViewModel {
    private final Repo mRepo;

    public CounterHistoryViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repo(application);
    }

    public LiveData<List<CounterHistory>> getCounterHistoryList(long counterId) {
        return mRepo.getCounterHistoryList(counterId);
    }

    public LiveData<List<CounterHistory>> getCounterHistoryListSortByValue(long counterId) {
        return mRepo.getCounterHistoryListSortByValue(counterId);
    }

    public void clean(long id) {
            mRepo.deleteCounterHistory(id);
    }
}
