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
import java.io.IOException;
import java.security.MessageDigest;
import java.io.DataInputStream;

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
    private long tailleFichier;

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

    private static final int MD5_CONST = 500;
/**
 * Construteur d'un fichier.
 * @param nomFichier le nom absolu du fichier.
 * @param tailleFichier la taille en octet du fichier.
 * @param typeFichier le type du fichier (audio, vidéo, logiciel, image, document texte, autre)
 */
  public Fichier(String nomFichier, long tailleFichier, int typeFichier)
  {
    this.nomFichier    = nomFichier;
    this.tailleFichier = tailleFichier;
    this.typeFichier   = typeFichier;
    this.idFichier     = genereIdFichier (nomFichier);
  }

  public Fichier(String f){
      this.nomFichier    = f;
        this.tailleFichier = -1;
        this.typeFichier   = 0;
        this.idFichier     = genereIdFichier (nomFichier);
  }

  /**
   * A partir du nom (absolu) d'un fichier, génére une clé unique. Cette clé générée sera la même si on rappelle cette fonction avec le même paramêtre.
   * @param nomFichier le nom absolu du fichier.
   * @return un identifiant unique pour ce fichier,
   */
  private String genereIdFichier (String nomFichier)
  {
    File f = new File(nomFichier);
    tailleFichier = f.length();
    String renvoi = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      DataInputStream in = new DataInputStream(new java.io.FileInputStream(f));
      for (int i=0; i< ((f.length() < MD5_CONST) ? f.length() : MD5_CONST); i++)
        md.update(in.readByte());
      in.close();
      renvoi = new String(md.digest());
    }
    catch (java.security.NoSuchAlgorithmException ex) {
      System.out.println(ex);
    }
    catch (java.io.FileNotFoundException ex1) {
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
   * Renvoi le code correspondant au type de fichier du fichier <b>fichier</b>.
   * @param fichier le fichier dont on souhaite avoir le type.
   * @return le type de fichier.
   */
  public static int getTypeFichier (File fichier)
  {
    return 1;
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

  /**
   * @return le nom absolu du fichier.
   */
  public String getNomFichier ()
  {
    return nomFichier;
  }

  /**
   * @return la taille du fichier.
   */
  public long getTailleFichier ()
  {
    return tailleFichier;
  }



public static void main (String [] args)
{
  File fic = new File ("fshare.html");
  Fichier fichier = new Fichier (fic.getAbsolutePath (), fic.length (), 1);
  System.out.println ("Type de fichier : " + fichier.getTypeFichier ());
  System.out.println ("Id du fichier : " + fichier.getIdFichier ());
  System.out.println ("Nom du fichier : " + fichier.getNomFichier ());
  System.out.println ("Taille du fichier : " + fichier.getTailleFichier ());

  fic = new File ("../fshare.html");
  fichier = new Fichier (fic.getAbsolutePath (), fic.length (), 1);
  System.out.println ("Type de fichier : " + fichier.getTypeFichier ());
  System.out.println ("Id du fichier : " + fichier.getIdFichier ());
  System.out.println ("Nom du fichier : " + fichier.getNomFichier ());
  System.out.println ("Taille du fichier : " + fichier.getTailleFichier ());

  AttributFichierClient afc = new AttributFichierClient ("", null, 12, true, "tartanpion");

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
