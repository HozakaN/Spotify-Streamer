package com.hozakan.spotifystreamer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hozakan.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by gimbert on 15-06-28.
 */
public class ArtistsListAdapter extends ArrayAdapter<Artist> {

    //technical attributes
    private final LayoutInflater mInflater;
    private ViewHolder mTmpHolder;
    private Artist mTmpArtist;

    public ArtistsListAdapter(Context context, List<Artist> artists) {
        super(context, 0, artists);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mTmpArtist = getItem(position);
        mTmpHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_artist, parent, false);
            mTmpHolder = new ViewHolder(convertView);
            convertView.setTag(mTmpHolder);
        } else {
            mTmpHolder = (ViewHolder) convertView.getTag();
        }

        if (mTmpArtist.images != null && mTmpArtist.images.size() > 0) {
            Picasso.with(getContext()).load(mTmpArtist.images.get(0).url).into(mTmpHolder.picture);
        }
        mTmpHolder.name.setText(mTmpArtist.name);

        return convertView;
    }

    static class ViewHolder {

        public final ImageView picture;
        public final TextView name;

        public ViewHolder(View itemView) {
            picture = (ImageView) itemView.findViewById(R.id.picture);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
