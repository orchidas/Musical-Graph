//this OSC.ck file communicates with processing to send and receive messages
OscOut xmit;
6450 => int port;
//local host - sends message to the same computer
xmit.dest("localhost",port);

//just like an Event
OscIn oin;
//port number to receive OSC,same as sender
6449 => oin.port; 
OscMsg msg;
//same as sender address
oin.addAddress("/chuckReceives");

PlayChords p;
0 => int flag;

while(true){
     oin => now;
    //when even is received, progress
    if(oin.recv(msg) != 0){
        msg.getString(0) => string chord;
        <<<"Received message", chord>>>;
        //see if one chord or a list of chords
        if(chord.find(' ') > -1){
            //more than one chord
            0 => int start;
            0 => int arrPos;  
            for( 0 => int i;i< chord.length();i++){
                if(chord.charAt(i) == ' '){
                    chord.substring(start,(i-start)) => string curChord; 
                    <<<curChord>>>;
                    i+1 => start;
                    //if a list of chords, after each chord finishes playing,flag = 1 is sent
                    p.play(curChord,0.2::second);
                    xmit.start("/chuckSends");
                    xmit.add(flag);
                    xmit.add(curChord);
                    xmit.send();
                    //small gap between 2 chords
                    0.1::second => now;
                }
            }
        }
        else{
        //just one chord sent by processing
        p.play(chord,0.2::second) => flag;
        //send flag as result
        xmit.start("/chuckSends");
        xmit.add(flag);
        xmit.add(chord);
        xmit.send();
        }
    }
}
