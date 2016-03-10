# Manual Tests

## GUI Tests

This manual test check if the GUI works.

#### Initialization

    1 For testing the GUI a jUnit Launch Configuration is needed.
    2 In this Launch Configuration are tests from the project which shall analysed with Beagle.
    3 It also must be possible that are there more than one Launch Configuration.

#### Test GUI

    1 By right click a PCM component a field to 'Analyse with Beagle' must be available.
    2 By right click on the internal_architecture_model.repository a field to 'Analyse with Beagle' must be available.
    3 By click 'Analyse with Beagle' a window must appear with a progressbar which shows that Beagle is running.
    4 When Beagle is running it must be possible to pause the analyse by click the 'pause' button in the running dialog.
    5 By click the 'pause' button a dialog must appear that the analysis is paused.
    6 In the pause dialog it must by possible to resume the analysis by click 'continue'. 
    7 By click the 'continue' button a dialog must appear that the analysis is running again.
    8 The running dialog must close (and stop analysis) by click the 'abort' button.
    9 The pause dialog must close (and stop analysis) by click the 'abort' button.
    10 When the 'abort' button is clicked a dialog must appear which ask "Are you sure?" to assure oneself want to abort the analysis.
    11 By confirming this dialog it must close the dialog and stop the analysis.

    12 By right click on the internal_architecture_model.repository on "Run As" and than "Run Cofiguration..." it must be possible to adjust Beagle.
    13 In the Run Configuration dialog must be a tab called "Beagle".
    14 Under this tab must be undertabs with the single PCM Component which Beagle can analyse.
    15 Also there must be one undertab where the hole project can be analysed with.
    16 On the right side of the Run Configuration window must be these five tabs:
    Project, Selection Overview, Timeout, Launch Configuration and Common.

    17.1 In Project must be possible to choose a project which shall analysed.
    17.2 In Selection Overview must be a list with all selected PCM components which shall be anaylsed.
    17.3.1 In Timeout it must be possible to choose between an adaptive, custom and no timeout by a radio button.
    17.3.2 The radio button should work as well by clicking the text beside the button.
    17.3.3 By choose a other type of timeout, as custom timeout the field for set the custom timeout should disabled and grayed out.
    17.3.4 In the custom timeout field must only allowed to set digits.
    17.3.5 The run button should grayed out when no number is filled in the custom timeout field.
    17.4.1 In Launch Configurations shuld all Lanch Configurations listed which fits to the project.
    17.4.2 These Launch Configuration must be check on with a checkbox.
    17.4.3 Over these Launch Configuration must be a checkbox which hide configuration of other projects.
    17.4.4 By uncheck this box all other Lauch Configuration must appear.

## Extensionpoint Manual Test

For testing if the extension points are work right, follow these steps:

    1. Import eclipse extension point test in eclipse.
    2. Run Eclipse in Eclipse.
    3. In the must a button 'Beagle' avaiable press it.
    4. Choose the 'Available Measurement Tools' and press it.
    5. Now must a window 'Avariable Tools' appear with the following kind of text:
    
        Available measurement tools:
        -
        a mocked measurement tool
        Available measurement result analyser:
        -
        a mocked measurement result analyser
        Available proposed expression analyser:
        -
        a mocked proposed expression analyser


If that happend the extension points test succeeded.