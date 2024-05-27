package com.aem.assignment.core.services.impl;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.commons.ReferenceSearch;
import com.aem.assignment.core.entities.KeyValue;
import com.aem.assignment.core.entities.OrphanAsset;
import com.aem.assignment.core.services.OrphanAssetService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

@Component(service = OrphanAssetService.class)
public class OrphanAssetServiceImpl implements OrphanAssetService {

    @Reference
    QueryBuilder queryBuilder;

    private List<Asset> orphanAssetsToDelete = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(OrphanAssetService.class);



    @Override
    public List<KeyValue> getDropDown(ResourceResolver resolver, String path) {
        List<KeyValue> dropDownList = new ArrayList<>();
        Resource resource = resolver.getResource(path);
        if(resource == null) return null;

        Iterable<Resource> iterable = resource.getChildren();
        for(Resource childResource: iterable){
            String primaryType = childResource.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE).toString();
            if(primaryType.equals("cq:Page")){
                String projectName = childResource.getName();
                dropDownList.add(new KeyValue(projectName,projectName));
            }
        }

        return dropDownList;
    }

    @Override
    public void clearOrphanAsset(ResourceResolver resolver, String finalPath) {
        Session session = resolver.adaptTo(Session.class);
        List<Asset> assetsToDelete = new ArrayList<>(orphanAssetsToDelete);
        try {
            for (Asset asset : assetsToDelete) {
                String assetPath = asset.getPath();
                session.removeItem(assetPath);
                orphanAssetsToDelete.remove(asset);
            }
            session.save();
        } catch (RepositoryException e) {
            throw new RuntimeException("Failed to clear orphan assets", e);
        }
    }

    @Override
    public List<OrphanAsset> getAllOrphanAssets(ResourceResolver resolver, String finalPath) {

        List<OrphanAsset> orphanAssets = new ArrayList<>();

        Session session = resolver.adaptTo(Session.class);
        Query query = queryBuilder.createQuery(PredicateGroup.create(getQuery(finalPath)),session);
        SearchResult searchResult = query.getResult();
        Iterator<Resource> iterator = searchResult.getResources();



        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            Asset asset = resource.adaptTo(Asset.class);
            if(isOrphanAsset(resolver ,asset)){
                OrphanAsset orphanAsset = new OrphanAsset();
                orphanAsset.setName(asset.getName());
                orphanAsset.setPath(asset.getPath());
                orphanAssets.add(orphanAsset);
                orphanAssetsToDelete.add(asset);
            }
        }

        return orphanAssets;

    }

    private boolean isOrphanAsset(ResourceResolver resolver ,Asset asset){
        ReferenceSearch referenceSearch = new ReferenceSearch();
        referenceSearch.setExact(true);
        referenceSearch.setHollow(true);
        referenceSearch.setMaxReferencesPerPage(-1);

        Collection<ReferenceSearch.Info> resultSet = referenceSearch.search(resolver,asset.getPath()).values();
        if(resultSet.isEmpty()){
            return true;
        }
        return false;
    }


    private Map<String, String> getQuery(String path){
        Map<String,String> map = new HashMap<>();
        map.put("path",path);
        map.put("property",JcrConstants.JCR_PRIMARYTYPE);
        map.put("property.value","dam:Asset");
        return map;
    }
}
