<%@ page import="it.codingjam.todolist.api.utils.APIVersion" %>
<%@ page session="false" %>
<% response.sendRedirect("/api" + APIVersion.V1 + "/todos"); %>