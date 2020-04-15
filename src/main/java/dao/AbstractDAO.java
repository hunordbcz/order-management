package dao;

import connection.ConnectionFactory;
import model.Pair;
import util.Constants;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final List<String> fieldNames;
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.fieldNames = getDeclaredFields();
    }

    private List<String> getDeclaredFields() {
        List<String> fields = new LinkedList<>();
        for (Field field : type.getDeclaredFields()) {
            String name = field.getName();
            if (!name.startsWith("x_")) {
                fields.add(name);
            }
        }

        return fields;
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

    private String createSelectQuery(List<String> reference) {
        return "SELECT * FROM " + Constants.getTablePrefix() +
                type.getSimpleName() +
                this.getWHERE(reference);
    }

    private String createInsertQuery(T t) {
        StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO ");
        sb.append(Constants.getTablePrefix()).append(type.getSimpleName());
        sb.append(" (");

        sb.append(String.join(", ", this.fieldNames));

        sb.append(") VALUES (");

        for (int i = 0; i < this.fieldNames.size(); i++) {
            sb.append("?");
            if (i != this.fieldNames.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(")");

        return sb.toString();
    }

    private String createUpdateQuery(T obj, List<String> reference) {
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

    private Integer sendUpdate(String query, List<Object> values) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            if (values != null) {
                for (int i = 0; i < values.size(); i++) {
                    statement.setObject(i + 1, values.get(i));
                }
            }

            return statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:sendUpdate " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return 0;
    }

    private List<T> sendQuery(String query, List<Object> values) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            if (values != null) {
                for (int i = 0; i < values.size(); i++) {
                    statement.setObject(i + 1, values.get(i));
                }
            }

            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:sendQuery " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private List<T> select(List<Pair<String, Object>> rules) {
        List<String> fields = new LinkedList<>();
        List<Object> values = new LinkedList<>();

        if (rules != null) {
            for (Pair<String, Object> rule : rules) {
                fields.add(rule.first);
                values.add(rule.second);
            }
        }

        String query = createSelectQuery(fields);

        return sendQuery(query, values);
    }

    private List<Object> getFieldValues(T t) throws IllegalAccessException {
        List<Object> fieldValues = new LinkedList<>();
        for (Field field : type.getDeclaredFields()) {
            if (field.getName().startsWith("x_")) {
                continue;
            }
            field.setAccessible(true);
            Object value = field.get(t);
            fieldValues.add(value);
        }
        return fieldValues;
    }

    public Boolean insert(T t) {
        List<Object> values = null;
        try {
            values = getFieldValues(t);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String query = createInsertQuery(t);
        return sendUpdate(query, values) != 0;
    }

    public Boolean update(T t) {
        List<Object> referenceValues = null;
        try {
            referenceValues = getFieldValues(t);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        List<String> referenceNames = new LinkedList<>();

        referenceNames.add(fieldNames.get(1));


        assert referenceValues != null;
        referenceValues.add(referenceValues.get(1));


        String query = createUpdateQuery(t, referenceNames);
        return sendUpdate(query, referenceValues) != 0;
    }

    public List<T> findAll() {
        return this.select(null);
    }

    public T findById(int id) {
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair("id", id));
        return select(rules).get(0);
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();

        try {
            while (resultSet.next()) {
                T instance = type.newInstance();
                for (String fieldName : fieldNames) {
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException | InvocationTargetException | SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
