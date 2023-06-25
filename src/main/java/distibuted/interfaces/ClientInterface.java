package distibuted.interfaces;

import view.interfaces.*;

import java.rmi.Remote;

/**
 * This interface is a collection of interfaces that contains all the methods a server can call on client
 */
public interface ClientInterface
        extends Remote, ViewCollection, Binder{}
