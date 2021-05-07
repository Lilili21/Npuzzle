package solver;

import exceptions.ParseProblemException;

public enum Flag {
    AALGORITM("A* Search"), GREEDYSEARCH("Greedy Search"), UNIFORMCOST( "UniformCost Search"),
    MANHATTAN("Manhattan"), HEMMING("Hemming"), EUCLIDEAN("Euclidean"),
    LINEARCONFLICT("Linear Conflict"), CHEBYSHEV("Chebyshev"), SA("All heuristics"),
    PRINTCOLOREDSTEPS("Print colored steps"), PRINTSTEPS("Print steps"),
    PRINTCOMPLEXITYINFO("Print complexity info"),
    SNAKESOLUTION("Snake solution"), CLASSICALSOLUTION("Classical solution");

    private String name;

    private Flag(String name) {
        this.name = name;
    }

    public static Flag returnFlag(String flag){
        switch (flag) {
            case "-A":
                return AALGORITM;
            case "-G":
                return GREEDYSEARCH;
            case "-U":
                return UNIFORMCOST;
            case "-M":
                return MANHATTAN;
            case "-H":
                return HEMMING;
            case "-E":
                return EUCLIDEAN;
            case "-L":
                return LINEARCONFLICT;
            case "-CH":
                return CHEBYSHEV;
            case "-C":
                return PRINTCOLOREDSTEPS;
            case "-P":
                return PRINTSTEPS;
            case "-O":
                return PRINTCOMPLEXITYINFO;
            case "-SA":
                return SA;
            case "-Sn":
                return SNAKESOLUTION;
            case "-Cl":
                return CLASSICALSOLUTION;
            default:
                throw new ParseProblemException("Wrong flags");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
