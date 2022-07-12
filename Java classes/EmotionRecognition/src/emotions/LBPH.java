
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
