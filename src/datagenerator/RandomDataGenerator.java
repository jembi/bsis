package datagenerator;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import repository.CollectedSampleRepository;
import repository.DonorRepository;
import repository.IssueRepository;
import repository.LocationRepository;
import repository.LocationTypeRepository;
import repository.RequestRepository;
import repository.TestResultRepository;
import repository.UsageRepository;

public class RandomDataGenerator {

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private LocationTypeRepository locationTypeRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private CollectedSampleRepository collectionRepository;

  @Autowired
  private TestResultRepository testResultRepository;

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private IssueRepository issueRepository;

  @Autowired
  private UsageRepository usageRepository;

  Random r = new Random();

  public static void main(String[] args) throws Exception {
    RandomDataGenerator gen = new RandomDataGenerator();
    gen.generate();
  }

  private void generate() {
    
  }

}
