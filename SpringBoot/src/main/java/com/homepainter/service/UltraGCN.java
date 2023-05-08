package com.homepainter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.homepainter.util.ExcuteCmd.excuteCmd;
import static com.homepainter.util.ReadJson.readJson;
import static com.homepainter.util.getStyleUtils.getStyle;

@Service
@Component
public class UltraGCN {
    @Autowired
    OutXlxsUltraGCNService outXlxsUltraGCNService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String args[]){
        List<Integer> res = GetStyle(1);

        List<Integer> temp = GetFurniture(1);
    }

    /**
     * 每 15*60*1000 ms  = 900000 ms 15分钟执行一次 ，风格训练算法
     */
    @Scheduled(fixedRate = 900000)
    public void RunStyle() throws IOException {
        // 在这里编写你的定时任务逻辑
        System.out.println("开始执行风格训练算法 "+new Date().getTime());

        // 编写Excel
        outXlxsUltraGCNService.writeStyleXlxs();

        // 运行shello脚本
        excuteCmd("UltraGCN_Style.sh");

        // 清除风格的新用户列表
        RemoveStyleList();
    }

    /**
     * 每天凌晨四点执行，风格训练算法
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void RunFurniture() throws IOException {
        System.out.println("开始执行家具训练算法 "+new Date().getTime());

        // 编写Excel
        outXlxsUltraGCNService.writeStyleXlxs();

        // 运行shello脚本
        excuteCmd("UltraGCN_Furniture.sh");

        // 清除家具的新用户列表
        RemoveFurnitureList();
    }

    /**
     * 获取算法结果的用户喜欢的风格
     * @param id
     * @return
     */
    public static List<Integer> GetStyle(Integer id){
        List<Integer> res = new ArrayList<>();
        // 读取解析Json文件
        String styleJson = readJson("rec_result_style","UltraGCN/");
        JSONObject style = JSON.parseObject(styleJson);
        String keyname = "user" + id.toString();


        res = (List<Integer>) style.get(keyname);
        return res;
    }

    /**
     * 获取算法结果的用户喜欢的家具
     * @param id
     * @return
     */
    public static List<Integer> GetFurniture(Integer id){
        List<Integer> res = new ArrayList<>();
        // 读取解析Json文件
        String styleJson = readJson("rec_result_fur","UltraGCN/");
        JSONObject style = JSON.parseObject(styleJson);
        String keyname = "user" + id.toString();


        res = (List<Integer>) style.get(keyname);
        return res;
    }

    /**
     * 新用户注册添加 待处理 列表
     * @param id
     */
    public void AddUser(int id){
        Map<String,Object> temp = new HashMap<>();
        // 判断是否存在
        if(redisUtil.hasKey("WaitTrainNewUser")){
            temp = (Map<String, Object>) redisUtil.get("WaitTrainNewUser");
        }else{
            List<Integer> Newstyle = new ArrayList<>();
            List<Integer> Newfurniture = new ArrayList<>();
            temp.put("style",Newstyle);
            temp.put("furniture",Newfurniture);
        }
        // 添加key
        List<Integer> style = (List<Integer>) temp.get("style");
        List<Integer> furniture = (List<Integer>) temp.get("furniture");
        style.add(id);
        furniture.add(id);
        temp.put("style",style);
        temp.put("furniture",furniture);

        redisUtil.set("WaitTrainNewUser",temp);
    }

    /**
     * 清除 风格 新用户待处理列表
     */
    public void RemoveStyleList(){
        if(redisUtil.hasKey("WaitTrainNewUser")){
            Map<String,Object> temp = (Map<String, Object>) redisUtil.get("WaitTrainNewUser");
            List<Integer> newlist = new ArrayList<>();
            temp.put("style",newlist);
            redisUtil.set("WaitTrainNewUser",temp);
        }
    }

    /**
     * 清除 家具 新用户待处理列表
     */
    public void RemoveFurnitureList(){
        if(redisUtil.hasKey("WaitTrainNewUser")){
            Map<String,Object> temp = (Map<String, Object>) redisUtil.get("WaitTrainNewUser");
            List<Integer> newlist = new ArrayList<>();
            temp.put("furniture",newlist);
            redisUtil.set("WaitTrainNewUser",temp);
        }
    }

    /**
     * 用户是否在风格的 新用户待处理列表
     * @param id
     * @return
     */
    public Boolean IsUserInStyleWaitTrainNewUser(Integer id){
        if(redisUtil.hasKey("WaitTrainNewUser")){
            Map<String,Object> temp = (Map<String, Object>) redisUtil.get("WaitTrainNewUser");
            List<Integer> style = (List<Integer>) temp.get("style");
            for(int i=0;i<style.size();i++){
                if(style.get(i)==id){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }

    /**
     * 用户是否在 家具的 新用户待处理列表
     * @param id
     * @return
     */
    public Boolean IsUserInFurnitureWaitTrainNewUser(Integer id){

        if(redisUtil.hasKey("WaitTrainNewUser")){
            Map<String,Object> temp = (Map<String, Object>) redisUtil.get("WaitTrainNewUser");
            List<Integer> furniture = (List<Integer>) temp.get("furniture");
            for(int i=0;i<furniture.size();i++){

                if(furniture.get(i).equals(id)){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }

    public String GetUserStyle(int id){
        if(IsUserInStyleWaitTrainNewUser(id)){
            // 获取注册风格
            String sql = "select styleId from style where userId = " +id;
            List<Map<String, Object>> StyleList = jdbcTemplate.queryForList(sql);

            if(StyleList.size()!=0)
                return getStyle(Integer.parseInt( StyleList.get(0).get("style").toString()) );
            else
                return "现代";
        }else{
            // 获取推荐风格
            List<Integer> stylelist = GetStyle(id);
            if(stylelist.size()!=0)
                return getStyle(stylelist.get(0));
            else
                return "现代";
        }
    }
}
