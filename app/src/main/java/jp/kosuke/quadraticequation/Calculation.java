package jp.kosuke.quadraticequation;

import java.util.ArrayList;

/**
 * Created by 孝輔 on 2016/03/30.
 */
public class Calculation {
    public static boolean isInt(double d) {
        double f = Math.floor(d);

        if (d - f == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPrime (int src) {
        for (int i = 2; i < src; i++) {
            if (src % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static ArrayList<Integer> PrimeFactorization (int src) {
        ArrayList<Integer> array = new ArrayList<Integer>();
        int temp = src;

        if (!isPrime(src)) {
            for (int i = 2; i < temp; i++) {
                for (; ;) {
                    if (src % i == 0) {
                        array.add(i);
                        src /= i;
                    } else {
                        break;
                    }
                }
            }
        } else if (src == 0){
            array.add(0);
        } else {
            array.add(src);
        }

        return array;
    }

    public static int GCD (int a, int b) {
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        } while (a != 0) {
            int temp = a;
            a = b % a;
            b = temp;
        }

        return b;
    }

    public static int LCM (int a, int b) {
        return (a * b) / GCD(a, b);
    }

    public static int easierRoot(ArrayList<Integer> PF, boolean isOutside) {
        if (PF.get(0) == 0) {
            return 0;
        } else {
            int[] iPF = new int[PF.size()];
            for (int i = 0; i < PF.size(); i++) {
                iPF[i] = PF.get(i);
            }

            int temp = -1;
            int inside = 1;
            int outside = 1;

            for (int i = 0; i < iPF.length; i++) {
                if (temp == -1) {
                    temp = iPF[i];
                    if (i == iPF.length - 1) {
                        inside = inside * temp;
                    }
                    continue;
                } else {
                    if (temp == iPF[i]) {
                        outside = outside * iPF[i];
                        temp = -1;
                    } else {
                        if (temp == iPF[i]) {
                            outside = outside * iPF[i];
                            temp = iPF[i];
                        } else {
                            inside = inside * temp;
                            temp = iPF[i];
                        }
                    }
                }
            }

            if (isOutside) {
                return outside;
            } else {
                return inside;
            }
        }
    }
}
