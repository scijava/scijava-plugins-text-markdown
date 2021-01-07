
package org.scijava.plugins.text.markdown;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scijava.Context;
import org.scijava.io.handle.DataHandleService;
import org.scijava.text.TextService;

public class MarkdownTextFormatTest {

	private static Context context;

	private final String markdown = "ImageJ*2* uses **SciJava**\n";
	private final String expected =
		"<p>ImageJ<em>2</em> uses <strong>SciJava</strong></p>\n";

	@BeforeClass
	public static void setUp() {
		context = new Context(TextService.class, DataHandleService.class);
	}

	@AfterClass
	public static void tearDown() {
		context.dispose();
	}

	@Test
	public void testMarkdownToHTML() {
		MarkdownTextFormat format = new MarkdownTextFormat();
		String html = format.asHTML(markdown);
		assertEquals("Format markdown as HTML", expected, html);
	}

	@Test
	public void testTextServiceMarkdown() throws IOException {
		File tmpFile = File.createTempFile("MarkdownTextFormatTest", ".md");
		tmpFile.deleteOnExit();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFile))) {
			writer.write(markdown);
		}
		TextService textService = context.getService(TextService.class);
		String html = textService.asHTML(tmpFile);
		assertEquals("Handler for Markdown format", MarkdownTextFormat.class,
			textService.getHandler(tmpFile).getClass());
		assertEquals("Markdown converted from file", "<html><body>" + expected +
			"</body></html>", html);
	}
}
