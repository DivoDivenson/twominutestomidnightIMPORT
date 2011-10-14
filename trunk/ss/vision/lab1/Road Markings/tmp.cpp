#ifdef _CH_
#pragma package <opencv>
#endif

#include "cv.h"
#include "highgui.h"
#include <stdio.h>
#include <stdlib.h>
#include "../utilities.h"


#define NUM_IMAGES 5

// Locate the red pixels in the source image.
void find_red_points( IplImage* source, IplImage* result, IplImage* temp )
{
	int width_step=source->widthStep;
	int pixel_step=source->widthStep/source->width;
	int n_channels=source->nChannels;

	double threshold = 70;

	cvZero(result);
	unsigned char white[4] = {255,255,255,0};
	int row = 0, col = 0;

	for(row=0; row < result->height; row++){
		for(col=0; col < result->width; col++){
			unsigned char * curr_point = GETPIXELPTRMACRO(source, col, row, width_step, pixel_step);
			//if((curr_point[RED_CH] <= threshold) && ((curr_point[BLUE_CH] < threshold) || (curr_point[GREEN_CH] < threshold))){
			
			//If the channels fluctuate suffecently then we have an image pixel (This eliminates the white background)
			if(( (curr_point[RED_CH] - 10) > curr_point[BLUE_CH]) || ((curr_point[RED_CH] - 10) > curr_point[GREEN_CH])){
				//Now analyse for red content. Want the red component to be a certain percentage greater then green and blue
				//After lots of looking at pixel values and playing around 5.95 was chosen.
				threshold = curr_point[RED_CH] - curr_point[RED_CH] / 5.95f;  //Found through experimentation. Seems a bit mad. Also makes the process very expensive
				if((curr_point[RED_CH] >= threshold) && ((curr_point[BLUE_CH] < threshold) || (curr_point[GREEN_CH] < threshold))){
					PUTPIXELMACRO( result, col, row, white, width_step, pixel_step, n_channels);
				}
			}
		}
	}

	cvMorphologyEx( result, temp, NULL, NULL, CV_MOP_OPEN, 1 );
	cvMorphologyEx( temp, result, NULL, NULL, CV_MOP_CLOSE, 1 );
}

CvSeq* connected_components( IplImage* source, IplImage* result )
{
	IplImage* binary_image = cvCreateImage( cvGetSize(source), 8, 1 );
	cvConvertImage( source, binary_image );
	CvMemStorage* storage = cvCreateMemStorage(0);
	CvSeq* contours = 0;
	cvThreshold( binary_image, binary_image, 1, 255, CV_THRESH_BINARY );
	cvFindContours( binary_image, storage, &contours, sizeof(CvContour),	CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE );
	if (result)
	{
		cvZero( result );
		for(CvSeq* contour = contours ; contour != 0; contour = contour->h_next )
		{
			CvScalar color = CV_RGB( rand()&255, rand()&255, rand()&255 );
			/* replace CV_FILLED with 1 to see the outlines */
			cvDrawContours( result, contour, color, color, -1, CV_FILLED, 8 );
		}
	}
	return contours;
}

//Tested, works 100%
void invert_image( IplImage* source, IplImage* result )
{
	// TO DO:  Write code to invert all points in the source image (i.e. for each channel for each pixel in the result
	//        image the value should be 255 less the corresponding value in the source image).
	int width_step=source->widthStep;
	int pixel_step=source->widthStep/source->width;
	int n_channels=source->nChannels;
	cvZero(result);
	

	int threshold = 70;

	cvZero(result);
	int row = 0, col = 0, chan = 0;
	unsigned char white[4] = {255,255,255,0};


	for(row=0; row < result->height; row++){
		for(col=0; col < result->width; col++){
			unsigned char * curr_point = GETPIXELPTRMACRO(source, col, row, width_step, pixel_step);
			for(chan = 0; chan < n_channels; chan++){
				curr_point[chan] = 255 - curr_point[chan];
			}
			PUTPIXELMACRO(result, col, row, curr_point, width_step, pixel_step, n_channels);
		}
	}

	
}

// Assumes a 1D histogram of 256 elements.
//From looking at the histogram we can see 3 very clear peaks
//TODO, check for divide by 0 properly
int determine_optimal_threshold( CvHistogram* hist )
{
	// TO DO:  Given a 1-D CvHistogram you need to determine and return the optimal threshold value.

	// NOTES:  Assume there are 256 elements in the histogram.
	//         To get the histogram value at index i
	//            int histogram_value_at_i = ((int) *cvGetHistValue_1D(hist, i));

	int i;
	int threshold = 127; //Start in the middle
	int thresholdNext = 127;// t+1
	int t=0;
	int background = 1; 
	int foreground = 1;
	int nobackpixels = 1;  //Number of backrgound pixels, 1 to avoid divide by zero error DO PROPER LATER
	//Sum of background and foreground
	do{
		nobackpixels = 1;
		background = 1;
		foreground = 1;
		threshold = thresholdNext;
		for(i=0; i < 256; i++){
			int value = ((int) *cvGetHistValue_1D(hist, i));
			if( i < threshold ){ //Backround pixel
				background += value * i;
				nobackpixels++;
			}else{ //foreground
				foreground += value * i;
			}
		}
		//printf("BACK %d\n", thresholdNext);
		//divided by number of background and foreground respectivly
		background = background / nobackpixels;
		foreground = foreground / ((255 - nobackpixels)+1); //Again, divide by zero error
		//printf("H %d %d\n", background, foreground);
		thresholdNext = (background + foreground) / 2;
	}while(threshold != thresholdNext);
	//printf("\n\n\n");
	printf("%d %d\n", thresholdNext, threshold);
	return thresholdNext;  // Just so that the project will compile...
}

void apply_threshold_with_mask(IplImage* grayscale_image,IplImage* result_image,IplImage* mask_image,int threshold)
{
	// TO DO:  Apply binary thresholding to those points in the passed grayscale_image which correspond to non-zero
	//        points in the passed mask_image.  The binary results (0 or 255) should be stored in the result_image.
	int row = 0;
	int col = 0;
	int width_step = grayscale_image->widthStep;
	int pixel_step = grayscale_image->widthStep / grayscale_image->width;
	int n_channels= grayscale_image->nChannels;
	//Just a quick check to see if I can use the same step for all images. Turns out I can. Spoke too soon
	//printf("%d %d %d\n", width_step, pixel_step, n_channels);

	int result_width_step = result_image->widthStep;
	int result_pixel_step = result_image->widthStep / result_image->width;
	int result_channels = result_image->nChannels;
	//cvZero(result_image); Already zeroed elsewhere
	unsigned char white[4] = {255,255,255,0};

	for(row = 0; row < grayscale_image->height; row++){
		for(col = 0; col < grayscale_image->width; col++){
			unsigned char * mask_point = GETPIXELPTRMACRO(mask_image, col, row, width_step, pixel_step);
			if(mask_point[0] != 0){ //Taken as 255 means apply the mask
				//printf("%d\n", mask_point[0]);
				unsigned char * grey_point = GETPIXELPTRMACRO(grayscale_image, col, row, width_step, pixel_step);
				//If greater than or equal to thresh we want the pixel
				if(grey_point[0] >= threshold){
					PUTPIXELMACRO(result_image, col, row, white, result_width_step, result_pixel_step, result_channels);
				}
			}
		}
	}
}

void determine_optimal_sign_classification( IplImage* original_image, IplImage* red_point_image, CvSeq* red_components, CvSeq* background_components, IplImage* result_image )
{
	int width_step=original_image->widthStep;
	int pixel_step=original_image->widthStep/original_image->width;
	IplImage* mask_image = cvCreateImage( cvGetSize(original_image), 8, 1 );
	IplImage* grayscale_image = cvCreateImage( cvGetSize(original_image), 8, 1 );
	cvConvertImage( original_image, grayscale_image );
	IplImage* thresholded_image = cvCreateImage( cvGetSize(original_image), 8, 1 );
	cvZero( thresholded_image );
	cvZero( result_image );
	int row=0,col=0;
	CvSeq* curr_red_region = red_components;
	// For every connected red component
	while (curr_red_region != NULL)
	{
		cvZero( mask_image );
		CvScalar color = CV_RGB( 255, 255, 255 );
		CvScalar mask_value = cvScalar( 255 );
		// Determine which background components are contained within the red component (i.e. holes)
		//  and create a binary mask of those background components.
		CvSeq* curr_background_region = curr_red_region->v_next;
		if (curr_background_region != NULL)
		{
			while (curr_background_region != NULL)
			{
				cvDrawContours( mask_image, curr_background_region, mask_value, mask_value, -1, CV_FILLED, 8 );
				cvDrawContours( result_image, curr_background_region, color, color, -1, CV_FILLED, 8 );
				curr_background_region = curr_background_region->h_next;
			}
			int hist_size=256;
			CvHistogram* hist = cvCreateHist( 1, &hist_size, CV_HIST_ARRAY );
			cvCalcHist( &grayscale_image, hist, 0, mask_image );
			// Determine an optimal threshold on the points within those components (using the mask)
			int optimal_threshold = determine_optimal_threshold( hist );
			apply_threshold_with_mask(grayscale_image,result_image,mask_image,optimal_threshold);
		}
		curr_red_region = curr_red_region->h_next;
		
	}

	for (row=0; row < result_image->height; row++)
	{
		unsigned char* curr_red = GETPIXELPTRMACRO( red_point_image, 0, row, width_step, pixel_step );
		unsigned char* curr_result = GETPIXELPTRMACRO( result_image, 0, row, width_step, pixel_step );
		for (col=0; col < result_image->width; col++)
		{
			curr_red += pixel_step;
			curr_result += pixel_step;
			if (curr_red[0] > 0)
				curr_result[2] = 255;
		}
	}

	cvReleaseImage( &mask_image );
}

int main( int argc, char** argv )
{
	int selected_image_num = 1;
	char show_ch = 's';
	IplImage* images[NUM_IMAGES];
	IplImage* selected_image = NULL;
	IplImage* temp_image = NULL;
	IplImage* red_point_image = NULL;
	IplImage* connected_reds_image = NULL;
	IplImage* connected_background_image = NULL;
	IplImage* result_image = NULL;
	CvSeq* red_components = NULL;
	CvSeq* background_components = NULL;

	// Load all the images.
	for (int file_num=1; (file_num <= NUM_IMAGES); file_num++)
	{
		if( (images[0] = cvLoadImage("./RealRoadSigns.jpg",-1)) == 0 )
			return 0;
		if( (images[1] = cvLoadImage("./RealRoadSigns2.jpg",-1)) == 0 )
			return 0;
		if( (images[2] = cvLoadImage("./ExampleRoadSigns.jpg",-1)) == 0 )
			return 0;
		if( (images[3] = cvLoadImage("./Parking.jpg",-1)) == 0 )
			return 0;

		if( (images[4] = cvLoadImage("./NoParking.jpg",-1)) == 0 )
			return 0;
	}

	// Explain the User Interface
    printf( "Hot keys: \n"
            "\tESC - quit the program\n"
			"\t1 - Real Road Signs (image 1)\n"
			"\t2 - Real Road Signs (image 2)\n"
			"\t3 - Synthetic Road Signs\n"
			"\t4 - Synthetic Parking Road Sign\n"
			"\t5 - Synthetic No Parking Road Sign\n"
			"\tr - Show red points\n"
			"\tc - Show connected red points\n"
			"\th - Show connected holes (non-red points)\n"
			"\ts - Show optimal signs\n"
			);
    
	// Create display windows for images
    cvNamedWindow( "Original", 1 );
    cvNamedWindow( "Processed Image", 1 );

	// Setup mouse callback on the original image so that the user can see image values as they move the
	// cursor over the image.
    cvSetMouseCallback( "Original", on_mouse_show_values, 0 );
	window_name_for_on_mouse_show_values="Original";
	image_for_on_mouse_show_values=selected_image;

	int user_clicked_key = 0;
	do {
		// Create images to do the processing in.
		if (red_point_image != NULL)
		{
			cvReleaseImage( &red_point_image );
			cvReleaseImage( &temp_image );
			cvReleaseImage( &connected_reds_image );
			cvReleaseImage( &connected_background_image );
			cvReleaseImage( &result_image );
		}
		selected_image = images[selected_image_num-1];
		red_point_image = cvCloneImage( selected_image );
		result_image = cvCloneImage( selected_image );
		temp_image = cvCloneImage( selected_image );
		connected_reds_image = cvCloneImage( selected_image );
		connected_background_image = cvCloneImage( selected_image );

		// Process image
		image_for_on_mouse_show_values = selected_image;
		find_red_points( selected_image, red_point_image, temp_image );
		red_components = connected_components( red_point_image, connected_reds_image );
		invert_image( red_point_image, temp_image );
		background_components = connected_components( temp_image, connected_background_image );
		determine_optimal_sign_classification( selected_image, red_point_image, red_components, background_components, result_image );

		// Show the original & result
        cvShowImage( "Original", selected_image );
		do {
			if ((user_clicked_key == 'r') || (user_clicked_key == 'c') || (user_clicked_key == 'h') || (user_clicked_key == 's'))
				show_ch = user_clicked_key;
			switch (show_ch)
			{
			case 'c':
				cvShowImage( "Processed Image", connected_reds_image );
				break;
			case 'h':
				cvShowImage( "Processed Image", connected_background_image );
				break;
			case 'r':
				cvShowImage( "Processed Image", red_point_image );
				break;
			case 's':
			default:
				cvShowImage( "Processed Image", result_image );
				break;
			}
	        user_clicked_key = (char)cvWaitKey(0);
		} while ((!((user_clicked_key >= '1') && (user_clicked_key <= '0'+NUM_IMAGES))) &&
			     ( user_clicked_key != ESC ));

		if ((user_clicked_key >= '1') && (user_clicked_key <= '0'+NUM_IMAGES))
		{
			selected_image_num = user_clicked_key-'0';
		}

	} while ( user_clicked_key != ESC );

    return 1;
}
