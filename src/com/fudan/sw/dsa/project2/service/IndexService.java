package com.fudan.sw.dsa.project2.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import com.fudan.sw.dsa.project2.bean.*;
import org.springframework.stereotype.Service;

import com.fudan.sw.dsa.project2.constant.FileGetter;
import sun.misc.Queue;
import sun.security.krb5.internal.crypto.Aes128;

/**
 * this class is what you need to complete
 * @author zjiehang
 *
 */
@Service
public class IndexService 
{
	//transport 0：走路 1：地铁
	//the subway graph
	private Graph graph = null;
	private double EARTH_RADIUS = 6378.137;
	
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

				ArrayList<Edge> lineRecord = new ArrayList<>();
				ArrayList<Edge> lineRecord2 = new ArrayList<>();
				ArrayList<Address> vertexUnionRecord = new ArrayList<>();
				ArrayList<Address> vertexUnionRecord2 = new ArrayList<>();
				while((line=bufferedReader.readLine())!=null)
				{
					if(line.indexOf("Line") != -1){
						lineRecord = new ArrayList<>();
						vertexUnionRecord = new ArrayList<>();
						graph.getUndergroundLines().add(lineRecord);
						graph.getVertxeUnion().add(vertexUnionRecord);
						transport = 1; //地铁
						lineIndex = Integer.parseInt(line.substring(4));
						fromVertex_edge = null;
						pretime = null;
						pretime2 = null;
						fromVertex_edge2= null;
						if(lineIndex == 10 || lineIndex == 11){
							lineRecord2 = new ArrayList<>();
							vertexUnionRecord2 = new ArrayList<>();
							graph.getUndergroundLines().add(lineRecord2);
							graph.getVertxeUnion().add(vertexUnionRecord2);
						}
					}else {
						switch (transport){
							case 1://地铁
								String[] arrStr = line.split("\\s+");
								int ifExisted = 0;
								for(Address address : graph.getVertexList()){
									if(address.getAddress().equals(arrStr[0])){ //address已经在图里出现了
										address.getThisVertexHasLine().add(lineIndex);
										ifExisted = 1;
										if(fromVertex_edge != null){
											lasTime = arrStr[3].split(":");
											if(lineIndex == 10 || lineIndex == 11){
												lasTime2 = arrStr[4].split(":");
												if(!arrStr[3].equals("--")){
													vertexUnionRecord.add(address);
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
													vertexUnionRecord2.add(address);
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
												vertexUnionRecord.add(address);
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
									vertex.getThisVertexHasLine().add(lineIndex);

									if(fromVertex_edge != null){
										lasTime = arrStr[3].split(":");
										if(lineIndex == 10 || lineIndex == 11){
											lasTime2 = arrStr[4].split(":");
											if(!arrStr[3].equals("--")){
												vertexUnionRecord.add(vertex);
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
												vertexUnionRecord2.add(vertex);
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
											vertexUnionRecord.add(vertex);
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
//				for (Address a : graph.getVertexList()){
//					if(a.getAddress().equals("上大路"))
//						System.out.println("上大路"+graph.getVertexList().indexOf(a));
//					if(a.getAddress().equals("五角场"))
//						System.out.println("五角场"+graph.getVertexList().indexOf(a));
////					if(a.getAddress().equals("虹桥火车站"))
////						System.out.println("虹桥火车站"+graph.getVertexList().indexOf(a));
////					if(a.getAddress().equals("徐泾北城"))
////						System.out.println("徐泾北城"+graph.getVertexList().indexOf(a));
//
//				}
//
//				System.out.println("=====================");
//				for(ArrayList<Edge> al : graph.getUndergroundLines()){
//					System.out.println(al.get(0).lineIndex);
//					int k = 1;
//					for(Edge e:al){
//						if(k==1){
//							System.out.println(e.getPreviousVertex().getAddress()+"---"+e.getNextVertex().getAddress());
//							k = 2;
//						}else {
//							k=1;
//							continue;
//						}
//
//					}
//				}
				
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
			addresses.clear();
			Address startVertex = null; //= graph.getVertexList().get(0)
			Address endVertex = null;
			double minDistance1 = 99999999;
			double minDisrance2 = 99999999;
			double debug = 0;
			for(Address address : graph.getVertexList()){
				if(getDistance(startPoint,address) < minDistance1){
					startVertex = address;
					minDistance1 = getDistance(startPoint,address);
				}
				if(getDistance(endPoint,address) < minDisrance2){
					endVertex = address;
					minDisrance2 = getDistance(endPoint,address);
				}

			}

			dijkstra(graph,startVertex,endVertex);
			Address tmp = endVertex;
			while (tmp != null){
				addresses.add(0,tmp);
				tmp = tmp.getPi();
			}
			System.out.println(endVertex.getD());
			time = endVertex.getD() + (int)((minDistance1 + minDisrance2) / (5000 / 60)) ;

			break;
		case "2":
			//换乘最少
			addresses.clear();
			startVertex = graph.getVertexList().get(151);
			endVertex = graph.getVertexList().get(219);

			dijkstra(graph,startVertex,endVertex);
			Address tmp2 = endVertex;
			while (tmp2 != null){
				addresses.add(0,tmp2);
				tmp2 = tmp2.getPi();
			}
			System.out.println(endVertex.getD());
			time = endVertex.getD() ;

//			//dijkstra_least_change(graph,startPoint,endPoint);
//			Address tmp2 = endPoint.getPi();
//			while (tmp2.getPi() != null){
//				addresses.add(0,tmp2);
//				tmp2 = tmp2.getPi();
//			}
//			addresses.add(endPoint);
//			time = endPoint.getD();//////////////////
//			System.out.println(graph.getVertexList().get(10).getD());
//			System.out.println(graph.getVertexList().get(55).getD());
//			System.out.println(graph.getVertexList().get(227).getD());
//			System.out.println(graph.getVertexList().get(315).getD());
			break;
		case "3":
			//时间最短:
			addresses.clear();

			for(Address address : graph.getVertexList()){
				Edge edge = new Edge(startPoint,address,getDistance(startPoint,address)/(5000/60),0);
				startPoint.getEdgesAfter().add(edge);
				Edge edge2 = new Edge(address,endPoint,getDistance(endPoint,address)/(5000/60),0);
				address.getEdgesAfter().add(0,edge2);

			}
			Edge edge0 = new Edge(startPoint,endPoint,getDistance(startPoint,endPoint)/(5000/60),0);
			startPoint.getEdgesAfter().add(edge0);
			graph.getVertexList().add(startPoint);
			graph.getVertexList().add(endPoint);
			dijkstra(graph,startPoint,endPoint);
			Address tmp3 = endPoint.getPi();
			while (tmp3 != null){
				addresses.add(0,tmp3);
				tmp3 = tmp3.getPi();
			}
			addresses.remove(0);
			time = (int)endPoint.getD() ;
			graph.getVertexList().remove(startPoint);
			graph.getVertexList().remove(endPoint);
			for(Address address : graph.getVertexList()){
				address.getEdgesAfter().remove(0);

			}

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
			a.setPi(null);
			a.setFromEdge(null);
		}
		S.setD(0);
		ArrayList<Address> SS = new ArrayList<>();
		ArrayList<Address> QQ = (ArrayList<Address>) G.getVertexList().clone();

		QQ.remove(S);
		QQ.add(0,S);//S放在第一个

		while (QQ.size() != 0){


			Address u = EXTEACT_MIN(QQ);
			SS.add(u);
			if(u == T){///////////////////////////////////////////////////////
				return;
			}

			if(QQ.size() == 0)
				return;
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

//	public void dijkstra_least_change(Graph G, Address S, Address T){
//		for(Address a : G.getVertexList()){
//			a.setD(999999);
//			a.setColor(0);
//			a.setChangeTimes(999999);
//			a.setPiList(new ArrayList<Address>());
//		}
//		BFS(graph,S);
//
//
//
//
//		S.setD(0);
//
//		ArrayList<Address> QQ = (ArrayList<Address>) G.getVertexList().clone();
//		QQ.remove(S);
//		QQ.add(0,S);//S放在第一个
//
//
//		while (QQ.size() != 0){
//			Address u = EXTEACT_MIN(QQ);
//			if(QQ.size() == 0)
//				return;
//			SS.add(u);
//
//			for(Edge edge : u.getEdgesAfter()){
//				int weight = 0;
//				if(edge.getPreviousVertex().getFromEdge() != null && edge.getTransportation() == edge.getPreviousVertex().getFromEdge().getTransportation() && edge.lineIndex == edge.getPreviousVertex().getFromEdge().lineIndex){
//
//				}else {
//					weight = 1;
//				}
//
//				if(edge.getNextVertex().getD() > u.getD() + weight){
//					//edge.nextVertex.d = u.d + edge.weight;
//					DOWNCREASR_KEY(QQ,edge.getNextVertex(),(u.getD() + weight));
//					edge.getNextVertex().setPi(u);
//					edge.getNextVertex().setFromEdge(edge);
//				}
//			}
//		}
//	}

	public void BFS(Graph G, Address S){
		S.setColor(1);//grey
		S.setChangeTimes(0);
		LinkedList<Address> Q = new LinkedList<Address>();//等同队列
		Q.offer(S);
		while (!Q.isEmpty()){
			Address u = Q.poll();
			for(Edge edge : u.getEdgesAfter()){
				if(edge.getNextVertex().getColor() == 0){//////可能会漏掉也许这个判断要删掉
					int weight = 0;
					if(edge.getPreviousVertex().getFromEdge() != null && edge.getTransportation() == edge.getPreviousVertex().getFromEdge().getTransportation() && edge.lineIndex == edge.getPreviousVertex().getFromEdge().lineIndex){

					}else {
						weight = 1;
					}

					if(edge.getNextVertex().getChangeTimes() >= u.getChangeTimes() + weight){
						if(edge.getNextVertex().getChangeTimes() == u.getChangeTimes() + weight){
							edge.getNextVertex().getPiList().add(u);
						}else {
							edge.getNextVertex().setPiList(new ArrayList<>());
							edge.getNextVertex().getPiList().add(u);
						}

						edge.getNextVertex().setChangeTimes(u.getChangeTimes() + weight);
						Q.offer(edge.getNextVertex());
					}
				}
			}
			u.setColor(3);//black
		}

	}

	public Address EXTEACT_MIN(ArrayList<Address> Q){
		if(Q.size() < 1){
			System.out.println("heap underflow");
			return null;
		}
		Address min = Q.get(0);
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
		}else {
			minest = address;
		}
		if(r != null && r.getD() < minest.getD()){
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

	private double rad(double d){
		return d * Math.PI / 180.0;
	}
	private double getDistance(Address aAdderss, Address bAddress){
		double lat1 = aAdderss.getLatitude();
		double lng1 = aAdderss.getLongitude();
		double lat2 = bAddress.getLatitude();
		double lng2 = bAddress.getLongitude();
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = rad(lat1) - rad(lat2);
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000d) / 10000d;
		s = s*1000;
		return s;
	}
}
