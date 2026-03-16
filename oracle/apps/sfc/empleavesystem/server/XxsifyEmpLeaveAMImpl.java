/*===========================================================================+
 | Sify Technologies – Employee Leave System                                 |
 | Application Module Implementation : XxsifyEmpLeaveAMImpl                 |
 +==========================================================================*/
package oracle.apps.sfc.empleavesystem.server;

import oracle.apps.fnd.framework.server.OAApplicationModuleImpl;
import oracle.apps.fnd.framework.OAException;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;

import java.sql.Timestamp;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Application Module for the Sify Employee Leave System.
 *
 * Exposed Methods:
 *   initSearchPage(String empId)
 *   searchLeaves(String leaveType, String startDate, String endDate)
 *   clearSearch()
 *   calculateLeaveDays(String startDate, String endDate)
 *   saveLeave()
 *   initCreatePage(String empId, String leaveId)
 */
public class XxsifyEmpLeaveAMImpl extends OAApplicationModuleImpl {

    // -----------------------------------------------------------------------
    // initSearchPage – Called on Search Page load
    // Sets the employee context in the Search VO using the logged-in user.
    // -----------------------------------------------------------------------
    public void initSearchPage(String empId) {
        XxsifyEmpLeaveSearchVOImpl searchVO = getXxsifyEmpLeaveSearchVO();

        // Set employee context (mandatory filter)
        searchVO.setNamedWhereClauseParam("p_employee_id",
            (empId != null && !empId.trim().isEmpty()) ? new Number(empId) : null);

        // Initialise optional filter params to NULL so the VO has no unbound vars
        searchVO.setNamedWhereClauseParam("p_leave_type",  null);
        searchVO.setNamedWhereClauseParam("p_start_date",  null);
        searchVO.setNamedWhereClauseParam("p_end_date",    null);

        // MUST execute the VO on page load even if the result set is empty.
        // Without this the advancedTable VO has no current row, causing OAF to
        // throw "Stale Data" when any button (Create, Search, Clear) is clicked.
        searchVO.executeQuery();
    }

    // -----------------------------------------------------------------------
    // searchLeaves – Execute the Search VO with optional filter parameters
    // -----------------------------------------------------------------------
    public void searchLeaves(String leaveType, String startDate, String endDate) {
        XxsifyEmpLeaveSearchVOImpl searchVO = getXxsifyEmpLeaveSearchVO();

        searchVO.setNamedWhereClauseParam("p_leave_type",  (leaveType  != null && !leaveType.isEmpty())  ? leaveType  : null);
        searchVO.setNamedWhereClauseParam("p_start_date",  (startDate  != null && !startDate.isEmpty())  ? toOAFDate(startDate)  : null);
        searchVO.setNamedWhereClauseParam("p_end_date",    (endDate    != null && !endDate.isEmpty())    ? toOAFDate(endDate)    : null);

        searchVO.executeQuery();
    }

    // -----------------------------------------------------------------------
    // clearSearch – Reset all filters and clear results
    // -----------------------------------------------------------------------
    public void clearSearch() {
        XxsifyEmpLeaveSearchVOImpl searchVO = getXxsifyEmpLeaveSearchVO();
        searchVO.setNamedWhereClauseParam("p_leave_type", null);
        searchVO.setNamedWhereClauseParam("p_start_date", null);
        searchVO.setNamedWhereClauseParam("p_end_date",   null);
        searchVO.clearCache();
    }

    // -----------------------------------------------------------------------
    // calculateLeaveDays – Compute number of days between start and end dates
    // Sets the NoOfDays attribute on the current Create VO row.
    // -----------------------------------------------------------------------
    public void calculateLeaveDays(String startDateStr, String endDateStr) {
        if (startDateStr == null || endDateStr == null ||
            startDateStr.isEmpty() || endDateStr.isEmpty()) {
            throw new OAException("Please provide both Start Date and End Date before calculating.", OAException.ERROR);
        }

        try {
            Date startDate = toOAFDate(startDateStr);
            Date endDate   = toOAFDate(endDateStr);

            long startTime = startDate.dateValue().getTime();
            long endTime   = endDate.dateValue().getTime();

            if (endTime < startTime) {
                throw new OAException("End Date cannot be earlier than Start Date.", OAException.ERROR);
            }

            long diffMillis = endTime - startTime;
            long noOfDays   = (diffMillis / (1000 * 60 * 60 * 24)) + 1; // inclusive

            XxsifyEmpLeaveCreateVOImpl createVO = getXxsifyEmpLeaveCreateVO();
            XxsifyEmpLeaveCreateVORowImpl row =
                (XxsifyEmpLeaveCreateVORowImpl) createVO.getCurrentRow();

            if (row != null) {
                row.setNoOfDays(new Number((int) noOfDays));
            }
        } catch (Exception ex) {
            throw new OAException("Error calculating leave days: " + ex.getMessage(), OAException.ERROR);
        }
    }

    // -----------------------------------------------------------------------
    // saveLeave – Commit the leave record to xxsify_emp_leave_det_t
    // -----------------------------------------------------------------------
    public void saveLeave() {
        try {
            getDBTransaction().commit();
        } catch (Exception ex) {
            getDBTransaction().rollback();
            throw new OAException("Error saving leave record: " + ex.getMessage(), OAException.ERROR);
        }
    }

    // -----------------------------------------------------------------------
    // initCreatePage – Pre-populate employee info on Create/Update page
    // If leaveId is provided, fetch existing record (Update mode).
    // -----------------------------------------------------------------------
    public void initCreatePage(String empId, String leaveId) {
        XxsifyEmpLeaveCreateVOImpl createVO = getXxsifyEmpLeaveCreateVO();

        if (leaveId != null && !leaveId.isEmpty()) {
            // UPDATE mode – fetch existing record
            createVO.setWhereClause("LEAVE_ID = :p_leave_id");
            createVO.setNamedWhereClauseParam("p_leave_id", new Number(leaveId));
            createVO.executeQuery();
        } else {
            // CREATE mode – prepare a new row
            createVO.clearCache();
            XxsifyEmpLeaveCreateVORowImpl newRow =
                (XxsifyEmpLeaveCreateVORowImpl) createVO.createRow();

            // Auto-populate employee info from Oracle login
            if (empId != null && !empId.isEmpty()) {
                populateEmployeeInfo(newRow, empId);
            }
            createVO.insertRow(newRow);
            createVO.setCurrentRow(newRow);
        }
    }

    // -----------------------------------------------------------------------
    // Private helper – fetch employee details and set on the row
    // -----------------------------------------------------------------------
    private void populateEmployeeInfo(XxsifyEmpLeaveCreateVORowImpl row, String empId) {
        java.sql.PreparedStatement stmt = null;
        java.sql.ResultSet rs = null;
        try {
            String sql =
                "SELECT employee_number, full_name " +
                "FROM   per_all_people_f " +
                "WHERE  person_id = ? " +
                "AND    SYSDATE BETWEEN effective_start_date AND effective_end_date " +
                "AND    ROWNUM = 1";

            stmt = getOADBTransaction().getJdbcConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(empId));
            rs = stmt.executeQuery();

            if (rs.next()) {
                row.setEmployeeId(new Number(empId));
                row.setEmployeeNumber(rs.getString(1));
                row.setEmployeeName(rs.getString(2));
            }
        } catch (java.sql.SQLException ex) {
            // Non-fatal: employee info will remain blank for manual entry
        } finally {
            try { if (rs   != null) rs.close();   } catch (java.sql.SQLException e) { /* ignore */ }
            try { if (stmt != null) stmt.close(); } catch (java.sql.SQLException e) { /* ignore */ }
        }
    }

    // -----------------------------------------------------------------------
    // Private helper – convert a date String to oracle.jbo.domain.Date.
    // Accepts both "dd-MON-yyyy" (OAF default display format, e.g. 15-JAN-2025)
    // and "yyyy-MM-dd" (ISO format).  Throws a user-friendly error for anything
    // else so the end user knows exactly what format is expected.
    // -----------------------------------------------------------------------
    private Date toOAFDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        String[] formats = { "dd-MMM-yyyy", "yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy" };
        for (String fmt : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(fmt);
                sdf.setLenient(false);
                java.util.Date parsed = sdf.parse(dateStr.trim());
                return new Date(new Timestamp(parsed.getTime()));
            } catch (java.text.ParseException e) {
                // try next format
            }
        }
        throw new OAException(
            "Invalid date '" + dateStr + "'. Please enter dates as dd-MON-yyyy (e.g. 15-JAN-2025).",
            OAException.ERROR);
    }

    // -----------------------------------------------------------------------
    // Private VO accessors
    // -----------------------------------------------------------------------
    private XxsifyEmpLeaveSearchVOImpl getXxsifyEmpLeaveSearchVO() {
        return (XxsifyEmpLeaveSearchVOImpl) findViewObject("XxsifyEmpLeaveSearchVO");
    }

    private XxsifyEmpLeaveCreateVOImpl getXxsifyEmpLeaveCreateVO() {
        return (XxsifyEmpLeaveCreateVOImpl) findViewObject("XxsifyEmpLeaveCreateVO");
    }
}
