package fshare.fsFrontEnd;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TestObjet
{
  private String nom;
  private int age;
  public TestObjet(String nom, int age)
  {
    this.nom = nom;
    this.age = age;
  }

  public String getNom ()
  {
    return nom;
  }

  public int getAge ()
  {
    return age;
  }

}
