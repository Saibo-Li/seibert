package tableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 *
 * Mysql+GUI案例
 *
 *
 *
 * Created by JackLi on 2019/8/10 9:19.
 */
public class TestGUI {
    public static int currentPage;
    public static int lastPage;
    public static boolean ok = true;

    static JFrame f = new JFrame("Hero表格");
    static JButton bEdit = new JButton("编辑数据");
    static JButton bAdd = new JButton("增加数据");
    static JButton bDel = new JButton("删除数据");
    static JButton bFirstPage = new JButton("首页");
    static JButton bPreviousPage = new JButton("上一页");
    static JButton bNextPage = new JButton("下一页");
    static JButton bLastPage = new JButton("末页");

    static JComboBox<Object> cb = new JComboBox<>();
    public static boolean cbListenerEnabled = true;
    static JLabel l = new JLabel();


    public static void isEmpty(JTextField tf, String msg) {
        if (!ok)
            return;
        if (0 == tf.getText().length()) {
            JOptionPane.showMessageDialog(f, msg + "不能为空，请确认！");
            ok = false;
        }
    }


    public static void isNumber(JTextField tf, String msg) {
        if (!ok)
            return;
        try {
            Double.parseDouble(tf.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, msg + "必须是数字，请确认！");
            ok = false;
        }
    }


    public static void isInt(JTextField tf, String msg) {
        if (!ok)
            return;
        try {
            Integer.parseInt(tf.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, msg + "必须是整数，请确认！");
            ok = false;
        }
    }


    public static void updateButtonStatus(int currentPage, int lastPage) {
        bDel.setEnabled(true);
        bFirstPage.setEnabled(true);
        bPreviousPage.setEnabled(true);
        bNextPage.setEnabled(true);
        bLastPage.setEnabled(true);

        if (new HeroDAO().list().size() == 0)
            bDel.setEnabled(false);

        if (currentPage == 1 || lastPage == 0) {
            bFirstPage.setEnabled(false);
            bPreviousPage.setEnabled(false);
        }
        if (currentPage == lastPage || lastPage == 0) {
            bNextPage.setEnabled(false);
            bLastPage.setEnabled(false);
        }

        String str = String.format("当前是第%d页，共%d页", currentPage, lastPage);
        l.setText(str);

        // 因为cb在执行.removeAllItems()等方法时会触发监听器，所以需得先令监听器赞不做出反应才可以执行这些方法
        // 否则就形成了递归逻辑，会出错
        cbListenerEnabled = false;
        if (cb.getItemCount() != 0)
            cb.removeAllItems();
        for (int i = 0; i < lastPage; i++)
            cb.addItem(i + 1);
        // 令cb选定某个页面
        cb.setSelectedItem(currentPage);
        cbListenerEnabled = true;
    }


    public static void main(String[] args) {
        f.setSize(800, 600);
        f.setLocation(300, 300);
        f.setLayout(null);

        final HeroTableModel htm = new HeroTableModel();
        currentPage = 1;
        final HeroDAO hd = new HeroDAO();
        // HeroDAO的list方法右边的参数是count，即从左边参数(起点下标)开始总共有多少项数据；
        // 即使起始加上count大于size也没关系，会自动列到最后一项数据为止
        htm.heros = hd.list(currentPage * 10 - 10, 10);
        if (hd.list().size() > 10) {
            if (hd.list().size() % 10 == 0)
                lastPage = hd.list().size() / 10;
            else
                lastPage = hd.list().size() / 10 + 1;
        } else
            lastPage = 1;
        // 初始化时根据生成的lastPage和当前页更新页面按钮状态
        updateButtonStatus(currentPage, lastPage);

        final JTable t = new JTable(htm);
        t.getSelectionModel().setSelectionInterval(0, 0);

        JScrollPane sp = new JScrollPane(t);
        sp.setBounds(0, 0, 780, 300);

        // 增加数据编辑页面的模态窗口
        final JDialog dAdd = new JDialog(f);
        dAdd.setModal(true);
        dAdd.setTitle("新增数据编辑");
        dAdd.setSize(400, 350);
        dAdd.setLocation(400, 400);
        dAdd.setResizable(false);
        dAdd.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBounds(20, 50, 340, 50);
        p1.setLayout(new FlowLayout());
        JLabel l1 = new JLabel("名称：");
        l1.setPreferredSize(new Dimension(120, 30));
        final JTextField tf1 = new JTextField();
        tf1.setPreferredSize(new Dimension(150, 30));
        p1.add(l1);
        p1.add(tf1);

        JPanel p2 = new JPanel();
        p2.setBounds(20, 120, 340, 50);
        p2.setLayout(new FlowLayout());
        JLabel l2 = new JLabel("血量（数字）：");
        l2.setPreferredSize(new Dimension(120, 30));
        final JTextField tf2 = new JTextField();
        tf2.setPreferredSize(new Dimension(150, 30));
        p2.add(l2);
        p2.add(tf2);

        JPanel p3 = new JPanel();
        p3.setBounds(20, 190, 340, 50);
        p3.setLayout(new FlowLayout());
        JLabel l3 = new JLabel("伤害（整数）：");
        l3.setPreferredSize(new Dimension(120, 30));
        final JTextField tf3 = new JTextField();
        tf3.setPreferredSize(new Dimension(150, 30));
        p3.add(l3);
        p3.add(tf3);

        JButton b1 = new JButton("提交");
        b1.setBounds(150, 250, 80, 50);
        b1.addActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent e) {
                isEmpty(tf1, "名称");
                isEmpty(tf2, "血量");
                isNumber(tf2, "血量");
                isEmpty(tf3, "伤害");
                isInt(tf3, "伤害");

                if (ok) {
                    HeroDAO hd = new HeroDAO();
                    Hero h = new Hero();
                    h.name = tf1.getText();
                    h.hp = (float) Double.parseDouble(tf2.getText());
                    h.damage = Integer.parseInt(tf3.getText());
                    hd.add(h);
                    if (hd.list().size() % 10 == 0)
                        lastPage = hd.list().size() / 10;
                    else
                        lastPage = hd.list().size() / 10 + 1;
                    htm.heros = hd.list(currentPage * 10 - 10, 10);
                    t.updateUI();
                    updateButtonStatus(currentPage, lastPage);
                    JOptionPane.showMessageDialog(f, "提交成功");
                }
                ok = true;
            }
        });

        dAdd.add(p1);
        dAdd.add(p2);
        dAdd.add(p3);
        dAdd.add(b1);

        // 主页面增加数据页面的入口按钮
        bAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tf1.setText(null);
                tf2.setText(null);
                tf3.setText(null);
                dAdd.setVisible(true);
            }
        });

        // 删除数据的按钮
        bDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 返回选中的所有行
                int[] rows = t.getSelectedRows();
                // 返回选中的开头一行
                // int row = t.getSelectedRow();
                if (rows.length == 0) {
                    JOptionPane.showMessageDialog(f, "未选中任何数据！");
                    return;
                }
                int option = JOptionPane.showConfirmDialog(f, "是否删除选中数据 ？");
                if (JOptionPane.OK_OPTION == option) {
                    HeroDAO hd = new HeroDAO();
                    for (int i = 0; i < rows.length; i++) {
                        Hero h = htm.heros.get(rows[i]);
                        hd.delete(h.id);
                    }
                    t.getSelectionModel().setSelectionInterval(0, 0);

                    if (hd.list().size() % 10 == 0) {
                        // size是10的倍数且当前页即末页说明末页内容删光了，需跳转显示上一页的内容
                        if (currentPage == lastPage)
                            currentPage--;
                        lastPage = hd.list().size() / 10;
                    } else
                        lastPage = hd.list().size() / 10 + 1;
                    htm.heros = hd.list(currentPage * 10 - 10, 10);
                    t.updateUI();
                    updateButtonStatus(currentPage, lastPage);
                    JOptionPane.showMessageDialog(f, "已删除选中数据！");
                }
            }
        });

        // 数据编辑模态窗口
        final JDialog dEdit = new JDialog(f);
        dAdd.setModal(true);
        dEdit.setTitle("数据修改");
        dEdit.setSize(400, 350);
        dEdit.setLocation(400, 400);
        dEdit.setResizable(false);
        dEdit.setLayout(null);

        JPanel p4 = new JPanel();
        p4.setBounds(20, 50, 340, 50);
        p4.setLayout(new FlowLayout());
        JLabel l4 = new JLabel("名称：");
        l4.setPreferredSize(new Dimension(120, 30));
        final JTextField tf4 = new JTextField();
        tf4.setPreferredSize(new Dimension(150, 30));
        p4.add(l4);
        p4.add(tf4);

        JPanel p5 = new JPanel();
        p5.setBounds(20, 120, 340, 50);
        p5.setLayout(new FlowLayout());
        JLabel l5 = new JLabel("血量（数字）：");
        l5.setPreferredSize(new Dimension(120, 30));
        final JTextField tf5 = new JTextField();
        tf5.setPreferredSize(new Dimension(150, 30));
        p5.add(l5);
        p5.add(tf5);

        JPanel p6 = new JPanel();
        p6.setBounds(20, 190, 340, 50);
        p6.setLayout(new FlowLayout());
        JLabel l6 = new JLabel("伤害（整数）：");
        l6.setPreferredSize(new Dimension(120, 30));
        final JTextField tf6 = new JTextField();
        tf6.setPreferredSize(new Dimension(150, 30));
        p6.add(l6);
        p6.add(tf6);

        JButton b2 = new JButton("提交");
        b2.setBounds(150, 250, 80, 50);
        b2.addActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent e) {
                isEmpty(tf4, "名称");
                isEmpty(tf5, "血量");
                isNumber(tf5, "血量");
                isEmpty(tf6, "伤害");
                isInt(tf6, "伤害");

                if (ok) {
                    int row = t.getSelectedRow();
                    if (row == -1) {
                        JOptionPane.showMessageDialog(f, "未选中任何数据！");
                        return;
                    }
                    int option = JOptionPane.showConfirmDialog(f, "是否修改选中数据 ？");
                    if (JOptionPane.OK_OPTION == option) {
                        Hero h = new Hero();
                        h.id = htm.heros.get(row).id;
                        h.name = tf4.getText();
                        h.hp = (float) Double.parseDouble(tf5.getText());
                        h.damage = Integer.parseInt(tf6.getText());

                        HeroDAO hd = new HeroDAO();
                        hd.update(h);

                        htm.heros = hd.list();
                        t.updateUI();
                        JOptionPane.showMessageDialog(f, "已修改选中数据！");
                        dEdit.setVisible(false);
                    }
                }
                ok = true;
            }
        });

        dEdit.add(p4);
        dEdit.add(p5);
        dEdit.add(p6);
        dEdit.add(b2);

        // 主页面编辑数据页面的入口按钮
        bEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tf4.setText(null);
                tf5.setText(null);
                tf6.setText(null);
                dEdit.setVisible(true);
            }
        });

        // 增加、删除、编辑按钮的整合面板
        JPanel pControl = new JPanel();
        pControl.setBounds(0, 350, 780, 60);
        pControl.setLayout(new FlowLayout());
        pControl.add(bAdd);
        pControl.add(bDel);
        pControl.add(bEdit);

        // 页面按钮组件添加监听器
        bFirstPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentPage = 1;
                htm.heros = hd.list(currentPage * 10 - 10, 10);
                t.updateUI();
                updateButtonStatus(currentPage, lastPage);
            }
        });

        bPreviousPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentPage--;
                htm.heros = hd.list(currentPage * 10 - 10, 10);
                t.updateUI();
                updateButtonStatus(currentPage, lastPage);
            }
        });

        bNextPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentPage++;
                htm.heros = hd.list(currentPage * 10 - 10, 10);
                t.updateUI();
                updateButtonStatus(currentPage, lastPage);
            }
        });

        bLastPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentPage = lastPage;
                htm.heros = hd.list(currentPage * 10 - 10, 10);
                t.updateUI();
                updateButtonStatus(currentPage, lastPage);
            }
        });

        cb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 为避免在updateButtonStatus时因会触发cb的监听器而导致递归死循环，在updateButtonStatus令监听器不做反应
                if (!cbListenerEnabled)
                    return;
                currentPage = (int) cb.getSelectedItem();
                System.out.println(currentPage);
                htm.heros = hd.list(currentPage * 10 - 10, 10);
                t.updateUI();
                updateButtonStatus(currentPage, lastPage);
            }
        });

        // 页面按钮整合面板
        JPanel pPage = new JPanel();
        pPage.setBounds(0, 430, 780, 60);
        pPage.setLayout(new FlowLayout());
        pPage.add(bFirstPage);
        pPage.add(bPreviousPage);
        pPage.add(cb);
        pPage.add(bNextPage);
        pPage.add(bLastPage);

        String str = String.format("当前是第%d页，共%d页", currentPage, lastPage);
        l.setText(str);
        l.setBounds(320, 500, 160, 50);

        f.add(sp);
        f.add(pControl);
        f.add(pPage);
        f.add(l);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }
}
