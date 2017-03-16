package edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation;

import edu.njit.cs.saboc.blu.core.abn.diff.utils.SetUtilities;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DeltaRelationship;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.FuzzyParentChange;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.FuzzyRelationshipChange;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.RelationshipGroupChange;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.RelationshipRefinementType;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.SimpleParentChange;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.SimpleRelationshipChange;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Generates a descriptive delta from a set of delta relationships.
 * 
 * Requires two consecutive SNOMED CT releases.
 * 
 * @author Chris O
 */
public class DescriptiveDeltaGenerator {
    
    public DescriptiveDelta createDescriptiveDelta(
            SCTRelease fromRelease,
            SCTRelease toRelease, 
            SCTConcept subhierarchyRoot,
            DeltaRelationships deltaRelationships) {
        
        // Step 1: Identify activated and retired concepts
        Set<SCTConcept> toInactiveConcepts = toRelease.getInactiveConcepts();
        
        Set<SCTConcept> allRetiredConcepts = new HashSet<>(fromRelease.getActiveConcepts());
        allRetiredConcepts.removeAll(toRelease.getActiveConcepts());
        allRetiredConcepts.retainAll(toInactiveConcepts);
        
        Set<SCTConcept> allActivatedConcepts = new HashSet<>(toRelease.getActiveConcepts());
        allActivatedConcepts.removeAll(fromRelease.getActiveConcepts());
        allActivatedConcepts.removeAll(toInactiveConcepts);
        
        
        // Step 2: Identify differences in the set of active concepts in the
        // subhierarchy of concepts
        final Hierarchy<SCTConcept> beforeSubhierarchy = fromRelease.getConceptHierarchy().getSubhierarchyRootedAt(subhierarchyRoot);
        Set<SCTConcept> beforeConcepts = beforeSubhierarchy.getNodes();

        final Hierarchy<SCTConcept> afterSubhierarchy = toRelease.getConceptHierarchy().getSubhierarchyRootedAt(subhierarchyRoot);
        Set<SCTConcept> afterConcepts = afterSubhierarchy.getNodes();
        
        Set<SCTConcept> allSubhierarchyConcepts = SetUtilities.getSetUnion(beforeConcepts, afterConcepts);
        
        Set<SCTConcept> returningConcepts = new HashSet<>(afterConcepts);
        returningConcepts.retainAll(beforeConcepts);
        
        Map<SCTConcept, Set<DeltaRelationship>> statedDeltaRelationships = deltaRelationships.getStatedDeltaRelationships();
        Map<SCTConcept, Set<DeltaRelationship>> inferredDeltaRelationships = deltaRelationships.getInferredDeltaRelationships();
        
        Set<SCTConcept> editedConcepts = new HashSet<>();
        
        // Step 3: Identify all of the concepts that had one or more 
        // stated or inferred editing operations applied
        statedDeltaRelationships.keySet().forEach( (editedConcept) -> {            
            if (allSubhierarchyConcepts.contains(editedConcept)) {
                editedConcepts.add(editedConcept);
            }
        });
        
        Set<SCTConcept> changedConcepts = new HashSet<>();
        
        inferredDeltaRelationships.keySet().forEach( (changedConcept) -> {
            if(allSubhierarchyConcepts.contains(changedConcept)) {
                changedConcepts.add(changedConcept);
            }
        });
    
        Set<SCTConcept> editedConceptSet = SetUtilities.getSetIntersection(allSubhierarchyConcepts, editedConcepts);
        Set<SCTConcept> changedConceptSet = SetUtilities.getSetIntersection(allSubhierarchyConcepts, changedConcepts);
        
        int statedDeltaEntries = 0;
        int inferredDeltaEntries = 0;
        
        // Step 4: Found the total number of relevant delta entries
        for(SCTConcept editedConcept : editedConceptSet) {
            if(statedDeltaRelationships.containsKey(editedConcept)) {
                statedDeltaEntries += statedDeltaRelationships.get(editedConcept).size();
            }
            
            if(inferredDeltaRelationships.containsKey(editedConcept)) {
                inferredDeltaEntries += inferredDeltaRelationships.get(editedConcept).size();
            }
        }
        
        // Step 5: Compute the descriptive delta result
        Set<SCTConcept> subhierarchyRetiredConcepts = SetUtilities.getSetIntersection(beforeConcepts, toInactiveConcepts);
        Set<SCTConcept> subhierarchyActivatedConcepts = SetUtilities.getSetIntersection(afterConcepts, allActivatedConcepts);

        Set<SCTConcept> subhierarchyAddedConcepts = SetUtilities.getSetDifference(afterConcepts, beforeConcepts);
        subhierarchyAddedConcepts = SetUtilities.getSetDifference(subhierarchyAddedConcepts, subhierarchyActivatedConcepts);
        
        Set<SCTConcept> subhierarchyRemovedConcepts = SetUtilities.getSetDifference(beforeConcepts, afterConcepts);
        subhierarchyRemovedConcepts = SetUtilities.getSetDifference(subhierarchyRemovedConcepts, subhierarchyRetiredConcepts);
        
        Map<SCTConcept, EditingOperationReport> statedEditingOperations = identifyEditingOperations(toRelease, editedConceptSet, statedDeltaRelationships);
        Map<SCTConcept, EditingOperationReport> inferredChanges = identifyEditingOperations(toRelease, changedConceptSet, inferredDeltaRelationships);
        
        return new DescriptiveDelta(
                fromRelease, 
                toRelease, 
                subhierarchyRoot,
                subhierarchyRetiredConcepts, 
                subhierarchyActivatedConcepts, 
                subhierarchyRemovedConcepts, 
                subhierarchyAddedConcepts,
                statedDeltaEntries, 
                inferredDeltaEntries, 
                statedEditingOperations, 
                inferredChanges
        );
    }
    
    /**
     * Identifies the editing operations that were applied to a set of concepts,
     * based on the set of delta relationships provided.
     * 
     * @param release
     * @param subhierarchyConcepts
     * @param relChanges
     * 
     * @return 
     */
    public Map<SCTConcept, EditingOperationReport> identifyEditingOperations(
            SCTRelease release,
            Set<SCTConcept> subhierarchyConcepts,
            Map<SCTConcept, Set<DeltaRelationship>> relChanges) {
        
        Map<SCTConcept, EditingOperationReport> reports = new HashMap<>();
        
        final SCTConcept IS_A = release.getConceptFromId(116680003l).get();
        relChanges.forEach((sourceConcept, changes) -> {

            if (subhierarchyConcepts.contains(sourceConcept)) {
                EditingOperationReport report = new EditingOperationReport(sourceConcept);
                
                // Step 1: Identify the sets of activated and retired relationships 
                Set<DeltaRelationship> activatedRelationships = new HashSet<>();
                Set<DeltaRelationship> retiredRelationships = new HashSet<>();
                
                changes.forEach( (change) -> {
                    
                    if (change.isDefining()) {
                        if (change.isActive()) {
                            activatedRelationships.add(change);
                        } else {
                            retiredRelationships.add(change);
                        }
                    }
                });
                
                Set<DeltaRelationship> matchedActiveRels = new HashSet<>();
                
                // Step 2: For each retired relationship, attempt to match it to an activated relationship.
                // If that isn't possible then the relationship was simply removed
                retiredRelationships.forEach((rel) -> {
                    
                    // Step 2a: Handle changes to parents
                    if (rel.getType().equals(IS_A)) {
                        SCTConcept retiredParent = rel.getTarget();
                        
                        Set<DeltaRelationship> moreRefinedParents = getMoreRefinedRels(release, rel, activatedRelationships);

                        if (moreRefinedParents.isEmpty()) {
                            Set<DeltaRelationship> lessRefinedParents = getLessRefinedRels(release, rel, activatedRelationships);

                            if (lessRefinedParents.isEmpty()) {
                                Set<DeltaRelationship> changedParents = getChangedRels(release, rel, activatedRelationships);

                                if (changedParents.isEmpty()) {
                                    report.addRemovedParent(retiredParent);
                                } else {
                                    Set<SCTConcept> potentialParents = new HashSet<>();
                                    
                                    changedParents.forEach( (parentRel) -> {
                                        potentialParents.add(parentRel.getTarget());
                                        
                                        matchedActiveRels.add(parentRel);
                                    });
                                    
                                    report.addChangedParent(new FuzzyParentChange(
                                                IS_A,
                                                retiredParent, 
                                                potentialParents, 
                                                RelationshipRefinementType.Changed));
                                }
                            } else {
                                lessRefinedParents.forEach((parentRel) -> {
                                    report.addLessRefinedParent(new SimpleParentChange(
                                            IS_A,
                                            retiredParent, 
                                            parentRel.getTarget(), 
                                            RelationshipRefinementType.LessRefined));
                                    
                                    
                                    matchedActiveRels.add(parentRel);
                                });
                            }
                        } else {
                            moreRefinedParents.forEach((parentRel) -> {
                                report.addMoreRefinedParent(new SimpleParentChange(
                                        IS_A, 
                                        retiredParent, 
                                        parentRel.getTarget(), 
                                        RelationshipRefinementType.MoreRefined));
                                
                                matchedActiveRels.add(parentRel);
                            });
                        }

                    } else { // Step 2b: Handle a change to an attribute relationship

                        SCTConcept retiredRelType = rel.getType();
                        SCTConcept retiredRelTarget = rel.getTarget();

                        Set<DeltaRelationship> relGroupChanged = getModifiedRelGroup(rel, activatedRelationships);

                        if (relGroupChanged.isEmpty()) {
                            Set<DeltaRelationship> moreRefinedRels = getMoreRefinedRels(release, rel, activatedRelationships);

                            if (moreRefinedRels.isEmpty()) {
                                Set<DeltaRelationship> lessRefinedRels = getLessRefinedRels(release, rel, activatedRelationships);

                                if (lessRefinedRels.isEmpty()) {
                                    Set<DeltaRelationship> changedRels = getChangedRels(release, rel, activatedRelationships);

                                    if (changedRels.isEmpty()) {
                                        report.addRemovedRelationship(rel);
                                    } else {
                                        
                                        Set<SCTConcept> potentialTargets = new HashSet<>();
                                        
                                        changedRels.forEach( (changedRel) -> {
                                            potentialTargets.add(changedRel.getTarget());
                                            
                                            matchedActiveRels.add(changedRel);
                                        });
                                        
                                        report.addChangedRelationship(new FuzzyRelationshipChange(
                                                    retiredRelType,
                                                    retiredRelTarget,
                                                    potentialTargets,
                                                    RelationshipRefinementType.Changed));
                                    }
                                } else {
                                    lessRefinedRels.forEach((refinedRel) -> {
                                        matchedActiveRels.add(refinedRel);

                                        report.addLessRefinedRelationship(new SimpleRelationshipChange(
                                                retiredRelType,
                                                retiredRelTarget,
                                                refinedRel.getTarget(),
                                                RelationshipRefinementType.LessRefined));
                                    });
                                }

                            } else {
                                moreRefinedRels.forEach((refinedRel) -> {

                                    matchedActiveRels.add(refinedRel);

                                    report.addMoreRefinedRelationship(new SimpleRelationshipChange(
                                            retiredRelType,
                                            retiredRelTarget,
                                            refinedRel.getTarget(),
                                            RelationshipRefinementType.MoreRefined));
                                });
                            }

                        } else {
                            Set<Integer> potentialGroups = new HashSet<>();
                            
                            relGroupChanged.forEach((changedRelGroup) -> {
                                matchedActiveRels.add(changedRelGroup);
                                potentialGroups.add(changedRelGroup.getGroup());
                            });
                            
                            report.addRelGroupChangedRelationship(new RelationshipGroupChange(
                                        retiredRelType,
                                        retiredRelTarget,
                                        rel.getGroup(), 
                                        potentialGroups));
                        }
                    }
                });
                
                activatedRelationships.stream().filter( (rel) -> {
                    return !matchedActiveRels.contains(rel);
                }).forEach((rel) -> {
                    if(rel.getType().equals(IS_A)) {
                        report.addNewParent(rel.getTarget());
                    } else {
                        report.addNewRelationships(rel);
                    }
                });
                
                reports.put(sourceConcept, report);
            }
        });
        
        return reports;
    }
    
    /**
     * For a given retired relationship, this method determines if there are 
     * any activated relationships of the same type and target but in a different
     * relationship group. Returns the set of matched activated relationships.
     * 
     * @param retiredRel
     * @param activeRels
     * 
     * @return 
     */
    private Set<DeltaRelationship> getModifiedRelGroup(
            DeltaRelationship retiredRel, 
            Set<DeltaRelationship> activeRels) {
        
        Set<DeltaRelationship> relGroupChanged = new HashSet<>();

        for (DeltaRelationship activeRel : activeRels) {
            if (retiredRel.getType().equals(activeRel.getType())
                    && retiredRel.getTarget().equals(activeRel.getTarget())
                    && retiredRel.getGroup() != activeRel.getGroup()) {

                relGroupChanged.add(activeRel);
            }
        }
        
        return relGroupChanged;
    }
    
    /**
     * For a given retired relationship, tries to find matching 
     * activated relationships of the same type but their 
     * target is a descendant of the original target. Returns
     * the set of matched activated relationships.
     * 
     * @param dataSource
     * @param retiredRel
     * @param activeRels
     * 
     * @return 
     */
    private Set<DeltaRelationship> getMoreRefinedRels(
            SCTRelease dataSource,
            DeltaRelationship retiredRel, 
            Set<DeltaRelationship> activeRels) {
                
        Set<SCTConcept> targetDescendants = dataSource.getConceptHierarchy().getDescendants(retiredRel.getTarget());
        
        Set<DeltaRelationship> targetRefinedRels = new HashSet<>();
        
        for (DeltaRelationship activeRel : activeRels) {
            if (retiredRel.getType().equals(activeRel.getType())
                && !retiredRel.getTarget().equals(activeRel.getTarget())) {

                if(targetDescendants.contains(activeRel.getTarget())) {
                    targetRefinedRels.add(activeRel);
                }
            }
        }
        
        return targetRefinedRels;
    }
    
    /**
     * For a given retired relationship, tries to find matching 
     * activated relationships of the same type but their 
     * target is an ancestor of the original target. Returns
     * the set of matched activated relationships.
     * 
     * @param dataSource
     * @param retiredRel
     * @param activeRels
     * 
     * @return 
     */
    private Set<DeltaRelationship> getLessRefinedRels(
            SCTRelease dataSource,
            DeltaRelationship retiredRel, 
            Set<DeltaRelationship> activeRels) {

        Set<SCTConcept> oldTargetAncestors = dataSource.getConceptHierarchy().getAncestorHierarchy(retiredRel.getTarget()).getNodes();
        
        oldTargetAncestors.remove(retiredRel.getTarget());
        
        Set<DeltaRelationship> targetRefinedRels = new HashSet<>();
        
        for (DeltaRelationship activeRel : activeRels) {
            if (retiredRel.getType().equals(activeRel.getType())
                && !retiredRel.getTarget().equals(activeRel.getTarget())) {
                
                if(oldTargetAncestors.contains(activeRel.getTarget())) {
                    targetRefinedRels.add(activeRel);
                }
            }
        }
        
        return targetRefinedRels;
    }

    /**
     * For a given retired relationship, tries to find matching 
     * activated relationships of the same type but their 
     * target is neither an ancestor or descendant (or equal to)
     * the original target.
     * 
     * @param dataSource
     * @param retiredRel
     * @param activeRels
     * 
     * @return 
     */
    private HashSet<DeltaRelationship> getChangedRels(
            SCTRelease dataSource,
            DeltaRelationship retiredRel, 
            Set<DeltaRelationship> activeRels) {

        HashSet<DeltaRelationship> changedRels = new HashSet<>();
                
        for (DeltaRelationship activeRel : activeRels) {
            if (retiredRel.getType().equals(activeRel.getType())
                && !retiredRel.getTarget().equals(activeRel.getTarget())) {

                changedRels.add(activeRel);
            }
        }
        
        return changedRels;
    }
}
