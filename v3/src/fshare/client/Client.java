package fshare.client;

/**
 * <p>Title: Client.java </p>
 * <p>Description: Le client a lancer pour l'application de partage de fichier. </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Giraud Rémy, Mandrioli Damien
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
import java.io.FileOutputStream;
import java.io.*;
import fshare.remote.*;

public class Client implements Runnable {

  public static final String DEFAULT_SHARE_REP = "partage";
  public static final String FIC_PROPRIETE = "setting.props";
  /** Le temps d'attente avant de revérifier que d'autre client n'ont pas le fichier */
  public static final long DEFAULT_TEMPS_TEMPO = 5000;

  private static Logger logger =
      Logger.getLogger("fshare.client");

  private Main appli = null;
  private String nomClient = null;
  private RemoteServeur fServeur = null;
  private ClientImpl client = null;
  private String repertoirePartage = null;
  private String urlServer = null;
  private ArrayList fichiersPartage = new ArrayList();
  private long tempsTempo = 0;

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
      appli = new Main(this);
      client.setGui(appli);
      prepareInfoFichierClient();
      appli = new Main(this);
    }
    else {
      try {

        /* Mise en place du code base */
        System.setProperty ("java.rmi.server.codebase",
                            Client.getServerHostName(serverURL)
                            + ":" + 8766 + "/");
System.out.println("Code base : " + System.getProperty("java.rmi.server.codebase"));

        /* Récupere l'objet serveur distant */
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
      appli = new Main(this);
      client.setGui(appli);
      this.nomClient = nomClient;
      prepareInfoFichierClient();
     // client.afficheListeFichierClient();

      String temps = Propriete.getPropriete(FIC_PROPRIETE, "TEMPS_TEMPO");
      if (null == temps)
        tempsTempo = Client.DEFAULT_TEMPS_TEMPO;
      else
        tempsTempo = Long.parseLong(temps);

    }
  }

  public String getServerName() {
    return urlServer;
  }

  public String getNickName() {
    return nomClient;
  }

  public ClientImpl getClientImpl () {
    return client;
  }

  public ArrayList getFichierPartage () {
    return this.fichiersPartage;
  }

  public boolean isConnected(){
    return (fServeur != null);
  }

  public void reConnectToServer(){
    stopClient();
    appli.addTrace("Reconnexion au serveur " + getServerName());
    try {
      connectToServer(urlServer, nomClient);
    }
    catch (RemoteException ex) {
      ex.printStackTrace();
    }
  }

  public void connectToServer(String serverURL, String nomClient) throws java.
      rmi.RemoteException {
    try {
      appli.addTrace("Connexion au serveur " + getServerName());
      //On change le code base pour prendre celui du serveur
      System.setProperty ("java.rmi.server.codebase",
                          Client.getServerHostName(serverURL)
                          + ":" + 8766 + "/");
System.out.println("Code base : " + System.getProperty("java.rmi.server.codebase"));
      /* Récupere l'objet serveur distant */
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
    client.setGui(appli);
    setRepertoirePartage(repertoirePartage);

    Propriete.setPropriete(FIC_PROPRIETE, "urlServer", serverURL);
    Propriete.setPropriete(FIC_PROPRIETE, "nickName", nomClient);

  }
/*
  public String[] getFichiers() {
    return client.getFichierClient();
  }
*/
  public static void main(String[] args) throws Exception {
    Logger.getLogger("fshare.client").setLevel(Level.ALL);
    //System.out.println("Entrer dans le main");

/*    if (args.length < 2) {
      System.out.println("You must give the URL of the chat server and the name of the client as arguments");
      System.out.println(" ex : ClientImpl rmi://clio.unice.fr/Server bob");
      System.exit(1);
    }*/
    System.out.println("Configuration security manager");

    // Create and install a security manager
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new java.rmi.RMISecurityManager());
    }
    System.out.println("Création du client");

    //Client fclient = new Client(args [0], args [1]);
    Client fclient = new Client(null, null);

    System.out.println("Démarrage du client");
    fclient.startClient();

  }

  private void startClient() {
/*    try {
      client = new ClientImpl();
    }
    catch (RemoteException ex) {
      System.out.println(ex.getMessage());
    }

    prepareInfoFichierClient();
//client.afficheListeFichierClient();
    appli = new Main(this);*/
//System.out.println("Entré dans télécharger");
  //  telechargeFichier ();
  }

  /**
   * Arrete le client (enleve les fichiers du serveur).
   */
  public void stopClient ()
  {
    /* On enlève les fichiers du serveur */
    try
    {
      if (isConnected())
        fServeur.retirerFichier(this.client);
      fServeur = null;

    }
    catch (RemoteException ex)
    {
      logger.warning("Impossible de fermer proprement le client");
      ex.printStackTrace();
    }
  }


  /**
   * Prépare les informations necessaire au bon fonctionnement du client
   */
  public void run ()
  {
    prepareInfosFichierClient();
  }

  private void prepareInfoFichierClient()
  {
    new Thread (this).start();
    logger.info("On a fini de mettre les infos de partage");
  }

  /**
   * Prépare les informations necessaire au bon fonctionnement du client
   */
  private void prepareInfosFichierClient() {
  /* On récupere le fichier de partage */
    repertoirePartage = Propriete.getPropriete(FIC_PROPRIETE, "shareDir");

    if (null == repertoirePartage) {
      repertoirePartage = DEFAULT_SHARE_REP;
    }
    logger.info("Votre repertoire de partage est : " + repertoirePartage);

    /* Ajout des fichiers du repertoire de partage */
    lectureFichierDansRepertoire(new File(repertoirePartage));

    /* Ajout des fichiers téléchargés dans incoming */
    lectureFichierDansRepertoire(new File("incoming"));

    /* Ajout des fichiers en cours de téléchargement */
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

  public RemoteServeur getServeur () {
    return fServeur;
  }

  public Main getGui () {
    return this.appli;
  }

  public long getTempsTempo () {
    return tempsTempo;
  }

  public Fichier[] rechercherFichier(String regexp) {

    try {
      return fServeur.rechercherFichier(regexp);
    }
    catch (RemoteException ex) {
      System.err.println("Erreur lors de la requête de recherche : " + ex);
    }
    return null;
  }

  /**
   * Récupere tous les fichiers dans le repertoire de partage (et sous repértoires)
   *  et les ajoute à
   * la liste du client avec les bonnes informations.
   * @param rep le répertoire racine.
   */
  private void lectureFichierDansRepertoire(File rep) {
    if (rep.isDirectory()) {
      File[] list = rep.listFiles();
      for (int i = 0; i < list.length; i++) {
        if (list[i].isDirectory())
            /* Appel récursif sur les sous-répertoires */
            {
          lectureFichierDansRepertoire(list[i]);
        }
        else
        /* C'est un fichier */ {
          /* Création des informations sur le fichier */
          Fichier f = new Fichier(list[i].toString(), list[i].length(),
                                  Fichier.getTypeFichier(list[i]));
          long nbParties = ((list[i].length() % ClientImpl.MAX_OCTET_LU) == 0) ?
                               (list[i].length() / ClientImpl.MAX_OCTET_LU) :
                               ((list[i].length() / ClientImpl.MAX_OCTET_LU) + 1);
          AttributFichierClient atc = new AttributFichierClient(list[i].
              getAbsolutePath(), null, nbParties, true, nomClient, client.getIdClient());
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

  /**
   *
   * @param rmiName le nom du serveur, par ex : rmi://patachou/patServer
   * @return le nom de l'hote, par ex http://patachou
   */
  public static String getServerHostName (String rmiName)
  {
    String res = rmiName.replaceFirst("rmi://", "http://");
    int index;
    if (-1 < (index = res.indexOf(":", "http://".length())))
      res = res.substring(0, index);
    if (-1 < (index = res.indexOf("/", "http://".length())))
      res = res.substring(0, index);

    return res;
  }

  public void telechargeFichier (Fichier fic)
  {

    new ClientTelecharge (this, fic, 0).start();
  }

} //Classe Client
