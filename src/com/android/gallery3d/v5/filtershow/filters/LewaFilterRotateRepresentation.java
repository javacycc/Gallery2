package com.android.gallery3d.v5.filtershow.filters;

import java.io.IOException;

import android.util.JsonReader;
import android.util.JsonWriter;

import com.android.gallery3d.v5.filtershow.editors.EditorRotate;
import com.test.filter.R;

public class LewaFilterRotateRepresentation extends FilterRepresentation {
	
	FilterRotateRepresentation mRotateRepresentation;
	FilterMirrorRepresentation mMirrorRepresentation;
	public static final String NAME_LEWA_ROTATE = "LEWA_ROTATE";
	public static final String SERIALIZATION_NAME = "LEWA_ROTATION";

	public LewaFilterRotateRepresentation(FilterRotateRepresentation rotateRep,
			FilterMirrorRepresentation mirrorRep) {
		super(NAME_LEWA_ROTATE);
		this.mRotateRepresentation = rotateRep;
		this.mMirrorRepresentation = mirrorRep;
		setSerializationName(SERIALIZATION_NAME);
		setFilterType(FilterRepresentation.TYPE_GEOMETRY);
		setShowParameterValue(false);
		setSupportsPartialRendering(true);
		setTextId(R.string.rotate);
		setEditorId(EditorRotate.ID);
	}
	public static LewaFilterRotateRepresentation create(){
		return new LewaFilterRotateRepresentation(new FilterRotateRepresentation(),new FilterMirrorRepresentation());
	}
	@Override
	public boolean equals(FilterRepresentation representation) {
		if (!(representation instanceof LewaFilterRotateRepresentation))
			return false;
		LewaFilterRotateRepresentation rep = (LewaFilterRotateRepresentation) representation;
		if (mRotateRepresentation != null
				&& mRotateRepresentation.equals(rep.getRotateRepresentation())
				&& mMirrorRepresentation != null
				&& mMirrorRepresentation.equals(rep.getMirrorRepresentation())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean allowsSingleInstanceOnly() {
		return true;
	}

	@Override
	public FilterRepresentation copy() {
		return new LewaFilterRotateRepresentation(
				((FilterRotateRepresentation) mRotateRepresentation.copy()),
				((FilterMirrorRepresentation) mMirrorRepresentation.copy()));
	}

	@Override
	protected void copyAllParameters(FilterRepresentation representation) {
		if (!(representation instanceof LewaFilterRotateRepresentation)) {
			throw new IllegalArgumentException(
					"calling copyAllParameters with incompatible types!");
		}
		super.copyAllParameters(representation);
		representation.useParametersFrom(this);
	}

	@Override
	public void useParametersFrom(FilterRepresentation a) {
		if (!(a instanceof LewaFilterRotateRepresentation)) {
			throw new IllegalArgumentException(
					"calling useParametersFrom with incompatible types!");
		}
		LewaFilterRotateRepresentation rep = (LewaFilterRotateRepresentation)a;
		if(mRotateRepresentation!=null){
			mRotateRepresentation.useParametersFrom(rep.mRotateRepresentation);
		}
		if(mMirrorRepresentation!=null){
			mMirrorRepresentation.useParametersFrom(rep.mMirrorRepresentation);
		}
	}
	
	@Override
	public void serializeRepresentation(JsonWriter writer) throws IOException {
		if(mMirrorRepresentation!=null){
			mMirrorRepresentation.serializeRepresentation(writer);
		}
		if(mRotateRepresentation!=null){
			mRotateRepresentation.serializeRepresentation(writer);
		}
	}
	
	@Override
	public void deSerializeRepresentation(JsonReader reader) throws IOException {
		if(mMirrorRepresentation!=null){
			mMirrorRepresentation.deSerializeRepresentation(reader);
		}
		if(mRotateRepresentation!=null){
			mRotateRepresentation.deSerializeRepresentation(reader);
		}
	}

	public FilterRotateRepresentation getRotateRepresentation() {
		return mRotateRepresentation;
	}

	public void setRotateRepresentation(
			FilterRotateRepresentation mRotateRepresentation) {
		this.mRotateRepresentation = mRotateRepresentation;
	}

	public FilterMirrorRepresentation getMirrorRepresentation() {
		return mMirrorRepresentation;
	}

	public void setMirrorRepresentation(
			FilterMirrorRepresentation mMirrorRepresentation) {
		this.mMirrorRepresentation = mMirrorRepresentation;
	}
	
	@Override
	public String toString() {
		
		return "mirror="+mMirrorRepresentation.getMirror()+"   rotate="+mRotateRepresentation.getRotation();
	}
}
