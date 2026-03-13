/*===========================================================================+
 | Sify Technologies – Employee Leave System                                 |
 | Page Controller : XxsifyEmpLeaveSearchCO                                 |
 | Page XML        : XxsifyEmpLeaveSearchPG.xml                             |
 +==========================================================================*/
package oracle.apps.sfc.empleavesystem.webui;

import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;
import oracle.apps.fnd.framework.OAApplicationModule;
import java.io.Serializable;

/**
 * Controller for the Employee Leave Search Page.
 *
 * processRequest  – Identifies logged-in employee, initialises the Search VO.
 * processFormRequest – Handles Search / Clear / Navigate to Create buttons.
 */
public class XxsifyEmpLeaveSearchCO extends OAControllerImpl {

    private static final String AM_DEF        = "oracle.apps.sfc.empleavesystem.server.XxsifyEmpLeaveAM";
    private static final String CONFIG        = "XxsifyEmpLeaveAMLocal";
    private static final String CREATE_PAGE   =
        "/oracle/apps/sfc/empleavesystem/webui/XxsifyEmpLeaveCreatePG";

    // -----------------------------------------------------------------------
    // processRequest – called on every page load / partial-page request
    // -----------------------------------------------------------------------
    @Override
    public void processRequest(OAPageContext pageContext, OAWebBean webBean) {
        super.processRequest(pageContext, webBean);

        OAApplicationModule am = pageContext.getApplicationModule(webBean);

        // Identify the logged-in employee
        String empId = String.valueOf(pageContext.getEmployeeId());   // FND_GLOBAL.EMPLOYEE_ID

        if (empId == null || empId.trim().isEmpty()) {
            throw new OAException(
                "Unable to identify the logged-in employee. Please log in through Oracle EBS.",
                OAException.ERROR);
        }

        // Initialise the search page – sets employee bind variable in the VO
        am.invokeMethod("initSearchPage", new Serializable[]{ empId });
    }

    // -----------------------------------------------------------------------
    // processFormRequest – handles button clicks
    // -----------------------------------------------------------------------
    @Override
    public void processFormRequest(OAPageContext pageContext, OAWebBean webBean) {
        super.processFormRequest(pageContext, webBean);

        OAApplicationModule am = pageContext.getApplicationModule(webBean);
        String event = pageContext.getParameter(EVENT_PARAM);

        if ("Search".equals(event)) {
            // Read filter values from the page
            String leaveType  = pageContext.getParameter("LeaveType");
            String startDate  = pageContext.getParameter("StartDate");
            String endDate    = pageContext.getParameter("EndDate");

            am.invokeMethod("searchLeaves",
                new Serializable[]{ leaveType, startDate, endDate });

        } else if ("Clear".equals(event)) {
            am.invokeMethod("clearSearch");

        } else if ("Create".equals(event)) {
            // Navigate to Create Page (new record mode)
            String empId = String.valueOf(pageContext.getEmployeeId());
            pageContext.forwardImmediately(
                "OA.jsp?page=" + CREATE_PAGE + "&empId=" + empId,
                null,
                OAWebBeanConstants.KEEP_MENU_CONTEXT,
                null,
                null,
                true,
                ADD_BREAD_CRUMB_YES);

        } else if ("Update".equals(event)) {
            // Navigate to Create Page (update mode) – leaveId passed as URL param
            String leaveId = pageContext.getParameter("leaveId");
            String empId   = String.valueOf(pageContext.getEmployeeId());

            pageContext.forwardImmediately(
                "OA.jsp?page=" + CREATE_PAGE
                    + "&leaveId=" + leaveId
                    + "&empId="   + empId,
                null,
                OAWebBeanConstants.KEEP_MENU_CONTEXT,
                null,
                null,
                true,
                ADD_BREAD_CRUMB_YES);
        }
    }
}
