
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport;
import javax.swing.ImageIcon;

/**
 *
 * @author Chris O
 */
public class DescriptiveDeltaGUIUtils {
    public static final ImageIcon getIconForEditingOperation(EditingOperationReport.EditingOperationType type) {
        
        String iconName;
        
        switch (type) {
        
            case AddedParent:
                iconName = "dd_isa_added.png";
                break;
                
            case RemovedParent:
                iconName = "dd_isa_removed.png";
                break;
                
            case ChangedParent:
                iconName = "dd_isa_changed.png";
                break;
                
            case ParentLessRefined:
                iconName = "dd_isa_lessrefined.png";
                break;
                
            case ParentMoreRefined:
                iconName = "dd_isa_morerefined.png";
                break;
                
            case AddedAttributeRelationship:
                iconName = "dd_rel_added.png";
                break;
                
            case RemovedAttributeRelationship:
                iconName = "dd_rel_removed.png";
                break;
                
            case ChangedAttributeRelationship:
                iconName = "dd_rel_changed.png";
                break;
                
            case AttributeRelationshipMoreRefined:
                iconName = "dd_rel_morerefined.png";
                break;
                
            case AttributeRelationshipLessRefined:
                iconName = "dd_rel_lessRefined.png";
                break;
                
            case RelationshipGroupChanged:
                iconName = "dd_group_change.png";
                break;
                
            default:
                return null;
        }
        
        return IconManager.getIconManager().getIcon(iconName);
    }
}
