package ru.geracimov.otus.java.collection;

import java.util.*;
import java.util.function.UnaryOperator;

public final class DIYarrayList<T> implements List<T> {
    private final static float DEFAULT_FACTOR = 1.5f;
    private static final int DEFAULT_CAPACITY = 8;

    private transient Object[] elements;
    private int size;
    private float factor;

    public DIYarrayList() {
        this(DEFAULT_CAPACITY, DEFAULT_FACTOR);
    }

    public DIYarrayList(int size) {
        this(size, DEFAULT_FACTOR);
    }

    public DIYarrayList(int size, float increaseFactor) {
        if (size > 0) {
            this.elements = new Object[size];
            this.factor = increaseFactor;
        } else {
            throw new IllegalArgumentException("Size must be positive");
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public void add(int index, T element) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size));
        }
        if (elements.length == size) {
            increase(factor);
        }
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    @Override
    public boolean add(T element) {
        add(size, element);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T element : c) {
            this.add(element);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        final Object element = elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[size - 1] = null;
        size--;
        return (T) element;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index < 0) return false;
        remove(index);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sort(Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        elements = new Object[]{};
        size = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size));
        }
        return (T) elements[index];
    }

    @Override
    public T set(int index, T element) {
        return null;
    }


    @Override
    public int indexOf(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }
        for (int i = size - 1; i >= 0; i--) {
            if (o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }


    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "DIYarrayList{}";
        }
        StringBuilder sb = new StringBuilder("DIYarrayList{");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i != size - 1) {
                sb.append(", ");
            }
        }
        return sb.append("}").toString();
    }

    private void increase(float factor) {
        if (factor <= 1.0) {
            throw new IllegalArgumentException(String.format("Factor %f must be greater then 1.0", factor));
        }
        final float rawSize = Math.max(elements.length, DEFAULT_CAPACITY) * factor;
        int newSize = rawSize > Integer.MAX_VALUE ? Integer.MAX_VALUE : Math.round(rawSize);
        Object[] newElements = new Object[newSize];
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = newElements;
    }

}
