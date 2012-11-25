insert ignore into User (id,username,password,firstname,isAdmin,isStaff,isActive,isDeleted) values
(1,'admin','admin321123','admin',1,1,1,0);
insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, derived, sourceField) values
('Donor', 'donorNumber', 'Donor Number', '', '0', '0', ''),
('Donor', 'firstName', 'First Name', '', '0', '0', ''),
('Donor', 'middleName', 'Middle Name', '', '0', '0', ''),
('Donor', 'lastName', 'Last Name', '', '0', '0', ''),
('Donor', 'birthDate', 'Birth Date', '', '0', '0', ''),
('Donor', 'gender', 'Gender', '', '0', '0', ''),
('Donor', 'firstName', 'First Name', '', '0', '0', ''),
('Donor', 'bloodGroup', 'Blood Group', '', '0', '0', ''),
('Donor', 'address', 'Address', '', '0', '0', ''),
('Donor', 'city', 'City', '', '0', '0', ''),
('Donor', 'state', 'State', '', '0', '0', ''),
('Donor', 'country', 'Country', '', '0', '0', ''),
('Donor', 'zipcode', 'Zip Code', '', '0', '0', ''),
('Donor', 'notes', 'Notes', '', '0', '0', '');
insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, derived, sourceField) values
('CollectedSample', 'collectionNumber', 'Collection Number', '', '0', '0', ''),
('CollectedSample', 'donor', 'Donor', '', '0', '0', ''),
('CollectedSample', 'donorType', 'Donor Type', '', '0', '0', ''),
('CollectedSample', 'shippingNumber', 'Shipping Number', '', '0', '1', 'collectionNumber'),
('CollectedSample', 'sampleNumber', 'sampleNumber', '', '0', '1', 'collectionNumber'),
('CollectedSample', 'collectionCenter', 'Center', '', '0', '0', ''),
('CollectedSample', 'bloodBagType', 'Blood Bag Type', '', '0', '0', ''),
('CollectedSample', 'collectionSite', 'Collection Site', '', '0', '0', ''),
('CollectedSample', 'notes', 'Notes', '', '0', '0', '');
