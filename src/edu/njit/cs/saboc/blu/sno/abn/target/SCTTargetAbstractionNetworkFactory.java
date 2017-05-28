package edu.njit.cs.saboc.blu.sno.abn.target;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.abn.targetbased.RelationshipTriple;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetworkFactory;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTStatedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Factory for creating SNOMED CT target abstraction networks. For SNOMED CT
 * target abstraction networks are based on the stated attribute relationships 
 * of a concept.
 * 
 * @author Chris O
 */
public class SCTTargetAbstractionNetworkFactory extends TargetAbstractionNetworkFactory {

    private final SCTReleaseWithStated release;
    
    public SCTTargetAbstractionNetworkFactory(
            SCTReleaseWithStated release, 
            Hierarchy<SCTConcept> sourceHierarchy,
            SCTInheritableProperty propertyType,
            Hierarchy<SCTConcept> targetHierarchy) {
        
        this.release = release;
    }
    
    @Override
    public Set<RelationshipTriple> getRelationshipsToTargetHierarchyFor(
            Concept concept, 
            Set<InheritableProperty> relationshipTypes, 
            Hierarchy<Concept> targetHierarchy) {
        
        SCTStatedConcept sctConcept = (SCTStatedConcept)concept;
        
        SCTInheritableProperty propertyType = (SCTInheritableProperty)relationshipTypes.iterator().next();

        return sctConcept.getStatedRelationships().stream().filter(
                   (rel) -> rel.getType().equals(propertyType.getPropertyType())
                ).map( (rel) -> {
                   return new RelationshipTriple(sctConcept, propertyType, rel.getTarget());
                }).collect(Collectors.toSet());
    }
}