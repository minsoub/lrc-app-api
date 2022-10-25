package com.bithumbsystems.lrc.management.api.core.util;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.file.exception.FileException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Map.entry;

public class FileUtil {

  public static final String[] ALLOW_FILE_EXT_DEFAULT = {
          "DOC",
          "DOCX",
          "XLSX",
          "XLS",
          "PPT",
          "PPTX",
          "AI",
          "MOV",
          "MP4",
          "AVI",
          "MKV",
          "JPG",
          "JPEG",
          "PNG",
          "GIF",
          "PDF",
          "TXT",
          "CSV",
          "APPLICATION/VND.OPENXMLFORMATS-OFFICEDOCUMENT.SPREADSHEETML.SHEET",
          "APPLICATION/VND.OPENXMLFORMATS-OFFICEDOCUMENT.WORDPROCESSINGML.DOCUMENT",
          "APPLICATION/VND.MS-EXCEL"
  };
  public static final String[] ALLOW_MIME_TYPE_DEFAULT = {
          "IMAGE/JPEG", "APPLICATION/VND.OPENXMLFORMATS-OFFICEDOCUMENT.SPREADSHEETML.SHEET",
          "APPLICATION/VND.OPENXMLFORMATS-OFFICEDOCUMENT.WORDPROCESSINGML.DOCUMENT",
          "APPLICATION/MSWORD", "APPLICATION/POSTSCRIPT", "IMAGE/GIF", "TEXT/PLAIN", "VIDEO/QUICKTIME", "VIDEO/X-MSVIDEO",
          "IMAGE/PNG",
          "APPLICATION/PDF", "AI", "MOV", "MP4", "AVI", "MKV", "TXT", "DOC", "DOCX", "XLS",
          "XLSX", "PPT", "PPTX", "PDF", "JPG", "JPEG", "PNG", "GIF", "CSV"
  };

  // content-type과 허용가능한 파일 확장자 맵핑
  // 빠진 항목 : MP4, MKV, DOCX, XLSX, PPTX
  public static final Map<String, List<String>> allowContentType = Map.ofEntries(
        entry("application/msword", new ArrayList<String>(Arrays.asList("DOC"))),
        entry("application/vnd.openxmlformats-officedocument.wordprocessingml.document", new ArrayList<String>(Arrays.asList("DOCX"))),
        entry("application/pdf", new ArrayList<String>(Arrays.asList("PDF"))),
        entry("application/postscript", new ArrayList<String>(Arrays.asList("AI", "PS"))),
        entry("application/vnd.ms-excel", new ArrayList<String>(Arrays.asList("XLS"))),
        entry("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new ArrayList<String>(Arrays.asList("XLSX"))),
        entry("application/vnd.ms-powerpoint", new ArrayList<String>(Arrays.asList("PPT"))),
        entry("application/vnd.openxmlformats-officedocument.presentationml.presentation", new ArrayList<String>(Arrays.asList("PPTX"))),
        entry("image/gif", new ArrayList<String>(Arrays.asList("GIF"))),
        entry("image/jpeg", new ArrayList<String>(Arrays.asList("JPEG", "JPG"))),
        entry("image/png", new ArrayList<String>(Arrays.asList("PNG"))),
        entry("text/plain", new ArrayList<String>(Arrays.asList("TXT"))),
        entry("text/csv", new ArrayList<String>(Arrays.asList("CSV"))),
        entry("video/quicktime", new ArrayList<String>(Arrays.asList("MOV"))),
        entry("video/x-msvideo", new ArrayList<String>(Arrays.asList("AVI"))),
        entry("video/mp4", new ArrayList<String>(Arrays.asList("MP4"))),
        entry("video/x-matroska", new ArrayList<String>(Arrays.asList("MKV")))
        //entry("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new ArrayList<String>(Arrays.asList("avi")))
  );


  public static final long ALLOW_FILE_MAX_SIZE_DEFAULT = 100;

  public static String readResourceFile(String path) throws IOException {
    ClassPathResource resources = new ClassPathResource(path);
    try (InputStream inputStream = resources.getInputStream()) {
      String string = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      System.out.println(string);
      return string;
    }
  }


  /**
   * 첨부 파일 사이즈 체크
   * @param maxSize
   * @param fileSize
   * @return
   * @throws FileException
   */
  public static String getFileSize(long maxSize, long fileSize) throws FileException {
    double fileSizeInKB = (double)fileSize/1024;
    fileSizeInKB = Math.round(fileSizeInKB * 10) / 10.0;

    double fileSizeInMB = fileSizeInKB/1024;
    fileSizeInMB = Math.round(fileSizeInMB * 10) / 10.0;

    //100 메가 이상 업로드 금지.
    if(fileSizeInMB > maxSize){
      throw new FileException(ErrorCode.INVALID_MAX_FILE_SIZE);
    }
    String fileSizeStr = (fileSizeInMB > 1)? fileSizeInMB + "MB" : fileSizeInKB + "KB";
    return fileSizeStr;
  }

  /**
   * 첨부파일 확장자 체크
   * @param fileName
   * @return
   * @throws FileException
   */
  public static String getFileExt(String[] allowExt, String fileName) throws FileException {
    String ext = "";
    try {
      ext = StringUtils.getFilenameExtension(fileName).toUpperCase();
    }catch(Exception e){
      throw new FileException(ErrorCode.INVALID_FILE_EXT);
    }
    if(Arrays.asList(allowExt).contains(ext) == false){
      throw new FileException(ErrorCode.INVALID_FILE_EXT);
    }
    return ext;
  }

}
