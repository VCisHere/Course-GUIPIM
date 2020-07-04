package guipim.frame;

import guipim.Data;
import guipim.dialog.ContactDialog;
import guipim.entity.PIMContact;
import guipim.entity.PIMEntity;
import guipim.user.UserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * 联系人窗口
 */
public class ContactFrame extends JFrame implements Observer {

    private static ContactFrame instance = null;
    private Data data;
    private Font font = new Font("黑体", Font.PLAIN, 18);

    private JList<String> contactList;

    public ContactFrame() {
        if (instance != null) {
            dispose();
            instance.setVisible(true);
            return;
        }
        instance = this;
        data = Data.getInstance();
        setTitle("联系人");
        setSize(400, 600);
        setLayout(new GridLayout(1, 1));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        initContactListPanel();

        setVisible(true);
        data.addObserver(this);
    }

    private void initContactListPanel() {
        JPanel noteListPanel = new JPanel();
        noteListPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;

        contactList = new JList<>();
        contactList.setFixedCellHeight(32);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int index = contactList.locationToIndex(e.getPoint());
                    contactList.setSelectedIndex(contactList.locationToIndex(e.getPoint()));
                    JPopupMenu jpopupmenu = new JPopupMenu();
                    JMenuItem editItem = new JMenuItem("编辑");
                    editItem.addActionListener(e1 -> new ContactDialog("编辑联系人", index));
                    JMenuItem deleteItem = new JMenuItem("删除");
                    deleteItem.addActionListener(e1 -> data.deleteContactForCache(index));
                    jpopupmenu.add(editItem);
                    jpopupmenu.add(deleteItem);
                    jpopupmenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        contactList.setFont(font);
        flushContactList();

        JScrollPane jScrollPane = new JScrollPane(contactList);

        noteListPanel.add(jScrollPane, constraints);
        add(noteListPanel);
    }

    private void flushContactList() {
        Vector<String> contactData = new Vector<>();
        Collection<PIMContact> collection =
                data.getData().getContacts(UserData.getInstance().getSelectUserName());
        for (PIMEntity entity : collection) {
            contactData.add(entity.toString());
        }
        contactList.setListData(contactData);
    }

    @Override
    public void update(Observable o, Object arg) {
        flushContactList();
    }
}
