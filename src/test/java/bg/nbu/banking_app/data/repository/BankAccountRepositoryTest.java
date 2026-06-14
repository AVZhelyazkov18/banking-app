package bg.nbu.banking_app.data.repository;

import bg.nbu.banking_app.data.entity.BankAccount;
import bg.nbu.banking_app.data.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BankAccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    void findByCustomerIdReturnsOnlyAccountsForRequestedCustomer() {
        Person firstCustomer = new Person();
        firstCustomer.setFirstName("Ivan");
        firstCustomer.setLastName("Petrov");
        firstCustomer.setPin("9001011234");
        firstCustomer.setClientNumber("CL00000001");
        entityManager.persistAndFlush(firstCustomer);

        Person secondCustomer = new Person();
        secondCustomer.setFirstName("Maria");
        secondCustomer.setLastName("Ivanova");
        secondCustomer.setPin("9002025678");
        secondCustomer.setClientNumber("CL00000002");
        entityManager.persistAndFlush(secondCustomer);

        BankAccount firstAccount = account("BG80BNBG96611020345678", firstCustomer, "100.00");
        BankAccount secondAccount = account("BG11BNBG22221234567890", firstCustomer, "200.00");
        BankAccount otherCustomerAccount = account("BG18RZBB91550123456789", secondCustomer, "300.00");

        entityManager.persist(firstAccount);
        entityManager.persist(secondAccount);
        entityManager.persist(otherCustomerAccount);
        entityManager.flush();

        List<BankAccount> result = bankAccountRepository.findByCustomerId(firstCustomer.getId());

        assertThat(result)
                .hasSize(2)
                .extracting(BankAccount::getIban)
                .containsExactlyInAnyOrder(
                        "BG80BNBG96611020345678",
                        "BG11BNBG22221234567890"
                );
    }

    private static BankAccount account(String iban, Person customer, String balance) {
        BankAccount account = new BankAccount();
        setField(account, "iban", iban);
        setField(account, "customer", customer);
        account.setBalance(new BigDecimal(balance));
        account.setStatus(true);
        return account;
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
