package edu.njit.cs.saboc.blu.sno.nat;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.nat.generic.gui.panels.ResultPanel.DataRetriever;
import java.util.ArrayList;

/**
 *
 * @author Chris O
 */
public class SCTNATDataRetrievers {
    
    public static DataRetriever<SCTConcept, ArrayList<AttributeRelationship>> getAttributeRelationshipRetriever(SCTConceptBrowserDataSource dataSource) {
        
        return new DataRetriever<SCTConcept, ArrayList<AttributeRelationship>>() {

            @Override
            public ArrayList<AttributeRelationship> getData(SCTConcept concept) {
                ArrayList<AttributeRelationship> relationships = new ArrayList<>(concept.getAttributeRelationships());
                
                relationships.sort( (a, b) -> {

                    if(a.getGroup() == b.getGroup()) {
                        if(a.getType().equals(b.getType())) {
                            return a.getTarget().getName().compareToIgnoreCase(b.getTarget().getName());
                        } else {
                            return a.getType().getName().compareToIgnoreCase(b.getType().getName());
                        }
                    }
                    
                    return a.getGroup() - b.getGroup();
                });
                
                return relationships;
            }

            @Override
            public String getDataType() {
                return "Attribute Relationships";
            }
            
        };
    }
}
