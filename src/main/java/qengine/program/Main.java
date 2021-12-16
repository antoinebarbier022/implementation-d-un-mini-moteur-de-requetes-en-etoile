package qengine.program;

import java.io.File;

/**
 * 
 * @author Antoine Barbier <antoine.barbier01@etu.umontpellier.fr>
 * @author Djamel Bennameur
 */
final class Main {

	private static boolean exportQueriesToCsv = true;

	enum MessageType {
		LOADED, LOADING, ERROR, WARNING;
	};

	private static void gestionDesOptions(String[] args, DataInformations infos) throws Exception {
		// Si c'est impaire alors on a pas bien écrit les paramètres
		if (args.length % 2 != 0) {
			if (args.length == 1) {
				if (args[0].equals("clean")) {
					// On supprime le fichier historique
					File file = new File("output/output-historique.csv");
					if (file.exists()) {
						file.delete();
						print(MessageType.LOADED, "Clean",
								"Le fichier historique 'output-historique.csv' est supprimé !");
					}
					System.exit(1);
				}
			}
			System.out.println("\n============= Warnings & Errors =============");
			System.out.println("Les différentes option sont les suivantes :");
			System.out.println("-queries <queryFile>");
			System.out.println("-data <dataFile>");
			System.out.println("-output <restultFolder>");
			System.out.println();
			throw new Exception(ConsoleColor.RED +
					"Erreur : Vous devez saisir les options de la façon suivante : qengine -queries <queryFile> -data <dataFile> -output <restultFolder> \n"
					+ ConsoleColor.RESET);
		} else {
			String queriesFile = "";// workingDir + "sample_query.queryset";
			String dataFile = ""; // workingDir + "sample_data.nt";
			String resultFolder = "";
			// On place les noms des chemins vers les fichiers
			for (int i = 0; i < args.length; i++) {
				// alors c'est le nom de l'option
				if (i % 2 == 0) {
					switch (args[i]) {
						case "-queries":
							queriesFile = args[i + 1];
							break;
						case "-data":
							dataFile = args[i + 1];
							break;
						case "-output":
							resultFolder = args[i + 1];
							break;
						case "-type-output":
							if (args[i + 1].equals("csv")) {
								exportQueriesToCsv = true;
							} else {
								exportQueriesToCsv = false;
							}
							break;
						default:
							System.out.println("\n============= Warnings & Errors =============");
							throw new Exception(ConsoleColor.RED +
									"Erreur : Option <" + args[i].replace("-", "") + "> non valide \n"
									+ ConsoleColor.RESET);
					}
				}
			}
			if (queriesFile.isEmpty()) {
				System.out.println("\n============= Warnings & Errors =============");
				throw new Exception(ConsoleColor.RED +
						"Erreur : Vous devez saisir le fichier de requêtes. \n" + ConsoleColor.RESET);
			}
			if (dataFile.isEmpty()) {
				System.out.println("\n============= Warnings & Errors =============");
				throw new Exception(ConsoleColor.RED +
						"Erreur : Vous devez saisir le fichier de données. \n" + ConsoleColor.RESET);
			}

			// On regarde si l'ouput est définie, si il ne l'est pas on lui attribue un
			// dossier en fonction du fichier data et du fichier queries
			if (resultFolder.isEmpty()) {
				String[] queriesFileSplit = queriesFile.split("/");
				String[] dataFileSplit = dataFile.split("/");
				String nomDuFichierQueries = queriesFileSplit[queriesFileSplit.length - 1];
				String nomDuFichierData = dataFileSplit[dataFileSplit.length - 1];

				resultFolder = "output/output-" + nomDuFichierData.split("\\.")[0] + "-" + nomDuFichierQueries + "/";
			}

			infos.setDataFile(dataFile);
			infos.setQueryFile(queriesFile);
			infos.setResultFolder(resultFolder);
		}

	}

	private static void print(MessageType type, String header, String body) {
		// Début du message
		switch (type) {
			case ERROR:
			case WARNING:
				System.out.print(ConsoleColor.RED);
				break;
			case LOADED:
				System.out.print(ConsoleColor.GREEN);
				break;
			case LOADING:
				System.out.print(ConsoleColor.YELLOW);
				break;
			default:
				System.out.print(ConsoleColor.RESET);
				break;
		}
		System.out.print("[" + header + "] ");
		System.out.print("\t -> \t");
		System.out.print(body);
		// Fin du message
		System.out.print(ConsoleColor.RESET);
		System.out.println();
	}

	public static void main(String[] args) throws Exception {

		DataInformations infos = new DataInformations();

		// On gère les options passé dans le programme
		gestionDesOptions(args, infos);

		System.out.println("\n============= Début =============\n");

		// Création du dictionnaire vide et de l'index vide
		Dictionnaire dictionnaire = new Dictionnaire();
		Index index = new Index();

		// On parse le fichier des données et on remplie le dictionnaire et l'index
		print(MessageType.LOADING, "Lecture des données", "en cours ...");
		ParserDatas parserDatas = new ParserDatas(infos.getDataPathFile(), dictionnaire, index);
		infos.setTimeLectureDatas(parserDatas.getTempsLecture()); // temps de lecture des datas

		// Création du dictionnaire terminé
		int tempsCreationDictionnaire = parserDatas.getTempsCreationDico();
		infos.setTimeCreationDico(tempsCreationDictionnaire); // temps dico
		infos.setNbTripletsRDF(parserDatas.getNombreTripletsRDF()); // nombre de triplets RDF

		print(MessageType.LOADED, "Création du dico", tempsCreationDictionnaire + " ms");

		// Création de l'index terminé
		int tempsCreationIndex = parserDatas.getTempsCreationIndex();
		infos.setTimeCreationIndex(tempsCreationIndex); // temps index
		infos.setNbIndex(index.getNbIndex()); // nombre d'index

		print(MessageType.LOADED, "Création de l'index", tempsCreationIndex + " ms");

		// On Parse les requêtes et on leurs trouve des résultats
		print(MessageType.LOADING, "Lecture des requêtes", "en cours ...");
		ParserQueries parserQueries = new ParserQueries(infos.getQueryPathFile(), dictionnaire, index);
		infos.setTimeLectureQueries(parserQueries.getTempsLecture()); // temps lecture requêtes
		infos.setNbRequetes(parserQueries.getNombreRequetes()); // nombre de requête
		infos.setNbRequetesZeroResult(parserQueries.getNombreRequetesAvecZeroResultat()); // nombre de requetes avec 0
		// resultats
		infos.setNbRequetesDoublons(parserQueries.getNbRequetesDoublons()); // nombre de doublons
		infos.setTimeWorkloadQueries(parserQueries.getTempsWorkloadQueries()); // temps pour trouvé la solution des
																				// requêtes
		infos.setNbRequetesConditions(parserQueries.getHashMapNbConditionsRequete());

		print(MessageType.LOADED, "Solution des requêtes", "ok");

		// Exportation du dictionnaire

		print(MessageType.LOADING, "Exportation du dictionnaire", "en cours ...");
		dictionnaire.export(infos.getResultPathFolder()); // export dico
		infos.setTimeExportDico(dictionnaire.getExportTime()); // temps d'exportation dico

		// Exportation de l'index
		System.out.print(ConsoleColor.YELLOW);
		print(MessageType.LOADING, "Exportation de l'index", "en cours ...");
		index.export(infos.getResultPathFolder()); // export index
		infos.setTimeExportIndex(index.getExportTime()); // temps d'exportation indexs

		// Exportation des résultats des requêtes
		print(MessageType.LOADING, "Exportation des requêtes", "en cours ...");
		parserQueries.export(infos.getResultPathFolder(), dictionnaire, exportQueriesToCsv); // export requetes
		infos.setTimeExportQueries(parserQueries.getExportTime()); // temps exportations des requêtes

		// Message exportation des résultats des requêtes terminé
		print(MessageType.LOADING, "Exportation des résultats",
				"exporté dans le dossier : " + infos.getResultPathFolder());

		System.out.println("\n============= Informations sur l'exécution du programme =============");

		// On affiche les informations des données sur la sortie standard
		infos.affichage();

		// On exporte les informations des données dans le fichier csv "Information.csv"
		infos.export("Informations");

		System.out.println("\n============= Fin =============\n");

	}

}
