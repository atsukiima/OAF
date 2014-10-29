/*******************************************************************************
 * $Id: SynonymPanel.java,v 1.3 2014/06/14 22:12:03 uid57051 Exp $
 */
package edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels;

import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.FocusConcept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.SnomedConceptBrowser;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.utils.filterable.entry.FilterableSynonymEntry;
import edu.njit.cs.saboc.blu.sno.utils.filterable.list.SCTFilterableList;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;


/**
 * The middle left panel of the NAT.  Displays synonyms of the Focus Concept.
 */
public class SynonymPanel extends BaseNavPanel {
    private SCTFilterableList list;

    public SynonymPanel(final SnomedConceptBrowser mainPanel, SCTDataSource dataSource) {
        super(mainPanel, dataSource);

        list = new SCTFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions(), false, false);

        focusConcept.addDisplayPanel(FocusConcept.Fields.SYNONYMS, this);
        setBorder(BaseNavPanel.createConceptBorder("SYNONYMS"));
        
        setBackground(mainPanel.getNeighborhoodBGColor());
        setLayout(new BorderLayout());
        add(list, BorderLayout.CENTER);

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

        FlowLayout buttonLayout = new FlowLayout(FlowLayout.TRAILING);
        buttonLayout.setHgap(0);
        JPanel northPanel = new JPanel(buttonLayout);
        northPanel.setBackground(mainPanel.getNeighborhoodBGColor());
        northPanel.add(filterButton);
        northPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        add(northPanel, BorderLayout.NORTH);
    }

    @Override
    public void dataPending() {
        setBorder(BaseNavPanel.createConceptBorder("SYNONYMS"));
        list.showPleaseWait();
    }

    public void dataEmpty() {
        setBorder(BaseNavPanel.createConceptBorder("SYNONYMS"));
        list.showDataEmpty();
    }

    @Override
    public void dataReady() {
        ArrayList<String> synonyms = (ArrayList<String>)focusConcept.getConceptList(FocusConcept.Fields.SYNONYMS);
        
        ArrayList<Filterable> synonymEntries = new ArrayList<Filterable>();

        for(String s : synonyms) {
            synonymEntries.add(new FilterableSynonymEntry(s));
        }

        list.setContents(synonymEntries);
        
        setBorder(BaseNavPanel.createConceptBorder("SYNONYMS  (" +
                synonyms.size() +")"));
    }
}
