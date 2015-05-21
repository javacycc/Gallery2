package com.test.filter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.android.gallery3d.v5.filtershow.FilterShowActivity;
import com.test.filter.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.util.LruCache;
import android.support.v4.widget.CursorAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


public class MainActivity extends Activity {
	GridView photoGrid ;
	 Cursor cursor ;
	 DisplayMetrics dm = new DisplayMetrics();
	 LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(30*1024*1024);
	 ArrayList<String> imagePaths = new ArrayList<String>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getResources().getIdentifier("convolve3x3", defType, defPackage)
       System.loadLibrary("jni_filter_eglfence");

        //Bitmap b = BitmapFactory.decodeResource(this.getResources(),R.drawable.filtershow_border_black);
        //Log.i("main", b.getWidth()+"");
    }
    @Override
    protected void onStart() {
    	 File dcimDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera");
         if(!dcimDir.exists()||!dcimDir.isDirectory()){
         	throw new RuntimeException("bad dir");
         }
         imagePaths.clear();
         cache.evictAll();
         Uri mUri = Uri.parse("content://media/external/images/media");
         Uri mImageUri = null;
         Cursor cursor = managedQuery(
                                         MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                                         MediaStore.Images.Media.DEFAULT_SORT_ORDER);
         cursor.moveToFirst();

         while (!cursor.isAfterLast()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
               
               if(data.contains("/DCIM/Camera/")){
            	   imagePaths.add("content://media/external/images/media/"+id);
               }
               cursor.moveToNext();
         }
//    	 File[] fs = dcimDir.listFiles();
//         imagePaths = new ArrayList<String>();
//         for(File f:fs){
//         	if(f.isFile()){
//         		imagePaths.add(f.getAbsolutePath());
//         	}
//         }
         photoGrid = (GridView) findViewById(R.id.gridView1);
         photoGrid.setAdapter(new PhotoAdapter());
         photoGrid.setOnItemClickListener(new OnItemClickListener() {

 			@Override
 			public void onItemClick(AdapterView<?> parent, View view,
 					int position, long id) {
 		        Intent intent = new Intent(MainActivity.this,FilterShowActivity.class);
 		        intent.setAction("android.intent.action.EDIT");
 		        intent.setData(Uri.parse(imagePaths.get(position)));
 		        startActivity(intent);
 			}
 		});
    	super.onStart();
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    }
    class PhotoAdapter extends BaseAdapter{
    	
    	public PhotoAdapter(){
    	}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imagePaths.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return imagePaths.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView iv= new ImageView(MainActivity.this);
			iv.setScaleType(ScaleType.CENTER_CROP);
			Bitmap bitmap = null;
			try {
				bitmap = decodeBitmap(imagePaths.get(position));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			iv.setImageBitmap(bitmap);
			return iv;
		}
    	
	
    	
    }
    /** 
     * ��path�л�ȡͼƬ��Ϣ 
     * @param path 
     * @return 
     */  
    private Bitmap decodeBitmap(String path) throws IOException{  
    	Bitmap b = cache.get(path);
    	if(b!=null){
    		Log.d("main", "get cache bitmap name="+path);
    		return b;
    	}else{
    		
	        BitmapFactory.Options op = new BitmapFactory.Options();  
	        op.inJustDecodeBounds = true;  
	        InputStream in = null;
	        InputStream in2 = null;
	       try{
	    	   in = getContentResolver().openInputStream(Uri.parse(path));
	    	  
		        Bitmap bmp = BitmapFactory.decodeStream(in,null, op); //��ȡ�ߴ���Ϣ  
		       if(op.outHeight<300||op.outWidth<300){
		    	   op.inSampleSize = 1;
		       }else{
			        if(op.outWidth>op.outHeight){
			        	op.inSampleSize = op.outWidth/300;
			        }else{
			        	op.inSampleSize = op.outHeight/300;
			        }
		       }
		        op.inJustDecodeBounds = false;
		        in2 = getContentResolver().openInputStream(Uri.parse(path));
		        bmp = BitmapFactory.decodeStream(in2, null, op);
		        Log.d("main", "put cache bitmap name="+path);
		        cache.put(path, bmp);
		        return bmp;
    		}finally{
    			if(in!=null) in.close();
    			if(in2!=null) in2.close();
    		}
	        
    	}
    }  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
