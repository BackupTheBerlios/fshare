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
 * <p>Description: Lance un thread pour t�l�charger un fichier. </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Giraud R�my, Mandrioli Damien
 * @version 1.0
 */

public class ClientTelecharge extends Thread
{
  /**
   * Le client qui t�l�charge.
   */
  private Client client = null;

  /**
   * Le fichier t�l�charg�.
   */
  private Fichier fichier = null;

  /**
   * la partie � laquelle on commence a t�l�charger.
   */
  private long partie;


  private static Logger logger =
      Logger.getLogger("fshare.client");



  /**
   *
   * @param client le client qui t�l�charger le fichier
   * @param fichier le fichier qu'on va t�l�charger
   * @param partie la partie a laquelle on va commencer le t�l�chargement.
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
    /* Ouverture du fichier de r�ception */
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
      /* Fichier pas trouv� */
      logger.warning("Impossible d'ouvrir le fichier en �criture");
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
        logger.warning ("Connexion avec le serveur impossible pour r�cuperer les clients qui ont le fichier");
        ex.printStackTrace ();
        /* On efface le fichier cr�er, on peut plus rien faire */
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
        /*s�lection d'un client au hasard */
        for (; ; )
        {
          numClient = (int) (Math.random () * listeClient.length);
          System.out.println ("client au hasar : " + numClient);
          if (numClient != listeClient.length)break;
        }
//pour les tests
//numClient = 0;
        /* �criture du fichier sauf la derniere partie */
        for (; partie < (fic.getNbPartiesFichier () - 1); ++partie)
        {
          byte[] b;
          logger.info ("On va t�l�charger la partie " + partie + " du fichier");
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
          /* �criture de la derniere partie */
          int tailleBuff = (int) (fic.getTailleFichier () -
                                  ((fic.getNbPartiesFichier () - 1) *
                                   ClientImpl.MAX_OCTET_LU));

          byte[] b;
          logger.info ("On va t�l�charger la derniere partie du fichier");
          b = listeClient[numClient].telechargerFichier (idFichier,
              fic.getNbPartiesFichier () - 1);
          if (null == b)
          {
            /* On a pas pu lire la partie, on dit que le client ne poss�de plus le fichier
               NORMALEMENT ON ENLEVE LA PARTIE QUI MANQUE POUR LE CLIENT � voir version future */
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
        /* La fonction de t�l�chargement � �chou�
           le client est deconnect� */
        System.out.println ("On a pas r�ussi a avoir la partie (d�connexion du client ayant le fichier).");
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

    //On d�place le fichier dans la r�pertoire de partage.
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

logger.info("T�l�chargement de : " + (newPath.getName()) +
            " R�USSI dans le r�pertoire : " + (newPath.getPath()));
*/
logger.info("T�l�chargement de : " + (new File(rep).getName()) +
            " R�USSI dans le r�pertoire : " + (new File (rep).getParentFile().getAbsolutePath()));

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
