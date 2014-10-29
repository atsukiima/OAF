package edu.njit.cs.saboc.blu.sno.sctdatasource.middlewareproxy;

import SnomedShared.pareataxonomy.Area;
import SnomedShared.Concept;
import SnomedShared.pareataxonomy.ConceptPAreaInfo;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.InheritedRelationship.InheritanceType;
import SnomedShared.OutgoingLateralRelationship;
import SnomedShared.PAreaDetailsForConcept;
import SnomedShared.pareataxonomy.GroupParentInfo;
import SnomedShared.pareataxonomy.PAreaSummary;
import SnomedShared.pareataxonomy.Region;
import SnomedShared.SearchResult;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import SnomedShared.overlapping.EntryPoint;
import SnomedShared.overlapping.EntryPointSet;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.properties.AreaToolProperties;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRemoteDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.utils.ServletWriter;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import javax.swing.JOptionPane;

/**
 * The singleton class that acts as a proxy so the client can easily send commands and receive data
 * from the middleware servlet. Contains all methods for querying the middleware and
 * the database. 
 * 
 * <b>NOTE: </b> This proxy is based off of the previously developed Neighborhood Auditing Tool's proxy. Eventually it should be replaced
 * with a standard interface, such as JSON.
 * @author Chris
 */
public class MiddlewareAccessorProxy {
    
    
    /**
     * The full address of where the middleware servlet is located
     */
    private static final String webBase =
            AreaToolProperties.getAreaToolProperties().getHostAddress() +
            AreaToolProperties.getAreaToolProperties().getServletLocation() + "SnomedServlet";

    /**
     * The singleton instance of the SnomedAccessorProxy class
     */
    private static MiddlewareAccessorProxy accessorRef;

    /**
     * Creates or returns the singleton instance of the Snomed Accessor Proxy
     * @return The single instance of the SnomedAccessorProxy
     */
    public static MiddlewareAccessorProxy getProxy() {
        if(accessorRef == null) {
            accessorRef = new MiddlewareAccessorProxy();
        }

        return accessorRef;
    }

    /**
     * The versions of SNOMED that are available for use.
     */
    private ArrayList<String> snomedVersions;

    /**
     * The set of our commonly used relationship name abbreviations.
     */
    private HashMap<Long, String> relationshipAbbreviations;

    /**
     * A mapping of releases to sets of SNOMED CT taxonomies.
     */
    private HashMap<String, HashMap<Concept, PAreaTaxonomy>> pareaTaxonomies =
            new HashMap<String, HashMap<Concept, PAreaTaxonomy>>();

    /**
     * A mapping of releases to a set of unique lateral relationships in each SNOMED CT hierarchy.
     */
    private HashMap<String, HashMap<Concept, HashMap<Long, String>>> uniqueLateralRels =
            new HashMap<String, HashMap<Concept, HashMap<Long, String>>>();


    /**
     * A mapping of releases to sets of SNOMED CT Tribal Abstraction Networks.
     */
    private HashMap<String, HashMap<Concept, TribalAbstractionNetwork>> clusterTaxonomies =
            new HashMap<String, HashMap<Concept, TribalAbstractionNetwork>>();

    /**
     * Creates a new SnomedAccessorProxy object. Gets a sessionId from the middleware
     * and gets the available SNOMED versions from the middleware.
     */
    private MiddlewareAccessorProxy() {
        getSupportedSnomedVersions();
    }

    /**
     * Returns the SNOMED CT version as a string located at the specified index in the list of available SCT versions.
     * @param index The index.
     * @return Version name as a string.
     */
    public String getSnomedVersionAtIndex(int index) {
        return snomedVersions.get(index);
    }
    
    public int getVersionIndexFromName(String name) {
        for(int c = 0; c < snomedVersions.size(); c++) {
            if(snomedVersions.get(c).equals(name)) {
                return c;
            }
        }
        
        return -1;
    }

    /**
     * Queries the middleware to get a concept from a ConceptId
     * @param version The version of SNOMED CT used.
     * @param id The ConceptId of the concept that will be loaded
     * @return The concept matched to the ConceptId, if it exists
     */
    public Concept getConceptFromId(String version, long id) {
        Serializable objs[] = {version, "getConceptFromId", id};
        Concept c = (Concept)sendCommand(objs, false);

        return c;
    }

    /**
     * Returns the concepts that are parents of the given concept
     * @param version The version of SNOMED CT used.
     * @param c Concept that is a child of the returned concepts
     * @return The set of concepts that are the parent of the given concept
     */
    public ArrayList<Concept> getConceptParents(String version, Concept c) {
        Serializable objs[] = {version, "getConceptParents", c};
        ArrayList<Concept> parents = (ArrayList<Concept>)sendCommand(objs, false);

        return parents;
    }

    /**
     * Returns the concepts that are the children of the given concept
     * @param version The version of SNOMED CT used.
     * @param c The concept that is the parent of the returned concepts
     * @return The set of concepts that are children of the given concept
     */
    public ArrayList<Concept> getConceptChildren(String version, Concept c) {
        Serializable objs[] = {version, "getConceptChildren", c};
        ArrayList<Concept> children = (ArrayList<Concept>)sendCommand(objs, false);

        return children;
    }

    /**
     * Returns the terms that are synonymous with the given concept
     * @param version The version of SNOMED CT used.
     * @param c A SNOMED CT concept
     * @return The set of terms (as strings) that are a synoynm of the given concept
     */
    public ArrayList<String> getConceptSynoynms(String version, Concept c) {
        Serializable objs[] = {version, "getConceptSynoynms", c};
        ArrayList<String> synonyms = (ArrayList<String>)sendCommand(objs, false);

        return synonyms;
    }

    /***
     * Returns the siblings (other children of a concept's parents) of a concept
     * @param version The version of SNOMED CT used.
     * @param c A SNOMED CT concept.
     * @return The set of concepts that are siblings of c.
     */
    public ArrayList<Concept> getConceptSiblings(String version, Concept c) {
        Serializable objs[] = {version, "getConceptSiblings", c};
        ArrayList<Concept> siblings = (ArrayList<Concept>)sendCommand(objs, false);

        return siblings;
    }

    /**
     * Returns the set of outgoing lateral (non IS_A) relationships with the given concept as the source
     * @param version The version of SNOMED CT used.
     * @param c The source concept of the lateral relationships
     * @return Set of outgoing lateral relationships
     */
    public ArrayList<OutgoingLateralRelationship> getOutgoingLateralRelationships(String version, Concept c) {
        Serializable objs[] = {version, "getOutgoingLateralRels", c};
        ArrayList<OutgoingLateralRelationship> lateralRels = (ArrayList<OutgoingLateralRelationship>)sendCommand(objs, false);

        return lateralRels;
    }

    /**
     * Returns the list of children of the concept "SNOMED CT Concept"
     * that are the roots of the hierarchies of SNOMED CT.
     * @param version The version of SNOMED CT used.
     * @return Set of root concepts of the SNOMED CT hierarchies
     */
    public ArrayList<Concept> getHierarchyRootConcepts(String version) {
        Serializable objs[] = {version, "getHierarchyRootConcepts"};
        ArrayList<Concept> roots = (ArrayList<Concept>)sendCommand(objs, false);

        return roots;
    }
    
    /**
     * Returns a summary of the partial-areas that contain the concept with the specified id.
     * @param version The version of SNOMED CT used.
     * @param conceptId The concept id of a SNOMED CT concept.
     * @return Information on the set of partial-areas that contain this concept.
     */
    public ArrayList<PAreaDetailsForConcept> getSummaryOfPAreasContainingConcept(String version, long conceptId) {
        Serializable objs[] = {version, "getPAreaSummariesForConcept", conceptId};
        ArrayList<PAreaDetailsForConcept> pareaSummaries = (ArrayList<PAreaDetailsForConcept>)sendCommand(objs, false);
    
        return pareaSummaries;
    }

    /**
     * Returns the concepts in a given partial-area within a given hierarchy.
     * @param version The version of SNOMED CT used.
     * @param hierarchyRoot The root concept of the hierarchy
     * @param pareaRoot The root concept of the PArea
     * @return Set of concepts within the PArea
     */
    public ArrayList<Concept> getConceptsInPArea(String version, Concept hierarchyRoot, Concept pareaRoot) {
        Serializable objs[] = {version, "getConceptsInPArea", hierarchyRoot, pareaRoot};
        ArrayList<Concept> concepts = (ArrayList<Concept>) sendCommand(objs, false);

        return concepts;
    }

    /**
     * Returns the concepts in a set of PAreas identified by their hierarchy ID and the Concept IDs of
     * roots of the PAreas in the set.
     * @param version The version of SNOMED CT used.
     * @param hierarchyRoot Root concepts of the SNOMED hierarchy
     * @param pareaRoots ConceptIds of the roots of the PAreas in the set
     * @return Mapping of PArea root concept IDs to list of concepts
     */
    public HashMap<Long, ArrayList<Concept>> getConceptsInPAreaSet(String version, Concept hierarchyRoot, ArrayList<Long> pareaRoots) {
        Serializable objs[] = {version, "getConceptsInPAreaSet", hierarchyRoot, pareaRoots};
        HashMap<Long,ArrayList<Concept>> concepts = (HashMap<Long,ArrayList<Concept>>) sendCommand(objs, false);

        return concepts;
    }

    /**
     * Returns the concepts in a given tribal cluster in a given hierarchy.
     * @param version The version of SNOMED CT used.
     * @param hierarchyRoot The root concept of the hierarchy
     * @param clusterRoot The root concept of the cluster
     * @return 
     */
    public ArrayList<Concept> getConceptsInCluster(String version, Concept hierarchyRoot, Concept clusterRoot) {
        Serializable objs[] = {version, "getConceptsInCluster", hierarchyRoot, clusterRoot};
        ArrayList<Concept> concepts = (ArrayList<Concept>) sendCommand(objs, false);

        return concepts;
    }

    /**
     * Returns the concepts in a set of tribal clustered identified by their hierarchy ID and the Concept IDs of
     * roots of the clusters in the set.
     * @param version The version of SNOMED CT used.
     * @param hierarchyRoot Root concepts of the SNOMED hierarchy
     * @param clusterRoots ConceptIds of the roots of the clusters in the set
     * @return Mapping of Cluster root concept IDs to lists of concepts
     */
    public HashMap<Long, ArrayList<Concept>> getConceptsInClusterSet(String version, 
            Concept hierarchyRoot, ArrayList<Long> clusterRoots) {

        Serializable objs[] = {version, "getConceptsInClusterSet", hierarchyRoot, clusterRoots};
        HashMap<Long,ArrayList<Concept>> concepts = (HashMap<Long,ArrayList<Concept>>) sendCommand(objs, false);

        return concepts;
    }

    /**
     * Returns the set of areas that exist within in the hierarchy that corresponds
     * to the given root.
     * @param version The version of SNOMED CT used.
     * @param hierarchyRoot The root of the hierarchy
     * @return The set of areas that exist in the hierarchy
     */
    public PAreaTaxonomy getPAreaHierarchyData(String version, Concept hierarchyRoot) {
        if(!pareaTaxonomies.containsKey(version)) {
            pareaTaxonomies.put(version, new HashMap<Concept, PAreaTaxonomy>());
        }

        if(!pareaTaxonomies.get(version).containsKey(hierarchyRoot)) {
            pareaTaxonomies.get(version).put(hierarchyRoot, loadPAreaHierarchyData(version, hierarchyRoot));
        }

        return pareaTaxonomies.get(version).get(hierarchyRoot);
    }

    /**
     * Returns the set of tribal bands that exist within in the hierarchy that corresponds
     * to the given root.
     * @param version The version of SNOMED CT used.
     * @param hierarchyRoot The root of the hierarchy
     * @return The set of tribal bands that exist in the hierarchy
     */
    public TribalAbstractionNetwork getClusterHierarchyData(String version, Concept hierarchyRoot) {
        if(!clusterTaxonomies.containsKey(version)) {
            clusterTaxonomies.put(version, new HashMap<Concept, TribalAbstractionNetwork>());
        }

        if(!clusterTaxonomies.get(version).containsKey(hierarchyRoot)) {
            clusterTaxonomies.get(version).put(hierarchyRoot, loadClusterHierarchyData(version, hierarchyRoot));
        }

        return clusterTaxonomies.get(version).get(hierarchyRoot);
    }

    /**
     * Searches for terms that exactly match the given term
     * @param version The version of SNOMED CT used.
     * @param term The term that will be matched to other terms
     * @return Set of search results that exactly match the entered term
     */
    public ArrayList<SearchResult> searchExact(String version, String term) {
        Serializable objs[] = {version, "searchExact", term};
        ArrayList<SearchResult> results = (ArrayList<SearchResult>)sendCommand(objs, false);

        return results;
    }

    /**
     * Searches for descriptions that start with the given term
     * @param version The version of SNOMED CT used.
     * @param term The term that will be matched to other terms
     * @return Set of search results that start with the entered term
     */
    public ArrayList<SearchResult> searchStarting(String version, String term) {
        Serializable objs[] = {version, "searchStarting", term};
        ArrayList<SearchResult> results = (ArrayList<SearchResult>)sendCommand(objs, false);

        return results;
    }

    /**
     * Searches for descriptions that contain the given term
     * @param version The version of SNOMED CT used.
     * @param term The term that will be matched to other terms
     * @return Set of search results that contain the entered term
     */
    public ArrayList<SearchResult> searchAnywhere(String version, String term) {
        Serializable objs[] = {version, "searchAnywhere", term};
        ArrayList<SearchResult> results = (ArrayList<SearchResult>)sendCommand(objs, false);

        return results;
    }

    /**
     * Returns the set of lateral relationships that exist within a given hierarchy as
     * a HashMap of relationshiptype mapped to fully specified name
     * @param version The version of SNOMED CT used.
     * @param hierarchyRoot The root concept of the hierarchy
     * @return Map of ConceptIds (relationshiptypes) to relationship name
     */
    public HashMap<Long, String> getUniqueLateralRelationshipsInHierarchy(String version, Concept hierarchyRoot) {

        if (!uniqueLateralRels.containsKey(version)) {
            uniqueLateralRels.put(version, new HashMap<Concept, HashMap<Long, String>>());
        }

        if (!uniqueLateralRels.get(version).containsKey(hierarchyRoot)) {
            Serializable objs[] = {version, "getUniqueLateralRelsInHierarchy", hierarchyRoot};
            HashMap<Long, String> results = (HashMap<Long, String>) sendCommand(objs, false);
            
            uniqueLateralRels.get(version).put(hierarchyRoot, results);
        }

        return uniqueLateralRels.get(version).get(hierarchyRoot);
    }

    /**
     * Returns the total number of unique concepts in a given set of partial-areas.
     * @param version The version of SNOMED CT used.
     * @param hierarchyRoot The SNOMED CT hierarchy root concept
     * @param pareaIds List of PArea Ids
     * @return Number of unique concepts in the set of PAreas identified by the given PArea IDs
     */
    public int getConceptCountInPAreaHierarchy(String version, Concept hierarchyRoot, ArrayList<Integer> pareaIds) {
        Serializable objs[] = {version, "getConceptCountInPAreaHierarchy", hierarchyRoot, pareaIds};
        int result = (Integer) sendCommand(objs, false);

        return result;
    }

     /**
     * Returns the total number of unique concepts in a given set of tribal clusters.
     * @param version The version of SNOMED CT used.
     * @param hierarchyRoot The SNOMED CT hierarchy root concept
     * @param pareaIds List of Cluster Ids
     * @return Number of unique concepts in the set of clusters identified by the given cluster IDs
     */
    public int getConceptCountInClusterHierarchy(String version, Concept hierarchyRoot, ArrayList<Integer> clusterIds) {
        Serializable objs[] = {version, "getConceptCountInClusterHierarchy", hierarchyRoot, clusterIds};
        int result = (Integer) sendCommand(objs, false);

        return result;
    }

    /**
     * General method to send an HTTP message to the middleware servlet.
     * @param objs Array of objects that will be sent to the servlet. First element of array is always
     * the name of the command that is being send
     * @param isVoid If the command will return an object or not return anything. False if return is expected.
     * @return Null if void, some object is middleware successfully handled request
     */
    private Object sendCommand(Serializable objs[], boolean isVoid) 
    {   
        try 
        {
            URL servlet = new URL(webBase);
            ObjectInputStream in = ServletWriter.postObjects(servlet, objs);
            Object ret = isVoid ? null : in.readObject();
            
            if(ret instanceof String) {
                System.out.println(ret);
            }

            in.close();
            return ret;
        }
        catch(ConnectException ce) 
        {
            JOptionPane.showMessageDialog(null,
                    "You have lost connection to the SNOMED service.\nPlease check " +
                    "your internet connection and restart the Snomed Tool.\nIf the problem " +
                    "persists please contact cro3@njit.edu.",
                    "ERROR: Connection Lost",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(-1);
        }
        catch(Exception e) 
        {
            // Invokes method in ConnectionStateMonitor to record an exception occurence.  If more
            // than 3 exceptions occur within 20 seconds of each other, a ConnectionStateMonitor 
            // thread begins to monitor whether the connection is alive and which GUI elements to 
            // enable/disable depending on the connection's status. - John
            
//            MainToolFrame.getMainFrame().getConnectionStateMonitor().recordConnectionError();
//            System.out.println("ERROR calling meta-function " + (String)objs[1] + " with args " + Arrays.toString(objs) + ":\n" + e);
//            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * Loads PArea Taxonomy data from an XML file.
     * @param version The version of SNOMED CT used.
     * @param hierarchyRoot The root concept of a SNOMED CT hierarchy.
     * @return Returns the taxonomy data for the given version of the given hierarchy.
     */
    private PAreaTaxonomy loadPAreaHierarchyData(String version, Concept hierarchyRoot) {
//        try {
//            Scanner scanner = new Scanner(MainToolFrame.class.getResourceAsStream("pareadata/" + version.replaceAll("_", "") + "/" + hierarchyRoot.getId() +
//                    "_" + version.replaceAll("us", "US") + ".txt"));
//
//            StringBuilder builder = new StringBuilder();
//
//            while(scanner.hasNext()) {
//                String line = scanner.nextLine().trim().replace("\n", "");
//
//                builder.append(line);
//                builder.append("\n");
//            }
//            
//            String input = builder.toString().replaceAll("\n\n", "\n");
//
//            scanner.close();
//            
//            scanner = new Scanner(input);
//
//            ArrayList<Area> areas = new ArrayList<Area>();
//            HashMap<Integer, PAreaSummary> pareas = new HashMap<Integer, PAreaSummary>();
//            HashMap<Integer, HashSet<Integer>> pareaHierarchy = new HashMap<Integer, HashSet<Integer>>();
//            
//            final HashMap<Long, String> hierarchyRels = this.getUniqueLateralRelationshipsInHierarchy(version, hierarchyRoot);
//
//            while (scanner.hasNext()) {
//                String line = scanner.nextLine();
//
//                if (line.startsWith("<area")) { // Read the area
//                    String areaIdStr = line.substring(line.indexOf("id=") + 3, line.length() - 1); // Get the AreaId
//                    int id = Integer.parseInt(areaIdStr);
//
//                    Area area = new Area(id, false);
//
//                    areas.add(area);
//
//                    line = scanner.nextLine().trim();
//
//                    boolean newArea = true;
//
//                    while (line.equals("<region>")) {
//                        ArrayList<InheritedRelationship> rels = new ArrayList<InheritedRelationship>();
//
//                        if (id != 0) {
//                            while ((line = scanner.nextLine()).trim().startsWith("<rel")) {
//                                String typeStr = line.substring(line.indexOf("type=") + 5, line.indexOf("inherit=") - 1);
//                                String inherit = line.substring(line.indexOf("inherit=") + 8, line.length() - 1);
//                                long typeId = Long.parseLong(typeStr);
//
//                                InheritanceType type = inherit.equals("*") ? InheritanceType.INHERITED : InheritanceType.INTRODUCED;
//
//                                rels.add(new InheritedRelationship(type, typeId));
//                            }
//
//                            Collections.sort(rels, new Comparator<InheritedRelationship>() {
//                                public int compare(InheritedRelationship a, InheritedRelationship b) {
//                                    String aStr = hierarchyRels.get(a.getRelationshipTypeId());
//                                    String bStr = hierarchyRels.get(b.getRelationshipTypeId());
//
//                                    return aStr.compareToIgnoreCase(bStr);
//                                }
//                            });
//
//                            if(newArea) {
//                                ArrayList<Long> relIds = new ArrayList<Long>();
//
//                                for(InheritedRelationship ir : rels) {
//                                    relIds.add(ir.getRelationshipTypeId());
//                                }
//
//                                area.setRels(relIds);
//
//                                newArea = false;
//                            }
//                        } else {
//                            line = scanner.nextLine().trim();
//                        }
//
//                        do {
//                            String pareaIdStr = line.substring(line.indexOf("id=") + 3, line.indexOf("pids") - 1);
//                            String pidsStr = line.substring(line.indexOf("pids=") + 5, line.indexOf("ccount") - 1);
//                            String ccountStr = line.substring(line.indexOf("ccount=") + 7, line.length() - 1);
//
//                            int pareaId = Integer.parseInt(pareaIdStr);
//
//                            String[] pidStrs = pidsStr.split(",");
//                            HashSet<Integer> pids = new HashSet<Integer>();
//
//                            if (id != 0) {
//                                for (String s : pidStrs) {
//                                    pids.add(Integer.parseInt(s));
//                                }
//                            }
//
//                            int conceptCount = Integer.parseInt(ccountStr);
//
//                            line = scanner.nextLine().trim();
//                            
//                            String conceptName = "NULL CONCEPT";
//                            long conceptid = -1;
//
//                            if (line.startsWith("<root")) {
//                                String cidStr = line.substring(line.indexOf("id=") + 3, line.length() - 1);
//
//                                conceptid = Long.parseLong(cidStr);
//                                conceptName = scanner.nextLine().trim();
//                                line = scanner.nextLine().trim();
//                            }
//
//                            PAreaSummary parea = new PAreaSummary(pareaId, new Concept(conceptid, conceptName, false), conceptCount, pids);
//                            parea.setRelationships(rels);
//
//                            pareas.put(pareaId, parea);
//
//                            for(int pid : pids) {
//                                HashSet<Integer> parentChildren;
//
//                                if(!pareaHierarchy.containsKey(pid)) {
//                                    pareaHierarchy.put(pid, parentChildren = new HashSet<Integer>());
//                                } else {
//                                    parentChildren = pareaHierarchy.get(pid);
//                                }
//
//                                parentChildren.add(pareaId);
//                            }
//                            
//                            area.addPArea(parea);
//
//                            line = scanner.nextLine().trim();
//                        } while ((line = scanner.nextLine()).trim().startsWith("<parea"));
//
//                        line = scanner.nextLine().trim();
//                    }
//                }
//            }
//
//            scanner.close();
//
//            for(Area a : areas) {
//                for(Region region : a.getRegions()) {
//                    ArrayList<PAreaSummary> summaries = region.getPAreasInRegion();
//
//                    Collections.sort(summaries, new Comparator<PAreaSummary>() {
//                        public int compare(PAreaSummary a, PAreaSummary b) {
//                            if (a.getConceptCount() == b.getConceptCount()) {
//                                return a.getRoot().getName().compareToIgnoreCase(b.getRoot().getName());
//                            }
//
//                            return a.getConceptCount() > b.getConceptCount() ? -1 : 1;
//                        }
//                    });
//                }
//            }
//
//            PAreaTaxonomy hd = new PAreaTaxonomy(hierarchyRoot, pareas.get(0), areas, pareas, pareaHierarchy,
//                    hierarchyRels, version, new SCTRemoteDataSource(version));
//
//            return hd;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        
        return null;
    }

    public TribalAbstractionNetwork loadClusterHierarchyData(String version, Concept hierarchyRoot) {
//        try {
//            Scanner scanner = new Scanner(MainToolFrame.class.getResourceAsStream(
//                    "clusterdata/" + version.replaceAll("_", "") + "/" + hierarchyRoot.getId() +
//                    "_" + version + "_overlap.txt"));
//
//            ArrayList<String> lines = new ArrayList<String>();
//
//            while(scanner.hasNext()) {
//                lines.add(scanner.nextLine());
//            }
//
//            String strHierarchyId = lines.get(0).split("hierarchy=")[1];
//            strHierarchyId = strHierarchyId.substring(0, strHierarchyId.length() - 1);
//
//            HashMap<Integer, HashSet<Integer>> clusterHierarchy = new HashMap<Integer, HashSet<Integer>>();
//            HashMap<Integer, ClusterSummary> clusters = new HashMap<Integer, ClusterSummary>();
//
//            ArrayList<ClusterSummary> entryPoints = new ArrayList<ClusterSummary>();
//
//            for(int i = 1; i < lines.size() - 1; i++) {
//                String summaryLine = lines.get(i);
//
//                String clusterIdStr = summaryLine.substring(summaryLine.indexOf("id=") + 3, summaryLine.indexOf("pids") - 1).trim();
//                String pidsStr = summaryLine.substring(summaryLine.indexOf("pids=") + 5, summaryLine.indexOf("ccount") - 1).trim();
//                String ccountStr = summaryLine.substring(summaryLine.indexOf("ccount=") + 7, summaryLine.indexOf(">")).trim();
//
//                HashSet<Integer> parents = new HashSet<Integer>();
//
//                String [] pids = pidsStr.split(",");
//
//                if(pids.length != 0) {
//                    for(String pid : pids) {
//                        if(!pid.trim().isEmpty()) {
//                            parents.add(Integer.parseInt(pid));
//                        }
//                    }
//                }
//
//                String entryPointSetStr = summaryLine.substring(summaryLine.indexOf("<entrypointset>") + 15, summaryLine.indexOf("</entrypointset>"));
//
//                String [] entryPointStrs = entryPointSetStr.split("/>");
//
//                EntryPointSet epSet = new EntryPointSet();
//
//                for (String entryPointStr : entryPointStrs) {
//                    String entryPointIdStr = entryPointStr.substring(entryPointStr.indexOf("id=") + 3, entryPointStr.indexOf("inherit") - 1).trim();
//                    String inheritStr = entryPointStr.substring(entryPointStr.length() - 1);
//
//                    epSet.add(new EntryPoint(Long.parseLong(entryPointIdStr),
//                            (inheritStr.equals("+") ? EntryPoint.InheritanceType.INTRODUCED : EntryPoint.InheritanceType.INHERITED)));
//                }
//
//                String headerStr = summaryLine.substring(summaryLine.indexOf("<header"), summaryLine.indexOf("</header>"));
//                String headerIdStr = headerStr.substring(headerStr.indexOf("id=") + 3, headerStr.indexOf(">"));
//                String headerNameStr = headerStr.substring(headerStr.indexOf(">") + 1);
//
//                Concept header = new Concept(Long.parseLong(headerIdStr), headerNameStr);
//
//                ClusterSummary cluster = new ClusterSummary(Integer.parseInt(clusterIdStr),
//                        header,
//                        Integer.parseInt(ccountStr),
//                        parents,
//                        epSet);
//
//                clusters.put(cluster.getId(), cluster);
//
//                if(cluster.getEntryPointSet().size() == 1) {
//                    entryPoints.add(cluster);
//                }
//
//                for (int parent : parents) {
//                    HashSet<Integer> parentChildren;
//
//                    if (!clusterHierarchy.containsKey(parent)) {
//                        clusterHierarchy.put(parent, parentChildren = new HashSet<Integer>());
//                    } else {
//                        parentChildren = clusterHierarchy.get(parent);
//                    }
//
//                    parentChildren.add(cluster.getId());
//                }
//            }
//
//            ArrayList<CommonOverlapSet> commonOverlapSets = new ArrayList<CommonOverlapSet>();
//
//            int areaId = 0;
//
//            for(ClusterSummary cluster : clusters.values()) {
//
//                if(cluster.getEntryPointSet().size() == 1) {
//                    continue;
//                }
//
//                boolean setFound = false;
//
//                for(CommonOverlapSet set : commonOverlapSets) {
//                    if(set.clusterBelongsIn(cluster)) {
//                        set.addClusterToSet(cluster);
//                        setFound = true;
//                        break;
//                    }
//                }
//
//                if(!setFound) {
//                    areaId++;
//
//                    HashSet<Long> epSet = new HashSet<Long>();
//
//                    for(EntryPoint ep : cluster.getEntryPointSet()) {
//                        epSet.add(ep.getEntryPointConceptId());
//                    }
//
//                    CommonOverlapSet cos = new CommonOverlapSet(areaId, epSet);
//                    cos.addClusterToSet(cluster);
//                    commonOverlapSets.add(cos);
//                }
//            }
//            
//            ArrayList<ClusterSummary> nonOverlappingEntryPoints = new ArrayList<ClusterSummary>();
//
//            for(ClusterSummary entryPoint : entryPoints) {
//                boolean overlaps = false;
//
//                long rootId = entryPoint.getHeaderConcept().getId();
//
//                for(ClusterSummary cluster : clusters.values()) {
//                    for(EntryPoint ep : cluster.getEntryPointSet()) {
//                        if(ep.getEntryPointConceptId() == rootId) {
//                            overlaps = true;
//                            break;
//                        }
//                    }
//
//                    if(!overlaps) {
//                        nonOverlappingEntryPoints.add(entryPoint);
//                    }
//                }
//            }
//
//            scanner.close();
//
//            return new TribalAbstractionNetwork(hierarchyRoot, commonOverlapSets, clusters,
//                    clusterHierarchy, version, entryPoints, nonOverlappingEntryPoints,
//                    new SCTRemoteDataSource(version));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        
        return null;
    }

    public ArrayList<ConceptPAreaInfo> getConceptPAreaInfo(String version, long conceptId) {
        Serializable objs[] = {version, "getPAreasContainingConcept", conceptId};
        ArrayList<ConceptPAreaInfo> pareas = (ArrayList<ConceptPAreaInfo>) sendCommand(objs, false);

        return pareas;
    }

    public ArrayList<GroupParentInfo> getPAreaParentInfo(String version, Concept hierarchyRoot, PAreaSummary parea) {
        Serializable objs[] = {version, "getPAreaParentInfo", hierarchyRoot, parea.getRoot().getId()};
        ArrayList<GroupParentInfo> results = (ArrayList<GroupParentInfo>) sendCommand(objs, false);

        return results;
    }

    public ArrayList<GroupParentInfo> getClusterParentInfo(String version, Concept hierarchyRoot, ClusterSummary cluster) {
        Serializable objs[] = {version, "getClusterParentInfo", hierarchyRoot, cluster.getRoot().getId()};
        ArrayList<GroupParentInfo> results = (ArrayList<GroupParentInfo>) sendCommand(objs, false);

        return results;
    }

    public HashMap<Concept, ArrayList<Concept>> getPAreaConceptHierarchy(String version, long hierarchyRootId, long pareaRootConceptId) {
        Serializable objs[] = {version, "getPAreaConceptHierarchy", hierarchyRootId, pareaRootConceptId};
        HashMap<Concept, ArrayList<Concept>> results = (HashMap<Concept, ArrayList<Concept>>)sendCommand(objs, false);

        return results;
    }

    public HashMap<Concept, ArrayList<Concept>> getClusterConceptHierarchy(String version, long hierarchyRootId, long clusterRootConceptId) {
        Serializable objs[] = {version, "getClusterConceptHierarchy", hierarchyRootId, clusterRootConceptId};
        HashMap<Concept, ArrayList<Concept>> results = (HashMap<Concept, ArrayList<Concept>>)sendCommand(objs, false);

        return results;
    }

    public HashMap<Concept, ArrayList<Concept>> getRegionConceptHierarchy(String version, long hierarchyRootId,
            ArrayList<Long> pareaRootConceptIds) {
        Serializable objs[] = {version, "getRegionConceptHierarchy", hierarchyRootId, pareaRootConceptIds};
        HashMap<Concept, ArrayList<Concept>> results = (HashMap<Concept, ArrayList<Concept>>)sendCommand(objs, false);

        return results;
    }

    public ArrayList<SearchResult> searchAnywhereWithinHierarchy(String version, long hierarchyRootId,
            ArrayList<Integer> pareaIds, String term) {
        Serializable objs[] = {version, "searchAnywhereWithinHierarchy", hierarchyRootId, pareaIds, term};
        ArrayList<SearchResult> results = (ArrayList<SearchResult>) sendCommand(objs, false);

        return results;
    }

    public ArrayList<String> getSupportedSnomedVersions() {
        if (this.snomedVersions == null) {
            Serializable objs[] = {"jan_2011", "getSupportedVersions"};
            snomedVersions = (ArrayList<String>) sendCommand(objs, false);
        }

        return snomedVersions;
    }

    public HashMap<Long, String> getRelationshipAbbreviations(String version) {
        if (this.relationshipAbbreviations == null) {
            Serializable objs[] = {version, "getRelationshipAbbreviations"};
            relationshipAbbreviations = (HashMap<Long, String>) sendCommand(objs, false);
        }

        return relationshipAbbreviations;
    }
}
