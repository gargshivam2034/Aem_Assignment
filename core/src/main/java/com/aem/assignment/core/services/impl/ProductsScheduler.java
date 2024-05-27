package com.aem.assignment.core.services.impl;


import com.aem.assignment.core.services.ConfigScheduler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Runnable.class)
@Designate(ocd = ConfigScheduler.class)
public class ProductsScheduler implements Runnable{


    Logger logger= LoggerFactory.getLogger(ProductsScheduler.class);
    @Override
    public  void run()
    {
        logger.info("scheduled successfully");
    }
    ObjectMapper objectMapper=new ObjectMapper();

}
