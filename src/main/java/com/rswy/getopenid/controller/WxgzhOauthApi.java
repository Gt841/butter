package com.rswy.getopenid.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.rswy.getopenid.domain.AppProps;
import com.rswy.getopenid.domain.GZHProps;
import com.rswy.getopenid.domain.entry.RulEntry;
import com.rswy.getopenid.service.RulService;
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

    @Autowired
    private AppProps appProps;

    @Autowired
    private RulService rulService;

    /**
     * 将微信公众号发送的请求转发到微信官方
     * 获取code
     *
     * @param response 请求内容
     * @param key
     * @param value
     * @throws IOException
     */
    @RequestMapping({"/{key}/{value}"})
    public void getOauthCode(HttpServletResponse response,@PathVariable("key") String key,@PathVariable("value")String value) throws IOException{
        //callback地址
        //String callback = GZHProps.getServUrl()+
        String callback = appProps.getUrl()+appProps.getApp()+GZHProps.getServUrl()+
                "/?key="+key
                +"&value=" + value
                +"&appId="+ GZHProps.getAppId()
                +"&appSecret="+ GZHProps.getAppKey()
                ;

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ GZHProps.getAppId()
                +"&redirect_uri="+URLEncoder.encode(callback,"UTF-8")
                +"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        response.sendRedirect(url);
    }

    /**
     * 获取code的回调
     * 1. 向微信发送获取openid的请求
     * 2. 跳转到指定的业务程序
     * @param request,response,key,value,appId,appSecret 请求参数
     * @throws IOException
     */
    @RequestMapping({"/callBack"})
    @ResponseBody
    //, String callbackUrl
    public void callBack(HttpServletRequest request, HttpServletResponse response, String key,String value, String appId, String appSecret) throws IOException {
        String url = "";
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
            String content = HttpUtil.get(wxUrl.toString());
            JSONObject json_content = JSONObject.parseObject(content);
            String openid = json_content.getString("openid");
            //创建返回前端程序地址
            //callbackUrl;
            //判断系统内是否存有该应用参数
            //if (!appProps.getReMap().containsKey(key)){
            //    response.sendRedirect(appProps.getUrl() + appProps.getApp()+"err2/"+"?openId=" + openid +
            //            "&key="+key+"&value="+value);
            //    return ;
            //}
            RulEntry rul = rulService.findRul(key);
            if (rul == null){
                url +=appProps.getErrKeyUrl();
            }else{
                url += rul.getReRul();
            }
            url += "?openId=" + openid +
                    "&key="+key+"&value="+value;
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.sendRedirect(url);
    }

    //带头像昵称的获取方式
    //1. 获取访问地址
    //2.在原有界面重载,进入应用
    //3.在java后台获取数据后进入业务逻辑
}
