package web;

import java.util.ArrayList;

public class FSDocument {
  // On stock dedans des String[]
  private ArrayList contenu;


  public FSDocument() {
    contenu = new ArrayList();
  }

  public ArrayList getContenu(){
    return contenu;
  }

  public String[] getLine(int indice){
    return (String[])contenu.get(indice);
  }

  public void addLine(String[] line){
    contenu.add(line);
  }

  public void dump(){
    for (int i = 0; i < contenu.size(); i++) {
      String[] line = getLine(i);
      for (int j = 0; j < line.length; j++)
        System.out.print(line[i] + " ");
      System.out.println();
    }
  }
}
