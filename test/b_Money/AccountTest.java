package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		testAccount.addTimedPayment("testPayment1",10,100,new Money(1000000, SEK),SweBank,"Alice");
		assertTrue(testAccount.timedPaymentExists("testPayment1"));
		testAccount.removeTimedPayment("testPayment1");
		assertFalse(testAccount.timedPaymentExists("testPayment1"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		testAccount.addTimedPayment("testPayment1",10,100,new Money(1000000, SEK),SweBank,"Alice");
		// 20 ticks | no payments
		for (int i = 0; i <= 10; i++) {
			testAccount.tick();
		}
		assertEquals(10000000, (int) testAccount.getBalance().getAmount());
		assertEquals(1000000, (int) SweBank.getBalance("Alice"));
		// 100 ticks | initial payment
		for (int i = 0; i <= 40; i++) {
			testAccount.tick();
		}
		assertEquals(9000000, (int) testAccount.getBalance().getAmount());
		assertEquals(2000000, (int) SweBank.getBalance("Alice"));

		// 122 ticks | 2 additional payments
		for (int i = 0; i <= 11; i++) {
			testAccount.tick();
		}
		assertEquals(7000000, (int) testAccount.getBalance().getAmount());
		assertEquals(4000000, (int) SweBank.getBalance("Alice"));
	}

	@Test
	public void testAddWithdraw() {
		testAccount.withdraw(new Money(1000000, SEK));
		assertEquals(9000000, (int) testAccount.getBalance().getAmount());
	}
	
	@Test
	public void testGetBalance() {
		assertEquals(10000000, (int) testAccount.getBalance().getAmount());
		assertSame(SEK, testAccount.getBalance().getCurrency());
	}
}
