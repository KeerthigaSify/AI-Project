/*===========================================================================+
 | Sify Technologies – Employee Leave System                                 |
 | LOV View Object Row : XxsifyLeaveTypeLovVORowImpl                        |
 +==========================================================================*/
package oracle.apps.sfc.empleavesystem.lov.server;

import oracle.apps.fnd.framework.server.OAViewRowImpl;

public class XxsifyLeaveTypeLovVORowImpl extends OAViewRowImpl {

    public static final int MEANING    = 0;
    public static final int LOOKUPCODE = 1;

    public String getMeaning()    { return (String) getAttributeInternal(MEANING); }
    public String getLookupCode() { return (String) getAttributeInternal(LOOKUPCODE); }
}
