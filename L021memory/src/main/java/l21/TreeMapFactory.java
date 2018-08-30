package l21;

import java.util.Map;
import java.util.TreeMap;

public class TreeMapFactory implements GeneralObjectFactory{

    private int limit;

    TreeMapFactory(int limit){
        this.limit = limit;
    }
    public Map<Integer, Integer> get(int ... args){
        Map<Integer,Integer> res = new TreeMap<Integer, Integer>();

        for(int i = 0; i<this.limit; i++){
            res.put(i,i);
        }
        return res;
    }
}
