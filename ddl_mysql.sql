CREATE TABLE PackType (
  id        SMALLINT NOT NULL AUTO_INCREMENT,
  packType  VARCHAR(50),
  canPool   BOOLEAN,
  canSplit  BOOLEAN,
  isDeleted BOOLEAN,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE PackType_AUD (
  id        SMALLINT NOT NULL,
  REV       INTEGER  NOT NULL,
  REVTYPE   TINYINT,
  packType  VARCHAR(50),
  canPool   BOOLEAN,
  canSplit  BOOLEAN,
  isDeleted BOOLEAN,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE BloodTest (
  id              SMALLINT NOT NULL AUTO_INCREMENT,
  bloodTestType   VARCHAR(30),
  category        VARCHAR(30),
  context         VARCHAR(30),
  isActive        BOOLEAN,
  isEmptyAllowed  BOOLEAN,
  negativeResults VARCHAR(255),
  positiveResults VARCHAR(255),
  rankInCategory  INTEGER,
  testName        VARCHAR(40),
  testNameShort   VARCHAR(25),
  validResults    VARCHAR(255),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE BloodTestResult (
  id                BIGINT NOT NULL AUTO_INCREMENT,
  createdDate       TIMESTAMP,
  lastUpdated       TIMESTAMP,
  notes             LONGTEXT,
  result            VARCHAR(10),
  testedOn          DATETIME,
  bloodTest_id      SMALLINT,
  donation_id       BIGINT,
  machineReading_id BIGINT,
  createdBy_id      SMALLINT,
  lastUpdatedBy_id  SMALLINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE BloodTest_AUD (
  id              SMALLINT NOT NULL,
  REV             INTEGER  NOT NULL,
  REVTYPE         TINYINT,
  bloodTestType   VARCHAR(30),
  category        VARCHAR(30),
  context         VARCHAR(30),
  isActive        BOOLEAN,
  isEmptyAllowed  BOOLEAN,
  negativeResults VARCHAR(255),
  positiveResults VARCHAR(255),
  rankInCategory  INTEGER,
  testName        VARCHAR(40),
  testNameShort   VARCHAR(25),
  validResults    VARCHAR(255),
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE BloodTest_WorksheetType (
  bloodTests_id     SMALLINT NOT NULL,
  worksheetTypes_id SMALLINT NOT NULL,
  PRIMARY KEY (bloodTests_id, worksheetTypes_id)
)
  ENGINE = InnoDB;

CREATE TABLE BloodTest_WorksheetType_AUD (
  REV               INTEGER  NOT NULL,
  bloodTests_id     SMALLINT NOT NULL,
  worksheetTypes_id SMALLINT NOT NULL,
  REVTYPE           TINYINT,
  PRIMARY KEY (REV, bloodTests_id, worksheetTypes_id)
)
  ENGINE = InnoDB;

CREATE TABLE BloodTestingRule (
  id                   MEDIUMINT NOT NULL AUTO_INCREMENT,
  bloodTestsIds        VARCHAR(200),
  category             VARCHAR(30),
  donationFieldChanged VARCHAR(12),
  context              VARCHAR(30),
  extraInformation     VARCHAR(30),
  isActive             BOOLEAN,
  markSampleAsUnsafe   BOOLEAN,
  newInformation       VARCHAR(30),
  pattern              VARCHAR(50),
  pendingTestsIds      VARCHAR(60),
  subCategory          VARCHAR(30),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE BloodTestingRule_AUD (
  id                   MEDIUMINT NOT NULL,
  REV                  INTEGER   NOT NULL,
  REVTYPE              TINYINT,
  bloodTestsIds        VARCHAR(200),
  category             VARCHAR(30),
  donationFieldChanged VARCHAR(12),
  context              VARCHAR(30),
  extraInformation     VARCHAR(30),
  isActive             BOOLEAN,
  markSampleAsUnsafe   BOOLEAN,
  newInformation       VARCHAR(30),
  pattern              VARCHAR(50),
  pendingTestsIds      VARCHAR(60),
  subCategory          VARCHAR(30),
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE Donation (
  id                           BIGINT NOT NULL AUTO_INCREMENT,
  bloodAbo                     VARCHAR(50),
  bloodPressure                DECIMAL(6, 2),
  bloodRh                      VARCHAR(50),
  bloodTypingStatus            VARCHAR(20),
  donationDate                 DATETIME,
  donationIdentificationNumber VARCHAR(20) UNIQUE,
  donorWeight                  DECIMAL(6, 2),
  extraBloodTypeInformation    VARCHAR(150),
  haemoglobinCount             DECIMAL(6, 2),
  isDeleted                    BOOLEAN,
  createdDate                  TIMESTAMP,
  lastUpdated                  TIMESTAMP,
  notes                        LONGTEXT,
  ttiStatus                    VARCHAR(20),
  packType_id                  SMALLINT,
  donationBatch_id             SMALLINT,
  donationCreatedBy_id         SMALLINT,
  donationType_id              SMALLINT,
  donor_id                     BIGINT,
  createdBy_id                 SMALLINT,
  lastUpdatedBy_id             SMALLINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE Donation_AUD (
  id                           BIGINT  NOT NULL,
  REV                          INTEGER NOT NULL,
  REVTYPE                      TINYINT,
  bloodAbo                     VARCHAR(50),
  bloodPressure                DECIMAL(6, 2),
  bloodRh                      VARCHAR(50),
  bloodTypingStatus            VARCHAR(20),
  donationDate                 DATETIME,
  donationIdentificationNumber VARCHAR(20),
  donorWeight                  DECIMAL(6, 2),
  extraBloodTypeInformation    VARCHAR(150),
  haemoglobinCount             DECIMAL(6, 2),
  isDeleted                    BOOLEAN,
  createdDate                  TIMESTAMP,
  lastUpdated                  TIMESTAMP,
  notes                        LONGTEXT,
  ttiStatus                    VARCHAR(20),
  packType_id                  SMALLINT,
  donationBatch_id             SMALLINT,
  donationCreatedBy_id         SMALLINT,
  donationType_id              SMALLINT,
  donor_id                     BIGINT,
  createdBy_id                 SMALLINT,
  lastUpdatedBy_id             SMALLINT,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE DonationBatch (
  id               SMALLINT NOT NULL AUTO_INCREMENT,
  batchNumber      VARCHAR(20) UNIQUE,
  isDeleted        BOOLEAN  NOT NULL,
  createdDate      TIMESTAMP,
  lastUpdated      TIMESTAMP,
  notes            LONGTEXT,
  createdBy_id     SMALLINT,
  lastUpdatedBy_id SMALLINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE DonationBatch_AUD (
  id               SMALLINT NOT NULL,
  REV              INTEGER  NOT NULL,
  REVTYPE          TINYINT,
  batchNumber      VARCHAR(20),
  isDeleted        BOOLEAN,
  createdDate      TIMESTAMP,
  lastUpdated      TIMESTAMP,
  notes            LONGTEXT,
  createdBy_id     SMALLINT,
  lastUpdatedBy_id SMALLINT,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE CompatibilityTest (
  id                      BIGINT NOT NULL AUTO_INCREMENT,
  compatibililityTestDate DATETIME,
  compatibilityResult     VARCHAR(15),
  isDeleted               BOOLEAN,
  createdDate             TIMESTAMP,
  lastUpdated             TIMESTAMP,
  notes                   LONGTEXT,
  testedBy                VARCHAR(255),
  transfusedBefore        BOOLEAN,
  crossmatchType_id       SMALLINT,
  forRequest_id           BIGINT,
  createdBy_id            SMALLINT,
  lastUpdatedBy_id        SMALLINT,
  testedComponent_id      BIGINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE CompatibilityTest_AUD (
  id                      BIGINT  NOT NULL,
  REV                     INTEGER NOT NULL,
  REVTYPE                 TINYINT,
  compatibililityTestDate DATETIME,
  compatibilityResult     VARCHAR(15),
  isDeleted               BOOLEAN,
  createdDate             TIMESTAMP,
  lastUpdated             TIMESTAMP,
  notes                   LONGTEXT,
  testedBy                VARCHAR(255),
  transfusedBefore        BOOLEAN,
  crossmatchType_id       SMALLINT,
  forRequest_id           BIGINT,
  createdBy_id            SMALLINT,
  lastUpdatedBy_id        SMALLINT,
  testedComponent_id      BIGINT,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE ContactMethodType (
  id                SMALLINT NOT NULL AUTO_INCREMENT,
  contactMethodType VARCHAR(30),
  isDeleted         BOOLEAN  NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE ContactMethodType_AUD (
  id                SMALLINT NOT NULL,
  REV               INTEGER  NOT NULL,
  REVTYPE           TINYINT,
  contactMethodType VARCHAR(30),
  isDeleted         BOOLEAN,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE CrossmatchType (
  id             SMALLINT NOT NULL AUTO_INCREMENT,
  crossmatchType VARCHAR(255),
  isDeleted      BOOLEAN,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE CrossmatchType_AUD (
  id             SMALLINT NOT NULL,
  REV            INTEGER  NOT NULL,
  REVTYPE        TINYINT,
  crossmatchType VARCHAR(255),
  isDeleted      BOOLEAN,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE DeferralReason (
  id        SMALLINT NOT NULL AUTO_INCREMENT,
  isDeleted BOOLEAN,
  reason    VARCHAR(100),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE DeferralReason_AUD (
  id        SMALLINT NOT NULL,
  REV       INTEGER  NOT NULL,
  REVTYPE   TINYINT,
  isDeleted BOOLEAN,
  reason    VARCHAR(100),
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE DonationType (
  id           SMALLINT NOT NULL AUTO_INCREMENT,
  donationType VARCHAR(50),
  isDeleted    BOOLEAN,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE DonationType_AUD (
  id           SMALLINT NOT NULL,
  REV          INTEGER  NOT NULL,
  REVTYPE      TINYINT,
  donationType VARCHAR(50),
  isDeleted    BOOLEAN,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE Donor (
  id                        BIGINT NOT NULL AUTO_INCREMENT,
  age                       TINYINT,
  birthDate                 DATE,
  birthDateInferred         DATE,
  birthDateEstimated        BOOLEAN,
  bloodAbo                  VARCHAR(10),
  bloodRh                   VARCHAR(10),
  callingName               VARCHAR(20),
  address                   VARCHAR(100),
  city                      VARCHAR(25),
  country                   VARCHAR(25),
  district                  VARCHAR(25),
  otherPhoneNumber          VARCHAR(20),
  phoneNumber               VARCHAR(20),
  province                  VARCHAR(25),
  state                     VARCHAR(25),
  zipcode                   VARCHAR(10),
  dateOfLastDonation        DATETIME,
  donorHash                 VARCHAR(50),
  donorNumber               VARCHAR(20) UNIQUE,
  donorStatus               VARCHAR(20),
  firstName                 VARCHAR(20),
  gender                    VARCHAR(15),
  isDeleted                 BOOLEAN,
  lastName                  VARCHAR(20),
  middleName                VARCHAR(20),
  createdDate               TIMESTAMP,
  lastUpdated               TIMESTAMP,
  nationalID                VARCHAR(15),
  notes                     LONGTEXT,
  preferredContactMethod_id SMALLINT,
  donorPanel_id             BIGINT,
  createdBy_id              SMALLINT,
  lastUpdatedBy_id          SMALLINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE DonorDeferral (
  id                 BIGINT NOT NULL AUTO_INCREMENT,
  deferredDonor_id   BIGINT,
  deferralReasonText LONGTEXT,
  deferredUntil      DATE,
  deferralReason_id  SMALLINT,
  createdDate        TIMESTAMP,
  lastUpdated        TIMESTAMP,
  createdBy_id       SMALLINT,
  lastUpdatedBy_id   SMALLINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE DonorDeferral_AUD (
  id                 BIGINT  NOT NULL,
  REV                INTEGER NOT NULL,
  REVTYPE            TINYINT,
  deferredDonor_id   BIGINT,
  deferralReasonText LONGTEXT,
  deferredUntil      DATE,
  deferralReason_id  SMALLINT,
  createdDate        TIMESTAMP,
  lastUpdated        TIMESTAMP,
  createdBy_id       SMALLINT,
  lastUpdatedBy_id   SMALLINT,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE Donor_AUD (
  id                        BIGINT  NOT NULL,
  REV                       INTEGER NOT NULL,
  REVTYPE                   TINYINT,
  age                       TINYINT,
  birthDate                 DATE,
  birthDateInferred         DATE,
  birthDateEstimated        BOOLEAN,
  bloodAbo                  VARCHAR(10),
  bloodRh                   VARCHAR(10),
  callingName               VARCHAR(20),
  address                   VARCHAR(100),
  city                      VARCHAR(25),
  country                   VARCHAR(25),
  district                  VARCHAR(25),
  otherPhoneNumber          VARCHAR(20),
  phoneNumber               VARCHAR(20),
  province                  VARCHAR(25),
  state                     VARCHAR(25),
  zipcode                   VARCHAR(10),
  dateOfLastDonation        DATETIME,
  donorHash                 VARCHAR(50),
  donorNumber               VARCHAR(20),
  donorStatus               VARCHAR(20),
  firstName                 VARCHAR(20),
  gender                    VARCHAR(15),
  isDeleted                 BOOLEAN,
  lastName                  VARCHAR(20),
  middleName                VARCHAR(20),
  createdDate               TIMESTAMP,
  lastUpdated               TIMESTAMP,
  nationalID                VARCHAR(15),
  notes                     LONGTEXT,
  preferredContactMethod_id SMALLINT,
  donorPanel_id             BIGINT,
  createdBy_id              SMALLINT,
  lastUpdatedBy_id          SMALLINT,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE FormField (
  id                 BIGINT NOT NULL AUTO_INCREMENT,
  autoGenerate       BOOLEAN,
  canBeOptional      BOOLEAN,
  defaultDisplayName VARCHAR(60),
  defaultValue       LONGTEXT,
  displayName        VARCHAR(60),
  field              VARCHAR(30),
  form               VARCHAR(30),
  hidden             BOOLEAN,
  isAutoGeneratable  BOOLEAN,
  isHidable          BOOLEAN,
  isRequired         BOOLEAN,
  isTimeField        BOOLEAN,
  maxLength          INTEGER,
  useCurrentTime     BOOLEAN,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE GenericConfig (
  id            BIGINT NOT NULL AUTO_INCREMENT,
  propertyName  VARCHAR(80),
  propertyOwner VARCHAR(30),
  propertyValue VARCHAR(80),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE Location (
  id           BIGINT NOT NULL AUTO_INCREMENT,
  isDeleted    BOOLEAN,
  isDonorPanel BOOLEAN,
  isMobileSite BOOLEAN,
  isUsageSite  BOOLEAN,
  name         VARCHAR(255),
  notes        LONGTEXT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE Location_AUD (
  id           BIGINT  NOT NULL,
  REV          INTEGER NOT NULL,
  REVTYPE      TINYINT,
  isDeleted    BOOLEAN,
  isDonorPanel BOOLEAN,
  isMobileSite BOOLEAN,
  isUsageSite  BOOLEAN,
  name         VARCHAR(255),
  notes        LONGTEXT,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE MachineReading (
  id              BIGINT NOT NULL AUTO_INCREMENT,
  columnNumber    SMALLINT,
  machineReading  DECIMAL(7, 3),
  rowNumber       SMALLINT,
  plateSession_id BIGINT,
  wellType_id     SMALLINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE MicrotiterPlate (
  id         SMALLINT NOT NULL AUTO_INCREMENT,
  isDeleted  BOOLEAN,
  notes      LONGTEXT,
  numColumns SMALLINT,
  numRows    SMALLINT,
  plateKey   VARCHAR(15) UNIQUE,
  plateName  VARCHAR(20),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE MicrotiterPlate_AUD (
  id         SMALLINT NOT NULL,
  REV        INTEGER  NOT NULL,
  REVTYPE    TINYINT,
  isDeleted  BOOLEAN,
  notes      LONGTEXT,
  numColumns SMALLINT,
  numRows    SMALLINT,
  plateKey   VARCHAR(15),
  plateName  VARCHAR(20),
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE Permission (
  id   BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE Permission_AUD (
  id      BIGINT  NOT NULL,
  REV     INTEGER NOT NULL,
  REVTYPE TINYINT,
  name    VARCHAR(50),
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE Permission_Role (
  permissions_id BIGINT NOT NULL,
  roles_id       BIGINT NOT NULL
)
  ENGINE = InnoDB;

CREATE TABLE Permission_Role_AUD (
  REV            INTEGER NOT NULL,
  permissions_id BIGINT  NOT NULL,
  roles_id       BIGINT  NOT NULL,
  REVTYPE        TINYINT,
  PRIMARY KEY (REV, permissions_id, roles_id)
)
  ENGINE = InnoDB;

CREATE TABLE PlateSession (
  id          BIGINT NOT NULL AUTO_INCREMENT,
  isDeleted   BOOLEAN,
  plateUsedOn DATETIME,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE PlateSession_AUD (
  id          BIGINT  NOT NULL,
  REV         INTEGER NOT NULL,
  REVTYPE     TINYINT,
  isDeleted   BOOLEAN,
  plateUsedOn DATETIME,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE PlateSession_BloodTestResult (
  PlateSession_id      BIGINT NOT NULL,
  bloodTestsOnPlate_id BIGINT NOT NULL,
  UNIQUE (bloodTestsOnPlate_id)
)
  ENGINE = InnoDB;

CREATE TABLE Component (
  id                 BIGINT NOT NULL AUTO_INCREMENT,
  createdOn          DATETIME,
  discardedOn        DATETIME,
  expiresOn          DATETIME,
  isDeleted          BOOLEAN,
  issuedOn           DATETIME,
  createdDate        TIMESTAMP,
  lastUpdated        TIMESTAMP,
  notes              LONGTEXT,
  status             VARCHAR(30),
  subdivisionCode    VARCHAR(3),
  donation_id        BIGINT,
  issuedTo_id        BIGINT,
  createdBy_id       SMALLINT,
  lastUpdatedBy_id   SMALLINT,
  parentComponent_id BIGINT,
  componentType_id   SMALLINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE ComponentStatusChange (
  id                     BIGINT NOT NULL AUTO_INCREMENT,
  newStatus              VARCHAR(30),
  statusChangeReasonText LONGTEXT,
  statusChangeType       VARCHAR(30),
  statusChangedOn        DATETIME,
  changedBy_id           SMALLINT,
  issuedTo_id            BIGINT,
  component_id           BIGINT,
  statusChangeReason_id  SMALLINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE ComponentStatusChangeReason (
  id                 SMALLINT NOT NULL AUTO_INCREMENT,
  category           VARCHAR(30),
  isDeleted          BOOLEAN,
  statusChangeReason VARCHAR(100),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE ComponentStatusChangeReason_AUD (
  id                 SMALLINT NOT NULL,
  REV                INTEGER  NOT NULL,
  REVTYPE            TINYINT,
  category           VARCHAR(30),
  isDeleted          BOOLEAN,
  statusChangeReason VARCHAR(100),
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE ComponentStatusChange_AUD (
  id                     BIGINT  NOT NULL,
  REV                    INTEGER NOT NULL,
  REVTYPE                TINYINT,
  newStatus              VARCHAR(30),
  statusChangeReasonText LONGTEXT,
  statusChangeType       VARCHAR(30),
  statusChangedOn        DATETIME,
  changedBy_id           SMALLINT,
  issuedTo_id            BIGINT,
  component_id           BIGINT,
  statusChangeReason_id  SMALLINT,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE ComponentType (
  id                     SMALLINT NOT NULL AUTO_INCREMENT,
  description            LONGTEXT,
  expiresAfter           INTEGER,
  expiresAfterUnits      VARCHAR(30),
  hasBloodGroup          BOOLEAN,
  isDeleted              BOOLEAN,
  componentTypeName      VARCHAR(50),
  componentTypeNameShort VARCHAR(30),
  pediComponentType_id   SMALLINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE ComponentTypeCombination (
  id              SMALLINT NOT NULL AUTO_INCREMENT,
  combinationName VARCHAR(300),
  isDeleted       BOOLEAN,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE ComponentTypeCombination_AUD (
  id              SMALLINT NOT NULL,
  REV             INTEGER  NOT NULL,
  REVTYPE         TINYINT,
  combinationName VARCHAR(300),
  isDeleted       BOOLEAN,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE ComponentTypeCombination_ComponentType (
  id                           SMALLINT NOT NULL AUTO_INCREMENT,
  componentTypeCombinations_id SMALLINT NOT NULL,
  componentTypes_id            SMALLINT NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE ComponentType_AUD (
  id                     SMALLINT NOT NULL,
  REV                    INTEGER  NOT NULL,
  REVTYPE                TINYINT,
  description            LONGTEXT,
  expiresAfter           INTEGER,
  expiresAfterUnits      VARCHAR(30),
  hasBloodGroup          BOOLEAN,
  isDeleted              BOOLEAN,
  componentTypeName      VARCHAR(50),
  componentTypeNameShort VARCHAR(30),
  pediComponentType_id   SMALLINT,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE ComponentUsage (
  id               BIGINT NOT NULL AUTO_INCREMENT,
  hospital         VARCHAR(50),
  isDeleted        BOOLEAN,
  createdDate      TIMESTAMP,
  lastUpdated      TIMESTAMP,
  notes            LONGTEXT,
  patientName      VARCHAR(50),
  usageDate        DATETIME,
  useIndication    VARCHAR(50),
  usedBy           VARCHAR(30),
  ward             VARCHAR(30),
  createdBy_id     SMALLINT,
  lastUpdatedBy_id SMALLINT,
  component_id     BIGINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE ComponentUsage_AUD (
  id               BIGINT  NOT NULL,
  REV              INTEGER NOT NULL,
  REVTYPE          TINYINT,
  hospital         VARCHAR(50),
  isDeleted        BOOLEAN,
  createdDate      TIMESTAMP,
  lastUpdated      TIMESTAMP,
  notes            LONGTEXT,
  patientName      VARCHAR(50),
  usageDate        DATETIME,
  useIndication    VARCHAR(50),
  usedBy           VARCHAR(30),
  ward             VARCHAR(30),
  createdBy_id     SMALLINT,
  lastUpdatedBy_id SMALLINT,
  component_id     BIGINT,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE Component_AUD (
  id                 BIGINT  NOT NULL,
  REV                INTEGER NOT NULL,
  REVTYPE            TINYINT,
  createdOn          DATETIME,
  discardedOn        DATETIME,
  expiresOn          DATETIME,
  isDeleted          BOOLEAN,
  issuedOn           DATETIME,
  createdDate        TIMESTAMP,
  lastUpdated        TIMESTAMP,
  notes              LONGTEXT,
  status             VARCHAR(30),
  subdivisionCode    VARCHAR(3),
  donation_id        BIGINT,
  issuedTo_id        BIGINT,
  createdBy_id       SMALLINT,
  lastUpdatedBy_id   SMALLINT,
  parentComponent_id BIGINT,
  componentType_id   SMALLINT,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE AuditRevision (
  id        INTEGER NOT NULL AUTO_INCREMENT,
  timestamp BIGINT,
  username  VARCHAR(30),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE EntityModification (
  id               INTEGER     NOT NULL AUTO_INCREMENT,
  auditRevision_id INTEGER     NOT NULL,
  revisionType     VARCHAR(30) NOT NULL,
  entityName       VARCHAR(30) NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE Request (
  id                BIGINT NOT NULL AUTO_INCREMENT,
  department        VARCHAR(30),
  fulfilled         BOOLEAN,
  hospital          VARCHAR(30),
  indicationForUse  VARCHAR(50),
  isDeleted         BOOLEAN,
  createdDate       TIMESTAMP,
  lastUpdated       TIMESTAMP,
  notes             LONGTEXT,
  numUnitsIssued    INTEGER,
  numUnitsRequested INTEGER,
  patientAge        INTEGER,
  patientBirthDate  DATE,
  patientBloodAbo   VARCHAR(30),
  patientBloodRh    VARCHAR(30),
  patientDiagnosis  VARCHAR(100),
  patientFirstName  VARCHAR(30),
  patientGender     INTEGER,
  patientLastName   VARCHAR(30),
  patientNumber     VARCHAR(30),
  requestDate       DATETIME,
  requestNumber     VARCHAR(20) UNIQUE,
  requestedBy       VARCHAR(30),
  requiredDate      DATETIME,
  ward              VARCHAR(20),
  createdBy_id      SMALLINT,
  lastUpdatedBy_id  SMALLINT,
  componentType_id  SMALLINT,
  requestSite_id    BIGINT,
  requestType_id    SMALLINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE RequestType (
  id          SMALLINT NOT NULL AUTO_INCREMENT,
  description VARCHAR(100),
  isDeleted   BOOLEAN,
  requestType VARCHAR(30),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE RequestType_AUD (
  id          SMALLINT NOT NULL,
  REV         INTEGER  NOT NULL,
  REVTYPE     TINYINT,
  description VARCHAR(100),
  isDeleted   BOOLEAN,
  requestType VARCHAR(30),
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE Request_AUD (
  id                BIGINT  NOT NULL,
  REV               INTEGER NOT NULL,
  REVTYPE           TINYINT,
  department        VARCHAR(30),
  fulfilled         BOOLEAN,
  hospital          VARCHAR(30),
  indicationForUse  VARCHAR(50),
  isDeleted         BOOLEAN,
  createdDate       TIMESTAMP,
  lastUpdated       TIMESTAMP,
  notes             LONGTEXT,
  numUnitsIssued    INTEGER,
  numUnitsRequested INTEGER,
  patientAge        INTEGER,
  patientBirthDate  DATE,
  patientBloodAbo   VARCHAR(30),
  patientBloodRh    VARCHAR(30),
  patientDiagnosis  VARCHAR(100),
  patientFirstName  VARCHAR(30),
  patientGender     INTEGER,
  patientLastName   VARCHAR(30),
  patientNumber     VARCHAR(30),
  requestDate       DATETIME,
  requestNumber     VARCHAR(20),
  requestedBy       VARCHAR(30),
  requiredDate      DATETIME,
  ward              VARCHAR(20),
  createdBy_id      SMALLINT,
  lastUpdatedBy_id  SMALLINT,
  componentType_id  SMALLINT,
  requestSite_id    BIGINT,
  requestType_id    SMALLINT,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE Role (
  id   BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE Role_AUD (
  id      BIGINT  NOT NULL,
  REV     INTEGER NOT NULL,
  REVTYPE TINYINT,
  name    VARCHAR(50),
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE SequenceNumberStore (
  id                    INTEGER NOT NULL AUTO_INCREMENT,
  columnName            VARCHAR(50),
  lastNumber            BIGINT,
  prefix                VARCHAR(5),
  sequenceNumberContext VARCHAR(5),
  targetTable           VARCHAR(50),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE Tips (
  tipsKey     VARCHAR(255) NOT NULL,
  isDeleted   BOOLEAN,
  tipsContent VARCHAR(1000),
  tipsName    VARCHAR(255),
  PRIMARY KEY (tipsKey)
)
  ENGINE = InnoDB;

CREATE TABLE User (
  id        SMALLINT     NOT NULL AUTO_INCREMENT,
  emailId   VARCHAR(255),
  firstName VARCHAR(20)  NOT NULL,
  isActive  BOOLEAN,
  isAdmin   BOOLEAN,
  isDeleted BOOLEAN,
  isStaff   BOOLEAN,
  lastLogin DATETIME,
  lastName  VARCHAR(20),
  notes     LONGTEXT,
  password  VARCHAR(255) NOT NULL,
  username  VARCHAR(30)  NOT NULL UNIQUE,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE User_AUD (
  id        SMALLINT NOT NULL,
  REV       INTEGER  NOT NULL,
  REVTYPE   TINYINT,
  emailId   VARCHAR(255),
  firstName VARCHAR(20),
  isActive  BOOLEAN,
  isAdmin   BOOLEAN,
  isDeleted BOOLEAN,
  isStaff   BOOLEAN,
  lastLogin DATETIME,
  lastName  VARCHAR(20),
  notes     LONGTEXT,
  password  VARCHAR(255),
  username  VARCHAR(30),
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE User_Role (
  users_id SMALLINT NOT NULL,
  roles_id BIGINT   NOT NULL
)
  ENGINE = InnoDB;

CREATE TABLE User_Role_AUD (
  REV      INTEGER  NOT NULL,
  users_id SMALLINT NOT NULL,
  roles_id BIGINT   NOT NULL,
  REVTYPE  TINYINT,
  PRIMARY KEY (REV, users_id, roles_id)
)
  ENGINE = InnoDB;

CREATE TABLE WellType (
  id             SMALLINT NOT NULL AUTO_INCREMENT,
  isDeleted      BOOLEAN,
  requiresSample BOOLEAN,
  wellType       VARCHAR(30),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE WellType_AUD (
  id             SMALLINT NOT NULL,
  REV            INTEGER  NOT NULL,
  REVTYPE        TINYINT,
  isDeleted      BOOLEAN,
  requiresSample BOOLEAN,
  wellType       VARCHAR(30),
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE Worksheet (
  id               BIGINT NOT NULL AUTO_INCREMENT,
  isDeleted        BOOLEAN,
  createdDate      TIMESTAMP,
  lastUpdated      TIMESTAMP,
  notes            LONGTEXT,
  worksheetNumber  VARCHAR(20) UNIQUE,
  createdBy_id     SMALLINT,
  lastUpdatedBy_id SMALLINT,
  worksheetType_id SMALLINT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE WorksheetType (
  id            SMALLINT NOT NULL AUTO_INCREMENT,
  context       VARCHAR(30),
  isDeleted     BOOLEAN,
  worksheetType VARCHAR(30),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE WorksheetType_AUD (
  id            SMALLINT NOT NULL,
  REV           INTEGER  NOT NULL,
  REVTYPE       TINYINT,
  context       VARCHAR(30),
  isDeleted     BOOLEAN,
  worksheetType VARCHAR(30),
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE Worksheet_AUD (
  id               BIGINT  NOT NULL,
  REV              INTEGER NOT NULL,
  REVTYPE          TINYINT,
  isDeleted        BOOLEAN,
  createdDate      TIMESTAMP,
  lastUpdated      TIMESTAMP,
  notes            LONGTEXT,
  worksheetNumber  VARCHAR(20),
  createdBy_id     SMALLINT,
  lastUpdatedBy_id SMALLINT,
  worksheetType_id SMALLINT,
  PRIMARY KEY (id, REV)
)
  ENGINE = InnoDB;

CREATE TABLE Worksheet_Donation (
  worksheets_id BIGINT NOT NULL,
  donations_id  BIGINT NOT NULL,
  PRIMARY KEY (worksheets_id, donations_id)
)
  ENGINE = InnoDB;

ALTER TABLE PackType_AUD
ADD INDEX FK_PACK_TYPE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_PACK_TYPE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE BloodTestResult
ADD INDEX FK39946CC9A49787C4 (createdBy_id),
ADD CONSTRAINT FK39946CC9A49787C4
FOREIGN KEY (createdBy_id)
REFERENCES User (id);

ALTER TABLE BloodTestResult
ADD INDEX FK39946CC932E145A (donation_id),
ADD CONSTRAINT FK39946CC932E145A
FOREIGN KEY (donation_id)
REFERENCES Donation (id);

ALTER TABLE BloodTestResult
ADD INDEX FK39946CC9E104121F (machineReading_id),
ADD CONSTRAINT FK39946CC9E104121F
FOREIGN KEY (machineReading_id)
REFERENCES MachineReading (id);

ALTER TABLE BloodTestResult
ADD INDEX FK39946CC9D0AFB367 (lastUpdatedBy_id),
ADD CONSTRAINT FK39946CC9D0AFB367
FOREIGN KEY (lastUpdatedBy_id)
REFERENCES User (id);

ALTER TABLE BloodTestResult
ADD INDEX FK39946CC945027987 (bloodTest_id),
ADD CONSTRAINT FK39946CC945027987
FOREIGN KEY (bloodTest_id)
REFERENCES BloodTest (id);

ALTER TABLE BloodTest_AUD
ADD INDEX FK_BLOOD_TEST_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_BLOOD_TEST_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE BloodTest_WorksheetType
ADD INDEX FK7A6DA3B518CB61F2 (worksheetTypes_id),
ADD CONSTRAINT FK7A6DA3B518CB61F2
FOREIGN KEY (worksheetTypes_id)
REFERENCES WorksheetType (id);

ALTER TABLE BloodTest_WorksheetType
ADD INDEX FK7A6DA3B5BC5F90CC (bloodTests_id),
ADD CONSTRAINT FK7A6DA3B5BC5F90CC
FOREIGN KEY (bloodTests_id)
REFERENCES BloodTest (id);

ALTER TABLE BloodTest_WorksheetType_AUD
ADD INDEX FK_BLOOD_TEST_WORKSHEET_TYPE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_BLOOD_TEST_WORKSHEET_TYPE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE BloodTestingRule_AUD
ADD INDEX FK_BLOOD_TESTING_RULE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_BLOOD_TESTING_RULE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

CREATE INDEX donation_donationDate_index ON Donation (donationDate);

CREATE INDEX donation_donationIdentificationNumber_index ON Donation (donationIdentificationNumber);

ALTER TABLE Donation
ADD INDEX FKF0658A33A49787C4 (createdBy_id),
ADD CONSTRAINT FKF0658A33A49787C4
FOREIGN KEY (createdBy_id)
REFERENCES User (id);

ALTER TABLE Donation
ADD INDEX FKF0658A33D04A4456 (donationCreatedBy_id),
ADD CONSTRAINT FKF0658A33D04A4456
FOREIGN KEY (donationCreatedBy_id)
REFERENCES User (id);

ALTER TABLE Donation
ADD INDEX FKF0658A33E5D4FEA3 (donationBatch_id),
ADD CONSTRAINT FKF0658A33E5D4FEA3
FOREIGN KEY (donationBatch_id)
REFERENCES DonationBatch (id);

ALTER TABLE Donation
ADD INDEX FKF0658A3359FAB30D (donor_id),
ADD CONSTRAINT FKF0658A3359FAB30D
FOREIGN KEY (donor_id)
REFERENCES Donor (id);

ALTER TABLE Donation
ADD INDEX FKF0658A338461A8D7 (donationType_id),
ADD CONSTRAINT FKF0658A338461A8D7
FOREIGN KEY (donationType_id)
REFERENCES DonationType (id);

ALTER TABLE Donation
ADD INDEX FKF0658A33D0AFB367 (lastUpdatedBy_id),
ADD CONSTRAINT FKF0658A33D0AFB367
FOREIGN KEY (lastUpdatedBy_id)
REFERENCES User (id);

ALTER TABLE Donation
ADD INDEX FKF0658A331D73927B (packType_id),
ADD CONSTRAINT FKF0658A331D73927B
FOREIGN KEY (packType_id)
REFERENCES PackType (id);

ALTER TABLE Donation_AUD
ADD INDEX FK_DONATION_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_DONATION_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE DonationBatch
ADD INDEX FK227631CA49787C4 (createdBy_id),
ADD CONSTRAINT FK227631CA49787C4
FOREIGN KEY (createdBy_id)
REFERENCES User (id);

ALTER TABLE DonationBatch
ADD INDEX FK227631CD0AFB367 (lastUpdatedBy_id),
ADD CONSTRAINT FK227631CD0AFB367
FOREIGN KEY (lastUpdatedBy_id)
REFERENCES User (id);

ALTER TABLE DonationBatch_AUD
ADD INDEX FK_DONATION_BATCH_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_DONATION_BATCH_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

CREATE INDEX compatibilityTest_crossmatchTestDate_index ON CompatibilityTest (compatibililityTestDate);

ALTER TABLE CompatibilityTest
ADD INDEX FK92798602A49787C4 (createdBy_id),
ADD CONSTRAINT FK92798602A49787C4
FOREIGN KEY (createdBy_id)
REFERENCES User (id);

ALTER TABLE CompatibilityTest
ADD INDEX FK92798602D4061B9F (forRequest_id),
ADD CONSTRAINT FK92798602D4061B9F
FOREIGN KEY (forRequest_id)
REFERENCES Request (id);

ALTER TABLE CompatibilityTest
ADD INDEX FK92798602EFD1FE7 (testedComponent_id),
ADD CONSTRAINT FK92798602EFD1FE7
FOREIGN KEY (testedComponent_id)
REFERENCES Component (id);

ALTER TABLE CompatibilityTest
ADD INDEX FK92798602D0AFB367 (lastUpdatedBy_id),
ADD CONSTRAINT FK92798602D0AFB367
FOREIGN KEY (lastUpdatedBy_id)
REFERENCES User (id);

ALTER TABLE CompatibilityTest
ADD INDEX FK927986028631CA7D (crossmatchType_id),
ADD CONSTRAINT FK927986028631CA7D
FOREIGN KEY (crossmatchType_id)
REFERENCES CrossmatchType (id);

ALTER TABLE CompatibilityTest_AUD
ADD INDEX FK_COMPATIBILITY_TEST_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_COMPATIBILITY_TEST_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE ContactMethodType_AUD
ADD INDEX FK_CONTACT_METHOD_TYPE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_CONTACT_METHOD_TYPE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE CrossmatchType_AUD
ADD INDEX FK_CROSSMATCH_TYPE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_CROSSMATCH_TYPE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE DeferralReason_AUD
ADD INDEX FK_DEFERRAL_REASON_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_DEFERRAL_REASON_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE DonationType_AUD
ADD INDEX FK_DONATION_TYPE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_DONATION_TYPE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

CREATE INDEX donor_donorNumber_index ON Donor (donorNumber);

CREATE INDEX donor_lastName_index ON Donor (lastName);

CREATE INDEX donor_firstName_index ON Donor (firstName);

ALTER TABLE Donor
ADD INDEX FK3F25E46A49787C4 (createdBy_id),
ADD CONSTRAINT FK3F25E46A49787C4
FOREIGN KEY (createdBy_id)
REFERENCES User (id);

ALTER TABLE Donor
ADD INDEX FK3F25E463043805 (donorPanel_id),
ADD CONSTRAINT FK3F25E463043805
FOREIGN KEY (donorPanel_id)
REFERENCES Location (id);

ALTER TABLE Donor
ADD INDEX FK3F25E46FCE9E976 (preferredContactMethod_id),
ADD CONSTRAINT FK3F25E46FCE9E976
FOREIGN KEY (preferredContactMethod_id)
REFERENCES ContactMethodType (id);

ALTER TABLE Donor
ADD INDEX FK3F25E46D0AFB367 (lastUpdatedBy_id),
ADD CONSTRAINT FK3F25E46D0AFB367
FOREIGN KEY (lastUpdatedBy_id)
REFERENCES User (id);

ALTER TABLE DonorDeferral
ADD INDEX FKC7E323D193E6BEC (deferredDonor_id),
ADD CONSTRAINT FKC7E323D193E6BEC
FOREIGN KEY (deferredDonor_id)
REFERENCES Donor (id);

ALTER TABLE DonorDeferral
ADD INDEX FKC7E323D1ED3A012D (createdBy_id),
ADD CONSTRAINT FKC7E323D1ED3A012D
FOREIGN KEY (createdBy_id)
REFERENCES User (id);

ALTER TABLE DonorDeferral
ADD INDEX FKC7E323D1C9CCBBFC (deferralReason_id),
ADD CONSTRAINT FKC7E323D1C9CCBBFC
FOREIGN KEY (deferralReason_id)
REFERENCES DeferralReason (id);

ALTER TABLE DonorDeferral_AUD
ADD INDEX FK_DONOR_DEFERRAL_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_DONOR_DEFERRAL_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE Donor_AUD
ADD INDEX FK_DONOR_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_DONOR_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE Location_AUD
ADD INDEX FK_LOCATION_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_LOCATION_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE MachineReading
ADD INDEX FK4CF2E0652531C00D (wellType_id),
ADD CONSTRAINT FK4CF2E0652531C00D
FOREIGN KEY (wellType_id)
REFERENCES WellType (id);

ALTER TABLE MachineReading
ADD INDEX FK4CF2E065AF35157F (plateSession_id),
ADD CONSTRAINT FK4CF2E065AF35157F
FOREIGN KEY (plateSession_id)
REFERENCES PlateSession (id);

ALTER TABLE MicrotiterPlate_AUD
ADD INDEX FK_MICROTITER_PLATE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_MICROTITER_PLATE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE Permission_AUD
ADD INDEX FK_PERMISSION_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_PERMISSION_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE Permission_Role
ADD INDEX FK63DCE8E61D2E2631 (roles_id),
ADD CONSTRAINT FK63DCE8E61D2E2631
FOREIGN KEY (roles_id)
REFERENCES Role (id);

ALTER TABLE Permission_Role
ADD INDEX FK63DCE8E612647DE3 (permissions_id),
ADD CONSTRAINT FK63DCE8E612647DE3
FOREIGN KEY (permissions_id)
REFERENCES Permission (id);

ALTER TABLE Permission_Role_AUD
ADD INDEX FK_PERMISSION_ROLE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_PERMISSION_ROLE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE PlateSession_AUD
ADD INDEX FK_PLATE_SESSION_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_PLATE_SESSION_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE PlateSession_BloodTestResult
ADD INDEX FK7A75514AAF35157F (PlateSession_id),
ADD CONSTRAINT FK7A75514AAF35157F
FOREIGN KEY (PlateSession_id)
REFERENCES PlateSession (id);

ALTER TABLE PlateSession_BloodTestResult
ADD INDEX FK7A75514A2DA883C0 (bloodTestsOnPlate_id),
ADD CONSTRAINT FK7A75514A2DA883C0
FOREIGN KEY (bloodTestsOnPlate_id)
REFERENCES BloodTestResult (id);

CREATE INDEX component_expiresOn_index ON Component (expiresOn);

ALTER TABLE Component
ADD INDEX FK50C664CFA49787C4 (createdBy_id),
ADD CONSTRAINT FK50C664CFA49787C4
FOREIGN KEY (createdBy_id)
REFERENCES User (id);

ALTER TABLE Component
ADD INDEX FK50C664CF994002DF (issuedTo_id),
ADD CONSTRAINT FK50C664CF994002DF
FOREIGN KEY (issuedTo_id)
REFERENCES Request (id);

ALTER TABLE Component
ADD INDEX FK50C664CF32E145A (donation_id),
ADD CONSTRAINT FK50C664CF32E145A
FOREIGN KEY (donation_id)
REFERENCES Donation (id);

ALTER TABLE Component
ADD INDEX FK50C664CF73AC2B90 (componentType_id),
ADD CONSTRAINT FK50C664CF73AC2B90
FOREIGN KEY (componentType_id)
REFERENCES ComponentType (id);

ALTER TABLE Component
ADD INDEX FK50C664CFD946D0A0 (parentComponent_id),
ADD CONSTRAINT FK50C664CFD946D0A0
FOREIGN KEY (parentComponent_id)
REFERENCES Component (id);

ALTER TABLE Component
ADD INDEX FK50C664CFD0AFB367 (lastUpdatedBy_id),
ADD CONSTRAINT FK50C664CFD0AFB367
FOREIGN KEY (lastUpdatedBy_id)
REFERENCES User (id);

ALTER TABLE ComponentStatusChange
ADD INDEX FKCCE48CB1994002DF (issuedTo_id),
ADD CONSTRAINT FKCCE48CB1994002DF
FOREIGN KEY (issuedTo_id)
REFERENCES Request (id);

ALTER TABLE ComponentStatusChange
ADD INDEX FKCCE48CB18BFC6394 (statusChangeReason_id),
ADD CONSTRAINT FKCCE48CB18BFC6394
FOREIGN KEY (statusChangeReason_id)
REFERENCES ComponentStatusChangeReason (id);

ALTER TABLE ComponentStatusChange
ADD INDEX FKCCE48CB1A8E71476 (component_id),
ADD CONSTRAINT FKCCE48CB1A8E71476
FOREIGN KEY (component_id)
REFERENCES Component (id);

ALTER TABLE ComponentStatusChange
ADD INDEX FKCCE48CB1438D2378 (changedBy_id),
ADD CONSTRAINT FKCCE48CB1438D2378
FOREIGN KEY (changedBy_id)
REFERENCES User (id);

ALTER TABLE ComponentStatusChangeReason_AUD
ADD INDEX FK_COMPONENT_STATUS_CHANGE_REASON_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_COMPONENT_STATUS_CHANGE_REASON_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE ComponentStatusChange_AUD
ADD INDEX FK_COMPONENT_STATUS_CHANGE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_COMPONENT_STATUS_CHANGE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE ComponentType
ADD INDEX FKA8168A93E7CEF4A (pediComponentType_id),
ADD CONSTRAINT FKA8168A93E7CEF4A
FOREIGN KEY (pediComponentType_id)
REFERENCES ComponentType (id);

ALTER TABLE ComponentTypeCombination_AUD
ADD INDEX FK_COMPONENT_TYPE_COMBINATION_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_COMPONENT_TYPE_COMBINATION_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE ComponentTypeCombination_ComponentType
ADD INDEX FK3F6704507548F61D (componentTypeCombinations_id),
ADD CONSTRAINT FK3F6704507548F61D
FOREIGN KEY (componentTypeCombinations_id)
REFERENCES ComponentTypeCombination (id);

ALTER TABLE ComponentTypeCombination_ComponentType
ADD INDEX FK3F670450EE5B3BAF (componentTypes_id),
ADD CONSTRAINT FK3F670450EE5B3BAF
FOREIGN KEY (componentTypes_id)
REFERENCES ComponentType (id);

ALTER TABLE ComponentType_AUD
ADD INDEX FK_COMPONENT_TYPE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_COMPONENT_TYPE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE ComponentUsage
ADD INDEX FK45B6D212A49787C4 (createdBy_id),
ADD CONSTRAINT FK45B6D212A49787C4
FOREIGN KEY (createdBy_id)
REFERENCES User (id);

ALTER TABLE ComponentUsage
ADD INDEX FK45B6D212A8E71476 (component_id),
ADD CONSTRAINT FK45B6D212A8E71476
FOREIGN KEY (component_id)
REFERENCES Component (id);

ALTER TABLE ComponentUsage
ADD INDEX FK45B6D212D0AFB367 (lastUpdatedBy_id),
ADD CONSTRAINT FK45B6D212D0AFB367
FOREIGN KEY (lastUpdatedBy_id)
REFERENCES User (id);

ALTER TABLE ComponentUsage_AUD
ADD INDEX FK_COMPONENT_USAGE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_COMPONENT_USAGE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE Component_AUD
ADD INDEX FK_COMPONENT_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_COMPONENT_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

CREATE INDEX request_requiredDate_index ON Request (requiredDate);

CREATE INDEX request_requestDate_index ON Request (requestDate);

CREATE INDEX request_bloodRhd_index ON Request (patientBloodRh);

CREATE INDEX request_requestNumber_index ON Request (requestNumber);

CREATE INDEX request_bloodAbo_index ON Request (patientBloodAbo);

ALTER TABLE Request
ADD INDEX FKA4878A6FA49787C4 (createdBy_id),
ADD CONSTRAINT FKA4878A6FA49787C4
FOREIGN KEY (createdBy_id)
REFERENCES User (id);

ALTER TABLE Request
ADD INDEX FKA4878A6F1520E0D (requestSite_id),
ADD CONSTRAINT FKA4878A6F1520E0D
FOREIGN KEY (requestSite_id)
REFERENCES Location (id);

ALTER TABLE Request
ADD INDEX FKA4878A6F73AC2B90 (componentType_id),
ADD CONSTRAINT FKA4878A6F73AC2B90
FOREIGN KEY (componentType_id)
REFERENCES ComponentType (id);

ALTER TABLE Request
ADD INDEX FKA4878A6F537AAD30 (requestType_id),
ADD CONSTRAINT FKA4878A6F537AAD30
FOREIGN KEY (requestType_id)
REFERENCES RequestType (id);

ALTER TABLE Request
ADD INDEX FKA4878A6FD0AFB367 (lastUpdatedBy_id),
ADD CONSTRAINT FKA4878A6FD0AFB367
FOREIGN KEY (lastUpdatedBy_id)
REFERENCES User (id);

ALTER TABLE RequestType_AUD
ADD INDEX FK_REQUEST_TYPE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_REQUEST_TYPE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE Request_AUD
ADD INDEX FK_REQUEST_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_REQUEST_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE Role_AUD
ADD INDEX FK_ROLE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_ROLE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE User_AUD
ADD INDEX FK_USER_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_USER_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE User_Role
ADD INDEX FK8B9F886A1D2E2631 (roles_id),
ADD CONSTRAINT FK8B9F886A1D2E2631
FOREIGN KEY (roles_id)
REFERENCES Role (id);

ALTER TABLE User_Role
ADD INDEX FK8B9F886A1D314A5B (users_id),
ADD CONSTRAINT FK8B9F886A1D314A5B
FOREIGN KEY (users_id)
REFERENCES User (id);

ALTER TABLE User_Role_AUD
ADD INDEX FK_USER_ROLE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_USER_ROLE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE WellType_AUD
ADD INDEX FK_WELL_TYPE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_WELL_TYPE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE Worksheet
ADD INDEX FKB98678CEA49787C4 (createdBy_id),
ADD CONSTRAINT FKB98678CEA49787C4
FOREIGN KEY (createdBy_id)
REFERENCES User (id);

ALTER TABLE Worksheet
ADD INDEX FKB98678CEBC88FBF5 (worksheetType_id),
ADD CONSTRAINT FKB98678CEBC88FBF5
FOREIGN KEY (worksheetType_id)
REFERENCES WorksheetType (id);

ALTER TABLE Worksheet
ADD INDEX FKB98678CED0AFB367 (lastUpdatedBy_id),
ADD CONSTRAINT FKB98678CED0AFB367
FOREIGN KEY (lastUpdatedBy_id)
REFERENCES User (id);

ALTER TABLE WorksheetType_AUD
ADD INDEX FK_WORKSHEET_TYPE_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_WORKSHEET_TYPE_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE Worksheet_AUD
ADD INDEX FK_WORKSHEET_AUDIT_REVISION (REV),
ADD CONSTRAINT FK_WORKSHEET_AUDIT_REVISION
FOREIGN KEY (REV)
REFERENCES AuditRevision (id);

ALTER TABLE Worksheet_Donation
ADD INDEX FK1BCDFCC2EA518FDE (worksheets_id),
ADD CONSTRAINT FK1BCDFCC2EA518FDE
FOREIGN KEY (worksheets_id)
REFERENCES Worksheet (id);

ALTER TABLE Worksheet_Donation
ADD INDEX FK1BCDFCC2C02466CD (donations_id),
ADD CONSTRAINT FK1BCDFCC2C02466CD
FOREIGN KEY (donations_id)
REFERENCES Donation (id);
        
   
        
