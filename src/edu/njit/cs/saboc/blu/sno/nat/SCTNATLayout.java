package edu.njit.cs.saboc.blu.sno.nat;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.nat.panels.AttributeRelationshipPanel;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.gui.layout.BasicNATAdjustableLayout;

/**
 *
 * @author Chris O
 */
public class SCTNATLayout extends BasicNATAdjustableLayout<SCTConcept> {
    
    private AttributeRelationshipPanel attributeRelList;
    
    public SCTNATLayout(SCTConceptBrowserDataSource dataSource) {
        super(dataSource);
    }
    
    @Override
    public SCTConceptBrowserDataSource getDataSource() {
        return (SCTConceptBrowserDataSource)super.getDataSource();
    }

    @Override
    public void createLayout(NATBrowserPanel mainPanel) {
        super.createLayout(mainPanel);
        
        attributeRelList = new AttributeRelationshipPanel(mainPanel, getDataSource());
        
        super.setRightPanelContents(attributeRelList);
        
        if(getDataSource().getRelease().supportsStatedRelationships()) {
            SCTStatedParentsList statedParentsList = new SCTStatedParentsList(mainPanel, getDataSource());
            SCTStatedChildrenList statedChildrenList = new SCTStatedChildrenList(mainPanel, getDataSource());
            
            super.getAncestorPanel().addResultListPanel(statedParentsList, "Stated Parents");
            super.getDescendantsPanel().addResultListPanel(statedChildrenList, "Stated Children");
        }
    }
}
