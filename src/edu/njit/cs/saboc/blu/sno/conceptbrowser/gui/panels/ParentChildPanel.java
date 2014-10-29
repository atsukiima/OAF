/*******************************************************************************
 * $Id: ParentChildPanel.java,v 1.5 2014/06/14 22:12:03 uid57051 Exp $
 */
package edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.FocusConcept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.SnomedConceptBrowser;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.utils.filterable.entry.FilterableConceptEntry;
import edu.njit.cs.saboc.blu.sno.utils.filterable.list.SCTFilterableList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


/**
 * A class that displays the parents or children of the Focus Concept.  The
 * top middle and bottom middle NAT panels are instances of this class.
 */
public class ParentChildPanel extends BaseNavPanel implements ActionListener {
    private SCTFilterableList list;

    public enum PanelType {
        PARENT, CHILD
    }
    
    private PanelType type;
    private String normalBorderText;
    private String grandBorderText;
    private FocusConcept.Fields normalField;
    private FocusConcept.Fields grandField;

    public ParentChildPanel(final SnomedConceptBrowser mainPanel, PanelType panelType, SCTDataSource dataSource) {
        super(mainPanel, dataSource);
        
        this.list = new SCTFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions(), true, true);

        this.type = panelType;

        if(type == PanelType.PARENT) {
            normalField = FocusConcept.Fields.PARENTS;
            grandField = FocusConcept.Fields.GRANDPARENTS;
            normalBorderText = "PARENTS";
        }
        else {
            normalField = FocusConcept.Fields.CHILDREN;
            grandField = FocusConcept.Fields.GRANDCHILDREN;
            normalBorderText = "CHILDREN";
        }
        
        focusConcept.addDisplayPanel(normalField, this);
        focusConcept.addDisplayPanel(grandField, this);

        Color bgColor = mainPanel.getNeighborhoodBGColor();
        setBackground(bgColor);
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(normalBorderText));

        JButton filterButton = new JButton();
        filterButton.setBackground(mainPanel.getNeighborhoodBGColor());
        filterButton.setPreferredSize(new Dimension(24, 24));
        filterButton.setIcon(IconManager.getIconManager().getIcon("filter.png"));
        filterButton.setToolTipText("Filter these entries");
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                list.toggleFilterPanel();
            }
        });

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridBagLayout());
        northPanel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.weightx = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        northPanel.add(Box.createHorizontalBox(), c);
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.weightx = 0;
        northPanel.add(filterButton, c);

        add(northPanel, BorderLayout.NORTH);

        add(list, BorderLayout.CENTER);
    }

    @Override
    public void dataReady() {
        String borderText = normalBorderText;
        int count = 0;

        ArrayList<Filterable> conceptEntries = new ArrayList<Filterable>();

        @SuppressWarnings("unchecked")
        ArrayList<Concept> concepts = (ArrayList<Concept>) focusConcept.getConceptList(normalField);

        for (Concept c : concepts) {
            conceptEntries.add(new FilterableConceptEntry(c));
        }

        count = concepts.size();

        setBorder(BaseNavPanel.createConceptBorder(
                String.format("%s (%d)", borderText, count)));

        list.setContents(conceptEntries);
    }

    @Override
    public void dataPending() {
        setBorder(BaseNavPanel.createConceptBorder(normalBorderText));

        list.showPleaseWait();
    }

    public void dataEmpty() {
        setBorder(BaseNavPanel.createConceptBorder(normalBorderText));

        list.showDataEmpty();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
