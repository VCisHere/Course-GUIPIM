package guipim.dialog;

import guipim.Data;
import guipim.entity.PIMAppointment;
import guipim.user.UserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;

/**
 * 约会窗口
 */
public class AppointmentDialog extends JDialog {

    private int index;

    public AppointmentDialog(String title) {
        this(title, -1);
    }

    public AppointmentDialog(String title, int index) {
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

        JLabel descriptionTextTip = new JLabel("约会：");
        descriptionTextTip.setPreferredSize(dLabel);
        descriptionTextTip.setFont(font);

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

        JTextField descriptionTextField = new JTextField();
        descriptionTextField.setFont(font);
        descriptionTextField.setPreferredSize(dTextField);

        JButton okButton = new JButton("确定");
        okButton.setFont(font);
        okButton.setPreferredSize(dLabel);

        okButton.addActionListener(e -> {
            if (index == -1) {
                //create
                PIMAppointment pimAppointment = new PIMAppointment();
                pimAppointment.setPriority(jComboBox.getSelectedIndex());
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, data.getSelectedYear());
                calendar.set(Calendar.MONTH, data.getSelectedMonth());
                calendar.set(Calendar.DAY_OF_MONTH, data.getSelectedDayOfMonth());
                pimAppointment.setDate(calendar.getTime());
                pimAppointment.setDescription(descriptionTextField.getText());
                pimAppointment.setIsPublic(isPublicButton.isSelected());
                data.addEntity(pimAppointment);
            } else {
                //edit
                data.setAppointmentForCache(index,
                        isPublicButton.isSelected(),
                        jComboBox.getSelectedIndex(),
                        descriptionTextField.getText());
            }

            getOwner().setEnabled(true);
            AppointmentDialog.this.dispose();
        });

        if (index != -1) {
            PIMAppointment pimAppointment = (PIMAppointment) data.getDateEntityForCache(index);
            jComboBox.setSelectedIndex(pimAppointment.getPriority());
            descriptionTextField.setText(pimAppointment.getDescription());
            isPublicButton.setSelected(pimAppointment.isPublic());
            setTitle("编辑 " + pimAppointment.getOwner() + " 的约会事项");
            if (!pimAppointment.getOwner().equals(UserData.getInstance().getSelectUserName())) {
                isPublicButton.setEnabled(false);
            }
        }

        descriptionTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    okButton.doClick();
                }
            }
        });

        add(priorityTip);
        add(jComboBox);
        add(descriptionTextTip);
        add(descriptionTextField);
        add(isPublicButton);
        add(okButton);
    }
}
