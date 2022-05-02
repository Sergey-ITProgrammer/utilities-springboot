package com.utilities.analysis.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AnalyzerServiceImpl implements AnalyzerService {

    @Override
    public List<Path> getBiggestFiles(List<Path> files, int amountOfFiles) {
        List<Path> listOfBiggestFiles = new ArrayList<>();

        if(files.isEmpty() || amountOfFiles > files.size()) return new ArrayList<>();

        List<File> ls = new ArrayList<>();
        for (Path item : files) {
            ls.add(new File(String.valueOf(item)));
        }

        ls.sort((a, b) -> (int) (a.length() - b.length()));

        for (int i = files.size() - amountOfFiles; i < files.size(); i++) {
            listOfBiggestFiles.add(ls.get(i).toPath());
        }

        return listOfBiggestFiles;
    }

    @Override
    public Set<Path> getDuplicates(List<Path> files) {
        Set<Path> listOfDuplicates = new HashSet<>();

        byte[][] digest = new byte[files.size()][];

        try {
            for (int i = 0; i < files.size(); i++) {
                MessageDigest md = null;
                md = MessageDigest.getInstance("md5");
                md.update(Files.readAllBytes(files.get(i)));
                digest[i] = md.digest();
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < digest.length; i++) {
            for (int j = 0; j < digest.length; j++) {
                if(i == j) continue;

                if (Arrays.equals(digest[i], digest[j])) {
                    listOfDuplicates.add(files.get(i));
                }
            }
        }
        return listOfDuplicates;
    }

    @Override
    public Map<Path, String> getUnknownFiles(List<Path> files) {
        Map<Path, String> listOfUnknownFiles = new HashMap<>();

        LinkedHashMap<String, String> listOfFileSignature = new LinkedHashMap<>();

        List<String> fileOfSignatures = getListOfSignatures();

        for (String s : fileOfSignatures) {
            StringBuilder signature = new StringBuilder();
            String extension = "";
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ';') {
                    extension = s.substring(i + 1).trim();
                    break;
                }
                signature.append(s.charAt(i));
            }

            listOfFileSignature.put(signature.toString(), extension);
        }


        for (int i = 0; i < files.size(); i++) {
            String fileExtension = getFileExtension(files.get(i));

            if (fileExtension.isEmpty()) {
                String extension = getUnknownFileExtension(files.get(i), listOfFileSignature, files.size());
                listOfUnknownFiles.put(files.get(i), extension);
            }
        }


        return listOfUnknownFiles;
    }

    private String getFileExtension(Path path) {
        StringBuilder fileName = new StringBuilder(new File(String.valueOf(path)).getName()).reverse();
        String fileExtension = "";

        for (int i = 0; i < fileName.length(); i++) {
            if (fileName.charAt(i) == '.') {
                fileExtension = fileName.substring(0, i);
                break;
            }
        }

        return fileExtension;
    }

    private String getUnknownFileExtension(Path filePath, LinkedHashMap<String, String> listOfFileSignature, int fileSize) {
        String signature = "";

        byte[] arrayOfBytesOfFile;
        try {
            arrayOfBytesOfFile = Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringBuilder bytesOfF = new StringBuilder();

        for (byte j : arrayOfBytesOfFile) {
            bytesOfF.append(Integer.toHexString(j & 0xff)).append(" ");
            if (bytesOfF.length() > 35) break;
        }

        String bytesOfFile = bytesOfF.toString().toUpperCase().trim();

        for (String s : listOfFileSignature.keySet()) {
            for (int i = 3; i <= bytesOfFile.length(); i += 2) {
                if (s.equals(bytesOfFile.substring(0, i).trim())) {
                    signature = listOfFileSignature.get(s);
                    break;
                }
            }
        }

        return signature;
    }

    private List<String> getListOfSignatures() {
        String list = "D4 C3 B2 A1; pcap\n" +
                "A1 B2 C3 D4; pcap\n" +
                "4D 3C B2 A1; pcap\n" +
                "A1 B2 3C 4D; pcap\n" +
                "0A 0D 0D 0A; pcapng\n" +
                "ED AB EE DB; rpm\n" +
                "53 51 4C 69 74 65 20 66; sqlitedb, sqlite, db\n" +
                "6F 72 6D 61 74 20 33 00; sqlitedb, sqlite, db\n" +
                "53 50 30 31; bin\n" +
                "00; PIC, PIF, SEA, YTR\n" +
                "00 00 00 00 00 00 00 00; PDB\n" +
                "BE BA FE CA; DBA\n" +
                "0 1 42 44; TDA\n" +
                "54 44 46 24; TDF\n" +
                "54 44 45 46; TDEF\n" +
                "0 0 1 0; ico\n" +
                "69 63 6e 73; icns\n" +
                "66 74 79 70 33 67; 3gp, 3g2\n" +
                "1F 9D; z, tar.z\n" +
                "1F A0; z, tar.z\n" +
                "42 41 43 4B 4D 49 4B 45; bac\n" +
                "44 49 53 4B; bac\n" +
                "49 4E 44 58; idx\n" +
                "42 5A 68; bz2\n" +
                "47 49 46 38 37 61; gif\n" +
                "47 49 46 38 39 61; gif\n" +
                "49 49 2A 0; tif, tiff\n" +
                "4D 4D 00 2A; tif, tiff\n" +
                "49 49 2A 0 10 0 0 0; cr2\n" +
                "43 52; cr2\n" +
                "80 2A 5F D7; cin\n" +
                "4E 55 52 55 49 4D 47; nui, nup\n" +
                "4E 55 52 55 50 41 4C; nui, nup\n" +
                "53 44 50 58; dpx\n" +
                "58 50 44 53; dpx\n" +
                "76 2F 31 1; exr\n" +
                "42 50 47 FB; bpg\n" +
                "FF D8 FF DB; jpg, jpeg\n" +
                "FF D8 FF E0 0 10 4A 46; jpg, jpeg\n" +
                "49 46 0 1; jpg, jpeg\n" +
                "FF D8 FF EE; jpg, jpeg\n" +
                "FF D8 FF E1 ?? ?? 45 78; jpg, jpeg\n" +
                "69 66 0 0; jpg, jpeg\n" +
                "0 0 0 C 6A 50 20 20 D A 87 A; jp2, j2k, jpf, jpm, jpg2, j2c, jpc, jpx, mj2\n" +
                "FF 4F FF 51; jp2, j2k, jpf, jpm, jpg2, j2c, jpc, jpx, mj2\n" +
                "46 4F 52 4D ?? ?? ?? ??; ilbm, lbm, ibm, iff\n" +
                "49 4C 42 4D; ilbm, lbm, ibm, iff\n" +
                "46 4F 52 4D ?? ?? ?? ??; 8svx, 8sv, svx, snd, iff\n" +
                "38 53 56 58; 8svx, 8sv, svx, snd, iff\n" +
                "46 4F 52 4D ?? ?? ?? ??; acbm, anbm, iff, anim, faxx, fax, ftxt, smus, smu, mus, cmus, yuvn, yuv, aiff, aif, aifc, snd, \n" +
                "41 43 42 4D; acbm, iff\n" +
                "41 4E 42 4D; anbm, iff\n" +
                "41 4E 49 4D; anim, iff\n" +
                "46 41 58 58; faxx, fax, iff\n" +
                "46 54 58 54; ftxt, iff\n" +
                "53 4D 55 53; smus, smu, mus, iff\n" +
                "43 4D 55 53; cmus, mus, iff\n" +
                "59 55 56 4E; yuvn, yuv, iff\n" +
                "46 41 4E 54; iff\n" +
                "41 49 46 46; aiff, aif, aifc, snd, iff\n" +
                "4C 5A 49 50; lz\n" +
                "4D 5A; exe, scr, sys, dll, fon, cpl, iec, ime, rs, tsp, mz\n" +
                "5A 4D; exe\n" +
                "50 4B 3 4; zip, aar, apk, docx, epub, ipa, jar, kmz, maff, msix, odp, ods, odt, pk3, pk4, pptx, usdz, vsdx, xlsx, xpi\n" +
                "50 4B 5 6; zip, aar, apk, docx, epub, ipa, jar, kmz, maff, msix, odp, ods, odt, pk3, pk4, pptx, usdz, vsdx, xlsx, xpi\n" +
                "50 4B 7 8; zip, aar, apk, docx, epub, ipa, jar, kmz, maff, msix, odp, ods, odt, pk3, pk4, pptx, usdz, vsdx, xlsx, xpi\n" +
                "52 61 72 21 1A 7 0; rar\n" +
                "52 61 72 21 1A 7 1 0; rar\n" +
                "89 50 4E 47 D A 1A A; png\n" +
                "C9; com\n" +
                "CA FE BA BE; class\n" +
                "EF BB BF; txt\n" +
                "FF FE; txt\n" +
                "FE FF; txt\n" +
                "FF FE 0 0; txt\n" +
                "0 0 FE FF; txt\n" +
                "E FE FF; txt\n" +
                "25 21 50 53; ps\n" +
                "49 54 53 46 3 0 0 0; chm\n" +
                "60 0 0 0; chm\n" +
                "25 50 44 46 2D; pdf\n" +
                "30 26 B2 75 8E 66 CF 11; asf, wma, wmv\n" +
                "A6 D9 0 AA 0 62 CE 6C; asf, wma, wmv\n" +
                "4F 67 67 53; ogg, oga, ogv\n" +
                "38 42 50 53; psd\n" +
                "52 49 46 46 ?? ?? ?? ??; wav\n" +
                "57 41 56 45; wav\n" +
                "52 49 46 46 ?? ?? ?? ??; avi\n" +
                "41 56 49 20; avi\n" +
                "FF FB; mp3\n" +
                "FF F3; mp3\n" +
                "FF F2; mp3\n" +
                "49 44 33; mp3\n" +
                "42 4D; bmp, dib\n" +
                "43 44 30 30 31; iso\n" +
                "53 49 4D 50 4C 45 20 20; fits\n" +
                "3D 20 20 20 20 20 20 20; fits\n" +
                "20 20 20 20 20 20 20 20; fits\n" +
                "20 20 20 20 20 54; fits\n" +
                "66 4C 61 43; flac\n" +
                "4D 54 68 64; mid, midi\n" +
                "D0 CF 11 E0 A1 B1 1A E1; doc, xls, ppt, msi, msg\n" +
                "64 65 78 0A 30 33 35 00; dex\n" +
                "4B 44 4D; vmdk\n" +
                "43 72 32 34; crx\n" +
                "41 47 44 33; fh8\n" +
                "5 7 0 0 42 4F 42 4F; cwk\n" +
                "5 7 0 0 0 0 0 0; cwk\n" +
                "0 0 0 0 0 1; cwk\n" +
                "6 7 E1 0 42 4F 42 4F; cwk\n" +
                "6 7 E1 0 0 0 0 0; cwk\n" +
                "0 0 0 0 0 1; cwk\n" +
                "45 52 2 0 0 0; toast\n" +
                "8B 45 52 2 0 0 0; toast\n" +
                "6B 6F 6C 79; dmg\n" +
                "78 61 72 21; xar\n" +
                "50 4D 4F 43 43 4D 4F 43; dat\n" +
                "4E 45 53 1A; nes\n" +
                "75 73 74 61 72 0 30 30; tar\n" +
                "75 73 74 61 72 20 20 00; tar\n" +
                "4F 41 52 ??; oar\n" +
                "74 6F 78 33; tox\n" +
                "4D 4C 56 49; MLV\n" +
                "37 7A BC AF 27 1C; 7z\n" +
                "1F 8B; gz, tar.gz\n" +
                "FD 37 7A 58 5A 0; xz, tar.xz\n" +
                "4 22 4D 18; lz4\n" +
                "4D 53 43 46; cab\n" +
                "46 4C 49 46; flif\n" +
                "1A 45 DF A3; mkv, mka, mks, mk3d, webm\n" +
                "4D 49 4C 20; stg\n" +
                "41 54 26 54 46 4F 52 4D; djvu, djv\n" +
                "?? ?? ?? ?? 44 4A 56; djvu, djv\n" +
                "30 82; der\n" +
                "44 49 43 4D; dcm\n" +
                "77 4F 46 46; woff\n" +
                "77 4F 46 32; woff2\n" +
                "3C 3F 78 6D 6C 20; xml\n" +
                "3C 0 3F 00 78 0 6D 0; xml\n" +
                "6C 0 20; xml\n" +
                "0 3C 0 3F 0 78 0 6D; xml\n" +
                "0 6C 0 20; xml\n" +
                "3C 0 0 0 3F 0 0 0; xml\n" +
                "78 0 0 0 6D 0 0 0; xml\n" +
                "6C 0 0 0 20 0 0 0; xml\n" +
                "0 0 0 3C 0 0 0 3F; xml\n" +
                "0 0 0 78 0 0 0 6D; xml\n" +
                "0 0 0 6C 0 0 0 20; xml\n" +
                "4C 6F A7 94 93 40; xml\n" +
                "0 61 73 6D; wasm\n" +
                "CF 84 1; lep\n" +
                "43 57 53; swf\n" +
                "46 57 53; swf\n" +
                "21 3C 61 72 63 68 3E A; deb\n" +
                "52 49 46 46 ?? ?? ?? ??; webp\n" +
                "57 45 42 50; webp\n" +
                "7B 5C 72 74 66 31; rtf\n" +
                "47; ts, tsv, tsa, mpg, mpeg\n" +
                "0 0 1 BA; m2p, vob, mpg, mpeg\n" +
                "0 0 1 B3; mpg, mpeg\n" +
                "66 74 79 70 69 73 6F 6D; mp4\n" +
                "78 1; zlib\n" +
                "78 9C; zlib\n" +
                "78 DA; zlib\n" +
                "78 20; zlib\n" +
                "78 7D; zlib\n" +
                "78 BB; zlib\n" +
                "78 F9; zlib\n" +
                "62 76 78 32; lzfse\n" +
                "4F 52 43; orc\n" +
                "4F 62 6A 1; avro\n" +
                "53 45 51 36; p25, obt\n" +
                "55 55 AA AA; pcv\n" +
                "78 56 34; pbt, pdt, pea, peb, pet, pgt, pict, pjt, pkt, pmt\n" +
                "45 4D 58 32; ez2\n" +
                "45 4D 55 33; ez3, iso\n" +
                "1B 4C 75 61; luac\n" +
                "62 6F 6F 6B 0 0 0 0; alias\n" +
                "6D 61 72 6B 0 0 0 0; alias\n" +
                "5B 5A 6F 6E 65 54 72 61; Identifier\n" +
                "6E 73 66 65 72 5D; Identifier\n" +
                "52 65 63 65 69 76 65 64; eml\n" +
                "3A; eml\n" +
                "20 2 1 62 A0 1E AB 7; tde\n" +
                "2 0 0 0; tde\n" +
                "37 48 3 2 0 0 0 0; kdb\n" +
                "58 35 30 39 4B 45 59; kdb\n" +
                "85 ?? ?? 3; pgp\n" +
                "28 B5 2F FD; zst\n" +
                "52 53 56 4B 44 41 54 41; rs\n" +
                "3A 29 A; sml\n" +
                "31 A 30 30; srt\n" +
                "34 12 AA 55; vpk\n" +
                "2A 2A 41 43 45 2A 2A; ace\n" +
                "60 EA; arj\n" +
                "49 53 63 28; cab\n" +
                "5A 4F 4F; zoo\n" +
                "50 31 A; pbm\n" +
                "50 32 A; pgm\t\n" +
                "50 33 A; ppm\n" +
                "D7 CD C6 9A; wmf\n" +
                "67 69 6D 70 20 78 63 66; xcf\n" +
                "2F 2A 20 58 50 4D 20 2A; xpm\n" +
                "2F; xpm\n" +
                "41 46 46; aff\n" +
                "45 56 46 32; Ex01\n" +
                "45 56 46; e01\n" +
                "51 46 49; qcow\n" +
                "52 49 46 46 ?? ?? ?? ??; ani\n" +
                "41 43 4F 4E; ani\n" +
                "52 49 46 46 ?? ?? ?? ??; cda\n" +
                "43 44 44 41; cda\n" +
                "52 49 46 46 ?? ?? ?? ??; qcq\n" +
                "51 4C 43 4D; qcq\n" +
                "52 49 46 58 ?? ?? ?? ??; dcr\n" +
                "46 47 44 4D; dcr\n" +
                "58 46 49 52 ?? ?? ?? ??; dcr\n" +
                "4D 44 47 46; dcr\n" +
                "52 49 46 58 ?? ?? ?? ??; dir, dxr, drx\n" +
                "4D 56 39 33; dir, dxr, drx\n" +
                "58 46 49 52 ?? ?? ?? ??; dir, dxr, drx\n" +
                "33 39 56 4D; dir, dxr, drx\n" +
                "46 4C 56; flv\n" +
                "3C 3C 3C 20 4F 72 61 63; vdi\n" +
                "6C 65 20 56 4D 20 56 69; vdi\n" +
                "72 74 75 61 6C 42 6F 78; vdi\n" +
                "20 44 69 73 6B 20 49 6D; vdi\n" +
                "61 67 65 20 3E 3E 3E; vdi\n" +
                "63 6F 6E 6E 65 63 74 69; vhd\n" +
                "78; vhd\n" +
                "76 68 64 78 66 69 6C 65; vhdx\n" +
                "49 73 5A 21; isz\n" +
                "44 41 41; evt\n" +
                "50 4D 43 43; grp\n" +
                "4B 43 4D 53; icm\n" +
                "72 65 67 66; dat\n" +
                "21 42 44 4E; pst\n" +
                "44 52 41 43 4F; drc\n" +
                "47 52 49 42; grib, grib2\n" +
                "42 4C 45 4E 44 45 52; blend\n" +
                "0 0 0 C 4A 58 4C 20 D A 87 A; jxl\n" +
                "FF A; jxl\n" +
                "0 1 0 0 0; ttf, tte, dfont\n" +
                "4D 53 57 49 4D 0 0 0; wim, swm, esd\n" +
                "D0 0 0 0 0; wim, swm, esd\n" +
                "21 2D 31 53 4C 4F 42 1F; slob";

        return Arrays.asList(list.split("\n"));
    }
}
