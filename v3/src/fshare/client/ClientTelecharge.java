package fshare.client;

import fshare.commun.Fichier;
import java.util.logging.Logger;
import fshare.remote.RemoteClient;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.io.IOException;
import fshare.commun.AttributFichierClient;

/**
 * <p>Title: ClientTelecharge.java </p>
 * <p>Description: Lance un thread pour télécharger un fichier. </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Giraud Rémy, Mandrioli Damien
 * @version 1.0
 */

public class ClientTelecharge extends Thread
{
  /**
   * Le client qui télécharge.
   */
  private Client client = null;

  /**
   * Le fichier téléchargé.
   */
  private Fichier fichier = null;

  /**
   * la partie à laquelle on commence a télécharger.
   */
  private long partie;


  private static Logger logger =
      Logger.getLogger("fshare.client");



  /**
   *
   * @param client le client qui télécharger le fichier
   * @param fichier le fichier qu'on va télécharger
   * @param partie la partie a laquelle on va commencer le téléchargement.
   */
  public ClientTelecharge(Client client, Fichier fichier, long partie)
  {
    this.client = client;
    this.fichier = fichier;
    this.partie = partie;
  }

  public void run ()
  {
    telechargeFichier(fichier);
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
      /*rep = client.getRepertoirePartage() +
          File.separator + Math.random () + "-" + fic.getNomFichier ();*/
      rep = client.getNickName() +
          File.separator + fic.getNomFichier ();
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
    while (true)
    {
      try
      {
      /* Rechercher des clients qui ont le fichier */
        listeClient = client.getServeur().rechercherClient (idFichier);
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
          Thread.sleep (client.getTempsTempo());
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
        for (; partie < (fic.getNbPartiesFichier () - 1); ++partie)
        {
          byte[] b;
          logger.info ("On va télécharger la partie " + partie + " du fichier");
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
          b = listeClient[numClient].telechargerFichier (idFichier, partie);
          fwrite.write (b, 0, b.length);
        }
        if (partie == (fic.getNbPartiesFichier () - 1))
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
            client.getServeur().retirerFichier (fic, listeClient[numClient]);
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
            client.getServeur().retirerFichier (listeClient[numClient]);
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

    //On déplace le fichier dans la répertoire de partage.
/*
    File newPath = new File (client.getRepertoirePartage() + File.separator + fic.getNomFichier());
    File oldPath = new File (rep);
    System.out.println("new path : " + client.getRepertoirePartage() + File.separator + fic.getNomFichier());
    System.out.println("old path " + rep);
    if (oldPath.renameTo(newPath))
      System.out.println("Renomage ok");
    else
      System.out.println("Renomage pas ok");
    if (oldPath.exists())
      System.out.println("old existe");
    else
      System.out.println("old n'existe pas");
    if (newPath.exists())
      System.out.println("new existe");
    else
      System.out.println("new n'existe pas");

    if (oldPath.exists()) oldPath.delete();

logger.info("Téléchargement de : " + (newPath.getName()) +
            " RéUSSI dans le répertoire : " + (newPath.getPath()));
*/
logger.info("Téléchargement de : " + (new File(rep).getName()) +
            " RéUSSI dans le répertoire : " + (new File (rep).getParentFile().getAbsolutePath()));

    /* On l'ajoute dans les fichiers partager et sur le serveur */
       //prepare les infos
    AttributFichierClient afc = new AttributFichierClient (rep, null, fic.getNbPartiesFichier(),
                                                           true, client.getNickName(), client.getClientImpl().getIdClient());
       //enregistrement sur le client
   client.getClientImpl().ajouterFichier (fic.getIdFichier (), afc);
      //enregistrement dans l'interface graphique
   client.getFichierPartage().add (fic);


     //Enregistrement au niveau du serveur
   try
   {
     if (client.getServeur() != null)
     {
       client.getServeur().ajouterFichier (fic, client.getClientImpl(), afc);
     }
   }
   catch (java.rmi.RemoteException e)
   {
     logger.severe ("Impossible d'ajouter le fichier sur le serveur");
     e.printStackTrace ();
   }

  }


}
