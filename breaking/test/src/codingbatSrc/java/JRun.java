import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

public class JRun {
   public static final boolean TEST_HANG = false;
   public static final boolean TEST_INTERNAL = false;
   public static final int SOCK_TRY_SECS = 30;
   public static final String WARMUP_CODE = "import java.util.*; import java.util.stream.Collectors; public class Shell {\npublic List<Integer> doubling(List<Integer> nums) {\nreturn nums.stream().map(n -> n * 2).collect(Collectors.toList());\n}\npublic String test0(){return(JSN.T(doubling((List)JSN.parse(\"[1, 2, 3, 4]\"))));}\npublic static final int casesN=1;\n}\n";
   public static final String LOOP_HANG_CODE = "import java.util.*; import java.util.stream.Collectors; public class Shell {\npublic int foo(int n) {\nwhile (n > 0) { n--; if (n!=0) n++; } return n; \n}\npublic String test0(){return(JSN.T( foo(1) )); }\npublic String test1(){return(JSN.T( foo(2) )); }\npublic static final int casesN=2;\n}\n";
   public static final String LOOP_HANG_CODE2 = "import java.util.*; import java.util.stream.Collectors; public class Shell {\npublic int foo(int n) {\nwhile (n > 0) { n--; if (n==10) n+= 10000; } return n; \n}\npublic String test0(){return(JSN.T( foo(1) )); }\npublic String test1(){return(JSN.T( foo(10000000) )); }\npublic static final int casesN=2;\n}\n";
   private static final Pattern pLocation = Pattern.compile("(\\s*location:)?(\\s+class)?\\s+Shell", Pattern.MULTILINE | Pattern.DOTALL);
   private static final Pattern pBadSymbol = Pattern.compile("(cannot find symbol)(\\s+symbol:\\s+(variable|method)\\s+)(\\w+)", Pattern.MULTILINE);
   public static final int PORT = 50000;
   public static final int PORT2 = 60000;
   public static final boolean VMTEST = false;
   public static final int SOCK_TIMEOUT = 3000;
   public static final int SOCK_TIMEOUT2 = 10000;
   public static final int SOCK_TRIES = 2;
   public static final int BASE_SLEEP = 500;
   public static final int SOCK_SLEEP = 2000;
   private static int delayDecade = 0;
   public static boolean poisoned = false;
   public static int counter = 0;
   public static int poisonLimit = -1;

   public static void hang() {
      try {
         Thread.sleep(100000000L);
      } catch (Exception var1) {
      }
   }

   public static String compileShell(String classname, CharSequence sourceCode, String workDir, String customDir) {
      String result = null;
      boolean internalError = true;
      StandardJavaFileManager fileManager = null;

      try {
         deleteFiles(workDir);
         String outName = workDir + "/" + classname + ".java";
         File outFile = new File(outName);
         OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outFile), "utf8");
         out.append(sourceCode);
         out.close();
         JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
         DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
         fileManager = compiler.getStandardFileManager(diagnostics, null, null);
         List<String> optionList = new ArrayList<>();
         if (customDir != null) {
            optionList.add("-classpath");
            optionList.add(withSlash(customDir));
         }

         internalError = false;
         Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(outFile));
         CompilationTask task = compiler.getTask(null, fileManager, diagnostics, optionList, null, compilationUnit);
         if (task.call()) {
            result = "";
         } else {
            List<Diagnostic<? extends JavaFileObject>> diags = diagnostics.getDiagnostics();
            result = "compile-error:";
            if (!diags.isEmpty()) {
               Diagnostic diag = diags.get(0);
               String locStr = "";
               long line = diag.getLineNumber();
               if (line != -1L) {
                  locStr = locStr + " line:" + (line - 1L);
               }

               result = result + diag.getMessage(null);
               Matcher m = pLocation.matcher(result);
               result = m.replaceAll("");
               Matcher m2 = pBadSymbol.matcher(result);
               if (m2.find()) {
                  result = m2.replaceFirst(m2.group(1) + " <b>" + m2.group(4) + "</b>");
               }

               result = result + locStr;
            }
         }
      } catch (Exception var30) {
         if (internalError) {
            result = "internal-error:" + var30.getMessage();
         } else {
            result = "compile-error:" + var30.getMessage();
         }
      } finally {
         if (fileManager != null) {
            try {
               fileManager.close();
            } catch (IOException var29) {
            }
         }
      }

      return result;
   }

   public static List<String> runShell(String workDir, String customDir, int timeout) {
      try {
         File myDest = new File(workDir);
         URL[] myDestUrls = new URL[]{myDest.toURL()};
         Object shell = null;

         try {
            shell = loadObjectClassic(myDestUrls, "Shell");
         } catch (Exception var7) {
            return Collections.singletonList("internal-error:classinit " + var7.toString());
         }

         List<String> lines = runTestsClassic(shell, timeout);
         lines.add(0, "run-ok");
         lines.add("end");
         return lines;
      } catch (Exception var8) {
         var8.printStackTrace();
         return Collections.singletonList("internal-error:runShell " + var8.toString());
      }
   }

   public static Object loadObjectClassic(URL[] urls, String classname) throws Exception {
      URLClassLoader cl = new URLClassLoader(urls);
      Class cls = cl.loadClass(classname);
      cl.close();
      return cls.newInstance();
   }

   public static List<String> runTestsClassic(final Object shell, int timeout) throws Exception {
      List<String> strings = new ArrayList<>(12);
      final Object[] holder = new Object[]{strings};
      long now = System.currentTimeMillis();
      Thread worker = new Thread() {
         @Override
         public void run() {
            try {
               Class c = shell.getClass();
               int casesN = 0;
               Field field = c.getField("casesN");
               if (field.getType() == int.class) {
                  casesN = field.getInt(null);
               }

               for (int i = 0; i < casesN; i++) {
                  Method meth;
                  try {
                     meth = c.getMethod("test" + i, null);
                  } catch (NoSuchMethodException var9) {
                     break;
                  }

                  String res = null;

                  try {
                     res = (String)meth.invoke(shell, null);
                  } catch (Exception var8) {
                     res = "error:" + JRun.exceptErrorMessage(var8);
                  }

                  List list = (List)holder[0];
                  if (list != null) {
                     list.add(res);
                  }

                  if (this.isInterrupted()) {
                     break;
                  }
               }
            } catch (Exception var10) {
               String eStr = var10.toString();
               System.err.println("runTests worker exception " + new Date().toGMTString() + " " + eStr);
            } catch (OutOfMemoryError var11) {
               System.err.println("runTests worker OutOutOfMemoryError " + new Date().toGMTString());
            }
         }
      };
      worker.setPriority(1);
      worker.setDaemon(true);
      worker.start();
      worker.join(timeout);
      if (worker.isAlive()) {
         holder[0] = null;
         worker.interrupt();
         Thread.sleep(2L);
         worker.stop();
      }

      return strings;
   }

   public static String withSlash(String path) {
      return !path.endsWith("/") ? path + "/" : path;
   }

   public static String testShell(CharSequence shellCode, String workDir, String customDir, boolean relaxTimeout) {
      workDir = withSlash(workDir);
      customDir = withSlash(customDir);
      File outDir = new File(workDir);
      if (!outDir.exists()) {
         outDir.mkdirs();
      }

      String compErr = compileShell("Shell", shellCode, workDir, customDir);
      if (compErr.length() > 0) {
         return compErr;
      } else {
         int timeout = 30;
         if (relaxTimeout) {
            timeout *= 40;
         }

         List<String> strings = runShell(workDir, customDir, timeout);
         String runError = "internal-error:init";
         if (strings.size() > 0) {
            runError = strings.get(0);
         }

         if (runError.startsWith("internal-error:")) {
            return runError;
         } else {
            StringBuilder result = new StringBuilder(1000);

            for (String s : strings) {
               result.append(s);
               result.append("\n");
            }

            return result.toString();
         }
      }
   }

   public static String exceptErrorMessage(Exception e) {
      String name = "Exception";
      String line = "";
      Throwable t = e.getCause();
      if (t != null) {
         name = t.toString();
         if (name.startsWith("java.lang.")) {
            name = name.substring(10);
         }

         StackTraceElement[] stack = t.getStackTrace();

         for (StackTraceElement ste : stack) {
            if ("Shell".equals(ste.getClassName())) {
               line = " (line:" + (ste.getLineNumber() - 1) + ")";
               break;
            }
         }
      }

      String result = name + line;
      return result.replace('\n', ' ');
   }

   public static String clientSendRetry(CharSequence code, boolean relax, Runnable failHandler) {
      int i = 0;

      while (true) {
         i++;
         int timeout = SOCK_TIMEOUT;
         if (i > 1) {
            timeout = SOCK_TIMEOUT2;
         }

         String result = clientSend(code, relax, timeout);
         if (!result.startsWith("internal-error")) {
            return result;
         }

         if (i == 1 && result.equals("internal-error:timeout-socket") && failHandler != null) {
            System.out.println("call failHandler " + i + " " + result + " " + new Date().toGMTString());
            failHandler.run();
         }

         if (i == 2) {
            System.out.println("fail " + i + " " + result + " " + new Date().toGMTString());
            return result;
         }

         System.out.println("retry " + i + " " + result + " " + new Date().toGMTString());
         int sleep = BASE_SLEEP;
         sleep += delayDecade * SOCK_SLEEP / 10;
         delayDecade = (delayDecade + 1) % 10;
         sleep(sleep);
      }
   }

   public static String clientSend(CharSequence code, boolean relax, int sockTimeout) {
      StringBuilder result = new StringBuilder(1000);
      Socket sock = null;

      String var29;
      try {
         StringBuilder request = new StringBuilder("java0");
         if (relax) {
            request.append(" relax");
         }

         request.append("\n");
         request.append(code);
         int port = PORT;
         String host = "127.0.0.1";
         sock = new Socket(host, port);
         sock.setSoTimeout(sockTimeout);
         OutputStreamWriter out = new OutputStreamWriter(sock.getOutputStream(), "utf8");
         out.append(request);
         out.flush();
         sock.shutdownOutput();
         InputStreamReader in = new InputStreamReader(sock.getInputStream(), "utf8");
         char[] buff = new char[1000];

         int len;
         while ((len = in.read(buff)) != -1) {
            result.append(buff, 0, len);
         }

         String str = result.toString();
         String err = null;
         if (str.length() == 0) {
            err = "internal-error:socket empty result";
         } else if (str.startsWith("run-ok") && !str.endsWith("\nend\n")) {
            err = "internal-error:socket missing end";
         }

         if (err == null) {
            return str;
         }

         var29 = err;
      } catch (SocketTimeoutException var26) {
         return "internal-error:timeout-socket";
      } catch (Exception var27) {
         return "internal-error:socket error " + var27.toString();
      } finally {
         if (sock != null) {
            try {
               sock.close();
            } catch (IOException var25) {
            }
         }
      }

      return var29;
   }

   public static void serverProcess(Socket sock, String workDir, String customDir) {
      try {
         InputStreamReader in = new InputStreamReader(sock.getInputStream(), "utf8");
         String res = "internal-error:server mystery error";
         StringBuilder sb = new StringBuilder();
         char[] buff = new char[1000];

         int len;
         while ((len = in.read(buff)) != -1) {
            sb.append(buff, 0, len);
         }

         String shellCode = sb.toString();
         String line0 = lineCSN(shellCode, 0).toString();
         if (line0.startsWith("java0")) {
            boolean relax = line0.contains("relax");
            shellCode = shellCode.substring(line0.length() + 1);
            res = testShell(shellCode, workDir, customDir, relax);
         }

         OutputStreamWriter out = new OutputStreamWriter(sock.getOutputStream(), "utf8");
         out.append(res);
         out.close();
      } catch (IOException var19) {
         System.err.println("IOException in serverProcess " + new Date().toGMTString() + " " + var19);
      } finally {
         try {
            sock.close();
         } catch (IOException var18) {
         }
      }
   }

   public static CharSequence lineCSN(CharSequence s, int n) {
      int lastI = -1;
      int lineN = 0;
      int len = s.length();

      for (int i = 0; i < len; i++) {
         char ch = s.charAt(i);
         if (ch == '\n') {
            if (lineN == n) {
               return s.subSequence(lastI + 1, i);
            }

            lastI = i;
            lineN++;
         }
      }

      return null;
   }

   public static void deleteFiles(String workDir) throws IOException {
      File dir = new File(workDir);

      File[] var5;
      for (File file : var5 = dir.listFiles()) {
         if (!file.isDirectory()) {
            file.delete();
         }
      }
   }

   public static ServerSocket obtainSocket(int port) throws IOException {
      int sleepMS = 200;
      int iter = 30 * (1000 / sleepMS);

      for (int i = 0; i < iter; i++) {
         try {
            return new ServerSocket(port);
         } catch (IOException var6) {
            try {
               Thread.sleep(sleepMS);
            } catch (InterruptedException var5) {
            }
         }
      }

      return new ServerSocket(port);
   }

   public static void runServer(String workDir, String customDir) {
      String mainDir = withSlash(workDir) + "main/";

      try {
         ServerSocket listener = obtainSocket(PORT);

         while (!poisoned) {
            Socket sock = listener.accept();
            String remoteIP = sock.getInetAddress().getHostAddress();
            if (!"127.0.0.1".equals(remoteIP) && !remoteIP.startsWith("10.0.")) {
               System.out.println("ignoring remote ip " + remoteIP);
               sock.close();
            } else {
               serverProcess(sock, mainDir, customDir);
            }
         }

         listener.close();
      } catch (Exception var6) {
         System.err.println("JRun exiting with exception " + new Date().toGMTString());
         System.err.println(var6 + " " + var6.getMessage());
      } catch (Error var7) {
         System.err.println("JRun exiting with error " + new Date().toGMTString());
         System.err.println(var7 + " " + var7.getMessage());
      }
   }

   public static void runWarmupThreaded(String workDir, final String customDir) {
      final String workDirTmp = withSlash(workDir) + "tmp/";
      Thread worker = new Thread() {
         @Override
         public void run() {
            String res = JRun.testShell(
                    WARMUP_CODE,
               workDirTmp,
               customDir,
               true
            );
            if (!res.startsWith("run-ok") || !res.endsWith("end\n")) {
               System.err.println("warmup error " + new Date().toGMTString());
               System.err.println(res);
            }
         }
      };
      worker.setDaemon(true);
      worker.start();
   }

   public static void runServerThreadPool(final String workDir, final String customDir, int threads) {
      try {
         ServerSocket listener = obtainSocket(PORT);
         ExecutorService executor = Executors.newFixedThreadPool(threads);

         while (!poisoned) {
            final Socket sock = listener.accept();
            String remoteIP = sock.getInetAddress().getHostAddress();
            if (!"127.0.0.1".equals(remoteIP) && !remoteIP.startsWith("10.0.")) {
               System.out.println("ignoring remote ip " + remoteIP);
               sock.close();
            } else {
               Runnable worker = new Runnable() {
                  @Override
                  public void run() {
                     long id = Thread.currentThread().getId();
                     JRun.serverProcess(sock, JRun.withSlash(workDir) + id, customDir);
                  }
               };

               try {
                  executor.execute(worker);
               } catch (RejectedExecutionException var9) {
                  worker.run();
               }
            }
         }

         listener.close();
      } catch (Exception var10) {
         System.err.println("JRun exiting with exception " + new Date().toGMTString());
         System.err.println(var10 + " " + var10.getMessage());
      }
   }

   public static void sleep(int ms) {
      try {
         Thread.sleep(ms);
      } catch (InterruptedException var2) {
      }
   }

   public static synchronized void addPoison() {
      counter++;
      if (poisonLimit != -1 && counter >= poisonLimit) {
         poisoned = true;
      }
   }

   // args: -t 2 /dev/shm/workdir/ /home/cb/cwrun/javacustom/
   public static void main(String[] args) throws IOException {
      System.setProperty("java.awt.headless", "true");
      List<String> a = new ArrayList<>(Arrays.asList(args));
      if ("-p".equals(a.get(0))) {
         poisonLimit = Integer.parseInt(a.get(1));
         a.remove(0);
         a.remove(0);
      }

      if (a.get(0).equals("-s")) {
         System.out.println("JRun start " + new Date().toGMTString());
         runWarmupThreaded(a.get(1), a.get(2));
         runServer(a.get(1), a.get(2));
      } else if (a.get(0).equals("-t")) {
         int threads = Integer.parseInt(a.get(1));
         System.out.println("JRun start threaded " + new Date().toGMTString());
         runWarmupThreaded(a.get(2), a.get(3));
         runServerThreadPool(a.get(2), a.get(3), threads);
      } else if (a.get(0).equals("-h")) {
         System.out.println("hello world!");
      } else {
         if (a.get(0).equals("-r")) {
            System.out
               .println(
                  testShell(
                          WARMUP_CODE,
                     a.get(1),
                     a.get(2),
                     true
                  )
               );
         }

         if (a.get(0).equals("-l")) {
            System.out
               .println(
                  testShell(
                     LOOP_HANG_CODE2,
                     a.get(1),
                     a.get(2),
                     false
                  )
               );

            try {
               Thread.sleep(60000L);
            } catch (Exception var3) {
            }
         }
      }
   }
}
