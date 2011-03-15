
//Messages to pass along the channels
mtype = { offhook, dialtone, number, ringing,
        busy, connected, hangup, hungup };
//Channel for the two process to comuncate on, 0 ensures the channel cannot store messages
// (Makes it a proper channel, not a queue type struct)
chan line = [0] of { mtype, chan };
//! means send over channel
//? means recv over channel

/*Promela docs say that for recv with an unbufferd channel the message must be sent simultaneously with the recv call
 (I don't really buy that), So wherever we recv a message, we must "wait" for a little bit
 
 Ok, so after playing with the code a little I don't understand how promela channels are implemented.
 They are therefore magic. Case closed
*/
active proctype central_office()
{       chan who;
idle:   line ? offhook,who; //Get connection from subscriber
        {       who ! dialtone; //Send a dial tone
                who ? number;  //Get requested number
                if //Promela only executes one of these statements cous it's mad as a bag of hammers
                :: who ! busy; goto zombie //If number is busy hangup
                :: who ! ringing ->
                        who ! connected;
                        if
                        :: who ! hungup; goto zombie //Hangup when the number client request hangs up
                        :: skip //Just so we dont hang up straight away
                        fi
                fi
         } unless
         {      if
                :: who ? hangup -> goto idle //During the execution of above, if the subscriber hangs up at any moment
                :: timeout -> goto zombie  //Or if no statement in any active process is executable
                fi
          }
zombie: who ? hangup; goto idle
}

active proctype subscriber()
{       chan me = [0] of { mtype };
idle: line ! offhook,me; //Connect to central office ( Simulate user lifting handset )
      me ? dialtone; //"Wait" for dialtone 
      me ! number; //Send number to dial
      if
      :: me ? busy
      :: me ? ringing ->
              if
              :: me ? connected;
                      if
                      :: me ? hungup
                      :: timeout
                      fi
              :: skip
              fi
      fi;
      me ! hangup; goto idle
}

