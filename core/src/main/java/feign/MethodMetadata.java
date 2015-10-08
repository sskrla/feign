/*
 * Copyright 2013 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package feign;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;

import feign.Param.Expander;

import static feign.Util.checkArgument;
import static feign.Util.checkState;
import static java.lang.String.format;

public final class MethodMetadata implements Serializable {

  private static final long serialVersionUID = 1L;
  private String configKey;
  private transient Type returnType;
  private Integer urlIndex;
  private Integer bodyIndex;
  private transient Type bodyType;
  private RequestTemplate template = new RequestTemplate();
  private List<String> formParams = new ArrayList<String>();
  private Map<Integer, Collection<String>> indexToName =
      new LinkedHashMap<Integer, Collection<String>>();
  private Map<Integer, Class<? extends Expander>> indexToExpanderClass =
      new LinkedHashMap<Integer, Class<? extends Expander>>();

  private final ParameterMetadata[] parameterMetadata;

  MethodMetadata(int parameterCount) {
    parameterMetadata = new ParameterMetadata[parameterCount];
    for(int i=0; i<parameterCount; i++) {
      parameterMetadata[i] = new ParameterMetadata();
    }
  }

  /**
   * @see Feign#configKey(Class, java.lang.reflect.Method)
   */
  public String configKey() {
    return configKey;
  }

  MethodMetadata configKey(String configKey) {
    this.configKey = configKey;
    return this;
  }

  public Type returnType() {
    return returnType;
  }

  MethodMetadata returnType(Type returnType) {
    this.returnType = returnType;
    return this;
  }

  public Integer urlIndex() {
    return urlIndex;
  }

  MethodMetadata urlIndex(Integer urlIndex) {
    this.urlIndex = urlIndex;
    return this;
  }

  public Integer bodyIndex() {
    return bodyIndex;
  }

  MethodMetadata bodyIndex(Integer bodyIndex) {
    this.bodyIndex = bodyIndex;
    return this;
  }

  /**
   * Type corresponding to {@link #bodyIndex()}.
   */
  public Type bodyType() {
    return bodyType;
  }

  MethodMetadata bodyType(Type bodyType) {
    this.bodyType = bodyType;
    return this;
  }

  public RequestTemplate template() {
    return template;
  }

  public List<String> formParams() {
    return formParams;
  }

  public Map<Integer, Collection<String>> indexToName() {
    return indexToName;
  }

  public Map<Integer, Class<? extends Expander>> indexToExpanderClass() {
    return indexToExpanderClass;
  }

  public ParameterMetadata parameterMetadata(int index) {
    checkArgument(index < parameterMetadata.length, format("invalid parameter index %d", index));
    return parameterMetadata[index];
  }

  public interface ParameterTransformer {
    Object transform(Object param);
  }

  /**
   * In addition to transforming the value, implementers may produce multiple template parameters.
   */
  public interface NamingParameterTransformer extends ParameterTransformer {
    @Override
    Map<String, Object> transform(Object param);
  }

  public static class ParameterMetadata {
    private ParameterTransformer transformer;

    public ParameterMetadata transformer(ParameterTransformer transformer) {
      checkState(transformer != null, "transformer already set");
      this.transformer = transformer;
      return this;
    }

    public ParameterTransformer transformer() {
      return this.transformer;
    }
  }
}
