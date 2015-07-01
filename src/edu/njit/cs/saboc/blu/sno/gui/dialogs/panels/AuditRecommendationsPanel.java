
package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels;

import SnomedShared.Concept;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.generic.GenericContainerPartition;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.properties.AuditReportProperties;
import edu.njit.cs.saboc.blu.sno.utils.BrowserLauncher;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JEditorPane;


/**
 * Generates Audit recommendations for Concepts that are likely to be erroneous and
 * displays them in descending order of the probability of error.
 * @author harsh
 */
public class AuditRecommendationsPanel extends JEditorPane {
//    JEditorPane textPane = new JEditorPane();
    public AuditRecommendationsPanel thisPanel = this;
    
    GenericContainerPartition partition;
    SCTAbstractionNetwork abstractionNetwork;
    
    HashMap<Long, GenericConceptGroup> rootIdMap;
    HashMap<Long, ArrayList<Concept>> concepts;
    HashMap<Concept, ArrayList<Long>> overlappingConcepts;
    
    StringBuilder builder;
    
    boolean initialized = false;
    boolean loading = false;
    
    public AuditRecommendationsPanel(GenericContainerPartition partition, SCTAbstractionNetwork hierarchyData,
            HashMap<Long, GenericConceptGroup> rootIdMap, HashMap<Long, ArrayList<Concept>> concepts,
            HashMap<Concept, ArrayList<Long>> overlappingConcepts, SCTDisplayFrameListener displayFrameListener) {
        
        this.partition = partition;
        this.abstractionNetwork = hierarchyData;
        
        this.rootIdMap = rootIdMap;
        this.concepts = concepts;
        this.overlappingConcepts = overlappingConcepts;
        builder = new StringBuilder();
        
        this.setLayout(null);
        this.setPreferredSize(new Dimension(this.getWidth(), 3000));
        
        this.setEditable(false);
        this.setContentType("text/html");
        this.addHyperlinkListener(new BrowserLauncher(displayFrameListener, hierarchyData));
        
        if (!initialized) {
            this.setText("<html> <font size=4 face=\"Arial\"> <b> LOADING... PLEASE WAIT... </b> </font> </html>");
            this.setEditable(false);

            if (!loading) {
                loading = true;
                new Thread(new AuditRecommendationsPanel.LoadErroneousConcepts()).start();
            }
        } 
        
        if (initialized) {
            this.setText(builder.toString());
            this.setEditable(false);
        }
        
        this.setSelectionStart(0);
        this.setSelectionEnd(0);
        
    }
    
    
    private class LoadErroneousConcepts implements Runnable {
        
        public LoadErroneousConcepts() {
            
        }
        
        public void run() {
            
            HashMap<String, ArrayList<Concept>> erroneousConcepts = new HashMap<String, ArrayList<Concept>>();
            HashMap<Integer, HashMap<String, Boolean>> errorCriteriaSatisfied = new HashMap<Integer, HashMap<String, Boolean>>();
            HashMap<String, HashMap<String, ArrayList<Long>>> regions = new HashMap<String, HashMap<String, ArrayList<Long>>>();  // <error description, <region name, Concept Ids>>

            String[] errorDescription = new String[15];
            
            String strictInheritanceRegionString = "Strict Inheritance Region";
            String mixedRegionString = "Mixed Region";
            String strictIntroductionRegionString = "Strict Introduction Region";

            errorCriteriaSatisfied.put(0, new HashMap<String, Boolean>());
            errorCriteriaSatisfied.put(1, new HashMap<String, Boolean>());
            errorCriteriaSatisfied.put(2, new HashMap<String, Boolean>());
            errorCriteriaSatisfied.put(3, new HashMap<String, Boolean>());
            
            boolean smallGroup, overlappingConcept, smallGroupManyRelationships, fewSmallPartialAreas;      // each for a criteria
            smallGroup = false;
            overlappingConcept = false;
            smallGroupManyRelationships = false;
            fewSmallPartialAreas = false;

            errorCriteriaSatisfied.get(0).put("Small Group", smallGroup);
            errorCriteriaSatisfied.get(1).put("Overlap", overlappingConcept);
            errorCriteriaSatisfied.get(2).put("Small Group Many Relationships", smallGroupManyRelationships);
            errorCriteriaSatisfied.get(3).put("Few Small Partial Areas", fewSmallPartialAreas);

            boolean manyRelationships = (((PAreaSummary)partition.getGroups().get(0)).getRelationships().size() > AuditReportProperties.getAuditReportProperties().getManyRelationships());

            int countSmallGroup = 0;
            for(GenericConceptGroup group : (ArrayList<GenericConceptGroup>)partition.getGroups()) {
                if (group.getConceptCount() <= AuditReportProperties.getAuditReportProperties().getSmallPArea()) {
                    countSmallGroup++;
                }
            }
            if(countSmallGroup <= AuditReportProperties.getAuditReportProperties().getFewSmallPAreas()) {
                fewSmallPartialAreas = true;
            }
            else {
                fewSmallPartialAreas = false;
            }        

            // Create all combinations of the 4 error criteria
            int total = errorCriteriaSatisfied.size();
            int pick = total;
            ArrayList<ArrayList<Integer>> allCombinations = new ArrayList<ArrayList<Integer>>();

            searchCombinations(total, pick, allCombinations, 1);
            searchCombinations(total, --pick, allCombinations, 4);
            searchCombinations(total, --pick, allCombinations, 6);
            searchCombinations(total, --pick, allCombinations, 4);

            int countCombinations = 0;
            for(ArrayList<Integer> comb : allCombinations) {
                StringBuilder errorString = new StringBuilder();
                Boolean oneError = true;

                for(int i : comb) {
                    if(oneError == true) {
                        for(String key : errorCriteriaSatisfied.get(i).keySet()) {
                            errorString.append(key);
                        }
                        oneError = false;
                    }
                    else {
                        errorString.append(", ");
                        for(String key : errorCriteriaSatisfied.get(i).keySet()) {
                            errorString.append(key);
                        }
                    }
                }

                errorDescription[countCombinations] = errorString.toString();
                erroneousConcepts.put(errorString.toString(), new ArrayList<Concept>());
                
                regions.put(errorString.toString(), new HashMap<String, ArrayList<Long>>());
                regions.get(errorString.toString()).put(strictInheritanceRegionString, new ArrayList<Long>());
                regions.get(errorString.toString()).put(mixedRegionString, new ArrayList<Long>());
                regions.get(errorString.toString()).put(strictIntroductionRegionString, new ArrayList<Long>());

                countCombinations++;
            }

            // For each Concept, determine the error probability
            for(Concept c : overlappingConcepts.keySet()) {
                smallGroup = false;
                smallGroupManyRelationships = false;
                overlappingConcept = (overlappingConcepts.get(c).size() > 1);

                // check if it belongs to small group and/or have many relationships
                for(Long id : overlappingConcepts.get(c)) {
                    if(concepts.get(id).size() <= AuditReportProperties.getAuditReportProperties().getSmallPArea()) {
                        smallGroup = true;
                        if(manyRelationships == true) {
                            smallGroupManyRelationships = true;
                        }
                        break;
                    }
                }

                boolean hasInherited;
                boolean hasIntroduced;

                boolean strictInheritance = false;
                boolean strictIntroduction = false;
                boolean mixture = false;

                for(Long id : overlappingConcepts.get(c)) {
                    hasInherited = false;
                    hasIntroduced = false;

                    ArrayList<InheritedRelationship> relsList = ((PAreaSummary)rootIdMap.get(id)).getRelationships();

//                    System.out.println(c.getName());
                    for(InheritedRelationship rel : relsList) {
//                        System.out.println(rel.getInheritanceType());
                        if(rel.getInheritanceType() == InheritedRelationship.InheritanceType.INHERITED) {
                            hasInherited = true;
                        }
                        else {
                            hasIntroduced = true;
                        }
                    }

                    if(hasInherited == true && hasIntroduced == false) {
                        strictInheritance = true;
                    }
                    else if(hasInherited == false && hasIntroduced == true) {
                        strictIntroduction = true;
                    }
                    else if(hasInherited == true && hasIntroduced == true) {
                        mixture = true;
                    }
                }

                

                errorCriteriaSatisfied.get(0).put("Small Group", smallGroup);
                errorCriteriaSatisfied.get(1).put("Overlap", overlappingConcept);
                errorCriteriaSatisfied.get(2).put("Small Group Many Relationships", smallGroupManyRelationships);
                errorCriteriaSatisfied.get(3).put("Few Small Partial Areas", fewSmallPartialAreas);

                StringBuilder errorString = new StringBuilder();
                int countTrue;

                for(ArrayList<Integer> comb : allCombinations) {
                    errorString = new StringBuilder();
                    Boolean oneError = true;
                    countTrue = 0;

                    for(int i : comb) {
                        if(errorCriteriaSatisfied.get(i).values().contains(true)) {
                            if(oneError == true) {
                                for(String key : errorCriteriaSatisfied.get(i).keySet()) {
                                    errorString.append(key);
                                }
                                oneError = false;
                            }
                            else {
                                errorString.append(", ");
                                for(String key : errorCriteriaSatisfied.get(i).keySet()) {
                                    errorString.append(key);
                                }
                            }

                            countTrue++;
                        }
                    }
                    if(countTrue == comb.size()) {
                        erroneousConcepts.get(errorString.toString()).add(c);
                        break;    
                    }
                }

                if(smallGroup == true && strictInheritance == true && mixture == false && strictIntroduction == false) {
                    regions.get(errorString.toString()).get(strictInheritanceRegionString).add(c.getId());
                }
                else if(smallGroup == true && mixture == true) {
                    regions.get(errorString.toString()).get(mixedRegionString).add(c.getId());
                }
                else if(smallGroup == true && strictInheritance == false && mixture == false && strictIntroduction == true) {
                    regions.get(errorString.toString()).get(strictIntroductionRegionString).add(c.getId());
                }
            }


            builder.append("<html>");
            builder.append("<font size=4 face=\"Arial\">");

            for(String conceptsInOneLevel : errorDescription) {

                if(erroneousConcepts.get(conceptsInOneLevel).size() > 0) {
                    builder.append(String.format("<b> <font size=5> %s </font> </b>", conceptsInOneLevel));
                    builder.append("&nbsp");
                    builder.append(String.format("<font size=5> (%s) </font> <br>", erroneousConcepts.get(conceptsInOneLevel).size()));
                }

                // find the range to which this concept belongs to
                int range = 0;
                for(int i=0; i < 15; i++) {
                    if(conceptsInOneLevel.equals(errorDescription[i])) {
                        range = i;
                    }
                }

                String color;
                color = chooseColor(range);
                
                if(AuditReportProperties.getAuditReportProperties().getStrictRegionsProperty() == false) {  // Regions not selected
                    if(AuditReportProperties.getAuditReportProperties().getPrimitivesLast() == false) {     // Primitives not selected
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {
                            builder.append(printSuspectConcept(c, color, overlappingConcepts, rootIdMap));
                        }
                        
                        if(erroneousConcepts.get(conceptsInOneLevel).size() > 0) {
                            builder.append("<br>");
                        }
                    }
                    else {                                                                                  // Primitives selected, Regions not selected
                        int countPrimitives = 0;
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {
                            if(c.isPrimitive()) {
                                countPrimitives++;
                            }
                        }

                        if((erroneousConcepts.get(conceptsInOneLevel).size() - countPrimitives) > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<i> Fully defined Concepts (%s) </i> <br>", (erroneousConcepts.get(conceptsInOneLevel).size() - countPrimitives)));
                        }
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {        // NON-Primitives first
                            if(!c.isPrimitive()) {
                                builder.append(printSuspectConcept(c, color, overlappingConcepts, rootIdMap));
                            }
                        }

                        if(countPrimitives > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<i> Primitive Concepts (%s) </i> <br>", countPrimitives));
                        }
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {        // Primitives later
                            if(c.isPrimitive()) {
                                builder.append(printSuspectConcept(c, color, overlappingConcepts, rootIdMap));
                            }
                        }

                        if(erroneousConcepts.get(conceptsInOneLevel).size() > 0) {
                            builder.append("<br>");
                        }
                    }
                }
                else {                                                                                      // Regions selected
                    if(AuditReportProperties.getAuditReportProperties().getPrimitivesLast() == false) {     // Primitives not selected
                        
                     // Strict Inheritance Region
                        if(regions.get(conceptsInOneLevel).get(strictInheritanceRegionString).size() > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<b> %s (%s) </b> <br>", strictInheritanceRegionString, regions.get(conceptsInOneLevel).get(strictInheritanceRegionString).size()));
                        }
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {
                            if(regions.get(conceptsInOneLevel).get(strictInheritanceRegionString).contains(c.getId())) {                            
                                builder.append(printSuspectConcept(c, color, overlappingConcepts, rootIdMap));
                            }
                        }
                        
                     // Mixed Region
                        if(regions.get(conceptsInOneLevel).get(mixedRegionString).size() > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<b> %s (%s) </b> <br>",mixedRegionString, regions.get(conceptsInOneLevel).get(mixedRegionString).size()));
                        }
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {
                            if(regions.get(conceptsInOneLevel).get(mixedRegionString).contains(c.getId())) {                            
                                builder.append(printSuspectConcept(c, color, overlappingConcepts, rootIdMap));
                            }
                        }
                        
                     // Strict Introduction Region
                        if(regions.get(conceptsInOneLevel).get(strictIntroductionRegionString).size() > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<b> %s (%s) </b> <br>", strictIntroductionRegionString, regions.get(conceptsInOneLevel).get(strictIntroductionRegionString).size()));
                        }
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {
                            if(regions.get(conceptsInOneLevel).get(strictIntroductionRegionString).contains(c.getId())) {
                                builder.append(printSuspectConcept(c, color, overlappingConcepts, rootIdMap));
                            }
                        }
                        
                        if(erroneousConcepts.get(conceptsInOneLevel).size() > 0) {
                            builder.append("<br>");
                        }
                    }
                    else {                                                                                   // Primitives selected, Regions selected
                        int countPrimitives = 0;
                        int countNonPrimitives = 0;
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {
                            if(c.isPrimitive() && regions.get(conceptsInOneLevel).get(strictInheritanceRegionString).contains(c.getId())) {
                                countPrimitives++;
                            }
                        }
                        countNonPrimitives = regions.get(conceptsInOneLevel).get(strictInheritanceRegionString).size() - countPrimitives;
                        
                     // Strict Inheritance Region  & Primitives
                        if(regions.get(conceptsInOneLevel).get(strictInheritanceRegionString).size() > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<b> %s (%s) </b> <br>", strictInheritanceRegionString, regions.get(conceptsInOneLevel).get(strictInheritanceRegionString).size()));
                        }

                        if(countNonPrimitives > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<i> Fully defined Concepts (%s) </i> <br>", countNonPrimitives));
                        }
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {                                    // NON-Primitives first
                            if(regions.get(conceptsInOneLevel).get(strictInheritanceRegionString).contains(c.getId()) &&
                                    !c.isPrimitive()) {
                                builder.append(" &nbsp&nbsp&nbsp ");
                                builder.append(printSuspectConcept(c, color, overlappingConcepts, rootIdMap));
                            }
                        }
                        if(countPrimitives > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<i> Primitive Concepts (%s) </i> <br>", countPrimitives));
                        }
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {                                    // Primitives later
                            if(regions.get(conceptsInOneLevel).get(strictInheritanceRegionString).contains(c.getId()) &&
                                    c.isPrimitive()) {
                                builder.append(" &nbsp&nbsp&nbsp ");
                                builder.append(printSuspectConcept(c, color, overlappingConcepts, rootIdMap));
                            }
                        }
                        
                     // Mixed Region  & Primitives
                        countPrimitives = 0;
                        countNonPrimitives = 0;
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {
                            if(c.isPrimitive() && regions.get(conceptsInOneLevel).get(mixedRegionString).contains(c.getId())) {
                                countPrimitives++;
                            }
                        }
                        countNonPrimitives = regions.get(conceptsInOneLevel).get(mixedRegionString).size() - countPrimitives;
                        
                        if(regions.get(conceptsInOneLevel).get(mixedRegionString).size() > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<b> %s (%s) </b> <br>", mixedRegionString, regions.get(conceptsInOneLevel).get(mixedRegionString).size()));
                        }
                        if(countNonPrimitives > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<i> Fully defined Concepts (%s) </i> <br>", countNonPrimitives));
                        }
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {                                    // NON-Primitives first
                            if(regions.get(conceptsInOneLevel).get(mixedRegionString).contains(c.getId()) &&
                                    !c.isPrimitive()) {
                                builder.append(" &nbsp&nbsp&nbsp ");
                                builder.append(printSuspectConcept(c, color, overlappingConcepts, rootIdMap));
                            }
                        }
                        if(countPrimitives > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<i> Primitive Concepts (%s) </i> <br>", countPrimitives));
                        }
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {                                    // Primitives later
                            if(regions.get(conceptsInOneLevel).get(mixedRegionString).contains(c.getId()) &&
                                    c.isPrimitive()) {
                                builder.append(" &nbsp&nbsp&nbsp ");
                                builder.append(printSuspectConcept(c, color, overlappingConcepts, rootIdMap));
                            }
                        }
                        
                     // Strict Introduction Region  & Primitives
                        countPrimitives = 0;
                        countNonPrimitives = 0;
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {
                            if(c.isPrimitive() && regions.get(conceptsInOneLevel).get(strictIntroductionRegionString).contains(c.getId())) {
                                countPrimitives++;
                            }
                        }
                        countNonPrimitives = regions.get(conceptsInOneLevel).get(strictIntroductionRegionString).size() - countPrimitives;

                        if(regions.get(conceptsInOneLevel).get(strictIntroductionRegionString).size() > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<b> %s (%s) </b> <br>", strictIntroductionRegionString, regions.get(conceptsInOneLevel).get(strictIntroductionRegionString).size()));
                        }
                        if(countNonPrimitives > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<i> Fully defined Concepts (%s) </i> <br>", countNonPrimitives));
                        }
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {                                    // NON-Primitives first
                            if(regions.get(conceptsInOneLevel).get(strictIntroductionRegionString).contains(c.getId()) &&
                                    !c.isPrimitive()) {
                                builder.append(" &nbsp&nbsp&nbsp ");
                                builder.append(printSuspectConcept(c, color, overlappingConcepts, rootIdMap));
                            }
                        }
                        if(countPrimitives > 0) {
                            builder.append("<br> &nbsp&nbsp&nbsp &nbsp&nbsp&nbsp ");
                            builder.append(String.format("<i> Primitive Concepts (%s) </i> <br>", countPrimitives));
                        }
                        for(Concept c : erroneousConcepts.get(conceptsInOneLevel)) {                                    // Primitives later
                            if(regions.get(conceptsInOneLevel).get(strictIntroductionRegionString).contains(c.getId()) &&
                                    c.isPrimitive()) {
                                builder.append(" &nbsp&nbsp&nbsp ");
                                builder.append(printSuspectConcept(c, color, overlappingConcepts, rootIdMap));
                            }
                        }
                        
                        if(erroneousConcepts.get(conceptsInOneLevel).size() > 0) {
                            builder.append("<br>");
                        }
                    }
                }
            }

            loading = false;
            initialized = true;
            
            thisPanel.setText(builder.toString());
            
            repaint();
        }
    }
    
    /**
     * Print detailed Concept info based on its probability of being erroneous.
     * @param c
     * @param color
     * @param overlappingConcepts
     * @param rootIdMap 
     * @return String
     */
    public String printSuspectConcept(Concept c, String color,
            HashMap<Concept, ArrayList<Long>> overlappingConcepts, HashMap<Long, GenericConceptGroup> rootIdMap) {

        StringBuilder smallBuilder = new StringBuilder();

        smallBuilder.append("&nbsp&nbsp&nbsp");
        smallBuilder.append(String.format("<b> <font color=\"%s\"> %s </font> </b>", color, c.getName()));
        if (c.isPrimitive()) {
            smallBuilder.append(" <b><font color ='purple'>[primitive]</font></b> ");
        }
        smallBuilder.append(String.format("&nbsp <b>(<a href=\"%d\">%d</a>)</b>", c.getId(), c.getId()));

        String pareaName = "";
        int counter = overlappingConcepts.get(c).size();
        for (long id : overlappingConcepts.get(c)) {
            GenericConceptGroup currentGroup = rootIdMap.get(id);
            pareaName += currentGroup.getRoot().getName();

            counter--;
            if (counter != 0) {
                pareaName += ", ";
            }
        }
        smallBuilder.append("&nbsp");
        smallBuilder.append(String.format(" in <font color=\"#848484\"> %s </font> <br>", pareaName));

        return smallBuilder.toString();
    }

    /**
     * Collect all combinations of r from n
     * @param n
     * @param r
     * @param allCombinations
     * @param totalCombinations : stores the number of possible combinations for the given pair of (n, r)
     */
    public void searchCombinations(int n, int r, ArrayList<ArrayList<Integer>> allCombinations, int totalCombinations) {
        ArrayList<Integer> currentCombination = new ArrayList<Integer>();
        
        for(int i=0; i < r; i++) {
            currentCombination.add(i);
        }

        allCombinations.add(new ArrayList<Integer>(currentCombination));    // store the first combination

        for(int i=1; i < totalCombinations; i++) {  // for the remaining combinations
            int j = r - 1;
            int max_value = n - 1;
            
            while(j >= 0 && currentCombination.get(j) == max_value) {   // find the rightmost element that is not at the max-value of that position
                j--;
                max_value--;
            }
            
            if(j >= 0) {
                int previousValue = currentCombination.get(j);
                currentCombination.remove(j);
                currentCombination.add(j, previousValue + 1);

                for(int k = j+1; k < r; k++) {
                    currentCombination.remove(k);
                    currentCombination.add(k, currentCombination.get(k-1) + 1);
                }
            }

            allCombinations.add(new ArrayList<Integer>(currentCombination));
        }
    }
    
    /**
     * Determines color, of Concepts to be printed, based on probability of error
     * @param givenRange
     * @return 
     */
    public String chooseColor(int givenRange) {
        String color = "";
        
        if(givenRange == 0) {
            color = "#DF0101";
        }
        else if(givenRange >= 1 && givenRange <=4) {
            color = "#FF0000";
        }
        else if(givenRange >= 5 && givenRange <= 10) {
            color = "#FE2E2E";
        }
        else if(givenRange >= 11 && givenRange <= 14) {
            color = "#FA5858";
        }
        
        return color;
    }
}
