package com.android.gallery3d.v5.filtershow.util;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.MeasureSpec;

public class ViewUtil {
	 public static int getTargetHeight(View v) {
			
			try {
				Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
						int.class);
				m.setAccessible(true);
				m.invoke(v, MeasureSpec.makeMeasureSpec(
						((View) v.getParent()).getMeasuredWidth(),
						MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0,
						MeasureSpec.UNSPECIFIED));
			} catch (Exception e) {
				
			}
			return v.getMeasuredHeight();
	 }
	 public static int getTargetWidth(View v) {
			
			try {
				Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
						int.class);
				m.setAccessible(true);
				m.invoke(v, MeasureSpec.makeMeasureSpec(
						((View) v.getParent()).getMeasuredWidth(),
						MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0,
						MeasureSpec.UNSPECIFIED));
			} catch (Exception e) {
				
			}
			return v.getMeasuredWidth();
	 }
}
