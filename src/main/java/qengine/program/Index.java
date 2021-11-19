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
                s = dictionnaire.dico.inverse().get(st.getSubject().toString());
                p = dictionnaire.dico.inverse().get(st.getPredicate().getLocalName());
                o = dictionnaire.dico.inverse().get(st.getObject().toString());
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
}
