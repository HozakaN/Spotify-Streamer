package com.hozakan.spotifystreamer.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hozakan.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by gimbert on 15-06-28.
 */
public class ArtistTopTenTracksAdapter extends ArrayAdapter<Track> {

    private final LayoutInflater mInflater;
    private ViewHolder mTmpHolder;
    private Track mTmpTrack;

    public ArtistTopTenTracksAdapter(Context context, List<Track> tracks) {
        super(context, 0, tracks);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_track, parent, false);
            mTmpHolder = new ViewHolder(convertView);
            convertView.setTag(mTmpHolder);
        } else {
            mTmpHolder = (ViewHolder) convertView.getTag();
        }

        mTmpTrack = getItem(position);

        mTmpHolder.title.setText(mTmpTrack.name);
        mTmpHolder.albumName.setText(mTmpTrack.album.name);
        if (mTmpTrack.album.images != null && mTmpTrack.album.images.size() > 0) {
            Picasso.with(getContext()).load(mTmpTrack.album.images.get(0).url).into(mTmpHolder.picture);
        }

        return convertView;
    }

    static class ViewHolder {

        public final ImageView picture;
        public final TextView title;
        public final TextView albumName;

        public ViewHolder(View itemView) {
            this.picture = (ImageView) itemView.findViewById(R.id.picture);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.albumName = (TextView) itemView.findViewById(R.id.album_name);
        }
    }
}
