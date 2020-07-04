package guipim.dialog;

import guipim.Data;
import guipim.entity.PIMTodo;
import guipim.user.UserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;

/**
 * 待办窗口
 */
public class TodoDialog extends JDialog {

    private int index;

    public TodoDialog(String title) {
        this(title, -1);
    }

    public TodoDialog(String title, int index) {
        this.index = index;
        setTitle(title);
        setModal(true);
        setResizable(false);
        setSize(400, 180);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        init();
        setVisible(true);
    }

    private void init() {
        Font font = new Font("黑体", Font.PLAIN, 20);
        Dimension dLabel = new Dimension(100, 36);
        Dimension dTextField = new Dimension(260, 36);
        Data data = Data.getInstance();

        JLabel priorityTip = new JLabel("优先级");
        priorityTip.setPreferredSize(dLabel);
        priorityTip.setFont(font);

        JLabel todoTextTip = new JLabel("待办：");
        todoTextTip.setPreferredSize(dLabel);
        todoTextTip.setFont(font);

        JLabel ownerTip = new JLabel();
        ownerTip.setPreferredSize(dLabel);
        ownerTip.setFont(font);

        JRadioButton isPublicButton = new JRadioButton("公开");
        isPublicButton.setPreferredSize(dLabel);
        isPublicButton.setFont(font);

        JComboBox<String> jComboBox = new JComboBox<>();
        jComboBox.setPreferredSize(dTextField);
        jComboBox.setFont(font);
        jComboBox.addItem("无");
        jComboBox.addItem("低");
        jComboBox.addItem("中");
        jComboBox.addItem("高");
        jComboBox.setFocusable(false);

        JTextField todoTextField = new JTextField();
        todoTextField.setFont(font);
        todoTextField.setPreferredSize(dTextField);

        JButton okButton = new JButton("确定");
        okButton.setFont(font);
        okButton.setPreferredSize(dLabel);

        okButton.addActionListener(e -> {
            if (index == -1) {
                //create
                PIMTodo pimTodo = new PIMTodo();
                pimTodo.setPriority(jComboBox.getSelectedIndex());
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, data.getSelectedYear());
                calendar.set(Calendar.MONTH, data.getSelectedMonth());
                calendar.set(Calendar.DAY_OF_MONTH, data.getSelectedDayOfMonth());
                pimTodo.setDate(calendar.getTime());
                pimTodo.setTodo(todoTextField.getText());
                pimTodo.setIsPublic(isPublicButton.isSelected());
                data.addEntity(pimTodo);
            } else {
                //edit
                data.setTodoForCache(index,
                        isPublicButton.isSelected(),
                        jComboBox.getSelectedIndex(),
                        todoTextField.getText());
            }

            getOwner().setEnabled(true);
            TodoDialog.this.dispose();
        });

        if (index != -1) {
            PIMTodo pimTodo = (PIMTodo) data.getDateEntityForCache(index);
            jComboBox.setSelectedIndex(pimTodo.getPriority());
            todoTextField.setText(pimTodo.getTodo());
            isPublicButton.setSelected(pimTodo.isPublic());
            setTitle("编辑 " + pimTodo.getOwner() + " 的待办事项");
            if (!pimTodo.getOwner().equals(UserData.getInstance().getSelectUserName())) {
                isPublicButton.setEnabled(false);
            }
        }

        todoTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    okButton.doClick();
                }
            }
        });

        add(priorityTip);
        add(jComboBox);
        add(todoTextTip);
        add(todoTextField);
        add(isPublicButton);
        add(okButton);
    }
}
