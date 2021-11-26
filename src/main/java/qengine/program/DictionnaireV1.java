package qengine.program;

import java.util.HashMap;

import org.eclipse.rdf4j.model.Statement;

public abstract class DictionnaireV1 {
	
	public DictionnaireV1(){};
	
	public Integer[] updateDictionary(Statement st) {
		return null;
	}
	
	public Integer getKey(String value) {return null;}
	public String getValue(Integer i) {return null;}
	
}


