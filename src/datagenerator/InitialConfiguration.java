package datagenerator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.admin.FormField;
import model.bloodbagtype.BloodBagType;
import model.donortype.DonorType;
import model.user.User;

public class InitialConfiguration {

  public static String PERSISTENCE_UNIT_NAME = "v2v";
  private EntityManagerFactory emf;
  private EntityManager em;
  
  public InitialConfiguration() {
    emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    em = emf.createEntityManager();
  }
  
  public static void main(String[] args) throws Exception {
    InitialConfiguration gen = new InitialConfiguration();
    gen.generate();
  }

  private void generate() {
    em.getTransaction().begin();
    createAdminUser();
    createFormFields();
    createDonorTypes();
    createBloodBagTypes();
    em.getTransaction().commit();
  }

  private void createBloodBagTypes() {
    BloodBagType bloodBagType;
    bloodBagType = new BloodBagType();
    bloodBagType.setBloodBagType("Single");
    
    em.persist(bloodBagType);

    bloodBagType = new BloodBagType();
    bloodBagType.setBloodBagType("Triple");
    em.persist(bloodBagType);

    bloodBagType = new BloodBagType();
    bloodBagType.setBloodBagType("Paedi");
    em.persist(bloodBagType);
  }

  private void createDonorTypes() {
    DonorType donorType;
    donorType = new DonorType();
    donorType.setDonorType("Voluntary");
    em.persist(donorType);

    donorType = new DonorType();
    donorType.setDonorType("Family");
    em.persist(donorType);
    donorType = new DonorType();

    donorType.setDonorType("Other");
    em.persist(donorType);
  }

  private void createFormFields() {
    createDonorFormFields();
    createCollectionFormFields();
  }

  private void createDonorFormFields() {
    FormField formField = new FormField();
    String[] fields = {"donorNumber", "firstName", "middleName", "lastName",
                       "birthDate", "gender", "bloodGroup", "address", "city",
                       "state", "country", "zipcode", "notes"
                      };
    for (String fieldName : fields) {
      formField.setForm("donor");
      formField.setDisplayName(getDefaultDisplayName(fieldName));
      formField.setHidden(false);
      formField.setDerived(false);
      em.persist(formField);
    }
  }

  private void createCollectionFormFields() {
    FormField formField = new FormField();
    String[] fields = {"collectionNumber", "donor", "donorType",
                       "shippingNumber", "sampleNumber", "centers",
                       "bloodBagType", "sites", "notes"
                      };
    for (String fieldName : fields) {
      formField.setForm("collection");
      formField.setDisplayName(getDefaultDisplayName(fieldName));
      formField.setHidden(false);
      formField.setDerived(false);
      em.persist(formField);
    }
  }

  /**
   * The default name for 'firstName' field would be 'First Name'.
   * Assuming field names are always in camel case.
   * @param fieldName
   * @return Display name to use for the field
   */
  private String getDefaultDisplayName(String fieldName) {
    StringBuilder displayName = new StringBuilder();

    int wordBegin = 0;
    int len = fieldName.length();
    for (int i = 0; i < len; ++i) {
      char ch = fieldName.charAt(i);
      if (Character.isUpperCase(ch)) {
        String word = " " +
                      Character.toUpperCase(fieldName.charAt(wordBegin)) +
                      fieldName.substring(wordBegin+1, i-1);
        displayName.append(word);
        wordBegin = i;
      }
    }
    return displayName.toString().trim();
  }

  private void createAdminUser() {
    User user = new User();
    user.setUsername("admin");
    user.setFirstName("admin");
    user.setPassword("admin321123");
    user.setIsStaff(true);
    user.setIsSuperuser(true);
    user.setIsActive(true);
    em.persist(user);
  }
}
