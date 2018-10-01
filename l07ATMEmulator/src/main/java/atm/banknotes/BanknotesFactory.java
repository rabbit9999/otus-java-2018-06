package atm.banknotes;

import java.util.ArrayList;
import java.util.List;

public class BanknotesFactory {
    public Banknote get(int nominal){
        Class<?> cl;
        try {
            cl = Class.forName(Banknote.class.getName() + nominal);
        }
        catch (ClassNotFoundException e){
            throw new Error("Incorrect nominal");
        }

        Banknote banknote;
        try {
            banknote = (Banknote) cl.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e){
            e.printStackTrace();
            throw new Error("Something goes wrong...");
        }

        return banknote;
    }

    public List<Banknote>get(int nominal, int amount){
        if(amount < 0){
            amount = 0;
        }
        List<Banknote>bundle = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            bundle.add(get(nominal));
        }
        return bundle;
    }
}
