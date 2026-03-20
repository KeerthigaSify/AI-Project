/*===========================================================================+
 | Sify Technologies – Employee Leave System                                 |
 | View Object Implementation : XxsifyEmpLeaveSearchVOImpl                  |
 +==========================================================================*/
package oracle.apps.sfc.empleavesystem.server;

import oracle.apps.fnd.framework.server.OAViewObjectImpl;

public class XxsifyEmpLeaveSearchVOImpl extends OAViewObjectImpl {

    /** Default constructor (do not remove) */
    public XxsifyEmpLeaveSearchVOImpl() {
        super();
    }

    /** Returns the current row cast to the correct row type */
    public XxsifyEmpLeaveSearchVORowImpl first() {
        return (XxsifyEmpLeaveSearchVORowImpl) super.first();
    }

    public XxsifyEmpLeaveSearchVORowImpl next() {
        return (XxsifyEmpLeaveSearchVORowImpl) super.next();
    }

    public XxsifyEmpLeaveSearchVORowImpl getCurrentRow() {
        return (XxsifyEmpLeaveSearchVORowImpl) super.getCurrentRow();
    }
}
