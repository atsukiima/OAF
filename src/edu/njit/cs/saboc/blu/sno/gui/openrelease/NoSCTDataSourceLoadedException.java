package edu.njit.cs.saboc.blu.sno.gui.openrelease;

/**
 * General purpose exception for when there is no given 
 * SNOMED CT release.
 * 
 * @author Chris O
 */
public class NoSCTDataSourceLoadedException extends Exception {
    public NoSCTDataSourceLoadedException() {
        super("No SCT Data Source Loaded");
    }
}