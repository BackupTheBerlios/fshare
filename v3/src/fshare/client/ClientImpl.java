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
import java.io.FileInputStream;
import java.io.IOException;

public class ClientImpl extends java.rmi.server.UnicastRemoteObject implements RemoteClient
{

  /**
   * Le nombre d'octet lu et envoyer aux autres clients
   * Le fichier sera donc découpé en partie de 500 octets.
   */
  public static final int MAX_OCTET_LU = 500;

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

  /**
   * Renvoi la partie <b>partie</b> du fichier d'identifiant <b>id</b>.
   * @param id String
   * @param partie int
   * @return un tableau de byte[], null en cas d'erreur.
   */
  public byte [] telechargerFichier(String id, int partie)
  {
System.out.println ("Telechargement de : " + id + ", partie : " + partie);
    if (!listeFichier.containsKey(id)) /* On a pas le fichier */
      return null;
    AttributFichierClient atc = (AttributFichierClient) listeFichier.get(id);
    if (null == atc) /* On a pas les attributs du fichier du client */
      return null;

    try
    {
      FileInputStream fread = new FileInputStream(atc.getNomFichierAbsolu());
      byte [] b = new byte [MAX_OCTET_LU];
      /* placement de la tete de lecture au bon endroit */
      long deplacement = partie * MAX_OCTET_LU;
      if (deplacement != fread.skip(deplacement))
        return null; /* on a pas réussi a se déplacer au bon endroit du fichier */
      /* Lecture des octets dans le fichier */
      int nbOctetLu = fread.read(b, 0, MAX_OCTET_LU);

      /* vérification qu'on a bien pu lire le nombre d'octet. attention pour la derniere partie
              A FAIRE
       */
      return b;

    }
    catch (IOException e)
    {
      e.printStackTrace ();
    }
    return null;
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
