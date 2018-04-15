package encoder;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.io.*;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
public class Application extends Thread {
private ServerSocket serverSocket;   
private static final Logger log = LoggerFactory.getLogger(Application.class);
BigInteger GAS_LIMIT = new BigInteger("150000000");
BigInteger GAS_PRICE = new BigInteger("150000000");
String string = new String("teststring");
Credentials credentials =  null;
ProofOfExistence contract = null;
public Application() throws IOException {
    serverSocket = new ServerSocket(4500);
    serverSocket.setSoTimeout(10000);
 }

    public static void main(String[] args) throws Exception {
        // new Application().run();

        try {
            Thread t = new Application();
            t.start();
         } catch (Exception e) {
            e.printStackTrace();
         }
    }

    

    public void run() {

        // We start by creating a new web3j instance to connect to remote nodes on the network.
        // Note: if using web3j Android, use Web3jFactory.build(...
        Web3j web3j = Web3j.build(new HttpService());  
        // log.info("Connected to Ethereum client version: "
        //         + web3j.web3ClientVersion().send().getWeb3ClientVersion());

        // We then need to load our Ethereum wallet file
        // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
        try {
            credentials = WalletUtils.loadCredentials("", "/home/will/.ethereum/testnet/keystore");
            log.info("Credentials loaded");
            contract = ProofOfExistence.load(
            "0x<address>|<ensName>", web3j, credentials, GAS_PRICE, GAS_LIMIT);
        log.info("Contract loaded");
        } catch (Exception e) {}
        

        

        while(true) {
            try {
                Socket server = serverSocket.accept();
            
                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
            
                System.out.println(in.readUTF());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");


                TransactionReceipt transactionReceipt = contract.notarizeHash(new BigInteger("15000"), string.getBytes()).send();
            } catch (SocketTimeoutException s) {
               System.out.println("Socket timed out!");
               break;
            } catch (Exception e) {
               e.printStackTrace();
               break;
            }
         }
         
    }
}