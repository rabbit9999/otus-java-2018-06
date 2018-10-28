package atmdp;

import atm.ATM;
import atm.ATMException;
import atm.banknotes.Banknote;
import atm.banknotes.BanknotesFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ATMDepartment {

    private List<ATM> atmList = new ArrayList<>();
    private Map<ATM,Map<Integer,Integer>> stateStorage = new HashMap<>();

    public void addATM(ATM atm){
        atmList.add(atm);
        stateStorage.put(atm, atm.getState());
    }

    public ATM getATM(int index){
        return atmList.get(index);
    }

    public int getBalance(){
        int sum = 0;
        for (ATM atm:atmList){
            sum += atm.getBalance();
        }
        return sum;
    }

    public void restore(){
        for(ATM atm:atmList){
            this.restoreAtm(atm);
        }
    }

    private void restoreAtm(ATM atm){
        Map<Integer,Integer> currentState = atm.getState();
        Map<Integer,Integer> savedState = stateStorage.get(atm);
        BanknotesFactory wallet = new BanknotesFactory();
        List<Banknote> bundle = new ArrayList<>();
        try {

            for(Integer nominal:savedState.keySet()){
                int difference = savedState.get(nominal) - currentState.get(nominal);
                if(difference>0){
                    bundle.addAll(wallet.get(nominal,difference));
                }
                else{
                    difference*= -1;
                    atm.withdrow(nominal,difference);
                }
            }
            atm.add(bundle);
        }
        catch (ATMException e){
            System.out.println(e.getMessage());
        }
    }
}
