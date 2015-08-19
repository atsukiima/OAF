package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Chris O
 */
public class SCTPAreaOptionsPanel extends AbstractNodeOptionsPanel<SCTPArea> {
    
    private Optional<SCTPArea> selectedPArea = Optional.empty();
    
    private final SCTPAreaTaxonomy taxonomy;
    
    private final SCTDisplayFrameListener displayListener;
    
    private final JButton btnNAT;
    private final JButton btnRootSubtaxonomy;

    public SCTPAreaOptionsPanel(SCTPAreaTaxonomy taxonomy, final SCTDisplayFrameListener displayListener) {
        
        this.setLayout(new BorderLayout());
        
        this.taxonomy = taxonomy;
        this.displayListener = displayListener;
        
        JPanel optionsPanel = new JPanel();
       
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
        
        this.btnNAT = this.createOptionButton("BluInvestigateRoot.png");
        this.btnRootSubtaxonomy = this.createOptionButton("BluSubtaxonomy.png");
        
        this.btnRootSubtaxonomy.addActionListener((ActionEvent ae) -> {
            if(selectedPArea.isPresent()) {
                SCTPAreaTaxonomy subtaxonomy = taxonomy.getRootSubtaxonomy(selectedPArea.get());
                
                displayListener.addNewPAreaGraphFrame(taxonomy, true, false);
            }
        });
        
        optionsPanel.add(Box.createHorizontalStrut(4));
        optionsPanel.add(btnNAT);
        optionsPanel.add(Box.createHorizontalStrut(4));
        optionsPanel.add(btnRootSubtaxonomy);
        optionsPanel.add(Box.createHorizontalStrut(4));
        optionsPanel.add(this.createOptionButton("BluExpandWindow.png"));
        optionsPanel.add(Box.createHorizontalStrut(4));
        optionsPanel.add(this.createOptionButton("BluExport.png"));
        optionsPanel.add(Box.createHorizontalStrut(4));
        optionsPanel.add(this.createOptionButton("BluHelp.png"));
        optionsPanel.setBackground(Color.WHITE);
        
        this.add(optionsPanel, BorderLayout.CENTER);
        
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Options"));
    }
    
    @Override
    public void enableOptionsForGroup(SCTPArea group) {
        
    }

    @Override
    public void setContents(SCTPArea group) {
        selectedPArea = Optional.of(group);
        
        this.enableOptionsForGroup(group);
    }
    
    public void clearContents() {
        selectedPArea = Optional.empty();
    }
    
    public void initUI() {
        
    }
    
    private JButton createOptionButton(String iconName) {
        ImageIcon icon = IconManager.getIconManager().getIcon(iconName);
        
        JButton btn = new JButton(icon);
        btn.setBackground(new Color(240, 240, 255));
        
        Dimension sizeDimension = new Dimension(50, 50);
        
        btn.setMinimumSize(sizeDimension);
        btn.setMaximumSize(sizeDimension);
        btn.setPreferredSize(sizeDimension);
        
        return btn;
    }

}
