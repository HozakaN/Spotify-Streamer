package com.hozakan.spotifystreamer.ui.fragment;

import android.app.Fragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hozakan.spotifystreamer.BuildConfig;
import com.hozakan.spotifystreamer.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by gimbert on 15-06-28.
 */
public class TrackPreviewFragment extends Fragment {

    private static final String TAG = TrackPreviewFragment.class.getSimpleName();

    private static final String ARTIST_NAME_ARG_KEY = "ARTIST_NAME_ARG_KEY";
    private static final String ALBUM_NAME_ARG_KEY = "ALBUM_NAME_ARG_KEY";
    private static final String ALBUM_ARTWORK_URL_ARG_KEY = "ALBUM_ARTWORK_URL_ARG_KEY";
    private static final String TRACK_NAME_ARG_KEY = "TRACK_NAME_ARG_KEY";
    private static final String TRACK_DURATION_ARG_KEY = "TRACK_DURATION_ARG_KEY";
    private static final String TRACK_PREVIEW_URL_ARG_KEY = "TRACK_PREVIEW_URL_ARG_KEY";

    private static final long SEEKBAR_UPDATE_INTERVAL = 500;

    public static TrackPreviewFragment newInstance(
            String artistName,
            String albumName,
            String albumArtworkUrl,
            String trackName,
            long trackDurationMs,
            String trackPreviewUrl) {
        TrackPreviewFragment fragment = new TrackPreviewFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_NAME_ARG_KEY, artistName);
        args.putString(ALBUM_NAME_ARG_KEY, albumName);
        args.putString(ALBUM_ARTWORK_URL_ARG_KEY, albumArtworkUrl);
        args.putString(TRACK_NAME_ARG_KEY, trackName);
        args.putLong(TRACK_DURATION_ARG_KEY, trackDurationMs);
        args.putString(TRACK_PREVIEW_URL_ARG_KEY, trackPreviewUrl);
        fragment.setArguments(args);
        return fragment;
    }

    //views
    private TextView mEtArtistName;
    private TextView mEtAlbumTitle;
    private ImageView mIvAlbumPicture;
    private TextView mEtTrackTitle;
    private SeekBar mSbPreview;
    private TextView mEtCurrentMoment;
    private TextView mEtLastMoment;
    private ImageButton mButtonBack;
    private ImageButton mButtonPlayPause;
    private ProgressBar mPbLoading;
    private ImageButton mButtonNext;

    //display logic
    private boolean mIsPlaying = false;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mProgressUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (TrackPreviewFragment.this.isResumed()) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//                    mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + 1000);
                    mSbPreview.setProgress(mMediaPlayer.getCurrentPosition());
                    mEtCurrentMoment.setText(formaReadabletDuration(mMediaPlayer.getCurrentPosition()));
                    mHandler.postDelayed(this, SEEKBAR_UPDATE_INTERVAL);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_track_preview, container, false);
        mEtArtistName = (TextView)rootView.findViewById(R.id.et_artist_name);
        mEtAlbumTitle = (TextView) rootView.findViewById(R.id.et_album_title);
        mIvAlbumPicture = (ImageView) rootView.findViewById(R.id.iv_album_picture);
        mEtTrackTitle = (TextView) rootView.findViewById(R.id.et_track_title);
        mSbPreview = (SeekBar) rootView.findViewById(R.id.sb_preview);
        mEtCurrentMoment = (TextView) rootView.findViewById(R.id.et_current_moment);
        mEtLastMoment = (TextView) rootView.findViewById(R.id.et_last_moment);
        mButtonBack = (ImageButton) rootView.findViewById(R.id.button_backward);
        mButtonPlayPause = (ImageButton) rootView.findViewById(R.id.button_play_pause);
        mPbLoading = (ProgressBar) rootView.findViewById(R.id.pb_loading);
        mButtonNext = (ImageButton) rootView.findViewById(R.id.button_forward);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle args = getArguments();
        mSbPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mEtArtistName.setText(args.getString(ARTIST_NAME_ARG_KEY));
        mEtAlbumTitle.setText(args.getString(ALBUM_NAME_ARG_KEY));
        Picasso.with(getActivity())
                .load(args.getString(ALBUM_ARTWORK_URL_ARG_KEY))
                .into(mIvAlbumPicture, new Callback() {
                    @Override
                    public void onSuccess() {
                        mIvAlbumPicture.post(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout.LayoutParams params =
                                        new LinearLayout.LayoutParams(mIvAlbumPicture.getWidth(), mIvAlbumPicture.getWidth());
                                mIvAlbumPicture.setLayoutParams(params);
                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                });
        mEtTrackTitle.setText(args.getString(TRACK_NAME_ARG_KEY));
        mSbPreview.setIndeterminate(false);
        mSbPreview.setMax((int) (args.getLong(TRACK_DURATION_ARG_KEY) / 1000));
        mSbPreview.setProgress(0);
        mSbPreview.setEnabled(true);
        mEtCurrentMoment.setText("00:00");
        mEtLastMoment.setText("00:30");
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button back clicked");
            }
        });
        mButtonPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button play pause clicked");
                if (!mIsPlaying) {
                    playMusic();
                } else {
                    pauseMusic();
                }
            }
        });
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button next clicked");
            }
        });
    }

    @Override
    public void onStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            mIsPlaying = false;
            mButtonPlayPause.setImageResource(android.R.drawable.ic_media_play);
        }
        super.onStop();
    }

    private void pauseMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mIsPlaying = false;
            mButtonPlayPause.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    private void playMusic() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    playMusic();
                }
            });
            mMediaPlayer.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
                @Override
                public void onTimedText(MediaPlayer mp, TimedText text) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "TimedText : " + text.getText());
                    }
                }
            });

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mp != null && mp == mMediaPlayer) {
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                        mButtonPlayPause.setImageResource(android.R.drawable.ic_media_play);
                        mEtCurrentMoment.setText("00:30");
                    }
                }
            });
//            mMediaPlayer.setScreenOnWhilePlaying(true); //setScreenOnWhilePlaying(true) is ineffective without a SurfaceHolder
            mMediaPlayer.setWakeMode(getActivity(), PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(getArguments().getString(TRACK_PREVIEW_URL_ARG_KEY));
                mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
                mPbLoading.setVisibility(View.VISIBLE);
                mButtonPlayPause.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mButtonPlayPause.setVisibility(View.VISIBLE);
            mPbLoading.setVisibility(View.GONE);
            mIsPlaying = true;
            mButtonPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            mSbPreview.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.start();
            getActivity().runOnUiThread(mProgressUpdateRunnable);
        }
    }

    private String formaReadabletDuration(int currentPosition) {
        String minutes = new DecimalFormat("#").format((currentPosition / 1000) / 60);
        int seconds = (currentPosition / 1000) % 60;
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        String strSeconds = new DecimalFormat("##").format(seconds);
        if (strSeconds.length() == 1) {
            strSeconds = "0" + strSeconds;
        }
        return String.format("%s:%s", minutes, strSeconds);
    }
}