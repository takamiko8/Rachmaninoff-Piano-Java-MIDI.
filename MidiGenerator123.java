import javax.sound.midi.*;
import java.io.File;

public class MidiGenerator {

    public static void main(String[] args) {

        try {
            Sequence sequence = new Sequence(Sequence.PPQ, 480);
            Track track = sequence.createTrack();

            // 🎹 Piano
            ShortMessage program = new ShortMessage();
            program.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 0, 0);
            track.add(new MidiEvent(program, 0));

            long tick = 0;

            // ~3 minutos a 60 BPM aprox
            // 3 min = 180 seg ≈ 180 compases de 4/4 lentos
            for (int i = 0; i < 180; i++) {

                // 🎼 Progresión armónica romántica
                int[][] chords = {
                        {57, 60, 64}, // Am
                        {53, 57, 60}, // F
                        {55, 59, 62}, // G
                        {48, 52, 55}, // C
                        {50, 53, 57}, // Dm
                        {52, 55, 59}, // Em
                        {45, 48, 52}, // Am grave
                        {48, 52, 55}  // C
                };

                int[] chord = chords[i % chords.length];

                // 🎼 MANO IZQUIERDA (clave de fa → graves, acordes rotos)
                addArpeggio(track, tick, chord[0] - 12, chord);

                // 🎼 MANO DERECHA (clave de sol → melodía)
                addMelody(track, tick, chord);

                tick += 480 * 4; // un compás 4/4
            }

            // 💾 Guardar en escritorio
            String path = System.getProperty("user.home")
                    + File.separator + "Escritorio"
                    + File.separator + "piano_3_minutos.mid";

            File output = new File(path);

            MidiSystem.write(sequence, 1, output);

            System.out.println("MIDI creado en:");
            System.out.println(output.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🎼 Acorde roto (mano izquierda)
    private static void addArpeggio(Track track, long tick, int base, int[] chord) throws Exception {

        int[] pattern = {0, 1, 2, 1};

        for (int i = 0; i < pattern.length; i++) {

            int note = chord[pattern[i]] - 12;

            ShortMessage on = new ShortMessage();
            on.setMessage(ShortMessage.NOTE_ON, 0, note, 80);

            ShortMessage off = new ShortMessage();
            off.setMessage(ShortMessage.NOTE_OFF, 0, note, 0);

            track.add(new MidiEvent(on, tick + i * 120));
            track.add(new MidiEvent(off, tick + i * 120 + 100));
        }
    }

    // 🎼 Melodía (mano derecha)
    private static void addMelody(Track track, long tick, int[] chord) throws Exception {

        int root = chord[0] + 12;

        int[] melody = {
                root, root + 2, root + 4, root + 7,
                root + 9, root + 7, root + 4, root
        };

        for (int i = 0; i < melody.length; i++) {

            ShortMessage on = new ShortMessage();
            on.setMessage(ShortMessage.NOTE_ON, 0, melody[i], 95);

            ShortMessage off = new ShortMessage();
            off.setMessage(ShortMessage.NOTE_OFF, 0, melody[i], 0);

            track.add(new MidiEvent(on, tick + i * 120));
            track.add(new MidiEvent(off, tick + i * 120 + 110));
        }
    }
}
