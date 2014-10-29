package edu.njit.cs.saboc.blu.sno.conceptbrowser;

import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A singleton class that keeps track of user-specified options.
 */
public class Options {
    public interface Listener {
        void optionsUpdated();
    }

    private static class Wrapper implements PropertyChangeListener {
        Listener l;

        Wrapper(Listener l) {
            this.l = l;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            l.optionsUpdated();
        }
    }
    
    private PropertyChangeSupport propertyChange;

    private boolean CUIsVisible;
    private boolean toolTipsVisible;

    private boolean grandparentsDisplayed;
    private boolean grandchildrenDisplayed;

    private SCTDataSource dataSource;

    public Options() {
        CUIsVisible = true;
        toolTipsVisible = true;
        grandparentsDisplayed = false;
        grandchildrenDisplayed = false;
        propertyChange = new PropertyChangeSupport(this);
    }

    public void addOptionListener(final Listener l) {
        addPropertyChangeListener(new Wrapper(l));
    }
 
    public void removedOptionListener(Listener l) {
    	for (PropertyChangeListener listener: propertyChange.getPropertyChangeListeners()) {
    		if (listener instanceof Wrapper) {
    			if (listener.equals(l)) {
    				propertyChange.removePropertyChangeListener(listener);
    				break;
    			}
    		}
    	}
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    	propertyChange.addPropertyChangeListener(listener);
    }
    public void addPropertyChangeListener(String propName, PropertyChangeListener listener) {
    	propertyChange.addPropertyChangeListener(propName, listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
    	propertyChange.removePropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(String propName, PropertyChangeListener listener) {
    	propertyChange.removePropertyChangeListener(propName, listener);
    }

    public boolean areCUIsVisible() {
        return CUIsVisible;
    }

    public void setCUIsVisible(boolean CUIsVisible) {
    	boolean oldValue = this.CUIsVisible;
        this.CUIsVisible = CUIsVisible;
        propertyChange.firePropertyChange("CUIsVisible", oldValue, CUIsVisible);
    }

    public boolean areGrandchildrenDisplayed() {
        return grandchildrenDisplayed;
    }

    public void setGrandchildrenDisplayed(boolean grandchildrenDisplayed) {
    	boolean oldValue = this.grandchildrenDisplayed;
        this.grandchildrenDisplayed = grandchildrenDisplayed;

        propertyChange.firePropertyChange("GrandchildrenDisplayed", oldValue, grandchildrenDisplayed);
    }

    public boolean areGrandparentsDisplayed() {
        return grandparentsDisplayed;
    }

    public void setGrandparentsDisplayed(boolean grandparentsDisplayed) {
    	boolean oldValue = this.grandparentsDisplayed;
        this.grandparentsDisplayed = grandparentsDisplayed;

        propertyChange.firePropertyChange("GrandparentsDisplayed", oldValue, grandparentsDisplayed);
    }

    public boolean areToolTipsVisible() {
        return toolTipsVisible;
    }

    public void setToolTipsVisible(boolean toolTipsVisible) {
    	boolean oldValue = this.toolTipsVisible;
        this.toolTipsVisible = toolTipsVisible;

        propertyChange.firePropertyChange("ToolTipsVisible", oldValue, toolTipsVisible);
    }
    
    public SCTDataSource getDataSource() {
    	return dataSource;
    }

    public void setDataSource(SCTDataSource dataSource) {
    	this.dataSource = dataSource;
    }
}