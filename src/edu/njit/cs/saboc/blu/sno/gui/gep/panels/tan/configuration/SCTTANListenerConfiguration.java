
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import edu.njit.cs.saboc.blu.core.abn.ParentNodeDetails;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionAdapter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.TANListenerConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.entry.ContainerReport;
import edu.njit.cs.saboc.blu.core.ontology.Concept;

/**
 *
 * @author Chris O
 */
public class SCTTANListenerConfiguration implements TANListenerConfiguration {

    private final SCTTANConfiguration config;
    
    public SCTTANListenerConfiguration(SCTTANConfiguration config) {
        this.config = config;
    }
    

    @Override
    public EntitySelectionListener<Concept> getClusterPatriarchSelectedListener() {
        return new EntitySelectionAdapter<>();
    }

    @Override
    public EntitySelectionListener<Concept> getBandPatriarchSelectedListener() {
        return new EntitySelectionAdapter<>();
    }
    
    

    @Override
    public EntitySelectionListener<ContainerReport> getContainerReportSelectedListener() {
        return new EntitySelectionAdapter<>();
    }

    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        return new EntitySelectionAdapter<>();
    }
    
    

    @Override
    public EntitySelectionListener<Cluster> getChildGroupListener() {
        return new EntitySelectionAdapter<>();
    }

    @Override
    public EntitySelectionListener<ParentNodeDetails<Cluster>> getParentGroupListener() {
        return new EntitySelectionAdapter<>();
    }
}
