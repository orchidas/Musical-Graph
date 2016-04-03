public class PlayChords{
    //setting up instruments
    Mandolin mand => NRev rev =>dac;
    Rhodey piano[5];
    piano[0] => rev => dac;
    piano[1] => rev;
    piano[2] => rev;
    piano[3] => rev;
    piano[4] => rev;
    0.05 => rev.mix;
    for(0 => int i;i<5;i++)
        0.1 => piano[i].gain;
    //make mandolin sound more like a guitar
    0.25 => mand.bodySize;
    0.3 => mand.gain;
    
    //global array to hold arpeggio notes
    int notes[];
    //global variable stores result of operation
    0 => int flag;
    
    fun void findChord(string chord){
        //ascii 65 = A = midi 57 = A3
        chord.charAt(0) - 8 => int root;
        chord.length() => int len;
        //see type of chord
        string type;
        if(chord.charAt(len-1) == 'm')
            "minor" => type;
        else
            "major" => type;
        //see sharps and flats
        if(len > 1){
            if(chord.charAt(1) == '#')
                1 +=> root;
            else if(chord.charAt(1) == 'b')
                1 -=> root;
        }
        findNotes(root, type);
    }
    
    fun void findNotes(int root, string type){
        if (type == "major")
            [root-12,root-8,root-5,root,root+4] @=> notes;
        else if(type == "minor")
            [root-12,root-5,root,root+3,root+7] @=> notes;
    }
    
    fun int playGuitar(string chord, dur tempo){
        this.findChord(chord);
        for(0 => int i;i <= (notes.cap()-1)*2;i++){
            Math.random2f(0.5,1.0) => mand.noteOn;
            Math.random2f(0,0.4) => mand.stringDamping;
            Math.random2f(0,0.05) => mand.stringDetune;
            if(i < notes.cap())
                Std.mtof(notes[i]) => mand.freq;
            else
                Std.mtof(notes[(notes.cap()-1)*2-i]) => mand.freq;
            //let last note ring
            if(i == (notes.cap()-1)*2)
                tempo*6 => now;
            else
                tempo => now;
        }     
    }
    
    fun void playPiano(string chord, dur tempo){
        this.findChord(chord);
        for(1 => int count; count <=2 ;count++){
        for(0 => int i;i<notes.cap();i++){
            if(count == 2){
                Std.mtof(notes[i]+12) => piano[i].freq;
                0.2 => piano[i].noteOn;
            }
            else{
                Std.mtof(notes[i]) => piano[i].freq;
                0.4 => piano[i].noteOn;
            }
        }
        tempo*8 => now;
    }    
    }
    
    fun int play(string chord, dur tempo){
        spork ~ playGuitar(chord,tempo);
        spork ~ playPiano(chord,tempo);
        tempo*14 => now;
        1 => flag;
        return flag;
    }
}


    