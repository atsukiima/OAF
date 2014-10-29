
package edu.njit.cs.saboc.blu.sno.gui.dialogs.disjointpareataxonomy;

import edu.njit.cs.saboc.blu.core.gui.disjointabn.DisjointGroupEntry;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;

/**
 *
 * @author Chris
 */
public class BluDisjointPartialArea extends DisjointGroupEntry<DisjointPartialArea> {
    
    public BluDisjointPartialArea(DisjointPartialArea disjointPartialArea) {
        super(disjointPartialArea);
    }
    
    public DisjointPartialArea getDisjointPArea() {
        return super.getDisjointGroup();
    }
    
    public String getRootName() {
        return this.getDisjointPArea().getRoot().getName();
    }
}
