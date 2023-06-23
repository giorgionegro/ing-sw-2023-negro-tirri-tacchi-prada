package distibuted.interfaces;

import view.interfaces.*;

import java.rmi.Remote;

public interface ClientInterface
        extends Remote, ViewCollection, Binder{}
