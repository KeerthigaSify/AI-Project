/*===========================================================================+
 | Sify Technologies – Employee Leave System                                 |
 | Entity Object Implementation : XxsifyEmpLeaveEOImpl                      |
 | Base Table : xxsify_emp_leave_det_t                                       |
 +==========================================================================*/
package oracle.apps.sfc.empleavesystem.schema;

import oracle.apps.fnd.framework.server.OAEntityImpl;
import oracle.apps.fnd.framework.OAException;
import oracle.jbo.AttributeList;
import oracle.jbo.Key;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityDefImpl;
import oracle.jbo.server.TransactionEvent;

/**
 * Entity Object for xxsify_emp_leave_det_t.
 * Handles WHO columns auto-population and PK generation via sequence.
 */
public class XxsifyEmpLeaveEOImpl extends OAEntityImpl {

    // -----------------------------------------------------------------------
    // Attribute indices – must match the order in XxsifyEmpLeaveEO.xml
    // -----------------------------------------------------------------------
    public static final int LEAVEID            = 0;
    public static final int EMPLOYEEID         = 1;
    public static final int EMPLOYEENUMBER     = 2;
    public static final int EMPLOYEENAME       = 3;
    public static final int LEAVETYPE          = 4;
    public static final int STARTDATE          = 5;
    public static final int ENDDATE            = 6;
    public static final int NOOFDAYS           = 7;
    public static final int REASON             = 8;
    public static final int STATUS             = 9;
    public static final int CREATEDBY          = 10;
    public static final int CREATIONDATE       = 11;
    public static final int LASTUPDATEDBY      = 12;
    public static final int LASTUPDATEDATE     = 13;
    public static final int LASTUPDATELOGIN    = 14;

    // Synonym name accessible from APPS user (see 02_sequence_synonym.sql).
    // The sequence lives in the XXSIFY schema; APPS user accesses it via
    // the synonym "xxsify_emp_leave_s" – NOT the raw sequence name.
    private static final String SEQUENCE_NAME = "xxsify_emp_leave_s";

    /** Default constructor (do not remove) */
    public XxsifyEmpLeaveEOImpl() {
        super();
    }

    // -----------------------------------------------------------------------
    // create() – Initialise default values & WHO columns on INSERT
    // -----------------------------------------------------------------------
    public void create(AttributeList attributeList) {
        super.create(attributeList);

        // Generate PK from sequence using OAF sequence API
        Number leaveId = getOADBTransaction().getSequenceValue(SEQUENCE_NAME);
        setLeaveId(leaveId);

        // Populate WHO columns
        populateWhoColumnsForInsert();

        // Default status
        setStatus("PENDING");
    }

    // -----------------------------------------------------------------------
    // prepareForDML() – Refresh WHO columns on UPDATE
    // -----------------------------------------------------------------------
    protected void prepareForDML(int operation, TransactionEvent e) {
        if (operation == DML_UPDATE) {
            populateWhoColumnsForUpdate();
        }
        super.prepareForDML(operation, e);
    }

    // -----------------------------------------------------------------------
    // Validate end_date > start_date
    // -----------------------------------------------------------------------
    protected void validateEntity() {
        super.validateEntity();

        Date startDate = getStartDate();
        Date endDate   = getEndDate();

        if (startDate != null && endDate != null) {
            if (endDate.compareTo(startDate) < 0) {
                throw new OAException(
                    "End Date cannot be earlier than Start Date.",
                    OAException.ERROR);
            }
        }
    }

    // -----------------------------------------------------------------------
    // getAttrInvokeAccessor: generated method. Do not modify.
    // -----------------------------------------------------------------------
    protected Object getAttrInvokeAccessor(int index,
                                           AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case LEAVEID:         return getLeaveId();
        case EMPLOYEEID:      return getEmployeeId();
        case EMPLOYEENUMBER:  return getEmployeeNumber();
        case EMPLOYEENAME:    return getEmployeeName();
        case LEAVETYPE:       return getLeaveType();
        case STARTDATE:       return getStartDate();
        case ENDDATE:         return getEndDate();
        case NOOFDAYS:        return getNoOfDays();
        case REASON:          return getReason();
        case STATUS:          return getStatus();
        case CREATEDBY:       return getCreatedBy();
        case CREATIONDATE:    return getCreationDate();
        case LASTUPDATEDBY:   return getLastUpdatedBy();
        case LASTUPDATEDATE:  return getLastUpdateDate();
        case LASTUPDATELOGIN: return getLastUpdateLogin();
        default:              return super.getAttrInvokeAccessor(index, attrDef);
        }
    }

    // -----------------------------------------------------------------------
    // setAttrInvokeAccessor: generated method. Do not modify.
    // -----------------------------------------------------------------------
    protected void setAttrInvokeAccessor(int index, Object value,
                                         AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case LEAVEID:         setLeaveId((Number) value);        return;
        case EMPLOYEEID:      setEmployeeId((Number) value);     return;
        case EMPLOYEENUMBER:  setEmployeeNumber((String) value); return;
        case EMPLOYEENAME:    setEmployeeName((String) value);   return;
        case LEAVETYPE:       setLeaveType((String) value);      return;
        case STARTDATE:       setStartDate((Date) value);        return;
        case ENDDATE:         setEndDate((Date) value);          return;
        case NOOFDAYS:        setNoOfDays((Number) value);       return;
        case REASON:          setReason((String) value);         return;
        case STATUS:          setStatus((String) value);         return;
        case CREATEDBY:       setCreatedBy((Number) value);      return;
        case CREATIONDATE:    setCreationDate((Date) value);     return;
        case LASTUPDATEDBY:   setLastUpdatedBy((Number) value);  return;
        case LASTUPDATEDATE:  setLastUpdateDate((Date) value);   return;
        case LASTUPDATELOGIN: setLastUpdateLogin((Number) value);return;
        default:              super.setAttrInvokeAccessor(index, value, attrDef); return;
        }
    }

    // -----------------------------------------------------------------------
    // WHO column helpers
    // -----------------------------------------------------------------------
    private void populateWhoColumnsForInsert() {
        Number userId   = new Number(getOADBTransaction().getUserId());
        Date   sysdate  = new Date(new java.sql.Timestamp(System.currentTimeMillis()));

        setCreatedBy(userId);
        setCreationDate(sysdate);
        setLastUpdatedBy(userId);
        setLastUpdateDate(sysdate);
        setLastUpdateLogin(new Number(getOADBTransaction().getSessionId()));
    }

    private void populateWhoColumnsForUpdate() {
        Number userId  = new Number(getOADBTransaction().getUserId());
        Date   sysdate = new Date(new java.sql.Timestamp(System.currentTimeMillis()));

        setLastUpdatedBy(userId);
        setLastUpdateDate(sysdate);
        setLastUpdateLogin(new Number(getOADBTransaction().getSessionId()));
    }

    // -----------------------------------------------------------------------
    // Accessor methods
    // -----------------------------------------------------------------------
    public Number getLeaveId()          { return (Number)  getAttributeInternal(LEAVEID); }
    public void   setLeaveId(Number v)  { setAttributeInternal(LEAVEID, v); }

    public Number getEmployeeId()           { return (Number)  getAttributeInternal(EMPLOYEEID); }
    public void   setEmployeeId(Number v)   { setAttributeInternal(EMPLOYEEID, v); }

    public String getEmployeeNumber()           { return (String) getAttributeInternal(EMPLOYEENUMBER); }
    public void   setEmployeeNumber(String v)   { setAttributeInternal(EMPLOYEENUMBER, v); }

    public String getEmployeeName()           { return (String) getAttributeInternal(EMPLOYEENAME); }
    public void   setEmployeeName(String v)   { setAttributeInternal(EMPLOYEENAME, v); }

    public String getLeaveType()          { return (String) getAttributeInternal(LEAVETYPE); }
    public void   setLeaveType(String v)  { setAttributeInternal(LEAVETYPE, v); }

    public Date   getStartDate()          { return (Date)   getAttributeInternal(STARTDATE); }
    public void   setStartDate(Date v)    { setAttributeInternal(STARTDATE, v); }

    public Date   getEndDate()            { return (Date)   getAttributeInternal(ENDDATE); }
    public void   setEndDate(Date v)      { setAttributeInternal(ENDDATE, v); }

    public Number getNoOfDays()           { return (Number) getAttributeInternal(NOOFDAYS); }
    public void   setNoOfDays(Number v)   { setAttributeInternal(NOOFDAYS, v); }

    public String getReason()         { return (String) getAttributeInternal(REASON); }
    public void   setReason(String v) { setAttributeInternal(REASON, v); }

    public String getStatus()         { return (String) getAttributeInternal(STATUS); }
    public void   setStatus(String v) { setAttributeInternal(STATUS, v); }

    public Number getCreatedBy()          { return (Number) getAttributeInternal(CREATEDBY); }
    public void   setCreatedBy(Number v)  { setAttributeInternal(CREATEDBY, v); }

    public Date   getCreationDate()         { return (Date) getAttributeInternal(CREATIONDATE); }
    public void   setCreationDate(Date v)   { setAttributeInternal(CREATIONDATE, v); }

    public Number getLastUpdatedBy()          { return (Number) getAttributeInternal(LASTUPDATEDBY); }
    public void   setLastUpdatedBy(Number v)  { setAttributeInternal(LASTUPDATEDBY, v); }

    public Date   getLastUpdateDate()         { return (Date) getAttributeInternal(LASTUPDATEDATE); }
    public void   setLastUpdateDate(Date v)   { setAttributeInternal(LASTUPDATEDATE, v); }

    public Number getLastUpdateLogin()          { return (Number) getAttributeInternal(LASTUPDATELOGIN); }
    public void   setLastUpdateLogin(Number v)  { setAttributeInternal(LASTUPDATELOGIN, v); }
}
