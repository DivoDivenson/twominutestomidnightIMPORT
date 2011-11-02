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
#define MINIMUM_GRADIENT_VALUE 5 //Is there something wrong with this value?
int PostboxLocations[NUMBER_OF_POSTBOXES][5] = {
                                {   6,  73,  95, 5, 92 }, {   6,  73,  95, 105, 192 },
                                { 105, 158, 193, 5, 92 }, { 105, 158, 193, 105, 192 },
                                { 204, 245, 292, 5, 92 }, { 204, 245, 292, 105, 192 } };
#define POSTBOX_TOP_ROW 0
#define POSTBOX_TOP_BASE_ROW 1
#define POSTBOX_BOTTOM_ROW 2
#define POSTBOX_LEFT_COLUMN 3
#define POSTBOX_RIGHT_COLUMN 4
#define MASK_SIZE 3

IplImage * prev_frame;


void indicate_post_in_box( IplImage* image, int postbox )
{
	write_text_on_image(image,(PostboxLocations[postbox][POSTBOX_TOP_ROW]+PostboxLocations[postbox][POSTBOX_BOTTOM_ROW])/2,PostboxLocations[postbox][POSTBOX_LEFT_COLUMN]+2, "Post in");
	write_text_on_image(image,(PostboxLocations[postbox][POSTBOX_TOP_ROW]+PostboxLocations[postbox][POSTBOX_BOTTOM_ROW])/2+19,PostboxLocations[postbox][POSTBOX_LEFT_COLUMN]+2, "this box");
}

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
	cvSmooth(temp, grayscale_image, CV_GAUSSIAN);
    //cvShowImage( "Debug", grayscale_image );
	cvZero( output_image);

	int row, col;
	int i, j; //Mask iterators
	int sum =0;
	
	int width_step= input_image->widthStep;
	int pixel_step= input_image->widthStep/input_image->width;
	int number_channels=input_image->nChannels;

	int gray_width_step = grayscale_image->widthStep;
	int gray_pixel_step = grayscale_image->widthStep/grayscale_image->width;
	int gray_number_channels = grayscale_image->nChannels;

	int mask[3][3] = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
	//Get pixel at each point in mask, mul by value in mask and sum the result
	//Put the edges in later
	for(row = 1; row < grayscale_image->height - 1; row++){
		for(col = 1; col < grayscale_image->width - 1; col++){
			//unsigned char * curr_point = GETPIXELPTRMACRO( _image, col, row, width_step, pixel_step);
			unsigned char * result_point = GETPIXELPTRMACRO( output_image, col, row, width_step, pixel_step);
			//Might as well get the average and the standard dev at the same time.
			//Put the edges in later
			//Could just use the i and j values, but using mask array makes it more explcit what im doing
			for(i = -1; i < 2; i++){
				for(j = -1; j < 2; j++){
					unsigned char * curr_point = GETPIXELPTRMACRO( grayscale_image, (col + j), (row + i), gray_width_step, gray_pixel_step);
					sum += curr_point[0] * mask[i + 1][j + 1];	
					//printf("%d x %d : ", mask[i + 1][j + 1], curr_point[0]);	
				}
				//printf("\n");
			}
			//printf("%d\n", sum);
			if( abs(sum) > MINIMUM_GRADIENT_VALUE + 50 ){
				result_point[RED_CH] = 255;
			}
			sum = 0;
		}
	//printf("Hello %d\n", row);
	}
	cvReleaseImage(&temp);
	cvReleaseImage(&grayscale_image);
}

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
	cvShowImage("Debug", prev_frame);		
	prev_frame = cvCloneImage(grayscale_image);
//	cvReleaseImage(&grayscale_image);
	if( ( (float) ((float)movement/(float)total)*100) > ALLOWED_MOTION_FOR_MOTION_FREE_IMAGE){
		return false;
	}
	return true;  // Just to allow the system to compile while the code is missing.
}

void check_postboxes(IplImage* input_image, IplImage* labelled_output_image, IplImage* vertical_edge_image )
{
	// TO-DO:  If the input_image is not motion free then do nothing.  Otherwise determine the vertical_edge_image and check
	//        each postbox to see if there is mail (by analysing the vertical edges).  Highlight the edge points used during your
	//        processing.  If there is post in a box indicate that there is on the labelled_output_image.
	//Passing in a global, keeps the method reusable
	if(motion_free_frame(input_image, prev_frame)){
		compute_vertical_edge_image(input_image, vertical_edge_image);
	}else{
		printf("Motion in frame");
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
	cvNamedWindow( "Debug", 0);
	
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
			labelled_image = cvCloneImage( corrected_frame );
			vertical_edge_image = cvCloneImage( corrected_frame );
			prev_frame = cvCreateImage( cvGetSize(input_image), 8, 1 );
			//This is screwed up, fix later
			cvConvertImage( corrected_frame, prev_frame );

		}
		check_postboxes(corrected_frame, labelled_image, vertical_edge_image );

		// Display the current frame and results of processing
        cvShowImage( "Input video", input_image );
        cvShowImage( "Vertical edges", vertical_edge_image );
        cvShowImage( "Results", labelled_image );
        
        // Wait for the delay between frames
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
