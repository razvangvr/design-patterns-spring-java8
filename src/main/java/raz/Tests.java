package raz;

/**
 * Created by razvan on 21/03/2019.
 */
public class Tests {


    public static void main(String[] args) {
        System.out.println(1);
        new RuntimeException("ZZ").printStackTrace();
        System.out.println(2);
    }

    static boolean isTrue(){
        boolean result = false;
        if(true)
            throw new  IllegalStateException("");
        return result;
    }
}
