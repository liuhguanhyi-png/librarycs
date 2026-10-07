package service;

import java.util.Scanner;

public class ConsoleUtil {
    public static Scanner sc = new Scanner(System.in);

    public static String readLine(String tip) {
        System.out.print(tip);
        return sc.nextLine().trim();
    }

    public static int readInt(String tip) {
        while (true) {
            try {
                System.out.print(tip);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("输入数字！");
            }
        }
    }

    public static void printLine() {
        System.out.println("------------------------------------------");
    }
}
