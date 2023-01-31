package XMLSerialization;

import user.User;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializationAndDeserialization {
    public static void serializeToXML(HashMap<String, User> users) throws IOException
    {
        FileOutputStream fos = new FileOutputStream("users_data_info.xml", true);
        fos.getChannel().truncate(0);
        XMLEncoder encoder = new XMLEncoder(fos);
        encoder.setExceptionListener(new ExceptionListener() {
            public void exceptionThrown(Exception e) {
                System.out.println("Exception! :"+e.toString());
            }
        });
        for (Map.Entry<String, User> user:users.entrySet()) {
            encoder.writeObject(user.getValue());
        }
        encoder.close();
        fos.close();
    }

//    public static User deserializeFromXML() throws IOException {
//        FileInputStream fis = new FileInputStream("users_data_info.xml");
//        XMLDecoder decoder = new XMLDecoder(fis);
//        User decodedSettings = (User) decoder.readObject();
//        decoder.close();
//        fis.close();
//        return decodedSettings;
//    }

    public static List<User> deserializeXML() throws IOException {
        FileInputStream fis = new FileInputStream("users_data_info.xml");
        XMLDecoder decoder = new XMLDecoder(fis);

        List<User> userList = new ArrayList<>();
        try {
            Object obj;
            while ((obj = decoder.readObject()) != null) {
                if (obj instanceof User) {
                    userList.add((User) obj);
                }
        }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("all done");
        }

        decoder.close();
        fis.close();
        return userList;
    }
}
