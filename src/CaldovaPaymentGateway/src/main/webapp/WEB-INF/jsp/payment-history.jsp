<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<html>
<head>
    <title>Caldova Payment Gateway - Payment History</title>
</head>
<body bgcolor="#f4f4f4">
<table width="980" align="center" cellpadding="8" cellspacing="0" border="1" bgcolor="#ffffff">
    <tr bgcolor="#003366">
        <td colspan="7">
            <font color="#ffffff"><b>Caldova Payment Gateway - Payment History</b></font>
            <span style="float:right;color:#ffffff;">User: <c:out value="${username}" escapeXml="true"/></span>
        </td>
    </tr>
    <tr bgcolor="#eeeeee">
        <td colspan="7">
            <a href="/" style="font-weight:bold;">&#9664; Caldova Pharmacy Portal</a> |
            <a href="makePayment.do">Post Payment</a> |
            <a href="paymentHistory.do">Refresh History</a>
        </td>
    </tr>
    <c:if test="${not empty statusMessage}">
    <tr>
        <td colspan="7"><font color="#006600"><b><c:out value="${statusMessage}" escapeXml="true"/></b></font></td>
    </tr>
    </c:if>
    <tr bgcolor="#d7d7d7">
        <th align="left">Transaction ID</th>
        <th align="left">Patient Account</th>
        <th align="right">Amount</th>
        <th align="left">Payment Type</th>
        <th align="left">Reference #</th>
        <th align="left">Transaction Date</th>
        <th align="left">Status</th>
    </tr>
    <logic:iterate id="row" name="historyRows">
        <tr>
            <td><c:out value="${row.transactionId}" escapeXml="true"/></td>
            <td><c:out value="${row.accountNumber}" escapeXml="true"/></td>
            <td align="right">$<c:out value="${row.amount}" escapeXml="true"/></td>
            <td><c:out value="${row.paymentType}" escapeXml="true"/></td>
            <td><c:out value="${row.referenceNumber}" escapeXml="true"/></td>
            <td><c:out value="${row.transactionDate}" escapeXml="true"/></td>
            <td><c:out value="${row.status}" escapeXml="true"/></td>
        </tr>
    </logic:iterate>
    <logic:empty name="historyRows">
        <tr>
            <td colspan="7"><i>No posted payments found yet.</i></td>
        </tr>
    </logic:empty>
</table>
</body>
</html>
