package music.utils;

/**
 * This class contains methods that really should exist in Java's Math class.
 * 
 */
public class ExtraMath {

    public static int gcd(int a, int b) {
        if (b > a) {
            int temp = b;
            b = a;
            a = temp;
        }
        if (b == 0) {
            if (a == 0) {
                a = 1;
            }
            return a;
        }
        return gcd(b, a % b);
    }

    public static int lcm(int a, int b) {
        return (a / gcd(a, b)) * b;
    }

}
