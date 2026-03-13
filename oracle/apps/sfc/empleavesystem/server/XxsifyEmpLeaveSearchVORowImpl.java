/*===========================================================================+
 | Sify Technologies – Employee Leave System                                 |
 | View Object Row Implementation : XxsifyEmpLeaveSearchVORowImpl           |
 +==========================================================================*/
package oracle.apps.sfc.empleavesystem.server;

import oracle.apps.fnd.framework.server.OAViewRowImpl;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;

/**
 * Row implementation for XxsifyEmpLeaveSearchVO.
 */
public class XxsifyEmpLeaveSearchVORowImpl extends OAViewRowImpl {

    // Attribute indices
    public static final int LEAVEID        = 0;
    public static final int EMPLOYEEID     = 1;
    public static final int EMPLOYEENUMBER = 2;
    public static final int EMPLOYEENAME   = 3;
    public static final int LEAVETYPE      = 4;
    public static final int STARTDATE      = 5;
    public static final int ENDDATE        = 6;
    public static final int NOOFDAYS       = 7;
    public static final int REASON         = 8;
    public static final int STATUS         = 9;

    public Number getLeaveId()      { return (Number) getAttributeInternal(LEAVEID); }
    public Number getEmployeeId()   { return (Number) getAttributeInternal(EMPLOYEEID); }
    public String getEmployeeNumber() { return (String) getAttributeInternal(EMPLOYEENUMBER); }
    public String getEmployeeName()   { return (String) getAttributeInternal(EMPLOYEENAME); }
    public String getLeaveType()    { return (String) getAttributeInternal(LEAVETYPE); }
    public Date   getStartDate()    { return (Date)   getAttributeInternal(STARTDATE); }
    public Date   getEndDate()      { return (Date)   getAttributeInternal(ENDDATE); }
    public Number getNoOfDays()     { return (Number) getAttributeInternal(NOOFDAYS); }
    public String getReason()       { return (String) getAttributeInternal(REASON); }
    public String getStatus()       { return (String) getAttributeInternal(STATUS); }
}
