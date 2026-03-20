/*===========================================================================+
 | Sify Technologies – Employee Leave System                                 |
 | View Object Implementation : XxsifyEmpLeaveCreateVOImpl                  |
 +==========================================================================*/
package oracle.apps.sfc.empleavesystem.server;

import oracle.apps.fnd.framework.server.OAViewObjectImpl;

public class XxsifyEmpLeaveCreateVOImpl extends OAViewObjectImpl {

    /** Default constructor (do not remove) */
    public XxsifyEmpLeaveCreateVOImpl() {
        super();
    }

    /** Returns the current row cast to the correct row type */
    public XxsifyEmpLeaveCreateVORowImpl createRow() {
        return (XxsifyEmpLeaveCreateVORowImpl) super.createRow();
    }

    public XxsifyEmpLeaveCreateVORowImpl first() {
        return (XxsifyEmpLeaveCreateVORowImpl) super.first();
    }

    public XxsifyEmpLeaveCreateVORowImpl next() {
        return (XxsifyEmpLeaveCreateVORowImpl) super.next();
    }

    public XxsifyEmpLeaveCreateVORowImpl getCurrentRow() {
        return (XxsifyEmpLeaveCreateVORowImpl) super.getCurrentRow();
    }
}
