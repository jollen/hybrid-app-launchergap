package org.metromenu.preview.helper;

public interface ConfigurationHelper {

	/**
	 * MetroMenu goes to edit mode.
	 * @param edit true when going edit mode
	 */
	void setResizableMode(boolean edit);
	
	boolean getEditMode();
	
	/**
	 * MetroMenu goes to resorting mode.
	 * @param edit true when going edit mode
	 */	
	void setResortableMode(boolean resort);
	
	boolean getResortMode();
}
