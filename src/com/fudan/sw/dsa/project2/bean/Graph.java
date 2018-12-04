package com.fudan.sw.dsa.project2.bean;

import java.util.ArrayList;

/**
 * for subway graph
 * @author zjiehang
 *
 */
public class Graph {
    private ArrayList<Address> vertexList;
    private ArrayList<ArrayList<Edge>> undergroundLines;
    int vertexNum = 0;
    int edgeLength = 0;

    public Graph(){
        vertexList = new ArrayList<Address>();
    }

    public void appendVertex(Address vertex){
        vertexNum++;
        edgeLength++;
        vertexList.add(vertex);
    }

    public ArrayList<Address> getVertexList() {
        return vertexList;
    }

    public ArrayList<ArrayList<Edge>> getUndergroundLines() {
        return undergroundLines;
    }

    public void setUndergroundLines(ArrayList<ArrayList<Edge>> undergroundLines) {
        this.undergroundLines = undergroundLines;
    }

    public void setVertexList(ArrayList<Address> vertexList) {
        this.vertexList = vertexList;
    }
//    public void addEdge(Edge edge){
//        edge.previousVertex.edgesAfter.add(edge);
//        edge.nextVertex.edgesAfter.add(edge);
//       // edge.nextVertex.egdesBefore.add(edge);
//    }

}
