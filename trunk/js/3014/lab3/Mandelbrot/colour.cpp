//Not working conversion from HSL to RGB. HSL_Color is struct like a SDL_Color.
#include <SDL/SDL.h>
#include <iostream>
SDL_Color HSLtoRGB(double H, double S, double L)
{
    double r, g, b, h, s, l;
    double temp1, temp2, tempr, tempg, tempb;
    
    //Scale each value to 0.0 - 1.0, from type int:
    h = H / 360.0;  //Hue is represented as a range of 360 degrees
    s = S / 256.0;  //Saturation 
    l = L / 256.0;  //Lightness
    
    if (s == 0)         //Saturation of 0 means a shade of grey
    {
    	r = g = b = l;
    }
    else                //
    {
        if (l < 0.5)
            temp2 = l * (1.0 + s);
        else
            temp2 = (l + s) - (l * s);
        
        temp1 = 2.0 * l - temp2;
        tempr = h + 1.0 / 3.0;
        if (tempr > 1.0)
            tempr-= 1.0;
        tempg = h;
        tempb = h - 1.0 / 3.0;
        if (tempb < 0) 
            tempb += 1.0; 
        
        // Calculate red value:     
        if (6.0 * tempr < 1.0)
        {
            r = temp1 + (temp2 - temp1) * 6.0 * tempr;
        }
        else if (2.0 * tempr < 1.0)
        {
            r = temp2;
        }
        else if (3.0 * tempr < 2.0)
        {
            r = temp1 + (temp2 - temp1) * ((2.0 / 3.0) - tempr) * 6.0;
        }
        else
        {
            r = temp1;
        }

        // Calculate green value       
        if (6.0 * tempg < 1.0)
        {
            g = temp1 + (temp2 - temp1) * 6.0 * tempg;
        }
        else if (2.0 * tempg < 1.0)
        {
            g = temp2;
        }
        else if (3.0 * tempg < 2.0)
        {
            g = temp1 + (temp2 - temp1) * ((2.0 / 3.0) - tempg) * 6.0;
        }
        else
        {
            g = temp1; 
        }

        // Calculate blue value    
        if (6.0 * tempb < 1.0)
        {
            b = temp1 + (temp2 - temp1) * 6.0 * tempb;
        }
        else if (2.0 * tempb < 1.0)
        {
            b = temp2;
        }
        else if (3.0 * tempr < 2.0)
        {
            b = temp1 + (temp2 - temp1) * ((2.0 / 3.0) - tempb) * 6.0;
        }
        else
        {
            g = temp1; 
        }
    }

    SDL_Color TempColor;
    TempColor.r = int(r * 256.0);
    TempColor.g = int(g * 256.0);
    TempColor.b = int(b * 256.0);
    //TempColor.unused = HSL.unused;
    
    return TempColor;
}


int main(){
   SDL_Colour temp = HSLtoRGB(240, 255, 255);
   std::cout <<"R: " << temp.r << " G: " << temp.g << " B: " << temp.b << std::endl;
   HSLtoRGB(120, 255, 255);
}
