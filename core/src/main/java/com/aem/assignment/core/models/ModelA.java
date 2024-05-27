package com.aem.assignment.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

public interface ModelA {

    @ScriptVariable
    Resource resource = null;




}
