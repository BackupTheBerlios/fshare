package fshare.gui;
import javax.swing.*;
import java.awt.Dimension;
import java.util.Vector;
import java.awt.GridLayout;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class Main {
  JFrame frame = new JFrame("fshare");
  JPanel panelD = new JPanel();
  JPanel panelU = new JPanel();
  JPanel panelS = new JPanel();
  JPanel panelC = new JPanel();
  JPanel panelR = new JPanel();
  JPanel panelA = new JPanel();

  JTextField fieldR = new JTextField("regexp...",30);
  JButton boutR = new JButton("Télécharger");

  JTabbedPane onglets = new JTabbedPane();
  JTable download;
  JTable upload;
  JTable recherche;
  JScrollPane scrollD;
  JScrollPane scrollU = new JScrollPane(upload);
  JScrollPane scrollR;
  /*
  JMenuBar menu = new JMenuBar();
  JMenu menuAction = new JMenu("Action");
  JMenuItem itemConfig = new JMenuItem("Configuration");
  JMenuItem itemQuit = new JMenuItem("Quitter");
*/
  String[] columnsD = {
      "Nom du fichier", "ID", "Nombre de parties", "Parties restantes", "Date"};
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
    upload = new JTable(dataD, columnsD);
    scrollU = new JScrollPane(upload);
    panelU.setLayout(new BorderLayout());
    panelU.add(scrollU, BorderLayout.CENTER);
  }

  private void constructionR(){
    SpringLayout layout = new SpringLayout();
      BorderLayout bol = new BorderLayout();
    JLabel r = new JLabel("Rechercher : ");
    recherche = new JTable(dataD, columnsD);
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

  public Main() {
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
    onglets.addTab("Partage", new ImageIcon("images/share.gif"), panelS,
                   "Gestion des fichiers partagés");
    onglets.addTab("Configuration", new ImageIcon("images/config.gif"), panelC,
                   "Configuration de l'application");
    onglets.addTab("Aide", new ImageIcon("images/aide.gif"), panelA,
                     "Aide de l'application");

    frame.getContentPane().add(onglets);

    constructionD();
    constructionU();
    constructionR();
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
    Main appli = new Main();

  }

}
