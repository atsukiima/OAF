package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import SnomedShared.Concept;
import SnomedShared.SearchResult;
import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 *
 * @author Chris
 */
public class SCTConceptSearchPanel extends JPanel implements ActionListener {

    // Search Panel
    private JPanel pnlSearch;
    private JRadioButton optAnywhere;
    private JRadioButton optStarting;
    private JRadioButton optExact;
    
    private SpinnerTextField txtSearchBox;
    private JButton btnDoSearch;
    private JButton btnCancelSearch;
    
    private JList searchList;
    
    private volatile int searchID = 0;

    private Thread searchThread = null;
    
    private ArrayList<SearchResult> searchResults = new ArrayList<SearchResult>();
    
    private SCTConceptSearchActions searchActions;

    // The special textbox with the spinner in it
    private class SpinnerTextField extends JPanel {
        private JTextField textField;
        private JLabel spinner;

        public SpinnerTextField() {
            super(new BorderLayout());
            
            textField = new JTextField();
            spinner = new JLabel(IconManager.getIconManager().getIcon("spinner.gif"));
            spinner.setOpaque(true);
            spinner.setBackground(textField.getBackground());
            
            setBorder(textField.getBorder());
            
            textField.setBorder(null);
            
            add(spinner, BorderLayout.EAST);
            add(textField, BorderLayout.CENTER);

            spinner.setVisible(false);
        }

        public JTextField getTextField() {
            return textField;
        }

        public void setSpinnerVisible(boolean v) {
            spinner.setVisible(v);
        }
    }

    // Convenience methods for control creation
    private JRadioButton makeRadioButton(String text, ButtonGroup group, Container panel) {
        return makeRadioButton(text, group, panel, null);
    }

    private JRadioButton makeRadioButton(String text, ButtonGroup group, Container panel, GridBagConstraints c) {
        JRadioButton rb = new JRadioButton(text);
        group.add(rb);
        if(c == null) {
            panel.add(rb);
        }
        else {
            panel.add(rb, c);
        }
        
        rb.addActionListener(this);
        return rb;
    }
    
    public SCTConceptSearchPanel(final SCTConceptSearchActions searchActions) {
        this.setLayout(new BorderLayout());

        this.searchActions = searchActions;

        // Search Panel
        JPanel buttonPanel = new JPanel();
        ButtonGroup group = new ButtonGroup();
        optStarting = makeRadioButton("Starts with", group, buttonPanel);
        optStarting.setSelected(true);
        
        optAnywhere = makeRadioButton("Anywhere", group, buttonPanel);
        optExact = makeRadioButton("Exact", group, buttonPanel);
        
        pnlSearch = new JPanel(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        pnlSearch.add(buttonPanel, c);

        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        c.gridwidth = 1;
        txtSearchBox = new SpinnerTextField();
        pnlSearch.add(txtSearchBox, c);
        txtSearchBox.getTextField().addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doSearch();
                }
            }
        });

        c.gridx = 1;
        c.weightx = 0;
        btnDoSearch = new JButton(IconManager.getIconManager().getIcon("search.png"));
        btnDoSearch.addActionListener(this);
        pnlSearch.add(btnDoSearch, c);

        btnCancelSearch = new JButton(IconManager.getIconManager().getIcon("cancel.png"));
        btnCancelSearch.setToolTipText("Cancel serach");
        btnCancelSearch.addActionListener(this);
        
        pnlSearch.add(btnCancelSearch, c);
        btnCancelSearch.setVisible(false);
        btnCancelSearch.setEnabled(false);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        searchList = new JList();
        
        searchList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = searchList.getSelectedIndex();
                
                if(e.getClickCount() == 2 && selectedIndex >= 0) {
                    SearchResult selectedResult = searchResults.get(selectedIndex);
                    searchActions.doAction(searchActions.getDataSource().getConceptFromId(selectedResult.getConceptId()));
                }
            }
        });

        pnlSearch.add(new JScrollPane(searchList), c);
        
        add(pnlSearch);
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == btnDoSearch) {
            doSearch();
        } else if(ae.getSource() == btnCancelSearch) {
            if(searchThread == null) {
                return;
            }
            
            searchThread.interrupt();
        }
    }

    private void doSearch() {
        String searchText = txtSearchBox.getTextField().getText().trim();

        if(searchText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a search term.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            long id = Long.parseLong(searchText);

            Concept c = searchActions.getDataSource().getConceptFromId(id);

            SearchResult sr = new SearchResult(c.getName(), c.getName(), c.getId());
            Vector<String> entry = new Vector<String>();
            
            entry.add(String.format("%s {%s} (%d)", sr.getTerm(), sr.getFullySpecifiedName(), sr.getConceptId()));
            
            searchResults = new ArrayList<SearchResult>(Arrays.asList(sr));
            
            searchList.setListData(entry);
            txtSearchBox.getTextField().selectAll();
            
            return;
        } catch(NumberFormatException e) {
            
        }

        searchList.setListData(new Vector<String>());

        // Gray everything out, set up the spinner.
        txtSearchBox.getTextField().setEnabled(false);
        
        btnDoSearch.setEnabled(false);
        btnDoSearch.setVisible(false);
        
        btnCancelSearch.setEnabled(true);
        btnCancelSearch.setVisible(true);
        
        optAnywhere.setEnabled(false);
        optExact.setEnabled(false);
        optStarting.setEnabled(false);
        
        txtSearchBox.setSpinnerVisible(true);

        // Interupt the old search thread if it's still going
        if(searchThread != null) {
            searchThread.interrupt();
        }

        // Create a new thread and get the results.
        searchThread = new Thread(new SearchRunner(searchText, ++searchID));
        searchThread.start();
    }

    private class SearchRunner implements Runnable {
        private String term;
        int id;

        public SearchRunner(String term, int id) {
            this.term = term;
            this.id = id;
        }

        @Override
        public void run() {
            SCTDataSource dataSource = searchActions.getDataSource();
            
            if(optExact.isSelected()) {
                searchResults = dataSource.searchExact(term);
            }
            else if(optAnywhere.isSelected()) {
                searchResults = dataSource.searchAnywhere(term);
            }
            else if(optStarting.isSelected()) {
                searchResults = dataSource.searchStarting(term);
            } else {
                searchResults = new ArrayList<SearchResult>();
            }

            // Send one last one to indicate it's finished
            SwingUtilities.invokeLater(new ResultSender(searchResults, id));
        }
    };

    // This will be used to send the results back through the main thread
    private class ResultSender implements Runnable {
        private ArrayList<SearchResult> results;
        private int id;

        public ResultSender(ArrayList<SearchResult> results, int id) {
            this.results = results;
            this.id = id;
        }

        public void run() {
            displaySearchResults(results, id);
        }
    }

    private void displaySearchResults(ArrayList<SearchResult> results, int id) {
        if(id != searchID) {
            return;
        }
        
        if(!btnDoSearch.isEnabled()) {
            txtSearchBox.getTextField().setEnabled(true);
            
            btnCancelSearch.setEnabled(false);
            btnCancelSearch.setVisible(false);
            
            btnDoSearch.setEnabled(true);
            btnDoSearch.setVisible(true);
            
            optAnywhere.setEnabled(true);
            optExact.setEnabled(true);
            optStarting.setEnabled(true);
        }

        Vector<String> searchEntries = new Vector<String>();

        for(SearchResult sr : results) {
            searchEntries.add(String.format("%s {%s} (%d)", sr.getTerm(), sr.getFullySpecifiedName(), sr.getConceptId()));
        }

        searchList.setListData(searchEntries);

        txtSearchBox.setSpinnerVisible(false);
    }
}
