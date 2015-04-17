package com.hybris.osh.core.storelocator.googlemaps;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;



public class UrlSigner
{
	private static final Logger LOG = Logger.getLogger(UrlSigner.class);
	// Note: Generally, you should store your private key someplace safe
	// and read them into your code


	byte[] key;

	public String getSignature(final String key, final String urlAddress) throws IOException, InvalidKeyException,
			NoSuchAlgorithmException, URISyntaxException
	{

		//final String keyString = "key ";

		// The URL shown in these examples must be already
		// URL-encoded. In practice, you will likely have code
		// which assembles your URL from user or web service input
		// and plugs those values into its parameters.
		final String urlString = urlAddress.toString();
		//http://maps.googleapis.com/maps/api/geocode/xml?address=%2C+95123%2C+United+States&sensor=true&client=gme-orchardsupplyhardware
		// This variable stores the binary key, which is computed from the string (Base64) key


		// Convert the string to a URL so we can parse it
		final URL url = new URL(urlString);

		final UrlSigner signer = new UrlSigner(key);
		final String request = signer.signRequest(url.getPath(), url.getQuery());

		//LOG.info("Signed URL :" + url.getProtocol() + "://" + url.getHost() + request);
		return request;
	}

	public UrlSigner(String keyString) throws IOException
	{
		// Convert the key from 'web safe' base 64 to binary
		keyString = keyString.replace('-', '+');
		keyString = keyString.replace('_', '/');
		//LOG.info("Key: " + keyString);
		key = Base64.decode(keyString);
	}

	public String signRequest(final String path, final String query) throws NoSuchAlgorithmException, InvalidKeyException,
			UnsupportedEncodingException, URISyntaxException
	{

		// Retrieve the proper URL components to sign
		final String resource = path + '?' + query;

		// Get an HMAC-SHA1 signing key from the raw key bytes
		final SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");

		// Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
		final Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(sha1Key);

		// compute the binary signature for the request
		final byte[] sigBytes = mac.doFinal(resource.getBytes());

		// base 64 encode the binary signature
		String signature = Base64.encodeBytes(sigBytes);

		// convert the signature to 'web safe' base 64
		signature = signature.replace('+', '-');
		signature = signature.replace('/', '_');

		return resource + "&signature=" + signature;
	}

}
