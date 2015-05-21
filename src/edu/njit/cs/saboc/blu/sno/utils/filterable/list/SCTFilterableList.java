package edu.njit.cs.saboc.blu.sno.utils.filterable.list;

import edu.njit.cs.saboc.blu.core.utils.filterable.list.FilterableList;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.FocusConcept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.Options;
import javax.swing.JList;
import javax.swing.JPanel;


/**
 * A {@link JPanel} that includes a {@link JList} along with a filter bar that
 * pops up when a key is typed.  It is backed by {@link ConceptListModel}.
 */
public class SCTFilterableList extends FilterableList {
    
    private Options options;

    public SCTFilterableList(final FocusConcept focusConcept, final Options options, final boolean navigable, final boolean CUIsValid) {
        this.options = options;

               
    }
}
