package qengine.program;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import org.eclipse.rdf4j.query.algebra.Projection;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.Var;
import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
/**
 * Programme simple lisant un fichier de requête et un fichier de données.
 * 
 * <p>
 * Les entrées sont données ici de manière statique, à vous de programmer les
 * entrées par passage d'arguments en ligne de commande comme demandé dans
 * l'énoncé.
 * </p>
 * 
 * <p>
 * Le présent programme se contente de vous montrer la voie pour lire les
 * triples et requêtes depuis les fichiers ; ce sera à vous d'adapter/réécrire
 * le code pour finalement utiliser les requêtes et interroger les données. On
 * ne s'attend pas forcémment à ce que vous gardiez la même structure de code,
 * vous pouvez tout réécrire.
 * </p>
 * 
 * @author Olivier Rodriguez <olivier.rodriguez1@umontpellier.fr>
 */


final class Main {
	static final String baseURI = null;

	/**
	 * Votre répertoire de travail où vont se trouver les fichiers à lire
	 */
	static final String workingDir = "data/";

	/**
	 * Fichier contenant les requêtes sparql
	 */
	//static final String queryFile = workingDir + "sample_query.queryset";
	static final String queryFile = workingDir + "STAR_ALL_workload.queryset";
	/**
	 * Fichier contenant des données rdf
	 */
	//static final String dataFile = workingDir + "sample_data.nt";
	static final String dataFile = workingDir + "100K.nt";


	static String output = "output/";

	
	/**
	 * Entrée du programme
	 */
	public static void main(String[] args) throws Exception {
		int compteur= 1000;

		StringBuilder builderBase = new StringBuilder();
		StringBuilder builder = new StringBuilder();
		StringBuilder path = new StringBuilder();
		
		path.append("Entrez SVP le path de OUTPUT");
		System.out.println(path.toString());
		Scanner sc = new Scanner(System.in);
		String toChange = sc.next();
		
		if(!(toChange.equals("defaut"))) {
			output = toChange;
		}
		
		System.out.println(output);
		
		
		builderBase.append("====== Bienvenue dans le projet qui s'intetule Implementation d’un mini moteur de requêtes en étoile  ====== \n");
		System.out.println(builderBase.toString());
		builder.append("====== Choisissez votre opération ======= \n" );
		
		builder.append("====== Sans écriture dans output ======= \n" );
		builder.append("\n1 : Création du dictionnaire ");
		builder.append("\n2 : Création des 6 indexes ");
		builder.append("\n3 : Chargement + exécution des requêtes \n");
		
		
		builder.append("====== Avec écriture dans output ======= \n" );
		builder.append("\n4 : Création du dictionnaire dans output/Dictionnaire.txt ");
		builder.append("\n5 : Création des 6 index dans output/  ");
		builder.append("\n6 : Chargement + exécution des requêtes dans output/ ");
		
		
		while(compteur != 0) {
			System.out.println(builder.toString());
			compteur = sc.nextInt();
			
			switch(compteur) {
			
			case 1: 
				System.out.println("Début de création du dictionnaire \n");
				
				parseData();
				System.out.println("Temps de création du dictionnaire :" + Dictionnaire.getTimeDictionnary() + " ms");
				
				
				System.out.println("Fin de création du dictionnaire \n");

				break; 
			
			case 2:
				System.out.println("Début de création des indexes \n");
				
				parseData();
				System.out.println("Temps de création des indexes :" + Indexe.getExecIndex() + " ms");

				
			
				System.out.println("Fin de création des indexes");

				break;
			
			case 3: 
				System.out.println("Début d'exécution des requetes");
				
				
				parseData();
				ArrayList<Requete> queries = parseQueries();
				Processor processor = new Processor(MainRDFHandler.dictionary ,MainRDFHandler.indexesToArray(), queries);
				processor.doQueries();
				System.out.println("Temps d'éxecution des requetes :" + processor.getExecQuery() + " ms \n");

				
				
				System.out.println("Fin d'exécution des requetes");

				break;		
			
			case 4 : 
				System.out.println("Début de création du dictionnaire \n");
				
				double start = System.currentTimeMillis();
				parseData();
				MainRDFHandler.writeDictionnary();
				double end = System.currentTimeMillis();
				double write = Dictionnaire.getTimeDictionnary() + (end - start);
				System.out.println("Temps de création du dictionnaire (AVEC ECRITURE dans /ouput :" + write + " ms");
				
				System.out.println("Fin de création du dictionnaire \n");
				break;
				
			case 5 : 
				System.out.println("Début de création des indexes \n");
				
				double start2 = System.currentTimeMillis();
				parseData();
				System.out.println("écriture en cours");
				MainRDFHandler.writeIndex();
				double end2 = System.currentTimeMillis();
				double write2 = Indexe.getExecIndex() + (end2 - start2 );
				
				System.out.println("Temps de création des 6 index ( AVEC ECRITURE dans /output :" + write2 + " ms \n");
				
				System.out.println("Fin de création des indexes");
				break;
			
			case 6 : 
				System.out.println("Début d'exécution des requetes");
				
				parseData();
				ArrayList<Requete> queries2 = parseQueries();
				Processor processor2 = new Processor(MainRDFHandler.dictionary ,MainRDFHandler.indexesToArray(), queries2);
				processor2.writeAnswers(output);
				System.out.println("Temps de création et d'exécution des requêtes (AVEC ECRITURE dans /output :" + processor2.getExecQueryWrite() + " ms \n");
				
				System.out.println("Fin d'exécution des requetes");
				break;	
				
			case 0 : 
				System.out.println("Merci de votre visite, bonne journée !");
			default : 
				System.out.println("Mauvaise entrée clavier");
			
			}			
		}


	}



	// ========================================================================

	/**
	 * Méthode utilisée ici lors du parsing de requête sparql pour agir sur l'objet
	 * obtenu.
	 */
	public static Requete processAQuery(ParsedQuery query, Requete output) {
		List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());
		
		for(StatementPattern p : patterns) {
			ArrayList<String> pattern = new ArrayList<String>();
			for(Var var :p.getVarList()) {
				if(var.getValue()==null)
					pattern.add("?");
				else
					pattern.add(var.getValue().toString());
			}
			output.getRequete().add(new Resultat(pattern.get(0),pattern.get(1),pattern.get(2)));
		}
	
		return output;
	}


	// ========================================================================

	/**
	 * Traite chaque requête lue dans {@link #queryFile} avec
	 * {@link #processAQuery(ParsedQuery)}.
	 */
	private static ArrayList<Requete> parseQueries() throws FileNotFoundException, IOException {
		/**
		 * Try-with-resources
		 * 
		 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">Try-with-resources</a>
		 */
		/*
		 * On utilise un stream pour lire les lignes une par une, sans avoir à toutes les stocker
		 * entièrement dans une collection.
		 */
		ArrayList<Requete> queries = new ArrayList<Requete>();
		try (Stream<String> lineStream = Files.lines(Paths.get(queryFile))) {
			SPARQLParser sparqlParser = new SPARQLParser();
			Iterator<String> lineIterator = lineStream.iterator();
			StringBuilder queryString = new StringBuilder();

			while (lineIterator.hasNext())
			/*
			 * On stocke plusieurs lignes jusqu'à ce que l'une d'entre elles se termine par un '}'
			 * On considère alors que c'est la fin d'une requête
			 */
			{
				String line = lineIterator.next();
				queryString.append(line);

				if (line.trim().endsWith("}")) {
					ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), baseURI);
					Requete queryObject = new Requete(queryString.toString().trim().replace("\t", ""));
					queries.add(processAQuery(query,queryObject)); // Traitement de la requête, à adapter/réécrire pour votre programme
					queryString.setLength(0); // Reset le buffer de la requête en chaine vide
				}
			}
		}
		return queries;
	}

	/**
	 * Traite chaque triple lu dans {@link #dataFile} avec {@link MainRDFHandler}.
	 */
	private static void parseData() throws FileNotFoundException, IOException {

		try (Reader dataReader = new FileReader(dataFile)) {
			// On va parser des données au format ntriples
			RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);
			
			// On utilise notre implémentation de handler
			rdfParser.setRDFHandler(new MainRDFHandler());

			// Parsing et traitement de chaque triple par le handler
			rdfParser.parse(dataReader, baseURI);
			
		}
	}
			

}
