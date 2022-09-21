package com.example.meetishah.carmina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Login extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sharedPreferences;

    EditText username,password;
    Button login,signup;
    String s1,s2;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // new SendPostRequest().execute();
        username= (EditText) findViewById(R.id.userEmail);
        password = (EditText) findViewById(R.id.userPaassword);
        login=(Button) findViewById(R.id.loginButton);
        login.setOnClickListener(this);
        signup=(Button) findViewById(R.id.signUp);
        signup.setOnClickListener(this);
        //t1.setOnClickListener(this);
       sharedPreferences=getSharedPreferences("login",0);
        if(sharedPreferences.contains("userid"))
        {
            Intent in=new Intent(this,HomePage.class);
            startActivity(in);
        }
        changeStatusBarColor();

    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view==login)
        {        s1=username.getText().toString().trim();
        s2=password.getText().toString().trim();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("userid",s1);
        editor.commit();
        new SendPostRequest().execute();}
        else
        {
            Intent i=new Intent(this,Signup.class);
            startActivity(i);

        }
    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://172.20.10.3/test/login.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", s1);
                postDataParams.put("password", s2);
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if(result.contains("welcome")) {
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), HomePage.class);
                startActivity(i);
            }
            else{

                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

}