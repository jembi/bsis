insert into MicrotiterPlate (id, plateKey, plateName, numRows, numColumns, notes, isDeleted) values
(1, 'bloodtyping', 'Blood Typing Plate', 8, 12, '', '0'),
(2, 'tti', 'Elisa Plate', 8, 12, '', '0');

insert into RawBloodTestGroup (id, testGroupName, isDeleted) values
(1, 'Blood Typing', '0'),
(2, 'TTI Tests', '0');

insert into RawBloodTest
(id, testNameShort, testName, dataType, validResults, negativeResults,
negativeRequiredForUse, plateUsedForTesting_id, rankOnPlate,
isConfidential, isActive) values
(1, 'Anti-A', 'Anti-A', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '1',
'0', '1'),
(2, 'Anti-B', 'Anti-B', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '2',
'0', '1'),
(3, 'Anti-A,B', 'Anti-A,B', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '3',
'0', '1'),
(4, 'Anti-D', 'Anti-D', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '4',
'0', '1'),
(5, 'AbScr', 'Antibody Screen', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '5',
'0', '1'),
(6, 'A1 Cells', 'A1 Cells', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '6',
'0', '1'),
(7, 'B Cells', 'B Cells', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '7',
'0', '1'),
(8, 'Haemolysin', 'Haemolysin', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '8',
'0', '1'),
(9, 'IAT Screen - 10 min', 'IAT Screen - 10 min', 'STRING', '0,1,2,3,4', '0',
'0', NULL, '1',
'0', '1'),
(10, 'IAT Screen - AHG', 'IAT Screen - AHG', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'0', '1'),
(11, 'DAT if Du Pos', 'DAT if Du Pos', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'0', '1'),
(12, 'AHG Control (IAT Screen)', 'AHG Control (IAT Screen)', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'0', '1'),
(13, 'AHG Control (Du)', 'AHG Control (Du)', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'0', '1'),
(14, 'Du', 'Du', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'0', '1'),
(15, 'AHG Control (DAT)', 'AHG Control (DAT)', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'0', '1');

insert into RawBloodTestGroup_RawBloodTest
(rawBloodTestGroups_id, bloodTestsInGroup_id) values
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8);

insert into RawBloodTest_TestsRequiredIfPositive
(RawBloodTest_id, testsRequiredIfPositive_id) values
(5, 9),
(5, 10),
(13, 11);

insert into RawBloodTest_TestsRequiredIfNegative
(RawBloodTest_id, testsRequiredIfNegative_id) values
(11, 15),
(14, 13),
(10, 12);


insert into Tips(tipsKey, tipsName, tipsContent) values
('bloodtyping.plate.step1', 'Step 1 of Blood Typing', 'Scan/type collection numbers for all columns on microtiter plate');

insert into GenericConfig (propertyName, propertyValue, propertyOwner) values
("horizontalentry", "true", "bloodtyping"),
("titerWellRadius", "20", "bloodTyping");