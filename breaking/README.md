# breaking codingbat

codingbat works by sending your code to their server (via a POST request to https://codingbat.com/run) and then running it there.

obviously for security reasons they've blocked quite a few things, from scanning the source code.
here are some things that are blocked:
- the `class` keyword
  - enums and interfaces aren't blocked but seemingly only the `Shell` class is copied to the classpath... so external classes don't work
  - same goes for anonymous classes
- the `static` keyword
- exceptions:
  - the `try`, `catch`, `throw`, and `throws` keywords
- unicode escapes do work, but seemingly happen before the checker runs, so
  `\u0043lass.forName(...)` would still get blocked.

the following java apis:
- `java.lang.System`
- `java.lang.Runtime`
- `java.lang.Process` and `java.lang.ProcessBuilder`
- `java.io` and `java.nio`
- `java.net`
- `javax.*`
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
  - strings `getClass`, `forName`, `getDeclaredMethod`, `getDeclaredConstructor`, `loadClass`,
    `getClassLoader`
  - `java.lang.reflect`
  - `java.lang.invoke`
  - `java.lang.Class` (but not `java.lang.Enum`)
  - `java.lang.ClassLoader`
  - `java.lang.Compiler`
  - `java.lang.Package`
- `java.lang.Thread` and `java.lang.ThreadGroup`
- `java.lang.StackTraceElement`

interesting things that aren't blocked:
- `sun.misc.Unsafe`
- `sun.reflect.*`

and some workarounds:

`System.getProperty(prop)` -> `sun.security.action.GetPropertyAction.privilegedGetProperty(prop)`

other stuff:

- you can change the method signature (notably the return type)
  - this is useful since you can't print stuff out, so you can change the return type to `String` and return the value you want to print out
- it runs in a class called `Shell`
- java version is `1.8.0_452` (so jpms hasn't been invented yet)
- the classpath is set to `/home/cb/cwrun/javacustom/` (but since no files, we don't know what's in here)
- the java vendor is "Private Build" interestingly

### problem numbering
- usually in the format p[6 digit number]
  - https://codingbat.com/prob/p1 is the exception
- ranges:
  - p000000-p099999 is empty
  - p100000-p199999 is very sparse - contains all of the official problems plus these 6:
    - p101217
    - p109483
    - p126345
    - p141371
    - p156549
    - p196933
  - p299999 and above are empty
    - with the exception of p375379 for some reason
