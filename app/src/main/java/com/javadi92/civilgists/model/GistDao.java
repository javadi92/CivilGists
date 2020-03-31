package com.javadi92.civilgists.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

//Dao dar model mvvm ke baraye amaliate tarakinesh dar database estefade mishavad
@Dao
public interface GistDao {

    @Insert
    void insert(Gist gist);

    @Update
    void update(Gist gist);

    @Delete
    void delete(Gist gist);

    @Query("SELECT * FROM gists")
    LiveData<List<Gist>> getAllGists();


}
