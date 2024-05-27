package com.aem.assignment.core.models.impl;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TestingModel {
    List<Pojo> pojoList;

    @PostConstruct
    public void init() throws IOException, JSONException {
        String apiUrl = "https://fakestoreapi.com/products/1";
        URL url = new URL(apiUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        InputStream inputStream = connection.getInputStream();
        String jsonResponse = IOUtils.toString(inputStream);
         JSONObject data = new JSONObject(jsonResponse);
         Pojo pojo=new Pojo();
         try
         {
             pojo.setId(data.getInt("id"));
            // pojo.setCategory(jsonObject.get("category").toString());
             pojo.setTitle(data.getString("title"));
             pojo.setImage(data.getString("image"));
             pojo.setPrice(data.getInt("price"));
             pojo.setDescription(data.getString("description"));
             pojoList.add(pojo);
         } catch (NumberFormatException e) {
             throw new RuntimeException(e);
         } catch (JSONException e) {
             throw new RuntimeException(e);
         }
    }

    public List<Pojo> getPojoList() {
        return pojoList;
    }
}
