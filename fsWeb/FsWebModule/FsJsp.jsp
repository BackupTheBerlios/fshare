<html>
<head>
<title>
FsJsp
</title>
</head>
<jsp:useBean id="fsJspBeanId" scope="session" class="fsfrontend.FsJspBean" />
<jsp:setProperty name="fsJspBeanId" property="*" />
<body bgcolor="#ffffff">

<%
	String cli = (String) request.getAttribute("html");
  out.println(cli);
%>


</body>
</html>
