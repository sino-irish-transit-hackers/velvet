package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserCapturesAdapter extends BaseAdapter {

    private Activity mContext;

    private ArrayList<CaptureDetails> mData;

    private String mUserId;

    public UserCapturesAdapter(Activity context, ArrayList<CaptureDetails> data, String userId) {
        mContext = context;
        mData = data;
        mUserId = userId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = getSimpleListingView(position, convertView, parent);
        return view;
    }

    public View getSimpleListingView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        SimpleListingViewHolder viewHolder = null;
        CaptureDetails capture = mData.get(position);

        if (rowView == null || !(rowView.getTag() instanceof SimpleListingViewHolder)) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            rowView = inflater.inflate(R.layout.row_simple_wine_detail, null);
            viewHolder = new SimpleListingViewHolder();
            viewHolder.wineImage = (ImageView) rowView.findViewById(R.id.image);
            viewHolder.producerName = (TextView) rowView.findViewById(R.id.producer_name);
            viewHolder.wineName = (TextView) rowView.findViewById(R.id.wine_name);
            viewHolder.ratingBarView = (RatingsBarView) rowView.findViewById(R.id.rating_bar);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (SimpleListingViewHolder) rowView.getTag();
        }

        String captureTitle = capture.getDisplayTitle();
        String captureName = capture.getDisplayDescription();
        String captureImageUrl = capture.getPhoto().getThumbUrl();

        // TODO: Toggle Privacy icon over the Image
        if (capture.getPrivate()) {
        } else {
        }

        viewHolder.producerName.setText(captureTitle);
        viewHolder.wineName.setText(captureName);
        ImageLoaderUtil.loadImageIntoView(mContext, captureImageUrl, viewHolder.wineImage);

        viewHolder.ratingBarView.setPercent(capture.getRatingPercentForId(mUserId));

        return rowView;
    }

    static class SimpleListingViewHolder {

        ImageView wineImage;

        TextView producerName;

        TextView wineName;

        RatingsBarView ratingBarView;
    }
}
