package com.corso.springboot.Customer_Subdomain.businesslayer;

import com.corso.springboot.Customer_Subdomain.datalayer.Customer;
import com.corso.springboot.Customer_Subdomain.datalayer.CustomerRepository;
import com.corso.springboot.Customer_Subdomain.datamapperlayer.CustomerRequestMapper;
import com.corso.springboot.Customer_Subdomain.datamapperlayer.CustomerResponseMapper;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerRequest;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerResponse;
import com.corso.springboot.email.businessLayer.EmailService;
import com.corso.springboot.email.datalayer.VerificationToken;
import com.corso.springboot.email.datalayer.VerificationTokenRepository;
import com.corso.springboot.utils.exceptions.CustomerNotFoundException;
import com.corso.springboot.utils.exceptions.InvalidRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerResponseMapper customerResponseMapper;

    private final CustomerRequestMapper customerRequestMapper;

    private final EmailService emailService;

    private final VerificationTokenRepository verificationTokenRepository;

    @Value("${backend.url}")
    private String backendDomain;

    @Value("${frontend.url}")
    private String frontendDomain;


    @Override
    public CustomerResponse getCustomerByUserId(String userId) {
        Customer customer = customerRepository.getCustomerByUserId(userId);

        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");


        return customerResponseMapper.toCustomerResponse(customer);
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest, String userId, String email) throws MessagingException {

        if (customerRepository.existsByUserId(userId))
            throw new InvalidRequestException("Customer already exists");

        Customer customer = customerRequestMapper.toCustomer(customerRequest);

        customer.setUserId(userId);

        if (!email.equals(customer.getEmail()))
            sendVerificationEmail(userId, customer.getEmail());
        else
            customer.setVerified(true);

        customerRepository.save(customer);




        return customerResponseMapper.toCustomerResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(CustomerRequest customerRequest, String userId, String email) throws MessagingException {
        Customer customer = customerRepository.getCustomerByUserId(userId);

        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        log.info("Updating customer for user {}", userId);
        log.info("Customer request {}", customerRequest);

        if (customerRequest.getEmail() != null && !customerRequest.getEmail().equals(customer.getEmail())) {
            customer.setVerified(false);
            sendVerificationEmail(userId, customerRequest.getEmail());
        }

        customer.setName(customerRequest.getName() != null ? customerRequest.getName() : customer.getName());
        customer.setEmail(customerRequest.getEmail() != null ? customerRequest.getEmail() : customer.getEmail());
        customer.setPhone(customerRequest.getPhone() != null ? customerRequest.getPhone() : customer.getPhone());
        customer.setAddress(customerRequest.getAddress() != null ? customerRequest.getAddress() : customer.getAddress());
        customer.setPostalCode(customerRequest.getPostalCode() != null ? customerRequest.getPostalCode() : customer.getPostalCode());
        customer.setCity(customerRequest.getCity() != null ? customerRequest.getCity() : customer.getCity());
        customer.setApartmentNumber(customerRequest.getApartmentNumber() != null ? customerRequest.getApartmentNumber() : customer.getApartmentNumber());

        if (email.equals(customer.getEmail()))
            customer.setVerified(true);

        customerRepository.save(customer);

        return customerResponseMapper.toCustomerResponse(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(String userId) {

        if (!customerRepository.existsByUserId(userId))
            throw new CustomerNotFoundException("Customer not found");

        customerRepository.deleteCustomerByUserId(userId);

        if (customerRepository.existsByUserId(userId))
            throw new InvalidRequestException("Customer not deleted");

    }

    @Override
    public boolean checkIfCustomerExists(String userId) {
        return customerRepository.existsByUserId(userId);
    }

    @Override
    public List<CustomerResponse> getCustomerByQueryParams(Map<String, String> requestParams) {

        log.info("Request params {}", requestParams);


        String name = requestParams.get("name");

        if (name == null)
            name = requestParams.get("username");


        String email = requestParams.get("email");


        if (name != null) {
            return customerResponseMapper.toCustomersResponse(customerRepository.findAllByNameStartingWithAndVerified(name, true));
        }

        if (email != null) {
            return customerResponseMapper.toCustomersResponse(customerRepository.findAllByEmailStartingWithAndVerified(email, true));
        }

        return customerResponseMapper.toCustomersResponse(customerRepository.findAll());

    }


    public void verifyEmail(String token, String email) {

        VerificationToken verificationToken = verificationTokenRepository.getVerificationTokenByTokenAndEmail(token, email);


        if (verificationToken == null)
            throw new InvalidRequestException("No verification request found");

        Calendar cal = Calendar.getInstance();
        if (verificationToken.getExpiryDate().before(cal.getTime())) {
            throw new InvalidRequestException("Token expired");
        }

        Customer customer = customerRepository.getCustomerByEmailAndUserId(email, verificationToken.getUserId());

        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        customer.setVerified(true);

        customerRepository.save(customer);

        verificationTokenRepository.delete(verificationToken);
    }

    @Override
    public void verifyCustomer(String userId) throws MessagingException {

        Customer customer = customerRepository.getCustomerByUserId(userId);

        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        if (customer.isVerified())
            throw new InvalidRequestException("Customer already verified");

        VerificationToken verificationToken = verificationTokenRepository.getVerificationTokenByUserId(userId);


        if (verificationToken != null) {
            if (!Objects.equals(verificationToken.getEmail(), customer.getEmail())) {
                verificationTokenRepository.delete(verificationToken);
                sendVerificationEmail(userId, customer.getEmail());
                return;
            }
            Calendar cal = Calendar.getInstance();
            if (verificationToken.getExpiryDate().after(cal.getTime())) {
                throw new InvalidRequestException("Email already sent, please check your inbox, otherwise try again in 15 minutes");
            }
            verificationTokenRepository.delete(verificationToken);
        }

        sendVerificationEmail(userId, customer.getEmail());

    }

    public void sendVerificationEmail(String userId, String email) throws MessagingException {
        VerificationToken verificationToken = new VerificationToken(userId, email);

        verificationTokenRepository.deleteAll(verificationTokenRepository.getAllByUserId(userId));

        verificationTokenRepository.save(verificationToken);


        HashMap<String, String> emailParams = new HashMap<>();


        emailParams.put("url", frontendDomain + "verify/email/" + verificationToken.getEmail() + "/" + verificationToken.getToken());

        emailService.sendEmail(email, "Verification de couriel/Verify your email", "verify-email", emailParams);


    }

}
