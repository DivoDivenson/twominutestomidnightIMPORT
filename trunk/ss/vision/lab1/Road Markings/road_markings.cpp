#ifdef _CH_
#pragma package <opencv>
#endif

#include <stdio.h>
#include <stdlib.h>
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
	int bright = 60;
	int result = 0;
	//If the RGB values are close together, class as grey
	/*if((variation*2) <= (abs(pixel[RED_CH] - pixel[BLUE_CH]) + abs(pixel[RED_CH] - pixel[GREEN_CH]))){
		result = 1;
	}*/
	/* HSV version */
	if( (pixel[2] > 70) && (pixel[1] < 20)){
		result = 1;
	}
	if(pixel[1] > max){
		max = pixel[1];
	}

	
	return result;
}



// This routine creates a binary result image where the points are 255,255,255 when the corresponding
// source points are grey/white.  The rule for deciding which points are white/grey is very debatable.
// Should the minimum value be greater?  Should the ratio of max to min values in the point be allowed
// to vary more (or less)?
void select_white_points( IplImage* source, IplImage* result )
{
	IplImage * temp = cvCloneImage(source) ;
	int width_step=source->widthStep;
	int pixel_step=source->widthStep/source->width;
	int number_channels=source->nChannels;

	cvZero(result);
	unsigned char white_pixel[4] = {255, 255, 255, 0};
	int row, col;

	cvCvtColor(source, temp, CV_RGB2HSV); //Too expensive



	for(row=0; row < result->height; row++){
		for(col=0; col < result->width; col++){
			//CHange back to source
			unsigned char* curr_point = GETPIXELPTRMACRO( temp, col, row, width_step, pixel_step );
			//if ((curr_point[RED_CH] >= THRESHOLD) && ((curr_point[BLUE_CH] > THRESHOLD) || (curr_point[GREEN_CH] > THRESHOLD))){
			if(isGrey(curr_point)){
				PUTPIXELMACRO( result, col, row, white_pixel, width_step, pixel_step, number_channels );
			}
		}
	}


	/*cvMorphologyEx( result, temp, NULL, NULL, CV_MOP_OPEN, 1 );
	cvMorphologyEx( temp, result, NULL, NULL, CV_MOP_CLOSE, 1 );*/


}


int main( int argc, char** argv )
{

	IplImage * frame;
	IplImage * input = NULL; //Image to run processing on;
	IplImage * result = NULL;
	int key;

	CvCapture * capture = cvCaptureFromAVI("StayingInLane.avi");

	int fps = (int) cvGetCaptureProperty( capture, CV_CAP_PROP_FPS);

	//1 scales the window to the content size (I think)
	cvNamedWindow("Original",1 );
	cvNamedWindow("StayingInLane", 1);

	cvSetMouseCallback( "Original", on_mouse_show_values, 0 );
	window_name_for_on_mouse_show_values="Original";


	//1048608 unicode? Who knows, it's space anyway.
	while(key != 'q'){
		//printf("key : %d\n", key);
		frame = cvQueryFrame( capture );
		if(!frame) break;

		//Hack to pause video
		if(key == 1048608){
			key = cvWaitKey(0);
		}

		input = cvCloneImage(frame);
		result = cvCloneImage(input);
		select_white_points(input, result);
		image_for_on_mouse_show_values=input;

		cvShowImage("StayingInLane", result);
		cvShowImage("Original", input);
		key = cvWaitKey(1000 / fps);
	}
		printf("Max : %d\n", max);


	cvReleaseCapture(&capture);
	cvDestroyWindow( "StayingInLane");
    return 0;
}