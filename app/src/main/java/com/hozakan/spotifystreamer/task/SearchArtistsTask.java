package com.hozakan.spotifystreamer.task;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by gimbert on 15-06-28.
 */
public class SearchArtistsTask extends AsyncTask<String, Void, ArtistsPager> {

    public interface SearchArtistsTaskCallback {
        void onSuccess(String searchTerm, ArtistsPager artistsPager);
        void onError(String searchTerm);
    }

    private final WeakReference<SearchArtistsTaskCallback> mCallback;
    private String mSearchTerm;

    public SearchArtistsTask(SearchArtistsTaskCallback callback) {
        mCallback = new WeakReference<>(callback);
    }

    @Override
    protected ArtistsPager doInBackground(String... params) {
        mSearchTerm = params[0];
        if (TextUtils.isEmpty(mSearchTerm)) {
            return null;
        }
        return new SpotifyApi().getService().searchArtists(mSearchTerm);
    }

    @Override
    protected void onPostExecute(ArtistsPager artistsPager) {
        super.onPostExecute(artistsPager);
        if (mCallback.get() != null && !isCancelled()) {
            if (artistsPager == null) {
                mCallback.get().onError(mSearchTerm);
            } else {
                mCallback.get().onSuccess(mSearchTerm, artistsPager);
            }
        }
    }
}
