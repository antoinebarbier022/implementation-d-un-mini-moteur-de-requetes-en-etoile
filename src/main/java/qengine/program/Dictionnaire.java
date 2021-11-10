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

import java.util.HashMap;

public class Dictionnaire {

    // Dictionnaire
    static HashMap<Integer, String> dico;

    /**
     * Construction d'un dictionnaire
     * 
     * @param file : données que l'on place dans un dictionnaire
     * @throws IOException
     * @throws FileNotFoundException
     */
    public Dictionnaire(String file) throws FileNotFoundException, IOException {
        dico = new HashMap<Integer, String>();
        parseData(file, null);
    }

    @Override
    public String toString() {
        return "\nDictionnaire : \n" + dico.toString().replace(" ", "\n").replace("=", " : ") + "\n";
    }

    /**
     * Traite chaque triple lu dans {@link #dataFile} avec {@link MainRDFHandler}.
     */
    private static void parseData(String dataFile, String baseURI) throws FileNotFoundException, IOException {

        try (Reader dataReader = new FileReader(dataFile)) {
            // On va parser des données au format ntriples
            RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);

            // On utilise notre implémentation de handler
            rdfParser.setRDFHandler(new MainRDFHandler());

            Model model = new LinkedHashModel();
            rdfParser.setRDFHandler(new StatementCollector(model));

            // Parsing et traitement de chaque triple par le handler
            rdfParser.parse(dataReader, baseURI);

            int key = 0;
            for (Statement st : model) {
                // Si subject de la data est déjà présente dans le dictionnaire, on ne le
                // rajoute pas
                if (!dico.containsValue(st.getSubject().toString())) {
                    dico.put(key++, st.getSubject().toString());
                }

                // Si predicate de la data est déjà présente dans le dictionnaire, on ne le
                // rajoute pas
                if (!dico.containsValue(st.getPredicate().getLocalName())) {
                    dico.put(key++, st.getPredicate().getLocalName());
                }

                // Si object de la data est déjà présente dans le dictionnaire, on ne le rajoute
                // pas
                if (!dico.containsValue(st.getObject().toString())) {
                    dico.put(key++, st.getObject().toString());
                }
            }
        }

    }

}
