package util;

import java.util.List;

/**
 * Used to make SQL Queries easily for any type of objects or any number of references
 *
 * @param <T> Defines the current Type
 */
public class SQL<T> {
    private final Class<T> type;
    private final List<String> fieldNames;

    public SQL(Class<T> type, List<String> fieldNames) {
        this.type = type;
        this.fieldNames = fieldNames;
    }

    /**
     * Create the WHERE part of an SQL query
     * @param fields The reference fields that should be added
     * @return The WHERE part as a String
     */
    private String getWHERE(List<String> fields) {
        if (fields == null || fields.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder(" WHERE ");
        fields.stream()
                .limit(fields.size() - 1)
                .forEach(field -> sb.append(field).append(" = ? AND"));
        sb.append(fields.get(fields.size() - 1)).append(" = ?");

        return sb.toString();
    }

    /**
     * Create a SELECT query
     * @param reference The reference fields that should be used in the filter ( WHERE )
     * @return The SELECT query as a String
     */
    public String createSelectQuery(List<String> reference) {
        return "SELECT * FROM " + Constants.getTablePrefix() +
                type.getSimpleName() +
                this.getWHERE(reference);
    }

    /**
     * Create an INSERT query
     * @param t The object that should be inserted
     * @return The INSERT query for the object as a String
     */
    public String createInsertQuery(T t) {
        StringBuilder sb = new StringBuilder();
        StringBuilder structure = new StringBuilder();
        StringBuilder values = new StringBuilder();

        sb.append("INSERT INTO ");
        sb.append(Constants.getTablePrefix()).append(type.getSimpleName());
        sb.append(" (");

        for (int i = 0; i < this.fieldNames.size(); i++) {
            if (this.fieldNames.get(i).equals("id")) {
                continue;
            }
            structure.append(this.fieldNames.get(i));
            values.append("?");
            if (i != this.fieldNames.size() - 1) {
                structure.append(", ");
                values.append(", ");
            }
        }

        sb.append(structure.toString());
        sb.append(") VALUES (");
        sb.append(values.toString());
        sb.append(")");

        return sb.toString();
    }

    /**
     * Create an UPDATE query
     *
     * @param obj       The object whose values should be updated
     * @param reference The reference fields that help find the object in the DB
     * @return The UPDATE query as a String
     */
    public String createUpdateQuery(T obj, List<String> reference) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(Constants.getTablePrefix()).append(type.getSimpleName() + " ");
        sb.append(" SET ");
        for (int i = 0; i < this.fieldNames.size(); i++) {
            sb.append(fieldNames.get(i)).append(" = ?");
            if (i != this.fieldNames.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(this.getWHERE(reference));

        return sb.toString();
    }

    /**
     * Create a DELETE query
     *
     * @param reference The reference fields that help find the objects in the DB
     * @return The DELETE query as a String
     */
    public String createDeleteQuery(List<String> reference) {
        String sb = "DELETE FROM " +
                Constants.getTablePrefix() + type.getSimpleName() +
                this.getWHERE(reference);
        return sb;
    }

    /**
     * Create a SELECT query with Descending Order by the creation
     *
     * @param reference The reference fields that should be used in the filter ( WHERE )
     * @return The SELECT query as a String
     */
    public String createDescSelectQuery(List<String> reference) {
        return this.createSelectQuery(reference) + " ORDER BY id DESC";
    }
}
