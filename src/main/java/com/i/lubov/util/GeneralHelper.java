package com.i.lubov.util;

import com.google.common.collect.Lists;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class GeneralHelper {

    /**
     * String -> Any，如果 handler 为 null 则把字符串转换为 8 种基础数据类型、及其包装类、Date或String
     *
     * @param type : 目标类型的Class对象
     * @param v    : 要转换的字符串
     * @return
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("unchecked")
    public static <T> T str2Object(Class<T> type, String v) {
        Object param;
        if (type == String.class)
            param = safeTrimString(v);
        else if (type == int.class)
            param = str2Int_0(v);
        else if (type == long.class)
            param = str2Long_0(v);
        else if (type == byte.class)
            param = str2Byte_0(v);
        else if (type == char.class)
            param = str2Char_0(v);
        else if (type == float.class)
            param = str2Float_0(v);
        else if (type == double.class)
            param = str2Double_0(v);
        else if (type == short.class)
            param = str2Short_0(v);
        else if (type == boolean.class)
            param = str2Boolean_False(v);
        else if (type == Integer.class)
            param = str2Int(v);
        else if (type == Long.class)
            param = str2Long(v);
        else if (type == Byte.class)
            param = str2Byte(v);
        else if (type == Character.class)
            param = str2Char(v);
        else if (type == Float.class)
            param = str2Float(v);
        else if (type == Double.class)
            param = str2Double(v);
        else if (type == Short.class)
            param = str2Short(v);
        else if (type == BigDecimal.class)
            param = str2BigDecimal(v);
        else if (type == Boolean.class)
            param = str2Boolean(v);
        else if (Date.class.isAssignableFrom(type))
            param = str2Date(v);
        else if (List.class.isAssignableFrom(type))
            param = object2Array(v);
        else
            throw new IllegalArgumentException(String.format("object type '%s' not valid", type));
        return (T) param;
    }

    /**
     * 把参数 s 转换为安全字符串并执行去除前后空格：如果 s = null，则把它转换为空字符串
     *
     * @param s
     * @return
     */
    public static String safeTrimString(String s) {
        if (s == null) {
            return "";
        }
        return s.trim();
    }

    /**
     * String -> Integer，如果转换不成功则返回 null
     *
     * @param s
     * @return
     */
    public static Integer str2Int(String s) {
        Integer returnVal;
        try {
            returnVal = Integer.decode(safeTrimString(s));
        } catch (Exception e) {
            returnVal = null;
        }
        return returnVal;
    }

    /**
     * String -> int，如果转换不成功则返回默认值 d
     *
     * @param s
     * @param d
     * @return
     */
    public static int str2Int(String s, int d) {
        int returnVal;
        try {
            returnVal = Integer.parseInt(safeTrimString(s));
        } catch (Exception e) {
            returnVal = d;
        }
        return returnVal;
    }

    /**
     * String -> int，如果转换不成功则返回 0
     *
     * @param s
     * @return
     */
    public static int str2Int_0(String s) {
        return str2Int(s, 0);
    }

    /**
     * String -> short，如果转换不成功则返回 null
     *
     * @param s
     * @return
     */
    public static BigDecimal str2BigDecimal(String s) {
        Double doubleVal = str2Double(safeTrimString(s));
        if (doubleVal == null) {
            return null;
        }
        BigDecimal returnVal;
        try {
            returnVal = BigDecimal.valueOf(doubleVal);
        } catch (Exception e) {
            returnVal = null;
        }
        return returnVal;
    }

    /**
     * String -> Short，如果转换不成功则返回 null
     *
     * @param s
     * @return
     */
    public static Short str2Short(String s) {
        Short returnVal;
        try {
            returnVal = Short.decode(safeTrimString(s));
        } catch (Exception e) {
            returnVal = null;
        }
        return returnVal;
    }

    /**
     * String -> short，如果转换不成功则返回默认值 d
     *
     * @param s
     * @param d
     * @return
     */
    public static short str2Short(String s, short d) {
        short returnVal;
        try {
            returnVal = Short.parseShort(safeTrimString(s));
        } catch (Exception e) {
            returnVal = d;
        }
        return returnVal;
    }

    /**
     * String -> short，如果转换不成功则返回 0
     *
     * @param s
     * @return
     */
    public static short str2Short_0(String s) {
        return str2Short(s, (short) 0);
    }

    /**
     * String -> Long，如果转换不成功则返回 null
     *
     * @param s
     * @return
     */
    public static Long str2Long(String s) {
        Long returnVal;
        try {
            returnVal = Long.decode(safeTrimString(s));
        } catch (Exception e) {
            returnVal = null;
        }
        return returnVal;
    }

    /**
     * String -> long，如果转换不成功则返回默认值 d
     *
     * @param s
     * @param d
     * @return
     */
    public static long str2Long(String s, long d) {
        long returnVal;
        try {
            returnVal = Long.parseLong(safeTrimString(s));
        } catch (Exception e) {
            returnVal = d;
        }
        return returnVal;
    }

    /**
     * String -> long，如果转换不成功则返回 0
     *
     * @param s
     * @return
     */
    public static long str2Long_0(String s) {
        return str2Long(s, 0L);
    }

    /**
     * String -> Float，如果转换不成功则返回 null
     *
     * @param s
     * @return
     */
    public static Float str2Float(String s) {
        Float returnVal;
        try {
            returnVal = Float.valueOf(safeTrimString(s));
        } catch (Exception e) {
            returnVal = null;
        }
        return returnVal;
    }

    /**
     * String -> float，如果转换不成功则返回默认值 d
     *
     * @param s
     * @param d
     * @return
     */
    public static float str2Float(String s, float d) {
        float returnVal;
        try {
            returnVal = Float.parseFloat(safeTrimString(s));
        } catch (Exception e) {
            returnVal = d;
        }
        return returnVal;
    }

    /**
     * String -> float，如果转换不成功则返回 0
     *
     * @param s
     * @return
     */
    public static float str2Float_0(String s) {
        return str2Float(s, 0F);
    }

    /**
     * String -> Double，如果转换不成功则返回 null
     *
     * @param s
     */
    public static Double str2Double(String s) {
        Double returnVal;
        try {
            returnVal = Double.valueOf(safeTrimString(s));
        } catch (Exception e) {
            returnVal = null;
        }
        return returnVal;
    }

    /**
     * String -> double，如果转换不成功则返回默认值 d
     *
     * @param s
     * @param d
     * @return
     */
    public static double str2Double(String s, double d) {
        double returnVal;
        try {
            returnVal = Double.parseDouble(safeTrimString(s));
        } catch (Exception e) {
            returnVal = d;
        }
        return returnVal;
    }

    /**
     * String -> double，如果转换不成功则返回 0.0
     *
     * @param s
     * @return
     */
    public static double str2Double_0(String s) {
        return str2Double(s, 0D);
    }

    /**
     * String -> Byte，如果转换不成功则返回 null
     *
     * @param s
     * @return
     */
    public static Byte str2Byte(String s) {
        Byte returnVal;
        try {
            returnVal = Byte.decode(safeTrimString(s));
        } catch (Exception e) {
            returnVal = null;
        }
        return returnVal;
    }

    /**
     * String -> byte，如果转换不成功则返回默认值 d
     *
     * @param s
     * @param d
     * @return
     */
    public static byte str2Byte(String s, byte d) {
        byte returnVal;
        try {
            returnVal = Byte.parseByte(safeTrimString(s));
        } catch (Exception e) {
            returnVal = d;
        }
        return returnVal;
    }

    /**
     * String -> byte，如果转换不成功则返回 0
     *
     * @param s
     * @return
     */
    public static byte str2Byte_0(String s) {
        return str2Byte(s, (byte) 0);
    }

    /**
     * String -> Character，如果转换不成功则返回 null
     *
     * @param s
     * @return
     */
    public static Character str2Char(String s) {
        Character returnVal;
        try {
            returnVal = safeTrimString(s).charAt(0);
        } catch (Exception e) {
            returnVal = null;
        }
        return returnVal;
    }

    /**
     * String -> char，如果转换不成功则返回默认值 d
     *
     * @param s
     * @param d
     * @return
     */
    public static char str2Char(String s, char d) {
        char returnVal;
        try {
            returnVal = safeTrimString(s).charAt(0);
        } catch (Exception e) {
            returnVal = d;
        }
        return returnVal;
    }

    /**
     * String -> char，如果转换不成功则返回 0
     *
     * @param s
     * @return
     */
    public static char str2Char_0(String s) {
        return str2Char(s, Character.MIN_VALUE);
    }

    /**
     * String -> Boolean，如果转换不成功则返回 null
     *
     * @param s
     * @return
     */
    public static Boolean str2Boolean(String s) {
        return Boolean.valueOf(safeTrimString(s));
    }

    /**
     * String -> boolean，如果转换不成功则返回默认值 d
     *
     * @param s
     * @param d
     * @return
     */
    public static boolean str2Boolean(String s, boolean d) {
        s = safeTrimString(s);
        if (s.equalsIgnoreCase("true"))
            return true;
        else if (s.equalsIgnoreCase("false"))
            return false;
        return d;
    }

    /**
     * String -> boolean，如果转换不成功则返回 0
     *
     * @param s
     * @return
     */
    public static boolean str2Boolean_False(String s) {
        return str2Boolean(s, false);
    }

    /**
     * String -> java.util.Date， str 的格式由 format  定义
     *
     * @param str
     * @param format
     * @return
     */
    public static Date str2Date(String str, String format) {
        Date date = null;

        try {
            DateFormat df = new SimpleDateFormat(format);
            date = df.parse(safeTrimString(str));
        } catch (Exception e) {

        }

        return date;
    }

    /**
     * String -> java.util.Date，由函数自身判断 str 的格式
     *
     * @param str
     * @return
     */
    public static Date str2Date(String str) {
        Date date = null;

        try {
            char SEPARATOR = '-';
            String[] PATTERN = {"yyyy", "MM", "dd", "HH", "mm", "ss", "SSS"};
            String[] values = safeTrimString(str).split("\\D");
            String[] element = new String[values.length];

            int length = 0;
            for (String e : values) {
                e = e.trim();
                if (e.length() != 0) {
                    element[length++] = e;
                    if (length == PATTERN.length)
                        break;
                }
            }

            if (length > 0) {
                StringBuilder value = new StringBuilder();
                if (length > 1) {
                    for (int i = 0; i < length; ++i) {
                        value.append(element[i]);
                        value.append(SEPARATOR);
                    }
                } else {
                    String src = element[0];
                    int remain = src.length();
                    int pos = 0;
                    int i;

                    for (i = 0; remain > 0 && i < PATTERN.length; ++i) {
                        int p_length = PATTERN[i].length();
                        int v_length = Math.min(p_length, remain);
                        String v = src.substring(pos, pos + v_length);
                        pos += v_length;
                        remain -= v_length;

                        value.append(v);
                        value.append(SEPARATOR);
                    }

                    length = i;
                }

                StringBuilder format = new StringBuilder();

                for (int i = 0; i < length; ++i) {
                    format.append(PATTERN[i]);
                    format.append(SEPARATOR);
                }

                date = str2Date(value.toString(), format.toString());
            }
        } catch (Exception ignore) {
        }

        return date;
    }

    /**
     * Any -> Object[]
     *
     * @param obj
     * @return
     */
    public static Object[] object2Array(Object obj) {
        Object[] array;

        if (obj == null)
            array = new Object[]{obj};
        else if (obj.getClass().isArray()) {
            Class<?> clazz = obj.getClass().getComponentType();

            if (Object.class.isAssignableFrom(clazz))
                array = (Object[]) obj;
            else {
                int length = Array.getLength(obj);

                if (length > 0) {
                    array = new Object[length];

                    for (int i = 0; i < length; i++)
                        array[i] = Array.get(obj, i);
                } else
                    array = new Object[0];
            }
        } else if (obj instanceof Collection<?>)
            array = ((Collection<?>) obj).toArray();
        else if (obj instanceof Iterable<?>) {
            List<Object> list = Lists.newArrayList();
            Iterator<?> it = ((Iterable<?>) obj).iterator();

            while (it.hasNext())
                list.add(it.next());

            array = list.toArray();
        } else if (obj instanceof Iterator) {
            List<Object> list = Lists.newArrayList();
            Iterator<?> it = (Iterator<?>) obj;

            while (it.hasNext())
                list.add(it.next());

            array = list.toArray();
        } else if (obj instanceof Enumeration<?>) {
            List<Object> list = Lists.newArrayList();
            Enumeration<?> it = (Enumeration<?>) obj;

            while (it.hasMoreElements())
                list.add(it.nextElement());

            array = list.toArray();
        } else
            array = new Object[]{obj};

        return array;
    }
}