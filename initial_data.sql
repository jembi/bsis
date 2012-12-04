insert ignore into User (id,username,password,firstname,isAdmin,isStaff,isActive,isDeleted) values
(1,'admin','admin321123','admin',1,1,1,0);

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, derived, sourceField) values
('Donor', 'donorNumber', 'Donor Number', '', '0', '0', ''),
('Donor', 'firstName', 'First Name', '', '0', '0', ''),
('Donor', 'middleName', 'Middle Name', '', '1', '0', ''),
('Donor', 'lastName', 'Last Name', '', '0', '0', ''),
('Donor', 'birthDate', 'Birth Date', '', '0', '0', ''),
('Donor', 'gender', 'Gender', '', '0', '0', ''),
('Donor', 'firstName', 'First Name', '', '0', '0', ''),
('Donor', 'bloodGroup', 'Blood Group', '', '0', '0', ''),
('Donor', 'address', 'Address', '', '0', '0', ''),
('Donor', 'city', 'City', 'Lusaka', '0', '0', ''),
('Donor', 'state', 'State', '', '0', '0', ''),
('Donor', 'country', 'Country', 'Zambia', '0', '0', ''),
('Donor', 'zipcode', 'Zip Code', '', '0', '0', ''),
('Donor', 'notes', 'Notes', '', '0', '0', '');

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, derived, sourceField) values
('CollectedSample', 'collectionNumber', 'Collection Number', '', '0', '0', ''),
('CollectedSample', 'collectedOn', 'Collected On', '', '0', '0', ''),
('CollectedSample', 'donor', 'Donor', '', '0', '0', ''),
('CollectedSample', 'donorType', 'Donor Type', '', '0', '0', ''),
('CollectedSample', 'shippingNumber', 'Shipping Number', '', '0', '1', 'collectionNumber'),
('CollectedSample', 'sampleNumber', 'Sample Number', '', '0', '1', 'collectionNumber'),
('CollectedSample', 'collectionCenter', 'Collection Center', '', '0', '0', ''),
('CollectedSample', 'bloodBagType', 'Blood Bag Type', '', '0', '0', ''),
('CollectedSample', 'collectionSite', 'Collection Site', '', '0', '0', ''),
('CollectedSample', 'notes', 'Notes', '', '0', '0', '');

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, derived, sourceField) values
('TestResult', 'collectionNumber', 'Collection Number', '', '0', '0', ''),
('TestResult', 'name', 'Test Name', '', '0', '0', ''),
('TestResult', 'result', 'Test Result', '', '0', '0', ''),
('TestResult', 'testedOn', 'Tested On', '', '0', '0', ''),
('TestResult', 'notes', 'Notes', '', '0', '0', '');

insert into DonorType (donorType) values ('Voluntary'), ('Family'), ('Other');

insert into BloodBagType (bloodBagType) values ('Single'), ('Triple'), ('Paedi');

insert into Location(name, isCenter, isCollectionSite, isMobileSite, isUsageSite, isDeleted, notes) values
('Lusaka', 1, 0, 0, 0, 0, ''),
('Ndola', 1, 1, 0, 0, 0, ''),
('Livingstone', 1, 1, 0, 0, 0, ''),
('Luanshya', 1, 0, 0, 0, 0, ''),
('Kasama', 1, 1, 0, 0, 0, ''),
('Chipata', 1, 0, 0, 0, 0, ''),
('Chingola', 1, 1, 0, 0, 0, '');

insert into BloodTest (id, name, correctResult, isRequired, notes) values
(1, 'HIV', 'negative', '1', ''),
(2, 'HBV', 'negative', '1', ''),
(3, 'HCV', 'negative', '1', ''),
(4, 'Syphilis', 'negative', '1', '');

insert into BloodTestAllowedResults(bloodTest_id, result) values
(1, 'positive'),
(1, 'negative'),
(2, 'positive'),
(2, 'negative'),
(3, 'positive'),
(3, 'negative'),
(4, 'positive'),
(4, 'negative');
