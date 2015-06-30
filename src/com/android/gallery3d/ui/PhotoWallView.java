package com.android.gallery3d.ui;

import java.util.ArrayList;
import java.util.Random;

import com.android.gallery3d.app.AbstractGalleryActivity;
import com.android.gallery3d.glrenderer.GLCanvas;
import com.android.gallery3d.glrenderer.GLPaint;
import com.android.gallery3d.ui.SlotView.Listener;
import com.android.gallery3d.ui.SlotView.SlotAnimation;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class PhotoWallView extends GLView {
	public static final String TAG = "PhotoWallView";
	
    public static final int OVERSCROLL_3D = 0;
    public static final int OVERSCROLL_SYSTEM = 1;
    public static final int OVERSCROLL_NONE = 2;
    
    private static final int INDEX_NONE = -1;
	
	PhotoWallAdapter mPhotoAdatper;
	public final Layout mLayout = new Layout();
	private final GestureDetector mGestureDetector;
	private final ScrollerHelper mScroller;
	
    private final Paper mPaper = new Paper();

    private Listener mListener;
    private UserInteractionListener mUIListener;

    private boolean mMoreAnimation = false;
    private SlotAnimation mAnimation = null;
    private int mStartIndex = INDEX_NONE;

    // whether the down action happened while the view is scrolling.
    private boolean mDownInScrolling;
    private int mOverscrollEffect = OVERSCROLL_3D;
    //private final Handler mHandler;

	public PhotoWallView(AbstractGalleryActivity activity) {
		mScroller = new ScrollerHelper(activity);
		mGestureDetector = new GestureDetector(activity,
				new PhotoWallGestureListener());
		mPhotoAdatper = new PhotoWallAdapter() {
			int[] slots = { 6, 3, 5, 8, 4, 7, 10  };

			@Override
			public int getSlotCount(int position) {
				return slots[position];
			}

			@Override
			public int getCount() {
				return slots.length;
			}
		};
	}

	private class PhotoWallGestureListener implements
			GestureDetector.OnGestureListener {

		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			int scrollLimit = mLayout.getScrollLimit();
			if (scrollLimit == 0)
				return false;
			mScroller.fling((int) -velocityY, 0, scrollLimit);
			if (mUIListener != null) mUIListener.onUserInteractionBegin();
			Log.d(TAG, "onFling");
			invalidate();
			return true;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			mScroller.startScroll(Math.round(distanceY), 0,
					mLayout.getScrollLimit());
			Log.d(TAG, "onScroll="+Math.round(distanceY));
			invalidate();
			return true;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	@Override
	protected boolean onTouch(MotionEvent event) {
		if (mUIListener != null) mUIListener.onUserInteraction();
		mGestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			 mDownInScrolling = !mScroller.isFinished();
			mScroller.forceFinished();
			break;
		case MotionEvent.ACTION_UP:
			// mPaper.onRelease();
			invalidate();
			break;
		}
		return true;
	}
	private void updateScrollPosition(int position, boolean force) {
        if (!force &&  position == mScrollY) return;
        mScrollY = position;
       
    }
	@Override
	protected void render(GLCanvas canvas) {
		super.render(canvas);
		long animTime = AnimationTime.get();
        boolean more = mScroller.advanceAnimation(animTime);
		//Log.d(TAG, "mScroller.getPosition()="+mScroller.getPosition());
		updateScrollPosition(mScroller.getPosition(), false);
		canvas.translate(-mScrollX, -mScrollY);
		for (int i = 0; i < mPhotoAdatper.getCount(); i++) {
			
			Rect rect = mLayout.getItemRect(i);
			if(checkPass(rect)) continue;
			canvas.save(GLCanvas.SAVE_FLAG_ALPHA | GLCanvas.SAVE_FLAG_MATRIX);
			canvas.translate(rect.left, rect.top, 0);
			renderTitle(canvas);
			canvas.restore();

			canvas.save(GLCanvas.SAVE_FLAG_ALPHA | GLCanvas.SAVE_FLAG_MATRIX);
			canvas.translate(rect.left,
					rect.top + mLayout.getItemTitleHeight(), 0);
			renderSlot(canvas, mPhotoAdatper.getSlotCount(i));
			canvas.restore();
		}
		canvas.translate(mScrollX, mScrollY);
		 if (more) invalidate();
	}

	public boolean checkPass(Rect rect){
		Log.d(TAG, rect.toString());
		int mButtomScrollY = mScrollY +1920;
		if(rect.top>=mScrollY&&(rect.top+rect.bottom)<=mButtomScrollY){
			return false;
		}
		if(rect.top<=mScrollY&&rect.top+rect.bottom<=mButtomScrollY){
			return false;
		}
		return true;
		
	}
	
	protected void renderTitle(GLCanvas canvas) {
		canvas.fillRect(0, 0, 1080, mLayout.getItemTitleHeight(),
				getRandomColor());
	}

	protected void renderSlot(GLCanvas canvas, int slotCount) {
		int row = (slotCount % 4) == 0 ? (slotCount / 4)
				: ((slotCount / 4) + 1);
		int drewCount = 0;
		int x = 0;
		int y = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < 4; j++) {
				if (drewCount++ >= slotCount) {
					break;
				}
				canvas.save(GLCanvas.SAVE_FLAG_ALPHA
						| GLCanvas.SAVE_FLAG_MATRIX);
				canvas.translate(x, y);
				canvas.fillRect(5, 5, 265, 265, Color.BLUE);
				canvas.restore();
				x += 270;
			}
			x = 0;
			y += 270;
		}
	}

	public int getRandomColor() {
		Random random = new Random();
		return Color.rgb(random.nextInt(256), random.nextInt(256),
				random.nextInt(256));
	}

	protected void renderItemTitle(Rect rect, int position) {

	}

	protected void renderItemContent() {

	}

	class Spec {

	}

	@Override
	protected void onLayout(boolean changeSize, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changeSize, left, top, right, bottom);
	}

	class Layout {
		private int mSlotWidth;
		private int mSlotHeight;
		private int mSlotGap;
		ArrayList<Rect> rects = new ArrayList<Rect>();
		private int mItemTitleHeight = 150;
		int totalHeight ;

		public Rect getItemRect(int position) {
			if (rects.size() <= position) {
				measure();
			}

			return rects.get(position);
		}

		public int getScrollLimit() {
			return totalHeight-1920;
		}

		public int getItemTitleHeight() {
			return mItemTitleHeight;
		}

		public void measure() {
			if (rects.size() > 0) {
				rects.clear();
			}
			totalHeight = 0;
			
			for (int i = 0; i < mPhotoAdatper.getCount(); i++) {
				int slotCount = mPhotoAdatper.getSlotCount(i);
				int row = (slotCount % 4) == 0 ? (slotCount / 4)
						: ((slotCount / 4) + 1);
				int height = row * 270;
				Rect rect = new Rect(0, totalHeight, 4 * 270, height
						+ mItemTitleHeight);
				rects.add(rect);
				totalHeight += (height + mItemTitleHeight);
			}
		}
	}

	interface PhotoWallAdapter {
		public int getCount();

		public int getSlotCount(int position);
	}
}
