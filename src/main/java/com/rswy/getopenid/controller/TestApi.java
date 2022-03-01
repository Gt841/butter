package com.rswy.getopenid.controller;

import com.alibaba.fastjson.JSONObject;
import com.rswy.getopenid.domain.AppProps;
import com.rswy.getopenid.domain.ZFBProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestApi {
    @Autowired
    private AppProps appProps;

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

    @RequestMapping("/getBrowser")
    public void getBrowser(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String userAgent = req.getHeader("user-agent");
        if (userAgent != null && userAgent.contains("AlipayClient")) {
            resp.sendRedirect(appProps.getUrl() + appProps.getApp()+"zfbwy/zfb/test");
        }else if (userAgent != null && userAgent.contains("MicroMessenger")) {
            resp.sendRedirect(appProps.getUrl() + appProps.getApp()+"wxgzh/wx/test");
        }else{
            resp.sendRedirect(appProps.getUrl() + appProps.getApp()+"test?error=error");
        }
    }
    //判断使用浏览器,引流至不同认证方式
    @RequestMapping({"/testBrowser/{key1}/{value1}"})
    public void testBrowser(HttpServletRequest req, HttpServletResponse resp,@PathVariable("key1") String key1,@PathVariable("value1")String value1) throws IOException {
        if (!appProps.getReMap().containsKey(key1)){
            resp.sendRedirect(appProps.getUrl() + appProps.getApp()+"test?error=error");
            return ;
        }
        String userAgent = req.getHeader("user-agent");
        if (userAgent != null && userAgent.contains("AlipayClient")) {
            resp.sendRedirect(appProps.getUrl() + appProps.getApp()+"zfbwy/"+key1+"/"+value1);
        }else if (userAgent != null && userAgent.contains("MicroMessenger")) {
            resp.sendRedirect(appProps.getUrl() + appProps.getApp()+"wxgzh/"+key1+"/"+value1);
        }else{
            resp.sendRedirect(appProps.getUrl() + appProps.getApp()+"test?error=error");
        }
    }

}
