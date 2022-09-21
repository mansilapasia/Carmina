package com.example.meetishah.carmina;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SongPlay extends AppCompatActivity {

    private Button btn;
    private boolean playPause;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;
    String name; Runnable runnable;
    private ImageButton b1,b2,b3,b4;
    private ImageView iv;
    public  static MediaPlayer mediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;
    private String songName;
    public static int oneTimeOnly = 0;
    public ImageView imageView;
    String songImagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_play);
        imageView=(ImageView)findViewById(R.id.imageView);
        songImagePath=getIntent().getStringExtra("songImagePath");

        final String songFileName = getIntent().getStringExtra("song");
        Log.e("path","ff"+songImagePath);
       songName=getIntent().getStringExtra("Songname");

        Log.e("sONG PLAY", "onCreate: "+songFileName );
            b1 = (ImageButton) findViewById(R.id.button);
            b2 = (ImageButton) findViewById(R.id.button2);
            b3 = (ImageButton)findViewById(R.id.button3);
            b4 = (ImageButton)findViewById(R.id.button4);
            tx1 = (TextView)findViewById(R.id.textView2);
            tx2 = (TextView)findViewById(R.id.textView3);
            tx3 = (TextView)findViewById(R.id.textView);
            tx3.setText(songName);
            tx3.toString().toUpperCase();

                try {
                    Uri myUri = Uri.parse("http://192.168.0.101/"+songFileName);
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(getApplicationContext(), myUri);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare(); //don't use prepareAsync for mp3 playback
                    mediaPlayer.start();
                    new DownloadImg().execute();
                } catch (IOException e) {
                    Log.e("hghj", "onCreate: "+e );
                    e.printStackTrace();
                }




            seekbar = (SeekBar)findViewById(R.id.seekBar);
            seekbar.setClickable(true);

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekbar.setMax(mediaPlayer.getDuration());
                    playCycle();
                    mediaPlayer.start();
                }
            });



            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                    if(input)
                    {
                        mediaPlayer.seekTo(progress);
                        tx2.setText(String.format("%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                finalTime)))
                        );

                        tx1.setText(String.format("%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                startTime)))
                        );
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            finalTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();

            if (oneTimeOnly == 0) {
                seekbar.setMax((int) finalTime);
                oneTimeOnly = 1;
            }
            b3.setEnabled(false);
            b2.setEnabled(true);
            tx2.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    finalTime)))
            );

            tx1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    startTime)))
            );


            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(UpdateSongTime,100);
            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"Playing sound",Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();

                    finalTime = mediaPlayer.getDuration();
                    startTime = mediaPlayer.getCurrentPosition();

                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }

                    tx2.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime)))
                    );

                    tx1.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            startTime)))
                    );

                    seekbar.setProgress((int)startTime);
                    myHandler.postDelayed(UpdateSongTime,100);
                    b2.setEnabled(true);
                    b3.setEnabled(false);
                }
            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                    mediaPlayer.pause();

                    finalTime = mediaPlayer.getDuration();
                    startTime = mediaPlayer.getCurrentPosition();

                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }

                    tx2.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime)))
                    );

                    tx1.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            startTime)))
                    );

                    seekbar.setProgress((int)startTime);
                    myHandler.postDelayed(UpdateSongTime,100);
                    b2.setEnabled(false);
                    b3.setEnabled(true);
                }
            });

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = (int)startTime;

                    if((temp+forwardTime)<=finalTime){
                        startTime = startTime + forwardTime;
                        mediaPlayer.seekTo((int) startTime);
                        Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            b4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = (int)startTime;

                    if((temp-backwardTime)>0){
                        startTime = startTime - backwardTime;
                        mediaPlayer.seekTo((int) startTime);
                        Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private Runnable UpdateSongTime = new Runnable() {
            public void run() {
                startTime = mediaPlayer.getCurrentPosition();
                tx1.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );
                seekbar.setProgress((int)startTime);
                myHandler.postDelayed(this, 100);
            }
        };
    public void playCycle()
    {
        seekbar.setProgress(mediaPlayer.getCurrentPosition());
        if(mediaPlayer.isPlaying())
        {
            runnable= new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            myHandler.postDelayed(runnable,1000);
        }
    }
    public class DownloadImg extends AsyncTask<String, Void, String> {

        Bitmap img;

        @Override
        protected void onPreExecute() {
            Bitmap.Config config = null;
            //itemimage= Bitmap.createBitmap(1,1,config);

        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                //imgURl = new URL(url);
                //Log.e("url", url);

                InputStream is = new URL("http://192.168.0.101/"+songImagePath).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                Log.e("url",songImagePath);
                img = BitmapFactory.decodeStream(is);

            } catch (Exception e) {
                Log.e("Exdd", e.toString());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            imageView.setImageBitmap(img);
            Log.e("Done","image");

        }
    }

}