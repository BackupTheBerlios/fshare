package fshare.remote;

/**
 * Title:        RemoteServeur.java
 * Description:  Interface distante offrant des m�thodes au niveau du serveur.
 * Copyright:    Copyright (c) 2004
 * Company:
 * @author Giraud R�my, Mandrioli Damien
 * @version 1.0
 */

 import fshare.commun.AttributFichierClient;
 import fshare.commun.Fichier;

public interface RemoteServeur extends java.rmi.Remote
{

  /**
   * Ajoute dans la liste du serveur, que le client <b>client</b> poss�de le fichier <b>fichier</b> avec les attributs <b>attr</b>.
   * @param fichier le fichier qui le client poss�de.
   * @param client le client qui poss�de le fichier.
   * @param attr les attributs sur le fichier pour ce client.
   */
  public void ajouterFichier(Fichier fichier, RemoteClient client, AttributFichierClient attr) throws java.rmi.RemoteException;

  /**
   * Retire dans le liste du serveur le fichier <b>fichier</b> pour le client <b>client</b>.
   * @param fichier le fichier que le client souhaite d�partager.
   * @param client le client qui souhaite d�partager le fichier.
   */
  public void retirerFichier(Fichier fichier, RemoteClient client) throws java.rmi.RemoteException;

  /**
   * Retire de la liste du serveur TOUS les fichiers partag�s par le client <b>client</b>.
   * @param client le client qui d�partage tous ces fichiers.
   */
  public void retirerFichier(RemoteClient client) throws java.rmi.RemoteException;

  /**
   * Recherche une liste de fichier repondant � l'expression r�guliere <b>regexp</b>.
   * @param regexp l'expression r�guli�re pour matcher les fichiers.
   * @return la liste des nom de fichiers correspondant a l'expression r�guli�re.
   */
  public String[] rechercherFichier(String regexp) throws java.rmi.RemoteException;

  /**
   * Donne la liste de tous les clients qui poss�de le fichier d'identifiant <b>idFichier</b>
   * @param idFichier l'identifiant du fichier consid�r�.
   * @return la liste des clients qui poss�de ce fichier.
   */
  public RemoteClient[] rechercherClient(String idFichier) throws java.rmi.RemoteException;

  /**
   * Met � jour les attributs <b>attr</b> du fichier aynt pour identifiant <b>idFichier</b> du client <b>client</b>.
   * @param idFichier l'identifiant du fichier.
   * @param client le client.
   * @param attr les nouveaux attributs.
   */
  public void majAttrFichier(String idFichier, RemoteClient client, AttributFichierClient attr) throws java.rmi.RemoteException;

} // Interface RemoteServeur