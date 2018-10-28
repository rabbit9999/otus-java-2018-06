package atm.banknotes;

public abstract class Banknote {
    private int nominal = 0;

    Banknote(int nominal){
        this.nominal = nominal;
    }

    public int getNominal(){
        return this.nominal;
    }


}
