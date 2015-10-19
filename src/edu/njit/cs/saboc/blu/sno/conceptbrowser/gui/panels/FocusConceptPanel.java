package edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels;

import SnomedShared.Concept;
import SnomedShared.SearchResult;
import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.FocusConcept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.History;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.Options;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.SnomedConceptBrowser;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.util.ArrayList;
import javax.swing.event.UndoableEditEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 * The center panel, which displays the information about the Focus
 * Concept.
 */
public class FocusConceptPanel extends BaseNavPanel {
    private UndoManager undoManager;
    private Document document;
    private JEditorPane jtf;
    boolean pending = false;

    private JButton backButton;
    private JButton forwardButton;
    private JButton homeButton;
    
    private JPanel editPanel;
    private JButton undoButton;
    private JButton redoButton;
    private JButton acceptButton;
    private JButton cancelButton;

    private History history;

    public FocusConceptPanel(final SnomedConceptBrowser mainPanel, SCTDataSource dataSource) {
        super(mainPanel, dataSource);

        Color bgColor = mainPanel.getNeighborhoodBGColor();
        setLayout(new BorderLayout());
        setBackground(bgColor);
        focusConcept.addDisplayPanel(FocusConcept.Fields.CONCEPT, this);

        history = focusConcept.getHistory();

        undoButton = new JButton();
        undoButton.setBackground(bgColor);
        undoButton.setPreferredSize(new Dimension(24, 24));
        undoButton.setMaximumSize(new Dimension(24, 24));
        undoButton.setAlignmentY(1);
        undoButton.setToolTipText("Undo Text");
        undoButton.setIcon(IconManager.getIconManager().getIcon("undo.png"));
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                try{
                    if(jtf.isEditable()){
                        undoManager.undo();
                    }
                }
                catch(CannotUndoException cue){

                }
            }
        });

        redoButton = new JButton();
        redoButton.setBackground(bgColor);
        redoButton.setPreferredSize(new Dimension(24, 24));
        redoButton.setMaximumSize(new Dimension(24, 24));
        redoButton.setAlignmentY(1);
        redoButton.setToolTipText("Redo Text");
        redoButton.setIcon(IconManager.getIconManager().getIcon("redo.png"));
        redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                try{
                    undoManager.redo();
                }
                catch(CannotRedoException cre){

                }
            }
        });

        acceptButton = new JButton();
        acceptButton.setBackground(bgColor);
        acceptButton.setPreferredSize(new Dimension(24, 24));
        acceptButton.setMaximumSize(new Dimension(24, 24));
        acceptButton.setAlignmentY(1);
        acceptButton.setToolTipText("Accept Change");
        acceptButton.setIcon(IconManager.getIconManager().getIcon("check.png"));
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                setConcept();
            }
        });

        cancelButton = new JButton();
        cancelButton.setBackground(bgColor);
        cancelButton.setPreferredSize(new Dimension(24, 24));
        cancelButton.setMaximumSize(new Dimension(24, 24));
        cancelButton.setAlignmentY(1);
        cancelButton.setToolTipText("Cancel Change");
        cancelButton.setIcon(IconManager.getIconManager().getIcon("cross.png"));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(!pending) {
                    editPanel.setVisible(false);
                    display();
                }
            }
        });

        mainPanel.getOptions().addOptionListener(new Options.Listener() {
            @Override
            public void optionsUpdated() {
                display();
            }
        });

        editPanel = new JPanel( new BorderLayout() );

        JPanel tempPanel = new JPanel();

        editPanel.setVisible(false);
        editPanel.setBackground(bgColor);
        tempPanel.setBackground(bgColor);
        tempPanel.add(undoButton);
        tempPanel.add(redoButton);
        tempPanel.add(acceptButton);
        tempPanel.add(cancelButton);

        editPanel.add(new JLabel("     Enter Concept Name or Concept ID:"),
                BorderLayout.WEST);
        editPanel.add(tempPanel, BorderLayout.EAST);

        JPanel fcp = new JPanel();
        fcp.setLayout(new BorderLayout());
        fcp.setBackground(bgColor);
        fcp.setBorder(new TitledBorder("FOCUS CONCEPT"));

        backButton = new JButton("Back");
        forwardButton = new JButton("Forward");
        homeButton = new JButton("Root");

        homeButton.setPreferredSize(new Dimension(300, 22));
        homeButton.addActionListener(
        		EventHandler.create(ActionListener.class, focusConcept, "navigateRoot"));
        
        backButton.setIcon(IconManager.getIconManager().getIcon("left-arrow.png"));
        backButton.setPreferredSize(new Dimension(128, 20));
        backButton.setBackground(mainPanel.getNeighborhoodBGColor());
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(history.getPosition() > 0) {
                    history.minusPosition();
                    focusConcept.navigate(history.getHistoryList().get(history.getPosition()));

                    forwardButton.setEnabled(true);

                    if(history.getPosition() == 0) {
                        backButton.setEnabled(false);
                    }
                }
            }
        });

        homeButton.setBackground(bgColor);

        forwardButton.setIcon(IconManager.getIconManager().getIcon("right-arrow.png"));
        forwardButton.setPreferredSize(new Dimension(128, 20));
        forwardButton.setBackground(mainPanel.getNeighborhoodBGColor());
        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(history.getPosition() < (history.getHistoryList().size() - 1)) {
                    history.plusPosition();
                    focusConcept.navigate(history.getHistoryList().get(history.getPosition()));

                    backButton.setEnabled(true);

                    if(history.getPosition() == history.getHistoryList().size() -1) {
                        forwardButton.setEnabled(false);
                    }
                }
            }
        });

        jtf = new JEditorPane() {
            @Override
            public String getToolTipText(MouseEvent evt) {
                if(mainPanel.getOptions().areToolTipsVisible() && !editPanel.isVisible()) {
                    Rectangle conceptRect = new Rectangle(jtf.getX(),
                            jtf.getY(),jtf.getWidth(),jtf.getPreferredSize().height);
                    if(!conceptRect.contains(evt.getPoint())) {
                        return null;
                    }

                    return focusConcept.getConceptName();
                }

                return null;
            }

            @Override
            public Point getToolTipLocation(MouseEvent evt) {
                if(getToolTipText(evt) == null) {
                    return null;
                }
                return new Point(evt.getX(), evt.getY() + 21);
            }
        };
        
        undoManager = new UndoManager() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                super.undoableEditHappened(e);
                updateUndoButtons();
            }

            @Override
            public void undo() {
                super.undo();
                updateUndoButtons();
                jtf.grabFocus();
            }

            @Override
            public void redo() {
                super.redo();
                updateUndoButtons();
                jtf.grabFocus();
            }
        };
        
        JScrollPane pane = new JScrollPane(jtf);
        ToolTipManager.sharedInstance().registerComponent(jtf);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel alignmentPanel = new JPanel(new BorderLayout());
        alignmentPanel.setBackground(mainPanel.getNeighborhoodBGColor());
        alignmentPanel.add(backButton, BorderLayout.WEST);
        alignmentPanel.add(forwardButton, BorderLayout.EAST);
        ImageIcon arrow = IconManager.getIconManager().getIcon("arrow.gif");

        JPanel centerPanel = new JPanel();
        centerPanel.add(homeButton);
        homeButton.setPreferredSize(new Dimension(80, 24));
        centerPanel.setBackground(new Color(0, true));
        alignmentPanel.add(centerPanel, BorderLayout.CENTER);

        fcp.add(editPanel, BorderLayout.NORTH);
        fcp.add(pane, BorderLayout.CENTER);

        add(alignmentPanel, BorderLayout.NORTH);
        add(fcp, BorderLayout.CENTER);
        add(new JLabel(arrow), BorderLayout.SOUTH);

        jtf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(!pending) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                        setConcept();
                        e.consume();
                    }
                    else if((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != InputEvent.CTRL_DOWN_MASK
                            && !e.isActionKey() && !jtf.isEditable()) {
                        openEditorPane();
                    }
                    //this happens before the default paste behavior, so the paste
                    //will occur into an editable area on its own
                    else if((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK
                            && e.getKeyCode() == KeyEvent.VK_V
                            && !jtf.isEditable()) {
                        openEditorPane();
                    }
                    else if((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK
                            && e.getKeyCode() == KeyEvent.VK_Z
                            && jtf.isEditable()) {
                        try{
                            undoManager.undo();
                        }
                        catch(CannotUndoException cue){

                        }
                    }
                    else if((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK
                            && e.getKeyCode() == KeyEvent.VK_Y
                            && jtf.isEditable()) {
                        try{
                            undoManager.redo();
                        }
                        catch(CannotRedoException cre){

                        }
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_ESCAPE
                            && jtf.isEditable()) {
                        if(!pending) {
                            editPanel.setVisible(false);
                            display();
                        }
                    }
                }
            }
        });

        jtf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!pending && e.getClickCount() == 2) {
                    if( !jtf.isEditable() ){
                        openEditorPane();
                    }
                    else{
                        editPanel.setVisible(false);
                        display();
                    }
                }
            }
        });
        jtf.addFocusListener(new FocusAdapter() {
        	@Override
        	public void focusLost(FocusEvent e) {
        		if (jtf.isEditable()) {
                    editPanel.setVisible(false);
                    display();
        		}
        	}
		});
    }

    private void setConcept(){
        document = null;
        if(jtf.isEditable()) {
            doConceptChange(jtf.getText());
        }

        editPanel.setVisible(false);
        display();
    }
    public void addHomeActionListener(ActionListener listener) {
        homeButton.addActionListener(listener);
    }

    public void display() {
        // When the focus concept is changed, hide the edit panel.
        editPanel.setVisible(false);
        document = null;

        jtf.setFont(jtf.getFont().deriveFont(Font.PLAIN));
        Options options = mainPanel.getOptions();

        Concept fc = focusConcept.getConcept();
        
        
        String conceptString = String.format("%s\n%d", fc.getName(), fc.getId());
        
        if (fc.primitiveSet()) {
            if (fc.isPrimitive()) {
                conceptString += "\nprimitive";
            }
        }

        jtf.setText(conceptString);

        if(options.areToolTipsVisible()) {
            jtf.setToolTipText(conceptString);
        }
        else {
            jtf.setToolTipText("");
        }

        jtf.setCaretPosition(0);
        jtf.getCaret().setVisible(false);
        jtf.setEditable(false);
        undoManager.discardAllEdits();
    }

    private void doConceptChange(String str) {
        
        try {
            long id = Long.parseLong(str);
            Concept c = dataSource.getConceptFromId(id);

            if( c == null ) {
                JOptionPane.showMessageDialog(
                    this, "The Concept with ConceptID '" + str + "' was not found.\n" +
                    "No Concept found with this ConceptID.",
                    "ConceptId Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            focusConcept.navigate(c);

            return;
        } catch (NumberFormatException nfe) {
            
        }

        ArrayList<SearchResult> results = dataSource.searchExact(str);

        if(results.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "The concept '" + str + "' was not found.\n" +
                    "You may want to try the Search Panel.",
                    "Concept Not Found", JOptionPane.ERROR_MESSAGE);
        }
        else if(results.size() == 1) {
            focusConcept.navigate(dataSource.getConceptFromId(
                    results.get(0).getConceptId()));
        }
        else {
            Object sel = JOptionPane.showInputDialog(this,
                    "'" + str + "' is a synonym of more than one Concept.\n" +
                    "Which concept did you mean?", "Search Ambiguity",
                    JOptionPane.QUESTION_MESSAGE, null, results.toArray(), null);

            if(sel != null) {
                focusConcept.navigate(dataSource.getConceptFromId(((SearchResult)sel).getConceptId()));
            }
        }
    }

    @Override
    public void dataPending() {
        editPanel.setEnabled(false);
    }

    public void openEditorPane() {
        jtf.setContentType("text/plain");
        jtf.setFont(jtf.getFont().deriveFont(Font.BOLD));
        jtf.setText(focusConcept.getConceptName());
        jtf.selectAll();
        jtf.setEditable(true);
        editPanel.setVisible(true);
        jtf.getCaret().setVisible(true);
        document = jtf.getDocument();
        document.addUndoableEditListener(undoManager);
        updateUndoButtons();
    }

    private void validateHistoryButtons() {
        backButton.setEnabled(history.getPosition() > 0);

        if(history.getPosition() > 0) {
            backButton.setEnabled(true);
            Concept prev = history.getHistoryList().get(history.getPosition() - 1);
            backButton.setToolTipText(
                    prev.getName());
        } else {
            backButton.setEnabled(false);
            backButton.setToolTipText(null);
        }
        
        if(history.getPosition() < (history.getHistoryList().size() - 1)) {
            forwardButton.setEnabled(true);
            Concept prev = history.getHistoryList().get(history.getPosition() + 1);
            forwardButton.setToolTipText(
                    prev.getName());
        } else {
            forwardButton.setEnabled(false);
            forwardButton.setToolTipText(null);
        }
    }

    @Override
    public void dataReady() {
        display();
        validateHistoryButtons();
    }

    public void dataEmpty() {
        jtf.setContentType("text/plain");
        editPanel.setVisible(false);
        jtf.setFont(jtf.getFont().deriveFont(Font.BOLD));
        jtf.setText("Please enter a valid concept.");
    }

    public void updateUndoButtons() {
        undoButton.setEnabled(undoManager.canUndo());
        redoButton.setEnabled(undoManager.canRedo());
    }
}
