package test;

import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ReadWriteObjectStream {

    private File file; //The file to be read and written to.
    public String dbLocation = "jdbc:sqlite:" + file.getName(); //Setting driver string



    /**
     * Constructs a new ReadWriteObjectStream type and creates the
     * File type to be used in the object.
     *
     * @param folderLocation the proposed directory the file will be saved/read from.
     * @param fileName the proposed file name.
     */
    public ReadWriteObjectStream(File folderLocation, String fileName) {

        file = new File(folderLocation, fileName);
        createNewDatabase();
    }

    //Function to open and start a connection with the database
    private Connection connect(){
        Connection conn = null;
        try { //Attempt
            conn = DriverManager.getConnection(dbLocation); //Database connecting through SQLite library
        } catch (SQLException e) { //Catch error if there was a problem connecting to the database
            System.out.println("An Error occurred while connecting to the database file\n\t" + e.getMessage());
        }
        return conn; } //Return the database connection so that the statements can be executed

    //Function to execute SQL statements. Path of file and the SQL statement as strings are passed as arguments
    public boolean executeStatement(String sql){
        System.out.println("Starting execution of SQL statement");
        System.out.println("----\n" +sql + "\n----");
        try (Connection conn = connect(); //Create new connection
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql); //Execute SQL statement passed through the SQLite library
            System.out.println("Execution of SQL statement successful");
            return true; //Statement successfully executed
        } catch (SQLException e) { //Catch error if there was a problem when executing SQL statement
            System.out.println("An Error occurred while executing the SQL statement\n\t" + e.getMessage());
            return false; //Statement not executed successfully
        }
    }

    /**
     * Saves a given object to the file specified in the constructor.
     * Will always create a new file if one is not found or wipe an old file.
     * @param objectToSave the object to be saved as a stream to the file.
     * @return true if the file was saved successfully, false if otherwise.
     */
    public boolean saveDataFile(HashMap<String, Location> objectToSave) {
        try {
            System.out.print("saving...???");
            file.delete();
            FileOutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
            objectStream.writeObject(objectToSave);
            objectStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Retrieves the object from the file specified in the constructor.
     *
     * @return the object retrieved from the file, null if there was na error.
     */
    public HashMap<String, Location> retrieveDataFile() {
        HashMap<String, Location> retrievedData = new HashMap<>();
        if(checkFileExists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                ObjectInputStream objectStream = new ObjectInputStream(inputStream);
                retrievedData = (HashMap<String, Location>) objectStream.readObject();
            } catch (ClassNotFoundException | IOException e) {
            }
        }
        return retrievedData;
    }


    /**
     * Function to check if the database file already exists in the directory required.
     * @return returns true if the file exists, false if otherwise.
     */
    private boolean checkFileExists(){
        if (!file.exists()){
            return false;
        }
        return true;
    }


    /**
     * Function to create a new database file using SQLite driver with error catching to output all progress
     * @return true if a new database has been made, false if an error occurred.
     */
    private boolean createNewDatabase() {
        try (Connection conn = connect()) { //New connection (if a file does not exist) will create a new .db file
            if (conn != null) {
                executeStatement("CREATE TABLE 'locations' ( 'id' INTEGER NOT NULL, 'name' TEXT NOT NULL, 'x' INTEGER NOT NULL, 'y' INTEGER NOT NULL, 'z' INTEGER NOT NULL, PRIMARY KEY('id'))");
                return true;
            }
        } catch (SQLException e) { //As long as Library/Driver/Filepath correct - should not error
            return false;
        }
        return false;
    }

}
