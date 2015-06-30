package com.android.gallery3d.v5.filtershow.util;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import android.util.Log;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.options.SerializeOptions;

/**
 * 
 * @author ycc
 *
 */
public class XmpUtilHelper {
	public static final String TAG = "XmpUtilHelper";
	public static int EXIF_MARK = 0xFFE1;
	public static int XMP_MARK = 0xFFE1;
	public static int JPG_MARK = 0xFFD8;
	public static String XMP_NAME_SPACE = "http://ns.adobe.com/xap/1.0/\0";
	public static XMPMeta extractXMPMeta(InputStream is) {
		if(is==null){
			return null;
		}
		try {
			if(readShort(is)!=JPG_MARK){
				 throw new IOException("this is not jpg file");
			}
			skipExifData(is);
			String xmpData = readXmpData(is);
			System.out.println(xmpData);
			return XMPMetaFactory.parseFromString(xmpData);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} catch (XMPException e) {
			Log.e(TAG, e.getMessage());
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        return null;
    }
	public static void skipExifData(InputStream is)throws IOException{
		if(readShort(is)!=EXIF_MARK){
			 throw new IOException("file header no Exif Mark");
		}
		int exifSize = readShort(is);
		if(exifSize<=0){
			 throw new IOException("exifSize <= 0");
		}
		is.skip(exifSize-2);
	}
	public static String readXmpData(InputStream is) throws IOException{
		if(readShort(is)!=XMP_MARK){
			throw new IOException("no XMP mark");
		}
		int xmpSize = 0;
		if((xmpSize = readShort(is))<=2){
			return null;
		}
		byte[] xmpNameSpaceBytes = new byte[29];
		is.read(xmpNameSpaceBytes);
		String xmpNameSpaceStr = new String(xmpNameSpaceBytes,Charset.forName("utf-8"));
		if(xmpNameSpaceStr==null||!XMP_NAME_SPACE.trim().equals(xmpNameSpaceStr.trim())){
			throw new IOException("xmp name space error");
		}
		byte[] xmpData = new byte[xmpSize-2-29];
		int len = is.read(xmpData);
		return new String(xmpData, 0, len,Charset.forName("UTF-8"));
		
	}
	public static boolean writeXMPMeta(String sourceFileName, Object meta) {
		return writeXMPMeta(sourceFileName,meta,null);
	}
    public static boolean writeXMPMeta(String sourceFileName, Object meta,String destFileName) {
    	File imageFile = new File(sourceFileName);
    	if(!imageFile.exists()||imageFile.isDirectory()){
    		return false;
    	}
    	File tempFile = null;
    	if(destFileName==null){
    		tempFile = new File(imageFile.getAbsolutePath()+".temp");
    	}else{
    		tempFile = new File(destFileName);
    	}
    	
    	OutputStream tempOut = null;
    	InputStream imageFileInput = null;
		try {
			tempOut = new FileOutputStream(tempFile);
			imageFileInput = new FileInputStream(imageFile);
		
	    	XMPMeta xmpMeta = (XMPMeta) meta;
	    	
	    	insertXmpMeta(imageFileInput,tempOut,xmpMeta);
	    	imageFile.delete();
	    	tempFile.renameTo(imageFile.getAbsoluteFile());
	    	return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (XMPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
			try {
				if(imageFileInput!=null){
					imageFileInput.close();
				}
				if(tempOut!=null){
					tempOut.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return false;
    }
    
    public static boolean insertXmpMeta(InputStream src,OutputStream dest,XMPMeta meta) throws IOException, XMPException{
    	byte[] exifData = readExif(src);
    	if(exifData==null||exifData.length<=0){
    		return false;
    	}
    	DataOutputStream destDataOutput = new DataOutputStream(dest);
    	dest.write(exifData);
    	
    	writeXmpMetaToOutput(dest,meta);
    	int mark ;
    	if((mark=readShort(src))!=XMP_MARK){
    		destDataOutput.writeShort(mark);
		}else{
			int xmpSize = 0;
			if((xmpSize = readShort(src))>=2){
				src.skip(xmpSize-2);
			}
		}
    	byte[] buff = new byte[1024];
    	int len;
    	while((len=src.read(buff))>=0){
    		destDataOutput.write(buff, 0, len);
    	}
    	return true;
    }
    
    public static void writeXmpMetaToOutput(OutputStream dest,XMPMeta meta)throws IOException, XMPException{
    	if(meta==null) return;
    	DataOutputStream dos = new DataOutputStream(dest);
    	dos.writeShort(XMP_MARK);
    	String metaStr = XMPMetaFactory.serializeToString(meta, new SerializeOptions(SerializeOptions.OMIT_PACKET_WRAPPER|SerializeOptions.ENCODE_UTF8));
    	byte[] metaBytes = (XMP_NAME_SPACE+metaStr).getBytes(Charset.forName("UTF-8"));
    	int metaSize = metaBytes.length+2;
    	dos.writeShort(metaSize);
    	dos.write(metaBytes);
    }
    
    public static byte[] readExif(InputStream src) throws IOException{
    	ByteArrayOutputStream headOuput = new ByteArrayOutputStream();
    	DataOutputStream dos = new DataOutputStream(headOuput);
    	int jpgMark = 0;
    	if((jpgMark=readShort(src))!=JPG_MARK){
			 throw new IOException("this is not jpg file");
		}
    	dos.writeShort(jpgMark);
    	int exifMark = 0;
		if((exifMark=readShort(src))!=EXIF_MARK){
			 throw new IOException("file head no Exif Mark");
		}
		dos.writeShort(exifMark);
		int exifSize = readShort(src);
		if(exifSize<=0){
			 throw new IOException("exifSize <= 0");
		}
		dos.writeShort(exifSize);
		byte[] exifData = new byte[exifSize-2];
		int len = src.read(exifData);
		if(len!=exifSize-2){
			throw new IOException("read exif data error");
		}
		dos.write(exifData);
		return headOuput.toByteArray();

    }
    
    public static int readShort(InputStream is) throws IOException{
    	int ch1 = is.read();
        int ch2 = is.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return ((ch1 << 8) + (ch2 << 0));
    }

	public static int bytesToInt(byte[] src, int offset) {
		int value;	
		value = (int) ((src[offset] & 0xFF) 
				| ((src[offset+1] & 0xFF)<<8) 
				| ((src[offset+2] & 0xFF)<<16) 
				| ((src[offset+3] & 0xFF)<<24));
		return value;
	}
	
	public static int bytesToShort(byte[] src, int offset) {
		int value;	
		value = (int) ((src[offset] & 0xFF) 
				| ((src[offset+1] & 0xFF)<<8));
		return value;
	}

	public static int bytesToInt2(byte[] src, int offset) {
		int value;	
		value = (int) ( ((src[offset] & 0xFF)<<24)
				|((src[offset+1] & 0xFF)<<16)
				|((src[offset+2] & 0xFF)<<8)
				|(src[offset+3] & 0xFF));
		return value;
	}
    
}
