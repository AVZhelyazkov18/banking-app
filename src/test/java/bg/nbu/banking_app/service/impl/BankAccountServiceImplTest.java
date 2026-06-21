package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.BankAccount.BankAccountDTO;
import bg.nbu.banking_app.data.dto.BankAccount.CreateBankAccountDTO;
import bg.nbu.banking_app.data.dto.BankAccount.UpdateBankAccountDTO;
import bg.nbu.banking_app.data.entity.BankAccount;
import bg.nbu.banking_app.data.entity.Person;
import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.repository.BankAccountRepository;
import bg.nbu.banking_app.data.repository.CustomerRepository;
import bg.nbu.banking_app.data.repository.UserRepository;
import bg.nbu.banking_app.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MapperUtil mapperUtil;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private void mockModelMapper() {
        when(mapperUtil.getModelMapper()).thenReturn(new ModelMapper());
    }

    private void setId(Object entity, long id) {
        ReflectionTestUtils.setField(entity, "id", id);
    }

    @Test
    void getAllBankAccountsShouldReturnMappedDtos() {
        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();

        BankAccountDTO dto1 = new BankAccountDTO();
        BankAccountDTO dto2 = new BankAccountDTO();

        when(bankAccountRepository.findAll()).thenReturn(List.of(account1, account2));
        when(mapperUtil.mapList(List.of(account1, account2), BankAccountDTO.class))
                .thenReturn(List.of(dto1, dto2));

        List<BankAccountDTO> result = bankAccountService.getAllBankAccounts();

        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));

        verify(bankAccountRepository).findAll();
        verify(mapperUtil).mapList(List.of(account1, account2), BankAccountDTO.class);
    }

    @Test
    void getBankAccountShouldReturnDtoWhenAccountExists() {
        mockModelMapper();

        BankAccount account = new BankAccount();
        setId(account, 1L);
        account.setIban("BG123456789");
        account.setBalance(new BigDecimal("1000.00"));
        account.setStatus(true);

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        BankAccountDTO result = bankAccountService.getBankAccount(1L);

        assertNotNull(result);
        verify(bankAccountRepository).findById(1L);
        verify(mapperUtil).getModelMapper();
    }

    @Test
    void getBankAccountShouldThrowWhenAccountDoesNotExist() {
        when(bankAccountRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bankAccountService.getBankAccount(99L)
        );

        assertEquals(
                "Bank Account with id 99 not found and cannot be read",
                exception.getMessage()
        );

        verify(bankAccountRepository).findById(99L);
        verify(mapperUtil, never()).getModelMapper();
    }

    @Test
    void createBankAccountShouldCreateInactiveAccountForCustomer() {
        mockModelMapper();

        Person customer = new Person();
        setId(customer, 10L);

        CreateBankAccountDTO request = new CreateBankAccountDTO();
        request.setCustomerId(10L);
        request.setBalance(new BigDecimal("500.00"));

        when(customerRepository.findById(10L)).thenReturn(Optional.of(customer));

        ArgumentCaptor<BankAccount> accountCaptor = ArgumentCaptor.forClass(BankAccount.class);

        when(bankAccountRepository.save(accountCaptor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BankAccountDTO result = bankAccountService.createBankAccount(request);

        BankAccount savedAccount = accountCaptor.getValue();

        assertNotNull(result);
        assertNotNull(savedAccount.getIban());
        assertTrue(savedAccount.getIban().startsWith("BG"));
        assertEquals(20, savedAccount.getIban().length());
        assertEquals(new BigDecimal("500.00"), savedAccount.getBalance());
        assertFalse(savedAccount.isStatus());
        assertSame(customer, savedAccount.getCustomer());

        verify(customerRepository).findById(10L);
        verify(bankAccountRepository).save(any(BankAccount.class));
    }

    @Test
    void createBankAccountShouldThrowWhenCustomerDoesNotExist() {
        CreateBankAccountDTO request = new CreateBankAccountDTO();
        request.setCustomerId(99L);
        request.setBalance(new BigDecimal("500.00"));

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bankAccountService.createBankAccount(request)
        );

        assertEquals("Customer with id 99 not found", exception.getMessage());

        verify(customerRepository).findById(99L);
        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    void updateBankAccountShouldUpdateBalanceAndStatus() {
        mockModelMapper();

        BankAccount existingAccount = new BankAccount();
        setId(existingAccount, 1L);
        existingAccount.setIban("BG123456789");
        existingAccount.setBalance(new BigDecimal("100.00"));
        existingAccount.setStatus(false);

        UpdateBankAccountDTO request = new UpdateBankAccountDTO();
        request.setBalance(new BigDecimal("999.99"));
        request.setStatus(true);

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));
        when(bankAccountRepository.save(existingAccount)).thenReturn(existingAccount);

        BankAccountDTO result = bankAccountService.updateBankAccount(request, 1L);

        assertNotNull(result);
        assertEquals(new BigDecimal("999.99"), existingAccount.getBalance());
        assertTrue(existingAccount.isStatus());

        verify(bankAccountRepository).findById(1L);
        verify(bankAccountRepository).save(existingAccount);
    }

    @Test
    void updateBankAccountShouldThrowWhenAccountDoesNotExist() {
        UpdateBankAccountDTO request = new UpdateBankAccountDTO();
        request.setBalance(new BigDecimal("999.99"));
        request.setStatus(true);

        when(bankAccountRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bankAccountService.updateBankAccount(request, 99L)
        );

        assertEquals(
                "Bank account with id 99 not found and cannot be updated",
                exception.getMessage()
        );

        verify(bankAccountRepository).findById(99L);
        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    void deleteBankAccountShouldDeleteById() {
        bankAccountService.deleteBankAccount(1L);

        verify(bankAccountRepository).deleteById(1L);
    }

    @Test
    void getMyBankAccountsShouldReturnAccountsForLoggedUserCustomer() {
        Person customer = new Person();
        setId(customer, 10L);

        User user = new User();
        user.setEmail("user@test.com");
        user.setCustomer(customer);

        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();

        BankAccountDTO dto1 = new BankAccountDTO();
        BankAccountDTO dto2 = new BankAccountDTO();

        List<BankAccount> accounts = List.of(account1, account2);
        List<BankAccountDTO> dtos = List.of(dto1, dto2);

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(bankAccountRepository.findByCustomerId(10L)).thenReturn(accounts);
        when(mapperUtil.mapList(accounts, BankAccountDTO.class)).thenReturn(dtos);

        List<BankAccountDTO> result = bankAccountService.getMyBankAccounts("user@test.com");

        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));

        verify(userRepository).findByEmail("user@test.com");
        verify(bankAccountRepository).findByCustomerId(10L);
        verify(mapperUtil).mapList(accounts, BankAccountDTO.class);
    }

    @Test
    void getMyBankAccountsShouldReturnEmptyListWhenUserHasNoCustomer() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setCustomer(null);

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        List<BankAccountDTO> result = bankAccountService.getMyBankAccounts("user@test.com");

        assertTrue(result.isEmpty());

        verify(userRepository).findByEmail("user@test.com");
        verify(bankAccountRepository, never()).findByCustomerId(anyLong());
        verify(mapperUtil, never()).mapList(anyList(), eq(BankAccountDTO.class));
    }

    @Test
    void getMyBankAccountsShouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bankAccountService.getMyBankAccounts("missing@test.com")
        );

        assertEquals("User not found: missing@test.com", exception.getMessage());

        verify(userRepository).findByEmail("missing@test.com");
        verify(bankAccountRepository, never()).findByCustomerId(anyLong());
    }
}