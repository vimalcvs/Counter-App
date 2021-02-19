package com.vimalcvs.counter.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vimalcvs.counter.Database.Daos.CounterDao;
import com.vimalcvs.counter.Database.Daos.CounterHistoryDao;
import com.vimalcvs.counter.Database.Models.Counter;
import com.vimalcvs.counter.Database.Models.CounterHistory;

import java.util.List;

public class Repo {

   private final CounterDao mCounterDao;
   private final CounterHistoryDao mCounterHistoryDao;

    public Repo(Application application){
       CounterDatabase database = CounterDatabase.getInstance(application);
       mCounterHistoryDao = database.CounterHistoryDao();
       mCounterDao = database.counterDao();
   }

    public void insertCounter(Counter counter){
        new Thread(() -> mCounterDao.insert(counter)).start();
    }

    public void deleteCounter(Counter counter){
        new Thread(() -> mCounterDao.delete(counter)).start();
    }

    public void updateCounter(Counter counter){
        new Thread(() -> mCounterDao.update(counter)).start();
    }

    public void insertCounterHistory(CounterHistory counterHistory){
        new Thread(() -> mCounterHistoryDao.insert(counterHistory)).start();
    }

    public void deleteCounterHistory(long counterId){
        new Thread(() -> mCounterHistoryDao.delete(counterId)).start();
    }

    public LiveData<List<CounterHistory>> getCounterHistoryList(long counterId){
        return mCounterHistoryDao.getCounterHistoryList(counterId);
    }

    public LiveData<List<CounterHistory>> getCounterHistoryListSortByValue(long counterId){
        return mCounterHistoryDao.getCounterHistoryListSortByValue(counterId);
    }

    public LiveData<List<Counter>> getAllCounters(){
       return mCounterDao.getAllCounters();
    }

    public LiveData<List<Counter>> getCountersByGroup(String group){
        return mCounterDao.getCountersByGroup(group);
    }

    public LiveData<Counter> getCounter(long id) {
        return mCounterDao.getCounter(id);
    }

    public LiveData<List<String>> getGroups(){
        return mCounterDao.getGroups();
    }


}





















