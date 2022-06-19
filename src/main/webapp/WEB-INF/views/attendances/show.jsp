<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>

<c:set var="actAtt" value="${ForwardConst.ACT_ATT.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdt" value="${ForwardConst.CMD_EDIT.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <h2>勤怠 詳細ページ</h2>

        <table>
            <tbody>
                <tr>
                    <th>氏名</th>
                    <td><c:out value="${attendance.employee.name}" /></td>
                </tr>
                <tr>
                    <th>日付</th>
                    <fmt:parseDate value="${attendance.attendanceDate}" pattern="yyyy-MM-dd" var="attendanceDay" type="date" />
                    <td><fmt:formatDate value='${attendanceDay}' pattern='yyyy-MM-dd' /></td>
                </tr>
                <tr>
                    <th>出勤時間</th>
                    <fmt:parseDate value="${attendance.startedAt}" pattern="yyyy-MM-dd'T'HH:mm" var="startedAt" type="time" />
                    <td><fmt:formatDate value="${startedAt}" pattern="HH:mm:ss" /></td>
                </tr>
                <tr>
                    <th>退勤時間</th>
                    <fmt:parseDate value="${attendance.leavedAt}" pattern="yyyy-MM-dd'T'HH:mm" var="leavedAt" type="time" />
                    <td><fmt:formatDate value="${leavedAt}" pattern="HH:mm:ss" /></td>
                </tr>
            </tbody>
        </table>

        <c:if test="${sessionScope.login_employee.id == attendance.employee.id}">
            <p>
                <a href="<c:url value='?action=${actAtt}&command=${commEdt}&id=${attendance.id}' />">この勤怠を編集する</a>
            </p>
        </c:if>

        <p>
            <a href="<c:url value='?action=${actAtt}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>