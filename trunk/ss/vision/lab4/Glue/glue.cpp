#ifdef _CH_
#pragma package <opencv>
#endif

#include "cv.h"
#include "highgui.h"
#include <stdio.h>
#include <stdlib.h>
#include "../utilities.h"


#define NUM_IMAGES 9
#define FIRST_LABEL_ROW_TO_CHECK 390
#define LAST_LABEL_ROW_TO_CHECK 490
#define ROW_STEP_FOR_LABEL_CHECK 20
#define NUMBER_STEPS 5




bool find_label_edges( IplImage* edge_image, IplImage* result_image, int row, int& left_label_column, int& right_label_column )
{
	// TO-DO:  Search for the sides of the labels from both the left and right on "row".  The side of the label is taken	
	//        taken to be the second edge located on that row (the side of the bottle being the first edge).  If the label
	//        are found set the left_label_column and the right_label_column and return true.  Otherwise return false.
	//        The routine should mark the points searched (in yellow), the edges of the bottle (in blue) and the edges of the
	//        label (in red) - all in the result_image.
	int i;
	left_label_column = 0;
	right_label_column = 0;
	cvZero()

	for(i = 0; i < edge_image->width; i++){
		//unsigned char * point = GETPIXELPTRMACRO( output_image, i, row, width_step, pixel_step);

	}

	return false;  // Just to let the project compile until the code is written.
}


void check_glue_bottle( IplImage* original_image, IplImage* result_image )
{
	// TO-DO:  Inspect the image of the glue bottle passed.  This routine should check a number of rows as specified by 
	//        FIRST_LABEL_ROW_TO_CHECK, LAST_LABEL_ROW_TO_CHECK and ROW_STEP_FOR_LABEL_CHECK.  If any of these searches
	//        fail then "No Label" should be written on the result image.  Otherwise if all left and right column values
	//        are roughly the same "Label Present" should be written on the result image.  Otherwise "Label crooked" should
	//        be written on the result image.

	//         To implement this you may need to use smoothing (cv::GaussianBlur() perhaps) and edge detection (cvCanny() perhaps).
	//        You might also need cvConvertImage() which converts between different types of image.
	IplImage* temp_image = cvCreateImage( cvGetSize(original_image), 8, 1 );

	cvConvertImage(original_image, temp_image);
	IplImage * grey_image = cvCloneImage(temp_image);
	cvSmooth(temp_image, grey_image, CV_GAUSSIAN, 11, 11);

	
	cvCanny(grey_image, grey_image, 20, 40, 3);
    cvShowImage( "Edges", grey_image );

    int step;
    int * left_result = new int[NUMBER_STEPS];
    int * right_result = new int[NUMBER_STEPS];

    int * lptr = left_result;
    int * rptr = right_result;
    int i =0;
    for(step = FIRST_LABEL_ROW_TO_CHECK; step <= LAST_LABEL_ROW_TO_CHECK; step += ROW_STEP_FOR_LABEL_CHECK){
    	find_label_edges( grey_image, result_image, step,  * lptr++, * rptr++);
    	
    }

	cvReleaseImage(&grey_image);

}

int main( int argc, char** argv )
{
	int selected_image_num = 1;
	IplImage* selected_image = NULL;
	IplImage* images[NUM_IMAGES];
	IplImage* result_image = NULL;

	// Load all the images.
	for (int file_num=1; (file_num <= NUM_IMAGES); file_num++)
	{
		char filename[100];
		sprintf(filename,"./Glue%d.jpg",file_num);
		if( (images[file_num-1] = cvLoadImage(filename,-1)) == 0 )
			return 0;
	}

	// Explain the User Interface
    printf( "Hot keys: \n"
            "\tESC - quit the program\n");
    printf( "\t1..%d - select image\n",NUM_IMAGES);
    
	// Create display windows for images
    cvNamedWindow( "Original", 1 );
    cvNamedWindow( "Processed Image", 1 );
    cvNamedWindow( "Edges", 1);

	// Create images to do the processing in.
	selected_image = cvCloneImage( images[selected_image_num-1] );
    result_image = cvCloneImage( selected_image );

	// Setup mouse callback on the original image so that the user can see image values as they move the
	// cursor over the image.
    cvSetMouseCallback( "Original", on_mouse_show_values, 0 );
	window_name_for_on_mouse_show_values="Original";
	image_for_on_mouse_show_values=selected_image;

	int user_clicked_key = 0;
	do {
		// Process image (i.e. setup and find the number of spoons)
		cvCopyImage( images[selected_image_num-1], selected_image );
        cvShowImage( "Original", selected_image );
		image_for_on_mouse_show_values=selected_image;
		check_glue_bottle( selected_image, result_image );
		cvShowImage( "Processed Image", result_image );

		// Wait for user input
        user_clicked_key = (char) cvWaitKey(0);
		if ((user_clicked_key >= '1') && (user_clicked_key <= '0'+NUM_IMAGES))
		{
			selected_image_num = user_clicked_key-'0';
		}
	} while ( user_clicked_key != ESC );

    return 1;
}
