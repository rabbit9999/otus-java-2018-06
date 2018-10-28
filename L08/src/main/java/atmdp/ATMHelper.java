package atmdp;

import atm.ATM;
import atm.ATMException;
import atm.banknotes.Banknote;
import atm.banknotes.BanknotesFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ATMHelper {

    private final int[] nominalList = {50,100,200,500,1000,2000,5000};

    public ATM getRandomATM(){
        ATM atm = new ATM();
        BanknotesFactory wallet = new BanknotesFactory();
        List<Banknote> bundle = new ArrayList<>();
        int iterations = ThreadLocalRandom.current().nextInt(1, 11);
        try {
            for(int i = 0; i<iterations; i++){
                int nominalId = ThreadLocalRandom.current().nextInt(0, nominalList.length);
                int amount = ThreadLocalRandom.current().nextInt(1, 200);
                bundle.addAll(wallet.get(nominalList[nominalId],amount));
            }
            atm.add(bundle);
        }
        catch (ATMException e){
            System.out.println(e.getMessage());
        }

        return atm;
    }

    public void spendRandomSum(ATM atm){
        int maxSum = atm.getBalance();
        int sum = ThreadLocalRandom.current().nextInt(1, maxSum/100) * 100;
        try {
            atm.get(sum);
        }
        catch (ATMException e){
//            e.printStackTrace();
        }
    }
}
