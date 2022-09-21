package com.example.meetishah.carmina;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SongLyrics extends AppCompatActivity {
String songName,lyrics,text="";
TextView song,lyric;
ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_lyrics);

        lyric=(TextView)findViewById(R.id.lyrics);
        songName=getIntent().getStringExtra("Songname");
        songName.toUpperCase();
        setTitle(songName);
        lyrics=getIntent().getStringExtra("Lyrics");

        
    }

    @Override
    protected void onStart() {
        super.onStart();
       SendPostRequest sd=new SendPostRequest();
       sd.execute("http://192.168.0.101/"+lyrics);
    }

    public class SendPostRequest extends AsyncTask<String,Integer,Void> {




        protected void onPreExecute(){
            pd = new ProgressDialog(SongLyrics.this);
            pd.setTitle("Reading the text file");
            pd.setMessage("Please wait.");
            pd.setCancelable(true);
            pd.setIndeterminate(false);
            pd.show();
        }

        protected Void  doInBackground(String... arg0) {

            URL url;
            try {
                //create url object to point to the file location on internet
                url = new URL(arg0[0]);
                //make a request to server
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                //get InputStream instance
                InputStream is=con.getInputStream();
                //create BufferedReader object
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String line;
                line=br.readLine();
                //read content of the file line by line
                while(line!=null){
                    line=br.readLine();
                    text+=line;
                    text+=("\n");
                }

                br.close();

            }catch (Exception e) {
                e.printStackTrace();
                //close dialog if error occurs
                if(pd!=null) pd.dismiss();
            }

            return null;

        }


        protected void onPostExecute(Void result){
            //close dialog
            if(pd!=null)
                pd.dismiss();
            lyric.setText(text);
            //display read text in TextVeiw


        }
    }



}

