package atm;

import atm.banknotes.Banknote;

import java.util.*;

public class ATM {

    private ATMStorage storage = new ATMStorage();

    public void add(Banknote banknote){
        storage.put(banknote);
    }

    public void add(List<Banknote> bundle){
        for(Banknote b:bundle){
            add(b);
        }
    }


    public List<Banknote> get(int sum){
        if(sum > getBalance()){
            throw new Error("Not enough funds");
        }

        List<Banknote> bundle = new ArrayList<>();
        if(checkPossibility(sum)){
            for(Integer nominal: calculateNominalList(sum)){
                bundle.add(storage.get(nominal));
            }
        }
        else{
            throw new Error("Incorrect sum: it gotta be multiple of " + storage.getMinNominal());
        }
        return bundle;
    }

    private List<Integer> calculateNominalList(int sum){
        List<Integer> nominalList = new ArrayList<>();
        TreeMap<Integer,Integer> reversedAmount = new TreeMap<>(Collections.reverseOrder());
        reversedAmount.putAll(storage.getAmount());
        for(Integer nominal:reversedAmount.keySet()){
            int amount = reversedAmount.get(nominal);

            while(sum - nominal >= 0 && amount > 0){
                sum -= nominal;
                amount--;
                nominalList.add(nominal);
            }
        }
        if(sum != 0){
            throw new Error("Incorrect sum: ATM cannot give out this amount");
        }
        return nominalList;
    }

    public int getBalance(){
        TreeMap<Integer,Integer> amount = storage.getAmount();
        Integer balance = 0;
        for(Integer nominal:amount.keySet()){
            balance += nominal * amount.get(nominal);
        }
        return balance;
    }

    private boolean checkPossibility(int sum){
        boolean posibility = false;
        Map<Integer,Integer> amountList = storage.getAmount();
        for(Integer nominal:amountList.keySet()){
            if(sum % nominal == 0){
                posibility = true;
            }
        }

        return posibility;
    }
}
