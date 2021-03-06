package com.fudan.sw.dsa.project2.bean;

import java.util.List;

public class ReturnValue 
{
	Address startPoint;
	List<Address>subwayList;
	Address endPoint;
	double minutes;
	int distance1;
	int distance2;
	long nanoTime;
	public Address getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(Address startPoint) {
		this.startPoint = startPoint;
	}
	public List<Address> getSubwayList() {
		return subwayList;
	}
	public void setSubwayList(List<Address> subwayList) {
		this.subwayList = subwayList;
	}
	public Address getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(Address endPoint) {
		this.endPoint = endPoint;
	}
	public double getMinutes() {
		return minutes;
	}
	public void setMinutes(double minutes) {
		this.minutes = minutes;
	}

	public double getDistance1() {
		return distance1;
	}

	public void setDistance1(int distance1) {
		this.distance1 = distance1;
	}

	public double getDistance2() {
		return distance2;
	}

	public void setDistance2(int distance2) {
		this.distance2 = distance2;
	}

	public long getNanoTime() {
		return nanoTime;
	}

	public void setNanoTime(long nanoTime) {
		this.nanoTime = nanoTime;
	}
}
