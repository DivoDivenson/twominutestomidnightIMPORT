//mtype {MSG, ACK}
mtype {SYN, ACK, REQUEST, TRYING, BUSY, CON, TERM, END} 
chan channel = [0] of {mtype, chan};
//! means send over channel
//? means recv over channel
//The CO
active proctype Receiver(){
	chan connection = [0] of {mtype}
	idle: channel ? SYN,connection;
	
	connection ? ACK;
	connection ! REQUEST;
	if
	:: connection ? BUSY
	:: connection ? TRYING ->
		if
		:: connection ? CON;
			if
			:: connection ? TERM
			   printf("Disconnecting\n")
			:: timeout
			fi
		:: skip
		fi
	fi;
	connection ! TERM; goto idle //hangup when call complete

}

//The client
active proctype Sender(){
	chan sub;
	idle : channel ? SYN;
	{
		sub ! ACK;
		sub ? REQUEST;
		if
		:: sub ! BUSY; goto wait_
		:: sub ! TRYING ->
			sub ! CON;
			if
			:: sub ! TERM; goto wait_
			:: skip
			fi
		fi
  	} unless {
		if
		:: sub ? TERM -> goto wait_
		:: timeout -> goto wait_
		fi
		}
wait_: sub ? END; goto idle
}
  

/*
init{
  run Sender();
  run Receiver();
}

*/
