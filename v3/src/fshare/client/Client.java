package fshare.client;

/**
 * <p>Title: Client.java </p>
 * <p>Description: Le client a lancer pour l'application de partage de fichier. </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Giraud R�my, Mandrioli Damien
 * @version 1.0
 */

import fshare.gui.Main;
import java.util.logging.Logger;
import fshare.remote.RemoteServeur;
import fshare.util.Propriete;
import java.io.File;
import fshare.commun.Fichier;
import fshare.commun.AttributFichierClient;
import fshare.gui.Main;
import java.util.logging.Level;
import java.rmi.*;
import java.util.*;

public class Client {

  public static final String DEFAULT_SHARE_REP = "partage";
  public static final String FIC_PROPRIETE = "setting.props";

  private static Logger logger =
      Logger.getLogger("fshare.client");

  private Main appli = null;
  private String nomClient = null;
  private RemoteServeur fServeur = null;
  private ClientImpl client = null;
  private String repertoirePartage = null;
  private String urlServer = null;
  private ArrayList fichiersPartage = new ArrayList();

  public Client(String serverURL, String nomClient) throws java.rmi.
      RemoteException {
    if (serverURL == null || nomClient == null) {
      serverURL = Propriete.getPropriete(FIC_PROPRIETE, "urlServer");
      nomClient = Propriete.getPropriete(FIC_PROPRIETE, "nickName");
    }
    urlServer = serverURL;
    this.nomClient = nomClient;

    if (serverURL == null || nomClient == null) {
      client = new ClientImpl();
      prepareInfoFichierClient();
      appli = new Main(this);
    }
    else {
      try {

        /* R�cupere l'objet serveur distant */
        logger.info("Connexion au serveur...");
        fServeur = (RemoteServeur) java.rmi.Naming.lookup(serverURL);
        logger.info("Connexion REUSSIE");
      }
      catch (java.net.MalformedURLException e) {
        throw new java.rmi.RemoteException(
            "The URL of the server if not well formed");
      }
      catch (java.rmi.NotBoundException e) {
        throw new java.rmi.RemoteException(
            "The server was not found at the given URL");
      }
      client = new ClientImpl();
      this.nomClient = nomClient;
      prepareInfoFichierClient();
      //client.afficheListeFichierClient();
      appli = new Main(this);

    }
  }

  public String getServerName() {
    return urlServer;
  }

  public String getNickName() {
    return nomClient;
  }

  public boolean isConnected(){
    return (fServeur != null);
  }

  public void deconnect(){
    fServeur = null;
  }

  public void connectToServer(String serverURL, String nomClient) throws java.
      rmi.RemoteException {
    try {

      /* R�cupere l'objet serveur distant */
      logger.info("Connexion au serveur...");
      fServeur = (RemoteServeur) java.rmi.Naming.lookup(serverURL);
      logger.info("Connexion REUSSIE");
    }
    catch (java.net.MalformedURLException e) {
      throw new java.rmi.RemoteException(
          "The URL of the server if not well formed");
    }
    catch (java.rmi.NotBoundException e) {
      throw new java.rmi.RemoteException(
          "The server was not found at the given URL");
    }
    this.nomClient = nomClient;
    client = new ClientImpl();

    setRepertoirePartage(repertoirePartage);

    Propriete.setPropriete(FIC_PROPRIETE, "urlServer", serverURL);
    Propriete.setPropriete(FIC_PROPRIETE, "nickName", nomClient);

  }

  public String[] getFichiers() {
    return client.getFichierClient();
  }

  public static void main(String[] args) throws Exception {
    Logger.getLogger("fshare.client").setLevel(Level.ALL);
    System.out.println("Entrer dans le main");

    if (args.length < 2) {
      System.out.println("You must give the URL of the chat server and the name of the client as arguments");
      System.out.println(" ex : ClientImpl rmi://clio.unice.fr/Server bob");
      System.exit(1);
    }
    System.out.println("Configuration security manager");

    // Create and install a security manager
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new java.rmi.RMISecurityManager());
    }
    System.out.println("Cr�ation du client");

    //Client fclient = new Client(args [0], args [1]);
    Client fclient = new Client(null, null);

    System.out.println("D�marrage du client");
    //fclient.startClient();

  }

  private void startClient() {
    try {
      client = new ClientImpl();
    }
    catch (RemoteException ex) {
      System.out.println(ex.getMessage());
    }

    prepareInfoFichierClient();
//client.afficheListeFichierClient();
    appli = new Main(this);
  }

  /**
   * Pr�pare les informations necessaire au bon fonctionnement du client
   */
  private void prepareInfoFichierClient() {
  /* On r�cupere le fichier de partage */
    //repertoirePartage = Propriete.getPropriete(FIC_PROPRIETE, "shareDir");

    if (null == repertoirePartage) {
      repertoirePartage = DEFAULT_SHARE_REP;
    }
    logger.info("Votre repertoire de partage est : " + repertoirePartage);

    /* Ajout des fichiers du repertoire de partage */
    lectureFichierDansRepertoire(new File(repertoirePartage));

    /* Ajout des fichiers en cours de t�l�chargement */
  }

  public void setRepertoirePartage(String rep) {
    repertoirePartage = rep;
    if (fServeur != null) {
      prepareInfoFichierClient();

    }
    Propriete.setPropriete(FIC_PROPRIETE, "shareDir", rep);
  }

  public String getRepertoirePartage() {
    return repertoirePartage;
  }

  public Fichier[] rechercherFichier(String regexp) {

    try {
      return fServeur.rechercherFichier(regexp);
    }
    catch (RemoteException ex) {
      System.err.println("Erreur lors de la requ�te de recherche : " + ex);
    }
    return null;
  }

  /**
   * R�cupere tous les fichiers dans le repertoire de partage (et sous rep�rtoires)
   *  et les ajoute �
   * la liste du client avec les bonnes informations.
   * @param rep le r�pertoire racine.
   */
  private void lectureFichierDansRepertoire(File rep) {
    if (rep.isDirectory()) {
      File[] list = rep.listFiles();
      for (int i = 0; i < list.length; i++) {
        if (list[i].isDirectory())
            /* Appel r�cursif sur les sous-r�pertoires */
             {
          lectureFichierDansRepertoire(list[i]);
        }
        else
        /* C'est un fichier */ {
          /* Cr�ation des informations sur le fichier */
          Fichier f = new Fichier(list[i].toString(), list[i].length(),
                                  Fichier.getTypeFichier(list[i]));
          AttributFichierClient atc = new AttributFichierClient(list[i].
              getAbsolutePath(),
              null, 1, true, nomClient);
          /* Enregistrement du fichier chez le client */
          client.ajouterFichier(f.getIdFichier(), atc);
          fichiersPartage.add(f);
          /* Enregistrement au niveau du serveur */
          try {
            if (fServeur != null) {
              fServeur.ajouterFichier(f, client, atc);

            }

          }
          catch (java.rmi.RemoteException e) {
            logger.severe("Impossible d'ajouter le fichier sur le serveur");
            e.printStackTrace();
          }
        }
      }
    }

  }

} //Classe Client
