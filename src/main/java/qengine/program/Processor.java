package qengine.program;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




public class Processor {
	  DictionnaireV1 dictionary;
	  ArrayList<IndexeV1> indexes;
	  ArrayList<Requete> queries;
	  double t1 = 0;
	  double t2 = 0;
	
	public Processor(DictionnaireV1 d, ArrayList<IndexeV1> idx, ArrayList<Requete> q){
		this.dictionary = d;
		this.indexes = idx;
		this.queries = q;
	}
	public String doQueries(){
		double start = System.currentTimeMillis();
		
		StringBuilder builder = new StringBuilder();
		builder.append("Requetes , Réponses aux requetes \n");
		for(Requete q : queries) {
			List<String> answer = doAQuery(q);
			if(answer!=null) {
				if(answer.size()!=0) {
					builder.append(q.getRealQuery()+" ,");
					for(String s :answer) {
					builder.append(s.toString()+" ");
					}
					builder.append("\n");
				}
			}
			else {
				
			}
				
		}
		
		
		double end = System.currentTimeMillis();
		t1 += ((end - start) / 1000);
		return builder.toString();
	}
	
	public double getExecQuery() {
		return t1;
	}
	
	public  double getExecQueryWrite() {
		return t2;
	}
	
	private   List<String> doAQuery(Requete q) {
		List<Integer> answerInIntegers = intersectionAnswers(q);
		if(answerInIntegers!=null)
			return getAnswersInString(answerInIntegers);
		return null;
	}
	
	
	private   List<Integer> intersectionAnswers(Requete q){
		List<List<Integer>> allAnswers = doQueriesInIntegers(q);
		List<Integer> output =null;
		if( allAnswers !=null){
			output = new ArrayList<Integer>(allAnswers.get(0));
			if(output!=null) {
				for (int i = 1; i < allAnswers.size(); i++) {
					List<Integer> l1 = allAnswers.get(i);
					if(l1 !=null) {
						List<Integer> l2 = intersection(l1,output);
						output = l2;
						
					}
					else 
						return null;
				}
			}
			else 
				return null;
		}

		return output;
		
	}
	
	private   List<List<Integer>> doQueriesInIntegers(Requete q) {
		List<List<Integer>> queriesInInteger = queriesToInteger(q);
		List<List<Integer>> output = new ArrayList<List<Integer>>();
		Integer idx= 0;
		for(List<Integer> queryInInteger : queriesInInteger) {
			if(!queryInInteger.contains(-1)) {
				List<Integer> temp = new ArrayList<Integer>(queryInInteger);
				temp.remove(0);
				for(IndexeV1 index : indexes) {
					List<Integer> answer = index.getAnswer(temp.get(0), temp.get(1));
					if(answer!=null) {
						output.add(answer);
					}
				}
			}
			else
				return null;
		}
		
		if(queriesInInteger.size()!=output.size())
			return null;
		return output;
	}
	
	private   List<List<Integer>> queriesToInteger(Requete query){
		List<List<Integer>> output = new ArrayList<List<Integer>>();
		for(Resultat select : query.getRequete()) {
			List<Integer> toAdd = new ArrayList<Integer>();
			for(String s : select.getResultat()) {
				if(s.equals("?")) {
					toAdd.add(0);
					}
				else {
					Integer key = dictionary.getKey(s);
					if(key!=null) {
						toAdd.add(key);
					}
					else 
						toAdd.add(-1);
				}
			}
			output.add(toAdd);
		}	
		return output;
	}
	
	private   List<String> getAnswersInString(List<Integer> answers ){
		List<String> output = new ArrayList<String>();
		for(Integer i :answers) {
			String answer = dictionary.getValue(i);
			if (answer!=null)
				output.add(answer);
		}
		return output;
	}
    public   <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
	
	public void writeAnswers(String path) throws IOException {
		double start = System.currentTimeMillis();
		String pathToFile = path + "export_query_results" + ".csv";
		FileWriter fw = null;
		try {
			fw = new FileWriter(pathToFile);
		} catch (IOException e1) {
				e1.printStackTrace();
			}  	   
			try {
				fw.write(doQueries());
				fw.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		double end = System.currentTimeMillis();
		t2 += (end - start);
		}
	
}
