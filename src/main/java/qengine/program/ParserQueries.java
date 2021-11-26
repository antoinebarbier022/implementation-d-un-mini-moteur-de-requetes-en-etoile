package qengine.program;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;

public class ParserQueries {

    // ========================================================================

    /**
     * Traite chaque requête lue dans {@link #queryFile} avec
     * {@link #processAQuery(ParsedQuery)}.
     */
    private static void parseQueries() throws FileNotFoundException, IOException {
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
        try (Stream<String> lineStream = Files.lines(Paths.get("queryFile"))) {
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

                    // processAQuery(query); // Traitement de la requête, à adapter/réécrire pour
                    // votre programme

                    queryString.setLength(0); // Reset le buffer de la requête en chaine vide
                }
            }
        }
    }

}
