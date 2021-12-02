package qengine.program;

import java.io.FileWriter;
import java.io.IOException;

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

    // Constructeur
    public DataInformations(String queryFile, String dataFile, String resultFolder) {
        this.str_queryPathFile = queryFile;
        this.str_dataPathFile = dataFile;
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

        System.out.println();
        System.out.println("Statistiques :");
        System.out.println("\tNB requêtes zero résultat : " + this.nb_requetes_zero_result + "/" + this.nb_queries);

        System.out.println();

        System.out.println("Temps de lectures (" + this.getTemps_lecture_total() + " ms)");
        System.out.println("\tLecture des données: \t" + this.t_lectureDatas + " ms");
        System.out.println("\tLecture des requêtes: \t" + this.t_lectureQueries + " ms");

        System.out.println();

        System.out.println("Temps de calcules (" + this.getTemps_calcule_total() + " ms)");
        System.out.println("\tCréation Dictionnaire: \t" + this.t_creationDico + " ms");
        System.out.println("\tCréation Index: \t" + this.t_creationIndex + " ms");
        System.out.println("\tWorkload (requêtes) : \t" + this.t_workloadQueries + " ms");

        System.out.println();

        System.out.println("Temps des exports (" + this.getTemps_exports_total() + " ms)");
        System.out.println("\tExport dictionnaire : \t" + this.t_exportDic + " ms");
        System.out.println("\tExport index : \t\t" + this.t_exportIndex + " ms");
        System.out.println("\tExport requêtes : \t" + this.t_exportQueries + " ms");

        System.out.println();

        System.out.println("Temps total du programme : " + this.getTemps_total() + " ms");
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
            fw.write("nom du fichier de données | " + this.str_dataPathFile + "\n");
            fw.write("nom du dossier des requêtes | " + this.str_queryPathFile + "\n");
            fw.write("nombre de triplets RDF | " + this.nb_tripletRDF + "\n");
            fw.write("nombre de requêtes | " + this.nb_queries + "\n");
            fw.write("nombre d’index | " + this.nb_index + "\n");
            fw.write("temps de lecture des données (ms) | " + this.t_lectureDatas + "\n");
            fw.write("temps de lecture des requêtes (ms) | " + this.t_lectureQueries + "\n");
            fw.write("temps création dico (ms) | " + this.t_creationDico + "\n");
            fw.write("temps de création des index (ms) | " + this.t_creationIndex + "\n");
            fw.write("temps total d’évaluation du workload (ms) | " + this.t_workloadQueries + "\n");
            fw.write("temps total (du début à la fin du programme) (ms) | " + this.getTemps_total() + "\n");

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
