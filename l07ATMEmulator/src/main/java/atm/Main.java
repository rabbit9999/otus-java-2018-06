package atm;

import atm.banknotes.Banknote;
import atm.banknotes.BanknotesFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class Main {
    public static void main(String ... args){
        ATM atm = new ATM();
        BanknotesFactory wallet = new BanknotesFactory();

        List<Banknote> bundle = new ArrayList<>();

        bundle.addAll(wallet.get(50,12));
        bundle.addAll(wallet.get(100,10));
        bundle.addAll(wallet.get(200,8));
        bundle.addAll(wallet.get(500,6));
        bundle.addAll(wallet.get(1000,4));
        bundle.addAll(wallet.get(2000,2));
        bundle.addAll(wallet.get(5000,1));
        atm.add(bundle);


        System.out.println("ATM balance: " + atm.getBalance());

        int sum = 4850;
        System.out.println("Getting sum: "+sum);
        printBundle(atm.get(sum));
    }

    public static void printBundle(List<Banknote> bList){
        TreeMap<Integer,Integer> m = new TreeMap<>(Collections.reverseOrder());
        for(Banknote b: bList){
            m.computeIfAbsent(b.getNominal(),k->0);
            m.put(b.getNominal(),m.get(b.getNominal())+1);
        }
        for(Integer nominal: m.keySet()){
            System.out.println(nominal + ": " + m.get(nominal));
        }
    }
}
