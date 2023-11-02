import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    public static Main main;
    public boolean testing = false;
    public int[] ordered;
    public int[] price = {30, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

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
     * 12. 可樂
     * 13. 雪碧
     * 14. 奶茶
     *
     * ---
     *
     * price[a] = price of number a
     */

    public Frame frame;

    public static void main(String[] args) {
        main = new Main();
        main.run();
    }

    public void run() {
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
                //TODO
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
        }
    }

    public int getPriceTotal(){
        int priceTotal = 0;

        for(int i = 0; i < price.length; i++){
            priceTotal += ordered[i * 2] * price[i];
            priceTotal += ordered[(i * 2) + 1] * (price[i] - 3);
        }

        return priceTotal;
    }
}