public class BeagleHelpers {
	private BeagleWebContext beagleContext;
	
	public BeagleHelpers(BeagleWebContext beagleContext) {
		this.beagleContext = beagleContext
	}
		
	public String setTitle(String title) {
		beagleContext.title = title
		return ''
	}
}
