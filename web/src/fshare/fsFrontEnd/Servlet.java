package fshare.fsFrontEnd;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Servlet extends HttpServlet
{
  public Servlet()
  {
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException,
      java.io.IOException
  {
    traitementRequete(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException,
      java.io.IOException
  {
    traitementRequete(request, response);
  }

  private void traitementRequete (HttpServletRequest request,
                                  HttpServletResponse response) throws
      ServletException,
      java.io.IOException

  {
    response.setContentType ("text/html");
    PrintWriter out = null;
    try
    {
      out = response.getWriter ();
/*
      out.println ("Coucou ca marche 1\n");
 */
      String cli = request.getParameter("client");
/*
      if (null == cli)
        out.println("client null");
      else
        out.println("valeur de client = " + cli);

      out.flush ();
  */
      TestObjet obj = new TestObjet (cli, 10);
      request.setAttribute("client", (cli + "- TRAITe"));
      request.setAttribute("obj", obj);

      ServletContext context = this.getServletConfig().getServletContext();

      RequestDispatcher rq = context.getRequestDispatcher("/essai.jsp");

      rq.forward(request, response);

    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
  }
}
