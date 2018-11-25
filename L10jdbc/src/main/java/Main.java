import myORM.DataSet;
import myORM.MyORMImpl;

public class Main {
    public static void main(String ... args){

        MyORMImpl orm = new MyORMImpl("my_orm_test","my_orm_test","123456");

        ORMUser user1 = new ORMUser();
        user1.name = "User 1";
        user1.age = 20;

        ORMUser user2 = new ORMUser();
        user2.name = "User 2";
        user2.age = 30;

        ORMProduct product1 = new ORMProduct();
        product1.name = "Product 1";
        product1.price = 500;
        product1.description = "Product 1 description";

        ORMProduct product2 = new ORMProduct();
        product2.name = "Product 2";
        product2.price = 500;
        product2.description = "Product 2 description";

        Long u1 = orm.add(user1);
        Long u2 = orm.add(user2);
        Long p1 = orm.add(product1);
        Long p2 = orm.add(product2);

        System.out.println("------");
        for(DataSet el : orm.getDataList(ORMUser.class)){
            System.out.println(el);
        }
        for(DataSet el : orm.getDataList(ORMProduct.class)){
            System.out.println(el);
        }

        orm.delete(ORMUser.class,u1);
        ORMProduct productToChange = (ORMProduct) orm.get(ORMProduct.class,p2);
        productToChange.name = "Changed Product Name";
        orm.update(productToChange);


        System.out.println("------ Getting full data -------");
        for(DataSet el : orm.getDataList(ORMUser.class)){
            System.out.println(el);
        }
        for(DataSet el : orm.getDataList(ORMProduct.class)){
            System.out.println(el);
        }

        System.out.println("------ Searching by --------");
        try {
            for(DataSet el : orm.searchBy(ORMUser.class,ORMUser.class.getField("name"),"User 2")){
                System.out.println(el);
            }
        }
        catch (NoSuchFieldException e){
            e.printStackTrace();
        }

        System.out.println("------ Get count --------");
        System.out.println(orm.getCount(ORMUser.class));
    }
}
