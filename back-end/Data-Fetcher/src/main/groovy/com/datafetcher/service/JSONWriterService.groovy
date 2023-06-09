package com.datafetcher.service

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import io.micronaut.core.annotation.Introspected

/**
 * A class to write JSON to database.
 */
@Introspected
class JSONWriterService {

    JsonSlurper reader = new JsonSlurper();
    JsonOutput writer = new JsonOutput();

    /** Name of the database file */
    File f = new File("./db.json");

    /**
     * Write to current database.
     * @param json string representation of current database.
     */
    void writeToDB(Object json){
        f.write(beautify(json));
    }

    /**
     * Read and parse local database file then return it as an ArrayList.
     * NOTE: After being parsed, the form cannot call its methods because it's not an object anymore.
     * NOTE: However, we can still use the dot notation. Example: form.id, form.fileName
     * @return ArrayList of Form
     */
    ArrayList<Form> updateDB() {
        return reader.parse(f) as List;
    }

    /**
     * Return pretty JSON given a POJO.
     * @param list any POJO
     * @return JSON representation of given POJO.
     */
     String beautify(Object list) {
        return writer.prettyPrint(writer.toJson(list));
    }

}
