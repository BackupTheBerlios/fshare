
/* Version 1.1*/
package fshare.gui;

import javax.swing.*;

import java.awt.Dimension;
import java.util.Vector;



import java.awt.GridLayout;


import java.awt.*;



import java.awt.BorderLayout;
import java.awt.GridBagLayout;





import java.awt.GridBagConstraints;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import fshare.remote.RemoteClient;

public class Main {
  JFrame frame = new JFrame("fshare");
  //panels correspondant aux onglets
  private RemoteClient controleur;

  JPanel panelD = new JPanel();
  JPanel panelU = new JPanel();
  JPanel panelP = new JPanel();
  JPanel panelC = new JPanel();
  JPanel panelR = new JPanel();
  JPanel panelA = new JPanel();

  JTextField fieldR = new JTextField("regexp...",30);
  JTextField fieldP = new JTextField("/",30);
  String partagePath;

  JButton boutR = new JButton("Télécharger");

  JTabbedPane onglets = new JTabbedPane();
  JTable download;
  JTable upload;
  JTable recherche;
  JTable partage;
  JScrollPane scrollD;
  JScrollPane scrollU = new JScrollPane(upload);
  JScrollPane scrollR;
  JScrollPane scrollP;

  /*
  JMenuBar menu = new JMenuBar();
  JMenu menuAction = new JMenu("Action");
  JMenuItem itemConfig = new JMenuItem("Configuration");
  JMenuItem itemQuit = new JMenuItem("Quitter");
*/
  String[] columnsD = {
      "Nom du fichier", "ID", "Nombre de parties", "Parties restantes", "Date"};
  String[] columnsU = {
      "Nom du fichier", "ID", "Nombre de parties", "Parties envoyées", "Date"};
  String[] columnsR = {
      "Nom du fichier", "ID", "Nombre de parties", "Nombre de sources", "Date"};
  String[] columnsP = {
      "Nom du fichier", "ID", "Nombre de parties", "Parties reçues", "Date"};

  Object[][] dataD = {
      {
      "emule.exe", "EFZFEGG435R34TG436T", new Integer(5), new Integer(2),
      "20/10/03"}
  };

  private void constructionD() {
    download = new JTable(dataD, columnsD);
    scrollD = new JScrollPane(download);
    panelD.setLayout(new BorderLayout());
    panelD.add(scrollD, BorderLayout.CENTER);
  }

  private void constructionU() {
    upload = new JTable(dataD, columnsU);
    scrollU = new JScrollPane(upload);
    panelU.setLayout(new BorderLayout());
    panelU.add(scrollU, BorderLayout.CENTER);
  }

  private void constructionR(){
    SpringLayout layout = new SpringLayout();
    BorderLayout bol = new BorderLayout();
    JLabel r = new JLabel("Rechercher : ");
    recherche = new JTable(dataD, columnsR);
    scrollR = new JScrollPane(recherche);
    JPanel rechPane = new JPanel();
    rechPane.setLayout(layout);
      panelR.setLayout(bol);

    rechPane.setPreferredSize(new Dimension(10, 35));
  layout.putConstraint(SpringLayout.WEST, r, 5, SpringLayout.WEST, rechPane);
    layout.putConstraint(SpringLayout.NORTH, r, 5, SpringLayout.NORTH, rechPane);
    layout.putConstraint(SpringLayout.WEST, fieldR, 5, SpringLayout.EAST, r);
    layout.putConstraint(SpringLayout.NORTH, fieldR, 5, SpringLayout.NORTH, rechPane);
    layout.putConstraint(SpringLayout.WEST, boutR, 5, SpringLayout.EAST, fieldR);
    layout.putConstraint(SpringLayout.NORTH, boutR, 5, SpringLayout.NORTH, rechPane);


  rechPane.add(r);
  rechPane.add(fieldR);
  rechPane.add(boutR);
  panelR.add(rechPane, BorderLayout.NORTH);
panelR.add(scrollR, BorderLayout.CENTER);
}

private void constructionP(){
  SpringLayout layout = new SpringLayout();
  BorderLayout bol = new BorderLayout();
  JButton boutAjout = new JButton("Ajouter un fichier");
  JButton boutRetire = new JButton("Retirer un fichier");
  JButton boutBrowse = new JButton("...");
  JLabel partLabel = new JLabel("Rép. par défaut ");

  partage = new JTable(dataD, columnsP);
  scrollP = new JScrollPane(partage);
  JPanel partPane = new JPanel();
  partPane.setLayout(layout);
    panelP.setLayout(bol);

  partPane.setPreferredSize(new Dimension(10, 35));
  layout.putConstraint(SpringLayout.WEST, boutAjout, 5, SpringLayout.WEST, partPane);
  layout.putConstraint(SpringLayout.NORTH, boutAjout, 5, SpringLayout.NORTH, partPane);
  layout.putConstraint(SpringLayout.WEST, boutRetire, 5, SpringLayout.EAST, boutAjout);
  layout.putConstraint(SpringLayout.NORTH, boutRetire, 5, SpringLayout.NORTH, partPane);
  layout.putConstraint(SpringLayout.WEST, partLabel, 10, SpringLayout.EAST, boutRetire);
  layout.putConstraint(SpringLayout.NORTH, partLabel, 10, SpringLayout.NORTH, partPane);
  layout.putConstraint(SpringLayout.WEST, fieldP, 5, SpringLayout.EAST, partLabel);
  layout.putConstraint(SpringLayout.NORTH, fieldP, 7, SpringLayout.NORTH, partPane);
  layout.putConstraint(SpringLayout.WEST, boutBrowse, 5, SpringLayout.EAST, fieldP);
  layout.putConstraint(SpringLayout.NORTH, boutBrowse, 5, SpringLayout.NORTH, partPane);

  boutBrowse.addMouseListener(new MouseListener(){
      public void mouseClicked(MouseEvent e) {
                JFileChooser repChooser = new JFileChooser();
        repChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = repChooser.showOpenDialog(frame);
        if(returnVal == JFileChooser.APPROVE_OPTION){
          partagePath = repChooser.getSelectedFile().getPath();
          fieldP.setText(partagePath);

        }
      }
      public void mouseEntered(MouseEvent e) {
      }
      public void mouseExited(MouseEvent e) {
      }
      public void mousePressed(MouseEvent e) {
      }
      public void mouseReleased(MouseEvent e) {
      }

    });

  partPane.add(boutAjout);
  partPane.add(boutRetire);
  partPane.add(partLabel);
  partPane.add(fieldP);
  partPane.add(boutBrowse);
  panelP.add(partPane, BorderLayout.NORTH);
  panelP.add(scrollP, BorderLayout.CENTER);
}

public void majListePartage(){

}

  public Main(RemoteClient controleur) {
    this.controleur = controleur;
    assemble();
  }

  public void assemble() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    onglets.addTab("Download", new ImageIcon("images/down.gif"), panelD,
                   "Gestion des téléchargements");
    onglets.addTab("Upload", new ImageIcon("images/up.gif"), panelU,
                   "Gestion des envois");
    onglets.addTab("Recherche", new ImageIcon("images/rech.gif"), panelR,
                   "Recherche de fichiers");
    onglets.addTab("Partage", new ImageIcon("images/share.gif"), panelP,
                   "Gestion des fichiers partagés");
    onglets.addTab("Configuration", new ImageIcon("images/config.gif"), panelC,
                   "Configuration de l'application");
    onglets.addTab("Aide", new ImageIcon("images/aide.gif"), panelA,
                     "Aide de l'application");

    frame.getContentPane().add(onglets);

    constructionD();
    constructionU();
    constructionR();
    constructionP();
    /*
        menu.add(menuAction);
        menuAction.add(itemConfig);
        menuAction.add(itemQuit);
        frame.setJMenuBar(menu);
     */
    frame.setBounds(10, 10, 800, 600);

    frame.setVisible(true);
  }

  public static void main(String[] arg) {
    Main appli = new Main(null);

  }

}
