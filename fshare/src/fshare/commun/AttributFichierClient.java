
package fshare.commun;

/**
 * Title:        AttributFichierClient.java
 * Description:  Stoke des informations suplémentaires sur le client pour un fichier donné.
 * Copyright:    Copyright (c) 2004
 * Company:
 * @author Giraud Rémy, Mandrioli Damien
 * @version 1.1
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
  private double nbPartieTotale;

  /**
   * Construit les attributs en fonction des parties et s'il est complet ou pas.
   * @param parties les parties possédées par le client. Si complet est vrai ce paramètre sera ignoré.
   * @param complet si le fichier est complet ou non.
   */
  public AttributFichierClient(ArrayList parties, double nbParties, boolean complet)
  {
    if (complet)
      parties = null;
    else this.parties = parties;
    this.complet = complet;
    this.nbPartieTotale = nbParties;

    dateModif = new Date ();
  }


  /**
   * Ajoute une partie pour le fichier considérer.
   * @param partie le numéro de partie qu'on souhaite ajouter.
   */
  public void ajoutePartie(double partie)
  {
    if (!parties.contains (new Double (partie)))
    {
      parties.add (new Double (partie));
      dateModif = new Date ();
      if (nbPartieTotale == parties.size ())
        complet = true;
    }
  }

  /**
   * @return true si la partie <B>partie</B> est téléchargée.
   */
  public boolean possedePartie (double partie)
  {
    return parties.contains (new Double (partie));
  }

  /**
   * @return true si on possede toutes les parties du fichier.
   */
  public boolean fichierComplet ()
  {
    return complet;
  }

  /**
   * @renvoi la date de derniere modification du fichier.
   */
  public Date getDateDerModif ()
  {
    return dateModif;
  }

} // Classe AttributFichierClient
