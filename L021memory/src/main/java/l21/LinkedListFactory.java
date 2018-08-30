package l21;

import java.util.LinkedList;
import java.util.List;

public class LinkedListFactory implements GeneralObjectFactory{

    private int limit;

    LinkedListFactory(int limit){
        this.limit = limit;
    }
    public List<Integer> get(int ... args){
        List<Integer> res = new LinkedList<Integer>();

        for(int i = 0; i<this.limit; i++){
            res.add(i);
        }
        return res;
    }
}
