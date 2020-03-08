package dev.niubi.commons.core.data.converter;

import org.springframework.core.GenericTypeResolver;

import java.util.Objects;

import javax.persistence.AttributeConverter;

/**
 * 自定义 hibernate 的枚举值转换,枚举需要实现 {@link PersistableEnum} 接口,并需要继承该 converter
 * 类<br/> <strong>示例:</strong>
 * <pre>
 * {@code
 * public enum Type implements PersistableEnum<Integer> {
 *     MALE(1),
 *     FEMALE(2);
 *     private final Integer value;
 *
 *     MenuType(int value) {
 *         this.value = value;
 *     }
 *
 *     public Integer getValue() {
 *         return value;
 *     }
 *
 *     public static class Converter extends PersistableEnumConverter<Type, Integer> {
 *
 *     }
 * }
 *
 * @Entity
 * public class ExampleEntity {
 *     @Convert(converter = Type.Converter.class)
 *     private Type type;
 * }
 * }
 * </pre>
 *
 * @author chenzhenjia
 * @since 2019-07-15
 */
public class PersistableEnumConverter<T extends Enum<T> & PersistableEnum<E>, E> implements AttributeConverter<T, E> {
    @Override
    public E convertToDatabaseColumn(T attribute) {
        return attribute != null ? attribute.getValue() : null;
    }


    @SuppressWarnings("unchecked")
    @Override
    public T convertToEntityAttribute(E dbData) {
        if (Objects.isNull(dbData)) return null;
        Class<?>[] classes = GenericTypeResolver.resolveTypeArguments(getClass(), PersistableEnumConverter.class);
        if (Objects.isNull(classes) || classes.length < 2) {
            return null;
        }
        Class<T> clazz = (Class<T>) classes[0];
        if (Objects.isNull(clazz)) {
            return null;
        }
        T[] enums = clazz.getEnumConstants();

        for (T e : enums) {
            if (e.getValue().equals(dbData)) {
                return e;
            }
        }
        return null;
    }
}
