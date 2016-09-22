package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport.EditingOperationType;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Chris O
 */
public class DescriptiveDeltaReportRenderer extends JPanel implements TableCellRenderer {
    
    public DescriptiveDeltaReportRenderer() {
        
    }
    
    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        doStyling(table, value, isSelected, hasFocus, row, column);
        
        this.removeAll();
        
        if (value != null) {
            EditingOperationReport eor = (EditingOperationReport) value;

            ArrayList<EditingOperationType> appliedEditingOperations = eor.getAppliedEditingOperationTypes();

            if (!appliedEditingOperations.isEmpty()) {
                final int MAX_COLUMNS = 4;

                int cols = Math.min(appliedEditingOperations.size(), MAX_COLUMNS);
                
                int rows = (appliedEditingOperations.size() / cols) + 1;

                final int GRID_SPACING = 4;
                
                this.setLayout(new GridLayout(0, cols, GRID_SPACING, GRID_SPACING));

                appliedEditingOperations.forEach((operation) -> {
                    this.add(getEditingOperationLabel(eor, operation));
                });

                int currentHeight = table.getRowHeight(row);
                
                int iconHeight = DescriptiveDeltaGUIUtils.getIconForEditingOperation(EditingOperationType.AddedParent).getIconHeight();

                if (row >= 0 && row < table.getRowCount()) {
                    table.setRowHeight(row, Math.max(rows * (iconHeight + GRID_SPACING), currentHeight));
                }
            } else {
                
            }
            
        } else {
            
        }

        return this;
    }
    
    private void doStyling(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        setFont(table.getFont());

        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));

            if (table.isCellEditable(row, column)) {
                setForeground(UIManager.getColor("Table.focusCellForeground"));
                setBackground(UIManager.getColor("Table.focusCellBackground"));
            }
        } else {
            setBorder(new EmptyBorder(1, 2, 1, 2));
        }
    }
    
    
    private JLabel getEditingOperationLabel(EditingOperationReport eor, EditingOperationType type) {
        ImageIcon icon = DescriptiveDeltaGUIUtils.getIconForEditingOperation(type);
        
        JLabel label = new JLabel();
        label.setIcon(icon);
        
        String text = String.format("%d", eor.getNumberOfOperationsForType(type));
        
        label.setText(text);
        
        return label;
    }
    
    
    
}
