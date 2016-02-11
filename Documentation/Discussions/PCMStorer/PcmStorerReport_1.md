Zu allen Seff-Konzepten bis auf die Loops sind mir einige, schwerwiegende Probleme
aufgefallen als ich mich mit dem Storer beschäftigt habe.

Zunächst zu den InternalActions:

Eine InternalAction kann mehrere ResourceDemands enthalten.
Außerdem gibt es bislang keine Möglichkeit den RDIA Typ festzustellen (HDD, CPU...)
Wie soll also annotiert werden, wenn überhaupt nichts über die Resource bekannt ist?
Soll einfach an allen Resourcen einer InternalAction dieselbe StoEx annotiert werden?
Sehr schlechte Lösung, wie ich finde...


Als nächstes zu den SeffBranches:

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
für einzelne Branches werden von Somox nicht annotiert). Das führt bislang dazu,
dass ich nur EINE CodeSection für den gesamten SeffBranch angeben kann (und eine weitere
Pseudocode-Section, weil wir mindestens 2 fordern...)
(Noch ein Problem: es gibt 2 typen von BranchTransitions, aber ich denke uns reichen die
ProbabilisticBranchTransitions)
||||>Und nun Zukunftsmusik, falls wir das Problem (wie auch immer) gefixt bekommen sollten:<||||
Wie würden wir die Wahrscheinlichkeiten für mehrere Branches innerhalb einer BranchAction
über unsere seffKlassen annotieren? Wir fassen alle Branches als einen SeffBranch zusammen.
Allerdings können wir dann diesem nur eine einzige EvaluableExpression annotieren und
nicht genau spezifizieren zu welchem Branch diese denn nun gehört.



Zu guter letzt zu den ExternalCallActions:

Unser ExternalCallParameter entspricht dem Pcm-Modell ziemlich kläglich.
Eine ExternalCallAction enthält eine Menge  von InputVariablen und OutputVariablen,
bei unserem Ansatz gibt es nur eine. Diese Variablen setzen sich widerum zusammen aus
Type (STRUCTURE, VALUE ... ) und Specification (das ist der StoEx).
Vorschlag:
Die ExternalCallParameter würde ich vom Storer mal außen vor lassen, da diese von dem
genetischen Ansatz abhängen, den wir sehr wahrscheinlich nicht implementieren werden.