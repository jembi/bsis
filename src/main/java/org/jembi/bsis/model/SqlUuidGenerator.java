package org.jembi.bsis.model;

import java.io.Serializable;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.jembi.bsis.repository.UuidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SqlUuidGenerator implements IdentifierGenerator {

  @Autowired
  private UuidRepository uuidRepository;

  @Override
  public Serializable generate(SessionImplementor session, Object obj) {
    return uuidRepository.generateBinaryUUID();
  }
}