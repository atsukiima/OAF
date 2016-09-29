package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;

/**
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
}
