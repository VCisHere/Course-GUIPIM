package guipim.dialog;

import guipim.Data;
import guipim.entity.PIMNote;
import guipim.user.UserData;

import javax.swing.*;
import java.awt.*;

/**
 * 备忘窗口
 */
public class NoteDialog extends JDialog {

    private int index;

    public NoteDialog(String title) {
        this(title, -1);
    }

    public NoteDialog(String title, int index) {
        this.index = index;
        setTitle(title);
        setModal(true);
        setResizable(false);
        setSize(500, 360);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        init();
        setVisible(true);
    }

    private void init() {
        Data data = Data.getInstance();
        Font font = new Font("黑体", Font.PLAIN, 20);
        Dimension dLabel = new Dimension(100, 36);
        Dimension dTextField = new Dimension(260, 36);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;

        JLabel priorityTip = new JLabel("优先级");
        priorityTip.setPreferredSize(dLabel);
        priorityTip.setFont(font);

        JComboBox<String> jComboBox = new JComboBox<>();
        jComboBox.setPreferredSize(dTextField);
        jComboBox.setFont(font);
        jComboBox.addItem("无");
        jComboBox.addItem("低");
        jComboBox.addItem("中");
        jComboBox.addItem("高");
        jComboBox.setFocusable(false);

        JTextArea noteTextArea = new JTextArea();
        noteTextArea.setFont(font);
        noteTextArea.setLineWrap(true);
        noteTextArea.setWrapStyleWord(true);

        JRadioButton isPublicButton = new JRadioButton("公开");
        isPublicButton.setPreferredSize(dLabel);
        isPublicButton.setFont(font);

        JButton okButton = new JButton("确定");
        okButton.setFont(font);
        okButton.setPreferredSize(dLabel);

        okButton.addActionListener(e -> {
            if (index == -1) {
                //create
                PIMNote pimNote = new PIMNote();
                pimNote.setPriority(jComboBox.getSelectedIndex());
                pimNote.setNote(noteTextArea.getText());
                data.addEntity(pimNote);
            } else {
                //edit
                data.setNoteForCache(index,
                        isPublicButton.isSelected(),
                        jComboBox.getSelectedIndex(),
                        noteTextArea.getText());
            }

            getOwner().setEnabled(true);
            NoteDialog.this.dispose();
        });

        if (index != -1) {
            PIMNote pimNote = data.getNoteForCache(index);
            jComboBox.setSelectedIndex(pimNote.getPriority());
            noteTextArea.setText(pimNote.getNote());
            isPublicButton.setSelected(pimNote.isPublic());
            setTitle("编辑 " + pimNote.getOwner() + " 的备忘");
            if (!pimNote.getOwner().equals(UserData.getInstance().getSelectUserName())) {
                isPublicButton.setEnabled(false);
            }
        }

        JScrollPane jScrollPane = new JScrollPane(noteTextArea);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        jPanel.add(jScrollPane, constraints);

        constraints.weightx = 0;
        constraints.weighty = 0;

        JPanel okPanel = new JPanel();
        okPanel.add(isPublicButton);
        okPanel.add(okButton);

        jPanel.add(okPanel, constraints);

        JPanel priorityPanel = new JPanel();
        priorityPanel.setLayout(new FlowLayout());
        priorityPanel.add(priorityTip);
        priorityPanel.add(jComboBox);

        add(priorityPanel);
        add(jPanel);
    }
}
