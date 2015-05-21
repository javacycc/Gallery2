package com.android.gallery3d.v5.filtershow.category.lewa;

import com.test.filter.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LewaRotatePanel extends LewaEditorBaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rotateView = inflater.inflate(R.layout.layout_editor_rotate_panel, null);
		return rotateView;
	}
}
