package com.homepainter.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.service.ChangeStyleService;
import com.homepainter.service.GetGoods;
import com.homepainter.service.SpliteHouseService;
import com.homepainter.util.HouseIdentifyHandler;
import com.homepainter.util.RedisUtil;

import com.homepainter.util.RuleUtils;
import com.qcloud.cos.utils.Jackson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.homepainter.util.DirectoryDeleter.deleteDirectory;
import static com.homepainter.util.FileUtils.createDir;
import static com.homepainter.util.FileUtils.isExit;
import static com.homepainter.util.ReadJson.readJson;
import static com.homepainter.util.WriteJson.writejson;

@RequestMapping("/houseData")
@RestController
public class HouseDataController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ChangeStyleService changeStyleService;

    @Autowired
    SpliteHouseService spliteHouse;

    @Autowired
    GetGoods getGoods;

    public String filename = "HouseData.json";

    /**
        风格切换
     */

    @PostMapping("/ChangeStyle")
    public JSONObject changeStyle(@RequestBody JSONObject data, @RequestHeader String token){
        JSONObject res = new JSONObject();
        JSONArray goods = data.getJSONObject("furniture").getJSONArray("goods");
        JSONArray rooms = data.getJSONObject("house").getJSONArray("Room");
        String style = data.getJSONObject("furniture").getString("style");

        List<Integer> changeRooms = (List<Integer>) data.getJSONObject("furniture").get("ChangeStyle");
        JSONObject furniure = data.getJSONObject("furniture");

        changeStyleService.changeStyle(style, rooms, furniure, changeRooms);

        furniure.remove("style");
        furniure.remove("ChangeStyle");
        save(data, (String) redisUtil.get(token));

        res.put("data", furniure);
        res.put("code", 0);
        res.put("msg", "更换后的家具如上");
        return res;
    }

    /**
        保存户型图蓝图信息，没有则新建文件夹，文件夹名为用户id
     */

    public void save(JSONObject input, String userid){
        String dir = "HouseData/"+userid+"/";
        if(isExit(filename,dir)){
            // 直接覆盖
        }else{
            // 创建文件夹
            createDir(dir);
        }

        // 覆盖文件
        try{
            writejson(input,filename,dir);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("2dto3d")
    public JSONObject twoToThree(@RequestBody JSONObject data, @RequestHeader String token){
        JSONObject res = new JSONObject();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        JSONArray rooms = data.getJSONObject("house").getJSONArray("Room");
        JSONObject furniture = data.getJSONObject("furniture");
        JSONArray allFloors = furniture.getJSONArray("floor");
        JSONArray allWallpaint = furniture.getJSONArray("WallPaper");
        for (int i = 0; i < rooms.size(); ++i){
            JSONObject room = rooms.getJSONObject(i);
            int roomId = room.getInteger("roomId");
            if (room.getString("style") == null) continue;
            String style = room.getString("style");
            List<Map<String, Object>> floors = getGoods.changeFloorStyle(style);
            int random = floors.size() - 1;
            Map<String, Object> floor = floors.get(random);
            List<Map<String, Object>> wallpaints = getGoods.changeWallpaintStyle(style);
            random = wallpaints.size() - 1;
            Map<String, Object> wallpaint = wallpaints.get(random);
            getFloorWalls(allFloors, i, roomId, floor);

            getFloorWalls(allWallpaint, i, roomId, wallpaint);

        }
        save(data, (String) redisUtil.get(token));
        res.put("data", data);
        res.put("code", 0);
        res.put("msg", "打标签成功了！！！");
        return res;
    }

    private void getFloorWalls(JSONArray allWallpaint, int i, int roomId, Map<String, Object> wallpaint) {
        for (int j = 0; j < allWallpaint.size(); ++j) {
            JSONObject f = allWallpaint.getJSONObject(i);
            if (f.getInteger("roomId") == roomId) {
                f.put("imageURL", wallpaint.get("imageURL"));
                f.put("price", wallpaint.get("price"));
                f.put("id", wallpaint.get("id"));
            }
        }
    }

    @PostMapping("/AddWall")
    public JSONObject addWall(@RequestBody HashMap<String, Object> data, @RequestHeader String token) throws Exception {
        HashMap<String, Object> origin = (HashMap<String, Object>) data.get("origin");
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        JSONObject res = new JSONObject();
        // 抠门扣窗
        try{
            data.put("DWW", HouseIdentifyHandler.getResult(JSON.parseObject( JSON.toJSONString( origin))));
        }catch (Exception e){
            res.put("code",21);
            res.put("Exception",e);
            res.put("msg","抠门扣窗失败");
            return res;
        }

        Map<String, Object> IdentifyResult = new HashMap<>();
        IdentifyResult.put("data", origin);
        IdentifyResult.put("code", 0);
        // 分房算法
        try {
            Map<String,Object> house = spliteHouse.SpliteHouseController(IdentifyResult,userId);
            data.put("house",house);
        }catch (Exception e){
            res.put("code",22);
            res.put("Exception",e);
            res.put("msg","分房算法失败");
            return res;
        }


        res.put("data", data);
        res.put("code", 0);
        res.put("msg", "户型识别检索成功");

        //给房间绑定门窗 //给room打id
        try{
            JSONArray rooms = RuleUtils.findDoorWindow((JSONObject) JSONObject.parse(res.toJSONString()));
            HashMap<String, Object> house = (HashMap<String, Object>) data.get("house");
            JSONObject furniture = new JSONObject();
            data.put("furniture", furniture);
            JSONArray floor = new JSONArray();
            JSONArray door = new JSONArray();
            JSONArray wallPaper = new JSONArray();
            JSONArray light = new JSONArray();
            JSONArray defaultFloors = new JSONArray();
            JSONArray defaultWallpaints = new JSONArray();
            JSONArray defaultLights = new JSONArray();
            for (int i = 0; i < rooms.size(); ++i) {
                JSONObject defaultFloor = new JSONObject();
                JSONObject defaultWallpaint = new JSONObject();
                JSONObject defaultLight = new JSONObject();
                JSONObject room = rooms.getJSONObject(i);

                defaultWallpaint.put("id", 780);
                defaultWallpaint.put("imageURL", "https://image-1304455659.cos.ap-nanjing.myqcloud.com/BiZhi/780.jpg");
                defaultWallpaint.put("roomId", i);
                defaultWallpaint.put("price", 15);
                defaultWallpaints.add(defaultWallpaint);
                defaultFloor.put("id", 6);
                defaultFloor.put("imageURL", "https://image-1304455659.cos.ap-nanjing.myqcloud.com/DiBan/6.jpg");
                defaultFloor.put("roomId", i);
                defaultFloor.put("price", 299.9);
                defaultLight.put("roomId", i);

                if (room.getString("name").equals("洗漱间") || room.getString("name").equals("卫生间")){
                }
                else if (room.getString("name").equals("阳台") || room.getString("name").equals("厨房")){
                }
                else if (room.getString("name").equals("客厅")){
                    defaultLight.put("modalId", "3f546069-dc34-425c-87d0-f1cc1f858a5c");
                    defaultLight.put("goodsId", 1345);
                    defaultLight.put("price", 1000);
                    defaultLight.put("center", room.getJSONObject("center"));
                }
                else{
                    defaultLight.put("modalId", "38ea2314-1803-442e-9add-ace97d2959a2");
                    defaultLight.put("goodsId", 141);
                    defaultLight.put("price", 1000);
                    defaultLight.put("center", room.getJSONObject("center"));
                }
                rooms.getJSONObject(i).put("roomId", i);


                defaultFloors.add(defaultFloor);
                //     defaultWallpaints.add(defaultWallpaint);
                defaultLights.add(defaultLight);
            }
            furniture.put("floor", defaultFloors);
            furniture.put("WallPaper", defaultWallpaints);
            furniture.put("goods", new ArrayList());
            furniture.put("light", defaultLights);

            house.put("Room", rooms);
        }catch (Exception e){
            res.put("code", 24);
            res.put("Exception", e);
            res.put("msg", "绑定门窗失败");
        }
        return res;
    }

    @PostMapping("/save")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public Map<String, Object> save_house(@RequestBody Map<String, Object> input, @RequestHeader String token){
        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);


        String userid =(String) redisUtil.get(token);

        String dir = "HouseData/"+userid+"/";
        if(isExit(filename,dir)){
            // 直接覆盖
        }else{
            // 创建文件夹
            createDir(dir);
        }

        // 覆盖文件
        try{
            writejson(input,filename,dir);
        }catch (Exception e){
            res.put("code",10);
            res.put("Exception",e);
            res.put("msg","保存出错");
            return res;
        }

        res.put("msg", "保存成功");
        return res;
    }



    /**
        查询 户型图 蓝图信息 根据id查询
     */
    @GetMapping("/get")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public Map<String, Object> get_house(@RequestHeader String token){
        Map<String, Object> res = new HashMap<>();

        String id =(String) redisUtil.get(token);
        String dir = "HouseData/"+id+"/";

        if(!isExit(filename,dir)){
            res.put("code",0);
            res.put("msg","无此用户数据");
            res.put("data",null);
            return res;
        }

        try {
            String resString = readJson(filename,dir);
            Map<String,Object> json = JSON.parseObject(resString);
            res.put("data",json);
        }catch (Exception e){
            res.put("code",11);
            res.put("Exception",e);
            res.put("msg","保存出错");
            return res;
        }

        res.put("code",0);
        res.put("msg","读取成功");
        return res;
    }

    /**
     * 比例尺调整
     * @param input
     * @param token
     * @return
     */
    @PostMapping("/ChangeScale")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public Map<String ,Object> ChangeScale(@RequestBody Map<String,Object> input,@RequestHeader String token){
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> data = new HashMap<>();

        double area = Double.parseDouble( input.get("area").toString() );
        data.put("area",area);
        Map<String,Object> DWW = (Map<String, Object>) input.get("DWW");
        Map<String,Object> house = (Map<String, Object>) input.get("house");
        Map<String,Object> House = (Map<String, Object>) house.get("House");

        try{
            double scale = Math.sqrt( area / Double.parseDouble( House.get("area").toString() ) );
            double scaleArea = area / Double.parseDouble( House.get("area").toString() );
            // 调整所有 x,y 直接乘scale
            List<Map<String,Object>> WallPoints = (List<Map<String, Object>>) DWW.get("WallPoints");
            for(int i=0;i<WallPoints.size();i++){
                WallPoints.get(i).put("x",Double.parseDouble( WallPoints.get(i).get("x").toString() )*scale) ;
                WallPoints.get(i).put("y",Double.parseDouble( WallPoints.get(i).get("y").toString() )*scale) ;
            }
            DWW.put("WallPoints",WallPoints);

            List<Map<String,Object>> DoorPoints = (List<Map<String, Object>>) DWW.get("DoorPoints");
            for(int i=0;i<DoorPoints.size();i++){
                DoorPoints.get(i).put("x",Double.parseDouble( DoorPoints.get(i).get("x").toString() )*scale) ;
                DoorPoints.get(i).put("y",Double.parseDouble( DoorPoints.get(i).get("y").toString() )*scale) ;
            }
            DWW.put("DoorPoints",DoorPoints);

            List<Map<String,Object>> WindowPoints = (List<Map<String, Object>>) DWW.get("WindowPoints");
            for(int i=0;i<WindowPoints.size();i++){
                WindowPoints.get(i).put("x",Double.parseDouble( WindowPoints.get(i).get("x").toString() )*scale) ;
                WindowPoints.get(i).put("y",Double.parseDouble( WindowPoints.get(i).get("y").toString() )*scale) ;
            }
            DWW.put("WindowPoints",WindowPoints);

            // remove
            List<Map<String,Object>> remove = (List<Map<String, Object>>) DWW.get("remove");
            for(int i=0;i<remove.size();i++){
                remove.get(i).put("dx1",Double.parseDouble( remove.get(i).get("dx1").toString() )*scale) ;
                remove.get(i).put("dx2",Double.parseDouble( remove.get(i).get("dx2").toString() )*scale) ;
                remove.get(i).put("dy1",Double.parseDouble( remove.get(i).get("dy1").toString() )*scale) ;
                remove.get(i).put("dy2",Double.parseDouble( remove.get(i).get("dy2").toString() )*scale) ;
            }
            DWW.put("remove",remove);

            data.put("DWW",DWW);

            // 调整house-House里面的点
            List<Map<String,Object>> HousePoint = (List<Map<String, Object>>) House.get("point");
            for(int i=0;i<HousePoint.size();i++){
                HousePoint.get(i).put("x",Double.parseDouble( HousePoint.get(i).get("x").toString() )*scale) ;
                HousePoint.get(i).put("y",Double.parseDouble( HousePoint.get(i).get("y").toString() )*scale) ;
            }
            Map<String,Object> center = (Map<String, Object>) House.get("center");
            center.put("x",Double.parseDouble( center.get("x").toString() ) *scale );
            center.put("y",Double.parseDouble( center.get("y").toString() ) *scale );

            House.put("point",HousePoint);
            House.put("area",area);
            House.put("center",center);

            house.put("House",House);

            // 调整house-room里面的点
            List<Map<String,Object>> Room = (List<Map<String, Object>>) house.get("Room");
            List<Map<String,Object>> RoomRes = new ArrayList<>();
            for(int i=0;i<Room.size();i++){
                Map<String,Object> roomSingle = Room.get(i);
                roomSingle.put("area",Double.parseDouble(roomSingle.get("area").toString())*scaleArea);

                Map<String,Object> roomCenter = (Map<String, Object>) roomSingle.get("center");
                roomCenter.put("x",Double.parseDouble(roomCenter.get("x").toString())*scale);
                roomCenter.put("y",Double.parseDouble(roomCenter.get("y").toString())*scale);
                roomSingle.put("center",roomCenter);

                List<Map<String,Object>> roomPoint = (List<Map<String, Object>>) roomSingle.get("point");
                for(int j=0;j<roomPoint.size();j++){

                    roomPoint.get(j).put("x",Double.parseDouble(roomPoint.get(j).get("x").toString())*scale);
                    roomPoint.get(j).put("y",Double.parseDouble(roomPoint.get(j).get("y").toString())*scale);

                }
                roomSingle.put("point",roomPoint);

                RoomRes.add(roomSingle);
            }
            house.put("Room",RoomRes);

            data.put("house",house);

            // 重新计算总面积
            int n = HousePoint.size();
            double matrix[][] = new double[n][n];
            for(int i=0;i<n;i++){
                matrix[i][0] = Double.parseDouble( HousePoint.get(i).get("x").toString() );
                matrix[i][1] = Double.parseDouble( HousePoint.get(i).get("y").toString() );
            }
            double areaAfterCalcuate = getArea(matrix);
            data.put("areaAfterCalcuate",areaAfterCalcuate);

            // save
            save_house(data,token);
        }catch (Exception e){
            res.put("code",12);
            res.put("msg","比利尺调整失败");
            res.put("Exception",e);
            return  res;
        }



        res.put("data",data);
        res.put("code",0);
        res.put("msg","调整成功");
        return res;
    }

    @GetMapping("/delete")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public Map<String ,Object> delete(@RequestHeader String token){
        Map<String ,Object> res = new HashMap<>();

        String id =(String) redisUtil.get(token);
        String dir = "HouseData/"+id+"/";

        try {
            deleteDirectory(dir);
        }catch (Exception e){
            res.put("code",14);
            res.put("msg","删除失败");
            res.put("Exception",e.toString());
        }

        res.put("code",0);
        res.put("msg","删除成功");
        return res;
    }

    @GetMapping
    @RequestMapping("/GetUserFurniture")
    public Map<String, Object> GetUserFurniture(@RequestHeader String token){
        Map<String ,Object> res = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String jsonString = GetUserHouse(userId).toString();
        JSONObject j = JSONObject.parseObject(jsonString);
        JSONArray array = j.getJSONObject("furniture").getJSONArray("goods");
        List<Map<String, Object>> l = new ArrayList<>();
        for (int i = 0; i < array.size(); ++i){
            JSONObject good = array.getJSONObject(i);
            String modalId = good.getString("modalId");
            String sql = "select * from goods where modalId = '" + modalId + "'";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql);
            l.add(map);
        }

        res.put("code", 0);
        res.put("msg", "success");
        res.put("data", l);
        return res;
     }

    public double getArea(double[][] matrix) {
        double area = 0.0;
        int n = matrix.length;

        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            area += matrix[i][0] * matrix[j][1];
            area -= matrix[i][1] * matrix[j][0];
        }

        area /= 2.0;
        return Math.abs(area);
    }


    /**
     * 获取 用户 房间信息
     * @param id
     * @return
     */
    public static Map<String,Object> GetUserHouse(Integer id){
        Map<String,Object> res = new HashMap<>();
        try {
            String dir = "HouseData/token"+id+"/";
            String resString = readJson("HouseData.json",dir);
            res = JSON.parseObject(resString);
        }catch (Exception e){
            res.put("code",11);
            res.put("Exception",e);
            res.put("msg","保存出错");
            return res;
        }
        return res;
    }

    public static void main(String args[]){
        System.out.println( GetUserHouse(105) );
    }

}
