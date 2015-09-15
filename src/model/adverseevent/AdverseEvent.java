package model.adverseevent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;

import repository.constant.AdverseEventNamedQueryConstants;

@NamedQueries({
    @NamedQuery(name = AdverseEventNamedQueryConstants.NAME_COUNT_ADVERSE_EVENTS_FOR_DONOR,
            query = AdverseEventNamedQueryConstants.QUERY_COUNT_ADVERSE_EVENTS_FOR_DONOR)
})
@Entity
@Audited
public class AdverseEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, insertable = false)
    private Long id;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private AdverseEventType type;
    
    @Lob
    @Column
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdverseEventType getType() {
        return type;
    }

    public void setType(AdverseEventType type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        return other instanceof AdverseEvent &&
                ((AdverseEvent) other).id == id;
    }

}
