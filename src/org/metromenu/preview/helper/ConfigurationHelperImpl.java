package org.metromenu.preview.helper;

public class ConfigurationHelperImpl implements ConfigurationHelper {

	private boolean mEditMode;
	private boolean mResortMode;
	
	public ConfigurationHelperImpl() {
		mEditMode = false;
	}

	@Override
	public void setResizableMode(boolean edit) {
		mEditMode = edit;
	}

	@Override
	public boolean getEditMode() {
		return mEditMode;
	}

	@Override
	public void setResortableMode(boolean resort) {
		mResortMode = resort;
	}

	@Override
	public boolean getResortMode() {
		return mResortMode;
	}
}
