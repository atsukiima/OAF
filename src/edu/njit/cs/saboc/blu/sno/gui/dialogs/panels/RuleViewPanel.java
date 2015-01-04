package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels;

import SnomedShared.Concept;
import SnomedShared.SearchResult;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.TextDrawingUtilities;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.ddirules.DDIDataLoader;
import edu.njit.cs.saboc.blu.sno.ddirules.RuleObject;
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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author azakharchenko
 */
public class RuleViewPanel extends JPanel {

    private GenericConceptGroup group;
    private SCTAbstractionNetwork ruleData;

    private HashMap<Concept, ArrayList<Concept>> inverseHierarchy;
    private HashMap<Concept, ArrayList<Concept>> hierarchy;

    private JTree ruletree;
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

            if (highlighted) {
                g2d.setPaint(Color.CYAN);
            } else if (filledAsParent) {
                g2d.setPaint(Color.BLUE);
            } else if (filledAsChild) {
                g2d.setPaint(Color.ORANGE);
            } else if (selected) {
                g2d.setPaint(Color.YELLOW);
            } else {

                if (concept.primitiveSet() && concept.isPrimitive()) {
                    g2d.setPaint(new Color(240, 94, 9));
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

            if (text.length() > 25) {
                text = text.substring(0, 25) + "...";
            }

            Font savedFont = g2d.getFont();

            g2d.setFont(new Font("Tahoma", Font.PLAIN, 11));

            TextDrawingUtilities.drawTextWithinBounds(g2d, text, new Rectangle(2, 2, bounds.width - 4, bounds.height - 4), new Rectangle(0, 0, bounds.width, bounds.width));

            g2d.setFont(savedFont);
        }
    }

    public RuleViewPanel(final GenericConceptGroup group, final SCTAbstractionNetwork ruleData, final JTree newruletree) {
        this.group = group;
        this.ruleData = ruleData;
        this.ruletree = newruletree;
        this.setLayout(null);
        this.setPreferredSize(new Dimension(this.getWidth(), 2000));

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                if (!initialized || loading) {
                    return;
                }

                ConceptEntry clickedEntry = getConceptEntryAt(e.getX(), e.getY());

                if (e.getClickCount() >= 1 && e.getButton() == MouseEvent.BUTTON1) {

                    for (ArrayList<ConceptEntry> level : conceptEntries) {
                        for (ConceptEntry entry : level) {
                            entry.selected = false;
                            entry.filledAsParent = false;
                            entry.filledAsChild = false;
                        }
                    }

                    if (clickedEntry != null) {

                        clickedEntry.selected = true;

                        Concept concept = clickedEntry.getConcept();

                        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(group.getRoot().getName(), true);
                        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
                        RuleObject localrule = DDIDataLoader.getRuleObject(group.getRoot().getName());
                        HashMap<String, String> localmap = localrule.getConceptlist();
                        DefaultMutableTreeNode localnode = new DefaultMutableTreeNode(concept.getName(), true);
                        String conceptcheck = concept.getName();
                        conceptcheck = conceptcheck.substring(0, conceptcheck.lastIndexOf("(")).trim();

                        if (conceptcheck.contains("observable")) {
                            conceptcheck = conceptcheck.substring(0, conceptcheck.lastIndexOf(" observable")).trim();
                        }
                        if (localmap.containsKey(conceptcheck)) {
                            localnode.add(new DefaultMutableTreeNode(localmap.get(conceptcheck)));
                        }
                        rootNode.add(localnode);
                        ruletree.setModel(treeModel);

                        ArrayList<Concept> parents = inverseHierarchy.get(concept);
                        ArrayList<Concept> children = hierarchy.get(concept);

                        if (parents != null) {
                            for (Concept parent : parents) {
                                conceptEntryMap.get(parent).filledAsParent = true;
                            }
                        }

                        if (children != null) {
                            for (Concept child : children) {
                                conceptEntryMap.get(child).filledAsChild = true;
                            }
                        }
                    } else {
                        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(group.getRoot().getName(), true);
                        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
                        RuleObject localrule = DDIDataLoader.getRuleObject(group.getRoot().getName());
                        HashMap<String, String> localmap = localrule.getConceptlist();

                        for (Map.Entry<String, String> localentry : localmap.entrySet()) {
                            DefaultMutableTreeNode localnode = new DefaultMutableTreeNode(localentry.getKey(), true);
                            localnode.add(new DefaultMutableTreeNode(localentry.getValue()));
                            rootNode.add(localnode);
                        }
                        ruletree.setModel(treeModel);
                    }
                }

                repaint();
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {

                if (!initialized || loading) {
                    return;
                }

                ConceptEntry mousedOverEntry = getConceptEntryAt(e.getX(), e.getY());

                for (ArrayList<ConceptEntry> level : conceptEntries) {
                    for (ConceptEntry entry : level) {
                        entry.highlighted = false;
                    }
                }

                if (mousedOverEntry != null) {
                    mousedOverEntry.highlighted = true;
                }

                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics bufferedGraphics = bi.getGraphics();

        bufferedGraphics.setColor(Color.WHITE);
        bufferedGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (!initialized) {
            bufferedGraphics.setColor(Color.BLACK);
            bufferedGraphics.setFont(new Font("Ariel", Font.BOLD, 18));
            bufferedGraphics.drawString("LOADING... PLEASE WAIT...", 200, 100);

            if (!loading) {
                new Thread(new RuleLoader()).start();
            }
        } else {
            final int startX = 20;

            final boolean isPartialAreaHierarchy = (group instanceof PAreaSummary);

            int xPos = startX;
            int yPos = 16;

            bufferedGraphics.setColor(Color.BLACK);
            bufferedGraphics.setFont(new Font("Ariel", Font.BOLD, 14));
            //rootNode.removeAllChildren();

            for (int l = 0; l < conceptEntries.size(); l++) {
                ((Graphics2D) (bufferedGraphics)).setTransform(AffineTransform.getTranslateInstance(0, 0));
                /* for(int u=0;u<conceptEntries.get(l).size();u++)
                 {
                 //System.out.println(conceptEntries.get(l).get(u).concept.getName());
                 String localres=DataLoader.getRule(conceptEntries.get(l).get(u).concept.getName());
                 if(localres!=null)
                 {
                 rootNode.add(new DefaultMutableTreeNode(localres));
                 System.out.println(localres);
                 }
                 }*/

                xPos = startX;
                String title = "";

                if (l == 0) {
                    title = "Rules associated with this concept";
                } else {
                    title += "_________________________________";
                }

                bufferedGraphics.drawString(title, xPos, yPos);

                yPos += 16;

                int fit = this.getWidth() / (ConceptEntry.CONCEPT_WIDTH + 8);
                int current = 0;

                int remaining = conceptEntries.get(l).size();

                for (ConceptEntry ce : conceptEntries.get(l)) {
                    System.out.println(l + ": " + ce.getConcept().getName());
                    ce.drawConceptAt((Graphics2D) bufferedGraphics, xPos, yPos);
                    xPos += (ConceptEntry.CONCEPT_WIDTH + 8);

                    current++;
                    remaining--;

                    if (current == fit) {
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
        for (ArrayList<ConceptEntry> level : conceptEntries) {
            for (ConceptEntry entry : level) {
                if (entry.currentBounds.contains(x, y)) {
                    return entry;
                }
            }
        }

        return null;
    }

    private class ConceptProxyQuery implements Callable {

        String version;
        long id;
        Entry entry;
        Concept localconcept;

        public ConceptProxyQuery(String version, Entry entry) {
            this.version = version;
            this.entry = entry;
        }

        public Object call() throws Exception {
            ArrayList<SearchResult> locallist = ruleData.getDataSource().searchAnywhere((String) entry.getKey());

            for (int l = 0; l < locallist.size(); l++) {
                SearchResult srcres = locallist.get(l);
                String entryPointName = srcres.getFullySpecifiedName().toLowerCase();
                entryPointName = entryPointName.substring(0, entryPointName.lastIndexOf("(")).trim();

                if (entryPointName.contains("observable")) {
                    entryPointName = entryPointName.substring(0, entryPointName.lastIndexOf(" observable")).trim();
                }
                if (entryPointName.equalsIgnoreCase((String) entry.getKey())) {
                    id = locallist.get(l).getConceptId();
                }
            }

            localconcept = ruleData.getDataSource().getConceptFromId(id);
            return localconcept;
        }

    }

    private class RuleLoader implements Runnable {

        public RuleLoader() {

        }

        public void run() {

            HashMap<Concept, ArrayList<Concept>> hierarchy = new HashMap<Concept, ArrayList<Concept>>();
            RuleObject localrule = DDIDataLoader.getRuleObject(group.getRoot().getName());
            long id = group.getRoot().getId();
            long hierarchyid = 0;
            ExecutorService executor = Executors.newFixedThreadPool(64);
            ArrayList<Future<Concept>> conceptlist = new ArrayList<Future<Concept>>();
            for (Map.Entry entry : localrule.getConceptlist().entrySet()) {

                /*ArrayList<SearchResult> locallist=MiddlewareAccessorProxy.getProxy().searchAnywhere(ruleData.getVersion(), (String)entry.getKey());
                 for(int l=0;l<locallist.size();l++)
                 {
                 SearchResult srcres=locallist.get(l);
                 String entryPointName = srcres.getFullySpecifiedName().toLowerCase();
                 entryPointName = entryPointName.substring(0, entryPointName.lastIndexOf("(")).trim();

                 if(entryPointName.contains("observable")) 
                 {
                 entryPointName = entryPointName.substring(0, entryPointName.lastIndexOf(" observable")).trim();
                 } 
                 if(entryPointName.equalsIgnoreCase((String) entry.getKey()))
                 {
                 id=locallist.get(l).getConceptId();
                 }
                 }
                 Concept localconcept=MiddlewareAccessorProxy.getProxy().getConceptFromId(ruleData.getVersion(), id);*/
                Callable callable = new ConceptProxyQuery(ruleData.getSCTVersion(), entry);
                conceptlist.add(executor.submit(callable));
            }

            for (Future<Concept> f : conceptlist) {
                /*ArrayList<Concept> parentlist=MiddlewareAccessorProxy.getProxy().getConceptParents(ruleData.getVersion(), localconcept);
                 System.out.println(parentlist.size());
                 System.out.println("The returned concept is " +localconcept.getName());*/
                try {
                    ArrayList<Concept> localconceptlist = new ArrayList<Concept>();
                    localconceptlist.add(f.get());
                    if (hierarchy.containsKey(group.getRoot())) {
                        hierarchy.get(group.getRoot()).add(f.get());
                    } else {
                        hierarchy.put(group.getRoot(), localconceptlist);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();
            /* if(group instanceof PAreaSummary) 
             {
             System.out.println("p-area");
             hierarchy = MiddlewareAccessorProxy.getProxy().getPAreaConceptHierarchy(ruleData.getVersion(),
             hierarchyid, id);
             System.out.println(hierarchy.size());
             } 
             else 
             {
             System.out.println("not p-area");
             hierarchy = MiddlewareAccessorProxy.getProxy().getClusterConceptHierarchy(ruleData.getVersion(),
             hierarchyid, id);
             System.out.println(hierarchy.size());
             }*/

            HashMap<Concept, ArrayList<Concept>> inverseHierarchy = new HashMap<Concept, ArrayList<Concept>>();

            Stack<Concept> stack = new Stack<Concept>();
            HashSet<Concept> processedConcepts = new HashSet<Concept>();

            stack.add(group.getRoot());

            while (!stack.isEmpty()) {
                Concept c = stack.pop();

                processedConcepts.add(c);

                ArrayList<Concept> children = hierarchy.get(c);

                if (children != null) {
                    for (Concept child : children) {
                        if (!stack.contains(child)) {
                            stack.push(child);
                        }

                        if (!inverseHierarchy.containsKey(child)) {
                            inverseHierarchy.put(child, new ArrayList<Concept>());
                        }

                        inverseHierarchy.get(child).add(c);
                    }
                }
            }

            RuleViewPanel.this.hierarchy = hierarchy;
            RuleViewPanel.this.inverseHierarchy = inverseHierarchy;

            HashSet<Concept> visitedConcepts = new HashSet<Concept>();

            ArrayList<ArrayList<Concept>> levels = new ArrayList<ArrayList<Concept>>();

            HashSet<Concept> levelConcepts = new HashSet<Concept>();

            levelConcepts.add(group.getRoot());

            while (!levelConcepts.isEmpty()) {

                levels.add(new ArrayList<Concept>(levelConcepts));

                HashSet<Concept> nextLevel = new HashSet<Concept>();

                for (Concept c : levelConcepts) {
                    ArrayList<Concept> conceptChildren = hierarchy.get(c);
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
            RuleViewPanel.this.conceptEntries.clear();
            for (ArrayList<Concept> level : levels) {
                Collections.sort(level, new Comparator<Concept>() {
                    public int compare(Concept a, Concept b) {
                        return a.getName().compareTo(b.getName());
                    }
                });

                ArrayList<ConceptEntry> conceptEntries = new ArrayList<ConceptEntry>();

                for (Concept c : level) {
                    ConceptEntry entry = new ConceptEntry(c);

                    conceptEntries.add(entry);
                    conceptEntryMap.put(c, entry);
                }

                RuleViewPanel.this.conceptEntries.add(conceptEntries);
            }

            loading = false;
            initialized = true;

            repaint();
        }
    }

}
