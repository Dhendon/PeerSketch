package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidhendon on 11/19/14.
 */
public class SongLibrary {

    // List of songs
    private List<Song> mSongs = new ArrayList<Song>();

    // Add a song to list of songs
    public void addSong(Song song) {
        mSongs.add(song);
    }

    // Get song name at position
    public Song getSong(int position) {
        return mSongs.get(position);
    };

    // Get size of song list, mSongs
    public int getSize() { return mSongs.size(); };


}

