package com.rswy.getopenid.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rswy.getopenid.domain.AppProps;
import com.rswy.getopenid.domain.GZHProps;
import com.rswy.getopenid.domain.ZFBProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class OauthApi {
    @Autowired
    private AppProps appProps;
    @Autowired
    private ZFBProps zfbProps;
    @Autowired
    private GZHProps gzhProps;

    /**
     * 通过判断浏览器表示跳转到只有openid或userid的函数
     * @param req
     * @param resp
     * @param key
     * @param value
     * @throws IOException
     */
    @RequestMapping({"/auoth/{key}/{value}"})
    public void testBrowser(HttpServletRequest req, HttpServletResponse resp, @PathVariable("key") String key, @PathVariable("value")String value) throws IOException {
        String url = "";
        String userAgent = req.getHeader("user-agent");
        if (userAgent != null && userAgent.contains("AlipayClient")) {
            url= appProps.getUrl() + appProps.getApp()+"zfbwy/"+key+"/"+value;
            resp.sendRedirect(url);
        }else if (userAgent != null && userAgent.contains("MicroMessenger")) {
            url = appProps.getUrl() + appProps.getApp()+"wxgzh/"+key+"/"+value;
            resp.sendRedirect(url);
        }else{
            String error="请使用微信或支付宝打开本应用";
            resp.sendRedirect(appProps.getErrBrowserUrl());
        }
    }


    //public JSONObject getInfoUrl(HttpServletRequest request, @PathVariable("url") String url){
    //    String returnUrl = "";
    //    String userAgent = request.getHeader("user-agent");
    //    if (userAgent != null && userAgent.contains("AlipayClient")){
    //        returnUrl = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id="+zfbProps.getAppId()+"&scope=auth_user&redirect_uri=ENCODED_URL";
    //
    //        //https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=APPID&scope=SCOPE&redirect_uri=ENCODED_URL
    //    }else if (userAgent != null && userAgent.contains("MicroMessenger")) {
    //        returnUrl = appProps.getUrl() + appProps.getApp()+"wxgzh/"+key+"/"+value;
    //        resp.sendRedirect(url);
    //    }else{
    //        String error="请使用微信或支付宝打开本应用";
    //        resp.sendRedirect(appProps.getErrBrowserUrl());
    //    }
    //    return null;
    //}
}
