package edu.njit.cs.saboc.blu.sno.localdatasource.concept;

/**
 *
 * @author Chris O
 */
public class SCTEntityNameUtils {
    public static String getConceptTypeName(boolean plural) {
        if(plural) {
            return "Concepts";
        } else {
            return "Concept";
        }
    }
    
    public static String getPropertyTypeName(boolean plural) {
        if(plural) {
            return "Attribute relationships";
        } else {
            return "Attribute relationship";
        }
    }

    public static String getParentConceptTypeName(boolean plural) {
        if(plural) {
            return "Parents";
        } else {
            return "Parent";
        }
    }

    public static String getChildConceptTypeName(boolean plural) {
        if(plural) {
            return "Children";
        } else {
            return "Child";
        }
    }
}
