package com.datafetcher.service

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import jakarta.inject.Singleton

@Singleton
class GoogleDriveService {

    private static final String CREDENTIALS_FILE_PATH = "./src/main/resources/credentials.json";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance()
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);


    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream inputStream = new FileInputStream(CREDENTIALS_FILE_PATH)
        if (inputStream == null) {
            return
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream))

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }


    /**
     * uploadFileGoogle - Function that uploads given file with given credentials
     *
     * @param filename - The name of the file in Google drive
     * @param csvToUpload - the filename of the file being uploaded
     */
    static void uploadFileGoogle(String filename, String csvToUpload, String pathToFolder) {

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive driveClient = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("data-fetcher test")
                .build()


        if (!filename.contains(".csv")) {
            filename = filename + ".csv"
        }

        File fileMetadata = new File()
        fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet")
        java.io.File file = new java.io.File(csvToUpload);
        FileContent mediaContent = new FileContent("text/csv", file)
        fileMetadata.setName(filename)

        File data = new File();
        data.setMimeType("application/vnd.google-apps.folder");
        data.setName(pathToFolder)
        println(pathToFolder)

        File f = driveClient.files().create(data)
                .setFields("id")
                .execute();
        println("Folder ID: " + f.getId())
        String folderId = f.getId()
        fileMetadata.setParents(Collections.singletonList(folderId))

        try {
            File uploadFile = driveClient.files().create(fileMetadata, mediaContent)
                    .setFields("id, parents")
                    .execute()
            System.out.println("File ID: " + uploadFile.getId());
        } catch (IOException e) {
            print("An error occurred: " + e)
        }
        printf("Completed upload to Google Drive\n")
    }

}
