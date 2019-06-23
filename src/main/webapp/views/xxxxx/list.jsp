<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

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
	<jstl:if test="${row.isFinal == false }">
		<a href="xxxxx/rookie/edit.do?xxxxxId=${row.id}">
			<spring:message code="xxxxx.edit"/>
		</a>
	</jstl:if>
	</display:column>
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
	<display:column class="${css }" property="moment" title="${columnTitle}" format="{0,date,yy-MM-dd HH:mm}" />
	</jstl:if>
	<display:column class="${css }" property="ticker" title="Ticker"/>
</display:table>

<input type="button" name="create"
		value="<spring:message code="xxxxx.create" />"
		onclick="javascript: relativeRedir('xxxxx/rookie/create.do?applicationId=${applicationId}');" />

</security:authorize>

<security:authorize access="hasRole('COMPANY')">
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
	<display:column class="${css }" property="moment" title="${columnTitle}" format="{0,date,yy-MM-dd HH:mm}" />
	</jstl:if>
	<display:column class="${css }" property="ticker" title="Ticker"/>
</display:table>

<input type="button" name="create"
		value="<spring:message code="xxxxx.create" />"
		onclick="javascript: relativeRedir('xxxxx/rookie/create.do?applicationId=${applicationId}');" />

</security:authorize>