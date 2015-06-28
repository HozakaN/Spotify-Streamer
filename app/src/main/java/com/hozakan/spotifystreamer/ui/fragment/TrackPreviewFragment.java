package com.hozakan.spotifystreamer.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hozakan.spotifystreamer.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by gimbert on 15-06-28.
 */
public class TrackPreviewFragment extends Fragment {

    private static final String TAG = TrackPreviewFragment.class.getSimpleName();

    private static final String ARTIST_NAME_ARG_KEY = "ARTIST_NAME_ARG_KEY";
    private static final String ALBUM_NAME_ARG_KEY = "ALBUM_NAME_ARG_KEY";
    private static final String ALBUM_ARTWORK_URL_ARG_KEY = "ALBUM_ARTWORK_URL_ARG_KEY";
    private static final String TRACK_NAME_ARG_KEY = "TRACK_NAME_ARG_KEY";
    private static final String TRACK_DURACTION_ARG_KEY = "TRACK_DURACTION_ARG_KEY";
    private static final String TRACK_PREVIEW_URL_ARG_KEY = "TRACK_PREVIEW_URL_ARG_KEY";

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
        args.putLong(TRACK_DURACTION_ARG_KEY, trackDurationMs);
        args.putString(TRACK_PREVIEW_URL_ARG_KEY, trackPreviewUrl);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView mEtArtistName;
    private TextView mEtAlbumTitle;
    private ImageView mIvAlbumPicture;
    private TextView mEtTrackTitle;
    private SeekBar mPbPreview;
    private TextView mEtCurrentMoment;
    private TextView mEtLastMoment;
    private ImageButton mButtonBack;
    private ImageButton mButtonPlayPause;
    private ImageButton mButtonNext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_track_preview, container, false);
        mEtArtistName = (TextView)rootView.findViewById(R.id.et_artist_name);
        mEtAlbumTitle = (TextView) rootView.findViewById(R.id.et_album_title);
        mIvAlbumPicture = (ImageView) rootView.findViewById(R.id.iv_album_picture);
        mEtTrackTitle = (TextView) rootView.findViewById(R.id.et_track_title);
        mPbPreview = (SeekBar) rootView.findViewById(R.id.sb_preview);
        mEtCurrentMoment = (TextView) rootView.findViewById(R.id.et_current_moment);
        mEtLastMoment = (TextView) rootView.findViewById(R.id.et_last_moment);
        mButtonBack = (ImageButton) rootView.findViewById(R.id.button_backward);
        mButtonPlayPause = (ImageButton) rootView.findViewById(R.id.button_play_pause);
        mButtonNext = (ImageButton) rootView.findViewById(R.id.button_forward);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle args = getArguments();
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
        mPbPreview.setIndeterminate(false);
        mPbPreview.setMax((int) (args.getLong(TRACK_DURACTION_ARG_KEY) / 1000));
        mPbPreview.setProgress(0);
        mPbPreview.setEnabled(true);
        mEtCurrentMoment.setText("0:00");
        mEtLastMoment.setText("0:30");
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
            }
        });
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button next clicked");
            }
        });
    }
}