package com.hozakan.spotifystreamer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hozakan.spotifystreamer.R;
import com.hozakan.spotifystreamer.ui.fragment.TrackPreviewFragment;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by gimbert on 15-06-28.
 */
public class TrackPreviewActivity extends AppCompatActivity {

    private static final String ARTIST_NAME_EXTRA_KEY = "ARTIST_NAME_EXTRA_KEY";
    private static final String ALBUM_NAME_EXTRA_KEY = "ALBUM_NAME_EXTRA_KEY";
    private static final String ALBUM_ARTWORK_URL_EXTRA_KEY = "ALBUM_ARTWORK_URL_EXTRA_KEY";
    private static final String TRACK_NAME_EXTRA_KEY = "TRACK_NAME_EXTRA_KEY";
    private static final String TRACK_DURACTION_EXTRA_KEY = "TRACK_DURACTION_EXTRA_KEY";
    private static final String TRACK_PREVIEW_URL_EXTRA_KEY = "TRACK_PREVIEW_URL_EXTRA_KEY";

    public static Intent createIntent(
            Context context,
            final String artistName,
            final Track track) {
        Intent intent = new Intent(context, TrackPreviewActivity.class);
        intent.putExtra(ARTIST_NAME_EXTRA_KEY, artistName);
        intent.putExtra(ALBUM_NAME_EXTRA_KEY, track.album.name);
        if (track.album.images != null && track.album.images.size() > 0) {
            intent.putExtra(ALBUM_ARTWORK_URL_EXTRA_KEY, track.album.images.get(0).url);
        }
        intent.putExtra(TRACK_NAME_EXTRA_KEY, track.name);
        intent.putExtra(TRACK_DURACTION_EXTRA_KEY, track.duration_ms);
        intent.putExtra(TRACK_PREVIEW_URL_EXTRA_KEY, track.preview_url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, TrackPreviewFragment
                            .newInstance(
                                    intent.getStringExtra(ARTIST_NAME_EXTRA_KEY),
                                    intent.getStringExtra(ALBUM_NAME_EXTRA_KEY),
                                    intent.getStringExtra(ALBUM_ARTWORK_URL_EXTRA_KEY),
                                    intent.getStringExtra(TRACK_NAME_EXTRA_KEY),
                                    intent.getLongExtra(TRACK_DURACTION_EXTRA_KEY, 0),
                                    intent.getStringExtra(TRACK_PREVIEW_URL_EXTRA_KEY)
                            ))
                    .commit();
        }
    }
}
