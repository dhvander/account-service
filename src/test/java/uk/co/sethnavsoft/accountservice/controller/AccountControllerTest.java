package uk.co.sethnavsoft.accountservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.co.sethnavsoft.accountservice.dao.AccountRepository;
import uk.co.sethnavsoft.accountservice.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

/**
 * This class tests the REST Api operations exposed by {@link AccountController}
 */
@RunWith(SpringRunner.class)
public class AccountControllerTest {


    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;


    @InjectMocks
    private AccountController accountController;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Before
    public void setUpMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }


    /**
     * Expecting 4 accounts in the response
     *
     * @throws Exception
     */
    @Test
    public void get_all_Accounts() throws Exception {

        //Create 4 accounts to be returned.
        Iterable<Account> accountList = createAccountList(3);
        Mockito.when(accountRepository.findAll())
                .thenReturn(accountList);

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/account/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", IsCollectionWithSize.hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", Is.is("Fname_0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName", Is.is("Fname_1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].firstName", Is.is("Fname_2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].firstName", Is.is("Fname_3")));
        ;
        Mockito.verify(accountRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(accountRepository);
    }


    /**
     * Test to create account with correct data.
     *
     * @throws Exception
     */
    @Test
    public void create_Account() throws Exception {
        Account account = new Account();
        account.setAccountNumber(1L);
        account.setFirstName("Rob");
        account.setSecondName("Van Dam");
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/account/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(account)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Account has been successfully added"));
        Mockito.verify(accountRepository, Mockito.times(1)).save(ArgumentMatchers.any(Account.class));
        Mockito.verifyNoMoreInteractions(accountRepository);
    }


    /**
     * This test should fail with bad data as it is not allowed to provide the id as input.
     */
    @Test
    public void create_Account_With_Id() throws Exception {
        Account account = new Account();
        account.setAccountNumber(1L);
        account.setId(1L);
        account.setFirstName("Rob");
        account.setSecondName("Van Dam");
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/account/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(account)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Account Id should not be set."));

        Mockito.verifyNoMoreInteractions(accountRepository);
    }

    /**
     * Delete account with given id.
     *
     * @throws Exception
     */
    @Test
    public void delete_Account() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/account/json/1")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Account successfully deleted"));
        Mockito.verify(accountRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(accountRepository);
    }


    private Iterable<Account> createAccountList(int rangeEnd) {
        List<Account> accounts = new ArrayList<>();
        LongStream.rangeClosed(0, rangeEnd).forEach(i -> {
                    Account account = new Account();
                    account.setId(i);
                    account.setFirstName("Fname_" + i);
                    account.setSecondName("SName_" + i);
                    account.setAccountNumber(i);
                    accounts.add(account);
                }
        );
        return accounts;
    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
