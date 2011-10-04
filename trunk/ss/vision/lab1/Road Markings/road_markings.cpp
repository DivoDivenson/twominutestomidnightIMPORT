#ifdef _CH_
#pragma package <opencv>
#endif

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <highgui.h>
#include "cv.h"
#include "highgui.h"
#include "../utilities.h"

#define THRESHOLD 70

/*
TODO
Identify white areas. Read some more of the notes.
Analyze different frames (at asy 0% 25% 50% 75% 100% of the video).
The white should be bit brighter (say 30) then the average RGB value of the scene. This works
because it's shit out and the camera is pointed at the road.



*/
int max = 0;
int isGrey(unsigned char * pixel){
	int variation = 3;
	int result = 1;
	//If the RGB values are close together, class as grey
	//printf("%d\n",pixel[0]);
	if(pixel[1] < 100){
		result = 0;
	}
	
	/* HSV version 
	if( (pixel[2] > 70) && (pixel[1] < 20)){
		result = 1;
	}
	if(pixel[1] > max){
		max = pixel[1];
	}
	*/
	
	return result;
}

//Map value from one range to another, assuming min of both is 0
//value, current max, new max
float mapHistValue(int value, int curMax, int newMax){
	//float result = (newMax * (value / curMax));
	//F*ck c++
	float result = (float)newMax * ((float)((float)value / (float)curMax));
	return result;
	//return (float)5 / 6;
}


//Pass in the image you want to get the histogram off, result is the histogram
//Expects greyScale for the moment
/*
And then I looked in the docs. Was a fun little exercise.

*/
void drawHist(IplImage * image, IplImage * result){
	//Init
	int height = result->height;
	int width = result->width;
	int i;

	int max = 0;
	int histogram[256];


	int width_step=result->widthStep;
	int pixel_step=result->widthStep/result->width;
	int number_channels=result->nChannels;

	for(i = 0; i < 256; i++){
		histogram[i] = 0;
	}

	unsigned char white_pixel[4] = {255, 255, 255, 0};
	int row, col;



	//All the heights / widths are the same so they get swapped around a lot
	for(row=0; row < image->height; row++){
		for(col=0; col < image->width; col++){
			unsigned char* curr_point = GETPIXELPTRMACRO( image, col, row, width_step, pixel_step );
			histogram[curr_point[0]]++;			
		}
	}


	for(i = 0; i < 256; i++){
		if(histogram[i] > max) max = histogram[i];
		//printf("HIST at %d: %d\n",i, histogram[i]);
	}

	cvZero(result);

	for(col=0; col < 256; col++){

		//Loss of percision and all that
		int point = (int) mapHistValue(histogram[col], max, 256);
		//So for some reason I can't invert the histogram. Thanks c++
		//point = height - point;
		printf("%d : %d\n",col,  point);

		PUTPIXELMACRO( result, col*2, point, white_pixel, width_step, pixel_step, number_channels);
	}
	printf("Done\n");




}




// This routine creates a binary result image where the points are 255,255,255 when the corresponding
// source points are grey/white.  The rule for deciding which points are white/grey is very debatable.
// Should the minimum value be greater?  Should the ratio of max to min values in the point be allowed
// to vary more (or less)?
//Takes a greyscale image
void select_white_points( IplImage* source, IplImage* result )
{
	//IplImage * temp = cvCloneImage(source) ;
	//IplImage * temp = cvCreateImage( cvSize(source->width, source->height), IPL_DEPTH_8U, 1);
	//cvtGrey(source, temp);

	
	int width_step=source->widthStep;
	int pixel_step=source->widthStep/source->width;
	int number_channels=source->nChannels;

	

	cvZero(result);
	unsigned char white_pixel[4] = {255, 255, 255, 0};
	int row, col;



	//All the heights / widths are the same so they get swapped around a lot
	for(row=0; row < source->height; row++){
		for(col=0; col < source->width; col++){
			//CHange back to source
			unsigned char* curr_point = GETPIXELPTRMACRO( source, col, row, width_step, pixel_step );			
			//if ((curr_point[RED_CH] >= THRESHOLD) && ((curr_point[BLUE_CH] > THRESHOLD) || (curr_point[GREEN_CH] > THRESHOLD))){
			if(isGrey(curr_point)){
				//PUTPIXELMACRO( result, col, row, white_pixel, width_step, pixel_step, number_channels );
				PUTPIXELMACRO( result, col, row, white_pixel, width_step, pixel_step, number_channels );
			}
			
		}
	}



	/*cvMorphologyEx( result, temp, NULL, NULL, CV_MOP_OPEN, 1 );
	cvMorphologyEx( temp, result, NULL, NULL, CV_MOP_CLOSE, 1 );*/



}





//Why this doesn't work as a a function I do not know
void cvtGrey(IplImage * source, IplImage * result){
	result = cvCreateImage( cvSize(source->width, source->height), IPL_DEPTH_8U, 1);
	cvCvtColor(source, result, CV_RGB2GRAY ); 
}


int main( int argc, char** argv )
{

	IplImage * frame;
	IplImage * input = NULL; //Image to run processing on;
	IplImage * result = NULL;
	CvHistogram * histogram = NULL;
	IplImage * histImage = NULL;
	int key;

	CvCapture * capture = cvCaptureFromAVI("StayingInLane.avi");

	int fps = (int) cvGetCaptureProperty( capture, CV_CAP_PROP_FPS);

	//1 scales the window to the content size (I think)
	cvNamedWindow("Original",1 );
	cvNamedWindow("StayingInLane", 1);
	cvNamedWindow("Histogram", 1);

	cvSetMouseCallback( "Original", on_mouse_show_values, 0 );
	window_name_for_on_mouse_show_values="Original";

	float range[] = {0, 255};
	float * ranges[] = {range};
	int numBits = 256;


	//1048608 unicode? Who knows, it's space anyway.
	while(key != 1048689){
		//printf("key : %d\n", key);

		//Get frame to process
		frame = cvQueryFrame( capture );
		if(!frame) break;

		//Hack to pause video
		if(key == 1048608){
			key = cvWaitKey(0);
		}

		//input = cvCloneImage(frame);
		//Convert frame to greyscale for processing
		input =  cvCreateImage( cvSize(frame->width, frame->height), IPL_DEPTH_8U, 1);
		cvCvtColor(frame, input, CV_RGB2GRAY);

		//init result
		result = cvCloneImage( input );
		//result = cvCloneImage(input);
		select_white_points(input, result);
		image_for_on_mouse_show_values=input;

		histogram = cvCreateHist(1, &numBits, CV_HIST_ARRAY, ranges, 1);
		cvCalcHist(&input, histogram, 0 , 0);
		IplImage * histImage = DrawHistogram(histogram);

		//drawHist(input, histogram);
		cvShowImage("Histogram", histImage);

		cvShowImage("StayingInLane", result);
		cvShowImage("Original", frame);
		key = cvWaitKey(1000 / fps);
	}
		//printf("Max : %d\n", max);


	cvReleaseCapture(&capture);
	cvDestroyWindow( "StayingInLane");
	cvDestroyWindow("Histogram");
	cvDestroyWindow("Original");
    return 0;
}