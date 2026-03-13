/*===========================================================================+
 | Sify Technologies – Employee Leave System                                 |
 | Page Controller : XxsifyEmpLeaveCreateCO                                 |
 | Page XML        : XxsifyEmpLeaveCreatePG.xml                             |
 +==========================================================================*/
package oracle.apps.sfc.empleavesystem.webui;

import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;
import oracle.apps.fnd.framework.OAApplicationModule;

/**
 * Controller for the Employee Leave Create / Update Page.
 *
 * processRequest  – Pre-populates employee info; supports Create & Update modes.
 * processFormRequest – Handles CalculatePeriod / Save / Cancel button events.
 */
public class XxsifyEmpLeaveCreateCO extends OAControllerImpl {

    private static final String SEARCH_PAGE =
        "/oracle/apps/sfc/empleavesystem/webui/XxsifyEmpLeaveSearchPG";

    // -----------------------------------------------------------------------
    // processRequest
    // -----------------------------------------------------------------------
    @Override
    public void processRequest(OAPageContext pageContext, OAWebBean webBean) {
        super.processRequest(pageContext, webBean);

        OAApplicationModule am = pageContext.getApplicationModule(webBean);

        String empId   = pageContext.getParameter("empId");
        String leaveId = pageContext.getParameter("leaveId");

        // Fallback: read employee from Oracle login if not passed as URL param
        if (empId == null || empId.trim().isEmpty()) {
            empId = pageContext.getEmployeeId();
        }

        // Initialise Create / Update page
        am.invokeMethod("initCreatePage", new Object[]{ empId, leaveId });
    }

    // -----------------------------------------------------------------------
    // processFormRequest – handle button events
    // -----------------------------------------------------------------------
    @Override
    public void processFormRequest(OAPageContext pageContext, OAWebBean webBean) {
        super.processFormRequest(pageContext, webBean);

        OAApplicationModule am = pageContext.getApplicationModule(webBean);
        String event = pageContext.getParameter(EVENT_PARAM);

        if ("CalculatePeriod".equals(event)) {
            // Read dates from form and calculate no. of days
            String startDate = pageContext.getParameter("StartDate");
            String endDate   = pageContext.getParameter("EndDate");

            am.invokeMethod("calculateLeaveDays",
                new Object[]{ startDate, endDate });

        } else if ("Save".equals(event)) {
            // Validate mandatory fields
            validateMandatoryFields(pageContext);

            // Persist via AM (EO commit)
            am.invokeMethod("saveLeave");

            // Show confirmation message and navigate back to Search Page
            pageContext.putDialogMessage(
                new OAException("Leave request saved successfully.", OAException.CONFIRMATION));

            pageContext.forwardImmediately(
                "OA.jsp?page=" + SEARCH_PAGE,
                null,
                OAWebBeanConstants.KEEP_MENU_CONTEXT,
                null,
                null,
                true,
                ADD_BREAD_CRUMB_NO);

        } else if ("Cancel".equals(event)) {
            // Navigate back without saving
            pageContext.forwardImmediately(
                "OA.jsp?page=" + SEARCH_PAGE,
                null,
                OAWebBeanConstants.KEEP_MENU_CONTEXT,
                null,
                null,
                true,
                ADD_BREAD_CRUMB_NO);
        }
    }

    // -----------------------------------------------------------------------
    // validateMandatoryFields – client-side defensive check before Save
    // -----------------------------------------------------------------------
    private void validateMandatoryFields(OAPageContext pageContext) {
        String leaveType = pageContext.getParameter("LeaveType");
        String startDate = pageContext.getParameter("StartDate");
        String endDate   = pageContext.getParameter("EndDate");
        String reason    = pageContext.getParameter("Reason");

        if (leaveType == null || leaveType.trim().isEmpty()) {
            throw new OAException("Leave Type is required.", OAException.ERROR);
        }
        if (startDate == null || startDate.trim().isEmpty()) {
            throw new OAException("Start Date is required.", OAException.ERROR);
        }
        if (endDate == null || endDate.trim().isEmpty()) {
            throw new OAException("End Date is required.", OAException.ERROR);
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new OAException("Reason is required.", OAException.ERROR);
        }
    }
}
