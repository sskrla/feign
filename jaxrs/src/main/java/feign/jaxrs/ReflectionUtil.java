package feign.jaxrs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by sskrla on 10/7/15.
 */
public final class ReflectionUtil {
  private ReflectionUtil() { }

  /**
   * Returns all declared field, including those on superclasses, optionally setting accessible set to true.
   *
   * @param cls
   * @return
   */
  public static Field[] getAllDeclaredFields(Class<?> cls, boolean accessible) {
    List<Field> fields = new ArrayList<Field>();
    while(cls != null) {
      fields.addAll(Arrays.asList(cls.getDeclaredFields()));
      cls = cls.getSuperclass();
    }

    if(accessible)
      for(Field field: fields)
        field.setAccessible(true);

    return fields.toArray(new Field[fields.size()]);
  }
}
