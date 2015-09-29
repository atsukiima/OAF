package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band;

import SnomedShared.Concept;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeSummaryPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTBandSummaryPanel extends AbstractNodeSummaryPanel<CommonOverlapSet> {


    
    private final SCTBandTribesDetailsPanel bandTribesPanel;
    
    private final SCTTANConfiguration config;
    
    public SCTBandSummaryPanel(SCTTANConfiguration config) {

        this.bandTribesPanel = new SCTBandTribesDetailsPanel(config);
        
        this.add(bandTribesPanel);
        
        this.config = config;
    }
    
    @Override
    protected String createDescriptionStr(CommonOverlapSet band) {
        String bandName = config.getContainerName(band);
        
        ArrayList<SCTCluster> clusters = config.getTribalAbstractionNetwork().convertClusters(band.getAllClusters());
        
        HashSet<Concept> allConcepts = new HashSet<>();
        
        clusters.forEach( (SCTCluster cluster) -> {
            allConcepts.addAll(cluster.getConcepts());
        });
        
        String result = String.format("<html><b>%s</b> is a tribal band that summarizes %d concepts in %d clusters.", 
                bandName, allConcepts.size(), band.getAllClusters().size());
        
        result += "<p><b>Help / Description:</b><br>";
        result += config.getContainerHelpDescription(band);
        
        return result;
    }
    
    @Override
    public void clearContents() {
        super.clearContents();
        
        bandTribesPanel.clearContents();
    }

    @Override
    public void setContents(CommonOverlapSet band) {
        super.setContents(band);
        
        ArrayList<Concept> tribalPatriarchs = new ArrayList<>();
        
        HashSet<Long> tribePatriarchIds = band.getSetEntryPoints();
        
        tribePatriarchIds.forEach( (Long tribePatriarchId) -> {
            tribalPatriarchs.add(config.getTribalAbstractionNetwork().getConcepts().get(tribePatriarchId));
        });
        
        Collections.sort(tribalPatriarchs, new ConceptNameComparator());
        
        bandTribesPanel.setContents(tribalPatriarchs);
    }
}
