package com.homepainter.service;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KouMen {
    private double x1, y1, x2, y2;

    public List<Map<String,Object>> SuCai_Doors = new ArrayList<>();

    private int maxid = 0;

    // 初始化，后续要注释掉
    public void init(){
        Map<String,Object> door1 = new HashMap<>();
        door1.put("id",1);
        door1.put("width",1.5);
        SuCai_Doors.add(door1);

        Map<String,Object> door2 = new HashMap<>();
        door2.put("id",2);
        door2.put("width",1.7);
        SuCai_Doors.add(door2);

        Map<String,Object> door3 = new HashMap<>();
        door3.put("id",3);
        door3.put("width",1.9);
        SuCai_Doors.add(door3);

        Map<String,Object> door4 = new HashMap<>();
        door4.put("id",4);
        door4.put("width",2.1);
        SuCai_Doors.add(door4);

        Map<String,Object> door5 = new HashMap<>();
        door5.put("id",5);
        door5.put("width",2.3);
        SuCai_Doors.add(door5);
    }

    public List<Map<String,Object>> start2(Map<String,Object> DWW,Map<String,Object> house){


        List<Map<String,Object>> Room = (List<Map<String,Object>>) house.get("Room");
        List<Map<String, Object>> Doors = (List<Map<String, Object>>) DWW.get("Doors");
        List<Map<String, Object>> DoorPoints = (List<Map<String, Object>>) DWW.get("DoorPoints");


        // 绑定
        for(int i=0;i<Doors.size();i++){
            Map<String,Object> start_point =  getWallPointById(Integer.parseInt( Doors.get(i).get("start_point").toString()),DoorPoints);
            Map<String,Object> end_point =  getWallPointById(Integer.parseInt( Doors.get(i).get("end_point").toString()),DoorPoints);
            Doors.get(i).put("start_point",start_point);
            Doors.get(i).put("end_point",end_point);
        }

        for(int i=0;i<Room.size();i++){
            // 每个房间
            List<Map<String,Object>> points = (List<Map<String, Object>>) Room.get(i).get("point");

            double lastX = Double.parseDouble(points.get(0).get("x").toString());
            double lastY = Double.parseDouble(points.get(0).get("y").toString());

            for(int j=1;j<=points.size();j++){
                // 遍历房间的每个点
                double x = 0;
                double y = 0;

                if(j==points.size()){
                    x = Double.parseDouble( points.get(0).get("x").toString() );
                    y = Double.parseDouble( points.get(0).get("y").toString() );
                }else{
                    x = Double.parseDouble( points.get(j).get("x").toString() );
                    y = Double.parseDouble( points.get(j).get("y").toString() );
                }

                int haveDorr = -1;
                // 这是一面墙的两个点
                for(int k=0;k<Doors.size();k++){
                    Map<String,Object> DoorPoint = Doors.get(k);
                    Map<String,Object> start_point = (Map<String, Object>) DoorPoint.get("start_point");
                    Map<String,Object> end_point = (Map<String, Object>) DoorPoint.get("end_point");
                    double start_x0 = Double.parseDouble( start_point.get("x").toString() );
                    double start_y0 = Double.parseDouble( start_point.get("y").toString() );
                    double end_x0 = Double.parseDouble( end_point.get("x").toString() );
                    double end_y0 = Double.parseDouble( end_point.get("y").toString() );

                    if(distancePointToLine(start_x0,start_y0,x,y,lastX,lastY )<0.1 || distancePointToLine(end_x0,end_y0,x,y,lastX,lastY  )<0.1){
                        haveDorr = k;
                        break;
                    }else{
                        // 门不在墙上
                    }
                }

                if(haveDorr!=-1){
                    List<Map<String,Object>> listDoor = new ArrayList<>();
                    if(Room.get(i).get("door")!=null){
                        listDoor = (List<Map<String, Object>>) Room.get(i).get("door");
                    }
                    listDoor.add(Doors.get(haveDorr));
                    Room.get(i).put("door",listDoor);
                    break;
                }


                lastX = x;
                lastY = y;
            }

        }


        return Room;
    }


    public Map<String,Object> start(Map<String,Object> origin,Map<String,Object> house){
        Map<String,Object> res = new HashMap<>();
        List<Map<String,Object>> NewDoors = new ArrayList<>();
        List<Map<String,Object>> NewRooms = new ArrayList<>();

        // 初始化，后续要注释掉
        init();


        List<Map<String,Object>> Room = (List<Map<String,Object>>) house.get("Room");
        List<Map<String, Object>> Doors = (List<Map<String, Object>>) origin.get("Doors");
        List<Map<String, Object>> DoorPoints = (List<Map<String, Object>>) origin.get("DoorPoints");

        // 获取门的最大Id
        for(int i=0;i<Doors.size();i++){
            if(Integer.parseInt(Doors.get(i).get("id").toString())>maxid)
                maxid = Integer.parseInt(Doors.get(i).get("id").toString());
        }

        // 绑定
        for(int i=0;i<Doors.size();i++){
            Map<String,Object> start_point =  getWallPointById(Integer.parseInt( Doors.get(i).get("start_point").toString()),DoorPoints);
            Map<String,Object> end_point =  getWallPointById(Integer.parseInt( Doors.get(i).get("end_point").toString()),DoorPoints);
            Doors.get(i).put("start_point",start_point);
            Doors.get(i).put("end_point",end_point);
        }

        for(int i=0;i<Room.size();i++){
            // 每个房间
            List<Map<String,Object>> points = (List<Map<String, Object>>) Room.get(i).get("point");
            List<Map<String,Object>> RoomWalls = new ArrayList<>();

            Map<String,Object> tempRoom = new HashMap<>();
            tempRoom.putAll(Room.get(i));

            double lastX = Double.parseDouble(points.get(0).get("x").toString());
            double lastY = Double.parseDouble(points.get(0).get("y").toString());

            for(int j=1;j<=points.size();j++){
                // 遍历房间的每个点
                double x = 0;
                double y = 0;

                if(j==points.size()){
                    x = Double.parseDouble( points.get(0).get("x").toString() );
                    y = Double.parseDouble( points.get(0).get("y").toString() );
                }else{
                    x = Double.parseDouble( points.get(j).get("x").toString() );
                    y = Double.parseDouble( points.get(j).get("y").toString() );
                }

                boolean haveDorr = false;
                // 这是一面墙的两个点
                for(int k=0;k<Doors.size();k++){
                    if(haveDorr) break;

                    Map<String,Object> DoorPoint = Doors.get(k);
                    Map<String,Object> start_point = (Map<String, Object>) DoorPoint.get("start_point");
                    Map<String,Object> end_point = (Map<String, Object>) DoorPoint.get("end_point");
                    double start_x0 = Double.parseDouble( start_point.get("x").toString() );
                    double start_y0 = Double.parseDouble( start_point.get("y").toString() );
                    double end_x0 = Double.parseDouble( end_point.get("x").toString() );
                    double end_y0 = Double.parseDouble( end_point.get("y").toString() );

                    if(distancePointToLine(start_x0,start_y0,x,y,lastX,lastY )<0.1 || distancePointToLine(end_x0,end_y0,x,y,lastX,lastY  )<0.1){
                        haveDorr = true;
                        // 门在墙上，找到距离近的一个
                        double now_dist1 = distanceBetweenPoints(x,y,start_x0,start_y0);
                        double now_dist2 = distanceBetweenPoints(x,y,end_x0,end_y0);
                        double last_dist1 = distanceBetweenPoints(lastX,lastY,start_x0,start_y0);
                        double last_dist2 = distanceBetweenPoints(lastX,lastY,end_x0,end_y0);
                        double doorWidth = distanceBetweenPoints(start_x0,start_y0,end_x0,end_y0);
                        double WallWidth = distanceBetweenPoints(x,y,lastX,lastY);
                        double KeBaiFangWidth = 0;
                        if(Math.min(now_dist1,now_dist2)<Math.min(last_dist1,last_dist2)){
                            // now点是短的
                            KeBaiFangWidth = WallWidth - Math.min(now_dist1,now_dist2);
                        }else{
                            KeBaiFangWidth = WallWidth - Math.min(last_dist2,last_dist1);
                        }
                        // 匹配门的大小
                        List<Map<String,Object>> PaiChu = new ArrayList<>();

                        while(PaiChu.size()!= SuCai_Doors.size()){
                            // 新的门
                            Map<String,Object> temp = PiPei_Door(doorWidth,PaiChu);
                            PaiChu.add(temp);
                            if(KeBaiFangWidth>doorWidth){
                                //开始摆放，计算门应该摆放的坐标
                                double []newpoint = new double[2];
                                if(Math.min(now_dist1,now_dist2)<Math.min(last_dist1,last_dist2)){
                                    // now点是短的
                                    if(now_dist1<now_dist2){
                                        System.out.println("startpoint - xy");
                                        newpoint = extendLine(x,y,start_x0,start_y0,Double.parseDouble(temp.get("width").toString()));

                                        // Add Door
                                        Map<String,Object > tempDoor = new HashMap<>();
                                        tempDoor.put("start_point",start_point);
                                        Map<String,Object> newpointMap = new HashMap<>();
                                        newpointMap.put("x",newpoint[0]);
                                        newpointMap.put("y",newpoint[1]);
                                        newpointMap.put("id",++maxid);
                                        tempDoor.put("end_point",newpointMap);
                                        NewDoors.add(tempDoor);

                                        Doors.remove(k);

                                        // Add Wall
                                        Map<String,Object> NewWall1 = new HashMap<>();
                                        NewWall1.put("start_x",lastX);
                                        NewWall1.put("start_y",lastY);
                                        NewWall1.put("end_x",newpoint[0]);
                                        NewWall1.put("end_y",newpoint[1]);
                                        RoomWalls.add(NewWall1);

                                        Map<String,Object> NewWall2 = new HashMap<>();
                                        NewWall2.put("start_x",start_x0);
                                        NewWall2.put("start_y",start_y0);
                                        NewWall2.put("end_x",x);
                                        NewWall2.put("end_y",y);
                                        RoomWalls.add(NewWall1);
                                    }
                                    else{
                                        System.out.println("endpoint - xy");

                                        newpoint = extendLine(x,y,end_x0,end_y0,Double.parseDouble(temp.get("width").toString()));

                                        Map<String,Object > tempDoor = new HashMap<>();
                                        tempDoor.put("start_point",end_point);
                                        Map<String,Object> newpointMap = new HashMap<>();
                                        newpointMap.put("x",newpoint[0]);
                                        newpointMap.put("y",newpoint[1]);
                                        newpointMap.put("id",++maxid);
                                        tempDoor.put("end_point",newpointMap);
                                        NewDoors.add(tempDoor);
                                        Doors.remove(k);

                                        // Add Wall
                                        Map<String,Object> NewWall1 = new HashMap<>();
                                        NewWall1.put("start_x",lastX);
                                        NewWall1.put("start_y",lastY);
                                        NewWall1.put("end_x",newpoint[0]);
                                        NewWall1.put("end_y",newpoint[1]);
                                        RoomWalls.add(NewWall1);

                                        Map<String,Object> NewWall2 = new HashMap<>();
                                        NewWall2.put("start_x",end_x0);
                                        NewWall2.put("start_y",end_y0);
                                        NewWall2.put("end_x",x);
                                        NewWall2.put("end_y",y);
                                        RoomWalls.add(NewWall1);
                                    }

                                }else{
                                    if(last_dist1<last_dist2){
                                        System.out.println("startpoint - lastxy");

                                        newpoint = extendLine(lastX,lastY,start_x0,start_y0,Double.parseDouble(temp.get("width").toString()));

                                        Map<String,Object > tempDoor = new HashMap<>();
                                        tempDoor.put("start_point",start_point);
                                        Map<String,Object> newpointMap = new HashMap<>();
                                        newpointMap.put("x",newpoint[0]);
                                        newpointMap.put("y",newpoint[1]);
                                        newpointMap.put("id",++maxid);
                                        tempDoor.put("end_point",newpointMap);
                                        NewDoors.add(tempDoor);
                                        Doors.remove(k);

                                        // Add Wall
                                        Map<String,Object> NewWall1 = new HashMap<>();
                                        NewWall1.put("start_x",lastX);
                                        NewWall1.put("start_y",lastY);
                                        NewWall1.put("end_x",start_x0);
                                        NewWall1.put("end_y",start_y0);
                                        RoomWalls.add(NewWall1);

                                        Map<String,Object> NewWall2 = new HashMap<>();
                                        NewWall2.put("start_x",newpoint[0]);
                                        NewWall2.put("start_y",newpoint[1]);
                                        NewWall2.put("end_x",x);
                                        NewWall2.put("end_y",y);
                                        RoomWalls.add(NewWall1);
                                    }
                                    else{
                                        System.out.println("endpoint - lastxy");

                                        newpoint = extendLine(lastX,lastY,end_x0,end_y0,Double.parseDouble(temp.get("width").toString()));

                                        Map<String,Object > tempDoor = new HashMap<>();
                                        tempDoor.put("start_point",end_point);
                                        Map<String,Object> newpointMap = new HashMap<>();
                                        newpointMap.put("x",newpoint[0]);
                                        newpointMap.put("y",newpoint[1]);
                                        newpointMap.put("id",++maxid);
                                        tempDoor.put("end_point",newpointMap);
                                        NewDoors.add(tempDoor);
                                        Doors.remove(k);

                                        // Add Wall
                                        Map<String,Object> NewWall1 = new HashMap<>();
                                        NewWall1.put("start_x",lastX);
                                        NewWall1.put("start_y",lastY);
                                        NewWall1.put("end_x",end_x0);
                                        NewWall1.put("end_y",end_y0);
                                        RoomWalls.add(NewWall1);

                                        Map<String,Object> NewWall2 = new HashMap<>();
                                        NewWall2.put("start_x",newpoint[0]);
                                        NewWall2.put("start_y",newpoint[1]);
                                        NewWall2.put("end_x",x);
                                        NewWall2.put("end_y",y);
                                        RoomWalls.add(NewWall1);
                                    }

                                }

                                break;
                            }else{
                                // 继续
                            }
                        }

                    }else{
                        // 门不在墙上
                    }
                }

                if(haveDorr==false){
                    Map<String,Object> tempWall = new HashMap<>();
                    tempWall.put("start_x",lastX);
                    tempWall.put("start_y",lastY);
                    tempWall.put("end_x",x);
                    tempWall.put("end_y",y);
                    RoomWalls.add(tempWall);
                }


                lastX = x;
                lastY = y;
            }

            tempRoom.put("wall",RoomWalls);
            NewRooms.add(tempRoom);
        }

        res.put("Door",NewDoors);
        res.put("house",NewRooms);
        return res;
    }

    public static double[] extendLine(double x1, double y1, double x2, double y2, double distance) {
        double length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double u = (x2 - x1) / length;
        double v = (y2 - y1) / length;
        double x3 = x2 + u * (distance+length);
        double y3 = y2 + v * (distance+length);
        return new double[] {x3, y3};
    }


    /**
     * 找到长度最匹配的门
     * @param DoorLength
     * @param paichu
     * @return
     */
    public Map<String,Object> PiPei_Door(double DoorLength,List<Map<String,Object>> paichu){
        double minn = 1000;
        int min_now = -1;

        for(int i=0;i<SuCai_Doors.size();i++){
            boolean InPaichu = false;
            for(int j=0;j<paichu.size();j++){
                if(paichu.get(j).get("id").toString().equals(SuCai_Doors.get(i).get("id")) ){
                    InPaichu = true;
                    break;
                }
            }
            if(!InPaichu){
                if(Math.abs( Double.parseDouble( SuCai_Doors.get(i).get("width").toString()) - DoorLength) <minn ){
                    minn = Math.abs( Double.parseDouble( SuCai_Doors.get(i).get("width").toString()) - DoorLength);
                    min_now = i;
                }
            }

        }

        return SuCai_Doors.get( min_now);
    }

    /**
     * 根据墙的id获取x、y
     * @param id
     * @param walls
     * @return
     */
    public Map<String,Object> getWallPointById(int id,List<Map<String,Object>> walls){
        for(int i=0;i<walls.size();i++){
            if(Integer.parseInt( walls.get(i).get("id").toString()) ==id){
                return walls.get(i);
            }
        }
        return null;
    }

    /**
     * 点到直线的距离公式
     * @param x0  目标点
     * @param y0  目标点
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double distancePointToLine(double x0, double y0, double x1, double y1, double x2, double y2) {
        double a = y2 - y1;
        double b = x1 - x2;
        double c = x2 * y1 - x1 * y2;
        double distance = Math.abs(a * x0 + b * y0 + c) / Math.sqrt(a * a + b * b);
        return distance;
    }

    /**
     * 两点之间距离计算
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return distance;
    }

}
