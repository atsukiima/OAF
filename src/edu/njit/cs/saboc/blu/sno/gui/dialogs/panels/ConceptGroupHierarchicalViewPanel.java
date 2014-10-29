package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels;

import SnomedShared.Concept;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.SingleRootedHierarchy;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.TextDrawingUtilities;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JPanel;


/**
 *
 * @author Chris
 */
public class ConceptGroupHierarchicalViewPanel extends JPanel{

    private GenericConceptGroup group;
    private SCTAbstractionNetwork abstractionNetwork;

    private SingleRootedHierarchy hierarchy;

    private boolean initialized = false;
    private boolean loading = false;

    private Rectangle currentBounds;
    
    private HashMap<Concept, ConceptEntry> conceptEntryMap = new HashMap<Concept, ConceptEntry>();

    private ArrayList<ArrayList<ConceptEntry>> conceptEntries = new ArrayList<ArrayList<ConceptEntry>>();

    private class ConceptEntry {

        public static final int CONCEPT_WIDTH = 100;
        public static final int CONCEPT_HEIGHT = 30;

        private Concept concept;
        
        private Rectangle currentBounds = new Rectangle();
        
        // Simplify with a single vaiable and a state enum
        private boolean filledAsParent = false;
        private boolean filledAsChild = false;
        
        private boolean highlighted = false;
        private boolean selected = false;

        public ConceptEntry(Concept concept) {
            this.concept = concept;
        }

        public Concept getConcept() {
            return concept;
        }

        public void drawConceptAt(Graphics2D g2d, int x, int y) {
            Rectangle bounds = new Rectangle(x, y, CONCEPT_WIDTH, CONCEPT_HEIGHT);
            currentBounds = bounds;
            
            AffineTransform transform = AffineTransform.getTranslateInstance(bounds.x, bounds.y);

            g2d.setTransform(transform);

            if(highlighted) {
                g2d.setPaint(Color.CYAN);
            } else if(filledAsParent) {
                g2d.setPaint(Color.BLUE);
            } else if(filledAsChild) {
                g2d.setPaint(Color.MAGENTA);
            } else if(selected) {
                g2d.setPaint(Color.YELLOW);
            } else {

                if (concept.primitiveSet() && concept.isPrimitive()) {
                    g2d.setPaint(new Color(128, 100, 128));
                } else {
                    g2d.setPaint(Color.LIGHT_GRAY);
                }
            }

            g2d.fillRect(0, 0, bounds.width, bounds.height);

            Stroke savedStroke = g2d.getStroke();

            g2d.setStroke(new BasicStroke(1));

            g2d.setPaint(Color.BLACK);
            
            g2d.drawRect(0, 0, bounds.width, bounds.height);
            
            g2d.setColor(Color.BLACK);

            g2d.setStroke(savedStroke);

            String text = concept.getName();

            text = text.substring(0, text.lastIndexOf("("));

            if(text.length() > 25) {
                text = text.substring(0, 25) + "...";
            }

            Font savedFont = g2d.getFont();

            g2d.setFont(new Font("Tahoma", Font.PLAIN, 11));

            TextDrawingUtilities.drawTextWithinBounds(g2d, text, 
                    new Rectangle(2, 2, bounds.width - 4, bounds.height - 4),
                    new Rectangle(0, 0, bounds.width, bounds.width));

            g2d.setFont(savedFont);
        }
    }
    
    public ConceptGroupHierarchicalViewPanel(final GenericConceptGroup group, 
            final SCTAbstractionNetwork abstractionNetwork) {
        
        this.group = group;
        this.abstractionNetwork = abstractionNetwork;
        this.setLayout(null);
        this.setPreferredSize(new Dimension(this.getWidth(), 2000));
        
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                if(!initialized || loading) {
                    return;
                }

                ConceptEntry clickedEntry = getConceptEntryAt(e.getX(), e.getY());

                if(e.getClickCount() >= 1 && e.getButton() == MouseEvent.BUTTON1) {

                    for (ArrayList<ConceptEntry> level : conceptEntries) {
                        for (ConceptEntry entry : level) {
                            entry.selected = false;
                            entry.filledAsParent = false;
                            entry.filledAsChild = false;
                        }
                    }

                    if(clickedEntry != null) {
                        if (e.getClickCount() >= 2) {
                            //MainToolFrame.getMainFrame().addNewBrowserFrame(clickedEntry.getConcept(), abstractionNetwork.getSCTDataSource());
                        }

                        clickedEntry.selected = true;

                        Concept concept = clickedEntry.getConcept();

                        HashSet<Concept> parents = hierarchy.getParents(concept);
                        HashSet<Concept> children = hierarchy.getChildren(concept);

                        if(parents != null) {
                            for(Concept parent : parents) {
                                conceptEntryMap.get(parent).filledAsParent = true;
                            }
                        }

                        if(children != null) {
                            for(Concept child : children) {
                                conceptEntryMap.get(child).filledAsChild = true;
                            }
                        }
                    }
                }

                repaint();
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                
                if(!initialized || loading) {
                    return;
                }

                ConceptEntry mousedOverEntry = getConceptEntryAt(e.getX(), e.getY());
                
                for (ArrayList<ConceptEntry> level : conceptEntries) {
                    for (ConceptEntry entry : level) {
                        entry.highlighted = false;
                    }
                }

                if(mousedOverEntry != null) {
                    mousedOverEntry.highlighted = true;
                }

                repaint();
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics bufferedGraphics = bi.getGraphics();

        bufferedGraphics.setColor(Color.WHITE);
        bufferedGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());

        if(!initialized) {
            bufferedGraphics.setColor(Color.BLACK);
            bufferedGraphics.setFont(new Font("Ariel", Font.BOLD, 18));
            bufferedGraphics.drawString("LOADING... PLEASE WAIT...", 200, 100);

            if(!loading) {
                new Thread(new ConceptGroupHierarchyLoader()).start();
            }
        } else {
            final int startX = 20;

            final boolean isPartialAreaHierarchy = (group instanceof PAreaSummary);

            int xPos = startX;
            int yPos = 16;
            
            bufferedGraphics.setColor(Color.BLACK);
            bufferedGraphics.setFont(new Font("Ariel", Font.BOLD, 14));

            for(int l = 0; l < conceptEntries.size(); l++) {
                ((Graphics2D)(bufferedGraphics)).setTransform(AffineTransform.getTranslateInstance(0, 0));
                
                xPos = startX;
                String title = "";

                if(l == 0) {
                    if(isPartialAreaHierarchy) {
                        title = "Partial-area Root Concept (NOTE: Shortest path distance is used for calculation)";
                    } else {
                        title = "Cluster Root Concept";
                    }
                } else if (l == 1) {
                    title = "Children of Root Concept";
                }
                else {
                    for(int i = 2; i < l; i++) {
                        title += "Great-";
                    }

                    if(isPartialAreaHierarchy) {
                        title += "Grandchildren of Root Concept";
                    } else {
                        title += "Grandchildren of Root Concept";
                    } 
                }

                bufferedGraphics.drawString(title, xPos, yPos);

                yPos += 16;

                int fit = this.getWidth() / (ConceptEntry.CONCEPT_WIDTH + 8);
                int current = 0;

                int remaining = conceptEntries.get(l).size();

                for(ConceptEntry ce : conceptEntries.get(l)) {
                    ce.drawConceptAt((Graphics2D)bufferedGraphics, xPos, yPos);
                    xPos += (ConceptEntry.CONCEPT_WIDTH + 8);

                    current++;
                    remaining--;

                    if(current == fit) {
                        xPos = startX;
                        yPos += (ConceptEntry.CONCEPT_HEIGHT + 8);
                        current = 0;
                    }
                }

                xPos = startX;
                yPos += ConceptEntry.CONCEPT_HEIGHT + 50;
            }
        }

        g.drawImage(bi, 0, 0, null);
    }
    
    private ConceptEntry getConceptEntryAt(int x, int y) {
        for(ArrayList<ConceptEntry> level : conceptEntries) {
            for(ConceptEntry entry : level) {
                if(entry.currentBounds.contains(x, y)) {
                    return entry;
                }
            }
        }

        return null;
    }

    private class ConceptGroupHierarchyLoader implements Runnable {

        public ConceptGroupHierarchyLoader() {

        }

        public void run() {

            SCTConceptHierarchy hierarchy;

            if (group instanceof PAreaSummary) {
                hierarchy = abstractionNetwork.getSCTDataSource().getPAreaConceptHierarchy(
                        (PAreaTaxonomy)abstractionNetwork, (PAreaSummary)group);
            } else {
                hierarchy = abstractionNetwork.getSCTDataSource().getClusterConceptHierarchy(
                        (TribalAbstractionNetwork)abstractionNetwork, (ClusterSummary)group);
            }
            
            ConceptGroupHierarchicalViewPanel.this.hierarchy = hierarchy;

            HashSet<Concept> visitedConcepts = new HashSet<Concept>();

            ArrayList<ArrayList<Concept>> levels = new ArrayList<ArrayList<Concept>>();

            HashSet<Concept> levelConcepts = new HashSet<Concept>();
            
            levelConcepts.add(group.getRoot());

            while(!levelConcepts.isEmpty()) {

                levels.add(new ArrayList<Concept>(levelConcepts));

                HashSet<Concept> nextLevel = new HashSet<Concept>();

                for(Concept c : levelConcepts) {
                    HashSet<Concept> conceptChildren = hierarchy.getChildren(c);
                    visitedConcepts.add(c);

                    if (conceptChildren != null) {
                        for (Concept child : conceptChildren) {
                            if (!visitedConcepts.contains(child)) {
                                nextLevel.add(child);
                            }
                        }
                    }
                }

                levelConcepts = nextLevel;
            }

            for(ArrayList<Concept> level : levels) {
                Collections.sort(level, new Comparator<Concept>() {
                    public int compare(Concept a, Concept b) {
                        return a.getName().compareTo(b.getName());
                    }
                });

                ArrayList<ConceptEntry> conceptEntries = new ArrayList<ConceptEntry>();

                for(Concept c : level) {
                    ConceptEntry entry = new ConceptEntry(c);
                    
                    conceptEntries.add(entry);
                    conceptEntryMap.put(c, entry);
                }

                ConceptGroupHierarchicalViewPanel.this.conceptEntries.add(conceptEntries);
            }

            loading = false;
            initialized = true;

            repaint();
        }
    }    
}
