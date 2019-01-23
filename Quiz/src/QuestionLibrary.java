import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
public class QuestionLibrary {
 
    private List<Question> _questions = null;
 
    /**
     * Fragenliste zurückgeben (einmalige Datenbankverbindung)
     * @param filePath 
     * @return Fragenliste
     */
    public List<Question> getQuestions(String filePath) {
        if (_questions == null)
            GetQuestionsFromDatabase(filePath);
        return _questions;
    }
 
   /**
    * Datenbankverbindung - Füllt die Fragenliste
    * Vorgang wie von Microsoft beschrieben
    * @param filePath
    */
    private void GetQuestionsFromDatabase(String filePath) {
        // variables
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
         
        _questions = new ArrayList<Question>();
 
        // Step 1: Loading or registering Oracle JDBC driver class
        try {
 
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        } catch (ClassNotFoundException cnfex) {
 
            System.out.println("Problem in loading or registering MS Access JDBC driver");
            cnfex.printStackTrace();
        }
        // Step 2: Opening database connection
        try {
            String msAccDB = filePath;
            String dbURL = "jdbc:ucanaccess://" + msAccDB; //Zugriffstreiber + Dateipfad
 
            // Step 2.A: Create and get connection using DriverManager class
            connection = DriverManager.getConnection(dbURL);
 
            // Step 2.B: Creating JDBC Statement
            statement = connection.createStatement();
 
            // Step 2.C: Executing SQL & retrieve data into ResultSet
            resultSet = statement.executeQuery("SELECT * FROM questions");//alles aus Tabelle questions laden
 
            // processing returned data and put into List
            while (resultSet.next()) {
                Question question = new Question();
                question.id = resultSet.getInt(1);
                question.question = resultSet.getString(2);
                question.type = QuestionType.valueOf(resultSet.getString(3));// valueOf prüft ob String in Enum vorkommt
                question.answer1 = resultSet.getString(4);
                question.answer2 = resultSet.getString(5);
                question.answer3 = resultSet.getString(6);
                question.answer4 = resultSet.getString(7);
                question.joker = resultSet.getString(8);
                question.correctAnswer = resultSet.getString(9);
                _questions.add(question);
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } finally {
            // Step 3: Closing database connection
            try {
                if (connection != null) {
 
                    // cleanup resources, once after processing
                    resultSet.close();
                    statement.close();
 
                    // and then finally close connection
                    connection.close();
                }
            } catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }
    }
}