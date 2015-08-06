package viewmodel;

import java.util.Date;

import model.user.User;

public class AuditRevisionViewModel {
    
    private int id;
    private Date revisionDate;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    public UserViewModel getUser() {
        if (user == null) {
            return null;
        }
        return new UserViewModel(user);
    }

    public void setUser(User user) {
        this.user = user;
    }

}
