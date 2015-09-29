package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.core.abn.OverlappingConceptResult;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.NavigateToGroupListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.ParentGroupSelectedListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.PAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.SCTDisjointPAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.SCTPAreaTaxonomyGEPConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayConceptBrowserListener;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyConfiguration extends PAreaTaxonomyConfiguration<
        Concept, 
        SCTPArea, 
        SCTArea, 
        SCTPAreaTaxonomy, 
        DisjointPartialArea, 
        InheritedRelationship,
        SCTConceptHierarchy,
        DisjointPAreaTaxonomy,
        SCTAggregatePArea> {

    private final SCTPAreaTaxonomy taxonomy;
    
    private final SCTDisplayFrameListener displayListener;
    
    private final SCTPAreaTaxonomyGEPConfiguration gepConfig;
    
    // When working with subject subtaxonomies in large hierarchies it takes
    private final HashMap<Integer, DisjointPAreaTaxonomy> loadedDisjointTaxonomies = new HashMap<>();
    private final HashMap<Integer, HashSet<OverlappingConceptResult<Concept, SCTPArea>>> loadedAreaOverlappingConcepts = new HashMap<>();
    
    public SCTPAreaTaxonomyConfiguration(
            SCTPAreaTaxonomy taxonomy, 
            SCTDisplayFrameListener displayListener, 
            SCTPAreaTaxonomyGEPConfiguration gepConfig) {
        
        this.taxonomy = taxonomy;
        this.displayListener = displayListener;
        
        this.gepConfig = gepConfig;
    }
    
    public SCTPAreaTaxonomy getPAreaTaxonomy() {
        return taxonomy;
    }
    
    public SCTDisplayFrameListener getDisplayListener() {
        return displayListener;
    }
    
    public SCTPAreaTaxonomyGEPConfiguration getGEPConfiguration() {
        return gepConfig;
    }
    
    private ArrayList<InheritedRelationship> getSortedRelationships(ArrayList<InheritedRelationship> rels) {
        Collections.sort(rels, new Comparator<InheritedRelationship>() {
            public int compare(InheritedRelationship a, InheritedRelationship b) {
                String aName = taxonomy.getLateralRelsInHierarchy().get(a.getRelationshipTypeId());
                String bName = taxonomy.getLateralRelsInHierarchy().get(b.getRelationshipTypeId());

                return aName.compareToIgnoreCase(bName);
            }
        });
        
        return rels;
    }
    
    @Override
    public ArrayList<InheritedRelationship> getAreaRelationships(SCTArea area) {
        ArrayList<InheritedRelationship> rels = new ArrayList<>(area.getRelationships());
        
        return getSortedRelationships(rels);
    }
    
    @Override
    public ArrayList<InheritedRelationship> getPAreaRelationships(SCTPArea parea) {
        ArrayList<InheritedRelationship> rels = new ArrayList<>(parea.getRelationships());

        return getSortedRelationships(rels);
    }

    @Override
    public Comparator<SCTPArea> getChildPAreaComparator() {
        return new Comparator<SCTPArea>() {
            public int compare(SCTPArea a, SCTPArea b) {
                return a.getRoot().getName().compareToIgnoreCase(b.getRoot().getName());
            }
        };
    }

    @Override
    public String getDisjointGroupName(DisjointPartialArea group) {
        return group.getRoot().getName();
    }
    
    private String getRelationshipNamesCommaSeparated(HashSet<InheritedRelationship> rels) {
        
        if(rels.isEmpty()) {
            return "";
        }
        
        ArrayList<String> relNames = new ArrayList<>();
        
        rels.forEach((InheritedRelationship rel) -> {
            relNames.add(taxonomy.getLateralRelsInHierarchy().get(rel.getRelationshipTypeId()));
        });
        
        Collections.sort(relNames);
        
        String areaName = relNames.get(0);
        
        for(int c = 1; c < relNames.size(); c++) {
            areaName += ", " + relNames.get(c);
        }
        
        return areaName;
    }

    @Override
    public String getContainerName(SCTArea container) {
        if(container.getRelationships().isEmpty()) {
            return "root area (no attribute relationships)";
        } else {
            return getRelationshipNamesCommaSeparated(container.getRelationships());
        }
    }

    @Override
    public ArrayList<SCTPArea> getSortedGroupList(SCTArea area) {
        return area.getAllPAreas();
    }

    @Override
    public String getConceptTypeName(boolean plural) {
        if(plural) {
            return "Concepts";
        } else {
            return "Concept";
        }
    }

    @Override
    public String getConceptName(Concept concept) {
        return concept.getName();
    }

    @Override
    public String getGroupName(SCTPArea group) {
        return group.getRoot().getName();
    }

    @Override
    public ArrayList<Concept> getSortedConceptList(SCTPArea group) {
        ArrayList<Concept> pareaConcepts = group.getConceptsInPArea();
        Collections.sort(pareaConcepts, new ConceptNameComparator());
        
        return pareaConcepts;
    }
    
    private DisjointPAreaTaxonomy createSimpleDisjointPAreaTaxonomy(SCTArea area) {
        SCTDisjointPAreaTaxonomyGenerator generator = new SCTDisjointPAreaTaxonomyGenerator();

        HashSet<SCTPArea> pareas = this.getContainerGroupSet(area);

        HashSet<Concept> roots = new HashSet<>();

        pareas.forEach((SCTPArea parea) -> {
            roots.add(parea.getRoot());
        });

        SCTMultiRootedConceptHierarchy hierarchy = new SCTMultiRootedConceptHierarchy(roots);

        pareas.forEach((SCTPArea parea) -> {
            if (taxonomy.isReduced()) {
                SCTAggregatePArea aggregatePArea = (SCTAggregatePArea) parea;

                hierarchy.addAllHierarchicalRelationships(this.getAggregatedPAreaHierarchy(aggregatePArea));
            } else {
                hierarchy.addAllHierarchicalRelationships(parea.getHierarchy());
            }
        });

        return generator.generateDisjointAbstractionNetwork(taxonomy, pareas, hierarchy);
    }
    
    private Optional<SCTArea> getCompleteArea(SCTArea area) {
        SCTLocalDataSource dataSource = (SCTLocalDataSource)taxonomy.getDataSource();
        
        ArrayList<Concept> rootHierarchies = dataSource.getHierarchiesConceptBelongTo(taxonomy.getRootGroup().getRoot());
        
        Concept hierarchyRoot = rootHierarchies.get(0); // There may be more than one hierarchy a concept belongs to, but we're not dealing with that for now
        
        SCTPAreaTaxonomy completeTaxonomy = dataSource.getCompleteTaxonomy(hierarchyRoot);
        
        ArrayList<SCTArea> completeAreas = completeTaxonomy.getAreas();
        
        Optional<SCTArea> matchingCompleteArea = Optional.empty();
        
        for(SCTArea completeArea : completeAreas) {
            if(completeArea.getRelationshipIds().equals(area.getRelationshipIds())) {
                matchingCompleteArea = Optional.of(completeArea);
                break;
            }
        }
        
        return matchingCompleteArea;
    }
    
    private DisjointPAreaTaxonomy createSubjectDisjointPAreaTaxonomy(SCTArea area) {
        SCTLocalDataSource dataSource = (SCTLocalDataSource)taxonomy.getDataSource();
        
        Optional<SCTArea> completeArea = getCompleteArea(area);
        
        HashSet<Concept> subhierarchyConcepts = area.getConcepts();
        
        HashSet<Concept> subhierarchyRoots = new HashSet<>();
        
        area.getAllPAreas().forEach( (SCTPArea parea) -> {
            subhierarchyRoots.add(parea.getRoot());
        });
        
        if(completeArea.isPresent()) {
            HashSet<Concept> completeAreaConcepts = completeArea.get().getConcepts();
            
            HashSet<SCTPArea> externalOverlappingPAreas = new HashSet<>();
            
            ArrayList<SCTPArea> allPAreas = completeArea.get().getAllPAreas();
            
            HashSet<Concept> externalConceptInArea = new HashSet<>();
                        
            subhierarchyConcepts.forEach((Concept c) -> {
                
                allPAreas.forEach((SCTPArea parea) -> {
                    if (parea.getHierarchy().getConceptsInHierarchy().contains(c)) {
                        externalOverlappingPAreas.add(parea);
                    }
                });
                
                HashSet<Concept> children = dataSource.getConceptHierarchy().getChildren(c);
                
                boolean isLeaf = true;
                
                for(Concept child : children) {
                    if(subhierarchyConcepts.contains(child)) {
                        isLeaf = false;
                        break;
                    }
                }
                
                if(isLeaf) {
                    externalConceptInArea.add(c);
                }
            });
            
            HashSet<SCTPArea> subjectPAreas = new HashSet<>(area.getAllPAreas());
            
            HashSet<SCTPArea> allSubjectOverlappingPAreas = new HashSet<>(subjectPAreas);
            allSubjectOverlappingPAreas.addAll(externalOverlappingPAreas);
            
            HashSet<Concept> roots = new HashSet<>();
            
            allSubjectOverlappingPAreas.forEach( (SCTPArea parea) -> {
                roots.add(parea.getRoot());
            });
            
            SCTMultiRootedConceptHierarchy hierarchy = new SCTMultiRootedConceptHierarchy(roots);
            
            HashSet<Concept> processedConcepts = new HashSet<>();
            HashSet<Concept> inQueueConcepts = new HashSet<>(externalConceptInArea);
            
            Queue<Concept> queue = new LinkedList<>(externalConceptInArea);
            
            while(!queue.isEmpty()) {
                Concept c = queue.remove();
                
                processedConcepts.add(c);
                inQueueConcepts.remove(c);
                
                HashSet<Concept> parents = dataSource.getConceptHierarchy().getParents(c);
                
                parents.forEach( (Concept parent) -> {
                    
                    if (completeAreaConcepts.contains(parent)) {
                         hierarchy.addIsA(c, parent);
                        
                        if (!inQueueConcepts.contains(parent) && !processedConcepts.contains(parent)) {
                            queue.add(parent);
                            inQueueConcepts.add(parent);
                        }
                    }
                });
            }
            
            SCTDisjointPAreaTaxonomyGenerator generator = new SCTDisjointPAreaTaxonomyGenerator();
            
            return generator.generateDisjointAbstractionNetwork(taxonomy, allSubjectOverlappingPAreas, hierarchy);
            
        } else {
            
            // This should never ever happen...
            
            return null;
        }
    }
    
    @Override
    public DisjointPAreaTaxonomy createDisjointAbN(SCTArea area) {
        DisjointPAreaTaxonomy disjointTaxonomy;
        
        if (loadedDisjointTaxonomies.containsKey(area.getId())) {
            disjointTaxonomy = loadedDisjointTaxonomies.get(area.getId());
        } else {
            if (isSubjectSubtaxonomy()) {
                disjointTaxonomy = createSubjectDisjointPAreaTaxonomy(area);
            } else {
                disjointTaxonomy = createSimpleDisjointPAreaTaxonomy(area);
            }
            
            loadedDisjointTaxonomies.put(area.getId(), disjointTaxonomy);
        }

        return disjointTaxonomy;

    }
    
    private boolean isSubjectSubtaxonomy() {
        return !taxonomy.getDataSource().getHierarchyRootConcepts().contains(taxonomy.getRootPArea().getRoot());
    }
    
    @Override
    public HashSet<SCTPArea> getContainerGroupSet(SCTArea area) {
        return new HashSet<>(area.getAllPAreas());
    }

    @Override
    public HashSet<Concept> getGroupConceptSet(SCTPArea parea) {
        return new HashSet<>(parea.getConceptsInPArea());
    }
    
    private HashSet<OverlappingConceptResult<Concept, SCTPArea>> getExternalOverlappingResults(SCTArea area) {
        Optional<SCTArea> completeArea = getCompleteArea(area);
        
        if(completeArea.isPresent()) {
            HashSet<SCTPArea> internalPAreas = this.getContainerGroupSet(area);
            
            HashSet<SCTPArea> externalPAreas = this.getContainerGroupSet(completeArea.get());
            
            HashMap<Concept, HashSet<SCTPArea>> conceptPAreas = new HashMap<>();
            
            HashSet<Concept> internalConcepts = area.getConcepts();
            
            internalConcepts.forEach( (Concept c) -> {
                conceptPAreas.put(c, new HashSet<>());
                
                internalPAreas.forEach((SCTPArea internalPArea) -> {
                    if(internalPArea.getHierarchy().getConceptsInHierarchy().contains(c)) {
                        conceptPAreas.get(c).add(internalPArea);
                    }
                });
                
                externalPAreas.forEach((SCTPArea externalPArea) -> {
                    if(externalPArea.getHierarchy().getConceptsInHierarchy().contains(c)) {
                        conceptPAreas.get(c).add(externalPArea);
                    }
                });
            });
            
            HashSet<OverlappingConceptResult<Concept, SCTPArea>> allOverlappingResults = new HashSet<>();
            
            conceptPAreas.forEach((Concept c, HashSet<SCTPArea> pareas) -> {
                if (pareas.size() > 1) {
                    allOverlappingResults.add(new OverlappingConceptResult<>(c, pareas));
                }
            });
            
            return allOverlappingResults;
        } else {
            return new HashSet<>();
        }
    }
    
    @Override
    public HashSet<OverlappingConceptResult<Concept, SCTPArea>> getContainerOverlappingResults(SCTArea area) {
        
        HashSet<OverlappingConceptResult<Concept, SCTPArea>> overlappingConcepts;
        
        if(loadedAreaOverlappingConcepts.containsKey(area.getId())) {
            overlappingConcepts = loadedAreaOverlappingConcepts.get(area.getId());
        } else {
            if (isSubjectSubtaxonomy()) {
                overlappingConcepts = getExternalOverlappingResults(area);
            } else {
                overlappingConcepts = super.getContainerOverlappingResults(area);
            }
            
            loadedAreaOverlappingConcepts.put(area.getId(), overlappingConcepts);
        }
        
        return overlappingConcepts;
    }

    @Override
    public int getContainerLevel(SCTArea area) {
        return area.getRelationships().size();
    }
    
    @Override
    public String getGroupsContainerName(SCTPArea parea) {
        return this.getRelationshipNamesCommaSeparated(parea.getRelationships());
    }
    
    @Override
    public SCTConceptHierarchy getAggregatedPAreaHierarchy(SCTAggregatePArea aggregatePArea) {
        SCTConceptHierarchy aggregateConceptHierarchy = new SCTConceptHierarchy(aggregatePArea.getRoot());

        HashSet<SCTPArea> aggregatedPAreaSet = aggregatePArea.getAggregatedGroupHierarchy().getNodesInHierarchy();
        
        aggregatedPAreaSet.forEach((SCTPArea aggregatedPArea) -> {
            aggregateConceptHierarchy.addAllHierarchicalRelationships(aggregatedPArea.getHierarchy());
        });

        HashSet<Concept> conceptsInAggregatedHierarchy = aggregateConceptHierarchy.getConceptsInHierarchy();
        
        aggregatedPAreaSet.forEach((SCTPArea aggregatedPArea) -> {
            conceptsInAggregatedHierarchy.add(aggregatedPArea.getRoot());
        });
        
        aggregatedPAreaSet.forEach((SCTPArea aggregatedPArea) -> {
            if(!aggregatedPArea.equals(aggregatePArea.getAggregatedGroupHierarchy().getRoots().iterator().next())) {
                ArrayList<Concept> aggregatedPAreaConcepts = aggregatedPArea.getConceptsInPArea();
                
                aggregatedPAreaConcepts.forEach((Concept c) -> {
                    ArrayList<Concept> parents = taxonomy.getDataSource().getConceptParents(c);

                    parents.forEach((Concept parent) -> {
                        if (conceptsInAggregatedHierarchy.contains(parent)) {
                            aggregateConceptHierarchy.addIsA(c, parent);
                        }
                    });
                });
            }
        });
        
        return aggregateConceptHierarchy;
    }
    
    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        return new DisplayConceptBrowserListener(this.getDisplayListener(), this.getPAreaTaxonomy().getDataSource());
    }

    @Override
    public EntitySelectionListener<SCTPArea> getChildGroupListener() {
        return new NavigateToGroupListener<>(this.getGEPConfiguration().getGEP());
    }
    
    @Override
    public EntitySelectionListener<GenericParentGroupInfo<Concept, SCTPArea>> getParentGroupListener() {
        return new ParentGroupSelectedListener<>(this.getGEPConfiguration().getGEP());
    }
    
    @Override
    public String getContainerHelpDescription(SCTArea container) {
        StringBuilder helpDesc = new StringBuilder();
        
        if(taxonomy.isReduced()) {
            helpDesc.append("An <b>aggregate area</b> represents a set of concepts (shown below) which are defined using the "
                    + " exact same set of attribute relationships types."
                    + "<p><p>"
                    + "Additionally, an aggregate area will summarize one or more concepts "
                    + "from one or more areas with additional attribute relationships if this area"
                    + "has at least one aggregate partial-area. This information is viewable in the <i>aggregated partial-areas</i> tab.");
        } else {
            helpDesc.append("An <b>area</b> represents a set of concepts (shown below) which are defined using the exact same set of attribute relationship "
                    + "types.");
        }
        
        return helpDesc.toString();
    }

    @Override
    public String getGroupHelpDescriptions(SCTPArea group) {
         StringBuilder helpDesc = new StringBuilder();
        
        if(taxonomy.isReduced()) {
            helpDesc.append("A <b>regular partial-area</b> summarizes a subhierarchy of structurally and semantically similar concepts in an ontology. "
                    + "All of the concepts in a regular partial-area are defined using the same types of attribute relationships."
                    
                    + "All of the concepts summarized by a regular partial-area are descendants of the same concepts, called the root, "
                    + "which is the introduction point for the set of attribute relationships. "
                    
                    + "A partial-area is named after the root and is labeled with the total number of concepts summarized by the partial-area (in parenthesis).");
            
            helpDesc.append("<p><p>");
            
            helpDesc.append("An <b>aggregate partial-area</b> summarizes the same content as a regular partial-area and "
                    + "one or more removed descendant partial-areas which were below the chosen minimum threshold and are defined using one or more additional "
                    + "attribute relationships in relation to the aggregated partial-area. "
                    + "To view the aggregated removed partial-areas, and which areas they came from, see the <i>aggregated partial-area</i> tab.");
        } else {
            helpDesc.append("A <b>partial-area</b> summarizes a subhierarchy of structurally and semantically similar concepts in SNOMED CT. "
                    + "All of the concepts in a partial-area are defined using the exact same types of attribute relationships. "
                    
                    + "All of the concepts summarized by a partial-area are descendants of the same concept, called the root, "
                    + "which is the introduction point for the set of attribute relationships. "
                    + "A partial-area is named after the root and is labeled with the total number "
                    + "of concepts summarized by the partial-area (in parenthesis).");
        }
        
        return helpDesc.toString();
    }
    
    @Override
    public String getAbNName() {
        if (taxonomy.isReduced()) {
            return String.format("%s Aggregate Partial-area Taxonomy", getGroupName(taxonomy.getRootGroup()));
        } else {
            return String.format("%s Partial-area Taxonomy", getGroupName(taxonomy.getRootGroup()));
        }
    }
    
    private String getRegularPAreaTaxonomySummary() {
        
        String rootName = getGroupName(taxonomy.getRootGroup());
        
        int pareaCount = taxonomy.getPAreaCount();
        int areaCount = taxonomy.getAreaCount();
        int conceptCount = taxonomy.getConceptHierarchy().getConceptsInHierarchy().size();

        String result = String.format("<html>The <b>%s</b> partial-area taxonomy summarizes a total of %d concept(s) in %d area(s) and %d partial-areas(s).", 
                rootName, conceptCount, areaCount, pareaCount);
        
        return result;
    }
    
    private String getAggregatePAreaTaxonomySummary() {
        String rootName = getGroupName(taxonomy.getRootGroup());
        
        int areaCount = taxonomy.getAreaCount();
        int conceptCount = taxonomy.getConceptHierarchy().getConceptsInHierarchy().size();
        
        HashSet<SCTAggregatePArea> aggregatePAreas = new HashSet<>();
        
        HashSet<SCTAggregatePArea> regularPAreas = new HashSet<>();
        
        taxonomy.getAreas().forEach( (SCTArea area) -> {
            ArrayList<SCTPArea> pareas = area.getAllPAreas();
            
            pareas.forEach( (SCTPArea parea) -> {
                SCTAggregatePArea aggregatePArea = (SCTAggregatePArea)parea;
                
                if(aggregatePArea.getAggregatedGroups().isEmpty()) {
                    regularPAreas.add(aggregatePArea);
                } else {
                    aggregatePAreas.add(aggregatePArea);
                }
            });
        });
        
        HashSet<SCTPArea> removedPAreas = new HashSet<>();
        
        aggregatePAreas.forEach( (SCTAggregatePArea parea) -> {
            removedPAreas.addAll(parea.getAggregatedGroups());
        });

        String result = String.format("<html>The <b>%s</b> aggregate partial-area "
                + "taxonomy summarizes a total of %d concept(s) in %d area(s), %d "
                + "aggregate partial-area(s), %d regular partial-area(s), and %d removed partial-area(s).", 
                rootName, 
                conceptCount, 
                areaCount, 
                aggregatePAreas.size(), 
                regularPAreas.size(), 
                removedPAreas.size());

        return result;
    }

    @Override
    public String getAbNSummary() {
        String summary;

        if (taxonomy.isReduced()) {
            summary = getAggregatePAreaTaxonomySummary();
        } else {
            summary = getRegularPAreaTaxonomySummary();
        }

        summary += "<p><b>Help / Description:</b><br>";
        summary += getAbNHelpDescription();

        return summary;
    }

    @Override
    public String getAbNHelpDescription() {
                
        String pareaTaxonomyDesc = "A partial-area taxonomy is a type of abstraction network that summarizes structurally similar "
                + "and semantically similar concepts. The partial-area taxonomy reveals and highlights "
                + "high-level structural and hierarchical aggregation patterns in SNOMED CT. "
                + "<p>"
                + "A partial-area taxonomy is composed of two kinds of nodes. Areas summarize disjoint sets of concepts that are modeled using "
                + "the exact same types of attribute relationships. "
                + "The concepts summarized by an area are all structurally similar in that they summarize concepts that have the same relationship structure. "
                + "Areas are shown as colored boxes and are labeled with their set of attribute relationship types, the number of concepts summarized, "
                + "and the number of partial-areas."
                + "Areas are organized into color-coded levels according to their number of attribute relationship types."
                + "<p>"
                + "The second type of node, called the partial-area, summarizes subhierarchies of semantically similar concepts within each area. "
                + "Within each area there may be multiple such subhierarchies. Partial-areas are shown as white boxes "
                + "within each area. Each partial-area is named after the concept that is the root of the subhierarchy "
                + "and is labeled with the total number of concepts summarized by the partial-area in parenthesis. "
                + "<p>"
                + "The <i>Is a</i> hierarchy between concepts is also summarized by the partial-area taxonomy. When a given partial-area is selected then its "
                + "parent partial-areas are shown in blue and its child partial-areas are shown in purple.";
        
        if(taxonomy.isReduced()) {
            String aggregateTaxonomyDesc = "An <b>aggregate partial-area taxonomy</b> is a partial-area taxonomy where the small partial-areas, which summarize "
                    + "a number of concepts below a chosen threshold, are not shown. The small removed partial-areas are aggregated into and summarized by"
                    + "their closest ancestor partial-area(s) which are above the chosen threshold. "
                    + "An aggregate partial-area, shown as a rounded rectangle, summarizes at least one removed partial-area. Regular partial-areas "
                    + "are partial-areas which summarize no removed partial-areas in an aggregate partial-area taxonomy.";
            
            return String.format("%s<p>%s", pareaTaxonomyDesc, aggregateTaxonomyDesc);
        } else {
            return pareaTaxonomyDesc;
        }
    }
}
