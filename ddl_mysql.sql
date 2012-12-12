
    drop table if exists BloodBagType;

    drop table if exists BloodTest;

    drop table if exists CollectedSample;

    drop table if exists Donor;

    drop table if exists DonorType;

    drop table if exists FormField;

    drop table if exists Location;

    drop table if exists LocationType;

    drop table if exists Product;

    drop table if exists ProductType;

    drop table if exists ProductUsage;

    drop table if exists Request;

    drop table if exists TestResult;

    drop table if exists User;

    create table BloodBagType (
        bloodBagType varchar(30) not null,
        isDeleted boolean,
        primary key (bloodBagType)
    ) ENGINE=InnoDB;

    create table BloodTest (
        name varchar(30) not null,
        allowedResults varchar(255),
        correctResult varchar(255),
        isDeleted boolean,
        isRequired boolean,
        notes longtext,
        primary key (name)
    ) ENGINE=InnoDB;

    create table CollectedSample (
        id bigint not null auto_increment,
        collectedOn datetime,
        collectionNumber varchar(30),
        isDeleted boolean,
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        sampleNumber varchar(50),
        shippingNumber varchar(50),
        bloodBagType_bloodBagType varchar(30),
        collectionCenter_id bigint,
        collectionSite_id bigint,
        donor_id bigint,
        donorType_donorType varchar(30),
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        primary key (id)
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
        isDeleted boolean,
        primary key (donorType)
    ) ENGINE=InnoDB;

    create table FormField (
        id bigint not null auto_increment,
        autoGenerate boolean,
        defaultDisplayName varchar(30),
        defaultValue longtext,
        derived boolean,
        displayName varchar(30),
        field varchar(30),
        form varchar(30),
        hidden boolean,
        isRequired boolean,
        sourceField varchar(30),
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
        expiresOn datetime,
        isAvailable boolean,
        isDeleted boolean,
        isQuarantined boolean,
        issuedOn datetime,
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        productNumber varchar(30),
        collectedSample_id bigint,
        issuedTo_id bigint,
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        productType_productType varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductType (
        productType varchar(30) not null,
        isDeleted boolean,
        primary key (productType)
    ) ENGINE=InnoDB;

    create table ProductUsage (
        id bigint not null auto_increment,
        hospital varchar(255),
        isAvailable boolean,
        isDeleted boolean,
        createdDate datetime,
        lastUpdated datetime,
        notes longtext,
        patientName varchar(30) not null,
        usageDate date,
        useIndication varchar(30) not null,
        ward varchar(30) not null,
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        product_id bigint,
        primary key (id)
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
        requestNumber varchar(30),
        requestedQuantity integer,
        requiredDate date,
        createdBy_id bigint,
        lastUpdatedBy_id bigint,
        productType_productType varchar(30),
        requestSite_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table TestResult (
        id bigint not null auto_increment,
        createdDate datetime,
        isDeleted boolean,
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

    create index collectedSample_collectedOn_index on CollectedSample (collectedOn);

    create index collectedSample_collectionNumber_index on CollectedSample (collectionNumber);

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
        add index FK50C664CFD0AFB367 (lastUpdatedBy_id), 
        add constraint FK50C664CFD0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table Product 
        add index FK50C664CFC5DF532 (productType_productType), 
        add constraint FK50C664CFC5DF532 
        foreign key (productType_productType) 
        references ProductType (productType);

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
