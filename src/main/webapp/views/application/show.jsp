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

<style type="text/css">
.GREEN{
  background-color: Indigo;
}
.ORANGE{
  background-color: DarkSlateGray;
}
.RED{
  background-color: PapayaWhip;
}
</style>

<security:authorize access="hasRole('ROOKIE')">

    <acme:showtext fieldset="true" code="position.ticker" value="${position.ticker}"/>

    <jstl:if test="${application.status != 'PENDING'}">
        <acme:showtext fieldset="true" code="application.submitMoment" value="${application.submitMoment}"/>
        <acme:showtext fieldset="true" code="application.explanation" value="${application.explanation}"/>

        <fieldset>
            <legend><spring:message code="application.link"/></legend>
            <a href='${application.link}'>${application.link}</a>
        </fieldset>
    </jstl:if>

    <fieldset>
        <legend><spring:message code="application.status"/></legend>
        <jstl:if test="${application.status == 'ACCEPTED' }">
            <spring:message code="application.accepted"/>
        </jstl:if>

        <jstl:if test="${application.status == 'SUBMITTED' }">
            <spring:message code="application.submitted"/>
        </jstl:if>

        <jstl:if test="${application.status == 'PENDING' }">
            <spring:message code="application.pending"/>
        </jstl:if>

        <jstl:if test="${application.status == 'REJECTED' }">
            <spring:message code="application.rejected"/></jstl:if>

        <jstl:if test="${application.status == 'CANCELLED' }">
            <spring:message code="application.cancelled"/>
        </jstl:if>
    </fieldset>

    <jstl:if test="${application.status == 'REJECTED' }">
        <acme:showtext fieldset="true" code='application.rejectComment'
                       value="${application.rejectComment}"/>
    </jstl:if>

    <fieldset>
        <legend><spring:message code="application.problem"/></legend>
        <a href="problem/show.do?problemID=${application.problem.id}"><spring:message code="application.problem"/></a>
    </fieldset>
    
    <display:table name="xxxxx" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

	<jstl:choose>
		<jstl:when test="${row.moment > haceUnMes }">
			<jstl:set var="css" value="GREEN"/>
		</jstl:when>
		<jstl:when test="${row.moment < haceUnMes && row.moment>haceDosMeses}">
			<jstl:set var="css" value="ORANGE"/>
		</jstl:when>
		<jstl:otherwise>
			<jstl:set var="css" value="RED" />
		</jstl:otherwise>
	</jstl:choose>
	

	<display:column class="${css }">
		<a href="xxxxx/rookie,company/show.do?xxxxxId=${row.id}">
			<spring:message code="xxxxx.view"/> ${month }
		</a>
	</display:column>
	<jstl:if test="${lang=='es'}">
	<spring:message code="xxxxx.moment" var="columnTitle"/>
	<display:column  class="${css }" property="moment" title="${columnTitle}" format="{0,date,dd-MM-yy HH:mm}" />
	</jstl:if>
	<jstl:if test="${lang=='en'}">
	<spring:message code="xxxxx.moment" var="columnTitle"/>
	<display:column class="${css }" property="moment" title="${columnTitle}" format="{0,date,yy/MM/dd HH:mm}" />
	</jstl:if>
	<display:column class="${css }" property="ticker" title="Ticker"/>
</display:table>
    

    <acme:cancel code="position.goBack" url="application/rookie/list.do"/>

</security:authorize>
