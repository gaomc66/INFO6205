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

//        Scanner scan = new Scanner(System.in);
//        System.out.println("Enter the Number of N:   ");
////        int n = scan.nextInt();
////        System.out.println("Enter the Times of Calculation:");
////        int times = scan.nextInt();
//        scan.close();

        int times = 1000;
        int average = 0;

        for(int n=100; n< 409601; n=n*2) {

            for (int i = 0; i < times; i++) {
                average = average + count(n);
            }
            System.out.println(n + " ; " + average/times +" ; " + );

        }
//        System.out.println("Average count: " + average/times);

    }





}
