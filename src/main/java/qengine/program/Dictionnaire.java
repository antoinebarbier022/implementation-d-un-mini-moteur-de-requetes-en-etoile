package qengine.program;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.FileWriter;

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

    // Méthode pour exporter le dictionnaire dans un fichier nommé Dictionnaire.txt
    // situé dans le dossier souhaité
    public void export(String outputDir) throws Exception {
        String filename = "Dictionnaire.txt";
        String path = outputDir + filename;

        FileWriter fw = null;
        try {
            fw = new FileWriter(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Erreur export dictionnaire : Problème ouverture du fichier : " + filename);
        }
        try {
            fw.write(this.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(
                    "Erreur export dictionnaire : Problème lors de l'écriture dans le fichier : " + filename);
        }

    }

    @Override
    public String toString() {
        return "\nDictionnaire : \n" + dico.toString().replace(",", "\n").replace("=", " : ") + "\n";
    }
}
