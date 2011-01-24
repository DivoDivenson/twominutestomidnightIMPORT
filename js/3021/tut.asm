add #256,r0,r1

//i = r26
//j = r27
//k = r16
//result = r2
p: add r26,r27,r16
   sll r16,#2,r16
   sub r16,#1,r16
   ret r18,#0
   add r0,r16,r2 //instruction after return cousa bandy pipeline thing


q: add r0,r1,r11
   sub r0,r26,r11 //move i and negate it
   call r18,p
   add r0,r0,r0
   ret r19,#0
   add r0,r0,r0

   //n = r26
f: add r0,r26,r0 //add n to 0 and neg flag is set, then n is negative
   jmp neg,else
   sub r26,#1,r10
   call r25,f
   add r0,r0,r0
   ret r25,#0
   mul r2,r26,r2

else : 
  add r0, #1, r2
  ret r25,#0  
   

   
