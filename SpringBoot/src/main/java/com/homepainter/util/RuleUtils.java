package com.homepainter.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.controller.HouseIdentifyController;
import javafx.util.Pair;
import org.apache.commons.collections4.CollectionUtils;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.homepainter.util.HouseIdentifyHandler.toDouble;

public class RuleUtils {

    /**
     * <p>
     * Principle of pnpoly algorithm: draw a ray from a target point,
     * and count the number of intersections between this ray and the pair of deformations.
     * If there are an odd number of intersections, the target point is inside the polygon,
     * and if there are even intersections, it is outside
     * </p>
     *
     * @param vertX polygon coordinates latitudes
     * @param vertY polygon coordinates longitudes
     * @param x     longitude
     * @param y     latitude
     * @return true indicate inside of the polygon， false indicate outside of the polygon
     */
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

    public static boolean isInHouse(List<HashMap<String, Object>> index, List<HashMap<String, Object>> wallPoints, float up){

        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        List<Double> z = new ArrayList<>();

        for (HashMap<String, Object> idx : index)
            z.add((double) idx.get("z"));

        for (HashMap<String, Object> wallPoint : wallPoints){
            x.add((double) wallPoint.get("x"));
            y.add((double) wallPoint.get("y"));
        }

        for (Double i : z)
            if (i > up || i < 0) {return false;}

        for (HashMap<String, Object> idx : index)
            if (!pnpolyAlgorithm(x, y, (double) idx.get("x"), (double)idx.get("y"))) {
                System.out.println(x);
                System.out.println(y);
                System.out.println(idx);
                return false;
            }

        return true;

    }

    public static boolean inputGoods(List<HashMap<String, Object>> goods, List<HashMap<String, Object>> wallPoints, float up, List<HashMap<String, Object>> index, double x, double y, double z){
        for (HashMap<String, Object> idx : index){
            double ix = (double) idx.get("x");
            double iy = (double) idx.get("y");
            double iz = (double) idx.get("z");

            idx.put("x", ix + x);
            idx.put("y", iy + y);
            idx.put("z", iz + z);
        }
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

     public static List roomHandler(JSONObject room, JSONArray remove){
        JSONArray points = (JSONArray) room.get("point");
         List<Object> walls = new ArrayList<>();
         for (int a = 0; a < points.size() - 1; ++a) {
             Map<String, Object> wall = new HashMap<>();
             JSONObject p1 = (JSONObject) points.get(a);
             JSONObject p2 = (JSONObject) points.get(a + 1);
             int s = (int) p1.get("id");
             int e = (int) p2.get("id");
             wall.put("p1", p1);
             wall.put("p2", p2);
             wall.put("category", 0);

             for (int i = 0; i < remove.size(); ++i){
                 JSONObject r = (JSONObject) remove.get(i);
                 int wsid = (int) r.get("wsid");
                 int weid = (int) r.get("weid");


              //   System.out.println(s + " " + e + " ws " + wsid + " " + weid);
                 if (wsid == s && weid == e || wsid == e && weid == s) {
                     wall.put("category", r.get("category"));
                     wall.put("dx1", r.get("dx1") );
                     wall.put("dx2", r.get("dx2") );
                     wall.put("dy1", r.get("dy1") );
                     wall.put("dy2", r.get("dy2") );
                 }
             }
             walls.add(wall);
         }
         System.out.println(walls);
        return walls;
    }


    public static void recordIndex(List<HashMap<String, Object>> goods, List<HashMap<String, Object>> index){
        List<Double> xtmp = new ArrayList<>();
        List<Double> ytmp = new ArrayList<>();
        List<Double> ztmp = new ArrayList<>();

        for (HashMap<String, Object> idx : index) {
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

    public static boolean checkIndex(List<HashMap<String, Object>> goods, List<HashMap<String, Object>> index){
        for (HashMap<String, Object> idx : index) {
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

    public static void findWall(List<HashMap<String, Object>> wallPoints, List<HashMap<String, Object>> remove){
        for (HashMap<String, Object> wall : wallPoints){

        }
    }




    public static void main(String[] args) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\25697\\Desktop\\hello.json")));
        JSONObject j = (JSONObject) JSONObject.parse(content);

        JSONObject data1 = (JSONObject) j.get("data");
        JSONObject data2 = (JSONObject) data1.get("RWW");

        JSONArray doorList = (JSONArray) data2.get("Doors");
        JSONArray doorPoints = (JSONArray) data2.get("DoorPoints");

        JSONArray wallList = (JSONArray) data2.get("Walls");
        JSONArray wallPoints = (JSONArray) data2.get("WallPoints");

        JSONArray windowList = (JSONArray) data2.get("Windows");
        JSONArray windowPoints = (JSONArray) data2.get("WindowPoints");

        String style = (String) data2.get("style");
        JSONObject house = (JSONObject) j.get("house");

       JSONArray rooms = (JSONArray) house.get("Room");

       JSONArray remove = (JSONArray) data1.get("remove");
//       roomHandler((JSONObject) rooms.get(8), remove);


      List<HashMap<String, Object>> l = new ArrayList<>();
      HashMap<String, Object> x = new HashMap<>();
      HashMap<String, Object> y = new HashMap<>();
      HashMap<String, Object> z = new HashMap<>();
      x.put("start", 1.1);
      x.put("end", 5.5);
      y.put("start", 1.1);
      y.put("end", 5.5);
      z.put("start", 1.1);
      z.put("end", 5.5);

      HashMap<String, Object> m = new HashMap<>();
      m.put("x", x);
      m.put("y", y);
      m.put("z", z);

      l.add(m);

      x = new HashMap<>();
     y = new HashMap<>();
        z = new HashMap<>();
        x.put("start", 7.7);
        x.put("end", 8.8);
        y.put("start", 7.7);
        y.put("end", 8.8);
        z.put("start", 7.7);
        z.put("end", 8.8);

        m = new HashMap<>();
        m.put("x", x);
        m.put("y", y);
        m.put("z", z);

        l.add(m);

      List<HashMap<String, Object>> indexs = new ArrayList<>();
        HashMap<String, Object> index = new HashMap<>();
        index.put("x", 3.14);
        index.put("y", 3.14);
        index.put("z", 5.6);
        indexs.add(index);

         index = new HashMap<>();
        index.put("x", 7.9);
        index.put("y",7.9);
        index.put("z", 7.2);

        indexs.add(index);

        List<HashMap<String, Object>> wa = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("x", 10.0);
        map.put("y", 10.0);
        wa.add(map);

        map = new HashMap<>();
        map.put("x", 0.0);
        map.put("y", 10.0);
        wa.add(map);

        map = new HashMap<>();
        map.put("x", 0.0);
        map.put("y", 0.0);
        wa.add(map);

        map = new HashMap<>();
        map.put("x", 10.0);
        map.put("y", 0.0);
        wa.add(map);


//        List<Double> dx = new ArrayList<>(); dx.add(0.0);dx.add(10.0);dx.add(10.0);dx.add(0.0);
//        List<Double> dy = new ArrayList<>(); dy.add(0.0);dy.add(0.0);dy.add(10.0);dy.add(10.0);
        System.out.println(checkIndex(l, indexs));
//        System.out.println(pnpolyAlgorithm(dx, dy, 3.14, 9.99));
        System.out.println(isInHouse(indexs, wa, 10f));
        System.out.println(inputGoods(l, wa, 9f, indexs, 1, 1, 1));
        System.out.println(indexs);
    }
}

