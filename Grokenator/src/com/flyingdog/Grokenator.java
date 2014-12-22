
package com.flyingdog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Grokenator extends DefaultHandler {
	
	private HashMap <String, List<String>> hm = new HashMap<String, List<String>>();
	
	// SomeValue = file1.element.attribute, file2.element
	
	private static String  fileInProcess = null;

	public static void main(String[] args) throws Exception {
		DefaultHandler handler = new Grokenator();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		SAXParser parser = factory.newSAXParser();
		fileInProcess = "deploy.xml";
		parser.parse(new File("/home/rick/Programming/JBoss/FSW/BPEL_Files/loan_approval/deploy.xml"), handler);
		fileInProcess = "loan_approval.bpel";
		parser.parse(new File("/home/rick/Programming/JBoss/FSW/BPEL_Files/loan_approval/loan_approval.bpel"), handler);
		fileInProcess = "loanServicePT.wsdl"; 
		parser.parse(new File("/home/rick/Programming/JBoss/FSW/BPEL_Files/loan_approval/loanServicePT.wsdl"), handler);
		fileInProcess = "risk_assessment.bpel";
		parser.parse(new File("/home/rick/Programming/JBoss/FSW/BPEL_Files/loan_approval/risk_assessment.bpel"), handler);
		fileInProcess = "riskAssessmentPT.wsdl";
		parser.parse(new File("/home/rick/Programming/JBoss/FSW/BPEL_Files/loan_approval/riskAssessmentPT.wsdl"), handler);
		fileInProcess = "switchyard.xml"; 
		parser.parse(new File("/home/rick/Programming/JBoss/FSW/BPEL_Files/loan_approval/switchyard.xml"), handler);
		
		
		
		
		
		((Grokenator) handler).printHM();
	}
	
	private void printHM(){
		// make a List, then sort it
		ArrayList<String> valuesList = new ArrayList<String>();
	    Iterator it = hm.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        String key = (String) pairs.getKey();
	        List<String> coordinates = (List<String>) pairs.getValue();
	        for (int i = 0; i < coordinates.size(); i++) {
	        	String thisCoordinate = coordinates.get(i);
	        	String reportLine = thisCoordinate + " has value " + key;
	        	for (int j = 0; j < coordinates.size(); j++){
	        		if (j != i){
	        			reportLine += " also at " + coordinates.get(j);
	        		}
	        	}
	        	System.out.println(reportLine);
	        }
	        
	        
	        
	        
	        for (String coordinate: coordinates){
	        	valuesList.add(coordinate + " has value " + key);
	        }
	        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    Collections.sort(valuesList);
	    for (String item: valuesList){
	    	System.out.println(item);
	    }
		
	}
	
	
	
	
	
	private String currentElement = null;

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		currentElement = qName;
		//System.out.println("\nStarting element:" + currentElement);
		
		// get the number of attributes in the list
		int length = attributes.getLength();
		// process each attribute
		for (int i = 0; i < length; i++) {
			// get qualified (prefixed) name by index
			String name = attributes.getQName(i);
			//System.out.println("Name:" + name);
			// get attribute's value by index.
			String value = attributes.getValue(i);
			//System.out.println("Value:" + value);
			// get namespace URI by index (if parser is namespace-aware)
			//String nsUri = attributes.getURI(i);
			//System.out.println("NS Uri:" + nsUri);
			// get local name by index
			//String lName = attributes.getLocalName(i);
			//System.out.println("Local Name:" + lName);
			
			String coordinate = fileInProcess + "." + currentElement + "." + name;
			List<String> theList = hm.get(value);
			if (null == theList){
				ArrayList<String> newList = new ArrayList<String>();
				newList.add(coordinate);
				hm.put(value, newList);
			}else{
				theList.add(coordinate);
				hm.put(value, theList);
			}
			
			
		}
	}

	
}
