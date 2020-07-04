package guipim.dialog;

import guipim.Data;
import guipim.entity.PIMContact;
import guipim.user.UserData;

import javax.swing.*;
import java.awt.*;

/**
 * 联系人窗口
 */
public class ContactDialog extends JDialog {

    private int index;

    public ContactDialog(String title) {
        this(title, -1);
    }

    public ContactDialog(String title, int index) {
        this.index = index;
        setTitle(title);
        setModal(true);
        setResizable(false);
        setSize(400, 260);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        init();
        setVisible(true);
    }

    private void init() {
        Data data = Data.getInstance();
        Font font = new Font("黑体", Font.PLAIN, 20);
        Dimension dLabel = new Dimension(100, 36);
        Dimension dTextField = new Dimension(260, 36);

        JComboBox<String> jComboBox = new JComboBox<>();
        jComboBox.setPreferredSize(dTextField);
        jComboBox.setFont(font);
        jComboBox.addItem("无");
        jComboBox.addItem("低");
        jComboBox.addItem("中");
        jComboBox.addItem("高");
        jComboBox.setFocusable(false);

        JLabel priorityTip = new JLabel("优先级");
        priorityTip.setPreferredSize(dLabel);
        priorityTip.setFont(font);

        JLabel firstNameTip = new JLabel("名");
        firstNameTip.setPreferredSize(dLabel);
        firstNameTip.setFont(font);

        JLabel lastNameTip = new JLabel("姓");
        lastNameTip.setPreferredSize(dLabel);
        lastNameTip.setFont(font);

        JLabel emailTip = new JLabel("电子邮箱");
        emailTip.setPreferredSize(dLabel);
        emailTip.setFont(font);

        JTextField firstNameTextField = new JTextField();
        firstNameTextField.setFont(font);
        firstNameTextField.setPreferredSize(dTextField);

        JTextField lastNameTextField = new JTextField();
        lastNameTextField.setFont(font);
        lastNameTextField.setPreferredSize(dTextField);

        JTextField emailTextField = new JTextField();
        emailTextField.setFont(font);
        emailTextField.setPreferredSize(dTextField);

        JRadioButton isPublicButton = new JRadioButton("公开");
        isPublicButton.setPreferredSize(dLabel);
        isPublicButton.setFont(font);

        JButton okButton = new JButton("确定");
        okButton.setFont(font);
        okButton.setPreferredSize(dLabel);

        okButton.addActionListener(e -> {
            if (index == -1) {
                //create
                PIMContact pimContact = new PIMContact();
                pimContact.setPriority(jComboBox.getSelectedIndex());
                pimContact.setFirstName(firstNameTextField.getText());
                pimContact.setLastName(lastNameTextField.getText());
                pimContact.setEmailAddress(emailTextField.getText());
                data.addEntity(pimContact);
            } else {
                //edit
                data.setContactForCache(index,
                        isPublicButton.isSelected(),
                        jComboBox.getSelectedIndex(),
                        firstNameTextField.getText(),
                        lastNameTextField.getText(),
                        emailTextField.getText());
            }

            getOwner().setEnabled(true);
            ContactDialog.this.dispose();
        });

        if (index != -1) {
            PIMContact pimContact = data.getContactForCache(index);
            jComboBox.setSelectedIndex(pimContact.getPriority());
            firstNameTextField.setText(pimContact.getFirstName());
            lastNameTextField.setText(pimContact.getLastName());
            emailTextField.setText(pimContact.getEmailAddress());
            isPublicButton.setSelected(pimContact.isPublic());
            setTitle("编辑 " + pimContact.getOwner() + " 的联系人");
            if (!pimContact.getOwner().equals(UserData.getInstance().getSelectUserName())) {
                isPublicButton.setEnabled(false);
            }
        }

        add(priorityTip);
        add(jComboBox);
        add(firstNameTip);
        add(firstNameTextField);
        add(lastNameTip);
        add(lastNameTextField);
        add(emailTip);
        add(emailTextField);
        add(isPublicButton);
        add(okButton);
    }
}
