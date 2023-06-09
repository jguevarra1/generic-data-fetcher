//file:noinspection GrMethodMayBeStatic
package com.datafetcher.service

import jakarta.inject.Singleton
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

// TODO: Replace all printlns in this class with Logging using Log42J

/** The class containing the methods to download a file to local disk */
@Singleton
class DownloadService
{
    /** Location of where the file was downloaded to */
    String localPathSaved = null

    /**
     * urlToName - Helper function that captures the last part of a URL
     *             ex. http://download.geonames.org/export/dump/AU.zip -> AU.zip
     *
     * @param url - The URL to parse
     * @return Returns the last part of a URL
     */
    String urlToName(String url)
    {
        if (url.lastIndexOf('/') == -1) {
            return url
        }

        return url.substring(url.lastIndexOf('/'))
    }

    /**
     * formNewUrl - Forms a new URL with the parsed link from the 'a href' attribute value added
     *              ex. https://download.geonames.org/export/zip/ -> https://download.geonames.org/export/zip/AD.zip
     *
     * @param source - The base URL containing the path to the directory listing
     * @param linkAdd - The string to add to the end of the URL
     * @return Returns a new URL with the 'a href' attribute value added to it
     */
    URL formNewUrl(URL source, String linkAdd)
    {
        URI uri = source.toURI()
        String newLink = uri.getPath() + linkAdd
        URI newUri = uri.resolve(newLink)

        return newUri.toURL()
    }

    /**
     * parseDictionary - Parses a webpage for all links with the 'a href' attribute to fetch all
     *                   zip/html links in the page using JSoup
     *
     * @param directory - The URL containing a directory listing of other URLs
     * @throws MalformedURLException Throws if the URL could not be opened
     */
    void parseDirectory(URL directory)
    {
        try
        {
            Document doc = Jsoup.connect(directory.toString()).get()
            Elements files = doc.select("a[href~=(?i)\\w*\\.(zip|html)]")

            for (Element link in files)
            {
                URL urlToDownload = formNewUrl(directory, urlToName(link.attr("href")))
                downloadFile(urlToDownload)
            }
        }

        catch(MalformedURLException)
        {
            println("Unable to open the URL: " + directory.toString())
        }
    }

    /**
     * getURLS - Helper method for reading in URLs into a HashSet from a file
     *
     * @param filePath - The local file path
     * @return Returns a HashSet containing the URLs to download
     */
    HashSet<String> getURLS(String filePath)
    {
        String line
        HashSet<String> links = new HashSet<>()
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath, "src/main/resources/links.txt"),
                StandardCharsets.UTF_8))
        {
            while ((line = reader.readLine()) != null)
            {
                links.add(line)
            }
        }

        catch(IOException)
        {
            println("Could not open the file: " + filePath)
        }

        return links
    }

    /**
     * downloadFile - Downloads a single file from a URL to a local disk location
     *
     * @param urlAsString - The URL in String form
     * @param localPath - The path location to save the downloaded file to
     */
    void downloadFile(String urlAsString, String localPath)
    {
        URL url = new URL(urlAsString)
        String filePath = localPath + urlToName(url.toString())
        setLocalPathSaved(filePath)

        try
        {
            File file = new File(filePath)
            FileUtils.copyURLToFile(url, file)
            println("Downloading " + url.toString())

            if (isZip(file))
            {
                String fileNoZipExtension = FilenameUtils.removeExtension(filePath)
                unZipFile(filePath, fileNoZipExtension)
                println("Unzipping " + file.toString())
                file.delete()
            }
        }

        catch(IllegalArgumentException e)
        {
            print("Url" + url.toString() + " was not valid")
        }
    }

    /**
     * downloadFiles - Downloads files from a set of URLs to a local disk location
     *
     * @param urlList - The set of URLs to download
     * @param localPath - The path location to save the downloaded file to
     * @throws IllegalArgumentException - Throws an error if the URL is not valid
     */
    void downloadFiles(HashSet<String> urlList, String localPath)
    {
        for (String url in urlList)
        {
            URL url_main = new URL(url)

            String filePath = localPath + urlToName(url)
            setLocalPathSaved(filePath)

            try
            {
                File file = new File(filePath)
                println("Downloading " + url)
                FileUtils.copyURLToFile(url_main, file)

                if (isZip(file))
                {
                    String fileNoZipExtension = FilenameUtils.removeExtension(filePath)
                    println("Unzipping " + file.toString())
                    unZipFile(filePath, fileNoZipExtension)
                    file.delete()
                }
            }

            catch (IllegalArgumentException)
            {
                print("Url " + url + " was not valid")
            }
        }
    }

    /**
     * unZipFile - Unzips a zip file and extracts its contents to local disk
     *
     * @param fileName - The name of the zip file to extract
     * @param extractDirName - The name of the directory to extract to
     */
    void unZipFile(String fileName, String extractDirName)
    {
        File destDirectory = new File(extractDirName)
        byte[] buffer = new byte[1024]

        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileName))
        ZipEntry zipEntry = zis.getNextEntry()

        while (zipEntry != null)
        {
            File newFile = newFileFromZip(destDirectory, zipEntry)

            if (zipEntry.isDirectory())
            {
                if (!newFile.isDirectory() && !newFile.mkdirs())
                {
                    throw new IOException("Failed to create directory " + newFile)
                }
            }

            else
            {
                File parentFile = newFile.getParentFile()
                // For windows users
                if (!parentFile.isDirectory() && !parentFile.mkdirs())
                {
                    throw new IOException("Failed to create directory " + parentFile)
                }

                FileOutputStream fos = new FileOutputStream(newFile)

                int length
                while ((length = zis.read(buffer)) > 0)
                {
                    fos.write(buffer, 0, length)
                }

                fos.close()
            }
            zipEntry = zis.getNextEntry()
        }
    }

    /**
     * newFileFromZip - Creates new files from the zip file's contents
     *
     * @param destDirectory - The destination of to make the file to
     * @param zipEntry - Contains the byte contents of the current file in the zip file
     * @return Returns a new file that was found in the zip file
     *
     * @throws IOException Throws an exception if the file was created outside of the target directory
     */
    File newFileFromZip(File destDirectory, ZipEntry zipEntry) throws IOException
    {
        File destFile = new File(destDirectory, zipEntry.getName())

        String dirPath = destDirectory.getCanonicalPath()
        String filePath  = destFile.getCanonicalPath()

        if (!filePath.startsWith(dirPath + File.separator))
        {
            throw new IOException("Entry is outside of the target directory: "
                    + zipEntry.getName())
        }

        return destFile
    }

    /**
     * isZip - Checks if a file is a zip file
     *
     * @param file - The file to check
     * @return Returns true if a file is a zip file
     */
    boolean isZip(File file)
    {
        return file.isFile() && file.getName().toLowerCase().endsWith(".zip")
    }

    /**
     * isZip - Checks if a file path is a zip file
     *
     * @param file - The file to check
     * @return Returns true if a file is a zip file
     */
    boolean isZip(String filePath)
    {
        return filePath.toLowerCase().endsWith(".zip")
    }
}
