package dao;

import bll.InvoiceBLL;
import connection.ConnectionFactory;
import model.Invoice;
import model.Order;
import model.Pair;
import util.Constants;
import util.OrderTypes;
import util.StatementTypes;

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

import static util.OrderTypes.ASC;
import static util.OrderTypes.DESC;


public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final List<String> fieldNames;
    private final Class<T> type;
    protected Object previous;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.fieldNames = getDeclaredFields();
        this.previous = null;
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

    private String createDeleteQuery(List<String> reference) {
        String sb = "DELETE FROM " +
                Constants.getTablePrefix() + type.getSimpleName() +
                this.getWHERE(reference);
        return sb;
    }

    private String createDescSelectQuery(List<String> reference) {
        return this.createSelectQuery(reference) + " ORDER BY id DESC";
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

    List<T> select(List<Pair<String, Object>> rules, OrderTypes order) {
        List<String> fields = new LinkedList<>();
        List<Object> values = new LinkedList<>();

        if (rules != null) {
            for (Pair<String, Object> rule : rules) {
                fields.add(rule.first);
                values.add(rule.second);
            }
        }

        String query = null;

        if (order == DESC) {
            query = createDescSelectQuery(fields);
        } else {
            query = createSelectQuery(fields);
        }

        return sendQuery(query, values);
    }

    private List<Object> getFieldValues(T t, StatementTypes sqlType) throws IllegalAccessException {
        List<Object> fieldValues = new LinkedList<>();
        for (Field field : type.getDeclaredFields()) {
            if (field.getName().startsWith("x_")) {
                continue;
            }

            if (sqlType == StatementTypes.INSERT && field.getName().equals("id")) {
                continue;
            }

            PropertyDescriptor propertyDescriptor = null;
            try {
                propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                Method method = propertyDescriptor.getReadMethod();
                Object value = method.invoke(t);
                fieldValues.add(value);
            } catch (IntrospectionException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return fieldValues;
    }

    public Boolean insert(T t) {
        List<Object> values = null;
        try {
            values = getFieldValues(t, StatementTypes.INSERT);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String query = createInsertQuery(t);
        return sendUpdate(query, values) != 0;
    }

    public Boolean update(T t) {
        List<Object> referenceValues = null;
        try {
            referenceValues = getFieldValues(t, StatementTypes.UPDATE);
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

    public Boolean delete(List<Pair<String, Object>> rules) {
        List<String> fields = new LinkedList<>();
        List<Object> values = new LinkedList<>();

        if (rules == null) {
            return false;
            //todo throw exception no reference given
        }

        for (Pair<String, Object> rule : rules) {
            fields.add(rule.first);
            values.add(rule.second);
        }

        String query = createDeleteQuery(fields);

        return sendUpdate(query, values) != 0;
    }

    public T findLast() {
        return this.select(null, DESC).get(0);
    }

    public List<T> findAll() {
        return this.select(null, ASC);
    }

    public T findById(int id) {
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair<>("id", id));

        List<T> response = select(rules, ASC);
        if (response != null && !response.isEmpty()) {
            return response.get(0);
        }
        return null;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Method method = null;
        try {
            while (resultSet.next()) {
                T instance = type.newInstance();
                for (String fieldName : fieldNames) {

                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);


                    if (fieldName.equals("_order") && previous != null) {
                        value = previous;
                    }
                    method = type.getMethod(propertyDescriptor.getWriteMethod().getName(), value.getClass());

                    method.invoke(instance, value);

                }

                if (instance instanceof Order) {
                    InvoiceBLL inv = new InvoiceBLL();
                    if (previous != null) {
                        ((Order) instance).addX_Invoices((Invoice) previous);
                        previous = null;
                    }
                    ((Order) instance).addX_Invoices(inv.findByOrder((Order) instance));
                }

                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println(method.getName());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println(method.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return list;
    }
}