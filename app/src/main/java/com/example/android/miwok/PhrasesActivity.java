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

public class PhrasesActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;

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

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<New> number = new ArrayList<>();

        number.add(new New("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        number.add(new New("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        number.add(new New("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        number.add(new New("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        number.add(new New("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        number.add(new New("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        number.add(new New("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        number.add(new New("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
        number.add(new New("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
        number.add(new New("Come here.", "әnni'nem", R.raw.phrase_come_here));

        NewAdapter adapter = new NewAdapter(this, number, R.color.category_phrases);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                releaseMediaPlayer();
                New audio = number.get(position);
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.
                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, audio.getmAudioResourceId());
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
