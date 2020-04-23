package util;

import java.util.List;

public class SQL<T> {
    private final Class<T> type;
    private final List<String> fieldNames;

    public SQL(Class<T> type, List<String> fieldNames) {
        this.type = type;
        this.fieldNames = fieldNames;
    }


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

    public String createSelectQuery(List<String> reference) {
        return "SELECT * FROM " + Constants.getTablePrefix() +
                type.getSimpleName() +
                this.getWHERE(reference);
    }

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

    public String createDeleteQuery(List<String> reference) {
        String sb = "DELETE FROM " +
                Constants.getTablePrefix() + type.getSimpleName() +
                this.getWHERE(reference);
        return sb;
    }

    public String createDescSelectQuery(List<String> reference) {
        return this.createSelectQuery(reference) + " ORDER BY id DESC";
    }
}
