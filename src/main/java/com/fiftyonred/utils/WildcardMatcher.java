package com.fiftyonred.utils;

public class WildcardMatcher {
	/**
	 * Performs a wildcard matching for the text and pattern
	 * provided.
	 * Source: http://www.adarshr.com/papers/wildcard
	 *
	 * @param text    the text to be tested for matches.
	 * @param pattern the pattern to be matched for.
	 *                This can contain the wildcard character '*' (asterisk).
	 * @return <tt>true</tt> if a match is found, <tt>false</tt>
	 * otherwise.
	 */
	public boolean match(String text, final String pattern) {
		// Create the cards by splitting using a RegEx. If more speed
		// is desired, a simpler character based splitting can be done.
		final String[] cards = pattern.split("\\*");

		// Iterate over the cards.
		for (int i = 0; i < cards.length; ++i) {
			final String card = cards[i];
			final int idx = text.indexOf(card);

			// Card not detected in the text.
			if (idx == -1) {
				return false;
			} else if (idx != 0 && i == 0) {
				return false; // test needs to start from 'card'
			}

			// Move ahead, towards the right of the text.
			text = text.substring(idx + card.length());
		}

		return true;
	}
}