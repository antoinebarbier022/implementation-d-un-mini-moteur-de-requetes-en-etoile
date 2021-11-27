package qengine.program;

import java.io.BufferedOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;


import static java.nio.file.StandardOpenOption.*;



/**
 * Le RDFHandler intervient lors du parsing de données et permet d'appliquer un traitement pour chaque élément lu par le parseur.
 * 
 * <p>
 * Ce qui servira surtout dans le programme est la méthode {@link #handleStatement(Statement)} qui va permettre de traiter chaque triple lu.
 * </p>
 * <p>
 * À adapter/réécrire selon vos traitements.
 * </p>
 */

public final class MainRDFHandler extends AbstractRDFHandler {
	
	static final String outputDictionnary = Main.output + "/Dictionnaire.txt";
	static final String outputIndex = Main.output;
	
	
	
	
	static DictionnaireV1 dictionary = new Dictionnaire();	
	static IndexeV1 SPO = new Indexe("SPO");
	static IndexeV1 SOP = new Indexe("SOP");
	static IndexeV1 PSO = new Indexe("PSO");
	static IndexeV1 POS = new Indexe("POS");
	static IndexeV1 OSP = new Indexe("OSP");
	static IndexeV1 OPS = new Indexe("OPS");

	static int nbTriplet = 0;
	
	
	
	
	@Override
	public void handleStatement(Statement st) {
		nbTriplet++;
		Integer[] toAdd = dictionary.updateDictionary(st);
		add(toAdd[0], toAdd[1], toAdd[2],SPO);
		add(toAdd[0], toAdd[1], toAdd[2],SOP);
		add(toAdd[0], toAdd[1], toAdd[2],PSO);
		add(toAdd[0], toAdd[1], toAdd[2],POS);
		add(toAdd[0], toAdd[1], toAdd[2],OSP);
		add(toAdd[0], toAdd[1], toAdd[2],OPS);

	};
	
	 
	public static void writeDictionnary() throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter(outputDictionnary);
		} catch (IOException e1) {
			e1.printStackTrace();
		} 	   
		try {
			fw.write(dictionary.toString());
			fw.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void writeIndex() throws IOException {
		ArrayList<IndexeV1> indexes = indexesToArray();
		for(IndexeV1 i : indexes) {
			String path = outputIndex + i.getOrder() + ".txt";
			FileWriter fw = null;
			try {
				fw = new FileWriter(path);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  	   
			try {
				fw.write(i.toString());
				fw.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static ArrayList<IndexeV1> indexesToArray(){
		ArrayList<IndexeV1> indexes = new ArrayList<IndexeV1>();
		indexes.add(SPO);
		indexes.add(SOP);	
		indexes.add(PSO);
		indexes.add(POS);
		indexes.add(OSP);
		indexes.add(OPS);

		return indexes;
	}
	
	
	public void add(Integer s,Integer p,Integer o , IndexeV1 idx) {
		String toSwitch = idx.getOrder();
		switch(toSwitch) {
		case "SPO" : 
			idx.add(s,p,o);
			break;
		case "SOP" : 
			idx.add(s,o,p);
			break;
		case "PSO" :
			idx.add(p,s,o);
			break;
		case "POS" :
			idx.add(p,o,s);
			break;
		case "OSP" :			
			idx.add(o,s,p);
			break;
		case "OPS" :
			idx.add(o,p,s);
			break;
		default:
			System.out.println("Default switch case");
			break;
		}
	}
	
	public static void writeToCSV(ArrayList<String> addToCSV) {
		String toWrite = "nom du fichier de données | nom du dossier des requêtes | nombre de triplets RDF | nombre de requêtes | temps de lecture des données (ms) | temps de lecture des requêtes (ms) | temps création dico (ms) | nombre d’index | temps de création des index (ms) | temps total d’évaluation du workload (ms) | temps total (du début à la fin du programme) (ms)\n";
		String path = outputIndex + "Question4" + ".csv";
		for(String s : addToCSV) {
			toWrite+= s+"|";
		}
		toWrite = toWrite.substring(0,toWrite.length()-1);
		FileWriter fw = null;

			try {
				fw = new FileWriter(path);
			} catch (IOException e1) {
				e1.printStackTrace();
			}  	   
			try {
				fw.write(toWrite.toString());
				fw.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}


	
	


	}
	