package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.BankAccount.BankAccountDTO;
import bg.nbu.banking_app.data.dto.BankAccount.UpdateBankAccountDTO;
import bg.nbu.banking_app.data.entity.BankAccount;
import bg.nbu.banking_app.data.entity.Person;
import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.repository.BankAccountRepository;
import bg.nbu.banking_app.data.repository.UserRepository;
import bg.nbu.banking_app.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    private final MapperUtil mapperUtil = new MapperUtil();

    @Test
    void getMyBankAccountsReturnsMappedAccountsForAuthenticatedUserCustomer() {
        Person customer = new Person();
        setField(customer, "id", 7L);

        User user = new User();
        user.setEmail("client@test.com");
        user.setCustomer(customer);

        BankAccount account = new BankAccount();
        account.setBalance(new BigDecimal("1250.50"));
        account.setStatus(true);
        setField(account, "iban", "BG80BNBG96611020345678");
        setField(account, "customer", customer);

        when(userRepository.findByEmail("client@test.com")).thenReturn(Optional.of(user));
        when(bankAccountRepository.findByCustomerId(7L)).thenReturn(List.of(account));

        BankAccountServiceImpl service = new BankAccountServiceImpl(bankAccountRepository, userRepository, mapperUtil);

        List<BankAccountDTO> result = service.getMyBankAccounts("client@test.com");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getBalance()).isEqualByComparingTo("1250.50");
        assertThat(result.getFirst().isStatus()).isTrue();
        verify(bankAccountRepository).findByCustomerId(7L);
    }

    @Test
    void getMyBankAccountsReturnsEmptyListWhenUserHasNoCustomer() {
        User user = new User();
        user.setEmail("employee@test.com");
        user.setCustomer(null);

        when(userRepository.findByEmail("employee@test.com")).thenReturn(Optional.of(user));

        BankAccountServiceImpl service = new BankAccountServiceImpl(bankAccountRepository, userRepository, mapperUtil);

        assertThat(service.getMyBankAccounts("employee@test.com")).isEmpty();
    }

    @Test
    void getMyBankAccountsThrowsWhenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        BankAccountServiceImpl service = new BankAccountServiceImpl(bankAccountRepository, userRepository, mapperUtil);

        assertThatThrownBy(() -> service.getMyBankAccounts("missing@test.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found: missing@test.com");
    }

    @Test
    void updateBankAccountUpdatesBalanceAndStatus() {
        BankAccount existing = new BankAccount();
        existing.setBalance(new BigDecimal("100.00"));
        existing.setStatus(false);

        UpdateBankAccountDTO update = new UpdateBankAccountDTO(new BigDecimal("250.75"), true);

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bankAccountRepository.save(existing)).thenReturn(existing);

        BankAccountServiceImpl service = new BankAccountServiceImpl(bankAccountRepository, userRepository, mapperUtil);

        BankAccountDTO result = service.updateBankAccount(update, 1L);

        assertThat(result.getBalance()).isEqualByComparingTo("250.75");
        assertThat(result.isStatus()).isTrue();
        verify(bankAccountRepository).save(existing);
    }

    private static void setField(Object target, String fieldName, Object value) {
        Class<?> current = target.getClass();
        while (current != null) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            } catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }
        throw new IllegalArgumentException("Field not found: " + fieldName);
    }
}
