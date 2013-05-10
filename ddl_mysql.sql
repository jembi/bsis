
    create table BloodBagType (
        id SMALLINT not null auto_increment,
        bloodBagType varchar(50),
        canPool boolean,
        canSplit boolean,
        isDeleted boolean,
        primary key (id)
    ) ENGINE=InnoDB;

    create table BloodBagType_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        bloodBagType varchar(50),
        canPool boolean,
        canSplit boolean,
        isDeleted boolean,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table BloodTest (
        id SMALLINT not null auto_increment,
        bloodTestType varchar(30),
        category varchar(30),
        context varchar(30),
        isActive boolean,
        isEmptyAllowed boolean,
        negativeResults varchar(255),
        positiveResults varchar(255),
        rankInCategory integer,
        testName varchar(40),
        testNameShort varchar(25),
        validResults varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table BloodTestResult (
        id bigint not null auto_increment,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        result varchar(10),
        testedOn datetime,
        bloodTest_id SMALLINT,
        collectedSample_id bigint,
        machineReading_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table BloodTest_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        bloodTestType varchar(30),
        category varchar(30),
        context varchar(30),
        isActive boolean,
        isEmptyAllowed boolean,
        negativeResults varchar(255),
        positiveResults varchar(255),
        rankInCategory integer,
        testName varchar(40),
        testNameShort varchar(25),
        validResults varchar(255),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table BloodTest_WorksheetType (
        bloodTests_id SMALLINT not null,
        worksheetTypes_id SMALLINT not null,
        primary key (bloodTests_id, worksheetTypes_id)
    ) ENGINE=InnoDB;

    create table BloodTest_WorksheetType_AUD (
        REV integer not null,
        bloodTests_id SMALLINT not null,
        worksheetTypes_id SMALLINT not null,
        REVTYPE tinyint,
        primary key (REV, bloodTests_id, worksheetTypes_id)
    ) ENGINE=InnoDB;

    create table BloodTestingRule (
        id MEDIUMINT not null auto_increment,
        bloodTestsIds varchar(200),
        category varchar(30),
        collectionFieldChanged varchar(12),
        context varchar(30),
        extraInformation varchar(30),
        isActive boolean,
        markSampleAsUnsafe boolean,
        newInformation varchar(30),
        pattern varchar(50),
        pendingTestsIds varchar(60),
        subCategory varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table BloodTestingRule_AUD (
        id MEDIUMINT not null,
        REV integer not null,
        REVTYPE tinyint,
        bloodTestsIds varchar(200),
        category varchar(30),
        collectionFieldChanged varchar(12),
        context varchar(30),
        extraInformation varchar(30),
        isActive boolean,
        markSampleAsUnsafe boolean,
        newInformation varchar(30),
        pattern varchar(50),
        pendingTestsIds varchar(60),
        subCategory varchar(30),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table CollectedSample (
        id bigint not null auto_increment,
        bloodAbo varchar(50),
        bloodPressure decimal(6,2),
        bloodRh varchar(50),
        bloodTypingStatus varchar(20),
        collectedOn datetime,
        collectionNumber varchar(20) unique,
        donorWeight decimal(6,2),
        extraBloodTypeInformation varchar(150),
        haemoglobinCount decimal(6,2),
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        ttiStatus varchar(20),
        bloodBagType_id SMALLINT,
        collectionBatch_id SMALLINT,
        collectionCenter_id bigint,
        collectionSite_id bigint,
        donationCreatedBy_id SMALLINT,
        donationType_id SMALLINT,
        donor_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table CollectedSample_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        bloodAbo varchar(50),
        bloodPressure decimal(6,2),
        bloodRh varchar(50),
        bloodTypingStatus varchar(20),
        collectedOn datetime,
        collectionNumber varchar(20),
        donorWeight decimal(6,2),
        extraBloodTypeInformation varchar(150),
        haemoglobinCount decimal(6,2),
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        ttiStatus varchar(20),
        bloodBagType_id SMALLINT,
        collectionBatch_id SMALLINT,
        collectionCenter_id bigint,
        collectionSite_id bigint,
        donationCreatedBy_id SMALLINT,
        donationType_id SMALLINT,
        donor_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table CollectionBatch (
        id SMALLINT not null auto_increment,
        batchNumber varchar(20) unique,
        isDeleted boolean not null,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        collectionCenter_id bigint,
        collectionSite_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table CollectionBatch_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        batchNumber varchar(20),
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        collectionCenter_id bigint,
        collectionSite_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table CompatibilityTest (
        id bigint not null auto_increment,
        compatibililityTestDate datetime,
        compatibilityResult integer,
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        testedBy varchar(255),
        transfusedBefore boolean,
        crossmatchType_id SMALLINT,
        forRequest_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        testedProduct_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table CompatibilityTest_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        compatibililityTestDate datetime,
        compatibilityResult integer,
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        testedBy varchar(255),
        transfusedBefore boolean,
        crossmatchType_id SMALLINT,
        forRequest_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        testedProduct_id bigint,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table ContactMethodType (
        id SMALLINT not null auto_increment,
        contactMethodType varchar(30),
        isDeleted boolean not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ContactMethodType_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        contactMethodType varchar(30),
        isDeleted boolean,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table CrossmatchType (
        id SMALLINT not null auto_increment,
        crossmatchType varchar(255),
        isDeleted boolean,
        primary key (id)
    ) ENGINE=InnoDB;

    create table CrossmatchType_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        crossmatchType varchar(255),
        isDeleted boolean,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table DeferralReason (
        id SMALLINT not null auto_increment,
        isDeleted boolean,
        reason varchar(100),
        primary key (id)
    ) ENGINE=InnoDB;

    create table DeferralReason_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        isDeleted boolean,
        reason varchar(100),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table DonationType (
        id SMALLINT not null auto_increment,
        donationType varchar(50),
        isDeleted boolean,
        primary key (id)
    ) ENGINE=InnoDB;

    create table DonationType_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        donationType varchar(50),
        isDeleted boolean,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Donor (
        id bigint not null auto_increment,
        age TINYINT,
        birthDate date,
        birthDateInferred date,
        bloodAbo varchar(10),
        bloodRh varchar(10),
        callingName varchar(20),
        address varchar(100),
        city varchar(25),
        country varchar(25),
        district varchar(25),
        otherPhoneNumber varchar(20),
        phoneNumber varchar(20),
        province varchar(25),
        state varchar(25),
        zipcode varchar(10),
        dateOfLastDonation datetime,
        donorHash varchar(50),
        donorNumber varchar(20) unique,
        donorStatus varchar(20),
        firstName varchar(20),
        gender varchar(15),
        isDeleted boolean,
        lastName varchar(20),
        middleName varchar(20),
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        nationalID varchar(15),
        notes longtext,
        preferredContactMethod_id SMALLINT,
        donorPanel_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table DonorDeferral (
        id bigint not null auto_increment,
        deferralReasonText longtext,
        deferredOn date,
        deferredUntil date,
        deferralReason_id SMALLINT,
        deferredBy_id SMALLINT,
        deferredDonor_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table DonorDeferral_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        deferralReasonText longtext,
        deferredOn date,
        deferredUntil date,
        deferralReason_id SMALLINT,
        deferredBy_id SMALLINT,
        deferredDonor_id bigint,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Donor_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        age TINYINT,
        birthDate date,
        birthDateInferred date,
        bloodAbo varchar(10),
        bloodRh varchar(10),
        callingName varchar(20),
        address varchar(100),
        city varchar(25),
        country varchar(25),
        district varchar(25),
        otherPhoneNumber varchar(20),
        phoneNumber varchar(20),
        province varchar(25),
        state varchar(25),
        zipcode varchar(10),
        dateOfLastDonation datetime,
        donorHash varchar(50),
        donorNumber varchar(20),
        donorStatus varchar(20),
        firstName varchar(20),
        gender varchar(15),
        isDeleted boolean,
        lastName varchar(20),
        middleName varchar(20),
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        nationalID varchar(15),
        notes longtext,
        preferredContactMethod_id SMALLINT,
        donorPanel_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table FormField (
        id bigint not null auto_increment,
        autoGenerate boolean,
        canBeOptional boolean,
        defaultDisplayName varchar(60),
        defaultValue longtext,
        displayName varchar(60),
        field varchar(30),
        form varchar(30),
        hidden boolean,
        isAutoGeneratable boolean,
        isHidable boolean,
        isRequired boolean,
        isTimeField boolean,
        maxLength integer,
        useCurrentTime boolean,
        primary key (id)
    ) ENGINE=InnoDB;

    create table GenericConfig (
        id bigint not null auto_increment,
        propertyName varchar(80),
        propertyOwner varchar(30),
        propertyValue varchar(80),
        primary key (id)
    ) ENGINE=InnoDB;

    create table Location (
        id bigint not null auto_increment,
        isCollectionCenter boolean,
        isCollectionSite boolean,
        isDeleted boolean,
        isDonorPanel boolean,
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

    create table Location_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        isCollectionCenter boolean,
        isCollectionSite boolean,
        isDeleted boolean,
        isDonorPanel boolean,
        isMobileSite boolean,
        isUsageSite boolean,
        name varchar(255),
        notes longtext,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table MachineReading (
        id bigint not null auto_increment,
        columnNumber SMALLINT,
        machineReading decimal(7,3),
        rowNumber SMALLINT,
        plateSession_id bigint,
        wellType_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table MicrotiterPlate (
        id SMALLINT not null auto_increment,
        isDeleted boolean,
        notes longtext,
        numColumns SMALLINT,
        numRows SMALLINT,
        plateKey varchar(15) unique,
        plateName varchar(20),
        primary key (id)
    ) ENGINE=InnoDB;

    create table MicrotiterPlate_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        isDeleted boolean,
        notes longtext,
        numColumns SMALLINT,
        numRows SMALLINT,
        plateKey varchar(15),
        plateName varchar(20),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Permission (
        id bigint not null auto_increment,
        name varchar(50),
        primary key (id)
    ) ENGINE=InnoDB;

    create table Permission_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        name varchar(50),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Permission_Role (
        permissions_id bigint not null,
        roles_id bigint not null
    ) ENGINE=InnoDB;

    create table Permission_Role_AUD (
        REV integer not null,
        permissions_id bigint not null,
        roles_id bigint not null,
        REVTYPE tinyint,
        primary key (REV, permissions_id, roles_id)
    ) ENGINE=InnoDB;

    create table PlateSession (
        id bigint not null auto_increment,
        isDeleted boolean,
        plateUsedOn datetime,
        primary key (id)
    ) ENGINE=InnoDB;

    create table PlateSession_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        isDeleted boolean,
        plateUsedOn datetime,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table PlateSession_BloodTestResult (
        PlateSession_id bigint not null,
        bloodTestsOnPlate_id bigint not null,
        unique (bloodTestsOnPlate_id)
    ) ENGINE=InnoDB;

    create table Product (
        id bigint not null auto_increment,
        createdOn datetime,
        discardedOn DATETIME,
        expiresOn datetime,
        isDeleted boolean,
        issuedOn datetime,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        status varchar(30),
        collectedSample_id bigint,
        issuedTo_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        productType_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductStatusChange (
        id bigint not null auto_increment,
        newStatus varchar(30),
        statusChangeReasonText longtext,
        statusChangeType varchar(30),
        statusChangedOn datetime,
        changedBy_id SMALLINT,
        issuedTo_id bigint,
        product_id bigint,
        statusChangeReason_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductStatusChangeReason (
        id SMALLINT not null auto_increment,
        category varchar(30),
        isDeleted boolean,
        statusChangeReason varchar(100),
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductStatusChangeReason_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        category varchar(30),
        isDeleted boolean,
        statusChangeReason varchar(100),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table ProductStatusChange_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        newStatus varchar(30),
        statusChangeReasonText longtext,
        statusChangeType varchar(30),
        statusChangedOn datetime,
        changedBy_id SMALLINT,
        issuedTo_id bigint,
        product_id bigint,
        statusChangeReason_id SMALLINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table ProductType (
        id SMALLINT not null auto_increment,
        canPool boolean,
        canSubdivide boolean,
        description longtext,
        expiresAfter integer,
        expiresAfterUnits varchar(30),
        hasBloodGroup boolean,
        isDeleted boolean,
        productType varchar(50),
        productTypeNameShort varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductTypeCombination (
        id SMALLINT not null auto_increment,
        combinationName varchar(300),
        isDeleted boolean,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductTypeCombination_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        combinationName varchar(300),
        isDeleted boolean,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table ProductTypeCombination_ProductType (
        productTypeCombinations_id SMALLINT not null,
        productTypes_id SMALLINT not null,
        primary key (productTypeCombinations_id, productTypes_id)
    ) ENGINE=InnoDB;

    create table ProductType_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        canPool boolean,
        canSubdivide boolean,
        description longtext,
        expiresAfter integer,
        expiresAfterUnits varchar(30),
        hasBloodGroup boolean,
        isDeleted boolean,
        productType varchar(50),
        productTypeNameShort varchar(30),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table ProductUsage (
        id bigint not null auto_increment,
        hospital varchar(50),
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        patientName varchar(50),
        usageDate datetime,
        useIndication varchar(50),
        usedBy varchar(30),
        ward varchar(30),
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        product_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductUsage_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        hospital varchar(50),
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        patientName varchar(50),
        usageDate datetime,
        useIndication varchar(50),
        usedBy varchar(30),
        ward varchar(30),
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        product_id bigint,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Product_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        createdOn datetime,
        discardedOn DATETIME,
        expiresOn datetime,
        isDeleted boolean,
        issuedOn datetime,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        status varchar(30),
        collectedSample_id bigint,
        issuedTo_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        productType_id SMALLINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table REVINFO (
        REV integer not null auto_increment,
        REVTSTMP bigint,
        primary key (REV)
    ) ENGINE=InnoDB;

    create table Request (
        id bigint not null auto_increment,
        department varchar(30),
        fulfilled boolean,
        hospital varchar(30),
        indicationForUse varchar(50),
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        numUnitsIssued integer,
        numUnitsRequested integer,
        patientAge integer,
        patientBirthDate date,
        patientBloodAbo varchar(30),
        patientBloodRh varchar(30),
        patientDiagnosis varchar(100),
        patientFirstName varchar(30),
        patientGender integer,
        patientLastName varchar(30),
        patientNumber varchar(30),
        requestDate datetime,
        requestNumber varchar(20) unique,
        requestedBy varchar(30),
        requiredDate datetime,
        ward varchar(20),
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        productType_id SMALLINT,
        requestSite_id bigint,
        requestType_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table RequestType (
        id SMALLINT not null auto_increment,
        description varchar(100),
        isDeleted boolean,
        requestType varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table RequestType_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        description varchar(100),
        isDeleted boolean,
        requestType varchar(30),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Request_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        department varchar(30),
        fulfilled boolean,
        hospital varchar(30),
        indicationForUse varchar(50),
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        numUnitsIssued integer,
        numUnitsRequested integer,
        patientAge integer,
        patientBirthDate date,
        patientBloodAbo varchar(30),
        patientBloodRh varchar(30),
        patientDiagnosis varchar(100),
        patientFirstName varchar(30),
        patientGender integer,
        patientLastName varchar(30),
        patientNumber varchar(30),
        requestDate datetime,
        requestNumber varchar(20),
        requestedBy varchar(30),
        requiredDate datetime,
        ward varchar(20),
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        productType_id SMALLINT,
        requestSite_id bigint,
        requestType_id SMALLINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Role (
        id bigint not null auto_increment,
        name varchar(50),
        primary key (id)
    ) ENGINE=InnoDB;

    create table Role_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        name varchar(50),
        primary key (id, REV)
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

    create table SubdividedProduct (
        id bigint not null auto_increment,
        divisionCode varchar(5),
        parentProduct_id bigint,
        primary key (id)
    ) ENGINE=InnoDB;

    create table SubdividedProduct_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        divisionCode varchar(5),
        parentProduct_id bigint,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Tips (
        tipsKey varchar(255) not null,
        isDeleted boolean,
        tipsContent varchar(1000),
        tipsName varchar(255),
        primary key (tipsKey)
    ) ENGINE=InnoDB;

    create table User (
        id SMALLINT not null auto_increment,
        emailId varchar(255),
        firstName varchar(15) not null,
        isActive boolean,
        isAdmin boolean,
        isDeleted boolean,
        isStaff boolean,
        lastLogin datetime,
        lastName varchar(15),
        notes longtext,
        password varchar(255) not null,
        username varchar(30) not null unique,
        primary key (id)
    ) ENGINE=InnoDB;

    create table User_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        emailId varchar(255),
        firstName varchar(15),
        isActive boolean,
        isAdmin boolean,
        isDeleted boolean,
        isStaff boolean,
        lastLogin datetime,
        lastName varchar(15),
        notes longtext,
        password varchar(255),
        username varchar(30),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table User_Role (
        users_id SMALLINT not null,
        roles_id bigint not null
    ) ENGINE=InnoDB;

    create table User_Role_AUD (
        REV integer not null,
        users_id SMALLINT not null,
        roles_id bigint not null,
        REVTYPE tinyint,
        primary key (REV, users_id, roles_id)
    ) ENGINE=InnoDB;

    create table VerifiedSender (
        id bigint not null auto_increment,
        isDeleted boolean,
        senderName varchar(200),
        primary key (id)
    ) ENGINE=InnoDB;

    create table VerifiedSender_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        isDeleted boolean,
        senderName varchar(200),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table WellType (
        id SMALLINT not null auto_increment,
        isDeleted boolean,
        requiresSample boolean,
        wellType varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table WellType_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        isDeleted boolean,
        requiresSample boolean,
        wellType varchar(30),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Worksheet (
        id bigint not null auto_increment,
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        worksheetNumber varchar(20) unique,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        worksheetType_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table WorksheetType (
        id SMALLINT not null auto_increment,
        context varchar(30),
        isDeleted boolean,
        worksheetType varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table WorksheetType_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        context varchar(30),
        isDeleted boolean,
        worksheetType varchar(30),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Worksheet_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        worksheetNumber varchar(20),
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        worksheetType_id SMALLINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Worksheet_CollectedSample (
        worksheets_id bigint not null,
        collectedSamples_id bigint not null,
        primary key (worksheets_id, collectedSamples_id)
    ) ENGINE=InnoDB;

    alter table BloodBagType_AUD 
        add index FKE16C6DF9DF74E053 (REV), 
        add constraint FKE16C6DF9DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table BloodTestResult 
        add index FK39946CC9A49787C4 (createdBy_id), 
        add constraint FK39946CC9A49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table BloodTestResult 
        add index FK39946CC932E145A (collectedSample_id), 
        add constraint FK39946CC932E145A 
        foreign key (collectedSample_id) 
        references CollectedSample (id);

    alter table BloodTestResult 
        add index FK39946CC9E104121F (machineReading_id), 
        add constraint FK39946CC9E104121F 
        foreign key (machineReading_id) 
        references MachineReading (id);

    alter table BloodTestResult 
        add index FK39946CC9D0AFB367 (lastUpdatedBy_id), 
        add constraint FK39946CC9D0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table BloodTestResult 
        add index FK39946CC945027987 (bloodTest_id), 
        add constraint FK39946CC945027987 
        foreign key (bloodTest_id) 
        references BloodTest (id);

    alter table BloodTest_AUD 
        add index FKE1FA995DDF74E053 (REV), 
        add constraint FKE1FA995DDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table BloodTest_WorksheetType 
        add index FK7A6DA3B518CB61F2 (worksheetTypes_id), 
        add constraint FK7A6DA3B518CB61F2 
        foreign key (worksheetTypes_id) 
        references WorksheetType (id);

    alter table BloodTest_WorksheetType 
        add index FK7A6DA3B5BC5F90CC (bloodTests_id), 
        add constraint FK7A6DA3B5BC5F90CC 
        foreign key (bloodTests_id) 
        references BloodTest (id);

    alter table BloodTest_WorksheetType_AUD 
        add index FK7F06EF06DF74E053 (REV), 
        add constraint FK7F06EF06DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table BloodTestingRule_AUD 
        add index FKE99C9FC3DF74E053 (REV), 
        add constraint FKE99C9FC3DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

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
        add index FKF0658A33E5D4FEA3 (collectionBatch_id), 
        add constraint FKF0658A33E5D4FEA3 
        foreign key (collectionBatch_id) 
        references CollectionBatch (id);

    alter table CollectedSample 
        add index FKF0658A3359FAB30D (donor_id), 
        add constraint FKF0658A3359FAB30D 
        foreign key (donor_id) 
        references Donor (id);

    alter table CollectedSample 
        add index FKF0658A338461A8D7 (donationType_id), 
        add constraint FKF0658A338461A8D7 
        foreign key (donationType_id) 
        references DonationType (id);

    alter table CollectedSample 
        add index FKF0658A33D0AFB367 (lastUpdatedBy_id), 
        add constraint FKF0658A33D0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table CollectedSample 
        add index FKF0658A331D73927B (bloodBagType_id), 
        add constraint FKF0658A331D73927B 
        foreign key (bloodBagType_id) 
        references BloodBagType (id);

    alter table CollectedSample_AUD 
        add index FKD18B6684DF74E053 (REV), 
        add constraint FKD18B6684DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table CollectionBatch 
        add index FK227631CA49787C4 (createdBy_id), 
        add constraint FK227631CA49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table CollectionBatch 
        add index FK227631CAED1731E (collectionSite_id), 
        add constraint FK227631CAED1731E 
        foreign key (collectionSite_id) 
        references Location (id);

    alter table CollectionBatch 
        add index FK227631CB29562D0 (collectionCenter_id), 
        add constraint FK227631CB29562D0 
        foreign key (collectionCenter_id) 
        references Location (id);

    alter table CollectionBatch 
        add index FK227631CD0AFB367 (lastUpdatedBy_id), 
        add constraint FK227631CD0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table CollectionBatch_AUD 
        add index FKB74A2EDDF74E053 (REV), 
        add constraint FKB74A2EDDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

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

    alter table CompatibilityTest_AUD 
        add index FKE8EE2D3DF74E053 (REV), 
        add constraint FKE8EE2D3DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table ContactMethodType_AUD 
        add index FK64A7DC2CDF74E053 (REV), 
        add constraint FK64A7DC2CDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table CrossmatchType_AUD 
        add index FK7FC1C4D0DF74E053 (REV), 
        add constraint FK7FC1C4D0DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table DeferralReason_AUD 
        add index FK85449400DF74E053 (REV), 
        add constraint FK85449400DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table DonationType_AUD 
        add index FK58EF0DDDDF74E053 (REV), 
        add constraint FK58EF0DDDDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    create index donor_donorNumber_index on Donor (donorNumber);

    create index donor_lastName_index on Donor (lastName);

    create index donor_firstName_index on Donor (firstName);

    alter table Donor 
        add index FK3F25E46A49787C4 (createdBy_id), 
        add constraint FK3F25E46A49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table Donor 
        add index FK3F25E463043805 (donorPanel_id), 
        add constraint FK3F25E463043805 
        foreign key (donorPanel_id) 
        references Location (id);

    alter table Donor 
        add index FK3F25E46FCE9E976 (preferredContactMethod_id), 
        add constraint FK3F25E46FCE9E976 
        foreign key (preferredContactMethod_id) 
        references ContactMethodType (id);

    alter table Donor 
        add index FK3F25E46D0AFB367 (lastUpdatedBy_id), 
        add constraint FK3F25E46D0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table DonorDeferral 
        add index FKC7E323D193E6BEC (deferredDonor_id), 
        add constraint FKC7E323D193E6BEC 
        foreign key (deferredDonor_id) 
        references Donor (id);

    alter table DonorDeferral 
        add index FKC7E323D1ED3A012D (deferredBy_id), 
        add constraint FKC7E323D1ED3A012D 
        foreign key (deferredBy_id) 
        references User (id);

    alter table DonorDeferral 
        add index FKC7E323D1C9CCBBFC (deferralReason_id), 
        add constraint FKC7E323D1C9CCBBFC 
        foreign key (deferralReason_id) 
        references DeferralReason (id);

    alter table DonorDeferral_AUD 
        add index FK17470122DF74E053 (REV), 
        add constraint FK17470122DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Donor_AUD 
        add index FKEB99F917DF74E053 (REV), 
        add constraint FKEB99F917DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Location_AUD 
        add index FK6563F26DF74E053 (REV), 
        add constraint FK6563F26DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table MachineReading 
        add index FK4CF2E0652531C00D (wellType_id), 
        add constraint FK4CF2E0652531C00D 
        foreign key (wellType_id) 
        references WellType (id);

    alter table MachineReading 
        add index FK4CF2E065AF35157F (plateSession_id), 
        add constraint FK4CF2E065AF35157F 
        foreign key (plateSession_id) 
        references PlateSession (id);

    alter table MicrotiterPlate_AUD 
        add index FK45D4695FDF74E053 (REV), 
        add constraint FK45D4695FDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Permission_AUD 
        add index FK6E934040DF74E053 (REV), 
        add constraint FK6E934040DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Permission_Role 
        add index FK63DCE8E61D2E2631 (roles_id), 
        add constraint FK63DCE8E61D2E2631 
        foreign key (roles_id) 
        references Role (id);

    alter table Permission_Role 
        add index FK63DCE8E612647DE3 (permissions_id), 
        add constraint FK63DCE8E612647DE3 
        foreign key (permissions_id) 
        references Permission (id);

    alter table Permission_Role_AUD 
        add index FKE7FE33B7DF74E053 (REV), 
        add constraint FKE7FE33B7DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table PlateSession_AUD 
        add index FK79BBDC91DF74E053 (REV), 
        add constraint FK79BBDC91DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table PlateSession_BloodTestResult 
        add index FK7A75514AAF35157F (PlateSession_id), 
        add constraint FK7A75514AAF35157F 
        foreign key (PlateSession_id) 
        references PlateSession (id);

    alter table PlateSession_BloodTestResult 
        add index FK7A75514A2DA883C0 (bloodTestsOnPlate_id), 
        add constraint FK7A75514A2DA883C0 
        foreign key (bloodTestsOnPlate_id) 
        references BloodTestResult (id);

    create index product_expiresOn_index on Product (expiresOn);

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
        add index FK50C664CFD0AFB367 (lastUpdatedBy_id), 
        add constraint FK50C664CFD0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table ProductStatusChange 
        add index FKCCE48CB1994002DF (issuedTo_id), 
        add constraint FKCCE48CB1994002DF 
        foreign key (issuedTo_id) 
        references Request (id);

    alter table ProductStatusChange 
        add index FKCCE48CB18BFC6394 (statusChangeReason_id), 
        add constraint FKCCE48CB18BFC6394 
        foreign key (statusChangeReason_id) 
        references ProductStatusChangeReason (id);

    alter table ProductStatusChange 
        add index FKCCE48CB1A8E71476 (product_id), 
        add constraint FKCCE48CB1A8E71476 
        foreign key (product_id) 
        references Product (id);

    alter table ProductStatusChange 
        add index FKCCE48CB1438D2378 (changedBy_id), 
        add constraint FKCCE48CB1438D2378 
        foreign key (changedBy_id) 
        references User (id);

    alter table ProductStatusChangeReason_AUD 
        add index FKA5ADFDA6DF74E053 (REV), 
        add constraint FKA5ADFDA6DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table ProductStatusChange_AUD 
        add index FK79A8FA02DF74E053 (REV), 
        add constraint FK79A8FA02DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table ProductTypeCombination_AUD 
        add index FK61943137DF74E053 (REV), 
        add constraint FK61943137DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table ProductTypeCombination_ProductType 
        add index FK3F6704507548F61D (productTypeCombinations_id), 
        add constraint FK3F6704507548F61D 
        foreign key (productTypeCombinations_id) 
        references ProductTypeCombination (id);

    alter table ProductTypeCombination_ProductType 
        add index FK3F670450EE5B3BAF (productTypes_id), 
        add constraint FK3F670450EE5B3BAF 
        foreign key (productTypes_id) 
        references ProductType (id);

    alter table ProductType_AUD 
        add index FKA50719FADF74E053 (REV), 
        add constraint FKA50719FADF74E053 
        foreign key (REV) 
        references REVINFO (REV);

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

    alter table ProductUsage_AUD 
        add index FKB27A6E3DF74E053 (REV), 
        add constraint FKB27A6E3DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Product_AUD 
        add index FKA859320DF74E053 (REV), 
        add constraint FKA859320DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    create index request_requiredDate_index on Request (requiredDate);

    create index request_requestDate_index on Request (requestDate);

    create index request_bloodRhd_index on Request (patientBloodRh);

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

    alter table RequestType_AUD 
        add index FK36989F9ADF74E053 (REV), 
        add constraint FK36989F9ADF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Request_AUD 
        add index FKA7FAE8C0DF74E053 (REV), 
        add constraint FKA7FAE8C0DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Role_AUD 
        add index FKF3FAE767DF74E053 (REV), 
        add constraint FKF3FAE767DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table SubdividedProduct 
        add index FK2137ED84D946D0A0 (parentProduct_id), 
        add constraint FK2137ED84D946D0A0 
        foreign key (parentProduct_id) 
        references Product (id);

    alter table SubdividedProduct_AUD 
        add index FKC0E9B955DF74E053 (REV), 
        add constraint FKC0E9B955DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table User_AUD 
        add index FKF3FCA03CDF74E053 (REV), 
        add constraint FKF3FCA03CDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table User_Role 
        add index FK8B9F886A1D2E2631 (roles_id), 
        add constraint FK8B9F886A1D2E2631 
        foreign key (roles_id) 
        references Role (id);

    alter table User_Role 
        add index FK8B9F886A1D314A5B (users_id), 
        add constraint FK8B9F886A1D314A5B 
        foreign key (users_id) 
        references User (id);

    alter table User_Role_AUD 
        add index FK269D713BDF74E053 (REV), 
        add constraint FK269D713BDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table VerifiedSender_AUD 
        add index FKE0CE028EDF74E053 (REV), 
        add constraint FKE0CE028EDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table WellType_AUD 
        add index FKD3033B19DF74E053 (REV), 
        add constraint FKD3033B19DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Worksheet 
        add index FKB98678CEA49787C4 (createdBy_id), 
        add constraint FKB98678CEA49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table Worksheet 
        add index FKB98678CEBC88FBF5 (worksheetType_id), 
        add constraint FKB98678CEBC88FBF5 
        foreign key (worksheetType_id) 
        references WorksheetType (id);

    alter table Worksheet 
        add index FKB98678CED0AFB367 (lastUpdatedBy_id), 
        add constraint FKB98678CED0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table WorksheetType_AUD 
        add index FK122EBEF9DF74E053 (REV), 
        add constraint FK122EBEF9DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Worksheet_AUD 
        add index FK2D0D8F9FDF74E053 (REV), 
        add constraint FK2D0D8F9FDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Worksheet_CollectedSample 
        add index FK1BCDFCC2EA518FDE (worksheets_id), 
        add constraint FK1BCDFCC2EA518FDE 
        foreign key (worksheets_id) 
        references Worksheet (id);

    alter table Worksheet_CollectedSample 
        add index FK1BCDFCC2C02466CD (collectedSamples_id), 
        add constraint FK1BCDFCC2C02466CD 
        foreign key (collectedSamples_id) 
        references CollectedSample (id);
