package com.fudan.sw.dsa.project2.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import com.fudan.sw.dsa.project2.bean.*;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
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
				String stationName = "";
				String stationName2 = "";
				Graph lineGraph = null;
				Graph lineGraph2 = null;


				ArrayList<Edge> lineRecord = new ArrayList<>();
				ArrayList<Edge> lineRecord2 = new ArrayList<>();
				ArrayList<Address> vertexUnionRecord = new ArrayList<>();
				ArrayList<Address> vertexUnionRecord2 = new ArrayList<>();
				int isFirstVertex = 1;
				while((line=bufferedReader.readLine())!=null)
				{
					if(line.indexOf("Line") != -1){
						if(!stationName.equals("")){
							graph.getStationNameArrL().add(stationName);
							stationName = "";
						}
						if(!stationName2.equals("")){
							graph.getStationNameArrL().add(stationName2);
							stationName2 = "";
						}
						isFirstVertex = 1;
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
						lineGraph = new Graph();
						graph.getRoutes().add(lineGraph);
						if(lineIndex == 10 || lineIndex == 11){
							lineGraph2 = new Graph();
							graph.getRoutes().add(lineGraph2);
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
													lineGraph.appendVertex(address);
													vertexUnionRecord.add(address);
													stationName += "&" + arrStr[0];
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
													lineGraph2.appendVertex(address);
													vertexUnionRecord2.add(address);
													stationName2 += "&" + arrStr[0];
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
												stationName += "&" + arrStr[0];
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
												lineGraph.appendVertex(address);
											}

										}
										if(lineIndex == 10 || lineIndex == 11){
											if(!arrStr[3].equals("--")){
												pretime = arrStr[3].split(":");
												fromVertex_edge = address;
												if(isFirstVertex == 1){
													vertexUnionRecord.add(address);
													lineGraph.appendVertex(address);
													stationName += "&" + arrStr[0];
												}
											}
											if(!arrStr[4].equals("--")){
												pretime2 = arrStr[4].split(":");
												fromVertex_edge2 = address;
												if(isFirstVertex == 1){
													vertexUnionRecord2.add(address);
													lineGraph2.appendVertex(address);
													stationName2 +="&" +  arrStr[0];
												}
											}

										}else {
											pretime = arrStr[3].split(":");
											fromVertex_edge = address;
											if(isFirstVertex == 1){
												vertexUnionRecord.add(address);
												lineGraph.appendVertex(address);
												stationName += "&" + arrStr[0];
											}
										}
										if(isFirstVertex == 1){
											isFirstVertex++;
										}

										break;
									}
								}
								if(ifExisted == 0){
									Address vertex = new Address(arrStr[0],arrStr[1],arrStr[2]);//节点不曾出现就新建节点
									graph.appendVertex(vertex);
									vertex.getThisVertexHasLine().add(lineIndex);
									graph.getMap().put(arrStr[0],vertex);
									graph.getMapMatrixIndex().put(arrStr[0],graph.getVertexNum()-1);//从0开始
//									System.out.println("num:" + graph.getVertexNum());

									if(fromVertex_edge != null){
										lasTime = arrStr[3].split(":");
										if(lineIndex == 10 || lineIndex == 11){
											lasTime2 = arrStr[4].split(":");
											if(!arrStr[3].equals("--")){
												vertexUnionRecord.add(vertex);
												stationName += "&" + arrStr[0];
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
												lineGraph.appendVertex(vertex);
											}
											if(!arrStr[4].equals("--")){
												vertexUnionRecord2.add(vertex);
												stationName2 +="&" +  arrStr[0];
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
												lineGraph2.appendVertex(vertex);
											}
										}else {
											vertexUnionRecord.add(vertex);
											stationName += "&" + arrStr[0];
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
											lineGraph.appendVertex(vertex);
										}


									}

									if(lineIndex == 10 || lineIndex == 11){
										if(!arrStr[3].equals("--")){
											pretime = arrStr[3].split(":");
											fromVertex_edge = vertex;
											if(isFirstVertex == 1){
												vertexUnionRecord.add(vertex);
												lineGraph.appendVertex(vertex);
												stationName += "&" + arrStr[0];
											}
										}
										if(!arrStr[4].equals("--")){
											pretime2 = arrStr[4].split(":");
											fromVertex_edge2 = vertex;
											if(isFirstVertex == 1){
												vertexUnionRecord2.add(vertex);
												lineGraph2.appendVertex(vertex);
												stationName2 +="&" +  arrStr[0];
											}
										}
									}else {
										pretime = arrStr[3].split(":");
										fromVertex_edge = vertex;
										if(isFirstVertex == 1){
											vertexUnionRecord.add(vertex);
											stationName +="&" +  arrStr[0];
											lineGraph.appendVertex(vertex);
										}
									}

									if(isFirstVertex == 1){
										isFirstVertex++;
									}


								}

								break;

						}
					}


				}
				graph.getStationNameArrL().add(stationName);

//======================================for test===============================
				System.out.println(graph.getRoutes().size());
				for (Graph g : graph.getRoutes()){
					for(Address a:g.getVertexList()){
						System.out.print(" "+a.getAddress()+" ");
					}
					System.out.print("\n\n\n");
				}
//				System.out.println(graph.getStationNameArrL().get(0).indexOf("莘庄")+"？？？？？？？");
//				for(String s : graph.getStationNameArrL()){
//					System.out.println(s+"\n\n");
//				}
			//	System.out.println("?????"+"&新江湾城&殷高东路&三门路&江湾体育场&五角场&国权路&同济大学&四平路&邮电新村&海伦路&四川北路&天潼路&南京东路&豫园&老西门&新天地&陕西南路&上海图书馆&交通大学&虹桥路&宋园路&伊犁路&水城路&龙溪路&上海动物园&虹桥1号航站楼&虹桥2号航站楼&虹桥火车站".substring(72,136));
//				for(ArrayList<Edge> al : graph.getUndergroundLines()){
//					for(Edge e : al){
//						System.out.println(e.getPreviousVertex().getAddress()+"  "+e.getNextVertex().getAddress());
//					}
//					System.out.println("\n\n");
//				}
//
//				int ddd = 1;
//				for(ArrayList<Address> debug:graph.getVertxeUnion()){
//					System.out.println(ddd+":");
//					for(Address debugg:debug){
//						System.out.println(debugg.getAddress());
//					}
//					System.out.println("\n\n\n");
//					ddd++;
//				}

				for(int i = 0; i < 17; i++){ //只算地铁的话是只有17
					int[][] matrix = new int[graph.getVertexNum()][graph.getVertexNum()];
					for(Address address : graph.getVertxeUnion().get(i)){
						for(Address relation : graph.getVertxeUnion().get(i)){
							if(relation == address)
								continue;
							dijkstra(graph.getRoutes().get(i),address,relation);
							matrix[graph.getMapMatrixIndex().get(address.getAddress())][graph.getMapMatrixIndex().get(relation.getAddress())] = (int)relation.getD();
						}
					}

					graph.getMatrixChangeLeastArrList().add(matrix);
				}
				graph.setMatrixTogether(matrixAddAll(graph.getMatrixChangeLeastArrList()));

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
			System.out.println("距离1："+minDistance1);
			System.out.println("距离2："+minDisrance2);

			break;
		case "2":
			//换乘最少
			long t1 = System.currentTimeMillis();
			addresses.clear();
			Address startVertex2 = null; //= graph.getVertexList().get(0)
			Address endVertex2 = null;
			double minDistance12 = 99999999;
			double minDisrance22 = 99999999;
			for(Address address : graph.getVertexList()){
				if(getDistance(startPoint,address) < minDistance12){
					startVertex2 = address;
					minDistance12 = getDistance(startPoint,address);
				}
				if(getDistance(endPoint,address) < minDisrance22){
					endVertex2 = address;
					minDisrance22 = getDistance(endPoint,address);
				}

			}

			addresses = findCase2Route(graph,startVertex2,endVertex2);
//			findCase2Route(graph,startVertex2,endVertex2);
//			Address tmp2 = endVertex2;
//			while (tmp2 != null){
//				addresses.add(0,tmp2);
//				tmp2 = tmp2.getPi();
//			}
			System.out.println(endVertex2.getD());
//
//			System.out.println("距离1："+minDistance12);
//			System.out.println("距离2："+minDisrance22);

			time = endVertex2.getD() + (int)((minDistance12 + minDisrance22) / (5000 / 60));
			long t2 = System.currentTimeMillis() - t1;
			System.out.println("case2用时： "+t2+" ms");
			endVertex2.setD(999999);



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
			double distance2 = getDistance(addresses.get(addresses.size()-1),endPoint);//addresses.get(1).getFromEdge().getWeight();
			double diatance1 = getDistance(startPoint,addresses.get(0));//endPoint.getFromEdge().getWeight();////////////////
			graph.getVertexList().remove(startPoint);
			graph.getVertexList().remove(endPoint);
			for(Address address : graph.getVertexList()){
				address.getEdgesAfter().remove(0);

			}
			System.out.println("距离1："+diatance1);
			System.out.println("距离2："+distance2);
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

	public List<Address> findCase2Route(Graph G, Address S, Address T){
		List<Address> addresses=new ArrayList<Address>();
		Address tmp;
		double time = 999999;
		int[] changeRoute = {999999,999999,999999};
		Address[] changeStation = new Address[2];
		int Sindex = G.getMapMatrixIndex().get(S.getAddress());
		int Tindex = G.getMapMatrixIndex().get(T.getAddress());
		if(G.getMatrixTogether()[Sindex][Tindex] > 0){ //直接可以到达
			for(int[][] m : G.getMatrixChangeLeastArrList()){
				if(m[Sindex][Tindex] > 0){
					//G.getMatrixChangeLeastArrList().indexOf(m)
					if(time > m[Sindex][Tindex]){
						time = m[Sindex][Tindex];
						changeRoute[0] = G.getMatrixChangeLeastArrList().indexOf(m);
					}
				}
			}

			dijkstra(G.getRoutes().get(changeRoute[0]),S,T);
			tmp = T;
			while (tmp != null){
				addresses.add(0,tmp);
				tmp = tmp.getPi();
			}

//
//			int firstIn = G.getStationNameArrL().get(changeRoute[0]).indexOf(S.getAddress());
//			int lastIn = G.getStationNameArrL().get(changeRoute[0]).indexOf(T.getAddress()) + T.getAddress().length() + 1;/////////maybe wrong
//			int firstIn2 = G.getStationNameArrL().get(changeRoute[0]).indexOf(S.getAddress()) + S.getAddress().length() + 1;
//			int lastIn2 = G.getStationNameArrL().get(changeRoute[0]).indexOf(T.getAddress());/////////maybe wrong
//
//			if(G.getStationNameArrL().get(changeRoute[0]).indexOf(S.getAddress()) < G.getStationNameArrL().get(changeRoute[0]).indexOf(T.getAddress())){
//				String routeStr = (lastIn < G.getStationNameArrL().get(changeRoute[0]).length())?G.getStationNameArrL().get(changeRoute[0]).substring(firstIn,lastIn):G.getStationNameArrL().get(changeRoute[0]).substring(firstIn);
//				String[] routeLittle = routeStr.split("&");
//				for (int i = 0; i < routeLittle.length; i++){
//					addresses.add((Address) G.getMap().get(routeLittle[i]));
//				}
//			}else {
//				String routeStr = (firstIn2 < G.getStationNameArrL().get(changeRoute[0]).length())?G.getStationNameArrL().get(changeRoute[0]).substring(lastIn2,firstIn2):G.getStationNameArrL().get(changeRoute[0]).substring(lastIn2);
//				String[] routeLittle = routeStr.split("&");
//				for (int i = 0; i < routeLittle.length; i++){
//					addresses.add(0,(Address) G.getMap().get(routeLittle[i]));
//				}
//			}

		}else{
			//转乘1次
			for(int i = 0; i < G.getMatrixChangeLeastArrList().size(); i++){
				for(int j = 0; j <G.getMatrixChangeLeastArrList().size(); j++){
					//换乘1次
					int timeTmp = 0;
					for(int k = 0; k < G.getMatrixTogether().length; k++){
						if(G.getMatrixChangeLeastArrList().get(i)[Sindex][k] == 0 || G.getMatrixChangeLeastArrList().get(j)[k][Tindex] == 0)
							timeTmp = 0;
						else {
							timeTmp = G.getMatrixChangeLeastArrList().get(i)[Sindex][k] + G.getMatrixChangeLeastArrList().get(j)[k][Tindex];
							if(timeTmp < time){
								time = timeTmp;
								changeRoute[0] = i;
								changeRoute[1] = j;//i号线转j号线
								changeStation[0] = G.getVertexList().get(k);///////////maybe wrong
							}
						}
					}
				}

			}
			if(!(changeRoute[0] == 999999 && changeRoute[1] == 999999)){
				dijkstra(G.getRoutes().get(changeRoute[0]),S,changeStation[0]);
				tmp = changeStation[0];
				while (tmp != null){
					addresses.add(0,tmp);
					tmp = tmp.getPi();
				}
				int start = addresses.size();
				dijkstra(G.getRoutes().get(changeRoute[1]),changeStation[0],T);
				tmp = T;
				while (tmp != changeStation[0]){
					addresses.add(start,tmp);
					 tmp = tmp.getPi();
				}

//				int firstIn = G.getStationNameArrL().get(changeRoute[0]).indexOf(S.getAddress());
//				int lastIn = G.getStationNameArrL().get(changeRoute[0]).indexOf(changeStation[0].getAddress()) + changeStation[0].getAddress().length() + 1;/////////maybe wrong
//				int firstIn2 = G.getStationNameArrL().get(changeRoute[0]).indexOf(S.getAddress()) + S.getAddress().length() + 1;
//				int lastIn2 = G.getStationNameArrL().get(changeRoute[0]).indexOf(changeStation[0].getAddress());/////////maybe wrong
//
//				if(G.getStationNameArrL().get(changeRoute[0]).indexOf(S.getAddress()) < G.getStationNameArrL().get(changeRoute[0]).indexOf(changeStation[0].getAddress())){
//					String routeStr = (lastIn < G.getStationNameArrL().get(changeRoute[0]).length())?G.getStationNameArrL().get(changeRoute[0]).substring(firstIn,lastIn):G.getStationNameArrL().get(changeRoute[0]).substring(firstIn);
//					String[] routeLittle = routeStr.split("&");
//					for (int ii = 0; ii < routeLittle.length-1; ii++){
//						addresses.add((Address) G.getMap().get(routeLittle[ii]));
//					}
//				}else {
//					String routeStr = (firstIn2 < G.getStationNameArrL().get(changeRoute[0]).length())?G.getStationNameArrL().get(changeRoute[0]).substring(lastIn2,firstIn2):G.getStationNameArrL().get(changeRoute[0]).substring(lastIn2);
//					String[] routeLittle = routeStr.split("&");
//					for (int ii = 0; ii < routeLittle.length-1; ii++){
//						addresses.add(0,(Address) G.getMap().get(routeLittle[ii]));
//					}
//				}
//
//
//				int firstIn_ = G.getStationNameArrL().get(changeRoute[1]).indexOf(changeStation[0].getAddress());
//				int lastIn_ = G.getStationNameArrL().get(changeRoute[1]).indexOf(T.getAddress()) + T.getAddress().length() + 1;/////////maybe wrong
//				int firstIn2_ = G.getStationNameArrL().get(changeRoute[1]).indexOf(changeStation[0].getAddress()) + changeStation[0].getAddress().length() + 1;
//				int lastIn2_ = G.getStationNameArrL().get(changeRoute[1]).indexOf(T.getAddress());/////////maybe wrong
//				int start_ = addresses.size();
//
//				if(G.getStationNameArrL().get(changeRoute[1]).indexOf(changeStation[0].getAddress()) < G.getStationNameArrL().get(changeRoute[1]).indexOf(T.getAddress())){
//					String routeStr = (lastIn_ < G.getStationNameArrL().get(changeRoute[1]).length())?G.getStationNameArrL().get(changeRoute[1]).substring(firstIn_,lastIn_):G.getStationNameArrL().get(changeRoute[1]).substring(firstIn_);
//					String[] routeLittle = routeStr.split("&");
//					for (int ii = 0; ii < routeLittle.length; ii++){
//						addresses.add((Address) G.getMap().get(routeLittle[ii]));
//					}
//				}else {
//					String routeStr = (firstIn2_ < G.getStationNameArrL().get(changeRoute[1]).length())?G.getStationNameArrL().get(changeRoute[1]).substring(lastIn2_,firstIn2_):G.getStationNameArrL().get(changeRoute[1]).substring(lastIn2_);
//					String[] routeLittle = routeStr.split("&");
//					for (int ii = 0; ii < routeLittle.length; ii++){
//						addresses.add(start_,(Address) G.getMap().get(routeLittle[ii]));
//					}
//				}
			}


			if(changeRoute[0] == 999999 && changeRoute[1] == 999999){
				time = 999999;
				//换乘2次
				for(int i = 0; i < G.getMatrixChangeLeastArrList().size(); i++){
					for(int j = 0; j < G.getMatrixChangeLeastArrList().size(); j++){
						for(int q = 0; q < G.getMatrixChangeLeastArrList().size(); q++){
							int lineTmp = 0;
							int timeTmp = 0;
							int[] newLine = new int[G.getMatrixTogether().length];
							int[] firstChangeStationIndex = new int[G.getMatrixTogether().length];
							for(int o = 0; o < newLine.length; o++){
								newLine[o] = 999999;
							}
							for(int l = 0; l < G.getMatrixTogether().length; l++){ //newline 的 index
								for(int k = 0; k < G.getMatrixTogether().length; k++){
									if(G.getMatrixChangeLeastArrList().get(i)[Sindex][k] == 0 || G.getMatrixChangeLeastArrList().get(j)[k][l] == 0)
										lineTmp = 0;
									else{
										lineTmp = G.getMatrixChangeLeastArrList().get(i)[Sindex][k] + G.getMatrixChangeLeastArrList().get(j)[k][l];
										if(lineTmp < newLine[l]){  ///////maybe wrong
											newLine[l] = lineTmp;
											firstChangeStationIndex[l] = k;
										}
									}
								}
								if(newLine[l] == 999999)
									newLine[l] = 0;
							}

							for(int k = 0; k < G.getMatrixTogether().length; k++){
								if(newLine[k] == 0 || G.getMatrixChangeLeastArrList().get(q)[k][Tindex] == 0)
									timeTmp = 0;
								else {
									timeTmp = newLine[k] + G.getMatrixChangeLeastArrList().get(q)[k][Tindex];
									if(timeTmp < time){
										time = timeTmp;
										changeRoute[0] = i;
										changeRoute[1] = j;//i号线转j号线转q号线
										changeRoute[2] = q;
										changeStation[0] = G.getVertexList().get(firstChangeStationIndex[k]);///////////maybe wrong
										changeStation[1] = G.getVertexList().get(k);
									}
								}
							}

						}
					}
				}
				if(changeRoute[0] == 999999){
					//addresses = null;
					int dubug = 0;
					dijkstra(G,S,T);
					tmp = T;
					while (tmp != null){
						addresses.add(0,tmp);
						tmp = tmp.getPi();
					}
					return addresses;
				}
				dijkstra(G.getRoutes().get(changeRoute[0]),S,changeStation[0]);
				tmp = changeStation[0];
				while (tmp != null){
					addresses.add(0,tmp);
					tmp = tmp.getPi();
				}
				int start = addresses.size();
				dijkstra(G.getRoutes().get(changeRoute[1]),changeStation[0],changeStation[1]);
				tmp = changeStation[1];
				while (tmp != changeStation[0]){
					addresses.add(start,tmp);
					tmp = tmp.getPi();
				}
				int start_ = addresses.size();
				dijkstra(G.getRoutes().get(changeRoute[2]),changeStation[1],T);
				tmp = T;
				while (tmp != changeStation[1]){
					addresses.add(start_,tmp);
					tmp = tmp.getPi();
				}
//
//				int firstIn = G.getStationNameArrL().get(changeRoute[0]).indexOf(S.getAddress());
//				int lastIn = G.getStationNameArrL().get(changeRoute[0]).indexOf(changeStation[0].getAddress()) + changeStation[0].getAddress().length() + 1;/////////maybe wrong
//				int firstIn2 = G.getStationNameArrL().get(changeRoute[0]).indexOf(S.getAddress()) + S.getAddress().length() + 1;
//				int lastIn2 = G.getStationNameArrL().get(changeRoute[0]).indexOf(changeStation[0].getAddress());/////////maybe wrong
//
//				if(G.getStationNameArrL().get(changeRoute[0]).indexOf(S.getAddress()) < G.getStationNameArrL().get(changeRoute[0]).indexOf(changeStation[0].getAddress())){
//					String routeStr = (lastIn < G.getStationNameArrL().get(changeRoute[0]).length())?G.getStationNameArrL().get(changeRoute[0]).substring(firstIn,lastIn):G.getStationNameArrL().get(changeRoute[0]).substring(firstIn);
//					String[] routeLittle = routeStr.split("&");
//					for (int ii = 0; ii < routeLittle.length-1; ii++){
//						addresses.add((Address) G.getMap().get(routeLittle[ii]));
//					}
//				}else {
//					String routeStr = (firstIn2 < G.getStationNameArrL().get(changeRoute[0]).length())?G.getStationNameArrL().get(changeRoute[0]).substring(lastIn2,firstIn2):G.getStationNameArrL().get(changeRoute[0]).substring(lastIn2);
//					String[] routeLittle = routeStr.split("&");
//					for (int ii = 0; ii < routeLittle.length-1; ii++){
//						addresses.add(0,(Address) G.getMap().get(routeLittle[ii]));
//					}
//				}
//
//
//				int firstIn_ = G.getStationNameArrL().get(changeRoute[1]).indexOf(changeStation[0].getAddress());
//				int lastIn_ = G.getStationNameArrL().get(changeRoute[1]).indexOf(changeStation[1].getAddress()) + changeStation[1].getAddress().length() + 1;/////////maybe wrong
//				int firstIn2_ = G.getStationNameArrL().get(changeRoute[1]).indexOf(changeStation[0].getAddress()) + changeStation[0].getAddress().length() + 1;
//				int lastIn2_ = G.getStationNameArrL().get(changeRoute[1]).indexOf(changeStation[1].getAddress());/////////maybe wrong
//				int start_ = addresses.size();
//
//				if(G.getStationNameArrL().get(changeRoute[1]).indexOf(changeStation[0].getAddress()) < G.getStationNameArrL().get(changeRoute[1]).indexOf(changeStation[1].getAddress())){
//					String routeStr = (lastIn_ < G.getStationNameArrL().get(changeRoute[1]).length())?G.getStationNameArrL().get(changeRoute[1]).substring(firstIn_,lastIn_):G.getStationNameArrL().get(changeRoute[1]).substring(firstIn_);
//					String[] routeLittle = routeStr.split("&");
//					for (int ii = 0; ii < routeLittle.length-1; ii++){
//						addresses.add((Address) G.getMap().get(routeLittle[ii]));
//					}
//				}else {
//					String routeStr = (firstIn2_ < G.getStationNameArrL().get(changeRoute[1]).length())?G.getStationNameArrL().get(changeRoute[1]).substring(lastIn2_,firstIn2_):G.getStationNameArrL().get(changeRoute[1]).substring(lastIn2_);
//					String[] routeLittle = routeStr.split("&");
//					for (int ii = 0; ii < routeLittle.length-1; ii++){
//						addresses.add(start_,(Address) G.getMap().get(routeLittle[ii]));
//					}
//				}
//
//
//				int firstIn__ = G.getStationNameArrL().get(changeRoute[2]).indexOf(changeStation[1].getAddress());
//				int lastIn__ = G.getStationNameArrL().get(changeRoute[2]).indexOf(T.getAddress()) + T.getAddress().length() + 1;/////////maybe wrong
//				int firstIn2__ = G.getStationNameArrL().get(changeRoute[2]).indexOf(changeStation[1].getAddress()) + changeStation[1].getAddress().length() + 1;
//				int lastIn2__ = G.getStationNameArrL().get(changeRoute[2]).indexOf(T.getAddress());/////////maybe wrong
//				int start__ = addresses.size();
//
//				if(G.getStationNameArrL().get(changeRoute[2]).indexOf(changeStation[1].getAddress()) < G.getStationNameArrL().get(changeRoute[2]).indexOf(T.getAddress())){
//					String routeStr = (lastIn__ < G.getStationNameArrL().get(changeRoute[2]).length())?G.getStationNameArrL().get(changeRoute[2]).substring(firstIn__,lastIn__):G.getStationNameArrL().get(changeRoute[2]).substring(firstIn__);
//					String[] routeLittle = routeStr.split("&");
//					for (int ii = 0; ii < routeLittle.length; ii++){
//						addresses.add((Address) G.getMap().get(routeLittle[ii]));
//					}
//				}else {
//					String routeStr = (firstIn2__ < G.getStationNameArrL().get(changeRoute[2]).length())?G.getStationNameArrL().get(changeRoute[2]).substring(lastIn2__,firstIn2__):G.getStationNameArrL().get(changeRoute[2]).substring(lastIn2__);
//					String[] routeLittle = routeStr.split("&");
//					for (int ii = 0; ii < routeLittle.length; ii++){
//						addresses.add(start__,(Address) G.getMap().get(routeLittle[ii]));
//					}
//				}
//
//				for (Address a : addresses){
//					System.out.println(a.getAddress()+"???");
//				}
			}

		//	System.out.println((changeRoute[0]+1)+" "+(changeRoute[1]+1)+" "+(changeRoute[2]+1)+"换乘"+changeStation[0].getAddress()+" "+changeStation[1].getAddress());


		}
		T.setD(time);
		return addresses;


	}

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
		double a = rad(lat1) - rad(lat2); //纬度差
		double b = rad(lng1) - rad(lng2); //经度差
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000d) / 10000d;
		s = s*1000;
		return s;
	}
	private int[][] matrixAddAll(ArrayList<int[][]> matrixArrL ){
		int[][] returnMatrix = new int[matrixArrL.get(0).length][matrixArrL.get(0).length];
		for(int i = 0; i < matrixArrL.size(); i++){
			for(int j = 0; j < matrixArrL.get(0).length; j++){
				for (int k = 0; k < matrixArrL.get(0).length; k++){
					returnMatrix[j][k] += matrixArrL.get(i)[j][k];
				}
			}
		}
		return returnMatrix;
	}
}
