/* ===========================================================
 * STRING : STRING�˻��ϱ�
 * ===========================================================
 *
 * (C) Copyright 2005, A&B Tech
 *
 * ---------------------
 * .java
 * ---------------------
 * Changes
 * -------
 * 2005�� 4�� 15�� : Version 1;
 *
 */

package com.anbtech.report.util;

import java.lang.SecurityException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public final class StringUtilities
{
  /**
   * Default Constructor.
   */
  private StringUtilities()
  {
  }

  /**
   * �־��� ���ڿ� start�� ���ڿ� base�� ���۵Ǿ����� �˻�
   *
   * @param base  the base string.
   * @param start  the starting text.
   *
   * @return true, if the string starts with the given starting text.
   */
  public static boolean startsWithIgnoreCase(final String base, final String start)
  {
    if (base.length() < start.length())
    {
      return false;
    }
    return base.regionMatches(true, 0, start, 0, start.length());
  }

  /**
   *  �־��� ���ڿ� end�� ���ڿ� base�� Ư����ġ���� ���۵Ǿ����� �˻�
   *  base.length() - end.length() : �˻������ġ
   * @param base  the base string.
   * @param end  the ending text.
   *
   * @return true, if the string ends with the given ending text.
   */
  public static boolean endsWithIgnoreCase(final String base, final String end)
  {
    if (base.length() < end.length())
    {
      return false;
    }
    return base.regionMatches(true, base.length() - end.length(), end, 0, end.length());
  }
  /**
   * �־��� Object���� String���� ��ȯ�Ͽ� �����ش� �ƴϸ� defaultValue���� ����
   *
   * @param value Object
   * @param defaultValue the default value
   * @return the parsed string.
   */
  public static String parseString (final Object value, final String defaultValue)
  {
    if (value == null)
    {
      return defaultValue;
    }
    try
    {
      return value.toString();
    }
    catch (Exception e)
    {
      return defaultValue;
    }
  }

  /**
   * �־��� ���ڿ��� integer�� �ٲپ� �����ϰ� �׷��� ���ϸ� defaultValue���� �����Ѵ�.
   *
   * @param value the to be parsed string
   * @param defaultValue the default value
   * @return the parsed string.
   */
  public static int parseInt (final String value, final int defaultValue)
  {
    if (value == null)
    {
      return defaultValue;
    }
    try
    {
      return Integer.parseInt(value);
    }
    catch (Exception e)
    {
      return defaultValue;
    }
  }
  /**
   * �־��� ���ڿ��� float�� �ٲپ� �����ϰ� �׷��� ���ϸ� defaultValue���� �����Ѵ�.
   *
   * @param value the to be parsed string
   * @param defaultValue the default value
   * @return the parsed string.
   */
  public static float parseFloat(final String value, final float defaultValue)
  {
    if (value == null)
    {
      return defaultValue;
    }
    try
    {
      Float vf = Float.valueOf(value);
	  return vf.floatValue();
	}
    catch (Exception e)
    { 
      return defaultValue;
    }
  }
  /**
   * �־��� ���ڿ��� double�� �ٲپ� �����ϰ� �׷��� ���ϸ� defaultValue���� �����Ѵ�.
   *
   * @param value the to be parsed string
   * @param defaultValue the default value
   * @return the parsed string.
   */
  public static double parseDouble(final String value, final double defaultValue)
  {
    if (value == null)
    {
      return defaultValue;
    }
    try
    {
	  return Double.parseDouble(value);
	}
    catch (Exception e)
    { 
      return defaultValue;
    }
  }

   /**
	 * ��Ʈ���� ���ڷ� �ٲܼ� �ִ��� �Ǵ��Ͽ� �ٲ����� Double Object��
	 *                                   �ƴϸ� String Object�� �����Ѵ�.
	 * @param  : String data
	 * @return : Object
	 **/
	public static Object parseObject(String data) 
	{
		if(data == null) data = "null";
		try
		{
			String c = Double.toString(Double.parseDouble(data));
			return new Double(c);
		}
		catch (Exception e)
		{
			return new String(data);
		}
	}

	/**
	 * Object�� ���ڷ� �ٲܼ� �ִ��� �Ǵ��Ͽ� �ٲ����� ������˿� �°� ����ϰ�
	 *                                   �ƴϸ� �������� �����Ѵ�. [�����̸� �����ϱ�����]
	 * @param  : Object data
	 * @return : format������ String
	 **/
	 public static String parseFormat(Object data,String format) 
	{
		DecimalFormat fmt = new DecimalFormat(format);			//format ����
		try
		{
			String c = fmt.format(Double.parseDouble(data.toString()));
			return c;
		}
		catch (Exception e)
		{
			return data.toString();
		}
	}

	/**
	 * double ���ڸ� ������˿� �°� ����ϰ�
	 *                                   �ƴϸ� �������� �����Ѵ�. [�����̸� �����ϱ�����]
	 * @param  : Object data
	 * @return : format������ String
	 **/
	 public static String doubleFormat(double data,String format) 
	{
		DecimalFormat fmt = new DecimalFormat(format);			//format ����
		try
		{
			String c = fmt.format(data);
			return c;
		}
		catch (Exception e)
		{
			return Double.toString(data);
		}
	}

	/**
	 * Object�� float�� ������������ ������������ �Ǵ��ϱ�
	 * Object�� ���ڷ� �Ǿ������� true �ƴϸ� false
	 *                                 
	 * @param  : Object data
	 * @return : format������ String
	 **/
	 public static boolean isDigit(Object data) 
	{
		try
		{
			double c = Double.parseDouble(data.toString());
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * Object�� int�� ������������ ������������ �Ǵ��ϱ�
	 * Object�� ���ڷ� �Ǿ������� true �ƴϸ� false
	 *                                 
	 * @param  : Object data
	 * @return : format������ String
	 **/
	 public static boolean isInteger(Object data) 
	{
		if(data == null) return false;
		try
		{
			int c = Integer.parseInt(data.toString());
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * String�� ������������ ������������ �Ǵ��ϱ�
	 * Object�� ���ڷ� �Ǿ������� true �ƴϸ� false
	 *                                 
	 * @param  : Object data
	 * @return : format������ String
	 **/
	 public static boolean isNumber(String data) 
	{
		try
		{
			double c = Double.parseDouble(data);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
  /**
   * Parses the given string into a boolean value. This returns true, if
   * the string's value is "true".
   * 
   * @param attribute the string that should be parsed.
   * @param defaultValue the default value, in case the string is null.
   * @return the parsed value.
   */
  public static boolean parseBoolean (final String attribute, final boolean defaultValue)
  {
    if (attribute == null)
    {
      return defaultValue;
    }
    if (attribute.equals("true"))
    {
      return true;
    }
    return false;
  }

  /**
     * Perform a search/replace operation on a String
     * There are String methods to do this since (JDK 1.4)
     *
     * @param inputString  the String to have the search/replace operation.
     * @param searchString  the search String.
     * @param replaceString  the replace String.
     *
     * @return The String with the replacements made.
     */
    public static String searchReplace(String inputString,
                                       String searchString,
                                       String replaceString) {

        int i = inputString.indexOf(searchString);
        if (i == -1) {
            return inputString;
        }

        String r = "";
        r += inputString.substring(0, i) + replaceString;
        if (i + searchString.length() < inputString.length()) {
            r += searchReplace(
                inputString.substring(i + searchString.length()),
                searchString, replaceString
            );
        }

        return r;
    }

  /**
   * �ѱ� ���ڵ�
   * @param String
   * @return �ѱ� ���ڵ�
   */
  public static String toHanguel(String s) {
		try {
		   if (s != null){
				s = s.replace('\'','`');
				return (new String(s.getBytes("8859_1"),"EUC_KR"));
		   }
		   return s;
		} catch (UnsupportedEncodingException e) {
		   return "Encoding Error";
		}
    }

}
