package com.pro.empdep.firebase;

import java.util.Random;

public class RandomPhotoUrlGenerator {

    public String randomUsernameExtension() {
        char[] chars1 = "ABCDEF012GHIJKL345MNOPQR678STUVWXYZ9/:'`abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb1 = new StringBuilder();
        Random random1 = new Random();
        int randomLength = getRandomNumber(1, 20);
        for (int i = 0; i < 8; i++) {
            char c1 = chars1[random1.nextInt(chars1.length)];
            sb1.append(c1);
        }
        return sb1.toString();

    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
