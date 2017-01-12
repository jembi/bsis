package org.jembi.bsis.viewmodel;

public class VersionViewModel {

  private String version;
  private String gitDescribe;
  private String buildNumber;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getGitDescribe() {
    return this.gitDescribe;
  }

  public void setGitDescribe(String gitDescribe) {
    this.gitDescribe = gitDescribe;
  }

  public String getBuildNumber() {
    return buildNumber;
  }

  public void setBuildNumber(String buildNumber) {
    this.buildNumber = buildNumber;
  }
}
