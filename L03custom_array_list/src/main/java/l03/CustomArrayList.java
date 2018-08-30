package l03;


import java.util.*;

public class CustomArrayList<T> extends AbstractList<T> implements List<T>, RandomAccess {
    private static final int MAX_ARRAY_SIZE = 2147483639;
    private Object[] storage;
    private int size = 0;

    CustomArrayList(int storageSize){
        if(storageSize < 0){
            storageSize = 0;
        }
        this.storage = new Object[storageSize];
    }

    CustomArrayList(){
        this.storage = new Object[0];
    }

    public boolean add(T val){
        if(storage.length == this.size){
            if(!this.grow()){
                return false;
            }
        }

        this.size++;
        this.storage[this.size - 1] = val;

        return true;
    }

    public T get(int index){
        Objects.checkIndex(index, this.size);
        return (T)this.storage[index];
    }

    public int indexOf(Object obj) {
        int i;
        if (obj == null) {
            for(i = 0; i < this.size; ++i) {
                if (this.storage[i] == null) {
                    return i;
                }
            }
        }
        else {
            for(i = 0; i < this.size; ++i) {
                if (obj.equals(this.storage[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    public T set(int index, T value){
        Objects.checkIndex(index, this.size);
        Object oldValue = this.storage[index];
        this.storage[index] = value;
        return (T)oldValue;
    }

    public T remove(int index) {
        Objects.checkIndex(index, this.size);
        Object oldValue = this.storage[index];

        int newSize;
        if ((newSize = this.size - 1) > index) {
            System.arraycopy(this.storage, index + 1, this.storage, index, newSize - index);
        }
        this.storage[this.size = newSize] = null;
        return (T)oldValue;
    }

    public void clear() {
        int to = this.size;
        for(int i = this.size = 0; i < to; ++i) {
            this.storage[i] = null;
        }
    }

    public int size(){
        return this.size;
    }

    public boolean isEmpty(){
        return this.size == 0;
    }

    public Object[] toArray(){
        return Arrays.copyOf(this.storage, this.size);
    }

    private boolean grow(){
        int storageSize = this.storage.length;
        if(storageSize == MAX_ARRAY_SIZE){
            return false;
        }
        if(storageSize < 10){
            storageSize ++;
        }
        else if(storageSize < 100){
            storageSize += 10;
        }
        else if(storageSize < 1000){
            storageSize += 100;
        }
        else if(storageSize < 10000){
            storageSize += 1000;
        }
        else{
            storageSize += 10000;
        }

        if(storageSize > MAX_ARRAY_SIZE){
            storageSize = MAX_ARRAY_SIZE;
        }

        this.storage = Arrays.copyOf(this.storage,storageSize);
        return true;
    }
}
