package fshare.serveur;

/**
 * Title:        InfoFichierServeur.java
 * Description:  M�morise pour un fichier donn� des informations telles que qui � le fichier avec quelles parties etc..
 * Copyright:    Copyright (c) 2004
 * Company:
 * @author Giraud R�my, Mandrioli Damien
 * @version 1.0
 */

import java.util.Date;
import java.util.ArrayList;

import fshare.commun.AttributFichierClient;
import fshare.commun.Fichier;
import fshare.remote.RemoteClient;

public class InfoFichierServeur
{

/**
 * Represente le fichier qu'on traite.
 */
    private Fichier fichier;

/**
 * Represente la liste des clients qui poss�de ce fichier
 */
    private ArrayList listeClient;

/**
 * Represente la date la plus ancienne du fichier.
 */
    private Date date;

/**
 * Represente la listes des attributs pour chaque client, pour le fichier consid�r�.
 * Il y a une forte correspondance entre listeClient et listeAttributFichierClient.
 * Pour le meme indice i, le client listeClient (i) possede listeAttributFichierClient (i).
 */
    private ArrayList listeAttributFichierClient;


  /**
   * Construit un objet pour contenir des information sur le fichier fichier.
   * @param fichier le fichier � considerer.
   */
  public InfoFichierServeur(Fichier fichier)
  {
    this.fichier = fichier;
    listeClient = new ArrayList ();
    listeAttributFichierClient = new ArrayList ();
    date = null;
  }

  /**
   * Ajoute les informations <B>attr</B> du client <B>client</B>.
   * @param client le client qui possede le fichier.
   * @param attr les attributs du fichier pour le client <B>client</B>
   */
  public void ajouteClient (RemoteClient client, AttributFichierClient attr)
  {
    /* On ajoute le client s'il n'existe pas */
    if (!listeClient.contains (client))
    {
      listeClient.add (client);
    }
    /* On r�cupere l'indice du client dans l'arrayListe */
    int indice = listeClient.indexOf (client);

    /* Au meme indice de la liste de client on ajoute ces attribut de fichier */
    listeAttributFichierClient.add (indice, attr);

    /* Si la date est plus ancienne on met � jour */
    if ((null != date) && (attr.getDateDerModif ().before (date)))
      date = attr.getDateDerModif ();
  }


  /**
   * Retire le fichier du partage pour le client <B>client</B>.<br>
   * Si le client ne possede pas le fichier, rien ne se passe.
   * @param client le client qui retire le fichier.
   */
  public void retirerFichierClient(RemoteClient client)
  {
    /* Supprime les 2 entr�es dans les 2 arrayListe */
    if (listeClient.contains (client))
    {
      int indice = listeClient.indexOf (client);
      listeClient.remove (indice);
      listeAttributFichierClient.remove (indice);
    }
  }

  /**
   * @return la liste de tous les clients poss�dant le fichier.
   */
  public RemoteClient [] getListeClient ()
  {
    int taille = listeClient.size ();
    RemoteClient [] result = new RemoteClient [taille];
    for (int i = 0; i < taille; ++i)
      result [i] = (RemoteClient) listeClient.get (i);
    return result;
  }

  /**
   * @return la liste des attributs de fichier pour le fichier consid�rer.
   */
  public AttributFichierClient [] getListeAttributFichierClient ()
  {
    int taille = listeAttributFichierClient.size ();
    AttributFichierClient [] result = new AttributFichierClient [taille];
    for (int i = 0; i < taille; ++i)
      result [i] = (AttributFichierClient) listeAttributFichierClient.get (i);
    return result;
  }

  /**
   *
   * @return les attributs du fichier pour le client <b>client</b>.<br> renvoi
   *   null si le client <b>client</b> ne possede pas le fichier.
   * @param client RemoteClient
   */
  public AttributFichierClient getAttributFichierClient (RemoteClient client)
  {
    if (listeClient.contains (client))
      return ((AttributFichierClient) listeAttributFichierClient.get (listeClient.indexOf (client)));
    return null;
  }

  /**
   * @return le fichier consid�r�.
   */
  public Fichier getFichier ()
  {
    return this.fichier;
  }

  /**
   * modifie les attributs du fichier du client <b>client</b> avec les attributs <b>attr</b>.
   * @param client le client dont on souhaite modifi� les attributs.
   * @param attr les nouveaux attributs.
   */
  public void modifAttributFichierClient(RemoteClient client, AttributFichierClient attr)
  {
    /* Si le client n'existe pas rien a faire */
    if (!listeClient.contains (client))
      return;

    /* On r�cupere l'indice du client dans l'arrayListe */
    int indice = listeClient.indexOf (client);

    /* Au meme indice de la liste de client on ajoute ces attribut de fichier */
    listeAttributFichierClient.add (indice, attr);

    /* Si la date est plus ancienne on met � jour */
    if (attr.getDateDerModif ().before (date))
      date = attr.getDateDerModif ();

  }

  /**
   * @return le nombre de client qui poss�de le fichier.
   */
  public int getNbrClientPossedeFichier ()
  {
    return this.listeClient.size();
  }


} // Classe InfoFichierServeur
