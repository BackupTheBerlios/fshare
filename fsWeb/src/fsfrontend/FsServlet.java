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


  //Initialize global variables
  public void init() throws ServletException
  {
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
        // Get concrete implementation
        TransformerFactory tFactory = TransformerFactory.newInstance();
        // Create a reusable templates for a particular stylesheet
        Templates templates = tFactory.newTemplates( new StreamSource( webApp.getRealPath( XSLT_PATH ) ) );
        // Create a transformer
        Transformer transformer = templates.newTransformer();

        // Get concrete implementation
        DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
        // Need a parser that support namespaces
        dFactory.setNamespaceAware( true );
        // Create the parser
        DocumentBuilder parser = dFactory.newDocumentBuilder();
        // Parse the XML document
        Document doc = parser.parse( webApp.getRealPath( XML_PATH ) );
        // Get the XML source
        Source xmlSource =  new DOMSource( doc );

        response.setContentType("text/html");
        // Transform input XML doc in HTML stream
        StringWriter sw = new StringWriter ();
        Result res = new StreamResult (sw);

        String sort = request.getParameter( "typeRequete" );

        if ( sort != null ) {
          if (sort.equals("clients"))
          {
            transformer.setParameter ("typeRequete", Boolean.TRUE);
          }
        }



        transformer.transform( xmlSource, res);

        //Forward au jsp
        request.setAttribute("html", sw.toString());
//        request.setAttribute("obj", obj);

        ServletContext context = this.getServletConfig().getServletContext();

        RequestDispatcher rq = context.getRequestDispatcher("/FsJsp.jsp");

        rq.forward(request, response);

    } catch (Exception ex) {
        throw new ServletException( ex );
    }
}

}
