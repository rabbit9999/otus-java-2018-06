package l05;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Stand{

    List<Integer> lst = new ArrayList<Integer>();

    public void start(){
        int counter = 0;
        while(true){
            Random generator = new Random();

            lst.add(generator.nextInt(10000));

            if(counter < 30){
                lst.remove(lst.size() - 1);
            }
            else{
                counter = 0;
            }
            counter++;
        }
    }

}
