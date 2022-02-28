package com.rswy.getopenid.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.rswy.getopenid.domain.GZHProps;
import com.rswy.getopenid.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.net.URLEncoder;

import java.io.IOException;

//微信公众号认证
@Controller
@RequestMapping({"/wxgzh"})
public class WxgzhOauthApi {

    @Autowired
    private GZHProps GZHProps;

    /**
     * 将微信公众号发送的请求转发到微信官方
     * 获取code
     *
     * @param response
     * @param key1
     * @param value1
     * @throws IOException
     */
    @RequestMapping({"/{key1}/{value1}"})
    public void getOauthCode(HttpServletResponse response,@PathVariable("key1") String key1,@PathVariable("value1")String value1) throws IOException{
        //callback地址
        String callback = GZHProps.getServUrl()+
                "/?key="+key1
                +"&value=" + value1
                +"&appId="+ GZHProps.getAppId()
                +"&appSecret="+ GZHProps.getAppKey()
                +"&callbackUrl="+ GZHProps.getReUrl();
        System.out.println(callback);

        System.out.println(URLEncoder.encode(callback, "UTF-8"));
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ GZHProps.getAppId()
                +"&redirect_uri="+URLEncoder.encode(callback,"UTF-8")
                +"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        response.sendRedirect(url);
    }

    /**
     * 获取code的回调
     * 向微信发送获取openid的请求
     * 显示结果
     * @param request
     * @param response
     * @param key
     * @param value
     * @param appId
     * @param appSecret
     * @param callbackUrl
     * @return
     * @throws IOException
     */
    @RequestMapping({"/callBack1"})
    @ResponseBody
    public Object callBack1(HttpServletRequest request, HttpServletResponse response, String key,String value, String appId, String appSecret, String callbackUrl) throws IOException {
        String url = "";
        String content = "";
        String openid = "";
        String code = request.getParameter("code");
        StringBuffer wxUrl = new StringBuffer();
        wxUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
                .append(appId)
                .append("&secret=")
                .append(appSecret)
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");
        try {
            content = HttpUtil.get(wxUrl.toString());
            JSONObject json_content = JSONObject.parseObject(content);
            openid = json_content.getString("openid");
            //创建返回前端程序地址
            url = callbackUrl +
                  "/#/?openId=" + openid +
                  "&key="+key+"&value="+value;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //response.sendRedirect(url);
        //测试输出
        JSONObject object = new JSONObject();
        object.put("openid",openid);
        object.put("key1",key);
        object.put("value",value);
        object.put("callbackUrl",url);
        System.out.println(object);
        return object;
    }


    /**
     * 获取code的回调
     * 1. 向微信发送获取openid的请求
     * 2. 跳转到指定的业务程序
     * @param request
     * @param response
     * @param key
     * @param value
     * @param appId
     * @param appSecret
     * @param callbackUrl
     * @return
     * @throws IOException
     */
    @RequestMapping({"/callBack"})
    @ResponseBody
    public void callBack(HttpServletRequest request, HttpServletResponse response, String key,String value, String appId, String appSecret, String callbackUrl) throws IOException {
        String url = "";
        String content = "";
        String openid = "";
        String code = request.getParameter("code");
        StringBuffer wxUrl = new StringBuffer();
        wxUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
                .append(appId)
                .append("&secret=")
                .append(appSecret)
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");
        try {
            content = HttpUtil.get(wxUrl.toString());
            JSONObject json_content = JSONObject.parseObject(content);
            openid = json_content.getString("openid");
            //创建返回前端程序地址
            url = callbackUrl +
                    "?openId=" + openid +
                    "&key="+key+"&value="+value;
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.sendRedirect(url);
    }

}
