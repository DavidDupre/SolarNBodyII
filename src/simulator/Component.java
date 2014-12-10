package simulator;

public abstract class Component {
	protected SimObject simObject;
	
	abstract public void update();
	abstract protected void initModifierDependents();
	
	public void setModifier(SimObject simObject) {
		this.simObject = simObject;
		initModifierDependents();
	}
}
