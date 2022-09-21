package com.example.meetishah.carmina;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SearchActivity extends AppCompatActivity {
    Intent i;
    String s;
    private Context mContext;
    String songName,artistName,songPath,songImagePath;

    JSONObject song,jsonObject,json1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final List<String> list = new ArrayList<>();
        final List<String> list1=new ArrayList<>();
        final List<String> list2=new ArrayList<>();
        s=getIntent().getExtras().getString("Name");
        ListView lv = findViewById(R.id.listview);
        //Log.e("d00",s);
        Log.e("dddd", "onCreate: "+s );
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);

        try {


            song=new JSONObject(s);
            //Log.e("json object",song.toString());

            JSONArray songs=song.getJSONArray("songs");
            json1=new JSONObject();
           // Log.e("json array object",song.toString());
            jsonObject=new JSONObject();
            for(int i=0;i<songs.length();i++){
                jsonObject=songs.getJSONObject(i);
                json1=jsonObject.getJSONObject("song");
                songName=json1.getString("songname");
               songPath=json1.getString("path");
               songImagePath=json1.getString("img");
                list1.add(songPath);
                list.add(songName);
                list2.add(songImagePath);
                Log.e("dgndj", "onCreate: "+list1.get(i).toString() );


                //   Log.e("name of song",songName);
            }
        } catch (JSONException e) {
            Log.e("incatch","hi"+e.toString());
            e.printStackTrace();
        }


        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (SongPlay.mediaPlayer!=null) {
                    if (SongPlay.mediaPlayer.isPlaying()) {
                        Log.e(TAG, "onClick: in if");

                        SongPlay.mediaPlayer.stop();

                    }
                }
            //    Intent i = new Intent(view.getContext(),SongPlay.class);




                i=new Intent(getApplicationContext(),SongPlay.class);
                i.putExtra("song",list1.get(position));
                i.putExtra("Songname",list.get(position));
                i.putExtra("songImagePath",list2.get(position));
                startActivity(i);
            }
        });
        adapter.notifyDataSetChanged();
    }
}



