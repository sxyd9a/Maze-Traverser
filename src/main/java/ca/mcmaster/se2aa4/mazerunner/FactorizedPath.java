package ca.mcmaster.se2aa4.mazerunner;

public class FactorizedPath {
    
    public String factorizePath(String canonicalPath) {
        StringBuilder factorizedPath = new StringBuilder();
        int fwdCount = 0; 

        for (char move : canonicalPath.toCharArray()) {
            if (move == 'F') { //If a forwar movement is found, count it
                fwdCount++;
            } else {
                //Append total number of forward movements to path before appeding the next turning movement
                if (fwdCount > 0) {
                    factorizedPath.append(fwdCount).append("F");
                    fwdCount = 0;
                }
                factorizedPath.append(move);
            }
        }

        //If the path ends in forward movements, append them at the end
        if (fwdCount > 0) {
            factorizedPath.append(fwdCount).append("F");
        }

        return factorizedPath.toString();
    }
}