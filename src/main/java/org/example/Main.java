package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static BlockingQueue<String> list1 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> list2 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> list3 = new ArrayBlockingQueue<>(100);


    public static void main(String[] args) throws InterruptedException {


        Thread textGenerator = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                String text = generateText("abc", 100000);
                try {
                    list1.put(text);
                    list2.put(text);
                    list3.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        textGenerator.start();

        Thread a = getThread(list1, 'a');
        Thread b = getThread(list2, 'b');
        Thread c = getThread(list3, 'c');

        a.start();
        b.start();
        c.start();

        a.join();
        b.join();
        c.join();


    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Thread getThread(BlockingQueue<String> list, char c) {
        return new Thread(() -> {
            int max = findMaxCharCounter(list, c);
            System.out.println("Максимальное кол-во символа - " + c + ": " + max);
        });
    }

    public static int findMaxCharCounter(BlockingQueue<String> list, char c) {
        int count = 0;
        int max = 0;
        String text;
        try {
            for (int i = 0; i < 10000; i++) {
                text = list.take();
                for (char ch : text.toCharArray()) {
                    if (ch == c) count++;
                }
                if (count > max) max = count;
                count = 0;

            }

        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "was interpuded");
            return -1;
        }
        return max;
    }
}