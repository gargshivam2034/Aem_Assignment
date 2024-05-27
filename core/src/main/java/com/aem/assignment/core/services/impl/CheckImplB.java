package com.aem.assignment.core.services.impl;


import com.aem.assignment.core.services.Check;
import org.osgi.service.component.annotations.Component;

@Component(service = Check.class)

public class CheckImplB implements Check{


    @Override
    public String getBookDetailsString()
    {
        return "ServiceImpl B ";
    }
}

