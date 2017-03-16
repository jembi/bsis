package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;

import java.util.UUID;

import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UuidRepositoryTests extends SecurityContextDependentTestSuite {

  @Autowired
  private UuidRepository uuidRepository;

  @Test
  public void testGenerateBinaryUuid_shouldReturnUuid() {
    UUID uuid = uuidRepository.generateBinaryUUID();

    assertThat(uuid, is(any(UUID.class)));
  }
}
