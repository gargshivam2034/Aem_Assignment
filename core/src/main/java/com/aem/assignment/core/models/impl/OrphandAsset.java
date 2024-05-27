package com.aem.assignment.core.models.impl;


import com.day.cq.dam.api.Asset;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OrphandAsset
{

    @SlingObject
    SlingHttpServletRequest slingHttpServletRequest;
    private String Path;
    public void getContentList()
    {
       Asset asset= slingHttpServletRequest.getResourceResolver().adaptTo(Asset.class);
        assert asset != null;
        asset.getSubAssets();
    }


}
