package edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels;

import SnomedShared.Concept;
import SnomedShared.SearchResult;
import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.DefaultListModelEx;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.History;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.Options;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.SnomedConceptBrowser;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.utils.filterablelist.SCTNavigableFilterableList;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.utils.filterable.entry.FilterableSearchEntry;

import javax.swing.JOptionPane;
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
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 * The upper right panel of the NAT.  Contains tabs for the search bar, the
 * history list, and user-specified options.
 * 
 * NOTES: This code is more or less copied from the NAT, and is probably
 * from 2006.
 */
public class SearchPanel extends BaseNavPanel implements ActionListener {
    // Search Panel
    private JPanel pnlSearch;
    private JRadioButton optAnywhere;
    private JRadioButton optStarting;
    private JRadioButton optExact;
    private SpinnerTextField txtSearchBox;
    private JButton btnDoSearch;
    private JButton btnCancelSearch;
    private SCTNavigableFilterableList searchList;
    // History Panel
    private JPanel pnlHistory;
    private JButton btnBack;
    private JButton btnForward;
    private DefaultListModelEx mdlHistory;
    private JList lstHistory;
    
    // Options Panel
    private JPanel pnlOptions;
    private JCheckBox chkCUI;
    private JCheckBox chkToolTip;
    
    // Other stuff
    private History history;

    private Options options = mainPanel.getOptions();

    private volatile int searchID = 0;

    private Thread searchThread = null;

    // The special textbox with the spinner in it
    private class SpinnerTextField extends JPanel {
        private JTextField textField;
        private JLabel spinner;

        public SpinnerTextField() {
            super(new BorderLayout());
            JPanel p = new JPanel(new BorderLayout());
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

    private JCheckBox makeCheckBox(String text, Container panel, boolean sel, GridBagConstraints c) {
        JCheckBox cb = new JCheckBox(text);
        panel.add(cb, c);
        cb.setSelected(sel);
        cb.addActionListener(this);
        return cb;
    }

    // Construtor!
    public SearchPanel(final SnomedConceptBrowser mainPanel, SCTDataSource dataSource) {
        super(mainPanel, dataSource);

        this.history = mainPanel.getFocusConcept().getHistory();
        
        focusConcept.addFocusConceptListener(this);
        setLayout(new BorderLayout());

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

            @Override
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

        searchList = new SCTNavigableFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions());
        searchList.setFilterPanelOpen(false, null);

        pnlSearch.add(searchList, c);

        // History Panel
        JPanel historyPanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel();

        historyPanel.add(buttonPanel, BorderLayout.SOUTH);

        btnBack = new JButton("<< Previous");
        btnForward = new JButton("Next >>");
        btnBack.addActionListener(this);
        btnForward.addActionListener(this);
        buttonPanel.add(btnBack);
        buttonPanel.add(btnForward);

        pnlHistory = new JPanel(new BorderLayout());
        mdlHistory = new DefaultListModelEx();
        lstHistory = new JList(mdlHistory);

        lstHistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if(evt.getClickCount() == 2 && lstHistory.getSelectedIndex() >= 0) {
                    history.setPosition(history.getHistoryList().size() - 1
                            - lstHistory.getSelectedIndex());
                    focusConcept.navigate(history.getHistoryList().get(history.getPosition()));
                }
            }
        });

        lstHistory.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if(evt.getKeyCode() == KeyEvent.VK_ENTER && lstHistory.getSelectedIndex() >= 0) {
                    lstHistory.setSelectedIndex(lstHistory.getSelectedIndex());
                    history.setPosition(history.getHistoryList().size() - 1
                            - lstHistory.getSelectedIndex());
                    focusConcept.navigate(history.getHistoryList().get(history.getPosition()));
                }
            }
        });

        pnlHistory.add(historyPanel, BorderLayout.SOUTH);
        pnlHistory.add(new JScrollPane(lstHistory), BorderLayout.CENTER);


        // Glue
        JTabbedPane tpane = new JTabbedPane();
        tpane.addTab("Search", pnlSearch);
        tpane.addTab("History", pnlHistory);
        add(tpane, BorderLayout.CENTER);

        updateHistory();
    }

    @Override
    public void focusConceptChanged() {
        updateHistory();
    }

    public void updateHistory() {
        mdlHistory.clear();
        ArrayList<Concept> historyVector = history.getHistoryList();

        for(int i = historyVector.size() - 1; i >= 0; --i) {
            Concept c = historyVector.get(i);

            String cs = c.getName().replaceAll("<", "&lt;");

            String entry = String.format("<html> <FONT color=%s><b>%s</b></FONT>",
                    i == history.getPosition() ? "black" : "gray", cs);
            
            mdlHistory.addElement(entry);
        }
        
        updateHistoryButtons();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == btnBack) {
            if(history.getPosition() > 0) {
                history.minusPosition();
                focusConcept.navigate(history.getHistoryList().get(history.getPosition()));
            }
        }
        else if(ae.getSource() == btnForward) {
            if(history.getPosition() < (history.getHistoryList().size() - 1)) {
                history.plusPosition();
                focusConcept.navigate(history.getHistoryList().get(history.getPosition()));
            }
        }
        else if(ae.getSource() == btnDoSearch) {
            doSearch();
        }
        else if(ae.getSource() == btnCancelSearch) {
            if(searchThread == null) {
                return;
            }
            searchThread.interrupt();
        }
        else if(ae.getSource() == chkCUI) {
            options.setCUIsVisible(chkCUI.isSelected());
        }
    }

    private void doSearch() {
        String searchText = txtSearchBox.getTextField().getText().trim();

        if(searchText.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Cannot search for a blank string.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            long id = Long.parseLong(searchText);

            Concept c = dataSource.getConceptFromId(id);

            SearchResult sr = new SearchResult(c.getName(), c.getName(), c.getId());
            Vector<Filterable> entry = new Vector<Filterable>();
            entry.add(new FilterableSearchEntry(mainPanel.getOptions(), sr));
            searchList.setContents(entry);
            txtSearchBox.getTextField().selectAll();
            return;
        }
        catch(NumberFormatException e) {
        }

        searchList.setContents(new Vector<Filterable>());

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
            ArrayList<SearchResult> results = new ArrayList<SearchResult>();

            if(optExact.isSelected()) {
                results = dataSource.searchExact(term);
            }
            else if(optAnywhere.isSelected()) {
                results = dataSource.searchAnywhere(term);
            }
            else if(optStarting.isSelected()) {
                results = dataSource.searchStarting(term);
            }

            // Send one last one to indicate it's finished
            SwingUtilities.invokeLater(new ResultSender(results, id));
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

        @Override
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

        ArrayList<Filterable> searchEntries = new ArrayList<Filterable>();

        for(SearchResult sr : results) {
            searchEntries.add(new FilterableSearchEntry(mainPanel.getOptions(), sr));
        }

        searchList.setContents(searchEntries);

        txtSearchBox.setSpinnerVisible(false);
    }

    public void updateHistoryButtons() {
        btnBack.setEnabled(history.getPosition() > 0);
        btnForward.setEnabled(history.getPosition() < (history.getHistoryList().size() - 1));
    }
}
