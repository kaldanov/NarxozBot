package com.example.spgtu.service;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationService {


    public static boolean login(String login, String password){
//        IvanovII
//        123456
        HttpPost post = new HttpPost("https://gup.kz/user");

        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("name", login));
        urlParameters.add(new BasicNameValuePair("pass", password));
        urlParameters.add(new BasicNameValuePair("form_build_id", "form-dPcxLSuUBe83kYXmEAjpZ96wT4hx5ZIZP40h84Di8es"));
        urlParameters.add(new BasicNameValuePair("form_id", "user_login"));

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(post)) {
                return response.getStatusLine().getStatusCode() == 302;
//                String asd =  response.getAllHeaders()[4].getValue();
//                return asd != null && asd.contains("https://gup.kz/node/");
//                Document document = Jsoup.parse(EntityUtils.toString(response.getEntity()));
//                //todo change logic
//
//                Element title =  document.getElementById("page-title");
//                return title != null && title.text() != null && title.text().equals("Личный кабинет студента");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

}