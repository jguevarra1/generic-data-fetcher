//file:noinspection GrMethodMayBeStatic
package com.datafetcher.service

import jakarta.inject.Singleton
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.io.FilenameUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.mozilla.universalchardet.Constants
import org.mozilla.universalchardet.ReaderFactory
import org.mozilla.universalchardet.UniversalDetector

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Path

/**
 * FileConverter - Contains the methods for converting a file into a UTF-8 Encoded CSV File
 */
@Singleton
class ConverterService
{
    /**
     * readFile - Reads a file and checks if it is a regular file to parse or if it is HTML
     *            Traverses over a directory to grab all other files in other sub-folders if necessary
     *            Skips over a file if it is already CSV formatted and UTF-8 encoded
     *
     * @param path - The path of the file/directory to read
     * @param delimiter - The user given delimiter of the file
     * @param fileType - The user given file type to indicate which type of parsing to perform
     * @param tableIndex - The optional user given index of the HTML table the user would like parsed
     * @param headers - The list of headers to put at the beginning of a CSV file if not provided already
     * @param selector - The selector of the unordered list to parse
     * @param listIndexes - The indexes of the HTML unordered list that the user would like parsed
     */
    void readFile(Path filePath, char delimiter, String fileType, int tableIndex, List<String> headers, String selector, List<Integer> listIndexes)
    {
        if (!(isCSV(filePath.toString()) && isUTF8(new File(filePath.toString()))))
        {
            String newFileName = renameFile(filePath.toString())

            switch (fileType)
            {
                case "Text File":
                    parseText(filePath, newFileName, delimiter, headers)
                    break

                case "Zip File":
                    parseText(filePath, newFileName, delimiter, headers)
                    break

                case "HTML Table":
                    parseHTMLTable(filePath, newFileName, tableIndex)
                    break

                case "HTML Unordered List":
                    parseUnorderedList(filePath, newFileName, listIndexes, selector, headers)
                    break

                default:
                    print("Unable to parse the file type: " + fileType)
            }
        }
    }

    /**
     * parseText - Parses the text file given its delimiter and writes the contents into CSV format
     *
     * This uses the ReaderFactory class from universalchardet library to create a bufferedreader of correct encoding
     * Uses CSVParser to read the files in a CSV encoding (ex to account for numbers with commas)
     *
     * @param filePath - The local path of the file that ie being parsed
     * @param newFileName - The name of the file to be written
     * @param delimiter - The user given delimiter of the file
     * @param headers - The headers of the file if not already provided
     */
    private void parseText(Path filePath, String newFileName, char delimiter, List<String> headers)
    {
        Reader reader = null

        try
        {
            File file = new File(filePath.toString())
            reader = ReaderFactory.createBufferedReader(file, Charset.defaultCharset())
            CSVParser parser = CSVParser.parse(reader, CSVFormat.newFormat(delimiter))
            writeRecordToFile(parser, newFileName, headers)
        }

        finally
        {
            if (reader != null)
            {
                reader.close()
            }
        }
    }

    /**
     * parseHTMLTable - Parses an HTML table given its index and writes its table contents into CSV format
     *
     * Uses JSoup library for parsing HTML text to grab specific HTML tags
     *
     * @param filePath - The local path of the file that is being parsed
     * @param newFileName - The name of the file to be written
     * @param tableIndex - The user given location of the HTML table they would like parsed
     */
    private void parseHTMLTable(Path filePath, String newFileName, int tableIndex)
    {
        List<Elements> tableData = new ArrayList<>()

        File file = new File(filePath.toString())

        Document doc = Jsoup.parse(file, null)
        Element table = doc.select("table").get(tableIndex)

        Elements rows = table.select("tr")
        Elements headers = table.select("th")

        for (Element row in rows)
        {
            Elements col = row.select("td")
            tableData.add(col)
        }

        writeTableToFile(headers, tableData, newFileName)
    }

    /**
     * parseUnorderedList - Parses a HTML unordered list given its selector and writes its contents into CSV format
     *
     * Uses JSoup library for parsing HTML text to grab specific HTML tags
     *
     * @param filePath - The local path of the file that is being parsed
     * @param newFileName - The name of the file to be written
     * @param listIndexes - The user given locations of the unordered lists they would like parsed
     * @param selector - The selector of the unordered list
     * @param headers - The headers of the file if not already provided
     */
    private void parseUnorderedList(Path filePath, String newFileName, List<Integer> listIndexes, String selector, List<String> headers)
    {
        if (selector == null || selector.isEmpty())
        {
            throw new Exception("A selector wasn't entered!\n")
        }

        File file = new File(filePath.toString())

        Document doc = Jsoup.parse(file, null)

        for (int index in listIndexes)
        {
            Element div = doc.select(selector).get(index)
            Element ul = div.select("ul").first()
            Elements listElements = ul.select("li")

            writeListToFile(listElements, newFileName, headers)
        }
    }

    /**
     * WriteRecordToFile - Writes the parsed records from a text to a CSV formatted file with UTF-8 encoding
     *                     Creates a new file if it doesn't already exist
     *
     * This uses the CSVPrinter to print the records into a CSV Format
     *
     * @param parser - The CSVParser containing the records
     * @param fileName - The name of the file to be written to
     * @param headers - The headers of the file if not already provided
     */
    private void writeRecordToFile(CSVParser parser, String newFileName, List<String> headers)
    {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
                (new FileOutputStream(newFileName), StandardCharsets.UTF_8))

        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)

        for (String header in headers)
        {
            printer.print(header)
            printer.flush()
        }

        printer.println()
        printer.printRecords(parser.getRecords())

        printer.close()
        writer.close()
    }

    /**
     * writeTableToFile - Writes the parsed HTML from Jsoup to a CSV formatted file with UTF-8 encoding
     *                    Creates a new file if it doesn't already exist
     *
     * @param headers - The headers for the CSV file
     * @param data - The data cells found in the HTML table
     * @param newFileName The name of the file to be written to
     */
    private void writeTableToFile(Elements headers, List<Elements> data, String newFileName)
    {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
                (new FileOutputStream(newFileName), StandardCharsets.UTF_8))
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)

        for (Element header in headers) {
            printer.print(header.text())
            printer.flush()
        }

        for (Elements dataCells in data) {
            printer.printRecord(dataCells.eachText())
            printer.flush()
        }

        printer.close()
        writer.close()
    }

    /**
     * writeListToFile - Writes the parsed HTML unordered list from Jsoup to a CSV formatted file with UTF-8 encoding
     *                   Creates a new file if it doesn't already exist
     *
     * @param listElements - The elements of the unordered list to write
     * @param newFileName The name of the file to be written to
     * @param headers - The headers of the file if not already provided
     */
    private void writeListToFile(Elements listElements, String newFileName, List<String> headers)
    {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
                (new FileOutputStream(newFileName, true), StandardCharsets.UTF_8))
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)

        for (String header in headers)
        {
            printer.print(header)
            printer.println()
            printer.flush()
        }

        for (Element listElement in listElements)
        {
            printer.print(listElement.text())
            printer.println()
            printer.flush()
        }

        printer.close()
        writer.close()
    }

    /**
     * isCSV - Checks if a file is a CSV file
     *
     * @param filename - The name of the file
     * @return Returns true if the file is a CSV file
     */
    boolean isCSV(String filename)
    {
        return FilenameUtils.getExtension(filename) == "csv"
    }

    /**
     * isUTF8 - Uses the juniversalchardet library to detect the encoding of a file
     *
     * @param file - The file to check encoding
     * @return Returns true if a file is UTF-8 encoded
     */
    boolean isUTF8(File file)
    {
        String encoding = UniversalDetector.detectCharset(file)

        if (encoding != null)
        {
            return encoding == Constants.CHARSET_UTF_8
        }

        return false
    }

    /**
     * renameFile - Renames a file to have the ".csv" extension
     *
     * @param fileName - The name of the file to change
     * @return Returns the file name with the ".csv" extension
     */
    String renameFile(String fileName)
    {
        StringBuilder sb = new StringBuilder(FilenameUtils.removeExtension(fileName))
        sb.append(".csv")

        return sb.toString()
    }
}
