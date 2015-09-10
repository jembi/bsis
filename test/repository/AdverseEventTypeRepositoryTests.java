package repository;

import static helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static helpers.builders.AdverseEventTypeViewModelBuilder.anAdverseEventTypeViewModel;
import static helpers.matchers.AdverseEventTypeViewModelMatcher.hasSameStateAsAdverseEventTypeViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.adverseevent.AdverseEventType;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.ContextDependentTestSuite;
import viewmodel.AdverseEventTypeViewModel;

public class AdverseEventTypeRepositoryTests extends ContextDependentTestSuite {
    
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AdverseEventTypeRepository adverseEventTypeRepository;
    
    @Test
    public void testFindAdverseEventTypeViewModels_shouldReturnViewModelsForAdverseEventTypes() {
        
        String firstExpectedName = "b.name";
        String secondExpectedName = "c.name";
        String firstExpectedDescription = "first.description";
        String secondExpectedDescription = "second.description";
        
        AdverseEventType secondAdverseEventType = anAdverseEventType()
                .withName(secondExpectedName)
                .withDescription(secondExpectedDescription)
                .buildAndPersist(entityManager);
        AdverseEventType firstAdverseEventType = anAdverseEventType()
                .withName(firstExpectedName)
                .withDescription(firstExpectedDescription)
                .buildAndPersist(entityManager);
        
        AdverseEventTypeViewModel firstExpectedViewModel = anAdverseEventTypeViewModel()
                .withId(firstAdverseEventType.getId())
                .withName(firstExpectedName)
                .withDescription(firstExpectedDescription)
                .build();
        AdverseEventTypeViewModel secondExpectedViewModel = anAdverseEventTypeViewModel()
                .withId(secondAdverseEventType.getId())
                .withName(secondExpectedName)
                .withDescription(secondExpectedDescription)
                .build();
        
        List<AdverseEventTypeViewModel> returnedViewModels = adverseEventTypeRepository.findAdverseEventTypeViewModels();
        
        assertThat(returnedViewModels.size(), is(2));
        assertThat(returnedViewModels.get(0), hasSameStateAsAdverseEventTypeViewModel(firstExpectedViewModel));
        assertThat(returnedViewModels.get(1), hasSameStateAsAdverseEventTypeViewModel(secondExpectedViewModel));
    }
    
    @Test
    public void testFindById_shouldReturnCorrectAdverseEventType() {
        anAdverseEventType().withName("first").buildAndPersist(entityManager);
        AdverseEventType expectedAdverseEventType = anAdverseEventType()
                .withName("second")
                .buildAndPersist(entityManager);
        anAdverseEventType().withName("third").buildAndPersist(entityManager);
        
        AdverseEventType returnedAdverseEventType = adverseEventTypeRepository.findById(expectedAdverseEventType.getId());
        
        assertThat(returnedAdverseEventType, is(expectedAdverseEventType));
    }
    
    @Test
    public void testFindNonDeletedAdverseEventTypeViewModels_shouldReturnViewModelsForNonDeletedAdverseEventTypes() {
        
        String firstExpectedName = "b.name";
        String secondExpectedName = "c.name";
        String firstExpectedDescription = "first.description";
        String secondExpectedDescription = "second.description";
        
        AdverseEventType secondAdverseEventType = anAdverseEventType()
                .withName(secondExpectedName)
                .withDescription(secondExpectedDescription)
                .buildAndPersist(entityManager);
        AdverseEventType firstAdverseEventType = anAdverseEventType()
                .withName(firstExpectedName)
                .withDescription(firstExpectedDescription)
                .buildAndPersist(entityManager);
        
        // Excluded by deleted flag
        anAdverseEventType()
                .thatIsDeleted()
                .withName("a.name")
                .withDescription("third description")
                .build();
        
        AdverseEventTypeViewModel firstExpectedViewModel = anAdverseEventTypeViewModel()
                .withId(firstAdverseEventType.getId())
                .withName(firstExpectedName)
                .withDescription(firstExpectedDescription)
                .build();
        AdverseEventTypeViewModel secondExpectedViewModel = anAdverseEventTypeViewModel()
                .withId(secondAdverseEventType.getId())
                .withName(secondExpectedName)
                .withDescription(secondExpectedDescription)
                .build();
        
        List<AdverseEventTypeViewModel> returnedViewModels = adverseEventTypeRepository
                .findNonDeletedAdverseEventTypeViewModels();
        
        assertThat(returnedViewModels.size(), is(2));
        assertThat(returnedViewModels.get(0), hasSameStateAsAdverseEventTypeViewModel(firstExpectedViewModel));
        assertThat(returnedViewModels.get(1), hasSameStateAsAdverseEventTypeViewModel(secondExpectedViewModel));
    }

}
