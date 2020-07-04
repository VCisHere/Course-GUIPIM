package guipim;

import guipim.entity.*;

import java.io.*;
import java.util.Calendar;
import java.util.Observable;

/**
 * PIMEntity的管理对象
 */
public class Data extends Observable {

    private static Data instance;
    private PIMCollection mainData;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDayOfMonth;

    private Data() {
        if (!readData()) {
            mainData = new PIMCollection();
            saveData();
        }
        selectToday();
        /*
        System.out.println("mainData:");
        for (PIMEntity entity : mainData) {
            System.out.println(entity.toString());
        }
         */
    }

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    /**
     * 保存所有PIMEntity对象到文件
     */
    private void saveData() {
        try {
            FileOutputStream fos = new FileOutputStream(new File("data"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mainData);
            oos.close();
            setChanged();
            notifyObservers();
        } catch (IOException ignored) {}
    }

    /**
     * 从文件中读取所有PIMEntity对象
     * @return 是否读取成功
     */
    private boolean readData() {
        try {
            FileInputStream fos = new FileInputStream(new File("data"));
            ObjectInputStream ois = new ObjectInputStream(fos);
            mainData = (PIMCollection) ois.readObject();
            ois.close();
            return true;
        } catch (IOException | ClassNotFoundException ignored) {
            return false;
        }
    }

    public PIMCollection getData() {
        return mainData;
    }

    /**
     * 选择日期为今天
     */
    public void selectToday() {
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 设置选择的日期
     * @param year 年
     * @param month 月
     * @param dayOfMonth 日
     */
    public void setDate(int year, int month, int dayOfMonth) {
        selectedYear = year;
        selectedMonth = month;
        selectedDayOfMonth = dayOfMonth;
    }

    /**
     * 获取选择的年
     * @return 年
     */
    public int getSelectedYear() {
        return selectedYear;
    }

    /**
     * 获取选择的月
     * @return 月
     */
    public int getSelectedMonth() {
        return selectedMonth;
    }

    /**
     * 获取选择的日
     * @return 日
     */
    public int getSelectedDayOfMonth() {
        return selectedDayOfMonth;
    }

    /**
     * 添加PIMEntity
     * @param entity 要添加的PIMEntity
     */
    public void addEntity(PIMEntity entity) {
        mainData.add(entity);
        saveData();
    }

    /**
     * 设置缓存中的PIMNote，同时会设置实际的PIMNote
     * @param index 索引
     * @param isPublic 是否公开
     * @param priority 优先级
     * @param noteStr 备忘内容
     */
    public void setNoteForCache(int index, boolean isPublic, int priority, String noteStr) {
        PIMNote pimNote = mainData.getNoteForCache(index);
        pimNote.setIsPublic(isPublic);
        pimNote.setPriority(priority);
        pimNote.setNote(noteStr);
        saveData();
    }

    /**
     * 获取缓存中的PIMNote
     * @param index 索引
     * @return 缓存中的PIMNote
     */
    public PIMNote getNoteForCache(int index) {
        return mainData.getNoteForCache(index);
    }

    /**
     * 删除缓存中的PIMNote，同时会删除实际的PIMNote
     * @param index 索引
     */
    public void deleteNoteForCache(int index) {
        mainData.deleteNoteForCache(index);
        saveData();
    }

    /**
     * 设置缓存中的PIMTodo，同时会设置实际的PIMTodo
     * @param index 索引
     * @param isPublic 是否公开
     * @param priority 优先级
     * @param todoStr 待办内容
     */
    public void setTodoForCache(int index, boolean isPublic, int priority, String todoStr) {
        PIMTodo pimTodo = (PIMTodo) mainData.getDateEntityForCache(index);
        if (pimTodo == null) {
            return;
        }
        pimTodo.setIsPublic(isPublic);
        pimTodo.setPriority(priority);
        pimTodo.setTodo(todoStr);
        saveData();
    }

    /**
     * 设置缓存中的PIMAppointment，同时会设置实际的PIMAppointment
     * @param index 索引
     * @param isPublic 是否公开
     * @param priority 优先级
     * @param descriptionStr 约会内容
     */
    public void setAppointmentForCache(int index, boolean isPublic, int priority, String descriptionStr) {
        PIMAppointment pimAppointment = (PIMAppointment) mainData.getDateEntityForCache(index);
        if (pimAppointment == null) {
            return;
        }
        pimAppointment.setIsPublic(isPublic);
        pimAppointment.setPriority(priority);
        pimAppointment.setDescription(descriptionStr);
        saveData();
    }

    /**
     * 获取缓存中的PIMTodo或PIMAppointment
     * @param index 索引
     * @return 缓存中的PIMTodo或PIMAppointment
     */
    public PIMEntity getDateEntityForCache(int index) {
        return mainData.getDateEntityForCache(index);
    }

    /**
     * 删除缓存中的PIMTodo或PIMAppointment，同时会删除实际的PIMTodo或PIMAppointment
     * @param index 索引
     */
    public void deleteDateEntityForCache(int index) {
        mainData.deleteDateEntityForCache(index);
        saveData();
    }

    /**
     * 设置缓存中的PIMContact，同时会设置实际的PIMContact
     * @param index 索引
     * @param isPublic 是否公开
     * @param priority 优先级
     * @param firstName 名
     * @param lastName 姓
     * @param email 电子邮箱
     */
    public void setContactForCache(int index,
                                   boolean isPublic,
                                   int priority,
                                   String firstName,
                                   String lastName,
                                   String email) {
        PIMContact pimContact = mainData.getContactEntityForCache(index);
        pimContact.setIsPublic(isPublic);
        pimContact.setPriority(priority);
        pimContact.setFirstName(firstName);
        pimContact.setLastName(lastName);
        pimContact.setEmailAddress(email);
        saveData();
    }

    /**
     * 获取缓存中的PIMContact
     * @param index 索引
     * @return 缓存中的联系人
     */
    public PIMContact getContactForCache(int index) {
        return mainData.getContactEntityForCache(index);
    }

    /**
     * 删除缓存中的PIMContact，同时会删除实际的PIMContact
     * @param index 索引
     */
    public void deleteContactForCache(int index) {
        mainData.deleteContactEntityForCache(index);
        saveData();
    }

    /**
     * 设置显示类别
     * @param type 显示类别
     */
    public void setShowType(int type) {
        mainData.setShowType(type);
        setChanged();
        notifyObservers();
    }

}
