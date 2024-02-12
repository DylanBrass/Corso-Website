package com.corso.springboot.Customer_Subdomain.businesslayer;

import com.corso.springboot.Customer_Subdomain.datalayer.Customer;
import com.corso.springboot.Customer_Subdomain.datalayer.CustomerRepository;
import com.corso.springboot.Customer_Subdomain.datamapperlayer.CustomerRequestMapper;
import com.corso.springboot.Customer_Subdomain.datamapperlayer.CustomerResponseMapper;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerRequest;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerResponse;
import com.corso.springboot.email.businessLayer.EmailService;
import com.corso.springboot.email.datalayer.VerificationTokenRepository;
import com.corso.springboot.utils.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class CustomerServiceUnitTest {

    Logger log = Logger.getLogger(CustomerServiceUnitTest.class.getName());

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerResponseMapper customerResponseMapper;

    @Mock
    private CustomerRequestMapper customerRequestMapper;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void getCustomerByUserId() {
        String userId = "google|123456789";

        CustomerResponse mockCustomerResponse = CustomerResponse.builder()
                .name("Alice Doe")
                .email("alice.doe@gmail.com")
                .phone("+1234567890")
                .postalCode("A1B 2C3")
                .address("123 Main St")
                .apartmentNumber("Apt 5")
                .city("Cityville")
                .build();

        Customer mockCustomer = Customer.builder()
                .name("Alice Doe")
                .email("alice.doe@gmail.com")
                .phone("+1234567890")
                .postalCode("A1B 2C3")
                .address("123 Main St")
                .apartmentNumber("Apt 5")
                .city("Cityville")
                .build();

        when(customerRepository.getCustomerByUserId(userId)).thenReturn(mockCustomer);

        when(customerResponseMapper.toCustomerResponse(mockCustomer)).thenReturn(mockCustomerResponse);

        CustomerResponse result = customerService.getCustomerByUserId(userId);


        assertEquals(mockCustomer.getAddress(), result.getAddress());

        assertEquals(mockCustomer.getApartmentNumber(), result.getApartmentNumber());

        assertEquals(mockCustomer.getCity(), result.getCity());

        assertEquals(mockCustomer.getEmail(), result.getEmail());

        assertEquals(mockCustomer.getName(), result.getName());

        assertEquals(mockCustomer.getPhone(), result.getPhone());

        assertEquals(mockCustomer.getPostalCode(), result.getPostalCode());


    }

    @Test
    void createCustomer() throws MessagingException {

        String userId = "google|123456789";

        CustomerRequest mockCustomerRequest = CustomerRequest.builder()
                .name("Alice Doe")
                .email("test@email.com")
                .phone("+1234567890")
                .postalCode("A1B 2C3")
                .address("123 Main St")
                .apartmentNumber("Apt 5")
                .city("Cityville")
                .build();

        Customer mockCustomer = Customer.builder()
                .name("Alice Doe")
                .email("test@email.com")
                .phone("+1234567890")
                .postalCode("A1B 2C3")
                .address("123 Main St")
                .apartmentNumber("Apt 5")
                .city("Cityville")
                .build();

        CustomerResponse mockCustomerResponse = CustomerResponse.builder()
                .name("Alice Doe")
                .email("test@email.com")
                .phone("+1234567890")
                .postalCode("A1B 2C3")
                .address("123 Main St")
                .apartmentNumber("Apt 5")
                .city("Cityville")
                .build();


        when(verificationTokenRepository.save(Mockito.any())).thenReturn(null);

        when(emailService.sendEmail(anyString(), anyString(), anyString(), anyMap())).thenReturn(200);

        when(customerRepository.existsByUserId(userId)).thenReturn(false);

        when(customerRequestMapper.toCustomer(mockCustomerRequest)).thenReturn(mockCustomer);

        when(customerRepository.save(mockCustomer)).thenReturn(mockCustomer);

        when(customerResponseMapper.toCustomerResponse(mockCustomer)).thenReturn(mockCustomerResponse);

        CustomerResponse result = customerService.createCustomer(mockCustomerRequest, userId, "email");

        assertEquals(mockCustomer.getAddress(), result.getAddress());

        assertEquals(mockCustomer.getApartmentNumber(), result.getApartmentNumber());

        assertEquals(mockCustomer.getCity(), result.getCity());

        assertEquals(mockCustomer.getEmail(), result.getEmail());

        assertEquals(mockCustomer.getName(), result.getName());

        assertEquals(mockCustomer.getPhone(), result.getPhone());

        assertEquals(mockCustomer.getPostalCode(), result.getPostalCode());


    }

    @Test
    void updateCustomer() throws MessagingException {

        String userId = "google|123456789";

        CustomerRequest mockCustomerRequest = CustomerRequest.builder()
                .name("Alice Doe")
                .email("test@email.com")
                .phone("+111111111")
                .postalCode("A1B 2C3")
                .address("123 Main St")
                .apartmentNumber("Apt 5")
                .city("new city")
                .build();

        Customer mockCustomerExisting = Customer.builder()
                .name("Alice Doe")
                .email("test@email.com")
                .phone("+1234567890")
                .postalCode("A1B 2C3")
                .address("123 Main St")
                .apartmentNumber("Apt 5")
                .city("Cityville")
                .build();

        CustomerResponse mockCustomerResponse = CustomerResponse.builder()
                .name("Alice Doe")
                .email("test@email.com")
                .phone("+111111111")
                .postalCode("A1B 2C3")
                .address("123 Main St")
                .apartmentNumber("Apt 5")
                .city("new city")
                .build();


        when(customerRepository.getCustomerByUserId(userId)).thenReturn(mockCustomerExisting);

        when(customerRequestMapper.toCustomer(mockCustomerRequest)).thenReturn(mockCustomerExisting);

        when(customerRepository.save(mockCustomerExisting)).thenReturn(mockCustomerExisting);

        when(customerResponseMapper.toCustomerResponse(mockCustomerExisting)).thenReturn(mockCustomerResponse);

        CustomerResponse result = customerService.updateCustomer(mockCustomerRequest, userId, "email");

        log.info("Phone: " + result.getPhone());

        log.info("City: " + result.getCity());


        assertEquals(mockCustomerExisting.getAddress(), result.getAddress());

        assertEquals(mockCustomerExisting.getApartmentNumber(), result.getApartmentNumber());

        assertEquals(mockCustomerExisting.getCity(), result.getCity());

        assertEquals(mockCustomerExisting.getEmail(), result.getEmail());

        assertEquals(mockCustomerExisting.getName(), result.getName());

        assertEquals(mockCustomerExisting.getPhone(), result.getPhone());

        assertEquals(mockCustomerExisting.getPostalCode(), result.getPostalCode());

    }

    @Test
    void updateManyFieldsForCustomer() throws MessagingException {

        String userId = "google|123456789";

        CustomerRequest mockCustomerRequest = CustomerRequest.builder()
                .name("Alice Doe")
                .email("new@email.com")
                .phone("+111111111")
                .postalCode("A9W 2C3")
                .address("123432 Main St")
                .apartmentNumber("Apt 55")
                .city("new city")
                .build();

        Customer mockCustomerExisting = Customer.builder()
                .name("Alice Doe")
                .email("test@email.com")
                .phone("+1234567890")
                .postalCode("A1B 2C3")
                .address("123 Main St")
                .apartmentNumber("Apt 5")
                .city("Cityville")
                .build();


        CustomerResponse mockCustomerResponse = CustomerResponse.builder()
                .name("Alice Doe")
                .email("new@email.com")
                .phone("+111111111")
                .postalCode("A9W 2C3")
                .address("123432 Main St")
                .apartmentNumber("Apt 55")
                .city("new city")
                .build();

        when(customerRepository.getCustomerByUserId(userId)).thenReturn(mockCustomerExisting);

        when(customerRequestMapper.toCustomer(mockCustomerRequest)).thenReturn(mockCustomerExisting);

        when(customerRepository.save(mockCustomerExisting)).thenReturn(mockCustomerExisting);

        when(customerResponseMapper.toCustomerResponse(mockCustomerExisting)).thenReturn(mockCustomerResponse);

        CustomerResponse result = customerService.updateCustomer(mockCustomerRequest, userId, "email");

        assertEquals(mockCustomerExisting.getAddress(), result.getAddress());

        assertEquals(mockCustomerExisting.getApartmentNumber(), result.getApartmentNumber());

        assertEquals(mockCustomerExisting.getCity(), result.getCity());

        assertEquals(mockCustomerExisting.getEmail(), result.getEmail());

        assertEquals(mockCustomerExisting.getName(), result.getName());

        assertEquals(mockCustomerExisting.getPhone(), result.getPhone());

        assertEquals(mockCustomerExisting.getPostalCode(), result.getPostalCode());

    }

    @Test
    void deleteCustomer() {
        // Given
        String userId = "google|123456789";

        when(customerRepository.existsByUserId(userId)).thenReturn(true, false);

        // When
        customerService.deleteCustomer(userId);

        // Then
        Mockito.verify(customerRepository, Mockito.times(2)).existsByUserId(userId);
        Mockito.verify(customerRepository, Mockito.times(1)).deleteCustomerByUserId(userId);
    }


    @Test
    void deleteFailsForCustomer() {

        String userId = "google|123456789";

        when(customerRepository.existsByUserId(userId)).thenReturn(true);

        assertThrows(InvalidRequestException.class, () -> customerService.deleteCustomer(userId));

    }

    @Test
    void checkIfCustomerExists() {

        String userId = "google|123456789";

        when(customerRepository.existsByUserId(userId)).thenReturn(true);

        boolean result = customerService.checkIfCustomerExists(userId);

        assertTrue(result);
    }


    @Test
    void getCustomerByName() {
        Map<String, String> requestParams = Map.of("name", "Alice");

        Customer mockCustomer = Customer.builder()
                .name("Alice Doe")
                .email("test@email.com")
                .phone("+1234567890")
                .postalCode("A1B 2C3")
                .address("123 Main St")
                .apartmentNumber("Apt 5")
                .city("Cityville")
                .build();

        when(customerRepository.findAllByNameStartingWithAndVerified("Alice", true)).thenReturn(List.of(mockCustomer));

        CustomerResponse mockCustomerResponse = CustomerResponse.builder()
                .name("Alice Doe")
                .email(mockCustomer.getEmail())
                .phone(mockCustomer.getPhone())
                .postalCode(mockCustomer.getPostalCode())
                .address(mockCustomer.getAddress())
                .apartmentNumber(mockCustomer.getApartmentNumber())
                .city(mockCustomer.getCity())
                .build();

        when(customerResponseMapper.toCustomersResponse(List.of(mockCustomer))).thenReturn(List.of(mockCustomerResponse));


        List<CustomerResponse> result = customerService.getCustomerByQueryParams(requestParams);

        assertEquals(mockCustomerResponse.getName(), result.get(0).getName());

        assertEquals(mockCustomerResponse.getEmail(), result.get(0).getEmail());

        assertEquals(mockCustomerResponse.getPhone(), result.get(0).getPhone());
    }


    @Test
    void getCustomerByEmail() {
        Map<String, String> requestParams = Map.of("email", "test@email.com");

        Customer mockCustomer = Customer.builder()
                .name("Alice Doe")
                .email(requestParams.get("email"))
                .phone("+1234567890")
                .postalCode("A1B 2C3")
                .address("123 Main St")
                .apartmentNumber("Apt 5")
                .city("Cityville")
                .build();

        when(customerRepository.findAllByEmailStartingWithAndVerified(requestParams.get("email"), true)).thenReturn(List.of(mockCustomer));

        CustomerResponse mockCustomerResponse = CustomerResponse.builder()
                .name(mockCustomer.getName())
                .email(mockCustomer.getEmail())
                .phone(mockCustomer.getPhone())
                .postalCode(mockCustomer.getPostalCode())
                .address(mockCustomer.getAddress())
                .apartmentNumber(mockCustomer.getApartmentNumber())
                .city(mockCustomer.getCity())
                .build();

        when(customerResponseMapper.toCustomersResponse(List.of(mockCustomer))).thenReturn(List.of(mockCustomerResponse));

        List<CustomerResponse> result = customerService.getCustomerByQueryParams(requestParams);

        assertEquals(mockCustomerResponse.getName(), result.get(0).getName());

        assertEquals(mockCustomerResponse.getEmail(), result.get(0).getEmail());
    }


}