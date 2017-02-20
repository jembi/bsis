package org.jembi.bsis.repository;

import org.jembi.bsis.model.transfusion.Transfusion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TransfusionRepository extends AbstractRepository<Transfusion> {

}