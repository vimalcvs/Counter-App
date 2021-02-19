package com.vimalcvs.counter.ViewModels.Factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vimalcvs.counter.ViewModels.CounterViewModel;

public class CounterViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private long id;
    private Application application;
   public CounterViewModelFactory(Application application, long id){
        super();
        this.id = id;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(CounterViewModel.class)){
            return (T) new CounterViewModel(application, id);
        }else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
