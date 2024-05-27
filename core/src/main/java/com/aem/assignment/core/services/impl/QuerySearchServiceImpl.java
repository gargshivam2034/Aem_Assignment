package com.aem.assignment.core.services.impl;

import com.aem.assignment.core.services.QuerySearchService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = QuerySearchService.class)
public class QuerySearchServiceImpl implements QuerySearchService{

    @Reference
    QueryBuilder queryBuilder;


    @Reference
    ResourceResolverFactory resourceResolverFactory;
    private final  static Logger logger= LoggerFactory.getLogger(QuerySearchServiceImpl.class);
    @Activate
    public  void activate()
    {

    }
    Map<String,String> createTextSearch(String searchText)
    {
        Map<String,String> queryMap=new HashMap<>();
        queryMap.put("type","cq:Page");
        queryMap.put("path","/content/we-retail");
        queryMap.put("fulltext",searchText);
        queryMap.put("p.limit","-1");
        return queryMap;
    }

    @Override
    public JSONObject getSearchResult(String searchText, ResourceResolver resourceResolver) throws LoginException, NoSuchMethodException {
        JSONObject jsonObjectG=new JSONObject();
        try {
            Map<String, Object> authMap = new HashMap<>();
            authMap.put(ResourceResolverFactory.SUBSERVICE, "shivam");
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(authMap);
//            ResourceResolver resourceResolver = resourceResolverFactory.getResourceResolver(authMap);

            Session session = resourceResolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(createTextSearch(searchText)), session);
            SearchResult searchResult = query.getResult();
            List<Hit> hitList = searchResult.getHits();
            JSONArray jsonArray = new JSONArray();
            for (Hit hit : hitList) {
                Page page = hit.getResource().adaptTo(Page.class);
                JSONObject jsonObject = new JSONObject();
                assert page != null;
                jsonObject.put("title", page.getTitle());
                jsonObject.put("path", page.getPath());
                jsonArray.put(jsonObject);
            }
            jsonObjectG.put("results",jsonArray);
        }
        catch (Exception e)
        {
            logger.info("Error",e.getMessage());
        }



        return jsonObjectG;


    }



}
