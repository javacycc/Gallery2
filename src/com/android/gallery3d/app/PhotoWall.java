package com.android.gallery3d.app;

import com.android.gallery3d.anim.StateTransitionAnimation;
import com.android.gallery3d.glrenderer.GLCanvas;
import com.android.gallery3d.glrenderer.GLPaint;
import com.android.gallery3d.glrenderer.RawTexture;
import com.android.gallery3d.glrenderer.StringTexture;
import com.android.gallery3d.ui.GLRoot;
import com.android.gallery3d.ui.GLView;
import com.android.gallery3d.ui.PhotoWallView;
import com.android.gallery3d.ui.PreparePageFadeoutTexture;
import com.android.gallery3d.util.UsageStatistics;

import android.graphics.Color;
import android.os.Bundle;

public class PhotoWall extends ActivityState  {

	PhotoWallView photoWall;
	RawTexture texture ;
	GLView g;
	ActivityState state = null;
	@Override
	protected void onCreate(Bundle data, Bundle storedState) {
		super.onCreate(data, storedState);
		photoWall = new PhotoWallView(mActivity);
		photoWall.setBackgroundColor(new float[]{0,0,0,0});
		photoWall.layout(0, 0, 1080, 1080);
		data.putString(AlbumSetPage.KEY_MEDIA_PATH, "/combo/{/local/all,/picasa/all}");
        try {
            state = AlbumSetPage.class.newInstance();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        
        state.initialize(mActivity, data);

        state.onCreate(data, null);
        state.onResume();
		
		new Thread(){
			@Override
			public void run() {
				
				
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					GLView gg = state.getContentPane();
					gg.layout(0, 0, 1080, 1080);
					PreparePageFadeoutTexture task = new PreparePageFadeoutTexture(gg);
				    if (task.isCancelled()) return;
				    GLRoot root = mActivity.getGLRoot();
				    root.addOnGLIdleListener(task);
				    texture = task.get();
				    g.invalidate();
				    
				}
				
			}
		}.start();;
		//setContentPane(photoWall);
		g = new GLView(){
			@Override
			protected void render(GLCanvas canvas) {

				canvas.fillRect(0, 0, 1080, 1920, Color.WHITE);
				GLPaint p = new GLPaint();
				p.setColor(Color.BLUE);
				canvas.drawRect(0f, 0f, 200f, 200f, p);
				StringTexture st = StringTexture.newInstance("杨春成", 100, Color.RED);
			
				canvas.drawTexture(st, 0, 300, st.getWidth(), st.getHeight());
				if(texture!=null){
					canvas.drawTexture(texture, 0, 500, 1080, 1080);
				}
			}
		};
		g.setBackgroundColor(new float[]{255,255,255,255});
		setContentPane(g);
	}
	
	
	
	
}
