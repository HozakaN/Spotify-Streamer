package com.hozakan.spotifystreamer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hozakan.spotifystreamer.ui.activity.ArtistActivity;
import com.hozakan.spotifystreamer.ui.fragment.ListArtistTopTenTracksFragment;
import com.hozakan.spotifystreamer.ui.fragment.SearchArtistFragment;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

public class MainActivity extends AppCompatActivity implements
        SearchArtistFragment.SearchArtistFragmentCallback,
        ListArtistTopTenTracksFragment.ListArtistTopTenTracksFragmentCallback {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.artist_top_ten_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArtistClicked(Artist artist) {
        if (!mTwoPane) {
            startActivity(ArtistActivity.createIntent(this, artist));
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.artist_top_ten_container, ListArtistTopTenTracksFragment.newInstance(artist.id))
                    .commit();
        }
    }

    @Override
    public void onTrackClicked(Track track) {

    }
}
