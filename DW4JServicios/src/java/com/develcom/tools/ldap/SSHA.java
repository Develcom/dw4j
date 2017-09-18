/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.tools.ldap;

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Level;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
import com.develcom.logs.Traza;

/**
 *
 * @author develcom
 */
public class SSHA {

    private Traza traza = new Traza(SSHA.class);
    //private static BASE64Encoder enc = new BASE64Encoder();

    //private static BASE64Decoder dec = new BASE64Decoder();


    private static Base64 enc = new Base64();

    private static Base64 dec = new Base64();


    private boolean verbose = false;

    private MessageDigest sha = null;

    private static SSHA inst = new SSHA("SHA");

    public static SSHA SHA1 = new SSHA("SHA");

    public static SSHA SHA2 = new SSHA("SHA-256");

	public static SSHA getInstance(){
		return inst;
	}

	/**
	 * @param shaEnc
	 */
	public void setAlgorithm(String shaEnc) {
		inst = new SSHA(shaEnc);
	}

	int size=20;

	/**
	* public constructor
	*/
	public SSHA(String alg) {
		verbose = false;

		if(alg.endsWith("256")){
			size = 32;
		}
		if(alg.endsWith("512")){
			size = 64;
		}

		try {
			sha = MessageDigest.getInstance(alg);
		} catch (java.security.NoSuchAlgorithmException e) {
			traza.trace("Construction failed: ", Level.ERROR, e);
		}
	}

	/**
	* Create Digest for each entity values passed in
	*
	* @param salt
	* String to set the base for the encryption
	* @param entity
	* string to be encrypted
	* @return string representing the salted hash output of the encryption
	* operation
	*/
	public String createDigest(String salt, String entity) {
		return createDigest(salt.getBytes(),entity);
	}

	/**
	* Create Digest for each entity values passed in
	*
	* @param salt
	* byte array to set the base for the encryption
	* @param entity
	* string to be encrypted
	* @return string representing the salted hash output of the encryption
	* operation
	*/
	public String createDigest(byte[] salt, String entity) {
		String label = "{SSHA}";

		// Update digest object with byte array of the source clear text
		// string and the salt
		sha.reset();
		sha.update(entity.getBytes());
		sha.update(salt);

		// Complete hash computation, this results in binary data
		byte[] pwhash = sha.digest();

		if (verbose) {
			traza.trace("pwhash, binary represented as hex: "+ toHex(pwhash) + " n", Level.INFO);
			traza.trace("Putting it all together: ", Level.INFO);
			traza.trace("binary digest of password plus binary salt: "+ pwhash + salt, Level.INFO);
			traza.trace("Now we base64 encode what is respresented above this line ...", Level.INFO);
		}

		return label + new String(enc.encode(concatenate(pwhash, salt)));
	}

	/**
	* Create Digest for each entity values passed in.  A random salt is used.
	*
	* @param entity
	* string to be encrypted
	* @return string representing the salted hash output of the encryption
	* operation
	*/
	public String createDigest(String entity) {
		return inst.createDigest(randSalt(),entity);
	}

	/**
	* Check Digest against entity
	*
	* @param digest
	* is digest to be checked against
	* @param entity
	* entity (string) to be checked
	* @return TRUE if there is a match, FALSE otherwise
	*/
	public boolean checkDigest(String digest, String entity) {
		return inst.checkDigest0(digest,entity);
	}

	/**
	* Check Digest against entity
	*
	* @param digest
	* is digest to be checked against
	* @param entity
	* entity (string) to be checked
	* @return TRUE if there is a match, FALSE otherwise
	*/
	private boolean checkDigest0(String digest, String entity) {
		boolean valid = true;

		// ignore the {SSHA} hash ID
		digest = digest.substring(6);

		// extract the SHA hashed data into hs[0]
		// extract salt into hs[1]
		byte[][] hs=null;
		try {
			//hs = split(dec.decodeBuffer(digest), size);

			hs = split(dec.decodeBase64(digest), size);

		} catch (Exception e) {
                    traza.trace("problemas en la decodificacion de la contraseña", Level.ERROR, e);
		}
		byte[] hash = hs[0];
		byte[] salt = hs[1];

		// Update digest object with byte array of clear text string and salt
		sha.reset();
		sha.update(entity.getBytes());
		sha.update(salt);

		// Complete hash computation, this is now binary data
		byte[] pwhash = sha.digest();

		if (verbose) {
			traza.trace("Salted Hash extracted (in hex): " + toHex(hash)+ " " + "nSalt extracted (in hex): " + toHex(salt), Level.INFO);
			traza.trace("Hash length is: " + hash.length+ " Salt length is: " + salt.length, Level.INFO);
			traza.trace("Salted Hash presented in hex: " + toHex(pwhash), Level.INFO);
		}

		if (!MessageDigest.isEqual(hash, pwhash)) {
                    valid = false;
                    if(verbose){
                        traza.trace("Hashes DON'T match: " + entity, Level.INFO);
                    }
		}

		if (MessageDigest.isEqual(hash, pwhash)) {
                    valid = true;
                    if(verbose) {
                        traza.trace("Hashes match: " + entity, Level.INFO);
                    }
		}

		return valid;
	}

	/**
	* set the verbose flag
	*/
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	* Combine two byte arrays
	*
	* @param l
	*      first byte array
	* @param r
	*      second byte array
	* @return byte[] combined byte array
	*/
	private static byte[] concatenate(byte[] l, byte[] r) {
		byte[] b = new byte[l.length + r.length];
		System.arraycopy(l, 0, b, 0, l.length);
		System.arraycopy(r, 0, b, l.length, r.length);
		return b;
	}

	/**
	* split a byte array in two
	*
	* @param src
	*      byte array to be split
	* @param n
	*      element at which to split the byte array
	* @return byte[][] two byte arrays that have been split
	*/
	private static byte[][] split(byte[] src, int n) {
		byte[] l, r;
		if (src == null || src.length <= n) {
			l = src;
			r = new byte[0];
		} else {
			l = new byte[n];
			r = new byte[src.length - n];
			System.arraycopy(src, 0, l, 0, n);
			System.arraycopy(src, n, r, 0, r.length);
		}
		byte[][] lr = { l, r };
		return lr;
	}

	private static String hexits = "0123456789abcdef";

	/**
	* Convert a byte array to a hex encoded string
	*
	* @param block
	*      byte array to convert to hexString
	* @return String representation of byte array
	*/
	private static String toHex(byte[] block) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < block.length; ++i) {
			buf.append(hexits.charAt((block[i] >>> 4) & 0xf));
			buf.append(hexits.charAt(block[i] & 0xf));
		}

		return buf + "";
	}

	public byte[] randSalt(){
		int saltLen = 8;
		byte[] b = new byte[saltLen];
		for(int i = 0;i<saltLen;i++){
			byte bt = (byte)(((Math.random())*256)-128);
			//System.out.println(bt);
			b[i]=bt;
		}
		return b;
	}


}
