package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		/* Checks Strings with method that should return name of Curreny as String */
		assertEquals("SEK",SEK.getName());
		assertEquals("DKK",DKK.getName());
		assertEquals("EUR",EUR.getName());
	}
	
	@Test
	public void testGetRate() {
		/* Checks doubles with method that should return rate of Currency as double */
		assertEquals(Optional.of(0.15d).get(),SEK.getRate());
		assertEquals(Optional.of(0.20d).get(),DKK.getRate());
		assertEquals(Optional.of(1.5d).get(),EUR.getRate());
	}
	
	@Test
	public void testSetRate() {
		/* Setting rate of currency using setter of that currency then checks if rate of that currency equals to given double value*/
		SEK.setRate(0.5);
		assertEquals(Optional.of(0.5d).get(),SEK.getRate());
		DKK.setRate(0.5);
		assertEquals(Optional.of(0.5d).get(),DKK.getRate());
		EUR.setRate(0.5);
		assertEquals(Optional.of(0.5d).get(),EUR.getRate());
	}
	
	@Test
	public void testGlobalValue() {
		/* Checks if method calculates properly universal value of given amount of that currency */
		assertEquals(Optional.of(150).get(),SEK.universalValue(1000));
		assertEquals(Optional.of(3).get(),SEK.universalValue(20));
		assertEquals(Optional.of(200).get(),DKK.universalValue(1000));
		assertEquals(Optional.of(6).get(),DKK.universalValue(30));
		assertEquals(Optional.of(1500).get(),EUR.universalValue(1000));
		assertEquals(Optional.of(75).get(),EUR.universalValue(50));
	}
	
	@Test
	public void testValueInThisCurrency() {
		/* Checks if method calculates properly given amount of given currency to current currency */
		assertEquals(Optional.of(10000).get(),SEK.valueInThisCurrency(1000,EUR));
		assertEquals(Optional.of(750).get(),DKK.valueInThisCurrency(1000,SEK));
		assertEquals(Optional.of(133).get(),EUR.valueInThisCurrency(1000,DKK));
	}

}
