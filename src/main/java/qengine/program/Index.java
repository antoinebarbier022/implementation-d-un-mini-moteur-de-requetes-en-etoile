package qengine.program;

import java.util.*; 

public class Index {
 
    // Temporaire : j'ai mis un ennumeration mais on peut améliorer ca 
    // 0 pour Subject, 1 pour Predicat, 2 pour objet  
    
    public enum Triplet{
        SPO(0,1,2),
        SOP(0,2,1),
        PSO(1,0,2),
        OSP(1,2,0),
        POS(2,0,1),
        OPS(2,1,0);
        
    public int S,P,O;     
    
    Triplet(int S, int P, int O ){
        this.S=S;
        this.P=P;
        this.O=O;
    }
    }

    // une map avec trois élements qui sont des entiers 
    private final Map<Integer, Map<Integer, List<Integer>>> indexe;
    
    // une instanciation de l'ennumeration 
    private Triplet triplet;
    
    // une instance de @Index 
    private static final Map<Triplet, Index> inst = new HashMap<>();
   
    // contructeur de l'index
    private Index(Triplet triplet){
        indexe = new TreeMap<>();
        this.triplet = triplet;
    }    

    // renvoie l'instance de l'index
    public static Index getInstance(Triplet type) {
        if(!inst.containsKey(type))
            inst.put(type, new Index(type));

        return inst.get(type);
    }

    // Ajouter les elements à l'index
    public void add(int S, int P, int O) {
        // on init avec l'ordre S,P,O
        int[] val = order(S, P, O);

        // si l'indexe contient une clé à val[0]
        if(indexe.containsKey(val[0])) {
            Map<Integer, List<Integer>> valeur = indexe.get(val[0]);
            
            // si l'indexe contient une clé à val[1]
            if(valeur.containsKey(val[1])) {
                valeur.get(val[1]).add(val[2]);
                } 
                else {
                    List<Integer> valeur2 = new ArrayList<>();
                    valeur2.add(val[2]);
                    valeur.put(val[1], valeur2);
                }
        } else {
            
            Map<Integer, List<Integer>> valeur = new TreeMap<>();
            List<Integer> valeur2 = new ArrayList<>();
            valeur2.add(val[2]);
            valeur.put(val[1], valeur2);
            indexe.put(val[0], valeur);
        }
    }

    private int[] order(int S, int P, int O) {
        int[] val = new int[3];
        val[triplet.S] = S;
        val[triplet.P] = P;
        val[triplet.O] = O;

        return val;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(Map.Entry<Integer, Map<Integer, List<Integer>>> index1 : indexe.entrySet()) {
            for(Map.Entry<Integer, List<Integer>> index2 : index1.getValue().entrySet()) {
                for (Integer third : index2.getValue()) {
                    builder.append("(")
                           .append(index1.getKey())
                           .append(".")
                           .append(index2.getKey())
                           .append(".")
                           .append(third)
                           .append(")\n");
                }
            }
        }

        return builder.toString();
    }
    
    
    


}
