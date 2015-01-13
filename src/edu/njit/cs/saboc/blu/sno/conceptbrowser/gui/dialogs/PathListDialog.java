package edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.dialogs;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.SnomedConceptBrowser;
import edu.njit.cs.saboc.blu.sno.utils.filterable.entry.FilterablePathConceptEntry;
import edu.njit.cs.saboc.blu.sno.utils.filterable.list.SCTFilterableList;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JDialog;

/**
 *
 * @author Chris O
 */
public class PathListDialog extends JDialog {
    
    public static void show(final SnomedConceptBrowser mainPanel, final ArrayList<Concept> path) {
        new PathListDialog(mainPanel, path);
    }
    
    private PathListDialog(final SnomedConceptBrowser mainPanel, final ArrayList<Concept> path) {
        super(mainPanel.getParentFrame());
        
        this.setTitle("Path to: "+ path.get(path.size() - 1).getName());
        this.setLocationRelativeTo(mainPanel);
        
        this.setSize(600, 250);
        
        this.setLayout(new BorderLayout());
        
        final SCTFilterableList pathList = new SCTFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions(), true, true);
        
        ArrayList<FilterablePathConceptEntry> pathConceptEntries = new ArrayList<FilterablePathConceptEntry>();
        
        for(Concept concept : path) {
            pathConceptEntries.add(new FilterablePathConceptEntry(concept));
        }
        
        pathList.setContents(pathConceptEntries);
        
        this.add(pathList, BorderLayout.CENTER);
        
        this.setVisible(true);
        
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
