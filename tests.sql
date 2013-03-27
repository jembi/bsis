insert into MicrotiterPlate (id, plateKey, plateName, numRows, numColumns, notes, isDeleted) values
(1, 'bloodtyping', 'Blood Typing Plate', 8, 12, '', '0'),
(2, 'tti', 'Elisa Plate', 8, 12, '', '0');

insert into BloodTest
(id, name, displayName, validResults, correctResult, isRequired, isDeleted, notes) values
(1, 'Blood ABO', 'Blood ABO', 'A,B,AB,O', '', '1', '0', ''),
(2, 'Blood Rh', 'Blood Rh', '+,-', '', '1', '0', ''),
(3, 'HIV', 'HIV', '+,-', '-', '1', '0', ''),
(4, 'HBV', 'HBV', '+,-', '-', '1', '0', ''),
(5, 'HCV', 'HCV', '+,-', '-', '1', '0', ''),
(6, 'Syphilis', 'Syphilis', '+,-', '-', '1', '0', '');

insert into BloodTypingTest
(id, testNameShort, testName, dataType,
validResults, negativeResults, positiveResults,
rankInCategory, bloodTypingTestType,
isEmptyAllowed, isActive) values
(1, 'Anti-A', 'Anti-A', 'STRING',
'+,-', '-', '+',
'1', 'BASIC',
'0', '1'),
(2, 'Anti-B', 'Anti-B', 'STRING',
'+,-', '-', '+',
'2', 'BASIC',
'0', '1'),
(3, 'Anti-A,B', 'Anti-A,B', 'STRING',
'+,-', '-', '+',
'3', 'BASIC',
'0', '1'),
(4, 'A1 Cells', 'A1 Cells', 'STRING',
'+,-', '-', '+',
'4', 'BASIC',
'0', '1'),
(5, 'B Cells', 'B Cells', 'STRING',
'+,-', '-', '+',
'5', 'BASIC',
'0', '1'),
(6, 'Anti-D', 'Anti-D', 'STRING',
'+,-', '-', '+',
'6', 'BASIC',
'0', '1'),
(7, 'AbScr', 'Antibody Screen', 'STRING',
'+,-', '-', '+',
'7', 'BASIC',
'0', '1'),
(8, 'Haemolysin', 'Haemolysin', 'STRING',
'+,-', '-', '+',
'8', 'BASIC',
'0', '1'),
(9, 'Du', 'Du', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED',
'0', '1'),
(10, 'AHG Control (Du)', 'AHG Control (Du)', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED',
'0', '1'),
(11, 'DAT if Du Pos', 'DAT if Du Pos', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED',
'0', '1'),
(12, 'AHG Control (DAT)', 'AHG Control (DAT)', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED',
'0', '1'),
(13, 'Immune anti-A', 'Immune anti-A', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED',
'0', '1'),
(14, 'IAT Screen - 10 min', 'IAT Screen - 10 min', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED',
'0', '1'),
(15, 'IAT Screen - AHG', 'IAT Screen - AHG', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED',
'0', '1'),
(16, 'AHG Control (IAT Screen)', 'AHG Control (IAT Screen)', 'STRING',
'+,-', '-', '+',
'1', 'ADVANCED',
'0', '1');

insert into BloodTypingRule
(bloodTypingTestIds, pattern,
 partOfBloodGroupChanged, newInformation, extraInformation,
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

insert into Tips(tipsKey, tipsName, tipsContent) values
('bloodtyping.plate.step1', 'Step 1 of Blood Typing', 'Scan/type collection numbers for all columns on microtiter plate');

insert into GenericConfig (propertyName, propertyValue, propertyOwner) values
("horizontalentry", "true", "bloodtyping"),
("titerWellRadius", "25", "bloodTyping");