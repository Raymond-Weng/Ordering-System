import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

public class Frame {
    public final String[][] ITEMS = {
            {"糖葫蘆（番茄）", "糖葫蘆（番茄+蜜餞）", "鮮奶茶", ""},
            {"炒泡麵", "炒泡麵（加蛋）", "炒泡麵（加起司）", "炒泡麵（都加）"},
            {"雞肉三明治", "雞肉三明治（加起司）", "火腿三明治", "火腿三明治（加起司）"},
            {"蜂蜜法吐", "蜂蜜法吐（+棉花糖）", "煉乳法吐", "煉乳法吐（+棉花糖）"}
    };

    public final boolean[] DISCOUNTABLE = {false, false, true, true, true, true, true, false, false, false, false, true, true, true, true};

    public int number = 0;

    public JFrame frame;
    public JPanel panel1;
    public JPanel panel2;
    public JPanel panel3;
    public JPanel panel4;
    public JScrollPane jScrollPane;

    int itemCount = 0;

    Dictionary<JButton, Integer> buttonDictionary = new Hashtable<>();

    public Frame() {
        frame = new JFrame();

        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        jScrollPane = new JScrollPane(panel4, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.setLayout(new GridLayout(2, 2));
        frame.add(panel1);
        frame.add(panel2);
        frame.add(panel3);
        frame.add(jScrollPane);

        panel1.add(new JTextArea("載入中..."));
        panel1.getComponent(0).setFont(new Font(null, Font.PLAIN, 30));
        ((JTextArea) panel1.getComponent(0)).setEditable(false);
        panel1.getComponent(0).setFocusable(false);

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
                    if (number != 3) {
                        panel3.getComponent(number).setEnabled(!panel3.getComponent(number).isEnabled());
                    } else {
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
                    Main.main.sendMessage();
                    String fileText = "";
                    for (int i = 0; i < Main.main.price.length; i++) {
                        fileText = fileText + (Main.main.ordered[i * 2] + Main.main.ordered[i * 2 + 1]);
                        fileText = fileText + ",";
                    }

                    boolean discount = false;
                    for (int i = 0; i < Main.main.price.length; i++) {
                        if (Main.main.ordered[i * 2 + 1] != 0) {
                            discount = true;
                        }
                    }
                    fileText = fileText + (discount ? 1 : 0) + ",";
                    fileText = fileText + (Main.main.ordered[2] / 3) + ",";
                    int a = Math.min(Main.main.ordered[11 * 2] + Main.main.ordered[11 * 2 + 1] + Main.main.ordered[12 * 2] + Main.main.ordered[12 * 2 + 1] + Main.main.ordered[13 * 2] + Main.main.ordered[13 * 2 + 1] + Main.main.ordered[14 * 2] + Main.main.ordered[14 * 2 + 1], Main.main.ordered[2 * 2] + Main.main.ordered[2 * 2 + 1]);
                    int b = Math.min(Main.main.ordered[12 * 2] + Main.main.ordered[12 * 2 + 1] + Main.main.ordered[14 * 2] + Main.main.ordered[14 * 2 + 1], Main.main.ordered[2 * 2] + Main.main.ordered[2 * 2 + 1]);
                    fileText = fileText + (a - b) + ",";
                    fileText = fileText + b;

                    fileText = fileText + "\n";

                    try (FileWriter fileWriter = new FileWriter(Main.main.file, true)) {
                        fileWriter.append(fileText);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "執行時發生錯誤：" + ex.getMessage());
                        ex.printStackTrace();
                    }
                    Main.main.ordered = new int[Main.main.price.length * 2];
                    Arrays.fill(Main.main.ordered, 0);
                    for (int i = 0; i < itemCount * 7; i++) {
                        panel4.remove(7);
                    }
                    itemCount = 0;
                    number++;
                    setLabel();
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
        for (int i = 0; i < 7; i++) {
            ((JLabel) panel4.getComponent(i)).setBorder(BorderFactory.createLineBorder(Color.black));
        }

        setLabel();
        panel1.updateUI();
        panel2.updateUI();
        panel3.updateUI();
        panel4.updateUI();

        frame.pack();
        frame.setTitle("點餐系統");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void order(int number) {
        if (Main.main.ordered[number * 2] == 0) {
            Main.main.ordered[number * 2] = 1;
            itemCount++;
            panel4.add(new JLabel(String.valueOf(itemCount)));
            ((JLabel) panel4.getComponent(itemCount * 7)).setBorder(BorderFactory.createLineBorder(Color.black));
            panel4.add(new JLabel(ITEMS[(number < 3) ? 0 : (number + 1) / 4][(number < 3) ? number : (number + 1) % 4]));
            ((JLabel) panel4.getComponent(itemCount * 7 + 1)).setBorder(BorderFactory.createLineBorder(Color.black));
            panel4.add(new JLabel(String.valueOf(Main.main.price[number])));
            ((JLabel) panel4.getComponent(itemCount * 7 + 2)).setBorder(BorderFactory.createLineBorder(Color.black));
            panel4.add(new JLabel("1"));
            ((JLabel) panel4.getComponent(itemCount * 7 + 3)).setBorder(BorderFactory.createLineBorder(Color.black));
            panel4.add(new JButton(DISCOUNTABLE[number] ? "否" : "不可用"));
            ((JButton) panel4.getComponent(itemCount * 7 + 4)).addActionListener(new ActionListenerImpl(0, itemCount, number));
            if (!DISCOUNTABLE[number]) {
                panel4.getComponent(itemCount * 7 + 4).setEnabled(false);
            }
            panel4.add(new JButton("+"));
            ((JButton) panel4.getComponent(itemCount * 7 + 5)).addActionListener(new ActionListenerImpl(1, itemCount, number));
            panel4.add(new JButton("-"));
            ((JButton) panel4.getComponent(itemCount * 7 + 6)).addActionListener(new ActionListenerImpl(2, itemCount, number));

            panel4.updateUI();
            jScrollPane.updateUI();
            setLabel();
        } else {
            JOptionPane.showMessageDialog(null, "該餐點已經被點過了");
        }
    }

    public void setLabel() {
        //折扣計算
        String discounts = "";
        //自備餐具少三元
        for (int i = 0; i < Main.main.price.length; i++) {
            if (Main.main.ordered[i * 2 + 1] != 0) {
                discounts = discounts + "自備餐具少三元\n";
                break;
            }
        }
        //糖葫蘆3個100
        if (Main.main.ordered[2] >= 3) {
            discounts = discounts + "糖葫蘆（番茄+蜜餞）三串100（" + Main.main.ordered[2] / 3 + "次折扣）\n";
        }

        //43號組
        if(Math.min(Main.main.ordered[11 * 2] + Main.main.ordered[11 * 2 + 1] + Main.main.ordered[12 * 2] + Main.main.ordered[12 * 2 + 1] + Main.main.ordered[13 * 2] + Main.main.ordered[13 * 2 + 1] + Main.main.ordered[14 * 2] + Main.main.ordered[14 * 2 + 1], Main.main.ordered[2 * 2] + Main.main.ordered[2 * 2 + 1]) != 0){
            discounts = discounts + "法吐+奶茶套餐80元（" + Math.min(Main.main.ordered[11 * 2] + Main.main.ordered[11 * 2 + 1] + Main.main.ordered[12 * 2] + Main.main.ordered[12 * 2 + 1] + Main.main.ordered[13 * 2] + Main.main.ordered[13 * 2 + 1] + Main.main.ordered[14 * 2] + Main.main.ordered[14 * 2 + 1], Main.main.ordered[2 * 2] + Main.main.ordered[2 * 2 + 1]) + "次折扣）\n";
        }

        //更新
        ((JTextArea) panel1.getComponent(0)).setText("目前號碼："
                + ((number < 10) ? "00" + number : (number < 100) ? "0" + number : number)
                + "\n\n目前總價："
                + Main.main.getPriceTotal()
                + "\n\n折扣：\n"
                + (discounts.isEmpty() ? "無" : discounts));
    }
}

class ActionListenerImpl implements ActionListener {

    int type;
    public int buttonNumber;
    public int order;

    public ActionListenerImpl(int type, int buttonNumber, int order) {
        this.type = type;
        this.buttonNumber = buttonNumber;
        this.order = order;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (type) {
            case 0:
                if (((JButton) e.getSource()).getText().equals("是")) {
                    if (Main.main.ordered[order * 2] == 0) {
                        ((JButton) e.getSource()).setText("否");
                        Main.main.ordered[order * 2] = Main.main.ordered[order * 2 + 1];
                        Main.main.ordered[order * 2 + 1] = 0;
                        Main.main.frame.setLabel();
                    } else {
                        JOptionPane.showMessageDialog(null, "該餐點無自備餐具的品項已經存在");
                    }
                } else {
                    if (Main.main.ordered[order * 2 + 1] == 0) {
                        ((JButton) e.getSource()).setText("是");
                        Main.main.ordered[order * 2 + 1] = Main.main.ordered[order * 2];
                        Main.main.ordered[order * 2] = 0;
                        Main.main.frame.setLabel();
                    } else {
                        JOptionPane.showMessageDialog(null, "該餐點自備餐具的品項已經存在");
                    }
                }
                break;
            case 1:
                Main.main.ordered[order * 2 + (((JButton) Main.main.frame.panel4.getComponent(buttonNumber * 7 + 4)).getText().equals("是") ? 1 : 0)] += 1;
                ((JLabel) Main.main.frame.panel4.getComponent(buttonNumber * 7 + 3)).setText(String.valueOf(Integer.parseInt(((JLabel) Main.main.frame.panel4.getComponent(buttonNumber * 7 + 3)).getText()) + 1));
                Main.main.frame.setLabel();
                break;
            case 2:
                Main.main.ordered[order * 2 + (((JButton) Main.main.frame.panel4.getComponent(buttonNumber * 7 + 4)).getText().equals("是") ? 1 : 0)] -= 1;
                if (Main.main.ordered[order * 2 + (((JButton) Main.main.frame.panel4.getComponent(buttonNumber * 7 + 4)).getText().equals("是") ? 1 : 0)] == 0) {
                    for (int i = 0; i < 7; i++) {
                        Main.main.frame.panel4.remove(buttonNumber * 7);
                    }
                    Main.main.frame.panel4.updateUI();
                    Main.main.frame.itemCount--;
                    for (int r = buttonNumber; r < Main.main.frame.itemCount + 1; r++) {
                        ((JLabel) Main.main.frame.panel4.getComponent(r * 7)).setText(String.valueOf(Integer.parseInt(((JLabel) Main.main.frame.panel4.getComponent(r * 7)).getText()) - 1));
                        for (int k = 0; k < 3; k++) {
                            ((ActionListenerImpl) ((JButton) Main.main.frame.panel4.getComponent(r * 7 + 4 + k)).getActionListeners()[0]).buttonNumber--;
                        }
                    }
                } else {
                    ((JLabel) Main.main.frame.panel4.getComponent(buttonNumber * 7 + 3)).setText(String.valueOf(Integer.parseInt(((JLabel) Main.main.frame.panel4.getComponent(buttonNumber * 7 + 3)).getText()) - 1));
                }
                Main.main.frame.setLabel();
                break;
        }
    }
}
