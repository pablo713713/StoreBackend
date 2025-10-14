package com.example.onlinestore.service;

import com.example.onlinestore.model.Client;
import com.example.onlinestore.model.CreatorCreditCardPayment;
import com.example.onlinestore.model.Payment;
import com.example.onlinestore.repository.ClientRepository;
import com.example.onlinestore.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

	// Constantes compartidas para evitar duplicación de literales
	private static final String CARD_NUMBER = "4111111111117777";
	private static final String CARD_HOLDER = "John Doe";
	private static final String EXPIRY_DATE = "12/29";
	private static final String CVV = "123";
	private static final BigDecimal AMOUNT = new BigDecimal("123.45");
	private static final BigDecimal SMALL_AMOUNT = new BigDecimal("50.00");

    //tests for getPaymentDetails method

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private ClientRepository clientRepository;

	@InjectMocks
	private PaymentService paymentService;

	private Client client;

	@BeforeEach
	void setUp() {
		client = new Client("John", "Doe", "Smith", "ID1", "john@example.com", "pass");
	}

	@Test
	void getPaymentDetailsWithoutMethodReturnsDefaultMessage() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

		String details = paymentService.getPaymentDetails(1L);

		assertEquals("No se ha asignado método de pago", details);
	}

	@Test
	void getPaymentDetailsWithMethodReturnsDetails() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
		Payment payment = new CreatorCreditCardPayment(AMOUNT,
				CARD_NUMBER, CARD_HOLDER, EXPIRY_DATE, CVV);
		client.setPaymentMethod(payment);

		String details = paymentService.getPaymentDetails(1L);

		assertEquals("CreditCard ****7777", details);
	}

	//tests for makePayment method

	@Test
	void makePaymentWithoutMethodThrowsIllegalStateException() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

		assertThrows(IllegalStateException.class, () -> paymentService.makePayment(1L));
	}

	@Test
	void makePaymentWithMethodReturnsTrue() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
		Payment payment = new CreatorCreditCardPayment(SMALL_AMOUNT,
				CARD_NUMBER, CARD_HOLDER, EXPIRY_DATE, CVV);
		client.setPaymentMethod(payment);

		boolean result = paymentService.makePayment(1L);

		assertTrue(result);
	}

	//test for createPaymentForClient method

	private static final String TYPE_PAYPAL = "paypal";
	private static final String TYPE_CREDITCARD = "creditcard";

	@Test
	void createPaymentForClientClientNotFoundThrows() {
		when(clientRepository.findById(1L)).thenReturn(Optional.empty());
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
				paymentService.createPaymentForClient(1L, TYPE_PAYPAL, AMOUNT, "user@example.com", "pwd")
		);
		assertEquals("Cliente no encontrado", ex.getMessage());
	}

	@Test
	void createPaymentForClientPaypalMissingParamsThrows() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
				paymentService.createPaymentForClient(1L, TYPE_PAYPAL, AMOUNT, "solo_email")
		);
		assertEquals("Faltan parámetros para PayPal", ex.getMessage());
	}

	@Test
	void createPaymentForClientPaypalInvalidEmailThrows() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
				paymentService.createPaymentForClient(1L, TYPE_PAYPAL, AMOUNT, "email_invalido", "pwd")
		);
		assertEquals("Email de PayPal inválido", ex.getMessage());
	}

	@Test
	void createPaymentForClientPaypalSuccess() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
		Payment payment = paymentService.createPaymentForClient(1L, TYPE_PAYPAL, AMOUNT, "user@example.com", "pwdSegura");
		assertTrue(payment instanceof com.example.onlinestore.model.CreatorPayPalPayment);
		assertEquals(payment, client.getPaymentMethod());
	}

	@Test
	void createPaymentForClientCreditCardMissingParamsThrows() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
				paymentService.createPaymentForClient(1L, TYPE_CREDITCARD, AMOUNT, CARD_NUMBER, CARD_HOLDER)
		);
		assertEquals("Faltan parámetros para CreditCard", ex.getMessage());
	}

	@Test
	void createPaymentForClientCreditCardSuccess() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
		Payment payment = paymentService.createPaymentForClient(1L, TYPE_CREDITCARD, SMALL_AMOUNT,
				CARD_NUMBER, CARD_HOLDER, EXPIRY_DATE, CVV);
		assertTrue(payment instanceof CreatorCreditCardPayment);
		assertEquals(payment, client.getPaymentMethod());
	}
}

