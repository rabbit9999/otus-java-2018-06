package l21;

import java.lang.reflect.Constructor;

public class TypeFactory<T> implements GeneralObjectFactory {
    private Class cl;
    private T defVal;

    TypeFactory(Class cl, T def){
        this.cl = cl;
        this.defVal = def;
    }
    public T get(int ... args){
        T val;
        try {
            Constructor c = this.cl.getConstructor(String.class);
            val = (T)c.newInstance(this.defVal.toString());
        }
        catch (Exception e){
//            e.printStackTrace();
            val = null;
        }

//        System.out.println(val);
        return val;
    }


}
