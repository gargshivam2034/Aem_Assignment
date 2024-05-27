package com.aem.assignment.core.models.impl;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CookieModel {


    @Self
    SlingHttpServletRequest slingHttpServletRequest;
    private List<String> list=new ArrayList<>();


    List<String> getCookie()
    {
        Cookie[] cookies=slingHttpServletRequest.getCookies();
        for(Cookie cookie:cookies) {
            list.add(cookie.getName());
        }
        return list;
    }
}
