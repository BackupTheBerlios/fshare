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

public class MainServer implements ActionListener{
  RemoteServeurImpl controleur;
  JFrame frame;
  JMenuBar mb;
  JList traceTable = new JList();
  JScrollPane scrollT = new JScrollPane(traceTable);
  ArrayList traceVector = new ArrayList();

  public void addTrace(String trace){
  traceVector.add(trace);
  traceTable.setListData(traceVector.toArray());
}


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
  frame.getContentPane().setLayout(new BorderLayout());
  frame.getContentPane().add(scrollT);
  frame.setBounds(100,100,550,750);
  frame.setVisible(true);
}


  public void actionPerformed(ActionEvent e) {
    try {
      controleur.xmlExport();
/*
      File f = new File("../etat.xml");
      String[] contenu = f.list();
      for (int i=0;i<contenu.length; i++)
        addTrace(contenu[i]);
    */
      }
    catch (SAXException ex) {
      ex.printStackTrace();
    }
    catch (TransformerConfigurationException ex) {
      ex.printStackTrace();
    }
  }
}
