package l03;


import java.util.*;
import java.util.function.UnaryOperator;

public class CustomArrayList<T> implements List<T> {
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

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        boolean isFirstIteration = true;
        sb.append('[');
        for(int i = 0; i < this.size; i++){
            if(isFirstIteration){
                isFirstIteration = false;
            }
            else{
                sb.append(", ");
            }
            sb.append(this.storage[i]);
        }
        sb.append(']');

        return sb.toString();
    }

    @Override
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

    @Override
    public void add(int index, T val){
        throw new UnsupportedOperationException("add(int index, T val)");
    }

    @Override
    public T get(int index){
        Objects.checkIndex(index, this.size);
        return (T)this.storage[index];
    }

    @Override
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

    @Override
    public T set(int index, T value){
        Objects.checkIndex(index, this.size);
        Object oldValue = this.storage[index];
        this.storage[index] = value;
        return (T)oldValue;
    }

    @Override
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

    @Override
    public void clear() {
        int to = this.size;
        for(int i = this.size = 0; i < to; ++i) {
            this.storage[i] = null;
        }
    }

    @Override
    public int size(){
        return this.size;
    }

    @Override
    public boolean isEmpty(){
        return this.size == 0;
    }

    @Override
    public Object[] toArray(){
        return Arrays.copyOf(this.storage, this.size);
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        throw new UnsupportedOperationException("replaceAll(UnaryOperator<T> operator)");
    }

    @Override
    public void sort(Comparator<? super T> c) {
        throw new UnsupportedOperationException("sort(Comparator<? super T> c)");
    }

    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException("Spliterator<T> spliterator()");
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("contains(Object o)");
    }

    @Override
    public Iterator<T> iterator() {
        return this.listIterator();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        throw new UnsupportedOperationException("<T1> T1[] toArray(T1[] t1s)");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("remove(Object o)");
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        throw new UnsupportedOperationException("containsAll(Collection<?> collection)");
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        throw new UnsupportedOperationException("addAll(Collection<? extends T> collection)");
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        throw new UnsupportedOperationException("addAll(int i, Collection<? extends T> collection)");
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException("removeAll(Collection<?> collection)");
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException("retainAll(Collection<?> collection)");
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("lastIndexOf(Object o)");
    }

    @Override
    public ListIterator<T> listIterator() {
        return this.listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        return new CustomArrayList.ListItr(i);
    }

    @Override
    public List<T> subList(int i, int i1) {
        throw new UnsupportedOperationException("List<T> subList(int i, int i1)");
    }




    private int getNewStorageSize(){
        int storageSize = this.storage.length;
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
        return storageSize;
    }



    private boolean grow(){
        if(this.storage.length == MAX_ARRAY_SIZE){
            return false;
        }
        int storageSize = this.getNewStorageSize();
        this.storage = Arrays.copyOf(this.storage,storageSize);
        return true;
    }



    private class ListItr extends CustomArrayList<T>.Itr implements ListIterator<T> {
        ListItr(int index) {
            super();
            this.cursor = index;
        }

        public boolean hasPrevious() {
            return this.cursor != 0;
        }

        public T previous() {
            try {
                int i = this.cursor - 1;
                T previous = CustomArrayList.this.get(i);
                this.lastRet = this.cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException var3) {
                throw new NoSuchElementException();
            }
        }

        public int nextIndex() {
            return this.cursor;
        }

        public int previousIndex() {
            return this.cursor - 1;
        }

        public void set(T e) {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            } else {
                try {
                    CustomArrayList.this.set(this.lastRet, e);
                } catch (IndexOutOfBoundsException var3) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        public void add(T e) {
            try {
                int i = this.cursor;
                CustomArrayList.this.add(i, e);
                this.lastRet = -1;
                this.cursor = i + 1;
            } catch (IndexOutOfBoundsException var3) {
                throw new ConcurrentModificationException();
            }
        }
    }



    private class Itr implements Iterator<T> {
        int cursor;
        int lastRet;

        private Itr() {
            this.cursor = 0;
            this.lastRet = -1;
        }

        public boolean hasNext() {
            return this.cursor != CustomArrayList.this.size();
        }

        public T next() {
            try {
                int i = this.cursor;
                T next = CustomArrayList.this.get(i);
                this.lastRet = i;
                this.cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException var3) {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            } else {
                try {
                    CustomArrayList.this.remove(this.lastRet);
                    if (this.lastRet < this.cursor) {
                        --this.cursor;
                    }

                    this.lastRet = -1;
                } catch (IndexOutOfBoundsException var2) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }
}
