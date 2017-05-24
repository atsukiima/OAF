package edu.njit.cs.saboc.blu.sno.nat;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.nat.panels.AttributeRelationshipPanel;
import edu.njit.cs.saboc.nat.generic.DataSourceChangeListener;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.data.ConceptBrowserDataSource;
import edu.njit.cs.saboc.nat.generic.gui.layout.BasicNATAdjustableLayout;
import javax.swing.JRadioButton;

/**
 * SNOMED CT specific layout for the NAT. Displays SNOMED CT-specific panels.
 * 
 * @author Chris O
 */
public class SCTNATLayout extends BasicNATAdjustableLayout<SCTConcept> {
    
    private AttributeRelationshipPanel attributeRelList;
    
    private SCTStatedParentsList statedParentsList;
    
    private SCTStatedChildrenList statedChildrenList;
    
    private JRadioButton statedParentsBtn;
    
    private JRadioButton statedChildrenBtn;
    
    public SCTNATLayout() {
        
    }
    
    @Override
    public void createLayout(NATBrowserPanel mainPanel) {
        super.createLayout(mainPanel);

        attributeRelList = new AttributeRelationshipPanel(mainPanel);

        super.setRightPanelContents(attributeRelList);

        statedParentsList = new SCTStatedParentsList(mainPanel);
        statedChildrenList = new SCTStatedChildrenList(mainPanel);

        statedParentsBtn = super.getAncestorPanel().addResultListPanel(statedParentsList, "Stated Parents");
        statedChildrenBtn = super.getDescendantsPanel().addResultListPanel(statedChildrenList, "Stated Children");
        
        this.statedChildrenList.setActive(false);
        this.statedParentsList.setActive(false);
        
        this.statedParentsBtn.setEnabled(false);
        this.statedChildrenBtn.setEnabled(false);
        
        mainPanel.addDataSourceChangeListener(new DataSourceChangeListener<SCTConcept>() {
            @Override
            public void dataSourceLoaded(ConceptBrowserDataSource<SCTConcept> dataSource) {
                SCTConceptBrowserDataSource sctDataSource = (SCTConceptBrowserDataSource) dataSource;

                setStatedRelsEnabled(sctDataSource.getRelease().supportsStatedRelationships());
            }

            @Override
            public void dataSourceRemoved() {
                
            }
        });
    }
    
    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        
        this.attributeRelList.setEnabled(value);
        
        boolean enableStatedRels = false;
        
        if (value) {
            if (getMainPanel().getDataSource().isPresent()) {
                SCTConceptBrowserDataSource sctDataSource = (SCTConceptBrowserDataSource) getMainPanel().getDataSource().get();
                enableStatedRels = sctDataSource.getRelease().supportsStatedRelationships();
            }
        }
        
        this.setStatedRelsEnabled(enableStatedRels);
    }
    
    @Override
    public void reset() {
        super.reset();
        
        this.attributeRelList.reset();
        
        statedParentsList.reset();
        statedChildrenList.reset();
    }
    
    private void setStatedRelsEnabled(boolean value) {
        this.statedChildrenList.setActive(value);
        this.statedParentsList.setActive(value);

        this.statedParentsBtn.setEnabled(value);
        this.statedChildrenBtn.setEnabled(value);
    }
}
