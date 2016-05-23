package helpers.builders;

import model.component.Component;
import model.order.OrderForm;
import model.order.OrderFormComponent;

public class OrderFormComponentBuilder extends AbstractEntityBuilder<OrderFormComponent> {
  
  private Long id;
  private Component component;
  private OrderForm orderForm;
  private boolean isDeleted = false;

  public OrderFormComponentBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public OrderFormComponentBuilder withComponent(Component component) {
    this.component = component;
    return this;
  }

  public OrderFormComponentBuilder withOrderForm(OrderForm orderForm) {
    this.orderForm = orderForm;
    return this;
  }
  
  public OrderFormComponentBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  @Override
  public OrderFormComponent build() {
    OrderFormComponent entity = new OrderFormComponent();
    entity.setId(id);
    entity.setOrderForm(orderForm);
    entity.setComponent(component);
    entity.setIsDeleted(isDeleted);
    return entity;
  }

  public static OrderFormComponentBuilder anOrderFormComponent() {
    return new OrderFormComponentBuilder();
  }

}
