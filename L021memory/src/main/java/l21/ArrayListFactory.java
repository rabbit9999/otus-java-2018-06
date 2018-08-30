package l21;

import java.util.ArrayList;
import java.util.List;

public class ArrayListFactory implements GeneralObjectFactory{

    private int limit;

    ArrayListFactory(int limit){
        this.limit = limit;
    }
    public List<Integer> get(int ... args){
        List<Integer> res = new ArrayList<Integer>();

        for(int i = 0; i<this.limit; i++){
            res.add(i);
        }
        return res;
    }
}