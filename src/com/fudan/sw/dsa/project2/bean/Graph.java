package com.fudan.sw.dsa.project2.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * for subway graph
 * @author zjiehang
 *
 */
public class Graph {
    private ArrayList<Address> vertexList;
    private ArrayList<ArrayList<Edge>> undergroundLines = new ArrayList<>();
    private ArrayList<ArrayList<Address>> vertxeUnion = new ArrayList<>();
    private int vertexNum = 0;
    private int edgeLength = 0;
    private Map<String,Address> map = new HashMap<String, Address>();
    private Map<String,Integer> mapMatrixIndex = new HashMap<String, Integer>();
    private ArrayList<int[][]> matrixChangeLeastArrList = new ArrayList<>();
    private int[][] matrixTogether;
    private ArrayList<String> stationNameArrL = new ArrayList<>();
    private ArrayList<Graph> routes = new ArrayList<>();
    private Map<Integer,ArrayList<Integer>> relationLinesMap = new HashMap<>();
    private int[][] zuiXiaoHuanChengCiShu;
    private ArrayList<Relations> re = new ArrayList<>();
    private ArrayList<Integer>[][] publicStations = new ArrayList[17][17];


    public Graph(){
        vertexList = new ArrayList<Address>();
        for(int i = 0; i < 17; i++){
            for(int j = 0; j < 17; j++){
                ArrayList<Integer> a = new ArrayList<>();
                publicStations[i][j] = a;
            }
        }
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

    public ArrayList<ArrayList<Address>> getVertxeUnion() {
        return vertxeUnion;
    }

    public void setVertxeUnion(ArrayList<ArrayList<Address>> vertxeUnion) {
        this.vertxeUnion = vertxeUnion;
    }

    public Map getMap() {
        return map;
    }

    public Map<String, Integer> getMapMatrixIndex() {
        return mapMatrixIndex;
    }

    public int getVertexNum() {
        return vertexNum;
    }

    public void setVertexNum(int vertexNum) {
        this.vertexNum = vertexNum;
    }

    public ArrayList<int[][]> getMatrixChangeLeastArrList() {
        return matrixChangeLeastArrList;
    }

    public int[][] getMatrixTogether() {
        return matrixTogether;
    }

    public void setMatrixTogether(int[][] matrixTogether) {
        this.matrixTogether = matrixTogether;
    }

    public ArrayList<String> getStationNameArrL() {
        return stationNameArrL;
    }

    public ArrayList<Graph> getRoutes() {
        return routes;
    }

    public Map<Integer, ArrayList<Integer>> getRelationLinesMap() {
        return relationLinesMap;
    }

    public int[][] getZuiXiaoHuanChengCiShu() {
        return zuiXiaoHuanChengCiShu;
    }

    public void setZuiXiaoHuanChengCiShu(int[][] zuiXiaoHuanChengCiShu) {
        this.zuiXiaoHuanChengCiShu = zuiXiaoHuanChengCiShu;
    }

    public ArrayList<Relations> getRe() {
        return re;
    }

    public ArrayList<Integer>[][] getPublicStations() {
        return publicStations;
    }

    public void setPublicStations(ArrayList<Integer>[][] publicStations) {
        this.publicStations = publicStations;
    }
}
