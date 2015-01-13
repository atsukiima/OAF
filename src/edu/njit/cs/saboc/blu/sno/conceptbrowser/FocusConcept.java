package edu.njit.cs.saboc.blu.sno.conceptbrowser;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels.BaseNavPanel;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSCTConceptStated;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSourceWithStated;

import javax.swing.SwingUtilities;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * This class encapsulates the Focus Concept and handles getting
 * the concepts related to it.
 * @author Paul Accisano
 */
public class FocusConcept {
    private SCTDataSource dataSource;
    private SnomedConceptBrowser browser;
    
    private Concept concept = null;

    public enum Fields {
        CONCEPT, 
        PARENTS, 
        CHILDREN, 
        SYNONYMS, 
        SIBLINGS, 
        CONCEPTREL, 
        
        PARTIALAREA, 
        TRIBALAN, 
        
        HIERARCHYMETRICS, 
        ALLANCESTORS,
        ALLDESCENDANTS, 
        ALLPATHS, 
        
        STATEDPARENTS, 
        STATEDCHILDREN,
        STATEDCONCEPTRELS, 
        STATEDSIBLINGS, 
        STATEDANCESTORS, 
        STATEDDESCENDANTS, 
        STATEDHIERARCHYMETRICS
    };

    // Concept data
    private Map<Fields, Object> dataLists =
            new EnumMap<Fields, Object>(Fields.class);

    // Whether or not a given field has already been filled
    private Map<Fields, Boolean> alreadyFilled =
            new EnumMap<Fields, Boolean>(Fields.class);
    
    // The panels that actually display concepts
    private Map<Fields, BaseNavPanel> displayPanels =
            new EnumMap<Fields, BaseNavPanel>(Fields.class);

    // The panels that need to be notified of a concept change
    private ArrayList<BaseNavPanel> listeners =
            new ArrayList<BaseNavPanel>();

    private History history = new History();

    private Options options;

    private ArrayList<UpdateThread> updateThreads = new ArrayList<UpdateThread>();

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public FocusConcept(SnomedConceptBrowser browser, Options options, SCTDataSource dataSource) {
        this.browser = browser;
        this.options = options;
        this.dataSource = dataSource;
    }

    public History getHistory() {
        return history;
    }

    // Sets panel corresponding to the given field.  Called by NATtab upon
    // panel construction.
    public void addFocusConceptListener(BaseNavPanel fcl) {
        listeners.add(fcl);
    }

    public void addDisplayPanel(Fields displayField, BaseNavPanel panel) {
        displayPanels.put(displayField, panel);
    }

    public void reloadCurrentConcept() {
        if(concept != null) {
            navigate(new Concept(getConcept()));
        }
    }

    public void navigateRoot() {
        history.emptyHistory();
        navigate(dataSource.getConceptFromId(SnomedConceptBrowser.ROOT_CONCEPT_ID));
    }

    // Sets the Focus Concept.
    public void navigate(Concept c) {
        concept = c;

        history.addHistoryCUI(c);

        // We know nothing about this concept yet
        for(Fields f : Fields.values()) {
            alreadyFilled.put(f, false);
        }

        // Clear out the old stuff
        dataLists.clear();

        cancel();

        // Update all fields
        updateAll();
    }
    
    public SnomedConceptBrowser getAssociatedBrowser() {
        return browser;
    }

    public Concept getConcept() {
        return concept;
    }

    // Convenience methods
    public long getConceptId() {
        return getConcept().getId();
    }

    public String getConceptName() {
        return getConcept().getName();
    }

    // Returns the concepts in a field
    public Object getConceptList(Fields field) {
        return dataLists.get(field);
    }

    public void setAllDataEmpty() {
        for(Fields f : Fields.values()) {
            displayPanels.get(f).dataEmpty();
        }
    }

    public void updateAll() {

        // Notify listeners that the concept has changed
        for(BaseNavPanel panel : listeners) {
            panel.focusConceptChanged();
        }

        update(Fields.CONCEPT);

        update(Fields.CHILDREN);

        update(Fields.PARENTS);

        update(Fields.SYNONYMS);

        update(Fields.CONCEPTREL);

        update(Fields.SIBLINGS);

        update(Fields.PARTIALAREA);

        update(Fields.TRIBALAN);
        
        if (dataSource instanceof SCTLocalDataSource) {
            update(Fields.TRIBALAN);
            
            update(Fields.HIERARCHYMETRICS);

            update(Fields.ALLANCESTORS);

            update(Fields.ALLDESCENDANTS);

            update(Fields.ALLPATHS);

            if (dataSource.supportsStatedRelationships()) {
                update(Fields.STATEDPARENTS);

                update(Fields.STATEDCHILDREN);

                update(Fields.STATEDCONCEPTRELS);
                
                update(Fields.STATEDSIBLINGS);
                
                update(Fields.STATEDANCESTORS);
                
                update(Fields.STATEDDESCENDANTS);
                
                update(Fields.STATEDHIERARCHYMETRICS);
            }
        }       
    }

    // Updates the given field of the Focus Concept
    public void update(Fields field) {
        // Don't update a panel that has not yet been created.
        if(displayPanels.get(field) == null) {
            return;
        }

        // If we already have the requested data, update the corresponding
        // panel immediately.
        if(alreadyFilled.get(field)) {
            displayPanels.get(field).dataReady();
            return;
        }

        // Tell the corresponding panel that data is on the way.
        displayPanels.get(field).dataPending();

        UpdateThread thd = new UpdateThread(field);
        
        thd.start();
        
        updateThreads.add(thd);
    }

    // Cancels any query that is executing and clears the query queue.
    protected void cancel() {
        for(UpdateThread t : updateThreads) {
            t.cancel();
        }

        updateThreads.clear();
    }

    private class UpdateThread extends Thread {
        public Fields field;

        public Object result = null;
        
        private boolean cancelled = false;
        private boolean executing = true;

        public UpdateThread(Fields field) {
            this.field = field;
        }

        public void cancel() {
            cancelled = true;
        }

        public boolean isCancelled() {
            return cancelled;
        }

        public boolean isExecuting() {
            return executing;
        }

        // The thread entry function
        @Override
        public void run() {

            switch(field) {
                case PARENTS:
                    result = dataSource.getConceptParents(concept);
                    break;
                case CHILDREN:
                    result = dataSource.getConceptChildren(concept);
                    break;
                case SYNONYMS:
                    result = dataSource.getConceptSynoynms(concept);
                    break;
                case SIBLINGS:
                    result = dataSource.getConceptSiblings(concept);
                    break;
                case CONCEPTREL:
                    result = dataSource.getOutgoingLateralRelationships(concept);
                    break;
                case CONCEPT:
                    result = dataSource.getConceptFromId(concept.getId());
                    break;
                case PARTIALAREA:
                    result = dataSource.getSummaryOfPAreasContainingConcept(concept);
                    break;
                case TRIBALAN:
                    result = ((SCTLocalDataSource)dataSource).getSummaryOfClustersContainingConcept(concept);
                    break;
                    
                case HIERARCHYMETRICS:
                    result = ((SCTLocalDataSource)dataSource).getHierarchyMetrics(concept);
                    break;
                    
                case ALLANCESTORS:
                    result = ((SCTLocalDataSource)dataSource).getAllAncestorsAsList(concept);
                    break;
                    
                case ALLDESCENDANTS:
                    result = ((SCTLocalDataSource)dataSource).getAllDescendantsAsList(concept);
                    break;
                    
                case ALLPATHS:
                    result = ((SCTLocalDataSource)dataSource).getAllPathsToConcept(concept);
                    break;
                    
                case STATEDPARENTS:
                    result = ((SCTLocalDataSourceWithStated)dataSource).getStatedParents(concept);
                    break;
                    
                case STATEDCHILDREN:
                    result = ((SCTLocalDataSourceWithStated)dataSource).getStatedChildren(concept);
                    break;
                    
                case STATEDCONCEPTRELS:
                    result = ((LocalSCTConceptStated)concept).getStatedRelationships();
                    break;
                    
                case STATEDSIBLINGS:
                    result = ((SCTLocalDataSourceWithStated)dataSource).getStatedSiblings(concept);
                    break;
                    
                case STATEDANCESTORS:
                    result = ((SCTLocalDataSourceWithStated)dataSource).getStatedAncestors(concept);
                    break;
                    
                case STATEDDESCENDANTS:
                    result = ((SCTLocalDataSourceWithStated)dataSource).getStatedDescendants(concept);
                    break;
                    
                case STATEDHIERARCHYMETRICS:
                    result = ((SCTLocalDataSourceWithStated)dataSource).getStatedHierarchyMetrics(concept);
                    break;
            }

            // Send the notification to the main thread that we're done.
            try {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        executing = false;
                        finished(UpdateThread.this);
                    }
                });
            }
            catch(Exception e) {
            }
        }
    }

    // Executed by the main thread after an OracleThread finishes
    @SuppressWarnings("unchecked")
    private void finished(UpdateThread thread) {
        boolean badResult = false;

        // If the thread was cancelled, then this information is outdated.
        if(!thread.isCancelled()) {

            // Store the result of the thread
            switch(thread.field) {
                case CONCEPT:
                    if(thread.result == null) {
                        badResult = true;
                    }
                    else {
                        concept = (Concept)thread.result;
                    }

                    break;
                default:
                    dataLists.put(thread.field, thread.result);
                    break;
            }

            alreadyFilled.put(thread.field, true);

            if(!badResult) {
                // Inform the cooresponding panel that the data is ready.
                displayPanels.get(thread.field).dataReady();
            }
        }
    }
}
