/*===========================================================================+
 | Sify Technologies – Employee Leave System                                 |
 | Picklist View Object Row : XxsifyLeaveTypePicklistVORowImpl               |
 +==========================================================================*/
package oracle.apps.sfc.empleavesystem.picklist.server;

import oracle.apps.fnd.framework.server.OAViewRowImpl;

public class XxsifyLeaveTypePicklistVORowImpl extends OAViewRowImpl {

    public static final int MEANING    = 0;
    public static final int LOOKUPCODE = 1;

    public String getMeaning()    { return (String) getAttributeInternal(MEANING); }
    public String getLookupCode() { return (String) getAttributeInternal(LOOKUPCODE); }
}
