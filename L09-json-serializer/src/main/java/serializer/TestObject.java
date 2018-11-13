package serializer;

import java.io.Serializable;
import java.util.*;

public class TestObject implements Serializable {
    Double doubleField = 0.65;
    String stringField = "0050";
    List<String> listStringField = new ArrayList<>(Arrays.asList("001", "002", "003", "004", "005"));
    int intField = 50;
    List<Integer> listIntField = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    Integer[] arrayIntField = {10, 20, 30, 40, 50};
    List<List<Object>> listOfMixedLists = new ArrayList<>();
    Map<String,String> stringSubObject = new HashMap<>();





    TestObject(){

        Map<String,Object> subArrayObject = new HashMap<>();

        subArrayObject.put("integerValue",123);
        subArrayObject.put("stringValue","this is string");

        listOfMixedLists.add(new ArrayList<>(Arrays.asList("001", 2.5, "003", 4, "005")));
        listOfMixedLists.add(new ArrayList<>(Arrays.asList("006", 7, "008", 9, "010")));
        listOfMixedLists.add(new ArrayList<>(Arrays.asList("011", 12, "013", 14, subArrayObject)));

        stringSubObject.put("key_1","val_1");
        stringSubObject.put("key_2","val_2");
        stringSubObject.put("key_3","val_3");
    }
}
