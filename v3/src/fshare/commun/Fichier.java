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
import fshare.client.ClientImpl;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class Fichier implements Serializable
{

  /**
   * Conditionne tostring()
   */
    private String toString;


/**
 * Represente le nom (sans chemin) du fichier.
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
     * Le nombre de parties du fichier.
     */
    private long nbPartiesFichier;

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
     * L'indice des variables statiques doivent correspondent au type :
     * ex TYPE_S [LOGICIEL] = "logiciel"
     */
    public static final String [] TYPE_S    = {"AUTRE", "AUDIO", "VIDEO",
                                               "LOGICIEL", "IMAGE", "DOCUMENT"};



    private static final int MD5_CONST = 31;
/**
 * Construteur d'un fichier.
 * @param nomFichier le nom absolu du fichier.
 * @param tailleFichier la taille en octet du fichier.
 * @param typeFichier le type du fichier (audio, vidéo, logiciel, image, document texte, autre)
 */
  public Fichier(String nomFichier, long tailleFichier, int typeFichier)
  {
    this.nomFichier    = new File (nomFichier).getName();
    this.tailleFichier = tailleFichier;
    this.typeFichier   = typeFichier;
    this.idFichier     = genereIdFichier (nomFichier);
    this.setToString(getNomFichier());
    this.nbPartiesFichier = ((tailleFichier % ClientImpl.MAX_OCTET_LU) == 0) ?
                               (tailleFichier / ClientImpl.MAX_OCTET_LU) :
                               ((tailleFichier / ClientImpl.MAX_OCTET_LU) + 1);

  }

  public Fichier(String nomFichier){
      this.nomFichier    = new File (nomFichier).getName();
        this.tailleFichier = -1;
        this.typeFichier   = 0;
        this.idFichier     = genereIdFichier (nomFichier);
        this.setToString(getNomFichier());
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
      System.out.println(nomFichier + " " + f + " " + f.getAbsoluteFile());
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
  {/*
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
    }*/
   return TYPE_S [typeFichier];
  }

  /**
   * Renvoi le code correspondant au type de fichier du fichier <b>fichier</b>.
   * @param fichier le fichier dont on souhaite avoir le type.
   * @return le type de fichier.
   */
  public static int getTypeFichier (File fichier)
  {
    /* recupere le nom du fichier et son extension */
    String nomFichier = fichier.getName();
    String extension = nomFichier.substring(nomFichier.lastIndexOf(".")+1, nomFichier.length());
    if (null == extension) return AUTRE; /* pas d'extension */
    for (int i = 1; i < TYPE_S.length; ++i)
    {
      /* On récupere la ligne AUDIO ou VIDEO etc... */
      String type = fshare.util.Propriete.getPropriete(fshare.client.Client.
                                                       FIC_PROPRIETE, TYPE_S[i]);
      if (null == type) continue; /* la ligne n'est pas présente dans le fichier de propriété */
      StringTokenizer st = new StringTokenizer (type, " ");

      while (st.hasMoreTokens())
      {
        if (extension.equals(st.nextToken()))
          return i;
      }
    }
    return AUTRE;
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
   * @return le nom (juste le nom) du fichier.
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

  /**
   * @return le nombre de parties du fichier (en lequel il est découpé).
   */
  public long getNbPartiesFichier ()
  {
    return this.nbPartiesFichier;
  }

  /**
   * Change l'affichage du fichier
   *
   * @param s String
   */
  public void setToString(String s){
    toString = s;
  }

  public String toString(){
    return toString;
  }

public static void main (String [] args)
{
  File fic = new File ("test.txt");
  Fichier fichier = new Fichier (fic.getAbsolutePath (), fic.length (), 1);
  System.out.println ("Type de fichier : " + fichier.getTypeFichier ());
  System.out.println ("Id du fichier : " + fichier.getIdFichier ());
  System.out.println ("Nom du fichier : " + fichier.getNomFichier ());
  System.out.println ("Taille du fichier : " + fichier.getTailleFichier ());
/*
  fic = new File ("../fshare.html");
  fichier = new Fichier (fic.getAbsolutePath (), fic.length (), 1);
  System.out.println ("Type de fichier : " + fichier.getTypeFichier ());
  System.out.println ("Id du fichier : " + fichier.getIdFichier ());
  System.out.println ("Nom du fichier : " + fichier.getNomFichier ());
  System.out.println ("Taille du fichier : " + fichier.getTailleFichier ());
*/
  AttributFichierClient afc = new AttributFichierClient ("", null, 12, true, "tartanpion", "");

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

  System.out.println ("Type de fichier : " +  Fichier.getTypeFichier(Fichier.getTypeFichier(fic)));


}

} // Classe Fichier
