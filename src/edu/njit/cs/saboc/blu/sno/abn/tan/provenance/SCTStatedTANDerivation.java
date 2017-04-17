package edu.njit.cs.saboc.blu.sno.abn.tan.provenance;

import edu.njit.cs.saboc.blu.core.abn.tan.provenance.SimpleClusterTANDerivation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Stores the arguments needed to create a TAN from the stated
 * hierarchical relationships of a hierarchy of concepts
 * 
 * @author Chris O
 */
public class SCTStatedTANDerivation extends SimpleClusterTANDerivation {
    
    private final SimpleClusterTANDerivation base;
    
    public SCTStatedTANDerivation(SimpleClusterTANDerivation base) {
        super(base);
        this.base= base;
    }

    @Override
    public String getDescription() {
        return "Derived Stated Cluster TAN";
    }
    
    @Override
    public JSONArray serializeToJSON() {
        JSONArray result = new JSONArray();
        
        //serialize class
        JSONObject obj_class = new JSONObject();
        obj_class.put("ClassName","SCTStatedTANDerivation");       
        result.add(obj_class);
        
        //serialzie base
        JSONObject obj_base = new JSONObject();
        obj_base.put("BaseDerivation", base.serializeToJSON());   
        result.add(obj_base);
        
        return result;
        
    }    
}
