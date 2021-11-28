package qengine.program;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;

// On a un enum qui enregistre les différents types d'index (la position des s, o, p est différentes, on enregistre donc leurs positions)
enum TypeIndex {
    // enum constants calling the enum constructors

    // Comment le construire ?
    // On indique l'emplacement de la lettre pour chaque lettre.
    // Exemple pour OSP :
    // - l'emplacement du S est en case 1
    // - l'emplacement du P est en case 2
    // - l'emplacement du O est en case 0
    SPO(0, 1, 2), SOP(0, 2, 1), PSO(1, 0, 2), OSP(1, 2, 0), POS(2, 0, 1), OPS(2, 1, 0);

    public int S, P, O;

    // enum constructor
    TypeIndex(int S, int P, int O) {
        this.S = S;
        this.P = P;
        this.O = O;
    }
}

// la classe Index contient 6 types d'index et des triplet
public class Index {

    private int nbIndex = 0; // Nombre de ligne dans l'index
    private int t_export = 0; // Temps d'exportation du dictionnaire

    protected HashMap<TypeIndex, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>> indexes;

    /**
     * Constructeur
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Index() throws FileNotFoundException, IOException {
        indexes = new HashMap<TypeIndex, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>>();
    }

    /* Getters */

    /**
     * @return Nombre de ligne dans l'index
     */
    public int getNbIndex() {
        return this.nbIndex;
    }

    /**
     * @return Temps d'exportation du dictionnaire
     */
    public int getExportTime() {
        return this.t_export;
    }

    /* Méthodes */

    /**
     * Méthode d'ajout d'un triplet RDF dans l'index
     * 
     * @param subject
     * @param predicate
     * @param object
     */
    public void add(int subject, int predicate, int object) {

        int[] positionElement;
        int firstElement, secondElement, thirdElement;

        // On place le statement dans les différents type d'index
        for (TypeIndex type : TypeIndex.values()) {
            // On replace les éléments dans l'index en fonction du type : PSO,OSP, OPS ...
            positionElement = new int[3];

            // En fonction du type d'index, la position est différente
            positionElement[type.S] = subject;// subject;
            positionElement[type.P] = predicate;// predicate;
            positionElement[type.O] = object;// object;

            firstElement = positionElement[0];
            secondElement = positionElement[1];
            thirdElement = positionElement[2];

            // Initialisation du type d'index si cela n'est pas encore fait
            indexes.putIfAbsent(type, new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>());

            // On regarde si le premier élément existe dans l'index
            if (indexes.get(type).containsKey(firstElement)) {
                // On regarde si le deuxième élément existe dans l'index
                if (indexes.get(type).get(firstElement).containsKey(secondElement)) {
                    // On regarde si le troisième élément existe dans l'index
                    if (!indexes.get(type).get(firstElement).get(secondElement).contains(thirdElement)) {
                        // L'element n'existe pas déjà dans l'index, donc on le rajoute
                        indexes.get(type).get(firstElement).get(secondElement).add(thirdElement);
                    }
                } else {
                    // On rajoute seulement le second élément et le troisième élément
                    indexes.get(type).get(firstElement).put(secondElement,
                            new ArrayList<Integer>(Arrays.asList(thirdElement)));
                }
            } else {
                // Aucun élément n'est dans la liste, donc on les rajoute tous
                HashMap<Integer, ArrayList<Integer>> secondAndThird = new HashMap<Integer, ArrayList<Integer>>();
                secondAndThird.put(secondElement, new ArrayList<Integer>(Arrays.asList(thirdElement)));
                indexes.get(type).put(firstElement, secondAndThird);
            }

            // On compte le nombre d'index créer
            this.nbIndex++;
        }
    }

    /**
     * Méthode d'exportation de l'index
     * 
     * @param outputDir Dossier dans lequel le fichier index sera enregistré
     * @throws Exception
     */
    public void export(String outputDir) throws Exception {
        long startRecordExportIndexTime = System.currentTimeMillis();
        String filename = "Index.txt";
        String path = outputDir + filename;

        FileWriter fw = null;
        try {
            fw = new FileWriter(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Erreur export index : Problème ouverture du fichier : " + filename);
        }
        try {
            for (Map.Entry<TypeIndex, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>> mapentry : indexes
                    .entrySet()) {
                // System.out.println("Type d'index : " + mapentry.getKey());// + " | valeur: "
                // + mapentry.getValue());

                for (Map.Entry<Integer, HashMap<Integer, ArrayList<Integer>>> mapentry2 : indexes.get(mapentry.getKey())
                        .entrySet()) {

                    for (Map.Entry<Integer, ArrayList<Integer>> mapentry3 : indexes.get(mapentry.getKey())
                            .get(mapentry2.getKey()).entrySet()) {

                        for (Integer mapentry4 : indexes.get(mapentry.getKey()).get(mapentry2.getKey())
                                .get(mapentry3.getKey())) {
                            // Ecriture dans le fichier
                            fw.write(mapentry.getKey() + " -> (" + mapentry2.getKey() + "," + mapentry3.getKey() + ","
                                    + mapentry4 + ")\n");
                        }
                    }

                }
                // séparation entre les type d'index
                fw.write("\n=========\n");

            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Erreur export index : Problème lors de l'écriture dans le fichier : " + filename);
        }

        // temps d'export
        long endRecordExportIndexTime = System.currentTimeMillis();
        t_export = (int) (endRecordExportIndexTime - startRecordExportIndexTime);
    }

}
