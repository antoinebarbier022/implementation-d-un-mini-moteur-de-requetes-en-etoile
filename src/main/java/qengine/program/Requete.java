package qengine.program;

import java.util.ArrayList;

public class Requete {
	String part1Requete;
	ArrayList<Resultat> part2Requete = new ArrayList<Resultat>();
	
	public Requete(String rQ) {
		this.part1Requete = rQ;
	}
	
	
	public void setRequete(ArrayList<Resultat> part2Requete) {
		this.part2Requete = part2Requete;
	}


	public ArrayList<Resultat> getRequete() {return this.part2Requete;}
	
	
	public String getRealQuery() {
		return part1Requete;
	}


	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for(Resultat s : part2Requete) {
			builder.append(s.toString()+"\n");
		}
		
		return builder.toString();
	}
}
