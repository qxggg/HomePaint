package com.homepainter.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.service.GetGoods;
import org.apache.commons.collections4.CollectionUtils;
import org.bouncycastle.pqc.jcajce.provider.qtesla.SignatureSpi;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.homepainter.util.HouseIdentifyHandler.toDouble;

public class RuleUtils {



    public static double clockwiseAngle(double[] a, double[] b) {
        // 计算向量的点积
        double dotProduct = a[0] * b[0] + a[1] * b[1];
        // 计算向量的叉积
        double crossProduct = a[0] * b[1] - a[1] * b[0];
        // 判断向量 b 在向量 a 的顺时针方向还是逆时针方向
        if (crossProduct > 0)
            // b 在 a 的逆时针方向，夹角为负值
            return 2 * Math.PI - Math.abs(Math.atan2(crossProduct, dotProduct));
        else {
            // b 在 a 的顺时针方向，夹角为正值
            return Math.abs(Math.atan2(crossProduct, dotProduct));
        }
    }

    public static double[] rotate(double[] pivot, double[] point, double angle) {
        double[] rotatedPoint = new double[2];
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        rotatedPoint[0] = cos * (point[0] - pivot[0]) - sin * (point[1] - pivot[1]) + pivot[0];
        rotatedPoint[1] = sin * (point[0] - pivot[0]) + cos * (point[1] - pivot[1]) + pivot[1];
        return rotatedPoint;
    }


    private static boolean pnpolyAlgorithm(List<Double> vertX, List<Double> vertY, double x, double y) {
        if (CollectionUtils.isEmpty(vertX) || CollectionUtils.isEmpty(vertY)) {
            return false;
        }
        double maxX = vertX.stream().max(Comparator.comparingDouble(Double::doubleValue)).get();
        double maxY = vertY.stream().max(Comparator.comparingDouble(Double::doubleValue)).get();
        double minX = vertX.stream().min(Comparator.comparingDouble(Double::doubleValue)).get();
        double minY = vertY.stream().min(Comparator.comparingDouble(Double::doubleValue)).get();

        if (x < minX || x > maxX || y < minY || y > maxY) {
            return false;
        }
        int i, j;
        boolean result = false;
        int n = vertX.size();
        Double[] vertx = vertX.toArray(new Double[0]);
        Double[] verty = vertY.toArray(new Double[0]);
        for (i = 0, j = n - 1; i < n; j = i++) {
            if ((verty[i] > y) != (verty[j] > y) &&
                    (x < (vertx[j] - vertx[i]) * (y - verty[i]) / (verty[j] - verty[i]) + vertx[i])) {
                result = !result;
            }
        }
        return result;
    }

    public static boolean isPeopleInHouse(JSONObject idx, JSONArray wallPoints){
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        for (int i = 0; i < wallPoints.size(); ++i){
            JSONObject wallPoint = wallPoints.getJSONObject(i);
            x.add(toDouble(wallPoint.getBigDecimal("x")));
            y.add(toDouble(wallPoint.getBigDecimal("y")));
        }
        return (pnpolyAlgorithm(x, y, (double) idx.get("x"), (double) idx.get("y")));
    }



    public static boolean isInHouse(JSONArray index, JSONArray wallPoints, float up){

        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        List<Double> z = new ArrayList<>();

        for (int i = 0; i < index.size(); ++i) {
            JSONObject idx = index.getJSONObject(i);
            y.add((double) idx.get("y"));
        }

        for (int i = 0; i < wallPoints.size(); ++i){
            JSONObject wallPoint = wallPoints.getJSONObject(i);
            x.add( toDouble((BigDecimal) wallPoint.get("x")));
            z.add(toDouble((BigDecimal) wallPoint.get("y")));
        }

        for (Double i : y)
            if (i > up || i < 0) {return false;}

        for (int i = 0; i < index.size(); ++i) {
            JSONObject idx = index.getJSONObject(i);
            if (!pnpolyAlgorithm(x, z, (double) idx.get("x"), (double) idx.get("z"))) {
                System.out.println(x);
                System.out.println(z);
                System.out.println(idx);
                return false;
            }
        }

        return true;

    }

    public static boolean inputGoods(List<HashMap<String, Object>> goods, JSONArray wallPoints , float up, JSONArray index){
        if (isInHouse(index, wallPoints, up) && checkIndex(goods, index)) {
            recordIndex(goods, index);
            return true;
        }
        else return false;
    }

    public static boolean test(JSONObject data){
        JSONObject data1 = (JSONObject) data.get("data");
        JSONObject data2 = (JSONObject) data1.get("DWW");

        JSONArray doorList = (JSONArray) data2.get("Doors");
        JSONArray doorPoints = (JSONArray) data2.get("DoorPoints");

        JSONArray wallList = (JSONArray) data2.get("Walls");
        JSONArray wallPoints = (JSONArray) data2.get("WallPoints");

        JSONArray windowList = (JSONArray) data2.get("Windows");
        JSONArray windowPoints = (JSONArray) data2.get("WindowPoints");

        String style = (String) data2.get("style");
        JSONObject house = (JSONObject) data1.get("house");
        JSONArray rooms = (JSONArray) house.get("Room");


        System.out.println();
        System.out.println(rooms);
        return false;
    }

    public static String getDirection(JSONArray points) {
        int n = points.size();
        double sum = 0;
        for (int i = 0; i < n; i++) {
            JSONObject p1 = points.getJSONObject(i);
            JSONObject p2 = points.getJSONObject((i + 1) % n);
            double x1 = p1.getDouble("x");
            double y1 = p1.getDouble("y");
            double x2 = p2.getDouble("x");
            double y2 = p2.getDouble("y");
            sum += (x2 - x1) * (y2 + y1);
        }
        if (sum > 0) {
            return "逆时针";
        } else {
            return "顺时针";
        }
    }

//    public static JSONArray indexHandler(JSONArray index, JSONObject center, JSONObject target, JSONObject rotate){
//        double x = toDouble(center.getBigDecimal("x"));
//        double y = toDouble(center.getBigDecimal("y"));
//        double z = toDouble(center.getBigDecimal("z"));
//
//        double tx = toDouble(target.getBigDecimal("x"));
//        double ty = toDouble(target.getBigDecimal("y"));
//        double tz = toDouble(target.getBigDecimal("z"));
//
//        double fx = toDouble(rotate.getBigDecimal("x"));
//        double fy = toDouble(rotate.getBigDecimal("y"));
//        double fz = toDouble(rotate.getBigDecimal("z"));
//        double dx = tx - x, dy = ty - y, dz = tz - z;
//
//
//        for (int i = 0; i < index.size(); ++i){
//            JSONObject tmp = index.getJSONObject(i);
//            double lx = tmp.getDouble("x");
//            double ly = tmp.getDouble("y");
//            double lz = tmp.getDouble("z");
//            lx += dx; ly += dy; lz += dz;
//        }
//
//        return index;
//    }


    public static JSONArray roomHandler(JSONObject room, JSONArray remove){
        JSONArray points = (JSONArray) room.get("point");
         JSONArray walls = new JSONArray();

         if (getDirection(points).equals("顺时针")) {
             for (int a = 0; a < points.size() - 1; ++a) {
                 JSONObject wall = new JSONObject();
                 JSONObject p1 = (JSONObject) points.get(a);
                 JSONObject p2 = (JSONObject) points.get(a + 1);
                 roomHandlerUtil(remove, walls, wall, p1, p2);
             }
         }
         else{
             for (int a = points.size() - 1; a > 0; --a) {
                 JSONObject wall = new JSONObject();
                 JSONObject p1 = (JSONObject) points.get(a);
                 JSONObject p2 = (JSONObject) points.get(a - 1);
                 roomHandlerUtil(remove, walls, wall, p1, p2);
             }
         }
         System.out.println(walls);
        return walls;
    }


//    public static JSONArray wallAddHandler(JSONArray walls){
//      for (int i = 0; i < walls.size(); ++i){
//          JSONObject w1 = walls.getJSONObject((i));
//          if (w1.get("category").equals(2) || w1.get("category").equals(1)) continue;
//
//          BigDecimal x1 = w1.getJSONObject("p1").getBigDecimal("x");
//          BigDecimal x2 = w1.getJSONObject("p2").getBigDecimal("x");
//          BigDecimal y1 = w1.getJSONObject("p1").getBigDecimal("y");
//          BigDecimal y2 = w1.getJSONObject("p2").getBigDecimal("y");
//          int j = 1;
//
//          while(true) {
//              JSONObject w2 = walls.getJSONObject((i + j) % walls.size());
//              BigDecimal x3 = w2.getJSONObject("p2").getBigDecimal("x");
//              BigDecimal y3 = w2.getJSONObject("p2").getBigDecimal("y");
//          }
//      }
//    }

    private static void roomHandlerUtil(JSONArray remove, JSONArray walls, JSONObject wall, JSONObject p1, JSONObject p2) {
        int s = (int) p1.get("id");
        int e = (int) p2.get("id");

        wall.put("p1", p1);
        wall.put("p2", p2);
        wall.put("category", 0);

        for (int i = 0; i < remove.size(); ++i) {
            JSONObject r = (JSONObject) remove.get(i);
            int wsid = (int) r.get("wsid");
            int weid = (int) r.get("weid");


            //   System.out.println(s + " " + e + " ws " + wsid + " " + weid);
            if (wsid == s && weid == e || wsid == e && weid == s) {
                wall.put("category", r.get("category"));
                wall.put("dx1", r.get("dx1"));
                wall.put("dx2", r.get("dx2"));
                wall.put("dy1", r.get("dy1"));
                wall.put("dy2", r.get("dy2"));
            }
        }
        walls.add(wall);
    }


    public static void recordIndex(List<HashMap<String, Object>> goods, JSONArray index){
        List<Double> xtmp = new ArrayList<>();
        List<Double> ytmp = new ArrayList<>();
        List<Double> ztmp = new ArrayList<>();

        for (int i = 0; i < index.size(); ++i) {
            JSONObject idx = index.getJSONObject(i);
            xtmp.add((Double) idx.get("x"));
            ytmp.add((Double) idx.get("y"));
            ztmp.add((Double) idx.get("z"));
        }
        HashMap<String, Object> x = new HashMap<>();
        HashMap<String, Object> y = new HashMap<>();
        HashMap<String, Object> z = new HashMap<>();

        double startx = Collections.min(xtmp);
        double endx = Collections.max(xtmp);
        double starty = Collections.min(ytmp);
        double endy = Collections.max(ytmp);
        double startz = Collections.min(ztmp);
        double endz = Collections.max(ztmp);

        x.put("start", startx); x.put("end", endx);
        y.put("start", starty); y.put("end", endy);
        z.put("start", startz); z.put("end", endz);

        HashMap<String, Object> good = new HashMap<>();
        good.put("x", x);
        good.put("y", y);
        good.put("z", z);

        goods.add(good);
    }

    public static boolean checkIndex(List<HashMap<String, Object>> goods, JSONArray index){
        for (int i = 0; i < index.size(); ++i) {
            JSONObject idx = index.getJSONObject(i);
            double x = (double) idx.get("x");
            double y = (double) idx.get("y");
            double z = (double) idx.get("z");
            for (HashMap<String, Object> good : goods){
                HashMap<String, Object> gx = (HashMap<String, Object>) good.get("x");
                HashMap<String, Object> gy = (HashMap<String, Object>) good.get("y");
                HashMap<String, Object> gz = (HashMap<String, Object>) good.get("z");

                double startx = (double) gx.get("start");
                double endx = (double) gx.get("end");
                double starty = (double) gx.get("start");
                double endy = (double) gx.get("end");
                double startz = (double) gx.get("start");
                double endz = (double) gx.get("end");

                if (x <= endx && x >= startx && y <= endy && y >= starty && z <= endz && z >= startz)
                    return false;
            }
        }
        return true;
    }

    public static JSONObject insertWallGoods(JSONArray index, JSONObject wall, JSONObject center){
        double x1 = 0, y1 = 0, z1 = 0, x2 = 0, y2 = 0, z2 = 0;
        for (int i = 0; i < index.size(); ++i){
            JSONObject idx = index.getJSONObject(i);
            double x = toDouble((BigDecimal) idx.get("x"));
            double y =  toDouble((BigDecimal) idx.get("y"));
            double z =  toDouble((BigDecimal) idx.get("z"));

            if (x > 0 && z > 0 && y < 0){
                x1 = x; y1 = y; z1 = z;
            }

            if (x > 0 && z < 0 && y < 0){
                x2 = x; y2 = y; z2 = z;
            }
        }

        System.out.println("x1 " + x1);
        System.out.println("y1 " + y1);
        System.out.println("z1 " + z1);
        System.out.println("x2 " + x2);
        System.out.println("y2 " + y2);
        System.out.println("z2 " + z2);
        double x = (x1 + x2) / 2.0, y = (y1 + y2) / 2.0, z = (z1 + z2) / 2.0;

        System.out.println("下面输出一下原来物体最下方那个点的坐标");
        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
        System.out.println();

        double cx = toDouble((BigDecimal) center.get("x"));
        double cy = toDouble((BigDecimal) center.get("y"));
        double cz = toDouble((BigDecimal) center.get("z"));
        System.out.println("下面输出中心点");
        System.out.println(cx);
        System.out.println(cy);
        System.out.println(cz);
        System.out.println();
        double fx = x - cx, fy = y - cy, fz = z - cz;

        JSONObject p1 = wall.getJSONObject("p1");
        JSONObject p2 = wall.getJSONObject("p2");

        double wx1 = toDouble((BigDecimal) p1.get("x"));
        double wy1 = toDouble((BigDecimal) p1.get("y"));
        double wx2 = toDouble((BigDecimal) p2.get("x"));
        double wy2 = toDouble((BigDecimal) p2.get("y"));

        double xtmp = (wx1 + wx2) / 2.0;
        double ztmp = (wy1 + wy2) / 2.0;
        System.out.println("下面输出一下目标点");
        System.out.println(xtmp);
        System.out.println(ztmp);
        System.out.println();



        double cfx = xtmp - fx;
        double cfy = 0 - fy;
        double cfz = ztmp - fz;

        double[] point = {cfx, cfz};
        double[] pivot = {xtmp, ztmp};



        System.out.println(cfx);
        System.out.println(cfy);
        System.out.println(cfz);


        double []a = {0.0, 1.0};
        double []b = new double[2];
        b[0] = wx2 - wx1; b[1] = wy2 - wy1;
        System.out.println("wx2 " + b[0]);
        System.out.println("wx1 " + b[1]);
        System.out.println("最终坐标");
        double angle = 2 * Math.PI - clockwiseAngle(b, a);
        double[] res = rotate(pivot, point, angle);
        System.out.println();
        System.out.println(res[0] + " " + res[1]);
        System.out.println();
        calPoints(index, pivot, angle);
        JSONObject j = new JSONObject();
        System.out.println();
        System.out.println(angle * 180 / Math.PI);
        System.out.println(index);
        j.put("x", res[0]);
        j.put("y", cfy);
        j.put("z", res[1]);
        j.put("angle", angle * 180 / Math.PI);
        return j;
    }

    public static JSONArray calPoints(JSONArray index, double[] pivot, double angle){
        for (int i = 0; i < index.size(); ++i){
            JSONObject idx = index.getJSONObject(i);
            double x = toDouble((BigDecimal) idx.get("x"));
            double y =  toDouble((BigDecimal) idx.get("y"));
            double z =  toDouble((BigDecimal) idx.get("z"));

            double[] point = {x, z};
            double[] res = rotate(pivot, point, 2 * Math.PI - angle);

            idx.put("x", res[0]);
            idx.put("z", res[1]);
        }

        return index;
    }

    public static JSONArray findDoorWindow(JSONObject data){

        JSONObject data1 =  data.getJSONObject("data");
        JSONObject data2 = (JSONObject) data1.get("DWW");


        JSONArray doors = (JSONArray) data2.get("Doors");
        JSONArray doorPoints = (JSONArray) data2.get("DoorPoints");

        JSONArray windows = (JSONArray) data2.get("Windows");
        JSONArray windowPoints = (JSONArray) data2.get("WindowPoints");

        JSONArray walls = (JSONArray) data2.get("Walls");
        JSONArray wallPoints = (JSONArray) data2.get("WallPoints");

        JSONObject house = (JSONObject) data1.get("house");

        JSONArray rooms = (JSONArray) house.get("Room");

        for (int i = 0; i < rooms.size(); ++i){
            System.out.println("房间" + i);
            JSONObject room = rooms.getJSONObject(i);
            JSONArray doorList = new JSONArray();
            JSONArray windowList = new JSONArray();
            JSONArray wallList = new JSONArray();
            JSONArray points = (JSONArray) room.get("point");
            for (int j = 0; j < points.size(); ++j){
                JSONObject point = points.getJSONObject(j);
                JSONObject point2 = points.getJSONObject((j + 1) % points.size());
                double x1 = toDouble(point.getBigDecimal("x"));
                double y1 = toDouble(point.getBigDecimal("y"));
                double x2 = toDouble(point2.getBigDecimal("x"));
                double y2 = toDouble(point2.getBigDecimal("y"));

                for (int m = 0; m < doors.size(); ++m){
                    JSONObject door = doors.getJSONObject(m);
                    int start_point = door.getInteger("start_point");
                    int end_point = door.getInteger("end_point");
                    int doorId = door.getInteger("id");
                    double sx = 0, sy = 0, ex = 0, ey = 0, sid = 0, eid = 0;
                    for (int n = 0; n < doorPoints.size(); ++n){
                        JSONObject doorPoint = doorPoints.getJSONObject(n);
                        if (doorPoint.getInteger("id") == start_point){
                            sx = toDouble(doorPoint.getBigDecimal("x"));
                            sy = toDouble(doorPoint.getBigDecimal("y"));
                            sid = doorPoint.getInteger("id");

                        }
                        else if (doorPoint.getInteger("id") == end_point){
                            ex = toDouble(doorPoint.getBigDecimal("x"));
                            ey = toDouble(doorPoint.getBigDecimal("y"));
                            eid = doorPoint.getInteger("id");
                        }
                    }

                    double pxmin = Math.min(x1, x2);
                    double pxmax = Math.max(x1, x2);
                    double wxmin = Math.min(sx, ex);
                    double wxmax = Math.max(sx, ex);

                    double pymin = Math.min(y1, y2);
                    double pymax = Math.max(y1, y2);
                    double wymin = Math.min(sy, ey);
                    double wymax = Math.max(sy, ey);
                    if (pxmin <= wxmin && pxmax >= wxmax && pymin <= wymin && pymax >= wymax && Math.abs((wymax - pymin) * (pxmax - wxmax) - (wxmax - pxmin) * (pymax - wymax)) < 0.01
                            && Math.abs((wymin - pymin) * (pxmax - wxmin) - (wxmin - pxmin) * (pymax - wymin)) < 0.01 || pxmin == wxmin && pxmax == wxmax
                            && pymin == wymin && pymax == wymax){
                        JSONObject d = new JSONObject();
                        JSONObject startp = new JSONObject();
                        JSONObject endp = new JSONObject();
                        startp.put("x", sx);
                        startp.put("y", sy);
                        startp.put("id", sid);
                        endp.put("x", ex);
                        endp.put("y", ey);
                        endp.put("id", eid);
                        d.put("start_point", startp);
                        d.put("end_point", endp);
                        d.put("host", door.get("host"));
                        d.put("id", doorId);
                        d.put("category", door.get("category"));
                        d.put("isDoor", true);
                        if(!wallList.contains(d)) wallList.add(d);
                    }
                }
//                room.put("door", doorList);

//
//                for (int m = 0; m < windows.size(); ++m){
//                    JSONObject window = windows.getJSONObject(m);
//                    int start_point = window.getInteger("start_point");
//                    int end_point = window.getInteger("end_point");
//                    int doorId = window.getInteger("id");
//                    double sx = 0, sy = 0, ex = 0, ey = 0, sid = 0, eid = 0;
//                    for (int n = 0; n < windowPoints.size(); ++n){
//                        JSONObject windowPoint = windowPoints.getJSONObject(n);
//                        if (windowPoint.getInteger("id") == start_point){
//                            sx = toDouble(windowPoint.getBigDecimal("x"));
//                            sy = toDouble(windowPoint.getBigDecimal("y"));
//                            sid = windowPoint.getInteger("id");
//
//                        }
//                        else if (windowPoint.getInteger("id") == end_point){
//                            ex = toDouble(windowPoint.getBigDecimal("x"));
//                            ey = toDouble(windowPoint.getBigDecimal("y"));
//                            eid = windowPoint.getInteger("id");
//                        }
//                    }
//                    double pxmin = Math.min(x1, x2);
//                    double pxmax = Math.max(x1, x2);
//                    double wxmin = Math.min(sx, ex);
//                    double wxmax = Math.max(sx, ex);
//
//                    double pymin = Math.min(y1, y2);
//                    double pymax = Math.max(y1, y2);
//                    double wymin = Math.min(sy, ey);
//                    double wymax = Math.max(sy, ey);
//                    if (pxmin <= wxmin && pxmax >= wxmax && pymin <= wymin && pymax >= wymax && Math.abs((wymax - pymin) * (pxmax - wxmax) - (wxmax - pxmin) * (pymax - wymax)) < 0.01
//                            && Math.abs((wymin - pymin) * (pxmax - wxmin) - (wxmin - pxmin) * (pymax - wymin)) < 0.01 || pxmin == wxmin && pxmax == wxmax
//                            && pymin == wymin && pymax == wymax){
//                        JSONObject d = new JSONObject();
//                        JSONObject startp = new JSONObject();
//                        JSONObject endp = new JSONObject();
//                        startp.put("x", sx);
//                        startp.put("y", sy);
//                        startp.put("id", sid);
//                        endp.put("x", ex);
//                        endp.put("y", ey);
//                        endp.put("id", eid);
//                        d.put("start_point", startp);
//                        d.put("end_point", endp);
//                        d.put("host", window.get("host"));
//                        d.put("id", doorId);
//                        d.put("category", window.get("category"));
//                        if(!doorList.contains(d)) windowList.add(d);
//                    }
//                }
//                room.put("window", windowList);

                for (int m = 0; m < walls.size(); ++m){
                    JSONObject wall = walls.getJSONObject(m);
                    int start_point = wall.getInteger("start_point");
                    int end_point = wall.getInteger("end_point");
                    int wallId = wall.getInteger("id");
                    double sx = 0, sy = 0, ex = 0, ey = 0, sid = 0, eid = 0;
                    for (int n = 0; n < wallPoints.size(); ++n){
                        JSONObject wallPoint = wallPoints.getJSONObject(n);
                        if (wallPoint.getInteger("id") == start_point){
                            sx = toDouble(wallPoint.getBigDecimal("x"));
                            sy = toDouble(wallPoint.getBigDecimal("y"));
                            sid = wallPoint.getInteger("id");

                        }
                        else if (wallPoint.getInteger("id") == end_point){
                            ex = toDouble(wallPoint.getBigDecimal("x"));
                            ey = toDouble(wallPoint.getBigDecimal("y"));
                            eid = wallPoint.getInteger("id");
                        }
                    }

//
//                    System.out.println("sx " + sx + " sy " + sy + " ex " + ex + " ey " + ey);
//                    System.out.println("x1" + x1 + " y1" + y1 + " x2" + x2 + " y2" + y2);
//                    System.out.println();

                    double pxmin = Math.min(x1, x2);
                    double pxmax = Math.max(x1, x2);
                    double wxmin = Math.min(sx, ex);
                    double wxmax = Math.max(sx, ex);

                    double pymin = Math.min(y1, y2);
                    double pymax = Math.max(y1, y2);
                    double wymin = Math.min(sy, ey);
                    double wymax = Math.max(sy, ey);
                    if (pxmin <= wxmin && pxmax >= wxmax && pymin <= wymin && pymax >= wymax && Math.abs((wymax - pymin) * (pxmax - wxmax) - (wxmax - pxmin) * (pymax - wymax)) < 0.01
                            && Math.abs((wymin - pymin) * (pxmax - wxmin) - (wxmin - pxmin) * (pymax - wymin)) < 0.01 || pxmin == wxmin && pxmax == wxmax
                    && pymin == wymin && pymax == wymax){
                        JSONObject d = new JSONObject();
                        d.put("id", wallId);
                        d.put("isDoor", false);
                        if(!wallList.contains(d)) wallList.add(d);
                    }

                }
                for (int l = 0; l < wallList.size(); ++l)
                    for (int m = 0; m < wallList.size(); ++m) {
                        JSONObject wall = wallList.getJSONObject(l);
                        JSONObject mall = wallList.getJSONObject(m);
                        if (wall != mall && wall.get("id").equals(mall.get("id")))
                            if (wall.get("isDoor").equals(true)) wallList.remove(m);
                            else if (mall.get("isDoor").equals(false)) wallList.remove(l);
                    }

                room.put("wall", wallList);
            }
            System.out.println(wallList);
        }
        return rooms;
    }




    public static void main(String[] args) throws IOException {
//        String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\25697\\Desktop\\hello.json")));
//        JSONObject j = (JSONObject) JSONObject.parse(content);
//
//        JSONObject data1 =  j.getJSONObject("data");
//        JSONObject data2 = (JSONObject) data1.get("DWW");
//
//
//        JSONArray doorList = (JSONArray) data2.get("Doors");
//        JSONArray doorPoints = (JSONArray) data2.get("DoorPoints");
//
//        JSONArray wallList = (JSONArray) data2.get("Walls");
//        JSONArray wallPoints = (JSONArray) data2.get("WallPoints");
//
//        JSONArray windowList = (JSONArray) data2.get("Windows");
//        JSONArray windowPoints = (JSONArray) data2.get("WindowPoints");
////
//        String style = (String) data2.get("style");
//        JSONObject house = (JSONObject) data1.get("house");
//
//        JSONArray rooms = (JSONArray) house.get("Room");
//
//        JSONArray remove = (JSONArray) data1.get("remove");
//
//        findDoorWindow(j);
//
//        System.out.println(rooms);

        JSONArray wa = new JSONArray();
        JSONObject jj = new JSONObject();
        jj.put("x", 0.0);
        jj.put("y", 0.0);
        wa.add(jj);

        jj = new JSONObject();
        jj.put("x", 4.0);
        jj.put("y", 0.0);
        wa.add(jj);

        jj = new JSONObject();
        jj.put("x", 4.0);
        jj.put("y", 4.0);
        wa.add(jj);

        jj = new JSONObject();
        jj.put("x", 0.0);
        jj.put("y", 4.0);
        wa.add(jj);

        JSONObject idx = new JSONObject();
        idx.put("x", 2.0);
        idx.put("y", 3.99);
        System.out.println(isPeopleInHouse(idx, wa));


//        System.out.println(rooms);
//        JSONArray www = roomHandler(rooms.getJSONObject(0), remove);
//
//        System.out.println("www" + www);
//        System.out.println();
//        JSONObject room = rooms.getJSONObject(0);
//        JSONArray array = room.getJSONArray("point");
////        System.out.println(array);
////        System.out.println(roomHandler(rooms.getJSONObject(0), remove));
//        JSONObject wa = new JSONObject();
//        JSONObject p1 = new JSONObject();
//        JSONObject p2 = new JSONObject();
//        p2.put("x", 3.0);
//        p2.put("y", 5.0);
//        p1.put("x", 3.0);
//        p1.put("y", 19.0);
//
//        wa.put("p1", p1);
//        wa.put("p2", p2);
////
//        JSONArray indexs = new JSONArray();
//        JSONObject idx = new JSONObject();
//        idx.put("x", 4.0);
//        idx.put("y", 4.0);
//        idx.put("z", 4.0);
//
//        indexs.add(idx);
//
//        idx = new JSONObject();
//        idx.put("x", 4.0);
//        idx.put("y", 4.0);
//        idx.put("z", -4.0);
//
//        indexs.add(idx);
//
//        idx = new JSONObject();
//        idx.put("x", 4.0);
//        idx.put("y", -4.0);
//        idx.put("z", 4.0);
//
//        indexs.add(idx);
//
//
//        idx = new JSONObject();
//        idx.put("x", -4.0);
//        idx.put("y", 4.0);
//        idx.put("z", 4.0);
//
//        indexs.add(idx);
//
//        idx = new JSONObject();
//        idx.put("x", -4.0);
//        idx.put("y", -4.0);
//        idx.put("z", 4.0);
//
//        indexs.add(idx);
//
//
//        idx = new JSONObject();
//        idx.put("x",-4.0);
//        idx.put("y", 4.0);
//        idx.put("z", -4.0);
//
//        indexs.add(idx);
//
//        idx = new JSONObject();
//        idx.put("x", 4.0);
//        idx.put("y", -4.0);
//        idx.put("z", -4.0);
//
//        indexs.add(idx);
//
//        idx = new JSONObject();
//        idx.put("x", -4.0);
//        idx.put("y", -4.0);
//        idx.put("z", -4.0);
//
//        indexs.add(idx);
////
////
////
//        JSONObject center = new JSONObject();
//        center.put("x", 2.0);
//        center.put("y", 2.0);
//        center.put("z", 2.0);
//
////
////        System.out.println(insertWallGoods(indexs, wa, center));
//
//
//        JSONObject target = new JSONObject();
//        target.put("x", 3.0);
//        target.put("y", 3.0);
//        target.put("z", 3.0);
//
//        JSONObject rotate = new JSONObject();
//        rotate.put("x", 0);
//        rotate.put("y",0);
//        rotate.put("z", 0);
//
//
//        System.out.println(indexs);
//



    }
}

