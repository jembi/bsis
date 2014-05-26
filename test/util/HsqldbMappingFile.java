package util;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;

/**
 * 
 * @author devarshi.pandya
 *
 */
public class HsqldbMappingFile extends DefaultDataTypeFactory{

	public HsqldbMappingFile() {
		System.out.println("HsqldbMappingFile()..............................");
	}
	
	/**
	 * Override the default method and try to find out miss match datatype.
	 */
	@Override
	 public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
	     /*  
		 if ("SMALLINT".equals(sqlTypeName)) {
	            return DataType.BOOLEAN;
	        }
	       */
		 System.out.println("HsqldbMappingFile() method is call..............................");
		 DataType datatype = super.createDataType(sqlType, sqlTypeName); 
		 System.out.println(sqlTypeName + "-->"+datatype);
	        return datatype;
	    }

}
