import java.util.*;
class Primes {
    private static String[] primes = new String[64];
    // Use Sieve of Eratosthenes to get the first 64 prime numbers' square roots' first 32 bits (least signifcant)
    public static void initialize() {
        int[] a = new int[310];
        ArrayList<Integer> prim = new ArrayList<Integer>();
        for (int i = 0; i < a.length; i++) {
            a[i] = i+2;
        }
        int counter = 0;
        while (true) {
            int c = a[counter];
            for (int i = counter+c; i < a.length; i+=c) {
                a[i] = 0;
            }
            counter++;
            if (counter >= a.length) {
                break;
            }
            while (counter < a.length && a[counter] == 0) {
                counter++;
            }
            if (counter >= a.length) {
                break;
            }
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != 0) {
                prim.add(a[i]);
            }
        }
        
        for (int j = 0; j < primes.length; j++) {
            String temp = Long.toBinaryString(Long.valueOf(("" + Math.sqrt(prim.get(j))).split("\\.")[1]));
            String temp2 = "";
            if (temp.length() < 32) {
                for (int k = 0; k < 32 - temp.length(); k++) {
                    temp2 += "0";
                }
            }
            temp2 += temp;
            primes[j] = temp2.substring(temp2.length()-32);
        }
    } 
    // Return value at postion
    public static String get(int k) {
        return primes[k];
    }
}
