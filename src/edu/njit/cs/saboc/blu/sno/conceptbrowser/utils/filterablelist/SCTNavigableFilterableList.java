package edu.njit.cs.saboc.blu.sno.conceptbrowser.utils.filterablelist;

import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.FocusConcept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.Options;
import edu.njit.cs.saboc.blu.sno.utils.filterable.entry.NavigableEntry;
import edu.njit.cs.saboc.blu.sno.utils.filterable.list.SCTFilterableList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Chris O
 */
public class SCTNavigableFilterableList extends SCTFilterableList {
    
    public SCTNavigableFilterableList(final FocusConcept focusConcept, final Options options) {
        super(focusConcept, options, true, true);
        
        this.addListMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && list.getModel() == entryModel) {
                    Filterable f = entryModel.getFilterableAtModelIndex(getSelectedIndex());

                    if (f != null && f instanceof NavigableEntry) {
                        NavigableEntry nav = (NavigableEntry) f;

                        focusConcept.navigate(nav.getNavigateConcept());
                    }
                }
            }
        });
    }
    
}
