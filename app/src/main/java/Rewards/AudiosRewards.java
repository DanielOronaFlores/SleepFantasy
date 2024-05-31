package Rewards;

import AppContext.MyApplication;
import Database.DataAccess.PlaylistDataAccess;
import Database.DataAccess.AudiosDataAccess;
import Database.DataUpdates.PlaylistAudiosDataUpdate;
import Database.DataUpdates.AudiosDataUpdate;
import Database.DatabaseConnection;

public class AudiosRewards {
    private static AudiosDataUpdate AudiosDataUpdate;
    private static PlaylistDataAccess playlistDataAccess;
    private static AudiosDataAccess AudiosDataAccess;
    private static PlaylistAudiosDataUpdate PlaylistAudiosDataUpdate;

    private static void initializeConnections() {
        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        AudiosDataUpdate = new AudiosDataUpdate(connection);
        playlistDataAccess = new PlaylistDataAccess(connection);
        AudiosDataAccess = new AudiosDataAccess(connection);
        PlaylistAudiosDataUpdate = new PlaylistAudiosDataUpdate(connection);
    }
    private static String getAudioName(int id) {
        return switch (id) {
            case 1 -> "Ambiental_Biblioteca";
            case 2 -> "Naturaleza_Bosque";
            case 3 -> "Ruido Blanco_Burbuja";
            case 4 -> "Ambiental_Carretera";
            case 5 -> "Ambiental_Ciudad";
            case 6 -> "Ruido Blanco_Estatica";
            case 7 -> "Ruido Blanco_Fuego";
            case 8 -> "Naturaleza_Jungla";
            case 9 -> "Ambiental_Minecraft";
            case 10 -> "Ambiental_Multitud";
            case 11 -> "Naturaleza_Noche";
            case 12 -> "Clasica_Nocturno";
            case 13 -> "Naturaleza_Oceano";
            case 14 -> "Clasica_Pastoral";
            case 15 -> "Naturaleza_Playa";
            case 16 -> "Clasica_Preludio";
            case 17 -> "Ruido Blanco_Secreto";
            case 18 -> "Clasica_Sonata";
            case 19 -> "Ruido Blanco_Ventilador";
            case 20 -> "Clasica_Vivaldi";
            default -> "";
        };
    }
    private static String[] getPlaylistAudioName(String audioName) {
        return audioName.split("_");
    }

    public static void giveRewardAudio(int id) {
        initializeConnections();

        String[] playlistAudioName = getPlaylistAudioName(getAudioName(id));

        int playlistId = playlistDataAccess.getPlaylistId(playlistAudioName[0]);
        AudiosDataUpdate.addAudio(playlistAudioName[1], true);

        int audioId = AudiosDataAccess.getAudioID(playlistAudioName[1]);
        PlaylistAudiosDataUpdate.addaudioToPlaylist(playlistId, audioId);

        System.out.println("Se ha dado el audio " + playlistAudioName[1] + " de la playlist " + playlistAudioName[0]);
    }
}
