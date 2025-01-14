/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.sort.simple.Helper;
import edu.neu.coe.info6205.sort.simple.InsertionSort;
import edu.neu.coe.info6205.sort.simple.Sort;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * This class implements a simple Benchmark utility for measuring the running time of algorithms.
 * It is part of the repository for the INFO6205 class, taught by Prof. Robin Hillyard
 *
 * It requires Java 8 as it uses function types, in particular, UnaryOperator&lt;T&gt; (a function of T => T),
 * Consumer&lt;T&gt; (essentially a function of T => Void) and Supplier&lt;T&gt; (essentially a function of Void => T).
 *
 * In general, the benchmark class handles three phases of a "run:"
 * <ol>
 *     <li>The pre-function which prepares the input to the study function (field fPre) (may be null);</li>
 *     <li>The study function itself (field fRun) -- assumed to be a mutating function since it does not return a result;</li>
 *     <li>The post-function which cleans up and/or checks the results of the study function (field fPost) (may be null).</li>
 * </ol>
 *
 * Note that the clock does not run during invocations of the pre-function and the post-function (if any).
 *
 * @param <T> The generic type T is that of the input to the study function fRun, which you will pass in to the constructor.
 */
public class Benchmark<T> {

    /**
     * Constructor for a Benchmark with option of specifying all three functions.
     *
     * CONSIDER making fRun an ordinary Function&lt;T, R&gt;
     * CONSIDER making fPost a Predicate&lt;R&gt;
     *
     * @param fPre a function of T => T.
     *          Function fPre is run before each invocation of fRun (but with the clock stopped).
     *             The result of fPre (if any) is passed to fRun.
     * @param fRun a Consumer function (i.e. a function of T => Void).
     *          Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     *          When you create a lambda defining fRun, you must return "null."
     * @param fPost a Consumer function (i.e. a function of T => Void).
     *          Function fPost is run after each invocation of fRun (but with the clock stopped).
     */
    public Benchmark(UnaryOperator<T> fPre, Consumer<T> fRun, Consumer<T> fPost) {
        this.fPre = fPre;
        this.fRun = fRun;
        this.fPost = fPost;
    }

    /**
     * Constructor for a Benchmark with option of specifying all three functions.
     *
     * @param fPre a function of T => T.
     *             Function fPre is run before each invocation of fRun (but with the clock stopped).
     *             The result of fPre (if any) is passed to fRun.
     * @param fRun a Consumer function (i.e. a function of T => Void).
     *             Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     *             When you create a lambda defining fRun, you must return "null."
     */
    public Benchmark(UnaryOperator<T> fPre, Consumer<T> fRun) {
        this(fPre, fRun, null);
    }

    /**
     * Constructor for a Benchmark with only fRun and fPost Consumer parameters.
     *
     * @param fRun a Consumer function (i.e. a function of T => Void).
     *             Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     *             When you create a lambda defining fRun, you must return "null."
     * @param fPost a Consumer function (i.e. a function of T => Void).
     *          Function fPost is run after each invocation of fRun (but with the clock stopped).
     */
    public Benchmark(Consumer<T> fRun, Consumer<T> fPost) {
        this(null, fRun, fPost);
    }

    /**
     * Constructor for a Benchmark where only the (timed) run function is specified.
     *
     * @param f a Consumer function (i.e. a function of T => Void).
     *          Function f is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     *          When you create a lambda defining f, you must return "null."
     */
    public Benchmark(Consumer<T> f) {
        this(null, f, null);
    }

    /**
     * Run function f m times and return the average time in milliseconds.
     *
     * @param t the value that will in turn be passed to function f.
     * @param m the number of times the function f will be called.
     * @return the average number of milliseconds taken for each run of function f.
     */
    public double run(T t, int m) {
        return run(() -> t, m);
    }

    /**
     * The first parameter to the first run method signature is the parameter that will, in turn,
     * be passed to target function.
     *
     * In the second signature, supplier will be invoked each time to get
     * a t which is passed to the other run method.
     *
     * The second parameter to the run function (m) is the number of times the target function will be called.
     *
     * The return value from run is the average number of milliseconds taken for each run of the target function.
     *
     * Run function f m times and return the average time in milliseconds.
     *
     * @param supplier a Supplier of a T
     * @param m the number of times the function f will be called.
     * @return the average number of milliseconds taken for each run of function f.
     */
    public double run(Supplier<T> supplier, int m) {
        // Warmup phase
        int warmupRuns = Integer.max(2, Integer.min(10, m / 10));
        for (int i = 0; i < warmupRuns; i++) {
            doRun(supplier.get(), true);
        }
        // Timed phase
        long totalTime = 0;
        for (int i = 0; i < m; i++) {
            totalTime += doRun(supplier.get(), false);
        }
        return (double) totalTime / m / 1000000;
    }

    /**
     * Actually run the benchmark function(s).
     * @param t the input
     * @param warmup if true then do not measure any time, but return 0. Otherwise, time
     * @return the number of nanoseconds elapsed for this run
     */
    private long doRun(T t, boolean warmup) {
        // TO BE IMPLEMENTED: if fPre isn't null, then invoke it (using "apply") and memoize its result as "t1". Otherwise, assign "t" to "t1."
        T t1 = fPre != null ? fPre.apply(t) : t ;

        // TO BE IMPLEMENTED: if warmup is true, simply invoke fRun with t1 (using "accept") and return 0.
        if (warmup){
            fRun.accept(t1);
            return 0;
        }
        // TO BE IMPLEMENTED: start the timer, invoke fRun on t1 (using "accept"), stop the timer,
        long startTime = System.nanoTime();
        fRun.accept(t1);
        long stopTime = System.nanoTime() - startTime;

        // ... invoke fPost (if not-null) on t1 (using "accept").
        if(fPost != null){
            fPost.accept(t1);
        }
        // TO BE IMPLEMENTED: return the number of nanoseconds elapsed.
        return stopTime;
    }

    private final UnaryOperator<T> fPre;
    private final Consumer<T> fRun;
    private final Consumer<T> fPost;

    /**
     * Everything below this point has to do with a particular example of running a Benchmark
     * for various sort methods.
     * In this case, we time two types of simple sort on a random integer array of length 1000.
     * Each test is run 100 times.
     *
     * NOTE: In order to run this main program, InsertionSort and selectionSort must be fully implemented.
     *
     * @param args the command-line arguments, of which none are significant.
     */
    public static void main(String[] args) {
        Random random = new Random();
        int m = 100; // This is the number of repetitions: sufficient to give a good mean value of timing
        int n = 250; // This is the size of the array
        for (int k = 0; k < 5; k++) {
            Integer[] array = new Integer[n];
            // initialize a random array
            for (int i = 0; i < n; i++) {
                array[i] = random.nextInt();
            }
            // initialize a ordered array
//            for (int i = 0; i < n; i++) array[i] = i;
//            // initialize a part random array
//            for (int i=0; i<n/2; i++) array[i] = random.nextInt();
//            for (int i=n/2; i<n; i++) array[i] = i;
            // initialize a reversed-ordered array
//            for (int i=0; i<n; i++) array[i] = n-i;



            benchmarkSort(array, "InsertionSort: " + n, new InsertionSort<>(), m);
//            benchmarkSort(array, "SelectionSort: " + n, new SelectionSort<>(), m);
            n = n * 2;
        }
    }

    private static void benchmarkSort(Integer[] array, String name, Sort<Integer> sorter, int m) {
        UnaryOperator<Integer[]> preFunction = (xs) -> Arrays.copyOf(array, array.length);
        Consumer<Integer[]> sortFunction = (xs) -> sorter.sort(xs, false);
        final Helper<Integer> helper = sorter.getHelper();
        Consumer<Integer[]> cleanupFunction = (xs) -> {
            if (!helper.sorted(xs)) throw new RuntimeException("not sorted");
        };
        Benchmark<Integer[]> bm = new Benchmark<>(preFunction, sortFunction, cleanupFunction);
        double x = bm.run(array, m);
        System.out.println(name + ": " + x + " millisecs");
//        System.out.println(x);
    }
}
