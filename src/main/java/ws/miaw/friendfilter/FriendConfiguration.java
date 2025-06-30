package ws.miaw.friendfilter;

import java.io.*;
import java.util.*;

public class FriendConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final File SAVE_FILE = new File(FriendFilterMod.getMinecraftFolder().getAbsolutePath() + "\\miaw\\friendfilter");

    private boolean defaultShow; // whether join messages are shown by default
    private boolean consideringGuild;

    private Set<String> usernames;

    private FriendConfiguration() {
        this.defaultShow = true; // default behaviour is to show all friends unless otherwise specified
        this.consideringGuild = true;
        this.usernames = new HashSet<>();

        save();
    }

    public boolean addUsername(final String name) {
        boolean result = this.usernames.add(name.toLowerCase());
        if(result) save();
        return result;
    }

    public boolean removeUsername(final String name) {
        boolean result = this.usernames.remove(name.toLowerCase());
        if(result) save();
        return result;
    }

    public void clear() {
        usernames.clear();
        save();
    }

    public Set<String> getList() {
        return Collections.unmodifiableSet(usernames);
    }

    public boolean isDefaultShow() {
        return defaultShow;
    }

    public void setDefaultShow(final boolean value) {
        this.defaultShow = value;
        save();
    }

    public void setConsideringGuild(final boolean value) {
        this.consideringGuild = value;
        save();
    }

    public boolean isConsideringGuild() {
        return consideringGuild;
    }

    public boolean shouldShowMessage(final String name) {
        return usernames.contains(name.toLowerCase()) != defaultShow;
    }


    // Serialization

    protected void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE));
            oos.writeObject(this);
            oos.close(); // flushes
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected static FriendConfiguration create() {
        if (SAVE_FILE.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(SAVE_FILE));
                return (FriendConfiguration) ois.readObject();
            } catch (Exception e) {
                if(ois != null) {
                    try {
                        ois.close(); // make sure stream is closed before attempting to rename

                        // rename old file to save and create a new one; the old one is either of an old version or corrupted.
                        FriendFilterMod.getLogger().warn("Could not load friend configuration, attempting to create a new one.");

                        File oldFile = new File(SAVE_FILE.getParent(), SAVE_FILE.getName() + "-old");

                        if(oldFile.exists()) oldFile.delete();

                        if(SAVE_FILE.renameTo(oldFile)) {
                            return create();
                        }

                        FriendFilterMod.getLogger().error("Failed to create a new configuration.");
                        return null; // ?
                    } catch (IOException ioException) {
                        // this should never be called
                        ioException.printStackTrace();
                    }
                }

            }
        }

        return new FriendConfiguration();
    }



}
