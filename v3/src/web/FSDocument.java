package web;

public class FSDocument {
  private String [][] contenu;


  public FSDocument(int lines, int cols) {
    contenu = new String[lines][cols];
  }

  public String[][] getContenu(){
    return contenu;
  }
}
