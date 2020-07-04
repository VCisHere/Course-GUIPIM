package guipim.frame;

import guipim.Data;
import guipim.dialog.NoteDialog;
import guipim.entity.PIMNote;
import guipim.user.UserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * 备忘窗口
 */
public class NoteFrame extends JFrame implements Observer {

    private static NoteFrame instance = null;
    private Data data;
    private Font font = new Font("黑体", Font.PLAIN, 18);
    private JTextArea noteTextArea;

    private JList<String> noteList;

    public NoteFrame() {
        if (instance != null) {
            dispose();
            instance.setVisible(true);
            return;
        }
        instance = this;
        data = Data.getInstance();
        setTitle("备忘");
        setSize(800, 600);
        setLayout(new GridLayout(1, 2));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        initNoteListPanel();
        initShowPanel();

        setVisible(true);
        data.addObserver(this);
    }

    private void initNoteListPanel() {
        JPanel noteListPanel = new JPanel();
        noteListPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;

        noteList = new JList<>();
        noteList.setFixedCellHeight(32);
        noteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        noteList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    noteTextArea.setText(noteList.getSelectedValue());
                }
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    int index = noteList.locationToIndex(e.getPoint());
                    noteList.setSelectedIndex(index);
                    JPopupMenu jpopupmenu = new JPopupMenu();
                    JMenuItem editItem = new JMenuItem("编辑");
                    editItem.addActionListener(e1 -> new NoteDialog("编辑备忘", index));
                    JMenuItem deleteItem = new JMenuItem("删除");
                    deleteItem.addActionListener(e1 -> data.deleteNoteForCache(index));
                    jpopupmenu.add(editItem);
                    jpopupmenu.add(deleteItem);
                    jpopupmenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        noteList.setFont(font);
        flushNoteList();

        JScrollPane jScrollPane = new JScrollPane(noteList);

        noteListPanel.add(jScrollPane, constraints);
        add(noteListPanel);
    }

    private void initShowPanel() {
        JPanel showPanel = new JPanel();
        showPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;

        noteTextArea = new JTextArea();
        noteTextArea.setFont(font);
        noteTextArea.setLineWrap(true);
        noteTextArea.setWrapStyleWord(true);

        JScrollPane jScrollPane = new JScrollPane(noteTextArea);
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        showPanel.add(jScrollPane, constraints);

        add(showPanel);
    }

    private void flushNoteList() {
        Vector<String> noteData = new Vector<>();
        Collection<PIMNote> collection =
                data.getData().getNotes(UserData.getInstance().getSelectUserName());
        for (PIMNote pimNote : collection) {
            noteData.add(pimNote.toString());
        }
        noteList.setListData(noteData);
    }

    @Override
    public void update(Observable o, Object arg) {
        flushNoteList();
        noteTextArea.setText("");
    }
}
