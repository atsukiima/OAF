package edu.njit.cs.saboc.blu.sno.abn.local;

import SnomedShared.pareataxonomy.GroupParentInfo;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public interface LocalConceptGroup {
    public SCTConceptHierarchy getConceptHierarchy();
    
    public ArrayList<GroupParentInfo> getParentGroupInformation();
}
