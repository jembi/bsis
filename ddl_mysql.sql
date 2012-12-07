
    drop table if exists BloodBagType;

    drop table if exists BloodTest;

    drop table if exists BloodTestResult;

    drop table if exists CollectedSample;

    drop table if exists ConfigChange;

    drop table if exists DisplayNames;

    drop table if exists Donor;

    drop table if exists DonorType;

    drop table if exists FormField;

    drop table if exists Issue;

    drop table if exists Location;

    drop table if exists LocationType;

    drop table if exists Product;

    drop table if exists ProductType;

    drop table if exists ProductUsage;

    drop table if exists RecordFieldsConfig;

    drop table if exists ReportConfig;

    drop table if exists Request;

    drop table if exists Request_Product;

    drop table if exists TestResult;

    drop table if exists User;

    create table BloodBagType (
        bloodBagType varchar(30) not null,
        primary key (bloodBagType)
    ) ENGINE=InnoDB;

    create table BloodTest (
        name varchar(30) not null,
        correctResult varchar(30) not null,
        isDeleted boolean,
        isRequired boolean,
        notes longtext,
        primary key (name)
    ) ENGINE=InnoDB;

    create table BloodTestResult (
        id bigint not null auto_increment,
        result varchar(255),
        bloodTest_name varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table CollectedSample (
        id bigint not null auto_increment,
        collectedOn datetime,
        collectionNumber varchar(30) not null,
        isDeleted boolean,
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        sampleNumber varchar(50),
        shippingNumber varchar(50),
        bloodBagType_bloodBagType varchar(30),
        collectionCenter_id bigint,
        collectionSite_id bigint,
        donor_id bigint not null,
        donorType_donorType varchar(30),
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ConfigChange (
        id bigint not null auto_increment,
        createdDate datetime,
        lastUpdated datetime,
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table DisplayNames (
        formType varchar(255) not null,
        fieldNames varchar(255),
        primary key (formType)
    ) ENGINE=InnoDB;

    create table Donor (
        id bigint not null auto_increment,
        birthDate date,
        bloodAbo varchar(30),
        bloodRhd varchar(30),
        address varchar(255),
        city varchar(50),
        country varchar(50),
        phoneNumber varchar(50),
        state varchar(50),
        zipcode varchar(10),
        donorNumber varchar(30) not null,
        firstName varchar(30) not null,
        gender varchar(30),
        isAvailable boolean,
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

    create table DonorType (
        donorType varchar(30) not null,
        primary key (donorType)
    ) ENGINE=InnoDB;

    create table FormField (
        id bigint not null auto_increment,
        defaultDisplayName varchar(30) not null,
        defaultValue longtext,
        derived boolean,
        displayName varchar(30),
        field varchar(30) not null,
        form varchar(30) not null,
        hidden boolean,
        sourceField varchar(30) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table Issue (
        issueId bigint not null auto_increment,
        comments varchar(255),
        dateIssued datetime,
        isDeleted integer,
        productNumber varchar(255),
        siteId bigint,
        primary key (issueId)
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
        createdDate datetime,
        createdOn datetime,
        expiresOn datetime,
        isAvailable boolean,
        isDeleted boolean,
        isQuarantined boolean,
        issuedOn datetime,
        lastUpdated datetime,
        notes longtext,
        productNumber varchar(255) not null,
        collectedSample_id bigint,
        createdBy_id bigint,
        issuedTo_id bigint,
        lastUpdatedBy_id bigint,
        productType_productType varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductType (
        productType varchar(30) not null,
        primary key (productType)
    ) ENGINE=InnoDB;

    create table ProductUsage (
        usageId bigint not null auto_increment,
        comments varchar(255),
        dateUsed datetime,
        hospital varchar(255),
        isDeleted integer,
        patientName varchar(255),
        productNumber varchar(255),
        useIndication varchar(255),
        ward varchar(255),
        primary key (usageId)
    ) ENGINE=InnoDB;

    create table RecordFieldsConfig (
        recordType varchar(255) not null,
        fieldNames varchar(255),
        primary key (recordType)
    ) ENGINE=InnoDB;

    create table ReportConfig (
        reportType varchar(255) not null,
        fieldNames varchar(255),
        primary key (reportType)
    ) ENGINE=InnoDB;

    create table Request (
        id bigint not null auto_increment,
        bloodAbo varchar(30),
        bloodRhd varchar(30),
        fulfilled boolean,
        isDeleted boolean,
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        patientName varchar(255),
        requestDate date,
        requestNumber varchar(30) not null,
        requestedQuantity integer,
        requiredDate date,
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        productType_productType varchar(30),
        requestSite_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table Request_Product (
        Request_id bigint not null,
        issuedProducts_id bigint not null,
        unique (issuedProducts_id)
    ) ENGINE=InnoDB;

    create table TestResult (
        id bigint not null auto_increment,
        createdDate datetime,
        isDeleted boolean,
        lastUpdated datetime,
        notes longtext,
        testedOn datetime,
        bloodTest_name varchar(30),
        bloodTestResult_id bigint,
        collectedSample_id bigint not null,
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        primary key (id)
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
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        password varchar(255) not null,
        username varchar(30) not null unique,
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    alter table BloodTestResult 
        add index FK39946CC93A6D02C3 (bloodTest_name), 
        add constraint FK39946CC93A6D02C3 
        foreign key (bloodTest_name) 
        references BloodTest (name);

    alter table CollectedSample 
        add index FKF0658A33A49787C4 (createdBy_id), 
        add constraint FKF0658A33A49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table CollectedSample 
        add index FKF0658A33D10E68A8 (bloodBagType_bloodBagType), 
        add constraint FKF0658A33D10E68A8 
        foreign key (bloodBagType_bloodBagType) 
        references BloodBagType (bloodBagType);

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
        add index FKF0658A33F17A8E4E (donorType_donorType), 
        add constraint FKF0658A33F17A8E4E 
        foreign key (donorType_donorType) 
        references DonorType (donorType);

    alter table CollectedSample 
        add index FKF0658A3359FAB30D (donor_id), 
        add constraint FKF0658A3359FAB30D 
        foreign key (donor_id) 
        references Donor (id);

    alter table CollectedSample 
        add index FKF0658A33D0AFB367 (lastUpdatedBy_id), 
        add constraint FKF0658A33D0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table ConfigChange 
        add index FKF9B3DDB2A49787C4 (createdBy_id), 
        add constraint FKF9B3DDB2A49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table ConfigChange 
        add index FKF9B3DDB2D0AFB367 (lastUpdatedBy_id), 
        add constraint FKF9B3DDB2D0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

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

    create index donor_bloodAbo_index on Product (bloodAbo);

    create index donor_bloodRhd_index on Product (bloodRhd);

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
        add index FK50C664CFD0AFB367 (lastUpdatedBy_id), 
        add constraint FK50C664CFD0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table Product 
        add index FK50C664CFC5DF532 (productType_productType), 
        add constraint FK50C664CFC5DF532 
        foreign key (productType_productType) 
        references ProductType (productType);

    create index request_bloodRhd_index on Request (bloodRhd);

    create index request_requestNumber_index on Request (requestNumber);

    create index request_bloodAbo_index on Request (bloodAbo);

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
        add index FKA4878A6FD0AFB367 (lastUpdatedBy_id), 
        add constraint FKA4878A6FD0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table Request 
        add index FKA4878A6FC5DF532 (productType_productType), 
        add constraint FKA4878A6FC5DF532 
        foreign key (productType_productType) 
        references ProductType (productType);

    alter table Request_Product 
        add index FK385FB6FF27F33616 (Request_id), 
        add constraint FK385FB6FF27F33616 
        foreign key (Request_id) 
        references Request (id);

    alter table Request_Product 
        add index FK385FB6FF4C68456 (issuedProducts_id), 
        add constraint FK385FB6FF4C68456 
        foreign key (issuedProducts_id) 
        references Product (id);

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

    alter table TestResult 
        add index FKDB459F6F71B0E253 (bloodTestResult_id), 
        add constraint FKDB459F6F71B0E253 
        foreign key (bloodTestResult_id) 
        references BloodTestResult (id);

    alter table User 
        add index FK285FEBA49787C4 (createdBy_id), 
        add constraint FK285FEBA49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table User 
        add index FK285FEBD0AFB367 (lastUpdatedBy_id), 
        add constraint FK285FEBD0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);
