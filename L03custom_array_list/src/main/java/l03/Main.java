package l03;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class Main {
    public static void main(String... args){

        //List with type
        CustomArrayList<Integer> list = new CustomArrayList<>();

        //List_1 without type
        CustomArrayList list_1 = new CustomArrayList();

        //Initiate list with Collections.addAll
        Collections.addAll(list,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);

        //Initiate list_1 with 0
        for(int i=0; i<100000; i++){
            list_1.add(0);
        }

        //Copy values from list to list_1 with Collections.copy
        Collections.copy(list_1,list);

        //Search index of value with Collections.binarySearch
        int result = Collections.binarySearch(list,13);
        System.out.println("Found index is: " + result);

        //Add string value to list_1 and check with Collections.checkedList
        list_1.add("qwe");
        List chkList = Collections.checkedList(list_1, String.class);
        System.out.println("Checked list content: "+chkList);

        //Min and max values
        System.out.println("Min value: " + Collections.min(list));
        System.out.println("Max value: " + Collections.max(list));

        //Using iterator with custom array list
        ListIterator iter= list.listIterator();
        while (iter.hasNext()){
            System.out.println("Got with iterator: " + iter.next());
        }

        //Reverse elements with Collections.reverse
        System.out.println(list);
        Collections.reverse(list);
        System.out.println(list);

        //Remove element
        list.remove(3);
        System.out.println(list);

        //Clear
        list_1.clear();
        System.out.println(list_1);

        //Index of 10
        System.out.println("Index of 10: " + list.indexOf(10));

//        System.out.println(list_1.size());
//        System.out.println(list_1.get(0));
//        System.out.println(list_1.get(1));
//        System.out.println(list_1.get(2));
//        System.out.println(list_1.get(3));
//        System.out.println(list_1.get(4));

    }
}
