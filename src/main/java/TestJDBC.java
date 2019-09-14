import tableModel.Hero;

import java.sql.*;

/**
 *  // 建立与数据库的Connection连接
 // 这里需要提供：
 // 数据库所处于的ip:127.0.0.1 (本机)
 // 数据库的端口号： 3306 （mysql专用端口号）
 // 数据库名称 how2java
 // 编码方式 UTF-8
 // 账号 root
 // 密码 admin

 * Created by JackLi on 2019/8/8 12:29.
 */
public class TestJDBC {
    public static Hero get(int id) {
        Hero hero = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/how2java?characterEncoding=UTF-8","root", "admin");
             Statement s = c.createStatement();) {

            String sql = "select * from hero where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            // 因为id是唯一的，ResultSet最多只能有一条记录
            // 所以使用if代替while
            if (rs.next()) {
                hero = new Hero();
                String name = rs.getString(2);
                float hp = rs.getFloat("hp");
                int damage = rs.getInt(4);
                hero.name = name;
                hero.hp = hp;
                hero.damage = damage;
                hero.id = id;
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hero;

    }

    public static void main(String[] args) {

        Hero h = get(22);
        System.out.println(h.name);

    }
}
