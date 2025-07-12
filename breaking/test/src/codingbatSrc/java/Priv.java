import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

public class Priv {
   public static Object gLock = new Object();
   public static final int MAXOUTPUT = 0x100000;
   private static String lastCompile = "";

   public static StringBuilder fileToString(String filename) throws IOException {
      FileInputStream fis = new FileInputStream(filename);
      InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
      StringBuilder builder = new StringBuilder(10000);
      char[] buff = new char[10000];

      int len;
      while ((len = isr.read(buff)) > 0) {
         builder.append(buff, 0, len);
      }

      isr.close();
      return builder;
   }

   public static String fileToName(String fname) {
      int slash = fname.lastIndexOf('/');
      fname = fname.substring(slash + 1);
      if (fname.endsWith(".java")) {
         fname = fname.substring(0, fname.length() - 5);
         int dot = fname.indexOf('.');
         if (dot != -1) {
            fname = fname.substring(0, dot);
         }

         return fname;
      } else {
         throw new RuntimeException("Name should end with .java");
      }
   }

   public static String doCompile(String projDir, String sourceFilename, String sourceCode, String workDir) {
      String result = null;

      try {
         if (sourceCode.length() == 0) {
            sourceCode = fileToString(projDir + "/" + sourceFilename).toString();
         }

         String className = fileToName(sourceFilename);
         String outName = workDir + "/" + className + ".java";
         File outFile = new File(outName);
         OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
         out.append(sourceCode);
         out.close();
         JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
         synchronized (gLock) {
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
            List<String> optionList = new ArrayList<>();
            optionList.add("-classpath");
            optionList.add(System.getProperty("java.class.path") + ":/Users/nick/cw/cwrun/javacustom");
            Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(outFile));
            CompilationTask task = compiler.getTask(null, fileManager, diagnostics, optionList, null, compilationUnit);
            if (task.call()) {
               result = "";
            } else {
               for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                  result = result + "Compile error on line:" + diagnostic.getLineNumber() + "\n";
               }
            }

            fileManager.close();
         }
      } catch (Exception var19) {
         result = var19.getMessage();
         var19.printStackTrace();
      }

      return result;
   }

   public static int runCommandSimple(String[] cmds, ByteArrayOutputStream out) {
      String cmd = "";

      for (String c : cmds) {
         cmd = cmd + c;
         cmd = cmd + " ";
      }

      int exitCode = -1;
      Process proc = null;

      try {
         ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd);
         pb.redirectErrorStream(true);
         proc = pb.start();
         InputStream in = proc.getInputStream();
         byte[] buff = new byte[1024];

         while (true) {
            int bytesRead = in.read(buff);
            if (bytesRead == -1) {
               exitCode = proc.waitFor();
               break;
            }

            out.write(buff, 0, bytesRead);
         }
      } catch (Exception var9) {
         System.out.println("low level run except:" + var9);
         if (proc != null) {
            proc.destroy();
         }

         exitCode = -1;
      }

      return exitCode;
   }

   public static int runCommand(Job job, String[] cmds, ByteArrayOutputStream out) {
      return runCommand(job, cmds, 2000, out);
   }

   public static int runCommand(final Job job, final String[] cmds, int timeoutMs, final ByteArrayOutputStream output) {
      final int[] holder = new int[]{-10};
      int localExit = 0;

      try {
         Thread worker = new Thread() {
            @Override
            public void run() {
               holder[0] = Priv.lowlevelRun(job, cmds, output);
            }
         };
         worker.start();
         worker.join(timeoutMs);
         if (worker.isAlive()) {
            stopCommand(job);
            localExit = -11;
         }
      } catch (InterruptedException var7) {
         stopCommand(job);
         localExit = -12;
      }

      return localExit != 0 ? localExit : holder[0];
   }

   public static int lowlevelRun(Job job, String[] cmds, ByteArrayOutputStream out) {
      job.execProcess = null;

      int exitCode;
      try {
         ProcessBuilder pb = new ProcessBuilder(cmds);
         pb.redirectErrorStream(true);
         job.execProcess = pb.start();
         InputStream in = job.execProcess.getInputStream();
         byte[] buff = new byte[1024];

         while (true) {
            int bytesRead = in.read(buff);
            if (bytesRead == -1) {
               break;
            }

            out.write(buff, 0, bytesRead);
            if (out.size() > MAXOUTPUT) {
               job.execProcess.destroy();
               break;
            }
         }

         exitCode = job.execProcess.waitFor();
      } catch (Exception var8) {
         if (job.execProcess != null) {
            job.execProcess.destroy();
         }

         exitCode = -1;
      }

      job.execProcess = null;
      return exitCode;
   }

   public static void stopCommand(Job job) {
      if (job.execProcess != null) {
         job.execProcess.destroy();
      }
   }

   public static String wordCountOrig(String fname) throws IOException {
      BufferedReader in = new BufferedReader(new FileReader(fname));
      Map<String, Integer> map = new HashMap<>();

      String line;
      while ((line = in.readLine()) != null) {
         if (line.length() != 0) {
            String[] words = line.split("\\s+");

            for (String s : words) {
               if (map.containsKey(s)) {
                  map.put(s, 1);
               } else {
                  map.put(s, map.get(s) + 1);
               }
            }
         }
      }

      List<String> keys = new ArrayList<>(map.keySet());
      Collections.sort(keys);
      String result = "";

      for (String key : keys) {
         result = result + key + ": " + map.get(key) + "\n";
      }

      return result;
   }

   public static String norm(String s) {
      s = s.toLowerCase();
      int a = 0;

      while (a < s.length() && !Character.isLetter(s.charAt(a))) {
         a++;
      }

      int b = s.length() - 1;

      while (b >= a && !Character.isLetter(s.charAt(b))) {
         b--;
      }

      return s.substring(a, b + 1);
   }

   public static String wordCount(String fname) throws IOException {
      BufferedReader in = new BufferedReader(new FileReader(fname));
      Map<String, Integer> map = new HashMap<>();

      String line;
      while ((line = in.readLine()) != null) {
         if (line.length() != 0) {
            String[] words = line.split("\\s+");

            for (String s : words) {
               s = norm(s);
               if (s.length() != 0) {
                  if (!map.containsKey(s)) {
                     map.put(s, 1);
                  } else {
                     map.put(s, map.get(s) + 1);
                  }
               }
            }
         }
      }

      List<String> keys = new ArrayList<>(map.keySet());
      Collections.sort(keys);
      String result = "";

      for (String key : keys) {
         result = result + key + ": " + map.get(key) + "\n";
      }

      return result;
   }

   public void imageMain(String[] args) throws IOException {
      SimpleImage image = new SimpleImage(args[0]);
      System.out.println(image);
   }

   public void imagePuzzle(String[] args) throws IOException {
      System.setProperty("java.awt.headless", "true");
      SimpleImage image = new SimpleImage(args[0]);

      for (int y = 0; y < image.getHeight(); y++) {
         for (int x = 0; x < image.getWidth(); x++) {
            image.setRed(x, y, 0);
            image.setGreen(x, y, image.getGreen(x, y) * 10);
            image.setBlue(x, y, image.getBlue(x, y) * 10);
         }
      }

      System.out.println(image);
   }

   public static String readFile(String fname) throws IOException {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      FileInputStream in = new FileInputStream(new File(fname));
      byte[] buff = new byte[1000];

      while (true) {
         int bytesRead = in.read(buff);
         if (bytesRead == -1) {
            return out.toString("UTF-8");
         }

         out.write(buff, 0, bytesRead);
      }
   }

   public static CharSequence runJava(Project proj, String srcname, String contents, List<String> args, String workDir) {
      StringBuilder out = new StringBuilder();
      String err = doCompile(proj.projectDir, srcname, contents, workDir);
      if (!err.equals("")) {
         out.append("error c2\n");
         out.append(err);
         return out;
      } else {
         out.append("ok\n");
         String classname = fileToName(srcname);
         List<String> var12 = new ArrayList<>(args);

         for (int i = 0; i < var12.size(); i++) {
            if (!var12.get(i).startsWith("-")) {
               var12.set(i, proj.dataFile(var12.get(i)));
            }
         }

         var12.add(0, "java");
         var12.add(1, "-cp");
         var12.add(2, workDir + ":" + "/Users/nick/cw/cwrun/javacustom");
         var12.add(3, classname);
         String[] cmds = var12.toArray(new String[0]);
         Job job = new Job();
         ByteArrayOutputStream procout = new ByteArrayOutputStream();
         int status = runCommand(job, cmds, procout);
         out.append(String.valueOf(status) + "\n");
         out.append(procout.toString());
         return out;
      }
   }

   public static CharSequence runPython(Project proj, String srcname, String contents, List<String> args, String workDir, Case c) {
      StringBuilder out = new StringBuilder();
      String soutName = workDir + "/" + srcname;
      File soutFile = new File(soutName);

      try {
         OutputStreamWriter sout = new OutputStreamWriter(new FileOutputStream(soutFile), "UTF-8");
         sout.append(contents);
         sout.close();
         out.append("ok\n");
      } catch (Exception var13) {
         out.append("error py source write\n");
      }

      List<String> var14 = new ArrayList<>(args);

      for (int i = 0; i < var14.size(); i++) {
         if (((String)var14.get(i)).endsWith(".png") || ((String)var14.get(i)).endsWith(".jpeg") || ((String)var14.get(i)).endsWith(".txt")) {
            var14.set(i, proj.dataFile((String)var14.get(i)));
         }
      }

      if (c.mode.equals("--g2") && proj.attrs.containsKey("binky")) {
         System.out.println("binky run");
         var14.add("|");
         var14.add("/usr/local/bin/gifsicle");
         var14.add("--multifile");
         var14.add("-d");
         var14.add("5");
         var14.add("-");
         var14.add("|");
         var14.add("/usr/bin/base64");
      } else if (c.mode.equals("--g2")) {
         String arg = c.fileName();
         String gpath = proj.dataFile(gFilename(arg));
         String threshold = proj.getAttr("threshold");
         if (threshold == null) {
            threshold = "10";
         }

         var14.add("|");
         var14.add("/Users/nick/cw/priv/diffy.py");
         var14.add("-t");
         var14.add(threshold);
         var14.add("-a");
         var14.add(gpath);
         var14.add("-");
      }

      if (lineN(contents, 1).contains("python3")) {
         var14.add(0, "/usr/local/bin/python3");
      } else {
         var14.add(0, "/usr/bin/python");
      }

      var14.add(1, soutName);
      String[] cmds = var14.toArray(new String[0]);
      new Job();
      ByteArrayOutputStream procout = new ByteArrayOutputStream();
      int status = runCommandSimple(cmds, procout);
      out.append(String.valueOf(status) + "\n");
      out.append(procout.toString());
      return out;
   }

   public static String gFilename(String path) {
      int dot = path.lastIndexOf(".");
      return dot != -1 ? path.substring(0, dot) + ".g.txt" : path + ".g.txt";
   }

   public static String lineN(String s, int n) {
      int mark = 0;

      for (int i = 0; i < n - 1; i++) {
         int temp = s.indexOf(10, mark);
         if (temp == -1) {
            return "";
         }

         mark = temp + 1;
      }

      return s.substring(mark);
   }

   public static String lineNEnd(String s, int n) {
      int mark = 0;

      for (int i = 0; i < n - 1; i++) {
         int temp = s.indexOf(10, mark);
         if (temp == -1) {
            return "";
         }

         mark = temp + 1;
      }

      int next = s.indexOf(10, mark);
      return next == -1 ? s.substring(mark) : s.substring(mark, next);
   }

   public static SimpleImage extractImage(String content) {
      if (content.startsWith("data:image/png;base64,") || content.startsWith("data:image/jpeg;base64,")) {
         BufferedImage image = null;
         int start = content.indexOf(44) + 1;
         int end = content.lastIndexOf(10);
         if (end == -1) {
            end = content.length();
         }

         byte[] bytes = Base64.getDecoder().decode(content.substring(start, end));
         ByteArrayInputStream is = new ByteArrayInputStream(bytes);

         try {
            image = ImageIO.read(is);
            is.close();
            return new SimpleImage(image);
         } catch (IOException var7) {
         }
      }

      return null;
   }

   public static String grade(Project proj, Case c, String got) throws IOException {
      if (c.mode.equals("--c")) {
         if (!got.startsWith("error")) {
            return "--c fail: did not get a compile error\n" + got;
         }
      } else if (c.mode.equals("--e")) {
         if (!got.startsWith("ok") || lineN(got, 2).startsWith("0")) {
            return "--e fail: wanted ok line 1 and non-zero exit on line 2\n" + got;
         }
      } else {
         if (!c.mode.startsWith("--g")) {
            return "fail unknown mode:" + c.mode;
         }

         if (!got.startsWith("ok") || !lineN(got, 2).startsWith("0")) {
            return "--g fail: did not run\n" + got;
         }

         if (c.mode.equals("--g2")) {
            return "--g2 not happening";
         }

         String arg = c.fileName();
         String gpath = proj.dataFile(gFilename(arg));
         boolean ok = false;
         String gtext = "";
         if (new File(gpath).exists()) {
            gtext = readFile(gpath);
            if (gtext.startsWith("data:")) {
               SimpleImage gimage = extractImage(gtext);
               String content = lineN(got, 3);
               SimpleImage gotImage = extractImage(content);
               double err = gimage.imageDiff(gotImage);
               if (err < 1.0) {
                  return "ok averge-color-err:" + String.format("%.2f", err);
               }

               return "not-ok image averge-color-err:" + String.format("%.2f", err);
            }

            ok = gtext.startsWith(lineN(got, 3));
         }

         if (!ok) {
            return "--g fail .g not matching got:\n" + gtext.length() + " " + got.length();
         }
      }

      return "ok";
   }

   public static void runCase(Project proj, Case c) throws IOException {
      if (c.mode.equals("--o")) {
         String got = runJava(proj, c.srcname, "", c.args, proj.projectDir + "/tmp").toString();
         int n = got.indexOf(10);
         n = got.indexOf(10, n + 1);
         got = got.substring(n + 1);
         System.out.print(got);
      } else {
         System.out.print(c.srcname + " " + c.args + " " + c.mode);
         String got = runJava(proj, c.srcname, "", c.args, proj.projectDir + "/tmp").toString();
         String err = grade(proj, c, got);
         System.out.println(" " + err);
      }
   }

   public static String runCase(String projectName, String code, String args, String verb) throws IOException {
      File f = new File("/Users/nick/cw/priv/" + projectName);
      if (!f.exists()) {
         return "error bad project name";
      } else {
         Project proj = readProject(f.getAbsolutePath());
         System.err.println("runcase");
         System.err.println(args.trim());
         Case c = proj.findCase(args.trim());
         if (c != null && "show".equals(verb)) {
            String fname = c.args.get(0);
            if (!fname.endsWith(".png") && !fname.endsWith(".jpeg") && !fname.endsWith(".jpg")) {
               return "<p><b>" + fname + "</b>\n" + "<pre>\n" + fileToString(proj.dataFile(fname)) + "</pre>\n";
            } else {
               SimpleImage image = new SimpleImage(proj.dataFile(fname));
               return "<p><b>" + fname + "</b>\n" + "<p><img src='" + image.toString() + "'>\n";
            }
         } else {
            String result = "<p>error bad program args";
            if (c != null) {
               String got = proj.runCase(c, code);
               boolean ran = got.startsWith("ok") && lineN(got, 2).startsWith("0");
               if (!ran) {
                  return "low level run fail:" + got;
               }

               if (c.mode.equals("--g2") && proj.attrs.containsKey("binky")) {
                  String content = lineNEnd(got, 3);
                  return "data:image/gif;base64," + content;
               }

               if (c.mode.equals("--g2")) {
                  String content = lineN(got, 3);
                  String earea = lineNEnd(content, 1);
                  String ecolor = lineNEnd(content, 2);
                  Double ea = Double.parseDouble(earea);
                  Double proj_ea = Double.parseDouble(proj.getAttr("earea"));
                  String eline = "ea:" + ea + "  ecolor:" + ecolor + " proj_ea:" + proj_ea + "\n";
                  String grade;
                  if (ea <= proj_ea) {
                     grade = "<p>Correct <img src=/c2.jpg>\n";
                  } else {
                     grade = "<p>Not Yet <img src=/c1.jpg>\n";
                  }

                  String image = lineNEnd(content, 3);
                  return grade + eline + image;
               }

               String eval = "";
               String err = "";
               if (!c.mode.equals("--o")) {
                  err = grade(proj, c, got);
                  if (err.startsWith("ok")) {
                     eval = "<p>Correct (" + err + ") <img src=/c2.jpg>\n";
                  } else {
                     eval = "<p>Not Yet (" + err + ")  <img src=/c1.jpg>\n" + err;
                  }
               }

               String content = lineN(got, 3);
               if (content.startsWith("data:")) {
                  int end = content.indexOf(10);
                  if (end == -1) {
                     end = content.length();
                  }

                  return eval + "<p><img src='" + content.substring(0, end) + "'>";
               }

               if (!err.equals("") && !err.startsWith("ok")) {
                  result = eval + "<p>" + err;
               } else {
                  result = eval + "<p><pre>\n" + lineN(got, 3) + "</pre>\n";
               }
            }

            return result;
         }
      }
   }

   public static void runAll(String projectName, String code, PrintWriter out) throws IOException {
      File f = new File("/Users/nick/cw/priv/" + projectName);
      if (!f.exists()) {
         out.append("error bad project name");
      }

      Project proj = readProject(f.getAbsolutePath());
      boolean allCorrect = true;

      for (Case c : proj.cases) {
         if (c.mode.equals("--g")) {
            String got = proj.runCase(c, code);
            if (got.startsWith("ok") && lineN(got, 2).startsWith("0")) {
               boolean var12 = true;
            } else {
               boolean var10000 = false;
            }

            String err = grade(proj, c, got);
            String eval;
            if (err.startsWith("ok")) {
               eval = " - Correct <img src=/c2.jpg>\n";
            } else {
               eval = " - Not Yet <img src=/c1.jpg>\n";
               allCorrect = false;
            }

            out.append("<p>");
            out.append(c.args.get(0));
            out.append(eval);
            out.flush();
         }
      }

      if (allCorrect) {
         out.append("<p>All Correct <TODO IMG");
      }
   }

   public static String stripString(String line) {
      line = line.trim();
      int comment = line.indexOf("#");
      if (comment != -1) {
         line = line.substring(0, comment).trim();
      }

      return line;
   }

   public static Case lineToCase(String projectDir, String line) {
      if (line.length() == 0) {
         return null;
      } else {
         List<String> words = new ArrayList<>(Arrays.asList(line.split("\\s")));
         if (words.size() == 0) {
            return null;
         } else {
            if (words.size() >= 2) {
               String srcname = words.get(0);
               words.remove(0);
               String mode = words.get(words.size() - 1);
               words.remove(words.size() - 1);
               if (mode.equals("--g") || mode.equals("--e") || mode.equals("--c") || mode.equals("--o") || mode.equals("--g2")) {
                  Case c = new Case();
                  c.args = words;
                  c.argsName = "";

                  for (String s : words) {
                     if (c.argsName.length() != 0) {
                        c.argsName = c.argsName + "_";
                     }

                     if (s.startsWith("-")) {
                        s = s.substring(1);
                     }

                     c.argsName = c.argsName + s;
                  }

                  c.srcname = srcname;
                  c.mode = mode;
                  c.lang = null;
                  if (c.srcname.endsWith(".java")) {
                     c.lang = "java";
                  }

                  if (c.srcname.endsWith(".py")) {
                     c.lang = "python";
                  }

                  if (c.lang != null) {
                     return c;
                  }
               }
            }

            throw new RuntimeException("Case parse error: '" + line + "'");
         }
      }
   }

   public static Project readProject(String dir) throws IOException {
      Project p = new Project();
      File f = new File(dir);
      f = f.getCanonicalFile();
      p.projectDir = f.getPath();
      p.projectName = f.getName();
      List<Case> cases = new ArrayList<>();
      String index = fileToString(p.projectDir + "/" + "index.txt").toString();
      String[] lines = index.split("\\n");

      for (String line : lines) {
         line = stripString(line);
         if (line.length() != 0) {
            if (p.srcFilename == null) {
               p.srcFilename = line;
               p.srcCode = fileToString(p.projectDir + "/" + p.srcFilename).toString();
            } else if (p.attrs == null && line.startsWith("--a ")) {
               p.attrs = new HashMap<>();

               String[] var13;
               for (String term : var13 = line.substring(4).split("\\s+")) {
                  String[] pair = term.split(":");
                  p.attrs.put(pair[0], pair[1]);
               }
            } else {
               Case c = lineToCase(dir, line);
               if (c != null) {
                  cases.add(c);
               }
            }
         }
      }

      p.cases = cases;
      return p;
   }

   public static void main(String[] args) throws IOException {
      System.setProperty("java.awt.headless", "true");
      if (args[0].equals("-p")) {
         Project p = readProject(args[1]);

         for (Case c : p.cases) {
            if (c.mode.equals("--o")) {
               runCase(p, c);
               return;
            }
         }

         for (Case cx : p.cases) {
            runCase(p, cx);
         }
      }
   }

   public static class Case {
      List<String> args;
      String argsName;
      String mode;
      String srcname;
      String lang;

      String fileName() {
         String arg = this.args.get(0);
         return !arg.startsWith("-") ? arg : this.argsName;
      }
   }

   public static class Job {
      Process execProcess;
   }

   public static class Project {
      String projectName;
      String projectDir;
      String srcFilename;
      String srcCode;
      Map<String, String> attrs;
      List<Case> cases;

      public String dataFile(String filename) {
         return this.projectDir + "/data/" + filename;
      }

      public String solutionCode() {
         return this.srcCode;
      }

      public String getAttr(String key) {
         return this.attrs == null ? null : this.attrs.get(key);
      }

      public Case findCase(String arg) {
         for (Case c : this.cases) {
            if (c.mode.equals("--o") && arg.equals("print")) {
               return c;
            }

            if (c.mode.startsWith("--g") && arg.equals(c.argsName)) {
               return c;
            }

            if (c.mode.equals("--g") && arg.equals(c.args.get(0))) {
               return c;
            }
         }

         return null;
      }

      public List<String> casesList() {
         List<String> result = new ArrayList<>();

         for (Case c : this.cases) {
            if (c.mode.equals("--o")) {
               result.add("print");
            }

            if (c.mode.equals("--g") || c.mode.equals("--g2")) {
               String arg = "";
               if (c.args.size() > 0) {
                  arg = c.args.get(0);
               }

               if (arg.startsWith("-")) {
                  arg = c.argsName;
               }

               result.add(arg);
            }
         }

         return result;
      }

      public String runCase(Case c, String code) {
         if (c.lang.equals("java")) {
            return Priv.runJava(this, c.srcname, code, c.args, this.projectDir + "/tmp").toString();
         } else {
            return c.lang.equals("python") ? Priv.runPython(this, c.srcname, code, c.args, this.projectDir + "/tmp", c).toString() : null;
         }
      }
   }
}
