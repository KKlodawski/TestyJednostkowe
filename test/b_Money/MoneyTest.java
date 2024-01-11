package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		/* Checks Integers with method that should return amount of Curreny as Integer */
		assertEquals(Optional.of(10000).get(), SEK100.getAmount());
		assertEquals(Optional.of(1000).get(), EUR10.getAmount());
		assertEquals(Optional.of(20000).get(), SEK200.getAmount());
		assertEquals(Optional.of(2000).get(), EUR20.getAmount());
		assertEquals(Optional.of(0).get(), SEK0.getAmount());
		assertEquals(Optional.of(0).get(), EUR0.getAmount());
		assertEquals(Optional.of(-10000).get(), SEKn100.getAmount());
	}

	@Test
	public void testGetCurrency() {
		/* Checks if method returns the same object of currency that was created with */
		assertSame(SEK, SEK100.getCurrency());
		assertSame(EUR, EUR10.getCurrency());
		assertSame(SEK, SEK200.getCurrency());
		assertSame(EUR, EUR20.getCurrency());
		assertSame(SEK, SEK0.getCurrency());
		assertSame(EUR, EUR0.getCurrency());
		assertSame(SEK, SEKn100.getCurrency());
		Currency DKK = new Currency("DKK", 0.2);
		assertSame(DKK, new Money(100,DKK).getCurrency());

	}

	@Test
	public void testToString() {
		/* Checks if method of Money, toString returns exact given string with format "(amount) (currencyname)"  */
		assertEquals("100.0 SEK", SEK100.toString());
		assertEquals("10.0 EUR", EUR10.toString());
		assertEquals("200.0 SEK", SEK200.toString());
		assertEquals("20.0 EUR", EUR20.toString());
		assertEquals("0.0 SEK", SEK0.toString());
		assertEquals("0.0 EUR", EUR0.toString());
		assertEquals("-100.0 SEK", SEKn100.toString());
		Currency DKK = new Currency("DKK", 0.2);
		Money DKKfloat1 = new Money(155,DKK);
		assertEquals("1.55 DKK", DKKfloat1.toString());
		Money DKKfloat2 = new Money(250,DKK);
		assertEquals("2.5 DKK", DKKfloat2.toString());

	}

	@Test
	public void testGlobalValue() {
		/* Checks if amount of money is properly converted to universal currency with its currency */
		assertEquals(Optional.of(1500).get(),SEK100.universalValue());
		assertEquals(Optional.of(1500).get(),EUR10.universalValue());
		assertEquals(Optional.of(3000).get(),SEK200.universalValue());
		assertEquals(Optional.of(3000).get(),EUR20.universalValue());
		assertEquals(Optional.of(0).get(),SEK0.universalValue());
		assertEquals(Optional.of(0).get(),EUR0.universalValue());
		assertEquals(Optional.of(-1500).get(),SEKn100.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		/*  Checks if amount of universal money of two currencies are equall */
		assertTrue(SEK0.equals(EUR0));
		assertTrue(SEK100.equals(EUR10));
		assertTrue(SEK200.equals(EUR20));
		Currency DKK = new Currency("DKK", 0.2);
		assertTrue(SEKn100.equals(new Money(-7500,DKK)));
		assertFalse(SEK200.equals(EUR10));

	}

	@Test
	public void testAdd() {
		/* Checks if add method properly converts currency of other money to given currency and adds those values together in given currency */
		assertEquals("200.0 SEK", SEK100.add(EUR10).toString());
		assertEquals("400.0 SEK", SEK200.add(EUR20).toString());
		assertEquals("100.0 SEK", SEKn100.add(EUR20).toString());
	}

	@Test
	public void testSub() {
		/* Checks if sub method properly converts currency of other money to given currency and subtracts other money from this money in given currency  */
		assertEquals("0.0 SEK", SEK100.sub(EUR10).toString());
		assertEquals("0.0 SEK", SEK200.sub(EUR20).toString());
		assertEquals("-300.0 SEK", SEKn100.sub(EUR20).toString());
		assertEquals("-100.0 SEK", SEK0.sub(EUR10).toString());
	}

	@Test
	public void testIsZero() {
		/* Checks if amount if given money is equal to 0*/
		assertTrue(SEK0.isZero());
		assertTrue(EUR0.isZero());
		assertFalse(SEK100.isZero());
		assertFalse(EUR10.isZero());
		assertFalse(SEK200.isZero());
		assertFalse(EUR20.isZero());
		assertFalse(SEKn100.isZero());
	}

	@Test
	public void testNegate() {
		/* Checks if amount of money is properly negated*/
		assertEquals("-100.0 SEK", SEK100.negate().toString());
		assertEquals("-10.0 EUR", EUR10.negate().toString());
		assertEquals("-200.0 SEK", SEK200.negate().toString());
		assertEquals("-20.0 EUR", EUR20.negate().toString());
		assertEquals("0.0 SEK", SEK0.negate().toString());
		assertEquals("0.0 EUR", EUR0.negate().toString());
		assertEquals("100.0 SEK", SEKn100.negate().toString());
	}

	@Test
	public void testCompareTo() {
		/* Checks if universal value of two given Money's are equal/bigger/lower */
		assertEquals(0, EUR0.compareTo(SEK0));
		assertEquals(-1, EUR10.compareTo(EUR20));
		assertEquals(1, EUR20.compareTo(EUR10));
		assertEquals(0, SEK100.compareTo(EUR10));
		assertEquals(-1, SEKn100.compareTo(EUR0));
		assertEquals(1, EUR20.compareTo(SEKn100));

	}
}
