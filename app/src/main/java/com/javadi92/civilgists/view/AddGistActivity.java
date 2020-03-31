package com.javadi92.civilgists.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.javadi92.civilgists.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class AddGistActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button buttonConfirm,buttonPickImage;
    ImageView imageView;
    Intent intent;
    private static final int PICK_PHOTO_FOR_AVATAR=1;
    private static final int PERMISSION_REQUEST_CODE=2;
    String image_path;
    Uri selectedImage;
    String path="";
    InputStream inputStream;
    OutputStream outputStream;
    byte[] bytes=new byte[1024];
    ProgressDialog progressDialog;

    TextInputEditText textInputEditTextTitle,textInputEditTextBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gist);

        progressDialog=new ProgressDialog(this);

        textInputEditTextTitle=findViewById(R.id.text_input_title_insert);
        textInputEditTextBody=findViewById(R.id.text_input_text_insert);

        toolbar=findViewById(R.id.add_gist_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        setTitle("افزودن مطلب");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddGistActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonConfirm=findViewById(R.id.button2);
        buttonPickImage=findViewById(R.id.button_pick_image);
        imageView=findViewById(R.id.imageView_add_gist);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                progressDialog.setTitle("");
                Intent data=new Intent(AddGistActivity.this,MainActivity.class);
                String title=textInputEditTextTitle.getText().toString();
                String text=textInputEditTextBody.getText().toString();
                PersianDate pdate = new PersianDate();
                PersianDateFormat pdformater1 = new PersianDateFormat("Y/m/d");
                String date=pdformater1.format(pdate);
                String image="";

                //save image
                if(selectedImage!=null){
                    imageView.setImageURI(selectedImage);
                    path="data/data/"+AddGistActivity.this.getPackageName()+"/images";
                    File file=new File(path);
                    if(!file.exists()){
                        file.mkdir();
                    }
                    String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    image=path+"/"+timeStamp+".jpg";
                    File file1=new File(image);
                    //
                    try {

                        inputStream = getContentResolver().openInputStream(selectedImage);
                        outputStream=new FileOutputStream(file1);
                        int len=0;
                        while((len = inputStream.read(bytes))>0){
                            outputStream.write(bytes,0,len);
                        }
                        outputStream.flush();
                        outputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("add_activity",image);
                }

                data.putExtra("title",title);
                data.putExtra("text",text);
                data.putExtra("image",image);
                data.putExtra("date",date);
                setResult(RESULT_OK,data);
                finish();
            }
        });

        buttonPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }

    private void pickImage(){
        intent=new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int requestPermission= ContextCompat.checkSelfPermission(AddGistActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(requestPermission!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(AddGistActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
            }
            else {
                startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
            }
        }
        else{
            startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_REQUEST_CODE){
            if(grantResults.length>0){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
                }
            }
            else{
                ActivityCompat.shouldShowRequestPermissionRationale(AddGistActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_PHOTO_FOR_AVATAR && resultCode==RESULT_OK){
            selectedImage=data.getData();
            imageView.setImageURI(selectedImage);
        }
    }

}
