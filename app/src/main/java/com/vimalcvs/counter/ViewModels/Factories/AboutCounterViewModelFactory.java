package com.vimalcvs.counter.ViewModels.Factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vimalcvs.counter.ViewModels.AboutCounterViewModel;

public class AboutCounterViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final long id;
    private final Application application;

    public AboutCounterViewModelFactory(Application application, long id){
        super();
        this.id = id;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(AboutCounterViewModel.class)){
            return (T) new AboutCounterViewModel(application, id);
        }else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
