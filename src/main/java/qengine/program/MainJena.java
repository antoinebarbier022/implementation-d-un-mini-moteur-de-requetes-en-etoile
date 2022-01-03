package qengine.program;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.eclipse.rdf4j.query.algebra.Projection;

import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;

import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;

import java.io.File;

/**
 * 
 * @author Antoine Barbier <antoine.barbier01@etu.umontpellier.fr>
 * @author Djamel Bennameur
 */
final class MainJena {

    private static boolean exportQueriesToCsv = false;

    enum MessageType {
        LOADED, LOADING, ERROR, WARNING;
    };

    private static HashMap<Integer, HashMap<String, String>> parseQueriesMap(String queryFile)
            throws FileNotFoundException, IOException {

        try (Stream<String> lineStream = Files.lines(Paths.get(queryFile))) {
            SPARQLParser sparqlParser = new SPARQLParser();
            Iterator<String> lineIterator = lineStream.iterator();
            StringBuilder queryString = new StringBuilder();

            // Resultat
            int numRequetes = 0;
            HashMap<Integer, HashMap<String, String>> requetesResultats = new HashMap<Integer, HashMap<String, String>>();

            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                queryString.append(line);

                if (line.trim().endsWith("}")) {

                    ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), null);

                    processAQuery(query); // Traitement de la requête, à adapter/réécrire pour votre programme

                    // On place la requete et sa réponse vide dans la structure
                    HashMap<String, String> result = new HashMap<String, String>();
                    result.put(queryString.toString(), "");
                    requetesResultats.put(++numRequetes, result);

                    // On place la requete dans le hashMap

                    queryString.setLength(0); // Reset le buffer de la requête en chaine vide
                }
            }
            return requetesResultats;
        }
    }

    public static void processAQuery(ParsedQuery query) {
        // Utilisation d'une classe anonyme
        query.getTupleExpr().visit(new AbstractQueryModelVisitor<RuntimeException>() {

            public void meet(Projection projection) {
                projection.getProjectionElemList().getElements();
            }
        });
    }

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

        System.out.println("\n============= Début Jena =============\n");

        // Modele jena
        Model model = ModelFactory.createDefaultModel();
        model.read(infos.getDataPathFile());

        HashMap<Integer, HashMap<String, String>> requetesEtResultats = parseQueriesMap(
                infos.getQueryPathFile());

        // On cherche les resultats des requetes
        for (Map.Entry<Integer, HashMap<String, String>> mapentry : requetesEtResultats.entrySet()) {

            // On recupère la requete (en string)
            String query = String.valueOf(mapentry.getValue().entrySet().iterator().next().getKey());

            QueryExecution execution = QueryExecutionFactory.create(query, model);
            StringBuilder st = new StringBuilder();
            try {
                ResultSet rs = execution.execSelect();
                List<QuerySolution> solution = ResultSetFormatter.toList(rs);
                for (QuerySolution querySolution : solution) {
                    querySolution.varNames().forEachRemaining((varName) -> {
                        st.append(querySolution.get(varName)).append("\n");
                    });
                }
            } finally {
                execution.close();
            }
            mapentry.getValue().entrySet().iterator().next().setValue(st.toString());
        }

        // On export les resultats jena
        export(infos.getResultPathFolder(), requetesEtResultats);
        // Message exportation des résultats des requêtes terminé
        print(MessageType.LOADING, "Exportation des résultats",
                "exporté dans le fichier : " + infos.getResultPathFolder() + "jena-results.csv");

        System.out.println("\n============= Fin =============\n");

    }

    private static void export(String outputDir, HashMap<Integer, HashMap<String, String>> requetesResultats)
            throws Exception {
        // si on veut veux un fichier csv, sinon fichier textes
        String filename = "jena-results" + ".csv";
        String path = outputDir + filename;

        File directory = new File(outputDir);
        FileWriter fw = null;
        try {
            // création du dossier s'il n'existe pas
            if (!directory.exists()) {
                directory.mkdirs();
            }
            fw = new FileWriter(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(ConsoleColor.RED + "Erreur export requetes : Problème ouverture du fichier : "
                    + filename + ConsoleColor.RESET);
        }
        try {

            fw.write("Numéro requête , Requête , Nombre de résultats , Résultat(s) \n");

            for (Map.Entry<Integer, HashMap<String, String>> mapentry : requetesResultats.entrySet()) {
                // numéro de la requête
                fw.write(mapentry.getKey().toString());
                fw.write(" , ");
                // requête
                fw.write(mapentry.getValue().entrySet().iterator().next().getKey().replace("{",
                        "{").replace(" . ", " . "));
                fw.write(" , ");
                // Nombre de résultat de la requête (= nombre de saut à la ligne)
                int nbSautLigneResult = mapentry.getValue().entrySet().iterator().next().getValue().split("\n").length;
                Integer nbResult = 0;
                // si il y a au moins un resultat alors
                if (!mapentry.getValue().entrySet().iterator().next().getValue().isEmpty()) {
                    nbResult = nbSautLigneResult;
                }
                fw.write(nbResult.toString());
                fw.write(" , ");

                // résultat de la requête
                fw.write("\"");
                //
                String[] tabResultat = mapentry.getValue().entrySet().iterator().next().getValue().split("\n");
                Arrays.sort(tabResultat);
                // String resultat =
                // mapentry.getValue().entrySet().iterator().next().getValue().replace("\n",
                // "\r\n");
                fw.write(Arrays.toString(tabResultat));

                fw.write("\"");
                fw.write("\n");

            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(ConsoleColor.RED
                    + "Erreur export requetes : Problème lors de l'écriture dans le fichier : " + filename
                    + ConsoleColor.RESET);
        }

    }

}
