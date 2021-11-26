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

            // Pour chaque statement récupérer depuis le fichier data, on extrait le sujet,
            // le prédicat et l'objet et on les places dans un dictionnaire. Une fois le
            // statement placé dans un dictionnaire, on peut fabriquer l'index pour le
            // retrouver plus rapidement
            for (Statement st : model) {

                // Sujet, Prédicat, Objet
                subject = st.getSubject().toString();
                predicate = st.getPredicate().getLocalName();
                object = st.getObject().toString();

                // Première étape : On ajoute la donnée dans le dictionnaire
                // Deuxième étape : On ajoute les données (s, p, o) dans l'index

                // Si le sujet est déjà présent dans le dictionnaire, on ne le rajoute pas et on
                // récupère ça position (entier associé au string)
                if (!dictionnaire.dico.containsValue(subject)) {
                    keySubject = key;
                    dictionnaire.dico.put(key++, st.getSubject().toString());
                } else {
                    keySubject = dictionnaire.dico.inverse().get(subject);
                }

                // Si le prédicat est déjà présent dans le dictionnaire, on ne le rajoute pas et
                // on récupère ça position (entier associé au string)
                if (!dictionnaire.dico.containsValue(predicate)) {
                    keyPredicate = key;
                    dictionnaire.dico.put(key++, st.getPredicate().getLocalName());
                } else {
                    keyPredicate = dictionnaire.dico.inverse().get(predicate);
                }

                // Si l'objet est déjà présent dans le dictionnaire, on ne le rajoute pas et on
                // récupère ça position (entier associé au string)
                if (!dictionnaire.dico.containsValue(object)) {
                    keyObject = key;
                    dictionnaire.dico.put(key++, st.getObject().toString());
                } else {
                    keyObject = dictionnaire.dico.inverse().get(predicate);
                }

                // Ajout des 3 clefs qui représente le statement dans l'index
                index.add(keySubject, keyPredicate, keyObject);

                // System.out.println("SPO : "+keySubject + " " + keyPredicate + " " +
                // keyObject);

            }
        }

    }
}
