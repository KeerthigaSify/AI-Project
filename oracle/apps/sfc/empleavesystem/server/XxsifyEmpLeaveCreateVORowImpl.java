/*===========================================================================+
 | Sify Technologies – Employee Leave System                                 |
 | View Object Row Implementation : XxsifyEmpLeaveCreateVORowImpl           |
 +==========================================================================*/
package oracle.apps.sfc.empleavesystem.server;

import oracle.apps.fnd.framework.server.OAViewRowImpl;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;

/**
 * Row implementation for XxsifyEmpLeaveCreateVO.
 * Provides typed getters and setters for all editable form fields.
 */
public class XxsifyEmpLeaveCreateVORowImpl extends OAViewRowImpl {

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

    public Number getLeaveId()           { return (Number) getAttributeInternal(LEAVEID); }
    public void   setLeaveId(Number v)   { setAttributeInternal(LEAVEID, v); }

    public Number getEmployeeId()          { return (Number) getAttributeInternal(EMPLOYEEID); }
    public void   setEmployeeId(Number v)  { setAttributeInternal(EMPLOYEEID, v); }

    public String getEmployeeNumber()          { return (String) getAttributeInternal(EMPLOYEENUMBER); }
    public void   setEmployeeNumber(String v)  { setAttributeInternal(EMPLOYEENUMBER, v); }

    public String getEmployeeName()          { return (String) getAttributeInternal(EMPLOYEENAME); }
    public void   setEmployeeName(String v)  { setAttributeInternal(EMPLOYEENAME, v); }

    public String getLeaveType()         { return (String) getAttributeInternal(LEAVETYPE); }
    public void   setLeaveType(String v) { setAttributeInternal(LEAVETYPE, v); }

    public Date   getStartDate()         { return (Date) getAttributeInternal(STARTDATE); }
    public void   setStartDate(Date v)   { setAttributeInternal(STARTDATE, v); }

    public Date   getEndDate()           { return (Date) getAttributeInternal(ENDDATE); }
    public void   setEndDate(Date v)     { setAttributeInternal(ENDDATE, v); }

    public Number getNoOfDays()          { return (Number) getAttributeInternal(NOOFDAYS); }
    public void   setNoOfDays(Number v)  { setAttributeInternal(NOOFDAYS, v); }

    public String getReason()            { return (String) getAttributeInternal(REASON); }
    public void   setReason(String v)    { setAttributeInternal(REASON, v); }

    public String getStatus()            { return (String) getAttributeInternal(STATUS); }
    public void   setStatus(String v)    { setAttributeInternal(STATUS, v); }
}
