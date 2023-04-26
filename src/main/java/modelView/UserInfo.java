package modelView;

import model.User;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private final User.Status status;
    private final String errorMessage;

    public UserInfo(User o){
        this.status = o.getStatus();
        this.errorMessage = o.getReportedError();
    } //TODO

    public User.Status getStatus() {
        return status;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
