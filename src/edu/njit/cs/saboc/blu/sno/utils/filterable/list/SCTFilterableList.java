package edu.njit.cs.saboc.blu.sno.utils.filterable.list;

import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.FilterableListModel;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.FocusConcept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.Options;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * A {@link JPanel} that includes a {@link JList} along with a filter bar that
 * pops up when a key is typed.  It is backed by {@link ConceptListModel}.
 */
public class SCTFilterableList extends JPanel {
    
    private JTextField filterField = new JTextField();
    private JButton closeButton = new JButton();
    private DefaultListModel pleaseWaitModel = new DefaultListModel();
    private DefaultListModel dataEmptyModel = new DefaultListModel();
    protected FilterableListModel conceptModel;
    protected JList list;
    private JPanel filterPanel = new JPanel();
    
    private Options options;

    public SCTFilterableList(final FocusConcept focusConcept, final Options options, final boolean navigable, final boolean CUIsValid) {
        this.options = options;

        setLayout(new BorderLayout());

        conceptModel = new FilterableListModel(
                CUIsValid && options.areCUIsVisible());

        options.addOptionListener(new Options.Listener() {
            @Override
            public void optionsUpdated() {
                if(CUIsValid) {
                    conceptModel.setConceptIdsVisible(options.areCUIsVisible());
                }
            }
        });

        options.addPropertyChangeListener("CUIsVisible", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(!evt.getOldValue().equals(evt.getNewValue())) {
                    conceptModel.setConceptIdsVisible((Boolean)evt.getNewValue());
                }
            }
        });

        list = new JList() {
            // This method is called as the cursor moves within the list.
            @Override
            public String getToolTipText(MouseEvent evt) {
                if(getModel() != conceptModel) {
                    return null;
                }

                if(!options.areToolTipsVisible()) {
                    return null;
                }

                int index = locationToIndex(evt.getPoint());

                if(getCellBounds(index, index) == null
                        || !getCellBounds(index, index).contains(evt.getPoint())) {
                    return null;
                }

                if(index > -1) {
                    return getModel().getElementAt(index).toString();
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
        
        list.setModel(conceptModel);
        pleaseWaitModel.addElement("Please wait...");
        dataEmptyModel.addElement(" ");

        if(navigable) {
            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    if(evt.getClickCount() == 2 && list.getModel() == conceptModel) {
                        Filterable f = conceptModel.getFilterableAtModelIndex(
                                ((JList)evt.getComponent()).getSelectedIndex());

                        if(f.getNavigableConcept() != null) {
                            focusConcept.navigate(f.getNavigableConcept());
                        }
                    }
                }
            });
        }

        list.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() != KeyEvent.CHAR_UNDEFINED
                        && (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != KeyEvent.CTRL_DOWN_MASK) {
                    if(!filterPanel.isVisible()) {
                        if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                            Filterable f = conceptModel.getFilterableAtModelIndex(list.getSelectedIndex());

                            if(f.getNavigableConcept() != null) {
                                focusConcept.navigate(f.getNavigableConcept());
                            }
                        }
                    }

                    if(!filterPanel.isVisible()) { // Panel is closed
                        setFilterPanelOpen(true, e);
                    }
                    else { // Panel is open, return focus to it
                        filterField.setText(filterField.getText() + e.getKeyChar());
                        filterField.requestFocus();
                    }
                }
            }
        });

        // Sets list members to fixed size for faster refresh.
        // For more info read Yakup's journal on 08 august 07.
        list.setFixedCellHeight(17);
        list.setFixedCellWidth(1536); // Larger arbitary number to fix wrapping issue - Chris 8/18/09
        JScrollPane scrollpane = new JScrollPane(list);
        add(scrollpane, BorderLayout.CENTER);

        closeButton.setIcon(IconManager.getIconManager().getIcon("cross.png"));
        closeButton.setToolTipText("Close");

        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.add(closeButton);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(new JLabel("Filter:  "));
        filterPanel.add(filterField);

        add(filterPanel, BorderLayout.SOUTH);

        filterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setFilterPanelOpen(false, null);
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFilterPanelOpen(false, null);
            }
        });

        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                conceptModel.changeFilter(filterField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                conceptModel.changeFilter(filterField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                conceptModel.changeFilter(filterField.getText());
            }
        });
    }

    public void showPleaseWait() {
        list.setModel(pleaseWaitModel);
        conceptModel.changeFilter("");
        filterPanel.setVisible(false);
    }

    public void showDataEmpty() {
        list.setModel(dataEmptyModel);
        conceptModel.changeFilter("");
        filterPanel.setVisible(false);
    }

    public void setContents(Collection<? extends Filterable> content) {
        conceptModel.changeFilter("");
        filterPanel.setVisible(false);
        conceptModel.clear();
        conceptModel.addAll(content);
        list.setModel(conceptModel);
    }

    public void setCUIsVisible(boolean CUIsVisible) {
        conceptModel.setConceptIdsVisible(CUIsVisible);
    }

    /* opens (open = true) or closes the filter panel */
    public void toggleFilterPanel() {
        if(!filterPanel.isVisible()) {
            setFilterPanelOpen(true, null);
        }
        else {
            setFilterPanelOpen(false, null);
        }
    }

    /*opens the filter panell and uses a KeyEvent if openned by typing */
    public void setFilterPanelOpen(boolean open, KeyEvent e) {
        if(open) {
            if(!filterPanel.isVisible()) {
                filterPanel.setVisible(true);
                if(e != null) {
                    filterField.setText("" + e.getKeyChar());
                }
                else {
                    filterField.setText("");
                }
                filterField.requestFocus();
            }
        }
        else {
            conceptModel.changeFilter("");
            filterPanel.setVisible(false);
            list.grabFocus();
        }
    }
}
