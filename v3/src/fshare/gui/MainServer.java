package fshare.gui;

import javax.swing.*;
import fshare.serveur.RemoteServeurImpl;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import org.xml.sax.*;
import javax.xml.transform.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.io.File;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainServer
    implements ActionListener {
  RemoteServeurImpl controleur;
  JFrame frame;
  JMenuBar mb;
  JMenuItem xml, fichiers;
  JList traceTable = new JList();
  JScrollPane scrollT = new JScrollPane(traceTable);
  ArrayList traceVector = new ArrayList();

  public void addTrace(String trace) {
    traceVector.add(trace);
    traceTable.setListData(traceVector.toArray());
  }

  public void clearTrace() {
    traceVector.clear();
  }

  public MainServer(RemoteServeurImpl cont) {

    controleur = cont;
    frame = new JFrame("f-share - serveur");
    mb = new JMenuBar();
    JMenu m1 = new JMenu("Actions");
    xml = new JMenuItem("Exporter vers XML");
    fichiers = new JMenuItem("Liste des fichiers sur le réseau");

    xml.addActionListener(this);
    fichiers.addActionListener(this);

    traceTable.setFont(new Font("SansSerif", Font.PLAIN, 11));
    m1.add(xml);
    m1.add(fichiers);
    mb.add(m1);

    frame.setJMenuBar(mb);
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(scrollT);
    frame.setBounds(100, 100, 550, 750);
    frame.setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    try {
      if (e.getSource() == xml) {
        controleur.xmlExport();

        clearTrace();
        addTrace("Contenu du fichier " + System.getProperty("user.dir")
                 + File.separator
                 + "etat.xml");

        try {
          BufferedReader in = new BufferedReader(new FileReader(System.
              getProperty("user.dir")
              + File.separator
              + "etat.xml"));
          String str;
          while ( (str = in.readLine()) != null) {
            addTrace(str);
          }
          in.close();
        }
        catch (IOException ex) {
          ex.printStackTrace();
        }

      }
      if (e.getSource() == fichiers) {
        controleur.printListeFichiers();

      }

    }
    catch (SAXException ex) {
      ex.printStackTrace();
    }
    catch (TransformerConfigurationException ex) {
      ex.printStackTrace();
    }
  }
}
