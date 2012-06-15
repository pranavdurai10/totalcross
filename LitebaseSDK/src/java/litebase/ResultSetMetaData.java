/*********************************************************************************
 *  TotalCross Software Development Kit - Litebase                               *
 *  Copyright (C) 2000-2012 SuperWaba Ltda.                                      *
 *  All Rights Reserved                                                          *
 *                                                                               *
 *  This library and virtual machine is distributed in the hope that it will     *
 *  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                         *
 *                                                                               *
 *********************************************************************************/

package litebase;

import totalcross.io.IOException;
import totalcross.util.InvalidDateException;

/**
 * This class returns useful information for the <code>ResultSet</code> columns. Note that the information can be retrieved even if the 
 * <code>ResultSet</code> returns no data.
 * <P>Important: it is not possible to retrieve these information if the <code>ResultSet</code> is closed!
 */
public class ResultSetMetaData
{
   /**
    * Used by the <code>getColumnType</code> method to indicate that the column is of type <code>CHAR</code>.
    */
   public static final int CHAR_TYPE = 0;

   /**
    * Used by the <code>getColumnType</code> method to indicate that the column is of type <code>SHORT</code>.
    */
   public static final int SHORT_TYPE = 1;

   /**
    * Used by the <code>getColumnType</code> method to indicate that the column is of type <code>INT</code>.
    */
   public static final int INT_TYPE = 2;

   /**
    * Used by the <code>getColumnType</code> method to indicate that the column is of type <code>LONG</code>.
    */
   public static final int LONG_TYPE = 3;

   /**
    * Used by the <code>getColumnType</code> method to indicate that the column is of type <code>FLOAT</code>.
    */
   public static final int FLOAT_TYPE = 4;

   /**
    * Used by the <code>getColumnType</code> method to indicate that the column is of type <code>DOUBLE</code>.
    */
   public static final int DOUBLE_TYPE = 5;

   /**
    * Used by the <code>getColumnType</code> method to indicate that the column is of type <code>CHARS_NOCASE</code>.
    */
   public static final int CHAR_NOCASE_TYPE = 6;

   // The type BOOLEAN_TYPE is absent because a column will never have this type.
   // This is only used with expressions and this type would have the value 7.

   /**
    * Used by the <code>getColumnType</code> method to indicate that the column is of type <code>DATE_TYPE</code>.
    */
   public static final int DATE_TYPE = 8;

   /**
    * Used by the <code>getColumnType</code> method to indicate that the column is of type <code>DATETIME_TYPE</code>.
    */
   public static final int DATETIME_TYPE = 9;

   /**
    * Used by the <code>getColumnType</code> method to indicate that the column is of type <code>BLOB_TYPE</code>.
    */
   public static final int BLOB_TYPE = 10;

   /** 
    * The underlying <code>ResultSet</code>. 
    */
   private ResultSet rs;

   /**
    * Constructs a new <code>ResultSetMetaData</code> for a given <code>ResultSet</code>.
    *
    * @param aRs The ResultSet.
    */
   ResultSetMetaData(ResultSet aRs)
   {
      rs = aRs;
   }

   /**
    * Gets the number of columns for this <code>ResultSet</code>.
    *
    * @return The number of columns for this <code>ResultSet</code>.
    */
   public int getColumnCount()
   {
      rs.verifyResultSet(); // The driver or result set can't be closed.
      
      // juliana@230_36: corrected ResultSetMetaData returning extra columns in queries with order by where there are ordered fields that are not in 
      // the select clause.
      // juliana@230_14: removed temporary tables when there is no join, group by, order by, and aggregation.
      // juliana@114_10: skips the rowid.
      return rs.fields.length; 
   }

   /**
    * Given the column index (starting at 1), returns the display size. For chars, it will return the number of chars defined; for primitive types, 
    * it will return the number of decimal places it needs to be displayed correctly. Returns 0 if an error occurs.
    *
    * @param column The column index (starting at 1).
    * @return The display size or -1 if a problem occurs.
    */
   public int getColumnDisplaySize(int column)
   {
      ResultSet resultSet = rs;
      verifyRSMDState(column);
      
      // juliana@114_10: skips the rowid.
         
      if (resultSet.allRowsBitmap != null || resultSet.isSimpleSelect)
      {
         SQLResultSetField field = resultSet.fields[column - 1];
         column = field.parameter == null? field.tableColIndex + 1: field.parameter.tableColIndex + 1;
      }

      switch (resultSet.table.columnTypes[column - 1])
      {
         case ResultSetMetaData.SHORT_TYPE:
            return 6; // guich@560_4: adjusted the sizes
         case ResultSetMetaData.INT_TYPE:
            return 11;
         case ResultSetMetaData.LONG_TYPE:
            return 20;
         case ResultSetMetaData.FLOAT_TYPE:
            return 13;
         case ResultSetMetaData.DOUBLE_TYPE:
            return 21;
         case ResultSetMetaData.CHAR_TYPE:
         case ResultSetMetaData.CHAR_NOCASE_TYPE:
            return resultSet.table.columnSizes[column - 1];
         case ResultSetMetaData.DATE_TYPE:
            return 11; // rnovais@570_12
         case ResultSetMetaData.DATETIME_TYPE:
            return 31; // (11 + 20) // rnovais@570_12
         // BLOBs can't be displayed.
      }
      return -1;

   }

   /**
    * Given the column index (starting at 1), returns the column name. Note that if an alias is used to the column, the alias will be returned 
    * instead. If an error occurs, an empty string is returned. Note that LitebaseConnection 2.x tables must be recreated to be able to return this label information.
    *
    * @param column The column index (starting at 1).
    * @return The name or alias of the column, which can be an empty string if an error occurs.
    */
   public String getColumnLabel(int column)
   {
      verifyRSMDState(column);
      return rs.fields[column - 1].alias;
   }

   /**
    * Given the column index (starting at 1), returns the column type.
    *
    * @param column The column index (starting at 1).
    * @return The column type, which can be: <b><code>SHORT_TYPE</b></code>, <b><code>INT_TYPE</b></code>, <b><code>LONG_TYPE</b></code>, 
    * <b><code>FLOAT_TYPE</b></code>, <b><code>DOUBLE_TYPE</b></code>, <b><code>CHAR_TYPE</b></code>, <b><code>CHAR_NOCASE_TYPE</b></code>, 
    * <b><code>DATE_TYPE</b></code>, <b><code>DATETIME_TYPE</b></code>, or <b><code>BLOB_TYPE</b></code>.
    */
   public int getColumnType(int column)
   {
      ResultSet resultSet = rs;
      
      verifyRSMDState(column);
      if (resultSet.allRowsBitmap != null || resultSet.isSimpleSelect)
      {
         SQLResultSetField field = resultSet.fields[column - 1];
         return resultSet.table.columnTypes[field.parameter == null? field.tableColIndex : field.parameter.tableColIndex];
      }
      return resultSet.table.columnTypes[column - 1]; 
      
      // juliana@114_10: skips the rowid.
   }

   /**
    * Given the column index (starting at 1), returns the name of the column type.
    *
    * @param column The column index (starting at 1).
    * @return The name of the column type, which can be: <b><code>chars</b></code>, <b><code>short</b></code>, <b><code>int</b></code>, 
    * <b><code>long</b></code>, <b><code>float</b></code>, <b><code>double</b></code>, <b><code>date</b></code>, <b><code>datetime</b></code>, 
    * <b><code>blob</b></code>, or null if an error occurs.
    */
   public String getColumnTypeName(int column) 
   {
      switch (getColumnType(column)) // Gets the string representation of the type.
      { 
         case CHAR_TYPE:
         case CHAR_NOCASE_TYPE:
            return "chars";
         case SHORT_TYPE:
            return "short";
         case INT_TYPE:
            return "int";
         case LONG_TYPE:
            return "long";
         case FLOAT_TYPE:
            return "float";
         case DOUBLE_TYPE:
            return "double";
         case DATE_TYPE:
            return "date";
         case DATETIME_TYPE:
            return "datetime";
         case BLOB_TYPE:
            return "blob";
      }
      return null;
   }

   /**
    * Given the column index, (starting at 1) returns the name of the table it came from.
    *
    * @param columnIdx The column index.
    * @return The name of the table it came from.
    */
   public String getColumnTableName(int columnIdx)
   {
      verifyRSMDState(columnIdx);
      return rs.fields[columnIdx - 1].tableName;
   }
   
   /**
    * Given the column name or alias, returns the name of the table it came from.
    *
    * @param columnName The column name.
    * @return The name of the table it came from or <code>null</code> if the column name does not exist.
    * @throws DriverException If the column was not found.
    */
   public String getColumnTableName(String columnName) throws DriverException
   {
      rs.verifyResultSet(); // The driver or result set can't be closed.
      
      SQLResultSetField[] fields = rs.fields;
      int i = -1, 
          len = fields.length;

      while (++i < len) // Gets the name of the table or its alias given the column name.
         if (columnName.equalsIgnoreCase(fields[i].tableColName) || columnName.equalsIgnoreCase(fields[i].alias))
            return fields[i].tableName;
      throw new DriverException(LitebaseMessage.getMessage(LitebaseMessage.ERR_COLUMN_NOT_FOUND) + columnName);
   }
   
   // juliana@227_2: added methods to indicate if a column of a result set is not null or has default values.
   /**
    * Indicates if a column of the result set has default value.
    * 
    * @param columnIndex The column index.
    * @return <code>true</code> if the column has a default value; <code>false</code>, otherwise. 
    * @throws DriverException If an <code>IOException</code> occurs or the column index does not have an underlining table.
    */
   public boolean hasDefaultValue(int columnIndex) throws DriverException
   {      
      try // Gets the table column info.
      {
         String name = getColumnTableName(columnIndex); // It already tests if the result set is valid.
         SQLResultSetField field = rs.fields[columnIndex - 1];

         if (name != null)
            return ((rs.driver.getTable(name)).columnAttrs[field.tableColIndex >= 0? field.tableColIndex 
                                                                : field.parameter.tableColIndex] & Utils.ATTR_COLUMN_HAS_DEFAULT) != 0;
         throw new DriverException(LitebaseMessage.getMessage(LitebaseMessage.ERR_INVALID_COLUMN_NUMBER) + columnIndex);
      }
      catch (IOException exception)
      {
         throw new DriverException(exception);
      }
      catch (InvalidDateException exception) 
      {
         return false;
      }
   }
   
   /**
    * Indicates if a column of the result set has default value.
    * 
    * @param columnName The column name.
    * @return <code>true</code> if the column has a default value; <code>false</code>, otherwise. 
    * @throws DriverException If the column was not found, does not have an underlining table, or an <code>IOException</code> occurs.
    */
   public boolean hasDefaultValue(String columnName) throws DriverException
   {
      ResultSet resultSet = rs;
      
      resultSet.verifyResultSet(); // The driver or result set can't be closed.
      
      SQLResultSetField[] fields = resultSet.fields;
      SQLResultSetField field;
      int i = -1, 
          len = fields.length;

      while (++i < len) // Gets the name of the table or its alias given the column name and gets the table column info.
      {
         if (columnName.equalsIgnoreCase((field = fields[i]).tableColName) || columnName.equalsIgnoreCase(fields[i].alias))
            try
            {
               if (field.tableName != null)
                  return ((resultSet.driver.getTable(field.tableName).columnAttrs[field.tableColIndex >= 0? field.tableColIndex 
                                                                     : field.parameter.tableColIndex]) & Utils.ATTR_COLUMN_HAS_DEFAULT) != 0;
               throw new DriverException(LitebaseMessage.getMessage(LitebaseMessage.ERR_INVALID_COLUMN_NAME) + columnName);
            }
            catch (IOException exception)
            {
               throw new DriverException(exception);
            }
            catch (InvalidDateException exception) {}
      }
      if (i == len)
         throw new DriverException(LitebaseMessage.getMessage(LitebaseMessage.ERR_COLUMN_NOT_FOUND) + columnName);
      
      return false;
   }
   
   /**
    * Indicates if a column of the result set is not null.
    * 
    * @param columnIndex The column index.
    * @return <code>true</code> if the column is not null; <code>false</code>, otherwise. 
    * @throws DriverException If an <code>IOException</code> occurs or the column index does not have an underlining table.
    */
   public boolean isNotNull(int columnIndex) throws DriverException
   {            
      try // Gets the table column info.
      {
         String name = getColumnTableName(columnIndex); // It already tests if the result set is valid.
         SQLResultSetField field = rs.fields[columnIndex - 1];
         
         if (name != null)
            return ((rs.driver.getTable(name)).columnAttrs[field.tableColIndex >= 0? field.tableColIndex 
                                                         : field.parameter.tableColIndex] & Utils.ATTR_COLUMN_IS_NOT_NULL) != 0;
         throw new DriverException(LitebaseMessage.getMessage(LitebaseMessage.ERR_INVALID_COLUMN_NUMBER) + columnIndex);
      }
      catch (IOException exception)
      {
         throw new DriverException(exception);
      }
      catch (InvalidDateException exception) 
      {
         return false;
      }
   }
   
   /**
    * Indicates if a column of the result set is not null.
    * 
    * @param columnName The column name.
    * @return <code>true</code> if the column is not null; <code>false</code>, otherwise. 
    * @throws DriverException If the column was not found, does not have an underlining table, or an <code>IOException</code> occurs.
    */
   public boolean isNotNull(String columnName) throws DriverException
   {
      ResultSet resultSet = rs;
      
      resultSet.verifyResultSet(); // The driver or result set can't be closed.
      
      SQLResultSetField[] fields = resultSet.fields;
      SQLResultSetField field;
      int i = -1, 
          len = fields.length;

      while (++i < len) // Gets the name of the table or its alias given the column name and gets the table column info.
         if (columnName.equalsIgnoreCase((field = fields[i]).tableColName) || columnName.equalsIgnoreCase(fields[i].alias))
            try
            {
               if (field.tableName != null)
                  return ((resultSet.driver.getTable(field.tableName).columnAttrs[field.tableColIndex >= 0? field.tableColIndex 
                                                                     : field.parameter.tableColIndex]) & Utils.ATTR_COLUMN_IS_NOT_NULL) != 0;
               throw new DriverException(LitebaseMessage.getMessage(LitebaseMessage.ERR_INVALID_COLUMN_NAME) + columnName);
            }
            catch (IOException exception)
            {
               throw new DriverException(exception);
            }
            catch (InvalidDateException exception) {}
      
      if (i == len)
         throw new DriverException(LitebaseMessage.getMessage(LitebaseMessage.ERR_COLUMN_NOT_FOUND) + columnName);
      
      return false;
   }
   
   // juliana@230_28: if a public method receives an invalid argument, now an IllegalArgumentException will be thrown instead of a DriverException.
   /**
    * Checks if the driver or the result set is closed, and if the column index is invalid.
    * 
    * @param column The column index.
    * @throws IllegalArgumentException If the column index is invalid.
    */
   private void verifyRSMDState(int column) throws IllegalArgumentException
   {
      ResultSet resultSet = rs;
      
      resultSet.verifyResultSet(); // The driver or result set can't be closed.
      
      // juliana@230_36: corrected ResultSetMetaData returning extra columns in queries with order by where there are ordered fields that are not in 
      // the select clause.
      // juliana@213_5: Now a DriverException is thrown instead of returning an invalid value.
      if (column <= 0 || column > resultSet.fields.length)
         throw new IllegalArgumentException(LitebaseMessage.getMessage(LitebaseMessage.ERR_INVALID_COLUMN_NUMBER) + column);
   }
}