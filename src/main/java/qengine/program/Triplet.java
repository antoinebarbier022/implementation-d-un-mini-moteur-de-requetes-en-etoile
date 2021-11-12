package qengine.program;

import org.eclipse.rdf4j.model.Statement;

public class Triplet {
    private int first;
    private int second;
    private int third;

    private Statement data;

    public Triplet(int first, int second, int third, Statement data) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.data = data;
    }

    // getters et setters
    public int getFirst() {
        return this.first;
    }

    public int getSecond() {
        return this.second;
    }

    public int getThird() {
        return this.third;
    }

    public Statement getData() {
        return this.data;
    }

    public void setFirst(int index) {
        this.first = index;
    }

    public void setSecond(int index) {
        this.second = index;
    }

    public void setThird(int index) {
        this.third = index;
    }

    public void setData(Statement data) {
        this.data = data;
    }

}
