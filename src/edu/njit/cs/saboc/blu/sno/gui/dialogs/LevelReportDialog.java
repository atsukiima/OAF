package edu.njit.cs.saboc.blu.sno.gui.dialogs;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.utils.models.ClusterLevelTableModel;
import edu.njit.cs.saboc.blu.sno.gui.utils.models.PAreaLevelTableModel;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Chris
 */
public class LevelReportDialog extends JDialog {

    public LevelReportDialog(final BluGraph graph) {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        final SCTAbstractionNetwork hierarchyData = (SCTAbstractionNetwork)graph.getAbstractionNetwork();
        
        JTable levelTable = null;

        if(hierarchyData instanceof SCTPAreaTaxonomy) {
            levelTable = new JTable(new PAreaLevelTableModel((SCTPAreaTaxonomy)hierarchyData));
        } else {
            levelTable = new JTable(new ClusterLevelTableModel((TribalAbstractionNetwork)hierarchyData));
        }

        levelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        dialogPanel.add(new JScrollPane(levelTable), BorderLayout.CENTER);

        this.add(dialogPanel);

        setResizable(true);
        setModal(true);

        if(hierarchyData instanceof SCTPAreaTaxonomy) {
            setTitle("Hierarchy Level Report For: " + hierarchyData.getSCTRootConcept() + " | Rooted At: "
                    + ((SCTPAreaTaxonomy)hierarchyData).getRootPArea().getRoot().getName());
        }

        setSize(600, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
