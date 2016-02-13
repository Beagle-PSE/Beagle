# Extensionpoint Manual Test

For testing if the extension points are work right, follow these steps:

1. Import eclipse extension point test in eclipse.
2. Launch an “Eclipse Application” run configuration that includes Beagle and the test projects (*not* the “Beagle in Eclipse” run configuration)
3. In the must a button 'Beagle' available press it.
4. Choose the 'Available Measurement Tools' and press it.
5. Now must a window 'Available Tools' appear with the following kind of text:
    
        Available measurement tools:
        -
        a mocked measurement tool
    	Available measurement result analyser:
    	-
    	a mocked measurement result analyser
    	Available proposed expression analyser:
    	-
    	a mocked proposed expression analyser


If that happened the extension points test succeeded.

