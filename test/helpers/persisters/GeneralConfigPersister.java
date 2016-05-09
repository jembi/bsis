package helpers.persisters;

import static helpers.persisters.EntityPersisterFactory.aDataTypePersister;

import javax.persistence.EntityManager;

import model.admin.GeneralConfig;

public class GeneralConfigPersister extends AbstractEntityPersister<GeneralConfig> {

  @Override
  public GeneralConfig deepPersist(GeneralConfig generalConfig, EntityManager entityManager) {
    if (generalConfig.getDataType() != null) {
      aDataTypePersister().deepPersist(generalConfig.getDataType(), entityManager);
    }
    return persist(generalConfig, entityManager);
  }

}
