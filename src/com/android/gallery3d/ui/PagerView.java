package com.android.gallery3d.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.android.gallery3d.anim.Animation;
import com.android.gallery3d.app.AbstractGalleryActivity;
import com.android.gallery3d.common.Scroller;
import com.android.gallery3d.glrenderer.GLCanvas;
import com.android.gallery3d.glrenderer.RawTexture;

public class PagerView extends GLView {
	public static final String TAG = "PagerView";

	private static final int MIN_FLING_VELOCITY = 400; // dips
	
	int index = -1;

	private static final Interpolator sInterpolator = new Interpolator() {
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t * t * t + 1.0f;
		}
	};

	DecelerateInterpolator ploator = new DecelerateInterpolator();

	GestureDetector mGestureDetector;

	AbstractGalleryActivity mContext;

	PagerContainer container ;
	
	Animation anim ;
	
	PageChangeListener pageChangeListener ;
	
	


	public void setPageChangeListener(PageChangeListener pageChangeListener) {
		this.pageChangeListener = pageChangeListener;
	}

	public PagerView(AbstractGalleryActivity activity) {
		mContext = activity;
		initViewPager();
	}

	void initViewPager() {
		container = new PagerContainer(mContext);
		mGestureDetector = new GestureDetector(mContext,
				container);
		

	}

	@Override
	protected boolean onTouch(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		if(event.getAction()==MotionEvent.ACTION_UP){
			Log.d(TAG, "touch up ACTION_UP");
			container.onTouchUp();
			startCheck = false;
		}
		return true;
	}

	public void addGLView(GLView view) {
		container.addView(view);
	}

	@Override
	protected void render(GLCanvas canvas) {
		super.render(canvas);
			if (anim!=null&&anim.calculate(AnimationTime.get())) {
				invalidate();
			}
			if(index==-1){
				container.renderTarget(canvas);
				canvas.save();
				canvas.translate(0, 120);
				container.draw(canvas);
				canvas.restore();
			}else{
				canvas.save();
				canvas.translate(0, 120);
				container.views.get(index).view.render(canvas);
				canvas.restore();
			}
			
			
			
	}

	boolean startCheck;
	boolean checkResult;

	class PagerContainer   implements
			GestureDetector.OnGestureListener {
		
		public static final int SCROLL_LEFT = 0;
		public static final int SCROLL_RIGHT = 0;

		ArrayList<ViewEntry> views = new ArrayList<ViewEntry>();

		Scroller mScroller;
		
		int mScrollX;

		int gapWidth = 10;

		int itemWidth = 1080;

		int mCurrentIndex;

		int mScrollDirection;

		float deltaX = 0;
		
		

		int mFrom;

		public PagerContainer(Context context){
			mScroller = new Scroller(context);
		}
		
		
		private int getPageX(int pageIndex) {
			return 0;
		}

		public void scrollToNextPage(final boolean fling) {
			mFrom = mScrollX ;
			anim = new Animation() {
				
				@Override
				protected void onCalculate(float progress) {
					Log.d(TAG, "progress="+progress);
					if(fling){
						if(mScrollX<itemWidth){
							mScrollX = (int) (mFrom + (itemWidth+gapWidth - mFrom) * progress);
							
						}else{
							
							mScrollX = (int) (mFrom + (0 - mFrom) * progress);
							
						}
					}else{
						if(mScrollX>itemWidth/2){
							mScrollX = (int) (mFrom + (itemWidth+gapWidth - mFrom) * progress);
						}else{
							mScrollX = (int) (mFrom + (0 - mFrom) * progress);
						}
					}
					
					if(progress>=1){
						
						if(mScrollX<=1080){
							index = 0;
						}else{
							index = 1;
						}
						//addComponent(views.get(index).view);
						invalidate();
						//pageChangeListener.onPageChanged(index);
					}
					
				}
			};
			anim.setDuration(200);
			anim.start();
			invalidate();
		}

		public int getScrollX() {
			return mScrollX;
		}

		public void addView(GLView view) {
			if (view == null)
				return;
			for (ViewEntry entry : views) {
				if (entry.view == view) {
					return;
				}
			}
			ViewEntry entry = new ViewEntry(view);
			//entry.prepareTexture();
			views.add(entry);
		}

		public void renderTarget(GLCanvas canvas) {
			for (int i = 0; i < views.size(); i++) {
				ViewEntry entry = views.get(i);
//				if (i == mCurrentIndex
//						|| (mScrollDirection == SCROLL_LEFT && i == mCurrentIndex - 1)
//						|| (mScrollDirection == SCROLL_RIGHT && i == mCurrentIndex + 1)) {
					if (entry.texture == null) {
//						throw new NullPointerException(
//								"Page Texture must be prepared");
						entry.prepareTexture();
					}
					GLView v = entry.view;
					RawTexture t = entry.texture;
					if (v == null || t == null) {
						continue;
					}
					canvas.beginRenderTarget(t);
					v.render(canvas);
					canvas.endRenderTarget();
				//}

			}
		}

		public void draw(GLCanvas canvas) {
			int tempX = (int) (mScrollX + deltaX);
			if(tempX<=0){
				tempX = 0;
				mScrollX = 0;
			}
			
			canvas.save();
			int dx =  tempX % (itemWidth + gapWidth);
			int stepCount = tempX / (itemWidth + gapWidth);
			canvas.translate(-tempX, 0);
			for (int i = 0; i < views.size(); i++) {
				ViewEntry entry = views.get(i);
				//if (i < stepCount - 1 && i > stepCount) {
					entry.texture.draw(canvas, 0, 0);
					canvas.translate(itemWidth + gapWidth, 0);
				//}
			}
			canvas.restore();
			

		}
		
		public void onTouchUp(){
			mScrollX += deltaX;
			deltaX = 0;
			scrollToNextPage(false);
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			startCheck = true;
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if(startCheck){
				double dx = Math.abs(e2.getX()-e1.getX());
				double dy = Math.abs(e2.getY()-e1.getY());
				if((dx/dy) >= Math.tan(30 * (Math.PI/180))){
					checkResult = true;
				}else{
					checkResult = false;
				}
				startCheck = false;
			}
			if(!checkResult) return true;
			deltaX = e1.getX() - e2.getX();
			invalidate();
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			//scrollToNextPage(true);
			return true;
		}



		
	}

	class ViewEntry {
		GLView view;
		RawTexture texture;
		int x;
		int gap = 200;

		public ViewEntry(GLView view) {
			this.view = view;
		}

		public void prepareTexture() {

			if (view == null)
				throw new NullPointerException(
						"prepareTexture error because view is null");

			if (view.getWidth() <= 0 || view.getHeight() <= 0)
				throw new RuntimeException(
						"prepareTexture error view width or height <= 0");

			if (texture != null && texture.getWidth() == view.getWidth()
					&& texture.getHeight() == view.getHeight()) {
				return;
			}

			if (texture != null)
				texture.recycle();

			texture = new RawTexture(view.getWidth(), view.getHeight(), true);

		}

		public void recycle() {
			view = null;
			if (texture != null) {
				texture.recycle();
			}
		}
	}
	
	public interface PageChangeListener{
		public void onPageChanged(int index);
	}
}
