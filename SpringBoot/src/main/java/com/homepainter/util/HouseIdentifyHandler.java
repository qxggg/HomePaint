package com.homepainter.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class

HouseIdentifyHandler {
    public static int count = 10000;
    public static Object getResult(JSONObject data2) {



        JSONArray doorList = (JSONArray) data2.get("Doors");
        JSONArray doorPoints = (JSONArray) data2.get("DoorPoints");

        JSONArray wallList = (JSONArray) data2.get("Walls");
        JSONArray wallPoints = (JSONArray) data2.get("WallPoints");

        JSONArray windowList = (JSONArray) data2.get("Windows");
        JSONArray windowPoints = (JSONArray) data2.get("WindowPoints");

       JSONArray remove = new JSONArray();
        JSONArray wallAdd = new JSONArray();

        for (int i = 0; i < doorList.size(); ++i){
            JSONObject door = (JSONObject) doorList.get(i);
            int dstart = (int) door.get("start_point");
            int dend = (int) door.get("end_point");
            int did = (int) door.get("id");

            double dx1 = 0, dx2 = 0, dy1 = 0, dy2 = 0;
            int dsid = 0, deid = 0;
            BigDecimal tmp;

            for (int j = 0; j < doorPoints.size(); ++j){
                JSONObject doorPoint = (JSONObject) doorPoints.get(j);
                if ((int) doorPoint.get("id") == dstart) {
                    dx1 = toDouble( (BigDecimal) doorPoint.get("x"));
                    dy1 = toDouble( (BigDecimal) doorPoint.get("y"));
                    dsid = (int) doorPoint.get("id");
                }

                if ((int) doorPoint.get("id") == dend){
                    dx2 = toDouble( (BigDecimal)  doorPoint.get("x"));
                    dy2 = toDouble( (BigDecimal) doorPoint.get("y"));
                    deid = (int) doorPoint.get("id");
                }
            }

            JSONArray addList = new JSONArray();

            for (int k = 0; k < wallList.size(); ++k){

                JSONObject wall = (JSONObject) wallList.get(k);
                int wstart = (int) wall.get("start_point");
                int wend = (int) wall.get("end_point");
                int wid = (int) wall.get("id");

                double wx1 = 0, wx2 = 0, wy1 = 0, wy2 = 0;
                int wsid = 0, weid = 0;

                for (int m = 0; m < wallPoints.size(); ++m){
                    JSONObject wallPoint = (JSONObject) wallPoints.get(m);
                    if ((int) wallPoint.get("id") == wstart){
                        wx1 = toDouble((BigDecimal) wallPoint.get("x"));
                        wy1 = toDouble((BigDecimal) wallPoint.get("y"));
                        wsid = (int) wallPoint.get("id");
                    }

                    if ((int) wallPoint.get("id") == wend){
                        wx2 = toDouble((BigDecimal) wallPoint.get("x"));
                        wy2 = toDouble((BigDecimal) wallPoint.get("y"));
                        weid = (int) wallPoint.get("id");
                    }
                }



                if (wx1 == dx1 && wx2 == dx2 && wy2 > dy2 && wy1 < dy1 || wy1 == dy1 && wy2 == dy2 && wx1 < dx1 && wx2 > dx2){

                    JSONObject wall1 = new JSONObject();
                    wall1.put("start_point", wsid);
                    wall1.put("end_point", dsid);
                    wall1.put("id", count++);


                    JSONObject wall2 = new JSONObject();
                    wall2.put("start_point", deid);
                    wall2.put("end_point", weid);
                    wall2.put("id", count++);

                    wallList.add(wall1);
                    wallList.add(wall2);
                    JSONObject m = new JSONObject();
                    m.put("category", 1);
                    m.put("wsid", wsid);
                    m.put("weid", weid);
                    m.put("dx1", dx1);
                    m.put("dy1", dy1);
                    m.put("dx2", dx2);
                    m.put("dy2", dy2);
                    remove.add(m);
                    wallList.remove(k);


                    JSONObject wallpoint = new JSONObject();
                    wallpoint.put("id", dsid);
                    wallpoint.put("x", dx1);
                    wallpoint.put("y", dy1);

                    wallAdd.add(wallpoint);

                    JSONObject wallpoint2 = new JSONObject();
                    wallpoint2.put("id", deid);
                    wallpoint2.put("x", dx2);
                    wallpoint2.put("y", dy2);
                    wallAdd.add(wallpoint2);


                }

                else if(wx2 - wx1 != 0 && wy2 - wy1 != 0 && Math.abs((wy2 - wy1) * (dx2 - dx1) - (wx2 - wx1) * (dy2 - dy1)) <= 0.01 &&  (dy1 - wy1) * (wx2 - dx1) - (wy2 - dy1) * (dx1 - wx1) <= 0.01){
                    JSONObject wall1 = new JSONObject();
                    wall1.put("start_point", wsid);
                    wall1.put("end_point", dsid);
                    wall1.put("id", count++);


                    JSONObject wall2 = new JSONObject();
                    wall2.put("start_point", deid);
                    wall2.put("end_point", weid);
                    wall2.put("id", count++);

                    wallList.add(wall1);
                    wallList.add(wall2);

                    JSONObject m = new JSONObject();
                    m.put("category", 1);
                    m.put("wsid", wsid);
                    m.put("weid", weid);
                    m.put("dx1", dx1);
                    m.put("dy1", dy1);
                    m.put("dx2", dx2);
                    m.put("dy2", dy2);
                    remove.add(m);
                    wallList.remove(k);




                    JSONObject wallpoint = new JSONObject();
                    wallpoint.put("id", dsid);
                    wallpoint.put("x", dx1);
                    wallpoint.put("y", dy1);

                    wallAdd.add(wallpoint);

                    JSONObject wallpoint2 = new JSONObject();
                    wallpoint2.put("id", deid);
                    wallpoint2.put("x", dx2);
                    wallpoint2.put("y", dy2);
                    wallAdd.add(wallpoint2);



                }


            }

        }

        for (int i = 0; i < windowList.size(); ++i){
            JSONObject window = (JSONObject) windowList.get(i);
            int dstart = (int) window.get("start_point");
            int dend = (int) window.get("end_point");
            int did = (int) window.get("id");

            double dx1 = 0, dx2 = 0, dy1 = 0, dy2 = 0;
            int dsid = 0, deid = 0;
            BigDecimal tmp;

            for (int j = 0; j < windowPoints.size(); ++j){
                JSONObject windowPoint = (JSONObject) windowPoints.get(j);
                if ((int) windowPoint.get("id") == dstart) {
                    dx1 = toDouble( (BigDecimal) windowPoint.get("x"));
                    dy1 = toDouble( (BigDecimal) windowPoint.get("y"));
                    dsid = (int) windowPoint.get("id");
                }

                if ((int) windowPoint.get("id") == dend){
                    dx2 = toDouble( (BigDecimal)  windowPoint.get("x"));
                    dy2 = toDouble( (BigDecimal) windowPoint.get("y"));
                    deid = (int) windowPoint.get("id");
                }
            }

            JSONArray addList = new JSONArray();

            for (int k = 0; k < wallList.size(); ++k){
                JSONObject wall = (JSONObject) wallList.get(k);
                int wstart = (int) wall.get("start_point");
                int wend = (int) wall.get("end_point");
                int wid = (int) wall.get("id");

                double wx1 = 0, wx2 = 0, wy1 = 0, wy2 = 0;
                int wsid = 0, weid = 0;

                for (int m = 0; m < wallPoints.size(); ++m){
                    JSONObject wallPoint = (JSONObject) wallPoints.get(m);
                    if ((int) wallPoint.get("id") == wstart){
                        wx1 = toDouble((BigDecimal) wallPoint.get("x"));
                        wy1 = toDouble((BigDecimal) wallPoint.get("y"));
                        wsid = (int) wallPoint.get("id");
                    }

                    if ((int) wallPoint.get("id") == wend){
                        wx2 = toDouble((BigDecimal) wallPoint.get("x"));
                        wy2 = toDouble((BigDecimal) wallPoint.get("y"));
                        weid = (int) wallPoint.get("id");
                    }
                }

                if (wx1 == dx1 && wx2 == dx2 && wy2 > dy2 && wy1 < dy1 || wy1 == dy1 && wy2 == dy2 && wx1 < dx1 && wx2 > dx2){
                    JSONObject wall1 = new JSONObject();
                    wall1.put("start_point", wsid);
                    wall1.put("end_point", dsid);
                    wall1.put("id", count++);


                    JSONObject wall2 = new JSONObject();
                    wall2.put("start_point", deid);
                    wall2.put("end_point", weid);
                    wall2.put("id", count++);



                    wallList.add(wall1);
                    wallList.add(wall2);

                    JSONObject m = new JSONObject();
                    m.put("category", 2);
                    m.put("wsid", wsid);
                    m.put("weid", weid);
                    m.put("dx1", dx1);
                    m.put("dy1", dy1);
                    m.put("dx2", dx2);
                    m.put("dy2", dy2);
                    remove.add(m);
                    wallList.remove(k);


                    JSONObject wallpoint = new JSONObject();
                    wallpoint.put("id", dsid);
                    wallpoint.put("x", dx1);
                    wallpoint.put("y", dy1);

                    wallAdd.add(wallpoint);

                    JSONObject wallpoint2 = new JSONObject();
                    wallpoint2.put("id", deid);
                    wallpoint2.put("x", dx2);
                    wallpoint2.put("y", dy2);
                    wallAdd.add(wallpoint2);

                }

                else if(wx2 - wx1 != 0 && wy2 - wy1 != 0 && Math.abs((wy2 - wy1) * (dx2 - dx1) - (wx2 - wx1) * (dy2 - dy1)) <= 0.01 && (dy1 - wy1) * (wx2 - dx1) - (wy2 - dy1) * (dx1 - wx1) <= 0.01){
                    JSONObject wall1 = new JSONObject();
                    wall1.put("start_point", wsid);
                    wall1.put("end_point", dsid);
                    wall1.put("id", count++);


                    JSONObject wall2 = new JSONObject();
                    wall2.put("start_point", deid);
                    wall2.put("end_point", weid);
                    wall2.put("id", count++);

                    wallList.add(wall1);
                    wallList.add(wall2);

                    JSONObject m = new JSONObject();

                    m.put("category", 2);
                    m.put("wsid", wsid);
                    m.put("weid", weid);
                    m.put("dx1", dx1);
                    m.put("dy1", dy1);
                    m.put("dx2", dx2);
                    m.put("dy2", dy2);
                    remove.add(m);
                    wallList.remove(k);


                    JSONObject wallpoint = new JSONObject();
                    wallpoint.put("id", dsid);
                    wallpoint.put("x", dx1);
                    wallpoint.put("y", dy1);

                    wallAdd.add(wallpoint);

                    JSONObject wallpoint2 = new JSONObject();
                    wallpoint2.put("id", deid);
                    wallpoint2.put("x", dx2);
                    wallpoint2.put("y", dy2);
                    wallAdd.add(wallpoint2);

                }
            }

        }


        for (Object jj : wallAdd)
            wallPoints.add(jj);

        data2.put("remove", remove);
        return data2;
    }

    public static double toDouble(BigDecimal a){
        return a.doubleValue();
    }


    public static void main(String[] args) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\25697\\Desktop\\第十六届全国大学生软件创新大赛\\HomePaint\\json.json")));
        JSONObject j = (JSONObject) JSONObject.parse(content);
        HouseIdentifyHandler.getResult(j);


    }

}
