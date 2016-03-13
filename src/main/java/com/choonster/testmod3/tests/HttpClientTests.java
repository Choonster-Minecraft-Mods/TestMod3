package com.choonster.testmod3.tests;

import org.apache.http.Consts;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.net.URI;

/**
 * Tests for the Apache HTTP Client Fluent API.
 *
 * @author Choonster
 */
public class HttpClientTests extends Test {
	public static final HttpClientTests INSTANCE = new HttpClientTests();

	/**
	 * The URL of the Gist.
	 */
	private static final URI GIST_URL = URI.create("https://gist.github.com/Choonster/0d78bd2ba74e64d7ee8d/raw/e8954eebdf82ba9a755726d8edad0f2aa4972bde/HttpClientTest.txt");

	/**
	 * The content of the Gist.
	 */
	private static final String GIST_CONTENT = "This is a test of the Apache HTTP Client Fluent API for TestMod3.";

	@Override
	protected void runTest() {
		try {
			final Content content = Request.Get(GIST_URL).execute().returnContent();
			assertEqual(Consts.UTF_8, content.getType().getCharset());
			assertEqual(GIST_CONTENT, content.asString());
		} catch (IOException e) {
			exceptionThrown(e, "Failed to download Gist");
		}
	}
}
