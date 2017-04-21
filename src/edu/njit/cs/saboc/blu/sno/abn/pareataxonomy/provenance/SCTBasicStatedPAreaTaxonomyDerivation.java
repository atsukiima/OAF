package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.provenance;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.provenance.SimplePAreaTaxonomyDerivation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Identifies the arguments needed to create a stated partial-area taxonomy from
 * a hierarchy of concepts
 *
 * @author Chris O
 */
public class SCTBasicStatedPAreaTaxonomyDerivation extends SimplePAreaTaxonomyDerivation {

    private final SimplePAreaTaxonomyDerivation base;

    public SCTBasicStatedPAreaTaxonomyDerivation(
            SimplePAreaTaxonomyDerivation baseDerivation) {

        super(baseDerivation);

        this.base = baseDerivation;
    }

    @Override
    public String getDescription() {
        return "Derived SCT Stated PArea Taxonomy";
    }

    @Override
    public JSONArray serializeToJSON() {
        JSONArray result = new JSONArray();

        //serialize class
        JSONObject obj_class = new JSONObject();
        obj_class.put("ClassName", "SCTBasicStatedPAreaTaxonomyDerivation");
        result.add(obj_class);

        //serialzie base
        JSONObject obj_base = new JSONObject();
        obj_base.put("BaseDerivation", base.serializeToJSON());
        result.add(obj_base);

        return result;

    }
}
