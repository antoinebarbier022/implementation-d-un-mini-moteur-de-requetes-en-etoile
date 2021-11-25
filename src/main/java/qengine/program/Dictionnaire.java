package qengine.program;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;



public class Dictionnaire {

    // Dictionnaire
    
    // map1 avec premier élément un string puis un entier
    private final Map<String, Integer> map1; 
    
    // map2 avec premier élément un entier puis un string
    private final Map<Integer, String> map2; 

    // Un compteur
    private int compteur;

    // crée une instance de notre dictionnaire 
    private static Dictionnaire inst;
        
    
    
    /**
     * Construction d'un dictionnaire
     * 
     * @param file : données que l'on place dans un dictionnaire
     * @throws IOException
     * @throws FileNotFoundException
     */
    
    
    public Dictionnaire(){
        map1 = new HashMap<>();
        map2 = new HashMap<>();
        compteur = 1; 
    }


    /**
     * Création d'une instance @Dictionnaire      
     ** 
     */

    public static Dictionnaire getInstance(){
        if(inst == null){
            inst = new Dictionnaire();
        } 
        return inst;
    }
    
    /**
     * Recupération de la valeur      
     ** 
     */
    
    
    public int get(String value) throws NullPointerException {
        return map1.get(value);
    }
    
    /**
     * Recupération de la clef      
     ** 
     */
    
    public String get(int index) throws NullPointerException {
        return map2.get(index);
    }

     /**
     * ajouter la valeur à map1 si il contient pas de valeur et on augmente le compteur      
     ** 
     */

    public void add(String... values) {
        for(String value : values) {
            if(!map1.containsKey(value)) {
                map1.put(value, compteur);
                map2.put(compteur, value);
                compteur++;
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, Integer> entry : map1.entrySet()) {
            builder.append(entry.getKey())
                .append(" : ")
                .append(entry.getValue())
                .append(" \n");
                
        }

    return builder.toString(); 



    }




   
}
