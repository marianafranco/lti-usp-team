package bb;

import jason.asSyntax.Literal;
import jason.bb.BeliefBase;
import jason.bb.IndexedBB;

public class MyIndexedBB extends IndexedBB {

	public MyIndexedBB() {  }
    public MyIndexedBB(BeliefBase next) {
        super(next);
    }
    
    @Override
    public boolean add(int index, Literal l) {
    	return super.add(l);
    }
}
