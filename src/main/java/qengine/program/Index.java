package qengine.program;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

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
    public Statement[][][] index;

    // Dictionnaire
    private Dictionnaire dictionnaire;

    // Constructeur
    public Index(String typeIndex, Dictionnaire dic, String dataFile) throws FileNotFoundException, IOException {
        this.order = typeIndex;
        // this.order = stringToOrder(typeIndex);

        this.dictionnaire = dic;
        int sizeDico = dic.dico.size();
        index = new Statement[sizeDico][sizeDico][sizeDico];
        makeIndex(dataFile, null);
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
                        index[s][p][o] = st;
                        System.out.println(order + "(" + s + "." + p + "." + o + ") = \t" + st);
                        break;
                    case "sop":
                        System.out.println(order + "(" + s + "." + o + "." + p + ") = \t" + st);
                        index[s][o][p] = st;
                        break;
                    case "pso":
                        System.out.println(order + "(" + p + "." + s + "." + o + ") = \t" + st);
                        index[p][s][o] = st;
                        break;
                    case "pos":
                        System.out.println(order + "(" + p + "." + o + "." + s + ") = \t" + st);
                        index[p][o][s] = st;
                        break;
                    case "osp":
                        System.out.println(order + "(" + o + "." + s + "." + p + ") = \t" + st);
                        index[o][s][p] = st;
                        break;
                    case "ops":
                        System.out.println(order + "(" + o + "." + p + "." + s + ") = \t" + st);
                        index[o][p][s] = st;
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
