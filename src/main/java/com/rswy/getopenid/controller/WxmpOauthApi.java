package com.rswy.getopenid.controller;

import com.alibaba.fastjson.JSONObject;
import com.rswy.getopenid.domain.WXMPProps;
import com.rswy.getopenid.utils.AesCbcUtil;
import com.rswy.getopenid.utils.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

//微信小程序认证方式
@RestController
@RequestMapping("/wxmp")
public class WxmpOauthApi {

    @Autowired
    private WXMPProps wxmpProps;

    /**
     * 通过 encryptedData iv code 获取用户openid
     * 1.拼接参数发送到微信官方
     * 2.将官方返回数据,通过iv和encryptedData使用aes解密获取openid
     * 3.返回openid等数据
     * @param encryptedData
     * @param iv
     * @param code
     * @return
     */
    @RequestMapping("/decodeUserInfo")
    public Map  decodeUserInfo(String encryptedData,String iv , String code){
        Map map = new HashMap();
        //登录凭证不能为空
        if (code == null || code.length() == 0) {
            map.put("status", 0);
            map.put("msg", "code 不能为空");
            return map;
        }
        String grant_type = "authorization_code";
        //构建参数
        String params = "appid=" + wxmpProps.getAppId() +
                "&secret=" + wxmpProps.getAppKey() +
                "&js_code=" + code +
                "&grant_type=" + grant_type;
        //发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        //解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(sr);
        //获取会话密钥（session_key）
        String session_key = json.get("session_key").toString();
        //用户的唯一标识（openid）
        String openid = (String) json.get("openid");
        //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
        try {
            String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                map.put("status", 1);
                map.put("msg", "解密成功");

                JSONObject userInfoJSON = JSONObject.parseObject(result);
                Map userInfo = new HashMap();
                userInfo.put("openId", userInfoJSON.get("openId"));
                userInfo.put("nickName", userInfoJSON.get("nickName"));
                userInfo.put("gender", userInfoJSON.get("gender"));
                userInfo.put("city", userInfoJSON.get("city"));
                userInfo.put("province", userInfoJSON.get("province"));
                userInfo.put("country", userInfoJSON.get("country"));
                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
                userInfo.put("unionId", userInfoJSON.get("unionId"));
                map.put("userInfo", userInfo);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("status", 0);
        map.put("msg", "解密失败");
        return map;
    }
}
