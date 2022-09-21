package com.example.meetishah.carmina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class Feedback extends AppCompatActivity implements View.OnClickListener {
  TextView t2;
  Intent i;
  RatingBar ratingBar;
  EditText editText;
    TextView rateMessage;
    String ratedValue,s1,s2;
    Button b1;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        t2=(TextView)findViewById(R.id.text2);
        rateMessage=(TextView)findViewById(R.id.text);
        editText=(EditText)findViewById(R.id.editText);
        ratingBar=(RatingBar)findViewById(R.id.ratingbar);
        b1=(Button)findViewById(R.id.button);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratedValue = String.valueOf(ratingBar.getRating());
                rateMessage.setText("You have rated the App : "
                        + ratedValue + "/5.");
                t2.setEnabled(true);
                editText.setEnabled(true);
            }
        });
     b1.setOnClickListener(this);
        sharedPreferences=getSharedPreferences("login",0);
        if(sharedPreferences.contains("userid"))
        {
            s2=sharedPreferences.getString("userid",null);
            Log.e("gfg", "onCreate: "+s2 );
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "data sent", Toast.LENGTH_SHORT).show();
        s1=editText.getText().toString();
        new SendPostRequest().execute();

    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://172.20.10.3/test/feedback.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("feedback", s1);

                postDataParams.put("userid", s2);
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
            Log.e("ddd", "onPostExecute: "+result );
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

}
