package stas.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class UpdateFacility<T> {

    private T updatable;
    private Method setter;
    private Object previousValue;

    public UpdateFacility() {}

    public void init(T updatable, String fieldToUpdate) throws ReflectiveOperationException {
        this.updatable = updatable;
        Class<?> clas = updatable.getClass();
        Field field = clas.getDeclaredField(fieldToUpdate);
        Class<?> setParameter = field.getType().isPrimitive() ? field.getType() : String.class;
        this.setter = clas.getMethod(concatenateMethodName(fieldToUpdate, "set"), setParameter);
        Method getter = clas.getMethod(concatenateMethodName(fieldToUpdate, "get"));
        this.previousValue = getter.invoke(updatable);
    }

    public void setNewValue(Object newValue) throws ReflectiveOperationException {
        setter.invoke(updatable, newValue);
    }

    public void revert() throws ReflectiveOperationException {
        setter.invoke(updatable, previousValue);
    }

    public static String concatenateMethodName(String field, String methodType) {
        char firstLetter = (char) (field.charAt(0) - 32);
        StringBuilder str = new StringBuilder(field);
        str.setCharAt(0, firstLetter);
        str.insert(0, methodType, 0, methodType.length());
        return str.toString();
    }
}