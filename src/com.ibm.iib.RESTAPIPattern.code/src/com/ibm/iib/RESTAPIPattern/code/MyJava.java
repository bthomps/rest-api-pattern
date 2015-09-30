package com.ibm.iib.RESTAPIPattern.code;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.ibm.broker.rest.Api;
import com.ibm.broker.rest.ApiException;
import com.ibm.broker.rest.ApiProvider;
import com.ibm.broker.rest.ApiProviderFactory;
import com.ibm.broker.config.appdev.MessageFlow;
import com.ibm.broker.config.appdev.RestApi;
import com.ibm.broker.config.appdev.RestApiException;
import com.ibm.broker.config.appdev.nodes.InputNode;
import com.ibm.broker.config.appdev.nodes.OutputNode;
import com.ibm.broker.config.appdev.nodes.PassthroughNode;
import com.ibm.broker.config.appdev.patterns.GeneratePatternInstanceTransform;
import com.ibm.broker.config.appdev.patterns.PatternInstanceManager;

public class MyJava implements GeneratePatternInstanceTransform {

	//private List<String> selectionsEmploymentCompensation = new ArrayList<String>();	
	private List<String> operationValues = new ArrayList<String>();
	private List<String> methodValues = new ArrayList<String>();
	private List<String> resourceValues = new ArrayList<String>();	
	
	@Override
	public void onGeneratePatternInstance(PatternInstanceManager patternInstanceManager) {
		
		// The location for the generated projects 
		String location = patternInstanceManager.getWorkspaceLocation();		
		// The pattern instance name for this generation
		String patternInstanceName = patternInstanceManager.getPatternInstanceName();
		String restApiName = patternInstanceName+"_RESTAPI";						
		List<String> messageflownames = new ArrayList<String>();				
		setValue(patternInstanceManager.getParameterValue("pp3"));		    	
		StringBuffer swaggerInsert =  new StringBuffer("");
		int currentIndex = 0;
		boolean firstTime = true;
		while (!resourceValues.isEmpty()) {
			if (!firstTime) {
				swaggerInsert.append(",\r\n");
			}
			String resourceMatch = resourceValues.get(currentIndex);
			swaggerInsert.append("\t\t\""+resourceMatch+"\" : {\r\n");
			swaggerInsert.append("\t\t\t\""+methodValues.get(currentIndex).toLowerCase()+"\" : {\r\n");
			swaggerInsert.append("\t\t\t\t\"tags\" : [ \""+methodValues.get(currentIndex)+"\" ],\r\n");
			swaggerInsert.append("\t\t\t\t\"summary\" : \"\",\r\n");
			swaggerInsert.append("\t\t\t\t\"description\" : \"\",\r\n");
			swaggerInsert.append("\t\t\t\t\"operationId\" : \""+operationValues.get(currentIndex)+"\",\r\n");			
			if (methodValues.get(currentIndex).toLowerCase().equals("post")) {
				swaggerInsert.append("\t\t\t\t\"consumes\" : [ \"application/json\" ],\r\n");
			}			
			swaggerInsert.append("\t\t\t\t\"produces\" : [ \"application/json\" ]\r\n");
			swaggerInsert.append("\t\t\t}\r\n");			
			resourceValues.remove(currentIndex); 
			methodValues.remove(currentIndex);
			messageflownames.add(operationValues.get(currentIndex));
			operationValues.remove(currentIndex);			
			// Now look for other resource values which are the same ...			
			for (int j = 0; j < resourceValues.size(); j++) {				
				if (resourceValues.get(j).equals(resourceMatch)) {
					swaggerInsert.append("\t\t\t,\r\n");
					swaggerInsert.append("\t\t\t\""+methodValues.get(j).toLowerCase()+"\" : {\r\n");
					swaggerInsert.append("\t\t\t\t\"tags\" : [ \""+methodValues.get(j)+"\" ],\r\n");
					swaggerInsert.append("\t\t\t\t\"summary\" : \"\",\r\n");
					swaggerInsert.append("\t\t\t\t\"description\" : \"\",\r\n");
					swaggerInsert.append("\t\t\t\t\"operationId\" : \""+operationValues.get(j)+"\",\r\n");
					if (methodValues.get(j).toLowerCase().equals("post")) {
						swaggerInsert.append("\t\t\t\t\"consumes\" : [ \"application/json\" ],\r\n");
					}
					swaggerInsert.append("\t\t\t\t\"produces\" : [ \"application/json\" ]\r\n");
					swaggerInsert.append("\t\t\t}\r\n");					
					resourceValues.remove(j);
					methodValues.remove(j);
					messageflownames.add(operationValues.get(j));
					operationValues.remove(j);
					j--;
				}
			}
			// Now move to the next unique resource value			
			swaggerInsert.append("\t\t}\r\n");
			firstTime = false;
		}
		patternInstanceManager.setParameterValue("ppSwaggerInserts",swaggerInsert.toString());
		// First create a Swagger JSON inside the target RESTAPI project (empty project is created courtesy of the pattern framework)		
		patternInstanceManager.setParameterValue("ppRESTAPIName",patternInstanceName+"_RESTAPI");				
		patternInstanceManager.runScript("com.ibm.iib.RESTAPIPattern.code", "templates/main.php");

		ApiProvider provider = ApiProviderFactory.instance().get("swagger_20");
		Path projectDirectory = Paths.get(location+File.separator+restApiName);
		Path path = Paths.get(projectDirectory+File.separator+"SwaggerDefinition.json");
		Api api;		
		try {
			api = provider.load(path);
			RestApi restApi = new RestApi(restApiName);
			restApi.setProjectDirectory(projectDirectory);
			restApi.setProjectName(restApiName);			
			restApi.setApi(api);
			MessageFlow[] messageflow = new MessageFlow[messageflownames.size()];			
			int i = 0;
			for (String messageflowname : messageflownames) {
				messageflow[i] = restApi.implementOperation(messageflowname);
				PassthroughNode node = new PassthroughNode();				
				messageflow[i].addNode(node);
				messageflow[i].connect(((InputNode) messageflow[i].getNodeByName("Input")).OUTPUT_TERMINAL_OUT, node.INPUT_TERMINAL_IN);
				messageflow[i].connect(node.OUTPUT_TERMINAL_OUT, ((OutputNode) messageflow[i].getNodeByName("Output")).INPUT_TERMINAL_IN);				
				i++;
			}			
			restApi.save();
		} catch (RestApiException e) {
			e.printStackTrace();
		} catch (ApiException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setValue(String completeListAsString) {
		if (!completeListAsString.equals("")) {
			String[] splitValue = completeListAsString.split(",");
			for (int i = 0; i < splitValue.length; i++) {				
				operationValues.add(splitValue[i]); i++;
				methodValues.add(splitValue[i]); i++;
				resourceValues.add(splitValue[i]); 
			}
		}		
	}
	
	public List<String> getValue(String parameterValue) {			
		List<String> resultList = new ArrayList<String>();
		if (!parameterValue.equals("")) {			
			String[] splitValue = parameterValue.split(",");
			for (int a = 0; a < splitValue.length; a++) {
				resultList.add(splitValue[a]);							 				
			}			
		} 		
		return resultList;
	}

}
