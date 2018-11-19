package myORM;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyORM {

    private Connector connector;

    public MyORM(String dbName, String userName, String userPassword){
        connector = new Connector(dbName,userName,userPassword);
    }

    public DataSet get(Class clazz, long id){
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

    public long add(DataSet dataSet){
        if(checkTable(dataSet)){
            String tableName = dataSet.getClass().getName();
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("INSERT INTO ");
            queryBuilder.append("`");
            queryBuilder.append(tableName);
            queryBuilder.append("`");

            Field[] fieldList = getFieldList(dataSet.getClass());
            buildSetClause(queryBuilder,fieldList);

            try(PreparedStatement stmt = connector.getConnection().prepareStatement(queryBuilder.toString(),Statement.RETURN_GENERATED_KEYS)) {

                bindParams(stmt,dataSet,fieldList);

                stmt.execute();
                try(ResultSet key = stmt.getGeneratedKeys()){
                    if(key.next()){
                        return key.getLong(1);
                    }
                }
            }
            catch (SQLException | IllegalAccessException e){
                e.printStackTrace();
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

    public boolean update(DataSet dataSet){
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

            try(PreparedStatement stmt = connector.getConnection().prepareStatement(queryBuilder.toString())) {

                bindParams(stmt,dataSet,fieldList);
                stmt.execute();
                return stmt.getUpdateCount() > 0;
            }
            catch (SQLException | IllegalAccessException e){
                e.printStackTrace();
                return false;
            }
        }
        else{
            return false;
        }
    }

    public boolean delete(Class clazz, long id){
        String tableName = clazz.getName();
        if(tableExists(tableName)){
            String query = "DELETE FROM `"+tableName+"` WHERE `id` = "+ id;
            try(Statement stmt = connector.getConnection().createStatement()){
                stmt.executeUpdate(query);
                return stmt.getUpdateCount() > 0;
            }
            catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }
        else{
            return false;
        }
    }

    public List<DataSet> getDataList(Class clazz){
        String tableName = clazz.getName();
        String query = "SELECT * FROM `"+tableName+"`";
        return selectRequest(clazz,query);
    }

    private List<DataSet> selectRequest(Class clazz, String query){
        String tableName = clazz.getName();
        if(tableExists(tableName)){

            Field[] fieldList = getFieldList(clazz);
            List<DataSet> resultList = new ArrayList<>();

            try(ResultSet res = connector.getConnection().createStatement().executeQuery(query)){
                while (res.next()){
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
                    resultList.add(element);
                }
                return resultList;
            }
            catch (SQLException |
                    NoSuchMethodException |
                    InstantiationException |
                    IllegalAccessException |
                    InvocationTargetException e){
                e.printStackTrace();
                return new ArrayList<>();
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

    private boolean checkTable(DataSet o){
        Class clazz = o.getClass();
        String tableName = clazz.getName();
        if(!tableExists(tableName)){
            StringBuffer queryBuilder = new StringBuffer();
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

                try {
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
                }
                catch (IllegalAccessException e){
                    e.printStackTrace();
                    return false;
                }
                queryBuilder.append(", ");
            }
            queryBuilder.append("PRIMARY KEY ( id ))");

            try(Statement stmt = connector.getConnection().createStatement()) {
                stmt.executeUpdate(queryBuilder.toString());
            }
            catch (SQLException e){
                e.printStackTrace();
                return false;
            }

        }
        return true;
    }

    private boolean tableExists(String tableName){
        boolean tExists = false;
        try (ResultSet rs = connector.getConnection().getMetaData().getTables(null, null, tableName, null)) {
            while (rs.next()) {
                String tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals(tableName)) {
                    tExists = true;
                    break;
                }
            }
        }
        catch (SQLException e ){
            e.printStackTrace();
        }

        return tExists;
    }

}
