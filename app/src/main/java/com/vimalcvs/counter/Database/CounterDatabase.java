package com.vimalcvs.counter.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.vimalcvs.counter.Database.Daos.CounterDao;
import com.vimalcvs.counter.Database.Daos.CounterHistoryDao;
import com.vimalcvs.counter.Database.Models.Counter;
import com.vimalcvs.counter.Database.Models.CounterHistory;

import java.util.Date;

@Database(entities = {Counter.class, CounterHistory.class },  version = 25)
@TypeConverters({Converters.class})
public abstract class CounterDatabase extends RoomDatabase {
    private static CounterDatabase sInstance;
    public abstract CounterDao counterDao();
    public abstract CounterHistoryDao CounterHistoryDao();

    public static synchronized CounterDatabase getInstance(Context context){
        if (sInstance == null){
            sInstance = Room.databaseBuilder(context.getApplicationContext(),CounterDatabase.class, "counter.db")
                    .addCallback(rdc)
                    .addMigrations(MIGRATION_24_25)
                    .build();
        }
        return sInstance;
    }


    static final Migration MIGRATION_24_25 = new Migration(24, 25) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE counter_table ADD COLUMN createDataSort INTEGER DEFAULT null ");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN lastResetData INTEGER DEFAULT null");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN lastResetValue INTEGER NOT NULL DEFAULT 0 ");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN counterMaxValue INTEGER NOT NULL DEFAULT 0 ");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN counterMinValue INTEGER NOT NULL DEFAULT 0 ");
        }
    };


    private static final RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
        public void onCreate(SupportSQLiteDatabase db) {
            CounterDao mDao;
            mDao = sInstance.counterDao();
            Date currentDate = new Date();
            currentDate.getTime();

            new Thread(() -> {
                mDao.insert(new Counter("New counter",0, Counter.MAX_VALUE,
                        Counter.MIN_VALUE,
                        1, null, currentDate, currentDate, null,
                        0, 0, 0 ));
            }).start();
        }

        public void onOpen(SupportSQLiteDatabase db) {
            // do something every time database is open
        }
    };

}
