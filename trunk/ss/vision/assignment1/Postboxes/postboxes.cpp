#ifdef _CH_
#pragma package <opencv>
#endif

#include <stdio.h>
#include <stdlib.h>
#include "cv.h"
#include "highgui.h"
#include "../utilities.h"

#define VARIATION_ALLOWED_IN_PIXEL_VALUES 30
#define ALLOWED_MOTION_FOR_MOTION_FREE_IMAGE 1.0
#define NUMBER_OF_POSTBOXES 6
#define MINIMUM_GRADIENT_VALUE 15 
int PostboxLocations[NUMBER_OF_POSTBOXES][5] = {
                                {   6,  73,  95, 5, 92 }, {   6,  73,  95, 105, 192 },
                                { 105, 158, 193, 5, 92 }, { 105, 158, 193, 105, 192 },
                                { 204, 245, 292, 5, 92 }, { 204, 245, 292, 105, 192 } };

//Old redcount method
//int number_points[NUMBER_OF_POSTBOXES] = {0, 0, 0, 0, 0,0}; //The number of red points (edges) for each postbox in the first image
//int thresholds[NUMBER_OF_POSTBOXES] = {20, 20, 40, 40, 200, 200}; //Thresholds for each postbox

//Better line counting method
int number_lines[NUMBER_OF_POSTBOXES]; //Number of edges in each postbox in first image

#define POSTBOX_TOP_ROW 0
#define POSTBOX_TOP_BASE_ROW 1
#define POSTBOX_BOTTOM_ROW 2
#define POSTBOX_LEFT_COLUMN 3
#define POSTBOX_RIGHT_COLUMN 4
#define MASK_SIZE 3

#define MAX_LINE_LENGTH 10
#define LINE_AMOUNT_VAR 3

IplImage * prev_frame;


void indicate_post_in_box( IplImage* image, int postbox )
{
	write_text_on_image(image,(PostboxLocations[postbox][POSTBOX_TOP_ROW]+PostboxLocations[postbox][POSTBOX_BOTTOM_ROW])/2,PostboxLocations[postbox][POSTBOX_LEFT_COLUMN]+2, "Post in");
	write_text_on_image(image,(PostboxLocations[postbox][POSTBOX_TOP_ROW]+PostboxLocations[postbox][POSTBOX_BOTTOM_ROW])/2+19,PostboxLocations[postbox][POSTBOX_LEFT_COLUMN]+2, "this box");
}

//Follow a line segment and blank it out. 
//Recursive function. Never gets called more than 20+ times so space is not a problem
int follow_line(IplImage * input_image, int row, int col, int length){

	int width_step= input_image->widthStep;
	int pixel_step= input_image->widthStep/input_image->width;
	int number_channels=input_image->nChannels;

	//Blank off point
	unsigned char * start_point = GETPIXELPTRMACRO(input_image, col, row, width_step, pixel_step);
	start_point[RED_CH] = 0;

	int i, j;

	//Look for the next pixel in the line
	for(i = -1; i < 2; i++){
			for(j = -1; j < 2; j++){
			unsigned char * curr_point = GETPIXELPTRMACRO( input_image, (col + j), (row + i), width_step, pixel_step);
			if(curr_point[RED_CH] == 255){
				length += 1;
				length = follow_line(input_image, (row + i), (col + j), length);
			}
		}
	}
	//If end of line reached, bottom out and return the length
	return length;
}
//Count the number of lines in a given image segment.
/*Lines are filtered on length. If they are two small they are considered noise and discarded. Here the minimum length is quite long
	given the dimensions of the image. This is because we just want the long straight edges of the black tape
*/
int count_lines(IplImage * input_image, int x, int y, int width, int height){
	int row, col;
	int i, j;
	IplImage * temp = cvCloneImage(input_image);


	int result = 0;
	int width_step= input_image->widthStep;
	int pixel_step= input_image->widthStep/input_image->width;
	int number_channels=input_image->nChannels;


	for(row = x; row <= height; row++){
		for(col = y; col <= width; col++){
			unsigned char * point = GETPIXELPTRMACRO(temp, col, row, width_step, pixel_step);
			//If edge, follow it, deleting each point so we don't follow it twice. Then count it. 
			if(point[RED_CH] == 255){
				//Follow the line and get it's length, discard lines that are too small
				int length = follow_line(temp, row, col, 1);
				if(length > MAX_LINE_LENGTH){
					result++;
				}

			}
		}
	}
	//cvShowImage("Debug", temp);		


	cvReleaseImage(&temp);
	return result;
}

//Run non-maxima over an image region
//Could do for an image region, but for a single postbox is quicker. Override if really needed
void non_maxima_suppres(IplImage * input_image, int postbox, int width_step, int pixel_step){
	int row, col;
	for(row = PostboxLocations[postbox][POSTBOX_TOP_BASE_ROW]; row <= PostboxLocations[postbox][POSTBOX_BOTTOM_ROW]; row++){
			for(col = PostboxLocations[postbox][POSTBOX_LEFT_COLUMN]; col <= PostboxLocations[postbox][POSTBOX_RIGHT_COLUMN]; col++){
				//Could do some fancy pointer manipulation here, but can't depend on memory alignment.
				unsigned char * current_point = GETPIXELPTRMACRO(input_image, col, row, width_step, pixel_step);
				//Consider the two points to the left and right. Should use a loop to do this but it came out this way and now we all just have to live with it.
				unsigned char * left_point = GETPIXELPTRMACRO(input_image, col -1, row, width_step, pixel_step);
				unsigned char * right_point = GETPIXELPTRMACRO(input_image, col +1, row, width_step, pixel_step);

				unsigned char * left_point2 = GETPIXELPTRMACRO(input_image, col -2, row, width_step, pixel_step);
				unsigned char * right_point2 = GETPIXELPTRMACRO(input_image, col +2, row, width_step, pixel_step);
				//printf("%d %d %d\n", left_point[RED_CH], current_point[RED_CH], right_point[RED_CH]);
				//Well this if statement turned out hideous
				if( (current_point[RED_CH] == 0 ) || (current_point[RED_CH] < left_point[RED_CH]) || (current_point[RED_CH] < right_point[RED_CH]) || (current_point[RED_CH] < left_point2[RED_CH]) || (current_point[RED_CH] < right_point2[RED_CH]) ){
					current_point[RED_CH] = 0;

				}else{
					current_point[RED_CH] = 255;
					//redcount++;
				}			
			}
		}
}

/*
First iterate through each post box and the determine the partial first derivative of each point each box. Then run non-maxima suppression on each postbox
Lastly, count the number of lines in each postbox and set the values found as the initial number of edges in each postbox. This initial thresholding could
be rented out to another method, but it's handy to do it here
*/

void compute_vertical_edge_image(IplImage* input_image, IplImage* output_image)
{
	// TO-DO:  Compute the partial first derivative edge image in order to locate the vertical edges in the passed image,
	//   and then determine the non-maxima suppressed version of these edges (along each row as the rows can be treated
	//   independently as we are only considering vertical edges). Output the non-maxima suppressed edge image. 
	// Note:   You may need to smooth the image first.
	
	//Don't forget to free please	
	IplImage* grayscale_image = cvCreateImage( cvGetSize(input_image), 8, 1 );
	cvConvertImage( input_image, grayscale_image );
	
	//Having the same src and dest might make a bit of a mess of thing but does the job here
	IplImage * temp = cvCloneImage(grayscale_image);
	cvSmooth(temp, grayscale_image);
    //cvShowImage( "Debug", grayscale_image );
	cvZero( output_image);

	int row, col;
	int i, j; //Mask iterators
	
	int width_step= input_image->widthStep;
	int pixel_step= input_image->widthStep/input_image->width;
	int number_channels=input_image->nChannels;

	unsigned char black[4] = {0, 0, 0, 0};

	int gray_width_step = grayscale_image->widthStep;
	int gray_pixel_step = grayscale_image->widthStep/grayscale_image->width;
	int gray_number_channels = grayscale_image->nChannels;

	//int mask[3][3] = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}}; //sobel
	//int mask[3][3] = {{-1, 0 , 1}, {-1, 0, 1}, {-1, 0, 1}}; //Prewitt
	int mask[3][3] = {{ 0, 0, 0}, {-1, 0, 1}, {0, 0, 0}}; //Funzo, this mask gives a nice clean edge. As we have smoothed the image, noise is no longer really an issue
														  // so we no longer need to calculate the rate of change for neighboring pixels
	//Get pixel at each point in mask, mul by value in mask and sum the result
	//Only run edge detection inside the postboxes, ignore the rest of the image
	int postbox;
	for(postbox = 0; postbox < NUMBER_OF_POSTBOXES; postbox++){
		for(row = PostboxLocations[postbox][POSTBOX_TOP_BASE_ROW]; row <= PostboxLocations[postbox][POSTBOX_BOTTOM_ROW]; row++){
			for(col = PostboxLocations[postbox][POSTBOX_LEFT_COLUMN]; col <= PostboxLocations[postbox][POSTBOX_RIGHT_COLUMN]; col++){
				unsigned char * result_point = GETPIXELPTRMACRO( output_image, col, row, width_step, pixel_step);
				//Could just use the i and j values, but using mask array makes it more explicit what im doing
				//Iterate through the neighboring pixels and multiply them by their corresponding mask value, summing the results
				int sum = 0;
				for(i = -1; i < 2; i++){
					for(j = -1; j < 2; j++){
						unsigned char * curr_point = GETPIXELPTRMACRO( grayscale_image, (col + j), (row + i), gray_width_step, gray_pixel_step);
						sum += curr_point[0] * mask[i + 1][j + 1];
					}
				}
				unsigned char x = abs(sum);
				if( x > MINIMUM_GRADIENT_VALUE){
					result_point[RED_CH] = abs(sum);
				}
			
			}
		}
		
	}
	//Do the non-maxima suppression here.
	//I presume by along each row we need only consider the points to the left and right of a pixel.
	for(postbox = 0; postbox < NUMBER_OF_POSTBOXES; postbox++){
		//int redcount = 0;
		non_maxima_suppres(output_image, postbox, width_step, pixel_step);
		//if first frame
		if(number_lines[postbox] == 0){
			number_lines[postbox] = count_lines(output_image, PostboxLocations[postbox][POSTBOX_TOP_BASE_ROW], PostboxLocations[postbox][POSTBOX_LEFT_COLUMN], PostboxLocations[postbox][POSTBOX_RIGHT_COLUMN], PostboxLocations[postbox][POSTBOX_BOTTOM_ROW]);
		}
	}	
	cvReleaseImage(&temp);
	cvReleaseImage(&grayscale_image);
}


/*
Does exactly what is described in the to-do
*/
bool motion_free_frame(IplImage* input_image, IplImage* previous_frame)
{
	// TO-DO:  Determine the percentage of the pixels which have changed (by more than VARIATION_ALLOWED_IN_PIXEL_VALUES)
	//        and return whether that percentage is less than ALLOWED_MOTION_FOR_MOTION_FREE_IMAGE.

	IplImage* grayscale_image = cvCreateImage( cvGetSize(input_image), 8, 1 );
	cvConvertImage( input_image, grayscale_image );

	int row, col;
	int movement = 0;
	int total = 0;

	int width_step = grayscale_image->widthStep;
	int pixel_step = grayscale_image->widthStep/grayscale_image->width;
	int number_channels = grayscale_image->nChannels;

	for( row = 0; row < input_image->height; row++){
		for( col = 0; col < input_image->width; col++){
			int curr_point = *GETPIXELPTRMACRO( grayscale_image, col, row , width_step, pixel_step);
			int prev_point = *GETPIXELPTRMACRO( previous_frame, col, row , width_step, pixel_step);
			if( abs(curr_point - prev_point) > VARIATION_ALLOWED_IN_PIXEL_VALUES){
				movement++;
			}
			//Only need to do this once, optimize later
			total++;
		}
		
	}
	prev_frame = cvCloneImage(grayscale_image);
//	cvReleaseImage(&grayscale_image);
	if( ( (float) ((float)movement/(float)total)*100) > ALLOWED_MOTION_FOR_MOTION_FREE_IMAGE){
		return false;
	}
	return true;  // Just to allow the system to compile while the code is missing.
}

/*
Exactly as described in the to-do. The number of edges in each postbox are counted and compared the value in the first frame of the image
*/
void check_postboxes(IplImage* input_image, IplImage* labelled_output_image, IplImage* vertical_edge_image )
{
	// TO-DO:  If the input_image is not motion free then do nothing.  Otherwise determine the vertical_edge_image and check
	//        each postbox to see if there is mail (by analysing the vertical edges).  Highlight the edge points used during your
	//        processing.  If there is post in a box indicate that there is on the labelled_output_image.
	
	//Passing in a global, keeps the method reusable
	if(motion_free_frame(input_image, prev_frame)){
		compute_vertical_edge_image(input_image, vertical_edge_image);

		//iterate through the postboxes an examine them
		int postbox, row, col;

		int width_step= vertical_edge_image->widthStep;
		int pixel_step= vertical_edge_image->widthStep/vertical_edge_image->width;
		
		//Iterate through all the postboxes
		for(postbox = 0; postbox < NUMBER_OF_POSTBOXES; postbox++){
			//Count the number of lines in each postbox
			int numLines = count_lines(vertical_edge_image, PostboxLocations[postbox][POSTBOX_TOP_BASE_ROW], PostboxLocations[postbox][POSTBOX_LEFT_COLUMN], PostboxLocations[postbox][POSTBOX_RIGHT_COLUMN], PostboxLocations[postbox][POSTBOX_BOTTOM_ROW]);

			//If the number of lines decrease sufficiently, assume post in box. The > case never succeeds in this example but make the algo more robust
			if(numLines < number_lines[postbox] - LINE_AMOUNT_VAR || numLines > number_lines[postbox] + LINE_AMOUNT_VAR){
				indicate_post_in_box(labelled_output_image, postbox);
			}

		}
	}
	
}


int main( int argc, char** argv )
{
    IplImage *input_image=NULL;
	CvSize size;
	size.height = 300; size.width = 200;
	IplImage *corrected_frame = cvCreateImage( size, IPL_DEPTH_8U, 3 );
	IplImage *labelled_image=NULL;
	IplImage *vertical_edge_image=NULL;
    int user_clicked_key=0;
    
    // Load the video (AVI) file
    CvCapture *capture = cvCaptureFromAVI( "./Postboxes.avi" );
    // Ensure AVI opened properly
    if( !capture )
		return 1;    
    
    // Get Frames Per Second in order to playback the video at the correct speed
    int fps = ( int )cvGetCaptureProperty( capture, CV_CAP_PROP_FPS );
    
	// Explain the User Interface
    printf( "Hot keys: \n"
		    "\tESC - quit the program\n"
            "\tSPACE - pause/resume the video\n");

	CvPoint2D32f from_points[4] = { {3, 6}, {221, 11}, {206, 368}, {18, 373} };
	CvPoint2D32f to_points[4] = { {0, 0}, {200, 0}, {200, 300}, {0, 300} };
	//Warping crops the image
	CvMat* warp_matrix = cvCreateMat( 3,3,CV_32FC1 );
	cvGetPerspectiveTransform( from_points, to_points, warp_matrix );

	// Create display windows for images
	cvNamedWindow( "Input video", 0 );
	cvNamedWindow( "Vertical edges", 0 );
    cvNamedWindow( "Results", 0 );
	
	// Setup mouse callback on the original image so that the user can see image values as they move the
	// cursor over the image.
    cvSetMouseCallback( "Input video", on_mouse_show_values, 0 );
	window_name_for_on_mouse_show_values="Input video";

    while( user_clicked_key != ESC ) {
		// Get current video frame
        input_image = cvQueryFrame( capture );
		image_for_on_mouse_show_values=input_image; // Assign image for mouse callback
        if( !input_image ) // No new frame available
			break;

		cvWarpPerspective( input_image, corrected_frame, warp_matrix );

		if (labelled_image == NULL)
		{	// The first time around the loop create the image for processing
			vertical_edge_image = cvCloneImage( corrected_frame );
			prev_frame = cvCreateImage( cvGetSize(input_image), 8, 1 );
			cvConvertImage( corrected_frame, prev_frame );

		}
		//Refresh labelled image each time so we can write on it and get rid of the writing too
		labelled_image = cvCloneImage( corrected_frame );
		check_postboxes(corrected_frame, labelled_image, vertical_edge_image );

		// Display the current frame and results of processing
        cvShowImage( "Input video", input_image );
        cvShowImage( "Vertical edges", vertical_edge_image );
        cvShowImage( "Results", labelled_image );
        
        // Wait for the delay between frames
        //Set to 500 just to speed up debugging
        user_clicked_key = (char) cvWaitKey( 1000 / fps );
		if (user_clicked_key == ' ')
		{
			user_clicked_key = (char) cvWaitKey(0);
		}
	}
    
    /* free memory */
    cvReleaseCapture( &capture );
    cvDestroyWindow( "video" );
 
    return 0;
}
