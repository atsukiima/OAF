package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.Set;

/**
 * A diff partial-area taxonomy that includes descriptive delta
 * change information
 * 
 * @author Chris O
 */
public class SCTDescriptiveDiffPAreaTaxonomy extends DiffPAreaTaxonomy {
    
    public SCTDescriptiveDiffPAreaTaxonomy(
            SCTDescriptiveDiffAreaTaxonomy areaTaxonomy,
            PAreaTaxonomy fromTaxonomy,
            PAreaTaxonomy toTaxonomy,
            Hierarchy<DiffPArea> pareaHierarchy) {
        
        super(areaTaxonomy, fromTaxonomy, toTaxonomy, pareaHierarchy);
    }
    
    public SCTDescriptiveDiffAreaTaxonomy getAreaTaxonomy() {
        return (SCTDescriptiveDiffAreaTaxonomy)super.getAreaTaxonomy();
    }
    
    public DescriptiveDelta getDescriptiveDelta() {
        return getAreaTaxonomy().getDescriptiveDelta();
    }
    
    public String dumpDiffPAreaEditedConcepts() {
        Set<DiffPArea> diffPAreas = this.getPAreas();
        
        StringBuilder builder = new StringBuilder();
        
        DescriptiveDelta delta = this.getDescriptiveDelta();
        
        diffPAreas.forEach( (diffPArea) -> {
            diffPArea.getConcepts().forEach( (concept) -> {
               SCTConcept sctConcept = (SCTConcept) concept; 
               
               boolean opApplied = false;
  
               if(delta.getStatedEditingOperations().containsKey(sctConcept)) {
                   builder.append(String.format("%s\t%s\t%s", 
                           diffPArea.getName(), 
                           sctConcept.getName(),
                           "Stated Editing Op"));
                   
                   builder.append("\n");
                   
                   opApplied = true;
               }
               
               if(delta.getInferredChanges().containsKey(sctConcept)) {
                   builder.append(String.format("%s\t%s\t%s", 
                           diffPArea.getName(), 
                           sctConcept.getName(),
                           "Inferred Change"));
                   
                   builder.append("\n");
                   
                   opApplied = true;
               }
               
               if(!opApplied) {
                   builder.append(String.format("%s\t%s\t%s", 
                           diffPArea.getName(), 
                           sctConcept.getName(),
                           "No Change"));
                   
                   builder.append("\n");
               }
            });
        });
        
        return builder.toString();
    }
}
