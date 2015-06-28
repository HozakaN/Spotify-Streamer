package com.hozakan.spotifystreamer.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hozakan.spotifystreamer.R;
import com.hozakan.spotifystreamer.task.SearchArtistsTask;
import com.hozakan.spotifystreamer.ui.adapter.ArtistsListAdapter;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by gimbert on 15-06-28.
 */
public class SearchArtistFragment extends Fragment implements SearchArtistsTask.SearchArtistsTaskCallback {

    public interface SearchArtistFragmentCallback {
        void onArtistClicked(Artist artist);
    }

    private static final String SEARCH_TERM_KEY = "SEARCH_TERM_KEY";
    //views
    private ListView mListView;
    private EditText mEtSearch;

    //technical attributes
    private SpotifyApi mApi;
    private ArtistsListAdapter mAdapter;
    private SearchArtistsTask mTask;
    private SearchArtistFragmentCallback mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mCallback = (SearchArtistFragmentCallback) activity;
    }

    @Override
    public void onDetach() {
        this.mCallback = null;
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = new SpotifyApi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_search_artist, container, false);
        mEtSearch = (EditText) rootView.findViewById(R.id.artist_search_term);
        mListView = (ListView) rootView.findViewById(R.id.list);
        mAdapter = new ArtistsListAdapter(getActivity(), new ArrayList<Artist>());
        mListView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCallback != null) {
                    mCallback.onArtistClicked(mAdapter.getItem(position));
                }
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchArtists(s.toString());
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey(SEARCH_TERM_KEY)) {
            mEtSearch.setText(savedInstanceState.getString(SEARCH_TERM_KEY));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_TERM_KEY, mEtSearch.getText().toString());
    }

    private void searchArtists(String searchTerm) {
        if (mTask != null) {
            mTask.cancel(true);
        }
        mTask = new SearchArtistsTask(this);
        mTask.execute(searchTerm);
//        SpotifyService service = mApi.getService();
//        service.getRelatedArtists(searchTerm, new Callback<Artists>() {
//            @Override
//            public void success(Artists artists, Response response) {
//                mAdapter.setNotifyOnChange(false);
//                mAdapter.clear();
//                for (int i = 0; i < artists.artists.size(); i++) {
//                    mAdapter.add(artists.artists.get(i));
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                String text = "dqwd";
//                String dtext = "dqwd";
//            }
//        });
    }

    @Override
    public void onSuccess(String searchTerm, ArtistsPager artistsPager) {
        if (searchTerm.equals(mEtSearch.getText().toString())) {
            if (artistsPager.artists.total == 0) {
                mAdapter.clear();
                Toast.makeText(getActivity(), R.string.artist_not_found, Toast.LENGTH_SHORT).show();
            } else {
                mAdapter.setNotifyOnChange(false);
                mAdapter.clear();
                for (int i = 0; i < artistsPager.artists.items.size(); i++) {
                    mAdapter.add(artistsPager.artists.items.get(i));
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(String searchTerm) {
        mAdapter.clear();
    }
}
