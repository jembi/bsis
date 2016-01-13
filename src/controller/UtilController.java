package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import repository.TipsRepository;
import security.BsisUserDetails;

@org.springframework.stereotype.Component
public class UtilController {
  public static final String VERSION_NUMBER = "1.3";

  @Autowired
  private TipsRepository tipsRepository;

  @Autowired
  private ServletContext servletContext;

  public void addTipsToModel(Map<String, Object> m, String key) {
    m.put(key, tipsRepository.getTipsContent(key));
  }

  public Properties getDatabaseProperties() throws IOException {
    Properties prop = new Properties();
    BufferedReader reader = new BufferedReader(new InputStreamReader(servletContext.getResourceAsStream("/WEB-INF/classes/database.properties")));
    String propertyFileContents = "";
    String line;
    while ((line = reader.readLine()) != null) {
      propertyFileContents += line + "\n";
    }
    prop.load(new StringReader(propertyFileContents.replace("\\","\\\\")));
    return prop;
  }

  public User getCurrentUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = null;
    if (principal != null && principal instanceof BsisUserDetails)
      user = ((BsisUserDetails) principal).getUser();
    return user;
  }
}
