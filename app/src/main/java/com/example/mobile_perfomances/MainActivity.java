package com.example.mobile_perfomances;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Adapter pAdapter;
    private List<Perfom> listPer = new ArrayList<>();
    Spinner spinner;
    EditText filter;
    String[] i = {"по умолчанию","по наименованию", "по жанру"};
    ListView lvPerform;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvPerform = findViewById(R.id.BD_Perform);

        ListView ivProducts  = findViewById(R.id.BD_Perform);
        pAdapter = new Adapter(MainActivity.this, listPer);
        ivProducts.setAdapter(pAdapter);

        spinner=findViewById(R.id.sort);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, i);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Sort(listPer);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        //new GPerfomances().execute();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void Sort(List<Perfom> list){
        lvPerform.setAdapter(null);
        switch(spinner.getSelectedItemPosition()){
            case 0:
                new GPerfomances().execute();
                break;
            case 1:
                Collections.sort(list, Comparator.comparing(Perfom::getTitle));
                break;
            case 2:
                Collections.sort(list, Comparator.comparing(Perfom::getGenre));
                break;
            default:
                break;
        }
        SetAdapter(list);
    }

    public void SetAdapter(List<Perfom> list)
    {
        pAdapter = new Adapter(MainActivity.this,list);
        lvPerform.setAdapter(pAdapter);
        pAdapter.notifyDataSetInvalidated();
    }

    public void onAdd(View view) {
        startActivity(new Intent(this, add_data.class));
    }

    public void onSearch(View view) {

    }

    class GPerfomances extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://ngknn.ru:5001/ngknn/ФасхиеваДР/api/Performances");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String str = "";

                while ((str = reader.readLine()) != null) {
                    result.append(str);
                }
                return result.toString();
            } catch (Exception exception) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            try
            {
                listPer.clear();
                JSONArray tempArray = new JSONArray(s);
                for (int i = 0; i < tempArray.length(); i++)
                {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    Perfom tempAnimal = new Perfom(
                            productJson.getInt("ID"),
                            productJson.getString("Title"),
                            productJson.getString("Genre"),
                            productJson.getString("Producer"),
                            productJson.getString("Image")
                    );
                    listPer.add(tempAnimal);
                    pAdapter.notifyDataSetInvalidated();
                }
            } catch (Exception ignored) {
            }
        }
    }
}