package com.javadi92.civilgists.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Comparator;

//modeli ke be onvane table dar database estefade mishavad
@Entity(tableName = "gists")
public class Gist {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    public String getImage() {
        return image;
    }



    public String getText() {
        return text;
    }


    private String text;

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Gist(String title, String text, String image,String date) {
        this.title = title;
        this.text = text;
        this.image=image;
        this.date=date;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public static final Comparator<Gist> GIST_COMPRATOR=new Comparator<Gist>() {
        @Override
        public int compare(Gist o1, Gist o2) {

            return 0;
        }
    };

}
