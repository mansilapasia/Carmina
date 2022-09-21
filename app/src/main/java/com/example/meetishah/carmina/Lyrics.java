package com.example.meetishah.carmina;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Lyrics extends AppCompatActivity {
    Intent i;
    final List<String> list = new ArrayList<>();
    final List<String> list1=new ArrayList<>();
    ListView lv;
    String songName,lyricsPath,s;
    JSONArray songs;
    JSONObject song,jsonObject,json1;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);
        new SendPostRequest().execute();
        Log.e("check", "onCreate: "+s );

         lv = findViewById(R.id.listview);

        Log.e("dddd", "onCreate: "+s );
         adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),SongLyrics.class);
                i.putExtra("Lyrics",list1.get(position));
                i.putExtra("Songname",list.get(position));
                startActivity(i);
            }
        });
        adapter.notifyDataSetChanged();
   // Log.e("ddd",s);
    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {



        protected void onPreExecute(){

        }

        protected String doInBackground(String... arg0) {

            try {
                URL url = new URL("http://192.168.0.101/test/getlyrics.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("query", "");
                // postDataParams.put("password", s2);
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
            s=result;
           // Log.e("check", "onPostExecute: "+s );
            Log.e("result",result.toString());
            try {


                song=new JSONObject(s);
                //Log.e("json object",song.toString());
                songs=song.getJSONArray("songs");

                json1=new JSONObject();
                // Log.e("json array object",song.toString());
                jsonObject=new JSONObject();
                for(int i=0;i<songs.length();i++){
                    jsonObject=songs.getJSONObject(i);
                    json1=jsonObject.getJSONObject("song");
                    songName=json1.getString("songname");
                    lyricsPath=json1.getString("lyric");
                    list1.add(lyricsPath);
                    list.add(songName);
                    Log.e("dgndj", "onCreate: "+list1.get(i).toString() );


                    //   Log.e("name of song",songName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            lv.setAdapter(adapter);



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




