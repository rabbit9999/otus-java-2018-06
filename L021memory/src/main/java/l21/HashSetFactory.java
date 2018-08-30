package l21;

import java.util.HashSet;
import java.util.Set;

public class HashSetFactory implements GeneralObjectFactory{

    private int limit;

    HashSetFactory(int limit){
        this.limit = limit;
    }
    public Set<Integer> get(int ... args){
        Set<Integer> res = new HashSet<Integer>();

        for(int i = 0; i<this.limit; i++){
            res.add(i);
        }
        return res;
    }
}
