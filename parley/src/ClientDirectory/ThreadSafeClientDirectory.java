package ClientDirectory;

/* NOTE:This file exists for graded tests */

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.*;
import java.util.concurrent.locks.ReentrantLock;
import ConnectedClient.ConnectedClient;

public class ThreadSafeClientDirectory implements ClientDirectory {

  private final HashMap<String, ConnectedClient> directory;

  private final Lock lock = new ReentrantLock();


  public ThreadSafeClientDirectory() {
    this.directory = new HashMap<>();
  }

  public ConnectedClient get(String identifier) {
    return this.directory.get(identifier);
  }

  public ConnectedClient add(String identifier, ConnectedClient client) {
    return this.directory.put(identifier, client);
  }

  public ConnectedClient remove(String identifier) {
    return this.directory.remove(identifier);
  }

  public ConnectedClient update(String oldname, String newName){
    lock.lock();
    try {

      // get the client
      ConnectedClient client = this.get(oldname);

      // check if there is actually a client with the old username
      if (client == null) {
        return null;
      }

      // check to see if the new username is not already in use, return null if is
      if (this.directory.get(newName) != null){
        return null;
      }
      // put the new username entry in
      this.directory.put(newName, client);
      // Delete the old username entry
      this.directory.remove(oldname);
      // return client for the thrill of it
      return client;

    } finally {
      lock.unlock();
    }
  }

  public Set keySet() {
    lock.lock();
    try {
      return this.directory.keySet();
    } finally {
      lock.unlock();
    }
  };


}
