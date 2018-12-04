package com.fudan.sw.dsa.project2.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Edge {
    private Address previousVertex;
    private double weight;
    private Address nextVertex;
    private int transportation;
    public int lineIndex;

    public Edge(Address previousVertex, Address nextVertex, double weight,int transportation){
        this.previousVertex = previousVertex;
        this.nextVertex = nextVertex;
        this.weight = weight;
        this.transportation = transportation;
    }
    @JsonBackReference
    public Address getNextVertex() {
        return nextVertex;
    }
    @JsonBackReference
    public void setNextVertex(Address nextVertex) {
        this.nextVertex = nextVertex;
    }
    @JsonBackReference
    public Address getPreviousVertex() {
        return previousVertex;
    }
    @JsonBackReference
    public void setPreviousVertex(Address previousVertex) {
        this.previousVertex = previousVertex;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getTransportation() {
        return transportation;
    }

    public void setTransportation(int transportation) {
        this.transportation = transportation;
    }
}
