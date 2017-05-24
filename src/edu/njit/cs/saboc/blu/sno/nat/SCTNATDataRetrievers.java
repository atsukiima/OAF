package edu.njit.cs.saboc.blu.sno.nat;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTStatedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.gui.panels.ResultPanel.DataRetriever;
import edu.njit.cs.saboc.nat.generic.gui.panels.dataretrievers.CommonBrowserDataRetrievers;
import java.util.ArrayList;
import java.util.Set;

/**
 * SNOMED CT-specific data retrievers for the NAT
 * 
 * @author Chris O
 */
public class SCTNATDataRetrievers {
    
    /**
     * Returns the attribute relationships of the given concept
     * 
     * @param browserPanel
     * @return 
     */
    public static DataRetriever<SCTConcept, ArrayList<AttributeRelationship>> 
        getAttributeRelationshipRetriever(NATBrowserPanel<SCTConcept> browserPanel) {
        
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
    
    /**
     * Retriever for obtaining the stated parents of the given concept
     * 
     * @param browserPanel
     * @return 
     */
    public static DataRetriever<SCTConcept, ArrayList<SCTConcept>> getStatedParentRetriever(
            NATBrowserPanel<SCTConcept> browserPanel) {
        
        return new DataRetriever<SCTConcept, ArrayList<SCTConcept>>() {

            @Override
            public ArrayList<SCTConcept> getData(SCTConcept concept) {

                if (browserPanel.getDataSource().isPresent()) {

                    SCTConceptBrowserDataSource dataSource = (SCTConceptBrowserDataSource)browserPanel.getDataSource().get();
                    
                    SCTReleaseWithStated statedRelease = (SCTReleaseWithStated)dataSource.getRelease();

                    return CommonBrowserDataRetrievers.getSortedConceptList(statedRelease.getStatedHierarchy().getParents(concept));

                } else {
                    return new ArrayList<>();
                }

            }

            @Override
            public String getDataType() {
                return "Stated Parents";
            }
            
        };
    }
    
    /**
     * Retriever for obtaining the stated children (i.e., concepts with the given concept as a stated parent)
     * 
     * @param browserPanel
     * @return 
     */
    public static DataRetriever<SCTConcept, ArrayList<SCTConcept>> getStatedChildrenRetriever(
            NATBrowserPanel<SCTConcept> browserPanel) {
        
        return new DataRetriever<SCTConcept, ArrayList<SCTConcept>>() {
            @Override
            public ArrayList<SCTConcept> getData(SCTConcept concept) {
                
                if (browserPanel.getDataSource().isPresent()) {

                    SCTConceptBrowserDataSource dataSource = (SCTConceptBrowserDataSource)browserPanel.getDataSource().get();
                    
                    SCTReleaseWithStated statedRelease = (SCTReleaseWithStated)dataSource.getRelease();
     
                    return CommonBrowserDataRetrievers.getSortedConceptList(statedRelease.getStatedHierarchy().getChildren(concept));
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            public String getDataType() {
                return "Stated Children";
            }
        };
    }
    
    /**
     * Retriever for obtaining the stated siblings of a concept (i.e., concepts 
     * with at least one of the same stated parents).
     * 
     * @param browserPanel
     * @return 
     */
    public static DataRetriever<SCTConcept, ArrayList<SCTConcept>> getStatedSiblingRetriever(
            NATBrowserPanel<SCTConcept> browserPanel) {
        
        return new DataRetriever<SCTConcept, ArrayList<SCTConcept>>() {
            
            @Override
            public ArrayList<SCTConcept> getData(SCTConcept concept) {
                
                if (browserPanel.getDataSource().isPresent()) {

                    SCTConceptBrowserDataSource dataSource = (SCTConceptBrowserDataSource)browserPanel.getDataSource().get();
                    
                    SCTReleaseWithStated statedRelease = (SCTReleaseWithStated)dataSource.getRelease();
     
                    return CommonBrowserDataRetrievers.getSortedConceptList(statedRelease.getStatedHierarchy().getSiblings(concept));
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            public String getDataType() {
                return "Stated Siblings";
            }
        };
    }
    
    /**
     * Retriever for obtaining the stated strict siblings of the given concept 
     * (i.e., concepts with the exact same set of stated parents).
     * 
     * @param browserPanel
     * @return 
     */
    public static DataRetriever<SCTConcept, ArrayList<SCTConcept>> getStatedStrictSiblingRetriever(
            NATBrowserPanel<SCTConcept> browserPanel) {
        
        return new DataRetriever<SCTConcept, ArrayList<SCTConcept>>() {
            @Override
            public ArrayList<SCTConcept> getData(SCTConcept concept) {
                
                if (browserPanel.getDataSource().isPresent()) {

                    SCTConceptBrowserDataSource dataSource = (SCTConceptBrowserDataSource)browserPanel.getDataSource().get();
                    
                    SCTReleaseWithStated statedRelease = (SCTReleaseWithStated)dataSource.getRelease();
     
                    return CommonBrowserDataRetrievers.getSortedConceptList(statedRelease.getStatedHierarchy().getStrictSiblings(concept));
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            public String getDataType() {
                return "Stated Strict Siblings";
            }
        };
    }
    
    /**
     * Returns the stated attribute relationships of the given concept
     * 
     * @param browserPanel
     * 
     * @return 
     */
    public static DataRetriever<SCTConcept, ArrayList<AttributeRelationship>> 
        getStatedAttributRelationshipsRetriever(NATBrowserPanel<SCTConcept> browserPanel) {
        
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
    
    /**
     * Returns a sorted list of attribute relationships, where the relationships are
     * sorted by type and then subsorted based on the name of their target.
     * 
     * @param attributeRels
     * @return 
     */
    public static ArrayList<AttributeRelationship> getSortedAttributeRelationships(
            Set<AttributeRelationship> attributeRels) {
        
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
