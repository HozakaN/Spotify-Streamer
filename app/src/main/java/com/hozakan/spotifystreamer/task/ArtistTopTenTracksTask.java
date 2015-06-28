package com.hozakan.spotifystreamer.task;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by gimbert on 15-06-28.
 */
public class ArtistTopTenTracksTask extends AsyncTask<String, Void, Tracks> {

    public interface ArtistTopTenTracksTaskCallback {
        void onSuccess(String artistId, Tracks tracks);
        void onError(String artistId);
    }

    private WeakReference<ArtistTopTenTracksTaskCallback> mCallback;
    private String mArtistId;

    public ArtistTopTenTracksTask(ArtistTopTenTracksTaskCallback callback) {
        mCallback = new WeakReference<>(callback);
    }

    @Override
    protected Tracks doInBackground(String... params) {
        mArtistId = params[0];
        if (TextUtils.isEmpty(mArtistId)) {
            return null;
        }

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("country", Locale.getDefault().getCountry());

        return new SpotifyApi().getService().getArtistTopTrack(mArtistId, queryParams);
    }

    @Override
    protected void onPostExecute(Tracks tracks) {
        super.onPostExecute(tracks);
        if (mCallback.get() != null && !isCancelled()) {
            if (tracks != null) {
                mCallback.get().onSuccess(mArtistId, tracks);
            } else {
                mCallback.get().onError(mArtistId);
            }
        }
    }
}
