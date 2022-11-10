package com.example.mobile_perfomances;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class add_data extends AppCompatActivity {

    EditText Title, Genre, Producer;
    ImageView Photo;
    Bitmap Img = null, bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Title = findViewById(R.id.Title);
        Genre = findViewById(R.id.Genre);
        Producer = findViewById(R.id.Producer);
        Photo = findViewById(R.id.Photo);
        bm = BitmapFactory.decodeResource(add_data.this.getResources(), R.drawable.picture);
        Photo.setImageBitmap(bm);
    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    bm = BitmapFactory.decodeStream(is);
                    Photo.setImageBitmap(bm);
                } catch (Exception e) {
                    Log.e(e.toString(), e.getMessage());
                }
            }
        }
    });

    public void getImage(View v)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImg.launch(intent);
    }


    public void onClickBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onAdd(View view) {
        EncodeImageClass encodeImage = new EncodeImageClass();
        String title = Title.getText().toString();
        String genre = Genre.getText().toString();
        String producer = Producer.getText().toString();
            post(title, genre, producer, encodeImage.Image(bm), view);
        }

       /* catch(Exception ex){
            Toast.makeText(add_data.this,"Ошибка", Toast.LENGTH_LONG).show();
        }*/

    private void post(String title, String genre, String producer, String image, View v) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ФасхиеваДР/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Perfom modal = new Perfom(null, title, genre, producer, image);
        Call<Perfom> call = retrofitAPI.createPost(modal);
        call.enqueue(new Callback<Perfom>() {
            @Override
            public void onResponse(Call<Perfom> call, Response<Perfom> response) {
                Toast.makeText(add_data.this, "Успешное добавление!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Perfom> call, Throwable t) {
                Toast.makeText(add_data.this, "Что-то пошло не так!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}