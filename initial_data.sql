insert ignore into User (username,password,firstname,isAdmin,isStaff,isActive,isDeleted) values
('admin','admin321123','admin',1,1,1,0),
('test','test','technician',0,1,1,0);

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, isAutoGeneratable, autoGenerate, derived, sourceField, maxLength) values
('Donor', 'donorNumber', 'Donor Number', '', '0', '1', '1', '0', '0', '', 15),
('Donor', 'firstName', 'First Name', '', '0', '1', '0', '0', '0', '', 0),
('Donor', 'middleName', 'Middle Name', '', '1', '0', '0', '0', '0', '', 0),
('Donor', 'lastName', 'Last Name', '', '0', '0', '0', '0', '0', '', 0),
('Donor', 'birthDate', 'Birth Date', '', '0', '0', '0', '0', '0', '', 0),
('Donor', 'gender', 'Gender', '', '0', '0', '0', '0', '0', '', 0),
('Donor', 'bloodGroup', 'Blood Group', '', '0', '0', '0', '0', '0', '', 0),
('Donor', 'address', 'Address', '', '0', '0', '0', '0', '0', '', 0),
('Donor', 'city', 'City', '', '0', '0', '0', '0', '0', '', 0),
('Donor', 'province', 'Province', '', '0', '0', '0', '0', '0', '', 0),
('Donor', 'district', 'District', '', '0', '0', '0', '0', '0', '', 0),
('Donor', 'state', 'State', '', '1', '0', '0', '0', '0', '', 0),
('Donor', 'country', 'Country', '', '0', '0', '0', '0', '0', '', 0),
('Donor', 'zipcode', 'Zip Code', '', '0', '0', '0', '0', '0', '', 0),
('Donor', 'notes', 'Notes', '', '0', '0', '0', '0', '0', '', 0),
('Donor', 'lastUpdatedTime', 'Last Modified On', '', '0', '0', '0', '0', '0', '', 0),
('Donor', 'lastUpdatedBy', 'Last Modified By', '', '0', '0', '0', '0', '0', '', 0);

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, isAutoGeneratable, autoGenerate, derived, sourceField, maxLength) values
('CollectedSample', 'collectionNumber', 'Collection Number', '', '0', '1', '1', '1', '0', '', 0),
('CollectedSample', 'collectedOn', 'Collected On', '', '0', '1', '0', '0', '0', '', 0),
('CollectedSample', 'donorNumber', 'Donor Number', '', '0', '0', '0', '0', '0', '', 0),
('CollectedSample', 'donorType', 'Donor Type', '', '0', '1', '0', '0', '0', '', 0),
('CollectedSample', 'shippingNumber', 'Shipping Number', '', '1', '0', '0', '0', '1', 'collectionNumber', 0),
('CollectedSample', 'sampleNumber', 'Sample Number', '', '1', '0', '0', '0', '1', 'collectionNumber', 0),
('CollectedSample', 'collectionCenter', 'Collection Center', '', '0', '1', '0', '0', '0', '', 0),
('CollectedSample', 'bloodBagType', 'Blood Bag Type', '', '0', '1', '0', '0', '0', '', 0),
('CollectedSample', 'collectionSite', 'Collection Site', '', '0', '1', '0', '0', '0', '', 0),
('CollectedSample', 'notes', 'Notes', '', '0', '0', '0', '0', '0', '', 0),
('CollectedSample', 'lastUpdatedTime', 'Last Modified On', '', '0', '0', '0', '0', '0', '', 0),
('CollectedSample', 'lastUpdatedBy', 'Last Modified By', '', '0', '0', '0', '0', '0', '', 0),
('CollectedSample', 'testedStatus', 'Tested', '', '0', '0', '0', '0', '0', '', 0);


insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, isAutoGeneratable, autoGenerate, derived, sourceField, maxLength) values
('Product', 'productNumber', 'Product Number', '', '0', '1', '0', '0', '0', '', 0),
('Product', 'collectionNumber', 'Collection Number', '', '0', '1', '0', '0', '0', '', 0),
('Product', 'productType', 'Product Type', '', '0', '1', '0', '0', '0', '', 0),
('Product', 'createdOn', 'Created On', '', '0', '1', '0', '0', '0', '', 0),
('Product', 'issuedOn', 'Issued On', '', '0', '1', '0', '0', '0', '', 0),
('Product', 'expiresOn', 'Expires On', '', '0', '1', '0', '0', '0', '', 0),
('Product', 'status', 'Status', '', '0', '0', '0', '0', '0', '', 0),
('Product', 'bloodGroup', 'Blood Group', '', '0', '1', '0', '0', '0', '', 0),
('Product', 'age', 'Age', '', '0', '0', '0', '0', '0', '', 0),
('Product', 'notes', 'Notes', '', '0', '0', '0', '0', '0', '', 0),
('Product', 'issuedTo', 'Issued to request', '', '0', '0', '0', '0', '0', '', 0),
('Product', 'issuedBy', 'Issued by', '', '0', '0', '0', '0', '0', '', 0),
('Product', 'discardedOn', 'Discarded on', '', '0', '0', '0', '0', '0', '', 0),
('Product', 'discardedBy', 'Discarded by', '', '0', '0', '0', '0', '0', '', 0),
('Product', 'lastUpdatedTime', 'Last Modified On', '', '0', '0', '0', '0', '0', '', 0),
('Product', 'lastUpdatedBy', 'Last Modified By', '', '0', '0', '0', '0', '0', '', 0);

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, isAutoGeneratable, autoGenerate, derived, sourceField, maxLength) values
('TestResult', 'collectionNumber', 'Collection Number', '', '0', '1', '0', '0', '0', '', 0),
('TestResult', 'bloodTest', 'Test Name', '', '0', '0', '0', '0', '0', '', 0),
('TestResult', 'result', 'Test Result', '', '0', '0', '0', '0', '0', '', 0),
('TestResult', 'testedOn', 'Tested On', '', '0', '0', '0', '0', '0', '', 0),
('TestResult', 'notes', 'Notes', '', '0', '0', '0', '0', '0', '', 0),
('TestResult', 'lastUpdatedTime', 'Last Modified On', '', '0', '0', '0', '0', '0', '', 0),
('TestResult', 'lastUpdatedBy', 'Last Modified By', '', '0', '0', '0', '0', '0', '', 0);

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, isAutoGeneratable, autoGenerate, derived, sourceField, maxLength) values
('Request', 'requestNumber', 'Request Number', '', '0', '1', '1', '1', '0', '', 0),
('Request', 'requestDate', 'Request Date', '', '0', '1', '0', '0', '0', '', 0),
('Request', 'requiredDate', 'Required Date', '', '0', '1', '0', '0', '0', '', 0),
('Request', 'requestSite', 'Request Site', '', '0', '1', '0', '0', '0', '', 0),
('Request', 'numUnitsRequested', 'No. of Units Requested', '', '0', '1', '0', '0', '0', '', 0),
('Request', 'numUnitsIssued', 'No. of Units Issued', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'requestStatus', 'Request Status', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'bloodGroup', 'Blood Group', '', '0', '1', '0', '0', '0', '', 0),
('Request', 'productType', 'Product Type', '', '0', '1', '0', '0', '0', '', 0),
('Request', 'requestType', 'Request Type', '', '0', '1', '0', '0', '0', '', 0),
('Request', 'patientNumber', 'Patient Number', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'patientFirstName', 'Patient First Name', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'patientLastName', 'Patient Last Name', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'patientDiagnosis', 'Patient Diagnosis', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'patientBirthDate', 'Patient Date of Birth', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'patientAge', 'Patient Age', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'patientGender', 'Patient Gender', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'ward', 'Ward', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'department', 'Department', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'hospital', 'Hospital', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'requestedBy', 'Requested By', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'notes', 'Notes', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'lastUpdatedTime', 'Last Modified On', '', '0', '0', '0', '0', '0', '', 0),
('Request', 'lastUpdatedBy', 'Last Modified By', '', '0', '0', '0', '0', '0', '', 0);

insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, isAutoGeneratable, autoGenerate, derived, sourceField, maxLength) values
('CompatibilityTest', 'productNumber', 'Product number', '', '0', '1', '0', '0', '0', '', 0),
('CompatibilityTest', 'requestNumber', 'Request number', '', '0', '1', '0', '0', '0', '', 0),
('CompatibilityTest', 'compatibilityResult', 'Compatibility result', '', '0', '1', '0', '0', '0', '', 0),
('CompatibilityTest', 'compatibilityTestDate', 'Compatibility test date', '', '0', '0', '0', '0', '0', '', 0),
('CompatibilityTest', 'transfusedBefore', 'Transfused before', '', '0', '0', '0', '0', '0', '', 0),
('CompatibilityTest', 'crossmatchType', 'Crossmatch type', '', '0', '0', '0', '0', '0', '', 0),
('CompatibilityTest', 'testedBy', 'Tested by', '', '0', '0', '0', '0', '0', '', 0),
('CompatibilityTest', 'notes', 'Notes', '', '0', '0', '0', '0', '0', '', 0),
('CompatibilityTest', 'lastUpdatedTime', 'Last Modified On', '', '0', '0', '0', '0', '0', '', 0),
('CompatibilityTest', 'lastUpdatedBy', 'Last Modified By', '', '0', '0', '0', '0', '0', '', 0);


insert into FormField(form, field, defaultDisplayName, defaultValue, hidden, isRequired, isAutoGeneratable, autoGenerate, derived, sourceField, maxLength) values
('Usage', 'hospital', 'Hospital', '', '0', '1', '0', '0', '0', '', 0),
('Usage', 'patientName', 'Patient Name', '', '0', '0', '0', '0', '0', '', 0),
('Usage', 'ward', 'Ward', '', '0', '0', '0', '0', '0', '', 0),
('Usage', 'useIndication', 'Use Indication', '', '0', '0', '0', '0', '0', '', 0),
('Usage', 'usageDate', 'Usage Date', '', '0', '1', '0', '0', '0', '', 0),
('Usage', 'product', 'Product', '', '0', '0', '0', '0', '0', '', 0),
('Usage', 'notes', 'Notes', '', '0', '0', '0', '0', '0', '', 0),
('Usage', 'lastUpdatedTime', 'Last Modified On', '', '0', '0', '0', '0', '0', '', 0),
('Usage', 'lastUpdatedBy', 'Last Modified By', '', '0', '0', '0', '0', '0', '', 0);

insert into DonorType (donorType, isDeleted) values
('Voluntary', '0'),
('Family', '0'),
('Other', '0');

insert into RequestType (requestType, description, isDeleted) values
('Emergency', '', '0'),
('Group and Save', '', '0'),
('Group and Screen', '', '0'),
('Standard', '', '0'),
('Urgent', '', '0'),
('Elective Surgery', '', '0');

insert into ProductType (productType, description, shelfLife, shelfLifeUnits, isDeleted) values
('Whole Blood', '', 35, 'days', '0'),
('Red Blood Cells', '', 35, 'days', '0'),
('Fresh Frozen Plasma', '', 35, 'days', '0'),
('Platelets', '', 5, 'days', '0'),
('Cryoprecipitate', '', 35, 'days', '0'),
('Partial Platelets', '', 35, 'days', '0');

insert into BloodBagType (bloodBagType, isDeleted) values
('Single', '0'),
('Triple', '0'),
('Pedi', '0');

insert into Location(name, isCenter, isCollectionSite, isMobileSite, isUsageSite, isDeleted, notes) values
('Lusaka', 1, 0, 0, 0, 0, ''),
('Ndola', 1, 1, 0, 1, 0, ''),
('Livingstone', 1, 1, 0, 1, 0, ''),
('Luanshya', 1, 0, 0, 0, 0, ''),
('Kasama', 1, 1, 0, 1, 0, ''),
('Chipata', 1, 0, 0, 0, 0, ''),
('Chingola', 1, 1, 0, 1, 0, '');

insert into BloodTest (name, correctResult, allowedResults, isRequired, isDeleted, notes) values
('HIV', 'negative', 'positive,negative', '1', '0', ''),
('HBV', 'negative', 'positive,negative', '1', '0', ''),
('HCV', 'negative', 'positive,negative', '1', '0', ''),
('Syphilis', 'negative', 'positive,negative', '1', '0', ''),
('Blood ABO', '', 'A,B,AB,O', '0', '0', ''),
('Blood Rh', '', 'POSITIVE,NEGATIVE', '0', '0', '');

insert into Tips (tipsKey, tipsName, tipsContent) values
('report.inventory.generate', 'Generate Inventory' ,'Click the Generate Inventory Report button below to generate a report of your products.'),
('testresults.find', 'Find/Edit Test Results', 'Find Test results by collection number or find tests done between two dates.'),
('products.find', 'Find/Edit Products', 'Find Products by collection number/product number/product type.'),
('collectedSamples.find', 'Find/Edit Collections', 'Find Collections by collection number/blood bag type/collection center/collection site.'),
('collections.findcollection.collectionsummary', 'Collection Summary', 'Showing details of Collection below. Click on Edit to modify the details of this collection.'),
('donors.finddonor', 'Find/Edit Donors', 'Find Donors by donor number or part of donor first name or last name. Filter donors by Blood Group.'),
('donors.finddonor.donorsummary', 'Donor Summary', 'View previous donations by this donor by selecting View Donation History. Add a new collection for this donor by selecting Add Collection.'),
('testresults.worksheet', 'Enter worksheet results', 'Find a generated worksheet and enter test results for collections in that worksheet'),
('requests.findpending', 'Find Pending Requests', 'To issue products first find pending requests for a given site by product type. Optionally specify dates to filter requests made on or after Request date and required on or before Required date'),
('requests.findpending.requestsummary', 'Request Summary Page', 'Click on Find Matching Products button above to find all available products that are compatible for the given request.'),
('requests.findpending.findmatchingproducts', 'Matching Products for request', 'Select the products you want to issue from the table below and then click on Issue button.'),
('requests.addcompatibilityresult', 'Add compatibility tests for request', 'Record if a given product is compatible or not with the patient blood sample for the given request.'),
('report.inventory.productinventorychart', 'Product Inventory Report', 'Products in Inventory by Product Type and Blood Group. Click the columns to view products by Age. Click again to return.'),
('report.products.discardedproductsreport', 'Discarded Products Over Time', 'Number of products discarded over time categorized by blood group'),
('report.products.issuedproductsreport', 'Products Issued Over Time', 'Number of products issued over time categorized by blood group'),
('report.collections.collectionsreport', 'Collections Report', 'Track number collections done within a given date range for specific centers and sites. View daily, monthly, yearly numbers.'),
('report.collections.testresultsreport', 'Test Results Report', 'Track TTI numbers by collection site and collection center done within a given date range for specific centers and sites. View daily, monthly, yearly numbers.'),
('usage.addusage', 'Add Usage Form', 'Record usage of a product within a hospital. Optionally specify Hospital name, ward, patient name.');

insert into GenericConfig (propertyName, propertyValue, propertyOwner) values
("rowHeight", "30", "collectionsWorksheet"),
("columnWidth", "100", "collectionsWorksheet"),
("collectionNumber", "true", "collectionsWorksheet"),
("testedOn", "false", "collectionsWorksheet"),
("Blood ABO", "true", "collectionsWorksheet"),
("Blood Rh", "true", "collectionsWorksheet"),
("HIV", "true", "collectionsWorksheet"),
("HBV", "true", "collectionsWorksheet"),
("HCV", "true", "collectionsWorksheet"),
("Syphilis", "true", "collectionsWorksheet");

insert into CrossmatchType (crossmatchType, isDeleted) values
('Saline @ 37 degrees', '0'),
('Anti Human Globulin', '0');
