mtype {MSG, ACK}

chan toS = [2] of {mtype, bit};
chan toR = [2] of {mtype, bit};

proctype Sender(chan in_, out){

  bit sendbit, recvbit;
  do
    :: out ! MSG, sendbit ->
       in_ ? ACK, recvbit;
       if
       :: recvbit == sendbit ->
          sendbit = 1-sendbit
       :: else
       fi
  od

 }

proctype Receiver(chan in_, out)
{
  bit recvbit;
  do
    :: in_ ? MSG, recvbit ->
       out ! ACK, recvbit;
       printf("Received %d\n", recvbit);
  od
}

init{
  run Sender(toS, toR);
  run Receiver(toR, toS);
}
