<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ROOKIE')">
    <display:table name="applications" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

        <spring:message code="position.ticker" var="tickerHeader"/>
        <display:column title="${tickerHeader}">
                <jstl:out value="${appPosition[row.id].ticker}"></jstl:out>
        </display:column>

        <spring:message code="application.submitMoment" var="submitMoment"/>
        <display:column title="${submitMoment}">
            <jstl:if test="${row.status != 'PENDING' }">
                <jstl:out value="${row.submitMoment}"></jstl:out>
            </jstl:if>
        </display:column>

        <spring:message code="application.explanation" var="explanation"/>
        <display:column title="${explanation}">
            <jstl:if test="${row.status != 'PENDING' }">
                <jstl:out value="${row.explanation}"/>
            </jstl:if>
        </display:column>

        <spring:message code="application.link" var="link"/>
        <display:column title="${link}">
            <jstl:if test="${row.status != 'PENDING' }">
                <a href="${row.link}">${row.link}</a>
            </jstl:if>
        </display:column>

        <spring:message code="application.status" var="status"/>
        <display:column title="${status}" sortable="true">
            <jstl:if test="${row.status == 'ACCEPTED' }">
                <spring:message code="application.accepted"/>
            </jstl:if>

            <jstl:if test="${row.status == 'SUBMITTED' }">
                <spring:message code="application.submitted"/>
            </jstl:if>

            <jstl:if test="${row.status == 'PENDING' }">
                <spring:message code="application.pending"/>
            </jstl:if>

            <jstl:if test="${row.status == 'REJECTED' }">
                <spring:message code="application.rejected"/>
            </jstl:if>

            <jstl:if test="${row.status == 'CANCELLED' }">
                <spring:message code="application.cancelled"/>
            </jstl:if>
        </display:column>

        <spring:message code="application.rejectComment" var="rejectComment"/>
        <display:column title="${rejectComment}">
            <jstl:if test="${row.status == 'REJECTED' }">
                <jstl:out value="${row.rejectComment}"></jstl:out>
            </jstl:if>
        </display:column>

        <spring:message code="application.problem" var="problem"/>
        <display:column title="${problem}">
            <a href="problem/show.do?problemID=${row.problem.id}">
                <spring:message code="application.problem"/></a>
        </display:column>

        <spring:message code="application.update" var="update"/>
        <display:column title="${update}">
            <jstl:if test="${row.status == 'PENDING'}">
                <a href="application/rookie/update.do?applicationId=${row.id}">
                    <spring:message code="application.update"/></a>
            </jstl:if>
        </display:column>

        <spring:message code="application.show" var="show"/>
        <display:column title="${show}">
            <a href="application/rookie/show.do?applicationId=${row.id}">
                <spring:message code="application.show"/></a>
        </display:column>
        
        <spring:message code="application.xxxxx" var="show2"/>
        <display:column title="${show2}">
            <a href="xxxxx/rookie,company/list.do?applicationId=${row.id}">
                <spring:message code="application.xxxxx"/></a>
        </display:column>
    </display:table>
    

    <acme:cancel code="position.goBack" url="/"/>
</security:authorize>

<security:authorize access="hasRole('COMPANY')">
    <display:table name="applications" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

        <spring:message code="application.submitMoment" var="submitMoment"/>
        <display:column title="${submitMoment}">
            <jstl:out value="${row.submitMoment}"></jstl:out>
        </display:column>

        <spring:message code="application.explanation" var="explanation"/>
        <display:column title="${explanation}">
            <jstl:out value="${row.explanation}"></jstl:out>
        </display:column>

        <spring:message code="application.link" var="link"/>
        <display:column title="${link}">
            <a href="${row.link}">${row.link}</a>
        </display:column>

        <spring:message code="application.status" var="status"/>
        <display:column title="${status}" sortable="true">
            <jstl:if test="${row.status == 'ACCEPTED' }">
                <spring:message code="application.accepted"/>
            </jstl:if>

            <jstl:if test="${row.status == 'SUBMITTED' }">
                <spring:message code="application.submitted"/>
            </jstl:if>

            <jstl:if test="${row.status == 'REJECTED' }">
                <spring:message code="application.rejected"/>
            </jstl:if>

            <jstl:if test="${row.status == 'CANCELLED' }">
                <spring:message code="application.cancelled"/>
            </jstl:if>
        </display:column>

        <spring:message code="application.rejectComment" var="rejectComment"/>
        <display:column title="${rejectComment}">
            <jstl:if test="${row.status == 'REJECTED' }">
                <jstl:out value="${row.rejectComment}"></jstl:out>
            </jstl:if>
        </display:column>

        <spring:message code="application.problem" var="problem"/>
        <display:column title="${problem}">
            <a href="problem/show.do?problemID=${row.problem.id}">
                <spring:message code="application.problem"/></a>
        </display:column>

        <spring:message code="application.rookie" var="rookie"/>
        <display:column title="${rookie}">
            <a href="rookie/company/show.do?rookieId=${row.rookie.id}">
                <spring:message code="application.rookie"/></a>
        </display:column>

        <spring:message code="application.curricula" var="curricula"/>
        <display:column title="${curricula}">
            <a href="curricula/company/display.do?curriculaId=${row.curricula.id}">
                <spring:message code="application.curricula"/></a>
        </display:column>
        
        <spring:message code="application.show" var="show"/>
        <display:column title="${show}">
            <a href="application/company/show.do?applicationId=${row.id}">
                <spring:message code="application.show"/></a>
        </display:column>
        
        <spring:message code="application.xxxxx" var="show"/>
        <display:column title="${show}">
            <a href="xxxxx/rookie,company/list.do?applicationId=${row.id}">
                <spring:message code="application.xxxxx"/></a>
        </display:column>

        <spring:message code="application.accept" var="applicationAccept"/>
        <display:column title="${applicationAccept}">
            <jstl:if test="${row.status == 'SUBMITTED'}">
                <a href="application/company/accept.do?applicationId=${row.id}">
                    <spring:message code="application.accept"/></a>
            </jstl:if>
        </display:column>

        <spring:message code="application.reject" var="applicationReject"/>
        <display:column title="${applicationReject}">
            <jstl:if test="${row.status == 'SUBMITTED'}">
                <a href="application/company/reject.do?applicationId=${row.id}">
                    <spring:message code="application.reject"/></a>
                </a>
            </jstl:if>
        </display:column>

    </display:table>

    <acme:cancel code="position.goBack" url="position/company/list.do"/>

</security:authorize>