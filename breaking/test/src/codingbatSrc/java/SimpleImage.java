import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

public class SimpleImage {
   private BufferedImage image;

   public static SimpleImage readImage(String filename) throws IOException {
      SimpleImage im = new SimpleImage();
      im.image = ImageIO.read(new File(filename));
      return im;
   }

   public SimpleImage(String filename) throws IOException {
      this.image = ImageIO.read(new File(filename));
   }

   public SimpleImage(BufferedImage image) {
      this.image = image;
   }

   private SimpleImage() {
   }

   @Override
   public String toString() {
      try {
         return "data:image/png;base64," + this.toPNG64();
      } catch (IOException var2) {
         return "IOException reading image";
      }
   }

   public String toPNG64() throws IOException {
      ByteArrayOutputStream buff = new ByteArrayOutputStream();
      ImageIO.write(this.image, "png", buff);
      return DatatypeConverter.printBase64Binary(buff.toByteArray());
   }

   public void writeFile(String filename) throws IOException {
      FileOutputStream f = new FileOutputStream(filename);
      ImageIO.write(this.image, "png", f);
      f.close();
   }

   public static void giftest() throws IOException {
      SimpleImage image = new SimpleImage("flowers.jpg");
      image.fillRect(50, 50, 50, 50, Color.RED);
   }

   public static void main(String[] args) throws IOException {
      System.setProperty("java.awt.headless", "true");
      giftest();
      System.exit(0);
      SimpleImage image = new SimpleImage(args[1]);
      red0(args);
      if (args[0].equals("-b")) {
         System.out.println(image);
      }

      if (args[0].equals("-f")) {
         image.writeFile("out.png");
      }
   }

   public int getRGB(int x, int y) {
      return this.image.getRGB(x, y);
   }

   public int getWidth() {
      return this.image.getWidth();
   }

   public int getHeight() {
      return this.image.getHeight();
   }

   public int getRed(int x, int y) {
      return this.getRGB(x, y) >> 16 & 0xFF;
   }

   public int getGreen(int x, int y) {
      return this.getRGB(x, y) >> 8 & 0xFF;
   }

   public int getBlue(int x, int y) {
      return this.getRGB(x, y) & 0xFF;
   }

   public int clip(int val) {
      if (val < 0) {
         return 0;
      } else {
         return val > 255 ? 255 : val;
      }
   }

   public void setRed(int x, int y, int val) {
      Color c = new Color(this.image.getRGB(x, y));
      this.image.setRGB(x, y, new Color(this.clip(val), c.getGreen(), c.getBlue()).getRGB());
   }

   public void setGreen(int x, int y, int val) {
      Color c = new Color(this.image.getRGB(x, y));
      this.image.setRGB(x, y, new Color(c.getRed(), this.clip(val), c.getBlue()).getRGB());
   }

   public void setBlue(int x, int y, int val) {
      Color c = new Color(this.image.getRGB(x, y));
      this.image.setRGB(x, y, new Color(c.getRed(), c.getGreen(), this.clip(val)).getRGB());
   }

   public void fillRect(int x, int y, int width, int height, Color color) {
      Graphics2D g = this.image.createGraphics();
      g.setColor(color);
      g.fillRect(x, y, width, height);
      g.dispose();
   }

   public static void red0(String[] args) throws IOException {
      SimpleImage image = new SimpleImage(args[0]);

      for (int y = 0; y < image.getHeight(); y++) {
         for (int x = 0; x < image.getWidth(); x++) {
            image.setRed(x, y, 0);
         }
      }

      System.out.println(image);
   }

   public static void grayscale(String[] args) throws IOException {
      SimpleImage image = new SimpleImage(args[0]);

      for (int y = 0; y < image.getHeight(); y++) {
         for (int x = 0; x < image.getWidth(); x++) {
            int avg = (image.getRed(x, y) + image.getGreen(x, y) + image.getBlue(x, y)) / 3;
            image.setRed(x, y, avg);
            image.setGreen(x, y, avg);
            image.setBlue(x, y, avg);
         }
      }

      System.out.println(image);
   }

   public void factorImage(double factor) {
      for (int y = 0; y < this.image.getHeight(); y++) {
         for (int x = 0; x < this.image.getWidth(); x++) {
            Color c = new Color(this.image.getRGB(x, y));
            Color c2 = new Color((int)(c.getRed() * factor), (int)(c.getGreen() * factor), (int)(c.getBlue() * factor));
            this.image.setRGB(x, y, c2.getRGB());
         }
      }
   }

   public double imageDiff(SimpleImage other) {
      if (this.getWidth() == other.getWidth() && this.getHeight() == other.getHeight()) {
         double err = 0.0;

         for (int y = 0; y < this.image.getHeight(); y++) {
            for (int x = 0; x < this.image.getWidth(); x++) {
               err += Math.abs(this.getRed(x, y) - other.getRed(x, y));
               err += Math.abs(this.getGreen(x, y) - other.getGreen(x, y));
               err += Math.abs(this.getBlue(x, y) - other.getBlue(x, y));
            }
         }

         return err / (3 * this.image.getWidth() * this.image.getHeight());
      } else {
         return 999.0;
      }
   }
}
