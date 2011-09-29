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

	for(row=0; row < result->height; row++){
		for(col=0; col < result->width; col++){
			unsigned char* curr_point = GETPIXELPTRMACRO( source, col, row, width_step, pixel_step );
			if ((curr_point[RED_CH] >= THRESHOLD) && ((curr_point[BLUE_CH] > THRESHOLD) || (curr_point[GREEN_CH] > THRESHOLD))){
				PUTPIXELMACRO( result, col, row, white_pixel, width_step, pixel_step, number_channels );
			}
		}
	}

	/*cvMorphologyEx( result, temp, NULL, NULL, CV_MOP_OPEN, 3 );
	cvMorphologyEx( temp, result, NULL, NULL, CV_MOP_CLOSE, 3 );*/


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
	cvNamedWindow("StayingInLane", 1);
	cvNamedWindow("Original",1 );

	while(key != 'q'){
		printf("Frame");
		frame = cvQueryFrame( capture );
		if(!frame) break;

		input = cvCloneImage(frame);
		result = cvCloneImage(input);
		select_white_points(input, result);
		cvShowImage("StayingInLane", result);
		cvShowImage("Original", input);
		key = cvWaitKey(fps);
	}

	cvReleaseCapture(&capture);
	cvDestroyWindow( "StayingInLane");
    return 0;
}