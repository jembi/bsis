package org.jembi.bsis.interceptor;
import java.util.Date;

import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.PersistEvent;
import org.jembi.bsis.interceptor.EntitySaveListener;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;



public class EntitySaveListenerTest extends SecurityContextDependentTestSuite {
  
  EntitySaveListener listener = new EntitySaveListener();

  @Test
  public void testPersist_FirstTime() {
    Donor donor = new Donor();
    PersistEvent event = new PersistEvent(donor, null);
    
    listener.onPersist(event);
    
    Assert.assertEquals("Audit fields were set", loggedInUser, donor.getCreatedBy());
    Assert.assertEquals("Audit fields were set", loggedInUser, donor.getLastUpdatedBy());
    Assert.assertNotNull("Audit fields were set", donor.getCreatedDate());
    Assert.assertNotNull("Audit fields were set", donor.getLastUpdated());
  }
  
  @Test
  public void testPersist_SecondTime() {
    Donor donor = new Donor();
    Date createdDate = new Date();
    donor.setCreatedDate(createdDate);
    User user = new User();
    donor.setCreatedBy(user);
    PersistEvent event = new PersistEvent(donor, null);
    
    listener.onPersist(event);
    
    Assert.assertEquals("CreatedBy was not changed", user, donor.getCreatedBy());
    Assert.assertEquals("Audit fields were set", loggedInUser, donor.getLastUpdatedBy());
    Assert.assertEquals("CreatedDate was not changed", createdDate, donor.getCreatedDate());
    Assert.assertNotNull("Audit fields were set", donor.getLastUpdated());
  }
  
  @Test
  public void testMerge() {
    Donor donor = new Donor();
    Date createdDate = new Date();
    donor.setCreatedDate(createdDate);
    User user = new User();
    donor.setCreatedBy(user);
    MergeEvent event = new MergeEvent(donor, null);
    event.setEntity(donor);
    
    listener.onMerge(event);
    
    Assert.assertEquals("CreatedBy was not changed", user, donor.getCreatedBy());
    Assert.assertEquals("Audit fields were set", loggedInUser, donor.getLastUpdatedBy());
    Assert.assertEquals("CreatedDate was not changed", createdDate, donor.getCreatedDate());
    Assert.assertNotNull("Audit fields were set", donor.getLastUpdated());
  }
}
