# breaking codingbat

codingbat works by sending your code to their server (via a POST request to https://codingbat.com/run) and then running it there.

obviously for security reasons they've blocked quite a few things (not sure if it's by scanning the source code or the bytecode)
but here are some things that are blocked:
- classes
- the `static` keyword
- exceptions:
  - the `try`, `catch`, `throw`, and `throws` keywords

the following java apis:
- `java.lang.System`
- `java.lang.Runtime`
- `java.lang.Process` and `java.lang.ProcessBuilder`
- `java.io` and `java.nio`
- `java.net`
- exception classes:
  - `java.lang.Throwable`
  - `java.lang.Exception`
  - `java.lang.Error`
  - `java.lang.RuntimeException`
- `java.util` subpackages:
  - `zip`
  - `jar`
  - `concurrent`
- reflection:
  - `java.lang.reflect`
  - `java.lang.invoke`
  - `java.lang.Class` (but not `java.lang.Enum`)
  - `java.lang.ClassLoader`
  - `java.lang.Compiler`
  - `java.lang.Package`
- `java.lang.Thread` and `java.lang.ThreadGroup`
- `java.lang.StackTraceElement`

and some workarounds:

`System.getProperty(prop)` -> `java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction(prop))`

other stuff:

- you can change the method signature (notably the return type)
  - this is useful since you can't print stuff out, so you can change the return type to `String` and return the value you want to print out
- it runs in a class called `Shell`
- anonymous classes aren't blocked but don't work anyways

### problem numbering
- usually in the format p[6 digit number]
  - https://codingbat.com/prob/p1 is the exception
- ranges:
  - p000000-p099999 is empty
  - p100000-p199999 is very sparse
  - p299999 and above are empty
    - with the exception of p375379 for some reason
