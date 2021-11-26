package qengine.program;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Dictionnaire {

    // Dictionnaire
    // On a besoins d'accéder au dictionnaire dans les deux sens :
    // - clef-valeur et valeur-clef
    // On a donc fait le choix d'utiliser la structure BiMap qui permet de le faire
    public BiMap<Integer, String> dico;

    /**
     * Construction d'un dictionnaire
     * 
     * @param file : données que l'on place dans un dictionnaire
     * @throws IOException
     * @throws FileNotFoundException
     */
    public Dictionnaire() throws FileNotFoundException, IOException {
        dico = HashBiMap.create();
    }

    @Override
    public String toString() {
        return "\nDictionnaire : \n" + dico.toString().replace(" ", "\n").replace("=", " : ") + "\n";
    }
}
