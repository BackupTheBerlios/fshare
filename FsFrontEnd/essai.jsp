<%@ page import="fshare.fsFrontEnd.TestObjet" %>
<%@ page contentType="text/html; charset=ISO-8859-5" %>
<%! int i = 0; %>

<HTML>
<HEAD>
	<TITLE>Un essai</TITLE>
</HEAD>
<BODY>
C'est un test.<BR>
<% for (; i < 5; ++i) { %> <%=i%> <br> <% }%>

<%
for (int j = 0; j < 4; ++j) {
	out.println("valeur : " + j); %> <br />
  <%
}
	String cli = (String) request.getAttribute("client");
  out.println("parametre client : " + cli);
  TestObjet obj;
  obj = (TestObjet) request.getAttribute("obj");
  out.println("Le client de nom : " + obj.getNom() + ", a pour age : " + obj.getAge());
  %>
</BODY>
</HTML>
