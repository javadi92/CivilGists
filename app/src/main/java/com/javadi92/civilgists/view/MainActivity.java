package com.javadi92.civilgists.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.javadi92.civilgists.R;
import com.javadi92.civilgists.model.Gist;
import com.javadi92.civilgists.model.GistsDatabase;
import com.javadi92.civilgists.viewmodel.GistViewModel;

import java.util.ArrayList;
import java.util.List;

import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    GistAdapter gistAdapter;
    static GistViewModel gistViewModel;
    GistsDatabase gistsDatabase;
    List<Gist> gists=new ArrayList<>();
    LinearLayoutManager llm;
    private static final int INSERT_PAGE_REQUEST_CODE=1;
    private static final int SHOW_PAGE_REQUEST_CODE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gistViewModel= ViewModelProviders.of(this).get(GistViewModel.class);
        gistsDatabase=GistsDatabase.getInstanse(this.getApplicationContext());

        toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setTitle("یادداشت");
        toolbar.setTitleTextColor(Color.WHITE);

        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersianDate pdate = new PersianDate();
                PersianDateFormat pdformater1 = new PersianDateFormat("Y/m/d");
                pdformater1.format(pdate);
                Intent intent=new Intent(MainActivity.this, AddGistActivity.class);
                startActivityForResult(intent,INSERT_PAGE_REQUEST_CODE);
            }
        });

        recyclerView=findViewById(R.id.recyclerview);
        gistAdapter=new GistAdapter(gists);
        llm=new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(gistAdapter);

        gistViewModel.getAllGists().observe(this, new Observer<List<Gist>>() {
            @Override
            public void onChanged(List<Gist> gists) {
                gistAdapter.setTask(gists);
            }
        });

        gistAdapter.setOnItemClickListener(new GistAdapter.OnItemClickListener() {
            @Override
            public void click(Gist gist) {
                Intent intent=new Intent(MainActivity.this,ShowActivity.class);
                intent.putExtra("item_id",gist.getId());
                intent.putExtra("item_title",gist.getTitle());
                intent.putExtra("item_body",gist.getText());
                intent.putExtra("item_image",gist.getImage());
                startActivityForResult(intent,SHOW_PAGE_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == INSERT_PAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String text = data.getStringExtra("text");
            String image=data.getStringExtra("image");
            String date=data.getStringExtra("date");
            Gist gist = new Gist(title, text,image,date);
            gistViewModel.insertGist(gist);
        }
        else if(requestCode==SHOW_PAGE_REQUEST_CODE && resultCode==RESULT_OK){
            int id=data.getIntExtra("item_id_delete",-1);
            if(id>0){
                String title=data.getStringExtra("item_title_delete");
                String text=data.getStringExtra("item_body_delete");
                String image=data.getStringExtra("item_image_delete");
                String date=data.getStringExtra("item_date_delete");
                Gist gist=new Gist(title,text,image,date);
                gist.setId(id);
                gistViewModel.deleteGist(gist);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
