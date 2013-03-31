insert into MicrotiterPlate (id, plateKey, plateName, numRows, numColumns, notes, isDeleted) values
(1, 'bloodtyping', 'Blood Typing Plate', 8, 12, '', '0'),
(2, 'tti', 'Elisa Plate', 8, 12, '', '0');

insert into BloodTest
(id, testNameShort, testName, dataType,
validResults, negativeResults, positiveResults,
rankInCategory, bloodTestType, category,
isEmptyAllowed, isActive) values
(1, 'Anti-A', 'Anti-A', 'STRING',
'+,-', '-', '+',
'1', 'BASIC_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(2, 'Anti-B', 'Anti-B', 'STRING',
'+,-', '-', '+',
'2', 'BASIC_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(3, 'Anti-A,B', 'Anti-A,B', 'STRING',
'+,-', '-', '+',
'3', 'BASIC_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(4, 'A1 Cells', 'A1 Cells', 'STRING',
'+,-', '-', '+',
'4', 'BASIC_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(5, 'B Cells', 'B Cells', 'STRING',
'+,-', '-', '+',
'5', 'BASIC_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(6, 'Anti-D', 'Anti-D', 'STRING',
'+,-', '-', '+',
'6', 'BASIC_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(7, 'AbScr', 'Antibody Screen', 'STRING',
'+,-', '-', '+',
'7', 'BASIC_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(8, 'Haemolysin', 'Haemolysin', 'STRING',
'+,-', '-', '+',
'8', 'BASIC_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(9, 'Du', 'Du', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(10, 'AHG Control (Du)', 'AHG Control (Du)', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(11, 'DAT if Du Pos', 'DAT if Du Pos', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(12, 'AHG Control (DAT)', 'AHG Control (DAT)', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(13, 'Immune anti-A', 'Immune anti-A', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(14, 'IAT Screen - 10 min', 'IAT Screen - 10 min', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(15, 'IAT Screen - AHG', 'IAT Screen - AHG', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED_BLOODTYPING', 'BLOODTYPING',
'0', '1'),
(16, 'AHG Control (IAT Screen)', 'AHG Control (IAT Screen)', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED_BLOODTYPING', 'BLOODTYPING',
'0', '1');

insert into BloodTestRule
(bloodTestsIds, pattern,
 collectionFieldChanged, newInformation, extraInformation,
 extraTestsIds, markSampleAsUnsafe, isActive
) values
('1,2,3,4,5', '---++',
 'BLOODABO', 'O', '',
 '', '0', '1'
 ),
('1,2,3,4,5', '+-+-+',
 'BLOODABO', 'A', '',
 '', '0', '1'
 ),
('1,2,3,4,5', '-+++-',
 'BLOODABO', 'B', '',
 '', '0', '1'
 ),
('1,2,3,4,5', '+++--',
 'BLOODABO', 'AB', '',
 '', '0', '1'
 ),
('6', '+',
 'BLOODRH', '+', '',
 '', '0', '1'
 ),
('6', '-',
 'NOCHANGE', '', '',
 '9,10', '0', '1'
 ),
('6,9', '--',
 'BLOODRH', '-', '',
 '', '0', '1'
 ),
('6,9', '-+',
 'BLOODRH', '+', '',
 '', '0', '1'
 ),
 ('6,9,11', '-++',
 'EXTRA', 'DAT Pos;', '',
 '', '0', '1'
 ),
 ('6,9,11', '-+-',
 'EXTRA', 'DAT Neg;', '',
 '', '0', '1'
 ),
 ('1,2,3,4,5', '--+-+',
 'NOCHANGE', '', '',
 '13', '0', '1'
 ),
 ('1,2,3,4,5,13', '--+-++',
 'BLOODABO', 'A', 'wkA',
 '', '0', '1'
 ),
 ('1,2,3,4,5', '-++--',
 'NOCHANGE', '', '',
 '13', '0', '1'
 ),
 ('1,2,3,4,5,13', '-++--+',
 'BLOODABO', 'AB', 'wkAB',
 '', '0', '1'
 );

 insert into BloodTest
(id, testNameShort, testName, dataType,
validResults, negativeResults, positiveResults,
rankInCategory, bloodTestType, category,
isEmptyAllowed, isActive) values
(17, 'HIV', 'HIV', 'STRING',
'+,-', '-', '+',
'1', 'BASIC_TTI', 'TTI',
'0', '1'),
(18, 'HIV Conf 1', 'HIV Confirmatory 1', 'STRING',
'+,-', '-', '+',
'1', 'CONFIRMATORY_TTI', 'TTI',
'0', '1'),
(19, 'HIV Conf 2', 'HIV Confirmatory 2', 'STRING',
'+,-', '-', '+',
'1', 'CONFIRMATORY_TTI', 'TTI',
'0', '1'),
(20, 'HBV', 'HBV', 'STRING',
'+,-', '-', '+',
'1', 'BASIC_TTI', 'TTI',
'0', '1'),
(21, 'HBV Conf 1', 'HBV Confirmatory 1', 'STRING',
'+,-', '-', '+',
'1', 'CONFIRMATORY_TTI', 'TTI',
'0', '1'),
(22, 'HBV Conf 2', 'HBV Confirmatory 2', 'STRING',
'+,-', '-', '+',
'1', 'CONFIRMATORY_TTI', 'TTI',
'0', '1'),
(23, 'HCV', 'HCV', 'STRING',
'+,-', '-', '+',
'1', 'BASIC_TTI', 'TTI',
'0', '1'),
(24, 'HCV Conf 1', 'HCV Confirmatory 1', 'STRING',
'+,-', '-', '+',
'1', 'CONFIRMATORY_TTI', 'TTI',
'0', '1'),
(25, 'HCV Conf 2', 'HCV Confirmatory 2', 'STRING',
'+,-', '-', '+',
'1', 'CONFIRMATORY_TTI', 'TTI',
'0', '1'),
(26, 'Syphilis', 'Syphilis', 'STRING',
'+,-', '-', '+',
'1', 'BASIC_TTI', 'TTI',
'0', '1'),
(27, 'Syphilis Conf 1', 'Syphilis Confirmatory 1', 'STRING',
'+,-', '-', '+',
'1', 'CONFIRMATORY_TTI', 'TTI',
'0', '1'),
(28, 'Syphilis Conf 2', 'Syphilis Confirmatory 2', 'STRING',
'+,-', '-', '+',
'1', 'CONFIRMATORY_TTI', 'TTI',
'0', '1');

insert into BloodTestRule
(bloodTestsIds, pattern,
 collectionFieldChanged, newInformation, extraInformation,
 extraTestsIds, markSampleAsUnsafe, isActive
) values
('17,20,23,26', '----',
 'TTISTATUS', 'TTI_SAFE', '',
 '', '0', '1'
 ),
('17', '+',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '18,19', '0', '1'
 ),
 ('17,18,19', '-+-',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('17,18,19', '--+',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('17,18,19', '+--',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('17,18,19', '++-',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('17,18,19', '-++',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('17,18,19', '+-+',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('17,18,19', '---',
 'TTISTATUS', 'TTI_SAFE', '',
 '', '0', '1'
 ),
('20', '+',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '21,22', '0', '1'
 ),
 ('20,21,22', '-+-',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('20,21,22', '--+',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('20,21,22', '+--',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('20,21,22', '++-',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('20,21,22', '-++',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('20,21,22', '+-+',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
('23', '+',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '24,25', '0', '1'
 ),
 ('23,24,25', '-+-',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('23,24,25', '--+',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('23,24,25', '+--',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('23,24,25', '++-',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('23,24,25', '-++',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('23,24,25', '+-+',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
('26', '+',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '27,28', '0', '1'
 ),
 ('26,27,28', '-+-',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('26,27,28', '--+',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('26,27,28', '+--',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('26,27,28', '++-',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('26,27,28', '-++',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('26,27,28', '+-+',
 'TTISTATUS', 'TTI_UNSAFE', '',
 '', '0', '1'
 ),
 ('17,18,19,20,21,22,23,24,25,26,27,28', '------------',
 'TTISTATUS', 'TTI_SAFE', '',
 '', '0', '1'
 );

insert into Tips(tipsKey, tipsName, tipsContent) values
('bloodtyping.plate.step1', 'Step 1 of Blood Typing', 'Scan/type collection numbers for all columns on microtiter plate');

insert into GenericConfig (propertyName, propertyValue, propertyOwner) values
("horizontalentry", "true", "bloodtyping"),
("titerWellRadius", "25", "bloodTyping");

insert into WorksheetType
(id, worksheetType, isDeleted) values
(1, 'Blood Typing', '0'),
(2, 'Full Blood Typing', '0'),
(3, 'TTI', '0'),
(4, 'Full TTI', '0');

insert into BloodTest_WorksheetType
(bloodTests_id, worksheetTypes_id) values
(1,1),
(2,1),
(3,1),
(4,1),
(5,1),
(6,1),
(1,2),
(2,2),
(3,2),
(4,2),
(5,2),
(6,2),
(9,2),
(10,2),
(11,2),
(12,2),
(13,2),
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