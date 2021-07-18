package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;
    private Button  button;
    private TextView result_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field =findViewById(R.id.user_field);
        button = findViewById(R.id.button);
        result_info = findViewById(R.id.result);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_field.getText().toString().trim().equals(" "))
                    Toast.makeText(MainActivity.this,R.string.no_user_input,Toast.LENGTH_LONG).show();
                    else{
                        String city = user_field.getText().toString();
                        String key = "31fea84dc6bbace2b4c71de2f66ebeeb";
                        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid="+ key +"&units=metric";

                        new GetUrl().execute(url);


                }
            }
        });
    }
    private class GetUrl extends AsyncTask<String ,String,String>{

        protected void onPreExecute(){
            super.onPreExecute();
            result_info.setText("Wait....");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connect = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connect = (HttpURLConnection) url.openConnection();
                connect.connect();

                InputStream inputStream = connect.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                String line = " ";

                while ((line = reader.readLine())!=null) {
                    buffer.append(line).append("\n");

                    return buffer.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connect !=null)
                    connect.disconnect();

                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject jsobj = new JSONObject(result);
                result_info.setText("Temp"+jsobj.getJSONObject("main").getDouble("temp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
}