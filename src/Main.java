import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    public final static char NULL = '\0';
    public final static String RECORD_SEP = String.valueOf((char) 0x1e);
    public final static Charset WIN1251 = Charset.forName("windows-1251");
    public final static byte[] CRO_FILE = "CroFile".getBytes(WIN1251);
    public final static String PK_SENTINEL = new String(new byte[]{(byte) "Системный номер".getBytes(WIN1251).length}, WIN1251) + "Системный номер";
    public char[] KOD = new char[]{
            (char) 0x08, (char) (char) 0x63, (char) (char) 0x81, (char) (char) 0x38, (char) 0xa3, (char) 0x6b, (char) 0x82, (char) 0xa6, (char) 0x18, (char) 0x0d, (char) 0xac, (char) 0xd5, (char) 0xfe, (char) 0xbe, (char) 0x15, (char) 0xf6,
            (char) 0xa5, (char) 0x36, (char) 0x76, (char) 0xe2, (char) 0x2d, (char) 0x41, (char) 0xb5, (char) 0x12, (char) 0x4b, (char) 0xd8, (char) 0x3c, (char) 0x56, (char) 0x34, (char) 0x46, (char) 0x4f, (char) 0xa4,
            (char) 0xd0, (char) 0x01, (char) 0x8b, (char) 0x60, (char) 0x0f, (char) 0x70, (char) 0x57, (char) 0x3e, (char) 0x06, (char) 0x67, (char) 0x02, (char) 0x7a, (char) 0xf8, (char) 0x8c, (char) 0x80, (char) 0xe8,
            (char) 0xc3, (char) 0xfd, (char) 0x0a, (char) 0x3a, (char) 0xa7, (char) 0x73, (char) 0xb0, (char) 0x4d, (char) 0x99, (char) 0xa2, (char) 0xf1, (char) 0xfb, (char) 0x5a, (char) 0xc7, (char) 0xc2, (char) 0x17,
            (char) 0x96, (char) 0x71, (char) 0xba, (char) 0x2a, (char) 0xa9, (char) 0x9a, (char) 0xf3, (char) 0x87, (char) 0xea, (char) 0x8e, (char) 0x09, (char) 0x9e, (char) 0xb9, (char) 0x47, (char) 0xd4, (char) 0x97,
            (char) 0xe4, (char) 0xb3, (char) 0xbc, (char) 0x58, (char) 0x53, (char) 0x5f, (char) 0x2e, (char) 0x21, (char) 0xd1, (char) 0x1a, (char) 0xee, (char) 0x2c, (char) 0x64, (char) 0x95, (char) 0xf2, (char) 0xb8,
            (char) 0xc6, (char) 0x33, (char) 0x8d, (char) 0x2b, (char) 0x1f, (char) 0xf7, (char) 0x25, (char) 0xad, (char) 0xff, (char) 0x7f, (char) 0x39, (char) 0xa8, (char) 0xbf, (char) 0x6a, (char) 0x91, (char) 0x79,
            (char) 0xed, (char) 0x20, (char) 0x7b, (char) 0xa1, (char) 0xbb, (char) 0x45, (char) 0x69, (char) 0xcd, (char) 0xdc, (char) 0xe7, (char) 0x31, (char) 0xaa, (char) 0xf0, (char) 0x65, (char) 0xd7, (char) 0xa0,
            (char) 0x32, (char) 0x93, (char) 0xb1, (char) 0x24, (char) 0xd6, (char) 0x5b, (char) 0x9f, (char) 0x27, (char) 0x42, (char) 0x85, (char) 0x07, (char) 0x44, (char) 0x3f, (char) 0xb4, (char) 0x11, (char) 0x68,
            (char) 0x5e, (char) 0x49, (char) 0x29, (char) 0x13, (char) 0x94, (char) 0xe6, (char) 0x1b, (char) 0xe1, (char) 0x7d, (char) 0xc8, (char) 0x2f, (char) 0xfa, (char) 0x78, (char) 0x1d, (char) 0xe3, (char) 0xde,
            (char) 0x50, (char) 0x4e, (char) 0x89, (char) 0xb6, (char) 0x30, (char) 0x48, (char) 0x0c, (char) 0x10, (char) 0x05, (char) 0x43, (char) 0xce, (char) 0xd3, (char) 0x61, (char) 0x51, (char) 0x83, (char) 0xda,
            (char) 0x77, (char) 0x6f, (char) 0x92, (char) 0x9d, (char) 0x74, (char) 0x7c, (char) 0x04, (char) 0x88, (char) 0x86, (char) 0x55, (char) 0xca, (char) 0xf4, (char) 0xc1, (char) 0x62, (char) 0x0e, (char) 0x28,
            (char) 0xb7, (char) 0x0b, (char) 0xc0, (char) 0xf5, (char) 0xcf, (char) 0x35, (char) 0xc5, (char) 0x4c, (char) 0x16, (char) 0xe0, (char) 0x98, (char) 0x00, (char) 0x9b, (char) 0xd9, (char) 0xae, (char) 0x03,
            (char) 0xaf, (char) 0xec, (char) 0xc9, (char) 0xdb, (char) 0x6d, (char) 0x3b, (char) 0x26, (char) 0x75, (char) 0x3d, (char) 0xbd, (char) 0xb2, (char) 0x4a, (char) 0x5d, (char) 0x6c, (char) 0x72, (char) 0x40,
            (char) 0x7e, (char) 0xab, (char) 0x59, (char) 0x52, (char) 0x54, (char) 0x9c, (char) 0xd2, (char) 0xe9, (char) 0xef, (char) 0xdd, (char) 0x37, (char) 0x1e, (char) 0x8f, (char) 0xcb, (char) 0x8a, (char) 0x90,
            (char) 0xfc, (char) 0x84, (char) 0xe5, (char) 0xf9, (char) 0x14, (char) 0x19, (char) 0xdf, (char) 0x6e, (char) 0x23, (char) 0xc4, (char) 0x66, (char) 0xeb, (char) 0xcc, (char) 0x22, (char) 0x1c, (char) 0x5c};

    public static void main(String[] args) {
        var main = new Main();
        var structPath = Paths.get(args[1]);
        var dataTadPath = Paths.get(args[2]);
        var dataDatPath = Paths.get(args[3]);
        var tableStructure = main.parse_structure(structPath);
        for (var table : tableStructure.getValue()) {
            var columns = table.columns;
            columns.forEach(it -> System.out.print(it + " "));
            System.out.println();
            try {
                for (var row : parse_data(dataTadPath, dataDatPath, table.id, columns)) {
                    System.out.println(row);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static ArrayList<String> parse_data(Path dataTadPath, Path dataDatPath, int id, ArrayList<Column> columns) throws IOException {
        try (var inputDat = Files.newInputStream(dataDatPath); var inputTad = Files.newInputStream(dataTadPath)) {
            if (!Arrays.equals(inputDat.readNBytes(7), CRO_FILE)) {
                throw new IllegalArgumentException("Not a CroBank.dat file.");
            }
            inputTad.skipNBytes(8);
            var recordsTBR = new ArrayList<String>();
            var continueLoop = new boolean[]{true};
            IntStream.iterate(0, (it) -> continueLoop[0], it -> ++it).forEach(i -> {
                try {
                    var meta = inputTad.readNBytes(12);
                    var record = parse_record(meta, inputDat);
                    if (record == null || record.size() < 2) {
                        return;
                    }
                    if (id != (int) record.get(0)) {
                        return;
                    }
                    record.remove(0);
                    var data = new byte[record.size()];
                    for (var j = 0; j < record.size(); ++j) {
                        data[j] = record.get(j);
                    }
                    var records = new String(data, WIN1251).split(RECORD_SEP);
                    recordsTBR.addAll(Arrays
                            .stream(records)
                            .map(Main::decode_text)
                            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll)
                    );
                    if (recordsTBR.size() != columns.size()) {
                        recordsTBR.add(0, String.valueOf(i));
                    }
                } catch (IOException e) {
                    continueLoop[0] = false;
                }
            });
            return recordsTBR;
        }
    }

    private static ArrayList<Byte> parse_record(byte[] meta, InputStream dataDat) throws IOException {
        var dataBuffer = ByteBuffer.wrap(meta);
        var offset = dataBuffer.getInt();
        var length = dataBuffer.getShort();
        var next_offset = dataBuffer.getInt();
        var next_length = dataBuffer.getShort();
        dataDat.skipNBytes(offset);
        if (length == 0) {
            if (next_length == 0) {
                return null;
            }
        }
        var data = dataDat.readNBytes(length);
        var byteArrayList = new ArrayList<Byte>(data.length);
        for (var myByte : data) {
            byteArrayList.add(myByte);
        }
        while (next_length != 0) {
            dataDat.skipNBytes(next_offset);
            var next_data = dataDat.readNBytes(Math.min(252, next_length));
            if (next_data.length < 4) {
                break;
            }
            next_offset = ByteBuffer.wrap(next_data).getInt();
            for (var i = 4; i < next_data.length; ++i) {
                byteArrayList.add(next_data[i]);
            }
            if (next_length >= 252) {
                next_length -= 252;
            } else {
                next_length = 0;
            }
        }
        return byteArrayList;
    }

    private static String decode_text(String substring) {
        return substring.codePoints()
                .map(it -> Character.getName(it).charAt(0) == 'C' ? ' ' : (char) it)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static int vword(String substring) {
        var byteBuffer = ByteBuffer.wrap(substring.getBytes(WIN1251));
        var word = byteBuffer.getInt();
        return word & 0x00ffffff;
    }

    private AbstractMap.SimpleEntry<Map<String, String>, ArrayList<Table>> parse_structure(Path structPath) {
        try (var input = Files.newInputStream(structPath)) {
            var data = input.readAllBytes();
            if (!Arrays.equals(data, 0, CRO_FILE.length, CRO_FILE, 0, CRO_FILE.length)) {
                throw new IllegalArgumentException("File " + structPath + " is not a CronosPro data file!");
            }
            var sections = align_sections(data);
            var meta = parse_metadata(sections.get(0));
            var tables = new ArrayList<Table>();
            for (var table_section : sections) {
                for (var i = 0; i < 256; i++) {
                    tables.addAll(parse_table_section(table_section, i));
                }
            }
            return new AbstractMap.SimpleEntry<>(meta, tables);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("File " + structPath + " is not a CronosPro data file!");
        }
    }

    private ArrayList<Table> parse_table_section(Section section, int table_id) {
        var text = section.text;
        var sig = String.valueOf((char) (table_id)) + NULL + NULL + NULL;
        var offset = 0;
        var tables = new ArrayList<Table>();
        while (true) {
            var index = text.indexOf(sig, offset);
            if (index == -1) {
                break;
            }
            offset = index + 1;
            var next_byte = (byte) (index + sig.length());
            var table = parse_table(text, next_byte);
            if (table != null) {
                table.id = table_id;
                tables.add(table);
            }
        }
        return tables;
    }

    private Table parse_table(String text, byte next_byte) {
        var next_len = text.charAt(next_byte);
        next_byte++;
        if (text.length() < next_byte + next_len + 10)
            return null;
        if (text.charAt(next_byte + next_len) != 2)
            return null;
        var table_name = decode_text(text.substring(next_byte, next_byte + next_len));
        next_byte = (byte) (next_byte + next_len + 1);
        var table_abbr = decode_text(text.substring(next_byte, next_byte + 2));
        next_byte += 2;
        if (text.charAt(next_byte) != 1)
            return null;
        next_byte += 4;
        var col_count = ByteBuffer.wrap(text.getBytes(WIN1251), 0, next_byte).getInt();
        return new Table(table_name, table_abbr, parse_columns(text, next_byte + 4, col_count), col_count);
    }

    private ArrayList<Column> parse_columns(String text, int base, int count) {
        var columns = new ArrayList<Column>();
        for (var i = 0; i < count; ++i) {
            if (text.substring(base).length() < 8) {
                break;
            }
            var col_len = ByteBuffer.wrap(text.getBytes(WIN1251), 0, base).getShort();
            base += 2;
            if (text.substring(base).length() < col_len) {
                break;
            }
            var col_data = text.substring(base - 1, base - 1 + col_len);
            var bytes = ByteBuffer.wrap(col_data.getBytes(WIN1251));
            bytes.order(ByteOrder.BIG_ENDIAN);
            var type = bytes.getShort();
            var col_id = bytes.getShort();
            var text_len = bytes.getInt();
            var col_name = decode_text(col_data.substring(8, 8 + text_len));
            columns.add(new Column(col_id, col_name, type));
            base = base + col_len;
        }
        return columns;

    }

    private Map<String, String> parse_metadata(Section section) {
        var text = section.text;
        return Stream.of("BankId", "BankName")
                .collect(TreeMap::new, (map, it) -> {
                    var itNew = new String(new byte[]{(byte) it.getBytes(WIN1251).length}, WIN1251) + it;
                    var offset = text.indexOf(itNew) + itNew.length();
                    var length = Main.vword(text.substring(offset));
                    offset += 4;
                    map.put(it, Main.decode_text(text.substring(offset, offset + length)));
                }, TreeMap::putAll);
    }

    private ArrayList<Section> align_sections(byte[] data) {
        var sections = new ArrayList<Section>();
        for (var offset = 0; offset < 256; ++offset) {
            StringBuilder buf = new StringBuilder();
            for (var j = 0; j < data.length; ++j) {
                buf.append((char) (KOD[data[j]] - (byte) j - (byte) offset));
            }
            if (buf.indexOf(PK_SENTINEL) != -1) {
                sections.add(new Section(buf.toString(), offset, buf.indexOf(PK_SENTINEL)));
            }
        }
        sections.sort(Comparator.naturalOrder());
        return sections;
    }


}

class Section implements Comparable<Section> {
    public String text;
    public int offset;
    public int index;

    public Section(String t, int o, int i) {
        text = t;
        offset = o;
        index = i;
    }

    @Override
    public int compareTo(Section o) {
        return Integer.compare(index, o.index);
    }
}

class Table {
    public String name;
    public String abbr;
    public ArrayList<Column> columns;
    public int column_count;
    public int id;

    public Table(String name, String abbr, ArrayList<Column> columns, int column_count) {
        this.name = name;
        this.abbr = abbr;
        this.columns = columns;
        this.column_count = column_count;
    }
}

class Column {
    public int id;
    public String name;
    public short type;

    public Column(int id, String name, short type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
