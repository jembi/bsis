package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.dto.ReportRow;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonorReportsRepository {
    
    @PersistenceContext
    private EntityManager em;
    
    public List<ReportRow> generateDonorByDonorPanelReport() {
        return em.createQuery(
                "SELECT NEW model.dto.GraphRow(d.donorPanel.name, 'Donor Panel', COUNT(d))" +
                "FROM Donor d " +
                "GROUP BY d.donorPanel.name ",
                ReportRow.class)
                .getResultList();
    }

}
