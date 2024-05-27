package com.aem.assignment.core.services.OSGI;

import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;

import java.util.ArrayList;
import java.util.List;

@Component(service = OSGIFactoryConfigImpl.class)
@Designate(ocd = OSGIFactoryConfig.class, factory = true)
public class OSGIFactoryConfigImpl {

    private int serviceCount;
    private String serviceName;
    private List<OSGIFactoryConfig> configList = new ArrayList<>();

    @Activate
    @Modified
    protected void activate(OSGIFactoryConfig osgiFactoryConfig) {
//        serviceCount = Integer.parseInt(context.getProperties().get("ServiceCount").toString());
//        serviceName = context.getProperties().get("ServiceName").toString();
        serviceCount = osgiFactoryConfig.ServiceCount();
        serviceName = osgiFactoryConfig.ServiceName();
    }

    public int getServiceCount() {
        return serviceCount;
    }

    public String getServiceName() {
        return serviceName;
    }

    @Reference(service = OSGIFactoryConfig.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindOSGIFactoryConfig(OSGIFactoryConfig osgiFactoryConfig) {
        configList.add(osgiFactoryConfig);
    }

    public void unbindOSGIFactoryConfig(OSGIFactoryConfig osgiFactoryConfig) {
        configList.remove(osgiFactoryConfig);
    }

    public List<OSGIFactoryConfig> getConfigList() {
        return configList;
    }
}
