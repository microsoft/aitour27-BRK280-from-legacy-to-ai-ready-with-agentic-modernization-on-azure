<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html>
<head>
    <title>Caldova Payment Gateway - Post Payment</title>
</head>
<body bgcolor="#f4f4f4">
<table width="960" align="center" cellpadding="8" cellspacing="0" border="1" bgcolor="#ffffff">
    <tr bgcolor="#003366">
        <td colspan="2">
            <font color="#ffffff"><b>Caldova Payment Gateway - Post a Pharmacy Payment</b></font>
            <span style="float:right;color:#ffffff;">User: <%= request.getAttribute("username") == null ? "Unknown" : request.getAttribute("username") %></span>
        </td>
    </tr>
    <tr bgcolor="#eeeeee">
        <td colspan="2">
            <a href="/" style="font-weight:bold;">&#9664; Caldova Pharmacy Portal</a> |
            <a href="<%= request.getContextPath() %>/makePayment.do">Post Payment</a> |
            <a href="<%= request.getContextPath() %>/paymentHistory.do">Payment History</a>
        </td>
    </tr>
    <tr>
        <td width="70%" valign="top">
            <%
                Object statusError = request.getAttribute("statusError");
                if (statusError != null) {
            %>
            <p><font color="#cc0000"><b><%= statusError %></b></font></p>
            <%
                }
                Object statusMessage = request.getAttribute("statusMessage");
                if (statusMessage != null) {
            %>
            <p><font color="#006600"><b><%= statusMessage %></b></font></p>
            <%
                }
            %>
            <html:form action="/makePayment.do" method="post">
                <table border="0" cellpadding="6" cellspacing="0">
                    <tr>
                        <td><b>Patient Account</b></td>
                        <td>
                            <select name="accountId">
                                <%
                                    java.util.List accountOptions = (java.util.List) request.getAttribute("accountOptions");
                                    if (accountOptions != null) {
                                        for (Object row : accountOptions) {
                                            com.caldova.paymentgateway.AccountOption option = (com.caldova.paymentgateway.AccountOption) row;
                                %>
                                <option value="<%= option.getAccountId() %>"><%= option.getAccountNumber() %> - <%= option.getAccountType() %> ($<%= option.getBalance() %>)</option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><b>Amount</b></td>
                        <td><html:text property="amount" size="18" maxlength="20"/></td>
                    </tr>
                    <tr>
                        <td><b>Payment Type</b></td>
                        <td>
                            <html:select property="paymentType">
                                <html:option value="">-- Select --</html:option>
                                <html:option value="ACH">Prescription payment</html:option>
                                <html:option value="BillPay">Copay payment</html:option>
                                <html:option value="Wire">Patient-account payment</html:option>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td><b>Payment Note</b></td>
                        <td><html:text property="memo" size="48" maxlength="120"/></td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td><html:submit value="Post Payment"/></td>
                    </tr>
                </table>
            </html:form>
        </td>
        <td width="30%" valign="top" bgcolor="#f8f8f8">
            <b>Legacy Notes</b>
            <ul>
                <li>Payments are posted to the Caldova Claims Ledger via XML over HTTP.</li>
                <li>Payment choices cover prescription, copay, and patient-account payments.</li>
                <li>History is rendered in GridView-style table format.</li>
                <li>SSO token validation uses <code>SessionTokens</code> table.</li>
            </ul>
        </td>
    </tr>
</table>
</body>
</html>
