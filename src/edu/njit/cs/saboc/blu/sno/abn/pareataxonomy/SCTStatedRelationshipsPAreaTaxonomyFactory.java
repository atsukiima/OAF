package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.AreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty.InheritanceType;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.visitor.TopologicalVisitor;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTStatedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Factory for creating partial-area taxonomies from a concept's stated relationships
 * 
 * @author Chris O
 */
public class SCTStatedRelationshipsPAreaTaxonomyFactory extends PAreaTaxonomyFactory {
    
    /**
     * Visitor for computing the introduction and inheritance of attribute relationships
     * throughout a concept hierarchy (based on the stated hierarchical relationships)
     */
    private class SCTStatedRelationshipsVisitor extends TopologicalVisitor<SCTStatedConcept> {
        
        private final Map<SCTStatedConcept, Set<SCTInheritableProperty>> inheritedStatedRelationships = new HashMap<>();
        
        private final Map<SCTStatedConcept, Set<SCTInheritableProperty>> statedRelationships = new HashMap<>();
        
        public SCTStatedRelationshipsVisitor(Hierarchy<SCTStatedConcept> theHierarchy) {
            super(theHierarchy);
            
            theHierarchy.getNodes().forEach((concept) -> {
                statedRelationships.put(concept, new HashSet<>());
                
                concept.getStatedRelationships().forEach((relationship) -> {
                    statedRelationships.get(concept).add(new SCTInheritableProperty(
                            relationship.getType(),
                            InheritanceType.Introduced));
                });
            });
        }

        @Override
        public void visit(SCTStatedConcept concept) {
            Hierarchy<SCTStatedConcept> hierarchy = super.getHierarchy();
            
            Set<SCTInheritableProperty> conceptStatedRelationships = statedRelationships.get(concept);
            
            Set<SCTInheritableProperty> parentProperties = new HashSet<>();
            
            hierarchy.getParents(concept).forEach( (parent) -> {
                parentProperties.addAll(inheritedStatedRelationships.get(parent));
            });
            
            Set<SCTInheritableProperty> conceptProperties = new HashSet<>();

            conceptStatedRelationships.forEach((property) -> {
                InheritanceType inheritance;

                if (parentProperties.contains(property)) {
                    inheritance = InheritanceType.Inherited;
                } else {
                    inheritance = InheritanceType.Introduced;
                }
                
                conceptProperties.add(new SCTInheritableProperty(property.getPropertyType(), inheritance));
            });

            this.inheritedStatedRelationships.put(concept, conceptProperties);
        }
        
        public Map<SCTStatedConcept, Set<SCTInheritableProperty>> getPropertyInheritance() {
            return inheritedStatedRelationships;
        }
    }

    private final SCTReleaseWithStated statedRelease;
    
    private final Map<SCTStatedConcept, Set<SCTInheritableProperty>> conceptProperties;
    
    public SCTStatedRelationshipsPAreaTaxonomyFactory(SCTReleaseWithStated statedRelease, SCTConcept topLevelHierarchy) {
        super(statedRelease);
        
        this.statedRelease = statedRelease;
        
        Hierarchy<SCTStatedConcept> statedConceptHierarchy = 
                (Hierarchy<SCTStatedConcept>)(Hierarchy<?>)statedRelease.getStatedHierarchy().getSubhierarchyRootedAt(topLevelHierarchy);
        
        SCTStatedRelationshipsVisitor visitor = new SCTStatedRelationshipsVisitor(statedConceptHierarchy);
        
        statedConceptHierarchy.topologicalDown(visitor);

        this.conceptProperties = visitor.getPropertyInheritance();
    }

    @Override
    public Set<InheritableProperty> getRelationships(Concept c) {
        return (Set<InheritableProperty>)(Set<?>)this.conceptProperties.get((SCTStatedConcept)c);
    }

    @Override
    public <T extends PArea> PAreaTaxonomy createPAreaTaxonomy(AreaTaxonomy areaTaxonomy, Hierarchy<T> pareaHierarchy, Hierarchy<Concept> conceptHierarchy) {
        PAreaTaxonomy superTaxonomy = super.createPAreaTaxonomy(areaTaxonomy, pareaHierarchy, conceptHierarchy);
        
        return new SCTStatedRelationshipsPAreaTaxonomy(superTaxonomy);
    }
}
