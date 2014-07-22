package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.WineProfile;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Used for the rows in the dialog to filter a base wine by it's vintage. The first row shows all
 * years, and is backed by a {@link BaseWine} object. The rest of the rows show the vintages, which
 * are backed by {@link WineProfile} objects.
 */
public class WineProfilesAdapter extends BaseAdapter {

    private static final int FIRST_ROW = 0;

    private static final int FIRST_ROW_OFFSET = 1;

    private BaseWine mBaseWine;

    private ArrayList<WineProfile> mWineProfiles;

    public WineProfilesAdapter() {

    }

    public void setBaseWine(BaseWine baseWine) {
        mBaseWine = baseWine;
        mWineProfiles = baseWine.getWineProfiles();
    }

    @Override
    public int getCount() {
        return mWineProfiles.size() + FIRST_ROW_OFFSET; //+1 for the all years row
    }

    /**
     * @return Can return a {@link BaseWine} (if the first item was clicked) or a {@link
     * WineProfile}.
     */
    @Override
    public Parcelable getItem(int position) {
        if (position == FIRST_ROW) {
            return mBaseWine;
        }
        position -= FIRST_ROW_OFFSET;
        return mWineProfiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        position -= FIRST_ROW_OFFSET;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChooseVintageDialogRow row = (ChooseVintageDialogRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (ChooseVintageDialogRow) inflater
                    .inflate(R.layout.row_dialog_choose_vintage_with_sizing, parent, false);
        }

        if (position == FIRST_ROW) {
            row.updateData(mBaseWine);
        } else {
            position -= FIRST_ROW_OFFSET; //adjust position so show the correct wine profile data
            row.updateData(mWineProfiles.get(position));
        }
        return row;
    }

}
