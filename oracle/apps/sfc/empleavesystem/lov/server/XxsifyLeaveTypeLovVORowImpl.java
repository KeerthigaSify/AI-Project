/*===========================================================================+
 | Sify Technologies – Employee Leave System                                 |
 | LOV View Object Row : XxsifyLeaveTypeLovVORowImpl                        |
 +==========================================================================*/
package oracle.apps.sfc.empleavesystem.lov.server;

import oracle.apps.fnd.framework.server.OAViewRowImpl;
import oracle.jbo.server.AttributeDefImpl;

public class XxsifyLeaveTypeLovVORowImpl extends OAViewRowImpl {

    public static final int MEANING    = 0;
    public static final int LOOKUPCODE = 1;

    /** Default constructor (do not remove) */
    public XxsifyLeaveTypeLovVORowImpl() {
    }

    public String getMeaning()    { return (String) getAttributeInternal(MEANING); }
    public String getLookupCode() { return (String) getAttributeInternal(LOOKUPCODE); }

    public void setMeaning(String value) {
        setAttributeInternal(MEANING, value);
    }

    public void setLookupCode(String value) {
        setAttributeInternal(LOOKUPCODE, value);
    }

    /** getAttrInvokeAccessor: generated method. Do not modify. */
    protected Object getAttrInvokeAccessor(int index,
                                           AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case MEANING:
            return getMeaning();
        case LOOKUPCODE:
            return getLookupCode();
        default:
            return super.getAttrInvokeAccessor(index, attrDef);
        }
    }

    /** setAttrInvokeAccessor: generated method. Do not modify. */
    protected void setAttrInvokeAccessor(int index, Object value,
                                         AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case MEANING:
            setMeaning((String) value);
            return;
        case LOOKUPCODE:
            setLookupCode((String) value);
            return;
        default:
            super.setAttrInvokeAccessor(index, value, attrDef);
            return;
        }
    }
}
