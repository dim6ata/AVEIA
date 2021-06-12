package com.example.aveia;

/**
 * Enumerates the values of notes assigning each a name, a corresponding colour and currentButtonPosition within octave.
 */
public enum FrequencyValue {
    C("C", R.color.C, 0),
    C_SHARP("C#", R.color.CSharp, 1),
    D("D", R.color.D, 2),
    Eb("Eb", R.color.DSharp, 3),
    E("E", R.color.E, 4),
    F("F", R.color.F, 5),
    F_SHARP("F#", R.color.FSharp, 6),
    G("G", R.color.G, 7),
    Ab("Ab", R.color.GSharp, 8),//♭
    A("A", R.color.A, 9),
    Bb("Bb", R.color.ASharp, 10),//
    B("B", R.color.B, 11);

    private String name;
    private int colour;
    private int location;


    FrequencyValue(String name, int colour, int locationWithinOctave) {
        this.name = name;
        this.colour = colour;
        this.location = locationWithinOctave;
    }


    public static FrequencyValue getByNoteNum(int num){
        for(FrequencyValue v : values()){
            if(v.location == num){
                return v;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getColour() {
        return colour;
    }

    public int getLocationWithinOctave() {
        return location;
    }

    //TODO test with actual symbols:
    //RESULT - the symbols do not appear properly as they are already styled.
//♭   Flat note   &#9837;
//♮   Natural note &#9838;
//♯   Sharp note  &#9839;
}
