package guipim.menu;

import guipim.Data;
import guipim.entity.PIMCollection;
import guipim.frame.ContactFrame;
import guipim.frame.NoteFrame;

import javax.swing.*;

/**
 * “查看”菜单
 */
public class PIMShowMenu extends PIMMenu {
    @Override
    public JMenu getMenu() {
        JMenu showMenu = new JMenu("查看");

        Data data = Data.getInstance();

        JMenuItem showAllMenuItem = new JMenuItem("查看所有事项");
        showAllMenuItem.addActionListener(e -> data.setShowType(PIMCollection.ALL));
        JMenuItem showTodoMenuItem = new JMenuItem("查看待办事项");
        showTodoMenuItem.addActionListener(e -> data.setShowType(PIMCollection.TODO));
        JMenuItem showAppointmentMenuItem = new JMenuItem("新建约会事项");
        showAppointmentMenuItem.addActionListener(e -> data.setShowType(PIMCollection.APPOINTMENT));
        JMenuItem showNoteMenuItem = new JMenuItem("查看备忘");
        showNoteMenuItem.addActionListener(e -> new NoteFrame());
        JMenuItem showContactMenuItem = new JMenuItem("查看联系人");
        showContactMenuItem.addActionListener(e -> new ContactFrame());

        showMenu.add(showAllMenuItem);
        showMenu.add(showTodoMenuItem);
        showMenu.add(showAppointmentMenuItem);
        showMenu.add(showNoteMenuItem);
        showMenu.add(showContactMenuItem);

        return showMenu;
    }
}
