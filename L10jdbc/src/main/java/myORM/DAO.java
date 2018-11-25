package myORM;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DAO {

    private Connection connection;

    public DAO(Connection connection){
        this.connection = connection;
    }

    public long getCount(Class clazz) throws SQLException{
        String tableName = clazz.getName();
        if(tableExists(tableName)){
            String query = String.format("SELECT COUNT(*) AS `cnt` FROM `%s`", tableName);

            try (Statement stmt = connection.createStatement()) {
                stmt.execute(query);
                try (ResultSet res = stmt.getResultSet()) {
                    if(res.next()){
                        return res.getLong("cnt");
                    }
                }

            }
        }

        return 0L;
    }

    public List<DataSet> searchBy(Class clazz, Field targetField, Object searchParam) throws SQLException, IllegalAccessException{
        String tableName = clazz.getName();
        String fieldName = targetField.getName();
        if(tableExists(tableName)){
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT * FROM ");
            queryBuilder.append("`");
            queryBuilder.append(tableName);
            queryBuilder.append("`");
            queryBuilder.append(" WHERE ");
            queryBuilder.append("`");
            queryBuilder.append(fieldName);
            queryBuilder.append("`");
            queryBuilder.append(" = ?");

            PreparedStatement stmt = connection.prepareStatement(queryBuilder.toString());

            try {
                Object o = clazz.getConstructor().newInstance();
                Class fieldClass = targetField.get(o).getClass();
                if(fieldClass.equals(String.class)){
                    stmt.setString(1,(String)searchParam);
                }
                else if(fieldClass.equals(Integer.class)){
                    stmt.setInt(1,(Integer)searchParam);
                }
                else{
                    throw new RuntimeException("Unsupported field type: " + fieldClass.getName());
                }

                stmt.execute();
                try(ResultSet res = stmt.getResultSet()){
                    List<DataSet> results = new ArrayList<>();
                    while(res.next()){
                        results.add(resultToDataset(clazz, res));
                    }
                    return results;
                }
            }
            catch (NoSuchMethodException | InstantiationException |InvocationTargetException e){
                e.printStackTrace();
            }
        }
        return new ArrayList<>();

    }

    public DataSet get(Class clazz, long id) throws SQLException, IllegalAccessException{
        String tableName = clazz.getName();
        String query = "SELECT * FROM `"+tableName+"` WHERE `id` = " + id;
        List<DataSet> result = selectRequest(clazz,query);

        if(result.size() == 0){
            return null;
        }
        else{
            return result.get(0);
        }
    }

    public long add(DataSet dataSet) throws SQLException, IllegalAccessException{
        if(checkTable(dataSet)){
            String tableName = dataSet.getClass().getName();
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("INSERT INTO ");
            queryBuilder.append("`");
            queryBuilder.append(tableName);
            queryBuilder.append("`");

            Field[] fieldList = getFieldList(dataSet.getClass());
            buildSetClause(queryBuilder,fieldList);


            PreparedStatement stmt = connection.prepareStatement(queryBuilder.toString(), Statement.RETURN_GENERATED_KEYS);
            bindParams(stmt,dataSet,fieldList);

            stmt.execute();
            try(ResultSet key = stmt.getGeneratedKeys()){
                if(key.next()){
                    return key.getLong(1);
                }
            }
        }
        return 0L;
    }

    private StringBuilder buildSetClause(StringBuilder queryBuilder, Field[] fieldList){
        queryBuilder.append(" SET ");
        boolean isFirstField = true;
        for(Field f:fieldList){
            if(!isFirstField){
                queryBuilder.append(", ");
            }
            queryBuilder.append("`");
            queryBuilder.append(f.getName());
            queryBuilder.append("`");
            queryBuilder.append(" = ? ");
            isFirstField = false;
        }
        return queryBuilder;
    }

    private void bindParams (PreparedStatement stmt, DataSet dataSet, Field[] fieldList) throws SQLException, IllegalAccessException{
        for(int i = 0; i < fieldList.length; i++){
            Object val = fieldList[i].get(dataSet);
            if(val.getClass().equals(String.class)){
                stmt.setString(i+1,(String)val);
            }
            else if(val.getClass().equals(Integer.class)){
                stmt.setInt(i+1,(Integer)val);
            }
            else{
                throw new RuntimeException("Unsupported field type: "+val.getClass().getName());
            }
        }
    }

    public boolean update(DataSet dataSet) throws SQLException, IllegalAccessException{
        if(checkTable(dataSet)) {
            Long elementId = dataSet.id;
            String tableName = dataSet.getClass().getName();
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("UPDATE ");
            queryBuilder.append("`");
            queryBuilder.append(tableName);
            queryBuilder.append("`");

            Field[] fieldList = getFieldList(dataSet.getClass());
            buildSetClause(queryBuilder, fieldList);

            queryBuilder.append(" WHERE `id` = ");
            queryBuilder.append(elementId);

            PreparedStatement stmt = connection.prepareStatement(queryBuilder.toString());

                bindParams(stmt,dataSet,fieldList);
                stmt.execute();
                return stmt.getUpdateCount() > 0;

        }
        else{
            return false;
        }
    }

    public boolean delete(Class clazz, long id) throws SQLException{
        String tableName = clazz.getName();
        if(tableExists(tableName)){
            String query = "DELETE FROM `"+tableName+"` WHERE `id` = "+ id;
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            return stmt.getUpdateCount() > 0;

        }
        else{
            return false;
        }
    }

    public List<DataSet> getDataList(Class clazz) throws SQLException, IllegalAccessException{
        String tableName = clazz.getName();
        String query = "SELECT * FROM `"+tableName+"`";
        return selectRequest(clazz,query);
    }

    private DataSet resultToDataset(Class clazz, ResultSet res) throws SQLException, IllegalAccessException{
        Field[] fieldList = getFieldList(clazz);
        try {
            DataSet element = (DataSet)clazz.getConstructor().newInstance();
            element.id = res.getInt("id");
            for(Field f : fieldList){
                Object val = f.get(element);
                if(val.getClass().equals(String.class)){
                    f.set(element,res.getString(f.getName()));
                }
                else if(val.getClass().equals(Integer.class)){
                    f.set(element,res.getInt(f.getName()));
                }
                else {
                    throw new RuntimeException("Unsupported field type: "+val.getClass().getName());
                }
            }
            return element;
        }
        catch ( NoSuchMethodException |
                InstantiationException |
                InvocationTargetException e){
            e.printStackTrace();
            return null;
        }
    }

    private List<DataSet> selectRequest(Class clazz, String query) throws SQLException, IllegalAccessException{
        String tableName = clazz.getName();
        if(tableExists(tableName)){

            Field[] fieldList = getFieldList(clazz);
            List<DataSet> resultList = new ArrayList<>();

            try(ResultSet res = connection.createStatement().executeQuery(query)){
                while (res.next()){
                    resultList.add(resultToDataset(clazz, res));
                }
                return resultList;
            }
        }
        else{
            return new ArrayList<>();
        }
    }


    private Field[] getFieldList(Class clazz){
        try{
            return clazz.getDeclaredFields();
        }
        catch (Throwable e) {
            System.out.println("FAIL!");
            e.printStackTrace();
            return new Field[0];
        }
    }

    private boolean checkTable(DataSet o) throws SQLException, IllegalAccessException{
        Class clazz = o.getClass();
        String tableName = clazz.getName();
        if(!tableExists(tableName)){
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("CREATE TABLE ");
            queryBuilder.append("`");
            queryBuilder.append(tableName);
            queryBuilder.append("`");
            queryBuilder.append("(`id` INTEGER NOT NULL AUTO_INCREMENT, ");

            for(Field f:getFieldList(clazz)){

                String fieldName = f.getName();
                queryBuilder.append("`");
                queryBuilder.append(fieldName);
                queryBuilder.append("`");

                Class fieldClass = f.get(o).getClass();
                if(fieldClass.equals(Integer.class)){
                    queryBuilder.append(" INTEGER");
                }
                else if(fieldClass.equals(String.class)){
                    queryBuilder.append(" TEXT");
                }
                else{
                    throw new RuntimeException("Unsupported field type: " + fieldClass.getName());
                }

                queryBuilder.append(", ");
            }
            queryBuilder.append("PRIMARY KEY ( id ))");

            Statement stmt = connection.createStatement();
            stmt.executeUpdate(queryBuilder.toString());


        }
        return true;
    }

    private boolean tableExists(String tableName) throws SQLException{
        boolean tExists = false;
        try (ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null)) {
            while (rs.next()) {
                String tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals(tableName)) {
                    tExists = true;
                    break;
                }
            }
        }

        return tExists;
    }
}
