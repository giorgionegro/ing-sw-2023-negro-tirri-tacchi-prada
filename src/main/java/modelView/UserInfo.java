package modelView;

import model.User;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private final User.Status status;

    public UserInfo(User o){
        this.status = o.getStatus();
    } //TODO

    public User.Status getStatus() {
        return status;
    }
}
