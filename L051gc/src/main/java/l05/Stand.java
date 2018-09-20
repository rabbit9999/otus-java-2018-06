package l05;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Stand{

    List<Integer> lst = new ArrayList<Integer>();
    Thread tInc;
    Thread tDec;

    public void start(){
        Runnable incrementor = new Runnable(){
            public void run(){
                while(true){
                    Random generator = new Random();
                    try{
                        lst.add(generator.nextInt(10000));
                    }
                    catch (OutOfMemoryError e){
                        System.out.println(">>> OUT OF MEMORY!");
                        System.out.println(">>> NUMBER OF ADDED ELEMENTS: "+lst.size());
                        stop();
                    }
                }
            }
        };

        Runnable decrementor = new Runnable(){
            public void run(){
                while(true){
                    if(lst.size() > 0){
                        lst.remove(0);
                    }
                    int a = 1;
                    for(int i = 0; i < 2000000; i++){
                        a = a * i;
                    }
                }
            }
        };

        tInc = new Thread(incrementor);
        tDec = new Thread(decrementor);

        tInc.start();
        tDec.start();

    }

    public void stop(){
        System.out.println("Stopping...");
        System.exit(0);
    }

}
