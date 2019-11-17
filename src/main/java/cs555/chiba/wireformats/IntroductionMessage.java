package cs555.chiba.wireformats;

import cs555.chiba.service.Identity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class IntroductionMessage extends Message {

   private Identity identity;
   private Socket socket; // this is used to correct the receiver thread cache key

   public IntroductionMessage(Identity identity) {
      super(Protocol.INTRODUCTION);
      this.identity = identity;
   }

   public IntroductionMessage(byte[] message, Socket socket) throws IOException {
      super(Protocol.REGISTER, message);
      this.socket = socket;
   }

   @Override void parse(DataInputStream input) throws IOException {
      String key = readString(input);
      String uuid = readString(input);
      this.identity = Identity.builder().withIdentityKey(key).build();
   }

   @Override void spool(DataOutputStream output) throws IOException {
      sendString(this.identity.getIdentityKey(), output);
   }

   public Identity getIdentity() {
      return this.identity;
   }

   public Socket getSocket() {
      return this.socket;
   }

   @Override public String toString() {
      return "IntroductionMessage{" + "identity=" + identity + "} " + super.toString();
   }
}
