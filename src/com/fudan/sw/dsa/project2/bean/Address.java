package com.fudan.sw.dsa.project2.bean;

import java.util.ArrayList;

/**
 * For each station of subway
 * If you need other attribute, add it
 * @author zjiehang
 *
 */

public class Address 
{
	private String address;
	private double longitude;//经度
	private double latitude;//纬度
	private double d = 999999;
	private Address pi = null;//时间最短
	private int d_changTimes = 0;
	private Address pi_leastChange = null;
	private ArrayList<Edge> edgesAfter = new ArrayList<>();
	private Edge fromEdge = null;


	public Address(String address,String longitude,String latitude)
	{
		this.address=address;
		this.latitude=Double.parseDouble(latitude);
		this.longitude=Double.parseDouble(longitude);
	}
	public void addEdge(Edge edge){
		edgesAfter.add(edge);
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getD() {
		return d;
	}
	public void setD(double d) {
		this.d = d;
	}
	public Address getPi() {
		return pi;
	}
	public void setPi(Address pi) {
		this.pi = pi;
	}
	public ArrayList<Edge> getEdgesAfter() {
		return edgesAfter;
	}
	public void setEdgesAfter(ArrayList<Edge> edgesAfter) {
		this.edgesAfter = edgesAfter;
	}
	public Edge getFromEdge() {
		return fromEdge;
	}
	public void setFromEdge(Edge fromEdge) {
		this.fromEdge = fromEdge;
	}
	public int getD_changTimes() {
		return d_changTimes;
	}
	public void setD_changTimes(int d_changTimes) {
		this.d_changTimes = d_changTimes;
	}
	public Address getPi_leastChange() {
		return pi_leastChange;
	}
	public void setPi_leastChange(Address pi_leastChange) {
		this.pi_leastChange = pi_leastChange;
	}
}


