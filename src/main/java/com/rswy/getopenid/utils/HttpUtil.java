package com.rswy.getopenid.utils;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    public static String post(String uri, String data, String charset) throws IOException {
        HttpPost httpPost = new HttpPost(uri);
        StringEntity entity = new StringEntity(data, charset);
        httpPost.setEntity((HttpEntity)entity);
        return post(httpPost);
    }

    public static String post(String uri, String data) throws IOException {
        return post(uri, data, "utf-8");
    }

    public static String post(String uri, Map<String, String> params) throws IOException {
        HttpPost httpPost = new HttpPost(uri);
        List<BasicNameValuePair> postData = new ArrayList<>();
        for (Map.Entry<String, String> param : params.entrySet())
            postData.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData, "UTF-8");
        httpPost.setEntity((HttpEntity)entity);
        return post(httpPost);
    }

    public static String post(HttpPost httpPost) throws IOException {
        return doExecute((HttpRequestBase)httpPost);
    }

    public static String get(String uri) throws IOException {
        HttpGet httpGet = new HttpGet(uri);
        return get(httpGet);
    }

    public static String get(HttpGet httpGet) throws IOException {
        return doExecute((HttpRequestBase)httpGet);
    }

    public static String doExecute(HttpRequestBase request) throws IOException {
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).build();
        request.setConfig(requestConfig);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute((HttpUriRequest)request);
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                InputStream content = entity.getContent();
                result = streamToString(content);
            }
            return result;
        } catch (SocketTimeoutException e) {
            throw new RuntimeException(e);
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        }
    }

    public static String streamToString(InputStream content) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content, "UTF-8"));
        int count = -1;
        while ((count = reader.read()) != -1)
            stringBuffer.append((char)count);
        return stringBuffer.toString();
    }

    public static String httpPost(String gateway, String jsonParam) {
        String xmlText = "";
        CloseableHttpClient httpclient = HttpClients.custom().build();
        HttpPost httpPost = new HttpPost(gateway);
        httpPost.addHeader("charset", "UTF-8");
        StringEntity stentity = new StringEntity(jsonParam.toString(), "utf-8");
        stentity.setContentEncoding("UTF-8");
        stentity.setContentType("application/json");
        httpPost.setEntity((HttpEntity)stentity);
        try {
            CloseableHttpResponse response = httpclient.execute((HttpUriRequest)httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                String text;
                while ((text = bufferedReader.readLine()) != null)
                    xmlText = xmlText + text;
            }
            EntityUtils.consume(entity);
            response.close();
            httpclient.close();
            System.out.println(xmlText);
        } catch (Exception exception) {}
        return xmlText;
    }
}
