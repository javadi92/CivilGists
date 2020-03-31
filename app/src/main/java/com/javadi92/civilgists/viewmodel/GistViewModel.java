package com.javadi92.civilgists.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.javadi92.civilgists.model.Gist;
import com.javadi92.civilgists.model.GistRepository;

import java.util.List;

//viewmodel rabet beine database va view
public class GistViewModel extends AndroidViewModel {

    public LiveData<List<Gist>> gists;
    public GistRepository gistRepository;

    public GistViewModel(@NonNull Application application) {
        super(application);
        gistRepository=new GistRepository(application);
        gists=gistRepository.getAllGists();
    }


    public void insertGist(Gist gist){
        gistRepository.insertGist(gist);
    }

    public void updateGist(Gist gist){
        gistRepository.updateGist(gist);
    }

    public void deleteGist(Gist gist){
        gistRepository.deleteGist(gist);
    }


    public LiveData<List<Gist>> getAllGists(){
        return gists;
    }
}
