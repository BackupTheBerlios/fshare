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
      this.nomClient = nomClient;
      prepareInfoFichierClient();
      client.afficheListeFichierClient();
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
System.out.println("Entré dans télécharger");
    telechargeFichier ();
  }

  /**
   * Arrete le client (enleve les fichiers du serveur).
   */
  public void stopClient ()
  {
    /* On enlève les fichiers du serveur */
    try
    {
      fServeur.retirerFichier(this.client);
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
  private void prepareInfoFichierClient() {
  /* On récupere le fichier de partage */
    repertoirePartage = Propriete.getPropriete(FIC_PROPRIETE, "shareDir");

    if (null == repertoirePartage) {
      repertoirePartage = DEFAULT_SHARE_REP;
    }
    logger.info("Votre repertoire de partage est : " + repertoirePartage);

    /* Ajout des fichiers du repertoire de partage */
    lectureFichierDansRepertoire(new File(repertoirePartage));

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
              getAbsolutePath(),
              null, nbParties, true, nomClient);
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

  private void telechargeFichier() {
    //On récupere test.txt
    try {
      logger.info("Entrer dans télécharge fichier");
      Fichier[] list = fServeur.rechercherFichier("txt");
      if ( (list == null) || (list.length == 0))return; //rien a faire
      //on prend le premier fichier
      Fichier fic = list[0];
      logger.info(list[0].toString());
      String idFichier = fic.getIdFichier();
      fshare.remote.RemoteClient[] listeClient = fServeur.rechercherClient(
          idFichier);
      if (null == listeClient || listeClient.length == 0)return; //pas de client
      /*if (listeClient [0] == (client))
           {
        logger.info("Meme client");
        return;
           }*/
      logger.info(listeClient[0].toString());
      logger.info(client.toString());

      /* Ouverture du fichier de réception */
      try {
        String rep = repertoirePartage +
            File.separator + Math.random() + "-" + fic.getNomFichier();
        System.out.println(rep);
        FileOutputStream fwrite = new FileOutputStream(rep);
        /* lecture du fichier sauf la derniere partie */
        for (long i = 0; i < (fic.getNbPartiesFichier() - 1); ++i) {
          byte[] b;
          logger.info("On va télécharger la premiere partie du fichier");
          b = listeClient[0].telechargerFichier(idFichier, i);
          logger.info("octet lu : " + b.toString());
          fwrite.write(b, 0, b.length);
        }
        /* écriture de la derniere partie */
        int tailleBuff = (int) (fic.getTailleFichier() -
                                ( (fic.getNbPartiesFichier() - 1) *
                                 ClientImpl.MAX_OCTET_LU));
        System.out.println("taille buffer pour der lecture : " + tailleBuff);
        byte[] b;
        logger.info("On va télécharger la premiere partie du fichier");
        b = listeClient[0].telechargerFichier(idFichier,
                                              fic.getNbPartiesFichier() - 1);
        logger.info("octet lu : " + b.toString());
        fwrite.write(b, 0, tailleBuff);

      }
      catch (FileNotFoundException ex1) {
        ex1.printStackTrace();
      }
      catch (IOException ex1) {
        ex1.printStackTrace();
      }

    }

    catch (RemoteException ex) {
      ex.getMessage();
      ex.printStackTrace();
    }
  }


} //Classe Client
