package com.homepainter.filter;

import org.springframework.dao.DataAccessException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.ResponseBody;


import java.io.FileNotFoundException;
import java.io.IOException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Map<String, Object> RunTimeErrorHandler(RuntimeException e){
      e.printStackTrace();
      Map<String, Object> map = new HashMap<>();
      map.put("code", 4);
      map.put("msg", "程序运行时错误！");
      return map;
  }

    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public Map<String, Object> SQLErrorHandler(SQLException e){
        e.printStackTrace();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 2);
        map.put("msg", "SQL错误！");
        return map;
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public Map<String, Object> SQLErrorHandler(IOException e){
        e.printStackTrace();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 6);
        map.put("msg", "输入输出错误！");
        return map;
    }



//    @ExceptionHandler(SystemException.class)
//    @ResponseBody
//    public Map<String, Object> AllHandler(SystemException e){
//        e.printStackTrace();
//        Map<String, Object> map = new HashMap<>();
//        map.put("code", 9);
//        map.put("msg", "系统错误");
//        return map;
//    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public Map<String, Object> Handler(HttpMediaTypeNotSupportedException e){
        e.printStackTrace();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 7);
        map.put("msg", "接口数据格式错误");
        return map;
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public Map<String, Object> MybatisHandler(DataAccessException e){
        e.printStackTrace();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 5);
        map.put("msg", "数据库错误");
        return map;
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseBody
    public Map<String, Object> File(FileNotFoundException e){
        e.printStackTrace();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 3);
        map.put("msg", "该文件未找到");
        return map;
    }

//    @ExceptionHandler({SocketException.class, ConnectException.class, UnknownHostException.class})
//    @ResponseBody
//    public Map<String, Object> net(SocketException e1, ConnectException e2, UnknownHostException e3){
//        e1.printStackTrace(); e2.printStackTrace(); e3.printStackTrace();
//        Map<String, Object> map = new HashMap<>();
//        map.put("code", 1);
//        map.put("msg", "网络错误");
//        return map;
//    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Map<String, Object> net(HttpRequestMethodNotSupportedException e){
        e.printStackTrace();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 8);
        map.put("msg", "请求方式错误");
        return map;
    }

}
