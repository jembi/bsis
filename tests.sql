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

insert into RawBloodTest
(id, testNameShort, testName, dataType, validResults, negativeResults,
negativeRequiredForUse, plateUsedForTesting_id, rank,
affectsBloodTest_id, isConfidential, isActive) values
(1, 'Anti-A', 'Anti-A', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '1',
'1', '0', '1'),
(2, 'Anti-B', 'Anti-B', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '2',
'1', '0', '1'),
(3, 'Anti-A,B', 'Anti-A,B', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '3',
'1', '0', '1'),
(4, 'A1 Cells', 'A1 Cells', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '4',
'1', '0', '1'),
(5, 'B Cells', 'B Cells', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '5',
'1', '0', '1'),
(6, 'Anti-D', 'Anti-D', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '6',
'2', '0', '1'),
(7, 'AbScr', 'Antibody Screen', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '7',
'1', '0', '1'),
(8, 'Haemolysin', 'Haemolysin', 'STRING', '0,1,2,3,4,H', '0',
'0', '1', '8',
'1', '0', '1'),
(9, 'Du', 'Du', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'1', '0', '1'),
(10, 'AHG Control (Du)', 'AHG Control (Du)', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'1', '0', '1'),
(11, 'DAT if Du Pos', 'DAT if Du Pos', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'1', '0', '1'),
(12, 'AHG Control (DAT)', 'AHG Control (DAT)', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'1', '0', '1'),
(13, 'IAT Screen - 10 min', 'IAT Screen - 10 min', 'STRING', '0,1,2,3,4', '0',
'0', NULL, '1',
'1', '0', '1'),
(14, 'IAT Screen - AHG', 'IAT Screen - AHG', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'1', '0', '1'),
(15, 'AHG Control (IAT Screen)', 'AHG Control (IAT Screen)', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'1', '0', '1'),
(16, 'Immune anti-A', 'Immune anti-A', 'STRING', '0,1,2,3,4,H', '0',
'0', NULL, '1',
'1', '0', '1');

insert into RawBloodTest_TestsRequiredIfPositive
(RawBloodTest_id, testsRequiredIfPositive_id) values
(7, 13),
(7, 14),
(7, 15);

insert into RawBloodTest_TestsRequiredIfNegative
(RawBloodTest_id, testsRequiredIfNegative_id) values
(6, 9),
(6, 10),
(6, 11),
(6, 12);

insert into BloodTestMapper
(name, rules, bloodTest_id) values
('Blood Group Direct',
'{ "order": "1,2,3,4,5,6",
	 "---+++": {"group" : "O+", "comment": "direct"},
	 "---++-": {"group" : "O-", "comment": "direct"},
   "+-+-++": {"group" : "A+", "comment": "direct"},
   "+-+-+-": {"group" : "A-", "comment": "direct"},
	 "-+++-+": {"group" : "B+", "comment": "direct"},
   "-+++--": {"group" : "B-", "comment": "direct"},
   "+++--+": {"group" : "AB+", "comment": "direct"},
   "+++---": {"group" : "AB-", "comment": "direct"},
}',
'1'),
('Blood Group Indirect',
'{ "order": "1,2,3,4,5,6,7",
	 "--+-+++": {"group" : "A+", "comment": "ReGroup=wkA"},
	 "--+-+-+": {"group" : "A-", "comment": "ReGroup=wkA"},
	 "-++--++": {"group" : "AB+", "comment": "ReGroup=wkAB"},
	 "-++---+": {"group" : "AB-", "comment": "ReGroup=wkAB"},
}',
'1'),
('Blood Group Indirect',
'{ "order": "1,2,3,4,5,6,7",
	 "--+-+++": {"group" : "A+", "comment": "ReGroup=wkA"},
	 "--+-+-+": {"group" : "A-", "comment": "ReGroup=wkA"},
	 "-++--++": {"group" : "AB+", "comment": "ReGroup=wkAB"},
	 "-++---+": {"group" : "AB-", "comment": "ReGroup=wkAB"},
}',
'1');

insert into Tips(tipsKey, tipsName, tipsContent) values
('bloodtyping.plate.step1', 'Step 1 of Blood Typing', 'Scan/type collection numbers for all columns on microtiter plate');

insert into GenericConfig (propertyName, propertyValue, propertyOwner) values
("horizontalentry", "true", "bloodtyping"),
("titerWellRadius", "20", "bloodTyping");