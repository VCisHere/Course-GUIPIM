package guipim.user;

import java.io.*;
import java.util.HashMap;
import java.util.Observable;
import java.util.Set;

/**
 * 用户数据的管理对象
 */
public class UserData extends Observable {

    public static UserData instance;

    private Users users;
    private String selectUserName;

    private static class Users implements Serializable {
        private HashMap<String, String> mainData;
        private Users() {
            mainData = new HashMap<>();
        }
    }

    private UserData() {
        if (!readData()) {
            users = new Users();
            saveData();
        }
        if (users.mainData.keySet().iterator().hasNext()) {
            selectUserName = users.mainData.keySet().iterator().next();
        } else {
            selectUserName = "";
        }
        /*
        System.out.println(selectUserName);
        for (Map.Entry<String, String> entry : users.mainData.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
         */
    }

    public static UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    private void saveData() {
        try {
            FileOutputStream fos = new FileOutputStream(new File("users"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.close();
        } catch (IOException ignored) {}
    }

    private boolean readData() {
        try {
            FileInputStream fos = new FileInputStream(new File("users"));
            ObjectInputStream ois = new ObjectInputStream(fos);
            users = (Users) ois.readObject();
            ois.close();
            return true;
        } catch (IOException | ClassNotFoundException ignored) {
            return false;
        }
    }

    /**
     * 添加新用户
     * @param userName 用户名
     * @param password 密码
     */
    public void addUser(String userName, String password) {
        users.mainData.put(userName, UserAuthenticator.encrypt(password));
        saveData();
        setChanged();
        notifyObservers();
    }

    /**
     * 获取所有用户名
     * @return 所有用户名
     */
    public Set<String> getUserNames() {
        return users.mainData.keySet();
    }

    /**
     * 获取当前选择用户的密码
     * @return 当前选择用户的密码（加密）
     */
    public String getPassword() {
        return users.mainData.get(selectUserName);
    }

    /**
     * 设置选择的用户
     * @param userName 选择的用户名
     */
    public void setSelectUserName(String userName) {
        selectUserName = userName;
    }

    /**
     * 获取选择的用户名
     * @return 选择的用户名
     */
    public String getSelectUserName() {
        return selectUserName;
    }

}
