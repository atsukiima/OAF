package edu.njit.cs.saboc.blu.sno.conceptbrowser;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.swing.RepaintManager;

// Class for printing the main NATtab frame. Is used when a PrintJob
// object is created after the "Print" button has been pressed.
public class NATPrinter implements Printable {

    private SnomedConceptBrowser mainPanel;

    public NATPrinter(SnomedConceptBrowser mainPanel) {
        
    }

    // Functions for drawing the the printer's graphics object. Draws the
    // NATtab to the printer.
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {

        // NAT is only one page
        if(page > 0) {
            return NO_SUCH_PAGE;
        }

        // Stop double buffering so we capture the entirety of the current
        // draw.
        RepaintManager repaintManager = RepaintManager.currentManager(mainPanel.getMainBrowserPanel());
        
        repaintManager.setDoubleBufferingEnabled(false);

        // Translate the Graphics object so it draws onto a printable area
        // of the page
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        int width = mainPanel.getMainBrowserPanel().getWidth();
        int height = mainPanel.getMainBrowserPanel().getHeight();

        // Create a new BufferedImage the size of the NATtab frame.
        BufferedImage bi = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_RGB);
        
        Graphics graphics = bi.getGraphics();

        // Paint the NATtab to BI's graphics object
        mainPanel.getMainBrowserPanel().printAll(graphics);

        double scale = pf.getImageableHeight() / bi.getWidth();

        AffineTransform transform = new AffineTransform();
        
        transform.scale(scale, scale);

        transform.rotate(Math.PI / 2, pf.getWidth() / 2,
                pf.getHeight() / 2);

        transform.translate((pf.getWidth() - (bi.getWidth() * scale)) / 2, 
                - (pf.getHeight() - bi.getHeight() * scale) / 2);

        g2d.drawRenderedImage(bi, transform);

        // Reenable double buffering
        repaintManager.setDoubleBufferingEnabled(true);

        // Page created successfully
        return PAGE_EXISTS;
    }
}
