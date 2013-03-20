
    create table BloodBagType (
        id TINYINT not null auto_increment,
        bloodBagType varchar(50),
        canPool boolean,
        canSplit boolean,
        isDeleted boolean,
        primary key (id)
    ) ENGINE=InnoDB;

    create table BloodBagType_AUD (
        id TINYINT not null,
        REV integer not null,
        REVTYPE tinyint,
        bloodBagType varchar(50),
        canPool boolean,
        canSplit boolean,
        isDeleted boolean,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table BloodTest (
        id bigint not null auto_increment,
        correctResult varchar(10),
        displayName varchar(30),
        isDeleted boolean,
        isRequired boolean,
        name varchar(30),
        notes longtext,
        validResults varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table BloodTestMapper (
        id TINYINT not null auto_increment,
        name varchar(20),
        pattern varchar(20),
        result varchar(30),
        bloodTest_id bigint not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table BloodTest_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        correctResult varchar(10),
        displayName varchar(30),
        isDeleted boolean,
        isRequired boolean,
        name varchar(30),
        notes longtext,
        validResults varchar(255),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table BloodTypingRule (
        id TINYINT not null auto_increment,
        bloodTypingTestIds varchar(200),
        extraInformation varchar(30),
        extraTestsIds varchar(30),
        isActive boolean,
        markSampleAsUnsafe boolean,
        newInformation varchar(30),
        partOfBloodGroupChanged varchar(12),
        pattern varchar(50),
        primary key (id)
    ) ENGINE=InnoDB;

    create table BloodTypingRule_AUD (
        id TINYINT not null,
        REV integer not null,
        REVTYPE tinyint,
        bloodTypingTestIds varchar(200),
        extraInformation varchar(30),
        extraTestsIds varchar(30),
        isActive boolean,
        markSampleAsUnsafe boolean,
        newInformation varchar(30),
        partOfBloodGroupChanged varchar(12),
        pattern varchar(50),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table BloodTypingTest (
        id TINYINT not null auto_increment,
        bloodTypingTestType varchar(12),
        dataType varchar(10),
        isActive boolean,
        negativeResults varchar(255),
        positiveResults varchar(255),
        rankInCategory integer,
        testName varchar(30),
        testNameShort varchar(15),
        validResults varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table BloodTypingTest_AUD (
        id TINYINT not null,
        REV integer not null,
        REVTYPE tinyint,
        bloodTypingTestType varchar(12),
        dataType varchar(10),
        isActive boolean,
        negativeResults varchar(255),
        positiveResults varchar(255),
        rankInCategory integer,
        testName varchar(30),
        testNameShort varchar(15),
        validResults varchar(255),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table CollectedSample (
        id bigint not null auto_increment,
        bloodAbo varchar(30),
        bloodPressure decimal(6,2),
        bloodRhd varchar(30),
        collectedOn datetime,
        collectionNumber varchar(20),
        donorWeight decimal(6,2),
        haemoglobinCount decimal(6,2),
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        testedStatus varchar(20),
        bloodBagType_id TINYINT,
        collectionBatch_id TINYINT(4),
        collectionCenter_id bigint,
        collectionSite_id bigint,
        donationCreatedBy_id SMALLINT,
        donationType_id TINYINT,
        donor_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table CollectedSample_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        bloodAbo varchar(30),
        bloodPressure decimal(6,2),
        bloodRhd varchar(30),
        collectedOn datetime,
        collectionNumber varchar(20),
        donorWeight decimal(6,2),
        haemoglobinCount decimal(6,2),
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        testedStatus varchar(20),
        bloodBagType_id TINYINT,
        collectionBatch_id TINYINT(4),
        collectionCenter_id bigint,
        collectionSite_id bigint,
        donationCreatedBy_id SMALLINT,
        donationType_id TINYINT,
        donor_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table CollectedSample_CollectionsWorksheet (
        collectedSamples_id bigint not null,
        worksheets_id bigint not null
    ) ENGINE=InnoDB;

    create table CollectedSample_CollectionsWorksheet_AUD (
        REV integer not null,
        collectedSamples_id bigint not null,
        worksheets_id bigint not null,
        REVTYPE tinyint,
        primary key (REV, collectedSamples_id, worksheets_id)
    ) ENGINE=InnoDB;

    create table CollectionBatch (
        id TINYINT(4) not null auto_increment,
        batchNumber varchar(30),
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
        id TINYINT(4) not null,
        REV integer not null,
        REVTYPE tinyint,
        batchNumber varchar(30),
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

    create table CollectionsWorksheet (
        id bigint not null auto_increment,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        worksheetBatchId varchar(255),
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table CollectionsWorksheet_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        worksheetBatchId varchar(255),
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
        crossmatchType_id TINYINT,
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
        crossmatchType_id TINYINT,
        forRequest_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        testedProduct_id bigint,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table ConfiguredPreDonationTest (
        id SMALLINT not null auto_increment,
        acceptableResults varchar(500),
        allowedResults varchar(500),
        enabled boolean,
        lowerLimit varchar(30),
        negateResult boolean,
        resultDataType integer,
        testName varchar(30),
        testType varchar(15),
        units varchar(10),
        upperLimit varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table ConfiguredPreDonationTest_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        acceptableResults varchar(500),
        allowedResults varchar(500),
        enabled boolean,
        lowerLimit varchar(30),
        negateResult boolean,
        resultDataType integer,
        testName varchar(30),
        testType varchar(15),
        units varchar(10),
        upperLimit varchar(30),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table CrossmatchType (
        id TINYINT not null auto_increment,
        crossmatchType varchar(255),
        isDeleted boolean,
        primary key (id)
    ) ENGINE=InnoDB;

    create table CrossmatchType_AUD (
        id TINYINT not null,
        REV integer not null,
        REVTYPE tinyint,
        crossmatchType varchar(255),
        isDeleted boolean,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table DeferralReason (
        id SMALLINT not null auto_increment,
        details varchar(255),
        reason varchar(50),
        primary key (id)
    ) ENGINE=InnoDB;

    create table DeferralReason_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        details varchar(255),
        reason varchar(50),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table DonationType (
        id TINYINT not null auto_increment,
        donationType varchar(50),
        isDeleted boolean,
        primary key (id)
    ) ENGINE=InnoDB;

    create table DonationType_AUD (
        id TINYINT not null,
        REV integer not null,
        REVTYPE tinyint,
        donationType varchar(50),
        isDeleted boolean,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Donor (
        id bigint not null auto_increment,
        birthDate date,
        birthDateInferred date,
        bloodAbo varchar(10),
        bloodRhd varchar(10),
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
        donorNumber varchar(15),
        firstName varchar(20),
        gender varchar(15),
        isDeleted boolean,
        lastName varchar(20),
        middleName varchar(20),
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        nationalID varchar(15),
        notes longtext,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table DonorDeferral (
        id bigint not null auto_increment,
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
        birthDate date,
        birthDateInferred date,
        bloodAbo varchar(10),
        bloodRhd varchar(10),
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
        donorNumber varchar(15),
        firstName varchar(20),
        gender varchar(15),
        isDeleted boolean,
        lastName varchar(20),
        middleName varchar(20),
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        nationalID varchar(15),
        notes longtext,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id, REV)
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

    create table Location_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        isCenter boolean,
        isCollectionSite boolean,
        isDeleted boolean,
        isMobileSite boolean,
        isUsageSite boolean,
        name varchar(255),
        notes longtext,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table MicrotiterPlate (
        id TINYINT not null auto_increment,
        isDeleted boolean,
        notes longtext,
        numColumns SMALLINT,
        numRows SMALLINT,
        plateKey varchar(15) unique,
        plateName varchar(20),
        primary key (id)
    ) ENGINE=InnoDB;

    create table MicrotiterPlate_AUD (
        id TINYINT not null,
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

    create table PlateContent (
        id TINYINT not null auto_increment,
        colNumber SMALLINT,
        contentType varchar(15),
        coverage varchar(15),
        isDeleted boolean,
        rowNumber SMALLINT,
        plateForContent_id TINYINT,
        rawBloodTest_id TINYINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table PlateContent_AUD (
        id TINYINT not null,
        REV integer not null,
        REVTYPE tinyint,
        colNumber SMALLINT,
        contentType varchar(15),
        coverage varchar(15),
        isDeleted boolean,
        rowNumber SMALLINT,
        plateForContent_id TINYINT,
        rawBloodTest_id TINYINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table PreDonationTest (
        id bigint not null auto_increment,
        testResult varchar(30),
        configuredPreDonationTest_id SMALLINT not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table PreDonationTest_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        testResult varchar(30),
        configuredPreDonationTest_id SMALLINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Product (
        id bigint not null auto_increment,
        bloodAbo varchar(30),
        bloodRhd varchar(30),
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
        discardReason_id SMALLINT,
        issuedTo_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        productType_id TINYINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductReturnReason (
        id bigint not null auto_increment,
        reasonDetails longtext,
        returnReason varchar(150),
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductStatusChange (
        id bigint not null auto_increment,
        newStatus varchar(30),
        notes longtext,
        statusChangedOn datetime,
        changedBy_id SMALLINT,
        issuedTo_id bigint,
        product_id bigint,
        statusChangeReason_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductStatusChangeReason (
        id SMALLINT not null auto_increment,
        isDeleted boolean,
        statusChangeReason varchar(100),
        category_id TINYINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductStatusChangeReasonCategory (
        id TINYINT not null,
        category varchar(20) not null,
        isDeleted boolean,
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductStatusChangeReasonCategory_AUD (
        id TINYINT not null,
        REV integer not null,
        REVTYPE tinyint,
        category varchar(20),
        isDeleted boolean,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table ProductStatusChangeReason_AUD (
        id SMALLINT not null,
        REV integer not null,
        REVTYPE tinyint,
        isDeleted boolean,
        statusChangeReason varchar(100),
        category_id TINYINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table ProductStatusChange_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        newStatus varchar(30),
        notes longtext,
        statusChangedOn datetime,
        changedBy_id SMALLINT,
        issuedTo_id bigint,
        product_id bigint,
        statusChangeReason_id SMALLINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table ProductType (
        id TINYINT not null auto_increment,
        canPool boolean,
        canSubdivide boolean,
        description longtext,
        expiresAfter integer,
        expiresAfterUnits varchar(30),
        hasBloodGroup boolean,
        isDeleted boolean,
        productType varchar(50),
        productTypeShortName varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table ProductType_AUD (
        id TINYINT not null,
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
        productTypeShortName varchar(30),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table ProductUsage (
        id bigint not null auto_increment,
        hospital varchar(255),
        isAvailable boolean,
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        patientName varchar(30),
        usageDate datetime,
        useIndication varchar(30),
        usedBy varchar(255),
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
        hospital varchar(255),
        isAvailable boolean,
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        patientName varchar(30),
        usageDate datetime,
        useIndication varchar(30),
        usedBy varchar(255),
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
        bloodAbo varchar(30),
        bloodRhd varchar(30),
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
        discardReason_id SMALLINT,
        issuedTo_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        productType_id TINYINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table REVINFO (
        REV integer not null auto_increment,
        REVTSTMP bigint,
        primary key (REV)
    ) ENGINE=InnoDB;

    create table RawBloodTestGroup (
        id TINYINT not null auto_increment,
        isDeleted boolean,
        testGroupName varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table RawBloodTestGroup_AUD (
        id TINYINT not null,
        REV integer not null,
        REVTYPE tinyint,
        isDeleted boolean,
        testGroupName varchar(30),
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table RawBloodTestGroup_BloodTypingTest (
        RawBloodTestGroup_id TINYINT not null,
        bloodTestsInGroup_id TINYINT not null
    ) ENGINE=InnoDB;

    create table RawBloodTestGroup_BloodTypingTest_AUD (
        REV integer not null,
        RawBloodTestGroup_id TINYINT not null,
        bloodTestsInGroup_id TINYINT not null,
        REVTYPE tinyint,
        primary key (REV, RawBloodTestGroup_id, bloodTestsInGroup_id)
    ) ENGINE=InnoDB;

    create table RawTestResult (
        id TINYINT not null auto_increment,
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        rawTestResult varchar(10),
        inferredTestResult_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        rawBloodTest_id TINYINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table RawTestResult_AUD (
        id TINYINT not null,
        REV integer not null,
        REVTYPE tinyint,
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        rawTestResult varchar(10),
        inferredTestResult_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        rawBloodTest_id TINYINT,
        primary key (id, REV)
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
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        productType_id TINYINT,
        requestSite_id bigint,
        requestType_id TINYINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table RequestType (
        id TINYINT not null auto_increment,
        description varchar(100),
        isDeleted boolean,
        requestType varchar(30),
        primary key (id)
    ) ENGINE=InnoDB;

    create table RequestType_AUD (
        id TINYINT not null,
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
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        productType_id TINYINT,
        requestSite_id bigint,
        requestType_id TINYINT,
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

    create table TestResult (
        id bigint not null auto_increment,
        extraInformation varchar(150),
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        result varchar(30),
        testedOn datetime,
        bloodTest_id bigint,
        collectedSample_id bigint not null,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id)
    ) ENGINE=InnoDB;

    create table TestResult_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        extraInformation varchar(150),
        isDeleted boolean,
        createdDate TIMESTAMP,
        lastUpdated TIMESTAMP,
        notes longtext,
        result varchar(30),
        testedOn datetime,
        bloodTest_id bigint,
        collectedSample_id bigint,
        createdBy_id SMALLINT,
        lastUpdatedBy_id SMALLINT,
        primary key (id, REV)
    ) ENGINE=InnoDB;

    create table Tips (
        tipsKey varchar(255) not null,
        isDeleted boolean,
        tipsContent varchar(512),
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

    alter table BloodBagType_AUD 
        add index FKE16C6DF9DF74E053 (REV), 
        add constraint FKE16C6DF9DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table BloodTestMapper 
        add index FK30D262ED4FE62E13 (bloodTest_id), 
        add constraint FK30D262ED4FE62E13 
        foreign key (bloodTest_id) 
        references BloodTest (id);

    alter table BloodTest_AUD 
        add index FKE1FA995DDF74E053 (REV), 
        add constraint FKE1FA995DDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table BloodTypingRule_AUD 
        add index FKEDC605DEDF74E053 (REV), 
        add constraint FKEDC605DEDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table BloodTypingTest_AUD 
        add index FK7BE9F874DF74E053 (REV), 
        add constraint FK7BE9F874DF74E053 
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

    alter table CollectedSample_CollectionsWorksheet_AUD 
        add index FKC0D7E0D6DF74E053 (REV), 
        add constraint FKC0D7E0D6DF74E053 
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

    alter table CollectionsWorksheet_AUD 
        add index FK5296084ADF74E053 (REV), 
        add constraint FK5296084ADF74E053 
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

    alter table ConfiguredPreDonationTest_AUD 
        add index FK564E4C9ADF74E053 (REV), 
        add constraint FK564E4C9ADF74E053 
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

    alter table MicrotiterPlate_AUD 
        add index FK45D4695FDF74E053 (REV), 
        add constraint FK45D4695FDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table PlateContent 
        add index FK8E9BC0A34BAEB3B7 (rawBloodTest_id), 
        add constraint FK8E9BC0A34BAEB3B7 
        foreign key (rawBloodTest_id) 
        references BloodTypingTest (id);

    alter table PlateContent 
        add index FK8E9BC0A3DC9AD7D (plateForContent_id), 
        add constraint FK8E9BC0A3DC9AD7D 
        foreign key (plateForContent_id) 
        references MicrotiterPlate (id);

    alter table PlateContent_AUD 
        add index FK63E0E4F4DF74E053 (REV), 
        add constraint FK63E0E4F4DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table PreDonationTest 
        add index FKAF309676903E59D (configuredPreDonationTest_id), 
        add constraint FKAF309676903E59D 
        foreign key (configuredPreDonationTest_id) 
        references ConfiguredPreDonationTest (id);

    alter table PreDonationTest_AUD 
        add index FKDE1E2BB8DF74E053 (REV), 
        add constraint FKDE1E2BB8DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    create index product_expiresOn_index on Product (expiresOn);

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
        add index FK50C664CFF00A1C69 (discardReason_id), 
        add constraint FK50C664CFF00A1C69 
        foreign key (discardReason_id) 
        references ProductStatusChangeReason (id);

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
        add index FKCCE48CB1E8B67365 (statusChangeReason_id), 
        add constraint FKCCE48CB1E8B67365 
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

    alter table ProductStatusChangeReason 
        add index FK9F250255107C079A (category_id), 
        add constraint FK9F250255107C079A 
        foreign key (category_id) 
        references ProductStatusChangeReasonCategory (id);

    alter table ProductStatusChangeReasonCategory_AUD 
        add index FK6A6ABAC4DF74E053 (REV), 
        add constraint FK6A6ABAC4DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

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

    alter table RawBloodTestGroup_AUD 
        add index FK1AF926ACDF74E053 (REV), 
        add constraint FK1AF926ACDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table RawBloodTestGroup_BloodTypingTest 
        add index FK778B723FF67F16D8 (RawBloodTestGroup_id), 
        add constraint FK778B723FF67F16D8 
        foreign key (RawBloodTestGroup_id) 
        references RawBloodTestGroup (id);

    alter table RawBloodTestGroup_BloodTypingTest 
        add index FK778B723FBC4C2D88 (bloodTestsInGroup_id), 
        add constraint FK778B723FBC4C2D88 
        foreign key (bloodTestsInGroup_id) 
        references BloodTypingTest (id);

    alter table RawBloodTestGroup_BloodTypingTest_AUD 
        add index FK4266890DF74E053 (REV), 
        add constraint FK4266890DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table RawTestResult 
        add index FK61DC7577A49787C4 (createdBy_id), 
        add constraint FK61DC7577A49787C4 
        foreign key (createdBy_id) 
        references User (id);

    alter table RawTestResult 
        add index FK61DC7577C6F28866 (inferredTestResult_id), 
        add constraint FK61DC7577C6F28866 
        foreign key (inferredTestResult_id) 
        references TestResult (id);

    alter table RawTestResult 
        add index FK61DC75774BAEB3B7 (rawBloodTest_id), 
        add constraint FK61DC75774BAEB3B7 
        foreign key (rawBloodTest_id) 
        references BloodTypingTest (id);

    alter table RawTestResult 
        add index FK61DC7577D0AFB367 (lastUpdatedBy_id), 
        add constraint FK61DC7577D0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table RawTestResult_AUD 
        add index FK8B530FC8DF74E053 (REV), 
        add constraint FK8B530FC8DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

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
        add index FKDB459F6FD0AFB367 (lastUpdatedBy_id), 
        add constraint FKDB459F6FD0AFB367 
        foreign key (lastUpdatedBy_id) 
        references User (id);

    alter table TestResult 
        add index FKDB459F6F4FE62E13 (bloodTest_id), 
        add constraint FKDB459F6F4FE62E13 
        foreign key (bloodTest_id) 
        references BloodTest (id);

    alter table TestResult_AUD 
        add index FK77A67DC0DF74E053 (REV), 
        add constraint FK77A67DC0DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table User_AUD 
        add index FKF3FCA03CDF74E053 (REV), 
        add constraint FKF3FCA03CDF74E053 
        foreign key (REV) 
        references REVINFO (REV);
