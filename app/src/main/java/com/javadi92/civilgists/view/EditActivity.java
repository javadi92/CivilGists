package com.javadi92.civilgists.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {

    TextInputEditText textInputEditTextTitle,textInputEditTextBody;
    String title,body,image,path;
    Toolbar toolbar;
    Button buttonPickImage;
    CircleImageView circleImageView;
    Uri uri;
    boolean check_pick_image_use=false;
    private static final int PICK_PHOTO_FOR_AVATAR=1;
    private static final int PERMISSION_REQUEST_CODE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        toolbar=findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("");

        textInputEditTextTitle=findViewById(R.id.text_input_title_edit);
        textInputEditTextBody=findViewById(R.id.text_input_body_edit);

        circleImageView=findViewById(R.id.profile_image);

        buttonPickImage=findViewById(R.id.button_pick_image_edit);

        title=getIntent().getStringExtra("item_title_edit");
        body=getIntent().getStringExtra("item_body_edit");
        image=getIntent().getStringExtra("item_image_edit");

        if(!image.equals("")){
            path=image;
        }


        textInputEditTextTitle.setText(title);
        textInputEditTextBody.setText(body);

        circleImageView.setImageURI(Uri.parse(image));

        buttonPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickImage();
            }
        });
    }

    private void pickImage(){
        Intent intentPickImage =new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("image/*");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int requestPermission= ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(requestPermission!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EditActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
            }
            else {
                startActivityForResult(intentPickImage, PICK_PHOTO_FOR_AVATAR);
            }
        }
        else{
            startActivityForResult(intentPickImage, PICK_PHOTO_FOR_AVATAR);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case (R.id.edit_menu_confirm):
                Intent intent=new Intent(EditActivity.this,ShowActivity.class);
                String title=textInputEditTextTitle.getText().toString();
                String body=textInputEditTextBody.getText().toString();

                if(check_pick_image_use){
                    //replace image
                    //path="/data/data/"+EditActivity.this.getPackageName()+"images/";
                    if(!image.equals("")){
                        File file=new File(path);
                        file.delete();
                        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String new_path="data/data/"+EditActivity.this.getPackageName()+"/images/"+timeStamp+".jpg";
                        image=new_path;
                        File file1=new File(new_path);
                        try {
                            OutputStream outputStream=new FileOutputStream(file1);
                            InputStream inputStream=getContentResolver().openInputStream(uri);
                            byte[] bytes=new byte[1024];
                            int length=0;
                            while((length=inputStream.read(bytes))>0){
                                outputStream.write(bytes,0,length);
                            }
                            outputStream.flush();
                            outputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("edit_activity",image);
                    }
                    else {
                        path="data/data/"+EditActivity.this.getPackageName()+"/images";
                        File file=new File(path);
                        if(!file.exists()){
                            file.mkdir();
                        }
                        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        image=path+"/"+timeStamp+".jpg";
                        File file1=new File(image);
                        //
                        try {

                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            OutputStream outputStream=new FileOutputStream(file1);
                            int len=0;
                            byte[] bytes=new byte[1024];
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
                        Log.d("edit_activity",image);
                    }
                }

                intent.putExtra("item_title_edit_back",title);
                intent.putExtra("item_body_edit_back",body);
                intent.putExtra("item_image_edit_back",image);

                setResult(RESULT_OK,intent);
                finish();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_REQUEST_CODE && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                pickImage();
            }
            else {
                ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_PHOTO_FOR_AVATAR && resultCode==RESULT_OK){
            uri=data.getData();
            circleImageView.setImageURI(uri);
            check_pick_image_use=true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
