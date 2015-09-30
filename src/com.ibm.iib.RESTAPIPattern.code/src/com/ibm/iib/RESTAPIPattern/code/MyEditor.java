package com.ibm.iib.RESTAPIPattern.code;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import com.ibm.broker.config.appdev.patterns.ui.*;

public class MyEditor extends BasePatternPropertyEditor {

		
	private MyEditorComposite segments;
	
	@Override
	public void configureEditor(PatternPropertyEditorSite site,
			boolean required, String configurationValues) {
		// This method is called by the site immediately after the user-defined editor is created.
		// It provides the editor with its site interface and also some basic configuration such
		// as whether the pattern parameter is required (mandatory) or optional. If the pattern
		// author has set up some configuration values then they are also passed in here. 
		
		// Must call the base class to pass on the property site
		super.configureEditor(site, required, configurationValues);
	}

	@Override
	public void createControls(Object parent) {
		// This method is called with the parent Composite	
		Composite composite = (Composite) parent;			
		PatternPropertyEditorSite site = getSite();		
		// Test if this is as fault ...
		segments = new MyEditorComposite(composite, SWT.BORDER, site);		
	}

	@Override
	public void setValue(String value) {

		// Sets our current value from the site - this can be one of four cases:

		//	1. A null value indicating a new pattern instance is being opened
		//		and this pattern parameter does not have a default value.
		//		The pattern parameter also does not have a watermark defined.
		//	2. The default value indicating a new pattern instance is being opened
		//		and this pattern parameter has been configured with a default value.
		//	3. A watermark string beginning with < and ending with a > character.
		//		This is the case when the pattern parameter has been marked as 
		//		required (mandatory), no default value has been specified and
		//		a watermark has also been configured to help guide the pattern user.
		//	4. The current value passed in is the pattern parameter value previously 
		//		saved to a pattern instance configuration file. In other words, 
		//		the pattern instance is being re-opened by the pattern user.

		// A good starting point is to simply ignore null values and watermarks!

		// Value is comma separated list of values of chapter 9 messages 
		if (value != null) {
			if (value.startsWith("<") == false) {
				segments.setValue(value);
			}
		}
	}
	
	@Override
	public String getValue() {

		// Return the current value of the user-defined editor to the site
		// Value is comma separated list of values of chapter 9 messages		
		return segments.getValue();
	}

}
