package fshare.serveur;

/**
 * Title:        RemoteServeurImp.java
 * Description:  Impl�mentation de l'interface RemoteServeur.
 * Copyright:    Copyright (c) 2004
 * Company:
 * @author Giraud R�my, Mandrioli Damien
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
   * Ajoute dans la liste du serveur, que le client <b>client</b> poss�de le fichier <b>fichier</b> avec les attributs <b>attr</b>.
   * @param fichier le fichier qui le client poss�de.
   * @param client le client qui poss�de le fichier.
   * @param attr les attributs sur le fichier pour ce client.
   */
  public void ajouterFichier(Fichier fichier, RemoteClient client,
                             AttributFichierClient attr)
  /* throws java.rmi.RemoteException */
  {
    logger.info("Ajout du fichier qui a pour cl� : " + fichier.getNomFichier() +
                ", qui a pour type : " + fichier.getTypeFichier());
/*    System.out.println("listeFichierServeur initialis�, null ?" +
                       ( (listeFichierServeur == null) ? "oui" : "non"));
    System.out.println("Attribut fichier initialis�, null ?" +
                       ( (attr == null) ? "oui" : "non"));
    System.out.println("client initialis�, null ?" +
                       ( (client == null) ? "oui" : "non")); */
    listeFichierServeur.ajouterFichier(fichier, client, attr);
  }

  /**
   * Retire dans le liste du serveur le fichier <b>fichier</b> pour le client <b>client</b>.
   * @param fichier le fichier que le client souhaite d�partager.
   * @param client le client qui souhaite d�partager le fichier.
   */
  public void retirerFichier(Fichier fichier,
                             RemoteClient client)
  /* throws java.rmi.RemoteException */
  {
    listeFichierServeur.retirerFichier(fichier, client);
  }

  /**
   * Retire de la liste du serveur TOUS les fichiers partag�s par le client <b>client</b>.
   * @param client le client qui d�partage tous ces fichiers.
   */
  public void retirerFichier(RemoteClient client)
  /* throws java.rmi.RemoteException */
  {
    listeFichierServeur.retirerFichierClient(client);
  }

  /**
   * Recherche une liste de fichier repondant � l'expression r�guliere <b>regexp</b>.
   * @param regexp l'expression r�guli�re pour matcher les fichiers.
   * @return la liste des nom de fichiers correspondant a l'expression r�guli�re.
   */
  public Fichier[] rechercherFichier(String regexp)
  /* throws java.rmi.RemoteException */
  {
    return listeFichierServeur.rechercherFichier(regexp);
  }

  /**
   * Donne la liste de tous les clients qui poss�de le fichier d'identifiant <b>idFichier</b>
   * @param idFichier l'identifiant du fichier consid�r�.
   * @return la liste des clients qui poss�de ce fichier.
   */
  public RemoteClient[] rechercherClient(String idFichier)
  /* throws java.rmi.RemoteException */
  {
    return listeFichierServeur.rechercherClient(idFichier);
  }

  public String getDateElement(int i) {
    Calendar calendrier = Calendar.getInstance(Locale.FRENCH);
    calendrier.setTime(new Date(System.currentTimeMillis()));
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

    // Propri�t�s de la sortie
    serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
    serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "etat.dtd");
    serializer.setOutputProperty(OutputKeys.INDENT, "yes");

    hd.setResult(streamResult);
    // D�but du document
    hd.startDocument();
    // On utilise un seul ensemble d'attributs que l'on vide a chaque element
    AttributesImpl atts = new AttributesImpl();

    atts.addAttribute("", "", "xmlns", "CDATA",
                      "http://deptinfo.unice.fr/minfo/ns/p2p");

    hd.startElement("", "", "etat", atts);

    atts.clear();
    // Attributs de date
    atts.addAttribute("", "", "jour", "NMTOKEN",
                      getDateElement(Calendar.DAY_OF_MONTH));
    atts.addAttribute("", "", "mois", "NMTOKEN", getDateElement(Calendar.MONTH));
    atts.addAttribute("", "", "ann�e", "NMTOKEN", getDateElement(Calendar.YEAR));
    atts.addAttribute("", "", "heure", "NMTOKEN",
                      getDateElement(Calendar.HOUR_OF_DAY));
    atts.addAttribute("", "", "minute", "NMTOKEN",
                      getDateElement(Calendar.MINUTE));

    hd.startElement("", "", "date", atts);
    hd.endElement("", "", "date");

    atts.clear();
    //Liste de fichiers
    hd.startElement("", "", "fichiers", atts);

    Fichier[] tmp = listeFichierServeur.getFichier();
    for (int i = 0; i < tmp.length; i++){
      atts.clear();
      hd.startElement("", "", "fichier",atts);
      hd.startElement("", "", "nom",atts);
      char [] nomFichier = tmp[i].getNomFichier().toCharArray();
      hd.characters(nomFichier, 0, nomFichier.length);
      hd.endElement("","","nom");

      // Manque la date

      // Taille du fichier
      hd.startElement("","","taille",atts);
      char [] tailleFichier = Long.toString(tmp[i].getTailleFichier()).toCharArray();
      hd.characters(tailleFichier, 0, tailleFichier.length);
      hd.endElement("","","taille");

      // Type du fichier
      hd.startElement("","","type",atts);
      char [] typeFichier = tmp[i].getTypeFichier().toCharArray();
      hd.characters(typeFichier, 0, typeFichier.length);
      hd.endElement("","","type");

      // Les clients poss�dant le fichier
      RemoteClient [] clients = listeFichierServeur.rechercherClient(tmp[i].getIdFichier());

    // Il faut r�cup�rer un ID, un NOM, une DATE par client

    // IL MANQUE DES DONNEES DANS LA STRUCTURE POUR LE MOMENT
    // IL FAUDRAIT EVITER DE FAIRE DES APPELS DISTANTS POUR DEMANDER LE NOM DE
    // CHAQUE CLIENT LORS DE LA GENERATION XML

    for (int j=0; j<clients.length; j++){
        //atts.addAttribute("", "", "id", "NMTOKEN", clients[i].getIdClient());
      }
      hd.endElement("", "", "fichier");
    }
    hd.endElement("", "", "fichiers");
    hd.endElement("", "", "etat");
    hd.endDocument();
    System.out.println("Export vers XML termin�");

      }
  /**
   * Met � jour les attributs <b>attr</b> du fichier aynt pour identifiant <b>idFichier</b> du client <b>client</b>.
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
      // Cr�e un serveur HTTP sur le port 8765
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
