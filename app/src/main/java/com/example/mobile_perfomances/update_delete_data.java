package com.example.mobile_perfomances;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
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

public class update_delete_data extends AppCompatActivity {

    Bundle arg;
    EditText Title;
    EditText Genre;
    EditText Producer;
    Bitmap bm = null;
    ImageView Img;
    Perfom performance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        arg = getIntent().getExtras();
        performance = arg.getParcelable(Perfom.class.getSimpleName());
        Title = findViewById(R.id.Et_Title);
        Genre = findViewById(R.id.Et_Genre);
        Producer = findViewById(R.id.Et_Producer);
        Img= findViewById(R.id.imPhoto);
        Title.setText(performance.getTitle());
        Genre.setText(performance.getGenre());
        Producer.setText(performance.getProducer());
        DecodeImageClass decodeImage = new DecodeImageClass(update_delete_data.this);
        Bitmap userImage = decodeImage.getUserImage(performance.getImage());
        Img.setImageBitmap(userImage);
        if(!performance.getImage().equals("null")){
            bm = userImage;
        }
    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    bm = BitmapFactory.decodeStream(is);
                    Img.setImageURI(uri);
                } catch (Exception e) {
                    Log.e(e.toString(), e.getMessage());
                }
            }
        }
    });

    public void Update(View view) {
        try{
            performance.setTitle(Title.getText().toString());
            performance.setGenre(Genre.getText().toString());
            performance.setProducer(Producer.getText().toString());
            EncodeImageClass EIC = new EncodeImageClass();
            performance.setImage(EIC.Image(bm));
            putUpdates(performance, view);
            SystemClock.sleep(1000);
            GoBack(view);
        }
        catch (Exception ex)
        {
            Toast.makeText(update_delete_data.this, "Что-то пошло не так!", Toast.LENGTH_SHORT).show();
        }
    }

    private void putUpdates(Perfom perfomance, View view)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ФасхиеваДР/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI update = retrofit.create(RetrofitAPI.class);
        Call<Perfom> call = update.createPut(performance, performance.getID());
        call.enqueue(new Callback<Perfom>() {
            @Override
            public void onResponse(Call<Perfom> call, Response<Perfom> response) {
                Toast.makeText(update_delete_data.this, "Успешное изменение!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Perfom> call, Throwable t) {
                Toast.makeText(update_delete_data.this, "Что-то пошло не так!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GoBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private  void delete(Perfom performance, View view)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ФасхиеваДР/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<Perfom> call = null;
        RetrofitAPI retrofitAPIsDel = retrofit.create(RetrofitAPI.class);
        call = retrofitAPIsDel.createDelete(performance.getID());
        call.enqueue(new Callback<Perfom>() {
            @Override
            public void onResponse(Call<Perfom> call, Response<Perfom> response) {
                Toast.makeText(update_delete_data.this, "Успешное удаление!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Perfom> call, Throwable t) {
                Toast.makeText(update_delete_data.this, "Что-то пошло не так!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void Delete(View view) {
        delete(performance, view);
        SystemClock.sleep(1000);
        GoBack(view);
    }

    public void getImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImg.launch(intent);
    }
}