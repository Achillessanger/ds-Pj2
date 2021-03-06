<#assign baseUrl=request.getContextPath()>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
	<link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/4.1.0/css/bootstrap.min.css">
	<style type="text/css">
		body, html, #allmap {
			width: 100%;
			height: 100%;
			overflow: hidden;
			margin: 0;
			font-family: "微软雅黑";
		}
	</style>

	<script type="text/javascript"
			src="http://api.map.baidu.com/api?v=2.0&ak=EuQZy4q2kFskzCIImsMvnvwtHkkyEzAd"></script>
	<title>地图展示</title>
</head>
<body style="height:100%;width: 100%;font-size:0px">
<input id="hiddenBaseUrl" type="hidden" value="${baseUrl}"/>

<div style="width:100%;display: block; max-width: none" id="allmap"></div>
<div style="width:100%; height:100%;display:block; float: left; overflow: auto">
	<form style="position:absolute;font-size:16px;top:3%;background-color: rgba(255,255,255,0.6);width: 18%;" id="submitForm">
		&nbsp;&nbsp;&nbsp;起点: <input  type="text" name="startAddress" id="startAddress" value="复旦大学张江校区"/>
		<input type="hidden" id="hiddenStartLongitude" value="121.604569"/>
		<input type="hidden" id="hiddenStartLatitude" value="31.196348"/>
		<br/><br/>
		&nbsp;&nbsp;&nbsp;终点: <input type="text" name="endAddress" id="endAddress" value="人民广场"/>
		<input type="hidden" id="hiddenEndLongitude" value="121.478941"/>
		<input type="hidden" id="hiddenEndLatitude" value="31.236009"/>
		<br/><br/>

		&nbsp;&nbsp;&nbsp;<input type="radio" name="items" value="1" checked/>步行最少<br/>
		&nbsp;&nbsp;&nbsp;<input type="radio" name="items" value="2"/>换乘最少<br/>
		&nbsp;&nbsp;&nbsp;<input type="radio" name="items" value="3"/>时间最短<br/><br/>

		<button class="btn btn-outline-dark" style="position:relative;left:40%;width:80px;height:30px; " type="button" value="查询"
				onclick="clickButton()">查询</button>
	</form>
	<br/>
	<div style="position:absolute;top:30%;width: 20%;height: 60%;">
		<span style="font-size:18px;position:relative;">display the result:</span><br/>
		<div id="resultDiv"
			 style="background-color:rgba(255,255,255,0.75);width: 90%;height:100%;font-size:18px;word-wrap: break-word; overflow: scroll;">

		</div>
	</div>
</div>
</body>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
    // 百度地图API功能
	var rightclick = 0;
	var preS;
	var preE;
    var map = new BMap.Map("allmap");    // 创建Map实例
    map.centerAndZoom("上海", 16);  // 初始化地图,设置中心点坐标和地图级别
    map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
    var geocoder = new BMap.Geocoder();
    map.addEventListener("rightclick", function (e) {
        RightClickMap(e.point);
    });

    function RightClickMap(point) {
        var EventStartMarker = function (map) {
            //addEventF是具体的菜单方法，要实现什么功能取决自身需求
            geocoder.getLocation(point, function (rs) {
                //addressComponents对象可以获取到详细的地址信息
                var addComp = rs.addressComponents;
                var site = addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                //将对应的HTML元素设置值
                $("#startAddress").val(site);
                $("#hiddenStartLongitude").val(point.lng);
                $("#hiddenStartLatitude").val(point.lat);
            });
        };
        var EventEndMarker = function (map) {
            //addEventF是具体的菜单方法，要实现什么功能取决自身需求
            geocoder.getLocation(point, function (rs) {
                //addressComponents对象可以获取到详细的地址信息
                var addComp = rs.addressComponents;
                var site = addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                //将对应的HTML元素设置值
                $("#endAddress").val(site);
                $("#hiddenEndLongitude").val(point.lng);
                $("#hiddenEndLatitude").val(point.lat);
            });
        };
        var markerMenu = new BMap.ContextMenu();
        markerMenu.addItem(new BMap.MenuItem('设为起点', EventStartMarker.bind(map)));
        map.addContextMenu(markerMenu);

        var markerMenuEnd = new BMap.ContextMenu();
        markerMenu.addItem(new BMap.MenuItem('设为终点', EventEndMarker.bind(map)));
        map.addContextMenu(markerMenuEnd);
        rightclick = 1;
    }



    function clickButton() {
        if(rightclick == 1 || ($("#startAddress").val()==preS && $("#endAddress").val()==preE)){
            preS = $("#startAddress").val();
            preE = $("#endAddress").val();
            clickButton2();
		}else if($("#startAddress").val()==preS){
            preS = $("#startAddress").val();
            preE = $("#endAddress").val();
            var options = {
                onSearchComplete: function(results){
                    if (local.getStatus() == BMAP_STATUS_SUCCESS){
                        // 判断状态是否正确
                        document.getElementById("hiddenEndLongitude").value = results.getPoi(0).point.lng;
                        document.getElementById("hiddenEndLatitude").value = results.getPoi(0).point.lat;
                        clickButton2();
                    }
                }
            };
            var local = new BMap.LocalSearch(map, options);
            local.search($("#endAddress").val());

		}else if($("#endAddress").val()==preE){
            preS = $("#startAddress").val();
            preE = $("#endAddress").val();
            var options = {
                onSearchComplete: function(results){
                    if (local.getStatus() == BMAP_STATUS_SUCCESS){
                        // 判断状态是否正确
                        document.getElementById("hiddenStartLongitude").value = results.getPoi(0).point.lng;
                        document.getElementById("hiddenStartLatitude").value = results.getPoi(0).point.lat;
                        clickButton2();
                    }
                }
            };
            var local = new BMap.LocalSearch(map, options);
            local.search($("#startAddress").val());
		} else {
            var start = document.getElementById("startAddress").value;
            var end = document.getElementById("endAddress").value;
            // alert(start+" "+end)
            preS = $("#startAddress").val();
            preE = $("#endAddress").val();

            if(start.length <= 0){
                alert("请输入起点");
                document.getElementById("startAddress").focus();
                return;
            }

            if(end.length <= 0){
                alert("请输入终点");
                document.getElementById("endAddress").focus();
                return;
            }

            var options = {
                onSearchComplete: function(results){
                    if (local.getStatus() == BMAP_STATUS_SUCCESS){
                        // 判断状态是否正确
                        document.getElementById("hiddenStartLongitude").value = results.getPoi(0).point.lng;
                        document.getElementById("hiddenStartLatitude").value = results.getPoi(0).point.lat;
                        local2.search(end)
                    }
                }
            };
            var options2 = {
                onSearchComplete: function(results){
                    if (local2.getStatus() == BMAP_STATUS_SUCCESS){
                        // 判断状态是否正确
                        document.getElementById("hiddenEndLongitude").value = results.getPoi(0).point.lng;
                        document.getElementById("hiddenEndLatitude").value = results.getPoi(0).point.lat;
                        if(start == "复旦大学张江校区"){
                            document.getElementById("hiddenStartLongitude").value = 121.604569;
                            document.getElementById("hiddenStartLatitude").value = 31.196348;
                        }
                        if(end == "人民广场"){
                            document.getElementById("hiddenEndLongitude").value = 121.478941;
                            document.getElementById("hiddenEndLatitude").value = 31.236009;
                        }
                        // alert("1")
                        clickButton2();
                    }
                }
            };

            var local = new BMap.LocalSearch(map, options);
            var local2 = new BMap.LocalSearch(map, options2);
            local.search(start);
		}
        rightclick = 0;


    }

    //提交按钮的点击事件
    function clickButton2() {
        //请在这里检查数据

        // var local = new BMap.LocalSearch(map,{renderOptions:{map:map},pageCapacity:1})
        // local.search("复旦大学");


        var baseUrl = $("#hiddenBaseUrl").val();
        $.ajax({
            url: baseUrl + "/submitsearch",
            type: 'POST',
            data:
                JSON.stringify({
                    "startAddress": $("#startAddress").val(),
                    "startLongitude": $("#hiddenStartLongitude").val(),
                    "startLatitude": $("#hiddenStartLatitude").val(),
                    "endAddress": $("#endAddress").val(),
                    "endLongitude": $("#hiddenEndLongitude").val(),
                    "endLatitude": $("#hiddenEndLatitude").val(),
                    "choose": $('input[name=items]:checked', '#submitForm').val()
                }),
            dataType: 'JSON',
            contentType: "application/json; charset=UTF-8",
            success: function (data) {
                map.clearOverlays();
                let len = data.subwayList.length;
                let placeStart = new BMap.Point(data.startPoint.longitude, data.startPoint.latitude);
                let placeEnd = new BMap.Point(data.endPoint.longitude, data.endPoint.latitude);
                let arrayList = [];
                arrayList.push(placeStart);
                for (let i = 0; i < len; ++i) {
                    let p = new BMap.Point(data.subwayList[i].longitude, data.subwayList[i].latitude);
                    arrayList.push(p);
                    drawPoint(p, data.subwayList[i].address);
                }
                arrayList.push(placeEnd);

                let polylineRoute = new BMap.Polyline(arrayList);
                map.addOverlay(polylineRoute);
                map.setViewport(arrayList);

                drawPoint(placeStart, data.startPoint.address);
                drawPoint(placeEnd, data.endPoint.address);

                let str = data.startPoint.address;
                for (let i = 0; i < len; ++i)
                    str += "&nbsp;&nbsp;->&nbsp;&nbsp;" + data.subwayList[i].address;
                str += "&nbsp;&nbsp;->&nbsp;&nbsp;" + data.endPoint.address + "<br/>";
                str += "花费时间为:&nbsp;&nbsp; " + data.minutes + "&nbsp;分钟" + "<br/>";
                if(data.distance2 != 0.1){
                    str += "步行至站点： " + data.distance1 + " 米" + "<br/>";
                    str += "步行至终点： " + data.distance2 + " 米" + "<br/>";
                }else {
                    str += "直接步行距离： " + data.distance1 + " 米" + "<br/>";
                }
                str += "本次搜索耗时： "+ data.nanoTime + " (ns)"

                $("#resultDiv").html(str);
            },
            error: function (data) {
                alert("很抱歉,服务器出错!");
            }
        });
    }

    // 绘制marker（起点、经点、终点），添加文本标注
    function drawPoint(point, content) {
        let marker = new BMap.Marker(point);
        this.map.addOverlay(marker);
        var label = new BMap.Label(content, {
            offset: new BMap.Size(20, -10)
        });
        marker.setLabel(label);
    }
</script>
<script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
<script src="https://cdn.staticfile.org/popper.js/1.12.5/umd/popper.min.js"></script>
<script src="https://cdn.staticfile.org/twitter-bootstrap/4.1.0/js/bootstrap.min.js"></script>
</html>
