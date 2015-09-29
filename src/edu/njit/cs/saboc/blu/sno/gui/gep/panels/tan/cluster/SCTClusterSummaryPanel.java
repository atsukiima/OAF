package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import SnomedShared.Concept;
import SnomedShared.overlapping.EntryPoint;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractEntityList;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeSummaryPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTClusterSummaryPanel extends AbstractNodeSummaryPanel<SCTCluster> {

    private final SCTTANConfiguration config;
    
    private final AbstractEntityList<EntryPoint> patriarchList;
    
    public SCTClusterSummaryPanel(SCTTANConfiguration config) {
        this.config = config;
        
        patriarchList = new AbstractEntityList<EntryPoint>(new SCTClusterPatriarchsTableModel(config.getTribalAbstractionNetwork())) {

            @Override
            protected String getBorderText(Optional<ArrayList<EntryPoint>> entities) {
                if(entities.isPresent()) {
                    return String.format("Tribes (%d)", entities.get().size());
                } else {
                    return "Tribes";
                }
            }
        };
        
        this.patriarchList.setMinimumSize(new Dimension(-1, 100));
        this.patriarchList.setPreferredSize(new Dimension(-1, 100));
        
        this.add(this.patriarchList);
    }
    
     public void setContents(SCTCluster cluster) {
        super.setContents(cluster);
        
        SCTTribalAbstractionNetwork tan = config.getTribalAbstractionNetwork();
        
        ArrayList<EntryPoint> patriarchs = new ArrayList<>(cluster.getEntryPointSet());
        
        Collections.sort(patriarchs, new Comparator<EntryPoint>() {
            public int compare(EntryPoint a, EntryPoint b) {
                String aName = tan.getConcepts().get(a.getEntryPointConceptId()).getName();
                String bName = tan.getConcepts().get(b.getEntryPointConceptId()).getName();
                
                return aName.compareTo(bName);
            }
        });
        
        this.patriarchList.setContents(patriarchs);
    }
    
    public void clearContents() {
        super.clearContents();
        
        this.patriarchList.clearContents();
    }

    protected String createDescriptionStr(SCTCluster cluster) {
        SCTTribalAbstractionNetwork tan = config.getTribalAbstractionNetwork();
        
        String conceptType = config.getConceptTypeName(true).toLowerCase();
        
        String rootName = config.getGroupName(cluster);
        int classCount = cluster.getConceptCount();

        int parentCount = tan.getParentGroups(cluster).size();
        int childCount = tan.getChildGroups(cluster).size();

        ArrayList<SCTCluster> descendantPAreas = config.convertClusterSummaryList(new ArrayList<>(tan.getDescendantGroups(cluster)));

        HashSet<Concept> descendantClasses = new HashSet<>();

        descendantPAreas.forEach((SCTCluster c) -> {
            descendantClasses.addAll(c.getConcepts());
        });
        
        String result = String.format("<html><b>%s</b> is a cluster that summarizes %d %s. It has %d parent clusters and %d child clusters. "
                + "There are a total of %d descendant clusters that summarize %d %s",
                rootName, classCount, conceptType, parentCount, childCount, descendantPAreas.size(), descendantClasses.size(), conceptType);
        
        result += "<p><b>Help / Description:</b><br>";
        result += config.getGroupHelpDescriptions(cluster);

        return result;
    }
}
