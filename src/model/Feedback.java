package model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long feedbackId;
    private String comment;

    @Basic(optional = false)
    @Column(name = "datetime", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;


    public Feedback(String comment) {
        this.comment = comment;
    }

    public Feedback() {
    }
}
