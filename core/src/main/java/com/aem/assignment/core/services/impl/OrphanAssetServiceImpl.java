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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Implementation of the OrphanAssetService to handle operations related to orphan assets.
 */
@Component(service = OrphanAssetService.class)
public class OrphanAssetServiceImpl implements OrphanAssetService {

    @Reference
    private QueryBuilder queryBuilder;

    private final List<Asset> orphanAssetsToDelete = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(OrphanAssetService.class);

    /**
     * Retrieves a list of KeyValue objects for a dropdown based on the given path.
     *
     * @param resolver The resource resolver.
     * @param path     The path to search for resources.
     * @return A list of KeyValue objects.
     */
    @Override
    public List<KeyValue> getDropDown(ResourceResolver resolver, String path) {
        Resource resource = resolver.getResource(path);
        if (resource == null) return Collections.emptyList();

        return StreamSupport.stream(resource.getChildren().spliterator(), false)
                .filter(childResource -> "cq:Page".equals(childResource.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE)))
                .map(childResource -> new KeyValue(childResource.getName(), childResource.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Clears orphan assets from the repository.
     *
     * @param resolver  The resource resolver.
     * @param finalPath The path to be used for reference, if needed.
     */
    @Override
    public void clearOrphanAsset(ResourceResolver resolver, String finalPath) {
        Session session = resolver.adaptTo(Session.class);
        List<Asset> assetsToDelete = new ArrayList<>(orphanAssetsToDelete);
        try {
            for (Asset asset : assetsToDelete) {
                session.removeItem(asset.getPath());
                orphanAssetsToDelete.remove(asset);
            }
            session.save();
        } catch (RepositoryException e) {
            LOGGER.error("Failed to clear orphan assets", e);
            throw new RuntimeException("Failed to clear orphan assets", e);
        }
    }

    /**
     * Retrieves all orphan assets within the given path.
     *
     * @param resolver  The resource resolver.
     * @param finalPath The path to search for orphan assets.
     * @return A list of OrphanAsset objects.
     */
    @Override
    public List<OrphanAsset> getAllOrphanAssets(ResourceResolver resolver, String finalPath) {
        List<OrphanAsset> orphanAssets = new ArrayList<>();
        Session session = resolver.adaptTo(Session.class);
        Query query = queryBuilder.createQuery(PredicateGroup.create(getQuery(finalPath)), session);
        SearchResult searchResult = query.getResult();

        searchResult.getResources().forEachRemaining(resource -> {
            Asset asset = resource.adaptTo(Asset.class);
            if (asset != null && isOrphanAsset(resolver, asset)) {
                OrphanAsset orphanAsset = new OrphanAsset(asset.getName(),asset.getPath());
                orphanAssets.add(orphanAsset);
                orphanAssetsToDelete.add(asset);
            }
        });

        return orphanAssets;
    }

    /**
     * Checks if the given asset is an orphan asset.
     *
     * @param resolver The resource resolver.
     * @param asset    The asset to check.
     * @return True if the asset is an orphan, otherwise false.
     */
    private boolean isOrphanAsset(ResourceResolver resolver, Asset asset) {
        ReferenceSearch referenceSearch = new ReferenceSearch();
        referenceSearch.setExact(true);
        referenceSearch.setHollow(true);
        referenceSearch.setMaxReferencesPerPage(-1);

        Collection<ReferenceSearch.Info> resultSet = referenceSearch.search(resolver, asset.getPath()).values();
        return resultSet.isEmpty();
    }

    /**
     * Creates a query map for searching DAM assets under the given path.
     *
     * @param path The path to search for assets.
     * @return A map representing the query.
     */
    private Map<String, String> getQuery(String path) {
        Map<String, String> map = new HashMap<>();
        map.put("path", path);
        map.put("property", JcrConstants.JCR_PRIMARYTYPE);
        map.put("property.value", "dam:Asset");
        return map;
    }
}
