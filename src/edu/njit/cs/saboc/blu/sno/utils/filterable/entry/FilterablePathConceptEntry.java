
package edu.njit.cs.saboc.blu.sno.utils.filterable.entry;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalLateralRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSnomedConcept;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Chris O
 */
public class FilterablePathConceptEntry extends FilterableConceptEntry {
    
    private String relStr;
    
    public FilterablePathConceptEntry(Concept c) {
        super(c);
        
         LocalSnomedConcept concept = (LocalSnomedConcept)c;
         
         ArrayList<LocalLateralRelationship> rels = concept.getAttributeRelationships();
         
         ArrayList<String> relStrs = new ArrayList<String>();

        for (LocalLateralRelationship rel : rels) {
            if (rel.getCharacteristicType() == 0) {
                String relName = rel.getRelationship().getName();
                relName = relName.substring(0, relName.lastIndexOf("(")).trim();
                
                String targetName = rel.getTarget().getName();
                targetName = targetName.substring(0, targetName.lastIndexOf("(")).trim();

                relStrs.add(String.format("<font color=\"red\">%s</font> => %s", relName, targetName));
            }
        }

        if (!relStrs.isEmpty()) {

            Collections.sort(relStrs);

            String relStr = "(";

            for (String rel : relStrs) {
                relStr += (rel + ", ");
            }

            relStr = relStr.substring(0, relStr.length() - 2);
            relStr += ")";

            this.relStr = relStr;
        } else {
            relStr = "";
        }
    }
    
    protected String createEntryStr(String conceptName, String conceptId) {
        return super.createEntryStr(conceptName, conceptId) + " " + relStr;
    }
    
    protected String createEntryStrWithPrimitive(String conceptName, String conceptId) {
        return super.createEntryStrWithPrimitive(conceptName, conceptId) + " " + relStr;
    }
}
