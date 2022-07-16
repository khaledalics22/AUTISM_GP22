package face_loc;

public class ArraySlicing {

	public static float[][] getSlice(float[][] img, int x, int y ,int w, int h){
		
		float[][] slice = new float[h][w]; 
		for(int i = x ; i< x+h;i++) {
			for (int j = y; j < y+w; j++) {
				slice[i-x][j-y] = img[i][j];
			}
		}
		
		return slice; 
	}
}
