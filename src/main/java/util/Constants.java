package util;

public class Constants {

    private static final String TABLE_PREFIX = "om_";
    private static final String DB_NAME = "order_management";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "secret";
    private static final String DB_CONNECTION_SETTINGS = "?useSSL=false&serverTimezone=UTC";

    private Constants() {

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
}
