package tp.jsp;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Recup implements Servlet
{
  public Recup () {}

  public void init (ServletConfig config) throws ServletException {}

  public void service (ServletRequest req, ServletResponse rep)
           throws ServletException, IOException
  {
    rep.setContentType ("text/html");
    PrintWriter out = rep.getWriter ();
    out.println ("Coucou ca marche toujours\n");
    out.flush ();
  }

  public void destroy () {}


  public java.lang.String getServletInfo() { return null; }

  public ServletConfig getServletConfig() { return null;}
}
