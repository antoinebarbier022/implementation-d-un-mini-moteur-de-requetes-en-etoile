package qengine.program;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/// class qui contient les données sur les temps de calcules, les noms des fichiers ...

public class DataInformations {

    // chemins des fichiers et dossiers
    private String str_queryPathFile;
    private String str_dataPathFile;
    private String str_resultPathFolder;

    // Les temps de lecture
    private int t_lectureDatas = 0;
    private int t_lectureQueries = 0;

    // Les temps de calcule
    private int t_creationDico = 0;
    private int t_creationIndex = 0;
    private int t_workloadQueries = 0;

    // Les temps d'exports
    private int t_exportDic = 0;
    private int t_exportIndex = 0;
    private int t_exportQueries = 0;

    // Les nombres
    private int nb_queries = 0;
    private int nb_tripletRDF = 0;
    private int nb_index = 0;
    private int nb_requetes_zero_result = 0;
    private int nb_requetes_doublons = 0;
    private HashMap<Integer, Integer> nb_requetesNbConditions = new HashMap<Integer, Integer>();

    // Constructeur
    public DataInformations() {
    }

    public DataInformations(String queryFile, String dataFile, String resultFolder) {
        this.str_queryPathFile = queryFile;
        this.str_dataPathFile = dataFile;
        this.str_resultPathFolder = resultFolder;
    }

    // setters des fichier et dossiers
    public void setQueryFile(String queryFile) {
        this.str_queryPathFile = queryFile;
    }

    public void setDataFile(String dataFile) {
        this.str_dataPathFile = dataFile;
    }

    public void setResultFolder(String resultFolder) {
        this.str_resultPathFolder = resultFolder;
    }

    // getters des noms fichier et dossier
    public String getQueryPathFile() {
        return this.str_queryPathFile;
    }

    public String getDataPathFile() {
        return this.str_dataPathFile;
    }

    public String getResultPathFolder() {
        return this.str_resultPathFolder;
    }

    // setters des nombres
    public void setNbRequetes(int nb) {
        this.nb_queries = nb;
    }

    public void setNbTripletsRDF(int nb) {
        this.nb_tripletRDF = nb;
    }

    public void setNbIndex(int nb) {
        this.nb_index = nb;
    }

    public void setNbRequetesZeroResult(int nb) {
        this.nb_requetes_zero_result = nb;
    }

    public void setNbRequetesDoublons(int nb) {
        this.nb_requetes_doublons = nb;
    }

    public void setNbRequetesConditions(HashMap<Integer, Integer> tableau) {
        this.nb_requetesNbConditions = tableau;
    }

    // setters des temps de calcule et lecture
    public void setTimeCreationIndex(int time) {
        this.t_creationIndex = time;
    }

    public void setTimeCreationDico(int time) {
        this.t_creationDico = time;
    }

    public void setTimeLectureDatas(int time) {
        this.t_lectureDatas = time;
    }

    public void setTimeLectureQueries(int time) {
        this.t_lectureQueries = time;
    }

    public void setTimeWorkloadQueries(int time) {
        this.t_workloadQueries = time;
    }

    // setters des temps d'export
    public void setTimeExportDico(int time) {
        this.t_exportDic = time;
    }

    public void setTimeExportIndex(int time) {
        this.t_exportIndex = time;
    }

    public void setTimeExportQueries(int time) {
        this.t_exportQueries = time;
    }

    // getters des temps totaux

    public int getTemps_lecture_total() {
        return this.t_lectureDatas + this.t_lectureQueries;
    }

    public int getTemps_calcule_total() {
        return this.t_creationDico + this.t_creationIndex + t_workloadQueries;
    }

    public int getTemps_exports_total() {
        return this.t_exportDic + this.t_exportIndex + this.t_exportQueries;
    }

    public int getTemps_total() {
        return this.getTemps_lecture_total() + this.getTemps_calcule_total() + this.getTemps_exports_total();
    }

    /* Méthodes */

    /**
     * Méthode qui affiche les données sur la sortie standard
     */
    public void affichage() {

        String colorData = ConsoleColor.BLUE;
        String colorReset = ConsoleColor.RESET;

        System.out.println();
        System.out.println("Statistiques :");
        System.out.println("\tNB requêtes zero résultat : " + colorData + this.nb_requetes_zero_result + "/"
                + this.nb_queries + colorReset);
        System.out.println(
                "\tNB de requêtes doublons  : " + colorData + this.nb_requetes_doublons + "/" + this.nb_queries
                        + colorReset);
        System.out.println(
                "\tNB requêtes avec les mêmes conditions : " + colorData + this.nb_requetesNbConditions + colorReset);
        for (Map.Entry<Integer, Integer> mapentry : this.nb_requetesNbConditions.entrySet()) {
            System.out.println("\t - " + colorData + mapentry.getValue() + colorReset + " requêtes avec "
                    + mapentry.getKey() + " condition"
                    + (mapentry.getKey() == 1 ? "," : "s,"));
        }

        System.out.println();

        System.out
                .println("Temps de lectures (" + colorData + this.getTemps_lecture_total() + " ms" + colorReset + ")");
        System.out.println("\tLecture des données: \t" + colorData + this.t_lectureDatas + " ms" + colorReset);
        System.out.println("\tLecture des requêtes: \t" + colorData + this.t_lectureQueries + " ms" + colorReset);

        System.out.println();

        System.out.println("Temps de calcules (" + colorData + this.getTemps_calcule_total() + " ms)" + colorReset);
        System.out.println("\tCréation Dictionnaire: \t" + colorData + this.t_creationDico + " ms" + colorReset);
        System.out.println("\tCréation Index: \t" + colorData + this.t_creationIndex + " ms" + colorReset);
        System.out.println("\tWorkload (requêtes) : \t" + colorData + this.t_workloadQueries + " ms" + colorReset);

        System.out.println();

        System.out
                .println("Temps des exports (" + colorData + this.getTemps_exports_total() + " ms" + colorReset + ")");
        System.out.println("\tExport dictionnaire : \t" + colorData + this.t_exportDic + " ms" + colorReset);
        System.out.println("\tExport index : \t\t" + colorData + this.t_exportIndex + " ms" + colorReset);
        System.out.println("\tExport requêtes : \t" + colorData + this.t_exportQueries + " ms" + colorReset);

        System.out.println();

        System.out.println("Temps total du programme : " + colorData + this.getTemps_total() + " ms" + colorReset);
    }

    /**
     * Méthode qui permet d'exporter les informations dans un fichier
     * 
     * @param fileName : Nom du fichier csv
     * @throws Exception
     */
    public void export(String fileName) throws Exception {
        String path = str_resultPathFolder + fileName + ".csv";

        FileWriter fw = null;

        try {
            fw = new FileWriter(path);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            // Pour une meilleur lisibilité du csv on a choisie de mettre les informations
            // sur deux colonnes plutot que de les mettres sur 2 lignes
            fw.write("nom du fichier de données , " + this.str_dataPathFile + "\n");
            fw.write("nom du dossier des requêtes , " + this.str_queryPathFile + "\n");
            fw.write("nombre de triplets RDF , " + this.nb_tripletRDF + "\n");
            fw.write("nombre de requêtes , " + this.nb_queries + "\n");
            fw.write("nombre de requêtes doublons , " + this.nb_requetes_doublons + "\n");
            fw.write("nombre de requêtes zéro résultat , " + this.nb_requetes_zero_result + "\n");
            fw.write("nombre de requêtes avec les mêmes conditions , "
                    + String.valueOf(this.nb_requetesNbConditions).replace(",", " ") + "\n");
            fw.write("nombre d’index , " + this.nb_index + "\n");
            fw.write("temps de lecture des données (ms) , " + this.t_lectureDatas + "\n");
            fw.write("temps de lecture des requêtes (ms) , " + this.t_lectureQueries + "\n");
            fw.write("temps création dico (ms) , " + this.t_creationDico + "\n");
            fw.write("temps de création des index (ms) , " + this.t_creationIndex + "\n");
            fw.write("temps total d’évaluation du workload (ms) , " + this.t_workloadQueries + "\n");
            fw.write("temps total (du début à la fin du programme) (ms) , " + this.getTemps_total() + "\n");

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // On enregistre dans l'historique
        addToHistory();

    }

    /**
     * Pour garder une trace de toutes les informations, on sauvegarde dans un
     * fichier historique toutes les infos
     * 
     * @param fileName
     * @throws Exception
     */
    public void addToHistory() throws Exception {
        String path = "output-historique.csv";
        // LinkedHashMap préserve l'ordre d'insertion des données
        LinkedHashMap<String, String> datasToExport = new LinkedHashMap<String, String>();

        Boolean isNewFile = false;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            // On regarde si c'est un nouveau fichier ou non
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                isNewFile = true;
            }
            // On met true pour écrire à la suite du fichier, false si reset
            fw = new FileWriter(path, true);
            bw = new BufferedWriter(fw);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            // On place les datas dans le hashMap
            datasToExport.put("Date",
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(LocalDateTime.now()).toString());
            datasToExport.put("nom du fichier de données", this.str_dataPathFile);
            datasToExport.put("nom du dossier des requêtes", this.str_queryPathFile);
            datasToExport.put("nombre de triplets RDF", String.valueOf(this.nb_tripletRDF));
            datasToExport.put("nombre de requêtes", String.valueOf(this.nb_queries));
            datasToExport.put("nombre de requêtes doublons", String.valueOf(this.nb_requetes_doublons));
            datasToExport.put("nombre de requêtes zéro résultat", String.valueOf(this.nb_requetes_zero_result));
            datasToExport.put("nombre de requêtes avec les mêmes conditions",
                    String.valueOf(this.nb_requetesNbConditions).replace(",", " "));
            datasToExport.put("nombre d’index", String.valueOf(this.nb_index));
            datasToExport.put("temps de lecture des données (ms)", String.valueOf(this.t_lectureDatas));
            datasToExport.put("temps de lecture des requêtes (ms)", String.valueOf(this.t_lectureQueries));
            datasToExport.put("temps création dico (ms)", String.valueOf(this.t_creationDico));
            datasToExport.put("temps de création des index (ms)", String.valueOf(this.t_creationIndex));
            datasToExport.put("temps total d’évaluation du workload (ms)", String.valueOf(this.t_workloadQueries));
            datasToExport.put("temps total (du début à la fin du programme) (ms)",
                    String.valueOf(this.getTemps_total()));

            // Si on repart d'un fichier vide, on ecrit la première ligne du csv, sion on
            // passe
            if (isNewFile) {
                for (Map.Entry<String, String> mapentry : datasToExport.entrySet()) {
                    bw.write(mapentry.getKey() + " , ");
                }
                bw.newLine();
            }

            // On ajoute les resultats dans le fichier
            for (Map.Entry<String, String> mapentry : datasToExport.entrySet()) {
                bw.write(mapentry.getValue() + " , ");
            }
            // On retourne à la ligne
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
