package fsfrontend;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.*;
import org.w3c.dom.*;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FsServlet extends HttpServlet
{
  private static final String CONTENT_TYPE = "text/html";

  public final static String
      /** The path to the stylesheet. */
      XSLT_PATH = "WEB-INF/etat.xsl",
      /** The path to the XML doc. */
      XML_PATH = "etat.xml";

  private FsBean beanStandard;



  //Initialize global variables
  public void init() throws ServletException
  {
    beanStandard = new FsBean (this, XSLT_PATH, XML_PATH);
  }

  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    processRequest (request, response);
/*
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();

    out.println("<html>");
    out.println("<head><title>FsServlet</title></head>");
    out.println("<body bgcolor=\"#ffffff\">");
    out.println("<p>The servlet has received a " + request.getMethod() + ". This is the reply.</p>");
    out.println("</body></html>");
*/
  }

  //Process the HTTP Post request
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    doGet(request, response);
  }

  //Clean up resources
  public void destroy()
  {
  }


  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
throws ServletException {
    ServletContext webApp = this.getServletContext();

    try {

        String typeRequete = request.getParameter( "typeRequete" );
        String requete    =  request.getParameter("requete");
        String typeTri    =  request.getParameter("typeTri");

        if (typeRequete == null) /* On a pas su récupérer la valeur de la requete, rien à faire */
          return;

        /* On traite les feuilles XSLT pour la liste des fichiers et la liste des clients */
        if (typeRequete.equals ("clients") || typeRequete.equals ("fichiers"))
        {

          beanStandard.setParameter ("typeRequete", typeRequete);
          beanStandard.setParameter("typeTri", typeTri);
          beanStandard.setParameter("requete", requete);

        }
        else
        {
            //autre valeur de feuille XSLT
        }



        request.setAttribute("html", beanStandard.getTransformXmlByXslt());
//response.getWriter().println(fsBean.getTransformXmlByXslt());

        ServletContext context = this.getServletConfig().getServletContext();

        RequestDispatcher rq = context.getRequestDispatcher("/FsJsp.jsp");

        rq.forward(request, response);

    } catch (Exception ex) {
        throw new ServletException( ex );
    }
}

}
