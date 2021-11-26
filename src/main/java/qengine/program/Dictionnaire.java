package qengine.program;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Dictionnaire {

    // Dictionnaire
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
