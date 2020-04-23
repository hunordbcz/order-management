package util;

public class Constants {

    /**
     * Defines the prefix of the tables from the DB
     */
    private static final String TABLE_PREFIX = "om_";

    /**
     * Defines the DB name
     */
    private static final String DB_NAME = "order_management";

    /**
     * Username from the DB
     */
    private static final String DB_USER = "root";

    /**
     * Password from the DB
     */
    private static final String DB_PASS = "secret";

    /**
     * Additional settings for the DB connection
     */
    private static final String DB_CONNECTION_SETTINGS = "?useSSL=false&serverTimezone=UTC";

    /**
     * The amount of details that should be printed in the order report
     */
    private static final int ORDER_REPORT_SIZE = 5;

    /**
     * The name of the input file
     */
    private static String INPUT_FILE = "commands.txt";

    private Constants() {

    }

    public static String getInputFile() {
        return INPUT_FILE;
    }

    public static void setInputFile(String inputFile) {
        INPUT_FILE = inputFile;
    }

    public static String getTablePrefix() {
        return TABLE_PREFIX;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static String getDbUser() {
        return DB_USER;
    }

    public static String getDbPass() {
        return DB_PASS;
    }

    public static String getDbConnectionSettings() {
        return DB_CONNECTION_SETTINGS;
    }

    public static int getOrderReportSize() {
        return ORDER_REPORT_SIZE;
    }
}
