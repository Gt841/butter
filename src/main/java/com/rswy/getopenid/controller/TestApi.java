package com.rswy.getopenid.controller;

import com.alibaba.fastjson.JSONObject;
import com.rswy.getopenid.domain.AppProps;
import com.rswy.getopenid.domain.ZFBProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

@RestController
public class TestApi {
    @Autowired
    private AppProps appProps;



    @GetMapping("/errBrowser")
    public JSONObject errBrowser(@RequestParam(required = false) Map<String,Object> param , @RequestBody(required = false) Map<String,Object> data){
        param.put("error","请使用微信或支付宝打开应用");
        JSONObject json = new JSONObject();
        json.put("method","get");
        json.put("param",param);
        json.put("data",data);
        return json;
    }
    @GetMapping("/errKey")
    public JSONObject errKey(@RequestParam(required = false) Map<String,Object> param , @RequestBody(required = false) Map<String,Object> data){
        param.put("error","系统内没有该应用信息,请检查访问参数");
        JSONObject json = new JSONObject();
        json.put("method","get");
        json.put("param",param);
        json.put("data",data);
        return json;
    }

    @RequestMapping(value="/test")
    @ResponseBody
    public JSONObject testRequest(HttpServletRequest request,@RequestParam(required = false) Map<String,Object> param , @RequestBody(required = false) Map<String,Object> data){
        JSONObject json = new JSONObject();
        json.put("method",request.getMethod());
        json.put("param",param);
        json.put("data",data);
        //request
        json.put("url",request.getRequestURL());
        return json;
    }

    //@GetMapping("/test")
    //public JSONObject testGet(@RequestParam(required = false) Map<String,Object> param , @RequestBody(required = false) Map<String,Object> data){
    //    JSONObject json = new JSONObject();
    //    json.put("method","get");
    //    json.put("param",param);
    //    json.put("data",data);
    //    return json;
    //}
    //
    //@PostMapping("/test")
    //public JSONObject testPost(@RequestParam(required = false) Map<String,Object> param , @RequestBody(required = false) Map<String,Object> data){
    //    JSONObject json = new JSONObject();
    //    json.put("method","post");
    //    json.put("param",param);
    //    json.put("data",data);
    //    return json;
    //}

    @RequestMapping("/getBrowser")
    public void getBrowser(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String userAgent = req.getHeader("user-agent");
        if (userAgent != null && userAgent.contains("AlipayClient")) {
            resp.sendRedirect(appProps.getUrl() + appProps.getApp()+"zfbwy/zfb/test");
        }else if (userAgent != null && userAgent.contains("MicroMessenger")) {
            resp.sendRedirect(appProps.getUrl() + appProps.getApp()+"wxgzh/wx/test");
        }else{
            resp.sendRedirect(appProps.getUrl() + appProps.getApp()+"err1");
        }
    }


    //重定向到指定应用
    @RequestMapping("/redirect")
    @ResponseBody
    public void testBrowser(HttpServletRequest req, HttpServletResponse resp,String redirect) throws IOException {
        String param = URLDecoder.decode(redirect, "utf-8");
        resp.sendRedirect(param);
    }

}
