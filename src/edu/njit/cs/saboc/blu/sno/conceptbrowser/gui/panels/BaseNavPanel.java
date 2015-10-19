/******************************************************************************
BaseNavPanel.java

Description:  This is a base class for the panels that display concepts
in the neighborhood of the focus concept; one (or two) steps from the
focus concept.

 ******************************************************************************/
package edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels;

import edu.njit.cs.saboc.blu.sno.conceptbrowser.FocusConcept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.SnomedConceptBrowser;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 * The base class for all NAT panels.
 */
public class BaseNavPanel extends JPanel {
    protected SCTDataSource dataSource;
    
    protected SnomedConceptBrowser mainPanel;
    protected FocusConcept focusConcept;
    protected Color baseColor = Color.darkGray;

    public BaseNavPanel(SnomedConceptBrowser mainPanel, SCTDataSource dataSource) {
        this.mainPanel = mainPanel;
        this.focusConcept = mainPanel.getFocusConcept();
        this.dataSource = dataSource;
    }

    public void focusConceptChanged() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void dataPending() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void dataReady() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void dataEmpty() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1, 1);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(1, 1);
    }

    // Creates a new line boarder with the given title
    public static TitledBorder createConceptBorder(String title){
        return (new TitledBorder(new LineBorder(Color.black, 1), title) {
            public Insets getBorderInsets(Component c, Insets insets) {
                super.getBorderInsets(c, insets);
                
                insets.top += 2;
                insets.left += 2;
                insets.right += 2;
                
                if(insets.top < 0) {
                    insets.top = 0;
                }
                
                return insets;
            }
        });
    }
}
