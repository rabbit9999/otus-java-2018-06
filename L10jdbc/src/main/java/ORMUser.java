import myORM.DataSet;

public class ORMUser extends DataSet {
    public String name = "";
    public int age = 0;

    public String toString(){
        return String.format("id: %s, name: %s, age: %s", id, name, age);
    }

}