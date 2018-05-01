package uk.co.sethnavsoft.accountservice.dao;

import org.springframework.data.repository.CrudRepository;
import uk.co.sethnavsoft.accountservice.model.Account;

/**
 * Default Crud implementation for Account.
 */
public interface AccountRepository extends CrudRepository<Account, Long> {
}
