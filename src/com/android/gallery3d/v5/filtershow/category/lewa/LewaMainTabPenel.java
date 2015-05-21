package com.android.gallery3d.v5.filtershow.category.lewa;

import com.test.filter.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LewaMainTabPenel extends Fragment{
	 public static final String FRAGMENT_TAG = "LewaMainTabPenel";

    interface OnTabClickListener {
    	void onClick(View view);
    }
    OnTabClickListener tabClickListener;
    public void setTabClickListener(OnTabClickListener tabClickListener) {
		this.tabClickListener = tabClickListener;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View tabPanel = inflater.inflate(R.layout.layout_main_tab, null);
    	View.OnClickListener listener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(tabClickListener!=null) tabClickListener.onClick(v);
			}
		};
    	tabPanel.findViewById(R.id.tab_card_filter).setOnClickListener(listener);
    	tabPanel.findViewById(R.id.tab_card_border).setOnClickListener(listener);
    	tabPanel.findViewById(R.id.tab_card_crop).setOnClickListener(listener);
    	tabPanel.findViewById(R.id.tab_card_curve).setOnClickListener(listener);
    	tabPanel.findViewById(R.id.tab_card_rotation).setOnClickListener(listener);
    	tabPanel.findViewById(R.id.tab_card_adjust).setOnClickListener(listener);
    	return tabPanel;
    }
	
}
