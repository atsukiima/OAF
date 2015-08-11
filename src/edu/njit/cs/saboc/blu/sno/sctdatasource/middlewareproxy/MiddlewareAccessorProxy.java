package edu.njit.cs.saboc.blu.sno.sctdatasource.middlewareproxy;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.ConceptPAreaInfo;
import SnomedShared.OutgoingLateralRelationship;
import SnomedShared.PAreaDetailsForConcept;
import SnomedShared.pareataxonomy.GroupParentInfo;
import SnomedShared.pareataxonomy.PAreaSummary;
import SnomedShared.SearchResult;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.pareataxonomy.Area;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.InheritedRelationship.InheritanceType;
import SnomedShared.pareataxonomy.Region;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.sno.abn.generator.InheritedRelWithHash;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.remote.RemoteSCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.properties.AreaToolProperties;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRemoteDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.utils.ServletWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
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
    private HashMap<String, HashMap<Concept, SCTPAreaTaxonomy>> pareaTaxonomies =
            new HashMap<String, HashMap<Concept, SCTPAreaTaxonomy>>();

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
    public SCTPAreaTaxonomy getPAreaHierarchyData(String version, Concept hierarchyRoot) {
        if(!pareaTaxonomies.containsKey(version)) {
            pareaTaxonomies.put(version, new HashMap<Concept, SCTPAreaTaxonomy>());
        }

        if(!pareaTaxonomies.get(version).containsKey(hierarchyRoot)) {
            pareaTaxonomies.get(version).put(hierarchyRoot, loadPAreaHierarchyData(version, hierarchyRoot));
        }

        return pareaTaxonomies.get(version).get(hierarchyRoot);
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
        catch(Exception e) {
            return null;
        }

        return null;
    }
    
    /**
     * TODO: Cache this data locally.
     * 
     * @param version
     * @param hierarchyRoot
     * @return 
     */
    private String loadRemotePAreaTaxonomyDataFile(String version, Concept hierarchyRoot) {
        InputStream inputStream = null;
        
        try {
            URL url = new URL(
                    String.format("http://nat.njit.edu/blusnodata/pareadata/%s/%s_%s.txt",
                            version.replaceAll("_", ""), hierarchyRoot.getId(), version.replaceAll("us", "US")));

            inputStream = url.openStream();  // throws an IOException
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            StringBuilder builder = new StringBuilder();
            
            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            
            return builder.toString();
            
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ioe) {
            }
        }
        
        return null;
    }

    /**
     * Loads PArea Taxonomy data from an XML file.
     * 
     * @param version The version of SNOMED CT used.
     * @param hierarchyRoot The root concept of a SNOMED CT hierarchy.
     * @return Returns the taxonomy data for the given version of the given hierarchy.
     */
    private SCTPAreaTaxonomy loadPAreaHierarchyData(String version, Concept hierarchyRoot) {
        try {
            String taxonomyFile = loadRemotePAreaTaxonomyDataFile(version, hierarchyRoot);
            
            if(taxonomyFile == null) {
                return null;
            }
            
            Scanner scanner = new Scanner(taxonomyFile);

            StringBuilder builder = new StringBuilder();

            while(scanner.hasNext()) {
                String line = scanner.nextLine().trim().replace("\n", "");

                builder.append(line);
                builder.append("\n");
            }
            
            String input = builder.toString().replaceAll("\n\n", "\n");

            scanner.close();
            
            scanner = new Scanner(input);

            ArrayList<Area> areas = new ArrayList<Area>();
            HashMap<Integer, PAreaSummary> pareas = new HashMap<Integer, PAreaSummary>();
            
            HashMap<Integer, HashSet<Integer>> pareaHierarchy = new HashMap<Integer, HashSet<Integer>>();
            
            final HashMap<Long, String> hierarchyRels = this.getUniqueLateralRelationshipsInHierarchy(version, hierarchyRoot);

            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (line.startsWith("<area")) { // Read the area
                    String areaIdStr = line.substring(line.indexOf("id=") + 3, line.length() - 1); // Get the AreaId
                    int id = Integer.parseInt(areaIdStr);

                    Area area = new Area(id, false);

                    areas.add(area);

                    line = scanner.nextLine().trim();

                    boolean newArea = true;

                    while (line.equals("<region>")) {
                        ArrayList<InheritedRelationship> rels = new ArrayList<InheritedRelationship>();

                        if (id != 0) {
                            while ((line = scanner.nextLine()).trim().startsWith("<rel")) {
                                String typeStr = line.substring(line.indexOf("type=") + 5, line.indexOf("inherit=") - 1);
                                String inherit = line.substring(line.indexOf("inherit=") + 8, line.length() - 1);
                                long typeId = Long.parseLong(typeStr);

                                InheritanceType type = inherit.equals("*") ? InheritanceType.INHERITED : InheritanceType.INTRODUCED;

                                rels.add(new InheritedRelationship(type, typeId));
                            }

                            Collections.sort(rels, new Comparator<InheritedRelationship>() {
                                public int compare(InheritedRelationship a, InheritedRelationship b) {
                                    String aStr = hierarchyRels.get(a.getRelationshipTypeId());
                                    String bStr = hierarchyRels.get(b.getRelationshipTypeId());

                                    return aStr.compareToIgnoreCase(bStr);
                                }
                            });

                            if(newArea) {
                                ArrayList<Long> relIds = new ArrayList<Long>();

                                for(InheritedRelationship ir : rels) {
                                    relIds.add(ir.getRelationshipTypeId());
                                }

                                area.setRels(relIds);

                                newArea = false;
                            }
                        } else {
                            line = scanner.nextLine().trim();
                        }

                        do {
                            String pareaIdStr = line.substring(line.indexOf("id=") + 3, line.indexOf("pids") - 1);
                            String pidsStr = line.substring(line.indexOf("pids=") + 5, line.indexOf("ccount") - 1);
                            String ccountStr = line.substring(line.indexOf("ccount=") + 7, line.length() - 1);

                            int pareaId = Integer.parseInt(pareaIdStr);

                            String[] pidStrs = pidsStr.split(",");
                            HashSet<Integer> pids = new HashSet<Integer>();

                            if (id != 0) {
                                for (String s : pidStrs) {
                                    pids.add(Integer.parseInt(s));
                                }
                            }

                            int conceptCount = Integer.parseInt(ccountStr);

                            line = scanner.nextLine().trim();
                            
                            String conceptName = "NULL CONCEPT";
                            long conceptid = -1;

                            if (line.startsWith("<root")) {
                                String cidStr = line.substring(line.indexOf("id=") + 3, line.length() - 1);

                                conceptid = Long.parseLong(cidStr);
                                conceptName = scanner.nextLine().trim();
                                line = scanner.nextLine().trim();
                            }

                            PAreaSummary parea = new PAreaSummary(pareaId, new Concept(conceptid, conceptName, false), conceptCount, pids);
                            parea.setRelationships(rels);

                            pareas.put(pareaId, parea);

                            for(int pid : pids) {
                                HashSet<Integer> parentChildren;

                                if(!pareaHierarchy.containsKey(pid)) {
                                    pareaHierarchy.put(pid, parentChildren = new HashSet<Integer>());
                                } else {
                                    parentChildren = pareaHierarchy.get(pid);
                                }

                                parentChildren.add(pareaId);
                            }
                            
                            area.addPArea(parea);

                            line = scanner.nextLine().trim();
                        } while ((line = scanner.nextLine()).trim().startsWith("<parea"));

                        line = scanner.nextLine().trim();
                    }
                }
            }

            scanner.close();

            for(Area a : areas) {
                for(Region region : a.getRegions()) {
                    ArrayList<PAreaSummary> summaries = region.getPAreasInRegion();

                    Collections.sort(summaries, new Comparator<PAreaSummary>() {
                        public int compare(PAreaSummary a, PAreaSummary b) {
                            if (a.getConceptCount() == b.getConceptCount()) {
                                return a.getRoot().getName().compareToIgnoreCase(b.getRoot().getName());
                            }

                            return a.getConceptCount() > b.getConceptCount() ? -1 : 1;
                        }
                    });
                }
            }
            
            ArrayList<SCTArea> convertedAreas = new ArrayList<SCTArea>();
            
            HashMap<Integer, SCTPArea> convertedPAreas = new HashMap<Integer, SCTPArea>();
            
            for(Area a : areas) {
                HashSet<InheritedRelationship> areaRels = new HashSet<InheritedRelationship>();
                
                for(long relId : a.getRelationships()) {
                    areaRels.add(new InheritedRelWithHash(InheritanceType.INHERITED, relId));
                }
                
                SCTArea convertedArea = new SCTArea(a.getId(), areaRels);
                
                ArrayList<PAreaSummary> areaPAreas = a.getAllPAreas();
                
                for(PAreaSummary parea : areaPAreas) {
                    RemoteSCTPArea remotePArea = new RemoteSCTPArea(new SCTRemoteDataSource(version), parea);
                    
                    convertedArea.addPArea(remotePArea);
                    
                    convertedPAreas.put(parea.getId(), remotePArea);
                }
                
                convertedAreas.add(convertedArea);
            }
            
            GroupHierarchy<SCTPArea> convertedPAreaHierarchy = new GroupHierarchy<>(convertedPAreas.get(0));
            
            convertedPAreas.values().forEach((SCTPArea parea) -> {
                HashSet<Integer> parentIds = parea.getParentIds();
                
                parentIds.forEach((Integer parentId) -> {
                    convertedPAreaHierarchy.addIsA(parea, convertedPAreas.get(parentId));
                });
            });
            
            SCTPAreaTaxonomy taxonomy = new SCTPAreaTaxonomy(hierarchyRoot, version, new SCTRemoteDataSource(version), 
                    null, convertedPAreas.get(0), convertedAreas, convertedPAreas, convertedPAreaHierarchy, hierarchyRels);

            return taxonomy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<ConceptPAreaInfo> getConceptPAreaInfo(String version, long conceptId) {
        Serializable objs[] = {version, "getPAreasContainingConcept", conceptId};
        ArrayList<ConceptPAreaInfo> pareas = (ArrayList<ConceptPAreaInfo>) sendCommand(objs, false);

        return pareas;
    }

    public ArrayList<GroupParentInfo> getPAreaParentInfo(String version, Concept hierarchyRoot, SCTPArea parea) {
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
