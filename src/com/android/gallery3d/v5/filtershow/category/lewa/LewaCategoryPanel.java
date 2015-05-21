package com.android.gallery3d.v5.filtershow.category.lewa;



import com.android.gallery3d.v5.filtershow.FilterShowActivity;
import com.android.gallery3d.v5.filtershow.category.CategoryAdapter;
import com.android.gallery3d.v5.filtershow.category.CategoryTrack;
import com.android.gallery3d.v5.filtershow.category.CategoryView;
import com.android.gallery3d.v5.filtershow.category.MainPanel;
import com.android.gallery3d.v5.filtershow.ui.HorizontalListView;
import com.test.filter.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LewaCategoryPanel extends LewaEditorBaseFragment {
	HorizontalListView actionListView ;
    private CategoryAdapter mAdapter;
    
    private int mCurrentAdapter = LewaMainPanel.LOOKS;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View categoryView = inflater.inflate(R.layout.layout_category_panel, null);
		actionListView = (HorizontalListView)categoryView.findViewById(R.id.action_lv);
		View panelView =  categoryView.findViewById(R.id.listItems);
		if (panelView instanceof CategoryTrack) {
            CategoryTrack panel = (CategoryTrack) panelView;
            if (mAdapter != null) {
                mAdapter.setOrientation(CategoryView.HORIZONTAL);
                panel.setAdapter(mAdapter);
                mAdapter.setContainer(panel);
            }
        } 
		return categoryView;
	}
	@Override
	public void onAttach(Activity activity) {
		loadAdapter(mCurrentAdapter);
		super.onAttach(activity);
	}
	public void loadAdapter(int adapter) {
        FilterShowActivity activity = (FilterShowActivity) getActivity();
        switch (adapter) {
            case LewaMainPanel.LOOKS: {
                mAdapter = activity.getCategoryLooksAdapter();
                if (mAdapter != null) {
                    mAdapter.initializeSelection(MainPanel.LOOKS);
                }
                activity.updateCategories();
                break;
            }
            case LewaMainPanel.BORDERS: {
                mAdapter = activity.getCategoryBordersAdapter();
                if (mAdapter != null) {
                    mAdapter.initializeSelection(MainPanel.BORDERS);
                }
                activity.updateCategories();
                break;
            }
        }
        
    }

}
