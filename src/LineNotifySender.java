import javax.swing.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LineNotifySender {
    public boolean testing = false;
    public int[] ordered = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public int[] price = {0,0,0,0,0,0,0,0,0};

    /*
     * ordered[2a] = number a with not having tableware
     * ordered[2a+1] = number a with having tableware
     *
     * - team1 (leader:3)
     * 1. 糖葫蘆
     *
     * - team2 (leader:21)
     * 2. 炒泡麵
     * 3. 炒泡麵（加蛋）
     * 4. 炒泡麵（加起司）
     * 5. 炒泡麵（都加）
     *
     * - team3 (leader:36)
     * 6. 三明治
     *
     * - team4 (leader:43)
     * 7. 法式吐司
     * 8. 可樂
     * 9. 雪碧
     *
     * ---
     *
     * price[a] = price of number a
     */

    public Frame frame;

    public static void main(String[] args) {
        new LineNotifySender().run();
    }

    public void run(){
        if(JOptionPane.showConfirmDialog(null, "請問在Line要輸出測試訊息嗎", "測試Line", JOptionPane.YES_NO_OPTION) == 0){
            testing = true;
            sendMessage();
        }

        frame = new Frame();
    }

    public void sendMessage() {
        try {
            String token = "G3vpb2WkhaUt65qyU41uyLfoTl3EHBRjUXZKNjurQPq"; // 將YOUR_LINE_NOTIFY_TOKEN替換為您的權杖
            String message = "";

            if(testing){
                message = "\n測試訊息";
                testing = false;
            }else{
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

            if(responseCode == 200){
                JOptionPane.showMessageDialog(null, "測試成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
