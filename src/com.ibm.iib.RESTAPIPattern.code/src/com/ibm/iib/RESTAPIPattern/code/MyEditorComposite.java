package com.ibm.iib.RESTAPIPattern.code;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import com.ibm.broker.config.appdev.patterns.ui.PatternPropertyEditorSite;

public class MyEditorComposite extends Composite {

	private List<String> allValues = new ArrayList<String>();
	private List<String> currentDeleteSelections = new ArrayList<String>();	
	private List<String> operationValues = new ArrayList<String>();
	private List<String> methodValues = new ArrayList<String>();
	private List<String> resourceValues = new ArrayList<String>();	
	private int NumberOfRows;
	private int NumberOfRowsEverAdded;
	
	public MyEditorComposite(Composite parent, int style, final PatternPropertyEditorSite site) {
		super(parent, style);
		
		this.setSize(500,250);
		this.setBounds(0,0,500,250);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = true;
		layout.marginWidth = 10;
		this.setLayout(layout);	
		
		parent.setBounds(0,0,500,750);
		
		
		// Operation     Method    Resource
		String[] operations = {"createObject","retrieveObject","updateObject","deleteObject"};
		String[] methods = {"POST","GET","PUT","DELETE"};
		String[] resources = {"/object","/object","/object","/object"};
		
		// Before doing anything with the UI, populate the default values
		allValues.add(0,"createObject"); allValues.add(1,"POST"); allValues.add(2,"/object");
		allValues.add(3,"retrieveObject"); allValues.add(4,"GET"); allValues.add(5,"/object");
		allValues.add(6,"updateObject"); allValues.add(7,"PUT"); allValues.add(8,"/object");
		allValues.add(9,"deleteObject"); allValues.add(10,"DELETE"); allValues.add(11,"/object");
		
		operationValues.add(0,"createObject");operationValues.add(1,"retrieveObject");operationValues.add(2,"updateObject");operationValues.add(3,"deleteObject");
		methodValues.add(0,"POST");methodValues.add(1,"GET");methodValues.add(2,"PUT");methodValues.add(3,"DELETE");
		resourceValues.add(0,"/object");resourceValues.add(1,"/object");resourceValues.add(2,"/object");resourceValues.add(3,"/object");
		
	    Device device = Display.getCurrent();
	    final Color col1 = new Color (device, 140, 210, 17);
	    final Color col2 = new Color (device, 90, 170, 250);
	    final Color col3 = new Color (device, 239, 193, 0);
	    final Color col4 = new Color (device, 255, 125, 135);
	    String[] titles = {"","Operation", "Method", "Resource"};	
		final Table table = new Table(this, SWT.BORDER | SWT.MULTI);
	    table.setLinesVisible(false);
	    table.setHeaderVisible(true);
	    for (int i = 0; i < 4; i++) {
	      TableColumn column = new TableColumn(table, SWT.NONE);
	      column.setText(titles[i]);
	      if (i==0) {
	    	  column.setWidth(22);
	      }
	      if (i==1) {
	    	  column.setWidth(150);
	      }
	      if (i==2) {
	    	  column.setWidth(70);
	      }
	      if (i==3) {
	    	  column.setWidth(200);
	      }	      
	    }
	    NumberOfRows = 4;
	    NumberOfRowsEverAdded = 4;
	    	    
	    	    	
	    for ( int i = 0; i < 4; i++) {
    	  	TableItem item = new TableItem(table, SWT.NONE);
    	  	TableEditor editor = new TableEditor(table);	        
    	  	Button checkbox = createCheckBox(table,i,site);
    	  	checkbox.setData("checkbox", editor);
    	    checkbox.pack();		    	
	    	editor.grabHorizontal = true;
	    	editor.setEditor(checkbox, item, 0);
	    	item.setData("checkbox", editor);	    			    	
	    	editor = new TableEditor(table);	        
	        Text text2 = createOperationTextBox(table,i,site);	        
	        text2.setText(operations[i]);
	        text2.setData("Col2", editor);
	        editor.grabHorizontal = true;
	        editor.setEditor(text2, item, 1);
	        item.setData("Col2", editor);	        	        
	        editor = new TableEditor(table);	        	        	        
			CCombo currentCombo = createMethodDropDowns(table, i, site);
			currentCombo.setText(methods[i]);
			currentCombo.setData("Col3", editor);
        	if (currentCombo.getText().equals("POST")) {
        		currentCombo.setBackground(col1);		        		
        	}
        	if (currentCombo.getText().equals("GET")) {
        		currentCombo.setBackground(col2);		        		
        	}
        	if (currentCombo.getText().equals("PUT")) {
        		currentCombo.setBackground(col3);		        		
        	}
        	if (currentCombo.getText().equals("DELETE")) {
        		currentCombo.setBackground(col4);		        		
        	}
			//comboHash.put(methods[2],currentCombo);
			currentCombo.pack();
			editor.minimumWidth = currentCombo.getSize().x;
			editor.horizontalAlignment = SWT.FILL;
			editor.setEditor(currentCombo, item, 2);
	        item.setData("Col3", editor);
	        editor = new TableEditor(table);
	        Text text1 = createResourceTextBox(table,i,site);
	        text1.setText(resources[i]);
	        text1.setData("Col4", editor);
	        editor.grabHorizontal = true;
	        editor.setEditor(text1, item, 3);	
	        item.setData("Col4", editor);
		}
		
	    table.addListener(SWT.MeasureItem, new Listener() {
	       public void handleEvent(Event event) {
	          event.height = 22;
	       }
	    });
	    
	    
	    Button button = new Button (this, SWT.PUSH);	    
	    button.setText ("Add Row");	        
	    button.addListener (SWT.Selection, new Listener () {
	      public void handleEvent (Event e) {		    	
	    	  	// Add new values to the storage array lists 
	    	  	operationValues.add("defaultOperation"+Integer.toString(NumberOfRowsEverAdded+1));
	    	  	methodValues.add("POST");
	    	  	resourceValues.add("/default"+Integer.toString(NumberOfRowsEverAdded+1));	    	  
	    	  	TableItem item = new TableItem(table, SWT.NONE);
	    	  	TableEditor editor = new TableEditor(table);	        
	    	  	Button checkbox = createCheckBox(table,NumberOfRows,site);
	    	  	checkbox.setData("checkbox", editor);
	    	  	checkbox.pack();		    	
		    	editor.grabHorizontal = true;
		    	editor.grabVertical = true;
		    	editor.setEditor(checkbox, item, 0);
		    	item.setData("checkbox", editor);	    
		    	editor = new TableEditor(table);	  	        
		    	Text text2 = createOperationTextBox(table,NumberOfRows,site);	 
	  	        text2.setText("defaultOperation"+Integer.toString(NumberOfRowsEverAdded+1));
	  	        text2.setData("Col2", editor);
	  	        editor.grabHorizontal = true;
	  	        editor.grabVertical = true;
	  	        editor.setEditor(text2, item, 1);
	  	        item.setData("Col2", editor);
	  	        editor = new TableEditor(table);	        	        	        
	  			CCombo currentCombo = createMethodDropDowns(table, NumberOfRows, site);
	  			currentCombo.setText("POST");
	        	currentCombo.setBackground(col1);
	        	currentCombo.setData("Col3", editor);
	  			//comboHash.put("POST",currentCombo);
	  			currentCombo.pack();
	  			editor.minimumWidth = currentCombo.getSize().x;
	  			editor.horizontalAlignment = SWT.FILL;
	  			editor.setEditor(currentCombo, item, 2);	  	        
	  	        item.setData("Col3", editor);	
	  	        editor = new TableEditor(table);
	  	        Text text1 = createResourceTextBox(table,NumberOfRows,site);
	  	        text1.setText("/default"+Integer.toString(NumberOfRowsEverAdded+1));
	  	        text1.setData("Col4", editor);
	  	        editor.grabHorizontal = true;
	  	        editor.grabVertical = true;
	  	        editor.setEditor(text1, item, 3);	  	        	  	        
	  	        item.setData("Col4", editor);	  	        
	  	        NumberOfRows++;
	  	        NumberOfRowsEverAdded++;
	  	        table.redraw();
	  	        // Update the array list based on updated table contents
		    	updateArrayListsBasedOnTableContents();  	  	        
	      }
	    });
	         
	    Button deleteCheckedRowsButton = new Button (this, SWT.PUSH);	    
	    deleteCheckedRowsButton.setText ("Delete Selected Rows");	 
	    deleteCheckedRowsButton.addSelectionListener(new SelectionAdapter() { 
            public void widgetSelected(SelectionEvent e) { 	    	  
	    	  int loopMax = currentDeleteSelections.size();	    	  
	    	  int numberOfDeletes = 0;	  	    		  
	    	  for (int i=0;i<loopMax;i++) {
	    			  if(currentDeleteSelections.get(i).equals("selected")) {	    				  
	    				  // Start from i and calculate how many entries in the currentDeleteSelections array list have their value set to "deleted".
	    				  // Subtract this number from i and also subtract the value of "numberOfDeletes" which relate 
	    				  // to those array entries which have been deleted *this time* through the delete actions of this loop.
	    				  int adjustedIndex = 0;
	    				  for (int j=0;j<i;j++) {
	    					  if(currentDeleteSelections.get(j).equals("deleted")) {
	    						  adjustedIndex++;
	    					  }	    					  
	    				  }	    				  
	    				  int newIndex = i - adjustedIndex - numberOfDeletes;
	    				  numberOfDeletes++;
	    				  TableItem item = table.getItem(newIndex);
	    				  TableEditor editor = (TableEditor) item.getData("checkbox");  
	                      editor.getEditor().dispose(); 
	                      editor.dispose();
	                      editor = (TableEditor) item.getData("Col2"); 
	                      editor.getEditor().dispose(); 
	                      editor.dispose(); 
	                      editor = (TableEditor) item.getData("Col3"); 
	                      editor.getEditor().dispose(); 
	                      editor.dispose(); 
	                      editor = (TableEditor) item.getData("Col4"); 
	                      editor.getEditor().dispose(); 
	                      editor.dispose();
	                      table.remove(newIndex);
	                      table.redraw();
	                      table.getParent().pack();
	                      currentDeleteSelections.set(i,"deletedtemp");	                      
	                      // Update arrays
	                      operationValues.set(i,null);
	                      methodValues.set(i,null);
	                      resourceValues.set(i,null);
	    			  }	    			   
	    	  }
			  for (int k=0;k<currentDeleteSelections.size();k++) {
				if(currentDeleteSelections.get(k).equals("deletedtemp")) {
					currentDeleteSelections.set(k,"deleted");
					}				  
			  }	    	  
	    	  NumberOfRows = NumberOfRows - numberOfDeletes;
	    	  // Reset allValues array list based on updates to operation/method/resource
	    	  updateArrayListsBasedOnTableContents();
	      }
	    });	    
	}
	
public Button createCheckBox(final Table table, final int rownumber, final PatternPropertyEditorSite site) {		
		final int deleteThisOne = currentDeleteSelections.size();
		currentDeleteSelections.add("unselected");
        final Button checkbox = new Button(table, SWT.CHECK);        
        checkbox.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {                
                Button btn = (Button) e.getSource();
        		btn.getSelection();        		        		
        		if(currentDeleteSelections.get(deleteThisOne).equals("selected")) {
        			currentDeleteSelections.set(deleteThisOne, "unselected");
                } 
        		if(currentDeleteSelections.get(deleteThisOne).equals("unselected")) {
                	currentDeleteSelections.set(deleteThisOne, "selected");
                }
            }
        });
		return checkbox;
}
	
	public CCombo createMethodDropDowns(Table table, final int rownumber, final PatternPropertyEditorSite site) {
		
	    Device device = Display.getCurrent();
	    final Color col1 = new Color (device, 140, 210, 17);
	    final Color col2 = new Color (device, 90, 170, 250);
	    final Color col3 = new Color (device, 239, 193, 0);
	    final Color col4 = new Color (device, 255, 125, 135);
	    	    
	    final CCombo combo = new CCombo(table,SWT.DROP_DOWN | SWT.FILL | SWT.READ_ONLY);
	    combo.add("POST");
	    combo.add("GET");
	    combo.add("PUT");
	    combo.add("DELETE"); 
	    
	    combo.addSelectionListener(new SelectionListener() {
		        @Override
		        public void widgetSelected(SelectionEvent e) {		        			        	
					methodValues.set(rownumber,combo.getText());		        	
		        	if (combo.getText().equals("POST")) {
		        		combo.setBackground(col1);		        		
		        	}
		        	if (combo.getText().equals("GET")) {
		        		combo.setBackground(col2);		        		
		        	}
		        	if (combo.getText().equals("PUT")) {
		        		combo.setBackground(col3);		        		
		        	}
		        	if (combo.getText().equals("DELETE")) {
		        		combo.setBackground(col4);		        		
		        	}
					site.valueChanged();			
			    	// Reset allValues array list based on updates to operation/method/resource
					updateArrayListsBasedOnTableContents();
		        }
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
				}
		    });
	     return combo;	     
	}
	
	
	public Text createOperationTextBox(Table table, final int rownumber, final PatternPropertyEditorSite site) {
        final Text operationText = new Text(table, SWT.NONE);
        operationText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {                
				operationValues.set(rownumber,operationText.getText());
		    	// Reset allValues array list based on updates to operation/method/resource
				updateArrayListsBasedOnTableContents();
            }
        });
		return operationText;
	}

	public Text createResourceTextBox(Table table, final int rownumber, final PatternPropertyEditorSite site) {				
        final Text resourceText = new Text(table, SWT.NONE);        
        resourceText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
				resourceValues.set(rownumber,resourceText.getText());
		    	// Reset allValues array list based on updates to operation/method/resource
				updateArrayListsBasedOnTableContents();
            }
        });        
		return resourceText;		
	}
	
	public void updateArrayListsBasedOnTableContents() {
		allValues.clear();			
		for (int i = 0; i < resourceValues.size(); i++) {
			if (operationValues.get(i) != null) {							
				allValues.add(operationValues.get(i));
				allValues.add(methodValues.get(i));
				allValues.add(resourceValues.get(i));
			}			
		}
	}
	
	public void setValue(String completeListAsString) {

		// Take completeListAsString which is a comma separated list of values
		// Iterate over the list and enter each string into allValues list
		if (!completeListAsString.equals("")) {
			String[] splitValue = completeListAsString.split(",");
			for (int i = 0; i < splitValue.length; i++) {				
				allValues.add(splitValue[i]);
			}
		}		
	}	
	
	public String getValue() {
		// Build up string based on list and return it as a String		
		if (allValues.size()>0) {		
			String returnlist = allValues.get(0);
			for (int a = 1; a < allValues.size(); a++) {
				returnlist = returnlist + "," + allValues.get(a);				 				
			}
			return returnlist;
		} else {
			return "";
		}
	}
	
}
