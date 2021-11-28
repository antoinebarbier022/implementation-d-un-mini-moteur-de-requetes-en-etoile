package qengine.program;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;

public class ParserDatas {

    private int tempsTotalParser = 0;
    private int nbTripletsRDF = 0;
    private ArrayList<Integer> tempsCreationIndexParRDF = new ArrayList<Integer>();
    private ArrayList<Integer> tempsCreationDicoParRDF = new ArrayList<Integer>();

    /**
     * 
     * @return Nombre de triplets RDF dans le fichier data
     */
    public int getNombreTripletsRDF() {
        return this.nbTripletsRDF;
    }

    /**
     * Constructeur
     * 
     * @param dataFile     : emplacement du fichier data
     * @param dictionnaire
     * @param index
     * @throws FileNotFoundException
     * @throws IOException
     */
    ParserDatas(String dataFile, Dictionnaire dictionnaire, Index index) throws FileNotFoundException, IOException {

        // Début du parsing
        long startTotalTime = System.currentTimeMillis();

        try (Reader dataReader = new FileReader(dataFile)) {
            // On va parser des données au format ntriples
            RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);

            // On utilise notre implémentation de handler
            rdfParser.setRDFHandler(new MainRDFHandler());

            Model model = new LinkedHashModel();
            rdfParser.setRDFHandler(new StatementCollector(model));

            // Parsing et traitement de chaque triple par le handler
            rdfParser.parse(dataReader, null);

            int key = 0;
            String subject, predicate, object;

            // Pour chaque statement récupérer depuis le fichier data, on extrait le sujet,
            // le prédicat et l'objet et on les places dans un dictionnaire. Une fois le
            // statement placé dans un dictionnaire, on peut fabriquer l'index pour le
            // retrouver plus rapidement
            for (Statement st : model) {

                // On compte le nombre de tripplet RDF
                nbTripletsRDF++;

                // Sujet, Prédicat, Objet
                subject = st.getSubject().stringValue();
                predicate = st.getPredicate().stringValue();
                object = st.getObject().stringValue();

                // Début de l'enregistrement dans le dictionnaire
                long startDicoTime = System.currentTimeMillis();

                // Première étape : On ajoute la donnée dans le dictionnaire
                // Deuxième étape : On ajoute les données (s, p, o) dans l'index

                // Si le sujet est déjà présent dans le dictionnaire, on ne le rajoute pas et on
                // récupère ça position (entier associé au string)
                if (!dictionnaire.dico.containsValue(subject)) {
                    dictionnaire.dico.put(key++, subject);
                }

                // Si le prédicat est déjà présent dans le dictionnaire, on ne le rajoute pas et
                // on récupère ça position (entier associé au string)
                if (!dictionnaire.dico.containsValue(predicate)) {
                    dictionnaire.dico.put(key++, predicate);
                }

                // Si l'objet est déjà présent dans le dictionnaire, on ne le rajoute pas et on
                // récupère ça position (entier associé au string)
                if (!dictionnaire.dico.containsValue(object)) {
                    dictionnaire.dico.put(key++, object);
                }

                // fin de l'enregistrement dans le dictionnaire
                long endDicoTime = System.currentTimeMillis();

                // On ajoute le temps de création de la ligne RDF dans le dico
                tempsCreationDicoParRDF.add((int) (endDicoTime - startDicoTime));

                // Ajout des 3 clefs qui représente le statement dans l'index
                long startIndexTime = System.currentTimeMillis(); // début enregistrement dans l'index
                index.add(dictionnaire.dico.inverse().get(subject), dictionnaire.dico.inverse().get(predicate),
                        dictionnaire.dico.inverse().get(object));
                long endIndexTime = System.currentTimeMillis(); // fin enregistrement dans l'index

                // On ajoute le temps de création de l'index pour ce RDF
                tempsCreationIndexParRDF.add((int) (endIndexTime - startIndexTime));
            }
        }

        // Fin du parsing
        long endTotalTime = System.currentTimeMillis();

        // Temps total qui comprend :
        // lecture des données + creation dico + création index
        this.tempsTotalParser = (int) (endTotalTime - startTotalTime);
    }

    /* =========== Getters =========== */

    // Le temps de création de l'index est le temps total du parser (qui comprend
    // lecture des données + creation dico + création index) auquelle on lui enleve
    // le temps de lecture et le temps de création de l'index

    /**
     * 
     * @return somme du temps de chaque ajout de RDF dans l'index
     */
    public int getTempsCreationIndex() {
        return tempsCreationIndexParRDF.stream().reduce(0, Integer::sum);
    }

    /**
     * 
     * @return somme du temps de chaque ajout de RDF dans le dico
     */
    public int getTempsCreationDico() {
        return tempsCreationDicoParRDF.stream().reduce(0, Integer::sum);
    }

    /**
     * 
     * @return le temps de lecture est le temps total moins celui de la création du
     *         dico et de l'index
     */
    public int getTempsLecture() {
        return getTempsTotalParserData() - (getTempsCreationIndex() + getTempsCreationDico());
    }

    /**
     * 
     * @return Temps total qui comprend : lecture des données + creation dico +
     *         création index
     */
    public int getTempsTotalParserData() {
        return tempsTotalParser;
    }

}
