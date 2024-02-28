package com.ppxxd.bankingsystem;

import java.util.Random;

public class Card {
    static int last_id = 0;
    int id;
    String digits;
    String pin = "";
    long balance;

    public Card(){
        last_id++;
        digits = generateCardNum();
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            pin += rand.nextInt(10);
        }
        balance = 0L;
    }

    public Card(int id_db, String number_db, String pin_db, long balance_db){
        id = id_db;
        digits = number_db;
        pin = pin_db;
        balance = balance_db;
    }

    public int getID() {
        return id;
    }

    public String getDigits() {
        return digits;
    }

    public String getPin() {
        return pin;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
        Database.updateBalance(this);
    }

    public static String generateCardNum() {
        StringBuilder number = new StringBuilder("400000");
        Random rand = new Random();
        for (int i = 1; i < 10; i++) {
            number.append(rand.nextInt(10));
        }

        number.append(sumLuhn(number.toString()));
        return number.toString();
    }

    public static String sumLuhn(String number) {
        int nSum = 0;
        boolean isOdd = true;
        for (int i = 0; i < number.length(); i++)
        {
            int d = number.charAt(i) - '0';
            if (isOdd) {
                d = d * 2;
            }
            nSum += d / 10;
            nSum += d % 10;

            isOdd = !isOdd;
        }
        nSum = (nSum % 10 == 0) ? 0 : (10 - nSum % 10);
        return Long.toString(nSum);

    }
}
