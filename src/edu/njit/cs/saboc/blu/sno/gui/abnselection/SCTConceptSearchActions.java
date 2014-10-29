package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;

/**
 *
 * @author Chris
 */
public interface SCTConceptSearchActions {
    public SCTDataSource getDataSource();
    public void doAction(Concept c);
}
