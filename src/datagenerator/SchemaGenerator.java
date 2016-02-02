package datagenerator;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import javax.persistence.Entity;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Deprecated - But I see no impact auditing
 * import org.hibernate.envers.configuration.AuditConfiguration;
 */

/**
 * Auto generate schema using hibernate hbm2ddl tool. This will prepare the full
 * schema for a fresh installation of BSIS.
 * The schema changes for the update need to be worked out manually.
 * Source:
 * http://jandrewthompson.blogspot.com/2009/10/how-to-generate-ddl-scripts-from.html
 * More information:
 * http://stackoverflow.com/questions/438146/hibernate-hbm2ddl-auto-possible-values-and-what-they-do
 */
public class SchemaGenerator {

  private Configuration cfg;

  private SchemaGenerator(String packageName) throws Exception {
    cfg = new Configuration();
    cfg.setProperty("hibernate.show_sql", "true");
    cfg.setProperty("hibernate.hbm2ddl.auto", "none");
    cfg.setProperty("hibernate.ejb.event.post-insert",
        "org.hibernate.ejb.event.EJB3PostInsertEventListener,org.hibernate.envers.event.AuditEventListener");
    cfg.setProperty("hibernate.ejb.event.post-update",
        "org.hibernate.ejb.event.EJB3PostUpdateEventListener,org.hibernate.envers.event.AuditEventListener");
    cfg.setProperty("hibernate.ejb.event.post-delete",
        "org.hibernate.ejb.event.EJB3PostDeleteEventListener,org.hibernate.envers.event.AuditEventListener");
    cfg.setProperty("hibernate.ejb.event.pre-collection-update",
        "org.hibernate.envers.event.AuditEventListener");
    cfg.setProperty("hibernate.ejb.event.pre-collection-remove",
        "org.hibernate.envers.event.AuditEventListener");
    cfg.setProperty("hibernate.ejb.event.post-collection-recreate",
        "org.hibernate.envers.event.AuditEventListener");
    for (Class<?> clazz : findAllClasses(packageName)) {
      System.out.println("Class: " + clazz);
      cfg.addAnnotatedClass(clazz);
    }
    cfg.buildMappings();
    // AuditConfiguration.getFor(cfg);
  }

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    SchemaGenerator gen = new SchemaGenerator("model");
    gen.generate(Dialect.MYSQL);
  }

  /**
   * Method that actually creates the file.
   *
   * @param dbDialect to use
   */
  private void generate(Dialect dialect) {
    cfg.setProperty("hibernate.dialect", dialect.getDialectClass());
    SchemaExport export = new SchemaExport(cfg);
    export.setDelimiter(";");
    export.setOutputFile("ddl_" + dialect.name().toLowerCase() + ".sql");
    export.execute(true, false, false, true);
  }

  /**
   * Utility method used to fetch Class list based on a package name.
   *
   * @param packageName (should be the package containing your annotated beans.
   */
  @SuppressWarnings("unused")
  private List<Class<?>> getClasses(String packageName) throws Exception {
    List<Class<?>> classes = new ArrayList<>();
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
      for (String file : files) {
        if (file.endsWith(".class")) {
          // removes the .class extension
          classes.add(Class.forName(packageName + '.'
              + file.substring(0, file.length() - 6)));
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

    List<Class<?>> candidates = new ArrayList<>();
    String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
        resolveBasePackage(basePackage) + "/" + "**/*.class";
    Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
    for (Resource resource : resources) {
      if (resource.isReadable()) {
        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
        if (isCandidate(metadataReader)) {
          candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
          System.out.println(metadataReader.getClassMetadata().getClassName());
        }
      }
    }
    return candidates;
  }

  private String resolveBasePackage(String basePackage) {
    return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
  }

  private boolean isCandidate(MetadataReader metadataReader) throws ClassNotFoundException {
    try {
      Class<?> c = Class.forName(metadataReader.getClassMetadata().getClassName());
      if (c.getAnnotation(Entity.class) != null) {
        return true;
      }
    } catch (Throwable e) {
    }
    return false;
  }

  /**
   * Holds the classnames of hibernate dialects for easy reference.
   */
  private enum Dialect {
    ORACLE("org.hibernate.dialect.Oracle10gDialect"), MYSQL(
        "org.hibernate.dialect.MySQL5InnoDBDialect"), HSQL(
        "org.hibernate.dialect.HSQLDialect");

    private String dialectClass;

    Dialect(String dialectClass) {
      this.dialectClass = dialectClass;
    }

    public String getDialectClass() {
      return dialectClass;
    }
  }
}