package Rewards;

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

    private static void initializeConnections(DatabaseConnection connection) {
        AudiosDataUpdate = new AudiosDataUpdate(connection);
        playlistDataAccess = new PlaylistDataAccess(connection);
        AudiosDataAccess = new AudiosDataAccess(connection);
        PlaylistAudiosDataUpdate = new PlaylistAudiosDataUpdate(connection);
    }
    private static String getAudioName(int id) {
        switch (id) {
            case 1:
                return "Ambiental_Biblioteca";
            case 2:
                return "Naturaleza_Bosque";
            case 3:
                return "Ruido Blanco_Burbuja";
            case 4:
                return "Ambiental_Carretera";
            case 5:
                return "Ambiental_Ciudad";
            case 6:
                return "Ruido Blanco_Estatica";
            case 7:
                return "Ruido Blanco_Fuego";
            case 8:
                return "Naturaleza_Jungla";
            case 9:
                return "Ambiental_Minecraft";
            case 10:
                return "Ambiental_Multitud";
            case 11:
                return "Naturaleza_Noche";
            case 12:
                return "Clasica_Nocturno";
            case 13:
                return "Naturaleza_Oceano";
            case 14:
                return "Clasica_Pastoral";
            case 15:
                return "Naturaleza_Playa";
            case 16:
                return "Clasica_Preludio";
            case 17:
                return "Ruido Blanco_Secreto";
            case 18:
                return "Clasica_Sonata";
            case 19:
                return "Ruido Blanco_Ventilador";
            case 20:
                return "Clasica_Vivaldi";
            default:
                return "";
        }
    }
    private static String[] getPlaylistAudioName(String audioName) {
        return audioName.split("_");
    }

    public static void giveRewardAudio(int id, DatabaseConnection connection) {
        initializeConnections(connection);

        String[] playlistAudioName = getPlaylistAudioName(getAudioName(id));

        int playlistId = playlistDataAccess.getPlaylistId(playlistAudioName[0]);
        AudiosDataUpdate.addAudio(playlistAudioName[1], true);

        int audioId = AudiosDataAccess.getAudioID(playlistAudioName[1]);
        PlaylistAudiosDataUpdate.addaudioToPlaylist(playlistId, audioId);

        System.out.println("Se ha dado el audio " + playlistAudioName[1] + " de la playlist " + playlistAudioName[0]);
    }
}
