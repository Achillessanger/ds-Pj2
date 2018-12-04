package com.fudan.sw.dsa.project2.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.fudan.sw.dsa.project2.bean.*;
import org.springframework.stereotype.Service;

import com.fudan.sw.dsa.project2.constant.FileGetter;
import sun.security.krb5.internal.crypto.Aes128;

/**
 * this class is what you need to complete
 * @author zjiehang
 *
 */
@Service
public class IndexService 
{
	//the subway graph
	private Graph graph = null;
	
	/**
	 * create the graph use file
	 */
	public void createGraphFromFile()
	{
		//如果图未初始化
		if(graph==null)     //在读文件
		{
			graph = new Graph();
			FileGetter fileGetter= new FileGetter();
			try(BufferedReader bufferedReader=new BufferedReader(new FileReader(fileGetter.readFileFromClasspath())))
			{
				String line = null;
				int transport = 0;
				int lineIndex = 0;
				String [] pretime = new String[2];
				String [] pretime2 = new String[2];
				String [] lasTime = new String[2];
				String [] lasTime2 = new String[2];
				Address fromVertex_edge = null;
				Address fromVertex_edge2 = null;
				while((line=bufferedReader.readLine())!=null)
				{
					ArrayList<Edge> lineRecord = null;
					ArrayList<Edge> lineRecord2 = null;
					if(line.indexOf("Line") != -1){
						lineRecord = new ArrayList<>();
						graph.getUndergroundLines().add(lineRecord);
						transport = 1; //地铁
						lineIndex = Integer.parseInt(line.substring(4));
						fromVertex_edge = null;
						pretime = null;
						pretime2 = null;
						fromVertex_edge2= null;
						if(lineIndex == 10 || lineIndex == 11){
							lineRecord2 = new ArrayList<>();
							graph.getUndergroundLines().add(lineRecord2);
						}
					}else {

						switch (transport){
							case 1:
								String[] arrStr = line.split("\\s+");
								int ifExisted = 0;
								for(Address address : graph.getVertexList()){
									if(address.getAddress().equals(arrStr[0])){ //address已经在图里出现了
										ifExisted = 1;
										if(fromVertex_edge != null){
											lasTime = arrStr[3].split(":");
											if(lineIndex == 10 || lineIndex == 11){
												lasTime2 = arrStr[4].split(":");
												if(!arrStr[3].equals("--")){
													int time = 60*(Integer.parseInt(lasTime[0])-Integer.parseInt(pretime[0]))+Integer.parseInt(lasTime[1])-Integer.parseInt(pretime[1]);
													Edge edge1 = new Edge(fromVertex_edge,address,time,transport);
													Edge edge11 = new Edge(address,fromVertex_edge,time,transport);
													edge11.lineIndex = (lineIndex == 10)? 101:111;
													edge1.lineIndex = (lineIndex == 10)? 101:111;//
													edge1.setTransportation(transport);
													edge11.setTransportation(transport);//
													fromVertex_edge.addEdge(edge1);
													address.addEdge(edge11);
													lineRecord.add(edge1);
													lineRecord.add(edge11);

												}
												if(!arrStr[4].equals("--")){
													int time2 = 60*(Integer.parseInt(lasTime2[0])-Integer.parseInt(pretime2[0]))+Integer.parseInt(lasTime2[1])-Integer.parseInt(pretime2[1]);
													Edge edge2 = new Edge(fromVertex_edge2,address,time2,transport);
													Edge edge22 = new Edge(address,fromVertex_edge2,time2,transport);
													edge2.lineIndex = (lineIndex == 10)? 102:112;
													edge22.lineIndex = (lineIndex == 10)? 102:112;
													edge2.setTransportation(transport);
													edge22.setTransportation(transport);
													fromVertex_edge2.addEdge(edge2);
													address.addEdge(edge22);
													lineRecord2.add(edge2);
													lineRecord2.add(edge22);
												}
											}else {
												int time = 60*(Integer.parseInt(lasTime[0])-Integer.parseInt(pretime[0]))+Integer.parseInt(lasTime[1])-Integer.parseInt(pretime[1]);
												Edge edge = new Edge(fromVertex_edge,address,time,transport);
												Edge edgee = new Edge(address,fromVertex_edge,time,transport);
												edge.lineIndex = lineIndex;
												edgee.lineIndex = lineIndex;
												edge.setTransportation(transport);
												edgee.setTransportation(transport);
												address.addEdge(edgee);
												fromVertex_edge.addEdge(edge);
												lineRecord.add(edge);
												lineRecord.add(edgee);
											}

										}
										if(lineIndex == 10 || lineIndex == 11){
											if(!arrStr[3].equals("--")){
												pretime = arrStr[3].split(":");
												fromVertex_edge = address;
											}
											if(!arrStr[4].equals("--")){
												pretime2 = arrStr[4].split(":");
												fromVertex_edge2 = address;
											}

										}else {
											pretime = arrStr[3].split(":");
											fromVertex_edge = address;
										}

										break;
									}
								}
								if(ifExisted == 0){
									Address vertex = new Address(arrStr[0],arrStr[1],arrStr[2]);//节点不曾出现就新建节点
									graph.appendVertex(vertex);

									if(fromVertex_edge != null){
										lasTime = arrStr[3].split(":");
										if(lineIndex == 10 || lineIndex == 11){
											lasTime2 = arrStr[4].split(":");
											if(!arrStr[3].equals("--")){
												int time = 60*(Integer.parseInt(lasTime[0])-Integer.parseInt(pretime[0]))+Integer.parseInt(lasTime[1])-Integer.parseInt(pretime[1]);
												Edge edge1 = new Edge(fromVertex_edge,vertex,time,transport);
												Edge edge11 = new Edge(vertex,fromVertex_edge,time,transport);
												edge1.lineIndex = (lineIndex == 10)? 101:111;
												edge11.lineIndex = (lineIndex == 10)? 101:111;
												edge1.setTransportation(transport);
												edge11.setTransportation(transport);
												fromVertex_edge.addEdge(edge1);
												vertex.addEdge(edge11);
												lineRecord.add(edge1);
												lineRecord.add(edge11);
											}
											if(!arrStr[4].equals("--")){
												int time2 = 60*(Integer.parseInt(lasTime2[0])-Integer.parseInt(pretime2[0]))+Integer.parseInt(lasTime2[1])-Integer.parseInt(pretime2[1]);
												Edge edge2 = new Edge(fromVertex_edge2,vertex,time2,transport);
												Edge edge22 = new Edge(vertex,fromVertex_edge,time2,transport);
												edge2.lineIndex = (lineIndex == 10)? 102:112;
												edge22.lineIndex = (lineIndex == 10)? 102:112;
												edge2.setTransportation(transport);
												edge22.setTransportation(transport);
												fromVertex_edge2.addEdge(edge2);
												vertex.addEdge(edge22);
												lineRecord2.add(edge2);
												lineRecord2.add(edge22);
											}
										}else {
											int time = 60*(Integer.parseInt(lasTime[0])-Integer.parseInt(pretime[0]))+Integer.parseInt(lasTime[1])-Integer.parseInt(pretime[1]);
											Edge edge = new Edge(fromVertex_edge,vertex,time,transport);
											Edge edgee = new Edge(vertex,fromVertex_edge,time,transport);
											edge.lineIndex = lineIndex;
											edgee.lineIndex = lineIndex;
											edge.setTransportation(transport);
											edgee.setTransportation(transport);
											fromVertex_edge.addEdge(edge);
											vertex.addEdge(edgee);
											lineRecord.add(edge);
											lineRecord.add(edgee);
										}


									}
									if(lineIndex == 10 || lineIndex == 11){
										if(!arrStr[3].equals("--")){
											pretime = arrStr[3].split(":");
											fromVertex_edge = vertex;
										}
										if(!arrStr[4].equals("--")){
											pretime2 = arrStr[4].split(":");
											fromVertex_edge2 = vertex;
										}
									}else {
										pretime = arrStr[3].split(":");
										fromVertex_edge = vertex;
									}


								}
								//System.out.println(arrStr[0]+" "+arrStr[1]+" "+arrStr[2]+" ");
								break;
						}
					}


				}
//				System.out.println(graph.vertexList.size()); //324
//				for(Address a:graph.vertexList){
//					System.out.println(a.getAddress());
//				}
				//create the graph from file
				//graph = new Graph();//读文件生成的图
				
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	
	public ReturnValue travelRoute(Map<String, Object>params)
	{
		String startAddress = 	params.get("startAddress").toString();	
		String startLongitude = params.get("startLongitude").toString();
		String startLatitude = params.get("startLatitude").toString();
		String endAddress = params.get("endAddress").toString();
		String endLongitude = params.get("endLongitude").toString();
		String endLatitude = params.get("endLatitude").toString();
		String choose = params.get("choose").toString();
		
		System.out.println(startAddress);
		System.out.println(startLongitude);
		System.out.println(startLatitude);
		System.out.println(endAddress);
		System.out.println(endLongitude);
		System.out.println(endLatitude);
		System.out.println(choose);
		
		Address startPoint = new Address(startAddress, startLongitude, startLatitude);
		Address endPoint = new Address(endAddress, endLongitude, endLatitude);
		List<Address> addresses=new ArrayList<Address>();
		double time = 0;
		switch (choose)
		{
		case "1":
			//步行最少
			Address S = graph.getVertexList().get(0);
			Address T = graph.getVertexList().get(graph.getVertexList().size()-1);

			dijkstra(graph,S,T);
			Address tmp = T.getPi();
			while (tmp.getPi() != null){
				addresses.add(0,tmp);
				tmp = tmp.getPi();
			}
			addresses.add(T);
			time = T.getD();
			break;
		case "2":
			startPoint = graph.getVertexList().get(0);
			endPoint = graph.getVertexList().get(graph.getVertexList().size()-1);

			dijkstra_least_change(graph,startPoint,endPoint);
			Address tmp2 = endPoint.getPi();
			while (tmp2.getPi() != null){
				addresses.add(0,tmp2);
				tmp2 = tmp2.getPi();
			}
			addresses.add(endPoint);
			time = endPoint.getD();//////////////////
			break;
		case "3":
			//时间最短:
			break;
		default:
			break;
		}
		
		ReturnValue returnValue=new ReturnValue();
		returnValue.setStartPoint(startPoint);
		returnValue.setEndPoint(endPoint);
		returnValue.setSubwayList(addresses);
		returnValue.setMinutes(time);
		return returnValue;
	}

	public void dijkstra(Graph G, Address S, Address T){
		for(Address a : G.getVertexList()){
			a.setD(999999);
		}
		S.setD(0);//应该要把S放在最前面！还没写/////////////////////!!!!!!!!!!!!
		ArrayList<Address> SS = new ArrayList<>();
		ArrayList<Address> QQ = (ArrayList<Address>) G.getVertexList().clone();
		QQ.remove(S);
		QQ.add(0,S);//S放在第一个

		while (QQ.size() != 0){
			Address u = EXTEACT_MIN(QQ);
			if(QQ.size() == 0)
				return;
			SS.add(u);
			if(u == T){///////////////////////////////////////////////////////
				return;
			}
			for(Edge edge : u.getEdgesAfter()){
				if(edge.getNextVertex().getD() > u.getD() + edge.getWeight()){
					//edge.nextVertex.d = u.d + edge.weight;
					DOWNCREASR_KEY(QQ,edge.getNextVertex(),(u.getD() + edge.getWeight()));
					edge.getNextVertex().setPi(u);
					edge.getNextVertex().setFromEdge(edge);
				}
			}
		}
	}

	public void dijkstra_least_change(Graph G, Address S, Address T){
		for(Address a : G.getVertexList()){
			a.setD(999999);
		}
		S.setD(0);
		ArrayList<Address> SS = new ArrayList<>();
		ArrayList<Address> QQ = (ArrayList<Address>) G.getVertexList().clone();
		QQ.remove(S);
		QQ.add(0,S);//S放在第一个

		while (QQ.size() != 0){
			Address u = EXTEACT_MIN(QQ);
			if(QQ.size() == 0)
				return;
			SS.add(u);
			if(u == T){///////////////////////////////////////////////////////
				return;
			}
			if(QQ.size() == 1){
				int debug = 0;
			}
			for(Edge edge : u.getEdgesAfter()){
				int weight = 0;
				if(edge.getPreviousVertex().getFromEdge() != null && edge.getTransportation() == edge.getPreviousVertex().getFromEdge().getTransportation() && edge.lineIndex == edge.getPreviousVertex().getFromEdge().lineIndex){
					if(QQ.size() <= 1){
						int debug = 1;
					}

				}else {
					if(QQ.size() <= 1){
						int debug = 1;
					}
					weight = 1;
				}

				if(edge.getNextVertex().getD() > u.getD() + weight){
					//edge.nextVertex.d = u.d + edge.weight;
					DOWNCREASR_KEY(QQ,edge.getNextVertex(),(u.getD() + weight));
					edge.getNextVertex().setPi(u);
					edge.getNextVertex().setFromEdge(edge);
				}
			}
		}
	}


	public Address EXTEACT_MIN(ArrayList<Address> Q){
		if(Q.size() < 1){
			System.out.println("heap underflow");
			return null;
		}
		Address min = Q.get(0);
		String debug = min.getAddress();///////
		Q.remove(0);
		if(Q.size() == 0){
			return null;
		}
		Q.add(0,Q.get(Q.size()-1));
		Q.remove(Q.size()-1);
		MIN_HEAPIFY(Q,Q.get(0));
		return min;
	}
	private void BUILD_MIN_HEAP(ArrayList<Address> Q){
		for(int i = Q.size()/2 ; i >= 0 ; i--){
			MIN_HEAPIFY(Q,Q.get(i));
		}
	}
	private void DOWNCREASR_KEY(ArrayList<Address> Q, Address address, double key){
		if(key > address.getD()) {
			return;
		}
		address.setD(key);
		int i = Q.indexOf(address);
		while (i > 0 && PARENT(Q,(i+1)).getD() > Q.get(i).getD()){
			Address parentTmp = PARENT(Q,(i+1));///////////////////////exchange parent&address
			int replaceNum1 = Q.indexOf(parentTmp);
			int replaceNum2 = Q.indexOf(address);
			Q.add(replaceNum1,Q.get(replaceNum2));
			Q.add(replaceNum2+1,Q.get(replaceNum1+1));
			Q.remove(replaceNum1+1);
			Q.remove(replaceNum2+1);
			i = replaceNum1;
		}
	}

	private void MIN_HEAPIFY(ArrayList<Address> Q, Address address){
		int index = Q.indexOf(address) + 1;
		Address l = null;
		Address r = null;
		int ifReturn = 0;
		if((2*index - 1) < Q.size()){
			l = LEFT(Q,index);
		}else {
			ifReturn = 1;
		}
		if(2*index < Q.size()){
			r = RIGHT(Q,index);
		}else {
			ifReturn = 1;
		}

		Address minest = address;
		if(l != null && l.getD() < address.getD()){
			minest = l;
		}
		if(r != null && r.getD() < address.getD()){
			minest = r;
		}

		if(minest != address){
			int replaceNum2 = Q.indexOf(minest);
			int replaceNum1 = Q.indexOf(address);
			Q.add(replaceNum1,Q.get(replaceNum2));
			Q.add(replaceNum2+1,Q.get(replaceNum1+1));
			Q.remove(replaceNum1+1);
			Q.remove(replaceNum2+1);
			if(ifReturn == 1){
				return;
			}
			MIN_HEAPIFY(Q,Q.get(replaceNum2));
		}
		if(ifReturn == 1){
			return;
		}
	}
	private Address PARENT(ArrayList<Address> Q, int index){return Q.get(index/2 - 1);}
	private Address LEFT(ArrayList<Address> Q, int index){return Q.get(2*index - 1);}
	private Address RIGHT(ArrayList<Address> Q, int index){return Q.get(2*index+1 - 1);}
}
