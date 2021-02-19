package com.vimalcvs.counter.ViewModels.Factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vimalcvs.counter.ViewModels.CreateEditCounterViewModel;

public class CreateEditCounterViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final long id;
    private final Application application;
   public CreateEditCounterViewModelFactory(Application application, long id){
        super();
        this.id = id;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(CreateEditCounterViewModel.class)){
            return (T) new CreateEditCounterViewModel(application, id);
        }else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
