package l21;

import java.util.HashMap;
import java.util.Map;

public class HashMapFactory implements GeneralObjectFactory{

    private int limit;

    HashMapFactory(int limit){
        this.limit = limit;
    }
    public Map<Integer, Integer> get(int ... args){
        Map<Integer,Integer> res = new HashMap<Integer, Integer>();

        for(int i = 0; i<this.limit; i++){
            res.put(i,i);
        }
        return res;
    }
}
