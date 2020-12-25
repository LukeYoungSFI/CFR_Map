package com.sfi.cfrmap.uims;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtil
{
  private static final char[] kHexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  private String sCharformat;
  private String privateKey;
  private String publicKey;
  
  public SecurityUtil(String sCharformat, String privateKey, String publicKey)
  {
    this.sCharformat = sCharformat;
    this.privateKey = privateKey;
    this.publicKey = publicKey;
  }
  
  public String encryptWithPrivateKey(String message)
    throws Exception
  {
    try
    {
      Cipher cipherClass = getCipherClass(this.privateKey, 1);
      byte[] aValuebytes = message.getBytes(this.sCharformat);
      
      byte[] result = cipherClass.doFinal(aValuebytes);
      
      return bufferToHex(result);
    }
    catch (BadPaddingException nspe)
    {
      System.out.println("BadPaddingException caught >" + nspe);
      throw new Exception("Error While Encrypting Message " + nspe.getMessage());
    }
    catch (IOException ioe)
    {
      System.out.println("Exception caught >" + ioe);
      throw new Exception("Error While Encrypting Message " + ioe.getMessage());
    }
    catch (IllegalBlockSizeException ibse)
    {
      System.out.println("Exception caught >" + ibse);
      throw new Exception("Error While Encrypting Message " + ibse.getMessage());
    }
    catch (Exception e)
    {
      System.out.println("Exception caught >" + e);
      throw new Exception("Error While Encrypting Message " + e.getMessage());
    }
  }
  
  public String encryptWithPublicKey(String message)
    throws Exception
  {
    try
    {
      Cipher cipherClass = getCipherClass(this.publicKey, 1);
      byte[] aValuebytes = message.getBytes(this.sCharformat);
      
      byte[] result = cipherClass.doFinal(aValuebytes);
      
      return bufferToHex(result);
    }
    catch (BadPaddingException nspe)
    {
      System.out.println("BadPaddingException caught >" + nspe);
      throw new Exception("Error While Encrypting Message " + nspe.getMessage());
    }
    catch (IOException ioe)
    {
      System.out.println("Exception caught >" + ioe);
      throw new Exception("Error While Encrypting Message " + ioe.getMessage());
    }
    catch (IllegalBlockSizeException ibse)
    {
      System.out.println("Exception caught >" + ibse);
      throw new Exception("Error While Encrypting Message " + ibse.getMessage());
    }
    catch (Exception e)
    {
      System.out.println("Exception caught >" + e);
      throw new Exception("Error While Encrypting Message " + e.getMessage());
    }
  }
  
  public String decryptWithPrivateKey(String value)
  {
    return decrypt(value, this.privateKey);
  }
  
  private String decrypt(String value, String key)
  {
    try
    {
      Cipher cipherClass = getCipherClass(key, 2);
      byte[] byteVal = hexToBuffer(value);
      byte[] result = cipherClass.doFinal(byteVal);
      return new String(result, this.sCharformat);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return value;
  }
  
  private Cipher getCipherClass(String key, int opMode)
    throws Exception
  {
    Cipher aCipher = null;
    SecretKeySpec myKey = null;
    IvParameterSpec ivspec = null;
    
    byte[] myIV = { 50, 51, 52, 53, 54, 55, 56, 57 };
    try
    {
      aCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
      myKey = new SecretKeySpec(key.getBytes(this.sCharformat), "DESede");
      ivspec = new IvParameterSpec(myIV);
      
      aCipher.init(opMode, myKey, ivspec);
    }
    catch (NoSuchAlgorithmException nsae)
    {
      System.out.println("In SecurityUtilServiceImpl Exception caught >" + nsae);
      throw new Exception("Internal Server Error" + nsae.getMessage());
    }
    catch (NoSuchPaddingException e)
    {
      System.out.println("In SecurityUtilServiceImpl Exception caught >" + e);
      throw new Exception("Internal Server Error" + e.getMessage());
    }
    catch (UnsupportedEncodingException e)
    {
      System.out.println("In SecurityUtilServiceImpl Exception caught >" + e);
      throw new Exception("In SecurityUtilServiceImpl Internal Server Error " + e.getMessage());
    }
    catch (InvalidKeyException e)
    {
      System.out.println("In SecurityUtilServiceImpl Exception caught >" + e);
      throw new Exception("In SecurityUtilServiceImpl Internal Server Error " + e.getMessage());
    }
    catch (InvalidAlgorithmParameterException e)
    {
      System.out.println("In SecurityUtilServiceImpl Exception caught >" + e);
      throw new Exception("In SecurityUtilServiceImpl Internal Server Error " + e.getMessage());
    }
    return aCipher;
  }
  
  private String bufferToHex(byte[] buffer)
  {
    return bufferToHex(buffer, 0, buffer.length);
  }
  
  private String bufferToHex(byte[] buffer, int startOffset, int length)
  {
    StringBuffer hexString = new StringBuffer(2 * length);
    int endOffset = startOffset + length;
    for (int i = startOffset; i < endOffset; i++) {
      appendHexPair(buffer[i], hexString);
    }
    return hexString.toString();
  }
  
  private void appendHexPair(byte b, StringBuffer hexString)
  {
    char highNibble = kHexChars[((b & 0xF0) >> 4)];
    char lowNibble = kHexChars[(b & 0xF)];
    
    hexString.append(highNibble);
    hexString.append(lowNibble);
  }
  
	/**
	 * Hex to buffer.
	 *
	 * @param hexString the hex string
	 * @return the byte[]
	 * @throws NumberFormatException the number format exception
	 */
	private byte[] hexToBuffer(String hexString) throws NumberFormatException {

		int length = hexString.length();
		byte[] buffer = new byte[(length + 1) / 2];
		boolean evenByte = true;
		byte nextByte = 0;
		int bufferOffset = 0;

		// If given an odd-length input string, there is an implicit
		// leading '0' that is not being given to us in the string.
		// In that case, act as if we had processed a '0' first.
		// It's sufficient to set evenByte to false, and leave nextChar
		// as zero which is what it would be if we handled a '0'.
		if ((length % 2) == 1)
			evenByte = false;

		for (int i = 0; i < length; i++) {
			char c = hexString.charAt(i);
			int nibble; // A "nibble" is 4 bits: a decimal 0..15

			if ((c >= '0') && (c <= '9'))
				nibble = c - '0';
			else if ((c >= 'A') && (c <= 'F'))
				nibble = c - 'A' + 0x0A;
			else if ((c >= 'a') && (c <= 'f'))
				nibble = c - 'a' + 0x0A;
			else
				throw new NumberFormatException("Invalid hex digit '" + c
						+ "'.");

			if (evenByte) {
				nextByte = (byte) (nibble << 4);
			} else {
				nextByte += (byte) nibble;
				buffer[bufferOffset++] = nextByte;
			}

			evenByte = !evenByte;
		}
		return buffer;
	}// hexToBuffer()
}

