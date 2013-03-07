
    alter table BloodTest_BloodTest 
        drop 
        foreign key FK5EC59E995FA14D0E;

    alter table BloodTest_BloodTest 
        drop 
        foreign key FK5EC59E993A6D02C3;

    alter table BloodTest_BloodTest 
        drop 
        foreign key FK5EC59E99F2F53069;

    alter table CollectedSample 
        drop 
        foreign key FKF0658A33A49787C4;

    alter table CollectedSample 
        drop 
        foreign key FKF0658A33D04A4456;

    alter table CollectedSample 
        drop 
        foreign key FKF0658A33AED1731E;

    alter table CollectedSample 
        drop 
        foreign key FKF0658A33B29562D0;

    alter table CollectedSample 
        drop 
        foreign key FKF0658A3359FAB30D;

    alter table CollectedSample 
        drop 
        foreign key FKF0658A3392C64B83;

    alter table CollectedSample 
        drop 
        foreign key FKF0658A33D0AFB367;

    alter table CollectedSample 
        drop 
        foreign key FKF0658A33675D568F;

    alter table CollectedSample 
        drop 
        foreign key FKF0658A331D73927B;

    alter table CollectedSample_CollectionsWorksheet 
        drop 
        foreign key FKB39FFD85225909B3;

    alter table CollectedSample_CollectionsWorksheet 
        drop 
        foreign key FKB39FFD85C02466CD;

    alter table CollectionsWorksheet 
        drop 
        foreign key FK72E3FEF9A49787C4;

    alter table CollectionsWorksheet 
        drop 
        foreign key FK72E3FEF9D0AFB367;

    alter table CompatibilityTest 
        drop 
        foreign key FK92798602A49787C4;

    alter table CompatibilityTest 
        drop 
        foreign key FK92798602D4061B9F;

    alter table CompatibilityTest 
        drop 
        foreign key FK92798602EFD1FE7;

    alter table CompatibilityTest 
        drop 
        foreign key FK92798602D0AFB367;

    alter table CompatibilityTest 
        drop 
        foreign key FK927986028631CA7D;

    alter table Donor 
        drop 
        foreign key FK3F25E46A49787C4;

    alter table Donor 
        drop 
        foreign key FK3F25E46D0AFB367;

    alter table Product 
        drop 
        foreign key FK50C664CFA49787C4;

    alter table Product 
        drop 
        foreign key FK50C664CF994002DF;

    alter table Product 
        drop 
        foreign key FK50C664CF32E145A;

    alter table Product 
        drop 
        foreign key FK50C664CF73AC2B90;

    alter table Product 
        drop 
        foreign key FK50C664CFFDFE9E0F;

    alter table Product 
        drop 
        foreign key FK50C664CFD0AFB367;

    alter table ProductIssue 
        drop 
        foreign key FK450DFD4A994002DF;

    alter table ProductIssue 
        drop 
        foreign key FK450DFD4A9F8F75E1;

    alter table ProductIssue 
        drop 
        foreign key FK450DFD4AA8E71476;

    alter table ProductReturn 
        drop 
        foreign key FK6B4876DF61638630;

    alter table ProductReturn 
        drop 
        foreign key FK6B4876DFA8E71476;

    alter table ProductReturn 
        drop 
        foreign key FK6B4876DFC1F8813D;

    alter table ProductReturn 
        drop 
        foreign key FK6B4876DFC7A8C5;

    alter table ProductUsage 
        drop 
        foreign key FK45B6D212A49787C4;

    alter table ProductUsage 
        drop 
        foreign key FK45B6D212A8E71476;

    alter table ProductUsage 
        drop 
        foreign key FK45B6D212D0AFB367;

    alter table Request 
        drop 
        foreign key FKA4878A6FA49787C4;

    alter table Request 
        drop 
        foreign key FKA4878A6F1520E0D;

    alter table Request 
        drop 
        foreign key FKA4878A6F73AC2B90;

    alter table Request 
        drop 
        foreign key FKA4878A6F537AAD30;

    alter table Request 
        drop 
        foreign key FKA4878A6FD0AFB367;

    alter table TestResult 
        drop 
        foreign key FKDB459F6FA49787C4;

    alter table TestResult 
        drop 
        foreign key FKDB459F6F32E145A;

    alter table TestResult 
        drop 
        foreign key FKDB459F6F3A6D02C3;

    alter table TestResult 
        drop 
        foreign key FKDB459F6FD0AFB367;

    drop table if exists BloodBagType;

    drop table if exists BloodTest;

    drop table if exists BloodTest_BloodTest;

    drop table if exists CollectedSample;

    drop table if exists CollectedSample_CollectionsWorksheet;

    drop table if exists CollectionsWorksheet;

    drop table if exists CompatibilityTest;

    drop table if exists CrossmatchType;

    drop table if exists DonationBatch;

    drop table if exists DonationType;

    drop table if exists Donor;

    drop table if exists FormField;

    drop table if exists GenericConfig;

    drop table if exists Location;

    drop table if exists LocationType;

    drop table if exists Product;

    drop table if exists ProductDiscardReason;

    drop table if exists ProductIssue;

    drop table if exists ProductReturn;

    drop table if exists ProductReturnReason;

    drop table if exists ProductType;

    drop table if exists ProductUsage;

    drop table if exists Request;

    drop table if exists RequestType;

    drop table if exists SequenceNumberStore;

    drop table if exists TestResult;

    drop table if exists Tips;

    drop table if exists User;

    create table BloodBagType (
        id integer not null auto_increment,
        bloodBagType varchar(50),
        isDeleted boolean,
        primary key (id)
    ) ENGINE=InnoDB;

    create table BloodTest (
        name varchar(30) not null,
        allowedResults varchar(255),
        correctResult varchar(255),
        displayName varchar(255),
        isConfidential boolean,
        isDeleted boolean,
        isFinalOutcome boolean,
        isRequired boolean,
        notes longtext,
        primary key (name)
    ) ENGINE=InnoDB;

    create table BloodTest_BloodTest (
        BloodTest_name varchar(30) not null,
        bloodTestsIfIncorrect_name varchar(30) not null,
        bloodTestsIfCorrect_name varchar(30) not null
    ) ENGINE=InnoDB;

    create table CollectedSample (
        id bigint not null auto_increment,
        bloodPressure decimal(6,2),
        collectedOn datetime,
        collectionNumber varchar(30),
        donorWeight decimal(6,2),
        haemoglobinCount decimal(6,2),
        isDeleted boolean,
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        testedStatus varchar(20),
        bloodBagType_id integer,
        collectionCenter_id bigint,
        collectionSite_id bigint,
        donationBatch_id bigint,
        donationCreatedBy_id bigint,
        donor_id bigint,
        donorType_id integer,
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table CollectedSample_CollectionsWorksheet (
        collectedSamples_id bigint not null,
        worksheets_id bigint not null
    ) ENGINE=InnoDB;

    create table CollectionsWorksheet (
        id bigint not null auto_increment,
        createdDate datetime,
        lastUpdated datetime,
        worksheetBatchId varchar(255),
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table CompatibilityTest (
        id bigint not null auto_increment,
        compatibililityTestDate datetime,
        compatibilityResult integer,
        isDeleted boolean,
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        testedBy varchar(255),
        transfusedBefore boolean,
        crossmatchType_id integer,
        forRequest_id bigint,
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        testedProduct_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table CrossmatchType (
        id integer not null auto_increment,
        crossmatchType varchar(255),
        isDeleted boolean,
        primary key (id)
    ) ENGINE=InnoDB;

    create table DonationBatch (
        id bigint not null auto_increment,
        batchNumber varchar(30),
        collectionNumberBegin varchar(30),
        collectionNumberEnd varchar(255),
        notes longtext,
        primary key (id)
    ) ENGINE=InnoDB;

    create table DonationType (
        id integer not null auto_increment,
        donationType varchar(50),
        isDeleted boolean,
        primary key (id)
    ) ENGINE=InnoDB;

    create table Donor (
        id bigint not null auto_increment,
        birthDate date,
        bloodAbo varchar(3),
        bloodRhd varchar(3),
        callingName varchar(30),
        address varchar(255),
        city varchar(50),
        country varchar(50),
        district varchar(50),
        phoneNumber varchar(50),
        province varchar(50),
        state varchar(50),
        zipcode varchar(10),
        donorNumber varchar(30),
        firstName varchar(30),
        gender varchar(15),
        isDeleted boolean,
        lastName varchar(30),
        middleName varchar(30),
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table FormField (
        id bigint not null auto_increment,
        autoGenerate boolean,
        canBeOptional boolean,
        defaultDisplayName varchar(30),
        defaultValue longtext,
        displayName varchar(30),
        field varchar(30),
        form varchar(30),
        hidden boolean,
        isAutoGeneratable boolean,
        isHidable boolean,
        isRequired boolean,
        maxLength integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create table GenericConfig (
        id bigint not null auto_increment,
        propertyName varchar(30),
        propertyOwner varchar(30),
        propertyValue varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table Location (
        id bigint not null auto_increment,
        isCenter boolean,
        isCollectionSite boolean,
        isDeleted boolean,
        isMobileSite boolean,
        isUsageSite boolean,
        name varchar(255),
        notes longtext,
        primary key (id)
    ) ENGINE=InnoDB;

    create table LocationType (
        id bigint not null auto_increment,
        isDeleted integer,
        name varchar(255),
        notes varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table Product (
        id bigint not null auto_increment,
        bloodAbo varchar(30),
        bloodRhd varchar(30),
        createdOn datetime,
        discardReason varchar(100),
        discardedOn datetime,
        expiresOn datetime,
        isDeleted boolean,
        issuedOn datetime,
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        productNumber varchar(30),
        status varchar(30),
        collectedSample_id bigint,
        discardedBy_id bigint,
        issuedTo_id bigint,
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        productType_id integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductDiscardReason (
        id bigint not null auto_increment,
        discardReason varchar(150),
        notes longtext,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductIssue (
        id bigint not null auto_increment,
        issuedOn datetime,
        notes longtext,
        issuedBy_id bigint,
        issuedTo_id bigint,
        product_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductReturn (
        id bigint not null auto_increment,
        returnedOn datetime,
        product_id bigint,
        returnReason_id bigint,
        returnedBy_id bigint,
        returnedForRequest_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductReturnReason (
        id bigint not null auto_increment,
        reasonDetails longtext,
        returnReason varchar(150),
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductType (
        id integer not null auto_increment,
        description longtext,
        isDeleted boolean,
        productType varchar(50),
        shelfLife integer,
        shelfLifeUnits varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductUsage (
        id bigint not null auto_increment,
        hospital varchar(255),
        isAvailable boolean,
        isDeleted boolean,
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        patientName varchar(30),
        usageDate datetime,
        useIndication varchar(30),
        usedBy varchar(255),
        ward varchar(30),
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        product_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table Request (
        id bigint not null auto_increment,
        department varchar(30),
        fulfilled boolean,
        hospital varchar(30),
        indicationForUse varchar(50),
        isDeleted boolean,
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        numUnitsIssued integer,
        numUnitsRequested integer,
        patientAge integer,
        patientBirthDate date,
        patientBloodAbo varchar(30),
        patientBloodRhd varchar(30),
        patientDiagnosis varchar(100),
        patientFirstName varchar(30),
        patientGender integer,
        patientLastName varchar(30),
        patientNumber varchar(30),
        requestDate datetime,
        requestNumber varchar(30),
        requestedBy varchar(30),
        requiredDate datetime,
        ward varchar(20),
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        productType_id integer,
        requestSite_id bigint,
        requestType_id integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create table RequestType (
        id integer not null auto_increment,
        description longtext,
        isDeleted boolean,
        requestType varchar(50),
        primary key (id)
    ) ENGINE=InnoDB;

    create table SequenceNumberStore (
        id integer not null auto_increment,
        columnName varchar(255),
        lastNumber bigint,
        prefix varchar(255),
        sequenceNumberContext varchar(255),
        targetTable varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table TestResult (
        id bigint not null auto_increment,
        isDeleted boolean,
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        result varchar(255),
        testedOn datetime,
        bloodTest_name varchar(30),
        collectedSample_id bigint not null,
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table Tips (
        tipsKey varchar(255) not null,
        isDeleted boolean,
        tipsContent varchar(512),
        tipsName varchar(255),
        primary key (tipsKey)
    ) ENGINE=InnoDB;

    create table User (
        id bigint not null auto_increment,
        emailId varchar(255),
        firstName varchar(30) not null,
        isActive boolean,
        isAdmin boolean,
        isDeleted boolean,
        isStaff boolean,
        lastLogin datetime,
        lastName varchar(30),
        middleName varchar(30),
        notes longtext,
        password varchar(255) not null,
        username varchar(30) not null unique,
        primary key (id)
    ) ENGINE=InnoDB;

    alter table BloodTest_BloodTest 
        add index FK5EC59E995FA14D0E (bloodTestsIfIncorrect_name), 
        add constraint FK5EC59E995FA14D0E 
        foreign key (bloodTestsIfIncorrect_name) 
        references BloodTest (name);

    alter table BloodTest_BloodTest 
        add index FK5EC59E993A6D02C3 (BloodTest_name), 
        add constraint FK5EC59E993A6D02C3 
        foreign key (BloodTest_name) 
        references BloodTest (name);

    alter table BloodTest_BloodTest 
        add index FK5EC59E99F2F53069 (bloodTestsIfCorrect_name), 
        add constraint FK5EC59E99F2F53069 
        foreign key (bloodTestsIfCorrect_name) 
        references BloodTest (name);

    create index collectedSample_collectedOn_index on CollectedSample (collectedOn);

    create index collectedSample_collectionNumber_index on CollectedSample (collectionNumber);

    alter table CollectedSample 
        add index FKF0658A33A49787C4 (createdBy_id), 
        add constraint FKF0658A33A49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table CollectedSample 
        add index FKF0658A33D04A4456 (donationCreatedBy_id), 
        add constraint FKF0658A33D04A4456 
        foreign key (donationCreatedBy_id) 
        references User (id);

    alter table CollectedSample 
        add index FKF0658A33AED1731E (collectionSite_id), 
        add constraint FKF0658A33AED1731E 
        foreign key (collectionSite_id) 
        references Location (id);

    alter table CollectedSample 
        add index FKF0658A33B29562D0 (collectionCenter_id), 
        add constraint FKF0658A33B29562D0 
        foreign key (collectionCenter_id) 
        references Location (id);

    alter table CollectedSample 
        add index FKF0658A3359FAB30D (donor_id), 
        add constraint FKF0658A3359FAB30D 
        foreign key (donor_id) 
        references Donor (id);

    alter table CollectedSample 
        add index FKF0658A3392C64B83 (donorType_id), 
        add constraint FKF0658A3392C64B83 
        foreign key (donorType_id) 
        references DonationType (id);

    alter table CollectedSample 
        add index FKF0658A33D0AFB367 (lastUpdatedBy_id), 
        add constraint FKF0658A33D0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table CollectedSample 
        add index FKF0658A33675D568F (donationBatch_id), 
        add constraint FKF0658A33675D568F 
        foreign key (donationBatch_id) 
        references DonationBatch (id);

    alter table CollectedSample 
        add index FKF0658A331D73927B (bloodBagType_id), 
        add constraint FKF0658A331D73927B 
        foreign key (bloodBagType_id) 
        references BloodBagType (id);

    alter table CollectedSample_CollectionsWorksheet 
        add index FKB39FFD85225909B3 (worksheets_id), 
        add constraint FKB39FFD85225909B3 
        foreign key (worksheets_id) 
        references CollectionsWorksheet (id);

    alter table CollectedSample_CollectionsWorksheet 
        add index FKB39FFD85C02466CD (collectedSamples_id), 
        add constraint FKB39FFD85C02466CD 
        foreign key (collectedSamples_id) 
        references CollectedSample (id);

    alter table CollectionsWorksheet 
        add index FK72E3FEF9A49787C4 (createdBy_id), 
        add constraint FK72E3FEF9A49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table CollectionsWorksheet 
        add index FK72E3FEF9D0AFB367 (lastUpdatedBy_id), 
        add constraint FK72E3FEF9D0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    create index compatibilityTest_crossmatchTestDate_index on CompatibilityTest (compatibililityTestDate);

    alter table CompatibilityTest 
        add index FK92798602A49787C4 (createdBy_id), 
        add constraint FK92798602A49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table CompatibilityTest 
        add index FK92798602D4061B9F (forRequest_id), 
        add constraint FK92798602D4061B9F 
        foreign key (forRequest_id) 
        references Request (id);

    alter table CompatibilityTest 
        add index FK92798602EFD1FE7 (testedProduct_id), 
        add constraint FK92798602EFD1FE7 
        foreign key (testedProduct_id) 
        references Product (id);

    alter table CompatibilityTest 
        add index FK92798602D0AFB367 (lastUpdatedBy_id), 
        add constraint FK92798602D0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table CompatibilityTest 
        add index FK927986028631CA7D (crossmatchType_id), 
        add constraint FK927986028631CA7D 
        foreign key (crossmatchType_id) 
        references CrossmatchType (id);

    create index donor_donorNumber_index on Donor (donorNumber);

    create index donor_bloodAbo_index on Donor (bloodAbo);

    create index donor_lastName_index on Donor (lastName);

    create index donor_bloodRhd_index on Donor (bloodRhd);

    create index donor_firstName_index on Donor (firstName);

    alter table Donor 
        add index FK3F25E46A49787C4 (createdBy_id), 
        add constraint FK3F25E46A49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table Donor 
        add index FK3F25E46D0AFB367 (lastUpdatedBy_id), 
        add constraint FK3F25E46D0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    create index product_expiresOn_index on Product (expiresOn);

    create index donor_bloodAbo_index on Product (bloodAbo);

    create index donor_bloodRhd_index on Product (bloodRhd);

    create index product_productNumber_index on Product (productNumber);

    alter table Product 
        add index FK50C664CFA49787C4 (createdBy_id), 
        add constraint FK50C664CFA49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table Product 
        add index FK50C664CF994002DF (issuedTo_id), 
        add constraint FK50C664CF994002DF 
        foreign key (issuedTo_id) 
        references Request (id);

    alter table Product 
        add index FK50C664CF32E145A (collectedSample_id), 
        add constraint FK50C664CF32E145A 
        foreign key (collectedSample_id) 
        references CollectedSample (id);

    alter table Product 
        add index FK50C664CF73AC2B90 (productType_id), 
        add constraint FK50C664CF73AC2B90 
        foreign key (productType_id) 
        references ProductType (id);

    alter table Product 
        add index FK50C664CFFDFE9E0F (discardedBy_id), 
        add constraint FK50C664CFFDFE9E0F 
        foreign key (discardedBy_id) 
        references User (id);

    alter table Product 
        add index FK50C664CFD0AFB367 (lastUpdatedBy_id), 
        add constraint FK50C664CFD0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table ProductIssue 
        add index FK450DFD4A994002DF (issuedTo_id), 
        add constraint FK450DFD4A994002DF 
        foreign key (issuedTo_id) 
        references Request (id);

    alter table ProductIssue 
        add index FK450DFD4A9F8F75E1 (issuedBy_id), 
        add constraint FK450DFD4A9F8F75E1 
        foreign key (issuedBy_id) 
        references User (id);

    alter table ProductIssue 
        add index FK450DFD4AA8E71476 (product_id), 
        add constraint FK450DFD4AA8E71476 
        foreign key (product_id) 
        references Product (id);

    alter table ProductReturn 
        add index FK6B4876DF61638630 (returnedForRequest_id), 
        add constraint FK6B4876DF61638630 
        foreign key (returnedForRequest_id) 
        references Request (id);

    alter table ProductReturn 
        add index FK6B4876DFA8E71476 (product_id), 
        add constraint FK6B4876DFA8E71476 
        foreign key (product_id) 
        references Product (id);

    alter table ProductReturn 
        add index FK6B4876DFC1F8813D (returnedBy_id), 
        add constraint FK6B4876DFC1F8813D 
        foreign key (returnedBy_id) 
        references User (id);

    alter table ProductReturn 
        add index FK6B4876DFC7A8C5 (returnReason_id), 
        add constraint FK6B4876DFC7A8C5 
        foreign key (returnReason_id) 
        references ProductReturnReason (id);

    alter table ProductUsage 
        add index FK45B6D212A49787C4 (createdBy_id), 
        add constraint FK45B6D212A49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table ProductUsage 
        add index FK45B6D212A8E71476 (product_id), 
        add constraint FK45B6D212A8E71476 
        foreign key (product_id) 
        references Product (id);

    alter table ProductUsage 
        add index FK45B6D212D0AFB367 (lastUpdatedBy_id), 
        add constraint FK45B6D212D0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    create index request_requiredDate_index on Request (requiredDate);

    create index request_requestDate_index on Request (requestDate);

    create index request_bloodRhd_index on Request (patientBloodRhd);

    create index request_requestNumber_index on Request (requestNumber);

    create index request_bloodAbo_index on Request (patientBloodAbo);

    alter table Request 
        add index FKA4878A6FA49787C4 (createdBy_id), 
        add constraint FKA4878A6FA49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table Request 
        add index FKA4878A6F1520E0D (requestSite_id), 
        add constraint FKA4878A6F1520E0D 
        foreign key (requestSite_id) 
        references Location (id);

    alter table Request 
        add index FKA4878A6F73AC2B90 (productType_id), 
        add constraint FKA4878A6F73AC2B90 
        foreign key (productType_id) 
        references ProductType (id);

    alter table Request 
        add index FKA4878A6F537AAD30 (requestType_id), 
        add constraint FKA4878A6F537AAD30 
        foreign key (requestType_id) 
        references RequestType (id);

    alter table Request 
        add index FKA4878A6FD0AFB367 (lastUpdatedBy_id), 
        add constraint FKA4878A6FD0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table TestResult 
        add index FKDB459F6FA49787C4 (createdBy_id), 
        add constraint FKDB459F6FA49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table TestResult 
        add index FKDB459F6F32E145A (collectedSample_id), 
        add constraint FKDB459F6F32E145A 
        foreign key (collectedSample_id) 
        references CollectedSample (id);

    alter table TestResult 
        add index FKDB459F6F3A6D02C3 (bloodTest_name), 
        add constraint FKDB459F6F3A6D02C3 
        foreign key (bloodTest_name) 
        references BloodTest (name);

    alter table TestResult 
        add index FKDB459F6FD0AFB367 (lastUpdatedBy_id), 
        add constraint FKDB459F6FD0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);
