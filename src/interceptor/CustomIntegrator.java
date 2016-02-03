package interceptor;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read about integrators here: http://docs.jboss.org/hibernate/orm/4.1/devguide/en-US/html_single/#integrators
 * http://stackoverflow.com/a/11672377/161628
 *
 * @author iamrohitbanga
 */
public class CustomIntegrator implements Integrator {

  static final Logger logger = LoggerFactory.getLogger(CustomIntegrator.class);

  @SuppressWarnings("unchecked")
  @Override
  public void integrate(Configuration configuration, SessionFactoryImplementor implementor,
                        SessionFactoryServiceRegistry registry) {
    logger.info("Registering event listeners");
    // you can add duplication strategory for duplicate registrations

    final EventListenerRegistry eventRegistry = registry.getService(EventListenerRegistry.class);
    // prepend to register before or append to register after
    // this example will register a persist event listener

    eventRegistry.prependListeners(EventType.PERSIST, EntitySaveListener.class);
    eventRegistry.appendListeners(EventType.MERGE, EntitySaveListener.class);
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