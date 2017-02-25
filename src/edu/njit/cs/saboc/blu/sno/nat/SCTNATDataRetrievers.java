package edu.njit.cs.saboc.blu.sno.nat;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTStatedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import edu.njit.cs.saboc.nat.generic.gui.panels.ResultPanel.DataRetriever;
import edu.njit.cs.saboc.nat.generic.gui.panels.dataretrievers.CommonBrowserDataRetrievers;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class SCTNATDataRetrievers {
    
    public static DataRetriever<SCTConcept, ArrayList<AttributeRelationship>> getAttributeRelationshipRetriever(SCTConceptBrowserDataSource dataSource) {
        
        return new DataRetriever<SCTConcept, ArrayList<AttributeRelationship>>() {

            @Override
            public ArrayList<AttributeRelationship> getData(SCTConcept concept) {
                return SCTNATDataRetrievers.getSortedAttributeRelationships(concept.getAttributeRelationships());
            }

            @Override
            public String getDataType() {
                return "Attribute Relationships";
            }
        };
    }
    
    public static DataRetriever<SCTConcept, ArrayList<SCTConcept>> getStatedParentRetriever(
            SCTConceptBrowserDataSource dataSource) {
        
        return new DataRetriever<SCTConcept, ArrayList<SCTConcept>>() {

            @Override
            public ArrayList<SCTConcept> getData(SCTConcept concept) {
                SCTReleaseWithStated statedRelease = (SCTReleaseWithStated)dataSource.getRelease();
                
                return CommonBrowserDataRetrievers.getSortedConceptList(statedRelease.getStatedHierarchy().getParents(concept));
            }

            @Override
            public String getDataType() {
                return "Stated Parents";
            }
            
        };
    }
    
    public static DataRetriever<SCTConcept, ArrayList<SCTConcept>> getStatedChildrenRetriever(
            SCTConceptBrowserDataSource dataSource) {
        
        return new DataRetriever<SCTConcept, ArrayList<SCTConcept>>() {
            @Override
            public ArrayList<SCTConcept> getData(SCTConcept concept) {
                SCTReleaseWithStated statedRelease = (SCTReleaseWithStated)dataSource.getRelease();
                
                return CommonBrowserDataRetrievers.getSortedConceptList(statedRelease.getStatedHierarchy().getChildren(concept));
            }

            @Override
            public String getDataType() {
                return "Stated Children";
            }
        };
    }
    
    public static DataRetriever<SCTConcept, ArrayList<SCTConcept>> getStatedSiblingRetriever(
            SCTConceptBrowserDataSource dataSource) {
        
        return new DataRetriever<SCTConcept, ArrayList<SCTConcept>>() {
            @Override
            public ArrayList<SCTConcept> getData(SCTConcept concept) {
                SCTReleaseWithStated statedRelease = (SCTReleaseWithStated)dataSource.getRelease();
                
                return CommonBrowserDataRetrievers.getSortedConceptList(statedRelease.getStatedHierarchy().getSiblings(concept));
            }

            @Override
            public String getDataType() {
                return "Stated Siblings";
            }
        };
    }
    
    public static DataRetriever<SCTConcept, ArrayList<SCTConcept>> getStatedStrictSiblingRetriever(
            SCTConceptBrowserDataSource dataSource) {
        
        return new DataRetriever<SCTConcept, ArrayList<SCTConcept>>() {
            @Override
            public ArrayList<SCTConcept> getData(SCTConcept concept) {
                SCTReleaseWithStated statedRelease = (SCTReleaseWithStated)dataSource.getRelease();
                
                return CommonBrowserDataRetrievers.getSortedConceptList(statedRelease.getStatedHierarchy().getStrictSiblings(concept));
            }

            @Override
            public String getDataType() {
                return "Stated Strict Siblings";
            }
        };
    }
    
    public static DataRetriever<SCTConcept, ArrayList<AttributeRelationship>> getStatedAttributRelationshipsRetriever(
            SCTConceptBrowserDataSource dataSource) {
        
        return new DataRetriever<SCTConcept, ArrayList<AttributeRelationship>>() {
            @Override
            public ArrayList<AttributeRelationship> getData(SCTConcept concept) {
                SCTStatedConcept statedConcept = (SCTStatedConcept)concept;
                
                return SCTNATDataRetrievers.getSortedAttributeRelationships(statedConcept.getStatedRelationships());
            }

            @Override
            public String getDataType() {
                return "Stated Attribute Relationships";
            }
        };
    }
    
    public static ArrayList<AttributeRelationship> getSortedAttributeRelationships(Set<AttributeRelationship> attributeRels) {
        ArrayList<AttributeRelationship> relationships = new ArrayList<>(attributeRels);

        relationships.sort((a, b) -> {

            if (a.getGroup() == b.getGroup()) {
                if (a.getType().equals(b.getType())) {
                    return a.getTarget().getName().compareToIgnoreCase(b.getTarget().getName());
                } else {
                    return a.getType().getName().compareToIgnoreCase(b.getType().getName());
                }
            }

            return a.getGroup() - b.getGroup();
        });
        
        return relationships;
    }
}
