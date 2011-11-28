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

int main( int argc, char** argv )
{
	int selected_image_num = 1;
	IplImage* selected_image = NULL;
	IplImage* sample_number_images[NUMBER_OF_KNOWN_CHARACTERS];
	IplImage* images[NUM_IMAGES];
	tLicensePlateCharacterFeatures known_object_features[NUMBER_OF_KNOWN_CHARACTERS];
	tLicensePlateCharacterFeatures unknown_object_features[100];

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

	// Create images to do the processing in.
	selected_image = cvCloneImage( images[selected_image_num-1] );

	// Setup mouse callback on the original image so that the user can see image values as they move the
	// cursor over the image.
    cvSetMouseCallback( "Original", on_mouse_show_values, 0 );
	//window_name_for_on_mouse_show_values="Original";
	//image_for_on_mouse_show_values=selected_image;

	int user_clicked_key = 0;
	do {
		// Process image (i.e. setup and find the number of spoons)
		cvCopyImage( images[selected_image_num-1], selected_image );

		printf("\nProcessing Image %d:\n",selected_image_num);
        cvShowImage( "Original", selected_image );

		// Wait for user input
        user_clicked_key = cvWaitKey(0);
		if ((user_clicked_key >= '1') && (user_clicked_key <= '0'+NUM_IMAGES))
		{
			selected_image_num = user_clicked_key-'0';
		}
	} while ( user_clicked_key != ESC );

    return 1;
}
