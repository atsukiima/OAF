/*******************************************************************************
 * $Id: History.java,v 1.1 2012/06/15 21:01:11 uid57051 Exp $
 */
package edu.njit.cs.saboc.blu.sno.conceptbrowser;

import SnomedShared.Concept;
import java.util.ArrayList;
import java.util.Vector;

/**
 * A singleton class that keeps track of the user's browsing history.
 */
public class History {
    private ArrayList<Concept> conceptHistory = new ArrayList<Concept>();;
    private int position = -1;

    public History() {
        
    }

    public void addHistoryCUI(Concept concept) {
        if(!conceptHistory.isEmpty() && conceptHistory.get(getPosition()).equals(concept)) {
            return;
        }

        clearFuture();
        
        conceptHistory.add(concept);
        position++;
    }

    public void minusPosition() {
        position--;
    }

    public void plusPosition() {
        position++;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int pos) {
        position = pos;
    }

    public ArrayList<Concept> getHistoryList() {
        return conceptHistory;
    }

    public Concept getCurrentConcept() {
        return conceptHistory.get(position);
    }

    public void clearFuture() {
        for(int i = (conceptHistory.size() - 1); i > position; i--) {
            conceptHistory.remove(i);
        }
    }

    // Removes all concepts from history
    public void emptyHistory() {
        conceptHistory.clear();
        position = -1;
    }

    public boolean atTopOfHistory() {
        return position == conceptHistory.size() - 1;
    } 
}
