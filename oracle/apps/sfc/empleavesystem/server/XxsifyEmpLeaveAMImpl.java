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
        if (empId != null && !empId.trim().equals("")) {
            searchVO.setBindVariableValue("p_employee_id", new Number(empId));
        }
        searchVO.setWhereClause(null);
        searchVO.setWhereClauseParams(null);
        // Do NOT execute query on load — wait for user to press Search
    }

    // -----------------------------------------------------------------------
    // searchLeaves – Execute the Search VO with optional filter parameters
    // -----------------------------------------------------------------------
    public void searchLeaves(String leaveType, String startDate, String endDate) {
        XxsifyEmpLeaveSearchVOImpl searchVO = getXxsifyEmpLeaveSearchVO();

        searchVO.setBindVariableValue("p_leave_type",  (leaveType  != null && !leaveType.isEmpty())  ? leaveType  : null);
        searchVO.setBindVariableValue("p_start_date",  (startDate  != null && !startDate.isEmpty())  ? Date.valueOf(startDate)  : null);
        searchVO.setBindVariableValue("p_end_date",    (endDate    != null && !endDate.isEmpty())    ? Date.valueOf(endDate)    : null);

        searchVO.executeQuery();
    }

    // -----------------------------------------------------------------------
    // clearSearch – Reset all filters and clear results
    // -----------------------------------------------------------------------
    public void clearSearch() {
        XxsifyEmpLeaveSearchVOImpl searchVO = getXxsifyEmpLeaveSearchVO();
        searchVO.setBindVariableValue("p_leave_type", null);
        searchVO.setBindVariableValue("p_start_date", null);
        searchVO.setBindVariableValue("p_end_date",   null);
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
            Date startDate = Date.valueOf(startDateStr);
            Date endDate   = Date.valueOf(endDateStr);

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
            createVO.setBindVariableValue("p_leave_id", new Number(leaveId));
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
        try {
            String sql =
                "SELECT employee_number, full_name " +
                "FROM   per_all_people_f " +
                "WHERE  person_id = :1 " +
                "AND    SYSDATE BETWEEN effective_start_date AND effective_end_date " +
                "AND    ROWNUM = 1";

            Object[] params = { new Number(empId) };
            Object[] result = (Object[]) getDBTransaction()
                .executeSelectOneValue(sql, params, null);

            if (result != null) {
                row.setEmployeeId(new Number(empId));
                row.setEmployeeNumber((String) result[0]);
                row.setEmployeeName((String) result[1]);
            }
        } catch (Exception ex) {
            // Non-fatal: employee info will remain blank for manual entry
        }
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
