import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by JackLi on 2019/7/21 16:06.
 */

public class Test {
    private String name; // 姓名
    BattleScore score;
    float hp; // 血量

    float armor; // 护甲

    int moveSpeed; // 移动速度

    public Test() {
        score.sendMsg("sdf");

        System.out.println("sdf");
    }


    // 非静态内部类，只有一个外部类对象存在的时候，才有意义
    // 战斗成绩只有在一个英雄对象存在的时候才有意义


    public static void main(String[] args) {
        Test garen = new Test();
        garen.hp = 10;
        System.out.println("qq");
//        garen.name = "盖伦";
        // 实例化内部类
        // BattleScore对象只有在一个英雄对象存在的时候才有意义
        // 所以其实例化必须建立在一个外部类对象的基础之上
//        score.kill = 9;
    }

}

class BattleScore {
    int kill;
    int die;
    int assit;

    public void legendary() {
        System.out.println("尚未超神！");
    }
    public void sendMsg(String msg) {// 用于发送信息
        try {
            System.out.println("qq");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}