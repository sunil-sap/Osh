/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.hybris.osh.utils;

import de.hybris.bootstrap.xml.UnicodeReader;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.util.CSVCellDecorator;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;



/**
 * This class parses a CSV InputStream to a list of maps. Each list entry represents a line of the stream and each entry
 * in the map is a parsed CSV field. By default, the reader ignores comments and empty lines. The format of the
 * CSV-source is expected as defined in <a href="http://www.rfc-editor.org/rfc/rfc4180.txt">RFC-4180</a>. Separator and
 * comment chars can differ from the ones in rfc.
 * <p>
 * Use of this CSVReader:
 * <p>
 * <ol>
 * <li><b>Create the Reader</b><br>
 * <code>csvreader = new CSVReader(inputStream, null ); //null for default encoding</code><br>
 * Use the constructor to set the encoding and the stream. There are also other constructors available.</li>
 * <li><b>Configure the Reader</b><br>
 * Example: <code>csvreader.setTextSeparator('?');</code><br>
 * If you skip this step, following defaults are set:<br>
 * <ul>
 * <li><code>commentOut = '#'</code> //chars for comment lines</li>
 * <li><code>fieldseparator = ';'</code> //separate the CSV fields</li>
 * <li><code>textseparator = '\"'</code> //enclose a CSV text</li>
 * <li><code>showComments = false</code> //all comment line are ignored</li>
 * <li><code>toSkip = 0</code> //there will be no lines ignored</li>
 * </ul>
 * ATTENTION: If <code>toSkip</code> is set to 3 the first 3 lines of data are skipped, regardless if they are empty
 * lines, comments, real header or data lines!
 * <li><b>Use the Reader</b>
 * 
 * <pre>
 * ArrayList i_am_the_parsed_csv_stream = new ArrayList();
 * while (csvreader.readNextLine())
 * {
 * 	i_am_the_parsed_csv_stream.add(csvreader.getLine());
 * }
 * </pre>
 * 
 * After this <code>while</code> loop the ArrayList contains all parsed CSV lines. Each line is a Map of the parsed CSV
 * fields. See {@link #parseLine(String)} for the setup of the map.</li>
 * <li><b>Close the Reader</b><br>
 * Do not forget to call <code>csvreader.close()</code> to close the reader.</li>
 * </ol>
 */
public class OSHCSVReader
{
	/** Used logger instance. **/
	private static final Logger log = Logger.getLogger(OSHCSVReader.class);

	/** Chars introducing a commented lines. */
	private char[] commentOut = new char[]
	{ CSVConstants.DEFAULT_COMMENT_CHAR };

	/** Field separator chars. Chars between two fields like the semicolon in <code>field1;field2</code>. */
	private char[] fieldSeparator = new char[]
	{ CSVConstants.DEFAULT_FIELD_SEPARATOR };

	/**
	 * Text separator char. Chars to explicitly enclose a field when using special characters like field separators
	 * within the field, as '"' in: <code>"fie;ld1";"f;ield2"</code>.
	 */
	private char textSeparator = CSVConstants.DEFAULT_QUOTE_CHARACTER;

	/** Enables returning of comment lines; as default comments are hidden. */
	private boolean showComments = false;

	/** Enables splitting one csv line across several lines by appending a '/'. */
	private boolean allowMultiLines = false;

	/** Text used for signalizing the continue of CSV-line in next line. */
	private final String multiLineSeparator = CSVConstants.DEFAULT_MULTI_LINE_SEPARATOR;

	/** Amount of lines to skip at head of source stream. */
	private int toSkip = 0;

	/**
	 * The current line number within source stream. It represents a logical number, because lines extended to many lines
	 * using the multi line character are count as one line, as well as lines containing fields enclosed with field
	 * separators and extended to many lines.
	 */
	private int currentLineNumber = 0;

	/** Used reader for reading from source stream. */
	private final BufferedReader reader;

	/** Buffer for holding the last line read from stream. */
	private String lastSourceLine = null;

	/** Buffer for holding the last parsed line from stream. */
	private Map<Integer, String> lastLine = null;

	/** Is the read process already finished? */
	private boolean finishedFlag = false;

	/**
	 * Maximal amount of lines buffered when reading a line containing a field with line breaks (All lines included in
	 * field will be buffered and concatenated afterwards to one line).
	 */
	private long maxBufferLines = CSVConstants.DEFAULT_MAX_MULTI_LINES;

	/** Map containing all decorators mapped to column positions. */
	private Map<Integer, CSVCellDecorator> cellDecorators = null;

	/**
	 * Opens the file with the given filename and sets the given encoding.
	 * 
	 * @param fileName
	 *           the filename of the CSV file
	 * @param encoding
	 *           the given encoding, default is {@link CSVConstants#DEFAULT_ENCODING}
	 * @throws UnsupportedEncodingException
	 *            thrown by not supported encoding type
	 * @throws FileNotFoundException
	 *            thrown if file not found
	 */
	public OSHCSVReader(final String fileName, final String encoding) throws UnsupportedEncodingException, FileNotFoundException
	{
		this(new FileInputStream(fileName), encoding);
	}

	/**
	 * Opens the file and sets the given encoding.
	 * 
	 * @param file
	 *           the CSV file
	 * @param encoding
	 *           the given encoding, default is {@link CSVConstants#DEFAULT_ENCODING}
	 * @throws UnsupportedEncodingException
	 *            thrown by not supported encoding type
	 * @throws FileNotFoundException
	 *            thrown if file not found
	 */
	public OSHCSVReader(final File file, final String encoding) throws UnsupportedEncodingException, FileNotFoundException
	{
		this(new FileInputStream(file), encoding);
	}

	/**
	 * Opens the given Inputstream with the given file.
	 * 
	 * @param is
	 *           the InputStream
	 * @param encoding
	 *           the given encoding, default is {@link CSVConstants#DEFAULT_ENCODING}
	 * @throws UnsupportedEncodingException
	 *            thrown by not supported encoding type
	 */
	public OSHCSVReader(final InputStream is, final String encoding) throws UnsupportedEncodingException
	{
		// CORE-4070
		//this( new InputStreamReader( is, (encoding == null || encoding.length()==0 )? DEFAULT_ENCODING : encoding));
		this(new UnicodeReader(is, "ISO-8859-1"));

		if (is == null)
		{
			throw new NullPointerException("Given input stream is null");
		}
	}

	/**
	 * Opens the given reader. The default encoding is set to {@link CSVConstants#DEFAULT_ENCODING}
	 * 
	 * @param reader
	 *           the reader
	 */
	public OSHCSVReader(final Reader reader)
	{
		this.reader = new BufferedReader(reader);
	}

	/**
	 * A convenience constructor for passing csv data as simple string object.
	 * 
	 * @param lines
	 *           the csv data as string
	 */
	public OSHCSVReader(final String lines)
	{
		this.reader = new BufferedReader(new StringReader(lines));
	}

	/**
	 * Returns a map containing the current csv decorators mapped to column positions. If there are no decorators
	 * declared, a new map will be created if <code>create</code> flag is set.
	 * 
	 * @param create
	 *           If no cell decorators declared and flag is set a new map will be returned
	 * @return map holding the current declared cell decorators
	 */
	protected Map<Integer, CSVCellDecorator> getDecoratorMap(final boolean create)
	{
		return this.cellDecorators != null ? this.cellDecorators : (create ? this.cellDecorators = new HashMap()
				: Collections.EMPTY_MAP);
	}

	/**
	 * Removes all declared cell decorators.
	 */
	public void clearAllCellDecorators()
	{
		getDecoratorMap(false).clear();
	}

	/**
	 * Removes one declared cell decorator.
	 * 
	 * @param position
	 *           the column position the decorator is mapped
	 */
	public void clearCellDecorator(final int position)
	{
		getDecoratorMap(false).remove(Integer.valueOf(position));
	}

	/**
	 * Maps a decorator to a column position.
	 * 
	 * @param position
	 *           position the decorator will be mapped
	 * @param decorator
	 *           the decorator
	 */
	public void setCellDecorator(final int position, final CSVCellDecorator decorator)
	{
		if (decorator != null)
		{
			getDecoratorMap(true).put(Integer.valueOf(position), decorator);
		}
		else
		{
			getDecoratorMap(false).remove(Integer.valueOf(position));
		}
	}

	/**
	 * Are there declared cell decorators?
	 * 
	 * @return true if decorators are declared
	 */
	public boolean hasCellDecorators()
	{
		return !getDecoratorMap(false).isEmpty();
	}

	/**
	 * Returns the decorator mapped to the given column position.
	 * 
	 * @param position
	 *           position of column for which the decorator is needed
	 * @return decorator mapped to given position or null if no one is declared
	 */
	public CSVCellDecorator getCellDecorator(final int position)
	{
		return getDecoratorMap(false).get(Integer.valueOf(position));
	}

	/**
	 * Reads next line from stream. If stream has reached end, <code>null</code> is returned and finished flag will be
	 * set.
	 * 
	 * @return next line from stream or <code>null</code>
	 */
	protected String readSrcLineFromStream()
	{
		String line = null;
		try
		{
			line = reader.readLine();
		}
		catch (final IOException e)
		{
			try
			{
				this.reader.close();
			}
			catch (final IOException ex)
			{
				log.warn("Can not close stream!", e);
			}
			throw new JaloSystemException(e);
		}
		if (line == null)
		{
			markFinished();
		}
		return line != null ? line : null;
	}

	/**
	 * Is reading from stream is already finished (has stream reached its end)?
	 * 
	 * @return true if the stream has reached its end and last call of <code>readNextLine</code> has returned null
	 */
	public boolean finished()
	{
		return this.finishedFlag;
	}

	/**
	 * Reads and parses next line from stream and returns true if the line was read and parsed successfully.
	 * 
	 * @return false if the end of the stream is reached, else true.
	 */
	public final boolean readNextLine()
	{
		do
		{
			boolean doSkip = false;
			do
			{
				doSkip = mustSkip(); // get skip flag BEFORE reading the line !
				List<String> multiLineBuffer = null;
				boolean gotMultiLine = false;
				do
				{
					this.lastSourceLine = readSrcLineFromStream();
					// check for stream end
					if (this.lastSourceLine == null)
					{
						return false;
					}

					boolean wrapped = false;
					final int wrappedLineNumber = this.currentLineNumber; // store start of wrapped line

					List<String> linebuffer = null;

					wrapped = isWrappedLine(this.currentLineNumber, this.lastSourceLine, wrapped);
					while (wrapped)
					{
						if (linebuffer == null)
						{
							linebuffer = new ArrayList();
							linebuffer.add(this.lastSourceLine);
						}

						this.lastSourceLine = readSrcLineFromStream();
						linebuffer.add(this.lastSourceLine);

						if (this.lastSourceLine == null)
						{
							throw new IllegalStateException("reached EOF and got odd number of '" + getTextSeparator()
									+ "'! File is corrupt. Error starts in line: \"" + linebuffer.get(0) + "\" and occurs somewhere in "
									+ "the following lines.");
						}
						else if (linebuffer.size() > this.maxBufferLines)
						{
							throw new IllegalStateException("Encounter a problem in line which starts with: " + linebuffer.get(0)
									+ ".\n" + "After reading the next " + this.maxBufferLines
									+ " lines, I give up. Check the file or set with "
									+ "CSVReader.setMaxBufferLines(int number) a greater number if you know that the csv file is ok");
						}

						wrapped = isWrappedLine(this.currentLineNumber, this.lastSourceLine, wrapped);
					}
					if (linebuffer != null)
					{
						final StringBuilder sb = new StringBuilder();
						final int s = linebuffer.size();
						for (int i = 0; i < s; i++)
						{
							if (i > 0)
							{
								sb.append(CSVConstants.DEFAULT_LINE_SEPARATOR);
							}
							sb.append(linebuffer.get(i));
						}
						linebuffer = null;
						this.lastSourceLine = sb.toString();
						this.currentLineNumber = wrappedLineNumber;
					}

					if (this.allowMultiLines)
					{
						// line continues ?
						final String l = trim(this.lastSourceLine, false, true); // got to trim line from end to get proper '\'
						if (l.endsWith(this.multiLineSeparator))
						{
							if (multiLineBuffer == null)
							{
								multiLineBuffer = new LinkedList();
							}
							// keep line in buffer but cut '\'
							multiLineBuffer.add(l.substring(0, l.length() - 1));
							gotMultiLine = true;
						}
						// not ? so maybe we're at end and need to append this line to multi line buffer
						else if (gotMultiLine)
						{
							if (multiLineBuffer != null)
							{
								multiLineBuffer.add(this.lastSourceLine);
							}
							gotMultiLine = false;
						}
					}
				}
				while (this.allowMultiLines && gotMultiLine);
				notifyNextLine(); // now increase line counter AND decrease skip counter!

				if (multiLineBuffer != null)
				{
					final StringBuilder sb = new StringBuilder();
					for (final String s : multiLineBuffer)
					{
						sb.append(s);
					}
					this.lastSourceLine = sb.toString();
					multiLineBuffer = null;
				}
				// now tokenize line 
				this.lastSourceLine = trim(this.lastSourceLine, true, true);
				this.lastLine = this.lastSourceLine.length() > 0 ? parseLine(this.lastSourceLine) : null;

				// if line is not completely empty
				if (this.lastLine != null && hasCellDecorators())
				{
					this.lastLine = applyDecorators(getDecoratorMap(false), this.lastLine);
				}
			}
			while (doSkip);
		}
		while (this.lastLine == null);

		return true;
	}

	/**
	 * Trims the given string like the trim() method of java.lang.String, but allows to disable trimming from start or
	 * end of the string. Another difference is, that all declared field separators will not be trimmed.
	 * 
	 * @param src
	 *           the string which will be trimmed
	 * @param fromStart
	 *           is trimming from the left side of the string enabled?
	 * @param fromEnd
	 *           is trimming from the right side of the string enabled?
	 * @return if there are no changes, the given string is returned, else a new trimmed copy
	 * @see String#trim()
	 */
	protected String trim(final String src, final boolean fromStart, final boolean fromEnd)
	{
		// if nothing to be trimmed, return given string
		if (src == null || src.length() == 0 || (!fromStart && !fromEnd))
		{
			return src;
		}
		// start and end pointer
		int len = src.length();
		int st = 0;
		// move start pointer to the right until a char appears which can not be trimmed
		if (fromStart)
		{
			while ((st < len) && (src.charAt(st) <= ' ') && !CSVUtils.isSeparator(this.fieldSeparator, src.charAt(st)))
			{
				st++;
			}
		}
		// move end pointer to the left until a char appears which can not be trimmed
		if (fromEnd)
		{
			while ((st < len) && (src.charAt(len - 1) <= ' ') && !CSVUtils.isSeparator(this.fieldSeparator, src.charAt(len - 1)))
			{
				len--;
			}
		}
		// if changes are made, return new string, else the original one
		return ((st > 0) || (len < src.length())) ? src.substring(st, len) : src;
	}

	/**
	 * Returns the parsed line as map.
	 * 
	 * @return the map
	 */
	public Map<Integer, String> getLine()
	{
		if (lastLine == null)
		{
			throw new IllegalStateException("end of stream already reached");
		}
		return lastLine;
	}

	/**
	 * Gets the last line read from stream.
	 * 
	 * @return the last read source line from the stream.
	 */
	public String getSourceLine()
	{
		if (lastSourceLine == null)
		{
			throw new IllegalStateException("end of stream already reached");
		}
		return lastSourceLine;
	}

	/**
	 * Tokenises the given line and returns a <code>Map</code> with following content: <br>
	 * <br>
	 * <code>Map{<br>
	 * 				{ 0:Integer, Field_1:String },   <br> 
	 * 				{ 1:Integer, Field_2:String },<br>
	 * 				...<br>
	 * 				{ n-1:Integer, Field_n:String }<br>
	 * 				}<br>
	 * </code><br>
	 * 
	 * @param line
	 *           the line
	 * @return a map with the parsed CSV fields or <code>null</code> if failure
	 */
	protected Map<Integer, String> parseLine(final String line)
	{
		final char[] separators = getFieldSeparator();

		Map<Integer, String> ret = null;
		int start = 0;
		final int length = line.length();

		while (start < length)
		{
			final boolean currentIsEscaped = line.charAt(start) == getTextSeparator();

			loop: for (int current = currentIsEscaped ? start + 1 : start; current < length; current++)
			{
				final char c = line.charAt(current);
				/*
				 * finish one column if: - separator is found - end of escaped string is found - end of whole line is found
				 */
				final boolean foundSeparator = (currentIsEscaped && c == getTextSeparator())
						|| (!currentIsEscaped && CSVUtils.isSeparator(separators, c));
				final boolean atEnd = current + 1 == length;
				if (foundSeparator || atEnd)
				{
					// skip if this is just a escaped '"'
					if (currentIsEscaped && !atEnd && line.charAt(current + 1) == getTextSeparator())
					{
						current++; // just jump over next '"'
						continue;
					}
					// end reached if final '"' or separator is hit
					else
					{
						if (ret == null)
						{
							ret = new HashMap();
						}
						final String value = currentIsEscaped ? CSVUtils.unescapeString(line.substring(start + 1, current).trim(),
								new char[]
								{ getTextSeparator() }, true) : line.substring(start, foundSeparator ? current : current + 1).trim();
						// stop parsing right now if we're not showing comment lines
						if (ret.isEmpty() && !isShowComments() && isCommentedOut(value))
						{
							return null;
						}
						ret.put(Integer.valueOf(ret.size()), value);

						final int nextSep = atEnd && !foundSeparator ? -1 : (!currentIsEscaped ? current : getNextSeparator(line,
								separators, current + 1));
						if (nextSep > -1)
						{
							// find next non-whitespace char
							final int nextChar = atEnd ? -1 : getNextNonWhitespace(line, nextSep + 1, separators);
							if (nextChar > -1)
							{
								// found start of new cell
								start = nextChar;
							}
							else
							{
								// end of line -> empty cell + abort
								ret.put(Integer.valueOf(ret.size()), "");
								start = length;
							}
						}
						else
						{
							// no sep -> abort
							start = length;
						}
						break loop;
					}
				}
			}
		}
		return ret != null ? ret : Collections.EMPTY_MAP;
	}

	/**
	 * Close the reader. Should be always called if parsing is finished.
	 * 
	 * @throws IOException
	 *            throws if error occurred
	 */
	public void close() throws IOException
	{
		this.reader.close();
		markFinished();
	}

	/**
	 * Close the reader quietly. The IOException will be catched and if the debug mode is enabled the exeption message is
	 * written to the log.
	 */
	public void closeQuietly()
	{
		try
		{
			if (this.reader != null)
			{
				close();
			}
		}
		catch (final IOException ioe)
		{
			if (log.isDebugEnabled())
			{
				log.debug("An IOException occured during closing quietly the CSVReader! " + ioe.getMessage());
			}
		}
	}


	/**
	 * Checks if a line is wrapped, means that the line is part of another line reasoned by a line break within a field.
	 * 
	 * @param lineNumber
	 *           number of given line for error handling
	 * @param line
	 *           line text that will be checked
	 * @param wasWrappedLineBefore
	 *           was the last line wrapped?
	 * @return true if line is wrapped, therefore part of another line
	 */
	private boolean isWrappedLine(final int lineNumber, final String line, final boolean wasWrappedLineBefore)
	{
		if (line == null || (!wasWrappedLineBefore && line.trim().length() == 0))
		{
			return false;
		}

		final char[] separators = getFieldSeparator();
		final char textSep = getTextSeparator();

		boolean quoted = wasWrappedLineBefore;
		boolean cellStarted = quoted;
		boolean cellEnded = false;

		final int s = line.length();
		for (int i = 0; i < s; i++)
		{
			final char c = line.charAt(i);
			if (quoted)
			{
				// found "
				if (c == textSep)
				{
					// a) skip doubled "s
					if (!(i + 1 == s) && line.charAt(i + 1) == textSep)
					{
						i++;
						continue;
					}
					// b) end of quoted text found -> switch off quote flag and mark cell as ended
					else
					{
						quoted = false;
						cellEnded = true;
					}
				}
			}
			else
			{
				// opening " 
				if (c == textSep)
				{
					// a) if cell was ended before complain
					if (cellEnded)
					{
						throw new IllegalStateException("unexpected text separator " + textSep + " after cell end at " + i
								+ " in line " + lineNumber + " : '" + line + "'");
					}
					// b) switch on quotation flag if cell has just begun (only whitespace chars after delimiter allowed)
					else if (!cellStarted)
					{
						quoted = true;
						cellStarted = true;
					}
					// c) otherwise ignore "s
				}
				// found cell separator -> reset starting and ending cell flag
				else if (CSVUtils.isSeparator(separators, c))
				{
					cellStarted = false;
					cellEnded = false;
				}
				// mark cell as started when a non-whitespace char is hit -> any subsequent "s will be ignored
				else if (!Character.isWhitespace(c))
				{
					// complain if a quoted cell just ended
					if (cellEnded)
					{
						throw new IllegalStateException("unexpected char " + c + " after cell end at " + i + " in line " + lineNumber
								+ " : '" + line + "'");
					}
					else
					{
						cellStarted = true;
					}
				}
			}
		}
		// line must be wrapped if we're still in quotation mode
		return quoted;
	}

	/**
	 * Returns the index of next occurrence of a separator in given line beginning at given start index.
	 * 
	 * @param line
	 *           line text to check
	 * @param separators
	 *           separators to check for occurrence in line
	 * @param startIndex
	 *           index from which the check will be started
	 * @return index of next separator in line or <code>-1</code> if no separator was found
	 */
	private int getNextSeparator(final String line, final char[] separators, final int startIndex)
	{
		final int s = line.length();
		for (int i = startIndex; i < s; i++)
		{
			if (CSVUtils.isSeparator(separators, line.charAt(i)))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of next occurrence of a non-whitespace in given line beginning at given start index, except a
	 * separator is a whitespace.
	 * 
	 * @param line
	 *           line text to check
	 * @param startIndex
	 *           index from which the check will be started
	 * @param separators
	 *           separators to check for occurrence in line (in case a separator is a whitespace)
	 * @return index of next non-whitespace (except separator is whitespace) in line or <code>-1</code> if only contains
	 *         whitespaces
	 */
	private int getNextNonWhitespace(final String line, final int startIndex, final char[] separators)
	{
		final int s = line.length();
		for (int i = startIndex; i < s; i++)
		{
			final char c = line.charAt(i);
			if (CSVUtils.isSeparator(separators, c) || !Character.isWhitespace(c))
			{
				return i;
			}
		}
		return -1;
	}


	//################ getter and setter

	/**
	 * Sets the text separator char which enclose in CSV a text(default is <code>"</code>). If line was read already a
	 * IllegalStateException will be thrown.
	 * 
	 * @param textseparator
	 *           the text separator char
	 */
	public void setTextSeparator(final char textseparator)
	{
		if (isReading())
		{
			throw new IllegalStateException("already reading stream, cannot set value");
		}
		this.textSeparator = textseparator;
	}

	/**
	 * @return true if all comment line are parsed
	 */
	public boolean isShowComments()
	{
		return showComments;
	}

	/**
	 * Returns true if the passed line is a comment (depends on {@link #setCommentOut(char[])}).
	 * 
	 * @param line
	 *           the passed line
	 * @return false if line is not a comment line
	 */
	protected boolean isCommentedOut(final String line)
	{
		if (line == null || line.length() == 0)
		{
			return false;
		}

		final char[] chars = this.commentOut;
		final char first = line.charAt(0);
		final int s = chars.length;
		for (int i = 0; i < s; i++)
		{
			if (first == chars[i])
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Set to true if all comment line should also parsed.
	 * 
	 * @param showComments
	 *           default value is false
	 */
	public void setShowComments(final boolean showComments)
	{
		if (isReading())
		{
			throw new IllegalStateException("already reading stream, cannot set value");
		}
		this.showComments = showComments;
	}

	/**
	 * Checks if reader is already reading from stream.
	 * 
	 * @return true if reader is already reading stream
	 */
	protected boolean isReading()
	{
		return this.lastSourceLine != null;
	}

	/**
	 * Changes whether or not the reader supports csv lines spread across multiple lines by putting a <code>\</code>
	 * (backslash) at the end of each unfinished line.
	 * <p>
	 * An example:
	 * 
	 * <pre>
	 * cell 1 ; cell 2 ; cell 3 starts here \
	 * ...and continues here ... \
	 * ... finally ends here ; cell4
	 * </pre>
	 * 
	 * This is read as one single line.
	 * 
	 * @param on
	 *           will multi line mode be enabled?
	 */
	public void setMultiLineMode(final boolean on)
	{
		if (isReading())
		{
			throw new IllegalStateException("already reading stream, cannot set value");
		}
		this.allowMultiLines = on;
	}

	/**
	 * Tells whether or not the reader supports csv lines spread across multiple lines by putting a <code>\</code>
	 * (backslash) at the end of each unfinished line.
	 * <p>
	 * Be default this feature is switched <b>off</b>.
	 * <p>
	 * An example:
	 * 
	 * <pre>
	 * cell 1 ; cell 2 ; cell 3 starts here \
	 * ...and continues here ... \
	 * ... finally ends here ; cell4
	 * </pre>
	 * 
	 * This is read as one single line.
	 * 
	 * @return is multi line mode enabled?
	 */
	public boolean isMultiLineMode()
	{
		return this.allowMultiLines;
	}

	/**
	 * Set the characters which indicates a comment line.
	 * 
	 * @param commentOut
	 *           default characters are '#'
	 */
	public void setCommentOut(final char[] commentOut)
	{
		if (isReading())
		{
			throw new IllegalStateException("already reading stream, cannot set value");
		}
		this.commentOut = commentOut;
	}

	/**
	 * Sets the CSV field separator char(s). Default is ';'.
	 * 
	 * @param fieldseparator
	 *           the char(s)
	 */
	public void setFieldSeparator(final char[] fieldseparator)
	{
		if (isReading())
		{
			throw new IllegalStateException("already reading stream, cannot set value");
		}
		this.fieldSeparator = fieldseparator;
	}

	/**
	 * @return the comment chars
	 */
	public char[] getCommentOut()
	{
		return this.commentOut;
	}

	/**
	 * @return the CSV field separator char(s)
	 */
	public char[] getFieldSeparator()
	{
		return this.fieldSeparator;
	}

	/**
	 * @return the CSV text separator char
	 */
	public char getTextSeparator()
	{
		return this.textSeparator;
	}

	/**
	 * Set the number of real lines which are skipped when {@link #readNextLine()} is called the first time.
	 * 
	 * @see #getCurrentLineNumber()
	 * @param i
	 *           must be a positive integer value
	 */
	public void setLinesToSkip(final int i)
	{
		if (isReading())
		{
			throw new IllegalStateException("already reading stream, cannot set value");
		}
		if (!(i >= 0 && i < Integer.MAX_VALUE))
		{
			throw new IllegalStateException("value is not a positive integer");
		}
		this.toSkip = i;
	}

	/**
	 * @return the position of the last line read via {@link #readNextLine()} within the input data (file).
	 */
	public int getCurrentLineNumber()
	{
		return this.currentLineNumber;
	}

	/**
	 * Increments the line number and decreases the line skip counter.
	 */
	protected void notifyNextLine()
	{
		this.currentLineNumber++;
		if (this.toSkip > 0)
		{
			this.toSkip--;
		}
	}

	/**
	 * Checks if lines have to be skipped still.
	 * 
	 * @return true if lines have to be skipped, otherwise false
	 */
	protected boolean mustSkip()
	{
		return this.toSkip > 0;
	}

	/**
	 * Set <code>maxBufferLines</code> to a new value. Must be larger than 5.
	 * 
	 * @param number
	 *           the number
	 */
	public void setMaxBufferLines(final int number)
	{
		if (number > 0)
		{
			maxBufferLines = number;
		}
	}

	/**
	 * Is reading of input stream finished (has reached end)?
	 * 
	 * @return true if input stream has reached end.
	 */
	public boolean isFinished()
	{
		return this.finishedFlag;
	}

	/**
	 * Sets the finished flag which indicates the reaching of stream end.
	 */
	protected void markFinished()
	{
		this.finishedFlag = true;
	}

	/**
	 * Convenience method which parses csv lines directly from the given reader.
	 * 
	 * @param reader
	 *           the reader holding the lines to be read
	 * @return a array of maps describing one parsed line each
	 */
	public static final Map<Integer, String>[] parse(final OSHCSVReader reader)
	{
		final List<Map<Integer, String>> ret = new LinkedList();
		while (reader.readNextLine())
		{
			ret.add(reader.getLine());
		}
		return ret.toArray(new Map[ret.size()]);
	}

	/**
	 * Convenience method which parses csv lines directly from the given string.
	 * 
	 * @param lines
	 *           text which will be read
	 * @param fieldSeparator
	 *           e.g. ;
	 * @param textSeparator
	 *           e.g. "
	 * @return a array of maps describing one parsed line each
	 */
	public static final Map<Integer, String>[] parse(final String lines, final char[] fieldSeparator, final char textSeparator)
	{
		final OSHCSVReader tmp = new OSHCSVReader(lines);
		tmp.setFieldSeparator(fieldSeparator);
		tmp.setTextSeparator(textSeparator);
		return parse(tmp);
	}

	/**
	 * Convenience method which parses csv lines directly from the given string.
	 * 
	 * @param lines
	 *           text which will be read
	 * @param fieldSeparator
	 *           e.g. ;
	 * @return a array of maps describing one parsed line each
	 */
	public static final Map<Integer, String>[] parse(final String lines, final char[] fieldSeparator)
	{
		final OSHCSVReader tmp = new OSHCSVReader(lines);
		tmp.setFieldSeparator(fieldSeparator);
		return parse(tmp);
	}

	/**
	 * Convenience method which parses csv lines directly from the given string.
	 * 
	 * @param lines
	 *           text which will be read
	 * @return a array of maps describing one parsed line each
	 */
	public static final Map<Integer, String>[] parse(final String lines)
	{
		final OSHCSVReader tmp = new OSHCSVReader(lines);
		return parse(tmp);
	}

	/**
	 * Applies given decorators to columns in given line.
	 * 
	 * @param decoratorMap
	 *           map containing the decorators mapped to column positions
	 * @param line
	 *           map containing columns of a line
	 * @return the line after applying given decorators
	 */
	public static Map applyDecorators(final Map<Integer, CSVCellDecorator> decoratorMap, final Map line)
	{
		if (decoratorMap != null && !decoratorMap.isEmpty())
		{
			/*
			 * We need to collect all decorated cells separately because all decorators should work upon the unchanged
			 * source line
			 */
			final Map<Integer, String> patched = new HashMap<Integer, String>(decoratorMap.size());
			for (final Map.Entry<Integer, CSVCellDecorator> e : decoratorMap.entrySet())
			{
				final Integer pos = e.getKey();
				patched.put(pos, e.getValue().decorate(pos.intValue(), line));
			}
			/*
			 * No apply to the line as whole; Note that we do not simply use putAll() because we have to avoid entries
			 * containing null values - these are removed from the line
			 */
			for (final Map.Entry<Integer, String> e : patched.entrySet())
			{
				final String value = e.getValue();
				if (value != null)
				{
					line.put(e.getKey(), value);
				}
				else
				{
					line.remove(e.getKey());
				}
			}
		}
		return line;
	}
}
