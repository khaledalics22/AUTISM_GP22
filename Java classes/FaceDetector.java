package face_loc;
import face_loc.ArraySlicing;
import face_loc.ImageProcessing;
public class FaceDetector {
	private static float[][] img = {{1,2,3,4,5,6,7,8,8,9},
			{1,2,3,4,5,6,7,8,8,9},
			{1,2,3,4,5,6,7,8,8,9},
			{1,2,3,4,5,6,7,8,8,9},
			{1,2,3,4,5,6,7,8,8,9},
			{1,2,3,4,5,6,7,8,8,9},
			{1,2,3,4,5,6,7,8,8,9}};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			float[][] i_img = ImageProcessing.getIntegralImg(img, 10, 7);
			float[][] slice = ImageProcessing.extractHaarFeatures(i_img, 10, 7, ImageProcessing.HAAR_FILTERS.TWO_H, 3);
			for(int i = 0; i< 7-3; i++) {
				for(int j= 0; j < 10-6; j++) {
					System.out.print(slice[i][j]);
					System.out.print("\t");
				}
				System.out.println(""); 
			}
	}

}
