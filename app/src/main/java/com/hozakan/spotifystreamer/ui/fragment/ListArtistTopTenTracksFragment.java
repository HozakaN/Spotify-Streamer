package com.hozakan.spotifystreamer.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hozakan.spotifystreamer.R;
import com.hozakan.spotifystreamer.task.ArtistTopTenTracksTask;
import com.hozakan.spotifystreamer.ui.adapter.ArtistTopTenTracksAdapter;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by gimbert on 15-06-28.
 */
public class ListArtistTopTenTracksFragment extends Fragment implements ArtistTopTenTracksTask.ArtistTopTenTracksTaskCallback {

    public interface ListArtistTopTenTracksFragmentCallback {
        void onTrackClicked(Track track);
    }

    private static final String ARTIST_ID_EXTRA_KEY = "ARTIST_ID_EXTRA_KEY";
    private static String sRetainedArtistId;
    private static Tracks sRetainedTracks;

    public static ListArtistTopTenTracksFragment newInstance(String artistId) {
        ListArtistTopTenTracksFragment fragment = new ListArtistTopTenTracksFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_ID_EXTRA_KEY, artistId);
        fragment.setArguments(args);
        return fragment;
    }

    private ListView mListView;
    private ArtistTopTenTracksAdapter mAdapter;
    private ArtistTopTenTracksTask mTask;
    private ListArtistTopTenTracksFragmentCallback mCallback;

    private String mArtistId;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mCallback = (ListArtistTopTenTracksFragmentCallback) activity;
    }

    @Override
    public void onDetach() {
        this.mCallback = null;
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArtistId = getArguments().getString(ARTIST_ID_EXTRA_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_list_artist_top_ten_tracks, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new ArtistTopTenTracksAdapter(getActivity(), new ArrayList<Track>());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCallback != null) {
                    mCallback.onTrackClicked(mAdapter.getItem(position));
                }
            }
        });
        if (savedInstanceState != null
                && savedInstanceState.containsKey(ARTIST_ID_EXTRA_KEY)
                && savedInstanceState.get(ARTIST_ID_EXTRA_KEY).equals(sRetainedArtistId)) {
            onSuccess(sRetainedArtistId, sRetainedTracks);
        } else {
            mTask = new ArtistTopTenTracksTask(this);
            mTask.execute(mArtistId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(sRetainedArtistId)) {
            outState.putString(ARTIST_ID_EXTRA_KEY, sRetainedArtistId);
        }
    }

    @Override
    public void onSuccess(String artistId, Tracks tracks) {
        if (artistId.equals(mArtistId)) {
            if (tracks.tracks.size() <= 0) {
                mAdapter.clear();
                Toast.makeText(getActivity(), R.string.top_ten_not_found, Toast.LENGTH_SHORT).show();
            } else {
                mAdapter.setNotifyOnChange(false);
                mAdapter.clear();
                for (int i = 0; i < tracks.tracks.size(); i++) {
                    mAdapter.add(tracks.tracks.get(i));
                }
                mAdapter.notifyDataSetChanged();
            }
            sRetainedArtistId = mArtistId;
            sRetainedTracks = tracks;
        }
    }

    @Override
    public void onError(String artistId) {
        mAdapter.clear();
    }
}
