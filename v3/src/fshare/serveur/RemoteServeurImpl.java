package fshare.serveur;

/**
 * Title:        RemoteServeurImp.java
 * Description:  Implémentation de l'interface RemoteServeur.
 * Copyright:    Copyright (c) 2004
 * Company:
 * @author Giraud Rémy, Mandrioli Damien
 * @version 1.0
 */

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.logging.Logger;
import java.util.logging.Level;

import fshare.remote.RemoteServeur;
import fshare.serveur.ListeFichierServeur;
import fshare.commun.AttributFichierClient;
import fshare.commun.Fichier;
import fshare.remote.RemoteClient;
import fshare.serveur.serveurhttp.ClassFileServer;
import java.io.*;
// SAX classes.
import org.xml.sax.*;
import org.xml.sax.helpers.*;
//JAXP 1.1
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.sax.*;
import fshare.gui.MainServer;
import java.util.Properties;
import java.util.Locale;
import java.text.DateFormat;
import java.util.Date;
import java.util.Calendar;

public class RemoteServeurImpl
    extends UnicastRemoteObject
    implements fshare.remote.RemoteServeur {

  private ListeFichierServeur listeFichierServeur = null;
  private MainServer mainServer;
  private static Logger logger =
      Logger.getLogger("fshare.serveur");

  public RemoteServeurImpl() throws RemoteException {
    listeFichierServeur = new ListeFichierServeur();
    mainServer = new MainServer(this);
  }

  /**
   * Ajoute dans la liste du serveur, que le client <b>client</b> possède le fichier <b>fichier</b> avec les attributs <b>attr</b>.
   * @param fichier le fichier qui le client possède.
   * @param client le client qui possède le fichier.
   * @param attr les attributs sur le fichier pour ce client.
   */
  public void ajouterFichier(Fichier fichier, RemoteClient client,
                             AttributFichierClient attr)
  /* throws java.rmi.RemoteException */
  {
    logger.info("Ajout du fichier qui a pour clé : " + fichier.getNomFichier() +
                ", qui a pour type : " + fichier.getTypeFichier());
    System.out.println("listeFichierServeur initialisé, null ?" +
                       ( (listeFichierServeur == null) ? "oui" : "non"));
    System.out.println("Attribut fichier initialisé, null ?" +
                       ( (attr == null) ? "oui" : "non"));
    System.out.println("client initialisé, null ?" +
                       ( (client == null) ? "oui" : "non"));
    listeFichierServeur.ajouterFichier(fichier, client, attr);
  }

  /**
   * Retire dans le liste du serveur le fichier <b>fichier</b> pour le client <b>client</b>.
   * @param fichier le fichier que le client souhaite départager.
   * @param client le client qui souhaite départager le fichier.
   */
  public void retirerFichier(Fichier fichier,
                             RemoteClient client)
                             /* throws java.rmi.RemoteException */
                             {
    listeFichierServeur.retirerFichier(fichier, client);
  }

  /**
   * Retire de la liste du serveur TOUS les fichiers partagés par le client <b>client</b>.
   * @param client le client qui départage tous ces fichiers.
   */
  public void retirerFichier(RemoteClient client)
  /* throws java.rmi.RemoteException */
  {
    listeFichierServeur.retirerFichierClient(client);
  }

  /**
   * Recherche une liste de fichier repondant à l'expression réguliere <b>regexp</b>.
   * @param regexp l'expression régulière pour matcher les fichiers.
   * @return la liste des nom de fichiers correspondant a l'expression régulière.
   */
  public Fichier[] rechercherFichier(String regexp)
  /* throws java.rmi.RemoteException */
  {
    return listeFichierServeur.rechercherFichier(regexp);
  }

  /**
   * Donne la liste de tous les clients qui possède le fichier d'identifiant <b>idFichier</b>
   * @param idFichier l'identifiant du fichier considéré.
   * @return la liste des clients qui posséde ce fichier.
   */
  public RemoteClient[] rechercherClient(String idFichier)
  /* throws java.rmi.RemoteException */
  {
    return listeFichierServeur.rechercherClient(idFichier);
  }

  public String getDateElement(int i){
    Calendar calendrier = Calendar.getInstance(Locale.FRENCH);
    calendrier.setTime(new Date(System.currentTimeMillis()));
    String renvoi = null;
    if (i == Calendar.MONTH)
      renvoi = Integer.toString(((calendrier.get(i) + 1)%12));
    else renvoi = Integer.toString(calendrier.get(i));

    if (renvoi.length() == 1)
      renvoi = "0" + renvoi;
    return renvoi;
  }

  public void xmlExport() throws TransformerConfigurationException,
      SAXException {
  // Creation du flux de sortie
    FileOutputStream fos = null;

    try {
      fos = new FileOutputStream("etat.xml");
    }
    catch (FileNotFoundException ex) {
      System.out.println(ex);
    }

  // Sax init
    PrintWriter out = new PrintWriter(fos);
    StreamResult streamResult = new StreamResult(out);
    SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.
        newInstance();
    TransformerHandler hd = tf.newTransformerHandler();
    Transformer serializer = hd.getTransformer();

    serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
    serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "etat.dtd");
    serializer.setOutputProperty(OutputKeys.INDENT, "yes");
    hd.setResult(streamResult);
    hd.startDocument();
    AttributesImpl atts = new AttributesImpl();
// USERS tag.

      System.out.println(getDateElement(Calendar.HOUR_OF_DAY));
      System.out.println(getDateElement(Calendar.MINUTE));
      System.out.println(getDateElement(Calendar.DAY_OF_MONTH));
      System.out.println(getDateElement(Calendar.MONTH));
      System.out.println(getDateElement(Calendar.YEAR));


    hd.startElement("", "", "date", atts);

// USER tags.
    String[] id = {
        "PWD122", "MX787", "A4Q45"};
    String[] type = {
        "customer", "manager", "employee"};
    String[] desc = {
        "Tim@Home", "Jack&Moud", "John D'oé"};
    for (int i = 0; i < id.length; i++) {
      atts.clear();
      atts.addAttribute("", "", "ID", "CDATA", id[i]);
      atts.addAttribute("", "", "TYPE", "CDATA", type[i]);
      hd.startElement("", "", "USER", atts);
      hd.characters(desc[i].toCharArray(), 0, desc[i].length());
      hd.endElement("", "", "USER");
    }
    hd.endElement("", "", "USERS");
    hd.endDocument();

  }

  /**
   * Met à jour les attributs <b>attr</b> du fichier aynt pour identifiant <b>idFichier</b> du client <b>client</b>.
   * @param idFichier l'identifiant du fichier.
   * @param client le client.
   * @param attr les nouveaux attributs.
   */
  public void majAttrFichier(String idFichier, RemoteClient client,
                             AttributFichierClient attr)
  /* throws java.rmi.RemoteException */
  {
    listeFichierServeur.majAttrFichier(idFichier, client, attr);
  }

  public static void main(String[] args) {
    try {
      // Crée un serveur HTTP sur le port 8765
      ClassFileServer httpServer = new ClassFileServer( ( (args.length < 2) ?
          8765 : Integer.parseInt(args[1])), null);

      if (args.length < 1) {
        System.out.println("Donner le nom du serveur en argument");
        System.exit(1);
      }
      //Logger.getLogger("fshare.client").setLevel(Level.WARNING);
      String serverName = args[0];
      // Create and install a security manager
      if (System.getSecurityManager() == null) {
        System.setSecurityManager(new java.rmi.RMISecurityManager());
      }
      RemoteServeurImpl obj = new RemoteServeurImpl();
      Naming.rebind(serverName, obj);
      System.out.println("Server bound in registry under URL ");
      System.out.println("   rmi://" + getHostName() + "/" + serverName);
    }
    catch (Exception e) {
      System.out.println("ServerImpl err: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static String getHostName() {
    try {
      return java.net.InetAddress.getLocalHost().getHostName();
    }
    catch (java.net.UnknownHostException e) {
      return "Unknown";
    }
  }

} // Classe RemoteServeurImpl
