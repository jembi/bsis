package helpers.builders;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import model.component.Component;
import model.componentbatch.BloodTransportBox;
import model.componentbatch.ComponentBatch;
import model.componentbatch.ComponentBatchStatus;

public class ComponentBatchBuilder extends AbstractEntityBuilder<ComponentBatch> {

  private Long id;
  private Date deliveryDate;
  private Date collectionDate;
  private Set<Component> components = new HashSet<>();
  private int bloodTransportBoxCount;
  private Set<BloodTransportBox> bloodTransportBoxes = new HashSet<>();
  private ComponentBatchStatus status;
  private boolean deleted;

  public ComponentBatchBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ComponentBatchBuilder withBloodTransportBox(BloodTransportBox bloodTransportBox) {
    bloodTransportBoxes.add(bloodTransportBox);
    bloodTransportBoxCount++;
    return this;
  }
  
  public ComponentBatchBuilder withComponent(Component component) {
    components.add(component);
    return this;
  }
  
  public ComponentBatchBuilder withStatus(ComponentBatchStatus status) {
    this.status = status;
    return this;
  }
  
  public ComponentBatchBuilder withDeliveryDate(Date deliveryDate) {
    this.deliveryDate = deliveryDate;
    return this;
  }
  
  public ComponentBatchBuilder withCollectionDate(Date collectionDate) {
    this.collectionDate = collectionDate;
    return this;
  }

  public ComponentBatchBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  @Override
  public ComponentBatch build() {
    ComponentBatch componentBatch = new ComponentBatch();
    componentBatch.setId(id);
    componentBatch.setStatus(status);
    componentBatch.setDeliveryDate(deliveryDate);
    componentBatch.setCollectionDate(collectionDate);
    componentBatch.setComponents(components);
    componentBatch.setBloodTransportBoxCount(bloodTransportBoxCount);
    componentBatch.setBloodTransportBoxes(bloodTransportBoxes);
    componentBatch.setIsDeleted(deleted);
    return componentBatch;
  }

  public static ComponentBatchBuilder aComponentBatch() {
    return new ComponentBatchBuilder();
  }

}
