package org.jembi.bsis.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;

import java.util.Date;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.utils.ComponentUtils;
import org.junit.Test;


public class ComponentUtilsTest extends UnitTestSuite {
  
  @Test
  public void testGetDaysToExpire(){
    
    Component component = aComponent()
        .withExpiresOn(new Date())
        .build();
        
        assertThat(0, comparesEqualTo(ComponentUtils.getDaysToExpire(component)));
    }
  }
