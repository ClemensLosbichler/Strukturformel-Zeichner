import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Stores the MainChain of the HydroCarbon
//Simply create an object of it and call the method regex

//Import variables are the enums hydroCarbon, greekNumber, double_bonds and triple_bonds
public class MainChain {

    //Stores carbon count and multibond count
    public HydroCarbons hydroCarbon;
    public GreekNumbers greekNumber_en;
    public GreekNumbers greekNumber_in;

    //Stores the position of the double and triple bonds
    public ArrayList<Integer> double_bonds = new ArrayList<>();
    public ArrayList<Integer> triple_bonds = new ArrayList<>();
    public ArrayList<Integer> bonds_per_carbon = new ArrayList<>();

    //String regex1 = "\\s?([A-Z]?[b-z]{3,10})(a)?(\\s?-?(\\d[\\d,]*)-?\\s?)?([a-z]{2,})?([ai]n)$";
    private String name;
    private String ending_an;
    private String ending_en;
    private String ending_in;

    //Stores the multiple bond positions e.g. 3,4
    private String multibond_positions_en;
    private String multibond_positions_in;
    //Stores the greek syllable for the multi bonds e.g. di
    private String greek_syllable_en;
    private String greek_syllable_in;
    //a or ""
    private String multibond_char;

    //Takes the input and turns calculates the output values
    //Returns if the input was correct or if there were any mistakes
    boolean regex(String input) {
        //Stores the input String, the regex, the name (e.g. Prop) and the ending (e.g. an)
        String regex = "\\s?([A-Z]?[b-z]{2,})(a)?" +
                "((an)|(\\s?-?(\\d(,\\d)*)-?\\s?([a-z]{2,})?(en))?" +
                "(\\s?-?(\\d(,\\d)*)-?\\s?([a-z]{2,})?(in))?)";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);

        if (m.find()) {

            name = m.group(1);
            multibond_char = m.group(2);
            ending_an = m.group(4);
            multibond_positions_en = m.group(6);
            greek_syllable_en = m.group(8);
            ending_en = m.group(9);
            multibond_positions_in = m.group(11);

            greek_syllable_in = m.group(13);
            ending_in = m.group(14);

            if (ending_an != null) {
                calc_multiple_bondings(name, ending_an, multibond_positions_en);
                calc_multiple_bondings(name, ending_an, multibond_positions_in);
                calc_enums(name, greek_syllable_en, greek_syllable_in);
                validate_input(multibond_char);
            } else {
                if (ending_en != null) {
                    calc_multiple_bondings(name, ending_en, multibond_positions_en);
                    calc_enums(name, greek_syllable_en, greek_syllable_in);
                    validate_input(multibond_char);
                }
                if (ending_in != null) {
                    calc_multiple_bondings(name, ending_in, multibond_positions_in);
                    calc_enums(name, greek_syllable_en, greek_syllable_in);
                    validate_input(multibond_char);
                }
                if (ending_en != null && ending_in != null) {
                    //No ending
                    System.out.println("Missing ending [an, en, in]");
                    return false;
                }

            }
            System.out.println(this);


            //If any of those methods detects a mistake it will return false
            return true;
        }
        System.out.println(this);
        return false;

    }

    //Calculates the multibonds with the given input
    //Returns if the input was correct
    private boolean calc_multiple_bondings(String name, String ending, String multibond_positions) {

        if ((ending.equals("en") || ending.equals("in")) && name.equals("Meth")) {
            //Meth syllable with en or in
            System.out.println("Meth" + ending + " doesn't exist!");
            return false;
        }
        //Getting the positions into the arrays double_bonds and triple_bonds
        if (ending.equals("en") || ending.equals("in")) {
            String[] pos_string = multibond_positions.split(",");
            for (String s : pos_string) {
                int newinput = Integer.parseInt(s);
                if (ending.equals("en") && !double_bonds.contains(newinput) && !triple_bonds.contains(newinput)) {
                    double_bonds.add(Integer.parseInt(s));

                } else if (ending.equals("in") && !triple_bonds.contains(newinput) && !double_bonds.contains(newinput)) {
                    triple_bonds.add(Integer.parseInt(s));

                } else {
                    //Duplicate Position
                    System.out.println("Duplicate Position of multibonds");
                    return false;
                }
            }
            if (pos_string.length == 0) {
                //Ending en or in but no position given
                System.out.println("No given Positions for \"" + ending + "\"!");
                return false;
            }
        } else if (ending.equals("an")) {
            if (multibond_positions != null && !multibond_positions.equals("")) {
                //Ending "an" but given multibond position
                System.out.println("An Alkane cannot have a multibond position!");
                return false;
            }
        }
        //Either all positions given or no multibond and ending an
        return true;
    }

    //Creates the enum values from the input name and the greek_syllable
    //Returns if the transition was successfull
    private boolean calc_enums(String name, String greek_syllable_en, String greek_syllable_in) {

        //Setting hydroCarbon to the given Value
        try {
            hydroCarbon = HydroCarbons.valueOf(name.toLowerCase());
        } catch (Exception e) {
            //No HydroCarbon or Wrong HydroCarbon name
            hydroCarbon = HydroCarbons.none;
            System.out.println("Wrong HydroCarbon name!");
            return false;
        }

        //setting greekNumber to the given syllable e.g. di
        if (double_bonds.size() >= 2) {
            try {
                greekNumber_en = GreekNumbers.valueOf(greek_syllable_en.toLowerCase());
            } catch (Exception e) {
                greekNumber_en = GreekNumbers.none;
                //No Greek Syllable or Wrong Greek Syllable name
                System.out.println("Wrong or no Greek Syllable!");
                return false;
            }

            if (double_bonds.size() != greekNumber_en.getValue()) {
                //No Greek Syllable or Wrong Greek Syllable name
                System.out.println("Wrong or no Greek Syllable!");
                return false;
            }
        } else if (triple_bonds.size() >= 2) {
            try {
                greekNumber_in = GreekNumbers.valueOf(greek_syllable_in.toLowerCase());
            } catch (Exception e) {
                greekNumber_in = GreekNumbers.none;
                //No Greek Syllable or Wrong Greek Syllable name
                System.out.println("Wrong or no Greek Syllable!");
                return false;
            }
            if (triple_bonds.size() != greekNumber_in.getValue()) {
                //No Greek Syllable or Wrong Greek Syllable name
                System.out.println("Wrong or no Greek Syllable!");
                return false;
            }
        } else {
            if (greek_syllable_en != null || greek_syllable_in != null) {
                //Greek_syllable even tho the ending is an
                System.out.println();
            }
            greekNumber_en = GreekNumbers.none;
            greekNumber_in = GreekNumbers.none;
        }
        return true;


    }

    //Validates the input
    //Returns if the input was correct
    private boolean validate_input(String multibond_char) {
        boolean correct_input = true;

        //Checking if there was an "a" input
        if (double_bonds.size() >= 2 || triple_bonds.size() >= 2) {
            if (multibond_char == null) {
                //missing a after carbon counting syllable
                //e.g Prop2,3dien
                System.out.println("Missing a after carbon counting syllable");
                return false;
            }
        }

        //Checking the positions of the multibonds
        if (double_bonds.size() >= 1 || triple_bonds.size() >= 1) {
            for (Integer double_bond : double_bonds) {
                if (double_bond <= 0 || double_bond >= hydroCarbon.getValue()) {
                    //Illegal Positions
                    System.out.println("Wrong Positions [1 - " + (hydroCarbon.getValue() - 1) + "] would be correct");
                    return false;
                }
            }

            for (Integer triple_bond : triple_bonds) {
                if (triple_bond <= 0 || triple_bond >= hydroCarbon.getValue()) {
                    //Illegal Positions
                    System.out.println("Wrong Positions [1 - " + (hydroCarbon.getValue() - 1) + "] would be correct");
                    return false;
                }
            }
        } else {
            if (multibond_char != null && multibond_char.equals("a")) {
                //Incorrect a after Carbon syllable
                //e.g. Propa1en
                System.out.println("Incorrect a after carbon counting syllable");
                return false;
            }
        }

        //Check if there are wrong positions and if every C Atom only got a max amount of 4 bonds
        for (int i = 1; i <= hydroCarbon.getValue(); i++) {
            if (i == 1) {
                if(double_bonds.contains(i)){
                    bonds_per_carbon.add(2);
                }
                else if(triple_bonds.contains(i)){
                    bonds_per_carbon.add(3);
                }
                else {
                    bonds_per_carbon.add(1);
                }

            }
            else if(i == hydroCarbon.getValue()){
                if(double_bonds.contains(i-1)){
                    bonds_per_carbon.add(2);
                }
                else if(triple_bonds.contains(i-1)){
                    bonds_per_carbon.add(3);
                }
                else {
                    bonds_per_carbon.add(1);
                }
            }
            else {
                int bonds = 0;
                if(double_bonds.contains(i-1)){
                    bonds +=2;
                }
                else if(triple_bonds.contains(i-1)){
                    bonds +=3;
                }
                else {
                    bonds +=1;
                }
                if(double_bonds.contains(i)){
                    bonds +=2;
                }
                else if(triple_bonds.contains(i)){
                    bonds +=3;
                }
                else {
                    bonds +=1;
                }

                if(bonds > 4){
                    //the user has given a wrong bond position
                    //e.g. Hexa 2,3 diin
                    System.out.println("Wrong bond positions [more than 4 bonds at] "+i+". C - Atom");
                    return false;
                }
                bonds_per_carbon.add(bonds);
            }

        }


        return true;
}

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", ending_an='" + ending_an + '\'' +
                ", ending_en='" + ending_en + '\'' +
                ", ending_in='" + ending_in + '\'' +
                ", multibond_positions_en='" + multibond_positions_en + '\'' +
                ", multibond_positions_in='" + multibond_positions_in + '\'' +
                ", greek_syllable_en='" + greek_syllable_en + '\'' +
                ", greek_syllable_in='" + greek_syllable_in + '\'' +
                ", multibond_char='" + multibond_char + '\'' +
                '}';
    }
}
