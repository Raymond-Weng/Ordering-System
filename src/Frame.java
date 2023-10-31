import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Dictionary;
import java.util.Hashtable;

public class Frame {
    public String[][] ITEMS = {
        {"糖葫蘆", "", "", ""},
        {"炒泡麵", "炒泡麵（加蛋）", "炒泡麵（加起司）", "炒泡麵（都加）"},
        {"三明治", "", "", ""},
        {"法式吐司", "可樂", "雪碧", ""}
    };

    public JFrame frame;
    public JPanel panel1;
    public JPanel panel2;

    Dictionary<JButton, Integer> buttonDictionary = new Hashtable<>();

    public Frame() {
        frame = new JFrame();
        panel1 = new JPanel();
        panel2 = new JPanel();

        frame.setLayout(new GridLayout(1, 2));
        frame.add(panel1);
        frame.add(panel2);

        panel1.setLayout(new GridLayout(4,4));
        for(int i = 0; i < 4; i++){
            for(int r = 0; r < 4; r++){
                JButton button = new JButton(ITEMS[i][r]);
                if(ITEMS[i][r].isEmpty()){
                    button.setEnabled(false);
                }else{
                    button.addActionListener(new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println(buttonDictionary.get(e.getSource()));
                        }
                    });
                    buttonDictionary.put(button, 4 * i + r);
                }
                panel1.add(button);
            }
        }

        panel2.setLayout(new GridLayout(2,1));

        frame.pack();
        frame.setTitle("點餐系統");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
