package com.aem.assignment.core.services;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "SampleScheduler")
public  @interface  ConfigScheduler {

    @AttributeDefinition(name = "Crown job expression",
            type = AttributeType.STRING
    )
    String scheduler_expression() default "0 0 12 1 1 ? *";

    @AttributeDefinition(name = "concurrent task")
    boolean scheduler_concurrent() default false;


}
