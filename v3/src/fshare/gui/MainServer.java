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

public class MainServer implements ActionListener{
  RemoteServeurImpl controleur;
  JFrame frame;
  JMenuBar mb;

  public MainServer(RemoteServeurImpl cont) {
  controleur = cont;
  frame = new JFrame("f-share - serveur");
  mb = new JMenuBar();
  JMenu m1 = new JMenu("Actions");
  JMenuItem xml = new JMenuItem("Exporter vers XML");
  xml.addActionListener(this);

  m1.add(xml);
  mb.add(m1);

  frame.setJMenuBar(mb);
  frame.setBounds(100,100,550,750);
  frame.setVisible(true);
}


  public void actionPerformed(ActionEvent e) {
    try {
      controleur.xmlExport();
    }
    catch (SAXException ex) {
      ex.printStackTrace();
    }
    catch (TransformerConfigurationException ex) {
      ex.printStackTrace();
    }
  }
}
