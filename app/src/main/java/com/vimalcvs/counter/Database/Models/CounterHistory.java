package com.vimalcvs.counter.Database.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "counterHistory_table")
public class CounterHistory {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public long value;
    public String data;
    public long counterId;

    public CounterHistory( long value, String data, long counterId) {
        this.value = value;
        this.data = data;
        this.counterId = counterId;
    }

    public void setId(long id) {
        this.id = id;
    }
}
