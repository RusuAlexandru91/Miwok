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

public class ColorsActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;

    /**
     * Handles audio focus when playing a sound file
     */
    private AudioManager mAudioManager;

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

    /**
     * This listener gets triggered when the player has completed playing the audio file.
     */
    private final MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
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

        number.add(new New("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        number.add(new New("mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        number.add(new New("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        number.add(new New("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        number.add(new New("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        number.add(new New("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        number.add(new New("black", "kululli", R.drawable.color_black, R.raw.color_black));
        number.add(new New("white", "kelelli", R.drawable.color_white, R.raw.color_white));

        // Making adapter
        NewAdapter adapter = new NewAdapter(this, number, R.color.category_colors);
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
                    // Create and setup the MediaPlayer for the audio resource associated
                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, audio.getmAudioResourceId());
                    // Start the audio file
                    mMediaPlayer.start();
                    // Setup a listener on the media player
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't be playing any more sounds.
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