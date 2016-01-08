	private CostRepository getCostModel(DSEWorkflowConfiguration configuration) throws CoreException {
		String costModelFileName = configuration.getRawConfiguration().getAttribute(DSEConstantsContainer.COST_FILE, "");
		CostRepository cr =  (CostRepository)EMFHelper.loadFromXMIFile(costModelFileName, costPackage.eINSTANCE);
		if (cr == null){
			throw new CoreException(new Status(Status.ERROR, "de.uka.ipd.sdq.dsexplore", 0, "Cost model "+costModelFileName+" could not be loaded.", null));
		}
		return cr;
	}