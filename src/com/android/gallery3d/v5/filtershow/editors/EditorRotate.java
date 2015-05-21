/*
 * Copyright (C) 2013 The Android Open Source Project
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.gallery3d.v5.filtershow.FilterShowActivity;
import com.android.gallery3d.v5.filtershow.filters.FilterMirrorRepresentation;
import com.android.gallery3d.v5.filtershow.filters.FilterRepresentation;
import com.android.gallery3d.v5.filtershow.filters.FilterRotateRepresentation;
import com.android.gallery3d.v5.filtershow.filters.LewaFilterRotateRepresentation;
import com.android.gallery3d.v5.filtershow.imageshow.ImageRotate;
import com.android.gallery3d.v5.filtershow.imageshow.LewaImageRotate;
import com.android.gallery3d.v5.filtershow.imageshow.MasterImage;
import com.android.gallery3d.v5.filtershow.pipeline.ImagePreset;
import com.test.filter.R;

public class EditorRotate extends Editor implements EditorInfo, OnClickListener {
    public static final String TAG = EditorRotate.class.getSimpleName();
    public static final int ID = R.id.editorRotate;
    LewaImageRotate mImageRotate;
    

    public EditorRotate() {
        super(ID);
        mChangesGeometry = true;
    }

    @Override
    public void createEditor(Context context, FrameLayout frameLayout) {
        super.createEditor(context, frameLayout);
        if (mImageRotate == null) {
            mImageRotate = new LewaImageRotate(context);
        }
        mView = mImageShow = mImageRotate;
        
        mImageRotate.setEditor(this);
    }

    @Override
    public void reflectCurrentFilter() {
        MasterImage master = MasterImage.getImage();
        master.setCurrentFilterRepresentation(master.getPreset()
                .getFilterWithSerializationName(LewaFilterRotateRepresentation.SERIALIZATION_NAME));
        super.reflectCurrentFilter();
        FilterRepresentation rep = getLocalRepresentation();
        if (rep == null || rep instanceof LewaFilterRotateRepresentation) {
            mImageRotate.setRotateRepresentation((LewaFilterRotateRepresentation)rep);
        } else {
            Log.w(TAG, "Could not reflect current filter, not of type: "
                    + FilterRotateRepresentation.class.getSimpleName());
        }
        mImageRotate.invalidate();
    }

    @Override
    public void openUtilityPanel(final LinearLayout accessoryViewList) {
//        final Button button = (Button) accessoryViewList.findViewById(R.id.applyEffect);
//        button.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                mImageRotate.rotate();
//                String displayVal = mContext.getString(getTextId()) + " "
//                        + mImageRotate.getLocalValue();
//                button.setText(displayVal);
//            }
//        });
    	Context c = accessoryViewList.getContext();
    	LayoutInflater.from(c).inflate(R.layout.layout_editor_rotate_panel, accessoryViewList);
    	ViewGroup rotateBtnContainer = (ViewGroup) accessoryViewList.findViewById(R.id.rotate_btn_container);
    	for(int i=0;i<rotateBtnContainer.getChildCount();i++){
    		View v = rotateBtnContainer.getChildAt(i);
    		if(v!=null) v.setOnClickListener(this);
    	}
    }

    @Override
    public void finalApplyCalled() {
    	FilterRepresentation r = getLocalRepresentation();
//		if(!(r instanceof LewaFilterRotateRepresentation)){
//			return;
//		}
		LewaFilterRotateRepresentation lewaRotateRep = (LewaFilterRotateRepresentation)r;
		ImagePreset oldPreset = MasterImage.getImage().getPreset();
      ImagePreset copy = new ImagePreset(oldPreset);
      FilterRepresentation representation = copy.getRepresentation(lewaRotateRep);
      if (representation == null) {
    	  lewaRotateRep =  (LewaFilterRotateRepresentation) lewaRotateRep.copy();
          copy.addFilter(lewaRotateRep);
      } else {
          if (lewaRotateRep.allowsSingleInstanceOnly()) {
              // Don't just update the filter representation. Centralize the
              // logic in the addFilter(), such that we can keep "None" as
              // null.
              if (!representation.equals(lewaRotateRep)) {
                  // Only do this if the filter isn't the same
                  // (state panel clicks can lead us here)
                  copy.removeFilter(representation);
                  copy.addFilter(lewaRotateRep);
              }
          }
      }
      MasterImage.getImage().setPreset(copy, lewaRotateRep, true);
      MasterImage.getImage().setCurrentFilterRepresentation(lewaRotateRep);
      MasterImage.getImage().getCurrentPreset();
    }

    @Override
    public int getTextId() {
        return R.string.rotate;
    }

    @Override
    public int getOverlayId() {
        return R.drawable.filtershow_button_geometry_rotate;
    }

    @Override
    public boolean getOverlayOnly() {
        return true;
    }

    @Override
    public boolean showsSeekBar() {
        return false;
    }

    @Override
    public boolean showsPopupIndicator() {
        return false;
    }
    
	@Override
	public void onClick(View v) {
		FilterRepresentation r = getLocalRepresentation();
		if(!(r instanceof LewaFilterRotateRepresentation)){
			return;
		}
		LewaFilterRotateRepresentation lewaRotateRep = (LewaFilterRotateRepresentation)r;
		
		FilterRepresentation rep = null;
		FilterRotateRepresentation rotate = lewaRotateRep.getRotateRepresentation();
		FilterMirrorRepresentation mirror = lewaRotateRep.getMirrorRepresentation();
		int id = v.getId();
		switch (id) {
		case R.id.rotate_btn_clockwise: 
			rotate.setClockWise(true);
			rotate.rotateCW();
			break;
		case R.id.rotate_btn_anticlockwise: 
			rotate.setClockWise(false);
			rotate.rotateCW();
			break;
		case R.id.rotate_btn_mirror_left: 
			mirror.setMirroDirection(FilterMirrorRepresentation.MIRROR_DIRECTION_LEFT_RIGHT);
			mirror.cycle();
			break;
		case R.id.rotate_btn_mirror_top: 
			mirror.setMirroDirection(FilterMirrorRepresentation.MIRROR_DIRECTION_TOP_BUTTOM);
			mirror.cycle();
			break;
		default:
			break;
		}
		
		mImageRotate.invalidate();
		

		
//		if(!(r instanceof LewaFilterRotateRepresentation)){
//			return;
//		}
//		LewaFilterRotateRepresentation lewaRotateRep = (LewaFilterRotateRepresentation)r;
//		FilterRotateRepresentation rotate = lewaRotateRep.getRotateRepresentation();
//		rotate.rotateCW();
//		MasterImage.getImage().onNewLook(rotate);
//		((FilterShowActivity)v.getContext()).showRepresentation(rotate);
	}
}
