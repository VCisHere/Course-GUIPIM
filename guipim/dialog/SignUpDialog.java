package guipim.dialog;

import guipim.user.UserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * 注册窗口
 */
public class SignUpDialog extends JDialog {
    public SignUpDialog() {
        setTitle("注册");
        setModal(true);
        setResizable(false);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        init();

        setVisible(true);
    }

    private void init() {
        Font font = new Font("黑体", Font.PLAIN, 18);
        Dimension dLabel = new Dimension(100, 36);
        Dimension dTextField = new Dimension(260, 36);

        JLabel userNameTip = new JLabel("新用户名");
        userNameTip.setFont(font);
        userNameTip.setPreferredSize(dLabel);

        JLabel passwordTip = new JLabel("密码");
        passwordTip.setFont(font);
        passwordTip.setPreferredSize(dLabel);

        JLabel passwordEnsureTip = new JLabel("确认密码");
        passwordEnsureTip.setFont(font);
        passwordEnsureTip.setPreferredSize(dLabel);

        JTextField userNameTextField = new JTextField();
        userNameTextField.setFont(font);
        userNameTextField.setPreferredSize(dTextField);

        JPasswordField passwordTextField = new JPasswordField();
        passwordTextField.setFont(font);
        passwordTextField.setPreferredSize(dTextField);
        passwordTextField.setEchoChar('*');

        JPasswordField passwordEnsureTextField = new JPasswordField();
        passwordEnsureTextField.setFont(font);
        passwordEnsureTextField.setPreferredSize(dTextField);
        passwordEnsureTextField.setEchoChar('*');

        JLabel stateTip = new JLabel("");
        stateTip.setHorizontalAlignment(JLabel.CENTER);
        stateTip.setFont(font);
        stateTip.setPreferredSize(new Dimension(380, 36));

        JButton signUpButton = new JButton("注册");
        signUpButton.setFont(font);
        signUpButton.setPreferredSize(dLabel);
        signUpButton.addActionListener(e -> {
            String userName = userNameTextField.getText();
            char[] password = passwordTextField.getPassword();
            char[] passwordEnsure = passwordEnsureTextField.getPassword();
            if (userName.length() == 0 || userName.length() > 16) {
                stateTip.setText("用户名不能为空且长度不能超过16");
            } else if (password.length == 0 || password.length > 16) {
                stateTip.setText("密码不能为空且长度不能超过16");
            } else if (!Arrays.equals(password, passwordEnsure)) {
                stateTip.setText("两次输入的密码不一致");
            } else {
                UserData.getInstance().addUser(userName, String.valueOf(password));

                getOwner().setEnabled(true);
                SignUpDialog.this.dispose();
            }
        });

        passwordEnsureTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    signUpButton.doClick();
                }
            }
        });

        add(userNameTip);
        add(userNameTextField);
        add(passwordTip);
        add(passwordTextField);
        add(passwordEnsureTip);
        add(passwordEnsureTextField);
        add(stateTip);
        add(signUpButton);
    }
}
