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
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import fshare.commun.AttributFichierClient;
import fshare.commun.Fichier;

public class ClientImpl extends java.rmi.server.UnicastRemoteObject implements RemoteClient
{

  /**
   * Represente la liste de fichier que possède le client.
   * La clé est l'identifiant du fichier et les valeurs sont des objets de type
   * AttributFichierClient.
   */
  private Map listeFichier;

  public ClientImpl () throws java.rmi.RemoteException
  {
    /* Création de la hasmap synchronizée */
    listeFichier = Collections.synchronizedMap (new HashMap ());
  }

  /**
   * Ajoute le fichier à la liste du client.
   * @param idFichier l'identifiant du fichier.
   * @param atc les attributs du fichier pour le client.
   */
  public void ajouterFichier (String idFichier, AttributFichierClient atc)
  {
    listeFichier.put(idFichier, atc);
  }

  /**
   * Retire le fichier à la liste du client.
   * @param idFichier String
   */
  public void retirerFichier (String idFichier)
  {
    listeFichier.remove(idFichier);
  }

  public void telechargerFichier(String id, int partie)
  {
System.out.println ("Telechargement de : " + id + ", partie : " + partie);
  }

  /**
   * Fonction de débug.
   */
  public void afficheListeFichierClient ()
  {
    Object [] c = listeFichier.values().toArray();
    for (int i = 0; i < c.length; ++i)
    {
      AttributFichierClient afc = (AttributFichierClient) c [i];
      System.out.println ("Nom Fichier : " + afc.getNomFichierAbsolu() +
                          ", du client : " + afc.getNomClient() +
                          ", contient '" + afc.getNbPartieTotale() + "' au total" +
                          ", et il est " + ((afc.fichierComplet()) ? "complet" : "incomplet") +
                          ", et sa date est : " + afc.getDateDerModif());
    }
  }

  // Destiné à l'affichage dans l'interface graphique
  public String[] getFichierClient(){
    Object [] c = listeFichier.values().toArray();
    String[] renvoi = new String[c.length];
    for (int i = 0; i < c.length; ++i)
    {
      AttributFichierClient afc = (AttributFichierClient) c [i];
      renvoi[i] = afc.getNomFichierAbsolu() +
                          " " + afc.getNomClient() +
                          "  " + afc.getNbPartieTotale() + " parties   (" +
                          ((afc.fichierComplet()) ? "complet" : "incomplet") +
                          ")   " + afc.getDateDerModif();
    }
    return renvoi;
  }

} // Classe ClientImpl
