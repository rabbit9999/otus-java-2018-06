import myORM.DataSet;

public class ORMProduct extends DataSet {
    public String name = "";
    public int price = 0;
    public String description = "";

    public String toString(){
        return String.format("id: %s, name: %s, price: %s", id, name, price);
    }
}
