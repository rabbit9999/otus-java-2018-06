package serializer;


public class Main {
    public static void main(String ... args){
        TestObject obj = new TestObject();
        ZSON zson = new ZSON();

        String jsonString = zson.stringify(obj);

        System.out.println(jsonString);

//        zson.parse(jsonString);

    }
}
