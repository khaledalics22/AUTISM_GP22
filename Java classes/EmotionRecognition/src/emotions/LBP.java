package emotions;

import java.util.ArrayList;
import java.util.TreeMap;

public class LBP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		extractFeatures(null); 
	}
	private static  Object extractFeatures(int[][] img) {
        int[][] img1 = new int[][]{{1,2,2,2,2,2,2},{5,6,4,5,6,7,4},{5,3,1,6,4,3,2},{5,3,1,6,4,3,2},{5,3,1,6,4,3,2},{5,3,1,6,4,3,2},{5,3,1,6,4,3,2}};
        int[][] lbp = get_LBP(img1, 8);
        printMatrix(lbp); 
        ArrayList<Integer> hist = get_LBP_Hist(lbp, 2);

        for(int val :hist) {
        System.out.println(val); 
        }
        return null;
    }
	public static void printMatrix(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }
    }
	private static ArrayList<Integer> get_LBP_Hist(int[][] lbp_img, int grid_size){
        ArrayList<Integer> hist = new ArrayList<>();
        // loop over lbp image grid by grid
        for(int i = 0; i <= lbp_img.length - grid_size; i+=grid_size){
            for(int j= 0; j <= lbp_img[0].length - grid_size; j+= grid_size){

                // get hist of each grid
                TreeMap<Integer,Integer> m = new TreeMap<>();
                for(int k = i; k < i + grid_size;k ++){
                    for(int l = j; l < j+grid_size; l++){
       
                            Integer val = m.get(lbp_img[k][l]); 
                            if(val != null) {
                                m.put(lbp_img[k][l], val + 1);
                            }else
                                m.put(lbp_img[k][l],  1);
                    }
                }
                // add new grid hists
                hist.addAll(m.values());
            }
        }
        return hist;
    }
    private static int[][] get_LBP(int[][] img, int neighbors) {
        int width, height;
        if (neighbors == 8) {
            height = img.length - 2;
            width = img[0].length - 2;
        } else {
            return null;
        }
        int[][] lbp_img = new int[height][width];
        char code = 0;
        for (int i = 1; i < height + 1; i++) {
            for (int j = 1; j < width + 1; j++) {
                code |= ((img[i - 1][j - 1] >= img[i][j]) ? 1 : 0) << 7;
                code |= ((img[i - 1][j] >= img[i][j]) ? 1 : 0) << 6;
                code |= ((img[i - 1][j + 1] >= img[i][j]) ? 1 : 0) << 5;
                code |= ((img[i][j + 1] >= img[i][j]) ? 1 : 0) << 4;
                code |= ((img[i + 1][j + 1] >= img[i][j]) ? 1 : 0) << 3;
                code |= ((img[i + 1][j] >= img[i][j]) ? 1 : 0) << 2;
                code |= ((img[i + 1][j - 1] >= img[i][j]) ? 1 : 0) << 1;
                code |= ((img[i][j - 1] >= img[i][j]) ? 1 : 0);
                lbp_img[i - 1][j - 1] = code;
            }
        }
        return lbp_img;
    }

}
