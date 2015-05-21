package com.android.gallery3d.v5.filtershow.category.lewa;

import com.android.gallery3d.app.Log;
import com.android.gallery3d.v5.filtershow.FilterShowActivity;
import com.android.gallery3d.v5.filtershow.editors.EditorPanel;
import com.android.gallery3d.v5.filtershow.filters.FilterRepresentation;
import com.android.gallery3d.v5.filtershow.imageshow.MasterImage;
import com.android.gallery3d.v5.filtershow.pipeline.ImagePreset;

import android.support.v4.app.Fragment;

public class LewaEditorBaseFragment extends Fragment {
	ImagePreset preImagePreset ;
	FilterRepresentation preRepresentation;
	public LewaEditorBaseFragment(){
		ImagePreset preset = null;
		if(this instanceof EditorPanel){
			preset = MasterImage.getImage().getPreset();
		}else{
			preset = MasterImage.getImage().getCurrentPreset();
		}
		MasterImage.getImage().getCurrentFilterRepresentation();
		preImagePreset = new ImagePreset(preset);
		preRepresentation = MasterImage.getImage().getCurrentFilterRepresentation();
		preRepresentation=preRepresentation==null?null:preRepresentation.copy();
		Log.d("LewaEditorBaseFragment", "get LewaEditorBaseFragment");
	}
	public void restorePreset(){
		if(preImagePreset!=null){
			MasterImage.getImage().setPreset(preImagePreset, null,false);
		}
		MasterImage.getImage().setCurrentFilterRepresentation(preRepresentation);
		((FilterShowActivity)getActivity()).invalidateViews();
		
	}
}
