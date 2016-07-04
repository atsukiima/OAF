package edu.njit.cs.saboc.blu.sno.gui.abnselection;

/**
 *  Bare-bones concept information for when a SCT Release has not yet been loaded
 * 
 * @author Chris O
 */
public class DummyConcept {

    private final long id;
    private final String name;

    public DummyConcept(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getID() {
        return id;
    }

    public String getName() {
        return name;
    }
}
