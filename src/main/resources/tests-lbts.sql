insert into MicrotiterPlate (id, plateKey, plateName, numRows, numColumns, notes, isDeleted) values
(1, 'bloodtyping', 'Blood Typing Plate', 8, 12, '', '0'),
(2, 'tti', 'Elisa Plate', 8, 12, '', '0');

insert into BloodTest
(id, testNameShort, testName,
validResults, negativeResults, positiveResults,
rankInCategory, bloodTestType, category,
context,
isEmptyAllowed, isActive) values
(1, 'ABO', 'ABO',
'A,B,AB,O', '', 'A,B,AB,O',
'1', 'BASIC_BLOODTYPING', 'BLOODTYPING',
'RECORD_BLOOD_TYPING_TESTS',
'0', '1'),
(2, 'Rh', 'Rh',
'POS,NEG', 'NEG', 'POS',
'2', 'BASIC_BLOODTYPING', 'BLOODTYPING',
'RECORD_BLOOD_TYPING_TESTS',
'0', '1'),
(3, 'Titre', 'Titre',
'HIGH,LOW', 'LOW', 'HIGH',
'3', 'BASIC_BLOODTYPING', 'BLOODTYPING',
'RECORD_BLOOD_TYPING_TESTS',
'0', '1'),
(4, 'Weak D', 'Weak D',
'POS,NEG', 'NEG', 'POS',
'4', 'BASIC_BLOODTYPING', 'BLOODTYPING',
'RECORD_BLOOD_TYPING_TESTS',
'0', '1');

insert into BloodTestingRule
(bloodTestsIds, pattern,
 donationFieldChanged, newInformation, extraInformation,
 context, category, subCategory,
 pendingTestsIds, markSampleAsUnsafe, isActive
) values
('1', 'O',
 'BLOODABO', 'O', '',
 'RECORD_BLOOD_TYPING_TESTS', 'BLOODTYPING', 'BLOODABO',
 '', '0', '1'
 ),
('1', 'A',
 'BLOODABO', 'A', '',
 'RECORD_BLOOD_TYPING_TESTS', 'BLOODTYPING', 'BLOODABO',
 '', '0', '1'
 ),
('1', 'B',
 'BLOODABO', 'B', '',
 'RECORD_BLOOD_TYPING_TESTS', 'BLOODTYPING', 'BLOODABO',
 '', '0', '1'
 ),
('1', 'AB',
 'BLOODABO', 'AB', '',
 'RECORD_BLOOD_TYPING_TESTS', 'BLOODTYPING', 'BLOODABO',
 '', '0', '1'
 ),
('2', 'POS',
 'BLOODRH', '+', '',
 'RECORD_BLOOD_TYPING_TESTS', 'BLOODTYPING', 'BLOODRH',
 '', '0', '1'
 ),
 ('2', 'NEG',
 'BLOODRH', '-', '',
 'RECORD_BLOOD_TYPING_TESTS', 'BLOODTYPING', 'BLOODRH',
 '', '0', '1'
 );

 insert into BloodTest
(id, testNameShort, testName,
validResults, negativeResults, positiveResults,
rankInCategory, bloodTestType, category,
context,
isEmptyAllowed, isActive) values
(17, 'HIV', 'HIV',
'POS,NEG', 'NEG', 'POS',
'1', 'BASIC_TTI', 'TTI',
'RECORD_TTI_TESTS',
'0', '1'),
(18, 'HIV Conf 1', 'HIV Confirmatory 1',
'POS,NEG', 'NEG', 'POS',
'1', 'CONFIRMATORY_TTI', 'TTI',
'RECORD_TTI_TESTS',
'0', '1'),
(19, 'HIV Conf 2', 'HIV Confirmatory 2',
'POS,NEG', 'NEG', 'POS',
'1', 'CONFIRMATORY_TTI', 'TTI',
'RECORD_TTI_TESTS',
'0', '1'),
(20, 'HBV', 'HBV',
'POS,NEG', 'NEG', 'POS',
'1', 'BASIC_TTI', 'TTI',
'RECORD_TTI_TESTS',
'0', '1'),
(21, 'HBV Conf 1', 'HBV Confirmatory 1',
'POS,NEG', 'NEG', 'POS',
'1', 'CONFIRMATORY_TTI', 'TTI',
'RECORD_TTI_TESTS',
'0', '1'),
(22, 'HBV Conf 2', 'HBV Confirmatory 2',
'POS,NEG', 'NEG', 'POS',
'1', 'CONFIRMATORY_TTI', 'TTI',
'RECORD_TTI_TESTS',
'0', '1'),
(23, 'HCV', 'HCV',
'POS,NEG', 'NEG', 'POS',
'1', 'BASIC_TTI', 'TTI',
'RECORD_TTI_TESTS',
'0', '1'),
(24, 'HCV Conf 1', 'HCV Confirmatory 1',
'POS,NEG', 'NEG', 'POS',
'1', 'CONFIRMATORY_TTI', 'TTI',
'RECORD_TTI_TESTS',
'0', '1'),
(25, 'HCV Conf 2', 'HCV Confirmatory 2',
'POS,NEG', 'NEG', 'POS',
'1', 'CONFIRMATORY_TTI', 'TTI',
'RECORD_TTI_TESTS',
'0', '1'),
(26, 'Syphilis', 'Syphilis',
'POS,NEG', 'NEG', 'POS',
'1', 'BASIC_TTI', 'TTI',
'RECORD_TTI_TESTS',
'0', '1'),
(27, 'Syphilis Conf 1', 'Syphilis Confirmatory 1',
'POS,NEG', 'NEG', 'POS',
'1', 'CONFIRMATORY_TTI', 'TTI',
'RECORD_TTI_TESTS',
'0', '1'),
(28, 'Syphilis Conf 2', 'Syphilis Confirmatory 2',
'POS,NEG', 'NEG', 'POS',
'1', 'CONFIRMATORY_TTI', 'TTI',
'RECORD_TTI_TESTS',
'0', '1');

insert into BloodTestingRule
(bloodTestsIds, pattern,
 donationFieldChanged, newInformation, extraInformation,
 context, category, subCategory,
 pendingTestsIds, markSampleAsUnsafe, isActive
) values
 ('17', 'NEG',
 'TTISTATUS', 'TTI_SAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '', '0', '1'
 ),
 ('17', 'POS',
 'TTISTATUS', 'TTI_UNSAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '18,19', '0', '1'
 ),
 ('18', 'POS',
 'TTISTATUS', 'TTI_UNSAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '', '0', '1'
 ),
 ('19', 'POS',
 'TTISTATUS', 'TTI_UNSAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '', '0', '1'
 ),
 ('20', 'NEG',
 'TTISTATUS', 'TTI_SAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '', '0', '1'
 ),
 ('20', 'POS',
 'TTISTATUS', 'TTI_UNSAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '21,22', '0', '1'
 ),
 ('21', 'POS',
 'TTISTATUS', 'TTI_UNSAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '', '0', '1'
 ),
 ('22', 'POS',
 'TTISTATUS', 'TTI_UNSAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '', '0', '1'
 ),
 ('23', 'NEG',
 'TTISTATUS', 'TTI_SAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '', '0', '1'
 ),
 ('23', 'POS',
 'TTISTATUS', 'TTI_UNSAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '24,25', '0', '1'
 ),
 ('24', 'POS',
 'TTISTATUS', 'TTI_UNSAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '', '0', '1'
 ),
 ('25', 'POS',
 'TTISTATUS', 'TTI_UNSAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '', '0', '1'
 ),
 ('26', 'NEG',
 'TTISTATUS', 'TTI_SAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '', '0', '1'
 ),
 ('26', 'POS',
 'TTISTATUS', 'TTI_UNSAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '27,28', '0', '1'
 ),
 ('27', 'POS',
 'TTISTATUS', 'TTI_UNSAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '', '0', '1'
 ),
 ('28', 'POS',
 'TTISTATUS', 'TTI_UNSAFE', '',
 'RECORD_TTI_TESTS', 'TTI', 'TTI',
 '', '0', '1'
 );

insert into Tips(tipsKey, tipsName, tipsContent) values
('bloodtyping.plate.step1', 'Step 1 of Blood Typing', 'Scan/type DINs for all columns on microtiter plate');

insert into GenericConfig (propertyName, propertyValue, propertyOwner) values
("horizontalentry", "true", "bloodtyping"),
("titerWellRadius", "25", "bloodTyping");

insert into GenericConfig (propertyName, propertyValue, propertyOwner) values
("horizontalentry", "true", "ttiWells"),
("titerWellRadius", "25", "ttiWells");

insert into WorksheetType
(id, worksheetType, context, isDeleted) values
(1, 'Blood Typing', 'RECORD_BLOOD_TYPING_TESTS', '0'),
(2, 'Full Blood Typing', 'RECORD_BLOOD_TYPING_TESTS', '0'),
(3, 'TTI', 'RECORD_TTI_TESTS', '0'),
(4, 'Full TTI', 'RECORD_TTI_TESTS', '0');

insert into BloodTest_WorksheetType
(bloodTests_id, worksheetTypes_id) values
(1,1),
(2,1),
(3,1),
(4,1),
(1,2),
(2,2),
(3,2),
(4,2),
(17,3),
(20,3),
(23,3),
(26,3),
(17,4),
(18,4),
(19,4),
(20,4),
(21,4),
(22,4),
(23,4),
(24,4),
(25,4),
(26,4),
(27,4),
(28,4);
