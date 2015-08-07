package viewmodel;

import java.util.Date;
import java.util.Set;

import model.user.User;
import utils.JsonDateSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AuditRevisionViewModel {
    
    private int id;
    private Date revisionDate;
    private User user;
    private Set<String> entityNames;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonSerialize(using = JsonDateSerialiser.class)
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

    public Set<String> getEntityNames() {
        return entityNames;
    }

    public void setEntityNames(Set<String> modifiedEntityNames) {
        this.entityNames = modifiedEntityNames;
    }

}
