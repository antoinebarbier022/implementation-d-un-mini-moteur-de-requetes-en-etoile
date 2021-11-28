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

		System.out.println("\n============= Warnings & Errors =============");

		// On passe les différents fichiers en arguments
		if (args.length < 3) {
			throw new Exception(
					"Erreur : Vous devez saisir les options de la façon suivante : qengine <queryFile> <dataFile> <restultFile> ");
		} else {
			queryFile = args[0];
			dataFile = args[1];
			resultFile = args[2];
		}

		// Création du dictionnaire vide
		Dictionnaire dictionnaire = new Dictionnaire();
		// Création d'un index vide
		Index index = new Index();

		// On parse le fichier des données et on remplie le dictionnaire et l'index
		long startRecordDataParserTime = System.currentTimeMillis();
		new ParserDatas(dataFile, dictionnaire, index);
		long endRecordDataParserTime = System.currentTimeMillis();

		// On exporte le dictionnaire dans un fichier
		long startRecordExportDicoTime = System.currentTimeMillis();
		dictionnaire.export(resultFile);
		long endRecordExportDicoTime = System.currentTimeMillis();

		// On exporte l'index dans un fichier
		long startRecordExportIndexTime = System.currentTimeMillis();
		index.export(resultFile);
		long endRecordExportIndexTime = System.currentTimeMillis();

		// On Parse les requêtes
		long startRecordParsingRequestTime = System.currentTimeMillis();
		ParserQueries requests = new ParserQueries(queryFile, dictionnaire, index);
		long endRecordParsingRequestTime = System.currentTimeMillis();

		// On exporte les requetes
		long startRecordExportRequestTime = System.currentTimeMillis();
		requests.export(resultFile, dictionnaire);
		long endRecordExportRequestTime = System.currentTimeMillis();

		System.out.println("\n============= Début =============");

		int temps_calcule_dic_et_index = (int) (endRecordDataParserTime - startRecordDataParserTime);
		int temps_calcule_requetes = (int) (endRecordParsingRequestTime - startRecordParsingRequestTime);
		int temps_calcule_total = temps_calcule_dic_et_index + temps_calcule_requetes;

		System.out.println("Temps d'executions (calcule):");
		System.out.println("\tDictionnaire + Index : \t" + temps_calcule_dic_et_index + " ms");
		System.out.println("\tRequêtes : \t\t" + temps_calcule_requetes + " ms");
		System.out.println("\tTotal calcules : \t" + temps_calcule_total + " ms");

		System.out.println();

		int temps_export_dic = (int) (endRecordExportDicoTime - startRecordExportDicoTime);
		int temps_export_index = (int) (endRecordExportIndexTime - startRecordExportIndexTime);
		int temps_export_requetes = (int) (endRecordExportRequestTime - startRecordExportRequestTime);
		int temps_export_total = temps_export_dic + temps_export_index + temps_export_requetes;

		System.out.println("Temps des exports (calcule):");
		System.out.println("\tExport dictionnaire : \t" + temps_export_dic + " ms");
		System.out.println("\tExport index : \t\t" + temps_export_index + " ms");
		System.out.println("\tExport requêtes : \t" + temps_export_requetes + " ms");
		System.out.println("\tTotal exports : \t" + temps_export_total + " ms");

		System.out.println();

		int temps_total = temps_calcule_total + temps_export_total;

		System.out.println("Temps total (calcules + exports):");
		System.out.println("\tTotal : " + temps_total + " ms");

		System.out.println("\n============= Fin =============\n");

	}

}
