#ifdef _CH_
#pragma package <opencv>
#endif

#include "cv.h"
#include "highgui.h"
#include <stdio.h>
#include <stdlib.h>
#include "../utilities.h"

#define NUM_IMAGES 9
#define NUMBER_OF_KNOWN_CHARACTERS 10


// Structure to store features of a known or unknown character.
typedef struct tLicensePlateCharacterFeatures_tag {
	char name[10];
} tLicensePlateCharacterFeatures;

//Simple invert
void invert_image( IplImage* source)
{
	
	int width_step=source->widthStep;
	int pixel_step=source->widthStep/source->width;
	int n_channels=source->nChannels;
	int row = 0, col = 0, chan = 0;
	unsigned char white[4] = {255,255,255,0};

	for(row=0; row < source->height; row++){
		for(col=0; col < source->width; col++){
			unsigned char * curr_point = GETPIXELPTRMACRO(source, col, row, width_step, pixel_step);
												//Could use a differnet macro above but this works
			for(chan = 0; chan < n_channels; chan++){
				curr_point[chan] = 255 - curr_point[chan]; //Invert ecah channel
			}
			//Store the inverted point into the reuslt image
		}
	}

	
}

IplImage * binary_image(IplImage * source){
	IplImage* binary_image = cvCreateImage( cvGetSize(source), 8, 1 );
	cvConvertImage( source, binary_image );
	IplImage * temp = cvCloneImage(binary_image);
	cvSmooth(temp, binary_image);
	CvScalar c = cvAvg(source);
	float threshold = c.val[0];
	cvThreshold( binary_image, binary_image, threshold-10, 255, CV_THRESH_BINARY );
	//cvMorphologyEx(binary_image, binary_image, NULL, NULL, CV_MOP_CLOSE, 1);

	cvReleaseImage(&temp);
	return binary_image;
}

CvSeq* connected_components( IplImage* source, IplImage* result )
{
	IplImage* binary_image = cvCreateImage( cvGetSize(source), 8, 1 );
	cvConvertImage( source, binary_image );
	CvMemStorage* storage = cvCreateMemStorage(0);
	CvSeq* contours = 0;
	CvScalar c = cvAvg(source);
	float threshold = c.val[0];
	//FIX
	cvThreshold( binary_image, binary_image, threshold-10, 255, CV_THRESH_BINARY );
	cvFindContours( binary_image, storage, &contours, sizeof(CvContour),	CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE );
	if (result)
	{
		cvZero( result );
		for(CvSeq* contour = contours ; contour != 0; contour = contour->h_next )
		{
			CvScalar color = CV_RGB( rand()&255, rand()&255, rand()&255 );
			CvScalar color2 = CV_RGB( 255, 255, 0 );

			/* replace CV_FILLED with 1 to see the outlines */
			cvDrawContours( result, contour, color, color, -1, CV_FILLED, 8 );
		}
	}
	cvReleaseImage(&binary_image);
	return contours;
}

void analyse_contour(CvSeq * contour){
	CvScalar color = CV_RGB( rand()&255, rand()&255, rand()&255 );
	//40 x 40 should be plenty big for the moment
	IplImage * tempImage = cvCreateImage( cvSize(400, 400), 8, 1);
	cvZero(tempImage);
	cvDrawContours( tempImage, contour, color, color, -1, CV_FILLED, 8);
	//cvShowImage( "Result", tempImage);
	printf("%d\n", contour->delta_elems);

	cvReleaseImage(&tempImage);

}

void ident_number(IplImage * src, IplImage * dst){
	//smooth image
	IplImage * temp;
	cvSmooth(src, dst);
	//cvThreshold(src, dst, 40, 255, CV_THRESH_BINARY);
	CvScalar c = cvAvg(dst);
	float threshold = c.val[0];
	cvThreshold(src, dst, threshold-10, 255, CV_THRESH_BINARY);
	temp = cvCloneImage(dst);
	cvMorphologyEx(dst, temp, NULL, NULL, CV_MOP_OPEN, 1);
	//cvMorphologyEx(temp, dst, NULL, NULL, CV_MOP_CLOSE, 1);



	dst = cvCloneImage(temp);
	//cvSetImageROI(temp, cvRect(10, 15, dst->width, dst->height));
	//find the conturs



	cvReleaseImage(&temp);
	

}
//Blank everything outside of the ROI
void blank_region( IplImage * img, CvRect roi){
	;
}

int main( int argc, char** argv )
{
	int selected_image_num = 1;
	IplImage* selected_image = NULL;
	IplImage* result_image = NULL;
	IplImage* bin_image = NULL;
	IplImage* sample_number_images[NUMBER_OF_KNOWN_CHARACTERS];
	IplImage* images[NUM_IMAGES];
	tLicensePlateCharacterFeatures known_object_features[NUMBER_OF_KNOWN_CHARACTERS];
	tLicensePlateCharacterFeatures unknown_object_features[100];
	CvSeq * components = NULL;

	// Load all the sample images and determine feature values for these characters.
	for (int character=0; (character<NUMBER_OF_KNOWN_CHARACTERS); character++)
	{
		char filename[100];
		sprintf(filename,"./%d.jpg",character);
		if( (sample_number_images[character] = cvLoadImage(filename,-1)) == 0 )
			return 0;
		sprintf(known_object_features[character].name,"%d",character);
	}

	// Load all the unknown license plate images.
	for (int file_num=1; (file_num <= NUM_IMAGES); file_num++)
	{
		char filename[100];
		sprintf(filename,"./LicensePlate%d.jpg",file_num);
		if( (images[file_num-1] = cvLoadImage(filename,-1)) == 0 )
			return 0;
	}


	// Explain the User Interface
    printf( "Hot keys: \n"
            "\tESC - quit the program\n");
    printf( "\t1..%d - select image\n",NUM_IMAGES);
    
	// Create display windows for images
    cvNamedWindow( "Original", 1 );
    cvNamedWindow( "Result", 1);
    cvMoveWindow( "Result", 200, 0);


	// Create images to do the processing in.
	selected_image = cvCloneImage( images[selected_image_num-1] );
	result_image = cvCloneImage(selected_image);
	bin_image = cvCloneImage(selected_image);


	// Setup mouse callback on the original image so that the user can see image values as they move the
	// cursor over the image.
    cvSetMouseCallback( "Original", on_mouse_show_values, 0 );
	//window_name_for_on_mouse_show_values="Original";
	//image_for_on_mouse_show_values=selected_image;

	int user_clicked_key = 0;
	do {
		// Process image (i.e. setup and find the number of spoons)
		cvCopyImage( images[selected_image_num-1], selected_image );
		bin_image = cvCloneImage(selected_image);
		result_image = cvCloneImage(selected_image);

		cvShowImage( "Original", selected_image );
		printf("\nProcessing Image %d:\n",selected_image_num);
		bin_image = binary_image(selected_image);
		invert_image(bin_image);

		//ident_number(selected_image, result_image);
		components = connected_components( bin_image, result_image);
		//analyse_contour(components->h_next->h_next->h_next->h_next->h_next);
        cvShowImage( "Result", result_image);

		// Wait for user input
        user_clicked_key = (char) cvWaitKey(0);
		if ((user_clicked_key >= '1') && (user_clicked_key <= '0'+NUM_IMAGES))
		{
			selected_image_num = user_clicked_key-'0';
		}
	} while ( user_clicked_key != ESC );

    return 1;
}
