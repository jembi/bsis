package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jembi.bsis.helpers.builders.PermissionBackingFormBuilder.aPermissionBackingForm;
import static org.jembi.bsis.helpers.builders.PermissionBuilder.aPermission;
import static org.jembi.bsis.helpers.builders.PermissionViewModelBuilder.aPermissionViewModel;
import static org.jembi.bsis.helpers.matchers.PermissionMatcher.hasSameStateAsPermission;
import static org.jembi.bsis.helpers.matchers.PermissionViewModelMatcher.hasSameStateAsPermissionViewModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jembi.bsis.backingform.PermissionBackingForm;
import org.jembi.bsis.model.user.Permission;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.PermissionViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;

public class PermissionFactoryTests extends UnitTestSuite {

  @InjectMocks
  private PermissionFactory permissionFactory;
  
  @Test
  public void testCreateViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    Permission permission = aPermission()
        .withId(1L)
        .withName("do this")
        .build();

    PermissionViewModel expectedViewModel = aPermissionViewModel()
        .withId(1L)
        .withName("do this")
        .build();
    
    // Exercise SUT
    PermissionViewModel returnedViewModel = permissionFactory.createViewModel(permission);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsPermissionViewModel(expectedViewModel));
  }

  @Test
  public void testCreateViewModelsWithNull_shouldReturnEmptyList() {
    Set<PermissionViewModel> returnedViewModels = permissionFactory.createViewModels(null);
    assertThat(returnedViewModels, is(notNullValue()));
    assertThat(returnedViewModels.size(), is(0));
  }

  @Test
  public void testCreateViewModels_shouldReturnExpectedViewModels() {
    List<Permission> perms = Arrays.asList(
        aPermission().withId(1L).build(),
        aPermission().withId(2L).build(),
        aPermission().withId(3L).build());
    
    Set<PermissionViewModel> returnedViewModels = permissionFactory.createViewModels(perms);
    
    assertThat(returnedViewModels.size(), is(3));
  }

  @Test
  public void testCreateEntity_shouldReturnExpectedEntity() {
    // Set up fixtures
    PermissionBackingForm form = aPermissionBackingForm()
        .withId(1L)
        .withName("do this")
        .build();

    Permission expectedPermission = aPermission()
        .withId(1L)
        .withName("do this")
        .build();

    // Exercise SUT
    Permission returnedPermission = permissionFactory.createEntity(form);

    // Verify
    assertThat(returnedPermission, hasSameStateAsPermission(expectedPermission));
  }

  @Test
  public void testCreateEntities_shouldReturnExpectedEntities() {
    // Set up fixtures
    Set<PermissionBackingForm> forms = new HashSet<>(Arrays.asList(
        aPermissionBackingForm()
          .withId(1L)
          .build(),
        aPermissionBackingForm()
          .withId(2L)
          .build()
        ));

    // Exercise SUT
    Set<Permission> returnedPermissions = permissionFactory.createEntities(forms);

    // Verify
    assertThat(returnedPermissions.size(), is(2));
  }
}