package com.rswy.getopenid.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TestApi {

    @GetMapping("/test")
    public JSONObject testGet(@RequestParam(required = false) Map<String,Object> param , @RequestBody(required = false) Map<String,Object> data){
        JSONObject json = new JSONObject();
        json.put("method","get");
        json.put("param",param);
        json.put("data",data);
        return json;
    }

    @PostMapping("/test")
    public JSONObject testPost(@RequestParam(required = false) Map<String,Object> param , @RequestBody(required = false) Map<String,Object> data){
        JSONObject json = new JSONObject();
        json.put("method","post");
        json.put("param",param);
        json.put("data",data);
        return json;
    }
}
