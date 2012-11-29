package interceptor;

import java.util.Iterator;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.beanvalidation.BeanValidationEventListener;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class CustomIntegrator implements Integrator {

  static final Logger logger = LoggerFactory.getLogger(CustomIntegrator.class);

  @Override
  public void integrate(Configuration configuration, SessionFactoryImplementor implementor,
                        SessionFactoryServiceRegistry registry) {
      logger.info("Registering event listeners");
      System.out.println("here");
      // you can add duplication strategory for duplicate registrations

      final EventListenerRegistry eventRegistry = registry.getService(EventListenerRegistry.class);
      // prepend to register before or append to register after
      // this example will register a persist event listener

      eventRegistry.prependListeners(EventType.PERSIST, EntitySaveListener.class);
      eventRegistry.appendListeners(EventType.MERGE, EntitySaveListener.class);
      Iterator<PreInsertEventListener> iter = eventRegistry.getEventListenerGroup(EventType.PRE_INSERT).listeners().iterator();
      while (iter.hasNext()) {
        PreInsertEventListener el = iter.next();
        System.out.println(el.getClass());
        BeanValidationEventListener listener = (BeanValidationEventListener) el;
        System.out.println(listener);
//        iter.remove();
        
      }
  }

  @Override
  public void disintegrate(SessionFactoryImplementor arg0,
      SessionFactoryServiceRegistry arg1) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void integrate(MetadataImplementor arg0,
      SessionFactoryImplementor arg1, SessionFactoryServiceRegistry arg2) {
    // TODO Auto-generated method stub
    
  }
}