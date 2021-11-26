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

public class ParserDatas {

    ParserDatas(String dataFile, Dictionnaire dictionnaire, Index index) throws FileNotFoundException, IOException {

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
            int keySubject, keyPredicate, keyObject;
            String subject, predicate, object;

            for (Statement st : model) {

                subject = st.getSubject().toString();
                predicate = st.getPredicate().getLocalName();
                object = st.getObject().toString();

                // Première étape : On ajoute la donnée dans le dictionnaire
                // Deuxième étape : On ajoute les données (s, p, o) dans l'index

                // Si subject de la data est déjà présente dans le dictionnaire, on ne le
                // rajoute pas
                if (!dictionnaire.dico.containsValue(subject)) {
                    keySubject = key;
                    dictionnaire.dico.put(key++, st.getSubject().toString());
                } else {
                    keySubject = dictionnaire.dico.inverse().get(subject);
                }

                // Si predicate de la data est déjà présente dans le dictionnaire, on ne le
                // rajoute pas
                if (!dictionnaire.dico.containsValue(predicate)) {
                    keyPredicate = key;
                    dictionnaire.dico.put(key++, st.getPredicate().getLocalName());
                } else {
                    keyPredicate = dictionnaire.dico.inverse().get(predicate);
                }

                // Si object de la data est déjà présente dans le dictionnaire, on ne le rajoute
                // pas
                if (!dictionnaire.dico.containsValue(object)) {
                    keyObject = key;
                    dictionnaire.dico.put(key++, st.getObject().toString());
                } else {
                    keyObject = dictionnaire.dico.inverse().get(predicate);
                }

                // Ajout des données dans l'index
                index.add(keySubject, keyPredicate, keyObject);

                System.out.println(keySubject + " " + keyPredicate + " " + keyObject);

            }
        }

    }
}
