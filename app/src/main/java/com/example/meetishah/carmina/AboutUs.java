package com.example.meetishah.carmina;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    Intent i;
    String addresses,subject,body;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        textView=(TextView)findViewById(R.id.emailtext);
        textView.setClickable(true);
        textView.setOnClickListener(this);
        addresses="carminamusic123@gmail.com";
        subject="";
        body="";


    }

    @Override
    public void onClick(View v) {

        Intent i=new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse("mailto"));
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL,new String[]{addresses});//change in email value
        i.putExtra(Intent.EXTRA_SUBJECT,subject);
        i.putExtra(Intent.EXTRA_TEXT,body);

        startActivity(Intent.createChooser(i,"choose your email client"));


    }
}
