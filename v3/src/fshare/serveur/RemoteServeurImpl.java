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
import java.util.Map;

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
    /*    System.out.println("listeFichierServeur initialisé, null ?" +
                           ( (listeFichierServeur == null) ? "oui" : "non"));
        System.out.println("Attribut fichier initialisé, null ?" +
                           ( (attr == null) ? "oui" : "non"));
        System.out.println("client initialisé, null ?" +
                           ( (client == null) ? "oui" : "non")); */
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

  // Prends en argument une constante Calendar.const, renvoi le formatage souhaité
  private static String getDateElement(int i, Date date) {
    Calendar calendrier = Calendar.getInstance(Locale.FRENCH);
    if (date == null) {
      calendrier.setTime(new Date(System.currentTimeMillis()));
    }
    else {
      calendrier.setTime(date);

    }
    String renvoi = null;
    if (i == Calendar.MONTH) {
      renvoi = Integer.toString( ( (calendrier.get(i) + 1) % 12));
    }
    else {
      renvoi = Integer.toString(calendrier.get(i));

    }
    if (renvoi.length() == 1) {
      renvoi = "0" + renvoi;
    }
    return renvoi;
  }

// Construit <client id="d" name="r"/>
  private static void setAttsClient(RemoteClient client,
                                    InfoFichierServeur info,
                                    AttributesImpl atts) {
    atts.clear();
    atts.addAttribute("", "", "id", "CDATA",
                      info.getAttributFichierClient(client).getIdClient());
    atts.addAttribute("", "", "name", "CDATA",
                      info.getAttributFichierClient(client).getNomClient());
  }

  private static void setDateOfClient(RemoteClient client,
                                      InfoFichierServeur info,
                                      AttributesImpl atts) {
    atts.clear();
    Date date = info.getAttributFichierClient(client).getDateDerModif();
    atts.addAttribute("", "", "jour", "NMTOKEN",
                      getDateElement(Calendar.DAY_OF_MONTH, date));
    atts.addAttribute("", "", "mois", "NMTOKEN",
                      getDateElement(Calendar.MONTH, date));
    atts.addAttribute("", "", "année", "NMTOKEN",
                      getDateElement(Calendar.YEAR, date));
    atts.addAttribute("", "", "heure", "NMTOKEN",
                      getDateElement(Calendar.HOUR_OF_DAY, date));
    atts.addAttribute("", "", "minute", "NMTOKEN",
                      getDateElement(Calendar.MINUTE, date));

  }

  // Modifie un ensemble d'attributs => Date
  private static void setDateNow(AttributesImpl atts) {
    atts.clear();
    atts.addAttribute("", "", "jour", "NMTOKEN",
                      getDateElement(Calendar.DAY_OF_MONTH, null));
    atts.addAttribute("", "", "mois", "NMTOKEN", getDateElement(Calendar.MONTH, null));
    atts.addAttribute("", "", "année", "NMTOKEN", getDateElement(Calendar.YEAR, null));
    atts.addAttribute("", "", "heure", "NMTOKEN",
                      getDateElement(Calendar.HOUR_OF_DAY, null));
    atts.addAttribute("", "", "minute", "NMTOKEN",
                      getDateElement(Calendar.MINUTE, null));

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

    // Propriétés de la sortie
    serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
    serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "etat.dtd");
    serializer.setOutputProperty(OutputKeys.INDENT, "yes");

    hd.setResult(streamResult);
    // Début du document
    hd.startDocument();
    // On utilise un seul ensemble d'attributs que l'on vide a chaque element
    AttributesImpl atts = new AttributesImpl();

    atts.addAttribute("", "", "xmlns", "CDATA",
                      "http://deptinfo.unice.fr/minfo/ns/p2p");

    hd.startElement("", "", "etat", atts);

    setDateNow(atts);
    hd.startElement("", "", "date", atts);
    hd.endElement("", "", "date");

    atts.clear();
    //Liste de fichiers
    hd.startElement("", "", "fichiers", atts);
    char[] charArray;
    Object[] tmp = listeFichierServeur.getInfoFichierServeur();

    // Pour chaque fichier du serveur
    for (int i = 0; i < tmp.length; i++) {
      atts.clear();
      hd.startElement("", "", "fichier", atts);

      // Le nom propre au fichier
      hd.startElement("", "", "nom", atts);
      charArray = ((InfoFichierServeur)tmp[i]).getFichier().getNomFichier().toCharArray();
      hd.characters(charArray, 0, charArray.length);
      hd.endElement("", "", "nom");

      // La date propre au fichier
      setDateNow(atts);
      hd.startElement("", "", "date", atts);
      hd.endElement("","","date");
      atts.clear();
      // La Taille propre au fichier
      hd.startElement("", "", "taille", atts);
      char[] tailleFichier = Long.toString(((InfoFichierServeur)tmp[i]).getFichier().getTailleFichier()).
          toCharArray();
      hd.characters(tailleFichier, 0, tailleFichier.length);
      hd.endElement("", "", "taille");

      // Type propre au fichier
      hd.startElement("", "", "type", atts);
      char[] typeFichier = ((InfoFichierServeur)tmp[i]).getFichier().getTypeFichier().toCharArray();
      hd.characters(typeFichier, 0, typeFichier.length);
      hd.endElement("", "", "type");

      // On boucle sur les clients possédants le fichier
      RemoteClient[] clients = ((InfoFichierServeur)tmp[i]).getListeClient();
      for (int j = 0; j < clients.length; j++) {
        setAttsClient(clients[j], (InfoFichierServeur)tmp[i], atts);
        // client + attributs
        hd.startElement("", "", "client", atts);
        // date propre au client
        setDateOfClient(clients[j], (InfoFichierServeur)tmp[i], atts);
        hd.startElement("", "", "date", atts);
        hd.endElement("", "", "date");
        hd.endElement("", "", "client");

      }
      hd.endElement("", "", "fichier");
    }
    hd.endElement("", "", "fichiers");
    hd.endElement("", "", "etat");
    hd.endDocument();
    System.out.println("Export vers XML terminé");

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
