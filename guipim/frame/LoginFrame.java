package guipim.frame;

import guipim.dialog.SignUpDialog;
import guipim.user.UserAuthenticator;
import guipim.user.UserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * 登录窗口
 */
public class LoginFrame extends JFrame implements Observer {

    private UserData userData;
    private JComboBox<String> userNameBox;

    public LoginFrame() {
        userData = UserData.getInstance();
        setTitle("PIM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 180);
        setResizable(false);
        setMinimumSize(getMinimumSize());
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        init();

        setVisible(true);
    }

    private void init() {
        userData.addObserver(this);

        Font font = new Font("黑体", Font.PLAIN, 18);
        Dimension dLabel = new Dimension(100, 36);
        Dimension dTextField = new Dimension(260, 36);

        JLabel userNameTip = new JLabel("用户名");
        userNameTip.setFont(font);
        userNameTip.setPreferredSize(dLabel);

        JLabel passwordTip = new JLabel("密码");
        passwordTip.setFont(font);
        passwordTip.setPreferredSize(dLabel);

        userNameBox = new JComboBox<>();
        userNameBox.setFont(font);
        userNameBox.setPreferredSize(dTextField);
        userNameBox.setFocusable(false);

        flushUserNames();

        JPasswordField passwordTextField = new JPasswordField();
        passwordTextField.setEchoChar('*');
        passwordTextField.setFont(font);
        passwordTextField.setPreferredSize(dTextField);

        JButton signUpButton = new JButton("注册");
        signUpButton.setFont(font);
        signUpButton.setPreferredSize(dLabel);
        signUpButton.addActionListener(e -> new SignUpDialog());

        JButton loginButton = new JButton("登录");
        loginButton.setFont(font);
        loginButton.setPreferredSize(dLabel);
        loginButton.addActionListener(e -> {
            userData.setSelectUserName(userNameBox.getItemAt(userNameBox.getSelectedIndex()));
            if (UserAuthenticator.authenticate(String.valueOf(passwordTextField.getPassword()))) {
                new MainFrame();
                dispose();
            } else {
                setTitle("登录失败");
            }
        });

        passwordTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });

        add(userNameTip);
        add(userNameBox);
        add(passwordTip);
        add(passwordTextField);
        add(signUpButton);
        add(loginButton);
    }

    private void flushUserNames() {
        Set<String> userNameSet = userData.getUserNames();
        userNameBox.removeAllItems();
        for (String str : userNameSet) {
            userNameBox.addItem(str);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        flushUserNames();
    }
}
