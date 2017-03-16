package edu.njit.cs.saboc.blu.sno.localdatasource.concept;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.OntologyEntityNameConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTEntityNameConfiguration implements OntologyEntityNameConfiguration {
    
    @Override
    public String getConceptTypeName(boolean plural) {
        if(plural) {
            return "Concepts";
        } else {
            return "Concept";
        }
    }
    
    @Override
    public String getPropertyTypeName(boolean plural) {
        if(plural) {
            return "Attribute relationships";
        } else {
            return "Attribute relationship";
        }
    }

    @Override
    public String getParentConceptTypeName(boolean plural) {
        if(plural) {
            return "Parents";
        } else {
            return "Parent";
        }
    }

    @Override
    public String getChildConceptTypeName(boolean plural) {
        if(plural) {
            return "Children";
        } else {
            return "Child";
        }
    }
}
