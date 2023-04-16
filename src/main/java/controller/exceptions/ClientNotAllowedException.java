package controller.exceptions;

public class ClientNotAllowedException extends Exception{
    public ClientNotAllowedException(){
        super("Client not allowed");
    }
}
