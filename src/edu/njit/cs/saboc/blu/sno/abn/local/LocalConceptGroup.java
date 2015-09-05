package edu.njit.cs.saboc.blu.sno.abn.local;

import SnomedShared.Concept;
import SnomedShared.generic.GenericConceptGroup;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public interface LocalConceptGroup<GROUP_T extends GenericConceptGroup> {
    public SCTConceptHierarchy getConceptHierarchy();
    
    public ArrayList<GenericParentGroupInfo<Concept, GROUP_T>> getParentGroupInformation();
}
