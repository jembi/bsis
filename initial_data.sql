insert ignore into User (username,password,firstname,isAdmin,isStaff,isActive,isDeleted) values
('admin','admin321123','admin',1,1,1,0),
('test','test','technician',0,1,1,0);

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, autoGenerate, derived, sourceField) values
('Donor', 'donorNumber', 'Donor Number', '', '0', '1', '0', '0', ''),
('Donor', 'firstName', 'First Name', '', '0', '1', '0', '0', ''),
('Donor', 'middleName', 'Middle Name', '', '1', '0', '0', '0', ''),
('Donor', 'lastName', 'Last Name', '', '0', '0', '0', '0', ''),
('Donor', 'birthDate', 'Birth Date', '', '0', '0', '0', '0', ''),
('Donor', 'gender', 'Gender', '', '0', '0', '0', '0', ''),
('Donor', 'firstName', 'First Name', '', '0', '0', '0', '0', ''),
('Donor', 'bloodGroup', 'Blood Group', '', '0', '0', '0', '0', ''),
('Donor', 'address', 'Address', '', '0', '0', '0', '0', ''),
('Donor', 'city', 'City', 'Lusaka', '0', '0', '0', '0', ''),
('Donor', 'state', 'State', '', '1', '0', '0', '0', ''),
('Donor', 'country', 'Country', 'Zambia', '0', '0', '0', '0', ''),
('Donor', 'zipcode', 'Zip Code', '', '0', '0', '0', '0', ''),
('Donor', 'notes', 'Notes', '', '0', '0', '0', '0', '');

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, autoGenerate, derived, sourceField) values
('CollectedSample', 'collectionNumber', 'Collection Number', '', '0', '1', '1', '0', ''),
('CollectedSample', 'collectedOn', 'Collected On', '', '0', '0', '0', '0', ''),
('CollectedSample', 'donor', 'Donor', '', '0', '0', '0', '0', ''),
('CollectedSample', 'donorType', 'Donor Type', '', '0', '0', '0', '0', ''),
('CollectedSample', 'shippingNumber', 'Shipping Number', '', '0', '0', '0', '1', 'collectionNumber'),
('CollectedSample', 'sampleNumber', 'Sample Number', '', '0', '0', '0', '1', 'collectionNumber'),
('CollectedSample', 'collectionCenter', 'Collection Center', '', '0', '0', '0', '0', ''),
('CollectedSample', 'bloodBagType', 'Blood Bag Type', '', '0', '0', '0', '0', ''),
('CollectedSample', 'collectionSite', 'Collection Site', '', '0', '0', '0', '0', ''),
('CollectedSample', 'notes', 'Notes', '', '0', '0', '0', '0', '');

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, autoGenerate, derived, sourceField) values
('Product', 'productNumber', 'Product Number', '', '0', '1', '0', '0', ''),
('Product', 'collectionNumber', 'Collection Number', '', '0', '1', '0', '0', ''),
('Product', 'productType', 'Product Type', '', '0', '1', '0', '0', ''),
('Product', 'bloodGroup', 'Blood Group', '', '1', '0', '0', '0', ''),
('Product', 'createdOn', 'Created On', '', '0', '1', '0', '0', ''),
('Product', 'expiresOn', 'Expires On', '', '0', '1', '0', '0', ''),
('Product', 'isQuarantined', 'Quarantined', '', '0', '0', '0', '0', ''),
('Product', 'isAvailable', 'Available', '', '0', '0', '0', '0', ''),
('Product', 'notes', 'Notes', '', '0', '0', '0', '0', '');

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, autoGenerate, derived, sourceField) values
('TestResult', 'collectionNumber', 'Collection Number', '', '0', '1', '0', '0', ''),
('TestResult', 'bloodTest', 'Test Name', '', '0', '0', '0', '0', ''),
('TestResult', 'result', 'Test Result', '', '0', '0', '0', '0', ''),
('TestResult', 'testedOn', 'Tested On', '', '0', '0', '0', '0', ''),
('TestResult', 'notes', 'Notes', '', '0', '0', '0', '0', '');

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, autoGenerate, derived, sourceField) values
('Request', 'requestNumber', 'Request Number', '', '1', '0', '0', '0', ''),
('Request', 'requestDate', 'Request Date', '', '0', '0', '0', '0', ''),
('Request', 'requiredDate', 'Required Date', '', '0', '0', '0', '0', ''),
('Request', 'requestSite', 'Request Site', '', '0', '1', '0', '0', ''),
('Request', 'requestedQuantity', 'Requested Quantity', '', '0', '1', '0', '0', ''),
('Request', 'issuedQuantity', 'Issued Quantity', '', '0', '0', '0', '0', ''),
('Request', 'requestStatus', 'Request Status', '', '0', '0', '0', '0', ''),
('Request', 'bloodGroup', 'Blood Group', '', '0', '1', '0', '0', ''),
('Request', 'productType', 'Product Type', '', '0', '1', '0', '0', ''),
('Request', 'patientName', 'Patient Name', '', '0', '0', '0', '0', ''),
('Request', 'notes', 'Notes', '', '0', '0', '0', '0', '');

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, autoGenerate, derived, sourceField) values
('Usage', 'hospital', 'Hospital', '', '0', '0', '0', '0', ''),
('Usage', 'patientName', 'Patient Name', '', '0', '0', '0', '0', ''),
('Usage', 'ward', 'Ward', '', '0', '0', '0', '0', ''),
('Usage', 'useIndication', 'Use Indication', '', '0', '0', '0', '0', ''),
('Usage', 'usageDate', 'Usage Date', '', '0', '0', '0', '0', ''),
('Usage', 'product', 'Product', '', '0', '0', '0', '0', ''),
('Usage', 'notes', 'Notes', '', '0', '0', '0', '0', '');

insert into DonorType (donorType, isDeleted) values ('Voluntary', '0'), ('Family', '0'), ('Other', '0');

insert into ProductType (productType, isDeleted) values ('Whole Blood', '0'), ('RCC', '0'), ('FFP', '0'), ('Platelets', '0'), ('Partial Platelets', '0');

insert into BloodBagType (bloodBagType, isDeleted) values ('Single', '0'), ('Triple', '0'), ('Paedi', '0');

insert into Location(name, isCenter, isCollectionSite, isMobileSite, isUsageSite, isDeleted, notes) values
('Lusaka', 1, 0, 0, 0, 0, ''),
('Ndola', 1, 1, 0, 1, 0, ''),
('Livingstone', 1, 1, 0, 1, 0, ''),
('Luanshya', 1, 0, 0, 0, 0, ''),
('Kasama', 1, 1, 0, 1, 0, ''),
('Chipata', 1, 0, 0, 0, 0, ''),
('Chingola', 1, 1, 0, 1, 0, '');

insert into BloodTest (name, correctResult, allowedResults, isRequired, notes) values
('HIV', 'negative', 'positive,negative', '1', ''),
('HBV', 'negative', 'positive,negative', '1', ''),
('HCV', 'negative', 'positive,negative', '1', ''),
('Syphilis', 'negative', 'positive,negative', '1', ''),
('Blood ABO', '', 'A,B,AB,O', '1', ''),
('Blood Rh', '', 'POSITIVE,NEGATIVE', '1', '');
