/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.admin.GeneralConfig;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author srikanth
 */

@Repository
@Transactional
public class GeneralConfigRepository {

    @PersistenceContext
    private EntityManager em;

    public List<GeneralConfig> getAll() {
        TypedQuery<GeneralConfig> query = em.createQuery("select gc from GeneralConfig gc", GeneralConfig.class);
        return query.getResultList();

    }

    public void updateAll(List<GeneralConfig> generalConfigs) {
        for (GeneralConfig config : generalConfigs) {
            update(config);
        }
    }

    public void update(GeneralConfig generalConfig) {
        em.merge(generalConfig);
    }

    public void save(GeneralConfig generalConfig) {
        em.persist(generalConfig);
    }

    public GeneralConfig getGeneralConfigById(Integer id) {
        TypedQuery<GeneralConfig> query = em.createQuery("SELECT gc FROM GeneralConfig gc WHERE gc.id = :id ", GeneralConfig.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

}