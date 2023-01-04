/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326;
import java.io.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
/**
 *
 * @author dimit
 */

interface Image {
    
    public void grayscale();
    public void doublesize();
    public void halfsize();
    public void rotateClockwise();
}
class RGBPixel {
    short red;
    short green;
    short blue;
    
    public RGBPixel(short red, short green, short blue){
        red = (short)(red - 128);
        green= (short)(green - 128);
        blue= (short)(blue - 128);
        byte r = (byte)red;
        byte g = (byte)green;
        byte b = (byte)blue;
        int pixel =(r<<16) | (g<<8) | b;
    }
    public RGBPixel(RGBPixel pixel){
        RGBPixel pixelB =pixel;
    }
    public RGBPixel(YUVPixel pixel){
        
        
        int C = (short)(pixel.Y - 16);
        int D = (short)(pixel.U - 128);
        int E = (short)(pixel.V - 128);

        red = clip((short)(( 298 * C + 409 * E + 128) >> 8));
        green = clip((short)(( 298 * C - 100 * D - 208 * E + 128) >> 8));
        blue = clip((short)(( 298 * C + 516 * D + 128) >> 8));


    }
    
    public short clip(short number){
        if(number<0){
            return(0);
        }
        else if(number>255){
            return(255);
        }
        else{
            return(number);
        }
    }
    
    short getRed(){
        return(red);
    }
    short getGreen(){
        return(green);
    }
    short getBlue(){
        return(blue);
    }
    void setRed(short red){
        red= this.red;
    }
    void setGreen(short green){
        green= this.green;
    }
    void setBlue(short blue){
        blue= this.blue;
    }      
    int getRGB(){
        int pixel = (red<<16) | (green<<8) | blue;
        return(pixel);
    }
    void setRGB(int value){
        byte r= (byte)(value >>> 16);
        byte g= (byte)(value >>> 8);
        byte b= (byte)value;
        red= (short)r;
        green= (short)g;
        blue= (short)b;
        red= (short)(red + 128);
        green= (short)(green + 128);
        blue= (short)(blue + 128);
    }
    final void setRGB(short red, short green, short blue){
        red= this.red;
        green= this.green;
        blue= this.blue;
    }
    @Override
    public String toString(){
        String str1= String.valueOf(red);
        String str2= String.valueOf(green);
        String str3= String.valueOf(blue);
        String str= str1+ " "+ str2+ " " + str3;
        return(str);
    }
}

class RGBImage{
    int width;
    int height;
    int colordepth;
    public static int MAX_COLORDEPTH= 255;
    RGBPixel [][] image = new RGBPixel[width][height];
    
    public RGBImage(){}
    
    public RGBImage(int width, int height, int colordepth){
        
        RGBImage imageB= null;
        imageB.image= new RGBPixel[width][height];
        imageB.width= width;
        imageB.height= height;
        imageB.colordepth= colordepth;
    }
    
    
    public RGBImage(RGBImage copyImg){
        RGBImage imageB= null;
        imageB.image= copyImg.image;
        imageB.width= copyImg.width;
        imageB.height= copyImg.height;
        imageB.colordepth= copyImg.colordepth;
          
    }
    
    
    public RGBImage(YUVImage YUVImg){
        
        width= YUVImg.width;
        height= YUVImg.height;
        colordepth= MAX_COLORDEPTH;
        for(int i=0; i< width; i++){
            for(int j=0; j<height; j++){
                int C = (short)(YUVImg.image[i][j].Y - 16);
                int D = (short)(YUVImg.image[i][j].U - 128);
                int E = (short)(YUVImg.image[i][j].V - 128);

                image[i][j].red = clip((short)(( 298 * C + 409 * E + 128) >> 8));
                image[i][j].green = clip((short)(( 298 * C - 100 * D - 208 * E + 128) >> 8));
                image[i][j].blue = clip((short)(( 298 * C + 516 * D + 128) >> 8));
            }
        }
    }
    
    public short clip(short number){
        if(number<0){
            return(0);
        }
        else if(number>255){
            return(255);
        }
        else{
            return(number);
        }
    }
    
    
    int getWidth(){
        return(width);
    }
    int getHeight(){
        return(height);
    }
    int getColorDepth(){
        return(colordepth);
    }
    RGBPixel getPixel(int row, int col){
        RGBPixel pixel;
        pixel= image[row][col];
        return(pixel);
    }
    void setPixel(int row, int col,  RGBPixel pixel){
        image[row][col]= pixel;
    }
    public void grayscale(){
        for(int i= 0; i<width; i++){
            for(int j=0; j<height; j++){
                image[i][j]= getPixel(i,j);
                short red= image[i][j].getRed();
                short green= image[i][j].getGreen();
                short blue= image[i][j].getBlue();
                red= (short)(red * 0.3);
                green= (short)(green* 0.59);
                blue= (short)(blue*0.11);
                image[i][j].setRed(red);
                image[i][j].setGreen(green);
                image[i][j].setBlue(blue);
                setPixel(i, j, image[i][j]);
            }
        }
    }
    public void doublesize(){
        /*RGBPixel [][] CPYImage = new RGBPixel[width][height];
        for(int i= 0; i<width; i++){
            System.arraycopy(image[i], 0, CPYImage[i], 0, height);
        }*/
        RGBPixel [][] testImage = new RGBPixel[width*2][height*2];
        for(int i= 0; i<width; i++){
            for(int j= 0; j<height; j++){
                testImage[2*i][2*j]= image[i][j];
                testImage[2*i+1][2*j]= image[i][j];
                testImage[2*i][2*j+1]= image[i][j];
                testImage[2*i+1][2*j+1]= image[i][j];     
            }
        }
    }
    public void halfsize(){
        RGBPixel [][] testImage = new RGBPixel[width/2][height/2];
        for(int i= 0; i<width/2; i++){
            for(int j= 0; j<height/2; j++){
                short red1= image[2*i][2*j].getRed();
                short green1= image[2*i][2*j].getGreen();
                short blue1= image[2*i][2*j].getBlue();
                short red2= image[2*i+1][2*j].getRed();
                short green2= image[2*i+1][2*j].getGreen();
                short blue2= image[2*i+1][2*j].getBlue();
                short red3= image[2*i][2*j+1].getRed();
                short green3= image[2*i][2*j+1].getGreen();
                short blue3= image[2*i][2*j+1].getBlue();
                short red4= image[2*i+1][2*j+1].getRed();
                short green4= image[2*i+1][2*j+1].getGreen();
                short blue4= image[2*i+1][2*j+1].getBlue();
                short red= (short)((red1+ red2+ red3+ red4)/4);
                short green= (short)((green1+ green2+ green3+ green4)/4); 
                short blue= (short)((blue1+ blue2+ blue3+ blue4)/4); 
                testImage[i][j].setRed(red);
                testImage[i][j].setGreen(green);
                testImage[i][j].setBlue(blue);
                setPixel(i, j, testImage[i][j]);
            }
        }
    }
    public void rotateClockwise(){
        RGBPixel [][] CPYImage = new RGBPixel[width][height];
        for(int i= 0; i<width; i++){
            System.arraycopy(image[i], 0, CPYImage[i], 0, height);
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {               
                image[j][height-1-i] = CPYImage[i][j];            
            }
        }
    }
}

class UnsupportedFileFormatException extends java.lang.Exception{
    static final long SerialVersionUID= -4567891456L;
    
    public UnsupportedFileFormatException(){
        super();
    }
    public UnsupportedFileFormatException(String msg){
        super(msg);
    }
    
}

class PPMImage extends  RGBImage{
    PPMImage imageP;
    java.io.File file;
    RGBImage imageR;
    public PPMImage(java.io.File file) throws FileNotFoundException, UnsupportedFileFormatException{
        PPMImage format= null;
        try(Scanner sc = new Scanner(file)) {
            format= new PPMImage(file);
            //file.getName().substring(file.getName().length()-4).equals(".ppm");
            String typo= sc.next();
            if(sc.hasNextInt()){
                imageP.width= sc.nextInt();
            }
            if(sc.hasNextInt()){
                imageP.height= sc.nextInt();
            }
            if(sc.hasNextInt()){
                imageP.colordepth= sc.nextInt();
            }

            for(int i=0; i<imageP.width; i++ ) {
                for(int j=0; j< imageP.height; j++){
                    imageP.image[i][j].red = (short)sc.nextInt();
                    imageP.image[i][j].green = (short)sc.nextInt();
                    imageP.image[i][j].blue= (short)sc.nextInt();
                }
            }
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open '"+file+"'");
        } catch (UnsupportedFileFormatException ex) {
            System.err.println("An IOException was caught!");
        }
    }
    
    
    
    public PPMImage(RGBImage img){
        imageP.width= img.width;
        imageP.height= img.height;
        imageP.colordepth= img.colordepth;
        imageP.image= img.image;
    }
    
    
    
    public PPMImage(YUVImage img){
        imageR.width= img.width;
        imageR.height= img.height;
        imageR.colordepth= MAX_COLORDEPTH;
        for(int i=0; i< width; i++){
            for(int j=0; j<height; j++){
                int C = (short)(img.image[i][j].Y - 16);
                int D = (short)(img.image[i][j].U - 128);
                int E = (short)(img.image[i][j].V - 128);

                imageR.image[i][j].red = clip((short)(( 298 * C + 409 * E + 128) >> 8));
                imageR.image[i][j].green = clip((short)(( 298 * C - 100 * D - 208 * E + 128) >> 8));
                imageR.image[i][j].blue = clip((short)(( 298 * C + 516 * D + 128) >> 8));
            }
        }
        imageP.width= imageR.width;
        imageP.height= imageR.height;
        imageP.colordepth= imageR.colordepth;
        imageP.image= imageR.image;
    }
    
    
    
    @Override
    public String toString(){
        StringBuffer string = new StringBuffer("P3\n");
        string.append(imageP.width);
        string.append(" ");
        string.append(imageP.height);
        string.append("\n");
        string.append(imageP.colordepth);
        string.append("\n");
        for(int i=0; i<imageP.width; i++ ) {
                for(int j=0; j< imageP.height; j++){
                   string.append(imageP.image[i][j].red);
                   string.append(" ");
                   string.append(imageP.image[i][j].green);
                   string.append(" ");
                   string.append(imageP.image[i][j].blue);
                   string.append(" ");
                }
                string.append("\n");
        }
        String test= string.substring(0, string.length());
        return(test);
    }
    
    
    public void toFile(java.io.File file){
        
        if(file.exists()){
            file.delete();
            try{
                file.createNewFile();
            }
            catch(IOException ex4){
                System.err.println("An IOException was caught!");
            }
        }
        String test= toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(test);
        }
        catch(IOException x){
        }
    }
}

class PPMImageStacker{
    java.io.File dir;
    java.util.List<File> wordsList = new ArrayList<>();
    PPMImage imageP= null;
    
    
    public PPMImageStacker(java.io.File dir) throws UnsupportedFileFormatException, FileNotFoundException{
        PPMImage format= null;
        
        if(!dir.exists()){
            throw new  FileNotFoundException("[ERROR] Directory " +dir.getName() + "does not exist!");
        }
        
        if(dir.isDirectory()){
            throw new  FileNotFoundException("[ERROR] " +dir.getName() + " is not a directory!");
        }
        
        File files[] = dir.listFiles();
        for(File f : files) {
            try{
                format= new PPMImage(f);
            }catch(UnsupportedFileFormatException | FileNotFoundException x){
                throw new UnsupportedFileFormatException();
            }
            wordsList.add(f);
        }
    }

    
    public void stack(){
        int counter= 0;
        ListIterator<File> it = wordsList.listIterator(wordsList.size());
        File file= it.previous();
        
        
        try(Scanner sc1 = new Scanner(file)) {
            String typo= sc1.next();
            if(sc1.hasNextInt()){
                imageP.width= sc1.nextInt();
            }
            if(sc1.hasNextInt()){
                imageP.height= sc1.nextInt();
            }
            if(sc1.hasNextInt()){
                imageP.colordepth= sc1.nextInt();
            }
            for(int i=0; i<imageP.width; i++ ) {
                for(int j=0; j< imageP.height; j++){
                    imageP.image[i][j].red = 0;
                    imageP.image[i][j].green = 0;
                    imageP.image[i][j].blue= 0;
                }
            }
        }
        catch(IOException x){
        }
        
        for (it = wordsList.listIterator(wordsList.size()); it.hasPrevious(); ) {
            file = it.previous();
            counter++;
            try(Scanner sc = new Scanner(file)){
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                for(int i=0; i<imageP.width; i++ ) {
                    for(int j=0; j< imageP.height; j++){
                        imageP.image[i][j].red = (short)((sc.nextInt()+ imageP.image[i][j].red)/counter);
                        imageP.image[i][j].green = (short)((sc.nextInt() +imageP.image[i][j].green)/counter);
                        imageP.image[i][j].blue= (short)((sc.nextInt() +imageP.image[i][j].blue)/counter);
                    }
                }
            }
            catch(IOException x){
            }
        }
    }
    public PPMImage getStackedImage(){
        return(imageP);
    }
}

class YUVPixel{
    short Y;
    short U;
    short V;
    YUVPixel pixelB;
    public YUVPixel(short Y, short U, short V){
        Y= this.Y;
        U= this.U;
        V= this.V;
    }
    public YUVPixel(YUVPixel pixel){
        YUVPixel pixelTest =pixel;
    }
    public YUVPixel(RGBPixel pixel){
        
        pixelB.Y = (short)(( (  66 * pixel.red + 129 * pixel.green +  25 * pixel.blue + 128) >> 8) +  16);
        pixelB.U = (short)(( ( -38 * pixel.red-  74 * pixel.green + 112 * pixel.blue + 128) >> 8) + 128);
        pixelB.V = (short)(( ( 112 * pixel.red -  94 *pixel.green -  18 * pixel.blue + 128) >> 8) + 128);

    }
    
    public short getY(){
        return(Y);
    }
    
    public short getU(){
        return(U);
    }
    
    public short getV(){
        return(V);
    }
    
    public void setΥ(short Υ){
        Y= this.Y;
    }
    
    public void setU(short U){
        U= this.U;
    }
    
    public void setV(short V){
        V= this.V;
    }
}

class YUVImage{
    int width;
    int height;
    YUVImage imageB;
    java.io.File file;
    YUVPixel[][] image = new YUVPixel[width][height];
    
    public YUVImage(int width, int height){
        imageB.image= new YUVPixel[width][height];
        imageB.width= width;
        imageB.height= height;
        for(int i=0; i<width; i++){
            for(int j=0; j< height; j++){
                imageB.image[i][j].Y= 16;
                imageB.image[i][j].U= 128;
                imageB.image[i][j].V= 128;
            }
        }
    }
    
    public YUVImage(YUVImage copyImg){
        imageB= copyImg;
    }
    
    public YUVImage(RGBImage RGBImg){
        imageB.width= RGBImg.width;
        imageB.height= RGBImg.height;
        
        for(int i=0; i< width; i++){
            for(int j=0; j<height; j++){
                imageB.image[i][j].Y = (short)(( (  66 * RGBImg.image[i][j].red + 129 * RGBImg.image[i][j].green +  25 * RGBImg.image[i][j].blue + 128) >> 8) +  16);
                imageB.image[i][j].U = (short)(( ( -38 * RGBImg.image[i][j].red-  74 * RGBImg.image[i][j].green + 112 * RGBImg.image[i][j].blue + 128) >> 8) + 128);
                imageB.image[i][j].V = (short)(( ( 112 * RGBImg.image[i][j].red -  94 *RGBImg.image[i][j].green -  18 * RGBImg.image[i][j].blue + 128) >> 8) + 128);
            }
        }
    }
    
    public YUVImage(java.io.File file)  throws FileNotFoundException, UnsupportedFileFormatException{
        YUVImage format= null;
        try(Scanner sc = new Scanner(file)) {
            format= new YUVImage(file); 
            file.getName().substring(file.getName().length()-4).equals(".yuv");
            String typo= sc.next();
            if(sc.hasNextInt()){
                imageB.width= sc.nextInt();
            }
            if(sc.hasNextInt()){
                imageB.height= sc.nextInt();
            }
            
            for(int i=0; i<imageB.width; i++ ) {
                for(int j=0; j< imageB.height; j++){
                    imageB.image[i][j].Y = (short)sc.nextInt();
                    imageB.image[i][j].U = (short)sc.nextInt();
                    imageB.image[i][j].V= (short)sc.nextInt();
                }
            }
        } catch(FileNotFoundException ex) {
        } catch (UnsupportedFileFormatException ex) {
        }
    }
    @Override
    public String toString(){
        StringBuffer string = new StringBuffer("YUV3\n");
        string.append(imageB.width);
        string.append(" ");
        string.append(imageB.height);
        string.append("\n");
        for(int i=0; i<imageB.width; i++ ) {
                for(int j=0; j< imageB.height; j++){
                   string.append(imageB.image[i][j].Y);
                   string.append(" ");
                   string.append(imageB.image[i][j].U);
                   string.append(" ");
                   string.append(imageB.image[i][j].V);
                   string.append(" ");
                }
                string.append("\n");
        }
        java.lang.String test= string.substring(0, string.length());
        return(test);
    }
    public void toFile(java.io.File file){
        
        if(file.exists()){
            file.delete();
            try{
                file.createNewFile();
            }
            catch(IOException ex4){
                System.err.println("An IOException was caught!");
            }
        }
        String test= toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(test);
        }
        catch(IOException e){
                System.err.println("An IOException was caught!");
            }
    }
    public void equalize(){
        short luminocity;
        Histogram bt = new Histogram(imageB);
        bt.equalize();
        
        for(int i=0; i< imageB.width; i++){
            for(int j=0; j< imageB.height; j++){
                luminocity= bt.getEqualizedLuminocity(image[i][j].Y);
                image[i][j].Y= luminocity;
            }
        }
    }
}

class Histogram{
    
    int[] example= new int[255];
    double[] matrix= new double[255];
    private static DecimalFormat df3 = new DecimalFormat("#.000");
    private static DecimalFormat df4 = new DecimalFormat("#.0000");
    
    public Histogram(YUVImage img){
        for(int i=0; i< img.width; i++){
            for(int j=0; j< img.height; j++){
                matrix[img.image[i][j].Y]++;
            }
        }
    }
    
    @Override
    public String toString(){
        StringBuffer string = new StringBuffer("");
        for(int i= 0; i<256; i++){
            string.append("\n" +df3.format(i)+ ".");
            string.append("(" +df4.format(matrix[i])+ ")    ");
            for(double j=matrix[i]- 1000; j>=0; j= j-1000){
                string.append("#");
            }
            for(double j=matrix[i]- 100; j>=0; j= j-100){
                string.append("$");
            }
            for(double j=matrix[i]- 10; j>=0; j= j-10){
                string.append("@");
            }
            for(double j=matrix[i]- 1; j>=0; j= j-1){
                string.append("*");
            }
             string.append("\n");
        }
        java.lang.String test= string.substring(0, string.length());
        return(test);
    }
    public void toFile(File file) throws IOException{
        String test= toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(test);
        }
    }
    public void equalize(){
        double counter=0;
        double counterB= 0;
        double[] test= new double[255];
        
        for(int i=0; i<256; i++){
            counter= matrix[i]+ counter;
        }
        for(int i=0; i<256; i++){
            matrix[i]= matrix[i]/counter;
        }
        
        for(int i=0; i<256; i++){
            counterB= 0;
            for(int j=0; j<=i; j++){
                 counterB= matrix[j]+ counterB;
            }
            test[i]= counterB/counter;
        }
        int maxColorDepth= 235;
        
        for(int i=0; i<256; i++){
            example[i]= (int)(test[i]*maxColorDepth);
        }
    }
    
    public short getEqualizedLuminocity(int luminocity){
                return((short)example[luminocity]);
    }
}

class ImageProcessing {
  JFrame frame;
  JPanel mainPanel;
  JLabel imgLabel;
  JScrollPane scrollPane;
  
  PPMImage ppmImg;
  YUVImage yuvImg;
  BufferedImage img;
  String imgFilename;
  
  JMenu saveMenu;
  JMenu actionMenu;
  
  public static final int MINIMUM_WIDTH = 500;
  public static final int MINIMUM_HEIGHT = 400;

  public ImageProcessing() {
    frame = new JFrame("CE325 HW3");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainPanel = new JPanel(new BorderLayout());
    frame.setContentPane(mainPanel);
    
    imgLabel = new JLabel();
    imgLabel.setPreferredSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
    scrollPane = new JScrollPane(imgLabel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    mainPanel.add(scrollPane, BorderLayout.CENTER);
    addMenu();
    frame.pack();
    frame.setVisible(true);
  }   

  public static void main(String []args) {    
    ImageProcessing frame = new ImageProcessing();
  }
  
  public void enableMenu() {
    if(!saveMenu.isEnabled())
      saveMenu.setEnabled(true);
    if(!actionMenu.isEnabled())
      actionMenu.setEnabled(true);
  }
  
  public void addMenu() {
    JMenuBar menuBar = new JMenuBar();
    
    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic(KeyEvent.VK_F);
    menuBar.add(fileMenu);
    
    JMenu openMenu = new JMenu("Open");
    openMenu.setMnemonic(KeyEvent.VK_O);
    JMenuItem ppmOpen = new JMenuItem("PPM File");
    ppmOpen.setMnemonic(KeyEvent.VK_P);        
    openMenu.add(ppmOpen);
    JMenuItem yuvOpen = new JMenuItem("YUV File");
    yuvOpen.setMnemonic(KeyEvent.VK_Y);
    openMenu.add(yuvOpen);
    JMenuItem otherOpen = new JMenuItem("Other Format");
    otherOpen.setMnemonic(KeyEvent.VK_Y);
    openMenu.add(otherOpen);
    fileMenu.add(openMenu);
    
    ppmOpen.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new PPMFileFilter());
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          try {
            ppmImg = new PPMImage(selectedFile);
            img = RGBImage2BufferedImage(ppmImg);
            imgLabel.setIcon(new ImageIcon(img));
            enableMenu();
          } catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(
               frame,
               "File not found!",
               "ERROR",
               JOptionPane.ERROR_MESSAGE);
          }catch(UnsupportedFileFormatException ex) {
            JOptionPane.showMessageDialog(
               frame,
               ex.getMessage(),
               "ERROR: File format",
               JOptionPane.ERROR_MESSAGE);
          }
          
        }
      }
    });
    
    yuvOpen.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new YUVFileFilter());
        fileChooser.addChoosableFileFilter(new PPMFileFilter());
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          try {
            yuvImg = new YUVImage(selectedFile);
            ppmImg = new PPMImage(new RGBImage(yuvImg));
            img = RGBImage2BufferedImage(ppmImg);
            imgLabel.setIcon(new ImageIcon(img));
            enableMenu();
          }catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(
               frame,
               "File not found!",
               "ERROR",
               JOptionPane.ERROR_MESSAGE);
          }catch(UnsupportedFileFormatException ex) {
            JOptionPane.showMessageDialog(
               frame,
               ex.getMessage(),
               "ERROR: File format",
               JOptionPane.ERROR_MESSAGE);
          }
          
        }
      }
    });
    
    otherOpen.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          try {
            img = ImageIO.read(selectedFile);
            imgLabel.setIcon(new ImageIcon(img));
            ppmImg = new PPMImage(BufferedImage2RGBImage(img));
            enableMenu();
          }catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(
               frame,
               "File not found!",
               "ERROR",
               JOptionPane.ERROR_MESSAGE);
          }catch(IOException ex) {
            JOptionPane.showMessageDialog(
               frame,
               ex.getMessage(),
               "ERROR: IO Error",
               JOptionPane.ERROR_MESSAGE);
          }
          
        }
      }
    });
    
    saveMenu = new JMenu("Save");
    saveMenu.setMnemonic(KeyEvent.VK_S);
    saveMenu.setEnabled(false);
    JMenuItem ppmSave = new JMenuItem("PPM File");
    ppmSave.setMnemonic(KeyEvent.VK_P);        
    saveMenu.add(ppmSave);
    JMenuItem yuvSave = new JMenuItem("YUV File");
    yuvSave.setMnemonic(KeyEvent.VK_Y);        
    saveMenu.add(yuvSave);
    fileMenu.add(saveMenu);
    
    ppmSave.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new PPMFileFilter());
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          if(selectedFile.exists()) {
            int answer = JOptionPane.showConfirmDialog(
                            frame, "File exists! Overwrite ?",
                            "Warning",
                            JOptionPane.YES_NO_OPTION);
            if(answer != JOptionPane.YES_OPTION)
              return;
          }
          ppmImg.toFile(selectedFile);
        }
      }
    });
    
    yuvSave.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new YUVFileFilter());
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          if(selectedFile.exists()) {
            int answer = JOptionPane.showConfirmDialog(
                            frame, "File exists! Overwrite ?",
                            "Warning",
                            JOptionPane.YES_NO_OPTION);
            if(answer != JOptionPane.YES_OPTION)
              return;
          }
          yuvImg = new YUVImage(ppmImg);
          yuvImg.toFile(selectedFile);
        }
      }
    });
    
    actionMenu = new JMenu("Tools");
    actionMenu.setEnabled(false);
    actionMenu.setMnemonic(KeyEvent.VK_A);    
    menuBar.add(actionMenu);
    JMenuItem grayscale = new JMenuItem("Grayscale");    
    JMenuItem incsize = new JMenuItem("Increase Size");
    JMenuItem decsize = new JMenuItem("Decrease Size");
    JMenuItem rotate = new JMenuItem("Rotate Clocwise");
    JMenuItem equalize = new JMenuItem("Equalize Histogram");
    JMenu stackMenu = new JMenu("Stacking Algorithm");
    JMenuItem stackDir = new JMenuItem("Select directory..");
    stackMenu.add(stackDir);
    actionMenu.add(grayscale);
    actionMenu.add(incsize);
    actionMenu.add(decsize);
    actionMenu.add(rotate);
    actionMenu.add(equalize);
    actionMenu.add(stackMenu);
    
    grayscale.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        ppmImg.grayscale();
        img = RGBImage2BufferedImage(ppmImg);
        imgLabel.setIcon(new ImageIcon(img));
      }
    });
    
    incsize.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        ppmImg.doublesize();
        img = RGBImage2BufferedImage(ppmImg);
        imgLabel.setIcon(new ImageIcon(img));
      }
    });
    
    decsize.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        ppmImg.halfsize();
        img = RGBImage2BufferedImage(ppmImg);
        imgLabel.setIcon(new ImageIcon(img));
      }
    });
    
    rotate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        ppmImg.rotateClockwise();
        img = RGBImage2BufferedImage(ppmImg);
        imgLabel.setIcon(new ImageIcon(img));
      }
    });
    
    equalize.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        yuvImg = new YUVImage(ppmImg);          
        yuvImg.equalize();
        ppmImg = new PPMImage(yuvImg);
        img = RGBImage2BufferedImage(ppmImg);
        imgLabel.setIcon(new ImageIcon(img));
      }
    });
    
    stackDir.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new PPMFileFilter());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          try {
            File selectedFile = fileChooser.getSelectedFile();
            PPMImageStacker stacker = new PPMImageStacker(selectedFile);
            stacker.stack();
            ppmImg = new PPMImage(stacker.getStackedImage());
            img = RGBImage2BufferedImage(ppmImg);
            imgLabel.setIcon(new ImageIcon(img));
          }catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(
               frame,
               ex.getMessage(),
               "ERROR",
               JOptionPane.ERROR_MESSAGE);
          }catch(UnsupportedFileFormatException ex) {
            JOptionPane.showMessageDialog(
               frame,
               ex.getMessage(),
               "ERROR",
               JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    });

    
    frame.setJMenuBar(menuBar);
  }
  
  public static BufferedImage RGBImage2BufferedImage(RGBImage img) {
    BufferedImage bimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
    for(int y=0; y<img.getHeight(); y++) {
      for(int x=0; x<img.getWidth(); x++) {
        bimg.setRGB(x, y, (new Color(img.getPixel(y,x).getRGB())).getRGB() );
      }
    }
    return bimg;
  }
  
  public static RGBImage BufferedImage2RGBImage(BufferedImage img) {
    RGBImage rgbImg = new RGBImage(img.getWidth(), img.getHeight(), RGBImage.MAX_COLORDEPTH);    
    for(int y=0; y<rgbImg.getHeight(); y++) {
      for(int x=0; x<rgbImg.getWidth(); x++) {
        Color c = new Color(img.getRGB(x,y));
        rgbImg.setPixel(y, x, new RGBPixel((short)c.getRed(), (short)c.getGreen(),(short)c.getBlue()));
      }
    }
    return rgbImg;
  }
  
}

class PPMFileFilter extends javax.swing.filechooser.FileFilter {
  public boolean accept(File file) {
    if(file.isDirectory())
      return true;
    String name = file.getName();    
    if(name.length()>4 && name.substring(name.length()-4, name.length()).toLowerCase().equals(".ppm"))
      return true;
    return false;
  }
  
  public String getDescription() { return "PPM File"; }
}

class YUVFileFilter extends javax.swing.filechooser.FileFilter {
  public boolean accept(File file) {
    if(file.isDirectory())
      return true;
    String name = file.getName();
    if(name.length()>4 && name.substring(name.length()-4, name.length()).toLowerCase().equals(".yuv"))
      return true;
    return false;
  }
  
  public String getDescription() { return "YUV File"; }
}