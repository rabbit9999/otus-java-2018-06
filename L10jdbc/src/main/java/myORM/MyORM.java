package myORM;

import java.lang.reflect.Field;
import java.util.List;

public interface MyORM {
    DataSet get(Class clazz, long id);

    long add(DataSet dataSet);

    boolean update(DataSet dataSet);

    boolean delete(Class clazz, long id);

    List<DataSet> getDataList(Class clazz);

    List<DataSet> searchBy(Class clazz, Field targetField, Object searchParam);

    long getCount(Class clazz);
}
