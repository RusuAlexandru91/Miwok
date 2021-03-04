package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;

    /**
     * Handles audio focus when playing a sound file
     */
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered when the MediaPlayer has completed
     */
    private final MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    /**
     * This listener gets triggered whenever the audio focus changes
     */
    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);

        // Create and setup the AudioManager to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Create an ArrayList for the numbers
        final ArrayList<New> number = new ArrayList<>();

        number.add(new New("father", "әpә", R.drawable.family_father, R.raw.family_father));
        number.add(new New("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        number.add(new New("son", "angsi", R.drawable.family_son, R.raw.family_son));
        number.add(new New("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        number.add(new New("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        number.add(new New("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        number.add(new New("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        number.add(new New("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        number.add(new New("grandmother ", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        number.add(new New("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        NewAdapter adapter = new NewAdapter(this, number, R.color.category_family);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        // Makeing the MediaPlayer
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();
                // Get the Word object at the given position the user clicked on
                New audio = number.get(position);
                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.
                    // Create and setup the MediaPlayer for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, audio.getmAudioResourceId());
                    // Start the audio file
                    mMediaPlayer.start();
                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {

            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
