Zu allen Seff-Konzepten bis auf die Loops sind mir einige, schwerwiegende Probleme
aufgefallen als ich mich mit dem Storer besch�ftigt habe.

Zun�chst zu den InternalActions:

Eine InternalAction kann mehrere ResourceDemands enthalten.
Au�erdem gibt es bislang keine M�glichkeit den RDIA Typ festzustellen (HDD, CPU...)
Wie soll also annotiert werden, wenn �berhaupt nichts �ber die Resource bekannt ist?
Soll einfach an allen Resourcen einer InternalAction dieselbe StoEx annotiert werden?
Sehr schlechte L�sung, wie ich finde...


Als n�chstes zu den SeffBranches:

Begriffsverdeutlichung/Definition:
"BranchAction"
Der gesamte Branch mit all seinen Verzweigungen
Zb
switch(number) {
	case 1:	;
	case 2:	;
	case ...:	;
	case default:	;
}

"Branch" oder "einzelner Branch"
Eine Verzweigung
Zb
case 1:	;


Wir bekommen bislang keine einzelnen Branches einer BranchAction (genauer: Die CodeSections
f�r einzelne Branches werden von Somox nicht annotiert). Das f�hrt bislang dazu,
dass ich nur EINE CodeSection f�r den gesamten SeffBranch angeben kann (und eine weitere
Pseudocode-Section, weil wir mindestens 2 fordern...)
(Noch ein Problem: es gibt 2 typen von BranchTransitions, aber ich denke uns reichen die
ProbabilisticBranchTransitions)
||||>Und nun Zukunftsmusik, falls wir das Problem (wie auch immer) gefixt bekommen sollten:<||||
Wie w�rden wir die Wahrscheinlichkeiten f�r mehrere Branches innerhalb einer BranchAction
�ber unsere seffKlassen annotieren? Wir fassen alle Branches als einen SeffBranch zusammen.
Allerdings k�nnen wir dann diesem nur eine einzige EvaluableExpression annotieren und
nicht genau spezifizieren zu welchem Branch diese denn nun geh�rt.



Zu guter letzt zu den ExternalCallActions:

Unser ExternalCallParameter entspricht dem Pcm-Modell ziemlich kl�glich.
Eine ExternalCallAction enth�lt eine Menge  von InputVariablen und OutputVariablen,
bei unserem Ansatz gibt es nur eine. Diese Variablen setzen sich widerum zusammen aus
Type (STRUCTURE, VALUE ... ) und Specification (das ist der StoEx).
Vorschlag:
Die ExternalCallParameter w�rde ich vom Storer mal au�en vor lassen, da diese von dem
genetischen Ansatz abh�ngen, den wir sehr wahrscheinlich nicht implementieren werden.