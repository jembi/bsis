/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jembi.bsis.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

/**
 * @author srikanth
 */
public class HibernateAwareObjectMapper extends ObjectMapper {

  public HibernateAwareObjectMapper() {
    registerModule(new Hibernate4Module());
  }
}
