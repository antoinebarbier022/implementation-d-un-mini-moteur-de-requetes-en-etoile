package qengine.program;

public enum Triplet{
    SPO(0,1,2),
    SOP(0,2,1),
    PSO(1,0,2),
    OSP(1,2,0),
    POS(2,0,1),
    OPS(2,1,0);
    
    public final int S,P,O;     

    Triplet(int S, int P, int O ){
        this.S=S;
        this.P=P;
        this.O=O;
    }
}