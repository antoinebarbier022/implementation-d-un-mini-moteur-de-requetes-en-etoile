package qengine.program;

import org.eclipse.rdf4j.model.Statement;

public class Triplet implements Comparable<Triplet> {
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

    @Override
    public String toString() {

        return "(" + first + "." + second + "." + third + ") = \t" + data;
    }

    @Override
    public int compareTo(Triplet o) {
        if (this.first < o.first) {
            return -1;
        } else if (this.first > o.first) {
            return 1;
        } else {
            // On vérifie le second chiffre si c'est égale
            if (this.second < o.second) {
                return -1;
            } else if (this.second > o.second) {
                return 1;
            } else {
                // On vérifie le 3eme chiffre si c'est égale
                if (this.third < o.third) {
                    return -1;
                } else if (this.third > o.third) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        // if current object is greater,then return 1
        // if current object is lower,then return -1
        // if current object is equal to o,then return 0
    }
}
