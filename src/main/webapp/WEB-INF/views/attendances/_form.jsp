<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<fmt:parseDate value="${attendance.attendanceDate}" pattern="yyyy-MM-dd" var="attendanceDay" type="date" />
<label for="${AttributeConst.ATT_DATE.getValue()}">日付</label><br />
<input type="date" name="${AttributeConst.ATT_DATE.getValue()}" value="<fmt:formatDate value='${attendanceDay}' pattern='yyyy-MM-dd' />" />
<br /><br />

<label for="name">氏名</label><br />
<c:out value="${sessionScope.login_employee.name}" />
<br /><br />

<fmt:parseDate value="${attendance.startedAt}" pattern="yyyy-MM-dd'T'HH:mm" var="startedAt" type="time" />
<label for="${AttributeConst.ATT_STARTED_AT.getValue()}">出社時間</label><br />
<input type="time" name="${AttributeConst.ATT_STARTED_AT.getValue()}" value="<fmt:formatDate value='${startedAt}' pattern='HH:mm:ss' />" />
<br /><br />

<fmt:parseDate value="${attendance.leavedAt}" pattern="yyyy-MM-dd'T'HH:mm" var="leavedAt" type="time" />
<label for="${AttributeConst.ATT_LEAVED_AT.getValue()}">退社時間</label><br />
<input type="time" name="${AttributeConst.ATT_LEAVED_AT.getValue()}" value="<fmt:formatDate value='${leavedAt}' pattern='HH:mm:ss' />" />
<br /><br />
<input type="hidden" name="${AttributeConst.ATT_ID.getValue()}" value="${sttendance.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>