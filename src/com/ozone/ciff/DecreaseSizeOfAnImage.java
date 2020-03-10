package com.ozone.ciff;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sun.image.codec.jpeg.ImageFormatException;

public class DecreaseSizeOfAnImage {

	public static final int LIMIT_AREA = 524288;
	
	public static void main(String[] args) throws IOException {
		
		String directory = "images";
		
		if(args != null && args.length > 0) {
			directory = args[0];
		}
		
		for(File file : new File(directory).listFiles()) {
			String filename = file.getName();
			System.out.println(filename);
			BufferedImage image = read(directory + "/" + filename);
			if(image == null) {
				continue;
			}
			HashMap<Channel, int[][]> matrix = convertToMatrix(image);
			
			int height = matrix.get(Channel.GREEN).length;
			int width = matrix.get(Channel.GREEN)[0].length;
			System.out.println(filename + "\t" + height + " x " + width);
			if(height*width > LIMIT_AREA) {
				int powerOf2 = (int)Math.floor(Math.log(height*width/(1.0*LIMIT_AREA))/Math.log(2));
				int multiple = (int)Math.pow(2, (1+Math.floor(Math.log(height*width/(1.0*LIMIT_AREA))/Math.log(2)))/2);
				if(powerOf2 % 2 == 0) {
					multiple = (int)Math.pow(2, Math.floor(Math.log(height*width/(1.0*LIMIT_AREA))/Math.log(2))/2);
				}
				matrix = shrink(matrix, multiple);
				
				
				int height_corrected = matrix.get(Channel.GREEN).length;
				int width_corrected = matrix.get(Channel.GREEN)[0].length;
				System.out.println(filename + "\t" + height_corrected + " x " + width_corrected + "\tCORRECTION");
				ImageIO.write(convertToImage(matrix), "jpg", new File(directory + "/" + filename));
			}
		}
	}
	
	private static HashMap<Channel, int[][]> shrink(HashMap<Channel, int[][]> matrix, int multiple) {
		int height = matrix.get(Channel.GREEN).length;
		int width = matrix.get(Channel.GREEN)[0].length;
		int newHeight = height/multiple;
		int newWidth = width/multiple;
		HashMap<Channel, int[][]> shrunkMatrix = new HashMap<Channel, int[][]>();
		
		for(Channel channel : Channel.values()) {
			int[][] pixels = new int[newHeight][newWidth];
			for(int i=0;i<newHeight;i++) {
				for(int j=0;j<newWidth;j++) {
					pixels[i][j] = matrix.get(channel)[Math.min(multiple*i, height)][Math.min(multiple*j, width)];
				}
			}
			shrunkMatrix.put(channel, pixels);
		}
		
		return shrunkMatrix;
	}

	public static void display(BufferedImage image){
		JFrame frame = new JFrame();
		frame.setSize(image.getWidth(), image.getHeight());
		JLabel label = new JLabel(new ImageIcon(image));
		frame.add(label);
		frame.setVisible(true);		
	}
	
	public static BufferedImage read(String filename){
		try {
			InputStream is = Files.newInputStream(Paths.get(System.getProperty("user.dir") + "/" + filename));
			BufferedImage image = com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(is).decodeAsBufferedImage();
			is.close();
			return image;
		}catch (ImageFormatException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (com.sun.image.codec.jpeg.TruncatedFileException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	public enum Channel{
		RED,
		GREEN,
		BLUE,
	}
	
	public static HashMap<Channel, int[][]> convertToMatrix(BufferedImage image){
		int width = image.getWidth();
		int height = image.getHeight();
		int [][] alpha = new int[height][width];
		int[][] red = new int[height][width];
		int[][] green = new int[height][width];
		int[][] blue = new int[height][width];
		
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				Color color = new Color(image.getRGB(j, i));
				alpha[i][j] = color.getAlpha();
				red[i][j] = color.getRed();
				green[i][j] = color.getGreen();
				blue[i][j] = color.getBlue();
			}
		}
		
		HashMap<Channel, int[][]> matrix = new HashMap<Channel, int[][]>();
		
		matrix.put(Channel.RED, red);
		matrix.put(Channel.GREEN, green);
		matrix.put(Channel.BLUE, blue);
		
		return matrix;
	}
	
	public static BufferedImage convertToImage(HashMap<Channel, int[][]> matrix){
		int height = matrix.get(Channel.RED).length;
		int width = matrix.get(Channel.RED)[0].length;
		int[][] red = matrix.get(Channel.RED);
		int[][] green = matrix.get(Channel.GREEN);
		int[][] blue = matrix.get(Channel.BLUE);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				Color color = new Color(validate(red[i][j]), validate((int)(green[i][j])) , validate(blue[i][j]));
				image.setRGB(j, i, color.getRGB());
			}
		}
		return image;
	}
	
	public static int validate(int value){
		return value > 255 ? 255 : (value < 0 ? 0 : value);
	}
	
	public static int threshold(int value, double threshold){
		return value > threshold ? value : 0;
	}
	
	public static int[][] contrast(int[][] matrix){
		int height = matrix.length;
		int width = matrix[0].length;
		int[][] contrast = new int[height][width];
		
		int max = 0;
		int min = 255;
		
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				max = Math.max(max, matrix[i][j]);
				min = Math.min(min, matrix[i][j]);
			}
		}

		System.out.println(max + "\t" + min);
		
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				contrast[i][j] = validate((int)Math.round((255*(matrix[i][j]-min))/(double)(max-min)));
			}
		}
		
		return contrast;
	}

}
