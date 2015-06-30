package com.android.gallery3d.app;

import java.util.ArrayList;

import com.android.gallery3d.anim.StateTransitionAnimation;
import com.android.gallery3d.glrenderer.GLCanvas;
import com.android.gallery3d.glrenderer.GLPaint;
import com.android.gallery3d.glrenderer.RawTexture;
import com.android.gallery3d.glrenderer.StringTexture;
import com.android.gallery3d.ui.GLRoot;
import com.android.gallery3d.ui.GLView;
import com.android.gallery3d.ui.PagerView;
import com.android.gallery3d.ui.PhotoWallView;
import com.android.gallery3d.ui.PreparePageFadeoutTexture;
import com.android.gallery3d.util.UsageStatistics;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

public class PhotoWall extends ActivityState implements PagerView.PageChangeListener {
	public static final String TAG = "PhotoWall";
	public static final String ALBUM_SET_PATH = "/combo/{/local/all,/picasa/all}";
	public static final String DCIM_PATH = "/local/all/-1739773001";
	PagerView page;
	StateWallper stateWallper = new StateWallper();
	PhotoWallView photoWall;
	@Override
	protected void onCreate(Bundle data, Bundle storedState) {
		super.onCreate(data, storedState);
		photoWall = new PhotoWallView(mActivity);
		photoWall.layout(0,120, 1080, 1920);
		setContentPane(photoWall);
//		try {
//			initStates();
//			stateWallper.onCreate(data, storedState);
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		page = new PagerView(mActivity);
//		for (int i = 0; i < stateWallper.size(); i++) {
//			GLView v = stateWallper.getState(i).getContentPane();
//			v.layout(0, 120, 1080, 1920);
//			page.addGLView(v);
//		}
//		page.layout(0, 0, 1080, 1920);
//		page.setPageChangeListener(this);
//		
//		setContentPane(page);
	}

	@Override
	protected void onPause() {
		stateWallper.onPause();
		super.onPause();
		// if(page!=null) page.destory();
	}

	@Override
	void resume() {
		stateWallper.resume();
		super.resume();
	}

	@Override
	protected void onResume() {
		stateWallper.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		stateWallper.onDestroy();
		super.onDestroy();
	}

	public void initStates() throws InstantiationException,
			IllegalAccessException {
		ActivityState albumState = AlbumPage.class.newInstance();
		Bundle b2 = new Bundle();
		b2.putString(AlbumPage.KEY_MEDIA_PATH, DCIM_PATH);
		albumState.initialize(mActivity, b2);
		albumState.onCreate(b2, null);
		albumState.onResume();
		stateWallper.addState(albumState);

		Bundle data = new Bundle();
		data.putString(AlbumSetPage.KEY_MEDIA_PATH, ALBUM_SET_PATH);
		ActivityState albumSetState = AlbumSetPage.class.newInstance();
		albumSetState.initialize(mActivity, data);
		albumSetState.onCreate(data, null);
		albumSetState.onResume();
		stateWallper.addState(albumSetState);

	}

	class StateWallper {
		ArrayList<ActivityState> states = new ArrayList<ActivityState>();
		
		
		public void addState(ActivityState state) {
			for (ActivityState s : states) {
				if (s == state)
					return;
			}
			states.add(state);
		}

		public void clear() {
			states.clear();
		}

		public int size() {
			return states.size();
		}

		public ActivityState getState(int index) {
			if(index>=states.size()){
				return null;
			}
			return states.get(index);
		}

		protected void onCreate(Bundle data, Bundle storedState) {

		}

		void initialize(AbstractGalleryActivity activity, Bundle data) {

		}

		protected void onPause() {
			for (ActivityState s : states) {
				s.onPause();
			}
			Log.d(TAG, "onPause...");
		}

		void resume() {
			for (ActivityState s : states) {
				s.resume();
			}
			Log.d(TAG, "resume...");
		}

		protected void onResume() {
			for (ActivityState s : states) {
				s.onResume();
			}
			Log.d(TAG, "onResume...");
		}

		protected void onDestroy() {
			for (ActivityState s : states) {
				s.onDestroy();
			}
			Log.d(TAG, "onDestroy...");
		}

		protected void onSaveState(Bundle outState) {

		}

	}

	@Override
	public void onPageChanged(int index) {
		ActivityState s = stateWallper.getState(index);
		if(s!=null){
			//s.onResume();
			//mActivity.getGLRoot().setContentPane(s.getContentPane());
			//mActivity.getGLRoot().requestRender();
		}
	}

}
