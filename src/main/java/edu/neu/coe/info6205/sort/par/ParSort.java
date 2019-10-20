package edu.neu.coe.info6205.sort.par;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
class ParSort {

    public static int cutoff = 1000;

    //    public static void sort(int[] array, int from, int to, Executor executor) {
    public static void sort(int[] array, int from, int to) {

//            System.out.println("1: " + ForkJoinPool.commonPool().getRunningThreadCount());

        if (to - from < cutoff) Arrays.sort(array, from, to);
        else if(ForkJoinPool.commonPool().getRunningThreadCount() >= 3){
//                System.out.println("2: " + ForkJoinPool.commonPool().getRunningThreadCount());
            Arrays.sort(array, from, to);
        }
        else {
//                System.out.println("3: " + ForkJoinPool.commonPool().getRunningThreadCount());
            CompletableFuture<int[]> parsort1 = parsort(array, from, from + (to - from) / 2); // TO IMPLEMENT
            CompletableFuture<int[]> parsort2 = parsort(array, from + (to - from) / 2, to); // TO IMPLEMENT
            CompletableFuture<int[]> parsort = parsort1.thenCombine(parsort2, (xs1, xs2) -> {
                int[] result = new int[xs1.length + xs2.length];
                // TO BE IMPLEMENTED ...
                int i = 0;
                int j = 0;
                int k = 0;
                while(i<xs1.length || j<xs2.length){
                    if(i == xs1.length){
                        for(;j<xs2.length;j++){
                            result[k++] = xs2[j];
                        }
                    } else if(j == xs2.length) {
                        for (; i < xs1.length; i++) {
                            result[k++] = xs1[i];
                        }
                    } else if(xs1[i] <= xs2[j]) {
                        result[k++]=xs1[i++];
                    } else if(xs1[i] > xs2[j]){
                        result[k++] = xs2[j++];
                    }
                }
//                System.out.println("check");
                // ... END IMPLEMENTATION
                return result;
            });

            parsort.whenComplete((result, throwable) -> System.arraycopy(result, 0, array, from, result.length));
//            System.out.println("# threads: "+ ForkJoinPool.commonPool().getRunningThreadCount());
            parsort.join();
        }
    }

    private static CompletableFuture<int[]> parsort(int[] array, int from, int to) {
//        Executor executor = Executors.newFixedThreadPool(40);
        return CompletableFuture.supplyAsync(
                () -> {
                    int[] result = new int[to - from];
                    // TO BE IMPLEMENTED ...
//                    if(to <= from) return;
                    int mid = from + ((to - from) / 2);
                    sort(array, from, to);
//                    sort(array, mid+1, to);
                    result = Arrays.copyOfRange(array,from,to);
                    // ... END IMPLEMENTATION
                    return result;
                }
//                , executor
        );
    }
}