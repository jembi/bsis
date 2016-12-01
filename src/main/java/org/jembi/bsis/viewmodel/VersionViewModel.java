package org.jembi.bsis.viewmodel;

public class VersionViewModel {

  private String version;
  private String gitDescribe;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setGitDescribe(String gitDescribe) {
    this.gitDescribe = gitDescribe;
  }

  public String getGitDescribe() {
    return this.gitDescribe;
  }
}
