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

package com.android.gallery3d.v5.filtershow.category;

import java.lang.reflect.Method;

import com.android.gallery3d.v5.filtershow.util.ViewUtil;
import com.test.filter.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CategoryTrack extends LinearLayout {
	public static final String TAG = "CategoryTrack";
    private CategoryAdapter mAdapter;
    private int mElemSize;
    private View mSelectedView;
    private float mStartTouchY;
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            if (getChildCount() != mAdapter.getCount()) {
                fillContent();
            } else {
                invalidate();
            }
        }
        @Override
        public void onInvalidated() {
            super.onInvalidated();
            fillContent();
        }
    };

    public CategoryTrack(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CategoryTrack);
        mElemSize = a.getDimensionPixelSize(R.styleable.CategoryTrack_iconSize, 0);
    }

    public void setAdapter(CategoryAdapter adapter) {
        mAdapter = adapter;
       
       try{
        mAdapter.registerDataSetObserver(mDataSetObserver);
       }catch(IllegalStateException e){
    	   Log.e(TAG, e.getMessage());
       }
        fillContent();
    }
    static int color = Color.RED;
    public void fillContent() {
        removeAllViews();
        mAdapter.setItemWidth(mElemSize);
        mAdapter.setItemHeight(LayoutParams.MATCH_PARENT);
        int n = mAdapter.getCount();
        for (int i = 0; i < n; i++) {
            View view = mAdapter.getView(i, null, this);
            CategoryView cv = (CategoryView) view;
            if(i!=0){
	            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT); 
	            lp.leftMargin = 10;
	            addView(cv, i, lp);
            }else{
            	addView(cv,i);
            }
        }
        requestLayout();
    }
   
    @Override
    public void invalidate() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = getChildAt(i);
            child.invalidate();
        }
    }

}
