package edu.neu.coe.info6205.ownTest;

import java.util.Date;

public class TestSort {

    public static int[] shellshort(int[] a){
        int n = a.length;
        int h = 1;
        while(h<n/3) h = h*3 + 1;
        while(h>=1){
            for(int i=h; i<n; i++){
                for(int j=i; j>=h && a[j]<a[j-h]; j -= h){
                    swap(a,j,j-h);
                }
            }
            h = h/3;
        }
        return a;
    }

    public static int[] selectionSort(int[] a){
        int n = a.length;
        for(int i=0; i<n; i++){
            int min = i;
            for(int j=i; j<n; j++){
                if(a[j]<a[min]){
                    min = j;
                }
            }
            swap(a,i,min);
        }
        return a;
    }

    public static int[] insertionSort(int[] a){
        int n = a.length;
        for(int i=0; i<n; i++){
            for(int j=i; j>0; j--){
                if(a[j] < a[j-1]){
                    swap(a,j-1,j);
                }
            }
        }
        return a;
    }

    public static void merge(Comparable[] a, Comparable[] b, int lo, int mid, int hi){
        assert isSorted(a, lo, mid);
        int i = lo;
        for(int k = lo; k<hi ; k++){

        }
    }

    private static boolean isSorted(Comparable[] a, int lo, int mid) {
        return true;
    }


    public static void swap(int[] a, int i, int j){
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void printArray(int[] a){
        for(int i : a){
            System.out.println(i);
        }
    }

    public void testEqualsCompareTo(){
        Date date1 = new Date(2019);
        Date date2 = new Date(2019);
        System.out.println(date1.equals(date2));
        System.out.println(date1.compareTo(date2));
    }

    public static void main(String[] args) {
        int[] a = {10,9,8,7,6,5,4,3,2,1};
//        printArray(shellshort(a));
//        printArray(selectionSort(a));
        printArray(insertionSort(a));

    }
}
