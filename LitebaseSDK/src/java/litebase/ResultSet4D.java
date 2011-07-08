/*********************************************************************************
 *  TotalCross Software Development Kit - Litebase                               *
 *  Copyright (C) 2000-2011 SuperWaba Ltda.                                      *
 *  All Rights Reserved                                                          *
 *                                                                               *
 *  This library and virtual machine is distributed in the hope that it will     *
 *  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                         *
 *                                                                               *
 *********************************************************************************/

package litebase;

import totalcross.sys.*;
import totalcross.util.*;

/**
 * This native class represents a set or rows resulting from a <code>LitebaseConnection.executeQuery()</code> method call. Here's an example:
 *
 * <pre>
 * ResultSet rs = driver.executeQuery(&quot;select name, salary, age from person&quot;);
 * while (rs.next())
 * {
 *    Vm.debug(pad(rs.getString(&quot;name&quot;), 32) + pad(rs.getString(&quot;salary&quot;), 16) + rs.getInt(&quot;age&quot;) + &quot; years&quot;);
 * }
 * </pre>
 *
 * Result sets cannot be constructed directly; instead, an sql must be issued to the driver.
 */
public class ResultSet4D
{
   /**
    * The <code>ResultSetBag</code> object, which stores the <code>ResultSet</code> data.
    */
   long bag;
   
   /**
    * A flag that indicates that this class has already been finalized.
    */
   boolean dontFinalize;

   // juliana@230_11: Litebase public class constructors are now not public any more. 
   /**
    * The constructor.
    */
   private ResultSet4D() {}
   
   /**
    * Returns the metadata for this result set.
    *
    * @return The metadata for this result set.
    * @throws DriverException If the result set or the driver is closed.
    */
   public native ResultSetMetaData4D getResultSetMetaData() throws DriverException;

   /**
    * Releases all memory allocated for this object. Its a good idea to call this when you no longer needs it, but it is also called by the GC when
    * the object is no longer in use.
    *
    * @throws DriverException If the result set is closed.
    */
   public native void close() throws DriverException;

   /**
    * Places the cursor before the first record.
    *
    * @throws DriverException If the result set or the driver is closed.
    */
   public native void beforeFirst() throws DriverException;

   /**
    * Places the cursor after the last record.
    *
    * @throws DriverException If the result set or the driver is closed.
    */
   public native void afterLast() throws DriverException;

   /**
    * Places the cursor in the first record of the result set.
    *
    * @return <code>true</code> if it was possible to place the cursor in the first record; <code>false</code>, otherwise.
    * @throws DriverException If the result set or the driver is closed.
    */
   public native boolean first() throws DriverException;

   /**
    * Places the cursor in the last record of the result set.
    *
    * @return <code>true</code> if it was possible to place the cursor in the last record; <code>false</code>, otherwise.
    * @throws DriverException If the result set or the driver is closed.
    */
   public native boolean last() throws DriverException;

   /**
    * Gets the next record of the result set.
    *
    * @return <code>true</code> if there is a next record to go to in the result set; <code>false</code>, otherwise.
    * @throws DriverException If the result set or the driver is closed.
    */
   public native boolean next() throws DriverException;

   /**
    * Returns the previous record of the result set.
    *
    * @return <code>true</code> if there is a previous record to go to in the result set; <code>false</code>, otherwise.
    * @throws DriverException If the result set or the driver is closed.
    */
   public native boolean prev() throws DriverException;

   /**
    * Given the column index (starting from 1), returns a short value that is represented by this column. Note that it is only possible to request
    * this column as short if it was created with this precision.
    *
    * @param colIdx The column index.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>0</code>.
    */
   public native short getShort(int colIdx);

   /**
    * Given the column name (case insensitive), returns a short value that is represented by this column. Note that it is only possible to request 
    * this column as short if it was created with this precision. This method is slightly slower then the method that accepts a column index.
    *
    * @param colName The column name.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>0</code>.
    */
   public native short getShort(String colName);

   /**
    * Given the column index (starting from 1), returns an integer value that is represented by this column. Note that it is only possible to request 
    * this column as integer if it was created with this precision.
    *
    * @param colIdx The column index.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>0</code>.
    */
   public native int getInt(int colIdx);

   /**
    * Given the column name (case insensitive), returns an integer value that is represented by this column. Note that it is only possible to request 
    * this column as integer if it was created with this precision. This method is slightly slower then the method that accepts a column index.
    *
    * @param colName The column name.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>0</code>.
    */
   public native int getInt(String colName);

   /**
    * Given the column index (starting from 1), returns a long value that is represented by this column. Note that it is only possible to request 
    * this column as long if it was created with this precision.
    *
    * @param colIdx The column index.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>0</code>.
    */
   public native long getLong(int colIdx);

   /**
    * Given the column name (case insensitive), returns a long value that is represented by this column. Note that it is only possible to request 
    * this column as long if it was created with this precision. This method is slightly slower then the method that accepts a column index.
    *
    * @param colName The column name.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>0</code>.
    */
   public native long getLong(String colName);

   /**
    * Given the column index (starting from 1), returns a float value that is represented by this column. Note that it is only possible to request 
    * this column as float if it was created with this precision.
    *
    * @param colIdx The column index.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>0.0</code>.
    */
   public native double getFloat(int colIdx);

   /**
    * Given the column name (case insensitive), returns a float value that is represented by this column. Note that it is only possible to request 
    * this column as float if it was created with this precision. This method is slightly slower then the method that accepts a column index.
    *
    * @param colName The column name.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>0.0</code>.
    */
   public native double getFloat(String colName);

   /**
    * Given the column index (starting from 1), returns a double value that is represented by this column. Note that it is only possible to request 
    * this column as double if it was created with this precision.
    *
    * @param colIdx The column index.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>0.0</code>.
    */
   public native double getDouble(int colIdx);

   /**
    * Given the column name (case insensitive), returns a double value that is represented by this column. Note that it is only possible to request 
    * this column as double if it was created with this precision. This method is slightly slower then the method that accepts a column index.
    *
    * @param colName The column name.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>0.0</code>.
    */
   public native double getDouble(String colName);

   /**
    * Given the column index (starting from 1), returns a char array that is represented by this column. Note that it is only possible to request 
    * this column as a char array if it was created as a string.
    *
    * @param colIdx The column index.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>null</code>.
    */
   public native char[] getChars(int colIdx);

   /**
    * Given the column name (case insensitive), returns a char array that is represented by this column. Note that it is only possible to request 
    * this column as a char array if it was created as a string. This method is slightly slower then the method that accepts a column index.
    *
    * @param colName The column name.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>null</code>.
    */
   public native char[] getChars(String colName);

   /**
    * Given the column index (starting from 1), returns a string that is represented by this column. Any column type can be returned as a string. 
    * <code>Double</code>/<code>float</code> values formatting will use the precision set with the <code>setDecimalPlaces()</code> method.
    *
    * @param colIdx The column index.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>null</code>
    */
   public native String getString(int colIdx);

   /**
    * Given the column name (case insensitive), returns a string that is represented by this column. Any column type can be returned as a string. 
    * <code>Double</code>/<code>float</code> values formatting will use the precision set with the <code>setDecimalPlaces()</code> method. This 
    * method is slightly slower then the method that accepts a column index.
    *
    * @param colName The column index.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>null</code>
    */
   public native String getString(String colName);

   /**
    * Given the column index (starting from 1), returns a byte (blob) array that is represented by this column. Note that it is only possible to 
    * request this column as a blob if it was created this way.
    *
    * @param colIdx The column index.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>null</code>.
    */
   public native byte[] getBlob(int colIdx);

   /**
    * Given the column name (case insensitive), returns a byte array (blob) that is represented by this column. Note that it is only possible to 
    * request this column as a blob if it was created this way. This method is slightly slower then the method that accepts a column index.
    *
    * @param colName The column name.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>null</code>.
    */
   public native byte[] getBlob(String colName);

   /**
    * Starting from the current cursor position, it reads all result set rows that are being requested. <code>first()</code>,  <code>last()</code>, 
    * <code>prev()</code>, or <code>next()</code> must be used to set the current position, but not  <code>beforeFirst()</code> or 
    * <code>afterLast()</code>. It doesn't return BLOB values. <code>null</code> is returned in their places instead.
    *
    * @param count The number of rows to be fetched, or -1 for all. 
    * @return A matrix, where <code>String[0]<code> is the first row, and <code>String[0][0], String[0][1]...</code> are the column elements of the 
    * first row. Returns <code>null</code> if here's no more element to be fetched. Double/float values will be formatted using the 
    * <code>setDecimalPlaces()</code> settings. If the value is SQL <code>NULL</code> or a <code>blob</code>, the value returned is 
    * <code>null</code>.
    */
   public native String[][] getStrings(int count);

   /**
    * Starting from the current cursor position, it reads all result set rows of the result set. <code>first()</code>,  <code>last()</code>, 
    * <code>prev()</code> or <code>next()</code> must be used to set the current position, but not <code>beforeFirst()</code> or 
    * <code>afterLast()</code>. It doesn't return BLOB values. <code>null</code> is returned in their places instead. 
    *
    * @return A matrix, where <code>String[0]<code> is the first row, and <code>String[0][0], String[0][1]...</code> are the column elements of the 
    * first row. Returns <code>null</code> if here's no more element to be fetched. Double/float values will be formatted using the 
    * <code>setDecimalPlaces()</code> settings. If the value is SQL <code>NULL</code> or a <code>blob</code>, the value returned is 
    * <code>null</code>.
    */
   public native String[][] getStrings();

   /**
    * Given the column index (starting from 1), returns a <code>Date</code> value that is represented by this column. Note that it is only possible 
    * to request this column as a date if it was created this way (DATE or DATETIME).
    *
    * @param colIdx The column index.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>null</code>.
    */
   public native Date getDate(int colIdx); // rnovais@567_3
   
   /**
    * Given the column name (case insensitive), returns a <code>Date</code> value that is represented by this column. Note that it is only possible 
    * to request this column as a date if it was created this way (DATE or DATETIME). This method is slightly slower then the method that accepts a 
    * column index.
    *
    * @param colName The column name.
    * @return The column value; if the value is SQL <code>NULL</code>, the value returned is <code>null</code>.
    */
   public native Date getDate(String colName); // rnovais@567_3

   /**
    * Given the column index (starting from 1), returns a <code>Time</code> (correspondent to a DATETIME data type) value that is represented by 
    * this column. Note that it is only possible to request this column as a date if it was created this way.
    *
    * @param colIdx The colum index.
    * @return The time of the DATETIME. If the DATETIME value is SQL <code>NULL</code>, the value returned is <code>null</code>.
    * @throws DriverException If the result set or the driver is closed.
    */
   public native Time getDateTime(int colIdx);

   /**
    * Given the column name (case insensitive), returns a <code>Time</code> (correspondent to a DATETIME data type) value that is represented by this
    * column. Note that it is only possible to request this column as a date if it was created this way. This method is slightly slower then the 
    * method that accepts a column index.
    *
    * @param colName The colum name.
    * @return The time of the DATETIME. If the DATETIME value is SQL <code>NULL</code>, the value returned is <code>null</code>.
    * @throws DriverException If the result set or the driver is closed.
    * @throws NullPointerException If the <code>colName</code> is null.
    */
   public native Time getDateTime(String colName) throws NullPointerException; // rnovais@567_3

   /**
    * Places this result set cursor at the given absolute row. This is the absolute physical row of the table. This method is usually used to restore
    * the row at a previous row got with the <code>getRow()</code> method.
    *
    * @param row The row to set the cursor.
    * @return <code>true</code> whenever this method does not throw an exception.
    * @throws DriverException If the result set is or the driver closed, or it is not possible to set the cursor at the given row.
    */
   public native boolean absolute(int row) throws DriverException;

   /**
    * Moves the cursor <code>rows</code> in distance. The value can be greater or lower than zero.
    *
    * @param rows The distance to move the cursor.
    * @return <code>true</code> whenever this method does not throw an exception.
    * @throws DriverException If the result set or the driver is closed, or it is not possible to set the cursor at the given row.
    */
   public native boolean relative(int rows) throws DriverException;

   /**
    * Returns the current physical row of the table where the cursor is. It must be used with <code>absolute()</code> method.
    *
    * @return The current physical row of the table where the cursor is.
    * @throws DriverException If the result set or the driver is closed.
    */
   public native int getRow() throws DriverException;

   /**
    * Sets the number of decimal places that the given column (starting from 1) will have when being converted to <code>String</code>.
    *
    * @param col The column.
    * @param places The number of decimal places.
    * @throws DriverException If the result set or the driver is closed, the column index is invalid, or the value for decimal places is invalid.
    */
   public native void setDecimalPlaces(int col, int places) throws DriverException;

   /**
    * Returns the number of rows of the result set.
    *
    * @return The number of rows.
    * @throws DriverException If the result set or the driver is closed.
    */
   public native int getRowCount() throws DriverException;

   /**
    * Given the column index (starting from 1), indicates if this column has a <code>NULL</code>.
    *
    * @param col The column index.
    * @return <code>true</code> if the value is SQL <code>NULL</code>; <code>false</code>, otherwise.
    * @throws DriverException If the result set or the driver is closed.
    */
   public native boolean isNull(int col) throws DriverException;

   /**
    * Given the column name (case insensitive), indicates if this column has a <code>NULL</code>.
    *
    * @param colName The column name.
    * @return <code>true</code> if the value is SQL <code>NULL</code>; <code>false</code>, otherwise.
    * @throws DriverException If the result set or the driver is closed.
    * @throws NullPointerException If the column name is null.
    */
   public native boolean isNull(String colName) throws DriverException, NullPointerException;

   /**
    * Finalizes the <code>PreparedStatement</code> object.
    */
   protected void finalize()
   {
      close();
   }
}
