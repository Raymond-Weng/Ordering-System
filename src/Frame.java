import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Dictionary;
import java.util.Hashtable;

public class Frame {
    public String[][] ITEMS = {
            {"糖葫蘆（番茄）", "糖葫蘆（番茄+蜜餞）", "", ""},
            {"炒泡麵", "炒泡麵（加蛋）", "炒泡麵（加起司）", "炒泡麵（都加）"},
            {"雞肉三明治", "雞肉三明治（加起司）", "火腿三明治", "火腿三明治（加起司）"},
            {"法式吐司", "可樂", "雪碧", "奶茶"}
    };

    public int number = 0;

    public JFrame frame;
    public JPanel panel1;
    public JPanel panel2;
    public JPanel panel3;
    public JPanel panel4;
    public JScrollPane jScrollPane;

    Dictionary<JButton, Integer> buttonDictionary = new Hashtable<>();

    public Frame() {
        frame = new JFrame();

        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        jScrollPane = new JScrollPane(panel4, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        frame.setLayout(new GridLayout(2, 2));
        frame.add(panel1);
        frame.add(panel2);
        frame.add(panel3);
        frame.add(jScrollPane);

        panel1.add(new JTextArea("載入中..."));

        panel2.setLayout(new GridLayout(2, 2));
        panel2.add(new JButton("更改目前號碼"));
        ((JButton) panel2.getComponent(0)).addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    number = Integer.parseInt(JOptionPane.showInputDialog("輸入目前號碼"));
                    setLabel();
                } catch (NumberFormatException numberFormatException) {
                    JOptionPane.showMessageDialog(null, "無法處理資料，請再試一次");
                }
            }
        });
        panel2.add(new JButton("發送測試訊息"));
        ((JButton) panel2.getComponent(1)).addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.main.testing = true;
                Main.main.sendMessage();
            }
        });
        panel2.add(new JButton("標示已賣完（切換）"));
        ((JButton) panel2.getComponent(2)).addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    number = Integer.parseInt(JOptionPane.showInputDialog("輸入按鈕號碼，從零起算"));
                    if (number != 2 && number != 3) {
                        panel3.getComponent(number).setEnabled(!panel3.getComponent(number).isEnabled());
                    }else{
                        JOptionPane.showMessageDialog(null, "輸入的資料為空白格，請重新輸入");
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException numberFormatException) {
                    JOptionPane.showMessageDialog(null, "無法處理資料，請再試一次");
                }
            }
        });

        panel2.add(new JButton("結帳"));
        ((JButton) panel2.getComponent(3)).addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "確定要結帳嗎？", "結帳確認", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                    System.out.println("ok");
                }
            }
        });

        panel3.setLayout(new GridLayout(4, 4));
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int r = 0; r < 4; r++) {
                JButton button = new JButton(ITEMS[i][r]);
                if (ITEMS[i][r].isEmpty()) {
                    button.setEnabled(false);
                } else {
                    button.addActionListener(new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            order(buttonDictionary.get(e.getSource()));
                        }
                    });
                    buttonDictionary.put(button, count);
                    count++;
                }
                panel3.add(button);
            }
        }

        panel4.setLayout(new GridLayout(0, 7));
        panel4.add(new JLabel("編號"));
        panel4.add(new JLabel("品項"));
        panel4.add(new JLabel("單價"));
        panel4.add(new JLabel("數量"));
        panel4.add(new JLabel("自備餐具"));
        panel4.add(new JLabel("增加"));
        panel4.add(new JLabel("減少"));
        for(int i = 0; i < 7; i++){
            ((JLabel)panel4.getComponent(i)).setBorder(BorderFactory.createLineBorder(Color.black));
        }

        setLabel();

        frame.pack();
        frame.setTitle("點餐系統");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void order(int number){
        if(Main.main.ordered[number] == 0){
            Main.main.ordered[number] = 1;
            //TODO
        }else{
            JOptionPane.showMessageDialog(null, "該餐點已經被點過了");
        }
    }

    public void setLabel(){
        ((JTextArea) panel1.getComponent(0)).setText("目前號碼："
                + ((number < 10) ? "00" + number : (number < 100) ? "0" + number : number)
                + "\n\n目前總價："
                + Main.main.getPriceTotal());
    }
}
