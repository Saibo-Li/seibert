package socket;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//聊天程序服务端
public class ChatServer {
    public static void main(String[] args) throws IOException {
        JFrame f = new JFrame("Seibert");
        f.setSize(400, 350);
        f.setLocation(300, 300);
        f.setLayout(null);

        final JTextArea ta = new JTextArea();
        ta.setLineWrap(true);

        JScrollPane sp = new JScrollPane(ta);
        sp.setBounds(20, 20, 340, 200);

        JPanel p = new JPanel();
        p.setBounds(40, 240, 300, 80);
        p.setLayout(null);

        final JTextField tf = new JTextField();
        tf.setBounds(20, 10, 200, 30);

        JButton b = new JButton("发送");
        b.setBounds(240, 10, 60, 30);

        p.add(tf);
        p.add(b);

        f.add(sp);
        f.add(p);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        tf.grabFocus();

        ServerSocket ss = new ServerSocket(8888);
        final Socket s = ss.accept();
        OutputStream os = s.getOutputStream();
        final DataOutputStream dos = new DataOutputStream(os);

// 发送信息是非持续性的，由按钮触发，给按钮加个监听器即可
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String msg = tf.getText();
                try {
                    dos.writeUTF(msg);
                } catch (IOException e1) {
// TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                StringBuffer sb = new StringBuffer(ta.getText());
// 令第一条消息出现在第一行而不是第二行
                if (sb.length() == 0)
                    sb.append("【本机】：" + msg);
                else
                    sb.append("\n【本机】：" + msg);

                ta.setText(sb.toString());
                tf.setText(null);
                tf.grabFocus();
            }
        });

// 信息接收监听是持续进行的，开一个循环执行的线程即可
        Thread tReceive = new Thread() {
            public void run() {
                try {
                    InputStream is = s.getInputStream();
                    DataInputStream dis = new DataInputStream(is);

                    while (true) {
                        String msg = dis.readUTF();
                        StringBuffer sb = new StringBuffer(ta.getText());

                        if (sb.length() == 0)
                            sb.append("【对方】：" + msg);
                        else
                            sb.append("\n【对方】：" + msg);

                        ta.setText(sb.toString());
                    }
                } catch (IOException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };

        tReceive.start();
    }
}


