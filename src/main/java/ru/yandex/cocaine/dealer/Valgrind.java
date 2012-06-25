package ru.yandex.cocaine.dealer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Valgrind {
    private final static String CONFIG_PATH = "./src/test/resources/dealer_config.json";
    private final static String PATH = "app1/test_handle";

    public static void main(String[] args) throws TimeoutException {
        TextMessage message = new TextMessage("hello world");
        MessagePolicy messagePolicy = MessagePolicy.builder()
                .timeout(100000, TimeUnit.MILLISECONDS).build();
        Dealer dealer = null;
        long cursum = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            dealer = new Dealer(CONFIG_PATH);
            int counter = 1;
            int total_counter = 1;
            for (;;) {
                Response r = null;
                try {
                    long begin = System.nanoTime();
                    r = dealer.sendMessage(PATH, message, messagePolicy);
                    String response = r.get(100000, TimeUnit.MILLISECONDS);
                    long end = System.nanoTime();
                    cursum += (end - begin);
                    if (counter % 1000 == 0) {
                        String date = sdf.format(Calendar.getInstance().getTime());
                        System.out.println(date+" " + response + " " + total_counter + " "
                                + ((cursum) / (counter * 1000000.0)));
                    }
                    if (counter % 10000 == 0) {
                        cursum = cursum / counter;
                        counter = 1;
                    }
                } finally {
                    if (r != null) {
                        r.close();
                    }
                }
                counter++;
                total_counter++;
            }
        } finally {
            if (dealer != null) {
                dealer.close();
            }
        }
    }
}
