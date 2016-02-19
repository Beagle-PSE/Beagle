package de.uka.ipd.sdq.beagle.core;

import java.util.Scanner;

/*
 * CHECKSTYLE:OFF
 */
public class TestFile {

	@SuppressWarnings("unused")
	public static void main(final String[] args) {
		// Benutzername und Passwort in der "Datenbank"
		final String benutzername = "Sonja";
		final String passwort = "xyz";

		/*
		 * In der Variable passwortEingabe wird das gespeichert, was der Benutzer auf der
		 * Konsole eingibt
		 */
		final String passwortEingabe = fragBenutzerPasswort(benutzername);

		// TODO Hier muss das Passwort ueberprueft werden!
	}

	/**
	 * Hilfsmethode zur Konsoleneingabe des Benutzerpassworts
	 *
	 * @param benutzername
	 * @return passwort, das der Benutzer eingegeben hat
	 */
	@SuppressWarnings("resource")
	static String fragBenutzerPasswort(final String benutzername) {
		System.out.println("Hallo " + benutzername + ". Bitte Passwort eingeben:");
		final Scanner sc = new Scanner(System.in);
		return sc.next();
	}

}
