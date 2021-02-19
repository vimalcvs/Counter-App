package com.vimalcvs.counter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vimalcvs.counter.Database.Models.Counter;
import com.vimalcvs.counter.Database.Repo;

import java.util.Date;
import java.util.List;

public class CreateCounterDialogViewModel extends AndroidViewModel {

    private final Repo mRepo;
    public CreateCounterDialogViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repo(application);
    }

    public void createCounter(String title, String group) {

            Counter counter = new Counter(title, 0, Counter.MAX_VALUE,
                    Counter.MIN_VALUE, 1, group, new Date(),
                    new Date(), null, 0, 0, 0);
            mRepo.insertCounter(counter);
    }

    public LiveData<List<String>> getGroups() {
        return mRepo.getGroups();
    }
}
