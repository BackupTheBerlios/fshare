package fsfrontend;

import javax.xml.transform.Transformer;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.Serializable;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FsBean implements Serializable
{

  private String xsltPath = null;
  private String xmlPath = null;
  private String requete = null;
  private Transformer transformer = null;
  private HttpServlet servlet = null;


  /**
   * Constructeur par défaut.
   */
  public FsBean ()
  {}

  /**
   * Construit un bean avec les fichier XML et XSLT.
   *
   * @param servlet la servlet en question.
   * @param xsltPath le path avec nom du fichier du fichier XSLT.
   * @param xmlPath le path avec nom du fichier du fichier XML.
   * @throws ServletException
   */
  public FsBean(HttpServlet servlet, String xsltPath, String xmlPath) throws
      ServletException
  {
    this.xsltPath = xsltPath;
    this.xmlPath = xmlPath;
    this.servlet = servlet;
    initTransformer ();
  }

  /**
   * Construit un bean avec les fichier XML et XSLT.
   *
   * @param xsltPath le path avec nom du fichier du fichier XSLT.
   * @param xmlPath le path avec nom du fichier du fichier XML.
   * @param requete la requete demandé.
   */
/*
  public FsBean(String xsltPath, String xmlPath, String requete)
  {
    this.xsltPath = xsltPath;
    this.xmlPath = xmlPath;
    this.requete = requete;
  }
*/


  /**
   * @return le path avec nom du fichier du fichier XSLT.
   */
  public String getXsltPath ()
  {
    return this.xsltPath;
  }

  /**
   * Fixe le fichier xslt.
   * @param xsltPath String
   */
  public void setXsltPath (String xsltPath)
  {
    this.xsltPath = xsltPath;
  }

  /**
   * @return le path avec nom du fichier du fichier XML.
   */
  public String getXmlPath ()
  {
    return this.xmlPath;
  }

  /**
   * Fixe le fichier xml.
   * @param xmlPath String
   */
  public void setXmlPath (String xmlPath)
  {
    this.xmlPath = xmlPath;
  }

  /**
   * Transforme le Xml en appliquant la feuille Xslt et renvoi le résutat sous forme de string.
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   * @throws TransformerException
   * @return le fichier xml traité par la feuille de style Xslt.
   */
  public String getTransformXmlByXslt () throws ParserConfigurationException,
      IOException, SAXException, TransformerException
  {
    ServletContext webApp = servlet.getServletContext ();
    // Get concrete implementation
    DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance ();
    // Need a parser that support namespaces
    dFactory.setNamespaceAware (true);
    // Create the parser
    DocumentBuilder parser = dFactory.newDocumentBuilder ();
    // Parse the XML document
    Document doc = parser.parse (webApp.getRealPath (xmlPath));
    // Get the XML source
    Source xmlSource = new DOMSource (doc);


    // Transform input XML doc in HTML stream
    StringWriter sw = new StringWriter ();
    Result res = new StreamResult (sw);
    transformer.transform( xmlSource, res);

    return sw.toString();
  }


  /**
   * Ajoute la valeur <b>value</b> pour le parametre <b>name</b> dans la feuille XSLT.
   * @param name le nom du parametre.
   * @param value la valeur du parametre.
   */
  public void setParameter (String name, Object value)
  {
    transformer.setParameter (name, value);
  }

  /**
   * Initialise le transformeur.
   * @throws ServletException
   */
  private void initTransformer () throws ServletException
  {
    try
    {
      ServletContext webApp = servlet.getServletContext ();
      // Get concrete implementation
      TransformerFactory tFactory = TransformerFactory.newInstance ();
      // Create a reusable templates for a particular stylesheet
      Templates templates = tFactory.newTemplates (new StreamSource (webApp.
          getRealPath (xsltPath)));
      // Create a transformer
      transformer = templates.newTransformer ();

    }
    catch (Exception ex)
    {
      throw new ServletException (ex);
    }

  }

}
