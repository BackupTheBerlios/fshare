package fshare.commun;

/**
 * Title:        AttributFichierClient.java
 * Description:  Stoke des informations suplémentaires sur le client pour un fichier donné.
 * Copyright:    Copyright (c) 2004
 * Company:
 * @author Giraud Rémy, Mandrioli Damien
 * @version 1.0
 */

import java.util.Date;
import java.util.ArrayList;
import java.io.Serializable;

public class AttributFichierClient implements Serializable
{

/**
 * Represente la liste des partie possédé par le client pour un certain fichier.
 */
  private ArrayList parties;


/**
 * Represente la date de derniere modification du fichier. Date de la derniere partie reçue.
 */
  private Date dateModif;

/**
 * Represente si le fichier est complet ou pas. S'il est complet parties est vide.
 */
  private boolean complet;

/**
 * Represente le nombre de parties totale du fichier.
 */
  private long nbPartieTotale;

  /**
   * Reprèsente la localisation exacte du fichier pour le client
  */
  private String nomFichierAbsolu;

  /**
   * Represente le nom du client.
   */
  private String nomClient;

  /**
   * Represente l'id du client.
   */
  private String idClient;


  /**
   * Construit les attributs en fonction des parties et s'il est complet ou pas.
   * @param parties les parties possédées par le client. Si complet est vrai ce paramètre sera ignoré.
   * @param complet si le fichier est complet ou non.
   * @param nbParties le nombre totale de parties du fichier.
   * @param nomFichierAbsolu le nom absolu du fichier.
   * @param nomClient le nom du client.
   */
  public AttributFichierClient(String nomFichierAbsolu, ArrayList parties, long nbParties, boolean complet, String nomClient, String idClient)
  {
    if (complet) /* pas besoin de stocker les parties. */
      parties = null;
    else
      this.parties = (null == parties) ? new ArrayList () : parties;

    this.complet = complet;
    this.nbPartieTotale = nbParties;
    this.nomFichierAbsolu = nomFichierAbsolu;
    this.nomClient = nomClient;
    this.idClient = idClient;
    dateModif = new Date ();
  }


  /**
   * Ajoute une partie pour le fichier considérer.
   * @param partie le numéro de partie qu'on souhaite ajouter.
   */
  public void ajoutePartie(long partie)
  {
    if (!parties.contains (new Long (partie)))
    {
      parties.add (new Long (partie));
      dateModif = new Date ();
      if (nbPartieTotale == parties.size ())
        complet = true;
    }
  }

  /**
   * @param partie la partie dont on souhaite savoir si on la possède.
   * @return true si la partie <B>partie</B> est téléchargée.
   */
  public boolean possedePartie (long partie)
  {
    return parties.contains (new Long (partie));
  }

  /**
   * @return true si on possede toutes les parties du fichier.
   */
  public boolean fichierComplet ()
  {
    return complet;
  }

  /**
   * Retourne le nom du client.
   * @return le nom du client.
   */
  public String getNomClient ()
  {
    return nomClient;
  }

  /**
   * Retourne l'id du client.
   * @return l'id du client.
   */
  public String getIdClient ()
  {
    return idClient;
  }

  /**
   * Retourne le nom absolu du fichier.
   * @return le nom absolu du fichier.
   */
  public String getNomFichierAbsolu ()
  {
    return nomFichierAbsolu;
  }

  /**
   * Retourne le nombre de parties. au total.
   * @return le nombre total de parties.
   */
  public long getNbPartieTotale ()
  {
    return nbPartieTotale;
  }

  /**
   * @return la date de derniere modification du fichier.
   */
  public Date getDateDerModif ()
  {
    return dateModif;
  }

} // Classe AttributFichierClient
