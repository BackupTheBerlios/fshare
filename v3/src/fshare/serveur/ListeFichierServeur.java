package fshare.serveur;

/**
 * Title:        ListeFichierServeur.java
 * Description:  Classe permettant de savoir les fichiers partager par tous les clients du cot� serveur.
 * Copyright:    Copyright (c) 2004
 * Company:
 * @author Giraud R�my, Mandrioli Damien
 * @version 1.0
 */

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

import fshare.remote.RemoteClient;
import fshare.commun.Fichier;
import fshare.serveur.InfoFichierServeur;
import fshare.commun.AttributFichierClient;

public class ListeFichierServeur
{

  /**
   * Represente le contenu de la liste.
   * La cl� est l'identifiant du fichier et les valeurs sont des objets de type
   * infoFichierServeur.
   */
  private Map contenuListe;



  /**
   * Constructeur qui initialise la liste.
   */
  public ListeFichierServeur()
  {
    /* Cr�ation de la hasmap synchroniz�e */
    contenuListe = Collections.synchronizedMap (new HashMap ());
  }

  /**
   * Ajoute le client <b>client</b> pour le fichier <b>fichier</B> avec ces attributs <b>attr</B>.
   * @param fichier le fichier qu'on traite, ou on souhaite ajouter le client et ses info sur son fichier.
   * @param client le client qui possede le fichier.
   * @param attr les informations sur le fichier pour le client <B>client</B>.
   */
  public void ajouterFichier(Fichier fichier, RemoteClient client, AttributFichierClient attr)
  {
//System.out.println ("contenuListe ajout fichier, null ?" + ((contenuListe == null) ? "oui" : "non"));
    /* On v�rifie si la cl� pour le fichier existe ou pas */
    if (contenuListe.containsKey (fichier.getIdFichier ()))
    {
      /* La cl� existe, il faut rajouter le client a la liste de valeur dej� cr��e */
      InfoFichierServeur info = (InfoFichierServeur) (contenuListe.get (fichier.getIdFichier()));
      info.ajouteClient(client, attr);
    }
    else
    {
      /* La cl� n'existe pas il faut cr�er une nouvelle instance infoFichier */
      InfoFichierServeur info = new InfoFichierServeur (fichier);
      info.ajouteClient(client, attr);
      /* puis l'ajouter au la liste */
      contenuListe.put (fichier.getIdFichier(), info);
    }
  }

  /**
   * Retire de la liste tous les fichiers qui correspondent au client <b>client</b>.
   * @param client le client dont on souhaite retirer tous ces fichiers.
   */
  public void retirerFichierClient(RemoteClient client)
  {
    /* On parcours tout le contenu de la liste et on verifie qu'il le client
       ne possede pas ce fichier, si c'est le cas on le retire */

    /* r�cuperation des valeurs pour la contenuListe */
    Object [] valeurListe = contenuListe.values ().toArray ();

    /* Rechercher du client et retrait du fichier. */
    for (int i = 0; i < valeurListe.length; ++i)
    {
      InfoFichierServeur info = (InfoFichierServeur) (valeurListe [i]);
      info.retirerFichierClient(client);
      /* On v�rifie qu'il y a bien au moins un client qui a encore le fichier, sinon on supprime l'entr�e */
      if (info.getNbrClientPossedeFichier() == 0)
        contenuListe.remove(info.getFichier().getIdFichier());

    }
  }

  /**
   * Recherche un nom de fichier qui match la regexp.
   * @param regexp l'expression r�guliere.
   * @return la liste des noms de fichier.
   */
  public String[] rechercherFichier(String regexp)
  {
    ArrayList resultat = new ArrayList ();

    /* r�cuperation des valeurs pour la contenuListe */
    Object [] valeurListe = contenuListe.values ().toArray ();

    /* Rechercher le nom du fichier match regexp. */
    for (int i = 0; i < valeurListe.length; ++i)
    {
      InfoFichierServeur info = (InfoFichierServeur) (valeurListe[i]);
      String nomFichier = info.getFichier ().getNomFichier ();
      if (nomFichier.matches (regexp))
      {//Le nom de fichier match bien l'expression
        resultat.add (nomFichier);
      }
    }

    /* Remplissage d'un tableau de string */
    String [] listeFichier = new String [resultat.size ()];
    for (int i = 0; i < resultat.size (); ++i)
    {
      listeFichier [i] = (String) resultat.get (i);
    }
    return listeFichier;
  }

  /**
   * @param idFichier l'identifiant du fichier dont on souhaite savoir quels client le poss�de.
   * @return la liste des clients qui poss�de le fichier idFichier.
   * renvoi null si le idFichier n'est pas contenu dans la liste.
   */
  public RemoteClient[] rechercherClient(String idFichier)
  {
    /* Si on a pas le fichier concern�, on renvoi null */
    if (!contenuListe.containsKey (idFichier))
      return null;

    /* sinon on renvoi le tableau */
    InfoFichierServeur info = (InfoFichierServeur) (contenuListe.get (idFichier));
    return info.getListeClient();
  }

  /**
   * Retire de la liste le fichier <B>fichier</B> poss�der par le client <B>client</B>.
   * Si le fichier n'est pas dans la liste ne fait rien.
   * @param fichier le fichier que l'on souhaite retirer pour le client client.
   * @param client le client qui veut departager son fichier.
   */
  public void retirerFichier(Fichier fichier, RemoteClient client)
  {

    if (!contenuListe.containsKey (fichier.getIdFichier()))
      return; /* Le fichier existe pas, rien a faire */

    /* On retire le client pour le fichier */
    InfoFichierServeur info = (InfoFichierServeur) contenuListe.get (fichier.getIdFichier());
    info.retirerFichierClient(client);

    /* On v�rifie qu'il y a bien au moins un client qui a encore le fichier, sinon on supprime l'entr�e */
    if (info.getNbrClientPossedeFichier() == 0)
      contenuListe.remove(fichier.getIdFichier());
  }


  public void majAttrFichier(String idFichier, RemoteClient client, AttributFichierClient attr)
  {
    if (!contenuListe.containsKey (idFichier))
      return; /* Le fichier existe pas, rien a faire */

    /* On met a jour les attributs du fichier */
    InfoFichierServeur info = (InfoFichierServeur) contenuListe.get (idFichier);
    info.modifAttributFichierClient (client, attr);

  }



} // Classe ListeFichierServeur