package qengine.program;

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

	// Votre répertoire de travail où vont se trouver les fichiers à lire
	// static final String workingDir = "data/";
	// Fichier contenant les requêtes sparql
	static String queryFile = "";// workingDir + "sample_query.queryset";
	// Fichier contenant des données rdf
	static String dataFile = ""; // workingDir + "sample_data.nt";
	// Fichier contenant le resultat
	static String resultFile = "";

	// ========================================================================

	/**
	 * Entrée du programme
	 */
	public static void main(String[] args) throws Exception {

		DataInformations infos;

		// On passe les différents fichiers en arguments
		if (args.length < 3) {
			System.out.println("\n============= Warnings & Errors =============");
			throw new Exception(
					"Erreur : Vous devez saisir les options de la façon suivante : qengine <queryFile> <dataFile> <restultFolder> ");
		} else {
			// On place les noms des chemins vers les fichiers
			// args[0] : chemin queries
			// args[1] : chemin datas
			// args[2] : chemin résultats
			infos = new DataInformations(args[0], args[1], args[2]);
		}

		System.out.println("\n============= Début =============");

		// Création du dictionnaire vide et de l'index vide
		Dictionnaire dictionnaire = new Dictionnaire();
		Index index = new Index();

		System.out.println();
		System.out.println("[Lecture des données]\t\t -> en cours ...\n");

		// On parse le fichier des données et on remplie le dictionnaire et l'index
		ParserDatas parserDatas = new ParserDatas(infos.getDataPathFile(), dictionnaire, index);

		System.out.println();
		System.out.println("[Création du dictionnaire]\t -> ok");
		System.out.println("[Création de l'index]\t\t -> ok");

		System.out.println();
		System.out.println("[Lecture des requêtes]\t\t -> en cours ...");

		// On Parse les requêtes et on leurs trouve des résultats
		ParserQueries parserQueries = new ParserQueries(infos.getQueryPathFile(), dictionnaire, index);

		System.out.println();
		System.out.println("[Solution des requêtes]\t\t -> ok");

		// On récupère les nombre pour les mettre dans la classe DataInformation
		infos.setNbRequetes(parserQueries.getNombreRequetes()); // nombre de requête
		infos.setNbTripletsRDF(parserDatas.getNombreTripletsRDF()); // nombre de triplets RDF
		infos.setNbIndex(index.getNbIndex()); // nombre d'index

		// On récupère les temps pour les mettre dans la classs DataInformation
		infos.setTimeCreationDico(parserDatas.getTempsCreationDico()); // temps dico
		infos.setTimeCreationIndex(parserDatas.getTempsCreationIndex()); // temps index
		infos.setTimeLectureDatas(parserDatas.getTempsLecture()); // temps lecture datas
		infos.setTimeLectureQueries(parserQueries.getTempsLecture()); // temps lecture requêtes
		infos.setTimeWorkloadQueries(parserQueries.getTempsWorkloadQueries()); // temps pour trouvé la solution des
																				// requêtes

		System.out.println();
		System.out.println("[Exportation des résultats]\t -> en cours");

		// On exporte les requêtes, le dictionnaire et l'index dans leurs fichiers
		// respectifs
		parserQueries.export(infos.getResultPathFolder(), dictionnaire);
		dictionnaire.export(infos.getResultPathFolder());
		index.export(infos.getResultPathFolder());

		System.out.println();
		System.out.println("[Exportation des résultats]\t -> ok");

		// On recupére les temps des exports pour les mettre dans la classe
		// DataInformation
		infos.setTimeExportDico(dictionnaire.getExportTime());
		infos.setTimeExportIndex(index.getExportTime());
		infos.setTimeExportQueries(parserQueries.getExportTime());

		System.out.println("\n============= Informations Données =============\n");

		// On affiche les informations des données sur la sortie standard
		infos.affichage();

		// On exporte les informations des données dans le fichier csv "Information.csv"
		infos.export("Informations");

		System.out.println("\n============= Fin =============\n");

	}

}
