package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.core.ontology.ConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Chris O
 */
public class SCTInferredPAreaTaxonomyFactory implements PAreaTaxonomyFactory {
    
    private final Map<SCTConcept, Set<InheritableProperty>> properties = new HashMap<>();
    
    public SCTInferredPAreaTaxonomyFactory(ConceptHierarchy<SCTConcept> hierarchy) {
        
        Map<SCTConcept, Set<SCTConcept>> uniqueRels = new HashMap<>();
        
        hierarchy.getConceptsInHierarchy().forEach((concept) -> {
            Set<AttributeRelationship> rels = concept.getAttributeRelationships().stream().filter(
                (rel) -> { 
                    return rel.getCharacteristicType() == 0; // Use defining relationships
                    
                }).collect(Collectors.toSet());
            
            uniqueRels.put(concept, new HashSet<>());
            
            rels.forEach((rel) -> {
                uniqueRels.get(concept).add(rel.getRelationshipType());
            });
        });
        
        uniqueRels.forEach((sourceConcept, sourceConceptRels) -> {

            properties.put(sourceConcept, new HashSet<>());

            Set<Concept> parentRoles = new HashSet<>();

            Set<SCTConcept> parents = hierarchy.getParents(sourceConcept);

            parents.forEach((parent) -> {
                parentRoles.addAll(uniqueRels.get(parent));
            });

            sourceConceptRels.forEach((role) -> {
                if (parentRoles.stream().anyMatch((parentRole) -> parentRole.equals(role))) {
                    properties.get(sourceConcept).add(new SCTInheritableProperty(role, InheritableProperty.InheritanceType.Inherited));
                } else {
                    properties.get(sourceConcept).add(new SCTInheritableProperty(role, InheritableProperty.InheritanceType.Introduced));
                }
            });
        });
    }

    @Override
    public Set<InheritableProperty> getRelationships(Concept c) {
        return properties.get((SCTConcept)c);
    }
}