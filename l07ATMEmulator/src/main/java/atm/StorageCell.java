package atm;

import atm.banknotes.Banknote;

import java.util.ArrayList;
import java.util.List;


public class StorageCell {
    private List<Banknote> storage = new ArrayList<>();
    private static final int CELL_LIMIT = 1000;

    public void put(Banknote banknote){
        if(storage.size() <= CELL_LIMIT){
            storage.add(banknote);
        }
        else{
            throw new Error("Cell storage is overflow!");
        }
    }

    public Banknote get(){
        if(storage.size() > 0){
            Banknote banknote = storage.get(storage.size() - 1);
            storage.remove(storage.size() - 1);
            return banknote;
        }
        else {
            throw new Error("Cell storage is empty!");
        }
    }

    public int getAmount(){
        return storage.size();
    }
}
