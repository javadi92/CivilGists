package com.javadi92.civilgists.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class GistRepository {

    //sakhte yek nemoone az class Dao baraye tarakonesh ba database
    static GistDao gistDao;

    //sakhte yek nemoone livedata is gistha baraye daryaft az database
    public static LiveData<List<Gist>> gists;

    public GistRepository(Application application){

        GistsDatabase gistsDatabase=GistsDatabase.getInstanse(application);
        this.gistDao=gistsDatabase.gistDao();
        gists=gistDao.getAllGists();
    }

    //methodi baraye daryafte yek livedata az modele morede nazar
    public LiveData<List<Gist>> getAllGists(){
        return gists;
    }


    //methodi baraye ezafe kardane gist dar viewmodel
    public void insertGist(Gist gist){
        new InsertGistAsync(gistDao).execute(gist);
    }

    //methodi baraye virayesh kardane gist dar viewmodel
    public void updateGist(Gist gist){
        new UpdateGistAsync(gistDao).execute(gist);
    }

    //methodi baraye hazf kardane gist dar viewmodel
    public void deleteGist(Gist gist){
        new DeleteGistAsync((gistDao)).execute(gist);
    }

    //sakhte class async baraye amaliate insert dar databse chon room ejaze nemidahad tarakonesh be soorate hamzaman anjam shavand
    public static class InsertGistAsync extends AsyncTask<Gist,Void,Void>{

        GistDao gistDao;
        public InsertGistAsync(GistDao gistDao){
            this.gistDao=gistDao;
        }

        @Override
        protected Void doInBackground(Gist... gists) {

            gistDao.insert(gists[0]);
            return null;
        }
    }

    //sakhte class async baraye amaliate update dar databse chon room ejaze nemidahad tarakonesh be soorate hamzaman anjam shavand
    public static class UpdateGistAsync extends AsyncTask<Gist,Void,Void>{

        GistDao gistDao;
        public UpdateGistAsync(GistDao gistDao){
            this.gistDao=gistDao;
        }
        @Override
        protected Void doInBackground(Gist... gists) {
            gistDao.update(gists[0]);
            return null;
        }
    }

    //sakhte class async baraye amaliate delete dar databse chon room ejaze nemidahad tarakonesh be soorate hamzaman anjam shavand
    public static class DeleteGistAsync extends AsyncTask<Gist,Void,Void>{

        GistDao gistDao;
        public DeleteGistAsync(GistDao gistDao){
            this.gistDao=gistDao;
        }
        @Override
        protected Void doInBackground(Gist... gists) {
            gistDao.delete(gists[0]);
            return null;
        }
    }
}
