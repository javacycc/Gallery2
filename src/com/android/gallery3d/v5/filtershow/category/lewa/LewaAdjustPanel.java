package com.android.gallery3d.v5.filtershow.category.lewa;

import com.android.gallery3d.v5.filtershow.ui.HorizontalListView;
import com.test.filter.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LewaAdjustPanel extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View adjustView = inflater.inflate(R.layout.layout_adjust_panel, null);
		return adjustView;
	}
}
