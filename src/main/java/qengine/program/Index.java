package qengine.program;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;

public class Index {

    // On définie l'ordre
    /*
     * public static final int[] spo = { 0, 1, 2 }; // subject, predicate, object
     * public static final int[] sop = { 0, 2, 1 }; // subject, object, predicate
     * public static final int[] pso = { 1, 0, 2 }; // predicate, subject, object
     * public static final int[] pos = { 1, 2, 0 }; // predicate, object, subject
     * public static final int[] osp = { 2, 0, 1 }; // object, subject, predicate
     * public static final int[] ops = { 2, 1, 0 }; // object, predicate, subject
     */
    // L'ordre choisie pour cet index
    private String order;
    // private int[] order;

    // Index
    public ArrayList<Triplet> index;

    // Dictionnaire
    private Dictionnaire dictionnaire;

    // Constructeur
    public Index(String typeIndex, Dictionnaire dic, String dataFile) throws FileNotFoundException, IOException {
        this.order = typeIndex;
        this.dictionnaire = dic;
        index = new ArrayList<Triplet>();
        makeIndex(dataFile, null);
        // On trie l'index
        Collections.sort(index);
    }

    private void makeIndex(String dataFile, String baseURI) throws FileNotFoundException, IOException {

        try (Reader dataReader = new FileReader(dataFile)) {
            // On va parser des données au format ntriples
            RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);

            // On utilise notre implémentation de handler
            rdfParser.setRDFHandler(new MainRDFHandler());

            Model model = new LinkedHashModel();
            rdfParser.setRDFHandler(new StatementCollector(model));

            // Parsing et traitement de chaque triple par le handler
            rdfParser.parse(dataReader, baseURI);

            int s, p, o;
            for (Statement st : model) {
                s = dictionnaire.getKeyByValue(st.getSubject().toString());
                p = dictionnaire.getKeyByValue(st.getPredicate().getLocalName());
                o = dictionnaire.getKeyByValue(st.getObject().toString());
                try {
                    switch (order) {
                    case "spo":
                        index.add(new Triplet(s, p, o, st));
                        break;
                    case "sop":
                        index.add(new Triplet(s, o, p, st));
                        break;
                    case "pso":
                        index.add(new Triplet(p, s, o, st));
                        break;
                    case "pos":
                        index.add(new Triplet(p, o, s, st));
                        break;
                    case "osp":
                        index.add(new Triplet(o, s, p, st));
                        break;
                    case "ops":
                        index.add(new Triplet(o, p, s, st));
                        break;
                    default:
                        throw new Exception("Erreur : le type d'index est incorrect");
                    }

                } catch (Exception e) {
                    System.err.println("Erreur :" + e);
                }

            }
        }

    }

    // On récupère l'ordre en fonction de l'index définie
    /*
     * private int[] stringToOrder(String str) { switch (str) { case "spo": return
     * spo; case "sop": return sop; case "pso": return pso; case "pos": return pos;
     * case "osp": return osp; case "ops": return ops; default: return spo; } }
     */
}
