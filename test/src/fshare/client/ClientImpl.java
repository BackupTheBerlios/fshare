package fshare.client;

/**
 * Title:        ClientImpl.java
 * Description:  Le client de l'application fshare.
 * Copyright:    Copyright (c) 2004
 * Company:
 * @author Giraud Rémy, Mandrioli Damien
 * @version 1.0
 */

import java.util.logging.Logger;
import java.util.logging.Level;

import fshare.remote.RemoteClient;
import fshare.remote.RemoteServeur;
import fshare.gui.Main;

public class ClientImpl extends java.rmi.server.UnicastRemoteObject implements RemoteClient
{

  private static Logger logger =
        Logger.getLogger("fshare.client");

  private Main appli             = null;
  private String nomClient       = null;
  private RemoteServeur fServeur = null;

  public ClientImpl(String serverURL, String nomClient) throws java.rmi.RemoteException
  {
    try {
      logger.info ("Connexion au serveur...");
      fServeur = (RemoteServeur) java.rmi.Naming.lookup(serverURL);
      logger.info ("Connexion REUSSIE");
    } catch (java.net.MalformedURLException e) {
      throw new java.rmi.RemoteException("The URL of the server if not well formed");
    } catch (java.rmi.NotBoundException e) {
      throw new java.rmi.RemoteException("The server was not found at the given URL");
    }
    this.nomClient = nomClient;
  }


  public static void main (String [] args) throws Exception
  {
    //Logger.getLogger("fshare.client").setLevel(Level.WARNING);
System.out.println ("Entrer dans le main");

    if (args.length < 2) {
      System.out.println("You must give the URL of the chat server and the name of the client as arguments");
      System.out.println(" ex : ClientImpl rmi://clio.unice.fr/Server bob");
      System.exit(1);
    }
System.out.println ("Configuration security manager");

    // Create and install a security manager
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new java.rmi.RMISecurityManager());
    }
    System.out.println ("Creation client");
    ClientImpl client = new ClientImpl(args[0], args[1]);
    System.out.println ("Mise en route client");
    client.startClient();
    System.out.println ("sortie le main");

  }

  void startClient ()
  {
    try{
    fServeur.ajouterFichier (new fshare.commun.Fichier ("fshare.html", 500, 1), this,
                             new fshare.commun.AttributFichierClient (new java.util.ArrayList (),
                                                        10, true));
    }
    catch (java.rmi.RemoteException Ex)
    {
      Ex.printStackTrace ();
    }
    appli = new Main ();
  }

  public void telechargerFichier(String id, int partie)
  {
System.out.println ("Telechargement de : " + id + ", partie : " + partie);
  }
}