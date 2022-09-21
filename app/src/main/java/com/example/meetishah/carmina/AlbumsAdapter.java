package com.example.meetishah.carmina;

/**
 * Created by Meeti Shah on 2/17/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import static android.content.ContentValues.TAG;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public ImageView thumbnail, overflow;

        public String songFileName,songImagePath,name;

        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);

            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (SongPlay.mediaPlayer!=null) {
                if (SongPlay.mediaPlayer.isPlaying()) {
                    Log.e(TAG, "onClick: in if");

                    SongPlay.mediaPlayer.stop();

                }
            }
            Intent i = new Intent(v.getContext(),SongPlay.class);
            i.putExtra("Songname",name);
            i.putExtra("song",songFileName);
            i.putExtra("songImagePath",songImagePath);
            mContext.startActivity(i);



        }
    }


    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.name=album.getName();

        holder.songFileName = album.getFileName();
        holder.songImagePath=album.getImagePath();
        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);


    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
