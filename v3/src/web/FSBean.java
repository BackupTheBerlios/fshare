package web;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class FSBean {

  // Le fichier XML sous forme d'arbre DOM
  Document doc;

  public FSBean() {
    // On récupère le contenu du document
    doc = parseXmlFile("etat.xml", true);

    visit(doc,0);
  }


  public static void main(String[] args) {



        // Parses an XML file and returns a DOM document.
        // If validating is true, the contents is validated against the DTD
        // specified in the file.
  }

  public static Document parseXmlFile(String filename, boolean validating) {
    try {
      // Create a builder factory
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(validating);
      factory.setNamespaceAware(true);
      // Create the builder and parse the file
      Document doc = factory.newDocumentBuilder().parse(new File(filename));
      return doc;
    }
    catch (SAXException e) {
      // A parsing error occurred; the xml input is not valid
    }
    catch (ParserConfigurationException e) {
    }
    catch (IOException e) {
    }
    return null;
  }

  // This method visits all the nodes in a DOM tree
  public static void visit(Node node, int level) {
    // Process node

    // If there are any children, visit each one
    NodeList list = node.getChildNodes();
    for (int i = 0; i < list.getLength(); i++) {
      // Get child node
      if (list.item(i).getNodeName() == "date"){
        System.out.println("Date : "+list.item(i).getAttributes().getNamedItem("jour"));
      }
      System.out.println(list.item(i).getNodeValue());
      Node childNode = list.item(i);

      // Visit child node
      visit(childNode, level + 1);
    }
  }

  public FSDocument getClients() {
    return null;
  }

  public FSDocument getFiles() {
    return null;
  }

  public FSDocument getFilesforClient(String idClient) {
    return null;
  }

  public FSDocument getClientsForFile(String idFile) {
    return null;
  }

  public FSDocument getClientsByFiles() {
    return null;
  }

  public FSDocument getFilesByClients() {
    return null;
  }



}
