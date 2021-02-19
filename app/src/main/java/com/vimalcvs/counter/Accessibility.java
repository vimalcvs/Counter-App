package com.vimalcvs.counter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;
import android.view.HapticFeedbackConstants;
import android.view.View;

import androidx.preference.PreferenceManager;

import com.vimalcvs.counter.R;

import java.util.Locale;

public class Accessibility {

    private final SoundPool mSoundPool;
    private final int mSoundIncId;
    private final int mSoundDecId;
    private final boolean mVibrationIsAllowed;
    private final boolean mClickSoundIsAllowed;
    private final boolean mSpeechOutputIsAllowed;
    private TextToSpeech mTextToSpeech;


    public Accessibility(Context context){
       AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();
       mSoundDecId = mSoundPool.load(context, R.raw.dec_click_sound, 1);
       mSoundIncId = mSoundPool.load(context, R.raw.inc_click_sound, 1);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mVibrationIsAllowed = sharedPreferences.getBoolean("clickVibration", false);
        mClickSoundIsAllowed = sharedPreferences.getBoolean("clickSound", true);
        mSpeechOutputIsAllowed = sharedPreferences.getBoolean("clickSpeak", false);


        mTextToSpeech = new TextToSpeech(context, status -> {
            if(status != TextToSpeech.ERROR) {
                mTextToSpeech.setLanguage(Locale.getDefault());
            }
        });
   }

    public void playIncFeedback(View view, String text){
        playIncSoundEffect();
        playIncVibrationEffect(view);
        speechOutput(text);
    }

    public void playDecFeedback(View view, String text){
        playDecSoundEffect();
        playDecVibrationEffect(view);
        speechOutput(text);
    }

    private void playIncSoundEffect(){
        if (mClickSoundIsAllowed)
        mSoundPool.play(mSoundIncId, 1, 1, 1, 0, 1f);
    }

    private void playDecSoundEffect(){
        if (mClickSoundIsAllowed)
        mSoundPool.play(mSoundDecId, 1, 1, 1, 0, 1f);

    }

    private void playIncVibrationEffect(View view){
        if (mVibrationIsAllowed)
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
    }

    private void playDecVibrationEffect(View view){
        if (mVibrationIsAllowed)
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
    }

    public void speechOutput(String text){
        if (mSpeechOutputIsAllowed)
        mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}
