package design.chat.template.manager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Vibrator;

import java.io.IOException;

import design.chat.template.util.SettingsPreferences;


/**
 * Created by Asi on 7/28/2017.
 */

public class SoundManger {
    private static  SoundManger instance = null;
    private  MediaPlayer mediaPlayer;
    private static int id;
    private boolean sound;
    private boolean vibrate;
    private static Context mContext;
    Vibrator vibrator;

    public static SoundManger getInstance(Context context) {
        if(instance == null) {
            instance = new SoundManger();
            mContext =context;
        }
        mContext =context;
        return instance;
    }

    private SoundManger() {
        mediaPlayer = new MediaPlayer();


    }

    public void setContext(Context context){
        mContext = context;
    }


    public  MediaPlayer getMediaPlayer(){
        return mediaPlayer;

    }

    public void playSound(AssetFileDescriptor afd) {

        if(SettingsPreferences.isSound(mContext)) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void vibrate(long milisec){
        if(SettingsPreferences.isVibrate(mContext)) {
            vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(milisec);
        }
    }

    public void vibrate(){
        if(SettingsPreferences.isVibrate(mContext)) {
            vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(400);
        }
    }

    public void release(){
        mediaPlayer.release();
    }

    public void reset(){
        mediaPlayer.reset();
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public  void setSound(int id) {
        this.id = id;
    }

}
