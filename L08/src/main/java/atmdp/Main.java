package atmdp;

public class Main {

    public static final int ATMNumber = 5;

    public static void main(String ... args){
        ATMHelper atmHelper = new ATMHelper();
        ATMDepartment atmdp = new ATMDepartment();

        for(int i = 0; i < ATMNumber; i++){
            atmdp.addATM(atmHelper.getRandomATM());
        }

        for(int i = 0; i < ATMNumber; i++){
            System.out.println("ATM " + i + ": " + atmdp.getATM(i).getBalance());
        }
        System.out.println("-------------------------------------------");
        System.out.println("DEPARTMENT BALANCE: " + atmdp.getBalance());
        System.out.println("-------------------------------------------");

        for(int i = 0; i < ATMNumber; i++){
            atmHelper.spendRandomSum(atmdp.getATM(i));
            System.out.println("ATM " + i + ": " + atmdp.getATM(i).getBalance());
        }

        System.out.println("-------------------------------------------");
        System.out.println("DEPARTMENT BALANCE: " + atmdp.getBalance());
        System.out.println("-------------------------------------------");

        atmdp.restore();

        for(int i = 0; i < ATMNumber; i++){
            System.out.println("ATM " + i + ": " + atmdp.getATM(i).getBalance());
        }
        System.out.println("-------------------------------------------");
        System.out.println("DEPARTMENT BALANCE: " + atmdp.getBalance());
        System.out.println("-------------------------------------------");
    }
}
