public class mainClass {
    public static void main(String[] args) {
         
        nodeNetwork test = new nodeNetwork();
 
 
        resourceNode moneys = new resourceNode("moneys");                   //Luxury resource desire
        desireNode bitches = new desireNode("bitches");                     //Special desire(diplomacy or event)
        resourceNode fucks = new resourceNode("fucks");                     //Primary resource desire
        desireNode wat4 = new desireNode("moneys");                         //Test - won't be added
        desireNode timeWithBitches = new resourceNode("timeWithBitches");   //Another special desire
         
        /*
         * The more fucks we give, the more bitches we will need(Relation 3)
         * The more bitches we want, the more money we will need(Relation 1)
         * The more fucks we give, the more money we will need  (Relation 2)
         * The more bitches we want, the less time we want to
         * spend with each one                                  (Relation 4)
         */
         
        bitches.addInfluence(moneys, 0, true, 0.5);              // Relation 1
        fucks.addInfluence(moneys, 0, true, 0.5);                // Relation 2
        fucks.addInfluence(bitches, 10, true, 1);                // Relation 3
        bitches.addInfluence(timeWithBitches, 10, true, -0.6);   // Relation 4
         
        fucks.setResourceCount(20);
        fucks.setResourceDesire(100);
 
        moneys.setResourceCount(70);
        moneys.setResourceDesire(50);
         
        timeWithBitches.setBaseValue(50);
         
        test.addToList(moneys);
        test.addToList(bitches);
        test.addToList(fucks);
        test.addToList(timeWithBitches);
        
        test.doStep();
        test.doStep();
        test.doStep();
        test.doStep();
        test.doStep();
        test.doStep();
        test.doStep();
        test.doStep();
        test.doStep();
        test.doStep();
        test.doStep();
        test.doStep();
        
        int moneysDesire = moneys.getDesire();
        int bitchesDesire = bitches.getDesire();
        int fucksDesire = fucks.getDesire();
        int timeWithBitchesDesire = timeWithBitches.getDesire();

         
        System.exit(0);
    }
}