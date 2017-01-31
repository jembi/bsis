package org.jembi.bsis.model.component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ComponentStatusTests {
  
  @Test
  public void testIsFinalComponentStatus_shouldReturn_True() {
    assertThat(ComponentStatus.isFinalStatus(ComponentStatus.DISCARDED), is(true));
    assertThat(ComponentStatus.isFinalStatus(ComponentStatus.ISSUED), is(true));
    assertThat(ComponentStatus.isFinalStatus(ComponentStatus.USED), is(true));
    assertThat(ComponentStatus.isFinalStatus(ComponentStatus.PROCESSED), is(true));
    assertThat(ComponentStatus.isFinalStatus(ComponentStatus.UNSAFE), is(true));
  }
  
  @Test
  public void testIsFinalComponentStatus_shouldReturn_False() {
    assertThat(ComponentStatus.isFinalStatus(ComponentStatus.EXPIRED), is(false));
    assertThat(ComponentStatus.isFinalStatus(ComponentStatus.AVAILABLE), is(false));
    assertThat(ComponentStatus.isFinalStatus(ComponentStatus.QUARANTINED), is(false));
  }
  
  @Test
  public void testAVAILABLEInGetComponentRelatedStatuses_shouldReturn_True() {
    ComponentStatus status = ComponentStatus.AVAILABLE;
    Set<ComponentStatus> componentStatuses = new HashSet<>(ComponentStatus.getComponentRelatedStatuses());
    assertThat(componentStatuses.contains(status), is(true));
  }
  
  @Test
  public void testQUARANTINEDInGetComponentRelatedStatuses_shouldReturn_True() {
    ComponentStatus status = ComponentStatus.QUARANTINED;
    Set<ComponentStatus> componentStatuses = new HashSet<>(ComponentStatus.getComponentRelatedStatuses());
    assertThat(componentStatuses.contains(status), is(true));
  }
  
  @Test
  public void testUNSAFEInGetComponentRelatedStatuses_shouldReturn_True() {
    ComponentStatus status = ComponentStatus.UNSAFE;
    Set<ComponentStatus> componentStatuses = new HashSet<>(ComponentStatus.getComponentRelatedStatuses());
    assertThat(componentStatuses.contains(status), is(true));
  }
  
  @Test
  public void testEXPIREDInGetComponentRelatedStatuses_shouldReturn_False() {
    ComponentStatus status = ComponentStatus.EXPIRED;
    Set<ComponentStatus> componentStatuses = new HashSet<>(ComponentStatus.getComponentRelatedStatuses());
    assertThat(componentStatuses.contains(status), is(false));
  }
  
  @Test
  public void testISSUEDInGetComponentRelatedStatuses_shouldReturn_False() {
    ComponentStatus status = ComponentStatus.ISSUED;
    Set<ComponentStatus> componentStatuses = new HashSet<>(ComponentStatus.getComponentRelatedStatuses());
    assertThat(componentStatuses.contains(status), is(false));
  }
  
  @Test
  public void testUSEDInGetComponentRelatedStatuses_shouldReturn_False() {
    ComponentStatus status = ComponentStatus.USED;
    Set<ComponentStatus> componentStatuses = new HashSet<>(ComponentStatus.getComponentRelatedStatuses());
    assertThat(componentStatuses.contains(status), is(false));
  }
  
  @Test
  public void testDISCARDEDInGetComponentRelatedStatuses_shouldReturn_False() {
    ComponentStatus status = ComponentStatus.DISCARDED;
    Set<ComponentStatus> componentStatuses = new HashSet<>(ComponentStatus.getComponentRelatedStatuses());
    assertThat(componentStatuses.contains(status), is(false));
  }
  
  @Test
  public void testPROCESSEDInGetComponentRelatedStatuses_shouldReturn_False() {
    ComponentStatus status = ComponentStatus.PROCESSED;
    Set<ComponentStatus> componentStatuses = new HashSet<>(ComponentStatus.getComponentRelatedStatuses());
    assertThat(componentStatuses.contains(status), is(false));
  }
}