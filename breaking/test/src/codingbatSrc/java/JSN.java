import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JSN {
   public static final Character LEFT = '[';
   public static final Character RIGHT = ']';
   public static final Character CLEFT = '{';
   public static final Character CRIGHT = '}';
   public static final Character COMMA = ',';
   public static final Character COLON = ':';
   public static final Object ERROR = "ERROR";

   public static Object parse(String str) {
      StringInput in = new StringInput(str);
      Object obj = parse(in, 0);
      return obj instanceof Character ? ERROR : obj;
   }

   public static Object parse(StringInput in, int depth) {
      if (depth >= 5) {
         return ERROR;
      } else {
         String s = in.s;

         while (in.i < s.length() && s.charAt(in.i) == ' ') {
            in.i++;
         }

         if (in.i == s.length()) {
            return ERROR;
         } else {
            char ch = s.charAt(in.i);
            if (ch == ',' || ch == ':' || ch == ']' || ch == '}') {
               in.i++;
               return ch;
            } else if (ch != '"' && ch != '\'') {
               if (ch == '-' || ch == '.' || ch >= '0' && ch <= '9') {
                  int j = in.i + 1;

                  boolean floatMode;
                  for (floatMode = ch == '.'; j < s.length(); j++) {
                     ch = s.charAt(j);
                     boolean digit = ch >= '0' && ch <= '9' || ch == '-' || ch == '+';
                     boolean floaty = ch == '.' || ch == 'e' || ch == 'E';
                     if (floaty) {
                        floatMode = true;
                     }

                     if (!digit && !floaty) {
                        break;
                     }
                  }

                  String word = s.substring(in.i, j);
                  in.i = j;
                  Object result = ERROR;

                  try {
                     if (floatMode) {
                        result = Double.parseDouble(word);
                     } else {
                        Long l = Long.parseLong(word);
                        if (l <= 2147483647L && l >= -2147483648L) {
                           result = l.intValue();
                        } else {
                           result = l;
                        }
                     }
                  } catch (NumberFormatException var9) {
                  }

                  return result;
               } else if (ch == '[') {
                  in.i++;
                  List result = new ArrayList();

                  Object b;
                  do {
                     Object a = parse(in, depth + 1);
                     if (a == ERROR) {
                        return ERROR;
                     }

                     if (RIGHT.equals(a)) {
                        return result;
                     }

                     if (a != null && a instanceof Character) {
                        return ERROR;
                     }

                     result.add(a);
                     b = parse(in, depth + 1);
                     if (b == ERROR) {
                        return ERROR;
                     }

                     if (RIGHT.equals(b)) {
                        return result;
                     }
                  } while (COMMA.equals(b));

                  return ERROR;
               } else if (ch == '{') {
                  in.i++;
                  Map result = new HashMap();

                  Object d;
                  do {
                     Object ax = parse(in, depth + 1);
                     if (ax == ERROR) {
                        return ERROR;
                     }

                     if (CRIGHT.equals(ax)) {
                        return result;
                     }

                     if (ax != null && ax instanceof Character) {
                        return ERROR;
                     }

                     Object b = parse(in, depth + 1);
                     Object c = parse(in, depth + 1);
                     d = parse(in, depth + 1);
                     if (b == ERROR || c == ERROR || d == ERROR) {
                        return ERROR;
                     }

                     if (!COLON.equals(b)) {
                        return ERROR;
                     }

                     if (c != null && c instanceof Character) {
                        return ERROR;
                     }

                     result.put(ax, c);
                     if (CRIGHT.equals(d)) {
                        return result;
                     }
                  } while (COMMA.equals(d));

                  return ERROR;
               } else {
                  if (Character.isLetter(ch)) {
                     int j = in.i + 1;

                     while (j < s.length() && Character.isLetter(s.charAt(j))) {
                        j++;
                     }

                     String word = s.substring(in.i, j);
                     in.i = j;
                     if ("false".equals(word) || "False".equals(word)) {
                        return Boolean.FALSE;
                     }

                     if ("true".equals(word) || "True".equals(word)) {
                        return Boolean.TRUE;
                     }

                     if ("null".equals(word) || "None".equals(word)) {
                        return null;
                     }
                  }

                  return ERROR;
               }
            } else {
               int j = in.i + 1;
               boolean slashMode = false;
               StringBuilder result = new StringBuilder();

               while (true) {
                  label299: {
                     if (j < s.length()) {
                        char now = s.charAt(j);
                        if (now == '\r' || now == '\n' || now == '\t') {
                           return ERROR;
                        }

                        if (slashMode) {
                           if (now != '"' && now != '\'' && now != '\\') {
                              return ERROR;
                           }

                           result.append(now);
                           slashMode = false;
                           break label299;
                        }

                        if (now != ch) {
                           if (now == '\\') {
                              slashMode = true;
                           } else {
                              result.append(now);
                           }
                           break label299;
                        }
                     }

                     if (j >= s.length()) {
                        return ERROR;
                     }

                     in.i = j + 1;
                     return result.toString();
                  }

                  j++;
               }
            }
         }
      }
   }

   public static String T(Object obj) {
      if (obj == null) {
         return "null";
      } else if (obj instanceof Integer || obj instanceof Boolean || obj instanceof Double || obj instanceof Long) {
         return String.valueOf(obj);
      } else if (obj instanceof String) {
         return T((String)obj);
      } else if (obj instanceof List) {
         return T((List)obj);
      } else if (obj instanceof Map) {
         return T((Map)obj);
      } else {
         return obj == ERROR ? (String)ERROR : null;
      }
   }

   public static String T(int num) {
      return String.valueOf(num);
   }

   public static String T(long num) {
      return String.valueOf(num);
   }

   public static String T(double num) {
      return String.valueOf(num);
   }

   public static String T(boolean b) {
      return String.valueOf(b);
   }

   public static String T(char c) {
      return T(String.valueOf(c));
   }

   public static String T(String string) {
      if (string == null) {
         return "null";
      } else {
         StringBuilder result = new StringBuilder();
         result.append("\"");

         for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (ch != '"' && ch != '\\') {
               result.append(ch);
            } else {
               result.append('\\');
               result.append(ch);
            }
         }

         result.append("\"");
         return result.toString();
      }
   }

   public static String TMarkup(String string, String expect, int lang) {
      boolean java = lang == 1;
      if (string == null) {
         return java ? "null" : "None";
      } else {
         StringBuilder result = new StringBuilder();
         String quot = "\"";
         if (!java) {
            quot = "'";
         }

         result.append(quot);
         boolean inspan = false;

         for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            char exp = i < expect.length() ? expect.charAt(i) : 0;
            if (ch != exp && !inspan) {
               result.append("<spo>");
               inspan = true;
            } else if (ch == exp && inspan) {
               result.append("</spo>");
               inspan = false;
            }

            if (ch == '"' || ch == '\\' || ch == '"') {
               result.append('\\');
               result.append(ch);
            } else if (ch == 0) {
               result.append("�");
            } else {
               result.append(ch);
            }
         }

         if (inspan) {
            result.append("</spo>");
         }

         if (string.length() < expect.length()) {
            result.append("<spo>");
            result.append(quot);
            result.append("</spo>");
         } else {
            result.append(quot);
         }

         return result.toString();
      }
   }

   public static String T(String[] strings) {
      if (strings == null) {
         return "null";
      } else {
         StringBuilder result = new StringBuilder();
         result.append("[");

         for (int i = 0; i < strings.length; i++) {
            result.append(T(strings[i]));
            if (i < strings.length - 1) {
               result.append(", ");
            }
         }

         result.append("]");
         return result.toString();
      }
   }

   public static String T(List objs) {
      if (objs == null) {
         return "null";
      } else {
         StringBuilder result = new StringBuilder();
         result.append("[");

         for (int i = 0; i < objs.size(); i++) {
            Object o = objs.get(i);
            result.append(T(o));
            if (i < objs.size() - 1) {
               result.append(", ");
            }
         }

         result.append("]");
         return result.toString();
      }
   }

   public static final boolean EQ(Object a, Object b) {
      return a == null && b == null || a != null && a.equals(b);
   }

   public static final String TLang(Object obj, int lang) {
      return lang == 1 ? T(obj) : TP(obj);
   }

   public static String TMarkup(List list, List expect, int lang) {
      boolean java = lang == 1;
      if (list == null) {
         return java ? "null" : "None";
      } else {
         StringBuilder result = new StringBuilder();
         result.append("[");
         boolean inspan = false;
         int esize = expect.size();

         for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            Object exp = i < esize ? expect.get(i) : ERROR;
            boolean eq = EQ(obj, exp);
            if (!eq && !inspan) {
               result.append("<spo>");
               inspan = true;
            } else if (eq && inspan) {
               result.append("</spo>");
               inspan = false;
            }

            result.append(TLang(obj, lang));
            if (i < list.size() - 1) {
               result.append(", ");
            }
         }

         if (inspan) {
            result.append("</spo>");
         }

         if (list.size() < expect.size()) {
            result.append("<spo>");
            result.append("]");
            result.append("</spo>");
         } else {
            result.append("]");
         }

         return result.toString();
      }
   }

   public static String T(Map map) {
      if (map == null) {
         return "null";
      } else {
         StringBuilder result = new StringBuilder();
         result.append("{");
         Iterator<Entry> it = map.entrySet().iterator();

         while (it.hasNext()) {
            Entry e = it.next();
            result.append(T(e.getKey()));
            result.append(": ");
            result.append(T(e.getValue()));
            if (it.hasNext()) {
               result.append(", ");
            }
         }

         result.append("}");
         return result.toString();
      }
   }

   public static String TMarkup(Map map, Map expect, int lang) {
      boolean java = lang == 1;
      if (map == null) {
         return java ? "null" : "None";
      } else {
         StringBuilder result = new StringBuilder();
         result.append("{");
         Iterator<Entry> it = map.entrySet().iterator();

         while (it.hasNext()) {
            Entry e = it.next();
            Object key = e.getKey();
            Object value = e.getValue();
            Object eValue = expect.get(e.getKey());
            boolean valueEq = EQ(value, eValue);
            boolean keyEq = expect.containsKey(key);
            if (!valueEq && !keyEq) {
               result.append("<spo>");
            }

            result.append(TLang(key, lang));
            result.append(": ");
            if (!valueEq && keyEq) {
               result.append("<spo>");
            }

            result.append(TLang(value, lang));
            if (!valueEq) {
               result.append("</spo>");
            }

            if (it.hasNext()) {
               result.append(", ");
            }
         }

         result.append("}");
         int count = 0;

         for (Entry ex : (Set<Entry>) expect.entrySet()) {
            if (!map.containsKey(ex.getKey())) {
               if (++count == 1) {
                  result.append(" <spo>missing: ");
               }

               result.append(TLang(ex.getKey(), lang));
               result.append(": ");
               result.append(TLang(ex.getValue(), lang));
            }
         }

         if (count > 0) {
            result.append("</spo>");
         }

         return result.toString();
      }
   }

   public static String TP(Object obj) {
      if (obj == null) {
         return "None";
      } else if (obj instanceof Integer || obj instanceof Long || obj instanceof Double) {
         return String.valueOf(obj);
      } else if (obj instanceof String) {
         return TP((String)obj);
      } else if (obj instanceof Boolean) {
         return TP(((Boolean)obj).booleanValue());
      } else if (obj instanceof List) {
         return TP((List)obj);
      } else if (obj instanceof Map) {
         return TP((Map)obj);
      } else {
         return obj == ERROR ? (String)ERROR : null;
      }
   }

   public static String TP(int num) {
      return String.valueOf(num);
   }

   public static String TP(long num) {
      return String.valueOf(num);
   }

   public static String TP(boolean b) {
      return b ? "True" : "False";
   }

   public static String TP(String string) {
      StringBuilder result = new StringBuilder();
      result.append("'");

      for (int i = 0; i < string.length(); i++) {
         char ch = string.charAt(i);
         if (ch != '\'' && ch != '\\') {
            result.append(ch);
         } else {
            result.append('\\');
            result.append(ch);
         }
      }

      result.append("'");
      return result.toString();
   }

   public static String TP(String[] strings) {
      StringBuilder result = new StringBuilder();
      result.append("[");

      for (int i = 0; i < strings.length; i++) {
         result.append(TP(strings[i]));
         if (i < strings.length - 1) {
            result.append(", ");
         }
      }

      result.append("]");
      return result.toString();
   }

   public static String TP(List objs) {
      StringBuilder result = new StringBuilder();
      result.append("[");

      for (int i = 0; i < objs.size(); i++) {
         Object o = objs.get(i);
         result.append(TP(o));
         if (i < objs.size() - 1) {
            result.append(", ");
         }
      }

      result.append("]");
      return result.toString();
   }

   public static String TP(Map map) {
      StringBuilder result = new StringBuilder();
      result.append("{");
      Iterator<Entry> it = map.entrySet().iterator();

      while (it.hasNext()) {
         Entry e = it.next();
         result.append(TP(e.getKey()));
         result.append(": ");
         result.append(TP(e.getValue()));
         if (it.hasNext()) {
            result.append(", ");
         }
      }

      result.append("}");
      return result.toString();
   }

   public static Map makeMap(Object[] objs) {
      return makeMap(Arrays.asList(objs));
   }

   public static Map makeMap(List objs) {
      Map map = new HashMap();

      for (int i = 0; i < objs.size(); i += 2) {
         map.put(objs.get(i), objs.get(i + 1));
      }

      return map;
   }

   public static void main(String[] args) throws IOException {
      System.setProperty("java.awt.headless", "true");
      System.out.println(T(parse("['a', 'b', 'c']")));
   }

   public static class StringInput {
      public String s;
      public int i;

      public StringInput(String s) {
         this.s = s;
         this.i = 0;
      }
   }
}
