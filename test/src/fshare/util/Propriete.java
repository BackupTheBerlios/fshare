/**
 * Title : Propriete.java
 * Description : Classe qui permet de les fichiers propri�t�s de mani�re plus souple
 * @version	1.1, Janvier 2004
 * @author 	Giraud R�my, Mandrioli Damien
**/

package fshare.util;

import java.util.Properties;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class Propriete
{

  private Properties propert;
  private String  nomFic;

  /**
   * Nouveau fichier de propri�t� qui aura pour nom nomFicProp
   * @param nomFichProp le nom du fichier propr�t� qui sera cr�e.
   */
  public Propriete (String nomFichProp)
  {
    this.nomFic = nomFichProp;
    propert = new Properties ();
    try
    {
      BufferedInputStream bis = new BufferedInputStream(
                                    new FileInputStream(nomFichProp));
      propert.load(bis);
    }
    catch (java.io.FileNotFoundException Ex)
    {
System.out.println ("Pas de fichier propr�t� prensent pour l'instant");
    }
    catch (java.io.IOException Ex)
    {
      Ex.printStackTrace();
    }
  }

  /**
   * Fixe le propri�t� <B>key</B> � la valeur <B>value</B>.
   * @param key le nom de la propri�t� � changer, ajouter ou modifier.
   * @param value la valeur de la propri�t�.
   */
  public void setPropriete (String key, String value)
  {
    propert.setProperty(key, value);
  }

  /**
   * Renvoie la valeur de la propri�t� key.
   * @param key le nom de la propri�t� dont ou souhaite la valeur.
   * @return la valeur de la propri�t�,
   *         null si pas trouv�e.
   */
  public String getPropriete (String key)
  {
    return propert.getProperty(key);
  }

  /**
   * V�rifie si le propri�t� <B>key</B> existe dans le fichier de propri�t�.
   * @param key le nom de la propri�t� � tester.
   * @return true si le propri�t� existe, false autrement.
   */
  public boolean containKey (String key)
  {
    if (null == propert.getProperty(key)) return false;
    return true;
  }

  /**
   * Enregistre les propri�t�s ajout�s dans le fichier de propi�t�s.
   */
  public void saveProprietes ()
  {
    try
    {
      BufferedOutputStream bos = new BufferedOutputStream(
                                           new FileOutputStream (nomFic));
      propert.store(bos, "Propri�t�s " + nomFic);
      bos.close();
    }
    catch (java.io.FileNotFoundException Ex)
    {
      Ex.printStackTrace();
      System.err.println ("Impossible d'ecrire dans le fichier " + nomFic);
    }
    catch (java.io.IOException Ex)
    {
      Ex.printStackTrace ();
    }
  }

  /**
   * Ajoute la propri�t� si elle n'existe pas d�j�.
   * @param key le nom de la propri�t� � changer, ajouter ou modifier.
   * @param value la valeur de la propri�t�.
   */
  public void addProprieteIfNotExist (String key, String value)
  {
    if (!this.containKey(key))
      this.setPropriete(key, value);
  }

  /**
   * Supprime la propri�t� du fichier de propri�t�.
   * @param key la propri�t� a supprimer.
   */
  public void removePropriete (String key)
  {
    propert.remove(key);
  }

  /**
   * Supprime le fichier propri�t� du disque.
   * @return true si le fichier a bien �t� supprim�,
   *         false sinon.
   */
  public boolean deleteFilePropriete ()
  {
    File file = new File (nomFic);
    return file.delete();
  }

  /**
   * Supprime le fichier propri�t� du disque.
   * @param nomFic le nom di ficher � supprimer.
   * @return true si le fichier a bien �t� supprim�,
   *         false sinon.
   */
  public static boolean deleteFilePropriete (String nomFic)
  {
    File file = new File (nomFic);
    return file.delete();
  }

  /**
   * Ajoute/met � jour la propri�t� key � la veleur value dans le fichier de
   * propri�t� nomFic.
   * @param nomFic le nom du fichier propri�t�.
   * @param key la cl� de la propri�t�.
   * @param value la valeur pour la cl�.
   */
  public static void setPropriete (String nomFic, String key, String value)
  {
    Propriete p = new Propriete (nomFic);
    p.setPropriete(key, value);
    p.saveProprietes();
  }

    /**
   * Renvoie la valeur de la propri�t� key dans le fichier de propri�t� nomFic.
   * @param nomFic le nom complet du fichier de propri�t�.
   * @param key le nom de la propri�t� dont ou souhaite la valeur.
   * @return la valeur de la propri�t�,
   *         null si pas trouv�e.
   */
  public static String getPropriete (String nomFic, String key)
  {
    Propriete p = new Propriete (nomFic);
    return p.getPropriete(key);
  }



  public static void main (String [] args)
  {
    System.out.println ("D�but test");
    Propriete p = new Propriete ("propriete.props");
    if (! p.containKey("test"))
    {
      System.out.println ("Le cl� n'existe pas");
      p.setPropriete("test", "ok");
    }
    else System.out.println("La cl� existe deja");
    p.saveProprietes();
    p.setPropriete("test2", "nok");
    p.saveProprietes();
    System.out.println("Contient test2 ? " + p.containKey("test2"));
    System.out.println("valeur de la cl� test2 : " + p.getPropriete("test2"));
    p.removePropriete("test2");
    System.out.println("Contient test2 ? " + p.containKey("test2"));
    System.out.println("valeur de la cl� test2 : " + p.getPropriete("test2"));
    p.saveProprietes();

    System.out.println ("Supression reussi ? " + deleteFilePropriete ("propriete.props"));
    System.out.println ("Supression reussi ? " + p.deleteFilePropriete ());

    setPropriete ("propriete1.props", "test", "ok");
    setPropriete ("propriete1.props", "test2", "ok");
    System.out.println("Nom de la propri�t� associ� a test2 : " +
                        getPropriete ("propriete1.props", "test2"));
    System.out.println("Nom de la propri�t� associ� a test3 : " +
                        getPropriete ("propriete1.props", "test3"));
  }

} // Classe Propriete
