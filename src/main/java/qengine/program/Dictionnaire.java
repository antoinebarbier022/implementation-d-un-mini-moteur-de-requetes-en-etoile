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
    private int t_export = 0; // Temps d'exportation du dictionnaire

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

    /* =========== Getters =========== */

    /**
     * Getter
     * 
     * @return Le temps d'exportation du dictionnaire
     */
    public int getExportTime() {
        return this.t_export;
    }

    /**
     * Récupère un élément du dictionnaire à partir de la valeur
     * 
     * @param value
     * @return
     */
    public int getKeyByValue(String value) {
        return this.dico.inverse().get(value);
    }

    /* =========== Méthodes =========== */

    // Méthode pour exporter le dictionnaire dans un fichier nommé Dictionnaire.txt
    // situé dans le dossier souhaité
    public void export(String outputDir) throws Exception {
        long startRecordExportDicoTime = System.currentTimeMillis();
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
        long endRecordExportDicoTime = System.currentTimeMillis();
        t_export = (int) (endRecordExportDicoTime - startRecordExportDicoTime);
    }

    @Override
    public String toString() {
        return "\nDictionnaire : \n" + dico.toString().replace(",", "\n").replace("=", " : ") + "\n";
    }
}
