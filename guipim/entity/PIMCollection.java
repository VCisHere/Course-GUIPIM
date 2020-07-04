package guipim.entity;

import java.util.*;

/**
 * PIMEntity的集合类
 */
public class PIMCollection extends ArrayList<PIMEntity> {

    public static final int ALL = 0;
    public static final int TODO = 1;
    public static final int APPOINTMENT = 2;
    private int showType = ALL;

    private ArrayList<PIMContact> contactCacheList = new ArrayList<>();
    private ArrayList<PIMNote> noteCacheList = new ArrayList<>();
    private ArrayList<PIMEntity> dateEntityCacheList = new ArrayList<>();

    /**
     * 获取待办事项
     * @param owner 待办事项的所有者
     * @return 待办事项
     */
    public Collection<PIMEntity> getTodos(String owner) {
        ArrayList<PIMEntity> todos = new ArrayList<>();
        for (PIMEntity pimEntity : this) {
            if ((pimEntity.isPublic() || pimEntity.getOwner().equals(owner))
                    && pimEntity instanceof PIMTodo) {
                todos.add(pimEntity);
            }
        }
        return todos;
    }

    /**
     * 获取备忘
     * @param owner 备忘的所有者
     * @return 备忘
     */
    public Collection<PIMNote> getNotes(String owner) {
        noteCacheList.clear();
        for (PIMEntity pimEntity : this) {
            if ((pimEntity.isPublic() || pimEntity.getOwner().equals(owner))
                    && pimEntity instanceof PIMNote) {
                noteCacheList.add((PIMNote) pimEntity);
            }
        }
        noteCacheList.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
        return noteCacheList;
    }

    /**
     * 获取约会事项
     * @param owner 约会事项的所有者
     * @return 约会事项
     */
    public Collection<PIMEntity> getAppointments(String owner) {
        ArrayList<PIMEntity> notes = new ArrayList<>();
        for (PIMEntity pimEntity : this) {
            if ((pimEntity.isPublic() || pimEntity.getOwner().equals(owner))
                    && pimEntity instanceof PIMNote) {
                notes.add(pimEntity);
            }
        }
        return notes;
    }

    /**
     * 获取联系人
     * @param owner 联系人的所有者
     * @return 联系人
     */
    public Collection<PIMContact> getContacts(String owner) {
        contactCacheList.clear();
        for (PIMEntity pimEntity : this) {
            if ((pimEntity.isPublic() || pimEntity.getOwner().equals(owner))
                    && pimEntity instanceof PIMContact) {
                contactCacheList.add((PIMContact) pimEntity);
            }
        }
        contactCacheList.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
        return contactCacheList;
    }

    /**
     * 按照显示类型获取某日期的事项（待办或约会）
     * @param owner 事项的所有者
     * @param d 日期
     * @return 事项
     */
    public Collection<PIMEntity> getItemsForDate(String owner, Date d) {
        dateEntityCacheList.clear();
        for (PIMEntity pimEntity : this) {
            if ((!pimEntity.isPublic() && !pimEntity.getOwner().equals(owner))) {
                continue;
            }
            if ((showType == ALL || showType == TODO) && pimEntity instanceof PIMTodo) {
                if (compareDate(d, ((PIMTodo) pimEntity).getDate())) {
                    dateEntityCacheList.add(pimEntity);
                }
            } else if ((showType == ALL || showType == APPOINTMENT) && pimEntity instanceof PIMAppointment) {
                if (compareDate(d, ((PIMAppointment) pimEntity).getDate())) {
                    dateEntityCacheList.add(pimEntity);
                }
            }
        }
        dateEntityCacheList.sort((o1, o2) -> {
            int flag = o2.getPriority() - o1.getPriority();
            if (flag == 0) {
                if (o1 instanceof PIMTodo && o2 instanceof PIMAppointment) {
                    return -1;
                }
            }
            return flag;
        });
        return dateEntityCacheList;
    }

    /**
     * 获取备忘
     * @param index 索引
     * @return 备忘
     */
    public PIMNote getNoteForCache(int index) {
        if (noteCacheList.size() <= index) {
            return null;
        }
        return noteCacheList.get(index);
    }

    /**
     * 删除备忘
     * @param index 索引
     */
    public void deleteNoteForCache(int index) {
        if (noteCacheList.size() <= index) {
            return;
        }
        PIMEntity pimEntity = noteCacheList.get(index);
        remove(pimEntity);
        noteCacheList.remove(index);
    }

    /**
     * 获取待办事项或约会事项（带有日期的事项）
     * @param index 索引
     * @return 待办事项或约会事项（带有日期的事项）
     */
    public PIMEntity getDateEntityForCache(int index) {
        if (dateEntityCacheList.size() <= index) {
            return null;
        }
        return dateEntityCacheList.get(index);
    }

    /**
     * 删除待办事项或约会事项（带有日期的事项）
     * @param index 索引
     */
    public void deleteDateEntityForCache(int index) {
        if (dateEntityCacheList.size() <= index) {
            return;
        }
        PIMEntity pimEntity = dateEntityCacheList.get(index);
        remove(pimEntity);
        dateEntityCacheList.remove(index);
    }

    /**
     * 获取联系人
     * @param index 索引
     * @return 联系人
     */
    public PIMContact getContactEntityForCache(int index) {
        if (contactCacheList.size() <= index) {
            return null;
        }
        return contactCacheList.get(index);
    }

    /**
     * 删除联系人
     * @param index 索引
     */
    public void deleteContactEntityForCache(int index) {
        if (contactCacheList.size() <= index) {
            return;
        }
        PIMEntity pimEntity = contactCacheList.get(index);
        remove(pimEntity);
        contactCacheList.remove(index);
    }

    /**
     * 设置显示类别
     * @param showType 显示类别
     */
    public void setShowType(int showType) {
        this.showType = showType;
    }

    private boolean compareDate(Date dateA, Date dateB) {
        Calendar calendarA = Calendar.getInstance();
        calendarA.setTime(dateA);
        Calendar calendarB = Calendar.getInstance();
        calendarB.setTime(dateB);
        return (calendarA.get(Calendar.YEAR) == calendarB.get(Calendar.YEAR))
                && (calendarA.get(Calendar.MONTH) == calendarB.get(Calendar.MONTH))
                && (calendarA.get(Calendar.DAY_OF_MONTH) == calendarB.get(Calendar.DAY_OF_MONTH));
    }

}