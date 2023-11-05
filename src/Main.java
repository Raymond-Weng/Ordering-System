import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Main {
    public static Main main;
    public boolean testing = false;
    public int[] ordered;
    public int[] price = {30, 35, 40, 50, 60, 60, 65, 30, 45, 0, 0, 50, 55, 50, 55};

    public File file = new File("./orders/" + new SimpleDateFormat("MMdd-HHmm").format(new Date()) + ".csv");

    /*
     * ordered[2a] = number a with not having tableware
     * ordered[2a+1] = number a with having tableware
     *
     * - team1 (leader:3)
     * 1. 糖葫蘆（番茄）
     * 2. 糖葫蘆（番茄+蜜餞）
     *
     * - team2 (leader:21)
     * 3. 炒泡麵
     * 4. 炒泡麵（加蛋）
     * 5. 炒泡麵（加起司）
     * 6. 炒泡麵（都加）
     *
     * - team3 (leader:36)
     * 7. 雞肉三明治
     * 8. 雞肉三明治（加起司）
     * 9. 火腿三明治
     * 10. 火腿三明治（加起司）
     *
     * - team4 (leader:43)
     * 11. 法式吐司
     * 12. 奶茶
     *
     * ---
     *
     * price[a] = price of number a
     *
     * ---
     *
     * csv index
     * 糖葫蘆（番茄）,糖葫蘆（番茄+蜜餞）,奶茶,炒泡麵,炒泡麵（加蛋）,炒泡麵（加起司）,炒泡麵（都加）,雞肉三明治,雞肉三明治（加起司）,火腿三明治,火腿三明治（加起司）,蜂蜜法吐,蜂蜜法吐（+棉花糖）,煉乳法吐,煉乳法吐（+棉花糖）,奶茶,自備餐具,糖葫蘆折扣
     */

    public Frame frame;

    public static void main(String[] args) {
        main = new Main();
        main.run();
    }

    public void run() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "執行時發生錯誤：" + e.getMessage());
            e.printStackTrace();
        }
        ordered = new int[price.length * 2];
        Arrays.fill(ordered, 0);

        if (JOptionPane.showConfirmDialog(null, "請問在Line要輸出測試訊息嗎", "測試Line", JOptionPane.YES_NO_OPTION) == 0) {
            testing = true;
            sendMessage();
        }

        frame = new Frame();
    }

    public void sendMessage() {
        try {
            String token = "G3vpb2WkhaUt65qyU41uyLfoTl3EHBRjUXZKNjurQPq"; // 將YOUR_LINE_NOTIFY_TOKEN替換為您的權杖
            String message = "";

            if (testing) {
                message = "\n測試訊息";
                testing = false;
            } else {
                String one = "";
                for (int i = 0; i < 2; i++) {
                    if (ordered[i * 2] != 0) {
                        one = one + Main.main.frame.ITEMS[0][i] + " * " + ordered[i * 2] + "\n";
                    }
                    if (ordered[i * 2 + 1] != 0) {
                        one = one + Main.main.frame.ITEMS[0][i] + "（自備餐具）* " + ordered[i * 2 + 1] + "\n";
                    }
                }
                if (one.isEmpty()) {
                    one = "無\n";
                }
                String two = "";
                for (int i = 3; i < 3 + 4; i++) {
                    if (ordered[i * 2] != 0) {
                        two = two + Main.main.frame.ITEMS[1][i - 3] + " * " + ordered[i * 2] + "\n";
                    }
                    if (ordered[i * 2 + 1] != 0) {
                        two = two + Main.main.frame.ITEMS[1][i - 3] + "（自備餐具）* " + ordered[i * 2 + 1] + "\n";
                    }
                }
                if (two.isEmpty()) {
                    two = "無\n";
                }
                String three = "";
                for (int i = 7; i < 7 + 4; i++) {
                    if (ordered[i * 2] != 0) {
                        three = three + Main.main.frame.ITEMS[2][i - 7] + " * " + ordered[i * 2] + "\n";
                    }
                    if (ordered[i * 2 + 1] != 0) {
                        three = three + Main.main.frame.ITEMS[2][i - 7] + "（自備餐具）* " + ordered[i * 2 + 1] + "\n";
                    }
                }
                if (three.isEmpty()) {
                    three = "無\n";
                }
                String four = "";
                for (int i = 11; i < 11 + 2; i++) {
                    if (ordered[i * 2] != 0) {
                        four = four + Main.main.frame.ITEMS[3][i - 11] + " * " + ordered[i * 2] + "\n";
                    }
                    if (ordered[i * 2 + 1] != 0) {
                        four = four + Main.main.frame.ITEMS[3][i - 11] + "（自備餐具）* " + ordered[i * 2 + 1] + "\n";
                    }
                }
                if (four.isEmpty()) {
                    four = "無\n";
                }
                if(ordered[2 * 2] != 0){
                    four = four + Main.main.frame.ITEMS[0][2] + " * " + ordered[2 * 2] + "\n";
                }
                if (ordered[2 * 2 + 1] != 0) {
                    four = four + Main.main.frame.ITEMS[0][2] + "（自備餐具）* " + ordered[2 * 2 + 1] + "\n";
                }
                message = ((Main.main.frame.number < 10) ? "00" + Main.main.frame.number : (Main.main.frame.number < 100) ? "0" + Main.main.frame.number : Main.main.frame.number)
                        + "\n第一組點單：\n" + one
                        + "\n第二組點單：\n" + two
                        + "\n第三組點單：\n" + three
                        + "\n第四組點單：\n" + four
                        + "\n本訂單共" + getPriceTotal() + "元";

            }

            String url = "https://notify-api.line.me/api/notify";

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            String data = "message=" + message;
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(dataBytes);
            }

            int responseCode = connection.getResponseCode();
            connection.disconnect();

            if (responseCode == 200) {
                JOptionPane.showMessageDialog(null, "發送成功");
            } else {
                JOptionPane.showMessageDialog(null, "發送失敗，錯誤代碼"
                        + responseCode
                        + "，以下是應發送的訊息：\n訂單:"
                        + message
                        + "\n\n訊息自動複製");
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("訂單:" + message), null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "發送訊息時產生錯誤");
            e.printStackTrace();
        }
    }

    public int getPriceTotal() {
        int priceTotal = 0;

        for (int i = 0; i < price.length; i++) {
            priceTotal += ordered[i * 2] * price[i];
            priceTotal += ordered[i * 2 + 1] * price[i];
        }

        //自備餐具少3元
        for (int i = 0; i < price.length; i++) {
            if (ordered[i * 2 + 1] != 0) {
                priceTotal -= 3;
                break;
            }
        }

        //糖葫蘆加蜜餞三個100
        priceTotal -= 5 * (ordered[2] / 3);

        return priceTotal;
    }
}
