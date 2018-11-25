package myORM;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyORMImpl implements MyORM {

    private Connector connector;

    public MyORMImpl(String dbName, String userName, String userPassword){
        connector = new Connector(dbName,userName,userPassword);
    }

    public DataSet get(Class clazz, long id){
        try(Connection conn = connector.getConnection()){
            return new DAO(conn).get(clazz, id);
        }
        catch (SQLException | IllegalAccessException e){
            e.printStackTrace();
            return null;
        }
    }

    public long add(DataSet dataSet){
        try(Connection conn = connector.getConnection()){
            return new DAO(conn).add(dataSet);
        }
        catch (SQLException | IllegalAccessException e){
            e.printStackTrace();
            return 0L;
        }
    }

    public boolean update(DataSet dataSet){
        try(Connection conn = connector.getConnection()){
            return new DAO(conn).update(dataSet);
        }
        catch (SQLException | IllegalAccessException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Class clazz, long id){
        try(Connection conn = connector.getConnection()){
            return new DAO(conn).delete(clazz, id);
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public List<DataSet> getDataList(Class clazz){
        try(Connection conn = connector.getConnection()){
            return new DAO(conn).getDataList(clazz);
        }
        catch (SQLException | IllegalAccessException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<DataSet> searchBy(Class clazz, Field targetField, Object searchParam){
        try(Connection conn = connector.getConnection()){
            return new DAO(conn).searchBy(clazz,targetField,searchParam);
        }
        catch (SQLException | IllegalAccessException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public long getCount(Class clazz){
        try(Connection conn = connector.getConnection()){
            return new DAO(conn).getCount(clazz);
        }
        catch (SQLException e){
            e.printStackTrace();
            return 0L;
        }
    }
}
