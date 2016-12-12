package org.jembi.bsis.service;

import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.jembi.bsis.viewmodel.VersionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VersionService {

  private static final Logger LOGGER = Logger.getLogger(VersionService.class);

  private static final String PATH_META_INF_MANIFEST_MF = "/META-INF/MANIFEST.MF";

  @Autowired
  ServletContext context;

  String version = null;
  String gitDescribe = null;
  String buildNumber = null;

  @PostConstruct
  public void loadVersionInformation() {
    Properties prop = new Properties();
    try {
      InputStream is = context.getResourceAsStream(PATH_META_INF_MANIFEST_MF);
      if (is == null) {
        LOGGER.error("Unable to obtain version information from " + PATH_META_INF_MANIFEST_MF
            + " file. The Resource could not be obtained as a stream");
      } else {
        prop.load(is);
        version = prop.getProperty("Implementation-Version");
        gitDescribe = prop.getProperty("git-describe");
        if (gitDescribe != null) {
          buildNumber = gitDescribe.substring(gitDescribe.lastIndexOf("-g") + 2);
        }
      }
    } catch (Exception e) {
      LOGGER.error(
          "Unable to obtain version information from " + PATH_META_INF_MANIFEST_MF + "file /r/n" + e.getMessage(), e);
    }
  }

  /**
   * Returns a version object containing the POM version and the git describe value at the time of
   * the build.
   *
   * @return VersionViewModel
   */
  public VersionViewModel getVersion() {

    VersionViewModel versionViewModel = new VersionViewModel();
    versionViewModel.setVersion(version);
    versionViewModel.setGitDescribe(gitDescribe);
    versionViewModel.setBuildNumber(buildNumber);

    return versionViewModel;
  }
}

