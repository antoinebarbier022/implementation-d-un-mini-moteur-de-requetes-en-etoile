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
    SPO(0, 1, 2), SOP(0, 2, 1), PSO(1, 0, 2), POS(1, 2, 0), OSP(2, 0, 1), OPS(2, 1, 0);

    public int S, P, O;

    // enum constructor
    TypeIndex(int S, int P, int O) {
        this.S = S;
        this.P = P;
        this.O = O;
    }
}

public class Index {

    // la classe Index contient 6 types d'index
    public HashMap<TypeIndex, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>> index;

    // Constructeur
    public Index() throws FileNotFoundException, IOException {
        index = new HashMap<TypeIndex, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>>();
    }

    // Ajout d'un statement dans les Indexes
    public void add(int subject, int predicate, int object) {

        int[] positionElement;
        int firstElement, secondElement, thirdElement;

        // On place le statement dans les différents type d'index
        for (TypeIndex type : TypeIndex.values()) {
            // On replace les éléments dans l'index en fonction du type : PSO,OSP, OPS ...
            positionElement = new int[3];

            // En fonction du type d'index, la position est différente
            positionElement[type.S] = subject;
            positionElement[type.P] = predicate;
            positionElement[type.O] = object;

            firstElement = positionElement[0];
            secondElement = positionElement[1];
            thirdElement = positionElement[2];

            // Initialisation du type d'index si cela n'est pas encore fait
            index.putIfAbsent(type, new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>());

            // On regarde si le premier élément existe dans l'index
            if (index.get(type).containsKey(firstElement)) {
                // On regarde si le deuxième élément existe dans l'index
                if (index.get(type).get(firstElement).containsKey(secondElement)) {
                    // On regarde si le troisième élément existe dans l'index
                    if (!index.get(type).get(firstElement).get(secondElement).contains(thirdElement)) {
                        // L'element n'existe pas déjà dans l'index, donc on le rajoute
                        index.get(type).get(firstElement).get(secondElement).add(thirdElement);
                    }
                } else {
                    // On rajoute seulement le second élément et le troisième élément
                    index.get(type).get(firstElement).put(secondElement,
                            new ArrayList<Integer>(Arrays.asList(thirdElement)));
                }
            } else {
                // Aucun élément n'est dans la liste, donc on les rajoute tous
                HashMap<Integer, ArrayList<Integer>> secondAndThird = new HashMap<Integer, ArrayList<Integer>>();
                secondAndThird.put(secondElement, new ArrayList<Integer>(Arrays.asList(thirdElement)));
                index.get(type).put(firstElement, secondAndThird);
            }

        }
    }

    public void export(String outputDir) throws Exception {
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
            for (Map.Entry<TypeIndex, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>> mapentry : index
                    .entrySet()) {
                // System.out.println("Type d'index : " + mapentry.getKey());// + " | valeur: "
                // + mapentry.getValue());

                for (Map.Entry<Integer, HashMap<Integer, ArrayList<Integer>>> mapentry2 : index.get(mapentry.getKey())
                        .entrySet()) {

                    for (Map.Entry<Integer, ArrayList<Integer>> mapentry3 : index.get(mapentry.getKey())
                            .get(mapentry2.getKey()).entrySet()) {

                        for (Integer mapentry4 : index.get(mapentry.getKey()).get(mapentry2.getKey())
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
            /*
             * for (Map.Entry<Integer, Map<Integer, ArrayList<Integer>>> t :
             * triplet.entrySet()) { for (Map.Entry<Integer, ArrayList<Integer>> tt :
             * t.getValue().entrySet()) { for (Integer ttt : tt.getValue()) { bf.write("(" +
             * t.getKey() + "," + tt.getKey() + "," + ttt + ")"); bf.newLine();
             * 
             * } } }
             */
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Erreur export index : Problème lors de l'écriture dans le fichier : " + filename);
        }

    }

}
