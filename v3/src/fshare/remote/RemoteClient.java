package fshare.remote;

/**
 * Title:        RemoteClient.java
 * Description:  Service offert par le client � distance.
 * Copyright:    Copyright (c) 2004
 * Company:
 * @author Giraud R�my, Mandrioli Damien
 * @version 1.0
 */

public interface RemoteClient extends java.rmi.Remote
{
  public byte [] telechargerFichier(String id, int partie) throws java.rmi.RemoteException;
}
