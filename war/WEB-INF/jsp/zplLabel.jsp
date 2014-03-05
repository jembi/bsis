<%@page contentType="text/zpl" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:scriptlet>
response.setHeader("Content-Disposition", "attachment; filename=\"label.zpl\"");
</jsp:scriptlet>

<c:if test="${!empty labelZPL}">
${labelZPL}
</c:if>