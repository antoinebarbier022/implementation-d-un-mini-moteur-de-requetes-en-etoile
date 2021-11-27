package qengine.program;

import java.util.ArrayList;

public class Resultat {
	ArrayList<String> select = new ArrayList<String>();
	ArrayList<Integer> answer = new ArrayList<Integer>();
	
	public Resultat(String s1, String s2, String s3) {
		select.add(s1);
		select.add(s2);
		select.add(s3);
	}
	
	
	public ArrayList<String> getResultat() {
		return this.select;
	}

	public String getSubject() {
		return select.get(0);
	}

	public String getPredicate() {
		return select.get(1);
	}


	public String getObject() {
		return select.get(2);
	}
	

	public ArrayList<Integer> getAnswer() {
		return answer;
	}
	
	public void setAnswer(ArrayList<Integer> answer) {
		this.answer = answer;
	}


	@Override
	public String toString() {
		return "Select [subject=" + getSubject() + ", predicate=" + getPredicate() + ", object=" + getObject() + "]";
	}


}