package helpers.builders;

import static helpers.builders.ComponentTypeBuilder.aComponentType;
import static helpers.builders.OrderFormBuilder.anOrderForm;
import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.OrderFormItemPersister;
import model.componenttype.ComponentType;
import model.order.OrderForm;
import model.order.OrderFormItem;

public class OrderFormItemBuilder extends AbstractEntityBuilder<OrderFormItem> {
  
  private Long id;
  private ComponentType componentType = aComponentType().build();
  private String bloodAbo = "A";
  private String bloodRh = "+";
  private int numberOfUnits = 5;
  private OrderForm orderForm = anOrderForm().build();

  public OrderFormItemBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public OrderFormItemBuilder withComponentType(ComponentType componentType) {
    this.componentType = componentType;
    return this;
  }
  
  public OrderFormItemBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }
  
  public OrderFormItemBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public OrderFormItemBuilder withNumberOfUnits(int numberOfUnits) {
    this.numberOfUnits = numberOfUnits;
    return this;
  }

  public OrderFormItemBuilder withOrderForm(OrderForm orderForm) {
    this.orderForm = orderForm;
    return this;
  }

  @Override
  public OrderFormItem build() {
    OrderFormItem item = new OrderFormItem();
    item.setId(id);
    item.setOrderForm(orderForm);
    item.setBloodAbo(bloodAbo);
    item.setBloodRh(bloodRh);
    item.setComponentType(componentType);
    item.setNumberOfUnits(numberOfUnits);
    return item;
  }
  
  @Override
  public AbstractEntityPersister<OrderFormItem> getPersister() {
    return new OrderFormItemPersister();
  }

  public static OrderFormItemBuilder anOrderItemForm() {
    return new OrderFormItemBuilder();
  }

}
