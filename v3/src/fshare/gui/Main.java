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
import java.io.File;
import fshare.commun.Fichier;
import java.util.ArrayList;
import fshare.client.Client;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.rmi.*;

public class Main {
  JFrame frame = new JFrame("fshare");
  //panels correspondant aux onglets
  private Client controleur;

  JPanel panelD = new JPanel();
  JPanel panelU = new JPanel();
  JPanel panelP = new JPanel();
  JPanel panelC = new JPanel();
  JPanel panelR = new JPanel();
  JPanel panelA = new JPanel();

  JTextField fieldR = new JTextField("regexp...", 30);
  JTextField fieldP;
  JTextField fieldC;
  JTextField fieldN;


  String partagePath;
  public ArrayList fichierPartage = new ArrayList();

  JButton boutR = new JButton("Télécharger");

  JTabbedPane onglets = new JTabbedPane();
  JList downloadTable;
  JList uploadTable;
  JList rechercheTable;
  JList partageTable;
  JScrollPane scrollD;
  JScrollPane scrollU = new JScrollPane(uploadTable);
  JScrollPane scrollR;
  JScrollPane scrollP;

  ArrayList downloadVector = new ArrayList();
  ArrayList uploadVector = new ArrayList();
  ArrayList partageVector = new ArrayList();

  public void addDownload(Fichier f){
    downloadVector.add(f);
    downloadTable.setListData(downloadVector.toArray());
  }


  public void addUpload(String f){
    uploadVector.add(f);
    uploadTable.setListData(uploadVector.toArray());
  }

  public void addPartage(String f){
    partageVector.add(f);
    partageTable.setListData(partageVector.toArray());
  }

  private void constructionD() {
    downloadTable = new JList();
    scrollD = new JScrollPane(downloadTable);
    panelD.setLayout(new BorderLayout());
    panelD.add(scrollD, BorderLayout.CENTER);
  }

  private void constructionU() {
    uploadTable = new JList();
    scrollU = new JScrollPane(uploadTable);
    panelU.setLayout(new BorderLayout());
    panelU.add(scrollU, BorderLayout.CENTER);
  }

  private void updateConnecState(){
    if (controleur.isConnected())
      frame.setTitle("f-share (connecté)");
    else frame.setTitle("f-share (déconnecté)");
  }

  private void constructionC() {
    SpringLayout layout = new SpringLayout();
    BorderLayout bol = new BorderLayout();
    JButton boutC1 = new JButton("Connexion");
    JButton boutC2 = new JButton("Déconnexion");
    fieldC = new JTextField(controleur.getServerName(), 30);
    JLabel r = new JLabel("URL du serveur ");
    fieldN = new JTextField(controleur.getNickName(), 30);
    JLabel nickLabel = new JLabel("Nickname");
    JPanel serverPane = new JPanel();
    //JPanel nickPane = new JPanel();

    serverPane.setLayout(layout);
    //nickPane.setLayout(bol);

    panelC.setLayout(bol);

    serverPane.setPreferredSize(new Dimension(10, 100));
    //nickPane.setPreferredSize(new Dimension(10, 35));

    layout.putConstraint(SpringLayout.WEST, r, 5, SpringLayout.WEST, serverPane);
    layout.putConstraint(SpringLayout.NORTH, r, 5, SpringLayout.NORTH,
                         serverPane);
    layout.putConstraint(SpringLayout.WEST, fieldC, 5, SpringLayout.EAST, r);
    layout.putConstraint(SpringLayout.NORTH, fieldC, 5, SpringLayout.NORTH,
                         serverPane);
    layout.putConstraint(SpringLayout.WEST, boutC1, 5, SpringLayout.EAST,
                         fieldC);
    layout.putConstraint(SpringLayout.NORTH, boutC1, 5, SpringLayout.NORTH,
                         serverPane);
    layout.putConstraint(SpringLayout.WEST, boutC2, 5, SpringLayout.EAST,
                         boutC1);
    layout.putConstraint(SpringLayout.NORTH, boutC2, 5, SpringLayout.NORTH,
                         serverPane);

    layout.putConstraint(SpringLayout.WEST, nickLabel, 5, SpringLayout.WEST,
                         serverPane);
    layout.putConstraint(SpringLayout.NORTH, nickLabel, 15, SpringLayout.SOUTH, r);
    layout.putConstraint(SpringLayout.WEST, fieldN, 38, SpringLayout.EAST,
                         nickLabel);
    layout.putConstraint(SpringLayout.NORTH, fieldN, 15, SpringLayout.SOUTH,
                         r);

    boutC1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          controleur.stopClient();
          controleur.connectToServer(fieldC.getText(), fieldN.getText());
          updateConnecState();
        }
        catch (RemoteException ex) {
          System.out.println("Erreur connexion serveur : " + ex);
        }
      }
    });
    boutC2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
          controleur.stopClient();
          updateConnecState();
        }
    });

    serverPane.add(r);
    serverPane.add(fieldC);
    serverPane.add(boutC1);
    serverPane.add(boutC2);
    serverPane.add(nickLabel);
    serverPane.add(fieldN);

    //nickPane.add(nickLabel);
    //nickPane.add(fieldN);
    panelC.add(serverPane, BorderLayout.NORTH);
    //panelC.add(nickPane, BorderLayout.CENTER);

  }

  private void constructionR() {
    SpringLayout layout = new SpringLayout();
    BorderLayout bol = new BorderLayout();
    JLabel r = new JLabel("Rechercher : ");
    rechercheTable = new JList();
    scrollR = new JScrollPane(rechercheTable);
    JPanel rechPane = new JPanel();
    rechPane.setLayout(layout);
    panelR.setLayout(bol);

    rechPane.setPreferredSize(new Dimension(10, 35));
    layout.putConstraint(SpringLayout.WEST, r, 5, SpringLayout.WEST, rechPane);
    layout.putConstraint(SpringLayout.NORTH, r, 5, SpringLayout.NORTH, rechPane);
    layout.putConstraint(SpringLayout.WEST, fieldR, 5, SpringLayout.EAST, r);
    layout.putConstraint(SpringLayout.NORTH, fieldR, 5, SpringLayout.NORTH,
                         rechPane);
    layout.putConstraint(SpringLayout.WEST, boutR, 5, SpringLayout.EAST, fieldR);
    layout.putConstraint(SpringLayout.NORTH, boutR, 5, SpringLayout.NORTH,
                         rechPane);

    fieldR.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Fichier[] files = null;
        if (controleur.isConnected())
          files = controleur.rechercherFichier(fieldR.getText());
        if (files != null){
          setToString(files, 0);
          rechercheTable.setListData(files);
        }
      }
    });
    boutR.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        controleur.telechargeFichier((Fichier)rechercheTable.getSelectedValue());
        addDownload((Fichier)rechercheTable.getSelectedValue());
        //rechercheTable.getSelectedValue()
      }
    });

    rechPane.add(r);
    rechPane.add(fieldR);
    rechPane.add(boutR);
    panelR.add(rechPane, BorderLayout.NORTH);
    panelR.add(scrollR, BorderLayout.CENTER);
  }

  private void constructionP() {
    fieldP = new JTextField(controleur.getRepertoirePartage(),30);
    SpringLayout layout = new SpringLayout();
    BorderLayout bol = new BorderLayout();
    JButton boutAjout = new JButton("Ajouter un fichier");
    JButton boutRetire = new JButton("Retirer un fichier");
    JButton boutBrowse = new JButton("...");
    JLabel partLabel = new JLabel("Rép. par défaut ");

    partageTable = new JList();
    scrollP = new JScrollPane(partageTable);
    JPanel partPane = new JPanel();
    partPane.setLayout(layout);
    panelP.setLayout(bol);

    partPane.setPreferredSize(new Dimension(10, 35));
    layout.putConstraint(SpringLayout.WEST, boutAjout, 5, SpringLayout.WEST,
                         partPane);
    layout.putConstraint(SpringLayout.NORTH, boutAjout, 5, SpringLayout.NORTH,
                         partPane);
    layout.putConstraint(SpringLayout.WEST, boutRetire, 5, SpringLayout.EAST,
                         boutAjout);
    layout.putConstraint(SpringLayout.NORTH, boutRetire, 5, SpringLayout.NORTH,
                         partPane);
    layout.putConstraint(SpringLayout.WEST, partLabel, 10, SpringLayout.EAST,
                         boutRetire);
    layout.putConstraint(SpringLayout.NORTH, partLabel, 10, SpringLayout.NORTH,
                         partPane);
    layout.putConstraint(SpringLayout.WEST, fieldP, 5, SpringLayout.EAST,
                         partLabel);
    layout.putConstraint(SpringLayout.NORTH, fieldP, 7, SpringLayout.NORTH,
                         partPane);
    layout.putConstraint(SpringLayout.WEST, boutBrowse, 5, SpringLayout.EAST,
                         fieldP);
    layout.putConstraint(SpringLayout.NORTH, boutBrowse, 5, SpringLayout.NORTH,
                         partPane);

    boutBrowse.addMouseListener(new MouseListener() {
      public void mouseClicked(MouseEvent e) {
        JFileChooser repChooser = new JFileChooser();
        repChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = repChooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          partagePath = repChooser.getSelectedFile().getPath();
          fieldP.setText(partagePath);
          controleur.stopClient();
          controleur.setRepertoirePartage(partagePath);
          controleur.reConnectToServer();
          /*Fichier[] files = controleur.getFichiers();
                     for (int i=0; i<files.length; i++)
            files[i].setToString(files[i].getNomFichier());
           */
   //       partageTable.setListData(controleur.getFichiers());

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

  private void setToString(Fichier[] f, int type) {
    for (int i = 0; i < f.length; i++) {
      switch (type) {
        case 0:
          f[i].setToString(f[i].getNomFichier() + " --- "
                           + f[i].getTailleFichier() + "octets ("
                           + f[i].getTypeFichier() + ")");
          break;
      }
    }
  }

  public Main(Client controleur) {
    this.controleur = controleur;
    assemble();
    updateConnecState();
//    partageTable.setListData(controleur.getFichiers());
    frame.addWindowListener(new java.awt.event.WindowAdapter() {


    public void windowClosing(java.awt.event.WindowEvent e)
    {
      stopClient ();
    }});

  }

  private void ajoutRecursif(File d) {

    if (d.isDirectory()) {
      File[] sub = d.listFiles();
      for (int i = 0; i < sub.length; i++) {
        if (sub[i].isDirectory()) {
          ajoutRecursif(sub[i]);
          continue;
        }
        try {
          fichierPartage.add(new Fichier(sub[i].toString()));
        }
        catch (OutOfMemoryError e) {
          System.out.println("ERROR " + sub[i].toString());
        }
      }
    }
    if (d.isFile()) {
      fichierPartage.add(new Fichier(d.toString()));
    }
  }

  public void assemble() {


    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    onglets.addTab("Connexion", new ImageIcon("images/config.gif"), panelC,
                   "Configuration de l'application");
    onglets.addTab("Download", new ImageIcon("images/down.gif"), panelD,
                   "Gestion des téléchargements");
    onglets.addTab("Upload", new ImageIcon("images/up.gif"), panelU,
                   "Gestion des envois");
    onglets.addTab("Recherche", new ImageIcon("images/rech.gif"), panelR,
                   "Recherche de fichiers");
    onglets.addTab("Partage", new ImageIcon("images/share.gif"), panelP,
                   "Gestion des fichiers partagés");

    onglets.addTab("Aide", new ImageIcon("images/aide.gif"), panelA,
                   "Aide de l'application");

    frame.getContentPane().add(onglets);

    constructionC();
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

    fieldP.setText(controleur.getRepertoirePartage());
    frame.setVisible(true);
  }

  public static void main(String[] arg) {
    Main appli = new Main(null);

  }

  private void stopClient ()
  {
System.out.println ("On stoppe le client");
    controleur.stopClient();
  }
}
