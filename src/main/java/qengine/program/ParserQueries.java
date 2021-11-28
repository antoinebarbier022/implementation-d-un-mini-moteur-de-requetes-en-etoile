package qengine.program;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;

public class ParserQueries {

    private int tempsTotalParser;
    private int t_export = 0;
    private ArrayList<Integer> tempsResolutionParPattern = new ArrayList<Integer>();
    private HashMap<String, ArrayList<Integer>> requetesResultats;

    /**
     * Traite chaque requête lue dans {@link #str_queryFile} avec
     * {@link #processAQuery(ParsedQuery)}.
     * 
     * @throws Exception
     */
    public ParserQueries(String queryFile, Dictionnaire dictionnaire, Index index) throws Exception {
        long startTotalTime = System.currentTimeMillis();
        requetesResultats = new HashMap<String, ArrayList<Integer>>();
        /**
         * Try-with-resources
         * 
         * @see <a href=
         *      "https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">Try-with-resources</a>
         */
        /*
         * On utilise un stream pour lire les lignes une par une, sans avoir à toutes
         * les stocker entièrement dans une collection.
         */
        try (Stream<String> lineStream = Files.lines(Paths.get(queryFile))) {
            SPARQLParser sparqlParser = new SPARQLParser();
            Iterator<String> lineIterator = lineStream.iterator();
            StringBuilder queryString = new StringBuilder();

            while (lineIterator.hasNext())
            /*
             * On stocke plusieurs lignes jusqu'à ce que l'une d'entre elles se termine par
             * un '}' On considère alors que c'est la fin d'une requête
             */
            {
                String line = lineIterator.next();
                queryString.append(line);

                if (line.trim().endsWith("}")) {
                    ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), null);

                    processAQuery(query, dictionnaire, index);
                    // Traitement de la requête, à adapter/réécrire pour votre programme

                    queryString.setLength(0); // Reset le buffer de la requête en chaine vide
                }
            }
        }

        long endTotalTime = System.currentTimeMillis();
        // Temps total qui comprend :
        // lecture des données + creation dico + création index
        this.tempsTotalParser = (int) (endTotalTime - startTotalTime);
    }

    /* =========== Getters =========== */

    /**
     * 
     * @return Nombre total de requêtes
     */
    public int getNombreRequetes() {
        return requetesResultats.size();
    }

    /**
     * 
     * @return Temps d'importation de la solution des requêtes
     */
    public int getExportTime() {
        return this.t_export;
    }

    /* =========== Methodes =========== */

    /**
     * Méthode utilisée ici lors du parsing de requête sparql pour agir sur l'objet
     * obtenu.
     * 
     * @throws Exception
     */
    public void processAQuery(ParsedQuery query, Dictionnaire dictionnaire, Index index) throws Exception {
        List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());

        HashMap<Integer, ArrayList<Integer>> resulatsRequete = new HashMap<Integer, ArrayList<Integer>>();

        // System.out.println("======");
        // Affichage de la requête
        // System.out.println(query.getSourceString());
        int P, O, S;

        int numPattern = 1;
        // Une requête peut avoir plusieurs patterns dans le WHERE,
        // on parcours donc tous les paterns
        for (StatementPattern pattern : patterns) {

            ArrayList<Integer> resultatsPattern = new ArrayList<Integer>();
            // Lorsque l'élément est une variable, la value est égale à null.
            // Ainsi on peut connaitre son emplacement
            Value subject = pattern.getSubjectVar().getValue();
            Value predicate = pattern.getPredicateVar().getValue();
            Value object = pattern.getObjectVar().getValue();
            P = -1;
            O = -1;
            S = -1;

            long startPatternTime = System.currentTimeMillis();
            try {

                if (subject == null) {
                    // Le sujet est null, alors on utilise soit l'index de type POS, soit OPS
                    // On récupère les clefs dans le dictionnaire, pour le prédicat et l'objet

                    P = dictionnaire.getKeyByValue(predicate.toString());
                    O = dictionnaire.getKeyByValue(object.toString().replace("\"", ""));

                    try {
                        if (P < O) {
                            // recherche dans POS
                            resultatsPattern = new ArrayList<Integer>(index.indexes.get(TypeIndex.POS).get(P).get(O));
                        } else {
                            // recherche dans OPS
                            resultatsPattern = new ArrayList<Integer>(index.indexes.get(TypeIndex.OPS).get(O).get(P));
                        }

                    } catch (Exception e) {
                        // System.out.println("Introuvable ");
                    }

                    // System.out.println("resultat patern : " + resultatsPattern);

                } else if (predicate == null) {
                    // Le predicat est null, alors on utilise soit l'index de type OSP, soit SOP
                    // On récupère les clefs dans le dictionnaire, pour le sujet et l'objet
                    S = dictionnaire.getKeyByValue(subject.toString());
                    O = dictionnaire.getKeyByValue(object.toString().replace("\"", ""));

                    if (S < O) {
                        // recherche dans SOP
                        resultatsPattern = new ArrayList<Integer>(index.indexes.get(TypeIndex.SOP).get(S).get(O));
                    } else {
                        // recherche dans OSP
                        resultatsPattern = new ArrayList<Integer>(index.indexes.get(TypeIndex.OSP).get(O).get(S));
                    }

                } else {
                    // L'objet est null, alors on utilise soit l'index de type PSO, soit SPO
                    // On récupère les clefs dans le dictionnaire, pour le prédicat et le sujet
                    P = dictionnaire.getKeyByValue(predicate.toString());
                    S = dictionnaire.getKeyByValue(subject.toString());

                    if (P < S) {
                        // recherche dans PSO
                        resultatsPattern = new ArrayList<Integer>(index.indexes.get(TypeIndex.PSO).get(P).get(S));
                    } else {
                        // recherche dans SPO
                        resultatsPattern = new ArrayList<Integer>(index.indexes.get(TypeIndex.SPO).get(S).get(P));
                    }

                }
            } catch (Exception e) {
                // on place la liste des résultats du pattern dans les résultats de la requète

                // Il y a eu une erreur donc cela signifie que la donnée n'est pas présente
                resulatsRequete.put(numPattern, new ArrayList<Integer>());
                // System.out.println("Résultats pattern " + numPattern + " : " + 0);
            } finally {
                // on place la liste des résultats du pattern dans les résultats de la requète
                if (resultatsPattern == null) {
                    // liste vide
                    resulatsRequete.put(numPattern, new ArrayList<Integer>());
                    // System.out.println("Résultats pattern " + numPattern + " : " + 0);
                } else {
                    resulatsRequete.put(numPattern, resultatsPattern);
                    // System.out.println("Résultats pattern " + numPattern + " : " +
                    // resultatsPattern.size());
                }

            }
            long endPatternTime = System.currentTimeMillis();

            // On ajoute le temps de resolution pour ce pattern
            tempsResolutionParPattern.add((int) (endPatternTime - startPatternTime));

            // System.out.println(" -> SPO : (" + S + "," + P + "," + O + ",");
            // On passe au pattern suivant
            numPattern++;
        }

        // Maintenant pour obtenir le resultat final on fait l'intersection des
        // résultats des paterns
        ArrayList<Integer> res = resulatsRequete.get(1);

        for (int i = 1; i < resulatsRequete.size(); i++) {
            res.retainAll(resulatsRequete.get(i));
        }

        for (int i = resulatsRequete.size(); i >= 1; i--) {
            res.retainAll(resulatsRequete.get(i));
        }

        // On place la requete et sa réponse dans la structure
        requetesResultats.putIfAbsent(query.getSourceString(), res);

        // System.out.println("résultats " + res.size());
        // System.out.println();
        // } catch (NullPointerException e) {
        // throw new Exception("ERROR : " + e);
        //
    }

    // On fait la somme du temps de chaque ajout resolution de pattern pour avoir le
    // temps pour trouvé toutes les solutions des requetes
    public int getTempsWorkloadQueries() {
        return tempsResolutionParPattern.stream().reduce(0, Integer::sum);
    }

    // le temps de lecture est le temps total moins celui des requetes
    public int getTempsLecture() {
        return getTempsTotalParserQueries() - this.getTempsWorkloadQueries();
    }

    // Temps total qui comprend :
    // lecture des données + creation dico + création index
    public int getTempsTotalParserQueries() {
        return tempsTotalParser;
    }

    public void export(String outputDir, Dictionnaire dictionnaire) throws Exception {
        long startRecordExportRequestTime = System.currentTimeMillis();
        String filename = "Requetes.txt";
        String path = outputDir + filename;

        FileWriter fw = null;
        try {
            fw = new FileWriter(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Erreur export requetes : Problème ouverture du fichier : " + filename);
        }
        try {
            fw.write(this.toString());
            int numRequest = 0;
            for (Map.Entry<String, ArrayList<Integer>> mapentry : requetesResultats.entrySet()) {
                fw.write("\n==== Requête n°" + (++numRequest) + " =======================\n");
                // Requete
                fw.write("\n");
                fw.write(mapentry.getKey().replace("{", "{\n").replace(" .	", " .\n	"));
                fw.write("\n\n");

                fw.write("\n\n");
                fw.write("Résultats (" + mapentry.getValue().size() + ") \n");

                for (Integer i : mapentry.getValue()) {
                    fw.write(" - " + dictionnaire.dico.get(i).toString() + "\n");
                }

            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Erreur export requetes : Problème lors de l'écriture dans le fichier : " + filename);
        }
        long endRecordExportRequestTime = System.currentTimeMillis();
        t_export = (int) (endRecordExportRequestTime - startRecordExportRequestTime);
    }

}