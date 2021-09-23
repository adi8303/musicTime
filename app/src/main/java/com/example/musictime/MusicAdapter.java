package com.example.musictime;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewholder> {
//instance variables
    private Context mContext;
    private ArrayList<MusicFiles> mFiles;

//    creating constructor
    MusicAdapter(Context mContext,ArrayList<MusicFiles> mFiles){
        this.mFiles=mFiles;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.music_items, parent, false);

        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
//    binds views with the data they will contain
        holder.file_name.setText(mFiles.get(position).getTitle());
    byte[] image =getAlbumArt(mFiles.get(position).getPath());
    if(image!=null){
        Glide.with(mContext).asBitmap()
                .load(image)
                .into(holder.album_art);
    }
else{
        Glide.with(mContext)
                .load(R.drawable.defaul)
                .into(holder.album_art);
    }
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }



    public class MyViewholder extends RecyclerView.ViewHolder {
        TextView file_name;
        ImageView album_art;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            file_name=itemView.findViewById(R.id.music_file_name);
            album_art=itemView.findViewById(R.id.music_img);
        }
    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
