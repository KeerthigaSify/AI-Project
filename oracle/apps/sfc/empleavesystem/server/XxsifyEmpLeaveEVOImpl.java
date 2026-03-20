/*===========================================================================+
 | Sify Technologies – Employee Leave System                                 |
 | Entity View Object Implementation : XxsifyEmpLeaveEVOImpl                |
 +==========================================================================*/
package oracle.apps.sfc.empleavesystem.server;

import oracle.apps.fnd.framework.server.OAViewObjectImpl;

public class XxsifyEmpLeaveEVOImpl extends OAViewObjectImpl {

    /** Default constructor (do not remove) */
    public XxsifyEmpLeaveEVOImpl() {
        super();
    }

    /** Returns the current row cast to the correct row type */
    public XxsifyEmpLeaveEVORowImpl first() {
        return (XxsifyEmpLeaveEVORowImpl) super.first();
    }

    public XxsifyEmpLeaveEVORowImpl next() {
        return (XxsifyEmpLeaveEVORowImpl) super.next();
    }

    public XxsifyEmpLeaveEVORowImpl getCurrentRow() {
        return (XxsifyEmpLeaveEVORowImpl) super.getCurrentRow();
    }
}
