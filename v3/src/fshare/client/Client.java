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

  public boolean isConnected(){
    return (fServeur != null);
  }

  public void deconnect(){
    fServeur = null;
  }

  public void reConnectToServer(){
    stopClient();
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


  public void telechargeFichier (Fichier fic)
  {
    if (null == fic)return;
    String idFichier = fic.getIdFichier ();
    RemoteClient[] listeClient = null;
    FileOutputStream fwrite = null;
    int numClient = -1;
    String rep;
    /* Ouverture du fichier de réception */
    try
    {
      rep = repertoirePartage +
          File.separator + Math.random () + "-" + fic.getNomFichier ();
      //System.out.println(rep);
      fwrite = new FileOutputStream (rep);
    }
    catch (FileNotFoundException ex1)
    {
      /* Fichier pas trouvé */
      logger.warning("Impossible d'ouvrir le fichier en écriture");
      ex1.printStackTrace ();
      return;
    }
    long i = 0;
    while (true)
    {
      try
      {
      /* Rechercher des clients qui ont le fichier */
        listeClient = fServeur.rechercherClient (idFichier);
      }
      catch (RemoteException ex)
      {
        logger.warning ("Connexion avec le serveur impossible pour récuperer les clients qui ont le fichier");
        ex.printStackTrace ();
        /* On efface le fichier créer, on peut plus rien faire */
        try
        {
          fwrite.close ();
        }
        catch (IOException ex3)
        {
          ex3.printStackTrace();
        }
        new File (rep).delete();
        return;
      }
      if (null == listeClient || listeClient.length == 0)
      {
        /* Pas de client pour l'instant, on attend un peu */
        try
        {
          Thread.sleep (tempsTempo);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
        continue;
      }

      try
      {
        /*sélection d'un client au hasard */
        for (; ; )
        {
          numClient = (int) (Math.random () * listeClient.length);
          System.out.println ("client au hasar : " + numClient);
          if (numClient != listeClient.length)break;
        }
//pour les tests
//numClient = 0;
        /* écriture du fichier sauf la derniere partie */
        for (; i < (fic.getNbPartiesFichier () - 1); ++i)
        {
          byte[] b;
          logger.info ("On va télécharger la partie " + i + " du fichier");
//pour les tests
/*
try
{
  Thread.sleep (1000);
}
catch (InterruptedException ex4)
{
}
 */
          b = listeClient[numClient].telechargerFichier (idFichier, i);
          fwrite.write (b, 0, b.length);
        }
        if (i == (fic.getNbPartiesFichier () - 1))
        {
          /* écriture de la derniere partie */
          int tailleBuff = (int) (fic.getTailleFichier () -
                                  ((fic.getNbPartiesFichier () - 1) *
                                   ClientImpl.MAX_OCTET_LU));

          byte[] b;
          logger.info ("On va télécharger la derniere partie du fichier");
          b = listeClient[numClient].telechargerFichier (idFichier,
              fic.getNbPartiesFichier () - 1);
          if (null == b)
          {
            /* On a pas pu lire la partie, on dit que le client ne possède plus le fichier
               NORMALEMENT ON ENLEVE LA PARTIE QUI MANQUE POUR LE CLIENT à voir version future */
            System.out.println ("Probleme lecture fichier");
            fServeur.retirerFichier (fic, listeClient[numClient]);
          }
          logger.info ("octet lu : " + b.toString ());
          fwrite.write (b, 0, tailleBuff);
          fwrite.flush();
          //On sort de la boucle
          break;
        }
      }
      catch (RemoteException ex)
      {
        /* La fonction de téléchargement à échoué
           le client est deconnecté */
        System.out.println ("On a pas réussi a avoir la partie (déconnexion du client ayant le fichier).");
        try
        {
          if (numClient > -1)
            fServeur.retirerFichier (listeClient[numClient]);
        }
        catch (RemoteException ex2)
        {
          logger.warning("Connexion avec le serveur impossible");
          ex2.printStackTrace ();
        }
      }
      catch (IOException ex1)
      {
        ex1.printStackTrace ();
      }
      //Si erreur on continue
    }
    logger.info("Téléchargement de : " + (new File (rep).getName()) +
                " RéUSSI dans le répertoire : " + (new File (rep).getAbsolutePath()));
    /* On l'ajoute dans les fichiers partager et sur le serveur */
       //prepare les infos
    AttributFichierClient afc = new AttributFichierClient (rep, null, fic.getNbPartiesFichier(),
                                                           true, nomClient, client.getIdClient());
       //enregistrement sur le client
   client.ajouterFichier (fic.getIdFichier (), afc);
      //enregistrement dans l'interface graphique
   fichiersPartage.add (fic);

     //Enregistrement au niveau du serveur
   try
   {
     if (fServeur != null)
     {
       fServeur.ajouterFichier (fic, client, afc);
     }
   }
   catch (java.rmi.RemoteException e)
   {
     logger.severe ("Impossible d'ajouter le fichier sur le serveur");
     e.printStackTrace ();
   }

  }


} //Classe Client
