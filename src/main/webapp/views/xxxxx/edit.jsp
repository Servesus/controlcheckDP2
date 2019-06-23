<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ROOKIE')">
<form:form action="xxxxx/rookie/edit.do?applicationId=${applicationId}" modelAttribute="xxxxx">
	<form:hidden path="id" />
	
	<form:label path="body">
		<spring:message code="xxxxx.body"/>
	</form:label>
	<form:textarea path="body"/>
	<form:errors cssClass="error" path="body"/>
	<br />
	
	<form:label path="picture">
		<spring:message code="xxxxx.picture"/>
	</form:label>
	<form:input path="picture"/>
	<form:errors cssClass="error" path="picture" /> 
	<br />
	
	<input type="submit" name="draft"
		value="<spring:message code="xxxxx.draft" />" />&nbsp; 
	<input type="submit" name="final"
		value="<spring:message code="xxxxx.final" />" />&nbsp; 
	<jstl:if test="${xxxxx.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="xxxxx.delete" />" />
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="xxxxx.cancel" />"
		onclick="javascript: relativeRedir('/xxxxx/rookie,company/list.do?applicationId=${applicationId}');" />
	<br />
	
	
</form:form>
</security:authorize>