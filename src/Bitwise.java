class Bitwise {
    // Bitwise XOR -> "1" bit if current position bits aren't the same ("1" and "0")
    public static String xor(String bin1, String bin2) {
        if (bin1.length() != bin2.length()) {
            System.out.println("Error in xor");
            System.exit(1);
        }
        String out = "";
        for (int i = 0; i < bin1.length(); i++) {
            if (bin1.charAt(i) != bin2.charAt(i)) {
                out += "1";
            } else {
                out += "0";
            }
        }
        return out;
    }
    // Bitwise NOT -> Flip all bits ("1" => "0" or "0" => "1")
    public static String not(String bin) {
        String out = "";
        for (int i = 0; i < bin.length(); i++) {
            out += bin.charAt(i) == '0' ? "1" : "0";
        }
        return out;
    }
    // Bitwise AND -> "1" bit only if both bits in current position are "1"
    public static String and(String bin1, String bin2) {
        if (bin1.length() != bin2.length()) {
            System.out.println("Error in and");
            System.exit(1);
        }
        String out = "";
        for (int i = 0; i < bin1.length(); i++) {
            if (bin1.charAt(i) == '1' && bin1.charAt(i) == bin2.charAt(i)) {
                out += "1";
            } else {
                out += "0";
            }
        }
        return out;
    }
    // Bitwise Right Shift -> Shift bits to the right k bits and add zeroes to the front until it is equal length to input
    public static String rShift(String bin, int k) {
        String out = "";
        for (int i = 0; i < bin.length() - (bin.length() - k); i++) {
            out += "0";
        }
        out += bin.substring(0, bin.length() - k);
        return out;
    }
    // Bitwise Right Rotate -> Circular rotation of bits k bit positions
    public static String rRotate(String bin, long k) {
        String[] out = new String[bin.length()];
        for (int i = 0; i < bin.length(); i++) {
            out[(int) ((i+k) % bin.length())] = "" + bin.charAt(i);
        }
        return String.join("", out);
    }
    // Bitwise addition -> Modular arithmetic on sum of 2 sequences by the sequences' maximum value
    public static String add(String bin1, String bin2) {
        if (bin1.length() != bin2.length()) {
            System.out.println("Error in add");
            System.exit(1);
        }
        String out = "";
        long result = (Long.valueOf(bin1, 2) + Long.valueOf(bin2, 2)) % ((long) Math.pow(2, bin1.length()));
        String temp = Long.toBinaryString(result);
        if (temp.length() < bin1.length()) {
            for (int i = 0; i < bin1.length() - temp.length(); i++) {
                out += "0";
            }
        }
        out += temp;
        return out;
    }
}
