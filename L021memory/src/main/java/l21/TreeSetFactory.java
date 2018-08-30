package l21;

import java.util.Set;
import java.util.TreeSet;

public class TreeSetFactory implements GeneralObjectFactory{

    private int limit;

    TreeSetFactory(int limit){
        this.limit = limit;
    }
    public Set<Integer> get(int ... args){
        Set<Integer> res = new TreeSet<Integer>();

        for(int i = 0; i<this.limit; i++){
            res.add(i);
        }
        return res;
    }
}
