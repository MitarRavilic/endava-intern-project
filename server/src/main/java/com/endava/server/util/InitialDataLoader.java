package com.endava.server.util;

import com.endava.server.model.Transfer;
import com.endava.server.model.User;
import com.endava.server.model.UserAccount;
import com.endava.server.repository.TransferRepository;
import com.endava.server.repository.UserAccountRepository;
import com.endava.server.repository.UserRepository;
import com.endava.server.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

@Component
public class InitialDataLoader implements ApplicationRunner {

    Logger logger = Logger.getLogger(InitialDataLoader.class.getName());

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    TransferRepository transferRepository;

    @Autowired
    TransferService transferService;



    String[] currencies = {"EUR", "USD", "CAD", "RUB", "GBP", "JPY"};


    //Get random currencyCode
    public String getRandomCurrencyCode(){
        Random random = new Random();
        return currencies[random.nextInt(currencies.length)];
    }
    public BigDecimal getRandomDepositAmount(){
        Random random = new Random();
        return BigDecimal.valueOf(random.nextInt(5000));
    }

    //Generate User email and password from username
    public User generateUserWithUsername(String username) {
        String password = username.toLowerCase();
        String email = username.toLowerCase() + "@email.com";
        return new User(username, email, password);
    }


    //Generate UserAccount with random currency and balance
    public UserAccount generateUserAccountWithUser(User user){
        return new UserAccount(user, getRandomCurrencyCode());
    }
    //GenerateData
    public void generateUserAndAccountData(String username){
       User user = userRepository.save(generateUserWithUsername(username));
       UserAccount account = generateUserAccountWithUser(user);
       MoneyUtility.depositMoneyToAccount(account, getRandomDepositAmount());
       userAccountRepository.save(account);
    }

    //Generate Transfers
//    public void generateTransfer(){
//       ArrayList<UserAccount> accounts = userAccountRepository.findAllByCurrencyCode(getRandomCurrencyCode());
//        int size = accounts.size();
//       Random random = new Random();
//       UserAccount sender = accounts.get(Math.abs(random.nextInt(size)));
//       UserAccount recipient = accounts.get(Math.abs(random.nextInt(size)));
//       BigDecimal amount = BigDecimal.valueOf(Math.abs(random.nextInt(500)));
//       MoneyUtility.sendMoney(sender, recipient, amount);
//       transferRepository.save(new Transfer(sender, recipient, amount));
//    }

    public void generateDeposit(Long userId, String currencyCode){
        Random random = new Random();
        BigDecimal amount = BigDecimal.valueOf(random.nextInt(500));
        transferService.depositMoney(userId, currencyCode, amount);
    }


    @Override
    public void run(ApplicationArguments args) throws FileNotFoundException {
        String csvFile = "C:/Users/Mitar.Ravilic/OneDrive - ENDAVA/Desktop/postman data/usernames.txt";
        Scanner scanner = new Scanner(new File(csvFile));
        scanner.useDelimiter(",");
        Random random = new Random();

            while (scanner.hasNextLine()) {
               String username = scanner.next();
               generateUserAndAccountData(username);
            }

        for (int i = 1; i <80 ; i++) {
            Long randomId = (long) random.nextInt(10) + 1;

            generateDeposit(randomId, getRandomCurrencyCode());
        }


    }
}
