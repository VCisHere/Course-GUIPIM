package guipim.menu;

import guipim.dialog.AppointmentDialog;
import guipim.dialog.ContactDialog;
import guipim.dialog.NoteDialog;
import guipim.dialog.TodoDialog;

import javax.swing.*;

/**
 * “新建”菜单
 */
public class PIMCreateMenu extends PIMMenu {
    @Override
    public JMenu getMenu() {
        JMenu createMenu = new JMenu("新建");

        JMenuItem createTodoMenuItem = new JMenuItem("新建待办事项");
        createTodoMenuItem.addActionListener(e -> new TodoDialog("新建待办事项"));
        JMenuItem createAppointmentMenuItem = new JMenuItem("新建约会事项");
        createAppointmentMenuItem.addActionListener(e -> new AppointmentDialog("新建约会事项"));
        JMenuItem createNoteMenuItem = new JMenuItem("新建备忘");
        createNoteMenuItem.addActionListener(e -> new NoteDialog("新建备忘"));
        JMenuItem createContactMenuItem = new JMenuItem("新建联系人");
        createContactMenuItem.addActionListener(e -> new ContactDialog("新建联系人"));

        createMenu.add(createTodoMenuItem);
        createMenu.add(createAppointmentMenuItem);
        createMenu.add(createNoteMenuItem);
        createMenu.add(createContactMenuItem);

        return createMenu;
    }
}
