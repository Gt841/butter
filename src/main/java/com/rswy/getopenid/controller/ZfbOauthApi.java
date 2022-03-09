package com.rswy.getopenid.controller;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.rswy.getopenid.domain.AppProps;
import com.rswy.getopenid.domain.ZFBProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

//支付宝网页认证
@Controller
@RequestMapping({"/zfbwy"})
public class ZfbOauthApi {
    @Autowired
    private ZFBProps zfbProps;
    @Autowired
    private AppProps appProps;

    /**
     * 初始拼接支付宝访问地址
     * @param response
     * @param key1
     * @param value1
     * @throws IOException
     */
    @RequestMapping({"/{key1}/{value1}"})
    public void getOauthCode(HttpServletResponse response, @PathVariable("key1") String key1, @PathVariable("value1")String value1) throws IOException {
        System.out.println(key1);
        //callback地址
        String callback = appProps.getUrl()+appProps.getApp()+zfbProps.getServUrl()+"/"+key1+"/"+value1;

        String url = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id="+ zfbProps.getAppId()
                +"&scope=auth_base&"
                +"&redirect_uri="+URLEncoder.encode(callback,"UTF-8");
        System.out.println(url);
        response.sendRedirect(url);
    }

    @RequestMapping({"/useCode/{key}/{value}"})
    @ResponseBody
    public void useCode(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) Map<String,Object> param,@PathVariable("key") String key,@PathVariable("value")String value) throws IOException{
        System.out.println(param);

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", zfbProps.getAppId(), zfbProps.getPriKey(), "json", "utf-8", zfbProps.getPubKey(), zfbProps.getSignType());
        AlipaySystemOauthTokenRequest request1 = new AlipaySystemOauthTokenRequest();
        request1.setCode(String.valueOf(param.get("auth_code")));
        request1.setGrantType("authorization_code");
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request1);
            System.out.println(oauthTokenResponse.getUserId());
            System.out.println(oauthTokenResponse.getAccessToken());
            //判断系统内是否存在该应用参数
            if (!appProps.getReMap().containsKey(key)){
                response.sendRedirect(appProps.getUrl() + appProps.getApp()+"err2/"+"?openId=" + oauthTokenResponse.getUserId() +
                        "&key="+key+"&value="+value);
                return ;
            }
            response.sendRedirect(appProps.getReMap().get(key)+"/#/?openId="+oauthTokenResponse.getUserId()+
                    "&key="+key+"&value="+value);
        } catch (AlipayApiException e) {
            //处理异常
            e.printStackTrace();
            response.sendRedirect(zfbProps.getReUrl());
        }

    }
}
