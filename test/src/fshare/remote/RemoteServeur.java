package fshare.remote;

/**
 * Title:        RemoteServeur.java
 * Description:  Interface distante offrant des méthodes au niveau du serveur.
 * Copyright:    Copyright (c) 2004
 * Company:
 * @author Giraud Rémy, Mandrioli Damien
 * @version 1.0
 */

 import fshare.commun.AttributFichierClient;
 import fshare.commun.Fichier;

public interface RemoteServeur extends java.rmi.Remote
{

  /**
   * Ajoute dans la liste du serveur, que le client <b>client</b> possède le fichier <b>fichier</b> avec les attributs <b>attr</b>.
   * @param fichier le fichier qui le client possède.
   * @param client le client qui possède le fichier.
   * @param attr les attributs sur le fichier pour ce client.
   */
  public void ajouterFichier(Fichier fichier, RemoteClient client, AttributFichierClient attr) throws java.rmi.RemoteException;

  /**
   * Retire dans le liste du serveur le fichier <b>fichier</b> pour le client <b>client</b>.
   * @param fichier le fichier que le client souhaite départager.
   * @param client le client qui souhaite départager le fichier.
   */
  public void retirerFichier(Fichier fichier, RemoteClient client) throws java.rmi.RemoteException;

  /**
   * Retire de la liste du serveur TOUS les fichiers partagés par le client <b>client</b>.
   * @param client le client qui départage tous ces fichiers.
   */
  public void retirerFichier(RemoteClient client) throws java.rmi.RemoteException;

  /**
   * Recherche une liste de fichier repondant à l'expression réguliere <b>regexp</b>.
   * @param regexp l'expression régulière pour matcher les fichiers.
   * @return la liste des nom de fichiers correspondant a l'expression régulière.
   */
  public String[] rechercherFichier(String regexp) throws java.rmi.RemoteException;

  /**
   * Donne la liste de tous les clients qui possède le fichier d'identifiant <b>idFichier</b>
   * @param idFichier l'identifiant du fichier considéré.
   * @return la liste des clients qui posséde ce fichier.
   */
  public RemoteClient[] rechercherClient(String idFichier) throws java.rmi.RemoteException;

  /**
   * Met à jour les attributs <b>attr</b> du fichier aynt pour identifiant <b>idFichier</b> du client <b>client</b>.
   * @param idFichier l'identifiant du fichier.
   * @param client le client.
   * @param attr les nouveaux attributs.
   */
  public void majAttrFichier(String idFichier, RemoteClient client, AttributFichierClient attr) throws java.rmi.RemoteException;

} // Interface RemoteServeur