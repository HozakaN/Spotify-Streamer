package com.hozakan.spotifystreamer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hozakan.spotifystreamer.R;
import com.hozakan.spotifystreamer.ui.fragment.ListArtistTopTenTracksFragment;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by gimbert on 15-06-28.
 */
public class ArtistActivity extends AppCompatActivity {

    private static final String ARTIST_ID_EXTRA_KEY = "ARTIST_ID_EXTRA_KEY";
    private static final String ARTIST_NAME_EXTRA_KEY = "ARTIST_NAME_EXTRA_KEY";

    public static Intent createIntent(Context context, Artist artist) {
        Intent intent = new Intent(context, ArtistActivity.class);
        intent.putExtra(ARTIST_ID_EXTRA_KEY, artist.id);
        intent.putExtra(ARTIST_NAME_EXTRA_KEY, artist.name);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        getSupportActionBar().setSubtitle(getIntent().getStringExtra(ARTIST_NAME_EXTRA_KEY));

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,
                            ListArtistTopTenTracksFragment.newInstance(getIntent().getStringExtra(ARTIST_ID_EXTRA_KEY)))
                    .commit();
        }
    }
}
