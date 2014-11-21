/*******************************************************************************
 * $Id: SnomedConceptBrowser.java,v 1.9 2014/07/28 17:57:00 uid57051 Exp $
 */
package edu.njit.cs.saboc.blu.sno.conceptbrowser;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels.AbstractionNetworkPanel;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels.FocusConceptPanel;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels.HierarchyMetricsPanel;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels.LogoPanel;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels.ParentChildPanel;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels.RelationshipPanel;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels.SearchPanel;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels.SynonymPanel;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

/**
 * The main class of the NAT.
 */
public class SnomedConceptBrowser {
    public static final long ROOT_CONCEPT_ID = 138875005;
	
    public String fullSourceNames = null;
    public String abbSourceNames = null;
    
    private Color neighborhoodBGColor = new Color(150, 190, 220);

    private SCTDataSource dataSource;

    private FocusConcept focusConcept;
    private Options options;

    private LogoPanel logoPanel;
    private ParentChildPanel parentPanel;
    private ParentChildPanel childPanel;
    private SearchPanel searchPanel;
    private SynonymPanel synonymPanel;
    private AbstractionNetworkPanel abstractionNetworkPanel;
    private HierarchyMetricsPanel hierarchyMetricsPanel;
        
    private FocusConceptPanel focusConceptPanel;
    private RelationshipPanel relationshipPanel;
    private JPanel mainBrowserPanel;
    
    private SCTDisplayFrameListener displayFrameListener;

    public SnomedConceptBrowser(SCTDataSource dataSource, SCTDisplayFrameListener displayFrameListener) {
        this.options = new Options();
        this.dataSource = dataSource;
        
        this.displayFrameListener = displayFrameListener;
        
        focusConcept = new FocusConcept(this, options, dataSource);
        
        mainBrowserPanel = new JPanel();
        initConceptBrowser(mainBrowserPanel);
        
        logoPanel.setSelectedVersionByName(dataSource.getSelectedVersion());
        
        navigateTo(ROOT_CONCEPT_ID);

        // Update
        focusConcept.updateAll();
    }
    
    public SCTDisplayFrameListener getDisplayFrameListener() {
        return displayFrameListener;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void navigateTo(Concept c) {
        focusConcept.navigate(c);
    }

    public void navigateTo(long conceptId) {
        Concept c = dataSource.getConceptFromId(conceptId);
        navigateTo(c);
    }

    public void initConceptBrowser(JPanel panel) {
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        logoPanel = new LogoPanel(this, dataSource);
        panel.add(logoPanel, c);

        c.gridx = 1;
        c.gridy = 0;
        parentPanel = new ParentChildPanel(this, ParentChildPanel.PanelType.PARENT, dataSource);
        panel.add(parentPanel, c);

        c.gridx = 2;
        c.gridy = 0;
        searchPanel = new SearchPanel(this, dataSource);
        panel.add(searchPanel, c);

        c.gridx = 0;
        c.gridy = 1;
        synonymPanel = new SynonymPanel(this, dataSource);
        panel.add(synonymPanel, c);

        c.gridx = 1;
        c.gridy = 1;
        focusConceptPanel = new FocusConceptPanel(this, dataSource);
        panel.add(focusConceptPanel, c);

        c.gridx = 2;
        c.gridy = 1;
        relationshipPanel = new RelationshipPanel(this, dataSource);
        panel.add(relationshipPanel, c);

        c.gridx = 1;
        c.gridy = 2;
        childPanel = new ParentChildPanel(this, ParentChildPanel.PanelType.CHILD, dataSource);
        panel.add(childPanel, c);

        c.gridx = 0;
        c.gridy = 2;
        abstractionNetworkPanel = new AbstractionNetworkPanel(this, dataSource);
        panel.add(abstractionNetworkPanel, c);     
        
        if (dataSource instanceof SCTLocalDataSource) {
            c.gridx = 2;
            c.gridy = 2;
            
            hierarchyMetricsPanel = new HierarchyMetricsPanel(this, dataSource);
            
            panel.add(hierarchyMetricsPanel, c);
        }
    }

    public FocusConceptPanel getFocusConceptPanel() {
        return focusConceptPanel;
    }

    public Color getNeighborhoodBGColor() {
        return neighborhoodBGColor;
    }

    public Options getOptions() {
        return options;
    }

    public FocusConcept getFocusConcept() {
        return focusConcept;
    }

    public JPanel getMainBrowserPanel() {
        return mainBrowserPanel;
    }
    
    public LogoPanel getLogoPanel() {
        return logoPanel;
    }

    public ParentChildPanel getParentPanel() {
        return parentPanel;
    }

    public ParentChildPanel getChildPanel() {
        return childPanel;
    }

    public RelationshipPanel getRelationshipPanel() {
        return relationshipPanel;
    }
}
