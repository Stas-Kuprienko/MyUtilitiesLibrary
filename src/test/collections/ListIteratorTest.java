package collections;

import org.junit.Test;
import stas.collections.SimpleList;

import java.util.ListIterator;

import static org.junit.Assert.*;

public class ListIteratorTest {

    @Test
    public void testListIteratorHasNext() {
        SimpleList<Integer> list = new SimpleList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListIterator<Integer> iterator = list.listIterator();

        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testListIteratorNext() {
        SimpleList<Integer> list = new SimpleList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListIterator<Integer> iterator = list.listIterator();

        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(3), iterator.next());
    }

    @Test
    public void testListIteratorHasPrevious() {
        SimpleList<Integer> list = new SimpleList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListIterator<Integer> iterator = list.listIterator();

        assertFalse(iterator.hasPrevious());
        iterator.next();
        assertTrue(iterator.hasPrevious());
        iterator.next();
        assertTrue(iterator.hasPrevious());
        iterator.next();
        assertTrue(iterator.hasPrevious());
        iterator.previous();
        assertTrue(iterator.hasPrevious());
        iterator.previous();
        assertTrue(iterator.hasPrevious());
        iterator.previous();
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testListIteratorPrevious() {
        SimpleList<Integer> list = new SimpleList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListIterator<Integer> iterator = list.listIterator();

        iterator.next();
        iterator.next();
        iterator.next();

        assertEquals(Integer.valueOf(3), iterator.previous());
        assertEquals(Integer.valueOf(2), iterator.previous());
        assertEquals(Integer.valueOf(1), iterator.previous());
    }

    @Test
    public void testListIteratorNextIndex() {
        SimpleList<Integer> list = new SimpleList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListIterator<Integer> iterator = list.listIterator();

        assertEquals(0, iterator.nextIndex());
        iterator.next();
        assertEquals(1, iterator.nextIndex());
        iterator.next();
        assertEquals(2, iterator.nextIndex());
        iterator.next();
        assertEquals(3, iterator.nextIndex());
    }

    @Test
    public void testListIteratorPreviousIndex() {
        SimpleList<Integer> list = new SimpleList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListIterator<Integer> iterator = list.listIterator();

        assertEquals(-1, iterator.previousIndex());
        iterator.next();
        assertEquals(0, iterator.previousIndex());
        iterator.next();
        assertEquals(1, iterator.previousIndex());
        iterator.next();
        assertEquals(2, iterator.previousIndex());
    }

    @Test
    public void testListIteratorSet() {
        SimpleList<Integer> list = new SimpleList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListIterator<Integer> iterator = list.listIterator();

        iterator.next();
        iterator.set(10);
        assertEquals(Integer.valueOf(10), list.get(0));
    }

    @Test
    public void testListIteratorAdd() {
        SimpleList<Integer> list = new SimpleList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListIterator<Integer> iterator = list.listIterator();

        iterator.next();
        iterator.add(10);
        assertEquals(Integer.valueOf(10), list.get(1));
        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Integer.valueOf(2), list.get(2));
        assertEquals(Integer.valueOf(3), list.get(3));
    }

    @Test
    public void testListIteratorRemove() {
        SimpleList<Integer> list = new SimpleList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListIterator<Integer> iterator = list.listIterator();

        iterator.next();
        iterator.remove();
        assertEquals(Integer.valueOf(2), list.get(0));
        assertEquals(Integer.valueOf(3), list.get(1));
    }
}