package com.android.gallery3d.v5.filtershow.imageshow;

import com.android.gallery3d.v5.filtershow.editors.EditorRotate;
import com.android.gallery3d.v5.filtershow.filters.FilterMirrorRepresentation;
import com.android.gallery3d.v5.filtershow.filters.FilterRepresentation;
import com.android.gallery3d.v5.filtershow.filters.FilterRotateRepresentation;
import com.android.gallery3d.v5.filtershow.filters.LewaFilterRotateRepresentation;
import com.android.gallery3d.v5.filtershow.imageshow.GeometryMathUtils.GeometryHolder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class LewaImageRotate extends ImageShow {
	EditorRotate editorRotate;
	LewaFilterRotateRepresentation mRotateRep = LewaFilterRotateRepresentation.create();
	private GeometryHolder mDrawHolder = new GeometryHolder();
	public LewaImageRotate(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public LewaImageRotate(Context context) {
		super(context);
	}
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Treat event as handled.
        return true;
    }

    public void applyCurrentRep(){
//    	if(mRotateRep!=null&&
//    			mRotateRep.getRotateRepresentation()!=null){
//    		mRotateRep.getRotateRepresentation().rotateCW();
//    	}
//    	if(mRotationRep !=null){
//    		mRotationRep.rotateCW();
//    	}
//    	if(mMirrorRep !=null){
//    		mMirrorRep.cycle();
//    	}
    	invalidate();
    }
    public void setRotateRepresentation(LewaFilterRotateRepresentation rep){
    	mRotateRep = rep==null ? LewaFilterRotateRepresentation.create() : rep;
    }
	@Override
    public void onDraw(Canvas canvas) {
		
//		drawRepresentation(canvas, mRotationRep);
//		drawRepresentation(canvas,mMirrorRep);
		 MasterImage master = MasterImage.getImage();
	        Bitmap image = master.getFiltersOnlyImage();
	        if (image == null) {
	            return;
	        }
		    GeometryMathUtils.initializeHolder(mDrawHolder,mRotateRep);
	        GeometryMathUtils.drawTransformedCropped(mDrawHolder, canvas, image, canvas.getWidth(),
	                canvas.getHeight());
    }
	private void drawRepresentation(Canvas canvas,FilterRepresentation rep){
		
	}
	public void setEditor(EditorRotate editorRotate) {
		this.editorRotate = editorRotate;
	}
	public FilterRepresentation getFinalRepresentation() {
		return mRotateRep;
	}

}
