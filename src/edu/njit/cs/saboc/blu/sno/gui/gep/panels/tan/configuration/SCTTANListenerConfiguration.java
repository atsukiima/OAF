
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.NavigateToContainerReportListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.NavigateToGroupListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.ParentGroupSelectedListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.BLUGenericTANListenerConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.entry.ContainerReport;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTBand;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayConceptBrowserListener;

/**
 *
 * @author Chris O
 */
public class SCTTANListenerConfiguration implements BLUGenericTANListenerConfiguration<SCTTribalAbstractionNetwork, SCTBand, SCTCluster, Concept>{

    private final SCTTANConfiguration config;
    
    public SCTTANListenerConfiguration(SCTTANConfiguration config) {
        this.config = config;
    }
    
    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        return new DisplayConceptBrowserListener(config.getUIConfiguration().getDisplayFrameListener(), 
                config.getDataConfiguration().getTribalAbstractionNetwork().getDataSource());
    }

    @Override
    public EntitySelectionListener<SCTCluster> getChildGroupListener() {
        return new NavigateToGroupListener<>(config.getUIConfiguration().getGEP());
    }
    
    @Override
    public EntitySelectionListener<GenericParentGroupInfo<Concept, SCTCluster>> getParentGroupListener() {
        return new ParentGroupSelectedListener<>(config.getUIConfiguration().getGEP());
    }
    
    
    @Override
    public EntitySelectionListener<ContainerReport<SCTBand, SCTCluster, Concept>> getContainerReportSelectedListener() {
        return new NavigateToContainerReportListener<>(config.getUIConfiguration().getGEP());
    }
    
    @Override
    public EntitySelectionListener<Concept> getClusterPatriarchSelectedListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntitySelectionListener<Concept> getBandPatriarchSelectedListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
