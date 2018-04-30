package uk.co.sethnavsoft.accountservice.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void get_all_Accounts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/account/json")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void create_Account() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/account/json")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void delete_Account() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/account/json/1")).andExpect(MockMvcResultMatchers.status().isOk());
    }

}
