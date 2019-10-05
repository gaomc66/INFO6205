package edu.neu.coe.info6205.union_find;

import java.util.*;

public class UFClient {


    private static int count(int n){

        UF_HWQUPC uf = new UF_HWQUPC(n,false);
        Random randomInt = new Random();

        int count = 0;
        do {
            int p = randomInt.nextInt(n);
            int q = randomInt.nextInt(n);

            if (!uf.connected(p,q)) {
                uf.union(p, q);
            }
            count ++;
        }while(uf.components() > 1);

        return count;
    }



    public static void main (String args[]) {

        Scanner scan = new Scanner(System.in);
//        System.out.println("Enter the Sites Number:");
//        int n = scan.nextInt();
        System.out.println("Enter the Times of Increase N:");
        int increase = scan.nextInt();
        System.out.println("Enter the Times of Each N to Calculate Average:");
        int times = scan.nextInt();
        scan.close();

        int average;

        double amount = 100*Math.pow(2,increase);

        for(int n=100;  n< amount; n=n*2) {

            average = 0;

            for (int j = 0; j < times; j++) {
                average = average + count(n);
            }

            System.out.println("sites: "+ n + " ;  Average: " + average/times );
            System.out.println("1/2NlnN: " + 1.0/2.0 * (double)n * Math.log((double)n));
            System.out.println();

        }
//        System.out.println("Average count: " + average/times);

    }





}
