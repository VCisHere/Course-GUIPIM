package guipim.entity;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class PIMCollectionTest {
    public static void main(String[] args) {
        PIMCollection pimCollection = new PIMCollection();
        PIMNote pimNote = new PIMNote();
        pimNote.setNote("note");

        PIMTodo pimTodo1 = new PIMTodo();
        pimTodo1.setDate(new Date());
        pimTodo1.setTodo("todo1");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        PIMTodo pimTodo2 = new PIMTodo();
        pimTodo2.setDate(calendar.getTime());
        pimTodo2.setTodo("todo2");

        PIMAppointment pimAppointment = new PIMAppointment();
        pimAppointment.setDate(new Date());
        pimAppointment.setDescription("desc");

        pimCollection.add(pimNote);
        pimCollection.add(pimTodo1);
        pimCollection.add(pimTodo2);
        pimCollection.add(pimAppointment);

        Collection<PIMNote> collectionNotes = pimCollection.getNotes("");
        for (PIMEntity pimEntity : collectionNotes) {
            System.out.println(((PIMNote) pimEntity).getNote());
        }

        System.out.println();

        Collection<PIMEntity> collectionTodos = pimCollection.getTodos("");
        for (PIMEntity pimEntity : collectionTodos) {
            System.out.println(((PIMTodo) pimEntity).getTodo());
        }

        System.out.println();

        Collection<PIMEntity> collection = pimCollection.getItemsForDate("", new Date());
        for (PIMEntity pimEntity : collection) {
            if (pimEntity instanceof PIMTodo) {
                System.out.println(((PIMTodo) pimEntity).getTodo());
            } else if (pimEntity instanceof PIMAppointment) {
                System.out.println(((PIMAppointment) pimEntity).getDescription());
            }
        }

    }
}
