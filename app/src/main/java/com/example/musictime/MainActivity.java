package com.example.musictime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE =1;
    ArrayList<MusicFiles> musicFiles ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
        initViewPager();
    }
//asking for permission from the user
    private void permission(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
            ,REQUEST_CODE);
        }
        else{
            Toast.makeText(this,"Permission Granted !",Toast.LENGTH_SHORT).show();
        musicFiles = getAllAudio(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted !",Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        ,REQUEST_CODE);
            }
        }
    }

    private void initViewPager() {

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager =findViewById(R.id.viewpager);

        TabLayout tabLayout =findViewById(R.id.tab_layout);
        // Create an adapter that knows which fragment should be shown on each page
        ViewPagerAdapter viewPagerAdapter =new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragmemts(new SongsFragment(),"Songs");
        viewPagerAdapter.addFragmemts(new AlbumFragment(),"Albums");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment>fragments;
        private ArrayList<String>titles;
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles = new ArrayList<>();
        }
        void addFragmemts(Fragment fragment,String title ){
            fragments.add(fragment);
           titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }


        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get
                    (position);
        }
    }

//    adding audionfro storage to the app :-

//MediaStore.Audio is works like a container. It contains all the audio files from internal and external storage.
//    We will follow some basic steps to retrieve audio files-
//
//    Get all the audio file from MediaStore.
//    Add all the retrieved files in a List.
//    Display the List.


//    This method will return list of all music files
    public static ArrayList<MusicFiles>getAllAudio(Context context){
//        temporary arraylist
        ArrayList<MusicFiles>tempAudioList=new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

//  First we have to spacify what are the contents we are going to retrieve, for that we will create an array of projections we need.
//        declaring arrays of strings
        String[] projection = {
                //you only want to retrieve _albulm ,title etc. columns

                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA, //for path
                MediaStore.Audio.Media.ARTIST
        };
//        Interaction with the media store is done through ContentResolver object.
//        You can obtain its instance from your app's context.
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if(cursor != null){
            //iterating over all of the found songs
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(0);
                String duration = cursor.getString(0);
                String path = cursor.getString(0);
                String artist = cursor.getString(0);
//initialising musicfiles
                MusicFiles musicFiles = new MusicFiles(path,title,artist,album,duration);
                Log.e("Path : " + path,"Album : "+album);
                tempAudioList.add(musicFiles);
            }
            cursor.close();  //imp
}
        return tempAudioList;
    }

}