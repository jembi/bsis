package helpers.persisters;

import static helpers.persisters.EntityPersisterFactory.aComponentTypePersister;
import static helpers.persisters.EntityPersisterFactory.anOrderFormPersister;

import javax.persistence.EntityManager;

import model.order.OrderFormItem;

public class OrderFormItemPersister extends AbstractEntityPersister<OrderFormItem> {

  @Override
  public OrderFormItem deepPersist(OrderFormItem orderFormItem, EntityManager entityManager) {
    if (orderFormItem.getComponentType() != null) {
      aComponentTypePersister().deepPersist(orderFormItem.getComponentType(), entityManager);
    }
    if (orderFormItem.getOrderForm() != null) {
      anOrderFormPersister().deepPersist(orderFormItem.getOrderForm(), entityManager);
    }
    return persist(orderFormItem, entityManager);
  }

}
