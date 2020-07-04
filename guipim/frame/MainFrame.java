package guipim.frame;

import guipim.Data;
import guipim.dialog.AppointmentDialog;
import guipim.dialog.TodoDialog;
import guipim.entity.PIMEntity;
import guipim.entity.PIMTodo;
import guipim.menu.PIMMenuFactory;
import guipim.user.UserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * 主窗口
 */
public class MainFrame extends JFrame implements Observer {

    private Font font_big = new Font("黑体", Font.PLAIN, 24);
    private Font font_mid = new Font("黑体", Font.PLAIN, 18);
    private Font font_small = new Font("黑体", Font.PLAIN, 14);

    private Dimension minDimension = new Dimension(400, 600);

    private int[] dayNumOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30 ,31 };
    private String[] columnNames = {"日", "一", "二", "三", "四", "五", "六"};

    private JLabel dateLabel;
    private JList<String> entityList;
    private JPanel calendarContentPanel;
    private JButton lastButton = null;
    private Data data;
    private int showYear;
    private int showMonth;

    @Override
    public Dimension getMinimumSize() {
        return minDimension;
    }

    public MainFrame() {
        data = Data.getInstance();
        showYear = data.getSelectedYear();
        showMonth = data.getSelectedMonth();
        setTitle("PIM - " + UserData.getInstance().getSelectUserName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setMinimumSize(getMinimumSize());
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        initMenu();
        initCalendarPanel();
        initEntityListPanel();

        data.addObserver(this);
        setVisible(true);
    }

    private void initMenu() {
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.setBackground(Color.WHITE);
        jMenuBar.add(PIMMenuFactory.createMenu("新建").getMenu());
        jMenuBar.add(PIMMenuFactory.createMenu("查看").getMenu());
        setJMenuBar(jMenuBar);
    }

    private void initCalendarPanel() {
        JPanel calendarPanel = new JPanel();
        calendarPanel.setLayout(new BoxLayout(calendarPanel, BoxLayout.Y_AXIS));

        JPanel calendarTitlePanel = new JPanel();
        calendarTitlePanel.setLayout(new FlowLayout());

        JButton upButton = new JButton("<");
        upButton.addActionListener(e -> {
            setLastMonth();
            flushDateLabel();
            flushCalendar();
        });
        upButton.setFocusable(false);
        upButton.setContentAreaFilled(false);
        upButton.setFont(font_mid);

        JButton downButton = new JButton(">");
        downButton.addActionListener(e -> {
            setNextMonth();
            flushDateLabel();
            flushCalendar();
        });
        downButton.setFocusable(false);
        downButton.setContentAreaFilled(false);
        downButton.setFont(font_mid);

        dateLabel = new JLabel();
        flushDateLabel();
        dateLabel.setHorizontalAlignment(JLabel.CENTER);
        dateLabel.setVerticalAlignment(JLabel.CENTER);
        dateLabel.setFont(font_big);

        JButton todayButton = new JButton("今天");
        todayButton.addActionListener(e -> {
            data.selectToday();
            showYear = data.getSelectedYear();
            showMonth = data.getSelectedMonth();
            flushDateLabel();
            flushCalendar();
            flushEntityList();
        });
        todayButton.setFocusable(false);
        todayButton.setContentAreaFilled(false);
        todayButton.setFont(font_mid);

        calendarTitlePanel.add(upButton);
        calendarTitlePanel.add(downButton);
        calendarTitlePanel.add(dateLabel);
        calendarTitlePanel.add(todayButton);

        calendarPanel.add(calendarTitlePanel);

        JPanel calendarContentPanelOut = new JPanel();
        calendarContentPanelOut.setLayout(new BorderLayout());

        calendarContentPanel = new JPanel();
        calendarContentPanel.setLayout(new GridLayout(7, 7));
        flushCalendar();

        calendarContentPanelOut.setMaximumSize(new Dimension(0x7FFFFFFF, calendarContentPanel.getHeight()));
        calendarContentPanelOut.add(calendarContentPanel, BorderLayout.NORTH);

        calendarPanel.add(calendarContentPanelOut);
        calendarPanel.setBackground(Color.orange);
        add(calendarPanel);
    }

    private void initEntityListPanel() {
        JPanel entityListPanel = new JPanel();

        entityList = new JList<>();
        entityList.setFixedCellHeight(32);
        entityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        entityList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int index = entityList.locationToIndex(e.getPoint());
                    if (index == -1) {
                        return;
                    }
                    entityList.setSelectedIndex(index);

                    JPopupMenu jpopupmenu = new JPopupMenu();
                    JMenuItem editItem = new JMenuItem("编辑");
                    editItem.setFont(font_small);
                    if (data.getDateEntityForCache(index) instanceof PIMTodo) {
                        editItem.addActionListener(e1 -> new TodoDialog("编辑待办事项", index));
                    } else {
                        editItem.addActionListener(e1 -> new AppointmentDialog("编辑约会事项", index));
                    }

                    JMenuItem deleteItem = new JMenuItem("删除");
                    deleteItem.setFont(font_small);
                    deleteItem.addActionListener(e1 -> data.deleteDateEntityForCache(index));
                    jpopupmenu.add(editItem);
                    jpopupmenu.add(deleteItem);
                    jpopupmenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        entityList.setFont(font_mid);
        flushEntityList();

        entityListPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        entityListPanel.add(entityList, c);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());

        JScrollPane jScrollPane = new JScrollPane(entityListPanel);
        jPanel.setBackground(Color.red);
        jPanel.add(jScrollPane, BorderLayout.CENTER);

        add(jPanel);
    }

    private void flushDateLabel() {
        String dateStr = showYear + "年"
                + (showMonth + 1) + "月";
        dateLabel.setText(dateStr);
    }

    private void flushEntityList() {
        Vector<String> entityData = new Vector<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, data.getSelectedYear());
        calendar.set(Calendar.MONTH, data.getSelectedMonth());
        calendar.set(Calendar.DAY_OF_MONTH, data.getSelectedDayOfMonth());
        Collection<PIMEntity> collection =  data.getData().getItemsForDate(UserData.getInstance().getSelectUserName(), calendar.getTime());
        for (PIMEntity entity : collection) {
            entityData.add(entity.toString());
        }
        entityList.setListData(entityData);
    }

    private void flushCalendar() {
        calendarContentPanel.removeAll();

        int year = showYear;
        int month = showMonth;

        int selectedYear = data.getSelectedYear();
        int selectedMonth = data.getSelectedMonth();
        int selectedDayOfMonth = data.getSelectedDayOfMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            dayNumOfMonth[1] = 29;
        } else {
            dayNumOfMonth[1] = 28;
        }
        for (int i = 0; i < 7; i++) {
            JButton button = new JButton(columnNames[i]);
            button.setFocusable(false);
            button.setFont(font_mid);
            button.setBackground(Color.lightGray);
            button.setVerticalAlignment(JLabel.CENTER);
            button.setHorizontalAlignment(JLabel.CENTER);
            calendarContentPanel.add(button);
        }
        for (int i = 0; i < index; i++) {
            JButton button = new JButton("");
            button.setFocusable(false);
            button.setFont(font_mid);
            button.setBackground(Color.lightGray);
            calendarContentPanel.add(button);
        }
        for (int i = 0; i < dayNumOfMonth[month]; i++) {
            JButton button = new JButton(String.valueOf(i + 1));
            button.setFocusable(false);
            button.setFont(font_mid);
            int finalI = i;
            button.addActionListener(e -> {
                if (lastButton != null) {
                    lastButton.setBackground(Color.lightGray);
                }
                button.setBackground(Color.GRAY);
                lastButton = button;
                data.setDate(year, month, finalI + 1);
                flushEntityList();
            });
            //Set color
            if (selectedMonth != month || i + 1 != selectedDayOfMonth || year != selectedYear) {
                button.setBackground(Color.lightGray);
            } else {
                lastButton = button;
                button.setBackground(Color.GRAY);
            }
            calendarContentPanel.add(button);
        }
        for (int i = dayNumOfMonth[month] + index; i < 42; i++) {
            JButton button = new JButton("");
            button.setFocusable(false);
            button.setFont(font_mid);
            button.setBackground(Color.lightGray);
            calendarContentPanel.add(button);
        }
        calendarContentPanel.updateUI();
    }

    public void setLastMonth() {
        if (showMonth > 0) {
            showMonth--;
        } else if (showYear > 1900) {
            showMonth = 11;
            showYear--;
        }
    }

    public void setNextMonth() {
        if (showMonth < 11) {
            showMonth++;
        } else {
            showMonth = 0;
            showYear++;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        flushEntityList();
    }
}
