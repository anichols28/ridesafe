package com.example.austin.ex2;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Created by Austin on 1/25/2015.
 */
public class Splash extends Activity {
    MediaPlayer ourSong;
   @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.splash);
       ourSong = MediaPlayer.create(Splash.this, R.raw.moto);
       ourSong.start();
       Thread timer = new Thread(){

           public void run (){
               try{
                    sleep(2876);
               } catch (InterruptedException e){
                    e.printStackTrace();
               }finally{
                    Intent openStartingPoint = new Intent("com.example.austin.ex2.CONTACT");
                    startActivity(openStartingPoint);
               }
           }

       };
        timer.start();
   }

    @Override
    protected void onPause(){
        super.onPause();
        ourSong.release();
        finish();
    }
}
