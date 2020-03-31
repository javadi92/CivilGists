package com.javadi92.civilgists.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

//database
@Database(entities = {Gist.class},version = 1)
public abstract class GistsDatabase extends RoomDatabase {

    //sakhte yek nemoone az databse baraye estefade dar singeltone
    private static GistsDatabase instanse;

    //name database
    private static final String DB_NAME="gists";

    //Dao baraye tarakonesh ba database(classhaye diger ba in vasile ba in database ertebat bargharar mikonan)
    public abstract GistDao gistDao();


    //design pattern singeltone
    public static synchronized GistsDatabase getInstanse(Context context){

        if(instanse==null){
            instanse=Room.databaseBuilder(context,GistsDatabase.class,DB_NAME)
                    .build();
        }
        return instanse;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
