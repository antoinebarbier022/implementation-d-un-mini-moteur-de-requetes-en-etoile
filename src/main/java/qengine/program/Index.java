package qengine.program;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
            firstElement = (positionElement[type.S] = subject);
            secondElement = (positionElement[type.P] = predicate);
            thirdElement = (positionElement[type.O] = object);

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

}
