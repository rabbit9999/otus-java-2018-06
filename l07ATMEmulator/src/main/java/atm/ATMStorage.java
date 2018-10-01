package atm;

import atm.banknotes.Banknote;

import java.util.TreeMap;

public class ATMStorage {
    private TreeMap<Integer, StorageCell> storage = new TreeMap<>();

    public void put(Banknote b){
        storage.computeIfAbsent(b.getNominal(), k-> new StorageCell()).put(b);
    }

    public Banknote get(int nominal){
        StorageCell cell = storage.get(nominal);
        if(cell == null){
            throw new Error("Storage cell not found");
        }
        return cell.get();
    }

    public int getMinNominal(){
        for(Integer nominal:storage.keySet()){
            if(storage.get(nominal).getAmount() > 0){
                return nominal;
            }
        }
        return 0;
    }

    public TreeMap<Integer,Integer> getAmount(){
        TreeMap<Integer,Integer> storageAmount = new TreeMap<>();
        for(Integer nominal:storage.keySet()){
            storageAmount.put(nominal,storage.get(nominal).getAmount());
        }
        return storageAmount;
    }

}
