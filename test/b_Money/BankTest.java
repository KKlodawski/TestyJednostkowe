package b_Money;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank", SweBank.getName());
		assertEquals("Nordea", Nordea.getName());
		assertEquals("DanskeBank", DanskeBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertSame(SEK, SweBank.getCurrency());
		assertSame(SEK, Nordea.getCurrency());
		assertSame(DKK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		// I added a getter of accountList to get information of accounts in setUp() are created correctly
		// These tests failed
		assertTrue(SweBank.getAccountlist().containsKey("Ulrika"));
		assertTrue(SweBank.getAccountlist().containsKey("Bob"));
		assertTrue(Nordea.getAccountlist().containsKey("Bob"));
		assertTrue(DanskeBank.getAccountlist().containsKey("Gertrud"));

	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		//These tests failed
		try{
			SweBank.deposit("Abba",new Money(100,DKK));
			fail("Doesn't throw " + String.valueOf(new AccountDoesNotExistException()));
		} catch (AccountDoesNotExistException e) {
			assertTrue(true);
		}
		SweBank.deposit("Ulrika",new Money(100,DKK));
		assertEquals(133, (int) SweBank.getAccountlist().get("Ulrika").getBalance().getAmount());
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		//These test failed
		try{
			SweBank.deposit("Abba",new Money(100,DKK));
			fail("Doesn't throw " + String.valueOf(new AccountDoesNotExistException()));
		} catch (AccountDoesNotExistException e) {
			assertTrue(true);
		}
		SweBank.withdraw("Ulrika",new Money(100,DKK));
		assertEquals(-133, (int) SweBank.getAccountlist().get("Ulrika").getBalance().getAmount());
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		try {
			assertEquals(0, (int) SweBank.getBalance("Abba"));
			fail("Doesn't throw " + String.valueOf(new AccountDoesNotExistException()));
		} catch (AccountDoesNotExistException e) {
			assertTrue(true);
		}

		assertEquals(0, (int) SweBank.getBalance("Ulrika"));
		SweBank.deposit("Ulrika",new Money(100,SEK));
		assertEquals(100, (int) SweBank.getBalance("Ulrika"));
		assertEquals(0, (int) SweBank.getBalance("Bob"));
		assertEquals(0, (int) Nordea.getBalance("Bob"));
		assertEquals(0, (int) DanskeBank.getBalance("Gertrud"));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		// First overload, transfer between banks
		try {
			SweBank.transfer("Ulrika",DanskeBank,"Bob",new Money(100,DKK));
			fail("Doesn't throw " + String.valueOf(new AccountDoesNotExistException()));
		} catch (AccountDoesNotExistException e) {
			assertTrue(true);
		}
		SweBank.transfer("Ulrika",DanskeBank,"Gertrud",new Money(100,SEK));
		assertEquals(-100, (int) SweBank.getBalance("Ulrika"));
		assertEquals( 75, (int) DanskeBank.getBalance("Gertrud"));

		// Second overload, transfer between the same bank
		//This failed
		try {
			SweBank.transfer("Ulrika","Getrud",new Money(100,DKK));
			fail("Doesn't throw " + String.valueOf(new AccountDoesNotExistException()));
		} catch (AccountDoesNotExistException e) {
			assertTrue(true);
		}
		SweBank.transfer("Ulrika","Bob",new Money(100,DKK));
		// Balance of Ulrika is -100 due to transfer few lines higher
		assertEquals(-233, (int) SweBank.getBalance("Ulrika"));
		assertEquals( 133, (int) SweBank.getBalance("Bob"));

	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		SweBank.addTimedPayment("Ulrika","testPayment1",10,100,new Money(100, SEK),Nordea,"Bob");
		// 20 ticks | no payments
		for (int i = 0; i <= 10; i++) {
			SweBank.tick();
		}
		assertEquals(0, (int) SweBank.getBalance("Ulrika"));
		assertEquals(0, (int) Nordea.getBalance("Bob"));
		// 100 ticks | initial payment
		for (int i = 0; i <= 40; i++) {
			SweBank.tick();
		}
		assertEquals(-100, (int) SweBank.getBalance("Ulrika"));
		assertEquals(100, (int) Nordea.getBalance("Bob"));
		// 122 ticks | 2 additional payments
		for (int i = 0; i <= 11; i++) {
			SweBank.tick();
		}
		assertEquals(-300, (int) SweBank.getBalance("Ulrika"));
		assertEquals(300, (int) Nordea.getBalance("Bob"));

		SweBank.removeTimedPayment("Ulrika","testPayment1");

		// 144 ticks | 2 additional payments undone because timedPayment is removed
		for (int i = 0; i <= 11; i++) {
			SweBank.tick();
		}

		assertEquals(-300, (int) SweBank.getBalance("Ulrika"));
		assertEquals(300, (int) Nordea.getBalance("Bob"));
	}
}
