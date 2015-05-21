/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.gallery3d.v5.filtershow.editors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.gallery3d.v5.filtershow.filters.FilterCurvesRepresentation;
import com.android.gallery3d.v5.filtershow.filters.FilterRepresentation;
import com.android.gallery3d.v5.filtershow.imageshow.ImageCurves;
import com.android.gallery3d.v5.filtershow.imageshow.Spline;
import com.test.filter.R;

public class EditorCurves extends Editor implements OnClickListener {
	public static final int ID = R.id.imageCurves;
	ImageCurves mImageCurves;

	public EditorCurves() {
		super(ID);
		// int k =R.menu.filtershow_menu_curves;
	}

	@Override
	protected void updateText() {

	}

	@Override
	public boolean showsPopupIndicator() {
		return true;
	}

	@Override
	public void createEditor(Context context, FrameLayout frameLayout) {
		super.createEditor(context, frameLayout);
		mView = mImageShow = mImageCurves = new ImageCurves(context);
		mImageCurves.setEditor(this);
	}

	@Override
	public void reflectCurrentFilter() {
		super.reflectCurrentFilter();
		FilterRepresentation rep = getLocalRepresentation();
		if (rep != null
				&& getLocalRepresentation() instanceof FilterCurvesRepresentation) {
			FilterCurvesRepresentation drawRep = (FilterCurvesRepresentation) rep;
			mImageCurves.setFilterDrawRepresentation(drawRep);
		}
	}

	@Override
	public void setUtilityPanelUI(View actionButton, View editControl) {
		super.setUtilityPanelUI(actionButton, editControl);
		// setMenuIcon(true);
	}

	@Override
	public void openUtilityPanel(LinearLayout accessoryViewList) {
		Context c = accessoryViewList.getContext();
		LayoutInflater.from(c).inflate(R.layout.layout_editor_curves_panel,
				accessoryViewList);
		ViewGroup cropBtnContainer = (ViewGroup) accessoryViewList
				.findViewById(R.id.curves_btn_container);
		for (int i = 0; i < cropBtnContainer.getChildCount(); i++) {
			View btn = cropBtnContainer.getChildAt(i);
			btn.setOnClickListener(this);
		}
	}

	@Override
	public boolean showsSeekBar() {
		return false;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.curve_btn_rgb:
			mImageCurves.setCurChannel(Spline.RGB);
			break;
		case R.id.curve_btn_red:
			mImageCurves.setCurChannel(Spline.RED);
			break;
		case R.id.curve_btn_green:
			mImageCurves.setCurChannel(Spline.GREEN);
			break;
		case R.id.curve_btn_blue:
			mImageCurves.setCurChannel(Spline.BLUE);
			break;

		default:
			break;
		}
	}
}
