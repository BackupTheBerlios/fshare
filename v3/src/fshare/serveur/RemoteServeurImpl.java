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


public class RemoteServeurImpl extends UnicastRemoteObject implements fshare.remote.RemoteServeur
{

  private ListeFichierServeur listeFichierServeur = null;
  private static Logger logger =
        Logger.getLogger("fshare.serveur");

  public RemoteServeurImpl() throws RemoteException
  {
    listeFichierServeur = new ListeFichierServeur ();
  }

  /**
   * Ajoute dans la liste du serveur, que le client <b>client</b> possède le fichier <b>fichier</b> avec les attributs <b>attr</b>.
   * @param fichier le fichier qui le client possède.
   * @param client le client qui possède le fichier.
   * @param attr les attributs sur le fichier pour ce client.
   */
  public void ajouterFichier(Fichier fichier, RemoteClient client, AttributFichierClient attr)/* throws java.rmi.RemoteException */
  {
    logger.info ("Ajout du fichier qui a pour clé : " + fichier.getNomFichier () +
                 ", qui a pour type : " + fichier.getTypeFichier ());
    System.out.println ("listeFichierServeur initialisé, null ?" + ((listeFichierServeur == null) ? "oui" : "non"));
    System.out.println ("Attribut fichier initialisé, null ?" + ((attr == null) ? "oui" : "non"));
    System.out.println ("client initialisé, null ?" + ((client == null) ? "oui" : "non"));
    listeFichierServeur.ajouterFichier (fichier, client, attr);
  }

  /**
   * Retire dans le liste du serveur le fichier <b>fichier</b> pour le client <b>client</b>.
   * @param fichier le fichier que le client souhaite départager.
   * @param client le client qui souhaite départager le fichier.
   */
  public void retirerFichier(Fichier fichier, RemoteClient client) /* throws java.rmi.RemoteException */
  {
    listeFichierServeur.retirerFichier (fichier, client);
  }

  /**
   * Retire de la liste du serveur TOUS les fichiers partagés par le client <b>client</b>.
   * @param client le client qui départage tous ces fichiers.
   */
  public void retirerFichier(RemoteClient client) /* throws java.rmi.RemoteException */
  {
    listeFichierServeur.retirerFichierClient (client);
  }

  /**
   * Recherche une liste de fichier repondant à l'expression réguliere <b>regexp</b>.
   * @param regexp l'expression régulière pour matcher les fichiers.
   * @return la liste des nom de fichiers correspondant a l'expression régulière.
   */
  public String[] rechercherFichier(String regexp) /* throws java.rmi.RemoteException */
  {
    return listeFichierServeur.rechercherFichier (regexp);
  }

  /**
   * Donne la liste de tous les clients qui possède le fichier d'identifiant <b>idFichier</b>
   * @param idFichier l'identifiant du fichier considéré.
   * @return la liste des clients qui posséde ce fichier.
   */
  public RemoteClient[] rechercherClient(String idFichier) /* throws java.rmi.RemoteException */
  {
    return listeFichierServeur.rechercherClient (idFichier);
  }

  /**
   * Met à jour les attributs <b>attr</b> du fichier aynt pour identifiant <b>idFichier</b> du client <b>client</b>.
   * @param idFichier l'identifiant du fichier.
   * @param client le client.
   * @param attr les nouveaux attributs.
   */
  public void majAttrFichier(String idFichier, RemoteClient client, AttributFichierClient attr) /* throws java.rmi.RemoteException */
  {
    listeFichierServeur.majAttrFichier (idFichier, client, attr);
  }

  public static void main(String[] args)
  {
    try {
      if (args.length < 1)
      {
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
      System.out.println("   rmi://"+getHostName()+"/"+serverName);
    }
    catch (Exception e)
    {
      System.out.println("ServerImpl err: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static String getHostName() {
    try {
       return java.net.InetAddress.getLocalHost().getHostName();
    } catch (java.net.UnknownHostException e) {
       return "Unknown";
    }
  }

} // Classe RemoteServeurImpl