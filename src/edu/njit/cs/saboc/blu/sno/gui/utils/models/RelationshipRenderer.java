
package edu.njit.cs.saboc.blu.sno.gui.utils.models;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class RelationshipRenderer extends JTextArea implements TableCellRenderer {

    public RelationshipRenderer() {
        super();
    }

    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        table.setRowHeight(row, value.toString().split("\n").length * 16);

        setText(value.toString());

        return this;
    }
}
