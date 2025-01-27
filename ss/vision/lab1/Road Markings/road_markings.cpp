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
//Arbitrary constant is best constant
#define THRESHOLD 40

/*
TODO
Identify white areas. Read some more of the notes.
Analyze different frames (at asy 0% 25% 50% 75% 100% of the video).
The white should be bit brighter (say 30) then the average RGB value of the scene. This works
because it's shit out and the camera is pointed at the road.

Turns out this big mad approach was only a tad better then just looking at the RGB values



*/
int max = 0;
int isGrey(unsigned char * pixel, float average ){
	int variation = 3;
	int result = 1;
	//If the RGB values are close together, class as grey
	//printf("%d\n",pixel[0]);
	//printf("%d : %f\n", pixel[0], max);
	if(pixel[0] < average - THRESHOLD){
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

/*
//Map value from one range to another, assuming min of both is 0
//value, current max, new max
float mapHistValue(float value, int curMax, int newMax){
	//float result = (newMax * (value / curMax));
	//F*ck c++
	float result = (float)newMax * ((float)((float)value / (float)curMax));
	return result;
	//return (float)5 / 6;
}

*/


//Wrote my own histogram code but it was a bit terrible, wrote this after reading up online, http://www.aishack.in/2010/07/drawing-histograms-in-opencv/
//This is to give me a better understanding of the scene and is not used for analysis
IplImage * drawHist(CvHistogram * hist, float scaleX=1, float scaleY=1){
	float max = 0;
	cvGetMinMaxHistValue(hist, 0, &max, 0, 0);


	IplImage * imgHist = cvCreateImage(cvSize(256*scaleX, 64*scaleY), 8 ,1);
	cvZero(imgHist);
	float most = 0;

	for(int i = 0; i<255; i++){
		float value = cvQueryHistValue_1D(hist, i);
		float nextValue = cvQueryHistValue_1D(hist, i+1);
		if(most < value) most = value;

		//Draw a poly showing the change between this and the next point 
		//along the top. (A bar) I have no idea how the math works, come back to this
		CvPoint pt1 = cvPoint(i*scaleX, 64*scaleY);
		CvPoint pt2 = cvPoint(i*scaleX+scaleX, 64*scaleY);
		CvPoint pt3 = cvPoint(i*scaleX+scaleX, (64-nextValue*64/max)*scaleY);
		CvPoint pt4 = cvPoint(i*scaleX, (64-value*64/max)*scaleY);

		CvPoint pts[] = {pt1, pt2, pt3, pt4, pt1};
		//5 == num of points
		cvFillConvexPoly(imgHist, pts, 5, cvScalar(255));
				
	}
	return imgHist;
}


//return the most common pixel the a greyScale image
//or the average luminance
int mostFrequent(IplImage * source, int row){
	int width_step=source->widthStep;
	int pixel_step=source->widthStep/source->width;
	int number_channels=source->nChannels;

	//int row;
	int col;

	int max = 0;
	int result = 0;
	int i;
    int histogram[256];
	for(i = 0; i < 256; i++){
		histogram[i] = 0;
	}

	//for(row=0; row < source->height; row++){
		for(col =0; col < source->width; col++){
			unsigned  char * curr_point = GETPIXELPTRMACRO( source, col, row, width_step, pixel_step );
			histogram[curr_point[0]]++;
		}
	//}

	for(i = 0; i < 256; i++){
		//MOst common value
		/*if(histogram[i] > max){
			max = histogram[i];
			result = i;
		}*/
		//Average
		result += (i * histogram[i]);
	}
	result = (int)result / 256;

	return result;
}

//Very simple bounds, might make more complex later
int boundImage(int row, int col){
	int result = 0;
	if(row > 33){
		result = 1;
	}
	return result;
}


// This routine creates a binary result image where the points are 255,255,255 when the corresponding
// source points are grey/white.  The rule for deciding which points are white/grey is very debatable.
// Should the minimum value be greater?  Should the ratio of max to min values in the point be allowed
// to vary more (or less)?
//Takes a greyscale image
void select_white_points( IplImage* source, IplImage* result )
{
	int width_step=source->widthStep;
	int pixel_step=source->widthStep/source->width;
	int number_channels=source->nChannels;

	cvZero(result);
	unsigned char white_pixel[4] = {255, 255, 255, 0};
	int row, col;


	//All the heights / widths are the same so they get swapped around a lot
	for(row=0; row < source->height; row++){
		//Get most common lum for a scene, row by row
		int max = mostFrequent(source, row);

		for(col=0; col < source->width; col++){
			unsigned char* curr_point = GETPIXELPTRMACRO( source, col, row, width_step, pixel_step );			
			if(boundImage(row, col) && isGrey(curr_point, max)){
				PUTPIXELMACRO( result, col, row, white_pixel, width_step, pixel_step, number_channels );
			}
			
		}
	}

	//Without these applied the reflections of the road markings on the car bonnet are picked up :)
	//cvMorphologyEx( result, result, NULL, NULL, CV_MOP_OPEN, 1 );
	cvMorphologyEx( result, result, NULL, NULL, CV_MOP_CLOSE, 1 );

}



/*Why this doesn't work as a a function I do not know
void cvtGrey(IplImage * source, IplImage * result){
	result = cvCreateImage( cvSize(source->width, source->height), IPL_DEPTH_8U, 1);
	cvCvtColor(source, result, CV_RGB2GRAY ); 
}*/


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

	//Histogram stuff
	float range[] = {0, 255};
	float * ranges[] = {range};
	int numBits = 256;

	float max = 0;

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

		histogram = cvCreateHist(1, &numBits, CV_HIST_ARRAY, ranges, 1);
		cvCalcHist(&input, histogram, 0 , 0);
		IplImage * histImage = drawHist(histogram, 3, 3);
		cvGetMinMaxHistValue(histogram, 0, &max, 0, 0);


		//init result
		result = cvCloneImage( input );
		//result = cvCloneImage(input);
		select_white_points(input, result);
		image_for_on_mouse_show_values=frame;

		

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