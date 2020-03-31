package com.javadi92.civilgists.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javadi92.civilgists.R;
import com.javadi92.civilgists.model.Gist;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ShowActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textViewTitle,textViewBody;
    String title,body,image,date;
    int id;
    Toolbar toolbar;
    ConstraintLayout constraintLayout;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        toolbar=findViewById(R.id.show_toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView=findViewById(R.id.image_view_show);
        constraintLayout=findViewById(R.id.constraintLayout_show);
        linearLayout=findViewById(R.id.linearlayout_show);
        textViewTitle=findViewById(R.id.tv_title_show);
        textViewBody=findViewById(R.id.tv_body_show);

        id=getIntent().getIntExtra("item_id",-1);
        title=getIntent().getStringExtra("item_title");
        body=getIntent().getStringExtra("item_body");
        image=getIntent().getStringExtra("item_image");
        date=getIntent().getStringExtra("item_date");

        imageHandler(image);

        textViewTitle.setText(title);
        textViewBody.setText(body);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.show_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case (R.id.show_menu_delete):
                Intent intentDelete=new Intent(ShowActivity.this,MainActivity.class);
                intentDelete.putExtra("item_id_delete",id);
                intentDelete.putExtra("item_title_delete",title);
                intentDelete.putExtra("item_body_delete",body);
                intentDelete.putExtra("item_image_delete",image);
                intentDelete.putExtra("item_date_delete",date);
                setResult(RESULT_OK,intentDelete);
                finish();
                return true;
            case (R.id.show_menu_edit):
                Intent intentEdit=new Intent(ShowActivity.this,EditActivity.class);
                intentEdit.putExtra("item_title_edit",title);
                intentEdit.putExtra("item_body_edit",body);
                intentEdit.putExtra("item_image_edit",image);
                intentEdit.putExtra("item_date_edit",date);
                startActivityForResult(intentEdit,1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String title=data.getStringExtra("item_title_edit_back");
            String body=data.getStringExtra("item_body_edit_back");
            String image=data.getStringExtra("item_image_edit_back");
            String date=data.getStringExtra("item_date_edit_back");
            textViewTitle.setText(title);
            textViewBody.setText(body);
            imageHandler(image);
            Gist gist=new Gist(title,body,image,date);
            gist.setId(id);
            MainActivity.gistViewModel.updateGist(gist);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void imageHandler(String image){
        if(image.equals("")){
            constraintLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }
        else{
            constraintLayout.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            Picasso.get().load(new File(image)).fit().into(imageView);
        }
    }
}
