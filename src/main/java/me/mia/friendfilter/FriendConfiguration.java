package me.mia.friendfilter;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.io.*;
import java.util.*;

public class FriendConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final File SAVE_FILE = new File(FriendFilter.getMinecraftFolder().getAbsolutePath() + "\\friend-filter");


    private boolean defaultShow; // whether join messages are shown by default

    private Set<String> usernames;

    private FriendConfiguration() {
        this.defaultShow = true; // default behaviour is to show all friends unless otherwise specified
        this.usernames = new HashSet<String>();

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

    // not currently in use as we save on any changes, but the system exists if ever needed.
    private static final long AUTOSAVE_INTERVAL_MILLIS = 1000 * 60 * 3; // 3 minutes
    private void startAutosave() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                super.run();
                FriendConfiguration.this.save();
            }
        });

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                save();
            }
        }, AUTOSAVE_INTERVAL_MILLIS, AUTOSAVE_INTERVAL_MILLIS);
    }

    protected static FriendConfiguration create() {
        if (SAVE_FILE.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE));
                return (FriendConfiguration) ois.readObject();
            } catch (Exception e) {
                // rename old file to save and create a new one; the old one is either of an old version or corrupted.
                System.out.println("Could not load friend-filter configuration, attempting to create a new one.");
                if(SAVE_FILE.renameTo(new File(FriendFilter.getMinecraftFolder().getAbsolutePath() + "\\friend-filter-old"))) {
                    return create();
                }
                System.out.println("Failed to create a new configuration. ");
                return null; // ?
            }
        }

        return new FriendConfiguration();
    }



}
