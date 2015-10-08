package feign.jaxrs;

import feign.MethodMetadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sskrla on 10/7/15.
 */
public class BeanParamTransformer implements MethodMetadata.NamingParameterTransformer {
  final String[] names;
  final Field[]  fields;
  final Method[] getters;

  public BeanParamTransformer(String[] names, Field[] fields, Method[] getters) {
    this.names = names;
    this.fields = fields;
    this.getters = getters;
  }

  @Override
  public Map<String, Object> transform(Object param) {
    try {
      Map<String, Object> mapped = new HashMap<String, Object>();
      for (int i = 0; i < names.length; i++) {
        mapped.put(names[i], fields[i] != null ? fields[i].get(param) : getters[i].invoke(param));
      }

      return mapped;

    } catch(Exception e) {
      throw new RuntimeException(e);
    }
  }
}
