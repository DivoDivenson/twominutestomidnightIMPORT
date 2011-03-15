
//Messages to pass along the channels
mtype = { SYN, ACK, REQUEST, TRYING, BUSY, CON, TERM, END };
//Channel for the two process to comuncate on, 0 ensures the channel cannot store connectionssages
// (Makes it a proper channel, not a queue type struct)
chan line = [0] of { mtype, chan };
//! connectionans send over channel
//? connectionans recv over channel

/*Proconnectionla docs say that for recv with an unbufferd channel the message must be sent simultaneously with the recv call
 (I don't really buy that), So wherever we recv a connectionssage, we must "wait" for a little bit
 
 Ok, so after playing with the code a little I don't understand how proconnectionla channels are implemented.
 They are therefore magic. Case closed
*/

//CO
active proctype recevier()
{       chan sub;
idle:   line ? SYN,sub; //Get connection from subscriber
        {       sub ! ACK; //Send a dial tone
                sub ? REQUEST;  //Get requested number
                if //Proconnectionla only executes one of these statements cous it's mad as a bag of hammers
                :: sub ! BUSY; goto wait //If REQUEST is busy TERM
                :: sub ! TRYING ->
                        sub ! CON;
                        if
                        :: sub ! END; goto wait //Hangup when the REQUEST client request hangs up
                        :: skip //Just so we dont hang up straight away
                        fi
                fi
         } unless
         {      if
                :: sub ? TERM -> goto idle //During the execution of above, if the subscriber hangs up at any moconnectionnt
                :: timeout -> goto wait  //Or if no statement in any active process is executable
                fi
          }
wait: sub ? TERM; goto idle
}
//Subscribers
active [4] proctype sender()
{       chan connection = [0] of { mtype };
idle: line ! SYN,connection; //Connect to central office ( Simulate user lifting handset )
      connection ? ACK; //"Wait" for dialtone 
      connection ! REQUEST; //Send number to dial
      if
      :: connection ? BUSY;
      :: connection ? TRYING ->
              if
              :: connection ? CON;
                      if
                      :: connection ? END
                      :: timeout
                      fi
              :: skip
              fi
      fi;
      connection ! TERM; goto idle
}
/*
Using "active" takes care of all of this

init{
  run sender();
  run receiver();
}
*/
