package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class NumbersFragment extends Fragment {

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
     * This listener gets triggered when the MediaPlayer has completed
     */
    private final MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    public NumbersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_main, container, false);

        // Create and setup the AudioManager to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create a list of words
        final ArrayList<New> number = new ArrayList<>();

        number.add(new New("One", "Lutti", R.drawable.number_one, R.raw.number_one));
        number.add(new New("Two", " otiiko", R.drawable.number_two, R.raw.number_two));
        number.add(new New("Three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        number.add(new New("For", "oyyisa", R.drawable.number_four, R.raw.number_four));
        number.add(new New("Five", "massokka", R.drawable.number_five, R.raw.number_five));
        number.add(new New("Six", "temmokka", R.drawable.number_six, R.raw.number_six));
        number.add(new New("Seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        number.add(new New("Eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        number.add(new New("Nine", "wo’e", R.drawable.number_nine, R.raw.number_nine));
        number.add(new New("Ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten));


        NewAdapter adapter = new NewAdapter(getActivity(), number, R.color.category_numbers);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        // Set a click listener
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
                    mMediaPlayer = MediaPlayer.create(getActivity(), audio.getmAudioResourceId());
                    // Start the audio file
                    mMediaPlayer.start();
                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
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