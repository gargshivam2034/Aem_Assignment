package com.aem.assignment.core.services;

import com.aem.assignment.core.entities.AlertContentFragmentEntity;

import java.util.List;

public interface AlertService {

    /**
     * Retrieves alerts from content fragments stored in a specified path.
     * Each alert is represented as an AlertContentFragmentEntity containing
     * the alert message and content.
     *
     * @return List of AlertContentFragmentEntity representing the alerts.
     */
    public List<AlertContentFragmentEntity> getAlertsFromContentFragment();

}
