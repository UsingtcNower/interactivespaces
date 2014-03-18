package interactivespaces.workbench;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import interactivespaces.SimpleInteractiveSpacesException;
import interactivespaces.util.data.json.JsonMapper;
import org.apache.commons.logging.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class ProjectConfigurator {

  /**
   * For converting json strings.
   */
  private static final JsonMapper MAPPER = new JsonMapper();

  /**
   * Pattern for parsing a configuration input line.
   */
  private static final Pattern INPUT_LINE_PATTERN = Pattern.compile("([^\\.]+)\\.([^=]+)=(.*)");

  /**
   * Group index for the container.
   */
  public static final int REGEXP_CONTAINER_GROUP = 1;

  /**
   * Group index for the property name.
   */
  public static final int REGEXP_NAME_GROUP = 2;

  /**
   * Group index for the property value.
   */
  public static final int REGEXP_VALUE_GROUP = 3;

  /**
   * Map of target protocol receptors.
   */
  private Map<String, Object> targetMap = Maps.newHashMap();

  /**
   * Logger to use.
   */
  private final Log log;

  /**
   * Create a new instance.
   *
   * @param log
   *          logger to use
   */
  public ProjectConfigurator(Log log) {
    this.log = log;
  }

  /**
   * Add a target object.
   *
   * @param targetId
   *          target id
   * @param target
   *          target object
   */
  public void addTarget(String targetId, Object target) {
    targetMap.put(targetId, target);
  }

  /**
   * Reflect input from a given file path.
   *
   * @param inputPath
   *          file path to process
   */
  public void readFromPath(String inputPath) {
    try {
      List<String> parameters = Files.readLines(new File(inputPath), Charset.defaultCharset());
      for (String line : parameters) {
        Matcher matcher = INPUT_LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
          throw new SimpleInteractiveSpacesException("Invalid property line syntax: " + line);
        }
        String propertyContainer = matcher.group(REGEXP_CONTAINER_GROUP);
        String propertyName = matcher.group(REGEXP_NAME_GROUP);
        String propertyValue = matcher.group(REGEXP_VALUE_GROUP);
        Object propertyObject = targetMap.get(propertyContainer);
        if (propertyContainer == null) {
          throw new SimpleInteractiveSpacesException("Could not find property container for " + propertyContainer);
        }
        setTargetProperty(propertyObject, propertyName, propertyValue);
      }
    } catch (Exception e) {
      throw new SimpleInteractiveSpacesException("Could not read/process spec file " + inputPath, e);
    }
  }

  /**
   * Set/add the dynamically specified project property. This will search the project class for a matching
   * setter or adder, and then set or add the property, accordingly.
   *
   * @param target
   *          the target object on which to set the property
   * @param name
   *          property name to set or add
   * @param value
   *          value to set
   */
  public void setTargetProperty(Object target, String name, String value) {
    try {
      String camlName = name.substring(0, 1).toUpperCase() + name.substring(1);
      Method setter = findMethod(target.getClass(), "set" + camlName);
      setter = setter != null ? setter : findMethod(target.getClass(), "add" + camlName);
      if (setter == null) {
        throw new SimpleInteractiveSpacesException("Matching set/add method not found");
      }
      Class<?> parameterType = setter.getParameterTypes()[0];
      Constructor<?> constructor = findStringConstructor(parameterType);
      Method converter = findMethod(parameterType, "parse" + parameterType.getSimpleName());

      if (parameterType.isAssignableFrom(String.class)) {
        // Case where there is a setter that takes a simple String parameter.
        setter.invoke(target, value);
      } else if (constructor != null) {
        // Case where there is a simple String constructor (like {@code File}).
        setter.invoke(target, constructor.newInstance(value));
      } else if (converter != null) {
        // Case where there is a converter function like Version.parseVersion(String).
        setter.invoke(target, converter.invoke(null, value));
      } else {
        // Case where there is a more complex type argument, and so construct a new instance using a builder pattern.
        Class<?> targetType = setter.getParameterTypes()[0];
        Object valueObject = buildFromJson(targetType, value);
        setter.invoke(target, valueObject);
      }
    } catch (Exception e) {
      throw new SimpleInteractiveSpacesException(
          String.format("Could not set/add property %s value %s", name, value), e);
    }
  }

  /**
   * Dynamically find a matching method.
   *
   *
   * @param targetClass
   *          the target class to query
   * @param methodName
   *          method name to look for
   *
   * @return method found, or {@code null}
   */
  Method findMethod(Class<?> targetClass, String methodName) {
    Method[] methods = targetClass.getMethods();
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1) {
          return method;
        }
      }
    }
    log.debug(String.format("Could not find method %s in class %s", methodName, targetClass.getName()));
    return null;
  }

  /**
   * Find a matching string constructor for the given type.
   *
   * @param targetType
   *          target class for which to find a constructor
   * @param <T>
   *          target type
   *
   * @return matching string constructor, or {@code null} if none
   */
  public <T> Constructor<T> findStringConstructor(Class<T> targetType) {
    try {
      return targetType.getConstructor(String.class);
    } catch (Exception e) {
      log.debug(String.format("Could not find string constructor for %s", targetType.getName()));
      return null;
    }
  }

  /**
   * Build a class of a given type from a json string.
   *
   * @param targetType
   *          type to build
   * @param json
   *          json input
   * @param <T>
   *          output type
   *
   * @return created object
   */
  public <T> T buildFromJson(Class<T> targetType, String json) {
    try {
      T targetObject = targetType.newInstance();
      Map<String, Object> fieldMap;
      try {
        fieldMap = MAPPER.parseObject(json);
      } catch (Exception e) {
        throw new SimpleInteractiveSpacesException("Parsing json: " + json, e);
      }
      for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
        String fieldName = entry.getKey();
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
          Method method = targetType.getMethod(methodName, entry.getValue().getClass());
          method.invoke(targetObject, entry.getValue());
        } catch (Exception e) {
          throw new SimpleInteractiveSpacesException("Could not find method " + methodName, e);
        }
      }
      return targetObject;
    } catch (Exception e) {
      throw new SimpleInteractiveSpacesException("Building from json for " + targetType.getSimpleName(), e);
    }
  }

}
