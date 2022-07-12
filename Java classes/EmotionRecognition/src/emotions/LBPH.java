/* Online Java Compiler and Editor */
import java.util.Random;
public class HelloWorld{
 public static void main(String []args){
     
    Random rand = new Random(); //instance of random class
    int upperbound = 255;

    int dim = 48;
    int[][] img1 = new int[dim][dim];
    for (int i =0; i < dim; i++ )
        for (int j =0; j < dim; j++ )
            img1[i][j] = rand.nextInt(upperbound); 
            
    int[][] lbp = get_LBP(img1, 8, 2);
    
    float[] hist = get_LBP_Hist(lbp);
    
    printMatrix(img1);
    System.out.println("llHeo, World!ll");
    printMatrix(lbp);
    System.out.println("llHeo, World!ll");
    for(float val:hist){
        System.out.print(val);
        System.out.print("--");
    }

 }
 private static int[][] get_LBP(int[][] img, int neighbors, int radius) {
    int width, height;
    if (neighbors == 8) {
        height = img.length - 2 * radius;
        width = img[0].length - 2 * radius;
    } else {
        return null;
    }
    int[][] lbp_img = new int[height][width];
    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            lbp_img[i][j] = 0;
        }
    }

    for(int n=0; n<neighbors; n++) {
        // sample points
        float x = (float)(radius * Math.cos(2.0*Math.PI*n/(float)(neighbors)));
        float y = (float)(-radius * Math.sin(2.0*Math.PI*n/(float)(neighbors)));
        // relative indices
        int fx = (int)(Math.floor(x));
        int fy = (int)(Math.floor(y));
        int cx = (int)(Math.ceil(x));
        int cy = (int)(Math.ceil(y));
        // fractional part
        float ty = y - fy;
        float tx = x - fx;
        // set interpolation weights
        float w1 = (1 - tx) * (1 - ty);
        float w2 =      tx  * (1 - ty);
        float w3 = (1 - tx) *      ty;
        float w4 =      tx  *      ty;
        // iterate through your data
        for(int i=radius; i < img.length-radius;i++) {
            for(int j=radius;j < img[0].length-radius;j++) {
                // calculate interpolated value
                float t = (float)(w1*img[i+fy][j+fx] + w2*img[i+fy][j+cx] + w3*img[i+cy][j+fx] + w4*img[i+cy][j+cx]);
                // floating point precision, so check some machine-dependent epsilon
                int temp = (((t >= img[i][j]) || (Math.abs(t-img[i][j]) < 0))) ? 1 : 0 ;
                lbp_img[i-radius][j-radius] += temp << n;
            }
        }
    }
    return lbp_img;
}
public static void printMatrix(int[][] arr) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = 0; j < arr[0].length; j++) {
            System.out.print(arr[i][j]);
            System.out.print("--");
        }
        System.out.println("LL");
        
    }
}
private static float [] get_LBP_Hist(int[][] lbp_img){
        int[][] weights = new int[][]{
            {0,1,1,1,1,0},
            {2,2,2,2,2,2},
            {2,4,4,4,4,2},
            {1,1,2,2,1,1},
            {1,2,4,4,2,1},
            {0,1,2,2,1,0},
        };
        int hist_count = 0;
        int [] pins_size = new int[]{1,64,64,0,64};
        int num_regions=weights.length;
        for(int i=0; i<num_regions; i++)
            for(int j=0; j<num_regions; j++)
                hist_count += pins_size[weights[i][j]];
        
        float [] hist = new float [hist_count];
        int index_to_add = 0;
        for (int i_region = 0; i_region < num_regions; i_region ++){
            for (int j_region = 0; j_region < num_regions; j_region ++){
                int weight = weights[i_region][j_region];
                int pin_size = pins_size[weight];
                
                if(pin_size == 1){
                    hist[index_to_add] = 0;
                    index_to_add++;
                } 
                else{
                    // loop over lbp image grid by grid
                    int [] subhist = new int [pin_size];
                    for(int k=0; k < pin_size; k++)
                        subhist[k] = 0;
                    
                    for(int k=i_region* (int)((lbp_img.length)/num_regions);k<(i_region+1)*(int)((lbp_img.length)/num_regions);k++){
                        for(int l=j_region* (int)((lbp_img[0].length)/num_regions);l<(j_region+1)*(int)((lbp_img[0].length)/num_regions);l++){

                            subhist[(int)(pin_size*lbp_img[k][l]/256)] += 1;
                        }
                    }
                    

                    float sum = (float)((int)((lbp_img.length)/num_regions) * (int)((lbp_img.length)/num_regions));
                    for(int k=0; k < pin_size; k++){
                        hist[index_to_add] = subhist[k] / sum * (float)weight;
                        index_to_add += 1;
                    }
                }
            }
        }
    return hist;
}


}
