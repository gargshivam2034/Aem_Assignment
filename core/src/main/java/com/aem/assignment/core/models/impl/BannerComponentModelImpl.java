package com.aem.assignment.core.models.impl;

import com.aem.assignment.core.models.BannerComponentModel;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;


@Model(adaptables = {SlingHttpServletRequest.class},
        adapters = {BannerComponentModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BannerComponentModelImpl implements BannerComponentModel {

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    String contentType;

    @ValueMapValue
    @Default(values = "")
    private String image;

    @ValueMapValue
    @Default(values = "")
    private String video;
}
