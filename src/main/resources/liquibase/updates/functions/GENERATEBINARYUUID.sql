CREATE FUNCTION GENERATEBINARYUUID() RETURNS BINARY(16)
BEGIN
  DECLARE uuid VARCHAR(36);
  SET uuid = UUID();
	RETURN UNHEX(CONCAT(             
      SUBSTR(uuid, 15, 4),
      SUBSTR(uuid, 10, 4),
      SUBSTR(uuid, 1, 8),
      SUBSTR(uuid, 20, 4),
      SUBSTR(uuid, 25) ));
END
