
package edu.njit.cs.saboc.blu.sno.localdatasource.load;

/**
 *
 * @author Chris
 */
public class RelationshipsRetrieverFactory {
    public static ConceptRelationshipsRetriever getRelationshipsRetriever(boolean statedRelationships) {
        if(statedRelationships) {
            return new StatedRelationshipsRetriever();
        } else {
            return new InferredRelationshipsRetriever();
        }
    }
}
