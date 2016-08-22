package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Chris O
 */
public class SCTHierarchySelectionPanel extends JPanel {
    
    public interface HierarchySelectionAction {
        public void performHierarchySelectionAction(DummyConcept hierarchyRoot, boolean useStated);
    } 
    
    private final HashMap<DummyConcept, JButton> hierarchyBtns = new HashMap<>();
    
    private final ArrayList<DummyConcept> enabledRoots;
    
    private boolean statedReleaseAvailable = false;
    private final JCheckBox chkUseStatedRelationships;
    
    private final String type;
    
    private final JPanel hierarchyPanel;
    
    private final SubjectAbstractionNetworkPanel subjectSelectionPanel;
    
    private Optional<SCTRelease> currentRelease = Optional.empty();
    
    public SCTHierarchySelectionPanel(
            ArrayList<DummyConcept> hierarchyRoots, 
            ArrayList<DummyConcept> enabledRoots, 
            String type, 
            HierarchySelectionAction selectionAction) {
        
        super(new BorderLayout());
        
        this.enabledRoots = enabledRoots;
        this.type = type;
        
        this.chkUseStatedRelationships = new JCheckBox(String.format("Use Stated Relationships to Derive %s", type));
        this.chkUseStatedRelationships.setEnabled(false);
        
        this.subjectSelectionPanel = new SubjectAbstractionNetworkPanel(selectionAction);
        this.subjectSelectionPanel.setEnabled(false);
        
        Dimension fixedSize = new Dimension(500, - 1);
        
        this.subjectSelectionPanel.setMinimumSize(fixedSize);
        this.subjectSelectionPanel.setMaximumSize(fixedSize);
        this.subjectSelectionPanel.setPreferredSize(fixedSize);
        
        hierarchyPanel = new JPanel(new GridLayout(4, 5, 2, 2));
        
        hierarchyPanel.setBorder(BorderFactory.createTitledBorder("Select a top-level hierarchy"));
        
        this.subjectSelectionPanel.setBorder(BorderFactory.createTitledBorder("Select a specific concept"));

        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        hierarchyRoots.forEach((root) -> {
            String hierarchyName = root.getName().substring(0, root.getName().lastIndexOf("(") - 1);
            
            JButton btn = new JButton(String.format("<html><div align=\"center\">%s", hierarchyName));
            btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            btn.addActionListener( (ae) -> {
                selectionAction.performHierarchySelectionAction(root, chkUseStatedRelationships.isSelected());
            });
            
            if(enabledRoots.contains(root)) {
                btn.setToolTipText(String.format("<html>Click me to view the <b>%s</b> hierarchy's %s.", hierarchyName, type));
            } else {
                btn.setToolTipText(String.format("<html>It is not possible to derive a %s for the <b>%s</b> hierarchy.", type, hierarchyName));
                btn.setEnabled(false);
            }
            
            btn.setIcon(getImageIconFor(root));
            
            hierarchyBtns.put(root, btn);
            hierarchyPanel.add(btn);
        });
        
        this.add(chkUseStatedRelationships, BorderLayout.NORTH);
        this.add(hierarchyPanel, BorderLayout.CENTER);
        this.add(subjectSelectionPanel, BorderLayout.EAST);
    }
    
    public void setCurrentRelease(SCTRelease release) {
        this.currentRelease = Optional.of(release);
        
        subjectSelectionPanel.setCurrentRelease(release);
    }
    
    public void clearCurrentRelease() {
        this.currentRelease = Optional.empty();
        
        subjectSelectionPanel.clearCurrentRelease();
    }
    
    public void setStatedReleaseAvailable(boolean value) {
        this.statedReleaseAvailable = value;
        
        if(value) {
            chkUseStatedRelationships.setText(String.format("Use Stated Relationships to Derive %s", type));
        } else {
            chkUseStatedRelationships.setText(
                    String.format("Use Stated Relationships to Derive %s (not available for selected data source or release)", type));
            chkUseStatedRelationships.setEnabled(false);
            chkUseStatedRelationships.setSelected(false);
        }
    }
    
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        
        hierarchyPanel.setEnabled(value);
        
        subjectSelectionPanel.setEnabled(value);
        
        if(statedReleaseAvailable) {
            chkUseStatedRelationships.setEnabled(value);
        }

        enabledRoots.forEach((enabledRoot) -> {
            hierarchyBtns.get(enabledRoot).setEnabled(value);
        });
    }
    
     /**
     * Returns the image icon for the specified hierarchy root concept.
     *
     * @param root The root concept of the hierarchy.
     * @return Icon, if one exists, for the given root's hierarchy.
     */
    private ImageIcon getImageIconFor(DummyConcept root) {

        String rootName = root.getName();
        String iconName = "";

        if (rootName.startsWith("Body structure")) {
            iconName = "bodystructure-icon.png";
        } else if (rootName.startsWith("Procedure")) {
            iconName = "procedure-icon.png";
        } else if (rootName.startsWith("Pharmaceutical")) {
            iconName = "pharmaceuticalproduct-icon.png";
        } else if (rootName.startsWith("Specimen")) {
            iconName = "specimen-icon.png";
        } else if (rootName.startsWith("Record artifact")) {
            iconName = "recordartifact-icon.png";
        } else if (rootName.startsWith("Clinical finding")) {
            iconName = "clinicalfinding-icon.png";
        } else if (rootName.startsWith("Environment or")) {
            iconName = "environmentgeoloc-icon.png";
        } else if (rootName.startsWith("Organism")) {
            iconName = "organism-icon.png";
        } else if (rootName.startsWith("Physical force")) {
            iconName = "physicalforce-icon.png";
        } else if (rootName.startsWith("Event")) {
            iconName = "event-icon.png";
        } else if (rootName.startsWith("Observable entity")) {
            iconName = "observableentity-icon.png";
        } else if (rootName.startsWith("Substance")) {
            iconName = "substance-icon.png";
        } else if (rootName.startsWith("Physical object")) {
            iconName = "physicalobject-icon.png";
        } else if (rootName.startsWith("Linkage concept")) {
            iconName = "linkage-icon.png";
        } else if (rootName.startsWith("Social context")) {
            iconName = "social-icon.png";
        } else if (rootName.startsWith("Staging")) {
            iconName = "scales-icon.png";
        } else if (rootName.startsWith("Situation ")) {
            iconName = "situation-icon.png";
        } else {
            return null;
        }

        return IconManager.getIconManager().getIcon(iconName);
    }
    
    
}
