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

public class Client {

  public static final String DEFAULT_SHARE_REP = "partage";
  public static final String FIC_PROPRIETE     = "setting.props";


  private static Logger logger =
      Logger.getLogger("fshare.client");

  private Main          appli             = null;
  private String        nomClient         = null;
  private RemoteServeur fServeur          = null;
  private ClientImpl    client            = null;
  private String        repertoirePartage = null;

  public Client(String serverURL, String nomClient) throws java.rmi.RemoteException
  {
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

    /* On récupere le fichier de partage */
    repertoirePartage = Propriete.getPropriete(FIC_PROPRIETE, "shareDir");
    if (null == repertoirePartage)
      repertoirePartage = DEFAULT_SHARE_REP;
    logger.info("Votre repertoire de partage est : " + repertoirePartage);

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
    System.out.println("Creation client");
    Client fclient = new Client(args [0], args [1]);
    System.out.println("Mise en route client");

    fclient.startClient();
    System.out.println("sortie le main");

  }

  private void startClient ()
  {
    prepareInfoFichierClient ();
client.afficheListeFichierClient();
    appli = new Main (this);
  }

  /**
   * Prépare les informations necessaire au bon fonctionnement du client
   */
  private void prepareInfoFichierClient ()
  {
    /* Ajout des fichiers du repertoire de partage */
    lectureFichierDansRepertoire (new File (repertoirePartage));

    /* Ajout des fichiers en cours de téléchargement */
  }

  public void setRepertoirePartage(String rep){
    repertoirePartage = rep;
    prepareInfoFichierClient();
  }

  public Fichier[] rechercherFichier(String regexp){

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
  private void lectureFichierDansRepertoire (File rep)
  {
    if (rep.isDirectory ())
    {
      File[] list = rep.listFiles ();
      for (int i = 0; i < list.length; i++)
      {
        if (list[i].isDirectory()) /* Appel récursif sur les sous-répertoires */
          lectureFichierDansRepertoire (list[i]);
        else /* C'est un fichier */
        {
            /* Création des informations sur le fichier */
            Fichier f = new Fichier (list[i].toString(), list[i].length(), Fichier.getTypeFichier(list[i]));
            AttributFichierClient atc = new AttributFichierClient (list[i].getAbsolutePath(),
                                                                   null, 1, true, nomClient);
            /* Enregistrement du fichier chez le client */
            client.ajouterFichier(f.getIdFichier(), atc);

            /* Enregistrement au niveau du serveur */
            try
            {
              fServeur.ajouterFichier (f, client, atc);


            }
            catch (java.rmi.RemoteException e)
            {
              logger.severe("Impossible d'ajouter le fichier sur le serveur");
              e.printStackTrace();
            }
        }
      }
    }

  }

} //Classe Client
