package face_loc;
import face_loc.ArraySlicing;
public class ImageProcessing {
	public enum HAAR_FILTERS{
		TWO_H,
		TWO_V,
		THREE, 
		FOUR, 
	}
	// Calculate Integral Image
		public static float[][] getIntegralImg(float[][] img,int w, int h){
			float [][] integral_img = new float[h+1][w+1];
			
			for(int i = 1; i< h+1; i++) {
				for(int j =1; j < w+1; j++) {
					integral_img[i][j] = img[i-1][j-1]+integral_img[i-1][j]+integral_img[i][j-1]-integral_img[i-1][j-1];
				}
			}
	
			return ArraySlicing.getSlice(integral_img, 1, 1, w, h);
		}
		
		
	// Calculate Haar Features
		public static float[][] extractHaarFeatures(float[][] i_img, int w, int h, HAAR_FILTERS FILTER, int rect_size){
			float[][] f_img;
			rect_size--; 
			switch(FILTER){
			case TWO_H: 
				System.out.println("TWO H FILTER");
				f_img = new float[h-rect_size][w-rect_size*2];
				for(int j =0 ; j < h-rect_size; j++) {
					for (int i = 0; i < w-rect_size*2; i++) {
						f_img[j][i] = i_img[j+rect_size][i+rect_size*2]  
								+ 2 * i_img[j][ i+rect_size] + i_img[j+rect_size][i] 
								- i_img[j][i+rect_size*2] - 2 * i_img[j+rect_size][i+rect_size] 
								- i_img[j][i];
					}
				}
				return f_img; 
			case TWO_V:
				f_img = new float[h-rect_size*2][w-rect_size];
				for(int j =0 ; j < h-rect_size*2; j++) {
					for (int i = 0; i < w-rect_size; i++) {
						f_img[j][i] = i_img[j+rect_size*2][i+rect_size]  + 2 * i_img[j+rect_size][i] - 
					              2 * i_img[j+rect_size][i+rect_size] - i_img[j+rect_size*2][i] + i_img[j][i+rect_size] -
					               i_img[j][i];
					}
				}
				return f_img;  
			case THREE: 
				f_img = new float[h-rect_size][w-rect_size*3];
				for(int j =0 ; j < h-rect_size; j++) {
					for (int i = 0; i < w-rect_size*3; i++) {
						f_img[j][i] = 2 * i_img[j][i+rect_size] + 2 * i_img[j+rect_size][i+rect_size*2]
						          + i_img[j+rect_size][i] + i_img[j][i+rect_size*3] - 2 * i_img[j][i+rect_size*2]
						          - 2 * i_img[j+rect_size][i+rect_size] - i_img[j][i] - i_img[j+rect_size][i+rect_size*3];
					}
				}
				return f_img; 
			default: 
				f_img = new float[h-rect_size*2][w-rect_size*2];
				for(int j =0 ; j < h-rect_size*2; j++) {
					for (int i = 0; i < w-rect_size*2; i++) {
						f_img[j][i] = - i_img[j][i] + 2 * i_img[j][i+rect_size]  - i_img[j][i+rect_size*2]
						          + 2 *  i_img[j+rect_size][i] - 4 * i_img[j+rect_size][i+rect_size]  + 2* i_img[j+rect_size][i+rect_size*2]
						          -  i_img[j+rect_size*2][i] + 2 * i_img[j+rect_size*2][i+rect_size]  - i_img[j+rect_size*2][i+rect_size*2];
					}
				}
				return f_img;  
			
			}

		}
		
		
}
