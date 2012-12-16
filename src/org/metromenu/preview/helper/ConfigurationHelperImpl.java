package org.metromenu.preview.helper;

public class ConfigurationHelperImpl implements ConfigurationHelper {

	private boolean mEditMode;
	
	public ConfigurationHelperImpl() {
		mEditMode = false;
	}

	@Override
	public void setEditMode(boolean edit) {
		mEditMode = edit;
	}

	@Override
	public boolean getEditMode() {
		return mEditMode;
	}

}
