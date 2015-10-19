package edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels;

import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.SnomedConceptBrowser;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.dialogs.PrintSelectDialog;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The upper left panel of the NAT.  Displays the NJIT log as well as the
 * sources, print, help, and about buttons.
 */
public class LogoPanel extends BaseNavPanel implements ActionListener {
    private JPanel versionPanel;
    private JComboBox version;

    private JLabel njitLogoLabel;
    private JLabel printLabel;

    public LogoPanel(SnomedConceptBrowser mainPanel, SCTDataSource dataSource) {
        super(mainPanel, dataSource);
        
        setLayout(new BorderLayout(5, 5));

        JPanel centerPanel = new JPanel(new BorderLayout(0, 5));

        ArrayList<String> versionNames = dataSource.getSupportedVersions();

        String [] versionList = new String[versionNames.size()];

        for(int c = 0; c < versionNames.size(); c++) {
            versionList[c] = UtilityMethods.getPrintableVersionName(versionNames.get(c));
        }

        njitLogoLabel = new JLabel(IconManager.getIconManager().getIcon("combinednew.png"), 
                JLabel.CENTER);
        
        centerPanel.add(njitLogoLabel, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel(new BorderLayout(5, 5));
        
        eastPanel.add(new JLabel("                 "), BorderLayout.SOUTH);
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(1, 1, 0, 5));

        printLabel = new JLabel(IconManager.getIconManager().getIcon("printerS.png"));
        optionsPanel.add(printLabel);
        printLabel.setToolTipText("<html><b>Print Options</b></html>");

        eastPanel.add(optionsPanel, BorderLayout.EAST);
        
        optionsPanel.setPreferredSize(new Dimension(48,0));

        printLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                imagelabelMouseClicked(evt);
            }

            @Override
            public void mouseEntered(MouseEvent evt) {
                imagelabelMouseEntered(evt);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                imagelabelMouseExited(evt);
            }
        });

        add(eastPanel, BorderLayout.EAST);
        
        mainPanel.getOptions().setDataSource(dataSource);

        version = new JComboBox(versionList);

        version.setSelectedIndex(0);

        version.addActionListener(this);
        
        versionPanel = new JPanel(new BorderLayout(3, 0));
        
        JLabel sctVersionLabel = new JLabel("Using SNOMED version: ", JLabel.CENTER);
        sctVersionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sctVersionLabel.setForeground(Color.BLACK);
        
        versionPanel.add(sctVersionLabel, BorderLayout.WEST);
        versionPanel.add(version, BorderLayout.CENTER);
        
        if(dataSource.isLocalDataSource()) {
            versionPanel.setVisible(false);
        }
        
        centerPanel.add(versionPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent ae) {
        setSelectedVersion(version.getSelectedIndex());
        
        mainPanel.getFocusConcept().reloadCurrentConcept();
    }
    
    public void setSelectedVersion(int versionIndex) {
        dataSource.setVersion(dataSource.getSupportedVersions().get(versionIndex));
        mainPanel.getOptions().getDataSource().setVersion(dataSource.getSupportedVersions().get(versionIndex));
        
        version.setSelectedIndex(versionIndex);
    }
    
    public void setSelectedVersionByName(String versionName) {
        for(int c = 0; c < dataSource.getSupportedVersions().size(); c++) {
            if(dataSource.getSupportedVersions().get(c).equals(versionName)) {
                setSelectedVersion(c);
                return;
            }
        }
    }

    private void imagelabelMouseEntered(MouseEvent evt) {
        if(evt.getSource() == printLabel) {
            printLabel.setIcon(IconManager.getIconManager().getIcon("printer.png"));
            printLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    private void imagelabelMouseExited(MouseEvent evt) {
        if(evt.getSource() == printLabel) {
            printLabel.setIcon(IconManager.getIconManager().getIcon("printerS.png"));
        }
    }

    private void imagelabelMouseClicked(MouseEvent evt) {
        if(evt.getSource() == printLabel) {
            PrintSelectDialog psd = new PrintSelectDialog(mainPanel);
        }
    }

    public int getSelectedVersion() {
        return version.getSelectedIndex();
    }
}
