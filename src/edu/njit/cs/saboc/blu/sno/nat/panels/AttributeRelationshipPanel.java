package edu.njit.cs.saboc.blu.sno.nat.panels;

import edu.njit.cs.saboc.blu.core.graph.pareataxonomy.BasePAreaTaxonomyLayout;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.nat.SCTConceptBrowserDataSource;
import edu.njit.cs.saboc.blu.sno.nat.SCTNATDataRetrievers;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.gui.panels.ResultPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Chris O
 */
public class AttributeRelationshipPanel extends ResultPanel<SCTConcept, ArrayList<AttributeRelationship>> {
    
    private final JPanel contentPanel;
    
    private final ArrayList<AttributeRelationshipEntryPanel> relEntries = new ArrayList<>();
    
    public AttributeRelationshipPanel(
            NATBrowserPanel<SCTConcept> mainPanel,
            SCTConceptBrowserDataSource dataSource) {

        super(mainPanel, 
                dataSource, 
                SCTNATDataRetrievers.getAttributeRelationshipRetriever(dataSource));
        
        this.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLACK), 
                    "Attribute Relationships")
            );
        
        this.setLayout(new BorderLayout());
        
        this.contentPanel = new JPanel();
        this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        this.contentPanel.setOpaque(false);
        
        JPanel testPanel = new JPanel(new BorderLayout());
        
        testPanel.add(contentPanel, BorderLayout.NORTH);
        testPanel.add(Box.createGlue(), BorderLayout.CENTER);
        testPanel.setBackground(Color.WHITE);
        
        
        this.add(new JScrollPane(testPanel), BorderLayout.CENTER);
    }

    @Override
    public void dataPending() {
        contentPanel.removeAll();
        contentPanel.repaint();
        contentPanel.revalidate();
        
        relEntries.clear();
        
        this.revalidate();
    }

    @Override
    public void displayResults(ArrayList<AttributeRelationship> data) {
        ArrayList<RelationshipGroup> relGroups = new ArrayList<>();
        
        Map<Integer, ArrayList<AttributeRelationship>> relGroupMap = new HashMap<>();
        
        data.forEach( (rel) -> {
            if(!relGroupMap.containsKey(rel.getGroup())) {
                relGroupMap.put(rel.getGroup(), new ArrayList<>());
            }
            
            relGroupMap.get(rel.getGroup()).add(rel);
        });
        
        relGroupMap.forEach( (id, attributeRels) -> {
            relGroups.add(new RelationshipGroup(id, attributeRels));
        });
        
        relGroups.forEach( (relGroup) -> {
            RelationshipGroupEntry entry = new RelationshipGroupEntry(relGroup, getMainPanel());
            
            relEntries.addAll(entry.getAttributeRelationshipEntries());
            
            contentPanel.add(entry);
        });
        
        relEntries.forEach( (entry) -> {
                    
            entry.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (e.getClickCount() > 1) {
                            getMainPanel().getFocusConceptManager().navigateTo(entry.getAttributeRelationship().getTarget());
                        } else {
                            relEntries.forEach( (relEntry) -> {
                                
                                if(relEntry != entry) {
                                    relEntry.setSelected(false);
                                }
                            });
                            
                            if(e.isControlDown()) {
                                entry.setSelected(!entry.isSelected());
                            } else {
                                if(!entry.isSelected()) {
                                    entry.setSelected(true);
                                }
                            }
                        }
                    }
                }
            });
        });
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    @Override
    protected void setFontSize(int fontSize) {
        
    }

}

class RelationshipGroup {
    private final int groupId;
    private final ArrayList<AttributeRelationship> rels;
    
    public RelationshipGroup(int groupId, ArrayList<AttributeRelationship> rels) {
        this.groupId = groupId;
        this.rels = rels;
    }
    
    public ArrayList<AttributeRelationship> getAttributeRelationships() {
        return rels;
    }
    
    public int getGroupId() {
        return groupId;
    }
}

class RelationshipGroupEntry extends JPanel {

    private final RelationshipGroup group;
    
    private final ArrayList<AttributeRelationshipEntryPanel> relEntries = new ArrayList<>();
    
    public RelationshipGroupEntry(RelationshipGroup group, NATBrowserPanel<SCTConcept> mainPanel) {
        this.group = group;
        
        this.setOpaque(false);
        
        if(group.getGroupId() > 0) {
            
            // Get some colors, not related to taxonomies
            ArrayList<Color> colors = BasePAreaTaxonomyLayout.getTaxonomyLevelColors();
            
            this.setBorder(BorderFactory.createTitledBorder(
                    
                    BorderFactory.createLineBorder(
                        colors.get(group.getGroupId() % colors.size()), 
                            2, 
                            true), 
                    
                    String.format("Relationship Group %d", group.getGroupId())));
        }
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        ArrayList<AttributeRelationship> rels = group.getAttributeRelationships();
        
        int x = 0;
        
        for(AttributeRelationship rel : rels) {
            AttributeRelationshipEntryPanel panel = new AttributeRelationshipEntryPanel(rel, x++, mainPanel);
            
            relEntries.add(panel);
            this.add(panel);
        }
    }
    
    public RelationshipGroup getRelationshipGroup() {
        return group;
    }
    
    public  ArrayList<AttributeRelationshipEntryPanel> getAttributeRelationshipEntries() {
        return relEntries;
    }
}

class AttributeRelationshipEntryPanel extends JPanel {
    
    private final JLabel attributeRelTypeLabel;
    private final JLabel targetConceptNameLabel;
    private final JLabel targetConceptIdLabel;
    
    private final int index;
    
    private final AttributeRelationship rel;
    
    private boolean isSelected = false;
    
    public AttributeRelationshipEntryPanel(
            AttributeRelationship rel, 
            int index, 
            NATBrowserPanel<SCTConcept> mainPanel) {
        
        this.rel = rel;
        this.index = index;
        
        this.setOpaque(false);
        
        this.setLayout(new BorderLayout());
        
        this.attributeRelTypeLabel = new JLabel();
        this.targetConceptNameLabel = new JLabel();
        this.targetConceptIdLabel = new JLabel();

        this.attributeRelTypeLabel.setFont(this.attributeRelTypeLabel.getFont().deriveFont(Font.PLAIN, 16));
        this.targetConceptNameLabel.setFont(this.targetConceptNameLabel.getFont().deriveFont(Font.PLAIN, 16));
        this.targetConceptIdLabel.setFont(this.targetConceptIdLabel.getFont().deriveFont(Font.PLAIN, 10));
        
        this.targetConceptIdLabel.setForeground(Color.BLUE);
        
        this.targetConceptNameLabel.setOpaque(false);
        this.targetConceptIdLabel.setOpaque(false);
        
        JPanel relNamePanel = new JPanel();
        relNamePanel.setOpaque(false);
        
        JLabel relStartLabel = new JLabel(" == ");
        relStartLabel.setFont(relStartLabel.getFont().deriveFont(Font.BOLD, 16));
        
        JLabel relEndLabel = new JLabel(" ==> ");
        relEndLabel.setFont(relEndLabel.getFont().deriveFont(Font.BOLD, 16));
        
        relStartLabel.setOpaque(false);
        relEndLabel.setOpaque(false);
        
        relStartLabel.setForeground(Color.RED);
        relEndLabel.setForeground(Color.RED);
        
        relNamePanel.add(relStartLabel);
        relNamePanel.add(attributeRelTypeLabel);
        relNamePanel.add(relEndLabel);
        
        JPanel targetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        targetPanel.add(targetConceptNameLabel);
        targetPanel.add(Box.createHorizontalStrut(8));
        targetPanel.add(targetConceptIdLabel);
        
        targetPanel.setOpaque(false);
        
        JPanel entryPanel = new JPanel();
        entryPanel.setOpaque(false);
        
        entryPanel.add(relNamePanel);
        entryPanel.add(targetPanel);
        
        this.add(entryPanel, BorderLayout.WEST);

        String relNameStr = rel.getType().getName();

        String conceptNameStr = rel.getTarget().getName();
        String conceptIdStr = rel.getTarget().getIDAsString();

        this.attributeRelTypeLabel.setText(relNameStr);
        this.targetConceptNameLabel.setText(conceptNameStr);
        this.targetConceptIdLabel.setText(conceptIdStr);
    }
    
    public AttributeRelationship getAttributeRelationship() {
        return rel;
    }
    
    public void setSelected(boolean value) {
        this.isSelected = value;
        
        if(value) {
            this.setOpaque(true);
            this.setBackground(new Color(220, 220, 255));
        } else {
            this.setOpaque(false);
            this.setBackground(Color.WHITE);
        }
    }
    
    public boolean isSelected() {
        return isSelected;
    }
}