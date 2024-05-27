package com.aem.assignment.core.services.OSGI;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name="OSGIExample",description = "example for checking something ")
public @interface OSGIFactoryConfig {

    @AttributeDefinition(
            name = "service",
            type = AttributeType.STRING,
            defaultValue = "Service name"
    )
    String ServiceName();
    @AttributeDefinition(
            name="service count",
            type = AttributeType.INTEGER,
            defaultValue = "0"
    )
    int ServiceCount();

}
