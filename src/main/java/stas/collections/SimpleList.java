package stas.collections;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * The data structure class. The dynamic resizable array. Implements {@link List} and {@link Serializable}.
 * It has the ability to {@linkplain SimpleList#searchElement(String, Object) search}
 *  by a sample string in the desired field specified by the getter method.
 * @param <E> The type of elements in this list.
 */
@SuppressWarnings("unused")
public class SimpleList<E> implements List<E>, Serializable {

    private E[] elements;

    private static final int DEFAULT_CAPACITY = 30;

    private int size;

    public SimpleList() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public SimpleList(int capacity) {
        elements = (E[]) new Object[capacity];
    }

    public SimpleList(E[] elements) {
        this.elements = elements;
        this.size = elements.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        E e = (E) o;
        return indexOf(e) >= 0;

    }

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index {@code i} such that
     * {@code Objects.equals(o, get(i))},
     * or -1 if there is no such index.
     */
    public int indexOf(Object element) {
        if (element != null && size > 0) {
            for (int i = 0; i < size; i++) {
                if (element.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Returns the element at the specified position in this list.
     * @param  index The index of the element to return.
     * @return The element at the specified position in this list.
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E get(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("The index " + index +
                    " is out of the array bounds - the size is " + size);
        }
        return elements[index];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E[] toArray() {
        if (size == 0) {
            return (E[]) new Object[0];
        }
        return Arrays.copyOf(elements, size);
    }

    @SuppressWarnings("all")
    @Override
    public <T> T[] toArray(T[] a) {
        if (size == 0) {
            return a;
        }
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        return a;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("The specified element is null.");
        }
        if (size == elements.length) {
            elements = grow(elements);
        }
        elements[size] = element;
        size += 1;
        return true;
    }

    /**
     * Add the specified element to the specified position of this list.
     * If the elements are present later the given position index,
     * the array moving, nothing is removing.
     *
     * @param index The wanted position for inserting an element.
     * @param e     Specified element to be added to this list.
     */
    public void add(int index, E e) {
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException("The index " + index +
                        " is out of the array bounds - the size is " + size);
        }
        if (e == null) {
            throw new NullPointerException("The specified element is null.");
        }
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) new Object[size - index];
        System.arraycopy(elements, index, arr, 0, arr.length);
        elements[index] = e;
        System.arraycopy(arr, 0, elements, index + 1, arr.length);
    }

    @Override
    public E set(int index, E e) {
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException("The index " + index +
                    " is out of the array bounds - the size is " + size);
        }
        if (e == null) {
            throw new NullPointerException("The specified element is null.");
        }
        E previous = elements[index];
        elements[index] = e;
        return previous;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException("The specified element is null.");
        }
        @SuppressWarnings("unchecked")
        E e = (E) o;
        for (int i = 0; i < size; i++)
            if (e.equals(elements[i])) {
                fastRemove(i);
                break;
            }
        return true;
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     *
     * @param index The index of the element to be removed.
     * @return True if it was successful.
     * @throws IndexOutOfBoundsException If the given index is out of this list bounds.
     */
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("The index " + index +
                    " is out of the array bounds - the size is " + size);
        }
        E e = elements[index];
        fastRemove(index);
        return e;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o != null && size > 0) {
            for (int i = size - 1; i >= 0 ; i--) {
                if (elements[i].equals(o)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new MyListIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index = " + index + ", size = " + size);
        }
        return new MyListIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (toIndex < fromIndex) {
            throw new IllegalArgumentException
                    (String.format("star of sublist (%s) bigger that end (%s).", fromIndex, toIndex));
        } if (fromIndex < 0 || fromIndex >= size || toIndex >= size) {
            throw new IndexOutOfBoundsException
                    (String.format("size = %s, arguments: from = %s, to = %s.", size,fromIndex, toIndex));
        }
        int length = toIndex - fromIndex;
        @SuppressWarnings("unchecked")
        E[] sub = (E[]) new Object[length];
        System.arraycopy(elements, fromIndex, sub, 0, length);
        return new SimpleList<>(sub);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("The specified object is null.");
        }
        for (Object o : c) {
            if (!(contains(o))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("The specified object is null.");
        } int cSize = c.size();
        if (size == 0) {
            elements = c.toArray(elements);
            size = cSize;
        }
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) new Object[cSize];
        arr = c.toArray(arr);
        if (elements.length == size) {
            grow(elements);
        }
        System.arraycopy(arr, 0, elements, size, cSize);
        size += cSize;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null || c.isEmpty()) {
            return false;
        } if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == size) {
            return addAll(c);
        }
        int cSize = c.size();
        @SuppressWarnings("unchecked")
        E[] rightHalf = (E[]) new Object[size - index];
        System.arraycopy(elements, index, rightHalf, 0, rightHalf.length);
        @SuppressWarnings("unchecked")
        E[] cToArray = (E[]) new Object[cSize];
        c.toArray(cToArray);
        System.arraycopy(cToArray, 0, elements, index, cSize);
        System.arraycopy(rightHalf, 0, elements, index + cSize, rightHalf.length);
        size += cSize;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("The specified object is null.");
        }
        for (int i = 0; i < size; i++) {
            E e = elements[i];
            if (c.contains(e)) {
                remove(e);
            }
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("The specified object is null.");
        }
        for (int i = 0; i < size; i++) {
            E e = elements[i];
            if (!(c.contains(e))) {
                remove(e);
                i--;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        size = 0;
        elements = (E[]) new Object[elements.length];
    }

    @Override
    public MyIterator iterator() {
        return new MyIterator();
    }

    /**
     * Search for an object in this {@link SimpleList} by specified field, using the string equaling with a given sample.
     * @param field Field of the searchable object for equaling.
     * @param sample A sample value to search the element.
     * @return  The {@link SimpleList list} of the relevant objects.
     * @throws NullPointerException If the given argument is null or empty.
     * @throws NoSuchElementException If something goes wrong.
     */
    @SuppressWarnings("unused")
    public SimpleList<E> searchElement(String field, Object sample) {
        String strSample = String.valueOf(sample);
        if (field == null || field.isEmpty()) {
            throw new NullPointerException("The field argument is null or empty.");
        }
        if (strSample == null || strSample.isEmpty()) {
            throw new NullPointerException("The sample argument is null or empty.");
        }
        MySearcher searcher = new MySearcher();
        searcher.search(field, strSample);
        if (searcher.relevant.isEmpty()) {
            throw new NoSuchElementException("The specified elements is not found.");
        }
        return searcher.relevant;
    }

    public Searcher<E> searcher() {
        return new MySearcher();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (size != ((SimpleList<?>) o).size) {
            return false;
        }
        for (Object e : (SimpleList<?>)o) {
            if (!(this.contains(e))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    private E[] grow(E[] arr) {
        @SuppressWarnings("unchecked")
        E[] result = (E[]) new Object[size << 1];
        System.arraycopy(arr, 0, result, 0, arr.length);
        return result;
    }

    private void fastRemove(int i) {
        size -= 1;
        elements[i] = null;
        if (size > i) {
            System.arraycopy(elements, i + 1, elements, i, size - i);
        }
    }


    private class MyIterator implements Iterator<E> {

        int n;

        int cursor;

        private MyIterator() {
            this.n = 0;
            this.cursor = -1;
        }

        @Override
        public boolean hasNext() {
            return (n < size);
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            cursor = n;
            n += 1;
            return elements[cursor];
        }
    }

    private class MyListIterator extends MyIterator implements ListIterator<E> {

        int p;

        private MyListIterator(int index) {
            super();
            this.n = index;
            this.p = index - 1;
        }

        @Override
        public E next() {
            E e = super.next();
            p += 1;
            return e;
        }

        @Override
        public boolean hasPrevious() {
            return (p >= 0);
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            if (p >= size) {
                throw new IndexOutOfBoundsException("index=" + n);
            }
            cursor = p;
            p -= 1;
            n -= 1;
            return elements[cursor];
        }

        @Override
        public int nextIndex() {
            return n;
        }

        @Override
        public int previousIndex() {
            return p;
        }

        @Override
        public void remove() {
            if (cursor < 0 || cursor >= size) {
                throw new IndexOutOfBoundsException("cursor = " + cursor);
            }
            fastRemove(cursor);
            n = cursor;
            p = n - 1;
        }

        @Override
        public void set(E e) {
            SimpleList.this.set(cursor, e);
        }

        @Override
        public void add(E e) {
            SimpleList.this.add(n, e);
        }
    }

    /**
     *  The class used by the {@linkplain SimpleList#searchElement(String, Object) search method}.
     */
    private class MySearcher implements Searcher<E> {

        private final SimpleList<E> relevant;

        private MySearcher() {
            this.relevant = new SimpleList<>();
        }

        private String getFieldValue(String field, E e) {
            try {
                Field fieldOfElement = e.getClass().getDeclaredField(field);
                fieldOfElement.setAccessible(true);
                Object fieldValue = fieldOfElement.get(e);
                fieldOfElement.setAccessible(false);
                return String.valueOf(fieldValue);
            } catch (IllegalAccessException | NoSuchFieldException exception) {
                return null;
            }
        }

        @Override
        public void search(String field, String sample) {
            if (sample == null) {
                throw new NullPointerException();
            }
            Arrays.stream(toArray()).forEach(e -> {
                if (sample.equalsIgnoreCase(getFieldValue(field, e))) {
                    relevant.add(e);
                }
            });
        }

        @Override
        public SimpleList<E> getRelevant() {
            return relevant;
        }
    }

    public interface Searcher<E> {

        void search(String field, String sample);

        SimpleList<E> getRelevant();
    }
}