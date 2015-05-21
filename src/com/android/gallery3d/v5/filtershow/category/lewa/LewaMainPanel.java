package com.android.gallery3d.v5.filtershow.category.lewa;

import java.util.ArrayList;

import com.android.gallery3d.v5.filtershow.FilterShowActivity;
import com.android.gallery3d.v5.filtershow.category.CategoryPanel;
import com.android.gallery3d.v5.filtershow.category.MainPanel;
import com.android.gallery3d.v5.filtershow.category.lewa.LewaMainTabPenel.OnTabClickListener;
import com.android.gallery3d.v5.filtershow.editors.BasicEditor;
import com.android.gallery3d.v5.filtershow.editors.EditorCrop;
import com.android.gallery3d.v5.filtershow.editors.EditorCurves;
import com.android.gallery3d.v5.filtershow.editors.EditorPanel;
import com.android.gallery3d.v5.filtershow.editors.EditorRotate;
import com.android.gallery3d.v5.filtershow.filters.FilterBasicRepresentation;
import com.android.gallery3d.v5.filtershow.filters.FilterFxRepresentation;
import com.android.gallery3d.v5.filtershow.filters.FilterImageBorderRepresentation;
import com.android.gallery3d.v5.filtershow.filters.FilterRepresentation;
import com.android.gallery3d.v5.filtershow.filters.FiltersManager;
import com.android.gallery3d.v5.filtershow.imageshow.MasterImage;
import com.android.gallery3d.v5.filtershow.pipeline.ImagePreset;
import com.android.gallery3d.v5.filtershow.state.StatePanel;
import com.test.filter.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class LewaMainPanel extends Fragment implements OnTabClickListener {
    public static final int LOOKS = 0;
    public static final int BORDERS = 1;
    public static final int CROP = 2;
    public static final int ROTATION = 3;
    public static final int ADJUST = 4;
    public static final int DRAW = 5;
    public static final int CURVE = 6;
    
    private int mCurrentSelected = -1;
	LewaMainTabPenel tabPanel = new LewaMainTabPenel();
	//LewaCategoryPanel categoryPanel = new LewaCategoryPanel();
	//LewaAdjustPanel adjustPanel = new LewaAdjustPanel();
	View mMainView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.layout_main_panel, null);
		setCategoryFragment(tabPanel,false);
		tabPanel.setTabClickListener(this);
		return mMainView;
	}
	public void showMainTabPanel(){
			setCategoryFragment(tabPanel,false);
			tabPanel.setTabClickListener(this);
		
	}
    public void setCategoryFragment(Fragment panel, boolean fromRight) {
    	if(panel instanceof LewaMainTabPenel){
    		((FilterShowActivity)getActivity()).showMainActionButton();
    	}else{
    		((FilterShowActivity)getActivity()).showPanelActionButton();
    	}
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (fromRight) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
            
        }
        transaction.replace(R.id.panel_container, panel, CategoryPanel.FRAGMENT_TAG);
        transaction.commitAllowingStateLoss();
    }
    public void showImageStatePanel(boolean show) {
        View container = mMainView.findViewById(R.id.state_panel_container);
        FragmentTransaction transaction = null;
        if (container == null) {
            FilterShowActivity activity = (FilterShowActivity) getActivity();
            container = activity.getMainStatePanelContainer(R.id.state_panel_container);
        } else {
            transaction = getChildFragmentManager().beginTransaction();
        }
        if (container == null) {
            return;
        } else {
            transaction = getFragmentManager().beginTransaction();
        }
       // int currentPanel = mCurrentSelected;
        if (show) {
            container.setVisibility(View.VISIBLE);
            StatePanel statePanel = new StatePanel();
            //statePanel.setMainPanel(this);
            FilterShowActivity activity = (FilterShowActivity) getActivity();
            activity.updateVersions();
            transaction.replace(R.id.state_panel_container, statePanel, StatePanel.FRAGMENT_TAG);
        } else {
            container.setVisibility(View.GONE);
            Fragment statePanel = getChildFragmentManager().findFragmentByTag(StatePanel.FRAGMENT_TAG);
            if (statePanel != null) {
                transaction.remove(statePanel);
            }
//            if (currentPanel == VERSIONS) {
//                currentPanel = LOOKS;
//            }
        }
        //mCurrentSelected = -1;
       // showPanel(currentPanel);
        transaction.commit();
    }
    public void setToggleVersionsPanelButton(ImageButton button) {
    	
    }
	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.tab_card_filter){
			loadCategoryLookPanel();
		}
		if(view.getId()==R.id.tab_card_border){
			loadCategoryBorderPanel();
		}
		if(view.getId()==R.id.tab_card_adjust){
			showAdjustPanel();
		}
		if(view.getId()==R.id.tab_card_rotation){
			loadCategoryRotatePanel();
		}
		if(view.getId()==R.id.tab_card_crop){
			loadCategoryCropPanel();
		}
		if(view.getId()==R.id.tab_card_curve){
			loadCategoryCurvePanel();
		}
	}
	public void showAdjustPanel(){
//		FilterRepresentation adjustRep = null;
//    	ArrayList<FilterRepresentation> list = FiltersManager.getManager().getEffects();
//    	for(FilterRepresentation r : list){
//    		if(r instanceof FilterBasicRepresentation){
//    			adjustRep = r;
//    			break;
//    		}
//    	}
    	EditorPanel adjustPanel = new EditorPanel();
    	adjustPanel.setCancelBtnVisible(View.GONE);
    	adjustPanel.setApplyBtnVisible(View.GONE);
    	adjustPanel.setEditor(R.id.basicEditor);
    	//((FilterShowActivity)getActivity()).showFilterRepresentation(adjustRep, adjustPanel);
    	setCategoryFragment(adjustPanel, true);
    	mCurrentSelected = ADJUST;
	}
    public void loadCategoryBorderPanel() {
        CategoryPanel categoryPanel = new CategoryPanel();
        categoryPanel.setAdapter(BORDERS);
        setCategoryFragment(categoryPanel, true);
        mCurrentSelected = BORDERS;
    }
    public void loadCategoryLookPanel() {
        CategoryPanel categoryPanel = new CategoryPanel();
        categoryPanel.setAdapter(LOOKS);
        setCategoryFragment(categoryPanel, true);
        mCurrentSelected = LOOKS;
    }
    public void loadCategoryRotatePanel(){
    	FilterRepresentation rotateRep = null;
    	ArrayList<FilterRepresentation> list = FiltersManager.getManager().getTools();
    	for(FilterRepresentation r : list){
    		if(R.id.editorRotate == r.getEditorId()){
    			rotateRep = r;
    			break;
    		}
    	}
    	ImagePreset preset = MasterImage.getImage().getPreset();
    	FilterRepresentation f = preset.getRepresentation(rotateRep);
    	EditorPanel rotatePanel = new EditorPanel();
    	rotatePanel.setApplyBtnVisible(View.GONE);
    	rotatePanel.setCancelBtnVisible(View.GONE);
    	rotatePanel.setEditor(R.id.editorRotate);
    	((FilterShowActivity)getActivity()).showFilterRepresentation(f==null?rotateRep.copy() : f.copy(), rotatePanel);
    	setCategoryFragment(rotatePanel, true);
    	mCurrentSelected = ROTATION;
    }
    public void loadCategoryCropPanel(){
    	FilterRepresentation cropRep = null;
    	ArrayList<FilterRepresentation> list = FiltersManager.getManager().getTools();
    	for(FilterRepresentation r : list){
    		if(R.id.editorCrop == r.getEditorId()){
    			cropRep = r;
    			break;
    		}
    	}
    	EditorPanel cropPanel = new EditorPanel();
    	cropPanel.setEditor(R.id.editorCrop);
    	cropPanel.setApplyBtnVisible(View.GONE);
    	cropPanel.setCancelBtnVisible(View.GONE);
    	((FilterShowActivity)getActivity()).showFilterRepresentation(cropRep, cropPanel);
    	setCategoryFragment(cropPanel, true);
    	mCurrentSelected = CROP;
    }
    
    public void loadCategoryCurvePanel(){
    	FilterRepresentation curveRep =  FiltersManager.getManager().getCurvesRepresentation();

    	EditorPanel curvePanel = new EditorPanel();
    	curvePanel.setEditor(R.id.imageCurves);
    	curvePanel.setApplyBtnVisible(View.GONE);
    	curvePanel.setCancelBtnVisible(View.GONE);
    	((FilterShowActivity)getActivity()).showFilterRepresentation(curveRep, curvePanel);
    	setCategoryFragment(curvePanel, true);
    	mCurrentSelected = CURVE;
    }
    
	public boolean onBackPressed() {
		Fragment panel = getChildFragmentManager().findFragmentByTag(CategoryPanel.FRAGMENT_TAG);
		if(!(panel instanceof LewaMainTabPenel)){
			if(panel instanceof EditorPanel){
				EditorPanel editorPanel = (EditorPanel)panel;
				if(editorPanel.getEditorId()==BasicEditor.ID){
					View v = getActivity().findViewById(R.id.controlArea);
					if(v!=null) v.setVisibility(View.GONE);
				}
				if(editorPanel.getEditorId()==EditorCurves.ID||editorPanel.getEditorId()==EditorCrop.ID){
					editorPanel.cancelCurrentFilter();
					((FilterShowActivity)getActivity()).showDefaultImageView();
				}
			}else if(panel instanceof CategoryPanel ){
	        	int category = ((CategoryPanel)panel).getCurrentCategory();
	        	if(category == MainPanel.LOOKS){
	        		((FilterShowActivity)getActivity()).showRepresentation(
	                        new FilterFxRepresentation(this.getString(R.string.none),
	                                0, R.string.none));
	        	}else{
	        		((FilterShowActivity)getActivity()).showRepresentation(new FilterImageBorderRepresentation(0));
	        	}
	        	
	        }
			
			setCategoryFragment(tabPanel,false);
			return true;
		}
		return false;
	}
}
