package helpers.builders;

import org.hibernate.envers.RevisionType;

import model.audit.EntityModification;

public class EntityModificationBuilder {
    
    private int id;
    private RevisionType revisionType;
    private String entityName;

    public EntityModificationBuilder withId(int id) {
        this.id = id;
        return this;
    }
    
    public EntityModificationBuilder withRevisionType(RevisionType revisionType) {
        this.revisionType = revisionType;
        return this;
    }
    
    public EntityModificationBuilder withEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
    
    public EntityModification build() {
        EntityModification entityModification = new EntityModification();
        entityModification.setId(id);
        entityModification.setRevisionType(revisionType);
        entityModification.setEntityName(entityName);
        return entityModification;
    }
    
    public static EntityModificationBuilder anEntityModification() {
        return new EntityModificationBuilder();
    }

}
