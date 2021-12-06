import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Hash {
    // Constants
    private static String h0 = "";
    private static String h1 = "";
    private static String h2 = "";
    private static String h3 = "";
    private static String h4 = "";
    private static String h5 = "";
    private static String h6 = "";
    private static String h7 = "";
    // Stores each 512 bit chunk
    private static String[][] chunks;
    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("Missing arguments\nUse \"t\" to start in text mode and \"f\" to start in file mode");
            System.exit(1);
        }
        // Initialize all constants
        initializeConst();
        if (args[0].equals("t")) {
            textMode();
        } else if (args[0].equals("f")) {
            fileMode();
        } else {
            System.out.println("Invalid argument, run with no arguments for help");
            System.exit(1);
        }
    }
    static void initializeConst() {
        Primes.initialize();
        String temp = "";
        temp = Long.toBinaryString(Long.valueOf((""+Math.cbrt(2)).split("\\.")[1]));
        h0 = temp.substring(temp.length()-32);
        temp = Long.toBinaryString(Long.valueOf((""+Math.cbrt(3)).split("\\.")[1]));
        h1 = temp.substring(temp.length()-32);
        temp = Long.toBinaryString(Long.valueOf((""+Math.cbrt(5)).split("\\.")[1]));
        h2 = temp.substring(temp.length()-32);
        temp = Long.toBinaryString(Long.valueOf((""+Math.cbrt(7)).split("\\.")[1]));
        h3 = temp.substring(temp.length()-32);
        temp = Long.toBinaryString(Long.valueOf((""+Math.cbrt(11)).split("\\.")[1]));
        h4 = temp.substring(temp.length()-32);
        temp = Long.toBinaryString(Long.valueOf((""+Math.cbrt(13)).split("\\.")[1]));
        h5 = temp.substring(temp.length()-32);
        temp = Long.toBinaryString(Long.valueOf((""+Math.cbrt(17)).split("\\.")[1]));
        h6 = temp.substring(temp.length()-32);
        temp = Long.toBinaryString(Long.valueOf((""+Math.cbrt(19)).split("\\.")[1]));
        h7 = temp.substring(temp.length()-32);
    }
    static String subArrayLink(String[] array, int start, int to) {
        String out = "";
        for (int i = start; i < to; i++) {
            out += array[i];
        }
        return out;
    }
    static String binToHex(String bin) {
        int nibbles = bin.length()/4;
        long val = Long.valueOf(bin, 2);
        String temp = Long.toString(val, 16);
        String out = "";
        for (int i = temp.length(); i < nibbles; i++) {
            out += "0";
        }
        out += temp;
        return out;
    }
    static void textMode() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your text: ");
        String entered = input.nextLine();
        // Initialize all chunks needed
        chunks = new String[(int) ((double) entered.length() / 64.0 + 1)][64];
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < 64; j++) {
                // Padding
                if (i*64+j >= entered.length()) {
                    if (i*64+j == entered.length()) {
                        chunks[i][j] = "10000000";
                    } else {
                        chunks[i][j] = "00000000";
                    }
                } else {
                    // Entering bytes of characters
                    String temp = Integer.toBinaryString((int) entered.charAt(i*64+j));
                    String temp2 = "";
                    if (temp.length() < 8) {
                        for (int k = 0; k < 8-temp.length(); k++) {
                            temp2 += "0";
                        }
                    }
                    temp2 += temp;
                    chunks[i][j] = temp2;
                }
            }
        }
        // Last 64 bits of last chunk is big endian integer representing length of input in bits
        String tmp = Integer.toBinaryString(entered.length()*8);
        String bigEndian = "";
        if (tmp.length() < 64) {
            for (int i = 0; i < 64-tmp.length(); i++) {
                bigEndian += "0";
            }
        }
        bigEndian += tmp;
        int counter = chunks[chunks.length-1].length-8;
        for (int i = 0; i < 8; i++) {
            chunks[chunks.length-1][counter] = bigEndian.substring(i*8, i*8+8);
            counter++;
        }
        // Start chunk loop
        chunkLoop();
        input.close();
    }
    static void fileMode() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter file name: ");
        String name = input.nextLine();
        byte[] array = {};
        try {
            array = Files.readAllBytes(Paths.get(name));
        } catch (Exception e) {
            System.out.println("File not found");
            System.exit(1);
        }
        chunks = new String[(int) ((double) array.length / 64.0 + 1)][64];
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < 64; j++) {
                if (i*64+j >= array.length) {
                    if (i*64+j == array.length) {
                        chunks[i][j] = "10000000";
                    } else {
                        chunks[i][j] = "00000000";
                    }
                } else {
                    String temp = Integer.toBinaryString(array[i*64+j]);
                    String temp2 = "";
                    if (temp.length() < 8) {
                        for (int k = 0; k < 8-temp.length(); k++) {
                            temp2 += "0";
                        }
                    }
                    temp2 += temp;
                    chunks[i][j] = temp2;
                }
            }
        }
        String tmp = Integer.toBinaryString(array.length * 8);
        String bigEndian = "";
        if (tmp.length() < 64) {
            for (int i = 0; i < 64-tmp.length(); i++) {
                bigEndian += "0";
            }
        }
        bigEndian += tmp;
        int counter = chunks[chunks.length-1].length-8;
        for (int i = 0; i < 8; i++) {
            chunks[chunks.length-1][counter] = bigEndian.substring(i*8, i*8+8);
            counter++;
        }
        chunkLoop();
        input.close();
    }
    static void chunkLoop() {
        for (int i = 0; i < chunks.length; i++) {
            // Mutate current chunk
            for (int j = 0; j < 64; j++) {
                for (int k = 0; k < 64; k++) {
                    if (k != j) {
                        chunks[i][k] = Bitwise.xor(Bitwise.xor(chunks[i][j], Bitwise.not(chunks[i][k])), Bitwise.rRotate(chunks[i][j], k));
                    }
                }
            }
            for (int j = 0; j < 64; j++) {
                chunks[i][j] = Bitwise.xor(Bitwise.rRotate(Bitwise.rShift(chunks[i][j], 2), j), Bitwise.not(chunks[i][(j*2)%64]));
            }
            for (int j = 16; j < 64; j++) {
                chunks[i][j] = Bitwise.add(Bitwise.xor(chunks[i][j-16], Bitwise.rShift(chunks[i][j-7], 3)), Bitwise.and(chunks[i][j-3], Bitwise.not(chunks[i][j-13])));
            }
            // End Mutation
            // Create round constants
            String[] roundConst = new String[64];
            for (int j = 0; j < 64; j++) {
                roundConst[j] = Primes.get(j);
            }
            // End Round Constant Initialization
            // Create Message Schedule
            String[] msgSched = new String[64];
            for (int j = 0; j < 64; j++) {
                msgSched[j] = "00000000000000000000000000000000";
            }
            for (int j = 0; j < 16; j++) {
                msgSched[j] = subArrayLink(chunks[i], j*4, j*4+4);
            }
            // End
            // Extend first 16 words into remaining 
            for (int j = 16; j < 64; j++) {
                String s0 = Bitwise.xor(Bitwise.xor(Bitwise.rRotate(msgSched[j-15], 7), Bitwise.rRotate(msgSched[j-15], 18)), Bitwise.rShift(msgSched[j-15], 3));
                String s1 = Bitwise.xor(Bitwise.xor(Bitwise.rRotate(msgSched[j-2], 17), Bitwise.rRotate(msgSched[j-2], 19)), Bitwise.rShift(msgSched[j-2], 10));
                msgSched[j] = Bitwise.add(Bitwise.add(Bitwise.add(msgSched[j-16], s0), msgSched[j-7]), s1);
            }
            // Mutate message schedule
            for (int j = 0; j < 64; j++) {
                String tp = Bitwise.xor(Bitwise.rRotate(msgSched[j], 3), Bitwise.xor(Bitwise.rRotate(msgSched[j], 7), Bitwise.rShift(Bitwise.rRotate(msgSched[j], 13), 4)));
                String tp2 = Bitwise.xor(Bitwise.and(Bitwise.xor(msgSched[j], tp), Bitwise.not(msgSched[(j+37)%64])), Bitwise.not(msgSched[(j+13)%64]));
                msgSched[j] = Bitwise.add(Bitwise.add(tp, tp2), Bitwise.rRotate(Bitwise.not(Bitwise.xor(msgSched[j], msgSched[Math.abs(j-63)])), 7));
            }
            // End
            // Compression
            String a = h0;
            String b = h1;
            String c = h2;
            String d = h3; 
            String e = h4;
            String f = h5;
            String g = h6;
            String h = h7;
            for (int j = 0; j < 64; j++) {
                String S1 = Bitwise.xor(Bitwise.xor(Bitwise.rRotate(e, 6), Bitwise.rRotate(e, 11)), Bitwise.rRotate(e, 25));
                String ch = Bitwise.xor(Bitwise.and(e, f), Bitwise.and(Bitwise.not(e), g));
                String temp1 = Bitwise.add(Bitwise.add(Bitwise.add(Bitwise.add(h, S1), ch), roundConst[j]), msgSched[j]);
                String S0 = Bitwise.xor(Bitwise.xor(Bitwise.rRotate(a, 2), Bitwise.rRotate(a, 13)), Bitwise.rRotate(a, 22));
                String maj = Bitwise.xor(Bitwise.xor(Bitwise.and(a, b), Bitwise.and(a, c)), Bitwise.and(b, c)); 
                String temp2 = Bitwise.add(S0, maj);
                h = new String(g);
                g = new String(f);
                f = new String(e);
                e = Bitwise.add(d, temp1);
                d = new String(c);
                c = new String(b);
                b = new String(a);
                a = Bitwise.add(temp1, temp2);
            }
            h0 = Bitwise.rRotate(Bitwise.add(h0, a), Long.valueOf(h0, 2));
            h1 = Bitwise.rRotate(Bitwise.add(h1, b), Long.valueOf(h1, 2));
            h2 = Bitwise.rRotate(Bitwise.add(h2, c), Long.valueOf(h2, 2));
            h3 = Bitwise.rRotate(Bitwise.add(h3, d), Long.valueOf(h3, 2));
            h4 = Bitwise.rRotate(Bitwise.add(h4, e), Long.valueOf(h4, 2));
            h5 = Bitwise.rRotate(Bitwise.add(h5, f), Long.valueOf(h5, 2));
            h6 = Bitwise.rRotate(Bitwise.add(h6, g), Long.valueOf(h6, 2));
            h7 = Bitwise.rRotate(Bitwise.add(h7, h), Long.valueOf(h7, 2));
        }
        // End
        System.out.println(binToHex(h0) + binToHex(h1) + binToHex(h2) + binToHex(h3) + binToHex(h4) + binToHex(h5) + binToHex(h6) + binToHex(h7));
    }
}