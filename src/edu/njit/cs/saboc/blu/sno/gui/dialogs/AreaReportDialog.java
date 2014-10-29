package edu.njit.cs.saboc.blu.sno.gui.dialogs;

import SnomedShared.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.graph.PAreaBluGraph;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluArea;
import edu.njit.cs.saboc.blu.sno.gui.utils.models.AreaTableModel;
import edu.njit.cs.saboc.blu.sno.gui.utils.models.RelationshipRenderer;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Chris
 */
public class AreaReportDialog extends JDialog {
    public AreaReportDialog(final PAreaBluGraph graph) {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        final PAreaTaxonomy pareaTaxonomy = graph.getPAreaTaxonomy();

        final RelationshipRenderer relRenderer = new RelationshipRenderer();

        final JTable areaTable = new JTable(new AreaTableModel(pareaTaxonomy)) {

            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 0) {
                    return relRenderer;
                }

                return super.getCellRenderer(row, column);
            }
        };

        ListSelectionModel listSelectionModel = areaTable.getSelectionModel();

        listSelectionModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.isSelectionEmpty()) {
                    return;
                }

                if (areaTable.getRowSelectionAllowed()) {
                    int selectedIndex = e.getFirstIndex();

                    Area a = pareaTaxonomy.getHierarchyAreas().get(selectedIndex);
                    final BluArea bluArea = (BluArea)graph.getContainerEntries().get(a.getId());

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                graph.getParentInternalFrame().focusOnComponent(bluArea);
                                graph.validate();
                                graph.repaint();
                            } catch(Exception e) {
                                System.err.println(e);
                            }
                        }
                    });
                }
            }
        });

        areaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        areaTable.getColumnModel().getColumn(0).setPreferredWidth(256);

        dialogPanel.add(new JScrollPane(areaTable), BorderLayout.CENTER);

        this.add(dialogPanel);

        setResizable(true);
        setModal(true);
        setTitle("Area Report For: " + pareaTaxonomy.getSNOMEDHierarchyRoot() + " | Rooted At: " +
                pareaTaxonomy.getRootPArea().getRoot().getName());

        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
