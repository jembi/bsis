package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;

import model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import repository.ConfigChangeRepository;

@Controller
public class FirstTimeConfigController {

  public final static String INSTALLED_CHECK_FILE_NAME = "v2v-version.txt"; 

  @Autowired
  ConfigChangeRepository configChangeRepository;

  private void firstTimeConfigure() throws Exception {
    File f = new File(INSTALLED_CHECK_FILE_NAME);
    BufferedWriter bw = new BufferedWriter(new FileWriter(f));
    bw.write("#Vein-to-Vein Installed " + new Date().toString());
    bw.write("version: " + UtilController.VERSION_NUMBER);
    bw.flush();
    bw.close();
    System.out.println("Configured");
  }
  
  @RequestMapping(value="/configureServerFirstTime", method=RequestMethod.POST)
  public ModelAndView configureServerFirstTime(HttpServletRequest request) {

    // check if user is allowed to configure server
    User user = (User) request.getSession().getAttribute("user");
    if (!user.getIsAdmin()) {
      return new ModelAndView("admin/adminPermissionRequired");
    }

    // check if first time configuration is required
    if (!isFirstTimeConfig()) {
      return new ModelAndView("admin/firstTimeConfigNotRequired");
    }

    // perform the first time configuration changes
    try {
      firstTimeConfigure();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return new ModelAndView("admin/configError");
    }

    ModelAndView modelAndView = new ModelAndView("welcomePage");

    Map<String, Object> model = new HashMap<String, Object>();
    modelAndView.addObject("model", model);
    return modelAndView;
  }

  /**
   * Utility method used to fetch Class list based on a package name.
   * 
   * @param packageName
   *          (should be the package containing your annotated beans.
   */
  private List<Class<?>> getClasses(String packageName) throws Exception {
    List<Class<?>> classes = new ArrayList<Class<?>>();
    File directory = null;
    try {
      ClassLoader cld = Thread.currentThread().getContextClassLoader();
      if (cld == null) {
        throw new ClassNotFoundException("Can't get class loader.");
      }
      String path = packageName.replace('.', '/');
      URL resource = cld.getResource(path);
      if (resource == null) {
        throw new ClassNotFoundException("No resource for " + path);
      }
      directory = new File(resource.getFile());
    } catch (NullPointerException x) {
      throw new ClassNotFoundException(packageName + " (" + directory
          + ") does not appear to be a valid package");
    }
    if (directory.exists()) {
      String[] files = directory.list();
      for (int i = 0; i < files.length; i++) {
        if (files[i].endsWith(".class")) {
          // removes the .class extension
          classes.add(Class.forName(packageName + '.'
              + files[i].substring(0, files[i].length() - 6)));
        }
      }
    } else {
      throw new ClassNotFoundException(packageName + " is not a valid package");
    }

    return classes;
  }

  private List<Class<?>> findAllClasses(String basePackage)
      throws IOException, ClassNotFoundException {

      ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
      MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

      List<Class<?>> candidates = new ArrayList<Class<?>>();
      String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                                 resolveBasePackage(basePackage) + "/" + "**/*.class";
      Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
      for (Resource resource : resources) {
          if (resource.isReadable()) {
              MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
              if (isCandidate(metadataReader)) {
                  candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
              }
          }
      }
      return candidates;
  }

  private String resolveBasePackage(String basePackage) {
      return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
  }

  private boolean isCandidate(MetadataReader metadataReader) throws ClassNotFoundException
  {
      try {
          Class<?> c = Class.forName(metadataReader.getClassMetadata().getClassName());
          if (c.getAnnotation(Entity.class) != null) {
              return true;
          }
      }
      catch(Throwable e){
      }
      return false;
  }

  public boolean isFirstTimeConfig() {
    File f = new File(INSTALLED_CHECK_FILE_NAME);
    return !f.exists();
  }
}
