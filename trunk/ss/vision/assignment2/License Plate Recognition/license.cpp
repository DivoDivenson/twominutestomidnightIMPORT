
#include "cv.h"
#include "highgui.h"
#include <stdio.h>
#include <stdlib.h>
#include "../utilities.h"
#include <float.h>
#include <math.h>

#define NUM_IMAGES 9
#define NUMBER_OF_KNOWN_CHARACTERS 10
#define MIN_AREA 90.0 //This is just easier

float min_number_area; //Inited in main. Hurray globals


// Structure to store features of a known or unknown character.
typedef struct tLicensePlateCharacterFeatures_tag {
	char name[10];
} tLicensePlateCharacterFeatures;

typedef struct feature_set{
	int no_holes;
	double hull_to_box;
	//This is just for drawing the number on the image
	CvPoint2D32f center;
	double area; //Used to get rid of small structures
} feature_set;

void print_feature(feature_set set){
	printf("No Holes : %d, Hull to Box: %f, Area %f\n", set.no_holes, set.hull_to_box, set.area);
}

IplImage * crop_image(IplImage * src){
	IplImage * temp;
	cvSetImageROI(src, cvRect(20,12,250,250));
	temp = cvCreateImage(cvGetSize(src), src->depth, src->nChannels);
	cvCopy(src, temp, NULL);
	cvResetImageROI(src);
	return temp;
}

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
	CvScalar c = cvAvg(binary_image);
	float threshold = c.val[0];
	cvThreshold( binary_image, binary_image, threshold-15, 255, CV_THRESH_BINARY );
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
	//FIX. So this is done twice for some image, but is need to pull out holes.
	//This was a clever hack but now I forget how it works
	cvThreshold( binary_image, binary_image, threshold-15, 255, CV_THRESH_BINARY );
	cvFindContours( binary_image, storage, &contours, sizeof(CvContour),	CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE );
	if (result)
	{
		cvZero( result );
		for(CvSeq* contour = contours ; contour != 0; contour = contour->h_next )
		{
			CvScalar color = CV_RGB( 255, 255, 255 );

			/* replace CV_FILLED with 1 to see the outlines */
			cvDrawContours( result, contour, color, color, -1, CV_FILLED, 8 );
		}
	}

	cvReleaseImage(&binary_image);
	return contours;
}

int seq_len(CvSeq * sequence){
	int result = 0;
	for(CvSeq* contour = sequence; contour != 0; contour = contour->h_next){
		result++;
	}
	return result;
}
//DO THIS
feature_set analyse_contour(CvSeq * contour){
	CvScalar color = CV_RGB( 255, 255, 255 );
	//40 x 40 should be plenty big for the moment
	IplImage * tempImage = cvCreateImage( cvSize(400, 400), 8, 1);
	cvZero(tempImage);
	cvDrawContours( tempImage, contour, color, color, -1, CV_FILLED, 8);

	//First find the number of holes
	invert_image(tempImage);
	//sequence, ambiguous name as it's reused for several things
	CvSeq * sequence = connected_components(tempImage, tempImage);
	int no_holes = seq_len(sequence) -1 ; //-1 to account for background

	//Bounding box area / hull area.
	CvRect rect = cvBoundingRect(contour);
	//this really does not seem right, but returns similar results
	//Area if hull is better than just area of the character. Less effected by noise and 
	//the inside of the character being borked
	sequence = cvConvexHull2(contour, 0, CV_CLOCKWISE, 1);
	float hull_area = cvContourArea(sequence);
	float hull_to_box = hull_area / (rect.width * rect.height);

	CvPoint2D32f center;
	float radius;
	//Because I'm using CvSeq to store the numbers I need a way of pulling out where they actualy are
	//in order to draw in top of them. This solytion is less than ideal from a performance point of view
	//but works well
	cvMinEnclosingCircle(contour, &center, &radius);


	//CvArr can be an image
	//printf("No elemnts %d\n", no_holes);
	//cvShowImage( "Debug", tempImage);

//old ---------------------------------
	//float arc_length = cvArcLength(contour);
	//float area = cvContourArea(contour);
	feature_set result = {no_holes, hull_to_box, center, hull_area};
	return result;

	cvReleaseImage(&tempImage);

}

int template_match(IplImage * input, IplImage * template_img){
	IplImage * result = cvCreateImage(cvGetSize(input),8,1);
	cvXor(input, template_img, result);

	int diff = 0;

	int width_step = result->widthStep;
	int pixel_step = result->widthStep/result->width;

	for (int row =0; row<result->height;row++){
		for (int col = 0; col<result->width; col++){
			unsigned char * currpoint = GETPIXELPTRMACRO(result,col,row,width_step,pixel_step);
			if (currpoint[0]==255){
				diff++;
			}
		}
	}	
	return diff;
}

//Pass in a sequence of components and classify them
void ident_numbers(CvSeq * components, IplImage * known[], IplImage * result){
	//smooth image
	CvSeq * contour = components->h_next->h_next->h_next->h_next;
	feature_set temp;
	int i;
	float diff = FLT_MAX;
	int number = 0;
	CvScalar color = CV_RGB( 255, 255, 255 );
	IplImage * number_image;
	IplImage * translated_number;

	for(CvSeq * contour = components; contour != 0; contour = contour->h_next){
		if(cvContourArea(contour) < MIN_AREA){
			continue;
		}
		number_image = cvCreateImage( cvSize(400, 400), 8, 1);
		cvZero(number_image);
		cvDrawContours( number_image, contour, color, color, -1, CV_FILLED, 8);
		CvPoint pt1, pt2;
		CvRect r = cvBoundingRect(contour, 0);
		translated_number = cvCreateImage(cvSize(r.width +8 , r.height +8), 8, 1);
		cvSetImageROI(number_image, r);
		pt1 = cvPoint(4, 4);
		cvCopyMakeBorder(number_image, translated_number, pt1, IPL_BORDER_CONSTANT);
		//translated_number = pad(translated_number);
		//cvResetImageROI()

		int diff = INT_MAX;
		int recoginized_number;
		for(i = 0; i < NUMBER_OF_KNOWN_CHARACTERS; i++){
			//Need to scale the image to the size of the sample size
			//resize a temp image to avoid resizing the same image over and over an introducing noise
			IplImage * tempScaled = cvCreateImage( cvGetSize(known[i]), 8, 1);
			cvResize(translated_number, tempScaled, CV_INTER_NN);
			cvShowImage("Debug", tempScaled);
			int newDiff = template_match(tempScaled, known[i]);
			if(newDiff < diff){
				diff = newDiff;
				recoginized_number = i;
			}
		}

		CvPoint2D32f center;
		float radius;
		//Because I'm using CvSeq to store the numbers I need a way of pulling out where they actualy are
		//in order to draw in top of them. This solytion is less than ideal from a performance point of view
		//but works well
		cvMinEnclosingCircle(contour, &center, &radius);

		char buffer[1];
			//sprintf(buffer, "%d", number);
		sprintf(buffer, "%d", recoginized_number);
		write_text_on_image(result, center.y, center.x, buffer);

		//cvSetImageROI()
		//Pull out ROI



		/*pt1.x = r.x;
        pt2.x = (r.x+r.width);
        pt1.y = r.y;
        pt2.y = (r.y+r.height);
        cvRectangle( result, pt1, pt2, CV_RGB(255,0,0), 3, 8, 0 );
        */

		//printf("RECT %d %d %d %d\n", bounds.x, bounds.y, bounds.width, bounds.height);
		//cvWaitKey(0);

		/*temp = analyse_contour(contour);
		//print_feature(temp);

		if(temp.area > MIN_AREA){
			print_feature(temp);
			//write_text_on_image(result, temp.center.y, temp.center.x, "a");
			for(i = 0; i < NUMBER_OF_KNOWN_CHARACTERS; i++){
				if(known[i].no_holes == temp.no_holes){
					
				}
			
			}
			char buffer[1];
			//sprintf(buffer, "%d", number);
			sprintf(buffer, "%d", temp.no_holes);
			cvDrawContours( result, contour, color, color, CV_FILLED, 8 );
			write_text_on_image(result, temp.center.y, temp.center.x, buffer);
		}*/

	}

}
//Blank everything outside of the ROI
void blank_region( IplImage * img, CvRect roi){
	;
}

//Get the min area of the features in the know_number set
float getMinArea(feature_set * set){
	int i;
	float min = FLT_MAX;
	for(i = 0; i < NUMBER_OF_KNOWN_CHARACTERS; i++){
		if(set[i].area < min){
			min = set[i].area;
		}
	}
	
	return min;
	
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

	feature_set known_number[NUMBER_OF_KNOWN_CHARACTERS];

	CvSeq * components = NULL;

	// Load all the real number sample images and determine feature values for these characters.
	for (int character=0; character< NUMBER_OF_KNOWN_CHARACTERS; character++)
	{
		char filename[100];
		sprintf(filename,"./real_numbers/%d.jpg",character);
		if( (sample_number_images[character] = cvLoadImage(filename,-1)) == 0 )
			return 0;
		sample_number_images[character] = binary_image(sample_number_images[character]);
		invert_image(sample_number_images[character]);
		components = connected_components(sample_number_images[character], sample_number_images[character]);
		known_number[character] =  analyse_contour(components);
		//cvShowImage( "Debug", sample_number_images[character]);
		print_feature(known_number[character]);
		sprintf(known_object_features[character].name,"%d",character);
		//cvShowImage("Debug", sample_number_images[character]);
	}

	min_number_area = getMinArea(known_number);

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
    cvNamedWindow( "Debug", 0);
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
		//cvCopyImage( images[selected_image_num-1], selected_image );
		selected_image = cvCloneImage(images[selected_image_num-1]);
		selected_image = crop_image(selected_image);
		bin_image = cvCloneImage(selected_image);
		result_image = cvCloneImage(selected_image);

		printf("\nProcessing Image %d:\n",selected_image_num);
		//Get binary image of licence plate
		bin_image = binary_image(selected_image);
		//Invert binary image
		invert_image(bin_image);

		//ident_number(selected_image, result_image);
		components = connected_components( bin_image, result_image);
		//analyse_contour(components->h_next->h_next->h_next->h_next->h_next);
		ident_numbers(components, sample_number_images, selected_image);
        cvShowImage( "Result", result_image);
        cvShowImage( "Original", selected_image );


		// Wait for user input
        user_clicked_key = (char) cvWaitKey(0);
		if ((user_clicked_key >= '1') && (user_clicked_key <= '0'+NUM_IMAGES))
		{
			selected_image_num = user_clicked_key-'0';
		}
	} while ( user_clicked_key != ESC );

    return 1;
}
