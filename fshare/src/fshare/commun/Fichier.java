package fshare.commun;

/**
 * Title:        fichier.java
 * Description:  Classe qui modélise les fichiers.
 * Copyright:    Copyright (c) 2004
 * Company:
 * @author Giraud Rémy, Mandrioli Damien
 * @version 1.0
 */


import java.lang.String;
import java.util.*;

import java.io.Serializable;
import java.io.File;
import java.security.*;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Fichier implements Serializable
{

/**
 * Represente le nom absolu du fichier.
 */
  private String nomFichier;

/**
 * Represente une clé unique pour ce fichier.
 */
    private String idFichier;

/**
 * Represente la taille du fichier.
 */
    private double tailleFichier;

/**
 * Represente le type de fichier (ex : video musique etc...).
 */
    private int typeFichier;

    public static final int AUTRE    = 0;
    public static final int AUDIO    = 1;
    public static final int VIDEO    = 2;
    public static final int LOGICIEL = 3;
    public static final int IMAGE    = 4;
    public static final int DOCUMENT = 5;

/**
 * Construteur d'un fichier.
 * @param nomFichier le nom absolu du fichier.
 * @param tailleFichier la taille en octet du fichier.
 * @param typeFichier le type du fichier (audio, vidéo, logiciel, image, document texte, autre)
 */
  public Fichier(String nomFichier, double tailleFichier, int typeFichier)
  {
    this.nomFichier    = nomFichier;
    this.tailleFichier = tailleFichier;
    this.typeFichier   = typeFichier;
    this.idFichier     = genereIdFichier (nomFichier);
  }

  /**
   * A partir du nom (absolu) d'un fichier, génére une clé unique. Cette clé générée sera la même si on rappelle cette fonction avec le même paramêtre.
   * @param nomFichier le nom absolu du fichier.
   * @return un identifiant unique pour ce fichier,
   */
  private String genereIdFichier (String nomFichier){
    File f = new File(nomFichier);
    String renvoi = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      DataInputStream in = new DataInputStream(new FileInputStream(f));
      for (int i=0; i< f.length(); i++)
        md.update(in.readByte());
      in.close();
      renvoi = new String(md.digest());
    }
    catch (NoSuchAlgorithmException ex) {
      System.out.println(ex);
    }
    catch (FileNotFoundException ex1) {
      System.out.println(ex1);
    }
    catch (IOException ex2) {
      System.out.println(ex2);
    }
    return renvoi;

}

  /**
   * A partir d'un type de fichier nous renvoie la string correspondante.
   * @param typeFichier le type de fichier.
   * @return la sigification du typeFichier sous forme de string.
   */
  public static String getTypeFichier (int typeFichier)
  {
    switch (typeFichier)
    {
      case AUDIO :
        return "audio";
      case VIDEO :
        return "vidéo";
      case DOCUMENT :
        return "document texte";
      case IMAGE :
        return "image";
      case LOGICIEL :
        return "logiciel";
      default :
        return "autre";
    }
  }

  /**
   * @return Renvoie un string representant le type de fichier.
   */
  public String getTypeFichier ()
  {
    return getTypeFichier (typeFichier);
  }

  /**
   * @return l'identifiant du fichier.
   */
  public String getIdFichier ()
  {
    return idFichier;
  }

  public String getFormatId(){
    StringBuffer buf = new StringBuffer();
    for (int i=0; i<idFichier.length();i++){
      buf.append(Integer.toString((int)idFichier.charAt(i)));
    }
    return buf.toString();
  }
  /**
   * Renvoi le nom absolu du fichier.
   */
  public String getNomFichier ()
  {
    return nomFichier;
  }

  /**
   * Renvoi la taille du fichier.
   */
  public double getTailleFichier ()
  {
    return tailleFichier;
  }



public static void main (String [] args)
{
  File fic = new File ("fshare2.html");
  Fichier fichier = new Fichier (fic.getAbsolutePath (), fic.length (), 1);
  System.out.println ("Type de fichier : " + fichier.getTypeFichier ());
  System.out.println ("Id du fichier : " + fichier.getIdFichier ());
  System.out.println ("Format ID : " + fichier.getFormatId());
  System.out.println ("Nom du fichier : " + fichier.getNomFichier ());
  System.out.println ("Taille du fichier : " + fichier.getTailleFichier ());

  fic = new File ("fshare2.html");
  fichier = new Fichier (fic.getAbsolutePath (), fic.length (), 1);
  System.out.println ("Type de fichier : " + fichier.getTypeFichier ());
  System.out.println ("Id du fichier : " + fichier.getIdFichier ());
  System.out.println ("Format ID : " + fichier.getFormatId());

  System.out.println ("Nom du fichier : " + fichier.getNomFichier ());
  System.out.println ("Taille du fichier : " + fichier.getTailleFichier ());

  AttributFichierClient afc = new AttributFichierClient (null, 12, true);

  java.util.Map map = java.util.Collections.synchronizedMap (new HashMap ());
  map.put ("toto", "valeur toto");
  map.put ("tata", "valeur tata");
  map.put ("tutu", "valeur tutu");
  map.put ("titi", "valeur titi");

  Object [] liste = map.values ().toArray ();
  for (int i = 0; i < liste.length; ++i)
  {
    String value = (String) liste [i];
    System.out.println ("valeur : " + value);
  }


}

} // Classe Fichier
