package edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.dialogs;

import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.NATPrinter;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.SnomedConceptBrowser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;


// Dialog for choosing what form of printing should be used. Prompts the user to
// print the NAT to a printer, a screenshot, or to a web page.
public class PrintSelectDialog extends JDialog implements ActionListener {
    private JButton printerBtn;
    private JButton screenshotBtn;

    private SnomedConceptBrowser mainPanel;

    public PrintSelectDialog(final SnomedConceptBrowser mainPanel) {
        this.mainPanel = mainPanel;

        this.setSize(256, 300);
        this.setModal(true);
        this.setTitle("Print Type Select");
        this.setLocationRelativeTo(mainPanel);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        initControls();

        this.setVisible(true);
    }

    private void initControls() {
        JPanel dialogPanel = new JPanel();

        dialogPanel.setBorder(new TitledBorder(new LineBorder(Color.black, 2),
                "Print Options"));

        Dimension buttonSize = new Dimension(200, 70);

        printerBtn = new JButton(" Printer ");
        printerBtn.setIcon(IconManager.getIconManager().getIcon("selectPrint.png"));
        printerBtn.setMinimumSize(buttonSize);
        printerBtn.setPreferredSize(buttonSize);
        printerBtn.setMaximumSize(buttonSize);
        printerBtn.addActionListener(this);
        printerBtn.setToolTipText("Print the Concept-centric browser on a Local or Network printer");

        screenshotBtn = new JButton(" File ");
        screenshotBtn.setIcon(IconManager.getIconManager().getIcon("selectScreenshot.png"));
        screenshotBtn.setMinimumSize(buttonSize);
        screenshotBtn.setPreferredSize(buttonSize);
        screenshotBtn.setMaximumSize(buttonSize);
        screenshotBtn.addActionListener(this);
        screenshotBtn.setToolTipText("Take a screenshot of the Concept-centric browser and save it to a JPEG file");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(printerBtn);
        buttonPanel.add(screenshotBtn);

        dialogPanel.add(buttonPanel);

        this.add(dialogPanel);
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == printerBtn) {
            Thread printThread = new Thread(new Runnable() {
                public void run() {
                    PrinterJob printerJob = PrinterJob.getPrinterJob();
                    printerJob.setPrintable(new NATPrinter(mainPanel));

                    boolean doPrint = printerJob.printDialog();

                    if(doPrint) {
                        try {
                            printerJob.print();
                        }
                        catch(PrinterException e) {
                            JOptionPane.showMessageDialog(mainPanel,
                                    "Printing unsucessful. Error while attempting to print.");
                        }
                    }
                    else {
                    }
                }
            });

            printThread.start();
            this.setVisible(false);
        }
        else if( ae.getSource() == screenshotBtn ) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Image Files", "png"));
            
            if( fileChooser.showDialog(mainPanel,
                    "Save Image") == JFileChooser.APPROVE_OPTION)
            {
                File file = fileChooser.getSelectedFile();

                if (file.getName().indexOf('.')==-1){
                    file = new File(file.getAbsolutePath() + ".png");
                }
                else{
                    int extIndex = file.getName().indexOf(".");

                    if( !file.getName().substring(extIndex + 1).equalsIgnoreCase("png") ){
                        JOptionPane.showMessageDialog(mainPanel,
                                "Error: You have entered an invalid file type. " +
                                "Screenshots must end in '.png'.");
                        return;
                    }
                }
                
                //SwingUtilities.invokeLater( new ScreenshotSaver(file) );
            }
            
            this.setVisible(false);
        }
    }
}
