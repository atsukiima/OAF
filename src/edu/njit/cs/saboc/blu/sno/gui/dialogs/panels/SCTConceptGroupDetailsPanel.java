package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels;

import edu.njit.cs.saboc.blu.core.gui.dialogs.panels.GroupDetailsPanel;
import edu.njit.cs.saboc.blu.core.gui.dialogs.panels.GroupDetailsPanel.GroupType;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.utils.BrowserLauncher;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author cro3
 */
public class SCTConceptGroupDetailsPanel extends GroupDetailsPanel {
    
    private JEditorPane groupSummaryPane;
    private JEditorPane groupParentsPane;
    private JEditorPane groupConceptsPane;
    private JEditorPane groupChildrenPane;
    
    private final SCTDisplayFrameListener displayFrameListener;
    
    public SCTConceptGroupDetailsPanel(SCTAbstractionNetwork abn, GroupType type, SCTDisplayFrameListener displayFrameListener) {
        super(abn, type);
        
        this.displayFrameListener = displayFrameListener;
    }
    
    protected void initialize() {

        groupSummaryPane = makeTextDetailsPane();

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BorderLayout());
        summaryPanel.add(new JScrollPane(groupSummaryPane), BorderLayout.CENTER);

        super.setGroupSummaryComponent(summaryPanel);

        groupParentsPane = makeTextDetailsPane();

        JPanel parentPanel = new JPanel();
        parentPanel.setLayout(new BorderLayout());
        parentPanel.add(new JScrollPane(groupParentsPane), BorderLayout.CENTER);

        if(this.getGroupType() == GroupType.PartialArea) {
            parentPanel.setBorder(BorderFactory.createTitledBorder("Partial-area Root Concept Parents"));
        } else if(this.getGroupType() == GroupType.Cluster) {
            parentPanel.setBorder(BorderFactory.createTitledBorder("Cluster Root Concept Parents"));
        } else if(this.getGroupType() == GroupType.DisjointPArea) {
            parentPanel.setBorder(BorderFactory.createTitledBorder("Disjoint Partial-area Root Concept Parents"));
        }

        super.setParentComponent(parentPanel);

        groupConceptsPane = this.makeTextDetailsPane();

        JPanel conceptPanel = new JPanel();
        conceptPanel.setLayout(new BorderLayout());
        conceptPanel.add(new JScrollPane(groupConceptsPane), BorderLayout.CENTER);

        if(this.getGroupType() == GroupType.PartialArea) {
            conceptPanel.setBorder(BorderFactory.createTitledBorder("Concepts in Partial-area (Alphabetical)"));
        } else if (this.getGroupType() == GroupType.Cluster) {
            conceptPanel.setBorder(BorderFactory.createTitledBorder("Concepts in Cluster (Alphabetical)"));
        } else if(this.getGroupType() == GroupType.DisjointPArea) {
            conceptPanel.setBorder(BorderFactory.createTitledBorder("Concepts in Disjoint Partial-area (Alphabetical)"));
        }

        super.setConceptsComponent(conceptPanel);

        groupChildrenPane = makeTextDetailsPane();

        JPanel childPanel = new JPanel();
        childPanel.setLayout(new BorderLayout());
        childPanel.add(new JScrollPane(groupChildrenPane), BorderLayout.CENTER);

        if(this.getGroupType() == GroupType.PartialArea) {
            childPanel.setBorder(BorderFactory.createTitledBorder("Child Partial-areas"));
        } else if (this.getGroupType() == GroupType.Cluster) {
            childPanel.setBorder(BorderFactory.createTitledBorder("Child Clusters"));
        } else if(this.getGroupType() == GroupType.DisjointPArea) {
            childPanel.setBorder(BorderFactory.createTitledBorder("Child Disjoint Partial-areas"));
        }

        this.setChildrenComponent(childPanel);
    }
    
    public void setSummaryText(String text) {
        groupSummaryPane.setText(text);
        resetTextPaneScrollPosition(groupSummaryPane);
    }

    public void setParentText(String text) {
        groupParentsPane.setText(text);
        resetTextPaneScrollPosition(groupParentsPane);
    }

    public void setConceptsText(String text) {
        groupConceptsPane.setText(text);
        resetTextPaneScrollPosition(groupConceptsPane);
    }

    public void setChildrenText(String text) {
        groupChildrenPane.setText(text);
        resetTextPaneScrollPosition(groupChildrenPane);
    }
    
    
    protected JEditorPane makeTextDetailsPane() {
        JEditorPane editorPane = super.makeTextDetailsPane();
        
        editorPane.addHyperlinkListener(new BrowserLauncher(displayFrameListener, (SCTAbstractionNetwork)abstractionNetwork));
        
        return editorPane;
    } 
}
